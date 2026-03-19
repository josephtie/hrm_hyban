package com.nectux.mizan.hyban.parametrages.web;

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

import com.nectux.mizan.hyban.common.dto.CpteVirmtBanqueRequest;
import com.nectux.mizan.hyban.common.dto.CpteVirmtBanqueResponse;
import com.nectux.mizan.hyban.parametrages.dto.CpteVirmtBanqueDTO;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.BanqueService;
import com.nectux.mizan.hyban.parametrages.service.CpteVirmtBanqueService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/parametrages/cpte-virement")
public class CpteVirmtBanqueRestController {

    private static final Logger logger = LoggerFactory.getLogger(CpteVirmtBanqueRestController.class);

    @Autowired
    private CpteVirmtBanqueService cpteVirmtBanqueService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    @Autowired
    private BanqueService banqueService;
    @Autowired
    private SocieteService societeService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/view")
    public ResponseEntity<String> viewCpteVirement() {
        try {
            return ResponseEntity.ok("Compte de virements view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<CpteVirmtBanqueDTO> saveCpteVirmtBanque(@RequestBody CpteVirmtBanqueRequest request) {
        try {
            CpteVirmtBanqueDTO result = cpteVirmtBanqueService.saver(
                request.getId(),
                request.getCodEtablVirmt(),
                request.getDonneurOrdCpteVirmt(),
                request.getIdbank(),
                request.getNumcpteCpteVirmt(),
                request.getNumguichCpteVirmt(),
                request.getRibCpteVirmt()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement du compte de virement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<CpteVirmtBanqueDTO> deleteCpteVirmtBanque(@RequestBody CpteVirmtBanqueRequest request) {
        try {
            CpteVirmtBanqueDTO result = cpteVirmtBanqueService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du compte de virement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<CpteVirmtBanqueDTO> findCpteVirmtBanque(@RequestBody CpteVirmtBanqueRequest request) {
        try {
            CpteVirmtBanqueDTO result = cpteVirmtBanqueService.findCpteVirmtBanque(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du compte de virement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<CpteVirmtBanqueDTO> findCpteVirmtBanques() {
        try {
            CpteVirmtBanqueDTO result = cpteVirmtBanqueService.findCpteVirmtBanques();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des comptes de virement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<CpteVirmtBanqueResponse<?>> getCpteVirmtBanqueList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            CpteVirmtBanqueDTO cpteVirmtBanqueDTO;
            
            if (search == null || search.trim().isEmpty()) {
                cpteVirmtBanqueDTO = cpteVirmtBanqueService.loadCpteVirmtBanques(pageRequest);
            } else {
                cpteVirmtBanqueDTO = cpteVirmtBanqueService.loadCpteVirmtBanques(pageRequest, search);
            }
            
            CpteVirmtBanqueResponse<Object> response = new CpteVirmtBanqueResponse<>();
            response.setRows(cpteVirmtBanqueDTO.getRows().stream().map(cpte -> (Object) cpte).collect(Collectors.toList()));
            response.setTotal((int)cpteVirmtBanqueDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des comptes de virement", e);
            CpteVirmtBanqueResponse<Object> response = new CpteVirmtBanqueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/banques")
    public ResponseEntity<?> getBanques() {
        try {
            return ResponseEntity.ok(banqueService.getBanques());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des banques", e);
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
