# Adaptation des Champs Rubriques - Vue, Backend, Service

## Objectif
Adapter les trois couches (Vue, Backend, Service) pour prendre en compte les nouveaux champs du formulaire RubriquesView.vue

## Changements dans la Vue RubriquesView.vue

### Anciens champs supprimés :
```vue
<!-- Anciens champs radio buttons -->
<el-radio-group v-model="form.imposable" size="large">
  <el-radio :value="true">Oui</el-radio>
  <el-radio :value="false">Non</el-radio>
</el-radio-group>

<el-radio-group v-model="form.cotisable" size="large">
  <el-radio :value="true">Oui</el-radio>
  <el-radio :value="false">Non</el-radio>
</el-radio-group>

<el-radio-group v-model="form.active" size="large">
  <el-radio :value="true">Oui</el-radio>
  <el-radio :value="false">Non</el-radio>
</el-radio-group>
```

### Nouveaux champs ajoutés :
```vue
<!-- Nature de la rubrique -->
<el-select v-model="form.typeRubrique" placeholder="Nature" size="large">
  <el-option label="Gain (Salaire, prime...)" value="GAIN" />
  <el-option label="Retenue (CNPS, impôt...)" value="RETENUE" />
  <el-option label="Charge (Part patronale)" value="CHARGE" />
</el-select>

<!-- Catégorie -->
<el-select v-model="form.categorie" size="large">
  <el-option label="Salaire de base" value="BASE" />
  <el-option label="Prime" value="PRIME" />
  <el-option label="Indemnité" value="INDEMNITE" />
  <el-option label="Cotisation sociale" value="COTISATION" />
  <el-option label="Impôt" value="IMPOT" />
</el-select>

<!-- Imposition -->
<el-select v-model="form.typeImposition" size="large">
  <el-option label="Totalement imposable" value="TOTAL" />
  <el-option label="Non imposable" value="NONE" />
  <el-option label="Partiellement imposable" value="PARTIEL" />
</el-select>

<!-- Champs conditionnels selon modeCalcul -->
<div class="form-group" v-if="form.modeCalcul === 'MONTANT'">
  <label class="form-label">Montant</label>
  <el-input v-model="form.montant" type="number" />
</div>

<div class="form-group" v-if="form.modeCalcul === 'TAUX'">
  <label class="form-label">Taux (%)</label>
  <el-input v-model="form.taux" type="number" />
</div>

<div class="form-group" v-if="form.modeCalcul === 'FORMULE'">
  <label class="form-label">Formule</label>
  <el-input v-model="form.formule" placeholder="ex: BASE * 0.1" />
</div>

<!-- Switchs -->
<el-switch v-model="form.afficherBulletin" />
<el-switch v-model="form.active" />
```

### Variables du formulaire mises à jour :
```typescript
const form = reactive({
  id: 0,
  code: '',
  libelle: '',
  typeRubrique: '', // NOUVEAU
  categorie: 'GAIN', // NOUVEAU
  modeCalcul: '',
  valeur: 0,
  montant: null, // NOUVEAU
  taux: null, // NOUVEAU
  formule: '', // NOUVEAU
  typeImposition: '', // NOUVEAU
  afficherBulletin: true, // NOUVEAU
  active: true,
  description: '',
  mtExedent: 0,
  imposable: true,
  cotisable: true
})
```

## Backend - DTO RubriqueRequest

### Champ ajouté :
```java
private String typeImposition; // TOTAL, NONE, PARTIEL

// Getters et setters
public String getTypeImposition() { return typeImposition; }
public void setTypeImposition(String typeImposition) { this.typeImposition = typeImposition; }
```

## Backend - Contrôleur RubriqueRestController

### Méthode saveRubrique mise à jour :
```java
@PostMapping("/enregistrer")
public ResponseEntity<RubriqueDTO> saveRubrique(@RequestBody RubriqueRequest request) {
    try {
        logger.info("Enregistrement rubrique avec les nouvelles données: {}", request);
        
        // Conversion du typeImposition vers etatImposition (Integer)
        Integer etatImposition = 1; // Valeur par défaut
        if (request.getTypeImposition() != null && !request.getTypeImposition().isEmpty()) {
            String typeImpos = request.getTypeImposition().toUpperCase();
            switch (typeImpos) {
                case "TOTAL":
                    etatImposition = 1;
                    break;
                case "NONE":
                    etatImposition = 2;
                    break;
                case "PARTIEL":
                    etatImposition = 3;
                    break;
                default:
                    etatImposition = 1;
            }
        }
        
        // Utilisation de la méthode complète avec tous les nouveaux champs
        RubriqueDTO result = rubriqueService.saveRubriqueComplete(
            request.getIdR(),
            request.getCode(),
            request.getLibelle(),
            etatImposition,
            request.getModeCalcul(),
            request.getValeurDef(),
            request.getCotisable(),
            request.getActive(),
            request.getPermanent(),
            request.getSpeciale(),
            request.getDescription(),
            request.getCategorie(), // NOUVEAU
            request.getFormule(), // NOUVEAU
            request.getMontant(), // NOUVEAU
            request.getTaux(), // NOUVEAU
            request.getMtExedent(),
            request.getImposable(),
            request.getTypeImposition(), // NOUVEAU
            request.getAfficherBulletin() // NOUVEAU
        );
        return ResponseEntity.ok(result);
    } catch (Exception e) {
        logger.error("Erreur lors de l'enregistrement de la rubrique", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
```

## Frontend - Service rubriquerest.service.ts

### Interface RubriqueRestDto mise à jour :
```typescript
export interface RubriqueRestDto {
  id?: number
  code?: string
  libelle?: string
  typeRubrique?: string // NOUVEAU
  categorie?: string // NOUVEAU
  modeCalcul?: string
  valeur?: number
  montant?: number // NOUVEAU
  taux?: number // NOUVEAU
  formule?: string // NOUVEAU
  typeImposition?: string // NOUVEAU
  afficherBulletin?: boolean // NOUVEAU
  active?: boolean
  description?: string
  mtExedent?: number
  imposable?: boolean
  cotisable?: boolean
  dateCreation?: string
}
```

### Méthode create mise à jour :
```typescript
async create(data: any): Promise<ApiResponse<RubriqueRestDto>> {
  try {
    // Préparation des données pour le backend avec les nouveaux champs
    const rubriqueRequest = {
      idR: null,
      code: data.code,
      libelle: data.libelle,
      typeRubrique: data.typeRubrique, // NOUVEAU
      categorie: data.categorie, // NOUVEAU
      modeCalcul: data.modeCalcul,
      valeur: data.valeur,
      montant: data.montant, // NOUVEAU
      taux: data.taux, // NOUVEAU
      formule: data.formule, // NOUVEAU
      typeImposition: data.typeImposition, // NOUVEAU
      afficherBulletin: data.afficherBulletin, // NOUVEAU
      active: data.active,
      description: data.description,
      mtExedent: data.mtExedent,
      imposable: data.imposable,
      cotisable: data.cotisable
    }
    
    const response = await api.post('/api/parametrages/rubriques/enregistrer', rubriqueRequest)
    
    return {
      success: true,
      data: response.data,
      message: 'Rubrique created successfully'
    }
  } catch (error: any) {
    return {
      success: false,
      data: {} as RubriqueRestDto,
      message: error.response?.data?.message || 'Failed to create rubrique'
    }
  }
}
```

## Mapping des Champs

### Vue vers Backend :
| Vue (form) | Backend (RubriqueRequest) | Description |
|------------|---------------------------|-------------|
| `form.typeRubrique` | `typeRubrique` | Nature de la rubrique (GAIN/RETENUE/CHARGE) |
| `form.categorie` | `categorie` | Catégorie (BASE/PRIME/INDEMNITE/etc) |
| `form.typeImposition` | `typeImposition` | Type d'imposition (TOTAL/NONE/PARTIEL) |
| `form.montant` | `montant` | Montant fixe |
| `form.taux` | `taux` | Taux en pourcentage |
| `form.formule` | `formule` | Formule de calcul |
| `form.afficherBulletin` | `afficherBulletin` | Affichage sur bulletin |

### Conversion Backend :
| Backend (typeImposition) | Backend (etatImposition) | Logique |
|-------------------------|-------------------------|---------|
| "TOTAL" | 1 | Totalement imposable |
| "NONE" | 2 | Non imposable |
| "PARTIEL" | 3 | Partiellement imposable |

## Endpoints Utilisés

### Frontend vers Backend :
- **Création** : `POST /api/parametrages/rubriques/enregistrer`
- **Modification** : `POST /api/parametrages/rubriques/modifier`
- **Suppression** : `POST /api/parametrages/rubriques/supprimer`
- **Liste** : `POST /api/parametrages/rubriques/lister`

## Validation et Tests

### Tests à effectuer :
1. **Création rubrique** : Vérifier que tous les nouveaux champs sont bien envoyés
2. **Modification rubrique** : Vérifier que les champs sont bien mis à jour
3. **Champs conditionnels** : Vérifier l'affichage selon modeCalcul
4. **Conversion typeImposition** : Vérifier la conversion TOTAL/NONE/PARTIEL

### Points de vigilance :
- Le backend doit avoir la méthode `saveRubriqueComplete` avec tous les paramètres
- La conversion de typeImposition vers etatImposition doit fonctionner
- Les champs conditionnels doivent s'afficher correctement dans la vue

## Résumé

L'adaptation est complète avec :
- **Vue** : Nouveaux champs intuitifs et conditionnels
- **Backend** : DTO et contrôleur mis à jour avec conversion
- **Service** : Mapping complet des nouveaux champs
- **Cohérence** : Les trois couches sont alignées

Le formulaire des rubriques est maintenant plus riche et fonctionnel !
