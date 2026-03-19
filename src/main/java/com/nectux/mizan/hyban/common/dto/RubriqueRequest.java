package com.nectux.mizan.hyban.common.dto;

public class RubriqueRequest extends PaginationRequest {
    private Long idR;
    private String code;
    private String libelle;
    private String type; // Pour recevoir le type du frontend (1-6)
    private Integer etatImposition;
    private String modeCalcul;
    private Double valeurDef;
    private Boolean cotisable;
    private Boolean active;
    private Boolean permanent;
    private Boolean speciale;
    private String description;
    private String categorie;
    private String formule;
    private Double montant;
    private Double taux;
    private Double mtExedent;
    private Boolean imposable;
    private Boolean afficherBulletin;

    // Getters et setters existants
    public Long getIdR() { return idR; }
    public void setIdR(Long idR) { this.idR = idR; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Integer getEtatImposition() { return etatImposition; }
    public void setEtatImposition(Integer etatImposition) { this.etatImposition = etatImposition; }
    
    public String getModeCalcul() { return modeCalcul; }
    public void setModeCalcul(String modeCalcul) { this.modeCalcul = modeCalcul; }
    
    public Double getValeurDef() { return valeurDef; }
    public void setValeurDef(Double valeurDef) { this.valeurDef = valeurDef; }
    
    public Boolean getCotisable() { return cotisable; }
    public void setCotisable(Boolean cotisable) { this.cotisable = cotisable; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public Boolean getPermanent() { return permanent; }
    public void setPermanent(Boolean permanent) { this.permanent = permanent; }
    
    public Boolean getSpeciale() { return speciale; }
    public void setSpeciale(Boolean speciale) { this.speciale = speciale; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    
    public String getFormule() { return formule; }
    public void setFormule(String formule) { this.formule = formule; }
    
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    
    public Double getTaux() { return taux; }
    public void setTaux(Double taux) { this.taux = taux; }
    
    public Double getMtExedent() { return mtExedent; }
    public void setMtExedent(Double mtExedent) { this.mtExedent = mtExedent; }
    
    public Boolean getImposable() { return imposable; }
    public void setImposable(Boolean imposable) { this.imposable = imposable; }
    
    public Boolean getAfficherBulletin() { return afficherBulletin; }
    public void setAfficherBulletin(Boolean afficherBulletin) { this.afficherBulletin = afficherBulletin; }
}
