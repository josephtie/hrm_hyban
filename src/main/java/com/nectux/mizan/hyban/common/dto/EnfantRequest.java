package com.nectux.mizan.hyban.common.dto;

public class EnfantRequest {
    private Long id;
    private Long idPersonnel;
    private String matricule;
    private String nom;
    private String dateNaissanceString;
    private String lieuNaissance;
    private String telephone;
    private Character sexe;
    private Boolean aCharge;
    private String ecole;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDateNaissanceString() { return dateNaissanceString; }
    public void setDateNaissanceString(String dateNaissanceString) { this.dateNaissanceString = dateNaissanceString; }
    public String getLieuNaissance() { return lieuNaissance; }
    public void setLieuNaissance(String lieuNaissance) { this.lieuNaissance = lieuNaissance; }
    public Character getSexe() { return sexe; }
    public void setSexe(Character sexe) { this.sexe = sexe; }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }



    public String getEcole() {
        return ecole;
    }



    public void setEcole(String ecole) {
        this.ecole = ecole;
    }

    public Boolean getaCharge() {
        return aCharge;
    }

    public void setaCharge(Boolean aCharge) {
        this.aCharge = aCharge;
    }
}
