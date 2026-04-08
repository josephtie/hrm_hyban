package com.nectux.mizan.hyban.paie.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.nectux.mizan.hyban.parametrages.entity.Auditable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.utils.Utils;

@Entity
@Component("gratification")
@Scope("prototype")
@Table(name="CGECI_RHPAIE_GRATIFICATION")
@SequenceGenerator(name="CGECI_RHPAIE_GRATIFICATION_SEQUENCE", sequenceName="CGECI_RHPAIE_GRATIFICATION_SEQ", initialValue=1, allocationSize=1)
public class Gratification extends Auditable {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CGECI_RHPAIE_GRATIFICATION_SEQUENCE")
	@Column(unique=true, nullable=false)
	private Long id;

	private BigDecimal nombrePart;

	@Transient
	private String nbrePart;

	private BigDecimal gratificationBase;

	@Transient
	private String montantGratificationBase;

	private BigDecimal sursalaire;

	@Transient
	private String montantSursalaire;

	private BigDecimal tauxGratification;

	@Transient
	private String montantTauxGratification;

	private BigDecimal indemniteTransport;

	@Transient
	private String indemniteTransp;

	private BigDecimal its;

	@Transient
	private String montantIts;

	private BigDecimal cn;

	@Transient
	private String montantCn;

	private BigDecimal igr;

	@Transient
	private String montantIgr;

	private BigDecimal cnps;

	@Transient
	private String montantCnps;

	private BigDecimal impotSalaire;

	@Transient
	private String montantIs;

	private BigDecimal ta;

	@Transient
	private String montantTa;

	private BigDecimal fpc;

	@Transient
	private String montantFpc;

	private BigDecimal prestationFamiliale;

	@Transient
	private String prestationFamil;

	private BigDecimal accidentTravail;

	@Transient
	private String accidentTrav;

	private BigDecimal retraite;

	@Transient
	private String montantRetraite;

	private BigDecimal indemniteFinCarriere;

	@Transient
	private String montantIndemniteFinCarriere;

	private BigDecimal totalPatronal;

	@Transient
	private String montantTotalPatronal;

	private BigDecimal totalMasseSalariale;

	@Transient
	private String montantTotalMasseSalariale;

	private BigDecimal brutImposable;

	@Transient
	private String montantBrutImposable;

	private BigDecimal netPayer;

	@Transient
	private String montantNetPayer;

	private BigDecimal totalRetenueFiscale;

	@Transient
	private String montantTotalRetenueFiscale;

	private BigDecimal totalRetenue;

	@Transient
	private String montantTotalRetenu;

	private BigDecimal totalBrut;

	@Transient
	private String montantTotalBrut;

	private BigDecimal totalChargeFiscalePatronale;

	@Transient
	private String montantcumulIts;

	@Transient
	private String montantcumulCn;

	private String montantcumulIgr;

	@Transient
	private String montantcumulCnpsSal;

	@Transient
	private String montantTotalChargeFiscalePatronale;

	private BigDecimal totalChargeSocialePatronale;

	@Transient
	private String montantTotalChargeSocialePatronale;

	private BigDecimal totalMasseSalarialeMensuelle;

	@Transient
	private String montantTotalMasseSalarialeMensuelle;

	private BigDecimal totalMasseSalarialeAnnuelle;

	@Transient
	private String montantTotalMasseSalarialeAnnuelle;


	@Transient
	private List<ImprimBulletinPaie> listImprimBulletinPaie;

	@Transient
	private List<ImprimBulletinPaie> listImprimBulletinPaieEngagement;

	@Transient
	private List<ImprimBulletinPaie> listImprimBulletinPaieIndemniteNonImp;

	/*private Boolean statut;

	@Transient
	private String enSommeil;*/

	@ManyToOne
	@JoinColumn(nullable=false)
	private ContratPersonnel contratPersonnel;

	@ManyToOne
	@JoinColumn(nullable=false)
	private PeriodePaie periodePaie;


	public Gratification() {
		super();
	}



	/*public Gratification(Long id, BigDecimal nombrePart, BigDecimal gratificationBase, BigDecimal sursalaire, BigDecimal indemniteTransport,
			BigDecimal its, BigDecimal cn, BigDecimal igr, BigDecimal cnps, BigDecimal impotSalaire, BigDecimal ta, BigDecimal fpc, BigDecimal prestationFamiliale,
			BigDecimal accidentTravail, BigDecimal retraite, BigDecimal indemniteFinCarriere, BigDecimal netPayer, BigDecimal brutImposable, BigDecimal totalPatronal, BigDecimal totalMasseSalariale,
			ContratPersonnel contratPersonnel, PeriodePaie periodePaie) {
		super();
		this.id = id;
		this.nombrePart = nombrePart;
		this.gratificationBase = gratificationBase;
		this.sursalaire = sursalaire;
		this.indemniteTransport = indemniteTransport;
		this.its = its;
		this.cn = cn;
		this.igr = igr;
		this.cnps = cnps;
		this.impotSalaire = impotSalaire;
		this.ta = ta;
		this.fpc = fpc;
		this.prestationFamiliale = prestationFamiliale;
		this.accidentTravail = accidentTravail;
		this.retraite = retraite;
		this.indemniteFinCarriere = indemniteFinCarriere;
		this.brutImposable = brutImposable;
		this.netPayer = netPayer;
		this.totalPatronal = totalPatronal;
		this.totalMasseSalariale = totalMasseSalariale;
		this.contratPersonnel = contratPersonnel;
		this.periodePaie = periodePaie;
	}*/


	public Gratification(BigDecimal nombrePart, BigDecimal gratificationBase, BigDecimal indemniteTransport, BigDecimal its, BigDecimal cn,
			BigDecimal igr, BigDecimal cnps, BigDecimal impotSalaire, BigDecimal ta, BigDecimal fpc, BigDecimal prestationFamiliale,
			BigDecimal accidentTravail, BigDecimal retraite, BigDecimal indemniteFinCarriere, BigDecimal totalPatronal,
			/*BigDecimal totalMasseSalariale, */BigDecimal brutImposable, BigDecimal netPayer, BigDecimal totalRetenueFiscale,
			BigDecimal totalRetenue, BigDecimal totalBrut, BigDecimal totalChargeFiscalePatronale, BigDecimal totalChargeSocialePatronale, BigDecimal totalMasseSalarialeMensuelle,
			BigDecimal totalMasseSalarialeAnnuelle, ContratPersonnel contratPersonnel, PeriodePaie periodePaie) {
		super();
		this.nombrePart = nombrePart;
		this.gratificationBase = gratificationBase;
		this.indemniteTransport = indemniteTransport;
		this.its = its;
		this.cn = cn;
		this.igr = igr;
		this.cnps = cnps;
		this.impotSalaire = impotSalaire;
		this.ta = ta;
		this.fpc = fpc;
		this.prestationFamiliale = prestationFamiliale;
		this.accidentTravail = accidentTravail;
		this.retraite = retraite;
		this.indemniteFinCarriere = indemniteFinCarriere;
		this.totalPatronal = totalPatronal;
		//this.totalMasseSalariale = totalMasseSalariale;
		this.brutImposable = brutImposable;
		this.netPayer = netPayer;
		this.totalRetenueFiscale = totalRetenueFiscale;
		this.totalRetenue = totalRetenue;
		this.totalBrut = totalBrut;
		this.totalChargeFiscalePatronale = totalChargeFiscalePatronale;
		this.totalChargeSocialePatronale = totalChargeSocialePatronale;
		this.totalMasseSalarialeMensuelle = totalMasseSalarialeMensuelle;
		this.totalMasseSalarialeAnnuelle = totalMasseSalarialeAnnuelle;
		this.contratPersonnel = contratPersonnel;
		this.periodePaie = periodePaie;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public BigDecimal getNombrePart() {
		return nombrePart;
	}


	public void setNombrePart(BigDecimal nombrePart) {
		this.nombrePart = nombrePart;
	}


	public String getNbrePart() {
		nbrePart = Utils.formattingAmount(nombrePart);
		return nbrePart;
	}


	public void setNbrePart(String nbrePart) {
		this.nbrePart = nbrePart;
	}


	public BigDecimal getGratificationBase() {
		return gratificationBase;
	}


	public void setGratificationBase(BigDecimal gratificationBase) {
		this.gratificationBase = gratificationBase;
	}


	public String getMontantGratificationBase() {
		montantGratificationBase = Utils.formattingAmount(gratificationBase);
		return montantGratificationBase;
	}


	public void setMontantGratificationBase(String montantGratificationBase) {
		this.montantGratificationBase = montantGratificationBase;
	}


	public BigDecimal getTauxGratification() {
		return tauxGratification;
	}


	public void setTauxGratification(BigDecimal tauxGratification) {
		this.tauxGratification = tauxGratification;
	}


	public String getMontantTauxGratification() {
		montantTauxGratification = Utils.formattingAmount(tauxGratification);
		return montantTauxGratification;
	}


	public void setMontantTauxGratification(String montantTauxGratification) {
		this.montantTauxGratification = montantTauxGratification;
	}


	public BigDecimal getIndemniteTransport() {
		return indemniteTransport;
	}


	public void setIndemniteTransport(BigDecimal indemniteTransport) {
		this.indemniteTransport = indemniteTransport;
	}


	public String getIndemniteTransp() {
		indemniteTransp = Utils.formattingAmount(indemniteTransport);
		return indemniteTransp;
	}


	public void setIndemniteTransp(String indemniteTransp) {
		this.indemniteTransp = indemniteTransp;
	}


	public BigDecimal getIts() {
		return its;
	}


	public void setIts(BigDecimal its) {
		this.its = its;
	}


	public String getMontantIts() {
		montantIts = Utils.formattingAmount(its);
		return montantIts;
	}


	public void setMontantIts(String montantIts) {
		this.montantIts = montantIts;
	}


	public BigDecimal getCn() {
		return cn;
	}


	public void setCn(BigDecimal cn) {
		this.cn = cn;
	}


	public String getMontantCn() {
		montantCn = Utils.formattingAmount(cn);
		return montantCn;
	}


	public void setMontantCn(String montantCn) {
		this.montantCn = montantCn;
	}


	public BigDecimal getIgr() {
		return igr;
	}


	public void setIgr(BigDecimal igr) {
		this.igr = igr;
	}


	public String getMontantIgr() {
		montantIgr = Utils.formattingAmount(igr);
		return montantIgr;
	}


	public void setMontantIgr(String montantIgr) {
		this.montantIgr = montantIgr;
	}


	public BigDecimal getCnps() {
		return cnps;
	}


	public void setCnps(BigDecimal cnps) {
		this.cnps = cnps;
	}


	public String getMontantCnps() {
		montantCnps = Utils.formattingAmount(cnps);
		return montantCnps;
	}


	public void setMontantCnps(String montantCnps) {
		this.montantCnps = montantCnps;
	}

	public BigDecimal getImpotSalaire() {
		return impotSalaire;
	}


	public void setImpotSalaire(BigDecimal impotSalaire) {
		this.impotSalaire = impotSalaire;
	}


	public String getMontantIs() {
		montantIs = Utils.formattingAmount(impotSalaire);
		return montantIs;
	}


	public void setMontantIs(String montantIs) {
		this.montantIs = montantIs;
	}


	public BigDecimal getTa() {
		return ta;
	}


	public void setTa(BigDecimal ta) {
		this.ta = ta;
	}


	public String getMontantTa() {
		montantTa = Utils.formattingAmount(ta);
		return montantTa;
	}


	public void setMontantTa(String montantTa) {
		this.montantTa = montantTa;
	}


	public BigDecimal getFpc() {
		return fpc;
	}


	public void setFpc(BigDecimal fpc) {
		this.fpc = fpc;
	}


	public String getMontantFpc() {
		montantFpc = Utils.formattingAmount(fpc);
		return montantFpc;
	}


	public void setMontantFpc(String montantFpc) {
		this.montantFpc = montantFpc;
	}


	public BigDecimal getPrestationFamiliale() {
		return prestationFamiliale;
	}


	public void setPrestationFamiliale(BigDecimal prestationFamiliale) {
		this.prestationFamiliale = prestationFamiliale;
	}


	public String getPrestationFamil() {
		prestationFamil = Utils.formattingAmount(prestationFamiliale);
		return prestationFamil;
	}


	public void setPrestationFamil(String prestationFamil) {
		this.prestationFamil = prestationFamil;
	}


	public BigDecimal getAccidentTravail() {
		return accidentTravail;
	}


	public void setAccidentTravail(BigDecimal accidentTravail) {
		this.accidentTravail = accidentTravail;
	}


	public String getAccidentTrav() {
		accidentTrav = Utils.formattingAmount(accidentTravail);
		return accidentTrav;
	}


	public void setAccidentTrav(String accidentTrav) {
		this.accidentTrav = accidentTrav;
	}


	public BigDecimal getRetraite() {
		return retraite;
	}


	public void setRetraite(BigDecimal retraite) {
		this.retraite = retraite;
	}


	public String getMontantRetraite() {
		montantRetraite = Utils.formattingAmount(retraite);
		return montantRetraite;
	}


	public void setMontantRetraite(String montantRetraite) {
		this.montantRetraite = montantRetraite;
	}


	public BigDecimal getIndemniteFinCarriere() {
		return indemniteFinCarriere;
	}


	public void setIndemniteFinCarriere(BigDecimal indemniteFinCarriere) {
		this.indemniteFinCarriere = indemniteFinCarriere;
	}


	public String getMontantIndemniteFinCarriere() {
		montantIndemniteFinCarriere = Utils.formattingAmount(indemniteFinCarriere);
		return montantIndemniteFinCarriere;
	}


	public void setMontantIndemniteFinCarriere(String montantIndemniteFinCarriere) {
		this.montantIndemniteFinCarriere = montantIndemniteFinCarriere;
	}


	public ContratPersonnel getContratPersonnel() {
		return contratPersonnel;
	}


	public void setContratPersonnel(ContratPersonnel contratPersonnel) {
		this.contratPersonnel = contratPersonnel;
	}


	public PeriodePaie getPeriodePaie() {
		return periodePaie;
	}


	public void setPeriodePaie(PeriodePaie periodePaie) {
		this.periodePaie = periodePaie;
	}


	public BigDecimal getSursalaire() {
		return sursalaire;
	}

	public void setSursalaire(BigDecimal sursalaire) {
		this.sursalaire = sursalaire;
	}

	public String getMontantSursalaire() {
		montantTa = Utils.formattingAmount(sursalaire);
		return montantTa;
	}

	public void setMontantSursalaire(String montantSursalaire) {
		this.montantSursalaire = montantSursalaire;
	}

	public BigDecimal getTotalPatronal() {
		return totalPatronal;
	}

	public void setTotalPatronal(BigDecimal totalPatronal) {
		this.totalPatronal = totalPatronal;
	}

	public String getMontantTotalPatronal() {
		montantTotalPatronal = Utils.formattingAmount(totalPatronal);
		return montantTotalPatronal;
	}

	public void setMontantTotalPatronal(String montantTotalPatronal) {
		this.montantTotalPatronal = montantTotalPatronal;
	}

	public BigDecimal getTotalMasseSalariale() {
		return totalMasseSalariale;
	}

	public void setTotalMasseSalariale(BigDecimal totalMassaeSalariale) {
		this.totalMasseSalariale = totalMassaeSalariale;
	}

	public String getMontantTotalMasseSalariale() {
		montantTotalMasseSalariale = Utils.formattingAmount(totalMasseSalariale);
		return montantTotalMasseSalariale;
	}

	public void setMontantTotalMasseSalariale(String montantTotalMassaeSalariale) {
		this.montantTotalMasseSalariale = montantTotalMassaeSalariale;
	}

	public BigDecimal getBrutImposable() {
		return brutImposable;
	}

	public void setBrutImposable(BigDecimal brutImposable) {
		this.brutImposable = brutImposable;
	}

	public String getMontantBrutImposable() {
		montantBrutImposable = Utils.formattingAmount(brutImposable);
		return montantBrutImposable;
	}

	public void setMontantBrutImposable(String montantBrutImposable) {
		this.montantBrutImposable = montantBrutImposable;
	}

	public BigDecimal getNetPayer() {
		return netPayer;
	}

	public void setNetPayer(BigDecimal netPayer) {
		this.netPayer = netPayer;
	}

	public String getMontantNetPayer() {
		montantNetPayer = Utils.formattingAmount(netPayer);
		return montantNetPayer;
	}

	public void setMontantNetPayer(String montantNetPayer) {
		this.montantNetPayer = montantNetPayer;
	}

	public BigDecimal getTotalRetenueFiscale() {
		return totalRetenueFiscale;
	}

	public void setTotalRetenueFiscale(BigDecimal totalRetenueFiscale) {
		this.totalRetenueFiscale = totalRetenueFiscale;
	}

	public String getMontantTotalRetenueFiscale() {
		montantTotalRetenueFiscale = Utils.formattingAmount(totalRetenueFiscale);
		return montantTotalRetenueFiscale;
	}

	public void setMontantTotalRetenueFiscale(String montantTotalRetenueFiscale) {
		this.montantTotalRetenueFiscale = montantTotalRetenueFiscale;
	}

	public BigDecimal getTotalRetenue() {
		return totalRetenue;
	}

	public void setTotalRetenue(BigDecimal totalRetenue) {
		this.totalRetenue = totalRetenue;
	}

	public String getMontantTotalRetenu() {
		montantTotalRetenu = Utils.formattingAmount(totalRetenue);
		return montantTotalRetenu;
	}

	public void setMontantTotalRetenu(String montantTotalRetenu) {
		this.montantTotalRetenu = montantTotalRetenu;
	}

	public BigDecimal getTotalBrut() {
		return totalBrut;
	}

	public void setTotalBrut(BigDecimal totalBrut) {
		this.totalBrut = totalBrut;
	}

	public String getMontantTotalBrut() {
		montantTotalBrut = Utils.formattingAmount(totalBrut);
		return montantTotalBrut;
	}

	public void setMontantTotalBrut(String montantTotalBrut) {
		this.montantTotalBrut = montantTotalBrut;
	}

	public BigDecimal getTotalChargeFiscalePatronale() {
		return totalChargeFiscalePatronale;
	}

	public void setTotalChargeFiscalePatronale(BigDecimal total) {
		this.totalChargeFiscalePatronale = total;
	}

	public String getMontantTotalChargeFiscalePatronale() {
		montantTotalChargeFiscalePatronale = Utils.formattingAmount(totalChargeFiscalePatronale);
		return montantTotalChargeFiscalePatronale;
	}

	public void setMontantTotalChargeFiscalePatronale(String montantTotal) {
		this.montantTotalChargeFiscalePatronale = montantTotal;
	}

	public BigDecimal getTotalChargeSocialePatronale() {
		return totalChargeSocialePatronale;
	}

	public void setTotalChargeSocialePatronale(BigDecimal totalChargeSocialePatronale) {
		this.totalChargeSocialePatronale = totalChargeSocialePatronale;
	}

	public String getMontantTotalChargeSocialePatronale() {
		montantTotalChargeSocialePatronale = Utils.formattingAmount(totalChargeSocialePatronale);
		return montantTotalChargeSocialePatronale;
	}

	public void setMontantTotalChargeSocialePatronale(String montantTotalChargeSocialePatronale) {
		this.montantTotalChargeSocialePatronale = montantTotalChargeSocialePatronale;
	}

	public BigDecimal getTotalMasseSalarialeMensuelle() {
		return totalMasseSalarialeMensuelle;
	}

	public void setTotalMasseSalarialeMensuelle(BigDecimal totalMasseSalarialeMensuelle) {
		this.totalMasseSalarialeMensuelle = totalMasseSalarialeMensuelle;
	}

	public String getMontantTotalMasseSalarialeMensuelle() {
		montantTotalMasseSalarialeMensuelle = Utils.formattingAmount(totalMasseSalarialeMensuelle);
		return montantTotalMasseSalarialeMensuelle;
	}

	public void setMontantTotalMasseSalarialeMensuelle(String montantTotalMasseSalarialeMensuelle) {
		this.montantTotalMasseSalarialeMensuelle = montantTotalMasseSalarialeMensuelle;
	}

	public BigDecimal getTotalMasseSalarialeAnnuelle() {
		return totalMasseSalarialeAnnuelle;
	}

	public void setTotalMasseSalarialeAnnuelle(BigDecimal totalMasseSalarialeAnnuelle) {
		this.totalMasseSalarialeAnnuelle = totalMasseSalarialeAnnuelle;
	}

	public String getMontantTotalMasseSalarialeAnnuelle() {
		montantTotalMasseSalarialeAnnuelle = Utils.formattingAmount(totalMasseSalarialeAnnuelle);
		return montantTotalMasseSalarialeAnnuelle;
	}

	public void setMontantTotalMasseSalarialeAnnuelle(String montantTotalMasseSalarialeAnnuelle) {
		this.montantTotalMasseSalarialeAnnuelle = montantTotalMasseSalarialeAnnuelle;
	}

	/*public Boolean getStatut() {
		return statut;
	}

	public void setStatut(Boolean statut) {
		this.statut = statut;
	}

	public String getEnSommeil() {
		if(statut)
			return "NON";
		return "OUI";
	}

	public void setEnSommeil(String enSommeil) {
		this.enSommeil = enSommeil;
	}*/

	@Override
	public String toString() {
		return "Gratification [id=" + id + ", nombrePart=" + nombrePart + ", nbrePart=" + nbrePart
				+ ", gratificationBase=" + gratificationBase + ", montantGratificationBase=" + montantGratificationBase
				+ ", tauxGratification=" + tauxGratification + ", montantTauxGratification=" + montantTauxGratification
				+ ", indemniteTransport=" + indemniteTransport + ", indemniteTransp=" + indemniteTransp + ", its=" + its
				+ ", montantIts=" + montantIts + ", cn=" + cn + ", montantCn=" + montantCn + ", igr=" + igr
				+ ", montantIgr=" + montantIgr + ", cnps=" + cnps + ", montantCnps=" + montantCnps + ", impotSalaire="
				+ impotSalaire + ", montantIs=" + montantIs + ", ta=" + ta + ", montantTa=" + montantTa + ", fpc=" + fpc
				+ ", montantFpc=" + montantFpc + ", prestationFamiliale=" + prestationFamiliale + ", prestationFamil="
				+ prestationFamil + ", accidentTravail=" + accidentTravail + ", accidentTrav=" + accidentTrav
				+ ", retraite=" + retraite + ", montantRetraite=" + montantRetraite + ", indemniteFinCarriere="
				+ indemniteFinCarriere + ", montantIndemniteFinCarriere=" + montantIndemniteFinCarriere
				+ ", contratPersonnel=" + contratPersonnel + ", periodePaie=" + periodePaie + "]";
	}



	public List<ImprimBulletinPaie> getListImprimBulletinPaie() {
		return listImprimBulletinPaie;
	}



	public void setListImprimBulletinPaie(
			List<ImprimBulletinPaie> listImprimBulletinPaie) {
		this.listImprimBulletinPaie = listImprimBulletinPaie;
	}



	public List<ImprimBulletinPaie> getListImprimBulletinPaieEngagement() {
		return listImprimBulletinPaieEngagement;
	}



	public void setListImprimBulletinPaieEngagement(
			List<ImprimBulletinPaie> listImprimBulletinPaieEngagement) {
		this.listImprimBulletinPaieEngagement = listImprimBulletinPaieEngagement;
	}



	public List<ImprimBulletinPaie> getListImprimBulletinPaieIndemniteNonImp() {
		return listImprimBulletinPaieIndemniteNonImp;
	}



	public void setListImprimBulletinPaieIndemniteNonImp(
			List<ImprimBulletinPaie> listImprimBulletinPaieIndemniteNonImp) {
		this.listImprimBulletinPaieIndemniteNonImp = listImprimBulletinPaieIndemniteNonImp;
	}



	public String getMontantcumulIts() {
		return montantcumulIts;
	}



	public void setMontantcumulIts(String montantcumulIts) {
		this.montantcumulIts = montantcumulIts;
	}



	public String getMontantcumulCn() {
		return montantcumulCn;
	}



	public void setMontantcumulCn(String montantcumulCn) {
		this.montantcumulCn = montantcumulCn;
	}



	public String getMontantcumulIgr() {
		return montantcumulIgr;
	}



	public void setMontantcumulIgr(String montantcumulIgr) {
		this.montantcumulIgr = montantcumulIgr;
	}



	public String getMontantcumulCnpsSal() {
		return montantcumulCnpsSal;
	}



	public void setMontantcumulCnpsSal(String montantcumulCnpsSal) {
		this.montantcumulCnpsSal = montantcumulCnpsSal;
	}

}
