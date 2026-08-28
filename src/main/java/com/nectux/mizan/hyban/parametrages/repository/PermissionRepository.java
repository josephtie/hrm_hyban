package com.nectux.mizan.hyban.parametrages.repository;

import com.nectux.mizan.hyban.parametrages.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByCode(String code);

    List<Permission> findByActiveTrue();

    List<Permission> findByCodeIn(List<String> codes);
}
