package com.nectux.mizan.hyban.paie.service;

import com.nectux.mizan.hyban.paie.entity.HeureSupplementaire;
import com.nectux.mizan.hyban.paie.entity.RegleHeureSupplementaire;
import com.nectux.mizan.hyban.paie.enums.StatutHS;
import com.nectux.mizan.hyban.paie.enums.TypeHS;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface HeureSupplementaireService {

    HeureSupplementaire create(HeureSupplementaire hs);

    HeureSupplementaire update(Long id, HeureSupplementaire hs);

    HeureSupplementaire findById(Long id);

    List<HeureSupplementaire> findAll();

    List<HeureSupplementaire> findWithFilters(Long personnelId, Long periodePaieId, StatutHS statut, Date dateDebut, Date dateFin);

    HeureSupplementaire soumettre(Long id);

    HeureSupplementaire valider(Long id, String validatedBy);

    HeureSupplementaire rejeter(Long id, String motifRejet);

    HeureSupplementaire integrerPaie(Long id);

    void delete(Long id);

    List<RegleHeureSupplementaire> findAllRegles();

    RegleHeureSupplementaire findRegleByType(TypeHS typeHS);

    BigDecimal calculerMontant(BigDecimal nombreHeures, BigDecimal tauxHoraire, BigDecimal coefficient);
}
