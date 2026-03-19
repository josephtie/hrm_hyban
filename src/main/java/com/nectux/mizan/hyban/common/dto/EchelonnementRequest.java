package com.nectux.mizan.hyban.common.dto;

public class EchelonnementRequest extends PaginationRequest {
    private Long idechel;
    private Long periodPaie;
    private Long idpretperso;

    public Long getIdechel() { return idechel; }
    public void setIdechel(Long idechel) { this.idechel = idechel; }
    public Long getPeriodPaie() { return periodPaie; }
    public void setPeriodPaie(Long periodPaie) { this.periodPaie = periodPaie; }
    public Long getIdpretperso() { return idpretperso; }
    public void setIdpretperso(Long idpretperso) { this.idpretperso = idpretperso; }
}
