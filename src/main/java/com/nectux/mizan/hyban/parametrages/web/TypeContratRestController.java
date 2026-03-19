package com.nectux.mizan.hyban.parametrages.web;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.TypeContratRequest;
import com.nectux.mizan.hyban.common.dto.TypeContratResponse;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.parametrages.entity.TypeContrat;
import com.nectux.mizan.hyban.parametrages.service.TypeContratService;

@RestController
@RequestMapping("/api/parametrages/types-contrats")
@CrossOrigin(origins = {"http://localhost:7153", "http://localhost:7200", "http://localhost:4200", "http://127.0.0.1:3000"}, 
             allowedHeaders = "*", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH},
             allowCredentials = "true")
public class TypeContratRestController {

    private static final Logger logger = LoggerFactory.getLogger(TypeContratRestController.class);

    @Autowired
    private TypeContratService typeContratService;

    @GetMapping("/{id}")
    public ResponseEntity<TypeContrat> getTypeContrat(@PathVariable Long id) {
        try {
            TypeContrat typeContrat = typeContratService.findTypeContrat(id);
            if (typeContrat != null) {
                return ResponseEntity.ok(typeContrat);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du type de contrat", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/list")
    public ResponseEntity<TypeContratResponse<Object>> getTypeContratList(@RequestBody TypeContratRequest request) {
        try {
            List<TypeContrat> typeContrats = typeContratService.findTypeContrats();
            
            TypeContratResponse<Object> response = new TypeContratResponse<>();
            response.setRows(typeContrats.stream().map(typeContrat -> (Object) typeContrat).collect(Collectors.toList()));
            response.setTotal(typeContrats.size());
            response.setResult("success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des types de contrats", e);
            TypeContratResponse<Object> response = new TypeContratResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<TypeContratResponse<TypeContrat>> saveTypeContrat(@RequestBody TypeContratRequest request) {
        try {
            TypeContratResponse<TypeContrat> response = new TypeContratResponse<>();
            
            TypeContrat typeContrat = new TypeContrat();
            typeContrat.setId(request.getId());
            typeContrat.setLibelle(request.getLibelle());
            
            TypeContrat result = typeContratService.save(typeContrat);
            
            if (result != null) {
                response.setResult("success");
                response.setMessage("Type de contrat créé avec succès");
            } else {
                response.setResult("error");
                response.setMessage("Erreur lors de la création du type de contrat");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du type de contrat", e);
            TypeContratResponse<TypeContrat> response = new TypeContratResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<TypeContratResponse<TypeContrat>> updateTypeContrat(@RequestBody TypeContratRequest request) {
        try {
            TypeContratResponse<TypeContrat> response = new TypeContratResponse<>();
            
            TypeContrat typeContrat = new TypeContrat();
            typeContrat.setId(request.getId());
            typeContrat.setLibelle(request.getLibelle());
            
            TypeContrat result = typeContratService.save(typeContrat);
            
            if (result != null) {
                response.setResult("success");
                response.setMessage("Type de contrat mis à jour avec succès");
            } else {
                response.setResult("error");
                response.setMessage("Erreur lors de la mise à jour du type de contrat");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du type de contrat", e);
            TypeContratResponse<TypeContrat> response = new TypeContratResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<TypeContratResponse<TypeContrat>> deleteTypeContrat(@RequestBody IdRequest request) {
        try {
            TypeContratResponse<TypeContrat> response = new TypeContratResponse<>();
            Boolean result = typeContratService.delete(request.getId());
            
            if (result) {
                response.setResult("success");
                response.setMessage("Type de contrat supprimé avec succès");
            } else {
                response.setResult("error");
                response.setMessage("Erreur lors de la suppression du type de contrat");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du type de contrat", e);
            TypeContratResponse<TypeContrat> response = new TypeContratResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<TypeContrat>> getAllTypeContrats() {
        try {
            List<TypeContrat> typeContrats = typeContratService.findTypeContrats();
            return ResponseEntity.ok(typeContrats);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de tous les types de contrats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
