package com.nectux.mizan.hyban.parametrages.web;


import com.nectux.mizan.hyban.parametrages.dto.CreateUserRequest;
import com.nectux.mizan.hyban.parametrages.dto.ResetPasswordRequest;
import com.nectux.mizan.hyban.parametrages.dto.UserWithRolesDto;
import com.nectux.mizan.hyban.parametrages.service.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final KeycloakUserService keycloakUserService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest request) {
        String userId = keycloakUserService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getRoles()
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
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserWithRolesDto>> listUsers(

    ) {

        return ResponseEntity.ok(
                keycloakUserService.getAllUsersWithRolesAndLastLogin()
        );
    }

    @GetMapping("/search/allUsers")
    public ResponseEntity<List<UserWithRolesDto>> listUsers(
            @RequestParam int limit,
            @RequestParam int offset,
            @RequestParam(required = false) String search
    ) {
        PageRequest pageRequest = PageRequest.of(offset / limit, limit, Sort.Direction.DESC, "id");
        return ResponseEntity.ok(
                keycloakUserService.getAllUsersWithRolesAndLastLoginseach(pageRequest,search));

    }



    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        keycloakUserService.deleteUser(userId);
        return ResponseEntity.ok().build();
       // return ResponseEntity.status(501).body("Service désactivé en profil local");
    }
    

}

