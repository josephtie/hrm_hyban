package com.nectux.mizan.hyban.common.dto;

public class AbsencesRequest extends PaginationRequest {
    private Long id;
    private String faute;
    private String type;
    private String commentaire;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFaute() { return faute; }
    public void setFaute(String faute) { this.faute = faute; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
