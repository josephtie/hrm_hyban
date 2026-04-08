package com.nectux.mizan.hyban.paie.entity;

import com.nectux.mizan.hyban.paie.repository.PrimePersonnelRepository;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Rubrique;
import com.nectux.mizan.hyban.parametrages.repository.RubriqueRepository;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.utils.DifferenceDate;
import com.nectux.mizan.hyban.utils.ProvisionConge;
import com.nectux.mizan.hyban.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.nectux.mizan.hyban.utils.CalculRICF.getRICF;
import static com.nectux.mizan.hyban.utils.ITSCalculator.calculerITS;
import static java.lang.Math.ceil;


public class LivreDePaieSimulation {

	RubriqueRepository rubriqueRepository;
	@Autowired
	private	PrimePersonnelRepository primePersonnelRepository;


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

	private BigDecimal autrePrelevment;

	private BigDecimal primeAnciennete;

	private Integer moisdepresence;

	private Integer tempspresence;


	private BigDecimal fpcregul;
	@Transient
	private String mtfpcregul;

	@Transient
	private String mtprimeAnciennete;

	private BigDecimal indemniteLogement;
	@Transient
	private String mtindemniteLogement;

	private BigDecimal brutImposable;
	@Transient
	private String mtbrutImposable;



	@Transient
	private String mtautrePrelevment;

	private BigDecimal regularisation;
	@Transient
	private String mtregularisation;

	private BigDecimal brutNonImposable;
	@Transient
	private String mtbrutNonImposable;

	private BigDecimal its;
	@Transient
	private String mtits;

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

	private BigDecimal indemniteRepresentation;
	@Transient
	private String mtindemniteRepresentation;

	private BigDecimal indemniteTransport;
	@Transient
	private String mtindemniteTransport;

	private BigDecimal autrePrelevmentSociale;
	@Transient
	private String mtautrePrelevmentSociale;
	private BigDecimal indemniteTransportImp;
	@Transient
	private String mtindemniteTransportImp;

	private BigDecimal indemniteResponsabilte;
	@Transient
	private String mtindemniteResponsabilte;

	private BigDecimal retenueSociiale;
	@Transient
	private String mtretenueSociiale;
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

	private BulletinPaie bullpaie;

	private BigDecimal jourTravail;

	private BigDecimal temptravail;


	private BigDecimal CMU;
	@Transient
	private String mtCMU ;

	private BigDecimal CMUSalarial;
	@Transient
	private String mtCMUSalarial ;

	private BigDecimal CMUPatronal;
	@Transient
	private String mtCMUPatronal ;

	private BigDecimal RetenueSociale;
	@Transient
	private String mtRetenueSocial;

	private BigDecimal totalRetenueSociale;
	@Transient
	private String mttotalRetenueSocial;


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
	List<PrimePersonnel> listGainsNet = new ArrayList<PrimePersonnel>();


	@ManyToOne
	@JoinColumn(nullable=false)
	private ContratPersonnel contratPersonnel;

	@ManyToOne
	@JoinColumn(nullable=false)
	private PeriodePaie periodePaie;


	public LivreDePaieSimulation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LivreDePaieSimulation(String mat, String nomPre, Float nbrePart, int ancien, BigDecimal salBase, BigDecimal sursal, BigDecimal indemLog,BigDecimal indemTrp, BigDecimal avanceEtAccompte, BigDecimal pretALIOS, Boolean ctratperso, TempEffectif tempeffect, PeriodePaie plconge, List<PrimePersonnel> listIndemnite, List<PrimePersonnel> listIndemniteNonImp1, List<PrimePersonnel> listMutuelle, List<PrimePersonnel> listGains) {
		super();
		this.matricule = mat;
		this.nomPrenom = nomPre;
		this.nombrePart = nbrePart;
		this.anciennete = ancien;
		this.listIndemniteBrut=listIndemnite;
		//this.listRubrique=rubriqueRepository.findByActiveTrue();
		this.listIndemniteNonImp=listIndemniteNonImp1;
		this.listRetenueMutuellt=listMutuelle;
		this.listGainsNet=listGains;
		if(ctratperso==true){
			if(tempeffect==null){
		       this.salaireBase = salBase;
		        if(sursal==null)
		          this.sursalaire = BigDecimal.ZERO;
		        else
		    	 this.sursalaire =sursal;

		        if(ancien>= 2)
		  		    this.primeAnciennete = salaireBase.multiply(BigDecimal.valueOf(ancien)).divide(BigDecimal.valueOf(100) );
		    	else
		    	    this.primeAnciennete = BigDecimal.ZERO;

		     	if(indemLog==null)
		     		this.indemniteLogement = BigDecimal.ZERO;
		     	else
		    	 this.indemniteLogement = indemLog;


		     	 if(indemTrp==null)
		     	 {    this.indemniteTransportImp=BigDecimal.ZERO;
		            this.indemniteTransport=BigDecimal.ZERO;

		     	 }else{
                     BigDecimal plafond = new BigDecimal("30000");
                     BigDecimal transport = indemTrp;

                     if (transport.compareTo(plafond) > 0) {

                         this.indemniteTransportImp = transport.subtract(plafond);
                         this.indemniteTransport = plafond;

                     } else {

                         this.indemniteTransportImp = BigDecimal.ZERO;
                         this.indemniteTransport = transport;
                     }

				 }

//		   			if(ctratperso.getIndemniteRepresent()==null)
//			   		ctratperso.setIndemniteRepresent(0d);
		   			this.autreIndemImposable=BigDecimal.ZERO;

//		  			 if((indemniteTransport+ctratperso.getIndemniteRepresent())<(salaireBase+primeAnciennete+indemniteLogement+sursalaire+ctratperso.getIndemniteRepresent()+indemniteTransport)*10/100)
//			  			 autreIndemImposable=0d;
//			  		 	else
//						autreIndemImposable=Math.rint((indemniteTransport+ctratperso.getIndemniteRepresent()-((salaireBase+primeAnciennete+indemniteLogement+sursalaire+ctratperso.getIndemniteRepresent()+indemniteTransport)*10/100)));

		  				 autreImposable=BigDecimal.ZERO;
		     			if(!listIndemniteBrut.isEmpty() || listIndemniteBrut!=null){
		     				for(PrimePersonnel primeImpos : listIndemniteBrut){
		     					if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
		     					{
		     					autreImposable=autreImposable.add(primeImpos.getMontant());
//		     					  primeImpos.setMontant(primeImpos.getValeur()*(primeImpos.getMontant()+(primeImpos.getMontant()*primeImpos.getPrime().getTaux()/100)));
//									     if(primeImpos!=null)
//										primePersonnelRepository.save(primeImpos);
		     					}else{
                                       if(primeImpos.getPrime().getMtExedent()!=null)
		     					           autreImposable=autreImposable.add(primeImpos.getMontant()).subtract(primeImpos.getPrime().getMtExedent());
                                       else
                                    	   autreImposable=autreImposable.add(primeImpos.getMontant());
		     				}
		     			}
		     		}

		         this.jourTravail=BigDecimal.valueOf(30);
				this.temptravail=BigDecimal.valueOf(173.33);
		    }
//	 	   else
//		   {
//			       this.salaireBase = Math.rint(salBase*tempeffect.getJourspresence()/30);
//			        if(sursal==null)
//			          this.sursalaire = Math.rint(0d);
//			           else
//			         this.sursalaire = Math.rint(sursal*tempeffect.getJourspresence()/30);
//
//			        if(ancien>= 2)
//				      this.primeAnciennete = Math.rint((salaireBase) * ancien / 100);
//			        else
//				        this.primeAnciennete = Math.rint(0);
//
//		            if(indemLog==null)
//			           this.indemniteLogement = Math.rint(0);
//			        else
//		                this.indemniteLogement = Math.rint(indemLog*tempeffect.getJourspresence()/30);
//
//		           if(ctratperso.getIndemniteTransport()==null)
//		            {  this.indemniteTransportImp=Math.rint(0);
//		              this.indemniteTransport=Math.rint(0);
//
//		            }else{
//		        	  if(ctratperso.getIndemniteTransport() > 25000){
//		        	     this.indemniteTransportImp=(ctratperso.getIndemniteTransport()-25000)*tempeffect.getJourspresence()/30;
//				        this.indemniteTransport=25000d;
//				         }else{
//				    	  this.indemniteTransportImp=0d;
//					        this.indemniteTransport=ctratperso.getIndemniteTransport();
//				       }
//		            }
//
//		            this.autreIndemImposable=0d;
//			       if(ctratperso.getIndemniteRepresent()==null) ctratperso.setIndemniteRepresent(0d);
//
////			       if((indemniteTransport+ctratperso.getIndemniteRepresent())<(salaireBase+primeAnciennete+indemniteLogement+sursalaire+ctratperso.getIndemniteRepresent()+indemniteTransport)*10/100)
////				      autreIndemImposable=0d;
////					  else
////					   autreIndemImposable=Math.rint((indemniteTransport+ctratperso.getIndemniteRepresent()-((salaireBase+primeAnciennete+indemniteLogement+sursalaire+ctratperso.getIndemniteRepresent()+indemniteTransport)*10/100)));
////		     //*tempeffect.getJourspresence()/30
//		            autreImposable=0d;
//		 	        if(listIndemniteBrut.size()>0 || listIndemniteBrut!=null){
//  			       for(PrimePersonnel primeImpos : listIndemniteBrut){
//  				       if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
//  				      {
//  					     autreImposable=autreImposable+(primeImpos.getValeur()*(primeImpos.getMontant()+(primeImpos.getMontant()*primeImpos.getPrime().getTaux()/100))*tempeffect.getJourspresence()/30);
//  				      }else{
//
//  					       if(primeImpos.getPrime().getMtExedent()!=null)
//					        autreImposable=autreImposable+(primeImpos.getMontant()-primeImpos.getPrime().getMtExedent())*tempeffect.getJourspresence()/30;
//                             else
//                    	   autreImposable=autreImposable+primeImpos.getMontant()*tempeffect.getJourspresence()/30;
//
//  					//autreImposable=autreImposable+primeImpos.getMontant();
//  				      }
//  			       }
//  			     }
//		        this.jourTravail=tempeffect.getJourspresence();
//				 this.temptravail=tempeffect.getHeurspresence();
//		}

			this.brutImposable = salaireBase.add(sursalaire).add(primeAnciennete).add(indemniteLogement).add(indemniteTransportImp).add(autreIndemImposable).add(autreImposable);
			BigDecimal ricf = BigDecimal.valueOf(getRICF(nombrePart));

            BigDecimal itsBrut = calculerITS(brutImposable, true);
            this.its = itsBrut
                    .subtract(ricf.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP))
                    .max(BigDecimal.ZERO);
		//	this.its = Math.rint(brutImposable * 1.2 / 100);
		  //  this.cn =  Math.rint(calculerCN());
		    //this.igr = Math.rint(calculerIGR());
		    this.totalRetenueFiscale = its ; // cn + igr
	     	//if(ctratperso.getIndemniteRepresent()==null)
			this.indemniteRepresentation = BigDecimal.ZERO;
			//else
			//this.indemniteRepresentation = Math.rint(ctratperso.getIndemniteRepresent());


//		if(ctratperso.getIndemniteResp()==null)
//			this.indemniteResponsabilte = Math.rint(0);
//		else
//			this.indemniteResponsabilte = Math.rint(ctratperso.getIndemniteResp());

			autreNonImposable=BigDecimal.ZERO;
			    if(!listIndemniteNonImp.isEmpty() || listIndemniteNonImp!=null){
					for(PrimePersonnel primeImpos : listIndemniteNonImp){
						/*if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
						{
							autreNonImposable=autreNonImposable+(primeImpos.getMontant()*primeImpos.getPrime().getTaux()*primeImpos.getValeur())*tempeffect.getJourspresence()/30;
						}else{*/
						if(tempeffect==null){
							 if(primeImpos.getPrime().getMtExedent()!=null)
							   autreNonImposable=autreNonImposable.add(primeImpos.getPrime().getMtExedent());
							 else
								autreNonImposable=autreNonImposable.add(primeImpos.getMontant());
						}else{

							 if(primeImpos.getPrime().getMtExedent()!=null)
								   autreNonImposable=autreNonImposable.add(primeImpos.getPrime().getMtExedent()).multiply(BigDecimal.valueOf(tempeffect.getJourspresence())).divide(BigDecimal.valueOf(30));
								 else
								   autreNonImposable=autreNonImposable.add(primeImpos.getMontant().multiply(BigDecimal.valueOf(tempeffect.getJourspresence())).divide(BigDecimal.valueOf(30)));
						}
						//}
					}
  			    }

				this.brutNonImposable=indemniteRepresentation.add(indemniteTransport).add(autreNonImposable);
		//this.indemniteRepresentation = Math.rint(calculerIndemniterRepresentation());
				this.basecnps=brutImposable.add(indemniteRepresentation).add(autreNonImposable).subtract(autreIndemImposable);
				this.cnps = calculCNPS(basecnps);
				this.avceAcpte = avanceEtAccompte;
				this.pretAlios = pretALIOS;
		//this.indemniteTransport = Math.rint(calculerIndemniteTransport());
			this.totalBrut = brutImposable.add(indemniteRepresentation).add(indemniteTransport).add(autreNonImposable);
			autrePrelevmentSociale= BigDecimal.valueOf(0d); BigDecimal patronalcmu= BigDecimal.valueOf(500d);BigDecimal salarialecmu= BigDecimal.valueOf(500d);
//			if(listRetenueSociale.size()>0 || listRetenueSociale!=null){
//				for(PrimePersonnel sociale : listRetenueSociale){
//					if(sociale.getPrime().getLibelle().equals("CMU Patronal")){
//						patronalcmu=sociale.getMontant();
//					}
//					else{
//						autrePrelevmentSociale=autrePrelevmentSociale + sociale.getMontant();}
//
//				}
//			}
			this.CMU=salarialecmu;
			this.retenueSociiale=salarialecmu.add(cnps);
			this.CMUPatronal=patronalcmu;


			autrePrelevment= BigDecimal.valueOf(0d);
			if(!listMutuelle.isEmpty()){
				for(PrimePersonnel mutuell : listMutuelle){
					autrePrelevment=autrePrelevment.add(mutuell.getMontant());
				}
			}

				this.totalRetenue = totalRetenueFiscale.add(cnps).add(avceAcpte).add(pretAlios).add(autrePrelevment) ;
				regularisation= BigDecimal.valueOf(0d);
			if(!listGains.isEmpty() ){
				for(PrimePersonnel primeGains : listGains){
					regularisation=regularisation.add(primeGains.getMontant());
				}
			}
				this.netPayer = brutImposable.add(indemniteRepresentation).add(indemniteTransport).add(autreNonImposable).add(regularisation).subtract(autreIndemImposable).subtract(totalRetenue) ;
				this.is =  this.its;
			    this.ta = brutImposable.multiply(BigDecimal.valueOf(0.4)).divide(BigDecimal.valueOf(100));
			   // this.fpc = Math.ceil(brutImposable * 0.6 / 100);
			    this.fpc = brutImposable.multiply(BigDecimal.valueOf(0.6)).divide(BigDecimal.valueOf(100));
			    this.fpcregul = brutImposable.multiply(BigDecimal.valueOf(0.6)).divide(BigDecimal.valueOf(100));
				//this.ta = Math.rint(brutImposable * 0.4 / 100);
				//this.fpc = Math.rint(brutImposable * 0.6 / 100);
				this.prestationFamiliale = calculerPrestationFamiliale();
				this.accidentTravail = calculerAccidentTravail();
				this.retraite =brutImposable.add(indemniteRepresentation).add(autreNonImposable).multiply(BigDecimal.valueOf(7.7)).divide(BigDecimal.valueOf(100));
				this.totalPatronal = is.add(ta).add(fpc).add(fpcregul).add(prestationFamiliale).add(accidentTravail).add(retraite);
				this.totalMasseSalariale = brutImposable.add( indemniteRepresentation).add(indemniteTransport).add(autreNonImposable).add(regularisation).add(totalPatronal);
				//this.tempspresence= countnbreJrdu(ctratperso.getPersonnel().getDateRetourcge(), plconge.getDatefin(), ctratperso);
				//this.moisdepresence= ProvisionConge.calculerTempsPresence(ctratperso.getPersonnel().getDateRetourcge(), plconge.getDatefin());
	    }
//	    else{
//
//			///// traitement consultant et stagiaire   //////////
//			    autreImposable=0d;
//			    if(listIndemniteBrut.size()>0 || listIndemniteBrut!=null){
//				  for(PrimePersonnel primeImpos : listIndemniteBrut){
//					if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
//					{
//						autreImposable=autreImposable+primeImpos.getValeur()*(primeImpos.getMontant()+(primeImpos.getMontant()*primeImpos.getPrime().getTaux()/100));
//					}else{
//						if(primeImpos.getPrime().getMtExedent()!=null)
//							autreImposable=autreImposable+primeImpos.getMontant()-primeImpos.getPrime().getMtExedent();
//						else
//							autreImposable=autreImposable+primeImpos.getMontant();
//					}
//				}
//
//				autreNonImposable=0d;
//				if(listIndemniteNonImp.size()>0 || listIndemniteNonImp!=null){
//						for(PrimePersonnel primeImpos : listIndemniteNonImp){
//					/*if(primeImpos.getPrime().getTaux()!=null && primeImpos.getValeur()>0)
//					{
//						autreNonImposable=autreNonImposable+(primeImpos.getMontant()*primeImpos.getPrime().getTaux()*primeImpos.getValeur())*tempeffect.getJourspresence()/30;
//					}else{*/
//							if(tempeffect==null){
//								if(primeImpos.getPrime().getMtExedent()!=null)
//									autreNonImposable=autreNonImposable+primeImpos.getPrime().getMtExedent();
//								else
//									autreNonImposable=autreNonImposable+primeImpos.getMontant();
//							}else{
//
//								if(primeImpos.getPrime().getMtExedent()!=null)
//									autreNonImposable=autreNonImposable+primeImpos.getPrime().getMtExedent()*tempeffect.getJourspresence()/30;
//								else
//									autreNonImposable=autreNonImposable+(primeImpos.getMontant()*tempeffect.getJourspresence()/30);
//							}
//							//}
//						}
//				}
//					regularisation=0d;
//					if(listGains.size()>0 || listGains!=null){
//						for(PrimePersonnel primeGains : listGains){
//
//							regularisation=regularisation+primeGains.getMontant();
//
//							//}
//						}
//					}
//			//if(ctratperso.getPersonnel().getCarec()==false)
//			//{	 this.its = Math.rint(0);
//				this.cn =  Math.rint(0);
//				this.igr = Math.rint(0);
//				this.totalRetenueFiscale = Math.rint(0);
//				this.brutImposable = Math.rint(autreImposable);
//				this.brutNonImposable = Math.rint(autreNonImposable);
//				this.basecnps=Math.rint(0);
//				this.is = Math.rint(0);
//				this.ta = Math.rint(0);
//				this.fpc = Math.rint(0);
//				this.prestationFamiliale = Math.rint(0);
//				this.accidentTravail = Math.rint(0);
//				this.retraite = Math.rint(0);
//				this.cnps = Math.rint(0);
//				this.retraite = Math.rint(0);
//				this.plafondFamiliale = Math.rint(0);
//
//				avceAcpte=Math.rint(0); pretAlios=Math.rint(0);
//					autrePrelevment=0d;
//					if(listMutuelle.size()>0 || listMutuelle!=null){
//						for(PrimePersonnel mutuell : listMutuelle){
//
//							autrePrelevment=autrePrelevment+mutuell.getMontant();
//
//							//}
//						}
//					}
//				this.totalRetenue = Math.rint(totalRetenueFiscale + cnps + avceAcpte + pretAlios +autrePrelevment);
//				this.netPayer = Math.rint(brutImposable +regularisation+ brutNonImposable-totalRetenue);
//				//this.is = Math.rint(brutImposable * 1.2 / 100);
//				//this.ta = Math.rint(brutImposable * 0.4 / 100);
//				//this.fpc = Math.rint(brutImposable * 1.2 / 100);
//				//this.prestationFamiliale = Math.rint(calcalerPrestationFamilial());
//				//this.accidentTravail = Math.rint(calculerAccidentTravail());
//				//this.retraite = Math.rint((brutImposable + indemniteRepresentation+indemniteResponsabilte+autreNonImposable) * 7.7 / 100);
//				this.totalPatronal = Math.rint(is + ta + fpc + prestationFamiliale + accidentTravail + retraite);
//				this.totalMasseSalariale = Math.rint(brutImposable + brutNonImposable+regularisation+ totalPatronal);
//			//	this.tempspresence= countnbreJrdu(ctratperso.getPersonnel().getDateRetourcge(), plconge.getDatefin(), ctratperso);
//			//	this.moisdepresence= ProvisionConge.calculerTempsPresence(ctratperso.getPersonnel().getDateRetourcge(), plconge.getDatefin());
//			//}
//
//		}
		//}
	}



    public BigDecimal calculerAccidentTravail() {

        BigDecimal plafond = new BigDecimal("70000");
        BigDecimal taux = new BigDecimal("0.04");

        BigDecimal pf = safe(brutImposable).add(safe(autreNonImposable));

        if (pf.compareTo(plafond) > 0) {
            return plafond.multiply(taux).setScale(0, RoundingMode.HALF_UP);
        } else if (pf.compareTo(BigDecimal.ZERO) > 0) {
            return safe(brutImposable)
                    .multiply(taux)
                    .setScale(0, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calculerPrestationFamiliale() {

        BigDecimal plafond = new BigDecimal("70000");
        BigDecimal taux = new BigDecimal("0.0575");

        BigDecimal pf = safe(brutImposable).add(safe(autreNonImposable));

        if (pf.compareTo(plafond) > 0) {
            return plafond.multiply(taux).setScale(0, RoundingMode.HALF_UP);
        } else if (pf.compareTo(BigDecimal.ZERO) > 0) {
            return safe(brutImposable)
                    .multiply(taux)
                    .setScale(0, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }
    private BigDecimal safe(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }
//	public BigDecimal calculerIndemniteTransport(){
//		BigDecimal it = brutImposable * 10 / 100 - 25000.0;
//		if(it > 0)
//			it = 25000.0;
//		else
//			it = 0.0;
//		return it;
//	}
//
//	public BigDecimal calculerIndemniterRepresentation(){
//		BigDecimal ir = brutImposable * 10 / 100 - 25000.0;
//		if(ir > 0)
//			ir = brutImposable * 10 / 100 - 25000.0;
//		else
//			ir = 0.0;
//		return ir;
//	}

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
		BigDecimal cnps = (basecnps );
		if(cnps.compareTo(BigDecimal.valueOf(3375000.0))  <0)
			cnps = (basecnps ).multiply(BigDecimal.valueOf(6.3)).divide(BigDecimal.valueOf(100));
		else
			cnps = BigDecimal.valueOf(3375000.0).multiply(BigDecimal.valueOf(6.3)).divide(BigDecimal.valueOf(100));
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

	@Override
	public String toString() {
		return "LivreDePaieSimulation{" +
				"matricule='" + matricule + '\'' +
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
				", autrePrelevment=" + autrePrelevment +
				", primeAnciennete=" + primeAnciennete +
				", moisdepresence=" + moisdepresence +
				", tempspresence=" + tempspresence +
				", mtprimeAnciennete='" + mtprimeAnciennete + '\'' +
				", indemniteLogement=" + indemniteLogement +
				", mtindemniteLogement='" + mtindemniteLogement + '\'' +
				", brutImposable=" + brutImposable +
				", mtbrutImposable='" + mtbrutImposable + '\'' +
				", mtautrePrelevment='" + mtautrePrelevment + '\'' +
				", regularisation=" + regularisation +
				", mtregularisation='" + mtregularisation + '\'' +
				", brutNonImposable=" + brutNonImposable +
				", mtbrutNonImposable='" + mtbrutNonImposable + '\'' +
				", its=" + its +
				", mtits='" + mtits + '\'' +
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
				", indemniteRepresentation=" + indemniteRepresentation +
				", mtindemniteRepresentation='" + mtindemniteRepresentation + '\'' +
				", indemniteTransport=" + indemniteTransport +
				", mtindemniteTransport='" + mtindemniteTransport + '\'' +
				", autrePrelevmentSociale=" + autrePrelevmentSociale +
				", mtautrePrelevmentSociale='" + mtautrePrelevmentSociale + '\'' +
				", indemniteTransportImp=" + indemniteTransportImp +
				", mtindemniteTransportImp='" + mtindemniteTransportImp + '\'' +
				", indemniteResponsabilte=" + indemniteResponsabilte +
				", mtindemniteResponsabilte='" + mtindemniteResponsabilte + '\'' +
				", retenueSociiale=" + retenueSociiale +
				", mtretenueSociiale='" + mtretenueSociiale + '\'' +
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
				", bullpaie=" + bullpaie +
				", jourTravail=" + jourTravail +
				", temptravail=" + temptravail +
				", CMU=" + CMU +
				", mtCMU='" + mtCMU + '\'' +
				", CMUSalarial=" + CMUSalarial +
				", mtCMUSalarial='" + mtCMUSalarial + '\'' +
				", CMUPatronal=" + CMUPatronal +
				", mtCMUPatronal='" + mtCMUPatronal + '\'' +
				", RetenueSociale=" + RetenueSociale +
				", mtRetenueSocial='" + mtRetenueSocial + '\'' +
				", totalRetenueSociale=" + totalRetenueSociale +
				", mttotalRetenueSocial='" + mttotalRetenueSocial + '\'' +
				", contratPersonnel=" + contratPersonnel +
				", periodePaie=" + periodePaie +
				'}';
	}

	public int countnbreJrdu(Date dateRetourDernierConge, Date dateDepartConge, ContratPersonnel Contratp) {
		// TODO Auto-generated method stub

		int tps=ProvisionConge.calculerTempsPresence(dateRetourDernierConge,dateDepartConge);
		int rf=(int) (tps*2.2*1.25);
        // 🔹 Ancienneté
        long anneesAnciennete = ChronoUnit.YEARS.between(
                Contratp.getPersonnel().getDateArrivee().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now()
        );
        int anciennete = (int) (anneesAnciennete + Contratp.getAncienneteInitial());
        int op = anciennete < 2 ? 0 : Math.min(anciennete, 25);
        int anc=(int)op ;

	     int jourSuppAnc=0; int jourSuppDam = 0;int jourSuppMed = 0;

		 if(anc>5 && anc<=10)  jourSuppAnc=1;
		 if(anc>10 && anc<=15) jourSuppAnc=2;
		 if(anc>15 && anc<=20) jourSuppAnc=3;
		 if(anc>20 && anc<=25) jourSuppAnc=5;
		 if(anc>25 && anc<=30) jourSuppAnc=7;
		 if(anc>30) jourSuppAnc=8;

		 Double age= DifferenceDate.valAge(new Date(), Contratp.getPersonnel().getDateNaissance());
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
//		tab[2] = (BigDecimal) partieApresVirg;
//
//
//
//		return tab;
//	}




}
