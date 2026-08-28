package com.nectux.mizan.hyban.parametrages.service;

import com.nectux.mizan.hyban.parametrages.entity.AuditLog;
import com.nectux.mizan.hyban.parametrages.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void log(String action, String entityType, String entityId, String performedBy, String details) {
        AuditLog entry = new AuditLog(action, entityType, entityId, performedBy, details);
        auditLogRepository.save(entry);
    }

    public Page<AuditLog> findAll(Pageable pageable) {
        return auditLogRepository.findAllByOrderByTimestampDesc(pageable);
    }
}
