package com.nectux.mizan.hyban.paie.config;

import com.nectux.mizan.hyban.paie.entity.RegleHeureSupplementaire;
import com.nectux.mizan.hyban.paie.enums.TypeHS;
import com.nectux.mizan.hyban.paie.repository.RegleHeureSupplementaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(2)
public class RegleHeureSupplementaireInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RegleHeureSupplementaireInitializer.class);

    @Autowired
    private RegleHeureSupplementaireRepository regleRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info(">>> Initialisation des règles heures supplémentaires...");

        createRegleIfNotExists("HS015", "HS 15%", TypeHS.HS_15, new BigDecimal("15"), new BigDecimal("1.15"));
        createRegleIfNotExists("HS050", "HS 50%", TypeHS.HS_50, new BigDecimal("50"), new BigDecimal("1.50"));
        createRegleIfNotExists("HS075", "HS 75%", TypeHS.HS_75, new BigDecimal("75"), new BigDecimal("1.75"));
        createRegleIfNotExists("HS100", "HS 100%", TypeHS.HS_100, new BigDecimal("100"), new BigDecimal("2.00"));

        logger.info(">>> Initialisation des règles HS terminée");
    }

    private void createRegleIfNotExists(String code, String libelle, TypeHS typeHS, BigDecimal tauxMajoration, BigDecimal coefficient) {
        if (regleRepository.findByCode(code).isEmpty()) {
            RegleHeureSupplementaire regle = new RegleHeureSupplementaire();
            regle.setCode(code);
            regle.setLibelle(libelle);
            regle.setTypeHS(typeHS);
            regle.setTauxMajoration(tauxMajoration);
            regle.setCoefficient(coefficient);
            regle.setActive(true);
            regleRepository.save(regle);
            logger.info("  Règle créée: {}", code);
        }
    }
}
