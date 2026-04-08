//package com.nectux.mizan.hyban.paie.web;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//
//import jakarta.persistence.EntityNotFoundException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort.Direction;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.nectux.mizan.hyban.common.dto.BulletinPaieRequest;
//import com.nectux.mizan.hyban.common.dto.BulletinPaieResponse;
//import com.nectux.mizan.hyban.common.dto.CalculSursalaireRequest;
//import com.nectux.mizan.hyban.paie.dto.BulletinPaieDTO;
//import com.nectux.mizan.hyban.paie.dto.LivreDePaieDTO;
//import com.nectux.mizan.hyban.paie.entity.BulletinPaie;
//import com.nectux.mizan.hyban.paie.entity.LivreDePaie;
//import com.nectux.mizan.hyban.paie.entity.PrimePersonnel;
//import com.nectux.mizan.hyban.paie.entity.TempEffectif;
//import com.nectux.mizan.hyban.paie.repository.BulletinPaieRepository;
//import com.nectux.mizan.hyban.paie.repository.PrimePersonnelRepository;
//import com.nectux.mizan.hyban.paie.repository.TempEffectifRepository;
//import com.nectux.mizan.hyban.paie.service.BulletinPaieService;
//import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
//import com.nectux.mizan.hyban.parametrages.entity.PlanningConge;
//import com.nectux.mizan.hyban.parametrages.repository.PlanningCongeRepository;
//import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
//import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
//import com.nectux.mizan.hyban.personnel.entity.Personnel;
//import com.nectux.mizan.hyban.personnel.repository.ContratPersonnelRepository;
//import com.nectux.mizan.hyban.personnel.repository.PersonnelRepository;
//
//@RestController
//@RequestMapping("/api/paie")
//public class BulletinPaieRestController {
//
//    private static final Logger logger = LoggerFactory.getLogger(BulletinPaieRestController.class);
//
//    @Autowired
//    private BulletinPaieService bulletinPaieService;
//    @Autowired
//    private BulletinPaieRepository bulletinPaieRepository;
//    @Autowired
//    private PrimePersonnelRepository primePersonnelRepository;
//    @Autowired
//    private PeriodePaieService periodePaieService;
//    @Autowired
//    private PersonnelRepository personnelRepository;
//    @Autowired
//    private ContratPersonnelRepository contratPersonnelRepository;
//    @Autowired
//    private TempEffectifRepository tempeffectifRepository;
//    @Autowired
//    private PlanningCongeRepository planningCongeRepository;
//
//    @GetMapping("/bulletins/periode/{idPeriod}")
//    public ResponseEntity<BulletinPaieResponse<BulletinPaie>> getBulletinsByPeriode(
//            @PathVariable Long idPeriod,
//            @RequestParam(defaultValue = "50") Integer limit,
//            @RequestParam(defaultValue = "0") Integer offset,
//            @RequestParam(required = false) String search) {
//
//        try {
//            PageRequest page = PageRequest.of(offset / 50, limit, Direction.DESC, "id");
//            PeriodePaie periode = periodePaieService.findPeriodePaie(idPeriod);
//
//            BulletinPaieDTO bulletinDTO;
//            if (search == null || search.trim().isEmpty()) {
//                bulletinDTO = bulletinPaieService.loadBulletinPaie(page, periode);
//            } else {
//                bulletinDTO = bulletinPaieService.loadBulletinPaie(page, periode, search);
//            }
//
//            BulletinPaieResponse<BulletinPaie> response = new BulletinPaieResponse<>();
//            response.setRows(bulletinDTO.getRows());
//            response.setTotal((int) bulletinDTO.getTotal());
//            response.setResult("success");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des bulletins", e);
//            BulletinPaieResponse<BulletinPaie> response = new BulletinPaieResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/bulletins/search")
//    public ResponseEntity<BulletinPaieResponse<BulletinPaie>> chargerBulletinsParPeriode(
//            @RequestBody BulletinPaieRequest request) {
//
//        try {
//            int pageOffset = request.getOffset() != null ? request.getOffset() : 0;
//            int pageLimit = request.getLimit() != null ? request.getLimit() : 50;
//
//            String criteria = "";
//            if (request.getSearch1() != null) criteria += request.getSearch1().trim();
//            if (request.getSearch() != null) criteria += request.getSearch().trim();
//
//            PageRequest page = PageRequest.of(pageOffset / 50, pageLimit, Direction.DESC, "id");
//            PeriodePaie periode = periodePaieService.findPeriodePaie(request.getIdPeriod());
//
//            BulletinPaieDTO bulletinDTO;
//
//            if (periode.getId() == null) {
//                if (!criteria.isEmpty()) {
//                    bulletinDTO = bulletinPaieService.loadBulletinPaieSearch(page, criteria);
//                } else {
//                    bulletinDTO = new BulletinPaieDTO();
//                }
//                bulletinDTO.setMessage("⚠️ Période introuvable pour l'id : " + request.getIdPeriod());
//            } else {
//                if (!criteria.isEmpty()) {
//                    bulletinDTO = bulletinPaieService.loadBulletinPaie(page, periode, criteria);
//                } else {
//                    bulletinDTO = bulletinPaieService.loadBulletinPaie(page, periode);
//                }
//            }
//
//            BulletinPaieResponse<BulletinPaie> response = new BulletinPaieResponse<>();
//            response.setRows(bulletinDTO.getRows());
//            response.setTotal((int) bulletinDTO.getTotal());
//            response.setResult("success");
//            response.setMessage(bulletinDTO.getMessage());
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la recherche des bulletins", e);
//            BulletinPaieResponse<BulletinPaie> response = new BulletinPaieResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @GetMapping("/livre-paie/generate")
//    public ResponseEntity<BulletinPaieResponse<LivreDePaie>> generateLivrePaie(
//            @RequestParam(required = false) Long id,
//            @RequestParam(defaultValue = "100") Integer limit,
//            @RequestParam(defaultValue = "0") Integer offset) {
//
//        try {
//            if (id == null) {
//                PeriodePaie periode = periodePaieService.findPeriodeactive();
//                id = periode.getId();
//            }
//
//            PageRequest page = PageRequest.of(offset / 100, limit, Direction.DESC, "id");
//            LivreDePaieDTO livreDePaieDTO = bulletinPaieService.genererOptimiseMois1(page, id);
//
//            BulletinPaieResponse<LivreDePaie> response = new BulletinPaieResponse<>();
//            response.setRows(livreDePaieDTO.getRows());
//            response.setTotal((int) livreDePaieDTO.getTotal());
//            response.setResult("success");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la génération du livre de paie", e);
//            BulletinPaieResponse<LivreDePaie> response = new BulletinPaieResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/calcul-sursalaire/liste")
//    public ResponseEntity<BulletinPaieResponse<LivreDePaie>> calculerSursalaireListe() {
//        try {
//            logger.info("Démarrage du calcul à l'envers pour tous les contrats actifs.");
//            PeriodePaie periode = periodePaieService.findPeriodeactive();
//            List<LivreDePaie> resultats = new ArrayList<>();
//            List<ContratPersonnel> contratsActifs = contratPersonnelRepository.findByStatut(true);
//
//            logger.info("{} contrats actifs récupérés.", contratsActifs.size());
//
//            for (ContratPersonnel ctratpersonnel : contratsActifs) {
//                Double netAPayer = ctratpersonnel.getNetAPayer();
//                if (netAPayer == null || netAPayer == 0d) {
//                    logger.warn("Net à payer manquant pour le personnel : {}", ctratpersonnel.getPersonnel().getNom());
//                    continue;
//                }
//
//                logger.info("Traitement du personnel {} (matricule: {}, net à payer: {})",
//                        ctratpersonnel.getPersonnel().getNom(),
//                        ctratpersonnel.getPersonnel().getMatricule(),
//                        netAPayer
//                );
//
//                try {
//                    PlanningConge planningConge = planningCongeRepository.findByContratPersonnelAndStatut(ctratpersonnel, true);
//                    TempEffectif tpeff = tempeffectifRepository.findByPersonnelAndPeriodePaie(ctratpersonnel.getPersonnel(), periode);
//
//                    long anneesAnciennete = ChronoUnit.YEARS.between(
//                            ctratpersonnel.getPersonnel().getDateArrivee().toInstant()
//                                    .atZone(ZoneId.systemDefault())
//                                    .toLocalDate(),
//                            LocalDate.now()
//                    );
//
//                    double newancienete;
//                    if (ctratpersonnel.getAncienneteInitial() != 0) {
//                        newancienete = anneesAnciennete + ctratpersonnel.getAncienneteInitial();
//                    } else {
//                        newancienete = anneesAnciennete;
//                    }
//
//                    int anc = (int) newancienete;
//                    int op = 0;
//                    if (anc < 2) op = 0;
//                    if (anc >= 2 && anc <= 25) op = anc;
//                    if (anc > 25) op = 25;
//
//                    Float nbpart = calculNbrepart(ctratpersonnel.getPersonnel().getNombrEnfant(), ctratpersonnel.getPersonnel());
//
//                    List<PrimePersonnel> listIndemnite = primePersonnelRepository.findByContratPersonnelPersonnelIdAndPeriodePaieId(
//                            ctratpersonnel.getPersonnel().getId(), periode.getId());
//
//                    List<PrimePersonnel> listIndemniteBrut = new ArrayList<>();
//                    List<PrimePersonnel> listIndemniteNonBrut = new ArrayList<>();
//                    List<PrimePersonnel> listRetenueMutuelle = new ArrayList<>();
//                    List<PrimePersonnel> listRetenueSociale = new ArrayList<>();
//                    List<PrimePersonnel> listGainsNet = new ArrayList<>();
//
//                    for (PrimePersonnel kprme : listIndemnite) {
//                        if (kprme.getPrime().getEtatImposition() == 1) listIndemniteBrut.add(kprme);
//                        if (kprme.getPrime().getEtatImposition() == 2) listIndemniteNonBrut.add(kprme);
//                        if (kprme.getPrime().getEtatImposition() == 3) {
//                            if (kprme.getPrime().getMtExedent() != null) {
//                                listIndemniteBrut.add(kprme);
//                                listIndemniteNonBrut.add(kprme);
//                            }
//                        }
//                        if (kprme.getPrime().getEtatImposition() == 4) listRetenueMutuelle.add(kprme);
//                        if (kprme.getPrime().getEtatImposition() == 5) listGainsNet.add(kprme);
//                        if (kprme.getPrime().getEtatImposition() == 6) listRetenueSociale.add(kprme);
//                    }
//
//                    LivreDePaie livredePaie = new LivreDePaie(
//                            ctratpersonnel.getPersonnel().getMatricule(),
//                            ctratpersonnel.getPersonnel().getNom() + " " + ctratpersonnel.getPersonnel().getPrenom(),
//                            nbpart, op, ctratpersonnel.getCategorie().getSalaireDeBase(), 5000d,
//                            ctratpersonnel.getIndemniteLogement(), 0d, 0d, ctratpersonnel, null, periode,
//                            listIndemniteBrut, listIndemniteNonBrut, listRetenueMutuelle, listGainsNet, listRetenueSociale
//                    );
//
//                    // Calcul itératif du sursalaire
//                    for (int i = 0; i < 20; i++) {
//                        Double nouvSursal = 0d;
//                        Double nouvDiff = 0d;
//                        Double nouvMontantBrutImp = 0d;
//
//                        nouvMontantBrutImp = Math.rint(ctratpersonnel.getNetAPayer() * livredePaie.getBrutImposable() / livredePaie.getNetPayer());
//                        nouvDiff = nouvMontantBrutImp - livredePaie.getBrutImposable();
//                        nouvSursal = nouvDiff + livredePaie.getSursalaire();
//
//                        livredePaie = new LivreDePaie(
//                                ctratpersonnel.getPersonnel().getMatricule(),
//                                ctratpersonnel.getPersonnel().getNom() + " " + ctratpersonnel.getPersonnel().getPrenom(),
//                                nbpart, op, ctratpersonnel.getCategorie().getSalaireDeBase(), nouvSursal,
//                                ctratpersonnel.getIndemniteLogement(), 0d, 0d, ctratpersonnel, null, periode,
//                                listIndemniteBrut, listIndemniteNonBrut, listRetenueMutuelle, listGainsNet, listRetenueSociale
//                        );
//                    }
//
//                    ctratpersonnel.setSursalaire(livredePaie.getSursalaire());
//                    contratPersonnelRepository.save(ctratpersonnel);
//                    resultats.add(livredePaie);
//
//                    logger.info("Calcul réussi pour {}. Net final: {}, brut final: {}",
//                            ctratpersonnel.getPersonnel().getNom(), livredePaie.getNetPayer(), livredePaie.getBrutImposable());
//
//                } catch (Exception e) {
//                    logger.error("Erreur pour le personnel {} : {}", ctratpersonnel.getPersonnel().getNom(), e.getMessage(), e);
//                }
//            }
//
//            logger.info("Fin du traitement. {} bulletins générés.", resultats.size());
//
//            BulletinPaieResponse<LivreDePaie> response = new BulletinPaieResponse<>();
//            response.setRows(resultats);
//            response.setTotal(resultats.size());
//            response.setResult("success");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors du calcul du sursalaire", e);
//            BulletinPaieResponse<LivreDePaie> response = new BulletinPaieResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/calcul-sursalaire")
//    public ResponseEntity<LivreDePaie> calculerSursalaire(@RequestBody CalculSursalaireRequest request) {
//        try {
//            PeriodePaie periode = periodePaieService.findPeriodeactive();
//            ContratPersonnel ctratpersonnel = contratPersonnelRepository.findByPersonnelIdAndStatut(request.getIdPersonnel(), true);
//
//            PlanningConge planningConge = planningCongeRepository.findByContratPersonnelAndStatut(ctratpersonnel, true);
//            TempEffectif tpeff = tempeffectifRepository.findByPersonnelAndPeriodePaie(ctratpersonnel.getPersonnel(), periode);
//
//            long anneesAnciennete = ChronoUnit.YEARS.between(
//                    ctratpersonnel.getPersonnel().getDateArrivee().toInstant()
//                            .atZone(ZoneId.systemDefault())
//                            .toLocalDate(),
//                    LocalDate.now()
//            );
//
//            double newancienete;
//            if (ctratpersonnel.getAncienneteInitial() != 0) {
//                newancienete = anneesAnciennete + ctratpersonnel.getAncienneteInitial();
//            } else {
//                newancienete = anneesAnciennete;
//            }
//
//            int anc = (int) newancienete;
//            int op = 0;
//            if (anc < 2) op = 0;
//            if (anc >= 2 && anc <= 25) op = anc;
//            if (anc > 25) op = 25;
//
//            Float nbpart = calculNbrepart(ctratpersonnel.getPersonnel().getNombrEnfant(), ctratpersonnel.getPersonnel());
//
//            List<PrimePersonnel> listIndemnite = primePersonnelRepository.findByContratPersonnelPersonnelIdAndPeriodePaieId(
//                    ctratpersonnel.getPersonnel().getId(), periode.getId());
//
//            List<PrimePersonnel> listIndemniteBrut = new ArrayList<>();
//            List<PrimePersonnel> listIndemniteNonBrut = new ArrayList<>();
//            List<PrimePersonnel> listRetenueMutuelle = new ArrayList<>();
//            List<PrimePersonnel> listRetenueSociale = new ArrayList<>();
//            List<PrimePersonnel> listGainsNet = new ArrayList<>();
//
//            for (PrimePersonnel kprme : listIndemnite) {
//                if (kprme.getPrime().getEtatImposition() == 1) listIndemniteBrut.add(kprme);
//                if (kprme.getPrime().getEtatImposition() == 2) listIndemniteNonBrut.add(kprme);
//                if (kprme.getPrime().getEtatImposition() == 3) {
//                    if (kprme.getPrime().getMtExedent() != null) {
//                        listIndemniteBrut.add(kprme);
//                        listIndemniteNonBrut.add(kprme);
//                    }
//                }
//                if (kprme.getPrime().getEtatImposition() == 4) listRetenueMutuelle.add(kprme);
//                if (kprme.getPrime().getEtatImposition() == 5) listGainsNet.add(kprme);
//                if (kprme.getPrime().getEtatImposition() == 6) listRetenueSociale.add(kprme);
//            }
//
//            LivreDePaie livredePaie = new LivreDePaie(
//                    ctratpersonnel.getPersonnel().getMatricule(),
//                    ctratpersonnel.getPersonnel().getNom() + " " + ctratpersonnel.getPersonnel().getPrenom(),
//                    nbpart, op, ctratpersonnel.getCategorie().getSalaireDeBase(), 5000d,
//                    ctratpersonnel.getIndemniteLogement(), 0d, 0d, ctratpersonnel, null, periode,
//                    listIndemniteBrut, listIndemniteNonBrut, listRetenueMutuelle, listGainsNet, listRetenueSociale
//            );
//
//            // Calcul itératif du sursalaire
//            for (int i = 0; i < 20; i++) {
//                Double nouvSursal = 0d;
//                Double nouvDiff = 0d;
//                Double nouvMontantBrutImp = 0d;
//
//                nouvMontantBrutImp = Math.rint(request.getNetApayer() * livredePaie.getBrutImposable() / livredePaie.getNetPayer());
//                nouvDiff = nouvMontantBrutImp - livredePaie.getBrutImposable();
//                nouvSursal = nouvDiff + livredePaie.getSursalaire();
//
//                livredePaie = new LivreDePaie(
//                        ctratpersonnel.getPersonnel().getMatricule(),
//                        ctratpersonnel.getPersonnel().getNom() + " " + ctratpersonnel.getPersonnel().getPrenom(),
//                        nbpart, op, ctratpersonnel.getCategorie().getSalaireDeBase(), nouvSursal,
//                        ctratpersonnel.getIndemniteLogement(), 0d, 0d, ctratpersonnel, null, periode,
//                        listIndemniteBrut, listIndemniteNonBrut, listRetenueMutuelle, listGainsNet, listRetenueSociale
//                );
//            }
//
//            return ResponseEntity.ok(livredePaie);
//        } catch (Exception e) {
//            logger.error("Erreur lors du calcul du sursalaire individuel", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/bulletins/mois-actif")
//    public ResponseEntity<BulletinPaieResponse<BulletinPaie>> getBulletinsMoisActif(
//            @RequestParam(defaultValue = "10") Integer limit,
//            @RequestParam(defaultValue = "0") Integer offset,
//            @RequestParam(required = false) String search) {
//
//        try {
//            PageRequest page = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
//            PeriodePaie periode = periodePaieService.findPeriodeactive();
//
//            BulletinPaieDTO bulletinDTO = bulletinPaieService.BulletinMoisCalculer(page, periode);
//
//            BulletinPaieResponse<BulletinPaie> response = new BulletinPaieResponse<>();
//            response.setRows(bulletinDTO.getRows());
//            response.setTotal((int) bulletinDTO.getTotal());
//            response.setResult("success");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des bulletins du mois actif", e);
//            BulletinPaieResponse<BulletinPaie> response = new BulletinPaieResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @GetMapping("/personnel/periode-actif")
//    public ResponseEntity<List<ContratPersonnel>> getPersonnelPeriodeActif() {
//        try {
//            List<Personnel> personnelList = personnelRepository.findAll();
//            List<ContratPersonnel> personnelListTrt = new ArrayList<>();
//            PeriodePaie periode = periodePaieService.findPeriodeactive();
//
//            if (periode != null) {
//                for (Personnel personnel : personnelList) {
//                    ContratPersonnel ctratpersonnel = contratPersonnelRepository.findByPersonnelId(personnel.getId());
//                    if (ctratpersonnel != null && ctratpersonnel.getStatut()) {
//                        personnelListTrt.add(ctratpersonnel);
//                    }
//                }
//            }
//
//            return ResponseEntity.ok(personnelListTrt);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération du personnel de la période active", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    // Méthode utilitaire pour calculer le nombre de parts
//    private Float calculNbrepart(Integer nombreEnfant, Personnel personnel) {
//        // Implémenter la logique de calcul du nombre de parts
//        // C'est une simplification, vous devrez adapter selon vos règles métier
//        Float parts = 1.0f; // Part pour l'employé
//        if (nombreEnfant != null && nombreEnfant > 0) {
//            parts += Math.min(nombreEnfant, 3) * 0.5f; // 0.5 part par enfant, max 3 enfants
//        }
//        if ( personnel.getSituationMatrimoniale() == 1) {
//            parts += 0.5f; // Part supplémentaire pour marié
//        }
//        return parts;
//    }
//}
