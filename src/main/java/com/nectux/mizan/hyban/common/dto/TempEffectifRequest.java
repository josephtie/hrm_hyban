package com.nectux.mizan.hyban.common.dto;

public class TempEffectifRequest extends PaginationRequest {
    private Double temptravail;
    private Double jourtravail;
    private Long idPers;
    private Long idPeriodDep;

    public Double getTemptravail() { return temptravail; }
    public void setTemptravail(Double temptravail) { this.temptravail = temptravail; }
    public Double getJourtravail() { return jourtravail; }
    public void setJourtravail(Double jourtravail) { this.jourtravail = jourtravail; }
    public Long getIdPers() { return idPers; }
    public void setIdPers(Long idPers) { this.idPers = idPers; }
    public Long getIdPeriodDep() { return idPeriodDep; }
    public void setIdPeriodDep(Long idPeriodDep) { this.idPeriodDep = idPeriodDep; }
}
