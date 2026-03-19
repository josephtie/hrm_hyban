package com.nectux.mizan.hyban.common.dto;

import java.util.List;

public class PointageSearchRequest {
    private Integer limit;
    private Integer offset;
    private String datePointage;
    private String datePointage1;
    private Long idPersonnel;
    private List<Long> listPersonnel;
    private String search;

    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public Integer getOffset() { return offset; }
    public void setOffset(Integer offset) { this.offset = offset; }
    public String getDatePointage() { return datePointage; }
    public void setDatePointage(String datePointage) { this.datePointage = datePointage; }
    public String getDatePointage1() { return datePointage1; }
    public void setDatePointage1(String datePointage1) { this.datePointage1 = datePointage1; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public List<Long> getListPersonnel() { return listPersonnel; }
    public void setListPersonnel(List<Long> listPersonnel) { this.listPersonnel = listPersonnel; }
    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }
}
