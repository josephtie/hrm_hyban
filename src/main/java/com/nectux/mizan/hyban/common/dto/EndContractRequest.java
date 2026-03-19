package com.nectux.mizan.hyban.common.dto;

public class EndContractRequest {
    private Long id;
    private String dateFin;
    private String dateMod;
    private Boolean permanent;
    private String ObservCtrat;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }
    public String getDateMod() { return dateMod; }
    public void setDateMod(String dateMod) { this.dateMod = dateMod; }
    public Boolean getPermanent() { return permanent; }
    public void setPermanent(Boolean permanent) { this.permanent = permanent; }
    public String getObservCtrat() { return ObservCtrat; }
    public void setObservCtrat(String observCtrat) { ObservCtrat = observCtrat; }
}
