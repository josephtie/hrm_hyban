package com.nectux.mizan.hyban.common.dto;

public class ContratPersonnelFilterRequest {
    private Integer offset;
    private Integer limit;
    private String search;
    private String statut; // "active", "inactive", "expiresOnDate", "expiresInPeriod"
    private String typeContrat; // "CDI", "CDD", "Stage", "Apprentissage"
    private String salaireRange; // "low", "medium", "high", "veryhigh"
    private Boolean carec; // État contractuel de l'employé
    private String expireDate; // Date d'expiration spécifique (YYYY-MM-DD)
    private String expirePeriodStart; // Début de période d'expiration (YYYY-MM-DD)
    private String expirePeriodEnd; // Fin de période d'expiration (YYYY-MM-DD)

    public ContratPersonnelFilterRequest() {}

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

    public String getSalaireRange() {
        return salaireRange;
    }

    public void setSalaireRange(String salaireRange) {
        this.salaireRange = salaireRange;
    }

    public Boolean getCarec() {
        return carec;
    }

    public void setCarec(Boolean carec) {
        this.carec = carec;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getExpirePeriodStart() {
        return expirePeriodStart;
    }

    public void setExpirePeriodStart(String expirePeriodStart) {
        this.expirePeriodStart = expirePeriodStart;
    }

    public String getExpirePeriodEnd() {
        return expirePeriodEnd;
    }

    public void setExpirePeriodEnd(String expirePeriodEnd) {
        this.expirePeriodEnd = expirePeriodEnd;
    }
}
