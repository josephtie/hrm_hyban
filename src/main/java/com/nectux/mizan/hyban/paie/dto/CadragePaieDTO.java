package com.nectux.mizan.hyban.paie.dto;

import java.math.BigDecimal;
import java.util.List;

public class CadragePaieDTO {
    private String periodeCourant;
    private String periodePrecedente;
    private CadrageGlobalDTO global;
    private List<EcartPersonnelDTO> ecartsPersonnels;

    public CadragePaieDTO() {}

    public String getPeriodeCourant() {
        return periodeCourant;
    }

    public void setPeriodeCourant(String periodeCourant) {
        this.periodeCourant = periodeCourant;
    }

    public String getPeriodePrecedente() {
        return periodePrecedente;
    }

    public void setPeriodePrecedente(String periodePrecedente) {
        this.periodePrecedente = periodePrecedente;
    }

    public CadrageGlobalDTO getGlobal() {
        return global;
    }

    public void setGlobal(CadrageGlobalDTO global) {
        this.global = global;
    }

    public List<EcartPersonnelDTO> getEcartsPersonnels() {
        return ecartsPersonnels;
    }

    public void setEcartsPersonnels(List<EcartPersonnelDTO> ecartsPersonnels) {
        this.ecartsPersonnels = ecartsPersonnels;
    }

    public static class CadrageGlobalDTO {
        private Integer effectifCourant;
        private Integer effectifPrecedent;
        private Integer ecartEffectif;
        private BigDecimal ecartEffectifPourcent;

        private BigDecimal salaireBaseCourant;
        private BigDecimal salaireBasePrecedent;
        private BigDecimal ecartSalaireBase;
        private BigDecimal ecartSalaireBasePourcent;

        private BigDecimal primesIndemnitesCourant;
        private BigDecimal primesIndemnitesPrecedent;
        private BigDecimal ecartPrimesIndemnites;
        private BigDecimal ecartPrimesIndemnitesPourcent;

        private BigDecimal totalBrutCourant;
        private BigDecimal totalBrutPrecedent;
        private BigDecimal ecartTotalBrut;
        private BigDecimal ecartTotalBrutPourcent;

        private BigDecimal chargesPatronalesCourant;
        private BigDecimal chargesPatronalesPrecedent;
        private BigDecimal ecartChargesPatronales;
        private BigDecimal ecartChargesPatronalesPourcent;

        private BigDecimal cotisationsSalarialesCourant;
        private BigDecimal cotisationsSalarialesPrecedent;
        private BigDecimal ecartCotisationsSalariales;
        private BigDecimal ecartCotisationsSalarialesPourcent;

        private BigDecimal netAPayerCourant;
        private BigDecimal netAPayerPrecedent;
        private BigDecimal ecartNetAPayer;
        private BigDecimal ecartNetAPayerPourcent;

        // Getters et Setters
        public Integer getEffectifCourant() { return effectifCourant; }
        public void setEffectifCourant(Integer effectifCourant) { this.effectifCourant = effectifCourant; }

        public Integer getEffectifPrecedent() { return effectifPrecedent; }
        public void setEffectifPrecedent(Integer effectifPrecedent) { this.effectifPrecedent = effectifPrecedent; }

        public Integer getEcartEffectif() { return ecartEffectif; }
        public void setEcartEffectif(Integer ecartEffectif) { this.ecartEffectif = ecartEffectif; }

        public BigDecimal getEcartEffectifPourcent() { return ecartEffectifPourcent; }
        public void setEcartEffectifPourcent(BigDecimal ecartEffectifPourcent) { this.ecartEffectifPourcent = ecartEffectifPourcent; }

        public BigDecimal getSalaireBaseCourant() { return salaireBaseCourant; }
        public void setSalaireBaseCourant(BigDecimal salaireBaseCourant) { this.salaireBaseCourant = salaireBaseCourant; }

        public BigDecimal getSalaireBasePrecedent() { return salaireBasePrecedent; }
        public void setSalaireBasePrecedent(BigDecimal salaireBasePrecedent) { this.salaireBasePrecedent = salaireBasePrecedent; }

        public BigDecimal getEcartSalaireBase() { return ecartSalaireBase; }
        public void setEcartSalaireBase(BigDecimal ecartSalaireBase) { this.ecartSalaireBase = ecartSalaireBase; }

        public BigDecimal getEcartSalaireBasePourcent() { return ecartSalaireBasePourcent; }
        public void setEcartSalaireBasePourcent(BigDecimal ecartSalaireBasePourcent) { this.ecartSalaireBasePourcent = ecartSalaireBasePourcent; }

        public BigDecimal getPrimesIndemnitesCourant() { return primesIndemnitesCourant; }
        public void setPrimesIndemnitesCourant(BigDecimal primesIndemnitesCourant) { this.primesIndemnitesCourant = primesIndemnitesCourant; }

        public BigDecimal getPrimesIndemnitesPrecedent() { return primesIndemnitesPrecedent; }
        public void setPrimesIndemnitesPrecedent(BigDecimal primesIndemnitesPrecedent) { this.primesIndemnitesPrecedent = primesIndemnitesPrecedent; }

        public BigDecimal getEcartPrimesIndemnites() { return ecartPrimesIndemnites; }
        public void setEcartPrimesIndemnites(BigDecimal ecartPrimesIndemnites) { this.ecartPrimesIndemnites = ecartPrimesIndemnites; }

        public BigDecimal getEcartPrimesIndemnitesPourcent() { return ecartPrimesIndemnitesPourcent; }
        public void setEcartPrimesIndemnitesPourcent(BigDecimal ecartPrimesIndemnitesPourcent) { this.ecartPrimesIndemnitesPourcent = ecartPrimesIndemnitesPourcent; }

        public BigDecimal getTotalBrutCourant() { return totalBrutCourant; }
        public void setTotalBrutCourant(BigDecimal totalBrutCourant) { this.totalBrutCourant = totalBrutCourant; }

        public BigDecimal getTotalBrutPrecedent() { return totalBrutPrecedent; }
        public void setTotalBrutPrecedent(BigDecimal totalBrutPrecedent) { this.totalBrutPrecedent = totalBrutPrecedent; }

        public BigDecimal getEcartTotalBrut() { return ecartTotalBrut; }
        public void setEcartTotalBrut(BigDecimal ecartTotalBrut) { this.ecartTotalBrut = ecartTotalBrut; }

        public BigDecimal getEcartTotalBrutPourcent() { return ecartTotalBrutPourcent; }
        public void setEcartTotalBrutPourcent(BigDecimal ecartTotalBrutPourcent) { this.ecartTotalBrutPourcent = ecartTotalBrutPourcent; }

        public BigDecimal getChargesPatronalesCourant() { return chargesPatronalesCourant; }
        public void setChargesPatronalesCourant(BigDecimal chargesPatronalesCourant) { this.chargesPatronalesCourant = chargesPatronalesCourant; }

        public BigDecimal getChargesPatronalesPrecedent() { return chargesPatronalesPrecedent; }
        public void setChargesPatronalesPrecedent(BigDecimal chargesPatronalesPrecedent) { this.chargesPatronalesPrecedent = chargesPatronalesPrecedent; }

        public BigDecimal getEcartChargesPatronales() { return ecartChargesPatronales; }
        public void setEcartChargesPatronales(BigDecimal ecartChargesPatronales) { this.ecartChargesPatronales = ecartChargesPatronales; }

        public BigDecimal getEcartChargesPatronalesPourcent() { return ecartChargesPatronalesPourcent; }
        public void setEcartChargesPatronalesPourcent(BigDecimal ecartChargesPatronalesPourcent) { this.ecartChargesPatronalesPourcent = ecartChargesPatronalesPourcent; }

        public BigDecimal getCotisationsSalarialesCourant() { return cotisationsSalarialesCourant; }
        public void setCotisationsSalarialesCourant(BigDecimal cotisationsSalarialesCourant) { this.cotisationsSalarialesCourant = cotisationsSalarialesCourant; }

        public BigDecimal getCotisationsSalarialesPrecedent() { return cotisationsSalarialesPrecedent; }
        public void setCotisationsSalarialesPrecedent(BigDecimal cotisationsSalarialesPrecedent) { this.cotisationsSalarialesPrecedent = cotisationsSalarialesPrecedent; }

        public BigDecimal getEcartCotisationsSalariales() { return ecartCotisationsSalariales; }
        public void setEcartCotisationsSalariales(BigDecimal ecartCotisationsSalariales) { this.ecartCotisationsSalariales = ecartCotisationsSalariales; }

        public BigDecimal getEcartCotisationsSalarialesPourcent() { return ecartCotisationsSalarialesPourcent; }
        public void setEcartCotisationsSalarialesPourcent(BigDecimal ecartCotisationsSalarialesPourcent) { this.ecartCotisationsSalarialesPourcent = ecartCotisationsSalarialesPourcent; }

        public BigDecimal getNetAPayerCourant() { return netAPayerCourant; }
        public void setNetAPayerCourant(BigDecimal netAPayerCourant) { this.netAPayerCourant = netAPayerCourant; }

        public BigDecimal getNetAPayerPrecedent() { return netAPayerPrecedent; }
        public void setNetAPayerPrecedent(BigDecimal netAPayerPrecedent) { this.netAPayerPrecedent = netAPayerPrecedent; }

        public BigDecimal getEcartNetAPayer() { return ecartNetAPayer; }
        public void setEcartNetAPayer(BigDecimal ecartNetAPayer) { this.ecartNetAPayer = ecartNetAPayer; }

        public BigDecimal getEcartNetAPayerPourcent() { return ecartNetAPayerPourcent; }
        public void setEcartNetAPayerPourcent(BigDecimal ecartNetAPayerPourcent) { this.ecartNetAPayerPourcent = ecartNetAPayerPourcent; }
    }

    public static class EcartPersonnelDTO {
        private Long id;
        private String matricule;
        private String nomPrenom;
        private BigDecimal brutCourant;
        private BigDecimal brutPrecedent;
        private BigDecimal ecartBrut;
        private BigDecimal ecartBrutPourcent;
        private BigDecimal netCourant;
        private BigDecimal netPrecedent;
        private BigDecimal ecartNet;
        private BigDecimal ecartNetPourcent;
        private String typeEcart; // "ENTREE", "SORTIE", "VARIABLE", "CONTRACTUEL"

        // Getters et Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getMatricule() { return matricule; }
        public void setMatricule(String matricule) { this.matricule = matricule; }

        public String getNomPrenom() { return nomPrenom; }
        public void setNomPrenom(String nomPrenom) { this.nomPrenom = nomPrenom; }

        public BigDecimal getBrutCourant() { return brutCourant; }
        public void setBrutCourant(BigDecimal brutCourant) { this.brutCourant = brutCourant; }

        public BigDecimal getBrutPrecedent() { return brutPrecedent; }
        public void setBrutPrecedent(BigDecimal brutPrecedent) { this.brutPrecedent = brutPrecedent; }

        public BigDecimal getEcartBrut() { return ecartBrut; }
        public void setEcartBrut(BigDecimal ecartBrut) { this.ecartBrut = ecartBrut; }

        public BigDecimal getEcartBrutPourcent() { return ecartBrutPourcent; }
        public void setEcartBrutPourcent(BigDecimal ecartBrutPourcent) { this.ecartBrutPourcent = ecartBrutPourcent; }

        public BigDecimal getNetCourant() { return netCourant; }
        public void setNetCourant(BigDecimal netCourant) { this.netCourant = netCourant; }

        public BigDecimal getNetPrecedent() { return netPrecedent; }
        public void setNetPrecedent(BigDecimal netPrecedent) { this.netPrecedent = netPrecedent; }

        public BigDecimal getEcartNet() { return ecartNet; }
        public void setEcartNet(BigDecimal ecartNet) { this.ecartNet = ecartNet; }

        public BigDecimal getEcartNetPourcent() { return ecartNetPourcent; }
        public void setEcartNetPourcent(BigDecimal ecartNetPourcent) { this.ecartNetPourcent = ecartNetPourcent; }

        public String getTypeEcart() { return typeEcart; }
        public void setTypeEcart(String typeEcart) { this.typeEcart = typeEcart; }
    }
}
