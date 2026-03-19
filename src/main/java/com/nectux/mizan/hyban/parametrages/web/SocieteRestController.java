package com.nectux.mizan.hyban.parametrages.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.nectux.mizan.hyban.common.dto.SocieteVueRequest;
import com.nectux.mizan.hyban.common.dto.SocieteVueResponse;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.parametrages.dto.SocieteDTO;
import com.nectux.mizan.hyban.parametrages.dto.SocieteVueDTO;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.entity.Mois;
import com.nectux.mizan.hyban.parametrages.dto.MoisDTO;
import com.nectux.mizan.hyban.parametrages.repository.SocieteRepository;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
import com.nectux.mizan.hyban.parametrages.service.MoisService;
import com.nectux.mizan.hyban.parametrages.service.BanqueService;

@RestController
@RequestMapping("/api/parametrages/societes")
@CrossOrigin(origins = {"http://localhost:7153", "http://localhost:7200", "http://localhost:4200", "http://127.0.0.1:3000"}, 
             allowedHeaders = "*", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH},
             allowCredentials = "true")
public class SocieteRestController {

    private static final Logger logger = LoggerFactory.getLogger(SocieteRestController.class);
    private static int maxFileSize = 1024 * 1024 * 2; // 2MB
    private static String imageType = new String(".jpg");

    @Autowired
    private SocieteService societeService;
    @Autowired
    private SocieteRepository societeRepository;
    @Autowired
    private MoisService moisService;
    @Autowired
    private BanqueService banqueService;

    public SocieteDTO excelAttribdto = null;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("SocieteRestController is working!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocieteVueDTO> getSociete(@PathVariable Long id) {
        try {
            Societe societe = societeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Societe not found for id " + id));
            SocieteVueDTO societeVueDTO = new SocieteVueDTO(societe);
            return ResponseEntity.ok(societeVueDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la société", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/list")
    public ResponseEntity<SocieteVueResponse<Object>> getSocieteList(@RequestBody SocieteVueRequest request) {
        try {
            Integer offset = request.getOffset() == null ? 0 : request.getOffset();
            Integer limit = request.getLimit() == null ? 10 : request.getLimit();
            String search = request.getSearch();

            PageRequest pageRequest = PageRequest.of(offset / limit, limit, Direction.DESC, "id");
            SocieteDTO societeDTO;
            
            if (search == null || search.trim().isEmpty()) {
                societeDTO = societeService.loadSociete(pageRequest);
            } else {
                societeDTO = societeService.loadSociete(pageRequest, search);
            }

            // Convertir les entités Societe en SocieteVueDTO pour la vue
            List<SocieteVueDTO> societeVueDTOs = societeDTO.getRows().stream()
                .map(societe -> new SocieteVueDTO((Societe) societe))
                .collect(Collectors.toList());

            SocieteVueResponse<Object> response = new SocieteVueResponse<>();
            response.setRows(societeVueDTOs.stream().map(dto -> (Object) dto).collect(Collectors.toList()));
            response.setTotal((int) societeDTO.getTotal());
            response.setResult("success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des sociétés", e);
            SocieteVueResponse<Object> response = new SocieteVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<SocieteVueResponse<Societe>> saveSociete(@RequestBody SocieteVueRequest request) {
        try {
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            
            SocieteDTO societeDTO = societeService.save(
                request.getId(),
                request.getRaisonSociale(),
                request.getSigle(),
                request.getActivitepp(),
                request.getAdresse(),
                request.getFormjuri(),
                request.getTelephone(),
                request.getBp(),
                request.getCommune(),
                request.getQuartier(),
                request.getRue(),
                request.getLot(),
                request.getSectpartiell(),
                request.getCentreImpot(),
                request.getCodeEts(),
                request.getCodeActivite(),
                request.getCodeEmployeur(),
                request.getCompteContribuable(),
                request.getNomcomptable(),
                request.getTelcomptable(),
                request.getAdrcomptable(),
                request.getTxprest(),
                request.getTxacctr(),
                request.getTxretraite(),
                request.getTxgratif(),
                request.getPrincipale() != null && request.getPrincipale() ? 1L : 0L
            );

            if (societeDTO.getMessage().contains("succes")) {
                response.setResult("success");
                response.setMessage("Société créée avec succès");
            } else {
                response.setResult("error");
                response.setMessage(societeDTO.getMessage());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la société", e);
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<SocieteVueResponse<Societe>> updateSociete(@RequestBody SocieteVueRequest request) {
        try {
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            
            SocieteDTO societeDTO = societeService.save(
                request.getId(),
                request.getRaisonSociale(),
                request.getSigle(),
                request.getActivitepp(),
                request.getAdresse(),
                request.getFormjuri(),
                request.getTelephone(),
                request.getBp(),
                request.getCommune(),
                request.getQuartier(),
                request.getRue(),
                request.getLot(),
                request.getSectpartiell(),
                request.getCentreImpot(),
                request.getCodeEts(),
                request.getCodeActivite(),
                request.getCodeEmployeur(),
                request.getCompteContribuable(),
                request.getNomcomptable(),
                request.getTelcomptable(),
                request.getAdrcomptable(),
                request.getTxprest(),
                request.getTxacctr(),
                request.getTxretraite(),
                request.getTxgratif(),
                request.getPrincipale() != null && request.getPrincipale() ? 1L : 0L
            );

            if (societeDTO.getMessage().contains("succes")) {
                response.setResult("success");
                response.setMessage("Société mise à jour avec succès");
            } else {
                response.setResult("error");
                response.setMessage(societeDTO.getMessage());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la société", e);
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<SocieteVueResponse<Societe>> deleteSociete(@RequestBody IdRequest request) {
        try {
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            Boolean result = societeService.delete(request.getId());
            
            if (result) {
                response.setResult("success");
                response.setMessage("Société supprimée avec succès");
            } else {
                response.setResult("error");
                response.setMessage("Erreur lors de la suppression de la société");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la société", e);
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/toggle-principale")
    public ResponseEntity<SocieteVueResponse<Societe>> togglePrincipale(@RequestBody IdRequest request) {
        try {
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            
            // Récupérer la société
            Societe societe = societeRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Societe not found for id " + request.getId()));
            
            // Inverser le statut principale
            Long nouvelleGratification = (societe.getGratification() != null && societe.getGratification() == 1L) ? 0L : 1L;
            
            societeService.save(
                societe.getId(),
                societe.getRaisonsoc(),
                societe.getSigle(),
                societe.getActivitepp(),
                societe.getAdress(),
                societe.getFormjuri(),
                societe.getTelephone(),
                societe.getBp(),
                societe.getCommune(),
                societe.getQuartier(),
                societe.getRue(),
                societe.getLot(),
                societe.getSectpartiell(),
                societe.getCentreImpot(),
                societe.getCodeEts(),
                societe.getCodeActivite(),
                societe.getCodeEmployeur(),
                societe.getCpteContrib(),
                societe.getNomcomptable(),
                societe.getTelcomptable(),
                societe.getAdrcomptable(),
                societe.getTxprest(),
                societe.getTxacctr(),
                societe.getTxretraite(),
                societe.getTxgratif(),
                nouvelleGratification
            );

            response.setResult("success");
            response.setMessage("Statut de société principale modifié avec succès");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la modification du statut principale", e);
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/upload-logo")
    public ResponseEntity<SocieteVueResponse<Societe>> uploadLogo(MultipartHttpServletRequest uploadfile, HttpServletRequest request) {
        try {
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            
            String uploadPath = request.getSession().getServletContext().getClass().getResource("") + "\\static\\logo\\";
            System.out.println(">>> CHEMIN UPLOAD >>>" + uploadPath);
            
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            Iterator<String> itr = uploadfile.getFileNames();
            MultipartFile mpf = uploadfile.getFile(itr.next());
            
            String filename = mpf.getOriginalFilename();
            String directory = uploadPath;
            String filepath = Paths.get(directory, filename).toString();
            
            List<Societe> malist = societeService.findtsmois();
            if (!malist.isEmpty()) {
                Societe mysociete = malist.get(0);
                mysociete.setUrlLogo("/static/logo/" + filename);
                societeService.save(mysociete);
            }
            
            // Save the file locally
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(mpf.getBytes());
            stream.close();
            
            System.out.println("PERIODE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + filename);
            
            response.setResult("success");
            response.setMessage("Logo téléchargé avec succès");

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Erreur lors du téléchargement du logo", e);
            SocieteVueResponse<Societe> response = new SocieteVueResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<SocieteVueDTO>> getAllSocietes() {
        try {
            List<Societe> societes = societeService.findtsmois();
            List<SocieteVueDTO> societeVueDTOs = societes.stream()
                .map(societe -> new SocieteVueDTO(societe))
                .collect(Collectors.toList());
            return ResponseEntity.ok(societeVueDTOs);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de toutes les sociétés", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/months")
    public ResponseEntity<MoisDTO> getMonths() {
        try {
            MoisDTO moisDTO = new MoisDTO();
            List<Mois> malist = moisService.findtsmois();
            moisDTO.setRows(malist);
            moisDTO.setTotal(malist.size());
            return ResponseEntity.ok(moisDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des mois", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/banks")
    public ResponseEntity<Object> getBanks() {
        try {
            return ResponseEntity.ok(banqueService.getBanques());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des banques", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
