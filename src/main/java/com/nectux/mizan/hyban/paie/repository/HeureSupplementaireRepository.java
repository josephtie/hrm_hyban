package com.nectux.mizan.hyban.paie.repository;

import com.nectux.mizan.hyban.paie.entity.HeureSupplementaire;
import com.nectux.mizan.hyban.paie.enums.StatutHS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HeureSupplementaireRepository extends JpaRepository<HeureSupplementaire, Long> {

    List<HeureSupplementaire> findByPersonnelId(Long personnelId);

    List<HeureSupplementaire> findByPeriodePaieId(Long periodePaieId);

    List<HeureSupplementaire> findByStatut(StatutHS statut);

    List<HeureSupplementaire> findByPersonnelIdAndPeriodePaieId(Long personnelId, Long periodePaieId);

    List<HeureSupplementaire> findByDateTravailBetween(Date dateDebut, Date dateFin);

    List<HeureSupplementaire> findByPersonnelIdAndDateTravailBetween(Long personnelId, Date dateDebut, Date dateFin);

    List<HeureSupplementaire> findByStatutAndPeriodePaieId(StatutHS statut, Long periodePaieId);

    List<HeureSupplementaire> findAllByOrderByDateTravailDesc();
}
