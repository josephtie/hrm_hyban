package com.nectux.mizan.hyban.paie.entity;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Rubrique;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.utils.CalculRICF;
import com.nectux.mizan.hyban.utils.DifferenceDate;
import com.nectux.mizan.hyban.utils.ProvisionConge;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.RoundingMode;
import static com.nectux.mizan.hyban.utils.CalculRICF.getRICF;
import static com.nectux.mizan.hyban.utils.ITSCalculator.calculerITS;
import static org.apache.commons.collections4.ListUtils.defaultIfNull;

public class LivreDePaie {

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



    private static final BigDecimal JOURS_OUVRABLES_MOIS = BigDecimal.valueOf(30);
    private static final BigDecimal PLAFOND_TRANSPORT = new BigDecimal("30000");
    private BigDecimal netCible = BigDecimal.ZERO;

    public LivreDePaie() {
    }

    public LivreDePaie(
            String mat,
            String nomPre,
            Float nbrePart,
            int ancien,
            BigDecimal salBase,
            BigDecimal sursal,
            BigDecimal indemLog,
            BigDecimal avanceEtAccompte,
            BigDecimal pretALIOS,
            ContratPersonnel ctratperso,
            TempEffectif tempeffect,
            PeriodePaie plconge,
            List<PrimePersonnel> listIndemnite,
            List<PrimePersonnel> listIndemniteNonImp,
            List<PrimePersonnel> listMutuelle,
            List<PrimePersonnel> listGains,
            List<PrimePersonnel> listRetenueSociale
    ) {

        this.matricule = mat;
        this.nomPrenom = nomPre;
        this.nombrePart = nbrePart != null ? nbrePart : 0F;
        this.anciennete = ancien;

        this.salaireBase = safe(salBase);
        this.sursalaire = safe(sursal);
        this.indemniteLogement = safe(indemLog);
        this.avceAcpte = safe(avanceEtAccompte);
        this.pretAlios = safe(pretALIOS);

        this.listIndemniteBrut = safeList(listIndemnite);
        this.listIndemniteNonImp = safeList(listIndemniteNonImp);
        this.listRetenueMutuellt = safeList(listMutuelle);
        this.listGainsNet = safeList(listGains);
        this.listRetenueSociale = safeList(listRetenueSociale);

        this.contratPersonnel = ctratperso;
        this.periodePaie = plconge;

        if (tempeffect != null) {
            this.netCible = safe(ctratperso.getNetAPayer())
                    .multiply(BigDecimal.valueOf(tempeffect.getJourspresence()))
                    .divide(JOURS_OUVRABLES_MOIS, 2, RoundingMode.HALF_UP);
        }

        // 🚀 moteur
        if (Boolean.TRUE.equals(ctratperso.getPersonnel().getCarec())) {
            calculerContractuel(tempeffect);
        } else {
            calculerConsultant(tempeffect);
        }
    }

    private BigDecimal appliquerProrata(BigDecimal montant, TempEffectif tempeffect) {

        if (tempeffect == null) return montant;

        BigDecimal joursPresence = BigDecimal.valueOf(tempeffect.getJourspresence());

        return montant
                .multiply(joursPresence)
                .divide(JOURS_OUVRABLES_MOIS, 4, RoundingMode.HALF_UP);
    }

    private void calculerContractuel(TempEffectif tempeffect) {

        calculTransport(tempeffect);
        calculIndemnites(tempeffect);

        calculBrutImposable(tempeffect);
        calculBrutNonImposable(tempeffect);
        BigDecimal valbaseCnps= calculBaseCnps();
        calculITS();

        calculCNPS(valbaseCnps);

        calculTotalRetenueFiscale(); // 🔥 AJOUT IMPORTANT
        calculRetenues();

        calculNet(tempeffect);

        calculChargesPatronales();
        calculTotalPatronal();
        calculMasseSalariale();
        this.tempspresence= countnbreJrdu(this.contratPersonnel.getPersonnel().getDateRetourcge(), this.periodePaie.getDatefin(), this.contratPersonnel);
        this.moisdepresence= ProvisionConge.calculerTempsPresence(this.contratPersonnel.getPersonnel().getDateRetourcge(), periodePaie.getDatefin());
    }
    private void calculerConsultant(TempEffectif tempeffect) {

       // calculTransport();
       // calculIndemnites();

        //calculBrutImposable();
       // calculBrutNonImposable();
       // BigDecimal valbaseCnps= calculBaseCnps();
       // calculITS();

      //  calculCNPS(valbaseCnps);

      //  calculTotalRetenueFiscale(); // 🔥 AJOUT IMPORTANT
        calculRetenues();

      //  calculNet(tempeffect);

      //  calculChargesPatronales();
      //  calculTotalPatronal();
        calculMasseSalariale();
    }
    private void calculIndemnites(TempEffectif tempeffect) {

        this.autreImposable = BigDecimal.ZERO;

        for (PrimePersonnel p : listIndemniteBrut) {

            BigDecimal montant = safe(p.getMontant());

            // 🔥 PRORATA
            montant = appliquerProrata(montant, tempeffect);

            if (p.getPrime().getMtExedent() != null) {
                montant = montant.subtract(safe(p.getPrime().getMtExedent()));
            }

            this.autreImposable = this.autreImposable.add(montant);
        }

        this.autreNonImposable = BigDecimal.ZERO;

        for (PrimePersonnel p : listIndemniteNonImp) {

            BigDecimal montant = safe(p.getMontant());

            if (p.getPrime().getMtExedent() != null) {
                montant = p.getPrime().getMtExedent();
            }

            this.autreNonImposable = this.autreNonImposable.add(montant);
        }
    }


    private void calculTransport(TempEffectif tempeffect) {

        BigDecimal transport = appliquerProrata(
                safe(contratPersonnel.getIndemniteTransport()),
                tempeffect
        );

        if (transport.compareTo(PLAFOND_TRANSPORT) > 0) {

            this.indemniteTransport = PLAFOND_TRANSPORT;
            this.indemniteTransportImp = transport.subtract(PLAFOND_TRANSPORT);

        } else {
            this.indemniteTransport = transport;
            this.indemniteTransportImp = BigDecimal.ZERO;
        }
    }


    private void calculBrutImposable(TempEffectif tempeffect) {

        BigDecimal base = appliquerProrata(safe(salaireBase), tempeffect);
        BigDecimal surs = appliquerProrata(safe(sursalaire), tempeffect);
        BigDecimal log = appliquerProrata(safe(indemniteLogement), tempeffect);
         this.salaireBase=base;
         this.sursalaire=log;
         this.indemniteLogement=base;
        this.brutImposable = ceil(
                base
                        .add(surs)
                        .add(log)
                        .add(safe(indemniteTransportImp)) // déjà proratisé
                        .add(safe(autreImposable))        // déjà proratisé
        );
    }

    private void calculBrutNonImposable(TempEffectif tempeffect) {

        BigDecimal representation = appliquerProrata(
                safe(indemniteRepresentation),
                tempeffect
        );

        this.brutNonImposable = ceil(
                representation
                        .add(safe(indemniteTransport))
                        .add(safe(autreNonImposable))
        );
    }


    private BigDecimal calculBaseCnps() {
        BigDecimal valretour ;
        BigDecimal inclus = brutImposable
                .add(indemniteRepresentation != null ? indemniteRepresentation : BigDecimal.ZERO)
                .add(autreNonImposable != null ? autreNonImposable : BigDecimal.ZERO);

        BigDecimal exclus = autreIndemImposable != null ? autreIndemImposable : BigDecimal.ZERO;

        this.basecnps = ceil(inclus.subtract(exclus));
        valretour=basecnps;
         return valretour;
    }


    private void calculITS() {

        BigDecimal ricf = BigDecimal.valueOf(CalculRICF.getRICF(nombrePart));

        BigDecimal itsBrut = ceil(calculerITS(brutImposable, true));

        this.its = itsBrut
                .subtract(ricf.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP))
                .max(BigDecimal.ZERO);
        this.is=its;
    }

    public BigDecimal calculCNPS(BigDecimal baseCnps) {

        final BigDecimal TAUX_CNPS = new BigDecimal("0.063");
        final BigDecimal PLAFOND = new BigDecimal("3375000");

        if (baseCnps == null) {
            return BigDecimal.ZERO;
        }

        // min(baseCnps, plafond)
        BigDecimal baseRetenue = baseCnps.min(PLAFOND);

        return baseRetenue
                .multiply(TAUX_CNPS)
                .setScale(0, RoundingMode.HALF_UP);
    }
    private void calculRetenues() {

        BigDecimal mutuelle = BigDecimal.ZERO;

        for (PrimePersonnel p : listRetenueMutuellt) {
            mutuelle = mutuelle.add(safe(p.getMontant()));
        }

        BigDecimal sociale = BigDecimal.ZERO;

        for (PrimePersonnel p : listRetenueSociale) {
            sociale = sociale.add(safe(p.getMontant()));
        }

        this.retenueSociiale = sociale.add(cnps);

        this.totalRetenue = ceil(
                its
                        .add(avceAcpte)
                        .add(pretAlios)
                        .add(mutuelle)
                        .add(retenueSociiale)
        );
    }

    private void calculNet(TempEffectif tempeffect) {

        this.regularisation = BigDecimal.ZERO;

        for (PrimePersonnel p : listGainsNet) {
            regularisation = regularisation.add(safe(p.getMontant()));
        }

        BigDecimal totalBrut = brutImposable
                .add(indemniteTransport)
                .add(autreNonImposable)
                .add(regularisation);

        this.netPayer = ceil(
                totalBrut.subtract(totalRetenue)
        );

        if(tempeffect!=null){
            this.netRegulPayer=netCible.subtract(netPayer) ;
            this.netPayer=netCible.subtract(pretAlios).subtract(avceAcpte) ;
        }
    }
    private void calculTotalRetenueFiscale() {

        this.totalRetenueFiscale =
                arrondi(
                        safe(its)
                                .add(safe(cn))
                                .add(safe(igr))
                );
    }
    private void calculChargesPatronales() {

        this.ta = percent(brutImposable, BigDecimal.valueOf(0.4));
        this.fpc = percent(brutImposable, BigDecimal.valueOf(0.6));
        this.fpcregul = percent(brutImposable, BigDecimal.valueOf(0.6));

        this.prestationFamiliale = calculerPrestationFamiliale();
        this.accidentTravail = calculerAccidentTravail();

        this.retraite = percent(
                brutImposable
                        .add(indemniteRepresentation)
                        .add(autreNonImposable),
                BigDecimal.valueOf(7.7)
        );

        // CMU patronale déjà calculée ailleurs
    }

    private void calculTotalPatronal() {

        this.totalPatronal = ceil(
                add(
                        is, // ITS
                        ta,
                        fpc,
                        fpcregul,
                        prestationFamiliale,
                        accidentTravail,
                        retraite,
                        CMUPatronal
                )
        );
    }

    private void calculMasseSalariale() {

        this.totalMasseSalariale = ceil(
                add(
                        brutImposable,
                        brutNonImposable,
                        regularisation,
                        totalPatronal
                )
        );
    }



    public BigDecimal calculerAccidentTravail() {

        BigDecimal plafond = new BigDecimal("75000");
        BigDecimal taux = new BigDecimal("0.04");

        BigDecimal pf = safe(brutImposable)
                .add(safe(autreNonImposable));

        if (pf.compareTo(plafond) <= 0) {
            return BigDecimal.ZERO;
        }

        return safe(brutImposable)
                .multiply(taux)
                .setScale(0, RoundingMode.HALF_UP);
    }

    public BigDecimal calculerPrestationFamiliale() {

        BigDecimal plafond = new BigDecimal("75000");
        BigDecimal taux = new BigDecimal("0.0575");

        BigDecimal pf = safe(brutImposable);
                //.add(safe(autreNonImposable))

        if (pf.compareTo(plafond) <= 0) {
            return BigDecimal.ZERO;
        }
        this.prestationFamiliale=safe(brutImposable)
                .multiply(taux)
                .setScale(0, RoundingMode.HALF_UP);
        return safe(brutImposable)
                .multiply(taux)
                .setScale(0, RoundingMode.HALF_UP);
    }


    private BigDecimal safe(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }


    private List<PrimePersonnel> safeList(List<PrimePersonnel> list) {
        return list == null ? List.of() : list;
    }

    private BigDecimal ceil(BigDecimal val) {
        return val.setScale(0, RoundingMode.CEILING);
    }

    private BigDecimal percent(BigDecimal base, BigDecimal taux) {
        return base.multiply(taux).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal arrondi(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(0, RoundingMode.HALF_UP);
    }

    private BigDecimal add(BigDecimal... values) {
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal v : values) {
            result = result.add(safe(v));
        }
        return result;
    }

    public int countnbreJrdu(Date dateRetourDernierConge, Date dateDepartConge, ContratPersonnel Contratp) {
        // TODO Auto-generated method stub

        int tps=ProvisionConge.calculerTempsPresence(dateRetourDernierConge,dateDepartConge);
        int rf=(int) (tps*2.2*1.25);
        BigDecimal[]ancienete= calculAnciennete(Contratp.getCategorie().getSalaireDeBase(),Contratp.getPersonnel().getDateArrivee());
        BigDecimal newancienete;
        if(Contratp.getAncienneteInitial()!=0) {
            newancienete=ancienete[1].add(BigDecimal.valueOf(Contratp.getAncienneteInitial()));
        }else{
            newancienete=ancienete[1];
        }
        BigDecimal anc=newancienete ;

        int jourSuppAnc=0; int jourSuppDam = 0;int jourSuppMed = 0;

        BigDecimal ancSafe = anc == null ? BigDecimal.ZERO : anc;

        if (ancSafe.compareTo(BigDecimal.valueOf(5)) > 0 &&
                ancSafe.compareTo(BigDecimal.valueOf(10)) <= 0) {

            jourSuppAnc = 1;

        } else if (ancSafe.compareTo(BigDecimal.valueOf(10)) > 0 &&
                ancSafe.compareTo(BigDecimal.valueOf(15)) <= 0) {

            jourSuppAnc = 2;

        } else if (ancSafe.compareTo(BigDecimal.valueOf(15)) > 0 &&
                ancSafe.compareTo(BigDecimal.valueOf(20)) <= 0) {

            jourSuppAnc = 3;

        } else if (ancSafe.compareTo(BigDecimal.valueOf(20)) > 0 &&
                ancSafe.compareTo(BigDecimal.valueOf(25)) <= 0) {

            jourSuppAnc = 5;

        } else if (ancSafe.compareTo(BigDecimal.valueOf(25)) > 0 &&
                ancSafe.compareTo(BigDecimal.valueOf(30)) <= 0) {

            jourSuppAnc = 7;

        } else if (ancSafe.compareTo(BigDecimal.valueOf(30)) > 0) {

            jourSuppAnc = 8;
        }

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


    public  BigDecimal[] calculAnciennete(BigDecimal salaireCategoriel, Date dateEntree){

        BigDecimal[] tab = new BigDecimal[5];

        BigDecimal anciennete = BigDecimal.valueOf(0) ;


        double age = DifferenceDate.valAge(new Date(), dateEntree);

        int partieEntiere = (int) age;
        int partieApresVirg = (int)((age - partieEntiere) * 12);


        if(age>=2)
            anciennete = salaireCategoriel.multiply(BigDecimal.valueOf(partieEntiere)).divide(BigDecimal.valueOf(100));

        tab[0] = anciennete;


        tab[1] = BigDecimal.valueOf(partieEntiere);
        tab[2] = BigDecimal.valueOf((partieApresVirg));



        return tab;
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

    public String getMtsalaireBase() {
        return mtsalaireBase;
    }

    public void setMtsalaireBase(String mtsalaireBase) {
        this.mtsalaireBase = mtsalaireBase;
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
        this.mtautreImposable = mtautreImposable;
    }

    public BigDecimal getAutreIndemImposable() {
        return autreIndemImposable;
    }

    public void setAutreIndemImposable(BigDecimal autreIndemImposable) {
        this.autreIndemImposable = autreIndemImposable;
    }

    public String getMtautreIndemImposable() {
        return mtautreIndemImposable;
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

    public BigDecimal getAutreNonImposable() {
        return autreNonImposable;
    }

    public void setAutreNonImposable(BigDecimal autreNonImposable) {
        this.autreNonImposable = autreNonImposable;
    }

    public String getMtautreNonImposable() {
        return mtautreNonImposable;
    }

    public void setMtautreNonImposable(String mtautreNonImposable) {
        this.mtautreNonImposable = mtautreNonImposable;
    }

    public BigDecimal getSursalaire() {
        return sursalaire;
    }

    public void setSursalaire(BigDecimal sursalaire) {
        this.sursalaire = sursalaire;
    }

    public String getMtsursalaire() {
        return mtsursalaire;
    }

    public void setMtsursalaire(String mtsursalaire) {
        this.mtsursalaire = mtsursalaire;
    }

    public BigDecimal getPrimeAnciennete() {
        return primeAnciennete;
    }

    public void setPrimeAnciennete(BigDecimal primeAnciennete) {
        this.primeAnciennete = primeAnciennete;
    }

    public Integer getMoisdepresence() {
        return moisdepresence;
    }

    public void setMoisdepresence(Integer moisdepresence) {
        this.moisdepresence = moisdepresence;
    }

    public Integer getTempspresence() {
        return tempspresence;
    }

    public void setTempspresence(Integer tempspresence) {
        this.tempspresence = tempspresence;
    }

    public String getMtprimeAnciennete() {
        return mtprimeAnciennete;
    }

    public void setMtprimeAnciennete(String mtprimeAnciennete) {
        this.mtprimeAnciennete = mtprimeAnciennete;
    }

    public BigDecimal getIndemniteLogement() {
        return indemniteLogement;
    }

    public void setIndemniteLogement(BigDecimal indemniteLogement) {
        this.indemniteLogement = indemniteLogement;
    }

    public String getMtindemniteLogement() {
        return mtindemniteLogement;
    }

    public void setMtindemniteLogement(String mtindemniteLogement) {
        this.mtindemniteLogement = mtindemniteLogement;
    }

    public BigDecimal getBrutImposable() {
        return brutImposable;
    }

    public void setBrutImposable(BigDecimal brutImposable) {
        this.brutImposable = brutImposable;
    }

    public String getMtbrutImposable() {
        return mtbrutImposable;
    }

    public void setMtbrutImposable(String mtbrutImposable) {
        this.mtbrutImposable = mtbrutImposable;
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

    public BigDecimal getAutrePrelevment() {
        return autrePrelevment;
    }

    public void setAutrePrelevment(BigDecimal autrePrelevment) {
        this.autrePrelevment = autrePrelevment;
    }

    public String getMtautrePrelevment() {
        return mtautrePrelevment;
    }

    public void setMtautrePrelevment(String mtautrePrelevment) {
        this.mtautrePrelevment = mtautrePrelevment;
    }

    public BigDecimal getAutrePrelevmentMutuelle() {
        return autrePrelevmentMutuelle;
    }

    public void setAutrePrelevmentMutuelle(BigDecimal autrePrelevmentMutuelle) {
        this.autrePrelevmentMutuelle = autrePrelevmentMutuelle;
    }

    public String getMtautrePrelevmentMutuelle() {
        return mtautrePrelevmentMutuelle;
    }

    public void setMtautrePrelevmentMutuelle(String mtautrePrelevmentMutuelle) {
        this.mtautrePrelevmentMutuelle = mtautrePrelevmentMutuelle;
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

    public BigDecimal getBrutNonImposable() {
        return brutNonImposable;
    }

    public void setBrutNonImposable(BigDecimal brutNonImposable) {
        this.brutNonImposable = brutNonImposable;
    }

    public String getMtbrutNonImposable() {
        return mtbrutNonImposable;
    }

    public void setMtbrutNonImposable(String mtbrutNonImposable) {
        this.mtbrutNonImposable = mtbrutNonImposable;
    }

    public BigDecimal getIts() {
        return its;
    }

    public void setIts(BigDecimal its) {
        this.its = its;
    }

    public String getMtits() {
        return mtits;
    }

    public void setMtits(String mtits) {
        this.mtits = mtits;
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

    public BigDecimal getCn() {
        return cn;
    }

    public void setCn(BigDecimal cn) {
        this.cn = cn;
    }

    public String getMtcn() {
        return mtcn;
    }

    public void setMtcn(String mtcn) {
        this.mtcn = mtcn;
    }

    public BigDecimal getIgr() {
        return igr;
    }

    public void setIgr(BigDecimal igr) {
        this.igr = igr;
    }

    public String getMtigr() {
        return mtigr;
    }

    public void setMtigr(String mtigr) {
        this.mtigr = mtigr;
    }

    public BigDecimal getTotalRetenueFiscale() {
        return totalRetenueFiscale;
    }

    public void setTotalRetenueFiscale(BigDecimal totalRetenueFiscale) {
        this.totalRetenueFiscale = totalRetenueFiscale;
    }

    public String getMttotalRetenueFiscale() {
        return mttotalRetenueFiscale;
    }

    public void setMttotalRetenueFiscale(String mttotalRetenueFiscale) {
        this.mttotalRetenueFiscale = mttotalRetenueFiscale;
    }

    public BigDecimal getCnps() {
        return cnps;
    }

    public void setCnps(BigDecimal cnps) {
        this.cnps = cnps;
    }

    public String getMtcnps() {
        return mtcnps;
    }

    public void setMtcnps(String mtcnps) {
        this.mtcnps = mtcnps;
    }

    public BigDecimal getBasecnps() {
        return basecnps;
    }

    public void setBasecnps(BigDecimal basecnps) {
        this.basecnps = basecnps;
    }

    public String getMtbasecnps() {
        return mtbasecnps;
    }

    public void setMtbasecnps(String mtbasecnps) {
        this.mtbasecnps = mtbasecnps;
    }

    public BigDecimal getAvceAcpte() {
        return avceAcpte;
    }

    public void setAvceAcpte(BigDecimal avceAcpte) {
        this.avceAcpte = avceAcpte;
    }

    public String getMtavceAcpte() {
        return mtavceAcpte;
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
        return mtpretAlios;
    }

    public void setMtpretAlios(String mtpretAlios) {
        this.mtpretAlios = mtpretAlios;
    }

    public BigDecimal getCarec() {
        return carec;
    }

    public void setCarec(BigDecimal carec) {
        this.carec = carec;
    }

    public String getMtcarec() {
        return mtcarec;
    }

    public void setMtcarec(String mtcarec) {
        this.mtcarec = mtcarec;
    }

    public BigDecimal getTotalRetenue() {
        return totalRetenue;
    }

    public void setTotalRetenue(BigDecimal totalRetenue) {
        this.totalRetenue = totalRetenue;
    }

    public String getMttotalRetenue() {
        return mttotalRetenue;
    }

    public void setMttotalRetenue(String mttotalRetenue) {
        this.mttotalRetenue = mttotalRetenue;
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

    public BigDecimal getIndemniteRepresentation() {
        return indemniteRepresentation;
    }

    public void setIndemniteRepresentation(BigDecimal indemniteRepresentation) {
        this.indemniteRepresentation = indemniteRepresentation;
    }

    public String getMtindemniteRepresentation() {
        return mtindemniteRepresentation;
    }

    public void setMtindemniteRepresentation(String mtindemniteRepresentation) {
        this.mtindemniteRepresentation = mtindemniteRepresentation;
    }

    public BigDecimal getIndemniteTransport() {
        return indemniteTransport;
    }

    public void setIndemniteTransport(BigDecimal indemniteTransport) {
        this.indemniteTransport = indemniteTransport;
    }

    public String getMtindemniteTransport() {
        return mtindemniteTransport;
    }

    public void setMtindemniteTransport(String mtindemniteTransport) {
        this.mtindemniteTransport = mtindemniteTransport;
    }

    public BigDecimal getIndemniteTransportImp() {
        return indemniteTransportImp;
    }

    public void setIndemniteTransportImp(BigDecimal indemniteTransportImp) {
        this.indemniteTransportImp = indemniteTransportImp;
    }

    public String getMtindemniteTransportImp() {
        return mtindemniteTransportImp;
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
        return mtindemniteResponsabilte;
    }

    public void setMtindemniteResponsabilte(String mtindemniteResponsabilte) {
        this.mtindemniteResponsabilte = mtindemniteResponsabilte;
    }

    public BigDecimal getNetRegulPayer() {
        return netRegulPayer;
    }

    public void setNetRegulPayer(BigDecimal netRegulPayer) {
        this.netRegulPayer = netRegulPayer;
    }

    public String getMtRegulnetPayer() {
        return mtRegulnetPayer;
    }

    public void setMtRegulnetPayer(String mtRegulnetPayer) {
        this.mtRegulnetPayer = mtRegulnetPayer;
    }

    public BigDecimal getNetPayer() {
        return netPayer;
    }

    public void setNetPayer(BigDecimal netPayer) {
        this.netPayer = netPayer;
    }

    public String getMtnetPayer() {
        return mtnetPayer;
    }

    public void setMtnetPayer(String mtnetPayer) {
        this.mtnetPayer = mtnetPayer;
    }

    public BigDecimal getTotalBrut() {
        return totalBrut;
    }

    public void setTotalBrut(BigDecimal totalBrut) {
        this.totalBrut = totalBrut;
    }

    public String getMttotalBrut() {
        return mttotalBrut;
    }

    public void setMttotalBrut(String mttotalBrut) {
        this.mttotalBrut = mttotalBrut;
    }

    public BigDecimal getIs() {
        return is;
    }

    public void setIs(BigDecimal is) {
        this.is = is;
    }

    public String getMtis() {
        return mtis;
    }

    public void setMtis(String mtis) {
        this.mtis = mtis;
    }

    public BigDecimal getTa() {
        return ta;
    }

    public void setTa(BigDecimal ta) {
        this.ta = ta;
    }

    public String getMtta() {
        return mtta;
    }

    public void setMtta(String mtta) {
        this.mtta = mtta;
    }

    public BigDecimal getFpc() {
        return fpc;
    }

    public void setFpc(BigDecimal fpc) {
        this.fpc = fpc;
    }

    public String getMtfpc() {
        return mtfpc;
    }

    public void setMtfpc(String mtfpc) {
        this.mtfpc = mtfpc;
    }

    public BigDecimal getFpcregul() {
        return fpcregul;
    }

    public void setFpcregul(BigDecimal fpcregul) {
        this.fpcregul = fpcregul;
    }

    public String getMtfpcregul() {
        return mtfpcregul;
    }

    public void setMtfpcregul(String mtfpcregul) {
        this.mtfpcregul = mtfpcregul;
    }

    public BigDecimal getPrestationFamiliale() {
        return prestationFamiliale;
    }

    public void setPrestationFamiliale(BigDecimal prestationFamiliale) {
        this.prestationFamiliale = prestationFamiliale;
    }

    public String getMtprestationFamiliale() {
        return mtprestationFamiliale;
    }

    public void setMtprestationFamiliale(String mtprestationFamiliale) {
        this.mtprestationFamiliale = mtprestationFamiliale;
    }

    public BigDecimal getAccidentTravail() {
        return accidentTravail;
    }

    public void setAccidentTravail(BigDecimal accidentTravail) {
        this.accidentTravail = accidentTravail;
    }

    public String getMtaccidentTravail() {
        return mtaccidentTravail;
    }

    public void setMtaccidentTravail(String mtaccidentTravail) {
        this.mtaccidentTravail = mtaccidentTravail;
    }

    public BigDecimal getRetraite() {
        return retraite;
    }

    public void setRetraite(BigDecimal retraite) {
        this.retraite = retraite;
    }

    public String getMtretraite() {
        return mtretraite;
    }

    public void setMtretraite(String mtretraite) {
        this.mtretraite = mtretraite;
    }

    public BigDecimal getTotalPatronal() {
        return totalPatronal;
    }

    public void setTotalPatronal(BigDecimal totalPatronal) {
        this.totalPatronal = totalPatronal;
    }

    public String getMttotalPatronal() {
        return mttotalPatronal;
    }

    public void setMttotalPatronal(String mttotalPatronal) {
        this.mttotalPatronal = mttotalPatronal;
    }

    public BigDecimal getTotalMasseSalariale() {
        return totalMasseSalariale;
    }

    public void setTotalMasseSalariale(BigDecimal totalMasseSalariale) {
        this.totalMasseSalariale = totalMasseSalariale;
    }

    public String getMttotalMasseSalariale() {
        return mttotalMasseSalariale;
    }

    public void setMttotalMasseSalariale(String mttotalMasseSalariale) {
        this.mttotalMasseSalariale = mttotalMasseSalariale;
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

    public BulletinPaie getBullpaie() {
        return bullpaie;
    }

    public void setBullpaie(BulletinPaie bullpaie) {
        this.bullpaie = bullpaie;
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

    public List<Rubrique> getListRubrique() {
        return listRubrique;
    }

    public void setListRubrique(List<Rubrique> listRubrique) {
        this.listRubrique = listRubrique;
    }

    public List<PrimePersonnel> getListIndemBrutNonImp() {
        return listIndemBrutNonImp;
    }

    public void setListIndemBrutNonImp(List<PrimePersonnel> listIndemBrutNonImp) {
        this.listIndemBrutNonImp = listIndemBrutNonImp;
    }

    public List<PrimePersonnel> getListRetenueMutuellt() {
        return listRetenueMutuellt;
    }

    public void setListRetenueMutuellt(List<PrimePersonnel> listRetenueMutuellt) {
        this.listRetenueMutuellt = listRetenueMutuellt;
    }

    public List<PrimePersonnel> getListRetenueSociale() {
        return listRetenueSociale;
    }

    public void setListRetenueSociale(List<PrimePersonnel> listRetenueSociale) {
        this.listRetenueSociale = listRetenueSociale;
    }

    public List<PrimePersonnel> getListGainsNet() {
        return listGainsNet;
    }

    public void setListGainsNet(List<PrimePersonnel> listGainsNet) {
        this.listGainsNet = listGainsNet;
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

    public BigDecimal getNetCible() {
        return netCible;
    }

    public void setNetCible(BigDecimal netCible) {
        this.netCible = netCible;
    }
}