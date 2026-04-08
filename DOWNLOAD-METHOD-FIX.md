# Correction de la méthode downloadDocument

## 🚨 Problème identifié

**Incohérence entre l'appel et la méthode** :
- **Bouton** : `@click="downloadDocument(row.id)"` (1 paramètre)
- **Méthode** : `downloadDocument(affectationId: number, poste: string, matricule?: string)` (3 paramètres)

## ✅ Correction apportée

### 1. Simplification de la signature

**Ancienne méthode** (trop complexe) :
```typescript
const downloadDocument = async (affectationId: number, poste: string, matricule?: string) => {
  const fileName = `affectation_${matricule}_${poste.replace(/\s+/g, '_')}_${timestamp}`
  
  const response = await api.post('/rh/carriere/affectations/download', {
    id: affectationId,
    fileName: fileName
  }, {
    responseType: 'blob'
  })
  
  const link = document.createElement('a')
  link.download = fileName  // ❌ Variable non définie après simplification
}
```

**Nouvelle méthode** (simplifiée) :
```typescript
const downloadDocument = async (affectationId: number) => {
  const response = await api.post('/rh/carriere/affectations/download', {
    id: affectationId
  }, {
    responseType: 'blob'
  })
  
  // Extraire le nom du fichier depuis les headers
  const contentDisposition = response.headers['content-disposition']
  let fileName = 'document'
  if (contentDisposition) {
    const fileNameMatch = contentDisposition.match(/filename="(.+)"/)
    if (fileNameMatch && fileNameMatch[1]) {
      fileName = fileNameMatch[1]
    }
  }
  
  const link = document.createElement('a')
  link.download = fileName  // ✅ Nom récupéré depuis le backend
}
```

## 🌟 Flux de téléchargement optimisé

### 1. Appel simplifié
```vue
<el-button @click="downloadDocument(row.id)">
```

### 2. Envoi au backend
```typescript
const response = await api.post('/rh/carriere/affectations/download', {
  id: affectationId  // ✅ Uniquement l'ID
}, {
  responseType: 'blob'
})
```

### 3. Backend traite et renvoie le fichier
```java
@PostMapping("/download")
public ResponseEntity<Resource> downloadDocument(@RequestBody IdRequest req) {
    Long affectationId = req.getId();
    AffectationDTO doc = affectationService.findAffectation(affectationId);
    
    // Le backend génère le nom du fichier
    String fileName = absolutePath.getFileName().toString();
    
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .body(fileResource);
}
```

### 4. Frontend extrait le nom depuis les headers
```typescript
const contentDisposition = response.headers['content-disposition']
// Ex: "attachment; filename=\"NE0651_Organisation_mensuelle1.docx\""

const fileNameMatch = contentDisposition.match(/filename="(.+)"/)
// fileNameMatch[1] = "NE0651_Organisation_mensuelle1.docx"
```

## 📋 Avantages de cette approche

### 1. Simplicité
- ✅ **Frontend minimal** : Envoie seulement l'ID
- ✅ **Backend gère tout** : Recherche, nom de fichier, headers
- ✅ **Pas de duplication** : Logique centralisée

### 2. Cohérence
- ✅ **Nom de fichier original** : Utilise le nom stocké en base
- ✅ **Extension préservée** : Le backend connaît le vrai format
- ✅ **Headers standards** : Content-Disposition respecté

### 3. Maintenance
- ✅ **Code simple** : Une seule responsabilité par couche
- ✅ **Débogage facile** : Logs clairs du backend
- ✅ **Évolution possible** : Ajout de métadonnées si besoin

## 🧪 Tests de validation

### 1. Scénario normal
**Input** : `row.id = 123`
**Backend** : Trouve l'affectation → fichier `NE0651_Organisation_mensuelle1.docx`
**Headers** : `Content-Disposition: attachment; filename="NE0651_Organisation_mensuelle1.docx"`
**Frontend** : Extrait `NE0651_Organisation_mensuelle1.docx`
**Résultat** : ✅ Fichier téléchargé avec son nom original

### 2. Gestion d'erreur
**Input** : `row.id = 999` (inexistant)
**Backend** : Exception → HTTP 404/500
**Frontend** : Catch → Message d'erreur

### 3. Header manquant
**Backend** : Oublie de setter Content-Disposition
**Frontend** : Utilise `fileName = 'document'` (fallback)

## 🔍 Comparaison des approches

### Approche 1 (complexe) - Gestion frontend
```typescript
// Frontend calcule le nom
const fileName = `affectation_${matricule}_${poste}_${timestamp}`

// Inconvénients :
- ❌ Logique dupliquée
- ❌ Nom différent de l'original
- ❌ Extension perdue
```

### Approche 2 (simple) - Gestion backend
```typescript
// Frontend utilise le nom du backend
const fileName = extractFromHeaders(response.headers)

// Avantages :
- ✅ Nom original préservé
- ✅ Extension correcte
- ✅ Logique centralisée
```

## 📊 État final

### Frontend
```vue
<el-button @click="downloadDocument(row.id)">Doc</el-button>
```

```typescript
const downloadDocument = async (affectationId: number) => {
  const response = await api.post('/rh/carriere/affectations/download', {
    id: affectationId
  }, { responseType: 'blob' })
  
  const fileName = extractFileNameFromHeaders(response.headers)
  downloadBlob(response.data, fileName)
}
```

### Backend
```java
@PostMapping("/download")
public ResponseEntity<Resource> downloadDocument(@RequestBody IdRequest req) {
    AffectationDTO doc = affectationService.findAffectation(req.getId());
    Resource file = loadFile(doc.getRow().getUrlDocument());
    
    return ResponseEntity.ok()
            .header(CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
}
```

## 🎯 Résultat final

Le téléchargement fonctionne maintenant de manière simple et cohérente :

- ✅ **Appel simplifié** : Un seul paramètre (l'ID)
- ✅ **Backend gère tout** : Recherche et nom de fichier
- ✅ **Nom original** : Fichier téléchargé avec son vrai nom
- ✅ **Code maintenable** : Logique centralisée

La méthode `downloadDocument` est maintenant correctement alignée avec l'appel du bouton !
