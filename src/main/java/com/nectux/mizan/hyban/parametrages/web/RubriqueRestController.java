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

import com.nectux.mizan.hyban.common.dto.RubriqueRequest;
import com.nectux.mizan.hyban.common.dto.RubriqueResponse;
import com.nectux.mizan.hyban.parametrages.dto.RubriqueDTO;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.service.RubriqueService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/parametrages/rubriques")

public class RubriqueRestController {

    private static final Logger logger = LoggerFactory.getLogger(RubriqueRestController.class);

    @Autowired
    private RubriqueService rubriqueService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    @Autowired
    private SocieteService societeService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/view")
    public ResponseEntity<String> viewRubriques() {
        try {
            return ResponseEntity.ok("Rubriques view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<RubriqueDTO> saveRubrique(@RequestBody RubriqueRequest request) {
        try {
            // Conversion du type (String) vers etatImposition (Integer)
            Integer etatImposition = null;
            if (request.getType() != null && !request.getType().isEmpty()) {
                try {
                    etatImposition = Integer.parseInt(request.getType());
                } catch (NumberFormatException e) {
                    // Si le type n'est pas un nombre, essayer de le convertir depuis le libellé
                    String typeStr = request.getType().toUpperCase();
                    switch (typeStr) {
                        case "IMPOSABLE":
                        case "1":
                            etatImposition = 1;
                            break;
                        case "NON_IMPOSABLE":
                        case "2":
                            etatImposition = 2;
                            break;
                        case "IMPOSABLE_NON_IMPOSABLE":
                        case "3":
                            etatImposition = 3;
                            break;
                        case "RETENUE_MUTUELLE":
                        case "4":
                            etatImposition = 4;
                            break;
                        case "REGULARISATION":
                        case "5":
                            etatImposition = 5;
                            break;
                        case "RETENUE_SOCIALE":
                        case "6":
                            etatImposition = 6;
                            break;
                        default:
                            etatImposition = 1; // Valeur par défaut
                    }
                }
            }
            
            // Utilisation de la nouvelle méthode complète
            RubriqueDTO result = rubriqueService.saveRubriqueComplete(
                request.getIdR(),
                request.getCode(),
                request.getLibelle(),
                etatImposition,
                request.getFormule(), // modeCalcul
                request.getMontant() != null ? request.getMontant() : request.getValeurDef(), // valeurDef
                request.getCotisable(),
                request.getActive(),
                request.getPermanent(),
                request.getSpeciale(),
                request.getDescription()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la rubrique", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/modifier")
    public ResponseEntity<RubriqueDTO> updateRubrique(@RequestBody RubriqueRequest request) {
        try {
            // Vérifier que l'ID est fourni pour la modification
            if (request.getIdR() == null) {
                logger.error("ID requis pour la modification de la rubrique");
                return ResponseEntity.badRequest().build();
            }
            
            // Conversion du type (String) vers etatImposition (Integer)
            Integer etatImposition = null;
            if (request.getType() != null && !request.getType().isEmpty()) {
                try {
                    etatImposition = Integer.parseInt(request.getType());
                } catch (NumberFormatException e) {
                    // Si le type n'est pas un nombre, essayer de le convertir depuis le libellé
                    String typeStr = request.getType().toUpperCase();
                    switch (typeStr) {
                        case "IMPOSABLE":
                        case "1":
                            etatImposition = 1;
                            break;
                        case "NON_IMPOSABLE":
                        case "2":
                            etatImposition = 2;
                            break;
                        case "IMPOSABLE_NON_IMPOSABLE":
                        case "3":
                            etatImposition = 3;
                            break;
                        case "RETENUE_MUTUELLE":
                        case "4":
                            etatImposition = 4;
                            break;
                        case "REGULARISATION":
                        case "5":
                            etatImposition = 5;
                            break;
                        case "RETENUE_SOCIALE":
                        case "6":
                            etatImposition = 6;
                            break;
                        default:
                            etatImposition = 1; // Valeur par défaut
                    }
                }
            }
            
            // Utilisation de la nouvelle méthode complète pour la mise à jour
            RubriqueDTO result = rubriqueService.saveRubriqueComplete(
                request.getIdR(),                    // ID obligatoire pour la modification
                request.getCode(),
                request.getLibelle(),
                etatImposition,
                request.getFormule(), // modeCalcul
                request.getMontant() != null ? request.getMontant() : request.getValeurDef(), // valeurDef
                request.getCotisable(),
                request.getActive(),
                request.getPermanent(),
                request.getSpeciale(),
                request.getDescription()
            );
            
            logger.info("Rubrique modifiée avec succès: ID={}, Code={}, Libelle={}", 
                       request.getIdR(), request.getCode(), request.getLibelle());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la modification de la rubrique", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<RubriqueDTO> deleteRubrique(@RequestBody RubriqueRequest request) {
        try {
            RubriqueDTO result = rubriqueService.delete(request.getIdR());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la rubrique", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<RubriqueDTO> findRubrique(@RequestBody RubriqueRequest request) {
        try {
            RubriqueDTO result = rubriqueService.findRubrique(request.getIdR());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la rubrique", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<RubriqueDTO> findRubriques() {
        try {
            RubriqueDTO result = rubriqueService.findRubriques();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des rubriques", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<RubriqueResponse<?>> getRubriqueList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / limit, limit, Direction.DESC, "id");
            RubriqueDTO rubriqueDTO;
            
            // Note: La recherche est commentée dans le controller original
            rubriqueDTO = rubriqueService.loadRubriques(pageRequest);
            
            RubriqueResponse<Object> response = new RubriqueResponse<>();
            response.setRows(rubriqueDTO.getRows().stream().map(rubrique -> (Object) rubrique).collect(Collectors.toList()));
            response.setTotal((int)rubriqueDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des rubriques", e);
            RubriqueResponse<Object> response = new RubriqueResponse<>();
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
