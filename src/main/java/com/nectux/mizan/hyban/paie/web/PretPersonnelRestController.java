package com.nectux.mizan.hyban.paie.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.PretPersonnelRequest;
import com.nectux.mizan.hyban.common.dto.PretPersonnelResponse;
import com.nectux.mizan.hyban.paie.dto.LivreDePaieSimulationDTO;
import com.nectux.mizan.hyban.paie.dto.PretDTO;
import com.nectux.mizan.hyban.paie.dto.PretPersonnelDTO;
import com.nectux.mizan.hyban.personnel.entity.Categorie;
import com.nectux.mizan.hyban.paie.entity.LivreDePaieSimulation;
import com.nectux.mizan.hyban.paie.entity.Pret;
import com.nectux.mizan.hyban.paie.entity.PrimePersonnel;
import com.nectux.mizan.hyban.paie.service.PretPersonnelService;
import com.nectux.mizan.hyban.paie.service.PretService;
import com.nectux.mizan.hyban.parametrages.dto.PeriodePaieDTO;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.personnel.service.CategorieService;
import com.nectux.mizan.hyban.utils.DifferenceDate;
import com.nectux.mizan.hyban.utils.Utils;

@RestController
@RequestMapping("/api/paie/pret-personnel")
public class PretPersonnelRestController {

    private static final Logger logger = LoggerFactory.getLogger(PretPersonnelRestController.class);

    // @Autowired
    // private UtilisateurService userService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private PretService pretService;
    @Autowired
    private CategorieService categorieService;
    @Autowired
    private PretPersonnelService pretPersonnelService;
    @Autowired
    private SocieteService societeService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/list")
    public ResponseEntity<PretPersonnelResponse<?>> getPretPersonnelList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            PretPersonnelDTO pretPersonnelDTO;
            
            if (search == null || search.trim().isEmpty()) {
                pretPersonnelDTO = pretPersonnelService.loadPretPersonnel(pageRequest);
            } else {
                pretPersonnelDTO = pretPersonnelService.loadPretPersonnel(pageRequest, search);
            }
            
            PretPersonnelResponse<Object> response = new PretPersonnelResponse<>();
            response.setRows(Collections.singletonList(pretPersonnelDTO.getRows()));
            response.setTotal((int) pretPersonnelDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des prêts personnel", e);
            PretPersonnelResponse<Object> response = new PretPersonnelResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<PretPersonnelDTO> savePretPersonnel(@RequestBody PretPersonnelRequest request) {
        try {
            PretPersonnelDTO result = pretPersonnelService.saver(
                request.getMontant(),
                request.getEchelonage(),
                request.getPret(),
                request.getIdpers(),
                request.getDEmprunt(),
                request.getPeriodepaie()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement du prêt personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/modifier")
    public ResponseEntity<PretPersonnelDTO> updatePretPersonnel(
            @RequestParam Long idpret,
            @RequestParam Double montant1,
            @RequestParam Long echelonage1,
            @RequestParam Long pret1,
            @RequestParam Long idpers1,
            @RequestParam String dEmprunt1,
            @RequestParam Long periodepaie1) {
        
        try {
            PretPersonnelDTO result = pretPersonnelService.update(
                idpret, montant1, echelonage1, pret1, idpers1, dEmprunt1, periodepaie1
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la modification du prêt personnel", e);
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

    @GetMapping("/individuel/{idp}")
    public ResponseEntity<PretPersonnelDTO> getPretIndividuel(@PathVariable Long idp) {
        try {
            PretPersonnelDTO result = pretPersonnelService.PretPersonneldupersonnel(idp);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du prêt individuel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/individuel/{idp}")
    public ResponseEntity<PretPersonnelResponse<?>> deletePretIndividuel(@PathVariable Long idp) {
        try {
            PretPersonnelResponse<Object> response = new PretPersonnelResponse<>();
            response.setResult(pretPersonnelService.delete(idp).toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du prêt individuel", e);
            PretPersonnelResponse<Object> response = new PretPersonnelResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/search/{idp}")
    public ResponseEntity<PretPersonnelDTO> searchPretIndividuel(@PathVariable Long idp) {
        try {PretPersonnelDTO result=new PretPersonnelDTO();
             result.setRow(pretPersonnelService.findpret(idp));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du prêt individuel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/simulation-calcul")
    public ResponseEntity<LivreDePaieSimulationDTO> simulerCalcul(
            @RequestParam Long categorie,
            @RequestParam Double montantTransp,
            @RequestParam Integer situationmatrimoniale,
            @RequestParam Integer nombreenfant,
            @RequestParam Double salaireNet) {
        
        try {
            LivreDePaieSimulation livredePaie = null;
            LivreDePaieSimulationDTO livreDePaieSimulationDTO = new LivreDePaieSimulationDTO();
            Categorie categorie1 = categorieService.findCategorie(categorie);
            List<LivreDePaieSimulation> livreDePaieSimulations = new ArrayList<>();

            int op = 0;
            Float nbpart = calculNbrepart(nombreenfant, situationmatrimoniale);
            
            List<PrimePersonnel> listIndemniteBrut = new ArrayList<>();
            List<PrimePersonnel> listIndemniteNonBrut = new ArrayList<>();
            List<PrimePersonnel> listRetenueMutuelle = new ArrayList<>();
            List<PrimePersonnel> listGainsNet = new ArrayList<>();

            livredePaie = new LivreDePaieSimulation(
                "MatTEST", "nomTest" + " " + "PrenomTest", nbpart, op, 
                categorie1.getSalaireDeBase(), 5000d, 0d, montantTransp, 0d, 0d, 
                true, null, null, listIndemniteBrut, listIndemniteNonBrut, 
                listRetenueMutuelle, listGainsNet
            );

            try {
                for (int i = 0; i < 20; i++) {
                    Double nouvSursal = 0d;
                    Double nouvDiff = 0d;
                    Double nouvMontantBrutImp = 0d;
                    
                    nouvMontantBrutImp = Math.rint(salaireNet * livredePaie.getBrutImposable() / livredePaie.getNetPayer());
                    nouvDiff = nouvMontantBrutImp - livredePaie.getBrutImposable();
                    nouvSursal = nouvDiff + livredePaie.getSursalaire();
                    
                    livredePaie = new LivreDePaieSimulation(
                        "MatTEST", "nomTest" + " " + "PrenomTest", nbpart, op, 
                        categorie1.getSalaireDeBase(), nouvSursal, 0d, montantTransp, 0d, 0d, 
                        true, null, null, listIndemniteBrut, listIndemniteNonBrut, 
                        listRetenueMutuelle, listGainsNet
                    );
                }
            } catch (Exception e) {
                logger.error("Erreur lors du calcul de simulation", e);
            }

            livreDePaieSimulations.add(livredePaie);
            livreDePaieSimulationDTO.setResult(true);
            livreDePaieSimulationDTO.setRows(livreDePaieSimulations);
            
            return ResponseEntity.ok(livreDePaieSimulationDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la simulation de calcul", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Méthode utilitaire pour calculer le nombre de parts
    private Float calculNbrepart(Integer nbEnfant, Integer persSituationMatrimoniale) {
        Float nbPart = 0F;

        if ((persSituationMatrimoniale == 2 || persSituationMatrimoniale == 3 || persSituationMatrimoniale == 4) && nbEnfant == 0)
            nbPart = (float) 1;

        if ((persSituationMatrimoniale == 2 || persSituationMatrimoniale == 3) && nbEnfant > 0) {
            nbPart = (float) (1.5 + (nbEnfant * 0.5));
            if (nbPart > 5)
                nbPart = (float) 5;
        }

        if (persSituationMatrimoniale == 1 && nbEnfant == 0)
            nbPart = (float) 2;

        if ((persSituationMatrimoniale == 1 || persSituationMatrimoniale == 4) && nbEnfant > 0) {
            nbPart = (float) (2 + (nbEnfant * 0.5));
            if (nbPart > 5)
                nbPart = (float) 5;
        }
        return nbPart;
    }

    // Méthode utilitaire pour calculer l'ancienneté
    private Double[] calculAnciennete(Double salaireCategoriel, Date dateEntree) {
        Double[] tab = new Double[5];
        Double anciennete = (double) 0;

        double age = DifferenceDate.valAge(new Date(), dateEntree);
        int partieEntiere = (int) age;
        int partieApresVirg = (int) ((age - partieEntiere) * 12);

        if (age >= 2)
            anciennete = (double) (salaireCategoriel * partieEntiere / 100);

        tab[0] = anciennete;
        tab[1] = (double) partieEntiere;
        tab[2] = (double) partieApresVirg;

        return tab;
    }
}
