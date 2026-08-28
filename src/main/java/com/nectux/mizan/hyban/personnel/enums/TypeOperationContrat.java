package com.nectux.mizan.hyban.personnel.enums;

public enum TypeOperationContrat {
    CREATION("Création"),
    MODIFICATION("Modification"),
    RENOUVELLEMENT("Renouvellement"),
    AVENANT("Avenant"),
    CLOTURE("Clôture"),
    SUSPENSION("Suspension"),
    RESILIATION("Résiliation"),
    REPRISE("Reprise");

    private final String libelle;

    TypeOperationContrat(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}