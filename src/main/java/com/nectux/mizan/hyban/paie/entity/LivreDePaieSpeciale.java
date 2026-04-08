package com.nectux.mizan.hyban.paie.entity;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.personnel.specifque.entity.SpecialContract;
import com.nectux.mizan.hyban.utils.DifferenceDate;
import com.nectux.mizan.hyban.utils.ProvisionConge;
import com.nectux.mizan.hyban.utils.Utils;

import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.util.Date;


public class LivreDePaieSpeciale {


	private String matricule;

	private String nomPrenom;



	private BigDecimal avceAcpte;
	@Transient
	private String mtavceAcpte;

	private BigDecimal pretAlios;
	@Transient
	private String mtpretAlios;

    private BigDecimal regularisation;
    @Transient
    private String mtregularisation;



	private BigDecimal jourTravail;

	private BigDecimal temptravail;


    private BigDecimal netPayer;
    @Transient
    private String mtnetPayer;


	private SpecialContract contratPersonnel;


	private PeriodePaie periodePaie;


    private  BulletinSpeciale bulletinSpeciale;


	public LivreDePaieSpeciale() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LivreDePaieSpeciale(String mat, String nomPre, BigDecimal avanceEtAccompte, BigDecimal pretALIOS, SpecialContract ctratperso, TempEffectif tempeffect, PeriodePaie plconge) {
		super();
		this.matricule = mat;
		this.nomPrenom = nomPre;
        final int JOURS_OUVRABLES_MOIS = 30;
        this.regularisation= BigDecimal.ZERO;
       this.contratPersonnel=ctratperso;
		if(ctratperso.getEmployee().getActif()==true){

						if(tempeffect==null){



							 this.jourTravail= BigDecimal.valueOf(30);
							 this.temptravail=BigDecimal.valueOf(173.33);
                             this.netPayer= contratPersonnel.getRemunerationForfaitaire();
						}
					   else
					   {

							   this.jourTravail= BigDecimal.valueOf(tempeffect.getJourspresence());
							   this.temptravail= BigDecimal.valueOf(tempeffect.getHeurspresence());

                           this.netPayer= (contratPersonnel.getRemunerationForfaitaire().multiply(BigDecimal.valueOf(tempeffect.getJourspresence())).divide(BigDecimal.valueOf(JOURS_OUVRABLES_MOIS)));
					   }
		

				this.avceAcpte = avanceEtAccompte;
				this.pretAlios = pretALIOS;

				this.netPayer = netPayer.add(regularisation).subtract(pretAlios).subtract(avceAcpte);

	    }



	}

//	public int countnbreJrdu(Date dateRetourDernierConge, Date dateDepartConge, ContratPersonnel Contratp) {
//		// TODO Auto-generated method stub
//
//		int tps=ProvisionConge.calculerTempsPresence(dateRetourDernierConge,dateDepartConge);
//		int rf=(int) (tps*2.2*1.25);
//		 BigDecimal[]ancienete= calculAnciennete(Contratp.getCategorie().getSalaireDeBase(),Contratp.getPersonnel().getDateArrivee());
//		 	BigDecimal newancienete;
//	    	if(Contratp.getAncienneteInitial()!=0) {
//	    		 newancienete=ancienete[1] +Contratp.getAncienneteInitial();
//	    	}else{
//	    		newancienete=ancienete[1];
//	    	}
//	    	BigDecimal anc=(int)newancienete ;
//
//	     int jourSuppAnc=0; int jourSuppDam = 0;int jourSuppMed = 0;
//
//		 if(anc>5 && anc<=10)  jourSuppAnc=1;
//		 if(anc>10 && anc<=15) jourSuppAnc=2;
//		 if(anc>15 && anc<=20) jourSuppAnc=3;
//		 if(anc>20 && anc<=25) jourSuppAnc=5;
//		 if(anc>25 && anc<=30) jourSuppAnc=7;
//		 if(anc>30) jourSuppAnc=8;
//
//		 BigDecimal age= DifferenceDate.valAge(new Date(), Contratp.getPersonnel().getDateNaissance());
//		 if(Contratp.getPersonnel().getSexe().equals("Feminin") && age<=21 && Contratp.getPersonnel().getNombrEnfant()>0){
//			 jourSuppDam=2*Contratp.getPersonnel().getNombrEnfant();
//		 }
//		 if(Contratp.getPersonnel().getSexe().equals("Feminin") && age>21 && Contratp.getPersonnel().getNombrEnfant()>0){
//
//			 if(Contratp.getPersonnel().getNombrEnfant()>=4)jourSuppDam=2*1;
//			 if(Contratp.getPersonnel().getNombrEnfant()>=5)jourSuppDam=2*2;
//			 if(Contratp.getPersonnel().getNombrEnfant()>=6)jourSuppDam=2*3;
//			 if(Contratp.getPersonnel().getNombrEnfant()>=7)jourSuppDam=2*4;
//			 if(Contratp.getPersonnel().getNombrEnfant()>=8)jourSuppDam=2*5;
//			 if(Contratp.getPersonnel().getNombrEnfant()>=9)jourSuppDam=2*6;
//		 }
//
//		 if(Contratp.getPersonnel().getSituationMedaille()==1 ){
//			 jourSuppMed=1;
//		 }
//		 int rfp=(int) (jourSuppAnc+jourSuppDam+jourSuppMed);
//		return (int) rfp+rf;
//	}
//
//
//
//
//public  BigDecimal[] calculAnciennete(BigDecimal salaireCategoriel, Date dateEntree){
//
//		BigDecimal[] tab = new BigDecimal[5];
//
//		BigDecimal anciennete = (BigDecimal) 0;
//
//
//		BigDecimal age = DifferenceDate.valAge(new Date(), dateEntree);
//
//		int partieEntiere = (int) age;
//		int partieApresVirg = (int)((age - partieEntiere) * 12);
//
//
//		if(age>=2)
//			anciennete = (BigDecimal) (salaireCategoriel*partieEntiere/100);
//
//		tab[0] = anciennete;
//
//
//		tab[1] = (BigDecimal) partieEntiere;
//		tab[2] = (BigDecimal) (partieApresVirg);
//
//
//
//		return tab;
//	}

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public BigDecimal getAvceAcpte() {
        return avceAcpte;
    }

    public void setAvceAcpte(BigDecimal avceAcpte) {
        this.avceAcpte = avceAcpte;
    }

    public String getMtavceAcpte() {
        return mtavceAcpte=Utils.formattingAmount(avceAcpte);
    }

    public void setMtavceAcpte(String mtavceAcpte) {
        this.mtavceAcpte = mtavceAcpte;
    }

    public BigDecimal getPretAlios() {
        return pretAlios;
    }

    public void setPretAlios(BigDecimal pretAlios) {
        this.pretAlios = pretAlios;
    }

    public String getMtpretAlios() {
        return mtpretAlios=Utils.formattingAmount(pretAlios);
    }

    public void setMtpretAlios(String mtpretAlios) {
        this.mtpretAlios = mtpretAlios;
    }

    public BigDecimal getRegularisation() {
        return regularisation;
    }

    public void setRegularisation(BigDecimal regularisation) {
        this.regularisation = regularisation;
    }

    public String getMtregularisation() {
        return mtregularisation=Utils.formattingAmount(regularisation);
    }

    public void setMtregularisation(String mtregularisation) {
        this.mtregularisation = mtregularisation;
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

    public BigDecimal getNetPayer() {
        return netPayer;
    }

    public void setNetPayer(BigDecimal netPayer) {
        this.netPayer = netPayer;
    }

    public String getMtnetPayer() {
        return mtnetPayer=Utils.formattingAmount(netPayer);
    }

    public void setMtnetPayer(String mtnetPayer) {
        this.mtnetPayer = mtnetPayer;
    }

    public SpecialContract getContratPersonnel() {
        return contratPersonnel;
    }

    public void setContratPersonnel(SpecialContract contratPersonnel) {
        this.contratPersonnel = contratPersonnel;
    }

    public PeriodePaie getPeriodePaie() {
        return periodePaie;
    }

    public void setPeriodePaie(PeriodePaie periodePaie) {
        this.periodePaie = periodePaie;
    }

    public BulletinSpeciale getBulletinSpeciale() {
        return bulletinSpeciale;
    }

    public void setBulletinSpeciale(BulletinSpeciale bulletinSpeciale) {
        this.bulletinSpeciale = bulletinSpeciale;
    }


    @Override
    public String toString() {
        return "LivreDePaieSpeciale{" +
                "matricule='" + matricule + '\'' +
                ", nomPrenom='" + nomPrenom + '\'' +
                ", avceAcpte=" + avceAcpte +
                ", mtavceAcpte='" + mtavceAcpte + '\'' +
                ", pretAlios=" + pretAlios +
                ", mtpretAlios='" + mtpretAlios + '\'' +
                ", regularisation=" + regularisation +
                ", mtregularisation='" + mtregularisation + '\'' +
                ", jourTravail=" + jourTravail +
                ", temptravail=" + temptravail +
                ", netPayer=" + netPayer +
                ", mtnetPayer='" + mtnetPayer + '\'' +
                ", contratPersonnel=" + contratPersonnel +
                ", periodePaie=" + periodePaie +
                ", bulletinSpeciale=" + bulletinSpeciale +
                '}';
    }
}
