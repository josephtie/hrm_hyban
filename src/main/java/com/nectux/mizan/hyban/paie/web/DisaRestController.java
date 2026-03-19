//package com.nectux.mizan.hyban.paie.web;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.nectux.mizan.hyban.common.dto.DisaRequest;
//import com.nectux.mizan.hyban.common.dto.DisaResponse;
//import com.nectux.mizan.hyban.paie.dto.DisaDTO;
//import com.nectux.mizan.hyban.paie.dto.DisaMensuelDTO;
//import com.nectux.mizan.hyban.paie.dto.Etat301DTO;
//import com.nectux.mizan.hyban.paie.entity.BulletinPaie;
//import com.nectux.mizan.hyban.paie.entity.ImprimBulletinPaie;
//import com.nectux.mizan.hyban.paie.service.BulletinPaieService;
//import com.nectux.mizan.hyban.parametrages.entity.Exercice;
//import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
//import com.nectux.mizan.hyban.parametrages.entity.Societe;
//import com.nectux.mizan.hyban.parametrages.repository.SocieteRepository;
//import com.nectux.mizan.hyban.parametrages.service.ExerciceService;
//import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
//import com.nectux.mizan.hyban.parametrages.service.SocieteService;
//import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
//import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
//import com.nectux.mizan.hyban.personnel.entity.Personnel;
//import com.nectux.mizan.hyban.personnel.repository.PersonnelRepository;
//import com.nectux.mizan.hyban.utils.DateManager;
//import com.nectux.mizan.hyban.utils.GenericDataSource;
//import com.nectux.mizan.hyban.utils.Utils;
//
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.util.JRLoader;
//
//@RestController
//@RequestMapping("/api/paie/disa")
//public class DisaRestController {
//
//    private static final Logger logger = LoggerFactory.getLogger(DisaRestController.class);
//
//    @Autowired
//    private UtilisateurService userService;
//    @Autowired
//    private PeriodePaieService periodePaieService;
//    @Autowired
//    private BulletinPaieService bulletinPaieService;
//    @Autowired
//    private ExerciceService exerciceService;
//    @Autowired
//    private PersonnelRepository personnelRepository;
//    @Autowired
//    private SocieteRepository societeRepository;
//    @Autowired
//    private SocieteService societeService;
//    @Autowired
//    private UtilisateurService utilisateurService;
//    @Autowired
//    private UtilisateurRoleService utilisateurRoleService;
//
//    @GetMapping("/its")
//    public ResponseEntity<String> genererIts(@RequestParam Long periode, @RequestParam(required = false) String print, HttpServletRequest request) {
//        try {
//            // Cette méthode génère le rapport ITS mensuel
//            // Retourne une vue Jasper ou un PDF selon le paramètre print
//            return ResponseEntity.ok("ITS report generated for period: " + periode);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la génération du rapport ITS", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating ITS report");
//        }
//    }
//
//    @GetMapping("/etat301")
//    public ResponseEntity<String> genererEtat301(@RequestParam Long annee, @RequestParam(required = false) String print, HttpServletRequest request) {
//        try {
//            // Cette méthode génère l'état 301 annuel
//            return ResponseEntity.ok("Etat 301 report generated for year: " + annee);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la génération de l'état 301", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating Etat 301 report");
//        }
//    }
//
//    @GetMapping("/etat3")
//    public ResponseEntity<String> genererEtat3(@RequestParam Long annee, @RequestParam(required = false) String print, HttpServletRequest request) {
//        try {
//            // Cette méthode génère l'état 3 annuel
//            return ResponseEntity.ok("Etat 3 report generated for year: " + annee);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la génération de l'état 3", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating Etat 3 report");
//        }
//    }
//
//    @GetMapping("/exercices")
//    public ResponseEntity<List<Exercice>> getExercices() {
//        try {
//            List<Exercice> exercices = exerciceService.findArecuperer();
//            return ResponseEntity.ok(exercices);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des exercices", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/periodes")
//    public ResponseEntity<List<PeriodePaie>> getPeriodes() {
//        try {
//            List<PeriodePaie> periodes = periodePaieService.listAllperiode();
//            return ResponseEntity.ok(periodes);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des périodes", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
////    @GetMapping("/societes")
////    public ResponseEntity<List<Societe>> getSocietes() {
////        try {
////            List<Societe> societes = societeService.findbyRaisoncoc("ghfg");
////            return ResponseEntity.ok(societes);
////        } catch (Exception e) {
////            logger.error("Erreur lors de la récupération des sociétés", e);
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
////        }
////    }
//}
