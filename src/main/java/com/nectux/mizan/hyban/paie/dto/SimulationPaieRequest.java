package com.nectux.mizan.hyban.paie.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Payload de simulation de salaire : on calcule le bulletin d'un personnel
 * à blanc (sans persistance) à partir de rubriques choisies et de paramètres
 * optionnels qui remplacent les valeurs du contrat/personnel le temps du calcul.
 */
public class SimulationPaieRequest {

    private Long idPeriode;
    private Long idPersonnel;

    /** Catégorie professionnelle de simulation (fournit le salaire de base). */
    private Long idCategorie;
    /** Salaire de base simulé (prioritaire sur idCategorie et le contrat). */
    private BigDecimal salaireBase;
    /** Sursalaire simulé (null = valeur du contrat). */
    private BigDecimal sursalaire;
    /** Indemnité de transport simulée (null = valeur du contrat). */
    private BigDecimal indemniteTransport;
    /** Situation matrimoniale simulée : 1=Marié, 2=Célibataire, 3=Divorcé, 4=Veuf. */
    private Integer situationMatrimoniale;
    /** Nombre d'enfants simulé. */
    private Integer nombreEnfants;
    /** Net à payer cible (comparaison affichée côté front). */
    private BigDecimal netCible;

    private List<RubriqueMontant> rubriques;

    public Long getIdPeriode() { return idPeriode; }
    public void setIdPeriode(Long idPeriode) { this.idPeriode = idPeriode; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public Long getIdCategorie() { return idCategorie; }
    public void setIdCategorie(Long idCategorie) { this.idCategorie = idCategorie; }
    public BigDecimal getSalaireBase() { return salaireBase; }
    public void setSalaireBase(BigDecimal salaireBase) { this.salaireBase = salaireBase; }
    public BigDecimal getSursalaire() { return sursalaire; }
    public void setSursalaire(BigDecimal sursalaire) { this.sursalaire = sursalaire; }
    public BigDecimal getIndemniteTransport() { return indemniteTransport; }
    public void setIndemniteTransport(BigDecimal indemniteTransport) { this.indemniteTransport = indemniteTransport; }
    public Integer getSituationMatrimoniale() { return situationMatrimoniale; }
    public void setSituationMatrimoniale(Integer situationMatrimoniale) { this.situationMatrimoniale = situationMatrimoniale; }
    public Integer getNombreEnfants() { return nombreEnfants; }
    public void setNombreEnfants(Integer nombreEnfants) { this.nombreEnfants = nombreEnfants; }
    public BigDecimal getNetCible() { return netCible; }
    public void setNetCible(BigDecimal netCible) { this.netCible = netCible; }
    public List<RubriqueMontant> getRubriques() { return rubriques; }
    public void setRubriques(List<RubriqueMontant> rubriques) { this.rubriques = rubriques; }

    public static class RubriqueMontant {
        private Long rubriqueId;
        private BigDecimal montant;
        private Integer valeur;

        public Long getRubriqueId() { return rubriqueId; }
        public void setRubriqueId(Long rubriqueId) { this.rubriqueId = rubriqueId; }
        public BigDecimal getMontant() { return montant; }
        public void setMontant(BigDecimal montant) { this.montant = montant; }
        public Integer getValeur() { return valeur; }
        public void setValeur(Integer valeur) { this.valeur = valeur; }
    }
}
