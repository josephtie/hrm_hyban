package com.nectux.mizan.hyban.parametrages.web;

import com.nectux.mizan.hyban.parametrages.entity.Permission;
import com.nectux.mizan.hyban.parametrages.entity.Role;
import com.nectux.mizan.hyban.parametrages.entity.RoleName;
import com.nectux.mizan.hyban.parametrages.repository.PermissionRepository;
import com.nectux.mizan.hyban.parametrages.repository.RoleRepository;
import com.nectux.mizan.hyban.parametrages.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
public class PermissionRestController {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_READ') or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        List<Map<String, Object>> result = permissions.stream()
                .map(p -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", p.getId());
                    map.put("code", p.getCode());
                    map.put("description", p.getDescription());
                    map.put("active", p.isActive());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('PERMISSION_READ') or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getRolesWithPermissions() {
        List<Role> roles = roleRepository.findAll();
        List<Map<String, Object>> result = roles.stream()
                .map(r -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", r.getId());
                    map.put("name", r.getName().name());
                    map.put("permissions", r.getPermissions().stream()
                            .map(Permission::getCode)
                            .collect(Collectors.toList()));
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<Map<String, Object>> getPermissionsByRole(@PathVariable String roleName) {
        try {
            RoleName rn = RoleName.valueOf(roleName.toUpperCase());
            Optional<Role> roleOpt = roleRepository.findByName(rn);
            if (roleOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Role role = roleOpt.get();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("role", role.getName().name());
            result.put("permissions", role.getPermissions().stream()
                    .filter(Permission::isActive)
                    .map(Permission::getCode)
                    .collect(Collectors.toList()));
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/role/{roleName}")
    @PreAuthorize("hasAuthority('ROLE_ASSIGN') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateRolePermissions(
            @PathVariable String roleName,
            @RequestBody UpdatePermissionsRequest request) {
        try {
            RoleName rn = RoleName.valueOf(roleName.toUpperCase());
            Optional<Role> roleOpt = roleRepository.findByName(rn);
            if (roleOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Role role = roleOpt.get();

            Set<Permission> perms = new HashSet<>();
            for (String code : request.getPermissions()) {
                permissionRepository.findByCode(code).ifPresent(perms::add);
            }
            role.setPermissions(perms);
            roleRepository.save(role);

            String currentUser = getCurrentUsername();
            auditLogService.log("UPDATE_ROLE_PERMISSIONS", "ROLE", role.getName().name(), currentUser,
                    "Permissions updated for role " + role.getName().name() + ": " +
                            perms.stream().map(Permission::getCode).collect(Collectors.joining(", ")));

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("role", role.getName().name());
            result.put("permissions", perms.stream().map(Permission::getCode).collect(Collectors.toList()));
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class UpdatePermissionsRequest {
        private List<String> permissions;

        public List<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}
