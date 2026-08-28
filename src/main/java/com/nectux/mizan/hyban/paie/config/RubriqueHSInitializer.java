package com.nectux.mizan.hyban.paie.config;

import com.nectux.mizan.hyban.parametrages.entity.Rubrique;
import com.nectux.mizan.hyban.parametrages.repository.RubriqueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(2)
public class RubriqueHSInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RubriqueHSInitializer.class);

    @Autowired
    private RubriqueRepository rubriqueRepository;

    @Override
    public void run(String... args) throws Exception {
        createRubriqueIfNotFound("HS015", "Heures supplémentaires 15%", new BigDecimal("1.15"));
        createRubriqueIfNotFound("HS050", "Heures supplémentaires 50%", new BigDecimal("1.50"));
        createRubriqueIfNotFound("HS075", "Heures supplémentaires 75%", new BigDecimal("1.75"));
        createRubriqueIfNotFound("HS100", "Heures supplémentaires 100%", new BigDecimal("2.00"));
    }

    private void createRubriqueIfNotFound(String code, String libelle, BigDecimal taux) {
        Rubrique existing = rubriqueRepository.findByCode(code);
        if (existing != null) {
            logger.info("Rubrique {} already exists, skipping", code);
            return;
        }

        Rubrique rubrique = new Rubrique();
        rubrique.setCode(code);
        rubrique.setLibelle(libelle);
        rubrique.setTaux(taux);
        rubrique.setEtatImposition(1);
        rubrique.setCategorie("PRIME");
        rubrique.setTypeRubrique("GAIN");
        rubrique.setModeCalcul("FIXE");
        rubrique.setCotisable(true);
        rubrique.setActive(true);
        rubrique.setPermanent(false);
        rubrique.setSpeciale(false);
        rubrique.setDateCreate(new java.util.Date());

        rubriqueRepository.save(rubrique);
        logger.info("Rubrique {} created successfully", code);
    }
}
