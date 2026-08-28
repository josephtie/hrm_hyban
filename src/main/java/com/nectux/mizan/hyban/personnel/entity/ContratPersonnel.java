package com.nectux.mizan.hyban.personnel.entity;

import com.nectux.mizan.hyban.personnel.enums.TypeOperationContrat;
import jakarta.persistence.*;

import com.nectux.mizan.hyban.parametrages.entity.Auditable;
import com.nectux.mizan.hyban.parametrages.entity.TypeContrat;
import com.nectux.mizan.hyban.personnel.enums.EtatContrat;
import com.nectux.mizan.hyban.personnel.enums.MotifClotureContrat;
import com.nectux.mizan.hyban.utils.CustomDateDeserializer;
import com.nectux.mizan.hyban.utils.Utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Entity
@Component("contratPersonnel")
@Scope("prototype")
@Table(name="CGECI_RHPAIE_CONTRAT_PERSONNEL")
@SequenceGenerator(name="CGECI_RHPAIE_CONTRAT_PERSONNEL_SEQUENCE", sequenceName="CGECI_RHPAIE_CONTRAT_PERSONNEL_SEQ", initialValue=1, allocationSize=1)
public class ContratPersonnel extends Auditable {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CGECI_RHPAIE_CONTRAT_PERSONNEL_SEQUENCE")
	@Column(unique=true, nullable=false)
	private Long id;
		
	private Boolean statut;
	
	private Boolean depart;

	private Boolean soldeCalcule;
	@ManyToOne
	@JoinColumn(nullable=false)
	private Categorie categorie;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Personnel personnel;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Fonction fonction;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private TypeContrat typeContrat;
	
	@JsonSerialize(using = CustomDateDeserializer.class)
	@Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private java.util.Date dateDebut;
	
	@Transient
	private String dDebut;
	
	@JsonSerialize(using = CustomDateDeserializer.class)
	@Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private java.util.Date dateFin;
	
	@Transient
	private String dFin;
	
	
	@JsonSerialize(using = CustomDateDeserializer.class)
	@Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private java.util.Date dateMod;
	
	@Transient
	private String dMod;
	
	private BigDecimal netAPayer;
	
	@Transient
	private String netPayer;
	
	private BigDecimal indemniteLogement;
	
	@Transient
	private String indemniteLogmt;
	
    private BigDecimal indemniteRepresent;    
	
	@Transient
	private String indemniteReprt;
	
	   /*	private BigDecimal indemniteResp;
		
	@Transient
	private String indemRespons;*/
	
	
	private String ObservCtrat;
    private BigDecimal indemniteTransport;
	
	@Transient
	private String indemniteTranspt;
	
	
	private BigDecimal sursalaire;
	
	@Transient
	private String sursal;
	
	private int ancienneteInitial;

	private String numeroContrat;

	@JsonSerialize(using = CustomDateDeserializer.class)
	@Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private java.util.Date dateSignature;

	@Transient
	private String dSignature;

	private Integer periodeEssai;

	private String devise;

	private String site;

	private String chantier;

	private String direction;

	private String responsable;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private EtatContrat etatContrat;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private MotifClotureContrat motifCloture;


	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private TypeOperationContrat operationContrat;

	public ContratPersonnel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public Boolean getStatut() {
		return statut;
	}

	public void setStatut(Boolean statut) {
		this.statut = statut;
	}



	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public Personnel getPersonnel() {
		return personnel;
	}

	public void setPersonnel(Personnel personnel) {
		this.personnel = personnel;
	}

	public Fonction getFonction() {
		return fonction;
	}

	public void setFonction(Fonction fonction) {
		this.fonction = fonction;
	}

	public TypeContrat getTypeContrat() {
		return typeContrat;
	}

	public void setTypeContrat(TypeContrat typeContrat) {
		this.typeContrat = typeContrat;
	}

	public java.util.Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(java.util.Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getdDebut() {
		dDebut = Utils.dateToString(dateDebut, "dd/MM/yyyy");
		return dDebut;
	}

	public void setdDebut(String dDebut) {
		this.dDebut = dDebut;
	}

	public java.util.Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(java.util.Date dateFin) {
		this.dateFin = dateFin;
	}

	public String getdFin() {
		dFin = Utils.dateToString(dateFin, "dd/MM/yyyy");
		return dFin;
	}

	public void setdFin(String dFin) {
		this.dFin = dFin;
	}

	public java.util.Date getDateMod() {
		return dateMod;
	}

	public void setDateMod(java.util.Date dateMod) {
		this.dateMod = dateMod;
	}

	public String getdMod() {
		return dMod=Utils.dateToString(dateMod, "dd/MM/yyyy");
	}

	public void setdMod(String dMod) {
		this.dMod = dMod;
	}

	public BigDecimal getNetAPayer() {
		return netAPayer;
	}

	public void setNetAPayer(BigDecimal netAPayer) {
		this.netAPayer = netAPayer;
	}

	public String getNetPayer() {
		netPayer = Utils.formattingAmount(netAPayer);
		return netPayer;
	}

	public void setNetPayer(String netPayer) {
		this.netPayer = netPayer;
	}

	public BigDecimal getSursalaire() {
		return sursalaire;
	}

	public void setSursalaire(BigDecimal sursalaire) {
		this.sursalaire = sursalaire;
	}

	public String getSursal() {
		return sursal=Utils.formattingAmount(sursalaire);
	}

	public void setSursal(String sursal) {
		this.sursal = sursal;
	}

	public BigDecimal getIndemniteLogement() {
		return indemniteLogement;
	}

	public void setIndemniteLogement(BigDecimal indemniteLogement) {
		this.indemniteLogement = indemniteLogement;
	}

	public String getIndemniteLogmt() {
		indemniteLogmt = Utils.formattingAmount(indemniteLogement);
		return indemniteLogmt;
	}

	public void setIndemniteLogmt(String indemniteLogmt) {
		this.indemniteLogmt = indemniteLogmt;
	}

	public int getAncienneteInitial() {
        return Optional.ofNullable(ancienneteInitial).orElse(0);
	}

	public void setAncienneteInitial(int ancienneteInitial) {
		this.ancienneteInitial = ancienneteInitial;
	}

	public EtatContrat getEtatContrat() {
		return etatContrat;
	}

	public void setEtatContrat(EtatContrat etatContrat) {
		this.etatContrat = etatContrat;
	}

	public MotifClotureContrat getMotifCloture() {
		return motifCloture;
	}

	public void setMotifCloture(MotifClotureContrat motifCloture) {
		this.motifCloture = motifCloture;
	}

	public String getNumeroContrat() {
		return numeroContrat;
	}

	public void setNumeroContrat(String numeroContrat) {
		this.numeroContrat = numeroContrat;
	}

	public java.util.Date getDateSignature() {
		return dateSignature;
	}

	public void setDateSignature(java.util.Date dateSignature) {
		this.dateSignature = dateSignature;
	}

	public String getdSignature() {
		dSignature = Utils.dateToString(dateSignature, "dd/MM/yyyy");
		return dSignature;
	}

	public void setdSignature(String dSignature) {
		this.dSignature = dSignature;
	}

	public Integer getPeriodeEssai() {
		return periodeEssai;
	}

	public void setPeriodeEssai(Integer periodeEssai) {
		this.periodeEssai = periodeEssai;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getChantier() {
		return chantier;
	}

	public void setChantier(String chantier) {
		this.chantier = chantier;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

public BigDecimal getIndemniteRepresent() {
		return indemniteRepresent;
	}

	public void setIndemniteRepresent(BigDecimal indemniteRepresent) {
		this.indemniteRepresent = indemniteRepresent;
	}

	public String getIndemniteReprt() {
		return indemniteReprt;
	}

	public void setIndemniteReprt(String indemniteReprt) {
		this.indemniteReprt = indemniteReprt;
	}

	public BigDecimal getIndemniteTransport() {
		return indemniteTransport;
	}

	public void setIndemniteTransport(BigDecimal indemniteTransport) {
		this.indemniteTransport = indemniteTransport;
	}

	public String getIndemniteTranspt() {
		return indemniteTranspt;
	}

	public void setIndemniteTranspt(String indemniteTranspt) {
		this.indemniteTranspt = indemniteTranspt;
	}

	public Boolean getSoldeCalcule() {
		return soldeCalcule;
	}

	public void setSoldeCalcule(Boolean soldeCalcule) {
		this.soldeCalcule = soldeCalcule;
	}


	public TypeOperationContrat getOperationContrat() {
		return operationContrat;
	}

	public void setOperationContrat(TypeOperationContrat operationContrat) {
		this.operationContrat = operationContrat;
	}

	@Override
	public String toString() {
		return "ContratPersonnel{" +
				"id=" + id +
				", statut=" + statut +
				", depart=" + depart +
				", soldeCalcule=" + soldeCalcule +
				", categorie=" + categorie +
				", personnel=" + personnel +
				", fonction=" + fonction +
				", typeContrat=" + typeContrat +
				", dateDebut=" + dateDebut +
				", dDebut='" + dDebut + '\'' +
				", dateFin=" + dateFin +
				", dFin='" + dFin + '\'' +
				", dateMod=" + dateMod +
				", dMod='" + dMod + '\'' +
				", netAPayer=" + netAPayer +
				", netPayer='" + netPayer + '\'' +
				", indemniteLogement=" + indemniteLogement +
				", indemniteLogmt='" + indemniteLogmt + '\'' +
				", indemniteRepresent=" + indemniteRepresent +
				", indemniteReprt='" + indemniteReprt + '\'' +
				", ObservCtrat='" + ObservCtrat + '\'' +
				", indemniteTransport=" + indemniteTransport +
				", indemniteTranspt='" + indemniteTranspt + '\'' +
				", sursalaire=" + sursalaire +
				", sursal='" + sursal + '\'' +
				", ancienneteInitial=" + ancienneteInitial +
				", numeroContrat='" + numeroContrat + '\'' +
				", dateSignature=" + dateSignature +
				", dSignature='" + dSignature + '\'' +
				", periodeEssai=" + periodeEssai +
				", devise='" + devise + '\'' +
				", site='" + site + '\'' +
				", chantier='" + chantier + '\'' +
				", direction='" + direction + '\'' +
				", responsable='" + responsable + '\'' +
				", etatContrat=" + etatContrat +
				", motifCloture=" + motifCloture +
				", operationContrat=" + operationContrat +
				'}';
	}

	public Boolean getDepart() {
		return depart;
	}

	public void setDepart(Boolean depart) {
		this.depart = depart;
	}

	public String getObservCtrat() {
		return ObservCtrat;
	}

	public void setObservCtrat(String observCtrat) {
		ObservCtrat = observCtrat;
	}

	

	/*public BigDecimal getIndemniteResp() {
		return indemniteResp;
	}

	public void setIndemniteResp(BigDecimal indemniteResp) {
		this.indemniteResp = indemniteResp;
	}

	public String getIndemRespons() {
		return indemRespons=Utils.formattingAmount(indemniteResp);
	}

	public void setIndemRespons(String indemRespons) {
		this.indemRespons = indemRespons;
	}*/

	

	
}
