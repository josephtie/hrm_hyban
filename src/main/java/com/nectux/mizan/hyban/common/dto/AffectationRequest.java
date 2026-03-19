package com.nectux.mizan.hyban.common.dto;

public class AffectationRequest extends PaginationRequest {
    private Long id;
    private Long idPersonnel;
    private Long idPoste;
    private Long idSite;
    private Boolean statutAffect;
    private String dateDebut;
    private String dateFin;
    private String observation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public Long getIdPoste() { return idPoste; }
    public void setIdPoste(Long idPoste) { this.idPoste = idPoste; }
    public Long getIdSite() { return idSite; }
    public void setIdSite(Long idSite) { this.idSite = idSite; }
    public Boolean getStatutAffect() { return statutAffect; }
    public void setStatutAffect(Boolean statutAffect) { this.statutAffect = statutAffect; }
    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
}
