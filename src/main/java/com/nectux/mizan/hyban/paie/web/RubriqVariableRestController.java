package com.nectux.mizan.hyban.paie.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import com.nectux.mizan.hyban.common.dto.AffectationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.RubriqVariableRequest;
import com.nectux.mizan.hyban.common.dto.RubriqVariableResponse;
import com.nectux.mizan.hyban.paie.dto.RubriqVariableDTO;
import com.nectux.mizan.hyban.paie.entity.RubriqVariable;
import com.nectux.mizan.hyban.paie.repository.RubriqVariableRepository;
import com.nectux.mizan.hyban.paie.service.RubriqVariableService;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.repository.PeriodePaieRepository;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.personnel.entity.Personnel;
import com.nectux.mizan.hyban.personnel.repository.PersonnelRepository;

@RestController
@RequestMapping("/api/paie/rubrique-variable")
public class RubriqVariableRestController {

    private static final Logger logger = LoggerFactory.getLogger(RubriqVariableRestController.class);

    // @Autowired
    // private UtilisateurService userService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private PeriodePaieRepository periodePaieRepository;
    @Autowired
    private PersonnelRepository personnelRepository;
    @Autowired
    private RubriqVariableRepository rubriqvariableRepository;
    @Autowired
    private RubriqVariableService rubriqvariableService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/list")
    public ResponseEntity<RubriqVariableResponse<?>> getRubriqVariableList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            RubriqVariableDTO rubriqVariableDTO;
            
            if (search == null || search.trim().isEmpty()) {
                rubriqVariableDTO = rubriqvariableService.loadRubriqVariable(pageRequest);
            } else {
                rubriqVariableDTO = rubriqvariableService.loadRubriqVariable(pageRequest, search);
            }

            RubriqVariableResponse<Object> response = new RubriqVariableResponse<>();
            response.setRows(rubriqVariableDTO.getRows().stream().map(rubrique -> (Object) rubrique).collect(Collectors.toList()));
            response.setTotal((int)rubriqVariableDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des rubriques variables", e);
            RubriqVariableResponse<Object> response = new RubriqVariableResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<RubriqVariableDTO> saveRubriqVariable(@RequestBody RubriqVariableRequest request) {
        try {
            RubriqVariableDTO result = rubriqvariableService.saver(
                request.getCn(),
                request.getIgr(),
                request.getAmao(),
                request.getSynaoni(),
                request.getMugefci(),
                request.getIvoireSante(),
                request.getIvoirePrev(),
                request.getDiversgainsImp(),
                request.getDiversgains(),
                request.getRegularisation(),
                request.getIdpers()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la rubrique variable", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/chercher/{idPersonnel}")
    public ResponseEntity<RubriqVariable> chercherRubriqVariable(@PathVariable Long idPersonnel) {
        try {
            Personnel personnel = personnelRepository.findById(idPersonnel)
                    .orElseThrow(() -> new EntityNotFoundException("Personnel not found for id " + idPersonnel));
            
            RubriqVariable result = rubriqvariableRepository.findByPersonnelId(personnel.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la rubrique variable", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/periodes")
    public ResponseEntity<List<PeriodePaie>> getPeriodes() {
        try {
            List<PeriodePaie> listPeriodepaie = periodePaieService.listperiodesupAuPret();
            return ResponseEntity.ok(listPeriodepaie);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des périodes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/personnels")
    public ResponseEntity<List<Personnel>> getPersonnels() {
        try {
            List<Personnel> personnels = personnelRepository.findAll();
            return ResponseEntity.ok(personnels);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des personnels", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
