package com.nectux.mizan.hyban.paie.web;

import java.util.ArrayList;
import java.util.List;
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

import com.nectux.mizan.hyban.common.dto.TempEffectifRequest;
import com.nectux.mizan.hyban.common.dto.TempEffectifResponse;
import com.nectux.mizan.hyban.paie.dto.TempEffectifDTO;
import com.nectux.mizan.hyban.paie.entity.TempEffectif;
import com.nectux.mizan.hyban.paie.repository.TempEffectifRepository;
import com.nectux.mizan.hyban.paie.service.TempEffectifService;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.repository.PeriodePaieRepository;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.personnel.entity.Personnel;
import com.nectux.mizan.hyban.personnel.repository.PersonnelRepository;

@RestController
@RequestMapping("/api/paie/temp-effectif")
public class TempEffectifRestController {

    private static final Logger logger = LoggerFactory.getLogger(TempEffectifRestController.class);

    // @Autowired
    // private UtilisateurService userService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private PeriodePaieRepository periodePaieRepository;
    @Autowired
    private PersonnelRepository personnelRepository;
    @Autowired
    private TempEffectifRepository tempEffectifRepository;
    @Autowired
    private TempEffectifService tempEffectifService;

    @GetMapping("/list")
    public ResponseEntity<TempEffectifResponse<TempEffectif>> getTempEffectifList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            TempEffectifDTO tempEffectifDTO;
            
            if (search == null || search.trim().isEmpty()) {
                tempEffectifDTO = tempEffectifService.loadTempEffectif(pageRequest);
            } else {
                tempEffectifDTO = tempEffectifService.loadTempEffectif(pageRequest, search);
            }
            
            TempEffectifResponse<TempEffectif> response = new TempEffectifResponse<>();
            response.setRows(tempEffectifDTO.getRows());
            response.setTotal((int) tempEffectifDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des effectifs temporaires", e);
            TempEffectifResponse<TempEffectif> response = new TempEffectifResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<TempEffectifDTO> saveTempEffectif(@RequestBody TempEffectifRequest request) {
        try {
            logger.info(">>> ENREGISTRER TEMP EFFECTIF");
            TempEffectifDTO result = tempEffectifService.saver(
                request.getTemptravail(),
                request.getJourtravail(),
                request.getIdPers(),
                request.getIdPeriodDep()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de l'effectif temporaire", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/chercher")
    public ResponseEntity<TempEffectif> chercherTempEffectif(
            @RequestParam Long idPersonnel,
            @RequestParam Long idPeriodDep) {
        
        try {
            logger.info(">>> CHERCHER TEMP EFFECTIF");
            Personnel personnel = personnelRepository.findById(idPersonnel)
                    .orElseThrow(() -> new EntityNotFoundException("Personnel not found for id " + idPersonnel));
            
            PeriodePaie periodePaie = periodePaieRepository.findById(idPeriodDep)
                    .orElseThrow(() -> new EntityNotFoundException("PeriodePaie not found for id " + idPeriodDep));
            
            TempEffectif result = tempEffectifRepository.findByPersonnelAndPeriodePaie(personnel, periodePaie);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de l'effectif temporaire", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/periodes")
    public ResponseEntity<List<PeriodePaie>> getPeriodes() {
        try {
            List<PeriodePaie> listPeriodepaie = periodePaieService.listperiodesupAuPret();
            return ResponseEntity.ok(listPeriodepaie);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des périodes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
