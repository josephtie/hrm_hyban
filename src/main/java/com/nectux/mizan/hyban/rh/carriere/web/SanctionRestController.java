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

import com.nectux.mizan.hyban.common.dto.SanctionRequest;
import com.nectux.mizan.hyban.common.dto.SanctionResponse;
import com.nectux.mizan.hyban.rh.carriere.dto.SanctionDTO;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.carriere.service.SanctionService;
import com.nectux.mizan.hyban.rh.carriere.service.TypeSanctionService;

@RestController
@RequestMapping("/api/rh/carriere/sanctions")
public class SanctionRestController {

    private static final Logger logger = LoggerFactory.getLogger(SanctionRestController.class);

    @Autowired
    private TypeSanctionService typeSanctionService;
    @Autowired
    private SanctionService sanctionService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    @Autowired
    private SocieteService societeService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/view")
    public ResponseEntity<String> viewSanctions() {
        try {
            return ResponseEntity.ok("Sanctions view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<SanctionDTO> saveSanction(@RequestBody SanctionRequest request) {
        try {
            SanctionDTO result = sanctionService.save(
                request.getId(),
                request.getIdTypeSanction(),
                request.getFaute(),
                request.getCommentaire()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la sanction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<SanctionDTO> findSanction(@RequestBody SanctionRequest request) {
        try {
            SanctionDTO result = sanctionService.findSanction(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la sanction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<SanctionDTO> findSanctions() {
        try {
            SanctionDTO result = sanctionService.findSanctions();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des sanctions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<SanctionDTO> deleteSanction(@RequestBody SanctionRequest request) {
        try {
            SanctionDTO result = sanctionService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la sanction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<SanctionResponse<?>> getSanctionList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            SanctionDTO sanctionDTO;
            
            if (search == null || search.trim().isEmpty()) {
                sanctionDTO = sanctionService.loadSanctions(pageRequest);
            } else {
                sanctionDTO = sanctionService.loadSanctions(pageRequest, search, search);
            }
            
            SanctionResponse<Object> response = new SanctionResponse<>();
            response.setRows(Collections.singletonList(sanctionDTO.getRows()));
            response.setTotal((int) sanctionDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des sanctions", e);
            SanctionResponse<Object> response = new SanctionResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/types-sanctions")
    public ResponseEntity<?> getTypesSanctions() {
        try {
            return ResponseEntity.ok(typeSanctionService.findTypesSanction());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des types de sanctions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
