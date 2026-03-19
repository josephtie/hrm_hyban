package com.nectux.mizan.hyban.parametrages.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.ExerciceRequest;
import com.nectux.mizan.hyban.common.dto.ExerciceResponse;
import com.nectux.mizan.hyban.parametrages.dto.ExerciceDTO;
import com.nectux.mizan.hyban.parametrages.entity.Exercice;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.repository.ExerciceRepository;
import com.nectux.mizan.hyban.parametrages.service.ExerciceService;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/parametrages/exercices")
public class ExerciceRestController {

    private static final Logger logger = LoggerFactory.getLogger(ExerciceRestController.class);

    // @Autowired
    // private UtilisateurService userService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private ExerciceRepository exerciceRepository;
    @Autowired
    private ExerciceService exerciceService;
    @Autowired
    private SocieteService societeService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/view")
    public ResponseEntity<String> viewExercice() {
        try {
            return ResponseEntity.ok("Exercice view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ExerciceResponse<?>> getExerciceList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "name");
            ExerciceDTO exerciceDTO;
            
            if (search == null || search.trim().isEmpty()) {
                exerciceDTO = exerciceService.loadExercice(pageRequest);
            } else {
                exerciceDTO = exerciceService.loadExercice(pageRequest, search);
            }
            
            ExerciceResponse<Object> response = new ExerciceResponse<>();
            response.setRows(exerciceDTO.getRows().stream().map(exercice -> (Object) exercice).collect(Collectors.toList()));
            response.setTotal((int) exerciceDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des exercices", e);
            ExerciceResponse<Object> response = new ExerciceResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<ExerciceResponse<?>> listExercices(@RequestBody(required = false) Map<String, Object> filters) {
        try {
            PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "annee");
            ExerciceDTO exerciceDTO = exerciceService.loadExercice(pageRequest);
            
            ExerciceResponse<Object> response = new ExerciceResponse<>();
            response.setRows(exerciceDTO.getRows().stream().map(exercice -> (Object) exercice).collect(Collectors.toList()));
            response.setTotal((int) exerciceDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des exercices", e);
            ExerciceResponse<Object> response = new ExerciceResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/actif")
    public ResponseEntity<ExerciceDTO> getExerciceActif() {
        try {
            ExerciceDTO exerciceDTO = new ExerciceDTO();
            Exercice monexercice = exerciceService.findExoactif();
            List<Exercice> listexo = new ArrayList<Exercice>();
            
            if (monexercice != null) {
                listexo.add(monexercice);
                exerciceDTO.setRow(monexercice);
                exerciceDTO.setRows(listexo);
                exerciceDTO.setResult("success");
            }
            
            return ResponseEntity.ok(exerciceDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'exercice actif", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/a-recuperer")
    public ResponseEntity<List<Exercice>> getExerciceARecuperer() {
        try {
            List<Exercice> exercices = exerciceService.findArecuperer();
            return ResponseEntity.ok(exercices);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des exercices à récupérer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cloturer")
    public ResponseEntity<ExerciceDTO> cloturerExercice(@RequestBody ExerciceRequest request) {
        try {
            ExerciceDTO exerciceDTO = new ExerciceDTO();
            
            Exercice maperiode = exerciceRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Exercice not found for id " + request.getId()));
            Exercice maperiodenew = exerciceRepository.findById(request.getId() + 1L)
                .orElseThrow(() -> new EntityNotFoundException("Nouvel exercice not found for id " + (request.getId() + 1L)));
            
            if (maperiode != null) {
                maperiode.setCloture(true);
                maperiode = exerciceRepository.save(maperiode);
            }
            
            maperiodenew.setCloture(false);
            maperiodenew = exerciceRepository.save(maperiodenew);
            
            exerciceDTO.setRow(maperiode);
            exerciceDTO.setResult("success");
            
            return ResponseEntity.ok(exerciceDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la clôture de l'exercice", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
