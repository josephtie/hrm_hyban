package com.nectux.mizan.hyban.common.dto;

public class DisaRequest extends PaginationRequest {
    private Long periode;
    private Long annee;
    private String print;

    public Long getPeriode() { return periode; }
    public void setPeriode(Long periode) { this.periode = periode; }
    public Long getAnnee() { return annee; }
    public void setAnnee(Long annee) { this.annee = annee; }
    public String getPrint() { return print; }
    public void setPrint(String print) { this.print = print; }
}
