package com.nectux.mizan.hyban.common.dto;

public class ThemeRequest {
    private Long id;
    private String intitule;
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
