# Correction des paramètres d'upload - Types de données

## 🚨 Problèmes identifiés

### Erreur 400 sur `/personnel/documents/upload`
- **Cause** : Types de paramètres incorrects
- **Backend attend** : Types Java spécifiques
- **Frontend envoyait** : Types JavaScript incorrects

### Erreur 404 sur `/personnel/upload`
- **Cause** : Endpoint n'existe pas
- **Seul endpoint** : `/personnels/personnel/upload/photo` (pour les photos)

## 🔍 Structure attendue par le backend

### EmployeeDocumentController.java
```java
@PostMapping("/upload")
public EmployeeDocumentDTO uploadDocument(
        @RequestParam("fichierDocument") MultipartFile uploadfile,      // ✅ File
        @RequestParam("idPersonnel") Long employeeId,               // ✅ Long
        @RequestParam("idDocument") Long typeId,                     // ✅ Long
        @RequestParam("dateDepot") String dtedepot,                 // ✅ String
        @RequestParam("statutpresent") Boolean statutpresent,         // ✅ Boolean
        @RequestParam("numeroReference") String numeroReference,        // ✅ String
        @RequestParam("idStorage") Long locationId,                  // ✅ Long
        @RequestParam(value = "description", required = false) String description,
        HttpServletRequest request) throws Exception {
```

## ✅ Corrections apportées

### 1. Conversion des types en chaînes
```typescript
// Avant (incorrect)
formData.append('idPersonnel', personnel.value?.id || '')           // Number
formData.append('idDocument', documentForm.typeId)                 // String
formData.append('statutpresent', documentForm.present)              // Boolean
formData.append('idStorage', documentForm.locationId || '')         // String

// Après (correct)
formData.append('idPersonnel', personnel.value?.id?.toString() || '')    // String
formData.append('idDocument', documentForm.typeId?.toString() || '')    // String
formData.append('statutpresent', documentForm.present.toString())       // String
formData.append('idStorage', documentForm.locationId?.toString() || '')   // String
```

### 2. Gestion des valeurs nulles
```typescript
// Ajout de ?.toString() pour éviter les erreurs
personnel.value?.id?.toString() || ''
documentForm.typeId?.toString() || ''
documentForm.locationId?.toString() || ''
```

## 🌋 Mapping final des paramètres

| Paramètre | Type Backend | Valeur Frontend | Conversion |
|-----------|--------------|-----------------|------------|
| `fichierDocument` | `MultipartFile` | `documentForm.fichier` | ✅ File |
| `idPersonnel` | `Long` | `personnel.value?.id?.toString()` | ✅ String |
| `idDocument` | `Long` | `documentForm.typeId?.toString()` | ✅ String |
| `dateDepot` | `String` | `documentForm.dateDepot || date` | ✅ String |
| `statutpresent` | `Boolean` | `documentForm.present.toString()` | ✅ String |
| `numeroReference` | `String` | `documentForm.numeroReference || ''` | ✅ String |
| `idStorage` | `Long` | `documentForm.locationId?.toString()` | ✅ String |
| `description` | `String` | `documentForm.remarques || ''` | ✅ String |

## 📋 Problème de l'ancien endpoint

### Endpoint `/personnel/upload` n'existe pas
**Seul endpoint upload existant** :
```java
@PostMapping("/upload/photo")  // Pour les photos de personnel
public ResponseEntity<PersonnelVueResponse<Personnel>> uploadPhoto(
    @RequestParam("photo") MultipartFile photoFile,
    @RequestParam("id") Long personnelId)
```

**URL complète** : `/api/personnels/personnel/upload/photo`

## 🧪 Tests de validation

### 1. Test avec types corrects
```bash
curl -X POST http://192.168.1.2:7200/api/personnel/documents/upload \
  -F "fichierDocument=@test.pdf" \
  -F "idPersonnel=123" \
  -F "idDocument=1" \
  -F "dateDepot=31/03/2026" \
  -F "statutpresent=true" \
  -F "numeroReference=REF-001" \
  -F "idStorage=1" \
  -F "description=Test document"
```

### 2. Vérification des logs
**Console frontend** :
```
🔄 Chargement des types de documents...
✅ Types de documents chargés: [{id: 1, nom: "CV"}, ...]
POST /api/personnel/documents/upload
✅ Document ajouté avec succès
```

**Console backend** :
```
Received file: test.pdf
Employee ID: 123
Document Type ID: 1
Date: 31/03/2026
Status: true
Reference: REF-001
Storage ID: 1
Description: Test document
```

## 📊 État final

### ✅ Endpoint fonctionnel
- **URL** : `/personnel/documents/upload`
- **Paramètres** : Types corrects
- **Statut** : Doit fonctionner maintenant

### ❌ Endpoint non fonctionnel
- **URL** : `/personnel/upload` (404)
- **Cause** : N'existe pas dans le backend
- **Solution** : Utiliser uniquement `/personnel/documents/upload`

## 🎯 Actions recommandées

### 1. Supprimer la méthode alternative
```typescript
// Garder uniquement la méthode qui fonctionne
const saveDocument = async () => {
  // Utilise /personnel/documents/upload avec types corrects
}
```

### 2. Mettre à jour l'interface
```vue
<el-form-item>
  <el-button type="primary" @click="saveDocument" :loading="formLoading">
    Enregistrer
  </el-button>
  <el-button @click="closeForm">Annuler</el-button>
</el-form-item>
```

### 3. Tester l'upload
1. **Remplir le formulaire** : Type, fichier, etc.
2. **Cliquer sur "Enregistrer"**
3. **Vérifier la réponse** : Succès attendu

## 🌟 Résultat final

L'upload devrait maintenant fonctionner avec :

- ✅ **Types corrects** : Tous les paramètres en String
- ✅ **Endpoint valide** : `/personnel/documents/upload`
- ✅ **Gestion des nulls** : Safe avec toString()
- ✅ **Validation** : Contrôles avant envoi

Le problème de type 400 devrait être résolu avec la conversion correcte des paramètres !
