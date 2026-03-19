package com.nectux.mizan.hyban.personnel.web;

import java.util.List;
import java.util.stream.Collectors;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.nectux.mizan.hyban.common.dto.PersonnelVueRequest;
import com.nectux.mizan.hyban.common.dto.PersonnelVueResponse;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.personnel.dto.PersonnelDTO;
import com.nectux.mizan.hyban.personnel.dto.ContratPersonnelDTO;
import com.nectux.mizan.hyban.personnel.entity.Personnel;
import com.nectux.mizan.hyban.personnel.service.PersonnelService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/personnels/personnel")

@CrossOrigin(origins = {"http://localhost:7153", "http://192.168.1.2:7153", "http://192.168.1.2:8080", "http://192.168.1.3:7156", "http://192.168.1.3:7157", "http://192.168.1.3:7158", "http://192.168.1.3:7159", "http://192.168.1.3:7160", "http://192.168.1.3:7161", "http://192.168.1.3:7162", "http://192.168.1.3:7163", "http://192.168.1.3:7164", "http://192.168.1.3:7165", "http://192.168.1.3:7166", "http://192.168.1.3:7167", "http://192.168.1.3:7168", "http://192.168.1.3:7169", "http://192.168.1.3:7170", "http://192.168.1.3:7171", "http://192.168.1.3:7172", "http://192.168.1.3:7173", "http://192.168.1.3:7174", "http://192.168.1.3:7175", "http://192.168.1.3:7176", "http://192.168.1.3:7177", "http://192.168.1.3:7178", "http://192.168.1.3:7179", "http://192.168.1.3:7180", "http://192.168.1.3:7181", "http://192.168.1.3:7182", "http://192.168.1.3:7183", "http://192.168.1.3:7184", "http://192.168.1.3:7185", "http://192.168.1.3:7186", "http://192.168.1.3:7187", "http://192.168.1.3:7188", "http://192.168.1.3:7189", "http://192.168.1.3:7190", "http://192.168.1.3:7191", "http://192.168.1.3:7192", "http://192.168.1.3:7193", "http://192.168.1.3:7194", "http://192.168.1.3:7195", "http://192.168.1.3:7196", "http://192.168.1.3:7197", "http://192.168.1.3:7198", "http://192.168.1.3:7199"},
        allowCredentials = "true"
)

public class PersonnelRestController {

    private static final Logger logger = LoggerFactory.getLogger(PersonnelRestController.class);

    @Autowired
    private PersonnelService personnelService;
    @Autowired
    private SocieteService societeService;
    // @Autowired
    // private UtilisateurService utilisateurService;

    @GetMapping("/{id}")
    public ResponseEntity<Personnel> getPersonnel(@PathVariable Long id) {
        try {
            Personnel personnel = personnelService.findPersonnel(id);
            if (personnel != null) {
                return ResponseEntity.ok(personnel);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/list")
    public ResponseEntity<PersonnelVueResponse<Object>> getPersonnelList(@RequestBody PersonnelVueRequest request) {
        try {
            Integer offset = request.getOffset() == null ? 0 : request.getOffset();
            Integer limit = request.getLimit() == null ? 10 : request.getLimit();
            String search = request.getSearch();

            PageRequest pageRequest = PageRequest.of(offset , limit, Direction.DESC, "id");
            PersonnelDTO personnelDTO;
            
            // Créer une map de filtres avec tous les paramètres
            java.util.Map<String, String> filters = new java.util.HashMap<>();
            
            // Ajouter la recherche si présente
            if (search != null && !search.trim().isEmpty()) {
                filters.put("search", search);
            }
            
            // Ajouter les autres filtres
            if (request.getServiceFilter() != null && !request.getServiceFilter().trim().isEmpty()) {
                filters.put("service", request.getServiceFilter());
            }
            if (request.getStatut() != null && !request.getStatut().trim().isEmpty()) {
                filters.put("statut", request.getStatut());
            }
            if (request.getModePaiement() != null && !request.getModePaiement().trim().isEmpty()) {
                filters.put("modePaiement", request.getModePaiement());
            }
            if (request.getFonctionFilter() != null && !request.getFonctionFilter().trim().isEmpty()) {
                filters.put("fonction", request.getFonctionFilter());
            }
            
            // Utiliser findAllfilter pour tous les cas (avec ou sans filtres)
            personnelDTO = personnelService.findAllfilter(filters, pageRequest);

            PersonnelVueResponse<Object> response = new PersonnelVueResponse<>();
            response.setRows(personnelDTO.getRows().stream().map(personnel -> (Object) personnel).collect(Collectors.toList()));
            response.setTotal((int) personnelDTO.getTotal());
            response.setResult("success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste du personnel", e);
            PersonnelVueResponse<Object> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportPersonnel(@RequestBody PersonnelVueRequest request) {
        try {
            logger.info("Exportation des personnels avec filtres");
            
            // Créer une map de filtres avec tous les paramètres
            java.util.Map<String, String> filters = new java.util.HashMap<>();
            
            // Ajouter la recherche si présente
            String search = request.getSearch();
            if (search != null && !search.trim().isEmpty()) {
                filters.put("search", search);
            }
            
            // Ajouter les autres filtres
            if (request.getServiceFilter() != null && !request.getServiceFilter().trim().isEmpty()) {
                filters.put("service", request.getServiceFilter());
            }
            if (request.getStatut() != null && !request.getStatut().trim().isEmpty()) {
                filters.put("statut", request.getStatut());
            }
            if (request.getModePaiement() != null && !request.getModePaiement().trim().isEmpty()) {
                filters.put("modePaiement", request.getModePaiement());
            }
            if (request.getFonctionFilter() != null && !request.getFonctionFilter().trim().isEmpty()) {
                filters.put("fonction", request.getFonctionFilter());
            }
            
            // Récupérer tous les personnels avec les filtres (sans pagination)
            Pageable unpaged = Pageable.unpaged();
            PersonnelDTO personnelDTO = personnelService.findAllfilter(filters, unpaged);
            
            // Créer le fichier Excel
            byte[] excelFile = createExcelFile(personnelDTO.getRows());
            
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=personnels_" + 
                    java.time.LocalDate.now().toString() + ".xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelFile);
                
        } catch (Exception e) {
            logger.error("Erreur lors de l'exportation des personnels", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private byte[] createExcelFile(List<Personnel> personnels) {
        try (org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Personnels");
            
            // Créer l'en-tête
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"Matricule", "Nom", "Prénom", "Téléphone", "Email", "Service", "Fonction", "Statut", "Mode Paiement"};
            
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Remplir les données
            int rowNum = 1;
            for (Personnel personnel : personnels) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(personnel.getMatricule() != null ? personnel.getMatricule() : "");
                row.createCell(1).setCellValue(personnel.getNom() != null ? personnel.getNom() : "");
                row.createCell(2).setCellValue(personnel.getPrenom() != null ? personnel.getPrenom() : "");
                row.createCell(3).setCellValue(personnel.getTelephone() != null ? personnel.getTelephone() : "");
                row.createCell(4).setCellValue(personnel.getEmail() != null ? personnel.getEmail() : "");
                row.createCell(5).setCellValue(personnel.getService().getLibelle() != null ? personnel.getService().getLibelle(): "");
                row.createCell(6).setCellValue(personnel.getFonction() != null ? personnel.getFonction() : "");
                row.createCell(7).setCellValue(personnel.getCarec() != null ? (personnel.getCarec() ? "Contractuel" : "Non Contractuel") : "");
                row.createCell(8).setCellValue(personnel.getModePaiement() != null ? personnel.getModePaiement() : "");
            }
            
            // Auto-size les colonnes
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Convertir en byte array
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            logger.error("Erreur lors de la création du fichier Excel", e);
            throw new RuntimeException("Erreur lors de la création du fichier Excel", e);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<PersonnelVueResponse<Personnel>> savePersonnel(@RequestBody PersonnelVueRequest request) {
        try {
            PersonnelVueResponse<Personnel> response = new PersonnelVueResponse<>();
            
            Personnel personnel = new Personnel();
            personnel.setId(request.getId());
            personnel.setNom(request.getNom());
            personnel.setPrenom(request.getPrenom());
            personnel.setMatricule(request.getMatricule());
            personnel.setSexe(request.getSexe());
            personnel.setEmail(request.getEmail());
            personnel.setResidence(request.getResidence());
            personnel.setAdresse(request.getAdresse());
            personnel.setTelephone(request.getTelephone());
            
            Personnel result = personnelService.save(personnel);
            
            if (result != null) {
                response.setResult("success");
                response.setMessage("Personnel créé avec succès");
            } else {
                response.setResult("error");
                response.setMessage("Erreur lors de la création du personnel");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du personnel", e);
            PersonnelVueResponse<Personnel> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== CRUD PERSONNEL AMÉLIORÉ ====================
    
    /**
     * Créer un nouveau personnel avec contrat
     * Endpoint: POST /api/personnels/personnel/enregistrerpersonnel
     */
    @PostMapping("/enregistrerpersonnel")
    public ResponseEntity<PersonnelVueResponse<ContratPersonnelDTO>> savePersonnel(@RequestBody com.nectux.mizan.hyban.common.dto.PersonnelRequest req) {
        try {
            logger.info("Création d'un nouveau personnel: {}", req.getMatricule());
            
            ContratPersonnelDTO result = personnelService.save(
                req.getId(), req.getNom(), req.getPrenom(), req.getNationalite(), 
                req.getService(), req.getCategorie(), req.getFonction(), req.getTypeContrat(), 
                req.getMatricule(), req.getSexe(), req.getDateNaissance(), req.getLieuNaissance(), 
                req.getEmail(), req.getResidence(), 
                req.getSituationMatrimoniale() == null ? 0 : req.getSituationMatrimoniale(), 
                req.getNombreEnfant() == null ? 0 : req.getNombreEnfant(),
                req.getDateArrivee(), req.getNumeroCNPS(), req.getAdresse(), req.getDateDebut(), 
                req.getDateFin(), req.getSalaireNet(), req.getIndemnitelogement(),
                req.getModePaiement(), req.getIdBanque(), req.getNumeroCompte(), 
                req.getNumeroGuichet(), req.getRib(), 
                req.getAncienneteInitial() == null ? 0 : req.getAncienneteInitial(), 
                req.getCarec(), req.getTypeEmp(), req.getTelephone(), 
                req.getSituationMedaille() == null ? 0 : req.getSituationMedaille(), 
                req.getSituationEmploie() == null ? 0 : req.getSituationEmploie(), 
                req.getDateRetourcg(), req.getIndemniteRespons(), req.getIndemniteRepresent(), 
                req.getIndemniteTransport(), req.getSursalaire()
            );
            
            PersonnelVueResponse<ContratPersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Personnel créé avec succès");
            response.setData(result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du personnel", e);
            PersonnelVueResponse<ContratPersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Mettre à jour un personnel existant
     * Endpoint: POST /api/personnels/personnel/modifierpersonnel
     */
    @PostMapping("/modifierpersonnel")
    public ResponseEntity<PersonnelVueResponse<PersonnelDTO>> updatePersonnel(@RequestBody com.nectux.mizan.hyban.common.dto.PersonnelRequest req) {
        try {
            logger.info("Mise à jour du personnel ID: {}", req.getId());
            
            PersonnelDTO result = personnelService.save(
                req.getId(), req.getNom(), req.getPrenom(), req.getNationalite(), 
                req.getService(), req.getMatricule(), req.getSexe(), req.getDateNaissance(), 
                req.getLieuNaissance(), req.getEmail(), req.getResidence(), 
                req.getSituationMatrimoniale() == null ? 0 : req.getSituationMatrimoniale(), 
                req.getNombreEnfant() == null ? 0 : req.getNombreEnfant(), req.getDateArrivee(), 
                req.getNumeroCNPS(), req.getAdresse(), req.getCarec(),
                req.getModePaiement(), req.getIdBanque(), req.getNumeroCompte(), 
                req.getNumeroGuichet(), req.getRib(), req.getCarec(), req.getTypeEmp(), 
                req.getTelephone(), req.getSituationMedaille() == null ? 0 : req.getSituationMedaille(), 
                req.getSituationEmploie() == null ? 0 : req.getSituationEmploie(), 
                req.getDateRetourcg()
            );
            
            PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Personnel mis à jour avec succès");
            response.setData(result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du personnel", e);
            PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Supprimer un personnel (soft delete)
     * Endpoint: POST /api/personnels/personnel/supprimerpersonnel
     */
    @PostMapping("/supprimerpersonnel")
    public ResponseEntity<PersonnelVueResponse<Boolean>> deletePersonnel(@RequestBody com.nectux.mizan.hyban.common.dto.IdRequest request) {
        try {
            logger.info("Suppression du personnel ID: {}", request.getId());
            
            Boolean result = personnelService.delete(request.getId());
            
            PersonnelVueResponse<Boolean> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage(result ? "Personnel supprimé avec succès" : "Personnel non trouvé");
            response.setData(result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du personnel", e);
            PersonnelVueResponse<Boolean> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Marquer le départ d'un personnel
     * Endpoint: POST /api/personnels/personnel/departpersonnel
     */
    @PostMapping("/departpersonnel")
    public ResponseEntity<PersonnelVueResponse<PersonnelDTO>> departPersonnel(@RequestBody com.nectux.mizan.hyban.common.dto.IdRequest request) {
        try {
            logger.info("Marquage du départ du personnel ID: {}", request.getId());
            
            PersonnelDTO result = personnelService.depart(request.getId());
            
            PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Départ enregistré avec succès");
            response.setData(result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement du départ", e);
            PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Rechercher un personnel par matricule
     * Endpoint: GET /api/personnels/personnel/recherche/{matricule}
     */
    @GetMapping("/recherche/{matricule}")
    public ResponseEntity<PersonnelVueResponse<Personnel>> searchByMatricule(@PathVariable String matricule) {
        try {
            logger.info("Recherche du personnel par matricule: {}", matricule);
            
            PersonnelDTO result = personnelService.findByMatricules(matricule);
            
            PersonnelVueResponse<Personnel> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Personnel trouvé");
            response.setData(result.getRow());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche par matricule", e);
            PersonnelVueResponse<Personnel> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Activer/Réactiver un personnel
     * Endpoint: POST /api/personnels/personnel/activer
     */
    @PostMapping("/activer")
    public ResponseEntity<PersonnelVueResponse<PersonnelDTO>> activerPersonnel(@RequestBody com.nectux.mizan.hyban.common.dto.IdRequest request) {
        try {
            logger.info("Activation du personnel ID: {}", request.getId());
            
            // Récupérer le personnel existant
            PersonnelDTO existingPersonnel = personnelService.findPersonneldto(request.getId());
            if (existingPersonnel == null) {
                PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
                response.setResult("error");
                response.setMessage("Personnel non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Mettre à jour le statut à true
            PersonnelDTO result = personnelService.save(request.getId(), 0, 0, true);
            
            PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Personnel activé avec succès");
            response.setData(result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de l'activation du personnel", e);
            PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Désactiver un personnel
     * Endpoint: POST /api/personnels/personnel/desactiver
     */
    @PostMapping("/desactiver")
    public ResponseEntity<PersonnelVueResponse<PersonnelDTO>> desactiverPersonnel(@RequestBody com.nectux.mizan.hyban.common.dto.IdRequest request) {
        try {
            logger.info("Désactivation du personnel ID: {}", request.getId());
            
            // Récupérer le personnel existant
            PersonnelDTO existingPersonnel = personnelService.findPersonneldto(request.getId());
            if (existingPersonnel == null) {
                PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
                response.setResult("error");
                response.setMessage("Personnel non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Mettre à jour le statut à false
            PersonnelDTO result = personnelService.save(request.getId(), 0, 0, false);
            
            PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Personnel désactivé avec succès");
            response.setData(result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la désactivation du personnel", e);
            PersonnelVueResponse<PersonnelDTO> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Uploader la photo d'un personnel
     * Endpoint: POST /api/personnels/personnel/upload/photo
     */
    @PostMapping("/upload/photo")
    public ResponseEntity<PersonnelVueResponse<Personnel>> uploadPhoto(
            @RequestParam("photo") MultipartFile photoFile,
            @RequestParam("id") Long personnelId) {
        try {
            logger.info("Upload de photo pour le personnel ID: {}", personnelId);
            
            // Vérifier si le personnel existe
            Personnel personnel = personnelService.findPersonnel(personnelId);
            if (personnel == null) {
                PersonnelVueResponse<Personnel> response = new PersonnelVueResponse<>();
                response.setResult("error");
                response.setMessage("Personnel non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Vérifier si un fichier a été uploadé
            if (photoFile.isEmpty()) {
                PersonnelVueResponse<Personnel> response = new PersonnelVueResponse<>();
                response.setResult("error");
                response.setMessage("Aucun fichier photo fourni");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Créer le répertoire des photos si nécessaire
            String uploadDir = "uploads/photos";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Générer un nom de fichier unique
            String originalFilename = photoFile.getOriginalFilename();
            String extension = "";
            int i = originalFilename.lastIndexOf('.');
            if (i > 0) {
                extension = originalFilename.substring(i);
            }
            String uniqueFilename = "personnel_" + personnelId + "_" + System.currentTimeMillis() + extension;
            
            // Sauvegarder le fichier
            String filePath = uploadDir + "/" + uniqueFilename;
            File dest = new File(filePath);
            photoFile.transferTo(dest);
            
            // Mettre à jour l'URL de la photo dans la base de données
            personnel.setUrlPhoto(filePath);
            Personnel updatedPersonnel = personnelService.save(personnel);
            
            PersonnelVueResponse<Personnel> response = new PersonnelVueResponse<>();
            response.setResult("success");
            response.setMessage("Photo uploadée avec succès");
            response.setData(updatedPersonnel);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de l'upload de photo", e);
            PersonnelVueResponse<Personnel> response = new PersonnelVueResponse<>();
            response.setResult("error");
            response.setMessage("Erreur lors de l'upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Récupérer la photo d'un personnel
     * Endpoint: GET /api/personnels/personnel/photo/{id}
     */
    @GetMapping("/photo/{id}")
    public ResponseEntity<Resource> getPhoto(@PathVariable Long id) {
        try {
            logger.info("Récupération de la photo pour le personnel ID: {}", id);
            
            Personnel personnel = personnelService.findPersonnel(id);
            if (personnel == null || personnel.getUrlPhoto() == null || personnel.getUrlPhoto().isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Path filePath = Paths.get(personnel.getUrlPhoto());
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la photo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<Personnel>> getAllPersonnel() {
        try {
            List<Personnel> personnelList = personnelService.findPersonnels();
            return ResponseEntity.ok(personnelList);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de tout le personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
