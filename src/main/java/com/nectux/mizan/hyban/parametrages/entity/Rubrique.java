package com.nectux.mizan.hyban.parametrages.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nectux.mizan.hyban.utils.CustomDateDeserializer;
import com.nectux.mizan.hyban.utils.Utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.context.annotation.Scope;
//import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Entity
@Component("rubrique")
@Scope("prototype")
@Table(name="CGECI_RHPAIE_RUBRIQUE")
@SequenceGenerator(name="CGECI_RHPAIE_RUBRIQUE_SEQUENCE", sequenceName="CGECI_RHPAIE_RUBRIQUE_SEQ", initialValue=1, allocationSize=1)
public class Rubrique extends Auditable {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CGECI_RHPAIE_RUBRIQUE_SEQUENCE")
	@Column(unique=true, nullable=false)
	private Long id;
	
	@Column(unique=true, nullable=false, length=10)
	private String code;
	
	@Column(unique=true, nullable=false)
	private String libelle;
	
	@Column( nullable=true)
	private BigDecimal taux;
	
	
	@Column( nullable=true)
	private BigDecimal mtExedent;
	
	
	@Transient
	private String strmtExedent;
	
	
	@Column(nullable=false)
	private Integer etatImposition;
	

    private String modeCalcul;

    private BigDecimal  valeurDef;

    @Column(nullable=true)
    private Boolean cotisable;

    @JsonSerialize(using = CustomDateDeserializer.class)
	@Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private java.util.Date dateCreate;
	
	@Transient
	private String dCreate;
	
	@Transient
	private String stretatimposition;
	
	@Transient
	private Boolean imposable;

	@Column(nullable=true)
	private Boolean active;

	private Boolean permanent;
    private String description;

    private Boolean speciale;

	public Rubrique() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public BigDecimal getTaux() {
		return taux;
	}

	public void setTaux(BigDecimal taux) {
		this.taux = taux;
	}

	public Integer getEtatImposition() {
		return etatImposition;
	}

	public void setEtatImposition(Integer etatImposition) {
		this.etatImposition = etatImposition;
	}

	public java.util.Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(java.util.Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String getdCreate() {
		return dCreate;
	}

	public void setdCreate(String dCreate) {
		this.dCreate = dCreate;
	}

    public String getModeCalcul() {
        return modeCalcul;
    }

    public void setModeCalcul(String modeCalcul) {
        this.modeCalcul = modeCalcul;
    }

    public BigDecimal getValeurDef() {
        return valeurDef;
    }

    public void setValeurDef(BigDecimal valeurDef) {
        this.valeurDef = valeurDef;
    }

    public Boolean getCotisable() {
        return cotisable;
    }

    public void setCotisable(Boolean cotisable) {
        this.cotisable = cotisable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMtExedent() {
		return mtExedent;
	}

	public void setMtExedent(BigDecimal mtExedent) {
		this.mtExedent = mtExedent;
	}

	public String getStretatimposition() {
		if(etatImposition==null)
			stretatimposition="";
		if(etatImposition==1)
			stretatimposition="Imposable";
		if(etatImposition==2)
			stretatimposition="Non Imposable";
		if(etatImposition==3)
			stretatimposition="Imposable et Non Imposable";
		if(etatImposition==4)
			stretatimposition="Retenue Mutuelle";
		if(etatImposition==5)
			stretatimposition="Regularisation";
		if(etatImposition==6)
			stretatimposition="Retenue Sociale";
		return stretatimposition;
	}

	public void setStretatimposition(String stretatimposition) {
		this.stretatimposition = stretatimposition;
	}

	@JsonProperty("mtExedent")
	public BigDecimal getMtExedentForFrontend() {
		return mtExedent;
	}

	public Boolean getImposable() {
		if(etatImposition == null) return null;
		return etatImposition == 1 || etatImposition == 3; // Imposable ou Imposable & Non Imposable
	}

	public void setImposable(Boolean imposable) {
		// Champ calculé, non persisté - géré via etatImposition
		// Ne fait rien pour éviter la confusion
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getStrmtExedent() {
		return strmtExedent=Utils.formattingAmount(mtExedent);
	}

	public void setStrmtExedent(String strmtExedent) {
		this.strmtExedent = strmtExedent;
	}

	public Boolean getPermanent() {
		return permanent;
	}

	public void setPermanent(Boolean permanent) {
		this.permanent = permanent;
	}

    public Boolean getSpeciale() {
        return speciale;
    }

    public void setSpeciale(Boolean speciale) {
        this.speciale = speciale;
    }

    @Override
    public String toString() {
        return "Rubrique{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", libelle='" + libelle + '\'' +
                ", taux=" + taux +
                ", mtExedent=" + mtExedent +
                ", strmtExedent='" + strmtExedent + '\'' +
                ", etatImposition=" + etatImposition +
                ", modeCalcul='" + modeCalcul + '\'' +
                ", valeurDef=" + valeurDef +
                ", cotisable=" + cotisable +
                ", dateCreate=" + dateCreate +
                ", dCreate='" + dCreate + '\'' +
                ", stretatimposition='" + stretatimposition + '\'' +
                ", imposable=" + imposable +
                ", active=" + active +
                ", permanent=" + permanent +
                ", description='" + description + '\'' +
                ", speciale=" + speciale +
                '}';
    }
}
