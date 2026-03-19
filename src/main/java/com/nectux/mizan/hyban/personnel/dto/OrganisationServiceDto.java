package com.nectux.mizan.hyban.personnel.dto;

import java.time.LocalDateTime;

public class OrganisationServiceDto {
    private Long id;
    private String libelle;
    private String type; // "DIRECTION", "DEPARTEMENT", "SERVICE"
    private Long idDirection;
    private Long idDepartement;
    private Boolean active;
    private String createdAt;
    
    // Données hiérarchiques pour la vue
    private OrganisationServiceDto direction;
    private OrganisationServiceDto departement;
    
    public OrganisationServiceDto() {}
    
    public OrganisationServiceDto(Long id, String libelle, String type) {
        this.id = id;
        this.libelle = libelle;
        this.type = type;
        this.active = true;
        this.createdAt = LocalDateTime.now().toString();
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Long getIdDirection() { return idDirection; }
    public void setIdDirection(Long idDirection) { this.idDirection = idDirection; }
    
    public Long getIdDepartement() { return idDepartement; }
    public void setIdDepartement(Long idDepartement) { this.idDepartement = idDepartement; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public OrganisationServiceDto getDirection() { return direction; }
    public void setDirection(OrganisationServiceDto direction) { this.direction = direction; }
    
    public OrganisationServiceDto getDepartement() { return departement; }
    public void setDepartement(OrganisationServiceDto departement) { this.departement = departement; }
}
