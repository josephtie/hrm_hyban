package com.nectux.mizan.hyban.paie.web;

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

import com.nectux.mizan.hyban.common.dto.MvtCongeRequest;
import com.nectux.mizan.hyban.common.dto.MvtCongeResponse;
import com.nectux.mizan.hyban.paie.dto.MvtCongeDTO;
import com.nectux.mizan.hyban.paie.service.MvtCongeService;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/personnel/mvt-conges")
public class MvtCongeRestController {

    private static final Logger logger = LoggerFactory.getLogger(MvtCongeRestController.class);

    @Autowired
    private MvtCongeService mvtCongeService;
    @Autowired
    private PeriodePaieService periodePaieService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    @Autowired
    private SocieteService societeService;

    @PostMapping("/enregistrer")
    public ResponseEntity<MvtCongeDTO> saveMvtConge(@RequestBody MvtCongeRequest request) {
        try {
            logger.info(">>> ENREGISTRER STOCK CONGE");
            MvtCongeDTO result = mvtCongeService.save(
                request.getId(), 
                request.getIdPersonnel(), 
                request.getDateDepart(), 
                request.getDateRetour(), 
                request.getMontantVerse()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement du mouvement congé", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/trouver/{id}")
    public ResponseEntity<MvtCongeDTO> findMvtConge(@PathVariable Long id) {
        try {
            logger.info(">>> TROUVER STOCK CONGE");
            MvtCongeDTO result = mvtCongeService.findMvtConge(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du mouvement congé", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/lister")
    public ResponseEntity<MvtCongeDTO> listerMvtConges() {
        try {
            logger.info(">>> LISTE STOCK CONGES");
            MvtCongeDTO result = mvtCongeService.findMvtConges();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des mouvements congé", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/lister/personnel/{idPersonnel}")
    public ResponseEntity<MvtCongeDTO> listerMvtCongesParPersonnel(@PathVariable Long idPersonnel) {
        try {
            logger.info(">>> LISTE STOCK CONGES PAR PERSONNEL");
            MvtCongeDTO result = mvtCongeService.findMvtCongesByPersonnel(idPersonnel);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des mouvements congé par personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<MvtCongeDTO> deleteMvtConge(@PathVariable Long id) {
        try {
            logger.info(">>> SUPPRIMER STOCK CONGE");
            MvtCongeDTO result = mvtCongeService.delete(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du mouvement congé", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paginer")
    public ResponseEntity<MvtCongeResponse<?>> getMvtCongeListJSON(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            logger.info(">>> LISTE STOCK CONGES AVEC PAGINATION");
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
            
            MvtCongeDTO mvtCongeDTO;
            if (search == null || search.trim().isEmpty()) {
                mvtCongeDTO = mvtCongeService.loadMvtConges(pageRequest);
            } else {
                mvtCongeDTO = mvtCongeService.loadMvtConges(pageRequest);
            }
            
            MvtCongeResponse<Object> response = new MvtCongeResponse<>();
            response.setRows(mvtCongeDTO.getRows().stream().map(mvt -> (Object) mvt).collect(Collectors.toList()));
            response.setTotal((int) mvtCongeDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la pagination des mouvements congé", e);
            MvtCongeResponse<Object> response = new MvtCongeResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
