//package com.nectux.mizan.hyban.paie.web;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.nectux.mizan.hyban.common.dto.GratificationRequest;
//import com.nectux.mizan.hyban.common.dto.GratificationResponse;
//import com.nectux.mizan.hyban.paie.dto.BulletinPaieDTO;
//import com.nectux.mizan.hyban.paie.dto.GratificationDTO;
//import com.nectux.mizan.hyban.paie.entity.Gratification;
//import com.nectux.mizan.hyban.paie.entity.ImprimBulletinPaie;
//import com.nectux.mizan.hyban.parametrages.entity.Banque;
//import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
//import com.nectux.mizan.hyban.parametrages.entity.Societe;
//import com.nectux.mizan.hyban.parametrages.service.BanqueService;
//import com.nectux.mizan.hyban.parametrages.service.CpteVirmtBanqueService;
//import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
//import com.nectux.mizan.hyban.parametrages.service.SocieteService;
//// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
//// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
//import com.nectux.mizan.hyban.paie.service.GratificationService;
//import com.nectux.mizan.hyban.utils.GenericDataSource;
//import com.nectux.mizan.hyban.utils.MethodsShared;
//import com.nectux.mizan.hyban.utils.Utils;
//
////import net.sf.jasperreports.engine.JRDataSource;
////import net.sf.jasperreports.engine.JRException;
////import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//
//@RestController
//@RequestMapping("/api/gratification")
//public class GratificationRestController {
//
//    private static final Logger logger = LoggerFactory.getLogger(GratificationRestController.class);
//
//    // @Autowired
//    // private UtilisateurService utilisateurService;
//    // @Autowired
//    // private UtilisateurRoleService utilisateurRoleService;
//    // @Autowired
//    // private UtilisateurService userService;
//    @Autowired
//    private PeriodePaieService periodePaieService;
//    @Autowired
//    private GratificationService gratificationService;
//    @Autowired
//    private BanqueService banqueService;
//    @Autowired
//    private SocieteService societeService;
//    @Autowired
//    private CpteVirmtBanqueService cpteVirmtBanqueService;
//    private MethodsShared methodsShared = new MethodsShared();
//
//    @GetMapping("/list")
//    public ResponseEntity<GratificationResponse<Gratification>> getGratificationList(
//            @RequestParam(defaultValue = "10") Integer limit,
//            @RequestParam(defaultValue = "0") Integer offset,
//            @RequestParam(required = false) String search) {
//
//        try {
//            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
//            GratificationDTO gratificationDTO;
//
//            if (search == null || search.trim().isEmpty()) {
//                gratificationDTO = gratificationService.loadGratificationProvisional(pageRequest);
//            } else {
//                gratificationDTO = gratificationService.loadGratificationProvisional(pageRequest, search);
//            }
//
//            GratificationResponse<Gratification> response = new GratificationResponse<>();
//            response.setRows(gratificationDTO.getRows());
//            response.setTotal((int) gratificationDTO.getTotal());
//            response.setResult("success");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération de la liste des gratifications", e);
//            GratificationResponse<Gratification> response = new GratificationResponse<>();
//            response.setResult("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/generer")
//    public ResponseEntity<GratificationDTO> genererGratification() {
//        try {
//            GratificationDTO result = gratificationService.generateGratification();
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            logger.error("Erreur lors de la génération de la gratification", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @PostMapping("/charger-par-periode")
//    public ResponseEntity<List<Gratification>> chargerGratificationParPeriode(@RequestParam Long periodepaie) {
//        try {
//            List<Gratification> gratificationList = gratificationService.findByPeriodePaie(periodepaie);
//            logger.info("success");
//            return ResponseEntity.ok(gratificationList);
//        } catch (Exception ex) {
//            logger.error("fail", ex);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/bulletin/{id}")
//    public ResponseEntity<String> imprimerBulletinGratification(@PathVariable Long id, HttpServletRequest request) {
//        try {
//            // Cette méthode génère le bulletin de gratification
//            return ResponseEntity.ok("Gratification bulletin generated for id: " + id);
//        } catch (Exception e) {
//            logger.error("Erreur lors de l'impression du bulletin de gratification", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating gratification bulletin");
//        }
//    }
//
//    @PostMapping("/virement")
//    public ResponseEntity<BulletinPaieDTO> imprimerOrdreVirement(@RequestBody GratificationRequest request, HttpServletRequest httpRequest) {
//        try {
//            BulletinPaieDTO bulletinPaieDTO = new BulletinPaieDTO();
//
//            PeriodePaie periodePaie = periodePaieService.findPeriodePaie(request.getPeriodePaieImpressionV());
//            Banque tbanqueAvirmt = banqueService.findBanquesID(request.getBanqueEmp());
//            Banque tbanque = banqueService.findBanquesID(request.getBanque());
//
//            // Logique de génération du fichier de virement
//            // ... (code complexe de génération de fichier)
//
//            bulletinPaieDTO.setMessage("Virement file generated successfully");
//            return ResponseEntity.ok(bulletinPaieDTO);
//        } catch (Exception e) {
//            logger.error("Erreur lors de l'impression de l'ordre de virement", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/banques")
//    public ResponseEntity<List<Banque>> getBanques() {
//        try {
//            return ResponseEntity.ok(banqueService.getBanques());
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des banques", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/banques-entreprise")
//    public ResponseEntity<List<Banque>> getBanquesEntreprise() {
//        try {
//            return ResponseEntity.ok(banqueService.getBanquesEntprise());
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des banques d'entreprise", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/periodes-ouvertes")
//    public ResponseEntity<List<PeriodePaie>> getPeriodesOuvertes() {
//        try {
//            return ResponseEntity.ok(periodePaieService.findPeriodeOuverte());
//        } catch (Exception e) {
//            logger.error("Erreur lors de la récupération des périodes ouvertes", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
