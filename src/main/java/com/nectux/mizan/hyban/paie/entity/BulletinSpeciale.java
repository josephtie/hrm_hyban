package com.nectux.mizan.hyban.paie.entity;

import com.nectux.mizan.hyban.parametrages.entity.Auditable;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.personnel.specifque.entity.SpecialContract;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Component("bulletinPaieSpeciale")
@Scope("prototype")
@Table(name="CGECI_RHPAIE_BULLETIN_PAIE_SPECIALE")
@SequenceGenerator(name="CGECI_RHPAIE_BULLETIN_PAIE_SPECIALE_SEQUENCE", sequenceName="CGECI_RHPAIE_BULLETIN_PAIE_SPECIALE_SEQ", initialValue=1, allocationSize=1)
public class BulletinSpeciale extends Auditable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CGECI_RHPAIE_BULLETIN_PAIE_SPECIALE_SEQUENCE")
    @Column(unique=true, nullable=false)
    private Long id;


    private String matricule;

    private String nomPrenom;

    private Boolean calculer;

    private Boolean cloture;

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

    private BigDecimal cumulNet;
    @Transient
    private String mtcumulNet;


    private BigDecimal netPayer;
    @Transient
    private String mtnetPayer;

    @ManyToOne
    @JoinColumn(nullable=false)
    private SpecialContract specialContract;

    @ManyToOne
    @JoinColumn(nullable=false)
    private PeriodePaie periodePaie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getCumulNet() {
        return cumulNet;
    }

    public void setCumulNet(BigDecimal cumulNet) {
        this.cumulNet = cumulNet;
    }

    public String getMtcumulNet() {
        return mtcumulNet;
    }

    public void setMtcumulNet(String mtcumulNet) {
        this.mtcumulNet = mtcumulNet;
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

    public SpecialContract getSpecialContract() {
        return specialContract;
    }

    public void setSpecialContract(SpecialContract specialContract) {
        this.specialContract = specialContract;
    }

    public PeriodePaie getPeriodePaie() {
        return periodePaie;
    }

    public void setPeriodePaie(PeriodePaie periodePaie) {
        this.periodePaie = periodePaie;
    }

    @Override
    public String toString() {
        return "BulletinSpeciale{" +
                "id=" + id +
                ", matricule='" + matricule + '\'' +
                ", nomPrenom='" + nomPrenom + '\'' +
                ", calculer=" + calculer +
                ", cloture=" + cloture +
                ", avceAcpte=" + avceAcpte +
                ", mtavceAcpte='" + mtavceAcpte + '\'' +
                ", pretAlios=" + pretAlios +
                ", mtpretAlios='" + mtpretAlios + '\'' +
                ", regularisation=" + regularisation +
                ", mtregularisation='" + mtregularisation + '\'' +

                ", jourTravail=" + jourTravail +
                ", temptravail=" + temptravail +
                ", cumulNet=" + cumulNet +
                ", mtcumulNet='" + mtcumulNet + '\'' +
                ", netPayer=" + netPayer +
                ", mtnetPayer='" + mtnetPayer + '\'' +
                ", specialContract=" + specialContract +
                ", periodePaie=" + periodePaie +
                '}';
    }
}
