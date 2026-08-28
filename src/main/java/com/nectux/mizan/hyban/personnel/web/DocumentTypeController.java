package com.nectux.mizan.hyban.personnel.web;

import com.nectux.mizan.hyban.personnel.entity.DocumentType;
import com.nectux.mizan.hyban.personnel.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/personnel/document-types")
@PreAuthorize("hasAnyAuthority('PARAMETER_READ', 'PARAMETER_UPDATE') or hasRole('ADMIN')")
public class DocumentTypeController {
    @Autowired
    private DocumentTypeRepository repository;

    @GetMapping
    public List<DocumentType> all() {
        return repository.findAll();
    }

    @PostMapping
    public DocumentType create(@RequestBody DocumentType docType) {
        return repository.save(docType);
    }
}