package com.nectux.mizan.hyban.rh.absences.web;

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

import com.nectux.mizan.hyban.common.dto.AbsencesRequest;
import com.nectux.mizan.hyban.common.dto.AbsencesResponse;
import com.nectux.mizan.hyban.rh.absences.dto.AbsencesDTO;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.absences.service.AbsencesService;

@RestController
@RequestMapping("/api/rh/absences")
public class AbsencesRestController {

    private static final Logger logger = LoggerFactory.getLogger(AbsencesRestController.class);

    @Autowired
    private AbsencesService absenceService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;
    @Autowired
    private SocieteService societeService;

    @GetMapping("/view")
    public ResponseEntity<String> viewAbsences() {
        try {
            return ResponseEntity.ok("Absences view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<AbsencesDTO> saveAbsences(@RequestBody AbsencesRequest request) {
        try {
            AbsencesDTO result = absenceService.save(
                request.getId(),
                request.getFaute(),
                request.getType(),
                request.getCommentaire()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de l'absence", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<AbsencesDTO> findAbsences(@RequestBody AbsencesRequest request) {
        try {
            AbsencesDTO result = absenceService.findAbsence(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de l'absence", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<AbsencesDTO> findAbsences() {
        try {
            AbsencesDTO result = absenceService.findAbsences();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des absences", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<AbsencesDTO> deleteAbsences(@RequestBody AbsencesRequest request) {
        try {
            AbsencesDTO result = absenceService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'absence", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<AbsencesResponse<?>> getAbsencesList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            AbsencesDTO absenceDTO;
            
            if (search == null || search.trim().isEmpty()) {
                absenceDTO = absenceService.loadAbsences(pageRequest);
            } else {
                absenceDTO = absenceService.loadAbsences(pageRequest, search);
            }
            
            AbsencesResponse<Object> response = new AbsencesResponse<>();
            response.setRows(absenceDTO.getRows().stream().map(absence -> (Object) absence).collect(Collectors.toList()));
            response.setTotal((int)absenceDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des absences", e);
            AbsencesResponse<Object> response = new AbsencesResponse<>();
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
