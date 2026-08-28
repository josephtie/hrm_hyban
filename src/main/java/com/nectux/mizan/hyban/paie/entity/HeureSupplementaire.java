package com.nectux.mizan.hyban.paie.entity;

import com.nectux.mizan.hyban.paie.enums.StatutHS;
import com.nectux.mizan.hyban.paie.enums.TypeHS;
import com.nectux.mizan.hyban.parametrages.entity.Auditable;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.personnel.entity.Personnel;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CGECI_RHPAIE_HEURE_SUPP")
@SequenceGenerator(name = "CGECI_RHPAIE_HEURE_SUPP_SEQUENCE", sequenceName = "CGECI_RHPAIE_HEURE_SUPP_SEQ", initialValue = 1, allocationSize = 1)
public class HeureSupplementaire extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CGECI_RHPAIE_HEURE_SUPP_SEQUENCE")
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Personnel personnel;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateTravail;

    @Column(nullable = false)
    private BigDecimal nombreHeures;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeHS typeHS;

    private BigDecimal tauxMajoration;

    private BigDecimal tauxHoraire;

    private BigDecimal coefficient;

    private BigDecimal montant;

    private String motif;

    @Column(length = 500)
    private String commentaire;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PeriodePaie periodePaie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutHS statut = StatutHS.BROUILLON;

    @Column(length = 500)
    private String motifRejet;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSoumission;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIntegrationPaie;

    private String validatedBy;

    public HeureSupplementaire() {
        super();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }

    public Date getDateTravail() { return dateTravail; }
    public void setDateTravail(Date dateTravail) { this.dateTravail = dateTravail; }

    public BigDecimal getNombreHeures() { return nombreHeures; }
    public void setNombreHeures(BigDecimal nombreHeures) { this.nombreHeures = nombreHeures; }

    public TypeHS getTypeHS() { return typeHS; }
    public void setTypeHS(TypeHS typeHS) { this.typeHS = typeHS; }

    public BigDecimal getTauxMajoration() { return tauxMajoration; }
    public void setTauxMajoration(BigDecimal tauxMajoration) { this.tauxMajoration = tauxMajoration; }

    public BigDecimal getTauxHoraire() { return tauxHoraire; }
    public void setTauxHoraire(BigDecimal tauxHoraire) { this.tauxHoraire = tauxHoraire; }

    public BigDecimal getCoefficient() { return coefficient; }
    public void setCoefficient(BigDecimal coefficient) { this.coefficient = coefficient; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public PeriodePaie getPeriodePaie() { return periodePaie; }
    public void setPeriodePaie(PeriodePaie periodePaie) { this.periodePaie = periodePaie; }

    public StatutHS getStatut() { return statut; }
    public void setStatut(StatutHS statut) { this.statut = statut; }

    public String getMotifRejet() { return motifRejet; }
    public void setMotifRejet(String motifRejet) { this.motifRejet = motifRejet; }

    public Date getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(Date dateSoumission) { this.dateSoumission = dateSoumission; }

    public Date getDateValidation() { return dateValidation; }
    public void setDateValidation(Date dateValidation) { this.dateValidation = dateValidation; }

    public Date getDateIntegrationPaie() { return dateIntegrationPaie; }
    public void setDateIntegrationPaie(Date dateIntegrationPaie) { this.dateIntegrationPaie = dateIntegrationPaie; }

    public String getValidatedBy() { return validatedBy; }
    public void setValidatedBy(String validatedBy) { this.validatedBy = validatedBy; }
}
