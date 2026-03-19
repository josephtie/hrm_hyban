package com.nectux.mizan.hyban.common.dto;

public class FormationPersonnelRequest {
    private Long id;
    private Long idFormation;
    private Long idPersonnel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdFormation() { return idFormation; }
    public void setIdFormation(Long idFormation) { this.idFormation = idFormation; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
}
