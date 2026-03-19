package com.nectux.mizan.hyban.common.dto;

public class BanqueRequest extends PaginationRequest {
    private Long id;
    private String sigle;
    private String libelle;
    private String codbanq;
    private Boolean statut;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSigle() { return sigle; }
    public void setSigle(String sigle) { this.sigle = sigle; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public String getCodbanq() { return codbanq; }
    public void setCodbanq(String codbanq) { this.codbanq = codbanq; }
    public Boolean getStatut() { return statut; }
    public void setStatut(Boolean statut) { this.statut = statut; }
}
