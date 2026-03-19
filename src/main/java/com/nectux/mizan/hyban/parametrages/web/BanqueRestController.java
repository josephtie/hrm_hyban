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

import com.nectux.mizan.hyban.common.dto.BanqueRequest;
import com.nectux.mizan.hyban.common.dto.BanqueResponse;
import com.nectux.mizan.hyban.parametrages.dto.BanqueDTO;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.BanqueService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/parametrages/banques")

public class BanqueRestController {

    private static final Logger logger = LoggerFactory.getLogger(BanqueRestController.class);

    @Autowired
    private BanqueService banqueService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    @Autowired
    private SocieteService societeService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/view")
    public ResponseEntity<String> viewBanques() {
        try {
            return ResponseEntity.ok("Banques view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<BanqueDTO> saveBanque(@RequestBody BanqueRequest request) {
        try {
            BanqueDTO result = banqueService.save(
                request.getId(),
                request.getSigle(),
                request.getLibelle(),
                request.getCodbanq(),
                request.getStatut()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la banque", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<BanqueDTO> deleteBanque(@RequestBody BanqueRequest request) {
        try {
            BanqueDTO result = banqueService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la banque", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<BanqueDTO> findBanque(@RequestBody BanqueRequest request) {
        try {
            BanqueDTO result = banqueService.findBanque(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la banque", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<BanqueDTO> findBanques() {
        try {
            BanqueDTO result = banqueService.findBanques();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des banques", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<BanqueResponse<?>> getBanqueList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            BanqueDTO banqueDTO;
            
            if (search == null || search.trim().isEmpty()) {
                banqueDTO = banqueService.loadBanques(pageRequest);
            } else {
                banqueDTO = banqueService.loadBanques(pageRequest, search);
            }
            
            BanqueResponse<Object> response = new BanqueResponse<>();
            response.setRows(banqueDTO.getRows().stream().map(banque -> (Object) banque).collect(Collectors.toList()));
            response.setTotal((int) banqueDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des banques", e);
            BanqueResponse<Object> response = new BanqueResponse<>();
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
