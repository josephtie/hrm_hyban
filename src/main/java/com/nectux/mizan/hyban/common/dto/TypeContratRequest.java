package com.nectux.mizan.hyban.common.dto;

public class TypeContratRequest extends PaginationRequest {
    private Long id;
    private String libelle;
    private String description;
    private Boolean actif;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
}
