package com.nectux.mizan.hyban.parametrages.service;

import com.nectux.mizan.hyban.parametrages.entity.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    Permission save(Permission permission);

    Boolean delete(Long id);

    Permission findById(Long id);

    Permission findByCode(String code);

    List<Permission> findAll();

    List<Permission> findActivePermissions();

    Set<Permission> findPermissionsByRoleName(String roleName);

    List<Permission> findByCodes(List<String> codes);
}
