package com.nectux.mizan.hyban.common.dto;

public class SanctionRequest extends PaginationRequest {
    private Long id;
    private Long idTypeSanction;
    private String faute;
    private String commentaire;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdTypeSanction() { return idTypeSanction; }
    public void setIdTypeSanction(Long idTypeSanction) { this.idTypeSanction = idTypeSanction; }
    public String getFaute() { return faute; }
    public void setFaute(String faute) { this.faute = faute; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
