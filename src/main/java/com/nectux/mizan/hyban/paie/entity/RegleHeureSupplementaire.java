package com.nectux.mizan.hyban.paie.entity;

import com.nectux.mizan.hyban.paie.enums.TypeHS;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CGECI_RHPAIE_REGLE_HS")
@SequenceGenerator(name = "CGECI_RHPAIE_REGLE_HS_SEQUENCE", sequenceName = "CGECI_RHPAIE_REGLE_HS_SEQ", initialValue = 1, allocationSize = 1)
public class RegleHeureSupplementaire {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CGECI_RHPAIE_REGLE_HS_SEQUENCE")
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String code;

    @Column(nullable = false)
    private String libelle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeHS typeHS;

    @Column(nullable = false)
    private BigDecimal tauxMajoration;

    @Column(nullable = false)
    private BigDecimal coefficient;

    @Column(nullable = false)
    private Boolean active = true;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    public RegleHeureSupplementaire() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public TypeHS getTypeHS() { return typeHS; }
    public void setTypeHS(TypeHS typeHS) { this.typeHS = typeHS; }

    public BigDecimal getTauxMajoration() { return tauxMajoration; }
    public void setTauxMajoration(BigDecimal tauxMajoration) { this.tauxMajoration = tauxMajoration; }

    public BigDecimal getCoefficient() { return coefficient; }
    public void setCoefficient(BigDecimal coefficient) { this.coefficient = coefficient; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
}
