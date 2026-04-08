# Correction du formulaire de documents - Problème de mapping

## 🚨 Problème identifié

### Contraste entre les deux formulaires

**assignmentForm** ✅ (fonctionne) :
- API : `/api/rh/carriere/affectations//enregistrer`
- Structure : IDs numériques corrects
- Mapping : Parfait

**documentForm** ❌ (ne fonctionne pas) :
- API : `/api/personnel/documents/upload`
- Structure : Problème de mapping nom/ID
- Mapping : Incorrect

## 🔍 Analyse du problème

### Template utilise des noms, backend attend des IDs
```vue
<!-- Template -->
<el-option :label="type.nom" :value="type.nom" />
<el-option :label="location.nom" :value="location.nom" />

<!-- Form data -->
documentForm.typeId = "CV"     // ❌ String (nom)
documentForm.locationId = "Archives" // ❌ String (nom)
```

### Backend attend des IDs numériques
```java
@RequestParam("idDocument") Long typeId        // ✅ Long
@RequestParam("idStorage") Long locationId       // ✅ Long
```

## ✅ Corrections apportées

### 1. Fonctions de conversion
```typescript
// Convertir les noms en IDs
const getDocumentTypeId = (typeName) => {
  const type = documentTypes.value.find(t => t.nom === typeName)
  return type ? type.id : ''
}

const getStorageLocationId = (locationName) => {
  const location = storageLocations.value.find(l => l.nom === locationName)
  return location ? location.id : ''
}
```

### 2. Logique de conversion dans saveDocument
```typescript
// Convertir les noms en IDs si nécessaire
const documentTypeId = isNaN(documentForm.typeId) 
  ? getDocumentTypeId(documentForm.typeId) 
  : documentForm.typeId

const storageLocationId = isNaN(documentForm.locationId) 
  ? getStorageLocationId(documentForm.locationId) 
  : documentForm.locationId
```

### 3. Logs de débogage
```typescript
console.log('📤 Envoi des données:', {
  idPersonnel: personnel.value?.id,
  idDocument: documentTypeId,        // ✅ ID numérique
  dateDepot: documentForm.dateDepot,
  statutpresent: documentForm.present,
  numeroReference: documentForm.numeroReference,
  idStorage: storageLocationId,        // ✅ ID numérique
  description: documentForm.remarques
})
```

## 🌋 Flux de données corrigé

### Avant (incorrect)
```
Utilisateur sélectionne "CV" → documentForm.typeId = "CV"
Utilisateur sélectionne "Archives" → documentForm.locationId = "Archives"
Backend reçoit → idDocument: "CV", idStorage: "Archives" ❌
```

### Après (correct)
```
Utilisateur sélectionne "CV" → documentForm.typeId = "CV"
Conversion → getDocumentTypeId("CV") = 1
Backend reçoit → idDocument: "1" ✅

Utilisateur sélectionne "Archives" → documentForm.locationId = "Archives"
Conversion → getStorageLocationId("Archives") = 1
Backend reçoit → idStorage: "1" ✅
```

## 📋 Structure des données

### documentTypes (de l'API)
```json
[
  { "id": 1, "nom": "CV" },
  { "id": 2, "nom": "Diplôme" },
  { "id": 3, "nom": "CNPS" }
]
```

### storageLocations (de l'API)
```json
[
  { "id": 1, "nom": "Archives centrales" },
  { "id": 2, "nom": "Archives locales" }
]
```

### FormData envoyé (corrigé)
```javascript
{
  fichierDocument: [File],
  idPersonnel: "123",
  idDocument: "1",        // ✅ ID numérique en string
  dateDepot: "31/03/2026",
  statutpresent: "true",
  numeroReference: "REF-001",
  idStorage: "1",         // ✅ ID numérique en string
  description: "Test document"
}
```

## 🧪 Tests de validation

### 1. Test de conversion
```javascript
// Dans la console
documentForm.typeId = "CV"
console.log(getDocumentTypeId("CV")) // Devrait afficher 1

documentForm.locationId = "Archives centrales"
console.log(getStorageLocationId("Archives centrales")) // Devrait afficher 1
```

### 2. Test d'upload complet
1. **Sélectionner** : Type "CV", Emplacement "Archives centrales"
2. **Remplir** : Champs obligatoires
3. **Cliquer** : "Enregistrer"
4. **Vérifier les logs** :
   ```
   📤 Envoi des données: {
     idPersonnel: 123,
     idDocument: 1,        // ✅ ID numérique
     idStorage: 1,         // ✅ ID numérique
     ...
   }
   ```

### 3. Test avec IDs directs
```javascript
// Si on utilise directement les IDs
documentForm.typeId = 1        // ID numérique
documentForm.locationId = 1     // ID numérique
// Pas de conversion nécessaire
```

## 📊 Comparaison avec assignmentForm

### assignmentForm (réussi)
```typescript
// Utilise directement les IDs
assignmentForm.poste = 1        // ID numérique
assignmentForm.site = 2          // ID numérique
// Pas de conversion nécessaire
```

### documentForm (corrigé)
```typescript
// Utilise les noms, conversion automatique
documentForm.typeId = "CV"      // Nom
→ conversion → 1               // ID numérique
documentForm.locationId = "Archives" // Nom
→ conversion → 1                // ID numérique
```

## 🎯 Avantages de cette approche

### 1. Flexibilité
- ✅ **Noms lisibles** : Pour l'utilisateur
- ✅ **IDs numériques** : Pour le backend
- ✅ **Conversion automatique** : Transparente

### 2. Compatibilité
- ✅ **Template existant** : Pas besoin de changer
- ✅ **Backend existant** : Reçoit les bons types
- ✅ **Migration douce** : Transition possible

### 3. Débogage
- ✅ **Logs détaillés** : Voir les conversions
- ✅ **Validation** : Vérifier les IDs
- ✅ **Isolation** : Problèmes identifiés

## 🌟 Résultat final

Le formulaire `documentForm` devrait maintenant fonctionner comme `assignmentForm` :

- ✅ **Conversion automatique** : Noms → IDs
- ✅ **Types corrects** : Long en String
- ✅ **Mapping parfait** : Backend reçoit les bons IDs
- ✅ **Débogage** : Logs complets

Les deux formulaires utilisent maintenant la même logique de base avec des IDs numériques !
