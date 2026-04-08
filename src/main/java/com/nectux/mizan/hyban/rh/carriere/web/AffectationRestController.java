package com.nectux.mizan.hyban.rh.carriere.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.AffectationRequest;
import com.nectux.mizan.hyban.common.dto.AffectationResponse;
import com.nectux.mizan.hyban.personnel.entity.EmployeeDocument;
import com.nectux.mizan.hyban.rh.carriere.dto.AffectationDTO;
import com.nectux.mizan.hyban.rh.carriere.service.AffectationService;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.personnel.entity.Personnel;
import com.nectux.mizan.hyban.personnel.service.DocumentService;
import com.nectux.mizan.hyban.personnel.service.PersonnelService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rh/carriere/affectations")
public class AffectationRestController {

    private static final Logger logger = LoggerFactory.getLogger(AffectationRestController.class);

    @Autowired
    private AffectationService affectationService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private PersonnelService personnelService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    @Autowired
    private PeriodePaieService periodePaieService;

    @GetMapping("/view")
    public ResponseEntity<String> viewAffectations() {
        try {
            return ResponseEntity.ok("Affectations view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @PostMapping(value = "/enregistrer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AffectationDTO> saveAffectation(
          AffectationRequest affecrequest,
            @RequestPart(required = false) MultipartFile document,HttpServletRequest request) throws IOException {
        String safeLibelle = "Mobilite Agent".replaceAll("[^a-zA-Z0-9_-]", "_");
        Personnel pers = personnelService.findPersonnel(affecrequest.getIdPersonnel());
        // Détection de l'environnement
        String rootPath;
        if (Files.exists(Paths.get("src/main/resources/uploads"))) {
            rootPath = "src/main/resources/uploads/documents";
        } else {
            rootPath = request.getServletContext().getRealPath("/uploads/documents");
        }

        File folder = Paths.get(rootPath, safeLibelle, String.valueOf(pers.getMatricule())).toAbsolutePath().toFile();
        if (!folder.exists()) folder.mkdirs();

        String originalFilename = document.getOriginalFilename();
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
            extension = originalFilename.substring(i);
            originalFilename = originalFilename.substring(0, i);
        }

        String uniqueName = pers.getMatricule() + "_" + originalFilename + extension;
        File dest = new File(folder, uniqueName);
        document.transferTo(dest);

        // Chemin relatif cohérent (ex: uploads/documents/Libelle/331/fichier.docx)
        String relativePath = Paths.get("uploads", "documents", safeLibelle, String.valueOf(pers.getMatricule()), uniqueName).toString();
//
//        String documentPath = null;
//
//        // Sauvegarder le document si présent
//        if (document != null && !document.isEmpty()) {
//            documentPath = documentService.saveDocument(document);
//            logger.info("📄 Document sauvegardé avec le chemin: {}", documentPath);
      //  }

        // Enregistrer l'affectation avec le chemin du document
        AffectationDTO result = affectationService.saveNew(
                affecrequest.getId(), affecrequest.getIdPersonnel(), affecrequest.getIdPoste(),
                affecrequest.getIdSite(), affecrequest.getStatutAffect(),
                affecrequest.getDateDebut(), affecrequest.getDateFin(),
                affecrequest.getObservation(), relativePath  // ✅ Ajout du chemin du document
        );

        return ResponseEntity.ok(result);
    }




    @PostMapping("/trouver")
    public ResponseEntity<AffectationDTO> findAffectation(@RequestBody AffectationRequest request) {
        try {
            AffectationDTO result = affectationService.findAffectation(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de l'affectation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister")
    public ResponseEntity<AffectationDTO> findAffectations() {
        try {
            AffectationDTO result = affectationService.findAffectations();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des affectations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister-par-personnel")
    public ResponseEntity<AffectationDTO> findAffectationsByPersonnel(@RequestBody AffectationRequest request) {
        try {
            AffectationDTO result = affectationService.findAffectationsByPersonnel(request.getIdPersonnel());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des affectations par personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/lister-par-poste")
    public ResponseEntity<AffectationDTO> findAffectationsByPoste(@RequestBody AffectationRequest request) {
        try {
            AffectationDTO result = affectationService.findAffectationsByPoste(request.getIdPoste());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des affectations par poste", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<AffectationDTO> deleteAffectation(@RequestBody AffectationRequest request) {
        try {
            AffectationDTO result = affectationService.delete(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'affectation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    @GetMapping("/list")
    public ResponseEntity<AffectationResponse<?>> getAffectationList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            AffectationDTO affectationDTO;
            
            if (search == null || search.trim().isEmpty()) {
                affectationDTO = affectationService.loadAffectations(pageRequest);
            } else {
                affectationDTO = affectationService.loadAffectations(pageRequest, search, search, search);
            }
            
            AffectationResponse<Object> response = new AffectationResponse<>();
            response.setRows(affectationDTO.getRows().stream().map(affectation -> (Object) affectation).collect(Collectors.toList()));
            response.setTotal((int) affectationDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des affectations", e);
            AffectationResponse<Object> response = new AffectationResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/periode-active")
    public ResponseEntity<PeriodePaie> getPeriodeActive() {
        try {
            PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
            return ResponseEntity.ok(periodePaie);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la période active", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @SuppressWarnings("null")
//    @PostMapping("/download")
//    public ResponseEntity<Resource> downloadDocument(@RequestBody Map<String, String> request) {
//        try {
//            String urlDocument = request.get("urlDocument");
//            String fileName = request.get("fileName");
//
//            // Construire le chemin complet du fichier
//            String filePath = urlDocument.startsWith("/") ? urlDocument.substring(1) : urlDocument;
//            Resource resource = new UrlResource("file:" + filePath);
//
//            if (resource.exists() && resource.isReadable()) {
//                String contentType = "application/octet-stream";
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(contentType))
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                        .body(resource);
//            } else {
//                logger.warn("Fichier non trouvé ou non lisible: {}", filePath);
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            logger.error("Erreur lors du téléchargement du document", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }


    @PostMapping("/download")
    public ResponseEntity<Resource> downloadDocument(@RequestBody IdRequest req, HttpServletRequest request) throws IOException {
        Long affectationId = req.getId();
        AffectationDTO doc = affectationService.findAffectation(affectationId);

        String relativePath = doc.getRow().getUrlDocument().replaceFirst("^/+", ""); // nettoyage

        Path absolutePath;

        Path localUploadsPath = Paths.get("src/main/resources/uploads");
        if (Files.exists(localUploadsPath)) {
            absolutePath = Paths.get("src/main/resources").resolve(relativePath);
        } else {
            String realBasePath = request.getServletContext().getRealPath("/");
            if (realBasePath != null) {
                absolutePath = Paths.get(realBasePath).resolve(relativePath);
            } else {
                throw new IllegalStateException("Impossible de déterminer le chemin d'accès en production.");
            }
        }

        if (!Files.exists(absolutePath)) {
            throw new FileNotFoundException("Fichier introuvable : " + absolutePath);
        }

        Resource fileResource = new UrlResource(absolutePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + absolutePath.getFileName().toString() + "\"")
                .body(fileResource);
    }

}
