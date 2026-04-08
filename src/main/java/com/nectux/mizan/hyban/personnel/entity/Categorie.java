package com.nectux.mizan.hyban.personnel.entity;

import jakarta.persistence.*;

import com.nectux.mizan.hyban.parametrages.entity.Auditable;
import com.nectux.mizan.hyban.utils.Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Entity
@Component("categorie")
@Scope("prototype")
@Table(name="CGECI_RHPAIE_CATEGORIE")
@SequenceGenerator(name="CGECI_RHPAIE_CATEGORIE_SEQUENCE", sequenceName="CGECI_RHPAIE_CATEGORIE_SEQ", initialValue=1, allocationSize=1)
public class Categorie extends Auditable {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CGECI_RHPAIE_CATEGORIE_SEQUENCE")
	@Column(unique=true, nullable=false)
	private Long id;
	
	@Column(unique=false, nullable=false)
	private String libelle;
	
	@Column(nullable=false)
	private BigDecimal salaireDeBase;
	
	@Transient
	private String salaireBase;
	
	private BigDecimal indemniteLogement;
	
	@Transient
	private String montantIndemniteLogement;

	public Categorie() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public BigDecimal getSalaireDeBase() {
		return salaireDeBase;
	}

	public void setSalaireDeBase(BigDecimal salaireDeBase) {
		this.salaireDeBase = salaireDeBase;
	}

	public String getSalaireBase() {
		salaireBase = Utils.formattingAmount(salaireDeBase);
		return salaireBase;
	}

	public void setSalaireBase(String salaireBase) {
		this.salaireBase = salaireBase;
	}

	public BigDecimal getIndemniteLogement() {
		return indemniteLogement;
	}

	public void setIndemniteLogement(BigDecimal indemniteLogement) {
		this.indemniteLogement = indemniteLogement;
	}

	public String getMontantIndemniteLogement() {
		montantIndemniteLogement=Utils.formattingAmount(indemniteLogement);
		return montantIndemniteLogement;
	}

	public void setMontantIndemniteLogement(String montantIndemniteLogement) {
		this.montantIndemniteLogement = montantIndemniteLogement;
	}


	

	
	
}
