package com.nectux.mizan.hyban.parametrages.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserWithRolesDto {
    private UserRepresentationDTO userdto;
    private List<String> roles;
}
