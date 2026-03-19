package com.nectux.mizan.hyban.rh.carriere.web;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.TypeSanctionRequest;
import com.nectux.mizan.hyban.common.dto.TypeSanctionResponse;
import com.nectux.mizan.hyban.rh.carriere.dto.TypeSanctionDTO;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.carriere.service.TypeSanctionService;

@RestController
@RequestMapping("/api/rh/carriere/types-sanctions")
public class TypeSanctionRestController {

    private static final Logger logger = LoggerFactory.getLogger(TypeSanctionRestController.class);

    @Autowired
    private TypeSanctionService typeSanctionService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;
    @Autowired
    private SocieteService societeService;

    @GetMapping("/view")
    public ResponseEntity<String> viewTypesSanctions() {
        try {
            return ResponseEntity.ok("Types Sanctions view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<TypeSanctionDTO> saveTypeSanction(@RequestBody TypeSanctionRequest request) {
        try {
            TypeSanctionDTO result = typeSanctionService.save(
                request.getId(),
                request.getLibelle(),
                request.getDescription()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement du type de sanction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<TypeSanctionDTO> findTypeSanction(@RequestBody TypeSanctionRequest request) {
        try {
            TypeSanctionDTO result = typeSanctionService.findTypeSanction(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du type de sanction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<TypeSanctionDTO> findTypeSanctions() {
        try {
            TypeSanctionDTO result = typeSanctionService.findTypeSanctions();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des types de sanctions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<TypeSanctionDTO> deleteTypeSanction(@RequestBody TypeSanctionRequest request) {
        try {
            TypeSanctionDTO result = typeSanctionService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du type de sanction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<TypeSanctionResponse<?>> getTypeSanctionList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            TypeSanctionDTO typeSanctionDTO;
            
            if (search == null || search.trim().isEmpty()) {
                typeSanctionDTO = typeSanctionService.loadTypeSanctions(pageRequest);
            } else {
                typeSanctionDTO = typeSanctionService.loadTypeSanctions(pageRequest, search, search);
            }
            
            TypeSanctionResponse<Object> response = new TypeSanctionResponse<>();
            response.setRows(Collections.singletonList(typeSanctionDTO.getRows()));
            response.setTotal((int) typeSanctionDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des types de sanctions", e);
            TypeSanctionResponse<Object> response = new TypeSanctionResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/societes")
    public ResponseEntity<List<Societe>> getSocietes() {
        try {
            List<Societe> societes = societeService.findtsmois();
            return ResponseEntity.ok(societes);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des sociétés", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
