package com.nectux.mizan.hyban.personnel.enums;

public enum MotifClotureContrat {
    FIN_CDD("Fin de CDD"),
    DEMISSION("Démission"),
    LICENCIEMENT("Licenciement"),
    RETRAITE("Retraite"),
    DECES("Décès"),
    AUTRE("Autre");

    private final String libelle;

    MotifClotureContrat(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
