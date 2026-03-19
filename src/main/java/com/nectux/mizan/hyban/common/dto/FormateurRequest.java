package com.nectux.mizan.hyban.common.dto;

public class FormateurRequest {
    private Long id;
    private Long cabinetFormationId;
    private String nom;
    private String civilite;
    private String sexe;
    private String situationMatrimonial;
    private String dateNaissance;
    private String lieuNaissance;
    private String fixe;
    private String mobile;

    public Long getId() { return id; }
    public Long getCabinetFormationId() { return cabinetFormationId; }
    public String getNom() { return nom; }
    public String getCivilite() { return civilite; }
    public String getSexe() { return sexe; }
    public String getSituationMatrimonial() { return situationMatrimonial; }
    public String getDateNaissance() { return dateNaissance; }
    public String getLieuNaissance() { return lieuNaissance; }
    public String getFixe() { return fixe; }
    public String getMobile() { return mobile; }

    public void setId(Long id) { this.id = id; }
    public void setCabinetFormationId(Long cabinetFormationId) { this.cabinetFormationId = cabinetFormationId; }
    public void setNom(String nom) { this.nom = nom; }
    public void setCivilite(String civilite) { this.civilite = civilite; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public void setSituationMatrimonial(String situationMatrimonial) { this.situationMatrimonial = situationMatrimonial; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
    public void setLieuNaissance(String lieuNaissance) { this.lieuNaissance = lieuNaissance; }
    public void setFixe(String fixe) { this.fixe = fixe; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}
