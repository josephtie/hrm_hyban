package com.nectux.mizan.hyban.common.dto;

public class ChangePasswordRequest {
    private Long id;
    private String ancien;
    private String nouveau;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAncien() { return ancien; }
    public void setAncien(String ancien) { this.ancien = ancien; }
    public String getNouveau() { return nouveau; }
    public void setNouveau(String nouveau) { this.nouveau = nouveau; }
}
