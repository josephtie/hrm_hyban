package com.nectux.mizan.hyban.rh.carriere.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.PromotionPersonnelRequest;
import com.nectux.mizan.hyban.common.dto.PromotionPersonnelResponse;
import com.nectux.mizan.hyban.rh.carriere.dto.PromotionPersonnelDTO;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.carriere.service.PromotionPersonnelService;

import java.util.Collections;

@RestController
@RequestMapping("/api/rh/carriere/promotions-personnel")
public class PromotionPersonnelRestController {

    private static final Logger logger = LoggerFactory.getLogger(PromotionPersonnelRestController.class);

    @Autowired
    private PromotionPersonnelService promotionPersonnelService;
    // @Autowired
    // private UtilisateurService utilisateurService;

    @GetMapping("/view")
    public ResponseEntity<String> viewPromotionsPersonnel() {
        try {
            return ResponseEntity.ok("Promotions Personnel view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<PromotionPersonnelDTO> savePromotionPersonnel(@RequestBody PromotionPersonnelRequest request) {
        try {
            PromotionPersonnelDTO result = promotionPersonnelService.save(
                request.getId(),
                request.getIdPersonnel(),
                request.getIdPromotion(),
                request.getDatePromotion(),
                request.getCommentaire()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la promotion personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<PromotionPersonnelDTO> findPromotionPersonnel(@RequestBody PromotionPersonnelRequest request) {
        try {
            PromotionPersonnelDTO result = promotionPersonnelService.findPromotionPersonnel(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la promotion personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<PromotionPersonnelDTO> findPromotionPersonnels() {
        try {
            PromotionPersonnelDTO result = promotionPersonnelService.findPromotionPersonnels();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des promotions personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister-par-personnel")
    public ResponseEntity<PromotionPersonnelDTO> findPromotionPersonnelsByPersonnel(@RequestBody PromotionPersonnelRequest request) {
        try {
            PromotionPersonnelDTO result = promotionPersonnelService.findPromotionPersonnelsByPersonnel(request.getIdPersonnel());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des promotions personnel par personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister-par-promotion")
    public ResponseEntity<PromotionPersonnelDTO> findPromotionPersonnelsByPromotion(@RequestBody PromotionPersonnelRequest request) {
        try {
            PromotionPersonnelDTO result = promotionPersonnelService.findPromotionPersonnelsByPromotion(request.getIdPromotion());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des promotions personnel par promotion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<PromotionPersonnelDTO> deletePromotionPersonnel(@RequestBody PromotionPersonnelRequest request) {
        try {
            PromotionPersonnelDTO result = promotionPersonnelService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la promotion personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<PromotionPersonnelResponse<?>> getPromotionPersonnelList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
            PromotionPersonnelDTO promotionPersonnelDTO;
            
            if (search == null || search.trim().isEmpty()) {
                promotionPersonnelDTO = promotionPersonnelService.loadPromotionPersonnels(pageRequest);
            } else {
                promotionPersonnelDTO = promotionPersonnelService.loadPromotionPersonnels(pageRequest, search, search, search);
            }
            
            PromotionPersonnelResponse<Object> response = new PromotionPersonnelResponse<>();
            response.setRows(Collections.singletonList(promotionPersonnelDTO.getRows()));
            response.setTotal((int) promotionPersonnelDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des promotions personnel", e);
            PromotionPersonnelResponse<Object> response = new PromotionPersonnelResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
