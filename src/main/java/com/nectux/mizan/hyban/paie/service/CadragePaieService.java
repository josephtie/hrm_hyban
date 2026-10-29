package com.nectux.mizan.hyban.paie.service;

import com.nectux.mizan.hyban.paie.dto.CadragePaieDTO;
import com.nectux.mizan.hyban.paie.entity.BulletinPaie;
import com.nectux.mizan.hyban.parametrages.entity.Mois;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.repository.MoisRepository;
import com.nectux.mizan.hyban.parametrages.repository.PeriodePaieRepository;
import com.nectux.mizan.hyban.paie.repository.BulletinPaieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CadragePaieService {

    @Autowired
    private BulletinPaieRepository bulletinPaieRepository;

    @Autowired
    private PeriodePaieRepository periodePaieRepository;

    @Autowired
    private MoisRepository moisRepository;

    public CadragePaieDTO calculerCadrage(Long periodeIdCourant) {
        // Récupérer la période courante
        PeriodePaie periodeCourant = periodePaieRepository.findById(periodeIdCourant).orElse(null);
        if (periodeCourant == null) {
            throw new IllegalArgumentException("Période courante non trouvée");
        }

        // Trouver la période précédente (même année, mois précédent)
        PeriodePaie periodePrecedente = trouverPeriodePrecedente(periodeCourant);
        if (periodePrecedente == null) {
            throw new IllegalArgumentException("Période précédente non trouvée pour comparer");
        }

        // Récupérer les bulletins des deux périodes
        List<BulletinPaie> bulletinsCourant = bulletinPaieRepository.findByPeriodePaieId(periodeIdCourant);
        List<BulletinPaie> bulletinsPrecedent = bulletinPaieRepository.findByPeriodePaieId(periodePrecedente.getId());

        // Créer le DTO de résultat
        CadragePaieDTO dto = new CadragePaieDTO();
        dto.setPeriodeCourant(periodeCourant.getAffiche());
        dto.setPeriodePrecedente(periodePrecedente.getAffiche());

        // Calculer le cadrage global
        dto.setGlobal(calculerCadrageGlobal(bulletinsCourant, bulletinsPrecedent));

        // Calculer les écarts par personnel
        dto.setEcartsPersonnels(calculerEcartsPersonnels(bulletinsCourant, bulletinsPrecedent));

        return dto;
    }

    private PeriodePaie trouverPeriodePrecedente(PeriodePaie periodeCourant) {
        if (periodeCourant.getMois() == null || periodeCourant.getAnnee() == null) {
            return null;
        }

        // Trouver le mois précédent
        List<Mois> tousLesMois = moisRepository.findAll();
        int ordreMoisCourant = trouverOrdreMois(periodeCourant.getMois().getMois(), tousLesMois);
        final int ordreMoisPrecedent;
        if (ordreMoisCourant > 1) {
            ordreMoisPrecedent = ordreMoisCourant - 1;
        } else {
            ordreMoisPrecedent = tousLesMois.size(); // Décembre
        }

        Mois moisPrecedent = tousLesMois.stream()
                .filter(m -> trouverOrdreMois(m.getMois(), tousLesMois) == ordreMoisPrecedent)
                .findFirst()
                .orElse(null);

        if (moisPrecedent == null) {
            return null;
        }

        // Chercher la période avec ce mois et la même année
        return periodePaieRepository.findByAnneeIdAndMoisId(periodeCourant.getAnnee().getId(), moisPrecedent.getId());
    }

    private int trouverOrdreMois(String libelleMois, List<Mois> tousLesMois) {
        for (int i = 0; i < tousLesMois.size(); i++) {
            if (tousLesMois.get(i).getMois().equalsIgnoreCase(libelleMois)) {
                return i + 1;
            }
        }
        return 1;
    }

    private CadragePaieDTO.CadrageGlobalDTO calculerCadrageGlobal(
            List<BulletinPaie> bulletinsCourant,
            List<BulletinPaie> bulletinsPrecedent) {

        CadragePaieDTO.CadrageGlobalDTO global = new CadragePaieDTO.CadrageGlobalDTO();

        // Effectif
        global.setEffectifCourant(bulletinsCourant.size());
        global.setEffectifPrecedent(bulletinsPrecedent.size());
        global.setEcartEffectif(bulletinsCourant.size() - bulletinsPrecedent.size());
        global.setEcartEffectifPourcent(calculerPourcentage(
                BigDecimal.valueOf(bulletinsCourant.size()),
                BigDecimal.valueOf(bulletinsPrecedent.size())
        ));

        // Salaires de base
        BigDecimal salaireBaseCourant = bulletinsCourant.stream()
                .map(b -> b.getSalairbase() != null ? b.getSalairbase() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal salaireBasePrecedent = bulletinsPrecedent.stream()
                .map(b -> b.getSalairbase() != null ? b.getSalairbase() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        global.setSalaireBaseCourant(salaireBaseCourant);
        global.setSalaireBasePrecedent(salaireBasePrecedent);
        global.setEcartSalaireBase(salaireBaseCourant.subtract(salaireBasePrecedent));
        global.setEcartSalaireBasePourcent(calculerPourcentage(salaireBaseCourant, salaireBasePrecedent));

        // Primes & indemnités (brut imposable - salaire de base - sursalaire)
        BigDecimal primesCourant = bulletinsCourant.stream()
                .map(b -> b.getBrutImposable() != null ? b.getBrutImposable() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(salaireBaseCourant)
                .subtract(bulletinsCourant.stream()
                        .map(b -> b.getSursalaire() != null ? b.getSursalaire() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal primesPrecedent = bulletinsPrecedent.stream()
                .map(b -> b.getBrutImposable() != null ? b.getBrutImposable() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(salaireBasePrecedent)
                .subtract(bulletinsPrecedent.stream()
                        .map(b -> b.getSursalaire() != null ? b.getSursalaire() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        global.setPrimesIndemnitesCourant(primesCourant);
        global.setPrimesIndemnitesPrecedent(primesPrecedent);
        global.setEcartPrimesIndemnites(primesCourant.subtract(primesPrecedent));
        global.setEcartPrimesIndemnitesPourcent(calculerPourcentage(primesCourant, primesPrecedent));

        // Total brut
        BigDecimal totalBrutCourant = bulletinsCourant.stream()
                .map(b -> b.getTotalbrut() != null ? b.getTotalbrut() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalBrutPrecedent = bulletinsPrecedent.stream()
                .map(b -> b.getTotalbrut() != null ? b.getTotalbrut() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        global.setTotalBrutCourant(totalBrutCourant);
        global.setTotalBrutPrecedent(totalBrutPrecedent);
        global.setEcartTotalBrut(totalBrutCourant.subtract(totalBrutPrecedent));
        global.setEcartTotalBrutPourcent(calculerPourcentage(totalBrutCourant, totalBrutPrecedent));

        // Charges patronales
        BigDecimal chargesPatronalesCourant = bulletinsCourant.stream()
                .map(b -> b.getTotalpatronal() != null ? b.getTotalpatronal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal chargesPatronalesPrecedent = bulletinsPrecedent.stream()
                .map(b -> b.getTotalpatronal() != null ? b.getTotalpatronal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        global.setChargesPatronalesCourant(chargesPatronalesCourant);
        global.setChargesPatronalesPrecedent(chargesPatronalesPrecedent);
        global.setEcartChargesPatronales(chargesPatronalesCourant.subtract(chargesPatronalesPrecedent));
        global.setEcartChargesPatronalesPourcent(calculerPourcentage(chargesPatronalesCourant, chargesPatronalesPrecedent));

        // Cotisations salariales (retenue totale - autres retenues)
        BigDecimal cotisationsSalarialesCourant = bulletinsCourant.stream()
                .map(b -> b.getTotalretenue() != null ? b.getTotalretenue() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cotisationsSalarialesPrecedent = bulletinsPrecedent.stream()
                .map(b -> b.getTotalretenue() != null ? b.getTotalretenue() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        global.setCotisationsSalarialesCourant(cotisationsSalarialesCourant);
        global.setCotisationsSalarialesPrecedent(cotisationsSalarialesPrecedent);
        global.setEcartCotisationsSalariales(cotisationsSalarialesCourant.subtract(cotisationsSalarialesPrecedent));
        global.setEcartCotisationsSalarialesPourcent(calculerPourcentage(cotisationsSalarialesCourant, cotisationsSalarialesPrecedent));

        // Net à payer
        BigDecimal netAPayerCourant = bulletinsCourant.stream()
                .map(b -> b.getNetapayer() != null ? b.getNetapayer() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netAPayerPrecedent = bulletinsPrecedent.stream()
                .map(b -> b.getNetapayer() != null ? b.getNetapayer() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        global.setNetAPayerCourant(netAPayerCourant);
        global.setNetAPayerPrecedent(netAPayerPrecedent);
        global.setEcartNetAPayer(netAPayerCourant.subtract(netAPayerPrecedent));
        global.setEcartNetAPayerPourcent(calculerPourcentage(netAPayerCourant, netAPayerPrecedent));

        return global;
    }

    private List<CadragePaieDTO.EcartPersonnelDTO> calculerEcartsPersonnels(
            List<BulletinPaie> bulletinsCourant,
            List<BulletinPaie> bulletinsPrecedent) {

        // Indexer les bulletins précédents par personnel_id
        Map<Long, BulletinPaie> bulletinsPrecedentMap = bulletinsPrecedent.stream()
                .collect(Collectors.toMap(
                        b -> b.getContratPersonnel().getPersonnel().getId(),
                        b -> b,
                        (b1, b2) -> b1
                ));

        List<CadragePaieDTO.EcartPersonnelDTO> ecarts = new ArrayList<>();

        for (BulletinPaie bulletinCourant : bulletinsCourant) {
            Long personnelId = bulletinCourant.getContratPersonnel().getPersonnel().getId();
            BulletinPaie bulletinPrecedent = bulletinsPrecedentMap.get(personnelId);

            CadragePaieDTO.EcartPersonnelDTO ecart = new CadragePaieDTO.EcartPersonnelDTO();
            ecart.setId(bulletinCourant.getId());
            ecart.setMatricule(bulletinCourant.getContratPersonnel().getPersonnel().getMatricule());
            ecart.setNomPrenom(bulletinCourant.getContratPersonnel().getPersonnel().getNom() + " " +
                    bulletinCourant.getContratPersonnel().getPersonnel().getPrenom());

            BigDecimal brutCourant = bulletinCourant.getTotalbrut() != null ? bulletinCourant.getTotalbrut() : BigDecimal.ZERO;
            BigDecimal netCourant = bulletinCourant.getNetapayer() != null ? bulletinCourant.getNetapayer() : BigDecimal.ZERO;

            ecart.setBrutCourant(brutCourant);
            ecart.setNetCourant(netCourant);

            if (bulletinPrecedent != null) {
                BigDecimal brutPrecedent = bulletinPrecedent.getTotalbrut() != null ? bulletinPrecedent.getTotalbrut() : BigDecimal.ZERO;
                BigDecimal netPrecedent = bulletinPrecedent.getNetapayer() != null ? bulletinPrecedent.getNetapayer() : BigDecimal.ZERO;

                ecart.setBrutPrecedent(brutPrecedent);
                ecart.setNetPrecedent(netPrecedent);

                ecart.setEcartBrut(brutCourant.subtract(brutPrecedent));
                ecart.setEcartBrutPourcent(calculerPourcentage(brutCourant, brutPrecedent));

                ecart.setEcartNet(netCourant.subtract(netPrecedent));
                ecart.setEcartNetPourcent(calculerPourcentage(netCourant, netPrecedent));

                // Catégoriser l'écart
                ecart.setTypeEcart(categoriserEcart(bulletinCourant, bulletinPrecedent));
            } else {
                // Nouveau salarié (entrée)
                ecart.setBrutPrecedent(BigDecimal.ZERO);
                ecart.setNetPrecedent(BigDecimal.ZERO);
                ecart.setEcartBrut(brutCourant);
                ecart.setEcartBrutPourcent(BigDecimal.valueOf(100)); // +100% = entrée
                ecart.setEcartNet(netCourant);
                ecart.setEcartNetPourcent(BigDecimal.valueOf(100));
                ecart.setTypeEcart("ENTREE");
            }

            ecarts.add(ecart);
        }

        // Ajouter les sorties (personnels présents en M-1 mais pas en M)
        for (BulletinPaie bulletinPrecedent : bulletinsPrecedent) {
            Long personnelId = bulletinPrecedent.getContratPersonnel().getPersonnel().getId();
            boolean presentEnM = bulletinsCourant.stream()
                    .anyMatch(b -> b.getContratPersonnel().getPersonnel().getId().equals(personnelId));

            if (!presentEnM) {
                CadragePaieDTO.EcartPersonnelDTO ecart = new CadragePaieDTO.EcartPersonnelDTO();
                ecart.setId(bulletinPrecedent.getId());
                ecart.setMatricule(bulletinPrecedent.getContratPersonnel().getPersonnel().getMatricule());
                ecart.setNomPrenom(bulletinPrecedent.getContratPersonnel().getPersonnel().getNom() + " " +
                        bulletinPrecedent.getContratPersonnel().getPersonnel().getPrenom());

                ecart.setBrutCourant(BigDecimal.ZERO);
                ecart.setBrutPrecedent(bulletinPrecedent.getTotalbrut() != null ? bulletinPrecedent.getTotalbrut() : BigDecimal.ZERO);
                ecart.setEcartBrut(bulletinPrecedent.getTotalbrut().negate());
                ecart.setEcartBrutPourcent(BigDecimal.valueOf(-100));

                ecart.setNetCourant(BigDecimal.ZERO);
                ecart.setNetPrecedent(bulletinPrecedent.getNetapayer() != null ? bulletinPrecedent.getNetapayer() : BigDecimal.ZERO);
                ecart.setEcartNet(bulletinPrecedent.getNetapayer().negate());
                ecart.setEcartNetPourcent(BigDecimal.valueOf(-100));

                ecart.setTypeEcart("SORTIE");
                ecarts.add(ecart);
            }
        }

        return ecarts;
    }

    private String categoriserEcart(BulletinPaie bulletinCourant, BulletinPaie bulletinPrecedent) {
        BigDecimal ecartBrut = bulletinCourant.getTotalbrut().subtract(bulletinPrecedent.getTotalbrut());
        BigDecimal ecartNet = bulletinCourant.getNetapayer().subtract(bulletinPrecedent.getNetapayer());

        // Si l'écart est significatif (> 5%), on considère comme variable ou contractuel
        if (ecartBrut.abs().compareTo(bulletinPrecedent.getTotalbrut().multiply(BigDecimal.valueOf(0.05))) > 0) {
            return "VARIABLE";
        } else if (ecartBrut.compareTo(BigDecimal.ZERO) != 0) {
            return "CONTRACTUEL";
        } else {
            return "STABLE";
        }
    }

    private BigDecimal calculerPourcentage(BigDecimal valeur, BigDecimal reference) {
        if (reference == null || reference.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return valeur.subtract(reference)
                .divide(reference, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
