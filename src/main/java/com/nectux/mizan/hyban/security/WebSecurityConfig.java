package com.nectux.mizan.hyban.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(JwtAuthConverterProperties.class)
public class WebSecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    private static final String ADMIN = "ADMIN";
    private static final String RH = "RH";
    private static final String DAF = "DAF";
    private static final String PTGE = "PTGE";

    private static final String[] PAIE_REFERENTIELS = {
            "/api/parametrages/rubriques/**",
            "/api/parametrages/cpte-virement/**",
            "/api/parametrages/banques/**"
    };

    private static final String[] PAIE_CONFIDENTIEL = {
            "/api/paie/bulletin/**",
            "/api/depart/**",
            "/histpaie/**"
    };

    private static final String[] PAIE_TRAITEMENTS = {
            "/api/paie/**",
            "/api/heures-supplementaires/**"
    };

    private static final String[] RH_REFERENTIELS = {
            "/api/parametrages/types-contrats/**",
            "/api/parametrages/types-services/**",
            "/api/parametrages/doc/**",
            "/api/personnel/document-types/**",
            "/api/storage-locations/**",
            "/api/categories/**",
            "/api/nationalites/**"
    };

    private static final String[] RH_DOSSIERS = {
            "/api/personnel/**",
            "/api/personnels/**",
            "/api/rh/**",
            "/api/conge/**",
            "/api/absence/**",
            "/formation/**",
            "/carriere/**",
            "/parametrages/**"
    };

    private static final String[] RH_READ_POST = {
            "/api/personnels/personnel/list",
            "/api/personnels/listcontratpersonneljson",
            "/api/personnels/listcontratpersonnelActifjson",
            "/api/personnels/listcontratparpersonneljson",
            "/api/personnels/listcontratpersonnelDepartjson",
            "/api/personnels/listcontratpersonnelfilterjson",
            "/api/personnels/fonctions/listfonctionjson",
            "/api/categories/listcategoriejson",
            "/api/rh/carriere/site/lister",
            "/api/personnels/personnel/export"
    };

    private static final String[] POINTAGE = {
            "/api/personnel/pointages/**"
    };

    private static final String[] ADMINISTRATION = {
            "/api/users/**",
            "/api/parametrages/user/**",
            "/api/parametrages/societe/**",
            "/api/parametrages/societes/**",
            "/api/permissions/**",
            "/api/audit/**"
    };

    private static final String[] EXERCICES = {
            "/api/parametrages/exercices/**",
            "/api/parametrages/mois/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // --- Acces public ---
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/auth/login", "/auth/api/debug/roles").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // --- Administration : utilisateurs, societe (ADMIN) ---
                        // Allow any authenticated user to fetch their own role permissions
                        .requestMatchers(HttpMethod.GET, "/api/permissions/role/**").authenticated()
                        .requestMatchers(ADMINISTRATION).hasRole(ADMIN)

                        // --- Exercices / mois : lecture partagee, ecriture ADMIN ---
                        .requestMatchers(HttpMethod.GET, EXERCICES).hasAnyRole(ADMIN, RH, DAF)
                        .requestMatchers(EXERCICES).hasRole(ADMIN)

                        // --- Pointage (PTGE, RH) ---
                        .requestMatchers(POINTAGE).hasAnyRole(ADMIN, RH, PTGE)

                        // --- Paie confidentielle : bulletins, solde de tout compte (DAF) ---
                        .requestMatchers(PAIE_CONFIDENTIEL).hasAnyRole(ADMIN, DAF)

                        // --- Referentiels de paie : lecture RH, ecriture DAF ---
                        .requestMatchers(HttpMethod.GET, PAIE_REFERENTIELS).hasAnyRole(ADMIN, RH, DAF)
                        .requestMatchers(PAIE_REFERENTIELS).hasAnyRole(ADMIN, DAF)

                        // --- Traitements de paie : elements variables, primes, prets (DAF) ---
                        .requestMatchers(HttpMethod.GET, PAIE_TRAITEMENTS).hasAnyRole(ADMIN, RH, DAF)
                        .requestMatchers(PAIE_TRAITEMENTS).hasAnyRole(ADMIN, DAF)

                        // --- Referentiels RH : lecture partagee, ecriture RH ---
                        .requestMatchers(HttpMethod.GET, RH_REFERENTIELS).hasAnyRole(ADMIN, RH, DAF, PTGE)
                        .requestMatchers(RH_REFERENTIELS).hasAnyRole(ADMIN, RH)

                        // --- Dossiers RH : endpoints POST de lecture (listes/pagination) accessibles a DAF ---
                        .requestMatchers(HttpMethod.POST, RH_READ_POST).hasAnyRole(ADMIN, RH, DAF)

                        // --- Dossiers RH : contrats, carriere, absences, conges, formation ---
                        .requestMatchers(HttpMethod.GET, RH_DOSSIERS).hasAnyRole(ADMIN, RH, DAF)
                        .requestMatchers(RH_DOSSIERS).hasAnyRole(ADMIN, RH)

                        // --- Autres parametrages : lecture partagee, ecriture ADMIN ---
                        .requestMatchers(HttpMethod.GET, "/api/parametrages/**").hasAnyRole(ADMIN, RH, DAF, PTGE)
                        .requestMatchers("/api/parametrages/**").hasRole(ADMIN)

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:7153",
            "http://127.0.0.1:7153",
            "http://192.168.1.7:7153",
            "http://83.171.249.150:7153"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}