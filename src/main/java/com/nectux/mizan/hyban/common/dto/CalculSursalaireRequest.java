package com.nectux.mizan.hyban.common.dto;

public class CalculSursalaireRequest {
    private Long idPersonnel;
    private Double netApayer;

    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public Double getNetApayer() { return netApayer; }
    public void setNetApayer(Double netApayer) { this.netApayer = netApayer; }
}
