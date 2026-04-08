package com.nectux.mizan.hyban.rh.carriere.web;

import com.nectux.mizan.hyban.common.dto.PromotionRequest;
import com.nectux.mizan.hyban.common.dto.PromotionResponse;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
import com.nectux.mizan.hyban.rh.carriere.dto.SiteWorkDTO;
import com.nectux.mizan.hyban.rh.carriere.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/rh/carriere/site")
public class SiteWorkRestController {

    private static final Logger logger = LoggerFactory.getLogger(SiteWorkRestController.class);

    @Autowired
    private SiteService SiteService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    @Autowired
    private SocieteService societeService;

    @GetMapping("/view")
    public ResponseEntity<String> viewPromotions() {
        try {
            return ResponseEntity.ok("Promotions view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<SiteWorkDTO> savePromotion(@RequestBody PromotionRequest request) {
        try {
            SiteWorkDTO result = SiteService.save(
                request.getId(),
                request.getLibelle(),
                request.getDescription()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la promotion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<SiteWorkDTO> findPromotion(@RequestBody PromotionRequest request) {
        try {
            SiteWorkDTO result = SiteService.findSite(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la promotion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<SiteWorkDTO> findPromotions() {
        try {
            SiteWorkDTO result = SiteService.findSites();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des promotions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<SiteWorkDTO> deletePromotion(@RequestBody PromotionRequest request) {
        try {
            SiteWorkDTO result = SiteService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la promotion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<PromotionResponse<?>> getPromotionList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            SiteWorkDTO SiteWorkDTO;
            
            if (search == null || search.trim().isEmpty()) {
                SiteWorkDTO = SiteService.loadSites(pageRequest);
            } else {
                SiteWorkDTO = SiteService.loadSites(pageRequest, search, search);
            }
            
            PromotionResponse<Object> response = new PromotionResponse<>();
            response.setRows(Collections.singletonList(SiteWorkDTO.getRows()));
            response.setTotal((int) SiteWorkDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des promotions", e);
            PromotionResponse<Object> response = new PromotionResponse<>();
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
