package com.nectux.mizan.hyban.parametrages.web;

import com.nectux.mizan.hyban.parametrages.entity.AuditLog;
import com.nectux.mizan.hyban.parametrages.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
@PreAuthorize("hasAuthority('ROLE_READ') or hasRole('ADMIN')")
public class AuditLogRestController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "timestamp");
        return ResponseEntity.ok(auditLogService.findAll(pageRequest));
    }
}
