package com.nectux.mizan.hyban.common.dto;

public class ContratPersonnelRequest {
    private Long id;
    private Long idPersonnel;
    private Long idCategorie;
    private Long idFonction;
    private Long idTypeContrat;
    private String dateDebut;
    private String dateFin;
    private Double netAPayer;
    private Double indemniteLogement;
    private int ancienete;
    private Double sursalaire;
    private Double indemnitetransport;
    private Double indemniterespons;
    private Double indemniterepresent;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public Long getIdCategorie() { return idCategorie; }
    public void setIdCategorie(Long idCategorie) { this.idCategorie = idCategorie; }
    public Long getIdFonction() { return idFonction; }
    public void setIdFonction(Long idFonction) { this.idFonction = idFonction; }
    public Long getIdTypeContrat() { return idTypeContrat; }
    public void setIdTypeContrat(Long idTypeContrat) { this.idTypeContrat = idTypeContrat; }
    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }
    public Double getNetAPayer() { return netAPayer; }
    public void setNetAPayer(Double netAPayer) { this.netAPayer = netAPayer; }
    public Double getIndemniteLogement() { return indemniteLogement; }
    public void setIndemniteLogement(Double indemniteLogement) { this.indemniteLogement = indemniteLogement; }
    public int getAncienete() { return ancienete; }
    public void setAncienete(int ancienete) { this.ancienete = ancienete; }
    public Double getSursalaire() { return sursalaire; }
    public void setSursalaire(Double sursalaire) { this.sursalaire = sursalaire; }
    public Double getIndemnitetransport() { return indemnitetransport; }
    public void setIndemnitetransport(Double indemnitetransport) { this.indemnitetransport = indemnitetransport; }
    public Double getIndemniterespons() { return indemniterespons; }
    public void setIndemniterespons(Double indemniterespons) { this.indemniterespons = indemniterespons; }
    public Double getIndemniterepresent() { return indemniterepresent; }
    public void setIndemniterepresent(Double indemniterepresent) { this.indemniterepresent = indemniterepresent; }
}
