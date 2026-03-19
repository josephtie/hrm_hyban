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

import com.nectux.mizan.hyban.common.dto.EchelonnementRequest;
import com.nectux.mizan.hyban.common.dto.EchelonnementResponse;
import com.nectux.mizan.hyban.paie.dto.EchelonnementDTO;
import com.nectux.mizan.hyban.paie.entity.Echelonnement;
import com.nectux.mizan.hyban.paie.repository.EchelonnementRepository;
import com.nectux.mizan.hyban.paie.service.EchelonnementService;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/paie/echelonnement")
public class EchelonnementRestController {

    private static final Logger logger = LoggerFactory.getLogger(EchelonnementRestController.class);

    // @Autowired
    // private UtilisateurService userService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private EchelonnementRepository echelonnementRepository;
    @Autowired
    private EchelonnementService echelonnementService;
    @Autowired
    private SocieteService societeService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/list")
    public ResponseEntity<EchelonnementResponse<?>> getEchelonnementList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest page = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            EchelonnementDTO echelonnDTO;
            
            if (search == null || search.trim().isEmpty()) {
                echelonnDTO = echelonnementService.loadEchelonnement(page);
            } else {
                echelonnDTO = echelonnementService.loadEchelonnement(page, search);
            }
            
            EchelonnementResponse<Object> response = new EchelonnementResponse<>();
            response.setRows(echelonnDTO.getRows().stream().map(echelonnement -> (Object) echelonnement).collect(Collectors.toList()));
            response.setTotal((int)echelonnDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste d'échelonnement", e);
            EchelonnementResponse<Object> response = new EchelonnementResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<EchelonnementResponse<Object>> updateEchelonnement(@RequestBody EchelonnementRequest request) {
        try {
            EchelonnementResponse<Object> response = new EchelonnementResponse<>();
            Echelonnement maechelonn = echelonnementRepository.findById(request.getIdechel())
                    .orElseThrow(() -> new EntityNotFoundException("Echelonnement not found for id " + request.getIdechel()));
            
            if (maechelonn != null) {
                response.setRow((Object) maechelonn);
            }
            
            maechelonn = echelonnementService.update(request.getIdechel(), request.getPeriodPaie());
            response.setRow((Object) maechelonn);
            
            if (maechelonn.getMessage().contains("succes")) {
                response.setResult("success");
            } else {
                response.setResult(maechelonn.getMessage());
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'échelonnement", e);
            EchelonnementResponse<Object> response = new EchelonnementResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/pret/{idpretperso}")
    public ResponseEntity<EchelonnementDTO> getEchelonnementDuPret(@PathVariable Long idpretperso) {
        try {
            EchelonnementDTO echelonnDTO = echelonnementService.findechelondupret(idpretperso);
            return ResponseEntity.ok(echelonnDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'échelonnement du prêt", e);
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
