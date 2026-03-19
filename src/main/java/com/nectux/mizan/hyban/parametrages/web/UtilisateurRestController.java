//package com.nectux.mizan.hyban.parametrages.web;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort.Direction;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.nectux.mizan.hyban.common.dto.UtilisateurVueRequest;
//import com.nectux.mizan.hyban.common.dto.UtilisateurVueResponse;
//import com.nectux.mizan.hyban.parametrages.dto.UserDto;
//import com.nectux.mizan.hyban.common.dto.EmailRequest;
//import com.nectux.mizan.hyban.common.dto.ChangePasswordRequest;
//import com.nectux.mizan.hyban.common.dto.IdRequest;
//import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
//import com.nectux.mizan.hyban.parametrages.entity.RoleName;
//import com.nectux.mizan.hyban.parametrages.service.SocieteService;
//// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
//// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
//
//@RestController
//@RequestMapping("/api/parametrages/utilisateurs")
//@CrossOrigin(origins = {"http://localhost:7153", "http://localhost:7200", "http://localhost:4200", "http://127.0.0.1:3000"},
//             allowedHeaders = "*",
//             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH},
//             allowCredentials = "true")
//public class UtilisateurRestController {
//
//    private static final Logger logger = LoggerFactory.getLogger(UtilisateurRestController.class);
//
//    // @Autowired
//    // private UtilisateurService utilisateurService;
//    // @Autowired
//    // private UtilisateurRoleService utilisateurRoleService;
//    @Autowired
//    private SocieteService societeService;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Long id) {
//        try {
//            // Utilisateur utilisateur = utilisateurService.findUtilisateur(id);
//            // if (utilisateur != null) {
//            //     return ResponseEntity.ok(utilisateur);
//            // } else {
//            //     return ResponseEntity.notFound().build();
//            // }
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération de l'utilisateur", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @PostMapping("/list")
//    public ResponseEntity<UtilisateurVueResponse<Object>> getUtilisateurList(@RequestBody UtilisateurVueRequest request) {
//        try {
//            Integer offset = request.getOffset() == null ? 0 : request.getOffset();
//            Integer limit = request.getLimit() == null ? 10 : request.getLimit();
//            String search = request.getSearch();
//
//            PageRequest pageRequest = PageRequest.of(offset / limit, limit, Direction.DESC, "id");
//            // UtilisateurRoleDTO utilisateurRoleDTO;
//            //
//            // if (search == null || search.trim().isEmpty()) {
//            //     utilisateurRoleDTO = utilisateurService.loadUtilisateur(pageRequest);
//            // } else {
//            //     utilisateurRoleDTO = utilisateurService.loadUtilisateur(pageRequest, search);
//            // }
//            //
//            // UtilisateurVueResponse<Object> response = new UtilisateurVueResponse<>();
//            // response.setData(utilisateurRoleDTO.getUtilisateurs());
//            // response.setTotalRecords(utilisateurRoleDTO.getTotalRecords());
//            // response.setTotalPages(utilisateurRoleDTO.getTotalPages());
//            // response.setCurrentPage(pageRequest.getPageNumber() + 1);
//            // response.setResult("success");
//            // return ResponseEntity.ok(response);
//            return ResponseEntity.ok(new UtilisateurVueResponse<>());
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération de la liste des utilisateurs", e);
//            UtilisateurVueResponse<Object> response = new UtilisateurVueResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/save")
//    public ResponseEntity<UtilisateurVueResponse<Utilisateur>> saveUtilisateur(@RequestBody UtilisateurVueRequest request) {
//        try {
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//
//            UserDto utilisateur = new UserDto();
//            // utilisateur.setPassword(request.getPassword() != null ? request.getPassword() : "1234567L");
//            // utilisateur.setEmail(request.getEmail());
//            // utilisateur.setNomComplet(request.getNomComplet());
//            // utilisateur.setUsername(request.getUsername() != null ? request.getUsername() : request.getDateNaissance());
//
//            // Mapping des rôles
//            if (request.getIdRole() != null) {
//                if (request.getIdRole() == 1) utilisateur.setRoleName(RoleName.ADMIN);
//                else if (request.getIdRole() == 2) utilisateur.setRoleName(RoleName.DAF);
//                else if (request.getIdRole() == 3) utilisateur.setRoleName(RoleName.RH);
//                else if (request.getIdRole() == 4) utilisateur.setRoleName(RoleName.PTGE);
//            }
//
//            // UtilisateurRoleDTO result = utilisateurService.save(
//            //     request.getId(),
//            //     request.getIdRole(),
//            //     request.getNomComplet(),
//            //     request.getDateNaissance(),
//            //     request.getTelephone(),
//            //     request.getAdresse(),
//            //     request.getEmail(),
//            //     utilisateur.getPassword()
//            // );
//
//            // if (result.getMessage().contains("succes")) {
//            //     response.setResult("success");
//            //     response.setMessage("Utilisateur créé avec succès");
//            // } else {
//            //     response.setResult("error");
//            //     response.setMessage(result.getMessage());
//            // }
//            response.setResult("error");
//            response.setMessage("Service non disponible");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la création de l'utilisateur", e);
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity<UtilisateurVueResponse<Utilisateur>> updateUtilisateur(@RequestBody UtilisateurVueRequest request) {
//        try {
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//
//            UserDto utilisateur = new UserDto();
//            // utilisateur.setPassword(request.getPassword() != null ? request.getPassword() : "1234567L");
//            // utilisateur.setEmail(request.getEmail());
//            // utilisateur.setNomComplet(request.getNomComplet());
//            // utilisateur.setUsername(request.getUsername() != null ? request.getUsername() : request.getDateNaissance());
//
//            // Mapping des rôles
//            if (request.getIdRole() != null) {
//                if (request.getIdRole() == 1) utilisateur.setRoleName(RoleName.ADMIN);
//                else if (request.getIdRole() == 2) utilisateur.setRoleName(RoleName.DAF);
//                else if (request.getIdRole() == 3) utilisateur.setRoleName(RoleName.RH);
//                else if (request.getIdRole() == 4) utilisateur.setRoleName(RoleName.PTGE);
//            }
//
//            // UtilisateurRoleDTO result = utilisateurService.save(
//            //     request.getId(),
//            //     request.getIdRole(),
//            //     request.getNomComplet(),
//            //     request.getDateNaissance(),
//            //     request.getTelephone(),
//            //     request.getAdresse(),
//            //     request.getEmail(),
//            //     utilisateur.getPassword()
//            // );
//
//            // if (result.getMessage().contains("succes")) {
//            //     response.setResult("success");
//            //     response.setMessage("Utilisateur mis à jour avec succès");
//            // } else {
//            //     response.setResult("error");
//            //     response.setMessage(result.getMessage());
//            // }
//            response.setResult("error");
//            response.setMessage("Service non disponible");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la mise à jour de l'utilisateur", e);
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/delete")
//    public ResponseEntity<UtilisateurVueResponse<Utilisateur>> deleteUtilisateur(@RequestBody IdRequest request) {
//        try {
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            // Boolean result = utilisateurService.delete(request.getId());
//
//            // if (result) {
//            //     response.setResult("success");
//            //     response.setMessage("Utilisateur supprimé avec succès");
//            // } else {
//            //     response.setResult("error");
//            //     response.setMessage("Échec de la suppression");
//            // }
//            response.setResult("error");
//            response.setMessage("Service non disponible");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la suppression de l'utilisateur", e);
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/forgot-password")
//    public ResponseEntity<UtilisateurVueResponse<Utilisateur>> forgotPassword(@RequestBody EmailRequest request) {
//        try {
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            // UtilisateurDTO result = utilisateurService.forgetPassword(request.getEmail());
//
//            // if (result.getMessage().contains("succes")) {
//            //     response.setResult("success");
//            //     response.setMessage(result.getMessage());
//            // } else {
//            //     response.setResult("error");
//            //     response.setMessage(result.getMessage());
//            // }
//            response.setResult("error");
//            response.setMessage("Service non disponible");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération du mot de passe", e);
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/change-password")
//    public ResponseEntity<UtilisateurVueResponse<Utilisateur>> changePassword(@RequestBody ChangePasswordRequest request) {
//        try {
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            // Utilisateur utilisateur = utilisateurService.findUtilisateur(request.getId());
//
//            // if (utilisateur != null) {
//            //     UtilisateurDTO result = utilisateurService.changePassword(
//            //         Long.valueOf(utilisateur.getId()),
//            //         request.getAncien(),
//            //         request.getNouveau()
//            //     );
//
//            //     if (result.getMessage().contains("succes")) {
//            //         response.setResult("success");
//            //         response.setMessage("Mot de passe changé avec succès");
//            //     } else {
//            //         response.setResult("error");
//            //         response.setMessage(result.getMessage());
//            //     }
//            // } else {
//            //     response.setResult("error");
//            //     response.setMessage("Utilisateur non trouvé");
//            // }
//            response.setResult("error");
//            response.setMessage("Service non disponible");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors du changement de mot de passe", e);
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/change-status")
//    public ResponseEntity<UtilisateurVueResponse<Utilisateur>> changeStatus(@RequestBody IdRequest request) {
//        try {
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            // UtilisateurDTO result = utilisateurService.changeStstus(request.getId());
//
//            // if (result.getMessage().contains("succes")) {
//            //     response.setResult("success");
//            //     response.setMessage("Statut changé avec succès");
//            // } else {
//            //     response.setResult("error");
//            //     response.setMessage(result.getMessage());
//            // }
//            response.setResult("error");
//            response.setMessage("Service non disponible");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors du changement de statut", e);
//            UtilisateurVueResponse<Utilisateur> response = new UtilisateurVueResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @GetMapping("/list/all")
//    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
//        try {
//            // Note: Cette méthode dépend de l'implémentation du service
//            // Vous pourriez avoir besoin d'ajouter une méthode dans le service
//            // return ResponseEntity.ok(utilisateurService.findUtilisateurs());
//            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération de tous les utilisateurs", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
