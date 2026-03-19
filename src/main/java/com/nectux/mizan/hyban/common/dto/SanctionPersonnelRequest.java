package com.nectux.mizan.hyban.common.dto;

public class SanctionPersonnelRequest extends PaginationRequest {
    private Long id;
    private Long idPersonnel;
    private Long idSanction;
    private String dateDebut;
    private String dateFin;
    private String observation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public Long getIdSanction() { return idSanction; }
    public void setIdSanction(Long idSanction) { this.idSanction = idSanction; }
    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
}
