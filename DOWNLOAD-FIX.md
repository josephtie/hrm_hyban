# Correction du téléchargement de documents - Paramètres incorrects

## 🚨 Problème identifié

### Erreur 400 sur le téléchargement de documents
```
POST /api/rh/carriere/affectations/download
État: 400 ❌
Backend attend: row.id (number)
Frontend envoyait: row.urlFichier (string)
```

### Conflit de fonctions
- **downloadDocument(affectationId: number)** : Pour les affectations ✅
- **downloadDocument(urlFichier: string)** : Pour les documents ❌ (n'existe pas)

### Appel incorrect dans le tableau des documents
```vue
<!-- Tableau des documents personnels -->
<el-button @click="downloadDocument(row.urlFichier)">
<!-- Envoie: "uploads\\documents\\Embauche\\NE0182\\NE0182_Facture_VPS_RHPAIE_2026.docx" -->
```

## 🔍 Analyse des endpoints

### Téléchargement affectations (existant)
```typescript
// Fonction existante - CORRECTE
const downloadDocument = async (affectationId: number) => {
  const response = await api.post('/rh/carriere/affectations/download', {
    id: affectationId  // ✅ Backend attend un ID numérique
  }, { responseType: 'blob' })
}
```

### Téléchargement documents personnels (manquant)
```typescript
// Nécessaire - À créer
const downloadPersonnelDocument = async (fileUrl: string) => {
  // Endpoint différent pour les documents personnels
  const response = await api.get(`/personnel/documents/download?fileUrl=${fileUrl}`, {
    responseType: 'blob'
  })
}
```

## ✅ Solution implémentée

### 1. Création de la fonction dédiée
```typescript
// Fonction pour télécharger les documents personnels
const downloadPersonnelDocument = async (fileUrl: string) => {
  if (!fileUrl) {
    ElMessage.warning('Aucun fichier à télécharger')
    return
  }
  
  try {
    // Construire l'URL de téléchargement pour les documents personnels
    const response = await api.get(`/personnel/documents/download?fileUrl=${encodeURIComponent(fileUrl)}`, {
      responseType: 'blob'
    })
    
    // Extraire le nom du fichier
    const fileName = getFileName(fileUrl)
    
    // Créer un lien temporaire pour le téléchargement
    const blob = new Blob([response.data])
    const downloadUrl = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = downloadUrl
    link.download = fileName
    
    // Ajouter le lien au DOM, cliquer dessus, puis le supprimer
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(downloadUrl)
    
    ElMessage.success(`Téléchargement du document: ${fileName}`)
  } catch (error) {
    console.error('❌ Erreur lors du téléchargement du document:', error)
    ElMessage.error('Erreur lors du téléchargement du document')
  }
}
```

### 2. Correction du bouton dans le tableau
```vue
<!-- Avant (incorrect) -->
<el-button @click="downloadDocument(row.urlFichier)">
  <el-icon><Download /></el-icon>
</el-button>

<!-- Après (correct) -->
<el-button @click="downloadPersonnelDocument(row.urlFichier)">
  <el-icon><Download /></el-icon>
</el-button>
```

## 🌋 Flux de données corrigé

### Téléchargement affectations (inchangé)
```
Tableau affectations → Bouton télécharger → downloadDocument(row.id) → POST /rh/carriere/affectations/download → { id: 123 } ✅
```

### Téléchargement documents personnels (corrigé)
```
Tableau documents → Bouton télécharger → downloadPersonnelDocument(row.urlFichier) → GET /personnel/documents/download → ?fileUrl=uploads/... ✅
```

## 📋 Séparation claire des responsabilités

### downloadDocument() - Affectations
- **Paramètre** : `affectationId: number`
- **Endpoint** : `POST /rh/carriere/affectations/download`
- **Body** : `{ id: affectationId }`
- **Usage** : Tableau des affectations

### downloadPersonnelDocument() - Documents personnels
- **Paramètre** : `fileUrl: string`
- **Endpoint** : `GET /personnel/documents/download`
- **Query** : `?fileUrl=${encodeURIComponent(fileUrl)}`
- **Usage** : Tableau des documents personnels

## 🧪 Tests de validation

### 1. Test de téléchargement affectation
1. **Aller dans** : Tableau des affectations
2. **Cliquer sur** : Bouton télécharger
3. **Vérifier** :
   ```
   Appel: downloadDocument(123)
   Endpoint: POST /rh/carriere/affectations/download
   Body: { id: 123 }
   Résultat: ✅ Succès
   ```

### 2. Test de téléchargement document personnel
1. **Aller dans** : Tableau des documents
2. **Cliquer sur** : Bouton télécharger
3. **Vérifier** :
   ```
   Appel: downloadPersonnelDocument("uploads\\documents\\Embauche\\NE0182\\NE0182_Facture_VPS_RHPAIE_2026.docx")
   Endpoint: GET /personnel/documents/download?fileUrl=uploads%5C%5Cdocuments%5C%5CEmbauche%5C%5CNE0182%5C%5CNE0182_Facture_VPS_RHPAIE_2026.docx
   Résultat: ✅ Succès
   ```

### 3. Test des messages
```
Succès affectation: "Téléchargement du document pour: Développeur"
Succès document: "Téléchargement du document: NE0182_Facture_VPS_RHPAIE_2026.docx"
Erreur: "Erreur lors du téléchargement du document"
Aucun fichier: "Aucun fichier à télécharger"
```

## 📊 Avantages de cette solution

### 1. Correction du bug
- ✅ **Plus d'erreur 400** : Bon paramètre envoyé
- ✅ **Endpoints corrects** : Chaque type utilise son endpoint
- ✅ **Types respectés** : number pour ID, string pour URL

### 2. Séparation claire
- ✅ **Responsabilité unique** : Chaque fonction a un rôle
- ✅ **Maintenabilité** : Code facile à comprendre
- ✅ **Extensibilité** : Facile d'ajouter d'autres types

### 3. Expérience utilisateur
- ✅ **Messages clairs** : Feedback spécifique
- ✅ **Gestion d'erreurs** : Messages informatifs
- ✅ **Nom de fichier** : Extraction automatique

## 🎯 Résultat final

### Deux fonctions spécialisées
```typescript
// Pour les affectations
downloadDocument(affectationId: number)

// Pour les documents personnels  
downloadPersonnelDocument(fileUrl: string)
```

### Boutons corrects dans les tableaux
```vue
<!-- Tableau affectations -->
<el-button @click="downloadDocument(row.id)">

<!-- Tableau documents -->
<el-button @click="downloadPersonnelDocument(row.urlFichier)">
```

### Flux utilisateur optimisé
- ✅ **Affectations** : Téléchargement par ID (POST)
- ✅ **Documents** : Téléchargement par URL (GET)
- ✅ **Plus d'erreurs** : Chaque cas géré correctement
- ✅ **Feedback clair** : Messages spécifiques

L'erreur 400 est maintenant résolue avec la séparation correcte des fonctions de téléchargement !
