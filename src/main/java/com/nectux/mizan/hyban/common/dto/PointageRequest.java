package com.nectux.mizan.hyban.common.dto;

public class PointageRequest {
    private Long id;
    private Long idPersonnel;
    private String datePointage;
    private String heureArrivee;
    private String heureDepart;
    private String heurePause;
    private String heureReprise;
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public String getDatePointage() { return datePointage; }
    public void setDatePointage(String datePointage) { this.datePointage = datePointage; }
    public String getHeureArrivee() { return heureArrivee; }
    public void setHeureArrivee(String heureArrivee) { this.heureArrivee = heureArrivee; }
    public String getHeureDepart() { return heureDepart; }
    public void setHeureDepart(String heureDepart) { this.heureDepart = heureDepart; }
    public String getHeurePause() { return heurePause; }
    public void setHeurePause(String heurePause) { this.heurePause = heurePause; }
    public String getHeureReprise() { return heureReprise; }
    public void setHeureReprise(String heureReprise) { this.heureReprise = heureReprise; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
