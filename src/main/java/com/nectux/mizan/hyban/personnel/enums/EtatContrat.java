package com.nectux.mizan.hyban.personnel.enums;

public enum EtatContrat {
    ACTIF("Contrat actif"),
    SUSPENDU("Contrat suspendu"),
    EXPIRE("Contrat a depassé\"  l'échéance"),
    RESILIE("Contrat résilié"),
    TERMINE("Contrat terminé"),
    EN_ATTENTE("Contrat en attente de validation"),
    INACTIF("Contrat inactif");

    private final String libelle;

    EtatContrat(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
