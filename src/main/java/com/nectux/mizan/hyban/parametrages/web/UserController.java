package com.nectux.mizan.hyban.parametrages.web;


import com.nectux.mizan.hyban.parametrages.dto.CreateUserRequest;
import com.nectux.mizan.hyban.parametrages.dto.ResetPasswordRequest;
import com.nectux.mizan.hyban.parametrages.dto.UserWithRolesDto;
import com.nectux.mizan.hyban.parametrages.service.AuditLogService;
import com.nectux.mizan.hyban.parametrages.service.KeycloakUserService;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('USER_READ', 'USER_CREATE', 'USER_UPDATE', 'USER_DELETE', 'ROLE_ASSIGN') or hasRole('ADMIN')")
public class UserController {

    private final KeycloakUserService keycloakUserService;

    @Autowired
    private AuditLogService auditLogService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest request) {
        List<String> roles = request.getRoles() == null ? new ArrayList<>() : request.getRoles();
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getFirstName() == null || request.getFirstName().isBlank()
                || request.getLastName() == null || request.getLastName().isBlank()
                || roles.isEmpty()) {
            return ResponseEntity.badRequest().body("Les champs username, firstName, lastName et roles sont obligatoires.");
        }
        String userId = keycloakUserService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                roles
        );
        return ResponseEntity.ok(userId);
     //   return ResponseEntity.status(501).body("Service désactivé en profil local");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserWithRolesDto> getUserById(@PathVariable String userId) {
        UserWithRolesDto user = keycloakUserService.getUserById(userId);
        return ResponseEntity.ok(user);
       // return ResponseEntity.status(501).body("Service désactivé en profil local");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserWithRolesDto> updateUser(@PathVariable String userId, @RequestBody CreateUserRequest request) {
      keycloakUserService.updateUser(userId, request);
        UserWithRolesDto updatedUser = keycloakUserService.getUserById(userId);
        return ResponseEntity.ok(updatedUser);
     //   return ResponseEntity.status(501).body("Service désactivé en profil local");
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<UserWithRolesDto> toggleUserStatus(@PathVariable String userId, @RequestParam boolean active) {
     keycloakUserService.toggleUserStatus(userId, active);
        UserWithRolesDto updatedUser = keycloakUserService.getUserById(userId);
        return ResponseEntity.ok(updatedUser);
      //  return ResponseEntity.status(501).body("Service désactivé en profil local");
    }

    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable String userId, @RequestBody ResetPasswordRequest request) {
        keycloakUserService.requestPasswordReset(userId, request.getNewPassword());
        return ResponseEntity.ok().build();
        //return ResponseEntity.status(501).body("Service désactivé en profil local");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allUsers")
    public ResponseEntity<?> listUsers(

    ) {
        try {
            return ResponseEntity.ok(
                    keycloakUserService.getAllUsersWithRolesAndLastLogin()
            );
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Authentification Keycloak admin refusée. Vérifiez keycloak.admin.clientId/clientSecret et les droits du client.");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/allUsers")
    public ResponseEntity<?> listUsers(
            @RequestParam int limit,
            @RequestParam int offset,
            @RequestParam(required = false) String search
    ) {
        try {
            PageRequest pageRequest = PageRequest.of(offset / limit, limit, Sort.Direction.DESC, "id");
            return ResponseEntity.ok(
                    keycloakUserService.getAllUsersWithRolesAndLastLoginseach(pageRequest,search));
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Authentification Keycloak admin refusée. Vérifiez keycloak.admin.clientId/clientSecret et les droits du client.");
        }

    }



    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('ROLE_ASSIGN') or hasRole('ADMIN')")
    public ResponseEntity<UserWithRolesDto> assignRoles(@PathVariable String userId, @RequestBody AssignRolesRequest request) {
        keycloakUserService.replaceRealmRoles(userId, request.getRoles());
        UserWithRolesDto updatedUser = keycloakUserService.getUserById(userId);

        String currentUser = getCurrentUsername();
        auditLogService.log("ASSIGN_USER_ROLES", "USER", userId, currentUser,
                "Roles assigned to user " + updatedUser.getUserdto().getUsername() + ": " +
                        (request.getRoles() != null ? String.join(", ", request.getRoles()) : "none"));

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        keycloakUserService.deleteUser(userId);
        return ResponseEntity.ok().build();
       // return ResponseEntity.status(501).body("Service désactivé en profil local");
    }

    public static class AssignRolesRequest {
        private List<String> roles;

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}

