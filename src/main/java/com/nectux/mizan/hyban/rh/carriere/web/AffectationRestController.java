package com.nectux.mizan.hyban.rh.carriere.web;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.AffectationRequest;
import com.nectux.mizan.hyban.common.dto.AffectationResponse;
import com.nectux.mizan.hyban.rh.carriere.dto.AffectationDTO;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.carriere.service.AffectationService;

@RestController
@RequestMapping("/api/rh/carriere/affectations")
public class AffectationRestController {

    private static final Logger logger = LoggerFactory.getLogger(AffectationRestController.class);

    @Autowired
    private AffectationService affectationService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    @Autowired
    private PeriodePaieService periodePaieService;

    @GetMapping("/view")
    public ResponseEntity<String> viewAffectations() {
        try {
            return ResponseEntity.ok("Affectations view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<AffectationDTO> saveAffectation(@RequestBody AffectationRequest request) {
        try {
            AffectationDTO result = affectationService.saveNew(
                request.getId(),
                request.getIdPersonnel(),
                request.getIdPoste(),
                request.getIdSite(),
                request.getStatutAffect(),
                request.getDateDebut(),
                request.getDateFin(),
                request.getObservation()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de l'affectation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<AffectationDTO> findAffectation(@RequestBody AffectationRequest request) {
        try {
            AffectationDTO result = affectationService.findAffectation(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de l'affectation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<AffectationDTO> findAffectations() {
        try {
            AffectationDTO result = affectationService.findAffectations();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des affectations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister-par-personnel")
    public ResponseEntity<AffectationDTO> findAffectationsByPersonnel(@RequestBody AffectationRequest request) {
        try {
            AffectationDTO result = affectationService.findAffectationsByPersonnel(request.getIdPersonnel());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des affectations par personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister-par-poste")
    public ResponseEntity<AffectationDTO> findAffectationsByPoste(@RequestBody AffectationRequest request) {
        try {
            AffectationDTO result = affectationService.findAffectationsByPoste(request.getIdPoste());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des affectations par poste", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<AffectationDTO> deleteAffectation(@RequestBody AffectationRequest request) {
        try {
            AffectationDTO result = affectationService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'affectation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<AffectationResponse<?>> getAffectationList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            AffectationDTO affectationDTO;
            
            if (search == null || search.trim().isEmpty()) {
                affectationDTO = affectationService.loadAffectations(pageRequest);
            } else {
                affectationDTO = affectationService.loadAffectations(pageRequest, search, search, search);
            }
            
            AffectationResponse<Object> response = new AffectationResponse<>();
            response.setRows(affectationDTO.getRows().stream().map(affectation -> (Object) affectation).collect(Collectors.toList()));
            response.setTotal((int) affectationDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des affectations", e);
            AffectationResponse<Object> response = new AffectationResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/periode-active")
    public ResponseEntity<PeriodePaie> getPeriodeActive() {
        try {
            PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
            return ResponseEntity.ok(periodePaie);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la période active", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
