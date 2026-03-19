package com.nectux.mizan.hyban.paie.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.PrimePersonnelRequest;
import com.nectux.mizan.hyban.common.dto.PrimePersonnelResponse;
import com.nectux.mizan.hyban.paie.dto.PretDTO;
import com.nectux.mizan.hyban.paie.dto.PrimePersonnelDTO;
import com.nectux.mizan.hyban.paie.entity.Pret;
import com.nectux.mizan.hyban.paie.entity.PrimePersonnel;
import com.nectux.mizan.hyban.paie.service.PretService;
import com.nectux.mizan.hyban.paie.service.PrimePersonnelService;
import com.nectux.mizan.hyban.parametrages.dto.PeriodePaieDTO;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.RubriqueService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/paie/prime-personnel")
public class PrimePersonnelRestController {

    private static final Logger logger = LoggerFactory.getLogger(PrimePersonnelRestController.class);

    // @Autowired
    // private UtilisateurService userService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private PretService pretService;
    @Autowired
    private PrimePersonnelService primePersonnelService;
    @Autowired
    private SocieteService societeService;
    @Autowired
    private RubriqueService rubriqueService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/list")
    public ResponseEntity<PrimePersonnelResponse<PrimePersonnel>> getPrimePersonnelList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            PrimePersonnelDTO primePersonnelDTO;
            
            if (search == null || search.trim().isEmpty()) {
                primePersonnelDTO = primePersonnelService.loadPrimePersonnel(pageRequest);
            } else {
                primePersonnelDTO = primePersonnelService.loadPrimePersonnel(pageRequest, search);
            }
            
            PrimePersonnelResponse<PrimePersonnel> response = new PrimePersonnelResponse<>();
            response.setRows(primePersonnelDTO.getRows());
            response.setTotal((int) primePersonnelDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des primes personnel", e);
            PrimePersonnelResponse<PrimePersonnel> response = new PrimePersonnelResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<PrimePersonnelDTO> savePrimePersonnel(@RequestBody PrimePersonnelRequest request) {
        try {
            PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
            Long idCtrat = request.getIdCtrat() != null ? request.getIdCtrat() : request.getCtrat();
            
            PrimePersonnelDTO result = primePersonnelService.saver(
                request.getId(),
                request.getMontantop(),
                request.getValeurop(),
                request.getIdAbsence(),
                idCtrat,
                periodePaie.getId()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la prime personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/periodes")
    public ResponseEntity<PeriodePaieDTO> getPeriodes() {
        try {
            PeriodePaieDTO periodeDTO = new PeriodePaieDTO();
            List<PeriodePaie> listPeriodepaie = periodePaieService.listperiodesupAuPret();
            
            if (listPeriodepaie.size() > 0) {
                periodeDTO.setRows(listPeriodepaie);
                periodeDTO.setResult("success");
            }
            
            return ResponseEntity.ok(periodeDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des périodes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/prets")
    public ResponseEntity<PretDTO> getPrets() {
        try {
            PretDTO pretDTO = new PretDTO();
            List<Pret> listPrets = pretService.listdesPret();
            
            if (listPrets.size() > 0) {
                pretDTO.setRows(listPrets);
                pretDTO.setResult("success");
            }
            
            return ResponseEntity.ok(pretDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des prêts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<PrimePersonnelDTO> deletePrimePersonnel(@PathVariable Long id) {
        try {
            PrimePersonnelDTO result = primePersonnelService.delete(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la prime personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/rechercher/{id}")
    public ResponseEntity<PrimePersonnel> rechercherPrimePersonnel(@PathVariable Long id) {
        try {
            PrimePersonnel result = primePersonnelService.findprimepersonnel(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la prime personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/prime-individuel")
    public ResponseEntity<List<PrimePersonnel>> getPrimeIndividuel(
            @RequestParam Long idPrime,
            @RequestParam Long idPeriode,
            @RequestParam Long idCtrat) {
        try {
            List<PrimePersonnel> result = primePersonnelService.listdesprimepersonnelPeriodePrime(idPeriode, idPrime, idCtrat);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la prime individuelle", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/prime-mois-encours/{id}")
    public ResponseEntity<PrimePersonnelDTO> getPrimeMoisEncours(
            @PathVariable Long id,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            
            PrimePersonnelDTO result = primePersonnelService.listdesprimepersonnelMoisEnPrime(pageRequest, periodePaie.getId(), id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des primes du mois en cours", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
