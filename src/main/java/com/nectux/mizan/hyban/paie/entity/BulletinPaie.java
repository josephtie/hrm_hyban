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
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.utils.Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;

@Entity
@Component("bulletinPaies")
@Scope("prototype")
@Table(name="CGECI_RHPAIE_BULLETIN_PAIE")
@SequenceGenerator(name="CGECI_RHPAIE_BULLETIN_PAIE_SEQUENCE", sequenceName="CGECI_RHPAIE_BULLETIN_PAIE_SEQ", initialValue=1, allocationSize=1)
public class BulletinPaie extends Auditable {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CGECI_RHPAIE_BULLETIN_PAIE_SEQUENCE")
	@Column(unique=true, nullable=false)
	private Long id;
	
	private Float nombrePart;
	
	@Transient
	private String nbrePart;
	
	private int anciennete;
	
	
	@Transient
	private String montantAnciennete;
	
    private BigDecimal salairbase;
	
	@Transient
	private String montantSalairbase;
	
	
	 private BigDecimal primeanciennete;
	 
	 private Integer moisOfpresence;
	 
	private Integer tempsOfpresence;
		
    @Transient
	private String montantPrimeanciennete;
	
    private BigDecimal indemnitelogement;
	
    @Transient
	private String montantIndemnitelogement;
    
    private BigDecimal brutImposable;
		
	@Transient
	private String brutImpo;
	
	private BigDecimal autreIndemImposable;
	
	 @Transient
		private String montantautreIndemImposable;

	 private BigDecimal brutNonImposable;
		
	@Transient
	private String mtbrutNonImpo;

	private BigDecimal autrePrelevement;

	@Transient
	private String mtautrePrelevement;

	private BigDecimal regularisation;

	@Transient
	private String mtregularisation;

	private BigDecimal autreImposable;
	
	@Transient
	private String mtautreImposable;
	
	
	private BigDecimal autreNonImposable;
	
	@Transient
	private String mtautreNonImposable;
	
	
	
	private BigDecimal sursalaire;
	
	@Transient
	private String montantSursalaire;
	
	private Boolean calculer;
	
	private Boolean cloture;
	
	private Boolean congeAc;
	
	private BigDecimal its;
	
	private BigDecimal cumuljourTravail;
	
	private BigDecimal jourTravail;
	
	private BigDecimal temptravail;
	
	private BigDecimal cumulIts;
	@Transient
	private String montantcumulIts;
	
	private BigDecimal cumulCn;
	@Transient
	private String montantcumulCn;
	
	private BigDecimal cumulIgr;
	@Transient
	
	private String montantcumulIgr;
	
	private BigDecimal cumulCnpsSal;
	@Transient
	private String montantcumulCnpsSal;


	private BigDecimal isPatronal;
    @Transient
    private String montantisPatronal;

	@Transient
	private String montantIts;
	
	private BigDecimal cn;
	
	@Transient
	private String montantCn;
	
	private BigDecimal igr;
	
	@Transient
	private String montantIgr;
	
	
    private BigDecimal totalretenuefiscal;
	
	@Transient
	private String montanttotalretenuefiscal;
	
	
	private BigDecimal avanceetacompte;
		
	@Transient
	private String montantavanceetacompte;
	
	private BigDecimal carec;
	
	@Transient
	private String montantcarec;
	
	
	@Transient
	private String tpsdepresence;
	
	@Transient
	private String nbcongedu;
	
	
   private BigDecimal totalbrut;
	
	@Transient
	private String montanttotalbrut;
	
    private BigDecimal totalmassesalarial;
	
	@Transient
	private String montanttotalmassesalarial;
	
	private BigDecimal	cumulRetenueNet;
	@Transient
	private String montantcumulRetenueNet;
	
	private BigDecimal cumulSalaireNet;
	@Transient
	private String montantcumulSalaireNet;
	
    private BigDecimal totalpatronal;
	
	@Transient
	private String montanttotalpatronal;
	
	
	private BigDecimal totalretenue;
	
	@Transient
	private String montanttotalretenue;
	
    private BigDecimal pretaloes;
	
	@Transient
	private String montantpretaloes;
	
	private BigDecimal cnps;
	
	@Transient
	private String montantCnps;
	
	
private BigDecimal basecnps;
	
	@Transient
	private String montantbaseCnps;

	private BigDecimal retenueSociiale;
	@Transient
	private String mtretenueSociiale;
	
	private BigDecimal indemniteRepresentation;
	
	@Transient
	private String indemniteRepresent;
	
	
	private BigDecimal indemniteRespons;
	
	@Transient
	private String mtindemniteRespons;

	private BigDecimal CMUPatronal;

	@Transient
	private String mtCMUPatronal;


	private BigDecimal CMUSalarial;

	@Transient
	private String mtCMUSalarial;
	
	private BigDecimal indemniteTransport;
	
	@Transient
	private String indemniteTransp;
	
	
    private BigDecimal indemniteTransportImp;
	
	@Transient
	private String mtindemniteTranspImp;
	
    private BigDecimal netapayer;
	
	@Transient
	private String montantNetapayer;


    private BigDecimal netRegulPayer;

    @Transient
    private String montantNetRegulpayer;
	
	private BigDecimal impotSalaire;
	
	@Transient
	private String montantIs;
	
	private BigDecimal ta;
	
	@Transient
	private String montantTa;
	
	private BigDecimal fpc;
	private BigDecimal fpcregul;
	@Transient
	private String montantFpc;

	@Transient
	private String montantFpcregul;
	
	private BigDecimal prestationFamiliale;
	
	@Transient
	private String prestationFamil;
	
	private BigDecimal accidentTravail;
	
	@Transient
	private String accidentTrav;
	
	private BigDecimal retraite;



	@Transient
	private String montantRetraite;
	
	@Transient
	private List<ImprimBulletinPaie> listImprimBulletinPaie;
	
	@Transient
	private List<ImprimBulletinPaie> listImprimBulletinPaieEngagement;
	
	@Transient
	private List<ImprimBulletinPaie> listImprimBulletinPaieIndemniteNonImp;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private ContratPersonnel contratPersonnel;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private PeriodePaie periodePaie;
	
	public BulletinPaie() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getNombrePart() {
		return nombrePart;
	}

	public void setNombrePart(Float nombrePart) {
		this.nombrePart = nombrePart;
	}

	public BigDecimal getIsPatronal() {
		return isPatronal;
	}

	public void setIsPatronal(BigDecimal isPatronal) {
		this.isPatronal = isPatronal;
	}

	public String getNbrePart() {
		return nbrePart;
	}

	public String getMontantAnciennete() {
		return montantAnciennete;
	}

	public void setNbrePart(String nbrePart) {
		this.nbrePart = nbrePart;
	}

	public int getAnciennete() {
		return anciennete;
	}

	public void setAnciennete(int anciennete) {
		this.anciennete = anciennete;
	}

	

	public void setMontantAnciennete(String montantAnciennete) {
		this.montantAnciennete = montantAnciennete;
	}

	public BigDecimal getBrutImposable() {
		return brutImposable;
	}

	public void setBrutImposable(BigDecimal brutImposable) {
		this.brutImposable = brutImposable;
	}

	public String getBrutImpo() {
		brutImpo = Utils.formattingAmount(brutImposable);
		return brutImpo;
	}

	public void setBrutImpo(String brutImpo) {
		this.brutImpo = brutImpo;
	}

	public BigDecimal getSursalaire() {
		return sursalaire;
	}

	public void setSursalaire(BigDecimal sursalaire) {
		this.sursalaire = sursalaire;
	}

	public String getMontantSursalaire() {
		montantSursalaire = Utils.formattingAmount(sursalaire);
		return montantSursalaire;
	}

	public void setMontantSursalaire(String montantSursalaire) {
		this.montantSursalaire = montantSursalaire;
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

	public BigDecimal getIndemniteRepresentation() {
		return indemniteRepresentation;
	}

	public void setIndemniteRepresentation(BigDecimal indemniteRepresentation) {
		this.indemniteRepresentation = indemniteRepresentation;
	}

	public String getIndemniteRepresent() {
		indemniteRepresent = Utils.formattingAmount(indemniteRepresentation);
		return indemniteRepresent;
	}

	public void setIndemniteRepresent(String indemniteRepresent) {
		this.indemniteRepresent = indemniteRepresent;
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

	public BigDecimal getImpotSalaire() {
		return impotSalaire;
	}

	public void setImpotSalaire(BigDecimal impotSalaire) {
		this.impotSalaire = impotSalaire;
	}


    public String getMontantisPatronal() {
        return montantisPatronal=Utils.formattingAmount(isPatronal);
    }

    public void setMontantisPatronal(String montantisPatronal) {
        this.montantisPatronal = montantisPatronal;
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

	public Boolean getCalculer() {
		return calculer;
	}

	public void setCalculer(Boolean calculer) {
		this.calculer = calculer;
	}

	public Boolean getCloture() {
		return cloture;
	}

	public void setCloture(Boolean cloture) {
		this.cloture = cloture;
	}

	public BigDecimal getSalairbase() {
		return salairbase;
	}

	public void setSalairbase(BigDecimal salairbase) {
		this.salairbase = salairbase;
	}

	public String getMontantSalairbase() {
		montantSalairbase = Utils.formattingAmount(salairbase);
		return montantSalairbase;
	}

	public void setMontantSalairbase(String montantSalairbase) {
		this.montantSalairbase = montantSalairbase;
		
	}

	public BigDecimal getPrimeanciennete() {
		return primeanciennete;
	}

	public void setPrimeanciennete(BigDecimal primeanciennete) {
		this.primeanciennete = primeanciennete;
	}

	public String getMontantPrimeanciennete() {
		
		montantPrimeanciennete = Utils.formattingAmount(primeanciennete);
		return montantPrimeanciennete;
	}

	public void setMontantPrimeanciennete(String montantPrimeanciennete) {
		this.montantPrimeanciennete = montantPrimeanciennete;
	}

	public BigDecimal getIndemnitelogement() {
		return indemnitelogement;
	}

	public void setIndemnitelogement(BigDecimal indemnitelogement) {
		this.indemnitelogement = indemnitelogement;
	}

	public String getMontantIndemnitelogement() {
		return montantIndemnitelogement= Utils.formattingAmount(indemnitelogement);
	}

	public void setMontantIndemnitelogement(String montantIndemnitelogement) {
		this.montantIndemnitelogement = montantIndemnitelogement;
	}

	public BigDecimal getTotalretenuefiscal() {
		return totalretenuefiscal;
	}

	public void setTotalretenuefiscal(BigDecimal totalretenuefiscal) {
		this.totalretenuefiscal = totalretenuefiscal;
	}

	public String getMontanttotalretenuefiscal() {
		return montanttotalretenuefiscal= Utils.formattingAmount(totalretenuefiscal);
	}

	public void setMontanttotalretenuefiscal(String montanttotalretenuefiscal) {
		this.montanttotalretenuefiscal = montanttotalretenuefiscal;
	}

	public BigDecimal getAvanceetacompte() {
		return avanceetacompte;
	}

	public void setAvanceetacompte(BigDecimal avanceetacompte) {
		this.avanceetacompte = avanceetacompte;
	}

	public String getMontantavanceetacompte() {
		return montantavanceetacompte= Utils.formattingAmount(avanceetacompte);
	}

	public void setMontantavanceetacompte(String montantavanceetacompte) {
		this.montantavanceetacompte = montantavanceetacompte;
	}

	public BigDecimal getCarec() {
		return carec;
	}

	public void setCarec(BigDecimal carec) {
		this.carec = carec;
	}

	public String getMontantcarec() {
		return montantcarec= Utils.formattingAmount(carec);
	}

	public void setMontantcarec(String montantcarec) {
		this.montantcarec = montantcarec;
	}

	public BigDecimal getTotalmassesalarial() {
		return totalmassesalarial;
	}

	public void setTotalmassesalarial(BigDecimal totalmassesalarial) {
		this.totalmassesalarial = totalmassesalarial;
	}

	public String getMontanttotalmassesalarial() {
		return montanttotalmassesalarial= Utils.formattingAmount(totalmassesalarial);
	}

	public void setMontanttotalmassesalarial(String montanttotalmassesalarial) {
		this.montanttotalmassesalarial = montanttotalmassesalarial;
	}

	public BigDecimal getTotalpatronal() {
		return totalpatronal;
	}

	public void setTotalpatronal(BigDecimal totalpatronal) {
		this.totalpatronal = totalpatronal;
	}

	public String getMontanttotalpatronal() {
		return montanttotalpatronal= Utils.formattingAmount(totalpatronal);
	}

	public void setMontanttotalpatronal(String montanttotalpatronal) {
		this.montanttotalpatronal = montanttotalpatronal;
	}

	public BigDecimal getTotalretenue() {
		return totalretenue;
	}

	public void setTotalretenue(BigDecimal totalretenue) {
		this.totalretenue = totalretenue;
	}

	public String getMontanttotalretenue() {
		return montanttotalretenue= Utils.formattingAmount(totalretenue);
	}

	public void setMontanttotalretenue(String montanttotalretenue) {
		this.montanttotalretenue = montanttotalretenue;
	}

	public BigDecimal getPretaloes() {
		return pretaloes;
	}

	public void setPretaloes(BigDecimal pretaloes) {
		this.pretaloes = pretaloes;
	}

	public String getMontantpretaloes() {
		return montantpretaloes= Utils.formattingAmount(pretaloes);
	}

	public void setMontantpretaloes(String montantpretaloes) {
		this.montantpretaloes = montantpretaloes;
	}

	public BigDecimal getNetapayer() {
		return netapayer;
	}

	public void setNetapayer(BigDecimal netapayer) {
		this.netapayer = netapayer;
	}

	public String getMontantNetapayer() {
		return montantNetapayer= Utils.formattingAmount(netapayer);
	}

	public void setMontantNetapayer(String montantNetapayer) {
		this.montantNetapayer = montantNetapayer;
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


	public String getMtretenueSociiale() {
		return mtretenueSociiale;
	}

	public void setMtretenueSociiale(String mtretenueSociiale) {
		this.mtretenueSociiale = Utils.formattingAmount(retenueSociiale);
	}

	public BigDecimal getRetenueSociiale() {
		return retenueSociiale;
	}

	public void setRetenueSociiale(BigDecimal retenueSociiale) {
		this.retenueSociiale = retenueSociiale;
	}

	public BigDecimal getIndemniteRespons() {
		return indemniteRespons;
	}

	public void setIndemniteRespons(BigDecimal indemniteRespons) {
		this.indemniteRespons = indemniteRespons;
	}

	public String getMtindemniteRespons() {
		return mtindemniteRespons=Utils.formattingAmount(indemniteRespons);
	}

	public void setMtindemniteRespons(String mtindemniteRespons) {
		this.mtindemniteRespons = mtindemniteRespons;
	}

	public BigDecimal getIndemniteTransportImp() {
		return indemniteTransportImp;
	}

	public void setIndemniteTransportImp(BigDecimal indemniteTransportImp) {
		this.indemniteTransportImp = indemniteTransportImp;
	}

	

	public String getMtindemniteTranspImp() {
		return mtindemniteTranspImp=Utils.formattingAmount(indemniteTransportImp);
	}

	public void setMtindemniteTranspImp(String mtindemniteTranspImp) {
		this.mtindemniteTranspImp = mtindemniteTranspImp;
	}

	public BigDecimal getBrutNonImposable() {
		return brutNonImposable;
	}

	public void setBrutNonImposable(BigDecimal brutNonImposable) {
		this.brutNonImposable = brutNonImposable;
	}

	public String getMtbrutNonImpo() {
		return mtbrutNonImpo=Utils.formattingAmount(brutNonImposable);
	}

	public void setMtbrutNonImpo(String mtbrutNonImpo) {
		this.mtbrutNonImpo = mtbrutNonImpo;
	}

	public BigDecimal getFpcregul() {
		return fpcregul;
	}

	public void setFpcregul(BigDecimal fpcregul) {
		this.fpcregul = fpcregul;
	}

	public String getMontantFpcregul() {
		return montantFpcregul=Utils.formattingAmount(fpcregul);
	}

	public void setMontantFpcregul(String montantFpcregul) {
		this.montantFpcregul = montantFpcregul;
	}

    public BigDecimal getNetRegulPayer() {
        return netRegulPayer;
    }

    public void setNetRegulPayer(BigDecimal netRegulPayer) {
        this.netRegulPayer = netRegulPayer;
    }

    public String getMontantNetRegulpayer() {
        return montantNetRegulpayer;
    }

    public void setMontantNetRegulpayer(String montantNetRegulpayer) {
        this.montantNetRegulpayer = montantNetRegulpayer;
    }

    @Override
	public String toString() {
		return "BulletinPaie{" +
				"id=" + id +
				", nombrePart=" + nombrePart +
				", nbrePart='" + nbrePart + '\'' +
				", anciennete=" + anciennete +
				", montantAnciennete='" + montantAnciennete + '\'' +
				", salairbase=" + salairbase +
				", montantSalairbase='" + montantSalairbase + '\'' +
				", primeanciennete=" + primeanciennete +
				", moisOfpresence=" + moisOfpresence +
				", tempsOfpresence=" + tempsOfpresence +
				", montantPrimeanciennete='" + montantPrimeanciennete + '\'' +
				", indemnitelogement=" + indemnitelogement +
				", montantIndemnitelogement='" + montantIndemnitelogement + '\'' +
				", brutImposable=" + brutImposable +
				", brutImpo='" + brutImpo + '\'' +
				", autreIndemImposable=" + autreIndemImposable +
				", montantautreIndemImposable='" + montantautreIndemImposable + '\'' +
				", brutNonImposable=" + brutNonImposable +
				", mtbrutNonImpo='" + mtbrutNonImpo + '\'' +
				", autrePrelevement=" + autrePrelevement +
				", mtautrePrelevement='" + mtautrePrelevement + '\'' +
				", regularisation=" + regularisation +
				", mtregularisation='" + mtregularisation + '\'' +
				", autreImposable=" + autreImposable +
				", mtautreImposable='" + mtautreImposable + '\'' +
				", autreNonImposable=" + autreNonImposable +
				", mtautreNonImposable='" + mtautreNonImposable + '\'' +
				", sursalaire=" + sursalaire +
				", montantSursalaire='" + montantSursalaire + '\'' +
				", calculer=" + calculer +
				", cloture=" + cloture +
				", congeAc=" + congeAc +
				", its=" + its +
				", cumuljourTravail=" + cumuljourTravail +
				", jourTravail=" + jourTravail +
				", temptravail=" + temptravail +
				", cumulIts=" + cumulIts +
				", montantcumulIts='" + montantcumulIts + '\'' +
				", cumulCn=" + cumulCn +
				", montantcumulCn='" + montantcumulCn + '\'' +
				", cumulIgr=" + cumulIgr +
				", montantcumulIgr='" + montantcumulIgr + '\'' +
				", cumulCnpsSal=" + cumulCnpsSal +
				", montantcumulCnpsSal='" + montantcumulCnpsSal + '\'' +
				", montantIts='" + montantIts + '\'' +
				", cn=" + cn +
				", montantCn='" + montantCn + '\'' +
				", igr=" + igr +
				", montantIgr='" + montantIgr + '\'' +
				", totalretenuefiscal=" + totalretenuefiscal +
				", montanttotalretenuefiscal='" + montanttotalretenuefiscal + '\'' +
				", avanceetacompte=" + avanceetacompte +
				", montantavanceetacompte='" + montantavanceetacompte + '\'' +
				", carec=" + carec +
				", montantcarec='" + montantcarec + '\'' +
				", tpsdepresence='" + tpsdepresence + '\'' +
				", nbcongedu='" + nbcongedu + '\'' +
				", totalbrut=" + totalbrut +
				", montanttotalbrut='" + montanttotalbrut + '\'' +
				", totalmassesalarial=" + totalmassesalarial +
				", montanttotalmassesalarial='" + montanttotalmassesalarial + '\'' +
				", cumulRetenueNet=" + cumulRetenueNet +
				", montantcumulRetenueNet='" + montantcumulRetenueNet + '\'' +
				", cumulSalaireNet=" + cumulSalaireNet +
				", montantcumulSalaireNet='" + montantcumulSalaireNet + '\'' +
				", totalpatronal=" + totalpatronal +
				", montanttotalpatronal='" + montanttotalpatronal + '\'' +
				", totalretenue=" + totalretenue +
				", montanttotalretenue='" + montanttotalretenue + '\'' +
				", pretaloes=" + pretaloes +
				", montantpretaloes='" + montantpretaloes + '\'' +
				", cnps=" + cnps +
				", montantCnps='" + montantCnps + '\'' +
				", basecnps=" + basecnps +
				", montantbaseCnps='" + montantbaseCnps + '\'' +
				", indemniteRepresentation=" + indemniteRepresentation +
				", indemniteRepresent='" + indemniteRepresent + '\'' +
				", indemniteRespons=" + indemniteRespons +
				", mtindemniteRespons='" + mtindemniteRespons + '\'' +
				", indemniteTransport=" + indemniteTransport +
				", indemniteTransp='" + indemniteTransp + '\'' +
				", indemniteTransportImp=" + indemniteTransportImp +
				", mtindemniteTranspImp='" + mtindemniteTranspImp + '\'' +
				", netapayer=" + netapayer +
				", montantNetapayer='" + montantNetapayer + '\'' +
				", impotSalaire=" + impotSalaire +
				", montantIs='" + montantIs + '\'' +
				", ta=" + ta +
				", montantTa='" + montantTa + '\'' +
				", fpc=" + fpc +
				", montantFpc='" + montantFpc + '\'' +
				", prestationFamiliale=" + prestationFamiliale +
				", prestationFamil='" + prestationFamil + '\'' +
				", accidentTravail=" + accidentTravail +
				", accidentTrav='" + accidentTrav + '\'' +
				", retraite=" + retraite +
				", montantRetraite='" + montantRetraite + '\'' +
				", listImprimBulletinPaie=" + listImprimBulletinPaie +
				", listImprimBulletinPaieEngagement=" + listImprimBulletinPaieEngagement +
				", listImprimBulletinPaieIndemniteNonImp=" + listImprimBulletinPaieIndemniteNonImp +
				", contratPersonnel=" + contratPersonnel +
				", periodePaie=" + periodePaie +
				'}';
	}

	public BigDecimal getTotalbrut() {
		return totalbrut;
	}

	public void setTotalbrut(BigDecimal totalbrut) {
		this.totalbrut = totalbrut;
	}

	public String getMontanttotalbrut() {
		return montanttotalbrut= Utils.formattingAmount(totalbrut);
	}

	public void setMontanttotalbrut(String montanttotalbrut) {
		this.montanttotalbrut = montanttotalbrut;
	}

	public BigDecimal getCumulIts() {
		return cumulIts;
	}

	public void setCumulIts(BigDecimal cumulIts) {
		this.cumulIts = cumulIts;
	}

	public BigDecimal getCumulCn() {
		return cumulCn;
	}

	public void setCumulCn(BigDecimal cumulCn) {
		this.cumulCn = cumulCn;
	}

	public BigDecimal getCumulIgr() {
		return cumulIgr;
	}

	public void setCumulIgr(BigDecimal cumulIgr) {
		this.cumulIgr = cumulIgr;
	}

	public BigDecimal getCumulCnpsSal() {
		return cumulCnpsSal;
	}

	public void setCumulCnpsSal(BigDecimal cumulCnpsSal) {
		this.cumulCnpsSal = cumulCnpsSal;
	}

	public String getMontantcumulIts() {
		
		return montantcumulIts= Utils.formattingAmount(cumulIts);
	}

	public void setMontantcumulIts(String montantcumulIts) {
		this.montantcumulIts = montantcumulIts;
	}

	public String getMontantcumulCn() {		
		return montantcumulCn= Utils.formattingAmount(cumulCn);
	}

	public void setMontantcumulCn(String montantcumulCn) {
		this.montantcumulCn = montantcumulCn;
	}

	public String getMontantcumulIgr() {
		
		return montantcumulIgr= Utils.formattingAmount(cumulIgr);
	}

	public void setMontantcumulIgr(String montantcumulIgr) {
		this.montantcumulIgr = montantcumulIgr;
	}

	public String getMontantcumulCnpsSal() {
		
		return montantcumulCnpsSal= Utils.formattingAmount(cumulCnpsSal);
	}

	public void setMontantcumulCnpsSal(String montantcumulCnpsSal) {
		this.montantcumulCnpsSal = montantcumulCnpsSal;
	}

	public BigDecimal getJourTravail() {
		return jourTravail;
	}

	public void setJourTravail(BigDecimal jourTravail) {
		this.jourTravail = jourTravail;
	}

	public BigDecimal getTemptravail() {
		return temptravail;
	}

	public void setTemptravail(BigDecimal temptravail) {
		this.temptravail = temptravail;
	}

	public BigDecimal getCumuljourTravail() {
		return cumuljourTravail;
	}

	public void setCumuljourTravail(BigDecimal cumuljourTravail) {
		this.cumuljourTravail = cumuljourTravail;
	}

	public String getTpsdepresence() {
		return tpsdepresence;
	}

	public void setTpsdepresence(String tpsdepresence) {
		this.tpsdepresence = tpsdepresence;
	}

	public String getNbcongedu() {
		return nbcongedu;
	}

	public void setNbcongedu(String nbcongedu) {
		this.nbcongedu = nbcongedu;
	}

	

	public Integer getTempsOfpresence() {
		return tempsOfpresence;
	}

	public void setTempsOfpresence(Integer tempsOfpresence) {
		this.tempsOfpresence = tempsOfpresence;
	}

	public Integer getMoisOfpresence() {
		return moisOfpresence;
	}

	public void setMoisOfpresence(Integer moisOfpresence) {
		this.moisOfpresence = moisOfpresence;
	}

	public Boolean getCongeAc() {
		return congeAc;
	}

	public void setCongeAc(Boolean congeAc) {
		this.congeAc = congeAc;
	}

	public BigDecimal getBasecnps() {
		return basecnps;
	}

	public void setBasecnps(BigDecimal basecnps) {
		this.basecnps = basecnps;
	}

	public String getMontantbaseCnps() {
		return montantbaseCnps=Utils.formattingAmount(basecnps);
	}

	public void setMontantbaseCnps(String montantbaseCnps) {
		this.montantbaseCnps = montantbaseCnps;
	}

	public BigDecimal getAutreImposable() {
		return autreImposable;
	}

	public void setAutreImposable(BigDecimal autreImposable) {
		this.autreImposable = autreImposable;
	}

	public String getMtautreImposable() {
		return mtautreImposable;
	}

	public void setMtautreImposable(String mtautreImposable) {
		this.mtautreImposable =Utils.formattingAmount(autreImposable);
	}

	public BigDecimal getAutreNonImposable() {
		return autreNonImposable;
	}

	public void setAutreNonImposable(BigDecimal autreNonImposable) {
		this.autreNonImposable = autreNonImposable;
	}

	public String getMtautreNonImposable() {
		return mtautreNonImposable=Utils.formattingAmount(autreNonImposable);
	}

	public void setMtautreNonImposable(String mtautreNonImposable) {
		this.mtautreNonImposable = mtautreNonImposable;
	}

	public BigDecimal getCumulRetenueNet() {
		return cumulRetenueNet;
	}

	public void setCumulRetenueNet(BigDecimal cumulRetenueNet) {
		this.cumulRetenueNet = cumulRetenueNet;
	}

	public String getMontantcumulRetenueNet() {
		return montantcumulRetenueNet=Utils.formattingAmount(cumulRetenueNet);
	}

	public void setMontantcumulRetenueNet(String montantcumulRetenueNet) {
		this.montantcumulRetenueNet = montantcumulRetenueNet;
	}

	public BigDecimal getCumulSalaireNet() {
		return cumulSalaireNet;
	}

	public void setCumulSalaireNet(BigDecimal cumulSalaireNet) {
		this.cumulSalaireNet = cumulSalaireNet;
	}

	public String getMontantcumulSalaireNet() {
		return montantcumulSalaireNet=Utils.formattingAmount(cumulSalaireNet);
	}

	public void setMontantcumulSalaireNet(String montantcumulSalaireNet) {
		this.montantcumulSalaireNet = montantcumulSalaireNet;
	}

	public BigDecimal getAutreIndemImposable() {
		return autreIndemImposable;
	}

	public void setAutreIndemImposable(BigDecimal autreIndemImposable) {
		this.autreIndemImposable = autreIndemImposable;
	}

	public String getMontantautreIndemImposable() {
		return montantautreIndemImposable=Utils.formattingAmount(autreIndemImposable);
	}

	public void setMontantautreIndemImposable(String montantautreIndemImposable) {
		this.montantautreIndemImposable = montantautreIndemImposable;
	}

	public BigDecimal getAutrePrelevement() {
		return autrePrelevement;
	}

	public void setAutrePrelevement(BigDecimal autrePrelevement) {
		this.autrePrelevement = autrePrelevement;
	}

	public String getMtautrePrelevement() {
		return mtautrePrelevement;
	}

	public void setMtautrePrelevement(String mtautrePrelevement) {
		this.mtautrePrelevement = mtautrePrelevement;
	}

	public BigDecimal getRegularisation() {
		return regularisation;
	}

	public void setRegularisation(BigDecimal regularisation) {
		this.regularisation = regularisation;
	}

	public String getMtregularisation() {
		return mtregularisation;
	}

	public void setMtregularisation(String mtregularisation) {
		this.mtregularisation = mtregularisation;
	}


	public BigDecimal getCMUPatronal() {
		return CMUPatronal;
	}

	public void setCMUPatronal(BigDecimal CMUPatronal) {
		this.CMUPatronal = CMUPatronal;
	}

	public String getMtCMUPatronal() {
		return mtCMUPatronal;
	}

	public void setMtCMUPatronal(String mtCMUPatronal) {
		this.mtCMUPatronal = mtCMUPatronal;
	}

	public BigDecimal getCMUSalarial() {
		return CMUSalarial;
	}

	public void setCMUSalarial(BigDecimal CMUSalarial) {
		this.CMUSalarial = CMUSalarial;
	}

	public String getMtCMUSalarial() {
		return mtCMUSalarial;
	}

	public void setMtCMUSalarial(String mtCMUSalarial) {
		this.mtCMUSalarial = mtCMUSalarial;
	}
}
