package com.nectux.mizan.hyban.common.dto;

public class PersonnelVueRequest extends PaginationRequest {
    private Long id;
    private String nom;
    private String prenom;
    private Long nationalite;
    private Long service;
    private Long categorie;
    private Long fonction;
    private Long typeContrat;
    private String matricule;
    private String sexe;
    private String dateNaissance;
    private String lieuNaissance;
    private String email;
    private String residence;
    private Integer situationMatrimoniale;
    private Integer nombreEnfant;
    private String dateEmbauche;
    private String numeroCnps;
    private String telephone;
    private String adresse;
    private Boolean actif;
    
    // Champs pour les filtres
    private String search;
    private String serviceFilter;
    private String statut;
    private String modePaiement;
    private String fonctionFilter;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public Long getNationalite() { return nationalite; }
    public void setNationalite(Long nationalite) { this.nationalite = nationalite; }
    public Long getService() { return service; }
    public void setService(Long service) { this.service = service; }
    public Long getCategorie() { return categorie; }
    public void setCategorie(Long categorie) { this.categorie = categorie; }
    public Long getFonction() { return fonction; }
    public void setFonction(Long fonction) { this.fonction = fonction; }
    public Long getTypeContrat() { return typeContrat; }
    public void setTypeContrat(Long typeContrat) { this.typeContrat = typeContrat; }
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getLieuNaissance() { return lieuNaissance; }
    public void setLieuNaissance(String lieuNaissance) { this.lieuNaissance = lieuNaissance; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getResidence() { return residence; }
    public void setResidence(String residence) { this.residence = residence; }
    public Integer getSituationMatrimoniale() { return situationMatrimoniale; }
    public void setSituationMatrimoniale(Integer situationMatrimoniale) { this.situationMatrimoniale = situationMatrimoniale; }
    public Integer getNombreEnfant() { return nombreEnfant; }
    public void setNombreEnfant(Integer nombreEnfant) { this.nombreEnfant = nombreEnfant; }
    public String getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(String dateEmbauche) { this.dateEmbauche = dateEmbauche; }
    public String getNumeroCnps() { return numeroCnps; }
    public void setNumeroCnps(String numeroCnps) { this.numeroCnps = numeroCnps; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    // Getters/Setters pour les filtres
    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }
    public String getServiceFilter() { return serviceFilter; }
    public void setServiceFilter(String service) { this.serviceFilter = service; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    public String getFonctionFilter() { return fonctionFilter; }
    public void setFonctionFilter(String fonction) { this.fonctionFilter = fonction; }
}
