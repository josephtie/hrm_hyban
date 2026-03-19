package com.nectux.mizan.hyban.common.dto;

public class MvtCongeRequest extends PaginationRequest {
    private Long id;
    private Long idPersonnel;
    private String dateDepart;
    private String dateRetour;
    private Double montantVerse;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public String getDateDepart() { return dateDepart; }
    public void setDateDepart(String dateDepart) { this.dateDepart = dateDepart; }
    public String getDateRetour() { return dateRetour; }
    public void setDateRetour(String dateRetour) { this.dateRetour = dateRetour; }
    public Double getMontantVerse() { return montantVerse; }
    public void setMontantVerse(Double montantVerse) { this.montantVerse = montantVerse; }
}
