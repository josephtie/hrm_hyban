package com.nectux.mizan.hyban.controllers;

import com.nectux.mizan.hyban.paie.entity.BulletinPaie;
import com.nectux.mizan.hyban.paie.repository.BulletinPaieRepository;
import com.nectux.mizan.hyban.paie.service.BulletinPaieService;
import com.nectux.mizan.hyban.paie.service.CongeService;
import com.nectux.mizan.hyban.parametrages.entity.Exercice;
import com.nectux.mizan.hyban.parametrages.entity.Mois;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.PlanningConge;
import com.nectux.mizan.hyban.parametrages.repository.PeriodePaieRepository;
import com.nectux.mizan.hyban.parametrages.service.ExerciceService;
import com.nectux.mizan.hyban.parametrages.service.MoisService;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.PlanningCongeService;
import com.nectux.mizan.hyban.paie.entity.Conge;
import com.nectux.mizan.hyban.personnel.entity.Personnel;
import com.nectux.mizan.hyban.personnel.repository.ContratPersonnelRepository;
import com.nectux.mizan.hyban.personnel.repository.PersonnelRepository;
import com.nectux.mizan.hyban.personnel.service.PersonnelService;
import com.nectux.mizan.hyban.utils.DifferenceDate;
import com.nectux.mizan.hyban.utils.PrintLs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/reporting")
public class ReportingRestController {

    private static final Logger logger = LoggerFactory.getLogger(ReportingRestController.class);

    private final PersonnelRepository personnelRepository;
    private final PersonnelService personnelService;
    private final ContratPersonnelRepository contratPersonnelRepository;
    private final BulletinPaieRepository bulletinPaieRepository;
    private final BulletinPaieService bulletinPaieService;
    private final PeriodePaieService periodePaieService;
    private final PeriodePaieRepository periodePaieRepository;
    private final ExerciceService exerciceService;
    private final MoisService moisService;
    private final CongeService congeService;
    private final PlanningCongeService planningCongeService;

    public ReportingRestController(PersonnelRepository personnelRepository,
                                   PersonnelService personnelService,
                                   ContratPersonnelRepository contratPersonnelRepository,
                                   BulletinPaieRepository bulletinPaieRepository,
                                   BulletinPaieService bulletinPaieService,
                                   PeriodePaieService periodePaieService,
                                   PeriodePaieRepository periodePaieRepository,
                                   ExerciceService exerciceService,
                                   MoisService moisService,
                                   CongeService congeService,
                                   PlanningCongeService planningCongeService) {
        this.personnelRepository = personnelRepository;
        this.personnelService = personnelService;
        this.contratPersonnelRepository = contratPersonnelRepository;
        this.bulletinPaieRepository = bulletinPaieRepository;
        this.bulletinPaieService = bulletinPaieService;
        this.periodePaieService = periodePaieService;
        this.periodePaieRepository = periodePaieRepository;
        this.exerciceService = exerciceService;
        this.moisService = moisService;
        this.congeService = congeService;
        this.planningCongeService = planningCongeService;
    }

    // ==================== KPIs ====================

    @GetMapping("/kpis")
    public ResponseEntity<Map<String, Object>> getKpis() {
        Map<String, Object> kpis = new LinkedHashMap<>();

        // Effectif total (non retirés)
        int totalEffectif = personnelRepository.countByRetraitEffectFalse();
        kpis.put("effectifTotal", totalEffectif);

        // Effectif by sexe
        List<Personnel> activePersonnel = personnelRepository.findByRetraitEffectFalse();
        long hommes = activePersonnel.stream()
                .filter(p -> "Masculin".equalsIgnoreCase(p.getSexe()))
                .count();
        long femmes = activePersonnel.stream()
                .filter(p -> "Feminin".equalsIgnoreCase(p.getSexe()))
                .count();
        kpis.put("effectifHommes", hommes);
        kpis.put("effectifFemmes", femmes);

        // Masse salariale (current period)
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        BigDecimal masseSalarialeTotale = BigDecimal.ZERO;
        if (periode != null) {
            List<PrintLs> masseParSite = bulletinPaieService.calculerMasseSalarialeParSite(periode);
            for (PrintLs p : masseParSite) {
                if (p.getValue1() != null) {
                    masseSalarialeTotale = masseSalarialeTotale.add(p.getValue1());
                }
            }
        }
        kpis.put("masseSalariale", masseSalarialeTotale);

        // Effectif par type
        long contractuels = activePersonnel.stream().filter(p -> Boolean.TRUE.equals(p.getCarec())).count();
        long stagiaires = activePersonnel.stream().filter(p -> Boolean.TRUE.equals(p.getStage())).count();
        long fonctionnaires = activePersonnel.stream().filter(p -> Boolean.TRUE.equals(p.getFonctionnaire())).count();
        long consultants = activePersonnel.stream().filter(p -> Boolean.TRUE.equals(p.getConsultant())).count();
        kpis.put("contractuels", contractuels);
        kpis.put("stagiaires", stagiaires);
        kpis.put("fonctionnaires", fonctionnaires);
        kpis.put("consultants", consultants);

        kpis.put("periodeActive", periode != null ? periode.getId() : null);

        return ResponseEntity.ok(kpis);
    }

    // ==================== Effectif annuel (5 dernières années) ====================

    @GetMapping("/effectif-annuel")
    public ResponseEntity<List<PrintLs>> getEffectifAnnuel(@RequestParam(value = "exerciceId", required = false) Long exerciceId) {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        exerciceId=periode.getAnnee().getId();
        Exercice exercice = resolveExercice(exerciceId);
        List<PrintLs> result = new ArrayList<>();

        if (exercice == null || exercice.getAnnee() == null) {
            logger.warn("effectif-annuel: no active exercice found");
            return ResponseEntity.ok(result);
        }

        logger.info("effectif-annuel: using exercice annee={}", exercice.getAnnee());
        int anneeEnCours = Integer.parseInt(exercice.getAnnee());

        for (int i = 0; i < 5; i++) {
            String annee = String.valueOf(anneeEnCours);
            java.sql.Date dateDeb = java.sql.Date.valueOf(annee + "-01-01");
            java.sql.Date dateFin = java.sql.Date.valueOf(annee + "-12-31");

            List<Personnel> hommes = personnelService.RechercherListPersonnelParAnnee(dateDeb, dateFin, "Masculin");
            List<Personnel> femmes = personnelService.RechercherListPersonnelParAnnee(dateDeb, dateFin, "Feminin");

            PrintLs printDTO = new PrintLs();
            printDTO.setS1(annee);
            printDTO.setI1(hommes != null ? hommes.size() : 0);
            printDTO.setTitle1("Hommes");
            printDTO.setI2(femmes != null ? femmes.size() : 0);
            printDTO.setTitle2("Femmes");
            result.add(printDTO);

            logger.info("effectif-annuel: annee={} hommes={} femmes={}", annee, printDTO.getI1(), printDTO.getI2());

            anneeEnCours--;
        }

        return ResponseEntity.ok(result);
    }

    // ==================== Effectif par site (période active) ====================

    @GetMapping("/effectif-par-site")
    public ResponseEntity<List<PrintLs>> getEffectifParSite() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<PrintLs> result = bulletinPaieService.calculerEffectifParSiteAlaPaie(periode);
        return ResponseEntity.ok(result);
    }

    // ==================== Masse salariale par type de contrat (période active) ====================

    @GetMapping("/masse-salariale-par-type-contrat")
    public ResponseEntity<List<PrintLs>> getMasseSalarialeParTypeContrat() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<PrintLs> result = bulletinPaieService.calculerMasseSalarialeParTypeContrat(periode);
        return ResponseEntity.ok(result);
    }

    // ==================== Masse salariale par site (période active) ====================

    @GetMapping("/masse-salariale-par-site")
    public ResponseEntity<List<PrintLs>> getMasseSalarialeParSite() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<PrintLs> result = bulletinPaieService.calculerMasseSalarialeParSite(periode);
        return ResponseEntity.ok(result);
    }

    // ==================== Effectif + masse salariale par site (merged) ====================

    @GetMapping("/effectif-masse-par-site")
    public ResponseEntity<List<PrintLs>> getEffectifMasseParSite() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<PrintLs> effectifParSite = bulletinPaieService.calculerEffectifParSiteAlaPaie(periode);
        List<PrintLs> masseSalarialeSite = bulletinPaieService.calculerMasseSalarialeParSite(periode);

        Map<String, PrintLs> merged = new LinkedHashMap<>();
        for (PrintLs eff : effectifParSite) {
            PrintLs item = new PrintLs();
            item.setS1(eff.getS1());
            item.setI1(eff.getI1());
            merged.put(eff.getS1(), item);
        }
        for (PrintLs masse : masseSalarialeSite) {
            PrintLs item = merged.computeIfAbsent(masse.getS1(), k -> new PrintLs());
            item.setS1(masse.getS1());
            item.setValue1(masse.getValue1());
        }

        return ResponseEntity.ok(new ArrayList<>(merged.values()));
    }

    // ==================== Congé statistics by month ====================

    @GetMapping("/conge-stat")
    public ResponseEntity<List<PrintLs>> getCongeStat(@RequestParam(value = "exerciceId", required = false) Long exerciceId) {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        exerciceId=periode.getAnnee().getId();
        Exercice exercice = resolveExercice(exerciceId);
        List<PrintLs> result = new ArrayList<>();

        if (exercice == null || exercice.getId() == null) {
            return ResponseEntity.ok(result);
        }

        List<Mois> listMois = moisService.findtsmois();
        for (Mois mois : listMois) {
            List<Conge> listconge = congeService.rechercherByAgenceMoisAnnee(mois, exercice);
            List<PlanningConge> listPlanning = planningCongeService.rechercherByAgenceMoisAnnee(mois, exercice);

            PrintLs printDTO = new PrintLs();
            printDTO.setS1(mois.getMois());
            printDTO.setI1(listconge != null ? listconge.size() : 0);
            printDTO.setTitle1(exercice.getAnnee());
            printDTO.setI2(listPlanning != null ? listPlanning.size() : 0);
            printDTO.setTitle2(exercice.getAnnee());
            result.add(printDTO);
        }

        return ResponseEntity.ok(result);
    }

    // ==================== Retraite statistics ====================

    @GetMapping("/retraite-stat")
    public ResponseEntity<List<PrintLs>> getRetraiteStat(@RequestParam(value = "exerciceId", required = false) Long exerciceId) {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        exerciceId=periode.getAnnee().getId();
        Exercice exercice = resolveExercice(exerciceId);
        List<PrintLs> result = new ArrayList<>();

        if (exercice == null || exercice.getAnnee() == null) {
            return ResponseEntity.ok(result);
        }

        int anneeEnCours = Integer.parseInt(exercice.getAnnee());
        String annee = String.valueOf(anneeEnCours);
        java.sql.Date dateAnDeb = java.sql.Date.valueOf(annee + "-01-01");

        List<Personnel> hommes = personnelService.RechercherListPersonnelParAnnee("Masculin");
        List<Personnel> femmes = personnelService.RechercherListPersonnelParAnnee("Feminin");

        int nbHommeRetrait = 0;
        int nbFemmeRetrait = 0;

        if (hommes != null) {
            for (Personnel p : hommes) {
                if (p.getDateNaissance() != null) {
                    double age = DifferenceDate.valAge(dateAnDeb, p.getDateNaissance());
                    if (age > 59) nbHommeRetrait++;
                }
            }
        }
        if (femmes != null) {
            for (Personnel p : femmes) {
                if (p.getDateNaissance() != null) {
                    double age = DifferenceDate.valAge(dateAnDeb, p.getDateNaissance());
                    if (age > 59) nbFemmeRetrait++;
                }
            }
        }

        PrintLs printDTO = new PrintLs();
        printDTO.setS1(annee);
        printDTO.setI1(nbHommeRetrait);
        printDTO.setTitle1("Hommes");
        printDTO.setI2(nbFemmeRetrait);
        printDTO.setTitle2("Femmes");
        result.add(printDTO);

        return ResponseEntity.ok(result);
    }

    // ==================== Type contrat statistics ====================

    @GetMapping("/type-contrat-stat")
    public ResponseEntity<List<PrintLs>> getTypeContratStat() {
        List<Personnel> activePersonnel = personnelRepository.findByRetraitEffectFalse();
        List<PrintLs> result = new ArrayList<>();

        long contractuels = activePersonnel.stream().filter(p -> Boolean.TRUE.equals(p.getCarec())).count();
        long stagiaires = activePersonnel.stream().filter(p -> Boolean.TRUE.equals(p.getStage())).count();
        long fonctionnaires = activePersonnel.stream().filter(p -> Boolean.TRUE.equals(p.getFonctionnaire())).count();
        long consultants = activePersonnel.stream().filter(p -> Boolean.TRUE.equals(p.getConsultant())).count();
        long autres = activePersonnel.size() - contractuels - stagiaires - fonctionnaires - consultants;

        result.add(createPrintLs("Contractuels", (int) contractuels));
        result.add(createPrintLs("Stagiaires", (int) stagiaires));
        result.add(createPrintLs("Fonctionnaires", (int) fonctionnaires));
        result.add(createPrintLs("Consultants", (int) consultants));
        if (autres > 0) {
            result.add(createPrintLs("Autres", (int) autres));
        }

        return ResponseEntity.ok(result);
    }

    // ==================== RH: Pyramide des âges ====================

    @GetMapping("/pyramide-ages")
    public ResponseEntity<List<PrintLs>> getPyramideAges() {
        List<Personnel> activePersonnel = personnelRepository.findByRetraitEffectFalse();
        List<PrintLs> result = new ArrayList<>();
        String[] tranches = {"18-25", "26-30", "31-35", "36-40", "41-45", "46-50", "51-55", "56-60", "60+"};
        int[] bornesInf = {18, 26, 31, 36, 41, 46, 51, 56, 61};
        int[] bornesSup = {25, 30, 35, 40, 45, 50, 55, 60, 200};
        java.util.Date now = new java.util.Date();
        for (int i = 0; i < tranches.length; i++) {
            int hommes = 0, femmes = 0;
            for (Personnel p : activePersonnel) {
                if (p.getDateNaissance() == null) continue;
                int age = (int) ((now.getTime() - p.getDateNaissance().getTime()) / (1000L * 60 * 60 * 24 * 365));
                if (age >= bornesInf[i] && age <= bornesSup[i]) {
                    if ("Masculin".equalsIgnoreCase(p.getSexe())) hommes++;
                    else if ("Feminin".equalsIgnoreCase(p.getSexe())) femmes++;
                }
            }
            PrintLs dto = new PrintLs();
            dto.setS1(tranches[i]);
            dto.setI1(hommes);
            dto.setI2(femmes);
            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }

    // ==================== RH: Ancienneté des employés ====================

    @GetMapping("/anciennete")
    public ResponseEntity<List<PrintLs>> getAnciennete() {
        List<Personnel> activePersonnel = personnelRepository.findByRetraitEffectFalse();
        List<PrintLs> result = new ArrayList<>();
        String[] tranches = {"0-2 ans", "3-5 ans", "6-10 ans", "11-15 ans", "16+ ans"};
        int[] bornesInf = {0, 3, 6, 11, 16};
        int[] bornesSup = {2, 5, 10, 15, 200};
        java.util.Date now = new java.util.Date();
        for (int i = 0; i < tranches.length; i++) {
            int count = 0;
            for (Personnel p : activePersonnel) {
                if (p.getDateArrivee() == null) continue;
                int ans = (int) ((now.getTime() - p.getDateArrivee().getTime()) / (1000L * 60 * 60 * 24 * 365));
                if (ans >= bornesInf[i] && ans <= bornesSup[i]) count++;
            }
            PrintLs dto = new PrintLs();
            dto.setS1(tranches[i]);
            dto.setI1(count);
            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }

    // ==================== RH: Évolution mensuelle des effectifs ====================

    @GetMapping("/effectif-mensuel")
    public ResponseEntity<List<PrintLs>> getEffectifMensuel(@RequestParam(value = "exerciceId", required = false) Long exerciceId) {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null || periode.getAnnee() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<PeriodePaie> periodes = periodePaieRepository.findByAnneeAnnee(periode.getAnnee().getAnnee());
        periodes.sort((a, b) -> {
            Long ma = a.getMois() != null ? a.getMois().getId() : 0L;
            Long mb = b.getMois() != null ? b.getMois().getId() : 0L;
            return ma.compareTo(mb);
        });
        List<PrintLs> result = new ArrayList<>();
        for (PeriodePaie pp : periodes) {
            List<BulletinPaie> bulletins = bulletinPaieRepository.findByPeriodePaieIdAndCalculerTrue(pp.getId());
            PrintLs dto = new PrintLs();
            dto.setS1(pp.getMois() != null ? pp.getMois().getMois() : "P" + pp.getId());
            dto.setI1(bulletins.size());
            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }

    // ==================== Paie: Évolution du net payé ====================

    @GetMapping("/net-paye-evolution")
    public ResponseEntity<List<PrintLs>> getNetPayeEvolution() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null || periode.getAnnee() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<PeriodePaie> periodes = periodePaieRepository.findByAnneeAnnee(periode.getAnnee().getAnnee());
        periodes.sort((a, b) -> {
            Long ma = a.getMois() != null ? a.getMois().getId() : 0L;
            Long mb = b.getMois() != null ? b.getMois().getId() : 0L;
            return ma.compareTo(mb);
        });
        List<PrintLs> result = new ArrayList<>();
        for (PeriodePaie pp : periodes) {
            List<BulletinPaie> bulletins = bulletinPaieRepository.findByPeriodePaieIdAndCalculerTrue(pp.getId());
            BigDecimal netTotal = BigDecimal.ZERO;
            for (BulletinPaie b : bulletins) {
                if (b.getNetapayer() != null) netTotal = netTotal.add(b.getNetapayer());
            }
            PrintLs dto = new PrintLs();
            dto.setS1(pp.getMois() != null ? pp.getMois().getMois() : "P" + pp.getId());
            dto.setValue1(netTotal);
            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }

    // ==================== Paie: Brut vs Net vs Charges ====================

    @GetMapping("/brut-net-charges")
    public ResponseEntity<List<PrintLs>> getBrutNetCharges() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<BulletinPaie> bulletins = bulletinPaieRepository.findByPeriodePaieIdAndCalculerTrue(periode.getId());
        BigDecimal brut = BigDecimal.ZERO, net = BigDecimal.ZERO, charges = BigDecimal.ZERO;
        for (BulletinPaie b : bulletins) {
            if (b.getTotalbrut() != null) brut = brut.add(b.getTotalbrut());
            if (b.getNetapayer() != null) net = net.add(b.getNetapayer());
            if (b.getTotalretenue() != null) charges = charges.add(b.getTotalretenue());
        }
        List<PrintLs> result = new ArrayList<>();
        result.add(makePrintLs("Brut", brut));
        result.add(makePrintLs("Net", net));
        result.add(makePrintLs("Charges", charges));
        return ResponseEntity.ok(result);
    }

    // ==================== Paie: Répartition des primes ====================

    @GetMapping("/repartition-primes")
    public ResponseEntity<List<PrintLs>> getRepartitionPrimes() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<BulletinPaie> bulletins = bulletinPaieRepository.findByPeriodePaieIdAndCalculerTrue(periode.getId());
        BigDecimal primeAnciennete = BigDecimal.ZERO, indemniteLogement = BigDecimal.ZERO,
                indemniteTransport = BigDecimal.ZERO, sursalaire = BigDecimal.ZERO, autres = BigDecimal.ZERO;
        for (BulletinPaie b : bulletins) {
            if (b.getPrimeanciennete() != null) primeAnciennete = primeAnciennete.add(b.getPrimeanciennete());
            if (b.getIndemnitelogement() != null) indemniteLogement = indemniteLogement.add(b.getIndemnitelogement());
            if (b.getIndemniteTransport() != null) indemniteTransport = indemniteTransport.add(b.getIndemniteTransport());
            if (b.getSursalaire() != null) sursalaire = sursalaire.add(b.getSursalaire());
            if (b.getAutreIndemImposable() != null) autres = autres.add(b.getAutreIndemImposable());
        }
        List<PrintLs> result = new ArrayList<>();
        result.add(makePrintLs("Prime ancienneté", primeAnciennete));
        result.add(makePrintLs("Indemnité logement", indemniteLogement));
        result.add(makePrintLs("Indemnité transport", indemniteTransport));
        result.add(makePrintLs("Sursalaire", sursalaire));
        result.add(makePrintLs("Autres", autres));
        return ResponseEntity.ok(result);
    }

    // ==================== Paie: Analyse des retenues ====================

    @GetMapping("/analyse-retenues")
    public ResponseEntity<List<PrintLs>> getAnalyseRetenues() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<BulletinPaie> bulletins = bulletinPaieRepository.findByPeriodePaieIdAndCalculerTrue(periode.getId());
        BigDecimal cnps = BigDecimal.ZERO, its = BigDecimal.ZERO, cn = BigDecimal.ZERO,
                avance = BigDecimal.ZERO, autres = BigDecimal.ZERO;
        for (BulletinPaie b : bulletins) {
            if (b.getCnps() != null) cnps = cnps.add(b.getCnps());
            if (b.getIts() != null) its = its.add(b.getIts());
            if (b.getCn() != null) cn = cn.add(b.getCn());
            if (b.getAvanceetacompte() != null) avance = avance.add(b.getAvanceetacompte());
            if (b.getTotalretenue() != null) autres = autres.add(b.getTotalretenue());
        }
        autres = autres.subtract(cnps).subtract(its).subtract(cn).subtract(avance);
        if (autres.compareTo(BigDecimal.ZERO) < 0) autres = BigDecimal.ZERO;
        List<PrintLs> result = new ArrayList<>();
        result.add(makePrintLs("CNPS", cnps));
        result.add(makePrintLs("ITS", its));
        result.add(makePrintLs("CN", cn));
        result.add(makePrintLs("Avances", avance));
        result.add(makePrintLs("Autres", autres));
        return ResponseEntity.ok(result);
    }

    // ==================== DG: Évolution de la masse salariale ====================

    @GetMapping("/masse-salariale-evolution")
    public ResponseEntity<List<PrintLs>> getMasseSalarialeEvolution() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null || periode.getAnnee() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<PeriodePaie> periodes = periodePaieRepository.findByAnneeAnnee(periode.getAnnee().getAnnee());
        periodes.sort((a, b) -> {
            Long ma = a.getMois() != null ? a.getMois().getId() : 0L;
            Long mb = b.getMois() != null ? b.getMois().getId() : 0L;
            return ma.compareTo(mb);
        });
        List<PrintLs> result = new ArrayList<>();
        for (PeriodePaie pp : periodes) {
            List<BulletinPaie> bulletins = bulletinPaieRepository.findByPeriodePaieIdAndCalculerTrue(pp.getId());
            BigDecimal masse = BigDecimal.ZERO;
            for (BulletinPaie b : bulletins) {
                if (b.getTotalmassesalarial() != null) masse = masse.add(b.getTotalmassesalarial());
            }
            PrintLs dto = new PrintLs();
            dto.setS1(pp.getMois() != null ? pp.getMois().getMois() : "P" + pp.getId());
            dto.setValue1(masse);
            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }

    // ==================== DG: Répartition des coûts du personnel ====================

    @GetMapping("/couts-personnel")
    public ResponseEntity<List<PrintLs>> getCoutsPersonnel() {
        PeriodePaie periode = periodePaieService.findPeriodeactive();
        if (periode == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<BulletinPaie> bulletins = bulletinPaieRepository.findByPeriodePaieIdAndCalculerTrue(periode.getId());
        BigDecimal salaireBase = BigDecimal.ZERO, primes = BigDecimal.ZERO,
                transport = BigDecimal.ZERO, cnpsPatronal = BigDecimal.ZERO, autres = BigDecimal.ZERO;
        for (BulletinPaie b : bulletins) {
            if (b.getSalairbase() != null) salaireBase = salaireBase.add(b.getSalairbase());
            if (b.getPrimeanciennete() != null) primes = primes.add(b.getPrimeanciennete());
            if (b.getIndemniteTransport() != null) transport = transport.add(b.getIndemniteTransport());
            if (b.getTotalpatronal() != null) cnpsPatronal = cnpsPatronal.add(b.getTotalpatronal());
            if (b.getTotalmassesalarial() != null)
                autres = autres.add(b.getTotalmassesalarial());
        }
        autres = autres.subtract(salaireBase).subtract(primes).subtract(transport).subtract(cnpsPatronal);
        if (autres.compareTo(BigDecimal.ZERO) < 0) autres = BigDecimal.ZERO;
        List<PrintLs> result = new ArrayList<>();
        result.add(makePrintLs("Salaire base", salaireBase));
        result.add(makePrintLs("Primes", primes));
        result.add(makePrintLs("Transport", transport));
        result.add(makePrintLs("Charges patronales", cnpsPatronal));
        result.add(makePrintLs("Autres", autres));
        return ResponseEntity.ok(result);
    }

    // ==================== Helpers ====================

    private Exercice resolveExercice(Long exerciceId) {
        if (exerciceId != null) {
            return exerciceService.findExo(exerciceId);
        }
        return exerciceService.findExoactif();
    }

    private PrintLs createPrintLs(String label, int count) {
        PrintLs dto = new PrintLs();
        dto.setS1(label);
        dto.setTitle1(label);
        dto.setI1(count);
        return dto;
    }

    private PrintLs makePrintLs(String label, BigDecimal value) {
        PrintLs dto = new PrintLs();
        dto.setS1(label);
        dto.setTitle1(label);
        dto.setValue1(value != null ? value : BigDecimal.ZERO);
        return dto;
    }
}
