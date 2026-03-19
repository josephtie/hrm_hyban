package com.nectux.mizan.hyban.common.dto;

public class FactureFormationRequest {
    private Long id;
    private Long idCabinet;
    private Long idFormation;
    private String reference;
    private String emission;
    private Double montant;
    private Boolean statut;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCabinet() {
        return idCabinet;
    }

    public void setIdCabinet(Long idCabinet) {
        this.idCabinet = idCabinet;
    }

    public Long getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(Long idFormation) {
        this.idFormation = idFormation;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getEmission() {
        return emission;
    }

    public void setEmission(String emission) {
        this.emission = emission;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }
}
