package com.nectux.mizan.hyban.common.dto;

public class SoldeTousCompteRequest extends PaginationRequest {
    private Long banque;
    private Long banqueEmp;
    private Long periodePaieImpressionV;

    public Long getBanque() { return banque; }
    public void setBanque(Long banque) { this.banque = banque; }
    public Long getBanqueEmp() { return banqueEmp; }
    public void setBanqueEmp(Long banqueEmp) { this.banqueEmp = banqueEmp; }
    public Long getPeriodePaieImpressionV() { return periodePaieImpressionV; }
    public void setPeriodePaieImpressionV(Long periodePaieImpressionV) { this.periodePaieImpressionV = periodePaieImpressionV; }
}
