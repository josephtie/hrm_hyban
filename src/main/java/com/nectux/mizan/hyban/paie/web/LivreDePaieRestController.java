package com.nectux.mizan.hyban.paie.web;

import com.nectux.mizan.hyban.paie.dto.BulletinPaieDTO;
import com.nectux.mizan.hyban.paie.dto.SimulationPaieRequest;
import com.nectux.mizan.hyban.paie.entity.LivreDePaie;
import com.nectux.mizan.hyban.paie.entity.LivreDePaieSpeciale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import com.nectux.mizan.hyban.paie.dto.LivreDePaieDTO;
import com.nectux.mizan.hyban.paie.service.BulletinPaieService;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;

import java.util.List;

/**
 * REST endpoints liés à la génération du Livre de Paie (bulletins) sur une période.
 *
 * Base path : /api/paie/bulletin
 */
@RestController
@RequestMapping("/api/paie/bulletin")
@CrossOrigin(origins = "*", allowCredentials = "false")
@PreAuthorize("hasAnyAuthority('PAYROLL_READ', 'PAYROLL_CALCULATE', 'PAYROLL_EXPORT') or hasRole('ADMIN')")
public class LivreDePaieRestController {

    private static final Logger logger = LoggerFactory.getLogger(LivreDePaieRestController.class);


    List<LivreDePaie> livredepaieList=null;
    List<LivreDePaieSpeciale> livreDePaieSpeciales=null;
    @Autowired
    @Qualifier("bulletinPaieService")
    private BulletinPaieService bulletinPaieService;

    @Autowired
    private PeriodePaieService periodePaieService;

    @Autowired
    private com.nectux.mizan.hyban.personnel.service.CategorieService categorieService;

    /**
     * Génère et enregistre les bulletins de paie pour la période demandée.
     * Si {@code idPeriode} est null, la période active est utilisée.
     *
     * REST Endpoint : POST /api/paie/bulletin/saveBullPersonnel
     * Body JSON : { "idPeriode": 1, "limit": 100, "offset": 0 }   // tous optionnels
     */
    @PostMapping(value = "/saveBullPersonnel")
    public ResponseEntity<LivreDePaieDTO> saveBullPersonnel(@RequestBody(required = false) GenerationRequest request) {
        try {
            Long idPeriode = (request != null) ? request.getIdPeriode() : null;
            Integer limit = (request != null && request.getLimit() != null) ? request.getLimit() : 100;
            Integer offset = (request != null && request.getOffset() != null) ? request.getOffset() : 0;

            if (idPeriode == null) {
                PeriodePaie active = periodePaieService.findPeriodeactive();
                if (active == null) {
                    LivreDePaieDTO err = new LivreDePaieDTO();
                    err.setResult("echec");
                    return ResponseEntity.badRequest().body(err);
                }
                idPeriode = active.getId();
            }

            // Si limit <= 0, on renvoie toutes les lignes sans pagination (mode scroll)
            int pageSize = (limit != null && limit > 0) ? limit : Integer.MAX_VALUE;
            PageRequest page = PageRequest.of(offset, pageSize, Direction.DESC, "id");

            logger.info(">>> Génération bulletins pour période id={} (limit={}, offset={})", idPeriode, limit, offset);
            LivreDePaieDTO dto = bulletinPaieService.genererOptimiseMois1(page, idPeriode);
            livredepaieList=dto.getRows();
            if (dto == null) {
                dto = new LivreDePaieDTO();
            }
            dto.setResult("success");
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Erreur lors de la génération des bulletins de paie", e);
            LivreDePaieDTO err = new LivreDePaieDTO();
            err.setResult("echec");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }



//    @ResponseStatus(HttpStatus.OK)
//    @RequestMapping(value = "/saveBulletinlivrepaie", method = RequestMethod.GET)
//    public @ResponseBody BulletinPaieDTO enregistrerLivredePaie(@RequestParam(value="limit", required=false) Integer limit,
//                                                                @RequestParam(value="offset", required=false) Integer offset) {
//        if(offset == null) offset = 0;
//        if(limit == null) limit = 10;
//
//        //final PageRequest page = new PageRequest(offset/10, limit, Direction.DESC, "id");
//        PageRequest page = PageRequest.of(offset / limit, limit, Direction.DESC, "id");
//        return bulletinPaieService.generateLivreDePaie(page);
//    }


    /**
     * Génère et enregistre les bulletins de paie pour une sélection de personnels.
     * Seuls les bulletins des personnels sélectionnés sont remplacés pour la période.
     *
     * REST Endpoint : POST /api/paie/bulletin/generer-selection
     * Body JSON : { "idPeriode": 1, "idsPersonnel": [12, 34, 56] }
     */
    @PostMapping(value = "/generer-selection")
    public ResponseEntity<LivreDePaieDTO> genererSelection(@RequestBody(required = false) SelectionRequest request) {
        try {
            Long idPeriode = (request != null) ? request.getIdPeriode() : null;
            List<Long> idsPersonnel = (request != null) ? request.getIdsPersonnel() : null;

            if (idsPersonnel == null || idsPersonnel.isEmpty()) {
                LivreDePaieDTO err = new LivreDePaieDTO();
                err.setResult("echec");
                return ResponseEntity.badRequest().body(err);
            }

            if (idPeriode == null) {
                PeriodePaie active = periodePaieService.findPeriodeactive();
                if (active == null) {
                    LivreDePaieDTO err = new LivreDePaieDTO();
                    err.setResult("echec");
                    return ResponseEntity.badRequest().body(err);
                }
                idPeriode = active.getId();
            }

            logger.info(">>> Génération bulletins sélection ({} personnel(s)) pour période id={}",
                    idsPersonnel.size(), idPeriode);
            PageRequest page = PageRequest.of(0, Integer.MAX_VALUE, Direction.DESC, "id");
            LivreDePaieDTO dto = bulletinPaieService.genererOptimiseMois1Selection(page, idPeriode, idsPersonnel);
            if (dto == null) {
                dto = new LivreDePaieDTO();
            }
            dto.setResult("success");
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Erreur lors de la génération des bulletins par sélection", e);
            LivreDePaieDTO err = new LivreDePaieDTO();
            err.setResult("echec");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    /**
     * Simulation de salaire : calcule le bulletin d'un personnel À BLANC
     * (aucune persistance) à partir d'une liste de rubriques choisies.
     *
     * REST Endpoint : POST /api/paie/bulletin/simuler
     * Body JSON : { "idPeriode": 1, "idPersonnel": 12,
     *               "rubriques": [ {"rubriqueId": 5, "montant": 50000, "valeur": 0}, ... ] }
     */
    @PostMapping(value = "/simuler")
    public ResponseEntity<LivreDePaieDTO> simuler(@RequestBody(required = false) SimulationPaieRequest request) {
        try {
            if (request == null || request.getIdPersonnel() == null) {
                LivreDePaieDTO err = new LivreDePaieDTO();
                err.setResult("echec");
                return ResponseEntity.badRequest().body(err);
            }

            Long idPeriode = request.getIdPeriode();
            if (idPeriode == null) {
                PeriodePaie active = periodePaieService.findPeriodeactive();
                if (active == null) {
                    LivreDePaieDTO err = new LivreDePaieDTO();
                    err.setResult("echec");
                    return ResponseEntity.badRequest().body(err);
                }
                idPeriode = active.getId();
            }

            request.setIdPeriode(idPeriode);

            logger.info(">>> Simulation salaire personnel id={} période id={} ({} rubrique(s))",
                    request.getIdPersonnel(), idPeriode,
                    request.getRubriques() == null ? 0 : request.getRubriques().size());
            LivreDePaieDTO dto = bulletinPaieService.simulerBulletin(request);
            if (dto == null) {
                dto = new LivreDePaieDTO();
            }
            if (dto.getResult() == null) {
                dto.setResult("success");
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Erreur lors de la simulation de salaire", e);
            LivreDePaieDTO err = new LivreDePaieDTO();
            err.setResult("echec");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    /**
     * Liste des catégories professionnelles pour l'écran de simulation
     * (accessible avec les droits paie, contrairement à /api/categories).
     *
     * REST Endpoint : GET /api/paie/bulletin/categories
     */
    @GetMapping(value = "/categories")
    public ResponseEntity<List<com.nectux.mizan.hyban.personnel.entity.Categorie>> categories() {
        try {
            return ResponseEntity.ok(categorieService.findCategories());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des catégories", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** Payload JSON pour la génération du livre de paie. */
    public static class GenerationRequest {
        private Long idPeriode;
        private Integer limit;
        private Integer offset;

        public Long getIdPeriode() { return idPeriode; }
        public void setIdPeriode(Long idPeriode) { this.idPeriode = idPeriode; }
        public Integer getLimit() { return limit; }
        public void setLimit(Integer limit) { this.limit = limit; }
        public Integer getOffset() { return offset; }
        public void setOffset(Integer offset) { this.offset = offset; }
    }

    /** Payload JSON pour la génération par sélection de personnels. */
    public static class SelectionRequest {
        private Long idPeriode;
        private List<Long> idsPersonnel;

        public Long getIdPeriode() { return idPeriode; }
        public void setIdPeriode(Long idPeriode) { this.idPeriode = idPeriode; }
        public List<Long> getIdsPersonnel() { return idsPersonnel; }
        public void setIdsPersonnel(List<Long> idsPersonnel) { this.idsPersonnel = idsPersonnel; }
    }
}
