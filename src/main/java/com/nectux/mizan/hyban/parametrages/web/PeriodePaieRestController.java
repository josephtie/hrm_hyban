package com.nectux.mizan.hyban.parametrages.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.PeriodePaieRequest;
import com.nectux.mizan.hyban.common.dto.PeriodePaieResponse;
import com.nectux.mizan.hyban.paie.entity.BulletinPaie;
import com.nectux.mizan.hyban.paie.entity.PrimePersonnel;
import com.nectux.mizan.hyban.paie.repository.PrimePersonnelRepository;
import com.nectux.mizan.hyban.paie.service.BulletinPaieService;
import com.nectux.mizan.hyban.parametrages.dto.PeriodePaieDTO;
import com.nectux.mizan.hyban.parametrages.entity.Exercice;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.repository.PeriodePaieRepository;
import com.nectux.mizan.hyban.parametrages.service.ExerciceService;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/parametrages/periodes")
public class PeriodePaieRestController {

    private static final Logger logger = LoggerFactory.getLogger(PeriodePaieRestController.class);

    // @Autowired
    // private UtilisateurService userService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private BulletinPaieService bulletinPaieService;
    @Autowired
    private PeriodePaieRepository periodePaieRepository;
    @Autowired
    private PrimePersonnelRepository primePersonnelRepository;
    @Autowired
    private ExerciceService exerciceService;
    @Autowired
    private SocieteService societeService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/view")
    public ResponseEntity<String> viewPeriode() {
        try {
            return ResponseEntity.ok("Periode de paie view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<PeriodePaieResponse<?>> getPeriodeList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / limit, limit, Direction.DESC, "id");
            PeriodePaieDTO periodeDTO;
            
            if (search == null || search.trim().isEmpty()) {
                periodeDTO = periodePaieService.loadPeriodePaie(pageRequest);
            } else {
                periodeDTO = periodePaieService.loadPeriodePaie(pageRequest, search);
            }
            
            PeriodePaieResponse<Object> response = new PeriodePaieResponse<>();
            response.setRows(Collections.singletonList(periodeDTO.getRows()));
            response.setTotal((int) periodeDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des périodes", e);
            PeriodePaieResponse<Object> response = new PeriodePaieResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<PeriodePaieDTO> savePeriode(@RequestBody PeriodePaieRequest request) {
        try {
            PeriodePaieDTO result = periodePaieService.genererPeriode(request.getMois(), request.getLibelle());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la période", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<PeriodePaieDTO> getPeriodeActive() {
        try {
            PeriodePaieDTO periodeDTO = new PeriodePaieDTO();
            List<PeriodePaie> listperiod = new ArrayList<PeriodePaie>();
            PeriodePaie maperiode = periodePaieService.findPeriodeactive();
            
            if (maperiode != null) {
                listperiod.add(maperiode);
                periodeDTO.setRow(maperiode);
                periodeDTO.setRows(listperiod);
                periodeDTO.setTotal(1L);
                periodeDTO.setResult("success");
            }
            
            return ResponseEntity.ok(periodeDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la période active", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cloturer")
    public ResponseEntity<PeriodePaieDTO> cloturerPeriode(@RequestBody PeriodePaieRequest request) {
        try {
            PeriodePaieDTO periodeDTO = new PeriodePaieDTO();
            PeriodePaie maperiode = periodePaieRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Periode not found for id " + request.getId()));
            PeriodePaie maperiodenew = periodePaieRepository.findById(request.getId() + 1L)
                .orElseThrow(() -> new EntityNotFoundException("Nouvelle periode not found for id " + (request.getId() + 1L)));
            
            maperiodenew.setCloture(false);
            maperiodenew = periodePaieRepository.save(maperiodenew);
            maperiode.setCloture(true);
            maperiode = periodePaieRepository.save(maperiode);
            
            if (maperiode != null) {
                List<BulletinPaie> listbulletin = new ArrayList<BulletinPaie>();
                List<PrimePersonnel> listprimePers = new ArrayList<PrimePersonnel>();
                List<PrimePersonnel> listprimePersNew = new ArrayList<PrimePersonnel>();
                
                listprimePers = primePersonnelRepository.findByPeriodePaieId(maperiode.getId());
                listbulletin = bulletinPaieService.rechercherBulletinMoisCalculer(maperiode, true);
                
                if (listbulletin.size() > 0) {
                    for (int k = 0; k < listbulletin.size(); k++) {
                        BulletinPaie monBull = new BulletinPaie();
                        monBull = listbulletin.get(k);
                        monBull.setCloture(true);
                        bulletinPaieService.save(monBull);
                    }
                    
                    for (PrimePersonnel myprime : listprimePers) {
                        if (myprime.getPrime().getPermanent() == true) {
                            PrimePersonnel cloned = new PrimePersonnel();
                            cloned.setPrime(myprime.getPrime());
                            cloned.setContratPersonnel(myprime.getContratPersonnel());
                            cloned.setMontant(myprime.getMontant());
                            cloned.setPeriode(maperiodenew);
                            listprimePersNew.add(cloned);
                        }
                    }
                    
                    primePersonnelRepository.saveAll(listprimePersNew);
                    
                    if (maperiode.getMois().getId() == 12L) {
                        Exercice monexo = exerciceService.findExo(maperiode.getAnnee().getId());
                        Exercice monexonew = exerciceService.findExo(maperiode.getAnnee().getId() + 1L);
                        if (monexo != null) {
                            monexo.setCloture(true);
                            monexo = exerciceService.save(monexo);
                        }
                        monexonew.setCloture(false);
                        monexonew = exerciceService.save(monexonew);
                    }
                    
                    periodeDTO.setResult("success");
                } else {
                    periodeDTO.setResult("error");
                }
            }
            
            periodeDTO.setRow(maperiode);
            return ResponseEntity.ok(periodeDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la clôture de la période", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cloturees")
    public ResponseEntity<List<PeriodePaie>> listPeriodeCloturee() {
        try {
            List<PeriodePaie> periodes = periodePaieService.findPeriodeCloture();
            return ResponseEntity.ok(periodes);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des périodes clôturées", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/ouvertes")
    public ResponseEntity<List<PeriodePaie>> listPeriodeOuverte() {
        try {
            List<PeriodePaie> periodes = periodePaieService.findPeriodeOuverte();
            return ResponseEntity.ok(periodes);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des périodes ouvertes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/all")
    public ResponseEntity<List<PeriodePaie>> listPeriodeAll() {
        try {
            List<PeriodePaie> periodes = periodePaieService.listAllperiode();
            return ResponseEntity.ok(periodes);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de toutes les périodes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
