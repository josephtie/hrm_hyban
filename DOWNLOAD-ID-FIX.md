# Correction finale - Téléchargement par ID pour les documents

## 🎯 Objectif de l'utilisateur

### Utiliser row.id pour le téléchargement des documents
```vue
<!-- Bouton dans le tableau des documents -->
<el-button @click="downloadPersonnelDocument(row.id)">
```

### Paramètre attendu par le backend
- **Backend attend** : `id` du document (number)
- **Frontier doit envoyer** : `row.id` au lieu de `row.urlFichier`

## ✅ Correction implémentée

### 1. Modification de la signature de la fonction
```typescript
// Avant (recevait une URL)
const downloadPersonnelDocument = async (fileUrl: string) => {
  // GET avec fileUrl en paramètre
}

// Après (reçoit un ID)
const downloadPersonnelDocument = async (documentId: number) => {
  // POST avec id dans le body
}
```

### 2. Changement de l'endpoint et du paramètre
```typescript
// Avant (GET avec URL)
const response = await api.get(`/personnel/documents/download?fileUrl=${encodeURIComponent(fileUrl)}`, {
  responseType: 'blob'
})

// Après (POST avec ID)
const response = await api.post('/personnel/documents/download', {
  id: documentId
}, {
  responseType: 'blob'
})
```

### 3. Extraction du nom depuis les headers
```typescript
// Avant (extraction depuis l'URL)
const fileName = getFileName(fileUrl)

// Après (extraction depuis les headers HTTP)
const contentDisposition = response.headers['content-disposition']
let fileName = 'document'
if (contentDisposition) {
  const fileNameMatch = contentDisposition.match(/filename="(.+)"/)
  if (fileNameMatch && fileNameMatch[1]) {
    fileName = fileNameMatch[1]
  }
}
```

## 🌋 Flux de données final

### Téléchargement des documents personnels
```
1. Utilisateur clique sur "Télécharger" dans le tableau des documents
2. Appel: downloadPersonnelDocument(row.id)
3. Envoi: POST /personnel/documents/download
4. Body: { id: 123 }
5. Backend: Trouve le document par ID, récupère le fichier
6. Response: Blob avec content-disposition header
7. Frontend: Extrait le nom du fichier depuis le header
8. Téléchargement: Fichier sauvegardé avec le bon nom
```

### Comparaison avec les affectations
```
Affectations: downloadDocument(row.id) → POST /rh/carriere/affectations/download → { id: row.id }
Documents:   downloadPersonnelDocument(row.id) → POST /personnel/documents/download → { id: row.id }
```

## 📋 Structure finale des fonctions

### downloadDocument() - Affectations
```typescript
const downloadDocument = async (affectationId: number) => {
  const response = await api.post('/rh/carriere/affectations/download', {
    id: affectationId
  }, { responseType: 'blob' })
  // ... extraction du nom depuis headers
}
```

### downloadPersonnelDocument() - Documents personnels
```typescript
const downloadPersonnelDocument = async (documentId: number) => {
  const response = await api.post('/personnel/documents/download', {
    id: documentId
  }, { responseType: 'blob' })
  // ... extraction du nom depuis headers
}
```

## 🧪 Tests de validation

### 1. Test du bouton dans le tableau des documents
```vue
<!-- Template correct -->
<el-button v-if="row.urlFichier" type="success" size="small" @click="downloadPersonnelDocument(row.id)">
  <el-icon><Download /></el-icon>
</el-button>
```

### 2. Test de l'appel API
```
Clic sur téléchargement → downloadPersonnelDocument(123)
↓
POST /personnel/documents/download
Body: { id: 123 }
Headers: { responseType: 'blob' }
↓
Response: Blob + Content-Disposition: filename="NE0182_Facture_VPS_RHPAIE_2026.docx"
↓
Téléchargement: NE0182_Facture_VPS_RHPAIE_2026.docx ✅
```

### 3. Test des messages
```
Succès: "Téléchargement du document: ID 123"
Erreur: "Erreur lors du téléchargement du document"
Aucun ID: "Aucun document à télécharger"
```

## 📊 Avantages de cette approche

### 1. Cohérence parfaite
- ✅ **Même pattern** : ID numérique pour les deux types
- ✅ **Même méthode** : POST avec body { id }
- ✅ **Même extraction** : Nom depuis headers

### 2. Sécurité améliorée
- ✅ **Pas d'URL exposée** : ID seulement dans le frontend
- ✅ **Backend contrôle** : Validation de l'ID côté serveur
- ✅ **Accès sécurisé** : Vérification des permissions

### 3. Simplicité
- ✅ **Un paramètre** : Juste l'ID du document
- ✅ **Pas d'encoding** : Pas besoin d'encoder les URLs
- ✅ **Standard REST** : POST avec body JSON

## 🎯 Résultat final

### Boutons corrects dans les deux tableaux
```vue
<!-- Tableau des affectations -->
<el-button @click="downloadDocument(row.id)">

<!-- Tableau des documents -->
<el-button @click="downloadPersonnelDocument(row.id)">
```

### Flux unifié
```
Téléchargement affectation: ID → POST /rh/carriere/affectations/download → { id }
Téléchargement document:    ID → POST /personnel/documents/download → { id }
```

### Expérience utilisateur
- ✅ **Cohérent** : Même logique pour les deux tableaux
- ✅ **Sécurisé** : IDs seulement, pas d'URLs exposées
- ✅ **Fiable** : Extraction du nom depuis les headers du serveur

Le téléchargement des documents utilise maintenant `row.id` comme demandé, avec une approche cohérente et sécurisée !
