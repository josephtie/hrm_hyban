package com.nectux.mizan.hyban.common.dto;

public class PlanningCongeRequest {
    private Long id;
    private String dateDepartString;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDateDepartString() { return dateDepartString; }
    public void setDateDepartString(String dateDepartString) { this.dateDepartString = dateDepartString; }
}
