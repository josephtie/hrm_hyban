package com.nectux.mizan.hyban.common.dto;

public class BulletinPaieRequest extends PaginationRequest {
    private Long idPeriod;
    private String search1;
    private Long idPersonnel;
    private Double netApayer;

    public Long getIdPeriod() { return idPeriod; }
    public void setIdPeriod(Long idPeriod) { this.idPeriod = idPeriod; }
    public String getSearch1() { return search1; }
    public void setSearch1(String search1) { this.search1 = search1; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public Double getNetApayer() { return netApayer; }
    public void setNetApayer(Double netApayer) { this.netApayer = netApayer; }
}
