package com.nectux.mizan.hyban.personnel.entity;

import com.nectux.mizan.hyban.personnel.enums.TypeOperationContrat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "CGECI_RHPAIE_CONTRAT_HISTORY")
@SequenceGenerator(name = "CGECI_RHPAIE_CONTRAT_HISTORY_SEQUENCE", sequenceName = "CGECI_RHPAIE_CONTRAT_HISTORY_SEQ", initialValue = 1, allocationSize = 1)
public class ContratHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CGECI_RHPAIE_CONTRAT_HISTORY_SEQUENCE")
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long contratId;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private TypeOperationContrat typeOperation;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String utilisateur;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOperation;

    public ContratHistory() {
    }

    public ContratHistory(Long contratId, TypeOperationContrat typeOperation, String description, String utilisateur) {
        this.contratId = contratId;
        this.typeOperation = typeOperation;
        this.description = description;
        this.utilisateur = utilisateur;
        this.dateOperation = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContratId() {
        return contratId;
    }

    public void setContratId(Long contratId) {
        this.contratId = contratId;
    }

    public TypeOperationContrat getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOperationContrat typeOperation) {
        this.typeOperation = typeOperation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Date getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }

    @Override
    public String toString() {
        return "ContratHistory{" +
                "id=" + id +
                ", contratId=" + contratId +
                ", typeOperation=" + typeOperation +
                ", description='" + description + '\'' +
                ", utilisateur='" + utilisateur + '\'' +
                ", dateOperation=" + dateOperation +
                '}';
    }
}
