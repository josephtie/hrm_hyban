package com.nectux.mizan.hyban.common.dto;

public class AbsencesPersonnelRequest {
    private Long id;
    private Long idPersonnel;
    private Long idAbsence;
    private String dateDebut;
    private String dateFin;
    private Double heursabsence;
    private Double joursabsence;
    private String observation;
    private Boolean statut;
    private Integer sanctionsalaire;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public Long getIdAbsence() { return idAbsence; }
    public void setIdAbsence(Long idAbsence) { this.idAbsence = idAbsence; }
    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }
    public Double getHeursabsence() { return heursabsence; }
    public void setHeursabsence(Double heursabsence) { this.heursabsence = heursabsence; }
    public Double getJoursabsence() { return joursabsence; }
    public void setJoursabsence(Double joursabsence) { this.joursabsence = joursabsence; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
    public Boolean getStatut() { return statut; }
    public void setStatut(Boolean statut) { this.statut = statut; }
    public Integer getSanctionsalaire() { return sanctionsalaire; }
    public void setSanctionsalaire(Integer sanctionsalaire) { this.sanctionsalaire = sanctionsalaire; }
}
