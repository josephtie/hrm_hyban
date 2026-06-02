package com.nectux.mizan.hyban.personnel.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * Requête JSON pour la modification rapide d'un personnel
 * (situation matrimoniale, nombre d'enfants, statut actif/sommeil).
 *
 * Utilisée par l'endpoint REST PUT /api/personnels/personnel/editerpersonnel
 */
@Data
public class EditerPersonnelRequest implements Serializable {
    private Long idPersonnel;
    private Integer situationMatrimoniale;
    private Integer nombreEnfant;
    /** true = actif, false = en sommeil */
    private Boolean statut;
}
