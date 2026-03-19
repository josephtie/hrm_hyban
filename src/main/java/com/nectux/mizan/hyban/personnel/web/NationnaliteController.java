package com.nectux.mizan.hyban.personnel.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.PersonnelVueResponse;
import com.nectux.mizan.hyban.personnel.entity.Nationnalite;
import com.nectux.mizan.hyban.personnel.service.NationnaliteService;

import java.util.List;

/**
 * Contrôleur pour la gestion des nationalités
 * Fournit les endpoints pour les opérations CRUD sur les nationalités
 */
@RestController
@RequestMapping("/api/nationalites")
@CrossOrigin(origins = "*")
public class NationnaliteController {

    private static final Logger logger = LoggerFactory.getLogger(NationnaliteController.class);

    @Autowired
    private NationnaliteService nationaliteService;

    /**
     * Récupérer toutes les nationalités
     * Endpoint: GET /api/nationalites
     */
    @GetMapping
    public ResponseEntity<PersonnelVueResponse<List<Nationnalite>>> getAllNationnalites() {
        try {
            logger.info("Récupération de toutes les nationalités");
            
            List<Nationnalite> nationalites = nationaliteService.findNationnalites();
            
            PersonnelVueResponse<List<Nationnalite>> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Nationalités récupérées avec succès");
            response.setData(nationalites);
            response.setTotal(nationalites.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des nationalités", e);
            PersonnelVueResponse<List<Nationnalite>> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Récupérer une nationalité par son ID
     * Endpoint: GET /api/nationalites/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonnelVueResponse<Nationnalite>> getNationnaliteById(@PathVariable Long id) {
        try {
            logger.info("Récupération de la nationalité ID: {}", id);
            
            Nationnalite nationalite = nationaliteService.findNationnalite(id);
            if (nationalite == null) {
                PersonnelVueResponse<Nationnalite> response = new PersonnelVueResponse<>();
                response.setResult("error");
                response.setMessage("Nationalité non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            PersonnelVueResponse<Nationnalite> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Nationalité récupérée avec succès");
            response.setData(nationalite);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la nationalité", e);
            PersonnelVueResponse<Nationnalite> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Créer une nouvelle nationalité
     * Endpoint: POST /api/nationalites
     */
    @PostMapping
    public ResponseEntity<PersonnelVueResponse<Nationnalite>> createNationnalite(@RequestBody Nationnalite nationalite) {
        try {
            logger.info("Création d'une nouvelle nationalité: {}", nationalite.getLibelle());
            
            Nationnalite result = nationaliteService.save(nationalite);
            
            PersonnelVueResponse<Nationnalite> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Nationalité créée avec succès");
            response.setData(result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la nationalité", e);
            PersonnelVueResponse<Nationnalite> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Mettre à jour une nationalité
     * Endpoint: PUT /api/nationalites/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonnelVueResponse<Nationnalite>> updateNationnalite(
            @PathVariable Long id, 
            @RequestBody Nationnalite nationalite) {
        try {
            logger.info("Mise à jour de la nationalité ID: {}", id);
            
            Nationnalite existingNationnalite = nationaliteService.findNationnalite(id);
            if (existingNationnalite == null) {
                PersonnelVueResponse<Nationnalite> response = new PersonnelVueResponse<>();
                response.setResult("error");
                response.setMessage("Nationalité non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            nationalite.setId(id);
            Nationnalite result = nationaliteService.save(nationalite);
            
            PersonnelVueResponse<Nationnalite> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Nationalité mise à jour avec succès");
            response.setData(result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la nationalité", e);
            PersonnelVueResponse<Nationnalite> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Supprimer une nationalité
     * Endpoint: DELETE /api/nationalites/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<PersonnelVueResponse<Boolean>> deleteNationnalite(@PathVariable Long id) {
        try {
            logger.info("Suppression de la nationalité ID: {}", id);
            
            Nationnalite existingNationnalite = nationaliteService.findNationnalite(id);
            if (existingNationnalite == null) {
                PersonnelVueResponse<Boolean> response = new PersonnelVueResponse<>();
                response.setResult("error");
                response.setMessage("Nationalité non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            nationaliteService.delete(id);
            
            PersonnelVueResponse<Boolean> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Nationalité supprimée avec succès");
            response.setData(true);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la nationalité", e);
            PersonnelVueResponse<Boolean> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
