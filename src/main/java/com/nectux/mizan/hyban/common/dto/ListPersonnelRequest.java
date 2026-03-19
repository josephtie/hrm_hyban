package com.nectux.mizan.hyban.common.dto;

public class ListPersonnelRequest {
    private Integer listSize;
    private String listPersonnel;
    private Long id;

    public Integer getListSize() { return listSize; }
    public void setListSize(Integer listSize) { this.listSize = listSize; }
    public String getListPersonnel() { return listPersonnel; }
    public void setListPersonnel(String listPersonnel) { this.listPersonnel = listPersonnel; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
