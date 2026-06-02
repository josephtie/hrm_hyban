package com.nectux.mizan.hyban.common.dto;

import java.util.List;

/**
 * Requête pour l'enregistrement en lot d'une prime collective.
 * Permet de créer N PrimePersonnel en un seul appel/transaction.
 */
public class PrimeCollectiveBatchRequest {
    private Long idPrime;
    private Double montantop;
    private Long valeurop;
    private Long idPeriode;
    private List<Long> idPersonnels;
    private String commentaire;

    public Long getIdPrime() { return idPrime; }
    public void setIdPrime(Long idPrime) { this.idPrime = idPrime; }

    public Double getMontantop() { return montantop; }
    public void setMontantop(Double montantop) { this.montantop = montantop; }

    public Long getValeurop() { return valeurop; }
    public void setValeurop(Long valeurop) { this.valeurop = valeurop; }

    public Long getIdPeriode() { return idPeriode; }
    public void setIdPeriode(Long idPeriode) { this.idPeriode = idPeriode; }

    public List<Long> getIdPersonnels() { return idPersonnels; }
    public void setIdPersonnels(List<Long> idPersonnels) { this.idPersonnels = idPersonnels; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
