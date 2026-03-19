package com.nectux.mizan.hyban.common.dto;

public class PromotionPersonnelRequest extends PaginationRequest {
    private Long id;
    private Long idPersonnel;
    private Long idPromotion;
    private String datePromotion;
    private String commentaire;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public Long getIdPromotion() { return idPromotion; }
    public void setIdPromotion(Long idPromotion) { this.idPromotion = idPromotion; }
    public String getDatePromotion() { return datePromotion; }
    public void setDatePromotion(String datePromotion) { this.datePromotion = datePromotion; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
