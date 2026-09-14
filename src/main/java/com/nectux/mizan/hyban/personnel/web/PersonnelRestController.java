package com.nectux.mizan.hyban.personnel.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nectux.mizan.hyban.paie.entity.Conge;
import com.nectux.mizan.hyban.paie.service.BulletinPaieService;
import com.nectux.mizan.hyban.paie.service.CongeService;
import com.nectux.mizan.hyban.parametrages.entity.Exercice;
import com.nectux.mizan.hyban.parametrages.entity.Mois;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.PlanningConge;
import com.nectux.mizan.hyban.parametrages.repository.ExerciceRepository;
import com.nectux.mizan.hyban.parametrages.repository.PeriodePaieRepository;
import com.nectux.mizan.hyban.parametrages.repository.TypeContratRepository;
import com.nectux.mizan.hyban.parametrages.service.*;
import com.nectux.mizan.hyban.personnel.entity.Service;
import com.nectux.mizan.hyban.personnel.repository.ContratPersonnelRepository;
import com.nectux.mizan.hyban.personnel.repository.DocumentTypeRepository;
import com.nectux.mizan.hyban.personnel.repository.PersonnelRepository;
import com.nectux.mizan.hyban.personnel.repository.StorageLocationRepository;
import com.nectux.mizan.hyban.personnel.service.ContratPersonnelService;
import com.nectux.mizan.hyban.personnel.service.FonctionService;
import com.nectux.mizan.hyban.personnel.service.ServiceService;
import com.nectux.mizan.hyban.rh.absences.service.AbsencesService;
import com.nectux.mizan.hyban.rh.carriere.repository.AffectationRepository;
import com.nectux.mizan.hyban.rh.carriere.repository.SiteWorkRepository;
import com.nectux.mizan.hyban.rh.carriere.service.PosteService;
import com.nectux.mizan.hyban.rh.carriere.service.PromotionService;
import com.nectux.mizan.hyban.rh.carriere.service.SanctionService;
import com.nectux.mizan.hyban.utils.DifferenceDate;
import com.nectux.mizan.hyban.utils.MethodsShared;
import com.nectux.mizan.hyban.utils.PrintLs;
import com.nectux.mizan.hyban.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

import com.nectux.mizan.hyban.common.dto.PersonnelVueRequest;
import com.nectux.mizan.hyban.common.dto.PersonnelVueResponse;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.personnel.dto.PersonnelDTO;
import com.nectux.mizan.hyban.personnel.dto.ContratPersonnelDTO;
import com.nectux.mizan.hyban.personnel.dto.EditerPersonnelRequest;
import com.nectux.mizan.hyban.personnel.entity.Personnel;
import com.nectux.mizan.hyban.personnel.service.PersonnelService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/personnels/personnel")
@PreAuthorize("hasAnyAuthority('EMPLOYEE_READ', 'EMPLOYEE_CREATE', 'EMPLOYEE_UPDATE', 'EMPLOYEE_DELETE', 'EMPLOYEE_EXPORT') or hasRole('ADMIN')")

//@CrossOrigin(origins = {"http://localhost:7153", "http://192.168.1.4:7153", "http://192.168.1.4:8080", "http://192.168.1.4:7156", "http://192.168.1.4:7157", "http://192.168.1.4:7158", "http://192.168.1.4:7159", "http://192.168.1.4:7160", "http://192.168.1.4:7161", "http://192.168.1.4:7162", "http://192.168.1.4:7163", "http://192.168.1.4:7164", "http://192.168.1.4:7165", "http://192.168.1.4:7166", "http://192.168.1.4:7167", "http://192.168.1.4:7168", "http://192.168.1.4:7169", "http://192.168.1.4:7170", "http://192.168.1.4:7171", "http://192.168.1.4:7172", "http://192.168.1.4:7173", "http://192.168.1.4:7174", "http://192.168.1.4:7175", "http://192.168.1.4:7176", "http://192.168.1.4:7177", "http://192.168.1.4:7178", "http://192.168.1.4:7179", "http://192.168.1.4:7180", "http://192.168.1.4:7181", "http://192.168.1.4:7182", "http://192.168.1.4:7183", "http://192.168.1.4:7184", "http://192.168.1.4:7185", "http://192.168.1.4:7186", "http://192.168.1.4:7187", "http://192.168.1.4:7188", "http://192.168.1.4:7189", "http://192.168.1.4:7190", "http://192.168.1.4:7191", "http://192.168.1.4:7192", "http://192.168.1.4:7193", "http://192.168.1.4:7194", "http://192.168.1.4:7195", "http://192.168.1.4:7196", "http://192.168.1.4:7197", "http://192.168.1.4:7198", "http://192.168.1.4:7199"},
//        allowCredentials = "true"
//)

public class PersonnelRestController {

    private static final Logger logger = LoggerFactory.getLogger(PersonnelRestController.class);

    @Autowired
    private PersonnelService personnelService;
    @Autowired
    private SocieteService societeService;



    @Autowired private ExerciceRepository exerciceRepository;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private AffectationRepository affectationRepository;
    @Autowired
    private StorageLocationRepository storageLocationRepository;
    @Autowired private PersonnelRepository personnelRepository;
    @Autowired private SiteWorkRepository siteWorkRepository;
    @Autowired private MoisService moisService;
    @Autowired private BulletinPaieService bulletinPaieService;
    @Autowired private ServiceService serviceService;
    @Autowired private CongeService congeService;
    @Autowired private PlanningCongeService planningCongeService;
    @Autowired private ContratPersonnelService contratPersonnelService;
    @Autowired private ContratPersonnelRepository contratPersonnelRepository;
    @Autowired private TypeContratRepository typeContratRepository;
    //Carriere
    @Autowired private SanctionService sanctionService;
    @Autowired private FonctionService fonctionService;
    @Autowired private PosteService posteService;
    @Autowired private PromotionService promotionService;
    @Autowired private PeriodePaieService periodePaieService;
    @Autowired private BanqueService banqueService;
    @Autowired private AbsencesService absenceService;
    @Autowired private PeriodePaieRepository periodePaieRepository;


    public MethodsShared methodsShared;
    public DifferenceDate differenceDate;
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

            PageRequest pageRequest = PageRequest.of(offset , limit, Direction.ASC, "nom", "prenom");
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
            
            // Récupérer tous les personnels avec les filtres (avec tri par nom, prénom)
            PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE, Direction.ASC, "nom", "prenom");
            PersonnelDTO personnelDTO = personnelService.findAllfilter(filters, pageRequest);
            
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
                req.getDateFin(), BigDecimal.valueOf(req.getSalaireNet()), BigDecimal.valueOf(req.getIndemnitelogement()),
                req.getModePaiement(), req.getIdBanque(), req.getNumeroCompte(), 
                req.getNumeroGuichet(), req.getRib(), 
                req.getAncienneteInitial() == null ? 0 : req.getAncienneteInitial(), 
                req.getCarec(), req.getTypeEmp(), req.getTelephone(), 
                req.getSituationMedaille() == null ? 0 : req.getSituationMedaille(), 
                req.getSituationEmploie(),
                req.getDateRetourcg(), BigDecimal.valueOf(req.getIndemniteRespons()), BigDecimal.valueOf(req.getIndemniteRepresent()),
                    BigDecimal.valueOf(req.getIndemniteTransport()), BigDecimal.valueOf(req.getSursalaire())
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
                req.getSituationEmploie(),
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

    /**
     * Modification rapide d'un personnel : situation matrimoniale, nombre d'enfants, statut (actif / sommeil).
     *
     * REST Endpoint : PUT /api/personnels/personnel/editerpersonnel
     *
     * Body JSON attendu :
     * {
     *   "idPersonnel": 1,
     *   "situationMatrimoniale": 1,   // 1=MARIE, 2=CELIBATAIRE, 3=DIVORCE, 4=VEUF
     *   "nombreEnfant": 2,
     *   "statut": true                  // true=actif, false=en sommeil
     * }
     */
    @PutMapping(value = "/editerpersonnel", consumes = MediaType.APPLICATION_JSON_VALUE,
                                            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonnelDTO> updatePersonnel(@RequestBody EditerPersonnelRequest request) {
        try {
            if (request == null || request.getIdPersonnel() == null) {
                return ResponseEntity.badRequest().build();
            }

            int situationMatrimoniale = request.getSituationMatrimoniale() == null ? 0 : request.getSituationMatrimoniale();
            int nombreEnfant = request.getNombreEnfant() == null ? 0 : request.getNombreEnfant();
            Boolean statut = request.getStatut();

            PersonnelDTO dto = personnelService.save(
                    request.getIdPersonnel(),
                    situationMatrimoniale,
                    nombreEnfant,
                    statut
            );

            if (dto == null || "echec".equals(dto.getResult())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Erreur lors de la modification du personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @RequestMapping(value = "/effectifPersonnel", method = RequestMethod.GET)
    @ResponseBody
    public String deleteUse(@RequestParam(value="id", required=false) Long id, ModelMap modelMap) throws IOException {
        Exercice anneeRecup = new Exercice();
        if(id != null){
            try {
                anneeRecup = exerciceRepository.findById(id) .orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(" annee :::::: "+anneeRecup.toString());
        List<PrintLs> listPrintDTO = new ArrayList<PrintLs>();
        if(anneeRecup.getId() != null){
            Integer anneeEnCours = Integer.valueOf(anneeRecup.getAnnee());
            for (int i = 0; i < 5; i++) {
                //System.out.println(" annee :::::: "+anneeEnCours);

                String annee = String.valueOf(anneeEnCours);
                java.sql.Date dateAnDeb = null;
                java.sql.Date dateAnFin = null;
                Date dateAnDeb1 = null;
                Date dateAnFin1 = null;
                try {
                    dateAnDeb1 = Utils.stringToDate("01/01/"+annee, "dd/MM/yyyy");
                    dateAnFin1 = Utils.stringToDate("31/12/"+annee, "dd/MM/yyyy");
                    dateAnDeb = new java.sql.Date(dateAnDeb1.getTime());
                    dateAnFin = new java.sql.Date(dateAnFin1.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<Personnel> listPersonnelHomme = new ArrayList<Personnel>();
                try{
                    listPersonnelHomme = personnelService.RechercherListPersonnelParAnnee(dateAnDeb, dateAnFin, "Masculin");
                    System.out.println("list Personnel : "+listPersonnelHomme.size());
                } catch(Exception ex){
                    logger.error(ex.getMessage());
                    logger.error(Arrays.toString(ex.getStackTrace()));
                    logger.error("une erreur a ete dectectee lors de la suppression du categorie Personnel");
                }

                List<Personnel> listPersonnelFemme = new ArrayList<Personnel>();
                try{
                    listPersonnelFemme = personnelService.RechercherListPersonnelParAnnee( dateAnDeb, dateAnFin, "Feminin");
                    System.out.println("list Personnel femme : "+listPersonnelFemme.size());

                } catch(Exception ex){

                }
                PrintLs printDTO = new PrintLs();
                printDTO.setI1(listPersonnelHomme.size());
                printDTO.setS1(annee);
                printDTO.setTitle1("Homme");
                printDTO.setI2(listPersonnelFemme.size());
                printDTO.setS2(annee);
                printDTO.setTitle2("Femme");
                listPrintDTO.add(printDTO);

                anneeEnCours = anneeEnCours - 1;
            }

        }
        return toJson(listPrintDTO);
    }


    @RequestMapping(value = "/effectifparsite", method = RequestMethod.GET)
    @ResponseBody
    public String effectifUse(@RequestParam(value="id", required=false) Long id, ModelMap modelMap) throws IOException {


        Exercice anneeRecup = new Exercice();
        if (id != null) {
            try {
                anneeRecup = exerciceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null) {
            return toJson(Collections.emptyList());
        }
        List<PrintLs> effectifParSite = bulletinPaieService.calculerEffectifParSiteAlaPaie(periode);
        List<PrintLs> masseSalarialeSite = bulletinPaieService.calculerMasseSalarialeParSite(periode);
        System.out.println(" annee :::::: " + anneeRecup.toString());
        Map<String, PrintLs> merged = new HashMap<>();
        for (PrintLs eff : effectifParSite) {
            PrintLs item = new PrintLs();
            item.setS1(eff.getS1()); // le site
            item.setI1(eff.getI1()); // l'effectif
            merged.put(eff.getS1(), item);
        }

        for (PrintLs masse : masseSalarialeSite) {
            PrintLs item = merged.computeIfAbsent(masse.getS1(), k -> new PrintLs());
            item.setS1(masse.getS1()); // le site
            item.setValue1(masse.getValue1()); // la masse salariale
        }

        return toJson(new ArrayList<>(merged.values()));
    }


    @RequestMapping(value = "/stat/conge", method = RequestMethod.GET)
    @ResponseBody
    public String statConge( @RequestParam(value="id", required=false) Long aid, ModelMap modelMap, Principal principal) throws IOException {


        List<PrintLs> listPrintDTO = new ArrayList<PrintLs>();

        Exercice annee = new Exercice();
        if(aid != null){
            try {
                annee = exerciceRepository.findById(aid) .orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + aid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(" annee :::::: "+annee.toString());
        if(annee.getId() != null){

            List<Mois> listMois = new ArrayList<Mois>();
            try {
                listMois = moisService.findtsmois();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Mois mois : listMois) {

                //Recherche de la liste des congées d'un mois
                List<Conge> listconge = new ArrayList<Conge>();
                try {
                    listconge = congeService.rechercherByAgenceMoisAnnee( mois, annee);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("nb conge mois : "+mois.getMois()+" annee : "+annee.getAnnee() +" ::::: "+listconge.size());

                //Recherche de la liste des plagning de congé d'un mois
                List<PlanningConge> listPlanning = new ArrayList<PlanningConge>();
                try {
                    listPlanning = planningCongeService.rechercherByAgenceMoisAnnee(mois, annee);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("nb conge mois : "+mois.getMois()+" annee : "+annee.getAnnee() +" ::::: "+listconge.size());

                PrintLs printDTO = new PrintLs();
                printDTO.setI1(listconge.size());
                printDTO.setS1(mois.getMois());
                printDTO.setTitle1(annee.getAnnee());
                printDTO.setI2(listPlanning.size());
                printDTO.setS2(mois.getMois());
                printDTO.setTitle2(annee.getAnnee());

                listPrintDTO.add(printDTO);
            }

        }


        //return new ModelAndView("redirect:../../../rhp/personnel/processing/listpersonnal?uid="+utilisateurCourant.getUid());
        return toJson(listPrintDTO);
    }

    @RequestMapping(value = "/stat/effectifPersonnelRetraite", method = RequestMethod.GET)
    @ResponseBody
    public String SatRetraite( @RequestParam(value="id", required=false) Long aid, ModelMap modelMap) throws IOException {



        Exercice  anneeRecup = new Exercice();
        if(aid != null){
            try {
                anneeRecup = exerciceRepository.findById(aid) .orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + aid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(" annee :::::: "+anneeRecup.toString());

        List<PrintLs> listPrintDTO = new ArrayList<PrintLs>();

        if(anneeRecup.getId() != null){

            Integer anneeEnCours = Integer.valueOf(anneeRecup.getAnnee());

            Integer nbHommeRetraitAn1 = 0;
            Integer nbFemmeRetraitAn1 = 0;

            Integer nbHommeRetraitAn2 = 0;
            Integer nbFemmeRetraitAn2 = 0;

            Integer nbHommeRetraitAn3 = 0;
            Integer nbFemmeRetraitAn3 = 0;

            Integer nbHommeRetraitAn4 = 0;
            Integer nbFemmeRetraitAn4 = 0;

            Integer nbHommeRetraitAn5 = 0;
            Integer nbFemmeRetraitAn5 = 0;

            String annee = String.valueOf(anneeEnCours);
            java.sql.Date dateAnDeb = null;	dateAnDeb = null;
            try {
                Date dateAnDeb11 = null;

                dateAnDeb11= Utils.stringToDate("01/01/"+annee, "dd/MM/yyyy");
                dateAnDeb =new java.sql.Date(dateAnDeb11.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<Personnel> listPersonnelHomme = new ArrayList<Personnel>();
            try{
                listPersonnelHomme = personnelService.RechercherListPersonnelParAnnee( "Masculin");
                System.out.println("list Personnel homme : "+listPersonnelHomme.size());
            } catch(Exception ex){
                logger.error(ex.getMessage());
            }
            for (Personnel personnel : listPersonnelHomme) {
                Date datNaiss = personnel.getDateNaissance();
                double age = differenceDate.valAge(dateAnDeb, datNaiss);
                System.out.println("Age de homme est : "+age);

                if(age > 59){
                    nbHommeRetraitAn1 = nbHommeRetraitAn1 + 1;
                }else{

                    if(age > 58){
                        nbHommeRetraitAn2 = nbHommeRetraitAn2 + 1;
                    }else{

                        if(age > 57){
                            nbHommeRetraitAn3 = nbHommeRetraitAn3 + 1;
                        }else{
                            if(age > 56){
                                nbHommeRetraitAn4 = nbHommeRetraitAn4 + 1;
                            }else{
                                if(age > 55){
                                    nbHommeRetraitAn5 = nbHommeRetraitAn5 + 1;
                                }
                            }
                        }
                    }
                }

            }

            List<Personnel> listPersonnelFemme = new ArrayList<Personnel>();
            try{
                listPersonnelFemme = personnelService.RechercherListPersonnelParAnnee( "Feminin");
                System.out.println("list Personnel femme : "+listPersonnelFemme.size());
            } catch(Exception ex){
                logger.error(ex.getMessage());
            }
            for (Personnel personnel : listPersonnelFemme) {
                Date datNaiss = personnel.getDateNaissance();
                double age = differenceDate.valAge(dateAnDeb, datNaiss);
                System.out.println("Age de femme est : "+age);

                if(age > 59){
                    nbFemmeRetraitAn1 = nbFemmeRetraitAn1 + 1;
                }else{

                    if(age > 58){
                        nbFemmeRetraitAn2 = nbFemmeRetraitAn2 + 1;
                    }else{

                        if(age > 57){
                            nbFemmeRetraitAn3 = nbFemmeRetraitAn3 + 1;
                        }else{
                            if(age > 56){
                                nbFemmeRetraitAn4 = nbFemmeRetraitAn4 + 1;
                            }else{
                                if(age > 55){
                                    nbFemmeRetraitAn5 = nbFemmeRetraitAn5 + 1;
                                }
                            }
                        }
                    }
                }

            }

            PrintLs printDTO = new PrintLs();
            printDTO.setI1(nbHommeRetraitAn1);
            printDTO.setS1(annee);
            printDTO.setTitle1("Homme");
            printDTO.setI2(nbFemmeRetraitAn1);
            printDTO.setS2(annee);
            printDTO.setTitle2("Femme");

            listPrintDTO.add(printDTO);

            PrintLs printDTO2 = new PrintLs();
            printDTO2.setI1(nbHommeRetraitAn2);
            printDTO2.setS1(String.valueOf(anneeEnCours+1));
            printDTO2.setTitle1("Homme");
            printDTO2.setI2(nbFemmeRetraitAn2);
            printDTO2.setS2(String.valueOf(anneeEnCours+1));
            printDTO2.setTitle2("Femme");

            listPrintDTO.add(printDTO2);

            PrintLs printDTO3 = new PrintLs();
            printDTO3.setI1(nbHommeRetraitAn3);
            printDTO3.setS1(String.valueOf(anneeEnCours+2));
            printDTO3.setTitle1("Homme");
            printDTO3.setI2(nbFemmeRetraitAn3);
            printDTO3.setS2(String.valueOf(anneeEnCours+2));
            printDTO3.setTitle2("Femme");

            listPrintDTO.add(printDTO3);

            PrintLs printDTO4 = new PrintLs();
            printDTO4.setI1(nbHommeRetraitAn4);
            printDTO4.setS1(String.valueOf(anneeEnCours+3));
            printDTO4.setTitle1("Homme");
            printDTO4.setI2(nbFemmeRetraitAn4);
            printDTO4.setS2(String.valueOf(anneeEnCours+3));
            printDTO4.setTitle2("Femme");

            listPrintDTO.add(printDTO4);

            PrintLs printDTO5 = new PrintLs();
            printDTO5.setI1(nbHommeRetraitAn5);
            printDTO5.setS1(String.valueOf(anneeEnCours+4));
            printDTO5.setTitle1("Homme");
            printDTO5.setI2(nbFemmeRetraitAn5);
            printDTO5.setS2(String.valueOf(anneeEnCours+4));
            printDTO5.setTitle2("Femme");

            listPrintDTO.add(printDTO5);

        }



        //return new ModelAndView("redirect:../../../rhp/personnel/processing/listpersonnal?uid="+utilisateurCourant.getUid());
        return toJson(listPrintDTO);
    }
    @RequestMapping(value = "/stat/moyenAge", method = RequestMethod.GET)
    @ResponseBody
    public String statMoyenneAge( @RequestParam(value="aid", required=false) Long aid, ModelMap modelMap, Principal principal) throws IOException {


        Exercice anneeRecup = new Exercice();
        if(aid != null){
            try {
                anneeRecup = exerciceRepository.findById(aid) .orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + aid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(" annee :::::: "+anneeRecup.toString());

        List<PrintLs> listPrintDTO = new ArrayList<PrintLs>();

        if(anneeRecup.getId() != null){

            Integer anneeEnCours = Integer.valueOf(anneeRecup.getAnnee());
            String annee = String.valueOf(anneeEnCours);
            java.sql.Date dateAnDeb = null;

            try {
                Date dateAnDeb11 = null;

                dateAnDeb11= Utils.stringToDate("01/01/"+annee, "dd/MM/yyyy");
                dateAnDeb =new java.sql.Date(dateAnDeb11.getTime());
                //dateAnDeb = Utils.stringToDate().stringToDateSql("01/01/"+annee, "dd/MM/yyyy");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Recherche du nombre de direction
            List<Service> listDirection = new ArrayList<Service>();
            try {
                listDirection = serviceService.findByTypeServiceId(1L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("####### Nb direction :::::: "+listDirection.size());

            for (Service direction : listDirection) {
                //Recherche de la liste du personnel pour une direction
                List<Personnel> listPers = new ArrayList<Personnel>();
                try {
                    listPers = personnelService.RechercherListPersonnelParDirection(direction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("####### Nb personnel par direction :::::: "+direction.getLibelle()+" nbre :"+listPers.size());

                double somAge = 0;
                //calculer l'age de chaque personnel
                for (Personnel personnel : listPers) {
                    Date datNaiss = personnel.getDateNaissance();
                    double age = differenceDate.valAge(dateAnDeb, datNaiss);
                    System.out.println("Age de homme est : "+age);
                    somAge = somAge + age;
                }
                double moyAge = 0;

                if(listPers.size() != 0)
                    moyAge = somAge / listPers.size();

                PrintLs printDTO = new PrintLs();
                printDTO.setI1((int)moyAge);
                printDTO.setS1(anneeRecup.getAnnee());
                printDTO.setTitle1(direction.getLibelle());

                listPrintDTO.add(printDTO);
            }


        }

        //return new ModelAndView("redirect:../../../rhp/personnel/processing/listpersonnal?uid="+utilisateurCourant.getUid());
        return toJson(listPrintDTO);
    }


    private String toJson(List<PrintLs> listPrintDTO) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String value = mapper.writeValueAsString(listPrintDTO);
            return value;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
