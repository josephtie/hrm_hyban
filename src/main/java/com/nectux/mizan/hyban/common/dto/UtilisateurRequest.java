package com.nectux.mizan.hyban.common.dto;

public class UtilisateurRequest {
    private Long id;
    private int idRole;
    private String nomComplet;
    private String dateNaissance;
    private String telephone;
    private String adresse;
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getIdRole() { return idRole; }
    public void setIdRole(int idRole) { this.idRole = idRole; }
    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }
    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
