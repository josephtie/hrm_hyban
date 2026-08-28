package com.nectux.mizan.hyban.parametrages.service.impl;

import com.nectux.mizan.hyban.parametrages.entity.Permission;
import com.nectux.mizan.hyban.parametrages.entity.Role;
import com.nectux.mizan.hyban.parametrages.entity.RoleName;
import com.nectux.mizan.hyban.parametrages.repository.PermissionRepository;
import com.nectux.mizan.hyban.parametrages.repository.RoleRepository;
import com.nectux.mizan.hyban.parametrages.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Boolean delete(Long id) {
        permissionRepository.deleteById(id);
        return true;
    }

    @Override
    public Permission findById(Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @Override
    public Permission findByCode(String code) {
        return permissionRepository.findByCode(code).orElse(null);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> findActivePermissions() {
        return permissionRepository.findByActiveTrue();
    }

    @Override
    public Set<Permission> findPermissionsByRoleName(String roleName) {
        try {
            RoleName rn = RoleName.valueOf(roleName.toUpperCase());
            return roleRepository.findByName(rn)
                    .map(Role::getPermissions)
                    .orElse(Collections.emptySet());
        } catch (IllegalArgumentException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public List<Permission> findByCodes(List<String> codes) {
        return permissionRepository.findByCodeIn(codes);
    }
}
