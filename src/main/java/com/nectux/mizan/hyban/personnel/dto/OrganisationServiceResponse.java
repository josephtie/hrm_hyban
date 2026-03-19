package com.nectux.mizan.hyban.personnel.dto;

import java.util.List;

public class OrganisationServiceResponse {
    private List<OrganisationServiceDto> rows;
    private int total;
    private String result;
    private String message;
    
    public OrganisationServiceResponse() {}
    
    public OrganisationServiceResponse(List<OrganisationServiceDto> rows, int total, String result) {
        this.rows = rows;
        this.total = total;
        this.result = result;
        this.message = "Services retrieved successfully";
    }
    
    // Getters et Setters
    public List<OrganisationServiceDto> getRows() { return rows; }
    public void setRows(List<OrganisationServiceDto> rows) { this.rows = rows; }
    
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
