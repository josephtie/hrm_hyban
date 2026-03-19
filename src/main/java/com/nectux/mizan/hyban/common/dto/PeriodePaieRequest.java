package com.nectux.mizan.hyban.common.dto;

public class PeriodePaieRequest extends PaginationRequest {
    private Long id;
    private String libelle;
    private Long mois;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public Long getMois() { return mois; }
    public void setMois(Long mois) { this.mois = mois; }
}
