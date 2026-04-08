package com.nectux.mizan.hyban.utils;

import java.math.BigDecimal;

public class HeuresSupplementaires {



    /**
     * Calcule le taux horaire à partir du salaire mensuel en considérant 173.33h par mois.
     */
    public static BigDecimal calculerTauxHoraire(BigDecimal salaireMensuelBase) {
        BigDecimal heuresMensuelles =  BigDecimal.valueOf(173.33);
        return salaireMensuelBase.divide(heuresMensuelles);
    }

    /**
     * Calcule les montants des heures supplémentaires et retourne les résultats sous forme de tableau.
     */
    public static BigDecimal[][] calculerHeureSupMensuel(BigDecimal salaireHoraire, int heuresTravaillees15, int heuresTravaillees50, int heuresDimanchesFeries) {
        // Calcul des montants
        BigDecimal montantHeuresSupp15 = BigDecimal.valueOf(heuresTravaillees15).multiply(salaireHoraire.multiply(BigDecimal.valueOf(0.15)));
        BigDecimal montantHeuresSupp50 = BigDecimal.valueOf(heuresTravaillees50).multiply(salaireHoraire.multiply(BigDecimal.valueOf( 0.50)));
        BigDecimal montantHeuresFeries = BigDecimal.valueOf(heuresDimanchesFeries).multiply(salaireHoraire.multiply(BigDecimal.valueOf(2))); // +100%

        // Retour sous forme de tableau : {heures, montant}
        return new BigDecimal[][]{
                {BigDecimal.valueOf(heuresTravaillees15), montantHeuresSupp15},
                {BigDecimal.valueOf(heuresTravaillees50), montantHeuresSupp50},
                {BigDecimal.valueOf(heuresDimanchesFeries), montantHeuresFeries}
        };
    }

}
