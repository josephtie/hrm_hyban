package com.nectux.mizan.hyban.parametrages.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRepresentationDTO {
    private String id;
    private String username;
    private String email;
    private boolean enabled;
    private LocalDateTime lastLogin;
}
