package com.nectux.mizan.hyban.parametrages.web;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.TypeServiceRequest;
import com.nectux.mizan.hyban.common.dto.TypeServiceResponse;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.parametrages.entity.TypeService;
import com.nectux.mizan.hyban.parametrages.service.TypeServiceService;

@RestController
@RequestMapping("/api/parametrages/types-services")
@CrossOrigin(origins = {"http://localhost:7153", "http://localhost:7200", "http://localhost:4200", "http://127.0.0.1:3000"}, 
             allowedHeaders = "*", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH},
             allowCredentials = "true")
public class TypeServiceRestController {

    private static final Logger logger = LoggerFactory.getLogger(TypeServiceRestController.class);

    @Autowired
    private TypeServiceService typeServiceService;

    @GetMapping("/{id}")
    public ResponseEntity<TypeService> getTypeService(@PathVariable Long id) {
        try {
            TypeService typeService = typeServiceService.findTypeService(id);
            if (typeService != null) {
                return ResponseEntity.ok(typeService);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du type de service", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/list")
    public ResponseEntity<TypeServiceResponse<Object>> getTypeServiceList(@RequestBody TypeServiceRequest request) {
        try {
            List<TypeService> typeServices = typeServiceService.findTypeServices();
            
            TypeServiceResponse<Object> response = new TypeServiceResponse<>();
            response.setRows(typeServices.stream().map(typeService -> (Object) typeService).collect(Collectors.toList()));
            response.setTotal(typeServices.size());
            response.setResult("success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des types de services", e);
            TypeServiceResponse<Object> response = new TypeServiceResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<TypeServiceResponse<TypeService>> saveTypeService(@RequestBody TypeServiceRequest request) {
        try {
            TypeServiceResponse<TypeService> response = new TypeServiceResponse<>();
            
            TypeService typeService = new TypeService();
            typeService.setId(request.getId());
            typeService.setLibelle(request.getLibelle());
            
            TypeService result = typeServiceService.save(typeService);
            
            if (result != null) {
                response.setResult("success");
                response.setMessage("Type de service créé avec succès");
            } else {
                response.setResult("error");
                response.setMessage("Erreur lors de la création du type de service");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du type de service", e);
            TypeServiceResponse<TypeService> response = new TypeServiceResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<TypeServiceResponse<TypeService>> updateTypeService(@RequestBody TypeServiceRequest request) {
        try {
            TypeServiceResponse<TypeService> response = new TypeServiceResponse<>();
            
            TypeService typeService = new TypeService();
            typeService.setId(request.getId());
            typeService.setLibelle(request.getLibelle());
            
            TypeService result = typeServiceService.save(typeService);
            
            if (result != null) {
                response.setResult("success");
                response.setMessage("Type de service mis à jour avec succès");
            } else {
                response.setResult("error");
                response.setMessage("Erreur lors de la mise à jour du type de service");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du type de service", e);
            TypeServiceResponse<TypeService> response = new TypeServiceResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<TypeServiceResponse<TypeService>> deleteTypeService(@RequestBody IdRequest request) {
        try {
            TypeServiceResponse<TypeService> response = new TypeServiceResponse<>();
            Boolean result = typeServiceService.delete(request.getId());
            
            if (result) {
                response.setResult("success");
                response.setMessage("Type de service supprimé avec succès");
            } else {
                response.setResult("error");
                response.setMessage("Erreur lors de la suppression du type de service");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du type de service", e);
            TypeServiceResponse<TypeService> response = new TypeServiceResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<TypeService>> getAllTypeServices() {
        try {
            List<TypeService> typeServices = typeServiceService.findTypeServices();
            return ResponseEntity.ok(typeServices);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de tous les types de services", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
