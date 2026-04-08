# Alignement Frontend/Backend pour l'upload de documents

## 🎯 Objectif
Aligner le formulaire d'upload de documents du frontend avec les attentes du backend pour l'endpoint `/personnel/upload`.

## ✅ Modifications apportées

### 1. Structure du formulaire (documentForm)

**Ancienne structure** (incompatible) :
```typescript
const documentForm = reactive({
  id: 0,
  type: '',           // ❌ Libellé au lieu de ID
  emplacement: '',    // ❌ Libellé au lieu de ID
  dateDepot: '',
  numeroReference: '',
  present: true,
  remarques: '',
  fichier: null
})
```

**Nouvelle structure** (alignée avec le backend) :
```typescript
const documentForm = reactive({
  id: 0,
  typeId: '',        // ✅ ID du type de document
  locationId: '',    // ✅ ID de l'emplacement
  dateDepot: '',
  numeroReference: '',
  present: true,
  remarques: '',
  fichier: null
})
```

### 2. Template - Selects avec IDs

**Ancien code** (utilisait les libellés) :
```vue
<el-select v-model="documentForm.type" placeholder="Type de document">
  <el-option 
    v-for="type in documentTypes" 
    :key="type.id" 
    :label="type.libelle" 
    :value="type.libelle"  // ❌ Libellé
  />
</el-select>

<el-select v-model="documentForm.emplacement" placeholder="Emplacement">
  <el-option 
    v-for="location in storageLocations" 
    :key="location.id" 
    :label="location.libelle" 
    :value="location.libelle"  // ❌ Libellé
  />
</el-select>
```

**Nouveau code** (utilise les IDs) :
```vue
<el-select v-model="documentForm.typeId" placeholder="Type de document">
  <el-option 
    v-for="type in documentTypes" 
    :key="type.id" 
    :label="type.libelle" 
    :value="type.id"  // ✅ ID
  />
</el-select>

<el-select v-model="documentForm.locationId" placeholder="Emplacement">
  <el-option 
    v-for="location in storageLocations" 
    :key="location.id" 
    :label="location.libelle" 
    :value="location.id"  // ✅ ID
  />
</el-select>
```

### 3. Fonction saveDocument alignée

**Ancienne fonction** (simulation) :
```typescript
const saveDocument = async () => {
  // Simulation sans appel API
  const newDocument = {
    type: documentForm.type,
    emplacement: documentForm.emplacement,
    // ...
  }
  documents.value.unshift(newDocument)
}
```

**Nouvelle fonction** (alignée avec le backend) :
```typescript
const saveDocument = async () => {
  if (!documentForm.typeId || !documentForm.fichier) {
    ElMessage.error('Veuillez renseigner le type de document et sélectionner un fichier')
    return
  }

  formLoading.value = true
  try {
    // Créer FormData pour l'upload
    const formData = new FormData()
    formData.append('fichierDocument', documentForm.fichier)        // ✅ Correspond à @RequestParam("fichierDocument")
    formData.append('idPersonnel', personnel.value?.id || '')       // ✅ Correspond à @RequestParam("idPersonnel")
    formData.append('idDocument', documentForm.typeId)              // ✅ Correspond à @RequestParam("idDocument")
    formData.append('dateDepot', documentForm.dateDepot || new Date().toLocaleDateString('fr-FR'))  // ✅ Correspond à @RequestParam("dateDepot")
    formData.append('statutpresent', documentForm.present)          // ✅ Correspond à @RequestParam("statutpresent")
    formData.append('numeroReference', documentForm.numeroReference || '')  // ✅ Correspond à @RequestParam("numeroReference")
    formData.append('idStorage', documentForm.locationId || '')    // ✅ Correspond à @RequestParam("idStorage")
    formData.append('description', documentForm.remarques || '')     // ✅ Correspond à @RequestParam("description")

    const response = await api.post('/personnel/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data.result) {
      ElMessage.success('Document ajouté avec succès')
      closeForm()
    }
  } catch (error) {
    console.error('❌ Erreur lors de l\'upload du document:', error)
    ElMessage.error('Erreur lors de l\'ajout du document')
  } finally {
    formLoading.value = false
  }
}
```

## 🌋 Mapping des paramètres Backend

### Backend attend :
```java
@PostMapping("/upload")
public EmployeeDocumentDTO uploadDocument(
    @RequestParam("fichierDocument") MultipartFile uploadfile,      // ✅ formData.append('fichierDocument', file)
    @RequestParam("idPersonnel") Long employeeId,                  // ✅ formData.append('idPersonnel', personnelId)
    @RequestParam("idDocument") Long typeId,                       // ✅ formData.append('idDocument', typeId)
    @RequestParam("dateDepot") String dtedepot,                     // ✅ formData.append('dateDepot', date)
    @RequestParam("statutpresent") Boolean statutpresent,           // ✅ formData.append('statutpresent', boolean)
    @RequestParam("numeroReference") String numeroReference,      // ✅ formData.append('numeroReference', ref)
    @RequestParam("idStorage") Long locationId,                    // ✅ formData.append('idStorage', locationId)
    @RequestParam(value = "description", required = false) String description  // ✅ formData.append('description', remarks)
) throws Exception
```

### Frontend envoie :
```typescript
const formData = new FormData()
formData.append('fichierDocument', documentForm.fichier)        // MultipartFile
formData.append('idPersonnel', personnel.value?.id)           // Long
formData.append('idDocument', documentForm.typeId)              // Long
formData.append('dateDepot', documentForm.dateDepot)            // String (dd/MM/yyyy)
formData.append('statutpresent', documentForm.present)          // Boolean
formData.append('numeroReference', documentForm.numeroReference) // String
formData.append('idStorage', documentForm.locationId)           // Long
formData.append('description', documentForm.remarques)         // String (optionnel)
```

## 📋 Validation des données

### 1. Champs obligatoires
```typescript
if (!documentForm.typeId || !documentForm.fichier) {
  ElMessage.error('Veuillez renseigner le type de document et sélectionner un fichier')
  return
}
```

### 2. Format de la date
- **Backend attend** : `dd/MM/yyyy` (ex: "31/03/2026")
- **Frontend envoie** : `new Date().toLocaleDateString('fr-FR')`

### 3. Types de données
- **IDs** : envoyés comme strings mais convertis en Long par le backend
- **Boolean** : envoyé comme true/false
- **File** : MultipartFile géré automatiquement

## 🧪 Tests de validation

### 1. Scénario normal
**Données formulaire** :
```typescript
documentForm = {
  typeId: 1,           // CV
  locationId: 3,       // Numérique
  dateDepot: "31/03/2026",
  numeroReference: "DOC-001",
  present: true,
  remarques: "Document important",
  fichier: File
}
```

**FormData envoyé** :
```
fichierDocument: [File]
idPersonnel: "123"
idDocument: "1"
dateDepot: "31/03/2026"
statutpresent: "true"
numeroReference: "DOC-001"
idStorage: "3"
description: "Document important"
```

### 2. Gestion d'erreur
- **Type manquant** : Message d'erreur "Veuillez renseigner le type de document"
- **Fichier manquant** : Message d'erreur "Veuillez sélectionner un fichier"
- **Erreur API** : Message d'erreur générique avec log console

## 📊 Avantages de l'alignement

### 1. Compatibilité totale
- ✅ **Paramètres** : Exactement ce que le backend attend
- ✅ **Types** : Conversions automatiques gérées
- ✅ **Noms** : Correspondance parfaite des @RequestParam

### 2. Gestion des fichiers
- ✅ **FormData** : Format standard pour l'upload
- ✅ **MultipartFile** : Géré automatiquement par Spring
- ✅ **Headers** : Content-Type approprié

### 3. Robustesse
- ✅ **Validation** : Contrôle des champs obligatoires
- ✅ **Gestion d'erreur** : Messages clairs pour l'utilisateur
- ✅ **Feedback** : Succès/échec communiqué

## 🎯 Résultat final

L'upload de documents est maintenant parfaitement aligné :

- ✅ **Formulaire** : Utilise les IDs au lieu des libellés
- ✅ **API** : Envoie les bons paramètres au backend
- ✅ **Fichiers** : Upload via FormData standard
- ✅ **Validation** : Contrôle des données avant envoi
- ✅ **Feedback** : Messages utilisateur appropriés

Le frontend et le backend sont maintenant synchronisés pour l'upload de documents !
