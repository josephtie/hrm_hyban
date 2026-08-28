package com.nectux.mizan.hyban.paie.service.impl;

import com.nectux.mizan.hyban.paie.entity.HeureSupplementaire;
import com.nectux.mizan.hyban.paie.entity.PrimePersonnel;
import com.nectux.mizan.hyban.paie.entity.RegleHeureSupplementaire;
import com.nectux.mizan.hyban.paie.enums.StatutHS;
import com.nectux.mizan.hyban.paie.enums.TypeHS;
import com.nectux.mizan.hyban.paie.repository.HeureSupplementaireRepository;
import com.nectux.mizan.hyban.paie.repository.PrimePersonnelRepository;
import com.nectux.mizan.hyban.paie.repository.RegleHeureSupplementaireRepository;
import com.nectux.mizan.hyban.paie.service.HeureSupplementaireService;
import com.nectux.mizan.hyban.parametrages.entity.Rubrique;
import com.nectux.mizan.hyban.parametrages.repository.RubriqueRepository;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.personnel.repository.ContratPersonnelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HeureSupplementaireServiceImpl implements HeureSupplementaireService {

    @Autowired
    private HeureSupplementaireRepository heureSuppRepository;

    @Autowired
    private RegleHeureSupplementaireRepository regleRepository;

    @Autowired
    private PrimePersonnelRepository primePersonnelRepository;

    @Autowired
    private RubriqueRepository rubriqueRepository;

    @Autowired
    private ContratPersonnelRepository contratPersonnelRepository;

    private static final String HS_15_CODE = "HS015";
    private static final String HS_50_CODE = "HS050";
    private static final String HS_75_CODE = "HS075";
    private static final String HS_100_CODE = "HS100";

    @Override
    public HeureSupplementaire create(HeureSupplementaire hs) {
        applyRegleCalcul(hs);
        hs.setStatut(StatutHS.BROUILLON);
        return heureSuppRepository.save(hs);
    }

    @Override
    public HeureSupplementaire update(Long id, HeureSupplementaire hs) {
        HeureSupplementaire existing = findById(id);
        if (existing.getStatut() != StatutHS.BROUILLON && existing.getStatut() != StatutHS.REJETE) {
            throw new IllegalStateException("Modification autorisée uniquement pour les statuts BROUILLON et REJETE");
        }

        existing.setPersonnel(hs.getPersonnel());
        existing.setDateTravail(hs.getDateTravail());
        existing.setNombreHeures(hs.getNombreHeures());
        existing.setTypeHS(hs.getTypeHS());
        existing.setMotif(hs.getMotif());
        existing.setCommentaire(hs.getCommentaire());
        existing.setPeriodePaie(hs.getPeriodePaie());
        existing.setTauxHoraire(hs.getTauxHoraire());

        applyRegleCalcul(existing);

        if (existing.getStatut() == StatutHS.REJETE) {
            existing.setStatut(StatutHS.BROUILLON);
            existing.setMotifRejet(null);
        }

        return heureSuppRepository.save(existing);
    }

    @Override
    public HeureSupplementaire findById(Long id) {
        return heureSuppRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Heure supplémentaire non trouvée: " + id));
    }

    @Override
    public List<HeureSupplementaire> findAll() {
        return heureSuppRepository.findAllByOrderByDateTravailDesc();
    }

    @Override
    public List<HeureSupplementaire> findWithFilters(Long personnelId, Long periodePaieId, StatutHS statut, Date dateDebut, Date dateFin) {
        List<HeureSupplementaire> all = findAll();

        return all.stream()
                .filter(hs -> personnelId == null || hs.getPersonnel().getId().equals(personnelId))
                .filter(hs -> periodePaieId == null || hs.getPeriodePaie().getId().equals(periodePaieId))
                .filter(hs -> statut == null || hs.getStatut() == statut)
                .filter(hs -> dateDebut == null || !hs.getDateTravail().before(dateDebut))
                .filter(hs -> dateFin == null || !hs.getDateTravail().after(dateFin))
                .collect(Collectors.toList());
    }

    @Override
    public HeureSupplementaire soumettre(Long id) {
        HeureSupplementaire hs = findById(id);
        if (hs.getStatut() != StatutHS.BROUILLON && hs.getStatut() != StatutHS.REJETE) {
            throw new IllegalStateException("Soumission autorisée uniquement depuis BROUILLON ou REJETE");
        }
        hs.setStatut(StatutHS.A_VALIDER);
        hs.setDateSoumission(new Date());
        hs.setMotifRejet(null);
        return heureSuppRepository.save(hs);
    }

    @Override
    public HeureSupplementaire valider(Long id, String validatedBy) {
        HeureSupplementaire hs = findById(id);
        if (hs.getStatut() != StatutHS.A_VALIDER) {
            throw new IllegalStateException("Validation autorisée uniquement depuis A_VALIDER");
        }
        hs.setStatut(StatutHS.VALIDE);
        hs.setDateValidation(new Date());
        hs.setValidatedBy(validatedBy);
        return heureSuppRepository.save(hs);
    }

    @Override
    public HeureSupplementaire rejeter(Long id, String motifRejet) {
        HeureSupplementaire hs = findById(id);
        if (hs.getStatut() != StatutHS.A_VALIDER) {
            throw new IllegalStateException("Rejet autorisé uniquement depuis A_VALIDER");
        }
        if (motifRejet == null || motifRejet.trim().isEmpty()) {
            throw new IllegalArgumentException("Le motif de rejet est obligatoire");
        }
        hs.setStatut(StatutHS.REJETE);
        hs.setMotifRejet(motifRejet);
        return heureSuppRepository.save(hs);
    }

    @Override
    public HeureSupplementaire integrerPaie(Long id) {
        HeureSupplementaire hs = findById(id);
        if (hs.getStatut() != StatutHS.VALIDE) {
            throw new IllegalStateException("Intégration autorisée uniquement depuis VALIDE");
        }

        String rubriqueCode = getRubriqueCodeForType(hs.getTypeHS());
        Rubrique rubrique = rubriqueRepository.findByCode(rubriqueCode);
        if (rubrique == null) {
            throw new IllegalStateException("Rubrique non trouvée pour le code: " + rubriqueCode + ". Veuillez créer la rubrique correspondante.");
        }

        ContratPersonnel contrat = contratPersonnelRepository
                .findTopByPersonnelIdAndStatutTrueOrderByIdDesc(hs.getPersonnel().getId())
                .orElseThrow(() -> new EntityNotFoundException("Aucun contrat actif trouvé pour le personnel: " + hs.getPersonnel().getId()));

        PrimePersonnel prime = new PrimePersonnel();
        prime.setPrime(rubrique);
        prime.setContratPersonnel(contrat);
        prime.setPeriode(hs.getPeriodePaie());
        prime.setMontant(hs.getMontant());
        prime.setValeur(hs.getNombreHeures().intValue());
        prime.setDateSaisie(new Date());
        primePersonnelRepository.save(prime);

        hs.setStatut(StatutHS.INTEGRE_PAIE);
        hs.setDateIntegrationPaie(new Date());
        return heureSuppRepository.save(hs);
    }

    @Override
    public void delete(Long id) {
        HeureSupplementaire hs = findById(id);
        if (hs.getStatut() != StatutHS.BROUILLON) {
            throw new IllegalStateException("Suppression autorisée uniquement pour le statut BROUILLON");
        }
        heureSuppRepository.delete(hs);
    }

    @Override
    public List<RegleHeureSupplementaire> findAllRegles() {
        return regleRepository.findAll();
    }

    @Override
    public RegleHeureSupplementaire findRegleByType(TypeHS typeHS) {
        return regleRepository.findByTypeHS(typeHS)
                .orElseThrow(() -> new EntityNotFoundException("Règle non trouvée pour le type: " + typeHS));
    }

    @Override
    public BigDecimal calculerMontant(BigDecimal nombreHeures, BigDecimal tauxHoraire, BigDecimal coefficient) {
        if (nombreHeures == null || tauxHoraire == null || coefficient == null) {
            return BigDecimal.ZERO;
        }
        return nombreHeures.multiply(tauxHoraire).multiply(coefficient).setScale(2, RoundingMode.HALF_UP);
    }

    private void applyRegleCalcul(HeureSupplementaire hs) {
        RegleHeureSupplementaire regle = findRegleByType(hs.getTypeHS());
        hs.setTauxMajoration(regle.getTauxMajoration());
        hs.setCoefficient(regle.getCoefficient());
        if (hs.getTauxHoraire() != null && hs.getNombreHeures() != null) {
            hs.setMontant(calculerMontant(hs.getNombreHeures(), hs.getTauxHoraire(), hs.getCoefficient()));
        }
    }

    private String getRubriqueCodeForType(TypeHS typeHS) {
        switch (typeHS) {
            case HS_15: return HS_15_CODE;
            case HS_50: return HS_50_CODE;
            case HS_75: return HS_75_CODE;
            case HS_100: return HS_100_CODE;
            default: throw new IllegalArgumentException("Type HS non supporté: " + typeHS);
        }
    }
}
