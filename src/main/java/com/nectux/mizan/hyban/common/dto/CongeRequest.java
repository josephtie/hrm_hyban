package com.nectux.mizan.hyban.common.dto;

public class CongeRequest extends PaginationRequest {
    private Long id;
    private Long periodepaie;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPeriodepaie() { return periodepaie; }
    public void setPeriodepaie(Long periodepaie) { this.periodepaie = periodepaie; }
}
