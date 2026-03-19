package com.nectux.mizan.hyban.parametrages.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.MoisRequest;
import com.nectux.mizan.hyban.common.dto.MoisResponse;
import com.nectux.mizan.hyban.parametrages.dto.MoisDTO;
import com.nectux.mizan.hyban.parametrages.entity.Mois;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.MoisService;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
import java.util.stream.Collectors;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/parametrages/mois")
public class MoisRestController {

    private static final Logger logger = LoggerFactory.getLogger(MoisRestController.class);

    // @Autowired
    // private UtilisateurService userService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private MoisService moisService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;
    @Autowired
    private SocieteService societeService;

    @GetMapping("/view")
    public ResponseEntity<String> viewMois() {
        try {
            return ResponseEntity.ok("Mois view loaded");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de la vue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading view");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<MoisResponse<?>> getMoisList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "name");
            MoisDTO moisDTO;
            
            if (search == null || search.trim().isEmpty()) {
                moisDTO = moisService.loadMois(pageRequest);
            } else {
                moisDTO = moisService.loadMois(pageRequest, search);
            }
            
            MoisResponse<Object> response = new MoisResponse<>();
            response.setRows(moisDTO.getRows().stream().map(mois -> (Object) mois).collect(Collectors.toList()));
            response.setTotal((int) moisDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des mois", e);
            MoisResponse<Object> response = new MoisResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/list-all")
    public ResponseEntity<MoisDTO> listMois() {
        try {
            MoisDTO moisDTO = new MoisDTO();
            List<Mois> malist = moisService.findtsmois();
            moisDTO.setRows(malist);
            moisDTO.setTotal(malist.size());
            moisDTO.setResult("success");
            return ResponseEntity.ok(moisDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste de tous les mois", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
