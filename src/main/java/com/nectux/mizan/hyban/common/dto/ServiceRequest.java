package com.nectux.mizan.hyban.common.dto;

public class ServiceRequest {
    private Long id;
    private String libelle;
    private Long idDepartement;
    private Long idDirection;
    private Long idTypeService;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public Long getIdDepartement() { return idDepartement; }
    public void setIdDepartement(Long idDepartement) { this.idDepartement = idDepartement; }
    public Long getIdDirection() { return idDirection; }
    public void setIdDirection(Long idDirection) { this.idDirection = idDirection; }
    public Long getIdTypeService() { return idTypeService; }
    public void setIdTypeService(Long idTypeService) { this.idTypeService = idTypeService; }
}
