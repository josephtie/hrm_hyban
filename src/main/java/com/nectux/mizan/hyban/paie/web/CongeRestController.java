package com.nectux.mizan.hyban.paie.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.CongeRequest;
import com.nectux.mizan.hyban.common.dto.CongeResponse;
import com.nectux.mizan.hyban.paie.dto.CongeDTO;
import com.nectux.mizan.hyban.paie.entity.Conge;
import com.nectux.mizan.hyban.paie.entity.ImprimBulletinPaie;
import com.nectux.mizan.hyban.paie.repository.CongeRepository;
import com.nectux.mizan.hyban.paie.service.CongeService;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
import com.nectux.mizan.hyban.personnel.repository.ContratPersonnelRepository;
import com.nectux.mizan.hyban.personnel.repository.PersonnelRepository;
import com.nectux.mizan.hyban.utils.DifferenceDate;
import com.nectux.mizan.hyban.utils.GenericDataSource;
import com.nectux.mizan.hyban.utils.ProvisionConge;
import com.nectux.mizan.hyban.utils.Utils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/api/conge")
public class CongeRestController {

    private static final Logger logger = LoggerFactory.getLogger(CongeRestController.class);

    @Autowired
    private CongeService congeService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private ContratPersonnelRepository contratPersonnelRepository;
    @Autowired
    private CongeRepository congeRepository;
    @Autowired
    private SocieteService societeService;
    @Autowired
    private PersonnelRepository personnelRepository;

    @GetMapping("/provision/list")
    public ResponseEntity<CongeResponse<?>> getProvisionCongeList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PageRequest page = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            CongeDTO congeDTO;
            
            if (search == null || search.trim().isEmpty()) {
                congeDTO = congeService.loadCongeProvisional(page);
            } else {
                congeDTO = congeService.loadCongeProvisional(page, search);
            }
            
            CongeResponse<Object> response = new CongeResponse<>();
            response.setRows(congeDTO.getRows().stream().map(conge -> (Object) conge).collect(Collectors.toList()));
            response.setTotal((int)congeDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la provision congé", e);
            CongeResponse<Object> response = new CongeResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/generer-bulletin")
    public ResponseEntity<CongeDTO> genererBulletinConge(@RequestBody CongeRequest request) {
        try {
            CongeDTO congeDTO = congeService.genererBulletinConge(request.getId());
            return ResponseEntity.ok(congeDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la génération du bulletin congé", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bulletin/{id}")
    public ResponseEntity<Conge> getBulletinConge(@PathVariable Long id, HttpServletRequest request) {
        try {
            Conge bullconge = congeService.findconge(id);
            
            if (bullconge.getId() == null) {
                return ResponseEntity.notFound().build();
            }

            // Préparation des données pour le bulletin
            List<ImprimBulletinPaie> listImprimBulletinPaie = new ArrayList<>();
            List<ImprimBulletinPaie> listImprimBulletinPaieEngt = new ArrayList<>();
            List<ImprimBulletinPaie> listImprimBulletinPaieIndem = new ArrayList<>();

            // Calcul des allocations congé
            ImprimBulletinPaie imprimBulletinPaieSB = new ImprimBulletinPaie();
            imprimBulletinPaieSB.setLibelle("Conge de base");
            imprimBulletinPaieSB.setTaux(30D);
            imprimBulletinPaieSB.setBase(bullconge.getBaseImposableAllocationConge());
            imprimBulletinPaieSB.setGain(bullconge.getBaseImposableAllocationConge());
            listImprimBulletinPaie.add(imprimBulletinPaieSB);

            ImprimBulletinPaie imprimBulletinPaieBrutImpos = new ImprimBulletinPaie();
            imprimBulletinPaieBrutImpos.setLibelle("Base Imposable Des Allocations Conges(1)");
            imprimBulletinPaieBrutImpos.setGain(bullconge.getBaseImposableAllocationConge());
            listImprimBulletinPaie.add(imprimBulletinPaieBrutImpos);

            ImprimBulletinPaie imprimBulletinPaieIts = new ImprimBulletinPaie();
            imprimBulletinPaieIts.setLibelle("Impot sur salaire");
            imprimBulletinPaieIts.setTaux(1.2D);
            imprimBulletinPaieIts.setBase(bullconge.getBaseImposableAllocationConge());
            imprimBulletinPaieIts.setRetenue(bullconge.getIts());
            listImprimBulletinPaie.add(imprimBulletinPaieIts);

            ImprimBulletinPaie imprimBulletinPaieCn = new ImprimBulletinPaie();
            imprimBulletinPaieCn.setLibelle("Contribution nationale");
            imprimBulletinPaieCn.setRetenue(bullconge.getCn());
            listImprimBulletinPaie.add(imprimBulletinPaieCn);

            ImprimBulletinPaie imprimBulletinPaieIgr = new ImprimBulletinPaie();
            imprimBulletinPaieIgr.setLibelle("Impot general sur revenu");
            imprimBulletinPaieIgr.setRetenue(bullconge.getIgr());
            listImprimBulletinPaie.add(imprimBulletinPaieIgr);

            ImprimBulletinPaie imprimBulletinPaieCnps = new ImprimBulletinPaie();
            imprimBulletinPaieCnps.setLibelle("CNPS ");
            imprimBulletinPaieCnps.setRetenue(bullconge.getCnps());
            listImprimBulletinPaie.add(imprimBulletinPaieCnps);

            ImprimBulletinPaie imprimBulletinPaieRetfiscsoc = new ImprimBulletinPaie();
            imprimBulletinPaieRetfiscsoc.setLibelle("Total retenues fiscales et sociales(2)");
            imprimBulletinPaieRetfiscsoc.setRetenue(bullconge.getCnps() + bullconge.getIgr() + bullconge.getCn() + bullconge.getIts());
            listImprimBulletinPaie.add(imprimBulletinPaieRetfiscsoc);

            ImprimBulletinPaie imprimBulletinPaieTotAutrRet = new ImprimBulletinPaie();
            imprimBulletinPaieTotAutrRet.setLibelle("TOTAL AUTRES RETENUES(3)");
            imprimBulletinPaieTotAutrRet.setRetenue(0d);
            listImprimBulletinPaieEngt.add(imprimBulletinPaieTotAutrRet);

            ImprimBulletinPaie imprimBulletinPaieTotalR = new ImprimBulletinPaie();
            imprimBulletinPaieTotalR.setLibelle("TOTAL (2+3)");
            imprimBulletinPaieTotalR.setRetenue(imprimBulletinPaieRetfiscsoc.getRetenue());
            listImprimBulletinPaieEngt.add(imprimBulletinPaieTotalR);

            ImprimBulletinPaie imprimBulletinPaieRepres = new ImprimBulletinPaie();
            imprimBulletinPaieRepres.setLibelle("AUTRE INDEMNITES NON IMPOSABLE");
            imprimBulletinPaieRepres.setTaux(30D);
            imprimBulletinPaieRepres.setGain(0d);
            listImprimBulletinPaieIndem.add(imprimBulletinPaieRepres);

            ImprimBulletinPaie imprimBulletinPaieTOTgainsNonImpos = new ImprimBulletinPaie();
            imprimBulletinPaieTOTgainsNonImpos.setLibelle("TOTAL GAINS NON IMPOSABLES (4)");
            imprimBulletinPaieTOTgainsNonImpos.setTaux(30D);
            imprimBulletinPaieTOTgainsNonImpos.setGain(0d);
            listImprimBulletinPaieIndem.add(imprimBulletinPaieTOTgainsNonImpos);

            ImprimBulletinPaie imprimBulletinPaieTBrutMens = new ImprimBulletinPaie();
            imprimBulletinPaieTBrutMens.setLibelle("SALAIRE BRUT MENSUEL (1+4) )");
            imprimBulletinPaieTBrutMens.setTaux(30D);
            imprimBulletinPaieTBrutMens.setGain(bullconge.getBaseImposableAllocationConge());
            listImprimBulletinPaieIndem.add(imprimBulletinPaieTBrutMens);

            // Calcul du net à payer
            bullconge.setAllocationCongeNet(bullconge.getBaseImposableAllocationConge() - bullconge.getCnps() - bullconge.getIgr() - bullconge.getCn() - bullconge.getIts());
            bullconge.setMontantNetPayer(Utils.formattingAmount(bullconge.getAllocationCongeNet()));

            bullconge.setListImprimBulletinPaie(listImprimBulletinPaie);
            bullconge.setListImprimBulletinPaieEngagement(listImprimBulletinPaieEngt);
            bullconge.setListImprimBulletinPaieIndemniteNonImp(listImprimBulletinPaieIndem);

            // Calcul des jours de congé
            Date dateRetourDernierConge = null;
            if (bullconge.getContratPersonnel().getPersonnel().getDateRetourcge() == null) {
                dateRetourDernierConge = bullconge.getContratPersonnel().getPersonnel().getDateArrivee();
            } else {
                dateRetourDernierConge = bullconge.getContratPersonnel().getPersonnel().getDateRetourcge();
            }

            int tps = ProvisionConge.calculerTempsPresence(dateRetourDernierConge, bullconge.getPeriodePaie().getDatefin());
            if (tps < 0) tps = tps * -1;

            int rf = (int) (tps * 2.2 * 1.25);
            bullconge.setNbcongedu(String.valueOf(bullconge.getTempsOfpresence()));
            bullconge.setTpsdepresence(String.valueOf(bullconge.getMoisOfpresence()));

            // Calcul des jours supplémentaires
            Double[] ancienete = calculAnciennete(bullconge.getContratPersonnel().getCategorie().getSalaireDeBase(), bullconge.getContratPersonnel().getPersonnel().getDateArrivee());
            double newancienete;
            if (bullconge.getContratPersonnel().getAncienneteInitial() != 0) {
                newancienete = ancienete[1] + bullconge.getContratPersonnel().getAncienneteInitial();
            } else {
                newancienete = ancienete[1];
            }
            double anc = (int) newancienete;

            int jourSuppAnc = 0;
            int jourSuppDam = 0;
            int jourSuppMed = 0;

            if (anc > 5 && anc <= 10) jourSuppAnc = 1;
            if (anc > 10 && anc <= 15) jourSuppAnc = 2;
            if (anc > 15 && anc <= 20) jourSuppAnc = 3;
            if (anc > 20 && anc <= 25) jourSuppAnc = 5;
            if (anc > 25 && anc <= 30) jourSuppAnc = 7;
            if (anc > 30) jourSuppAnc = 8;

            Double age = DifferenceDate.valAge(new Date(), bullconge.getContratPersonnel().getPersonnel().getDateNaissance());
            if (bullconge.getContratPersonnel().getPersonnel().getSexe().equals("Feminin") && age <= 21 && bullconge.getContratPersonnel().getPersonnel().getNombrEnfant() > 0) {
                jourSuppDam = 2 * bullconge.getContratPersonnel().getPersonnel().getNombrEnfant();
            }
            if (bullconge.getContratPersonnel().getPersonnel().getSexe().equals("Feminin") && age > 21 && bullconge.getContratPersonnel().getPersonnel().getNombrEnfant() > 0) {
                if (bullconge.getContratPersonnel().getPersonnel().getNombrEnfant() >= 4) jourSuppDam = 2 * 1;
                if (bullconge.getContratPersonnel().getPersonnel().getNombrEnfant() >= 5) jourSuppDam = 2 * 2;
                if (bullconge.getContratPersonnel().getPersonnel().getNombrEnfant() >= 6) jourSuppDam = 2 * 3;
                if (bullconge.getContratPersonnel().getPersonnel().getNombrEnfant() >= 7) jourSuppDam = 2 * 4;
                if (bullconge.getContratPersonnel().getPersonnel().getNombrEnfant() >= 8) jourSuppDam = 2 * 5;
                if (bullconge.getContratPersonnel().getPersonnel().getNombrEnfant() >= 9) jourSuppDam = 2 * 6;
            }

            if (bullconge.getContratPersonnel().getPersonnel().getSituationMedaille() == 1) {
                jourSuppMed = 1;
            }

            int rfp = (int) (jourSuppAnc + jourSuppDam + jourSuppMed);
            bullconge.setJouradditionel(String.valueOf(jourSuppAnc + jourSuppDam + jourSuppMed));

            return ResponseEntity.ok(bullconge);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du bulletin congé", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/livre-paie/periode")
    public ResponseEntity<List<Conge>> chargerCongeParPeriode(@RequestBody CongeRequest request) {
        try {
            List<Conge> congeList = congeService.loadLivrePaieCongePeriode(request.getPeriodepaie());
            return ResponseEntity.ok(congeList);
        } catch (Exception e) {
            logger.error("Erreur lors du chargement des congés par période", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
