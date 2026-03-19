package com.nectux.mizan.hyban.parametrages.dto;

import com.nectux.mizan.hyban.parametrages.entity.Societe;
import java.util.Date;

public class SocieteVueDTO {
    private Long id;
    private String raisonSociale;
    private String sigle;
    private String rccm;
    private String compteContribuable;
    private String telephone;
    private String email;
    private String adresse;
    private Boolean principale;
    private Date dateCreation;
    private String urlLogo;

    // Constructeurs
    public SocieteVueDTO() {}

    public SocieteVueDTO(Societe societe) {
        this.id = societe.getId();
        this.raisonSociale = societe.getRaisonsoc();
        this.sigle = societe.getSigle();
        this.rccm = societe.getCpteContrib(); // Utiliser cpteContrib comme RCCM
        this.compteContribuable = societe.getCpteContrib();
        this.telephone = societe.getTelephone();
        this.email = ""; // Pas de champ email dans l'entité
        this.adresse = societe.getAdress();
        this.principale = societe.getGratification() != null && societe.getGratification() == 1L;
        this.urlLogo = societe.getUrlLogo();
        // dateCreation peut être ajouté depuis l'entité mère si elle existe
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRaisonSociale() { return raisonSociale; }
    public void setRaisonSociale(String raisonSociale) { this.raisonSociale = raisonSociale; }
    public String getSigle() { return sigle; }
    public void setSigle(String sigle) { this.sigle = sigle; }
    public String getRccm() { return rccm; }
    public void setRccm(String rccm) { this.rccm = rccm; }
    public String getCompteContribuable() { return compteContribuable; }
    public void setCompteContribuable(String compteContribuable) { this.compteContribuable = compteContribuable; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public Boolean getPrincipale() { return principale; }
    public void setPrincipale(Boolean principale) { this.principale = principale; }
    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    public String getUrlLogo() { return urlLogo; }
    public void setUrlLogo(String urlLogo) { this.urlLogo = urlLogo; }

    // Méthode pour convertir vers l'entité
    public Societe toEntity() {
        Societe societe = new Societe();
        societe.setId(this.id);
        societe.setRaisonsoc(this.raisonSociale);
        societe.setSigle(this.sigle);
        societe.setCpteContrib(this.compteContribuable);
        societe.setTelephone(this.telephone);
        societe.setAdress(this.adresse);
        societe.setGratification(this.principale != null && this.principale ? 1L : 0L);
        societe.setUrlLogo(this.urlLogo);
        return societe;
    }
}
