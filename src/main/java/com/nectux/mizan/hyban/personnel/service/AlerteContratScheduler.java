package com.nectux.mizan.hyban.personnel.service;

import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.personnel.repository.ContratPersonnelRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class AlerteContratScheduler {

    private static final Logger logger = LogManager.getLogger(AlerteContratScheduler.class);

    @Autowired
    private ContratPersonnelRepository contratPersonnelRepository;

    @Scheduled(cron = "0 0 8 1 * ?")
    public void genererAlertesMensuelles() {
        logger.info(">>>>> DÉBUT GÉNÉRATION ALERTES CONTRATS - {}", new Date());

        Date now = new Date();
        int[] seuils = {90, 60, 30, 15};

        for (int seuil : seuils) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_MONTH, seuil);
            Date dateFin = cal.getTime();

            List<ContratPersonnel> contrats = contratPersonnelRepository.findContratsEcheance(now, dateFin);
            if (!contrats.isEmpty()) {
                logger.info(">>>>> ALERTE: {} contrat(s) arrivant à échéance dans {} jours", contrats.size(), seuil);
                for (ContratPersonnel cp : contrats) {
                    logger.info(">>>>>   - ID:{} | {} {} | Date fin: {} | Type: {}",
                            cp.getId(),
                            cp.getPersonnel().getNom(),
                            cp.getPersonnel().getPrenom(),
                            cp.getdFin(),
                            cp.getTypeContrat() != null ? cp.getTypeContrat().getLibelle() : "N/A");
                }
            }
        }

        List<ContratPersonnel> expires = contratPersonnelRepository.findContratsExpired(now);
        if (!expires.isEmpty()) {
            logger.info(">>>>> ALERTE: {} contrat(s) arrivés à échéance (date dépassée)", expires.size());
            for (ContratPersonnel cp : expires) {
                logger.info(">>>>>   - ID:{} | {} {} | Date fin: {}",
                        cp.getId(),
                        cp.getPersonnel().getNom(),
                        cp.getPersonnel().getPrenom(),
                        cp.getdFin());
            }
        }

        logger.info(">>>>> FIN GÉNÉRATION ALERTES CONTRATS");
    }
}
