package com.nectux.mizan.hyban.common.dto;

public class CpteVirmtBanqueRequest extends PaginationRequest {
    private Long id;
    private Long idbank;
    private Integer ribCpteVirmt;
    private String numguichCpteVirmt;
    private String numcpteCpteVirmt;
    private String donneurOrdCpteVirmt;
    private String codEtablVirmt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdbank() { return idbank; }
    public void setIdbank(Long idbank) { this.idbank = idbank; }
    public Integer getRibCpteVirmt() { return ribCpteVirmt; }
    public void setRibCpteVirmt(Integer ribCpteVirmt) { this.ribCpteVirmt = ribCpteVirmt; }
    public String getNumguichCpteVirmt() { return numguichCpteVirmt; }
    public void setNumguichCpteVirmt(String numguichCpteVirmt) { this.numguichCpteVirmt = numguichCpteVirmt; }
    public String getNumcpteCpteVirmt() { return numcpteCpteVirmt; }
    public void setNumcpteCpteVirmt(String numcpteCpteVirmt) { this.numcpteCpteVirmt = numcpteCpteVirmt; }
    public String getDonneurOrdCpteVirmt() { return donneurOrdCpteVirmt; }
    public void setDonneurOrdCpteVirmt(String donneurOrdCpteVirmt) { this.donneurOrdCpteVirmt = donneurOrdCpteVirmt; }
    public String getCodEtablVirmt() { return codEtablVirmt; }
    public void setCodEtablVirmt(String codEtablVirmt) { this.codEtablVirmt = codEtablVirmt; }
}
