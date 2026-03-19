package com.nectux.mizan.hyban.common.dto;

public class StockCongeRequest extends PaginationRequest {
    private Long id;
    private Long idConge;
    private String dateDepart;
    private String dateRetour;
    private Double montantVerse;
    private Long idPersonnel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdConge() { return idConge; }
    public void setIdConge(Long idConge) { this.idConge = idConge; }
    public String getDateDepart() { return dateDepart; }
    public void setDateDepart(String dateDepart) { this.dateDepart = dateDepart; }
    public String getDateRetour() { return dateRetour; }
    public void setDateRetour(String dateRetour) { this.dateRetour = dateRetour; }
    public Double getMontantVerse() { return montantVerse; }
    public void setMontantVerse(Double montantVerse) { this.montantVerse = montantVerse; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
}
