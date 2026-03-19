package com.nectux.mizan.hyban.rh.carriere.web;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.SanctionPersonnelRequest;
import com.nectux.mizan.hyban.common.dto.SanctionPersonnelResponse;
import com.nectux.mizan.hyban.rh.carriere.dto.SanctionPersonnelDTO;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.carriere.service.SanctionPersonnelService;

@RestController
@RequestMapping("/api/rh/carriere/sanctions-personnel")
public class SanctionPersonnelRestController {

    private static final Logger logger = LoggerFactory.getLogger(SanctionPersonnelRestController.class);

    @Autowired
    private SanctionPersonnelService sanctionPersonnelService;
    // @Autowired
    // private UtilisateurService utilisateurService;

    @GetMapping("/view")
    public ResponseEntity<String> viewSanctionsPersonnel() {
        try {
            return ResponseEntity.ok("Sanctions Personnel view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<SanctionPersonnelDTO> saveSanctionPersonnel(@RequestBody SanctionPersonnelRequest request) {
        try {
            SanctionPersonnelDTO result = sanctionPersonnelService.save(
                request.getId(),
                request.getIdPersonnel(),
                request.getIdSanction(),
                request.getDateDebut(),
                request.getDateFin(),
                request.getObservation()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la sanction personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<SanctionPersonnelDTO> findSanctionPersonnel(@RequestBody SanctionPersonnelRequest request) {
        try {
            SanctionPersonnelDTO result = sanctionPersonnelService.findSanctionPersonnel(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la sanction personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<SanctionPersonnelDTO> findSanctionPersonnels() {
        try {
            SanctionPersonnelDTO result = sanctionPersonnelService.findSanctionPersonnels();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des sanctions personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister-par-personnel")
    public ResponseEntity<SanctionPersonnelDTO> findSanctionPersonnelsByPersonnel(@RequestBody SanctionPersonnelRequest request) {
        try {
            SanctionPersonnelDTO result = sanctionPersonnelService.findSanctionPersonnelsByPersonnel(request.getIdPersonnel());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des sanctions personnel par personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister-par-sanction")
    public ResponseEntity<SanctionPersonnelDTO> findSanctionPersonnelsBySanction(@RequestBody SanctionPersonnelRequest request) {
        try {
            SanctionPersonnelDTO result = sanctionPersonnelService.findSanctionPersonnelsBySanction(request.getIdSanction());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des sanctions personnel par sanction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<SanctionPersonnelDTO> deleteSanctionPersonnel(@RequestBody SanctionPersonnelRequest request) {
        try {
            SanctionPersonnelDTO result = sanctionPersonnelService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la sanction personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<SanctionPersonnelResponse<?>> getSanctionPersonnelList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
            SanctionPersonnelDTO sanctionPersonnelDTO;
            
            if (search == null || search.trim().isEmpty()) {
                sanctionPersonnelDTO = sanctionPersonnelService.loadSanctionPersonnels(pageRequest);
            } else {
                sanctionPersonnelDTO = sanctionPersonnelService.loadSanctionPersonnels(pageRequest, search, search, search, search);
            }
            
            SanctionPersonnelResponse<Object> response = new SanctionPersonnelResponse<>();
            response.setRows(sanctionPersonnelDTO.getRows().stream().map(sanction -> (Object) sanction).collect(Collectors.toList()));
            response.setTotal((int)sanctionPersonnelDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des sanctions personnel", e);
            SanctionPersonnelResponse<Object> response = new SanctionPersonnelResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
