package com.nectux.mizan.hyban.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ITSCalculator {

    private static final BigDecimal[][] TRANCHES = {
            {bd("0"), bd("75000"), bd("0")},
            {bd("75000"), bd("240000"), bd("0.16")},
            {bd("240000"), bd("800000"), bd("0.21")},
            {bd("800000"), bd("2400000"), bd("0.24")},
            {bd("2400000"), bd("8000000"), bd("0.28")},
            {bd("8000000"), bd("999999999999"), bd("0.32")}
    };

    public static BigDecimal calculerITS(BigDecimal revenuImposable, boolean afficherDetails) {

        BigDecimal impots = BigDecimal.ZERO;

        if (revenuImposable == null) {
            return BigDecimal.ZERO;
        }

        for (BigDecimal[] tranche : TRANCHES) {

            BigDecimal bas = tranche[0];
            BigDecimal haut = tranche[1];
            BigDecimal taux = tranche[2];

            if (revenuImposable.compareTo(bas) > 0) {

                BigDecimal plafond = revenuImposable.min(haut);
                BigDecimal taxable = plafond.subtract(bas);

                if (taxable.compareTo(BigDecimal.ZERO) > 0) {

                    BigDecimal impotsTranche = taxable.multiply(taux);

                    impots = impots.add(impotsTranche);

                    if (afficherDetails) {
                        System.out.println(
                                "Tranche: " + bas + " - " + haut +
                                        " FCFA | Taux: " + taux.multiply(bd("100")) +
                                        "% | Taxable: " + taxable +
                                        " FCFA | Impôt: " + impotsTranche
                        );
                    }
                }

            } else {
                break;
            }
        }

        if (afficherDetails) {
            System.out.println(">>> Total ITS dû: " + impots);
        }

        return impots.setScale(0, RoundingMode.HALF_UP);
    }

    private static BigDecimal bd(String value) {
        return new BigDecimal(value);
    }
}
/*
public class ITSCalculator {

    private static final double[][] TRANCHES = {
            {0, 75_000, 0.0},
            {75_000, 240_000, 0.16},
            {240_000, 800_000, 0.21},
            {800_000, 2_400_000, 0.24},
            {2_400_000, 8_000_000, 0.28},
            {8_000_000, Double.MAX_VALUE, 0.32}
    };

    public static double calculerITS(double revenuImposable, boolean afficherDetails) {
        double impots = 0.0;

        for (double[] tranche : TRANCHES) {
            double bas = tranche[0];
            double haut = tranche[1];
            double taux = tranche[2];

            if (revenuImposable > bas) {
                double taxable = Math.min(revenuImposable, haut) - bas;
                double impotsTranche = taxable * taux;
                impots += impotsTranche;

                if (afficherDetails) {
                    System.out.printf("Tranche: %.0f - %.0f FCFA | Taux: %.0f%% | Taxable: %.0f FCFA | Impôt: %.2f FCFA%n",
                            bas, haut, taux * 100, taxable, impotsTranche);
                }
            } else {
                break;
            }
        }

        if (afficherDetails) {
            System.out.printf(">>> Total ITS dû: %.2f FCFA%n", impots);
        }

        return impots;
    }

    public static void main(String[] args) {
        double revenu = 2_055_237;
        double its = calculerITS(revenu, true);
    }
}
*/
