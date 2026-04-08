package com.nectux.mizan.hyban.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://127.0.0.1:3000", "http://127.0.0.1:5173"}, allowedHeaders = "*", allowCredentials = "true")
public class AuthController {

    private final RestTemplateBuilder restTemplateBuilder;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        String keycloakHost = System.getenv().getOrDefault("KEYCLOAK_ADMIN_HOST", "http://192.168.1.2:8080");
        String tokenUrl = keycloakHost + "/realms/hyban/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", "hrm_frontend");
        form.add("client_secret", "b6cFLwyL2MakdzHxomjsamxesop9IbIE");
        form.add("username", username);
        form.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        RestTemplate restTemplate = restTemplateBuilder.build();
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, entity, Map.class);
            return ResponseEntity.ok(response.getBody()); // contient access_token, refresh_token, etc.
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
    @GetMapping("/api/debug/roles")
    public ResponseEntity<?> roles(Authentication auth) {
        return ResponseEntity.ok(auth.getAuthorities());
    }
}
