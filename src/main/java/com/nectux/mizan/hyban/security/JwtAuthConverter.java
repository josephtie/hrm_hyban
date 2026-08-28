package com.nectux.mizan.hyban.security;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.nectux.mizan.hyban.parametrages.entity.Permission;
import com.nectux.mizan.hyban.parametrages.entity.RoleName;
import com.nectux.mizan.hyban.parametrages.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Set<String> SUPPORTED_ROLES =
            Arrays.stream(RoleName.values()).map(Enum::name).collect(Collectors.toUnmodifiableSet());

    private static final Map<String, String> ROLE_ALIASES = Map.of(
            "PAIE", RoleName.DAF.name(),
            "POINTAGE", RoleName.PTGE.name()
    );

    private static String normalizeRole(String role) {
        String upperCase = role == null ? "" : role.trim().toUpperCase(Locale.ROOT);
        if (upperCase.startsWith("ROLE_")) {
            upperCase = upperCase.substring("ROLE_".length());
        }
        return ROLE_ALIASES.getOrDefault(upperCase, upperCase);
    }

    private final RoleRepository roleRepository;

    public JwtAuthConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (realmAccess != null && realmAccess.containsKey("roles")) {
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");

            // Normaliser et filtrer les rôles supportés
            List<String> normalizedRoles = roles.stream()
                    .map(JwtAuthConverter::normalizeRole)
                    .filter(SUPPORTED_ROLES::contains)
                    .distinct()
                    .collect(Collectors.toList());

            // Ajouter les rôles comme ROLE_xxx
            for (String roleName : normalizedRoles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
            }

            // Charger et ajouter les permissions depuis la DB pour chaque rôle
            for (String roleName : normalizedRoles) {
                try {
                    RoleName rn = RoleName.valueOf(roleName);
                    roleRepository.findByName(rn).ifPresent(role -> {
                        Set<Permission> perms = role.getPermissions();
                        if (perms != null) {
                            for (Permission perm : perms) {
                                if (perm.isActive()) {
                                    authorities.add(new SimpleGrantedAuthority(perm.getCode()));
                                }
                            }
                        }
                    });
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}