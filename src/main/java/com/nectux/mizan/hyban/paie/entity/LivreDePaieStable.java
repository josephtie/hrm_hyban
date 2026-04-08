/*
package com.nectux.mizan.hyban.paie.entity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

import com.nectux.mizan.hyban.paie.repository.PrimePersonnelRepository;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.repository.RubriqueRepository;
import com.nectux.mizan.hyban.parametrages.entity.Rubrique;
import com.nectux.mizan.hyban.utils.CalculRICF;
import com.nectux.mizan.hyban.utils.DifferenceDate;
import com.nectux.mizan.hyban.utils.Utils;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.utils.ProvisionConge;
import org.springframework.beans.factory.annotation.Autowired;

import static com.nectux.mizan.hyban.utils.CNPSCalculator.calculerCNPS;
import static com.nectux.mizan.hyban.utils.CalculRICF.getRICF;
import static com.nectux.mizan.hyban.utils.ITSCalculator.calculerITS;


public class LivreDePaieStable {




    private String matricule;

    private String nomPrenom;

    private Float nombrePart;

    private int anciennete;

    private BigDecimal salaireBase;

    @Transient
    private String mtsalaireBase;

    private BigDecimal autreImposable;

    @Transient
    private String mtautreImposable;

    private BigDecimal autreIndemImposable;

    @Transient
    private String mtautreIndemImposable;


    private BigDecimal plafondFamiliale;
    @Transient
    private String mtplafondFamiliale;
    private BigDecimal autreNonImposable;

    @Transient
    private String mtautreNonImposable;

    private BigDecimal sursalaire;

    @Transient
    private String mtsursalaire;

    private BigDecimal primeAnciennete;

    private Integer moisdepresence;

    private Integer tempspresence;

    @Transient
    private String mtprimeAnciennete;

    private BigDecimal indemniteLogement;
    @Transient
    private String mtindemniteLogement;

    private BigDecimal brutImposable;
    @Transient
    private String mtbrutImposable;

    private BigDecimal autrePrelevmentSociale;
    @Transient
    private String mtautrePrelevmentSociale;

    private BigDecimal autrePrelevment;
    @Transient
    private String mtautrePrelevment;


    private BigDecimal autrePrelevmentMutuelle;
    @Transient
    private String mtautrePrelevmentMutuelle;

    private BigDecimal regularisation;
    @Transient
    private String mtregularisation;

    private BigDecimal brutNonImposable;
    @Transient
    private String mtbrutNonImposable;

    private BigDecimal its;


    @Transient
    private String mtits;

    private BigDecimal CMU;
    @Transient
    private String mtCMU ;

    private BigDecimal CMUSalarial;
    @Transient
    private String mtCMUSalarial ;

    private BigDecimal CMUPatronal;
    @Transient
    private String mtCMUPatronal ;

    private BigDecimal cn;
    @Transient
    private String mtcn;

    private BigDecimal igr;
    @Transient
    private String mtigr;

    private BigDecimal totalRetenueFiscale;
    @Transient
    private String mttotalRetenueFiscale;

    private BigDecimal cnps;
    @Transient
    private String mtcnps;

    private BigDecimal basecnps;
    @Transient
    private String mtbasecnps;

    private BigDecimal avceAcpte;
    @Transient
    private String mtavceAcpte;

    private BigDecimal pretAlios;
    @Transient
    private String mtpretAlios;

    private BigDecimal carec;
    @Transient
    private String mtcarec;

    private BigDecimal totalRetenue;
    @Transient
    private String mttotalRetenue;

    private BigDecimal RetenueSociale;
    @Transient
    private String mtRetenueSocial;

    private BigDecimal totalRetenueSociale;
    @Transient
    private String mttotalRetenueSocial;

    private BigDecimal indemniteRepresentation;
    @Transient
    private String mtindemniteRepresentation;

    private BigDecimal indemniteTransport;
    @Transient
    private String mtindemniteTransport;


    private BigDecimal indemniteTransportImp;
    @Transient
    private String mtindemniteTransportImp;

    private BigDecimal indemniteResponsabilte;
    @Transient
    private String mtindemniteResponsabilte;

    private BigDecimal netRegulPayer;
    @Transient
    private String mtRegulnetPayer;


    private BigDecimal netPayer;
    @Transient
    private String mtnetPayer;

    private BigDecimal totalBrut;
    @Transient
    private String mttotalBrut;

    private BigDecimal is;
    @Transient
    private String mtis;

    private BigDecimal ta;
    @Transient
    private String mtta;

    private BigDecimal fpc;
    @Transient
    private String mtfpc;

    private BigDecimal fpcregul;
    @Transient
    private String mtfpcregul;

    private BigDecimal prestationFamiliale;
    @Transient
    private String mtprestationFamiliale;

    private BigDecimal accidentTravail;
    @Transient
    private String mtaccidentTravail;

    private BigDecimal retraite;
    @Transient
    private String mtretraite;

    private BigDecimal totalPatronal;
    @Transient
    private String mttotalPatronal;

    private BigDecimal totalMasseSalariale;
    @Transient
    private String mttotalMasseSalariale;

    private BigDecimal retenueSociiale;
    @Transient
    private String mtretenueSociiale;

    private BulletinPaie bullpaie;

    private BigDecimal jourTravail;

    private BigDecimal temptravail;

    @Transient
    List<PrimePersonnel> listIndemniteBrut = new ArrayList<PrimePersonnel>();

    @Transient
    List<PrimePersonnel> listIndemniteNonImp = new ArrayList<PrimePersonnel>();

    @Transient
    List<Rubrique> listRubrique = new ArrayList<Rubrique>();

    @Transient
    List<PrimePersonnel> listIndemBrutNonImp = new ArrayList<PrimePersonnel>();


    @Transient
    List<PrimePersonnel> listRetenueMutuellt = new ArrayList<PrimePersonnel>();


    @Transient
    List<PrimePersonnel> listRetenueSociale = new ArrayList<PrimePersonnel>();

    @Transient
    List<PrimePersonnel> listGainsNet = new ArrayList<PrimePersonnel>();


    @ManyToOne
    @JoinColumn(nullable=false)
    private ContratPersonnel contratPersonnel;

    @ManyToOne
    @JoinColumn(nullable=false)
    private PeriodePaie periodePaie;


    public LivreDePaieStable() {
        super();
        // TODO Auto-generated constructor stub
    }

    public LivreDePaieStable(String mat, String nomPre, Float nbrePart, int ancien, BigDecimal salBase, BigDecimal sursal, BigDecimal indemLog, BigDecimal avanceEtAccompte, BigDecimal pretALIOS, ContratPersonnel ctratperso, TempEffectif tempeffect, PeriodePaie plconge, List<PrimePersonnel> listIndemnite, List<PrimePersonnel> listIndemniteNonImp1, List<PrimePersonnel> listMutuelle, List<PrimePersonnel> listGains, List<PrimePersonnel> listRetenueSociale) {
        super();
        this.matricule = mat;
        this.nomPrenom = nomPre;
        final int JOURS_OUVRABLES_MOIS = 30;
        BigDecimal netCible= BigDecimal.valueOf(0);
        this.listIndemniteBrut=listIndemnite;
        //this.listRubrique=rubriqueRepository.findByActiveTrue();
        this.listIndemniteNonImp=listIndemniteNonImp1;
        this.listRetenueSociale =listRetenueSociale;
        this.listRetenueMutuellt=listMutuelle;
        this.listGainsNet=listGains;
        if(tempeffect!=null)
            netCible= ctratperso.getNetAPayer().multiply(BigDecimal.valueOf(tempeffect.getJourspresence())).divide(BigDecimal.valueOf(JOURS_OUVRABLES_MOIS));


        if (Boolean.TRUE.equals(ctratperso.getPersonnel().getCarec())){

            if(tempeffect==null){

                this.nombrePart = nbrePart != null ? nbrePart : 0F;
                this.anciennete = ancien;
                this.salaireBase = salBase;
                if(sursal==null)
                    this.sursalaire = BigDecimal.valueOf(0);
                else
                    this.sursalaire = sursal;

                //	if(ancien>= 2)
                //		this.primeAnciennete = Math.ceil((salaireBase) * ancien / 100);
                //	else
                //	 this.primeAnciennete = Math.ceil(0);

                if(indemLog==null)
                    this.indemniteLogement = BigDecimal.valueOf(0);
                else
                    this.indemniteLogement = indemLog;


                if(ctratperso.getIndemniteTransport()==null)
                {
                    this.indemniteTransportImp= BigDecimal.valueOf(0);
                    this.indemniteTransport= BigDecimal.valueOf(0);
                }else{

                    if(ctratperso.getIndemniteTransport() > 30000){
                        this.indemniteTransportImp= BigDecimal.valueOf(ctratperso.getIndemniteTransport()-30000);
                        this.indemniteTransport= BigDecimal.valueOf(30000);
                    }else{
                        this.indemniteTransportImp= BigDecimal.valueOf(0);
                        this.indemniteTransport= BigDecimal.valueOf(ctratperso.getIndemniteTransport());
                    }
                }


                this.autreIndemImposable= BigDecimal.valueOf(0);

                autreImposable= BigDecimal.valueOf(0);
                if (listIndemniteBrut != null && !listIndemniteBrut.isEmpty()) {
                    for(PrimePersonnel primeImpos : listIndemniteBrut){
                        if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
                        {
                            autreImposable= autreImposable.add(primeImpos.getMontant()) ;

                        }else{
                            if(primeImpos.getPrime().getMtExedent()!=null)
                                autreImposable=autreImposable.add(primeImpos.getMontant()).subtract(primeImpos.getPrime().getMtExedent());
                            else
                                autreImposable=autreImposable.add(primeImpos.getMontant());
                        }
                    }
                }

                this.jourTravail= BigDecimal.valueOf(30d);
                this.temptravail= BigDecimal.valueOf(173.33d);
            }
            else
            {

                this.anciennete = ancien;

                this.nombrePart = nbrePart != null ? nbrePart : 0F;
                this.salaireBase = salBase.multiply(BigDecimal.valueOf(tempeffect.getJourspresence())).divide(BigDecimal.valueOf(JOURS_OUVRABLES_MOIS));
                if(sursal==null)
                    this.sursalaire = BigDecimal.valueOf(0d);
                else
                    this.sursalaire = sursal.multiply(BigDecimal.valueOf(tempeffect.getJourspresence())).divide(BigDecimal.valueOf(JOURS_OUVRABLES_MOIS));

                //if(ancien>= 2)
                //  this.primeAnciennete = Math.ceil((salaireBase) * ancien / 100);
                //else
                //	this.primeAnciennete = Math.ceil(0);

                if(indemLog==null)
                    this.indemniteLogement = BigDecimal.valueOf(0);
                else
                    this.indemniteLogement = indemLog.multiply(BigDecimal.valueOf(tempeffect.getJourspresence())).divide(BigDecimal.valueOf(JOURS_OUVRABLES_MOIS));

                if(ctratperso.getIndemniteTransport()==null)
                {
                    this.indemniteTransportImp= BigDecimal.valueOf(0);
                    this.indemniteTransport= BigDecimal.valueOf(0);

                }else{
                    if(ctratperso.getIndemniteTransport() > 30000){
                        this.indemniteTransportImp=(ctratperso.getIndemniteTransport()).s-30000*tempeffect.getJourspresence())).divide(BigDecimal.valueOf(JOURS_OUVRABLES_MOIS));
                        this.indemniteTransport=30000d;
                    }else{
                        this.indemniteTransportImp=0d;
                        this.indemniteTransport=ctratperso.getIndemniteTransport();
                    }
                }

                this.autreIndemImposable=0d;
                if(ctratperso.getIndemniteRepresent()==null) ctratperso.setIndemniteRepresent(0d);

                //			       if((indemniteTransport+ctratperso.getIndemniteRepresent())<(salaireBase+primeAnciennete+indemniteLogement+sursalaire+ctratperso.getIndemniteRepresent()+indemniteTransport)*10/100)
                //				      autreIndemImposable=0d;
                //					  else
                //					   autreIndemImposable=Math.ceil((indemniteTransport+ctratperso.getIndemniteRepresent()-((salaireBase+primeAnciennete+indemniteLogement+sursalaire+ctratperso.getIndemniteRepresent()+indemniteTransport)*10/100)));
                //		           //*tempeffect.getJourspresence()/30
                autreImposable=0d;

                if (listIndemniteBrut != null && !listIndemniteBrut.isEmpty()) {
                    for(PrimePersonnel primeImpos : listIndemniteBrut){
                        if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
                        {
                            autreImposable=autreImposable+(primeImpos.getValeur()*(primeImpos.getMontant()+(primeImpos.getMontant()*primeImpos.getPrime().getTaux()/100))*tempeffect.getJourspresence()/JOURS_OUVRABLES_MOIS);
                        }else{

                            if(primeImpos.getPrime().getMtExedent()!=null)
                                autreImposable=autreImposable+(primeImpos.getMontant()-primeImpos.getPrime().getMtExedent())*tempeffect.getJourspresence()/JOURS_OUVRABLES_MOIS;
                            else
                                autreImposable=autreImposable+primeImpos.getMontant()*tempeffect.getJourspresence()/JOURS_OUVRABLES_MOIS;
                        }
                    }
                }
                this.jourTravail=tempeffect.getJourspresence();
                this.temptravail=tempeffect.getHeurspresence();
            }

            // this.brutImposable = Math.ceil(salaireBase + sursalaire + primeAnciennete + indemniteLogement+indemniteTransportImp+autreIndemImposable+autreImposable);
            this.brutImposable = Math.ceil(salaireBase + sursalaire  + indemniteLogement+indemniteTransportImp+autreIndemImposable+autreImposable);
            BigDecimal ricf = getRICF(nombrePart);
            BigDecimal itsbrut =Math.ceil(calculerITS(brutImposable,true));
            this.its = Math.max(0, itsbrut - ricf / 12);
            this.cn = 0d;
            this.igr = 0d;
            this.totalRetenueFiscale = Math.ceil(its );//+ cn + igr);
            if(ctratperso.getIndemniteRepresent()==null)
                this.indemniteRepresentation = Math.ceil(0);
            else
                this.indemniteRepresentation = Math.ceil(ctratperso.getIndemniteRepresent());


//		if(ctratperso.getIndemniteResp()==null)
//			this.indemniteResponsabilte = Math.ceil(0);
//		else
//			this.indemniteResponsabilte = Math.ceil(ctratperso.getIndemniteResp());

            autreNonImposable=0d;
            if (listIndemniteNonImp != null && !listIndemniteNonImp.isEmpty()) {
                for(PrimePersonnel primeImpos : listIndemniteNonImp){
                                */
/*if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
                                {
                                    autreNonImposable=autreNonImposable+(primeImpos.getMontant()*primeImpos.getPrime().getTaux()*primeImpos.getValeur())*tempeffect.getJourspresence()/30;
                                }else{*//*

                    if(tempeffect==null){
                        if(primeImpos.getPrime().getMtExedent()!=null)
                            autreNonImposable=autreNonImposable+primeImpos.getPrime().getMtExedent();
                        else
                            autreNonImposable=autreNonImposable+primeImpos.getMontant();
                    }else{

                        if(primeImpos.getPrime().getMtExedent()!=null)
                            autreNonImposable=autreNonImposable+primeImpos.getPrime().getMtExedent()*tempeffect.getJourspresence()/JOURS_OUVRABLES_MOIS;
                        else
                            autreNonImposable=autreNonImposable+(primeImpos.getMontant()*tempeffect.getJourspresence()/JOURS_OUVRABLES_MOIS);
                    }
                    //}
                }
            }

            this.brutNonImposable=indemniteRepresentation+indemniteTransport+autreNonImposable;
            //this.indemniteRepresentation = Math.ceil(calculerIndemniterRepresentation());
            this.basecnps=brutImposable + indemniteRepresentation+autreNonImposable - autreIndemImposable;
            //BigDecimal cnpsBrut[]=calculerCNPS(basecnps,2);
            this.cnps = Math.ceil(calculCNPS(basecnps));//Math.ceil(cnpsBrut[2]);
            this.avceAcpte = Math.ceil(avanceEtAccompte);
            this.pretAlios = Math.ceil(pretALIOS);
            //this.indemniteTransport = Math.ceil(calculerIndemniteTransport());
            this.totalBrut = Math.ceil(brutImposable + indemniteRepresentation+ indemniteTransport+autreNonImposable);
            autrePrelevmentSociale=0d; BigDecimal patronalcmu=0d;BigDecimal salarialcmu=0d;
            if(listRetenueSociale != null && !listRetenueSociale.isEmpty()){

                for(PrimePersonnel sociale : listRetenueSociale){
                    if (sociale.getPrime().getPermanent()) {
                        if (sociale.getPrime().getLibelle().equals("CMU Patronal")) {
                            patronalcmu = sociale.getMontant();
                        } else if (sociale.getPrime().getLibelle().equals("CMU Salarial")) {
                            salarialcmu = sociale.getMontant();
                        } else {
                            autrePrelevmentSociale += sociale.getMontant();
                        }
                    }

                }
            }
            //this.CMU=autrePrelevmentSociale;
            this.CMUSalarial=salarialcmu;
            this.retenueSociiale=autrePrelevmentSociale +cnps;
            this.CMUPatronal=patronalcmu;
            autrePrelevmentMutuelle=0d;
            if (listMutuelle != null && !listMutuelle.isEmpty()) {

                for(PrimePersonnel mutuell : listMutuelle){
                    autrePrelevmentMutuelle=autrePrelevmentMutuelle+mutuell.getMontant();
                }
            }

            this.totalRetenue = Math.ceil(totalRetenueFiscale + avceAcpte + pretAlios+autrePrelevmentMutuelle +retenueSociiale);
            regularisation=0d;
            if(listGains != null && !listGains.isEmpty()){
                for(PrimePersonnel primeGains : listGains){
                    regularisation=regularisation+primeGains.getMontant();
                }
            }
            this.netPayer = Math.ceil((brutImposable + indemniteRepresentation + indemniteTransport+autreNonImposable)+ regularisation -autreIndemImposable- totalRetenue);
            if(tempeffect!=null){
                this.netRegulPayer=netCible - netPayer;
                netPayer=netCible -pretAlios-avceAcpte;
            }
            this.is = its;
            this.ta = Math.ceil(brutImposable * 0.4 / 100);
            this.fpc =Math.ceil(brutImposable * 0.6 / 100);
            this.fpcregul =Math.ceil(brutImposable * 0.6 / 100);
            this.prestationFamiliale = Math.ceil(calcalerPrestationFamilial());
            this.accidentTravail = Math.ceil(calculerAccidentTravail());
            this.retraite = Math.ceil((brutImposable + indemniteRepresentation+autreNonImposable) * 7.7 / 100);
            this.totalPatronal =Math.ceil(is + ta + fpc+fpcregul +prestationFamiliale + accidentTravail + retraite+ CMUPatronal); ///Math.ceil( ta + fpc + prestationFamiliale + accidentTravail + retraite);
            this.totalMasseSalariale = Math.ceil(brutImposable + indemniteRepresentation+ indemniteTransport +autreNonImposable+regularisation+ totalPatronal);
            this.tempspresence= countnbreJrdu(ctratperso.getPersonnel().getDateRetourcge(), plconge.getDatefin(), ctratperso);
            this.moisdepresence= ProvisionConge.calculerTempsPresence(ctratperso.getPersonnel().getDateRetourcge(), plconge.getDatefin());
        }

        if (Boolean.FALSE.equals(ctratperso.getPersonnel().getCarec())){
            ///// traitement consultant et stagiaire   //////////
            nombrePart = 0F;
            anciennete = 0;
            autreImposable=0d;
            if(listIndemniteBrut != null && !listIndemniteBrut.isEmpty()){
                for(PrimePersonnel primeImpos : listIndemniteBrut){
                    if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
                    {
                        autreImposable=autreImposable+primeImpos.getValeur()*(primeImpos.getMontant()+(primeImpos.getMontant()*primeImpos.getPrime().getTaux()/100));
                    }else{
                        if(primeImpos.getPrime().getMtExedent()!=null)
                            autreImposable=autreImposable+primeImpos.getMontant()-primeImpos.getPrime().getMtExedent();
                        else
                            autreImposable=autreImposable+primeImpos.getMontant();
                    }
                }
            }

            autreNonImposable=0d;
            if(listIndemniteNonImp!= null && !listIndemniteNonImp.isEmpty()){
                for(PrimePersonnel primeImpos : listIndemniteNonImp){
                            */
/*if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
                            {
                                autreNonImposable=autreNonImposable+(primeImpos.getMontant()*primeImpos.getPrime().getTaux()*primeImpos.getValeur())*tempeffect.getJourspresence()/30;
                            }else{*//*

                    if(tempeffect==null){
                        if(primeImpos.getPrime().getMtExedent()!=null)
                            autreNonImposable=autreNonImposable+primeImpos.getPrime().getMtExedent();
                        else
                            autreNonImposable=autreNonImposable+primeImpos.getMontant();
                    }else{

                        if(primeImpos.getPrime().getMtExedent()!=null)
                            autreNonImposable=autreNonImposable+primeImpos.getPrime().getMtExedent()*tempeffect.getJourspresence()/JOURS_OUVRABLES_MOIS;
                        else
                            autreNonImposable=autreNonImposable+(primeImpos.getMontant()*tempeffect.getJourspresence()/JOURS_OUVRABLES_MOIS);
                    }
                    //}
                }
            }
            regularisation=0d;
            if(listGains!= null && !listGains.isEmpty()){
                for(PrimePersonnel primeGains : listGains){

                    regularisation=regularisation+primeGains.getMontant();

                    //}
                }
            }
            //if(ctratperso.getPersonnel().getCarec()==false)
            //{	 this.its = Math.ceil(0);
            this.cn =  Math.ceil(0);
            this.igr = Math.ceil(0);
            this.totalRetenueFiscale = Math.ceil(0);
            this.brutImposable = Math.ceil(autreImposable);
            this.brutNonImposable = Math.ceil(autreNonImposable);
            this.basecnps=Math.ceil(0);
            this.is = Math.ceil(0);
            this.ta = Math.ceil(0);
            this.fpc = Math.ceil(0);
            this.prestationFamiliale = Math.ceil(0);
            this.accidentTravail = Math.ceil(0);
            this.retraite = Math.ceil(0);
            this.cnps = Math.ceil(0);
            this.retraite = Math.ceil(0);
            this.plafondFamiliale = Math.ceil(0);

            avceAcpte=Math.ceil(avanceEtAccompte); pretAlios=Math.ceil(pretALIOS);
            autrePrelevment=0d;
            if(listMutuelle!= null && !listMutuelle.isEmpty()){
                for(PrimePersonnel mutuell : listMutuelle){

                    autrePrelevment=autrePrelevment+mutuell.getMontant();

                    //}
                }
            }
            this.totalRetenue = Math.ceil(totalRetenueFiscale + cnps + avceAcpte + pretAlios +autrePrelevment);
            this.netPayer = Math.ceil(-totalRetenue+ctratperso.getNetAPayer());
            //                 if(tempeffect!=null){
            //                   this.netRegulPayer=netCible - netPayer;
            //                  netPayer=netCible;
            //                }
            //this.is = Math.ceil(brutImposable * 1.2 / 100);
            //this.ta = Math.ceil(brutImposable * 0.4 / 100);
            //this.fpc = Math.ceil(brutImposable * 1.2 / 100);
            //this.prestationFamiliale = Math.ceil(calcalerPrestationFamilial());
            //this.accidentTravail = Math.ceil(calculerAccidentTravail());
            //this.retraite = Math.ceil((brutImposable + indemniteRepresentation+indemniteResponsabilte+autreNonImposable) * 7.7 / 100);
            this.totalPatronal = Math.ceil(is + ta + fpc + prestationFamiliale + accidentTravail + retraite);
            this.totalMasseSalariale = Math.ceil(brutImposable + brutNonImposable+regularisation+ totalPatronal)+ctratperso.getNetAPayer();
            this.tempspresence= countnbreJrdu(ctratperso.getPersonnel().getDateRetourcge(), plconge.getDatefin(), ctratperso);
            this.moisdepresence= ProvisionConge.calculerTempsPresence(ctratperso.getPersonnel().getDateRetourcge(), plconge.getDatefin());
            //}

            //}
        }
    }

    public BigDecimal calculerAccidentTravail(){
        BigDecimal pf = brutImposable + autreNonImposable;
        if(pf == 70000)
            pf = 70000 * 4.0 / 100;
        else if(pf > 70000)
            pf = brutImposable * 4 / 100;
        else
            pf = 0.0;
        return pf;
    }

    public BigDecimal calcalerPrestationFamilial(){
        BigDecimal pf = brutImposable + autreNonImposable;
        if(pf == 70000)
            pf = 70000 * 5.75 / 100;
        else if(pf > 70000)
            pf = brutImposable * 5.75 / 100;
        else
            pf = 0.0;
        return pf;
    }

    public BigDecimal calculerIndemniteTransport(){
        BigDecimal it = brutImposable * 10 / 100 - 25000.0;
        if(it > 0)
            it = 25000.0;
        else
            it = 0.0;
        return it;
    }

    public BigDecimal calculerIndemniterRepresentation(){
        BigDecimal ir = brutImposable * 10 / 100 - 25000.0;
        if(ir > 0)
            ir = brutImposable * 10 / 100 - 25000.0;
        else
            ir = 0.0;
        return ir;
    }

//	public BigDecimal calculerCN(){
//		BigDecimal cn;
//		if(brutImposable > 250000.0)
//			cn = (brutImposable - 250000.0) * 8 / 100 + 4700;
//		else if(brutImposable > 162500.0)
//			cn = (brutImposable - 162500.0) * 4 / 100 + 1200;
//		else if(brutImposable > 62500.0)
//			cn = brutImposable * 1.2 / 100 - 750;
//		else
//			cn = 0.0;
//		return cn;
//	}

//	public BigDecimal calculerIGR(){
//		BigDecimal igr = ((brutImposable * 80 / 100 - its - cn) / nombrePart) * 85 / 100;
//		if(igr > 842167.0)
//			igr = ((brutImposable * 80 / 100 - its - cn) / nombrePart) * 85 / 100 * nombrePart * 60 / 160 - 98633.0 * nombrePart;
//		else if(igr > 389084.0)
//			igr = ((brutImposable * 80 / 100 - its - cn) / nombrePart) * 85 / 100 * nombrePart * 45 / 145 - 44181.0 * nombrePart;
//		else if(igr > 220334.0)
//			igr = ((brutImposable * 80 / 100 - its - cn) / nombrePart) * 85 / 100 * nombrePart * 35 / 135 - 24306.0 * nombrePart;
//		else if(igr > 126584.0)
//			igr = ((brutImposable * 80 / 100 - its - cn) / nombrePart) * 85 / 100 * nombrePart * 25 / 125 - 11250.0 * nombrePart;
//		else if(igr > 81584.0)
//			igr = ((brutImposable * 80 / 100 - its - cn) / nombrePart) * 85 / 100 * nombrePart * 20 / 120 - 7031.0 * nombrePart;
//		else if(igr > 45584.0)
//			igr = ((brutImposable * 80 / 100 - its - cn) / nombrePart) * 85 / 100 * nombrePart * 15 / 115 - 4076.0 * nombrePart;
//		else if(igr > 25000.0)
//			igr = ((brutImposable * 80 / 100 - its - cn) / nombrePart) * 85 / 100 * nombrePart * 15 / 110 - 2273.0 * nombrePart;
//		else
//			igr = 0.0;
//		return igr;
//	}

    public BigDecimal calculCNPS(BigDecimal basecnps){
        BigDecimal cnps = (basecnps ); //3000000
        if(cnps < 3375000.0)
            cnps = (basecnps ) * 6.3 / 100;
        else
            cnps = 3375000.0 * 6.3 / 100;
        return cnps;
    }

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

    public Float getNombrePart() {
        return nombrePart;
    }

    public void setNombrePart(Float nombrePart) {
        this.nombrePart = nombrePart;
    }

    public int getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(int anciennete) {
        this.anciennete = anciennete;
    }

    public BigDecimal getSalaireBase() {
        return salaireBase;
    }

    public void setSalaireBase(BigDecimal salaireBase) {
        this.salaireBase = salaireBase;
    }

    public BigDecimal getSursalaire() {
        return sursalaire;
    }

    public Integer getTempspresence() {
        return tempspresence;
    }

    public Integer getMoisdepresence() {
        return moisdepresence;
    }

    public void setMoisdepresence(Integer moisdepresence) {
        this.moisdepresence = moisdepresence;
    }

    public void setTempspresence(Integer tempspresence) {
        this.tempspresence = tempspresence;
    }

    public void setSursalaire(BigDecimal sursalaire) {
        this.sursalaire = sursalaire;
    }

    public BigDecimal getPrimeAnciennete() {
        return primeAnciennete;
    }

    public void setPrimeAnciennete(BigDecimal primeAnciennete) {
        this.primeAnciennete = primeAnciennete;
    }

    public BigDecimal getIndemniteLogement() {
        return indemniteLogement;
    }

    public void setIndemniteLogement(BigDecimal indemniteLogement) {
        this.indemniteLogement = indemniteLogement;
    }

    public BigDecimal getBrutImposable() {
        return brutImposable;
    }

    public void setBrutImposable(BigDecimal brutImposable) {
        this.brutImposable = brutImposable;
    }

    public BigDecimal getIts() {
        return its;
    }

    public void setIts(BigDecimal its) {
        this.its = its;
    }

    public BigDecimal getCn() {
        return cn;
    }

    public void setCn(BigDecimal cn) {
        this.cn = cn;
    }

    public BigDecimal getIgr() {
        return igr;
    }

    public void setIgr(BigDecimal igr) {
        this.igr = igr;
    }

    public BigDecimal getTotalRetenueFiscale() {
        return totalRetenueFiscale;
    }

    public void setTotalRetenueFiscale(BigDecimal totalRetenueFiscale) {
        this.totalRetenueFiscale = totalRetenueFiscale;
    }

    public BigDecimal getCnps() {
        return cnps;
    }

    public void setCnps(BigDecimal cnps) {
        this.cnps = cnps;
    }

    public BigDecimal getAvceAcpte() {
        return avceAcpte;
    }

    public void setAvceAcpte(BigDecimal avceAcpte) {
        this.avceAcpte = avceAcpte;
    }

    public BigDecimal getPretAlios() {
        return pretAlios;
    }

    public void setPretAlios(BigDecimal pretAlios) {
        this.pretAlios = pretAlios;
    }

    public BigDecimal getCarec() {
        return carec;
    }

    public void setCarec(BigDecimal carec) {
        this.carec = carec;
    }

    public BigDecimal getTotalRetenue() {
        return totalRetenue;
    }

    public void setTotalRetenue(BigDecimal totalRetenue) {
        this.totalRetenue = totalRetenue;
    }

    public BigDecimal getIndemniteRepresentation() {
        return indemniteRepresentation;
    }

    public void setIndemniteRepresentation(BigDecimal indemniteRepresentation) {
        this.indemniteRepresentation = indemniteRepresentation;
    }

    public BigDecimal getIndemniteTransport() {
        return indemniteTransport;
    }

    public void setIndemniteTransport(BigDecimal indemniteTransport) {
        this.indemniteTransport = indemniteTransport;
    }

    public BigDecimal getNetPayer() {
        return netPayer;
    }

    public void setNetPayer(BigDecimal netPayer) {
        this.netPayer = netPayer;
    }

    public BigDecimal getNetRegulPayer() {
        return netRegulPayer;
    }

    public void setNetRegulPayer(BigDecimal netRegulPayer) {
        this.netRegulPayer = netRegulPayer;
    }

    public String getMtRegulnetPayer() {
        return mtRegulnetPayer= Utils.formattingAmount(netRegulPayer);
    }

    public void setMtRegulnetPayer(String mtRegulnetPayer) {
        this.mtRegulnetPayer = mtRegulnetPayer;
    }

    public BigDecimal getTotalBrut() {
        return totalBrut;
    }

    public void setTotalBrut(BigDecimal totalBrut) {
        this.totalBrut = totalBrut;
    }

    public BigDecimal getIs() {
        return is;
    }

    public void setIs(BigDecimal is) {
        this.is = is;
    }

    public BigDecimal getTa() {
        return ta;
    }

    public void setTa(BigDecimal ta) {
        this.ta = ta;
    }

    public BigDecimal getFpc() {
        return fpc;
    }

    public void setFpc(BigDecimal fpc) {
        this.fpc = fpc;
    }

    public BigDecimal getPrestationFamiliale() {
        return prestationFamiliale;
    }

    public void setPrestationFamiliale(BigDecimal prestationFamiliale) {
        this.prestationFamiliale = prestationFamiliale;
    }

    public BigDecimal getAccidentTravail() {
        return accidentTravail;
    }

    public void setAccidentTravail(BigDecimal accidentTravail) {
        this.accidentTravail = accidentTravail;
    }

    public BigDecimal getRetraite() {
        return retraite;
    }

    public void setRetraite(BigDecimal retraite) {
        this.retraite = retraite;
    }

    public BigDecimal getTotalPatronal() {
        return totalPatronal;
    }

    public void setTotalPatronal(BigDecimal totalPatronal) {
        this.totalPatronal = totalPatronal;
    }

    public BigDecimal getTotalMasseSalariale() {
        return totalMasseSalariale;
    }

    public void setTotalMasseSalariale(BigDecimal totalMasseSalariale) {
        this.totalMasseSalariale = totalMasseSalariale;
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

    public BulletinPaie getBullpaie() {
        return bullpaie;
    }

    public void setBullpaie(BulletinPaie bullpaie) {
        this.bullpaie = bullpaie;
    }



    public String getMtsalaireBase() {

        return mtsalaireBase= Utils.formattingAmount(salaireBase);
    }

    public void setMtsalaireBase(String mtsalaireBase) {
        this.mtsalaireBase = mtsalaireBase;
    }

    public String getMtsursalaire() {

        return mtsursalaire= Utils.formattingAmount(sursalaire);
    }

    public void setMtsursalaire(String mtsursalaire) {
        this.mtsursalaire = mtsursalaire;
    }

    public String getMtprimeAnciennete() {

        return mtprimeAnciennete= Utils.formattingAmount(primeAnciennete);
    }

    public void setMtprimeAnciennete(String mtprimeAnciennete) {
        this.mtprimeAnciennete = mtprimeAnciennete;
    }

    public String getMtindemniteLogement() {

        return mtindemniteLogement= Utils.formattingAmount(indemniteLogement);
    }

    public void setMtindemniteLogement(String mtindemniteLogement) {
        this.mtindemniteLogement = mtindemniteLogement;
    }

    public String getMtbrutImposable() {

        return mtbrutImposable= Utils.formattingAmount(brutImposable);
    }

    public void setMtbrutImposable(String mtbrutImposable) {
        this.mtbrutImposable = mtbrutImposable;
    }

    public String getMtits() {

        return mtits= Utils.formattingAmount(its);
    }

    public void setMtits(String mtits) {
        this.mtits = mtits;
    }

    public String getMtcn() {

        return mtcn= Utils.formattingAmount(cn);
    }

    public void setMtcn(String mtcn) {
        this.mtcn = mtcn;
    }

    public String getMtigr() {

        return mtigr= Utils.formattingAmount(igr);
    }

    public void setMtigr(String mtigr) {
        this.mtigr = mtigr;
    }

    public String getMttotalRetenueFiscale() {

        return mttotalRetenueFiscale= Utils.formattingAmount(totalRetenueFiscale);
    }

    public void setMttotalRetenueFiscale(String mttotalRetenueFiscale) {
        this.mttotalRetenueFiscale = mttotalRetenueFiscale;
    }

    public String getMtcnps() {

        return mtcnps= Utils.formattingAmount(cnps);
    }

    public void setMtcnps(String mtcnps) {
        this.mtcnps = mtcnps;
    }

    public String getMtavceAcpte() {

        return mtavceAcpte= Utils.formattingAmount(avceAcpte);
    }

    public void setMtavceAcpte(String mtavceAcpte) {
        this.mtavceAcpte = mtavceAcpte;
    }

    public String getMtpretAlios() {

        return mtpretAlios= Utils.formattingAmount(pretAlios);
    }

    public void setMtpretAlios(String mtpretAlios) {
        this.mtpretAlios = mtpretAlios;
    }

    public String getMtcarec() {

        return mtcarec= Utils.formattingAmount(carec);
    }

    public void setMtcarec(String mtcarec) {
        this.mtcarec = mtcarec;
    }

    public String getMttotalRetenue() {

        return mttotalRetenue= Utils.formattingAmount(totalRetenue);
    }

    public void setMttotalRetenue(String mttotalRetenue) {
        this.mttotalRetenue = mttotalRetenue;
    }

    public String getMtindemniteRepresentation() {

        return mtindemniteRepresentation= Utils.formattingAmount(indemniteRepresentation);
    }

    public void setMtindemniteRepresentation(String mtindemniteRepresentation) {
        this.mtindemniteRepresentation = mtindemniteRepresentation;
    }

    public String getMtindemniteTransport() {

        return mtindemniteTransport= Utils.formattingAmount(indemniteTransport);
    }

    public void setMtindemniteTransport(String mtindemniteTransport) {
        this.mtindemniteTransport = mtindemniteTransport;

    }

    public String getMtnetPayer() {

        return mtnetPayer= Utils.formattingAmount(netPayer);
    }

    public void setMtnetPayer(String mtnetPayer) {
        this.mtnetPayer = mtnetPayer;
    }

    public String getMttotalBrut() {

        return mttotalBrut= Utils.formattingAmount(totalBrut);
    }

    public void setMttotalBrut(String mttotalBrut) {
        this.mttotalBrut = mttotalBrut;
    }

    public String getMtis() {

        return mtis= Utils.formattingAmount(is);
    }

    public void setMtis(String mtis) {
        this.mtis = mtis;
    }

    public String getMtta() {

        return mtta= Utils.formattingAmount(ta);
    }

    public void setMtta(String mtta) {
        this.mtta = mtta;
    }

    public String getMtfpc() {

        return mtfpc= Utils.formattingAmount(fpc);
    }

    public void setMtfpc(String mtfpc) {

        this.mtfpc = mtfpc;
    }

    public String getMtprestationFamiliale() {

        return mtprestationFamiliale= Utils.formattingAmount(prestationFamiliale);
    }

    public void setMtprestationFamiliale(String mtprestationFamiliale) {
        this.mtprestationFamiliale = mtprestationFamiliale;
    }

    public String getMtaccidentTravail() {

        return mtaccidentTravail= Utils.formattingAmount(accidentTravail);
    }

    public void setMtaccidentTravail(String mtaccidentTravail) {
        this.mtaccidentTravail = mtaccidentTravail;
    }

    public String getMtretraite() {
        return mtretraite= Utils.formattingAmount(retraite);
    }

    public void setMtretraite(String mtretraite) {
        this.mtretraite = mtretraite;
    }

    public String getMttotalPatronal() {

        return mttotalPatronal= Utils.formattingAmount(totalPatronal);
    }

    public void setMttotalPatronal(String mttotalPatronal) {
        this.mttotalPatronal = mttotalPatronal;
    }

    public String getMttotalMasseSalariale() {
        return mttotalMasseSalariale= Utils.formattingAmount(totalMasseSalariale);
    }

    public void setMttotalMasseSalariale(String mttotalMasseSalariale) {
        this.mttotalMasseSalariale = mttotalMasseSalariale;
    }

    public BigDecimal getBasecnps() {
        return basecnps;
    }

    public void setBasecnps(BigDecimal basecnps) {
        this.basecnps = basecnps;
    }

    public String getMtbasecnps() {
        return mtbasecnps= Utils.formattingAmount(basecnps);
    }

    public void setMtbasecnps(String mtbasecnps) {
        this.mtbasecnps = mtbasecnps;
    }

    public BigDecimal getIndemniteTransportImp() {
        return indemniteTransportImp;
    }

    public void setIndemniteTransportImp(BigDecimal indemniteTransportImp) {
        this.indemniteTransportImp = indemniteTransportImp;
    }

    public String getMtindemniteTransportImp() {
        return mtindemniteTransportImp=Utils.formattingAmount(indemniteTransportImp);
    }

    public void setMtindemniteTransportImp(String mtindemniteTransportImp) {
        this.mtindemniteTransportImp = mtindemniteTransportImp;
    }

    public BigDecimal getIndemniteResponsabilte() {
        return indemniteResponsabilte;
    }

    public void setIndemniteResponsabilte(BigDecimal indemniteResponsabilte) {
        this.indemniteResponsabilte = indemniteResponsabilte;
    }

    public String getMtindemniteResponsabilte() {
        return mtindemniteResponsabilte=Utils.formattingAmount(indemniteResponsabilte);
    }

    public void setMtindemniteResponsabilte(String mtindemniteResponsabilte) {
        this.mtindemniteResponsabilte = mtindemniteResponsabilte;
    }

    public BigDecimal getBrutNonImposable() {
        return brutNonImposable;
    }

    public void setBrutNonImposable(BigDecimal brutNonImposable) {
        this.brutNonImposable = brutNonImposable;
    }

    public String getMtbrutNonImposable() {
        return mtbrutNonImposable=Utils.formattingAmount(brutNonImposable);
    }

    public void setMtbrutNonImposable(String mtbrutNonImposable) {
        this.mtbrutNonImposable = mtbrutNonImposable;
    }

    public List<PrimePersonnel> getListIndemniteBrut() {
        return listIndemniteBrut;
    }

    public void setListIndemniteBrut(List<PrimePersonnel> listIndemniteBrut) {
        this.listIndemniteBrut = listIndemniteBrut;
    }

    public List<PrimePersonnel> getListIndemniteNonImp() {
        return listIndemniteNonImp;
    }

    public void setListIndemniteNonImp(List<PrimePersonnel> listIndemniteNonImp) {
        this.listIndemniteNonImp = listIndemniteNonImp;
    }

    public List<PrimePersonnel> getListIndemBrutNonImp() {
        return listIndemBrutNonImp;
    }

    public void setListIndemBrutNonImp(List<PrimePersonnel> listIndemBrutNonImp) {
        this.listIndemBrutNonImp = listIndemBrutNonImp;
    }

    public BigDecimal getAutreImposable() {
        return autreImposable;
    }

    public void setAutreImposable(BigDecimal autreImposable) {
        this.autreImposable = autreImposable;
    }

    public String getMtautreImposable() {
        return mtautreImposable=Utils.formattingAmount(autreImposable);
    }

    public void setMtautreImposable(String mtautreImposable) {
        this.mtautreImposable = mtautreImposable;
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

    public BigDecimal getAutreIndemImposable() {
        return autreIndemImposable;
    }

    public void setAutreIndemImposable(BigDecimal autreIndemImposable) {
        this.autreIndemImposable = autreIndemImposable;
    }

    public String getMtautreIndemImposable() {
        return mtautreIndemImposable=Utils.formattingAmount(autreIndemImposable);
    }

    public void setMtautreIndemImposable(String mtautreIndemImposable) {
        this.mtautreIndemImposable = mtautreIndemImposable;
    }

    public BigDecimal getPlafondFamiliale() {
        return plafondFamiliale;
    }

    public void setPlafondFamiliale(BigDecimal plafondFamiliale) {
        this.plafondFamiliale = plafondFamiliale;
    }

    public String getMtplafondFamiliale() {
        return mtplafondFamiliale;
    }

    public void setMtplafondFamiliale(String mtplafondFamiliale) {
        this.mtplafondFamiliale = mtplafondFamiliale;
    }

    public List<PrimePersonnel> getListRetenueMutuellt() {
        return listRetenueMutuellt;
    }

    public void setListRetenueMutuellt(List<PrimePersonnel> listRetenueMutuellt) {
        this.listRetenueMutuellt = listRetenueMutuellt;
    }

    public List<PrimePersonnel> getListGainsNet() {
        return listGainsNet;
    }

    public void setListGainsNet(List<PrimePersonnel> listGainsNet) {
        this.listGainsNet = listGainsNet;
    }

    public BigDecimal getAutrePrelevment() {
        return autrePrelevment;
    }

    public void setAutrePrelevment(BigDecimal autrePrelevment) {
        this.autrePrelevment = autrePrelevment;
    }

    public String getMtautrePrelevment() {
        return mtautrePrelevment=Utils.formattingAmount(autrePrelevment);
    }

    public void setMtautrePrelevment(String mtautrePrelevment) {
        this.mtautrePrelevment = mtautrePrelevment;
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

    public List<PrimePersonnel> getListRetenueSociale() {
        return listRetenueSociale;
    }

    public void setListRetenueSociale(List<PrimePersonnel> listRetenueSociale) {
        this.listRetenueSociale = listRetenueSociale;
    }

    public BigDecimal getCMU() {
        return CMU;
    }

    public void setCMU(BigDecimal CMU) {
        this.CMU = CMU;
    }

    public String getMtCMU() {
        return mtCMU;
    }

    public void setMtCMU(String mtCMU) {
        this.mtCMU = mtCMU;
    }

    public BigDecimal getAutrePrelevmentSociale() {
        return autrePrelevmentSociale;
    }

    public void setAutrePrelevmentSociale(BigDecimal autrePrelevmentSociale) {
        this.autrePrelevmentSociale = autrePrelevmentSociale;
    }

    public String getMtautrePrelevmentSociale() {
        return mtautrePrelevmentSociale;
    }

    public void setMtautrePrelevmentSociale(String mtautrePrelevmentSociale) {
        this.mtautrePrelevmentSociale = mtautrePrelevmentSociale;
    }

    public BigDecimal getRetenueSociale() {
        return RetenueSociale;
    }

    public void setRetenueSociale(BigDecimal retenueSociale) {
        RetenueSociale = retenueSociale;
    }

    public String getMtRetenueSocial() {
        return mtRetenueSocial;
    }

    public void setMtRetenueSocial(String mtRetenueSocial) {
        this.mtRetenueSocial = mtRetenueSocial;
    }

    public BigDecimal getTotalRetenueSociale() {
        return totalRetenueSociale;
    }

    public void setTotalRetenueSociale(BigDecimal totalRetenueSociale) {
        this.totalRetenueSociale = totalRetenueSociale;
    }

    public String getMttotalRetenueSocial() {
        return mttotalRetenueSocial;
    }

    public void setMttotalRetenueSocial(String mttotalRetenueSocial) {
        this.mttotalRetenueSocial = mttotalRetenueSocial;
    }

    public BigDecimal getRetenueSociiale() {
        return retenueSociiale;
    }



    public void setRetenueSociiale(BigDecimal retenueSociiale) {
        this.retenueSociiale = retenueSociiale;
    }

    public String getMtretenueSociiale() {
        return mtretenueSociiale;
    }

    public void setMtretenueSociiale(String mtretenueSociiale) {
        this.mtretenueSociiale = mtretenueSociiale;
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

    public BigDecimal getFpcregul() {
        return fpcregul;
    }

    public void setFpcregul(BigDecimal fpcregul) {
        this.fpcregul = fpcregul;
    }

    public String getMtfpcregul() {
        return mtfpcregul=Utils.formattingAmount(fpcregul);
    }

    public void setMtfpcregul(String mtfpcregul) {
        this.mtfpcregul = mtfpcregul;
    }

    public String getMtautrePrelevmentMutuelle() {
        return mtautrePrelevmentMutuelle;
    }

    public void setMtautrePrelevmentMutuelle(String mtautrePrelevmentMutuelle) {
        this.mtautrePrelevmentMutuelle = mtautrePrelevmentMutuelle;
    }

    public BigDecimal getAutrePrelevmentMutuelle() {
        return autrePrelevmentMutuelle;
    }

    public void setAutrePrelevmentMutuelle(BigDecimal autrePrelevmentMutuelle) {
        this.autrePrelevmentMutuelle = autrePrelevmentMutuelle;
    }

    @Override
    public String toString() {
        return "LivreDePaie{" +
                "rubriqueRepository=" + rubriqueRepository +
                ", primePersonnelRepository=" + primePersonnelRepository +
                ", matricule='" + matricule + '\'' +
                ", nomPrenom='" + nomPrenom + '\'' +
                ", nombrePart=" + nombrePart +
                ", anciennete=" + anciennete +
                ", salaireBase=" + salaireBase +
                ", mtsalaireBase='" + mtsalaireBase + '\'' +
                ", autreImposable=" + autreImposable +
                ", mtautreImposable='" + mtautreImposable + '\'' +
                ", autreIndemImposable=" + autreIndemImposable +
                ", mtautreIndemImposable='" + mtautreIndemImposable + '\'' +
                ", plafondFamiliale=" + plafondFamiliale +
                ", mtplafondFamiliale='" + mtplafondFamiliale + '\'' +
                ", autreNonImposable=" + autreNonImposable +
                ", mtautreNonImposable='" + mtautreNonImposable + '\'' +
                ", sursalaire=" + sursalaire +
                ", mtsursalaire='" + mtsursalaire + '\'' +
                ", primeAnciennete=" + primeAnciennete +
                ", moisdepresence=" + moisdepresence +
                ", tempspresence=" + tempspresence +
                ", mtprimeAnciennete='" + mtprimeAnciennete + '\'' +
                ", indemniteLogement=" + indemniteLogement +
                ", mtindemniteLogement='" + mtindemniteLogement + '\'' +
                ", brutImposable=" + brutImposable +
                ", mtbrutImposable='" + mtbrutImposable + '\'' +
                ", autrePrelevmentSociale=" + autrePrelevmentSociale +
                ", mtautrePrelevmentSociale='" + mtautrePrelevmentSociale + '\'' +
                ", autrePrelevment=" + autrePrelevment +
                ", mtautrePrelevment='" + mtautrePrelevment + '\'' +
                ", autrePrelevmentMutuelle=" + autrePrelevmentMutuelle +
                ", mtautrePrelevmentMutuelle='" + mtautrePrelevmentMutuelle + '\'' +
                ", regularisation=" + regularisation +
                ", mtregularisation='" + mtregularisation + '\'' +
                ", brutNonImposable=" + brutNonImposable +
                ", mtbrutNonImposable='" + mtbrutNonImposable + '\'' +
                ", its=" + its +
                ", mtits='" + mtits + '\'' +
                ", CMU=" + CMU +
                ", mtCMU='" + mtCMU + '\'' +
                ", CMUSalarial=" + CMUSalarial +
                ", mtCMUSalarial='" + mtCMUSalarial + '\'' +
                ", CMUPatronal=" + CMUPatronal +
                ", mtCMUPatronal='" + mtCMUPatronal + '\'' +
                ", cn=" + cn +
                ", mtcn='" + mtcn + '\'' +
                ", igr=" + igr +
                ", mtigr='" + mtigr + '\'' +
                ", totalRetenueFiscale=" + totalRetenueFiscale +
                ", mttotalRetenueFiscale='" + mttotalRetenueFiscale + '\'' +
                ", cnps=" + cnps +
                ", mtcnps='" + mtcnps + '\'' +
                ", basecnps=" + basecnps +
                ", mtbasecnps='" + mtbasecnps + '\'' +
                ", avceAcpte=" + avceAcpte +
                ", mtavceAcpte='" + mtavceAcpte + '\'' +
                ", pretAlios=" + pretAlios +
                ", mtpretAlios='" + mtpretAlios + '\'' +
                ", carec=" + carec +
                ", mtcarec='" + mtcarec + '\'' +
                ", totalRetenue=" + totalRetenue +
                ", mttotalRetenue='" + mttotalRetenue + '\'' +
                ", RetenueSociale=" + RetenueSociale +
                ", mtRetenueSocial='" + mtRetenueSocial + '\'' +
                ", totalRetenueSociale=" + totalRetenueSociale +
                ", mttotalRetenueSocial='" + mttotalRetenueSocial + '\'' +
                ", indemniteRepresentation=" + indemniteRepresentation +
                ", mtindemniteRepresentation='" + mtindemniteRepresentation + '\'' +
                ", indemniteTransport=" + indemniteTransport +
                ", mtindemniteTransport='" + mtindemniteTransport + '\'' +
                ", indemniteTransportImp=" + indemniteTransportImp +
                ", mtindemniteTransportImp='" + mtindemniteTransportImp + '\'' +
                ", indemniteResponsabilte=" + indemniteResponsabilte +
                ", mtindemniteResponsabilte='" + mtindemniteResponsabilte + '\'' +
                ", netPayer=" + netPayer +
                ", mtnetPayer='" + mtnetPayer + '\'' +
                ", totalBrut=" + totalBrut +
                ", mttotalBrut='" + mttotalBrut + '\'' +
                ", is=" + is +
                ", mtis='" + mtis + '\'' +
                ", ta=" + ta +
                ", mtta='" + mtta + '\'' +
                ", fpc=" + fpc +
                ", mtfpc='" + mtfpc + '\'' +
                ", fpcregul=" + fpcregul +
                ", mtfpcregul='" + mtfpcregul + '\'' +
                ", prestationFamiliale=" + prestationFamiliale +
                ", mtprestationFamiliale='" + mtprestationFamiliale + '\'' +
                ", accidentTravail=" + accidentTravail +
                ", mtaccidentTravail='" + mtaccidentTravail + '\'' +
                ", retraite=" + retraite +
                ", mtretraite='" + mtretraite + '\'' +
                ", totalPatronal=" + totalPatronal +
                ", mttotalPatronal='" + mttotalPatronal + '\'' +
                ", totalMasseSalariale=" + totalMasseSalariale +
                ", mttotalMasseSalariale='" + mttotalMasseSalariale + '\'' +
                ", retenueSociiale=" + retenueSociiale +
                ", mtretenueSociiale='" + mtretenueSociiale + '\'' +
                ", bullpaie=" + bullpaie +
                ", jourTravail=" + jourTravail +
                ", temptravail=" + temptravail +
                ", listIndemniteBrut=" + listIndemniteBrut +
                ", listIndemniteNonImp=" + listIndemniteNonImp +
                ", listRubrique=" + listRubrique +
                ", listIndemBrutNonImp=" + listIndemBrutNonImp +
                ", listRetenueMutuellt=" + listRetenueMutuellt +
                ", listRetenueSociale=" + listRetenueSociale +
                ", listGainsNet=" + listGainsNet +
                ", contratPersonnel=" + contratPersonnel +
                ", periodePaie=" + periodePaie +
                '}';
    }

    public int countnbreJrdu(Date dateRetourDernierConge, Date dateDepartConge, ContratPersonnel Contratp) {
        // TODO Auto-generated method stub

        int tps=ProvisionConge.calculerTempsPresence(dateRetourDernierConge,dateDepartConge);
        int rf=(int) (tps*2.2*1.25);
        BigDecimal[]ancienete= calculAnciennete(Contratp.getCategorie().getSalaireDeBase(),Contratp.getPersonnel().getDateArrivee());
        BigDecimal newancienete;
        if(Contratp.getAncienneteInitial()!=0) {
            newancienete=ancienete[1] +Contratp.getAncienneteInitial();
        }else{
            newancienete=ancienete[1];
        }
        BigDecimal anc=(int)newancienete ;

        int jourSuppAnc=0; int jourSuppDam = 0;int jourSuppMed = 0;

        if(anc>5 && anc<=10)  jourSuppAnc=1;
        if(anc>10 && anc<=15) jourSuppAnc=2;
        if(anc>15 && anc<=20) jourSuppAnc=3;
        if(anc>20 && anc<=25) jourSuppAnc=5;
        if(anc>25 && anc<=30) jourSuppAnc=7;
        if(anc>30) jourSuppAnc=8;

        BigDecimal age= DifferenceDate.valAge(new Date(), Contratp.getPersonnel().getDateNaissance());
        if(Contratp.getPersonnel().getSexe().equals("Feminin") && age<=21 && Contratp.getPersonnel().getNombrEnfant()>0){
            jourSuppDam=2*Contratp.getPersonnel().getNombrEnfant();
        }
        if(Contratp.getPersonnel().getSexe().equals("Feminin") && age>21 && Contratp.getPersonnel().getNombrEnfant()>0){

            if(Contratp.getPersonnel().getNombrEnfant()>=4)jourSuppDam=2*1;
            if(Contratp.getPersonnel().getNombrEnfant()>=5)jourSuppDam=2*2;
            if(Contratp.getPersonnel().getNombrEnfant()>=6)jourSuppDam=2*3;
            if(Contratp.getPersonnel().getNombrEnfant()>=7)jourSuppDam=2*4;
            if(Contratp.getPersonnel().getNombrEnfant()>=8)jourSuppDam=2*5;
            if(Contratp.getPersonnel().getNombrEnfant()>=9)jourSuppDam=2*6;
        }

        if(Contratp.getPersonnel().getSituationMedaille()==1 ){
            jourSuppMed=1;
        }
        int rfp=(int) (jourSuppAnc+jourSuppDam+jourSuppMed);
        return (int) rfp+rf;
    }




    public  BigDecimal[] calculAnciennete(BigDecimal salaireCategoriel, Date dateEntree){

        BigDecimal[] tab = new BigDecimal[5];

        BigDecimal anciennete = (BigDecimal) 0;


        BigDecimal age = DifferenceDate.valAge(new Date(), dateEntree);

        int partieEntiere = (int) age;
        int partieApresVirg = (int)((age - partieEntiere) * 12);


        if(age>=2)
            anciennete = (BigDecimal) (salaireCategoriel*partieEntiere/100);

        tab[0] = anciennete;


        tab[1] = (BigDecimal) partieEntiere;
        tab[2] = (BigDecimal) (partieApresVirg);



        return tab;
    }


}
*/
