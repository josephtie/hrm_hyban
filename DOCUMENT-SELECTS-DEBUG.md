# Débogage des selects de documents - Problème d'affichage des libellés

## 🚨 Problème signalé

**L'utilisateur** : "mon probleme c'est form Document ou les select ne fonctionne pas on ne voit pas le libelle"

## 🔍 Hypothèses possibles

### 1. Données non chargées
- Les APIs `/personnel/document-types` et `/storage-locations` ne retournent pas de données
- Les selects restent vides
- Les fonctions `getSelectedDocumentType()` et `getSelectedStorageLocation()` retournent `''`

### 2. Erreur de structure
- Les données retournées par l'API n'ont pas la bonne structure
- Les `find()` ne trouvent pas les éléments
- Les IDs ne correspondent pas

### 3. Problème de réactivité
- Les `ref` ne se mettent pas à jour correctement
- Le template ne réagit pas aux changements

## ✅ Ajout de logs de débogage

### 1. Logs de chargement
```typescript
const loadDocumentTypes = async () => {
  try {
    console.log('🔄 Chargement des types de documents...')
    const response = await api.get('/personnel/document-types')
    console.log('✅ Types de documents chargés:', response.data)
    documentTypes.value = response.data
  } catch (error) {
    console.error('❌ Erreur lors du chargement des types de documents:', error)
  }
}
```

### 2. Logs de sélection
```typescript
const getSelectedDocumentType = () => {
  console.log('🔍 Recherche du type de document, ID:', documentForm.typeId)
  console.log('📋 Types disponibles:', documentTypes.value)
  const selected = documentTypes.value.find(type => type.id === documentForm.typeId)
  console.log('✅ Type trouvé:', selected)
  return selected ? selected.libelle : ''
}

const getSelectedStorageLocation = () => {
  console.log('🔍 Recherche de l\'emplacement, ID:', documentForm.locationId)
  console.log('📋 Emplacements disponibles:', storageLocations.value)
  const selected = storageLocations.value.find(location => location.id === documentForm.locationId)
  console.log('✅ Emplacement trouvé:', selected)
  return selected ? selected.libelle : ''
}
```

## 🧪 Étapes de débogage

### 1. Vérifier la console
Ouvrir l'onglet "Documents" et regarder la console du navigateur :

**Logs attendus** :
```
🔄 Chargement des types de documents...
✅ Types de documents chargés: [{id: 1, libelle: "CV"}, {id: 2, libelle: "Diplôme"}]
🔄 Chargement des emplacements de stockage...
✅ Emplacements de stockage chargés: [{id: 1, libelle: "Archives centrales"}, {id: 2, libelle: "Archives locales"}]
```

**Logs en cas d'erreur** :
```
❌ Erreur lors du chargement des types de documents: Network Error
📋 Types disponibles: []
```

### 2. Vérifier les sélections
Après avoir sélectionné un type et un emplacement :

**Logs attendus** :
```
🔍 Recherche du type de document, ID: 1
📋 Types disponibles: [{id: 1, libelle: "CV"}, {id: 2, libelle: "Diplôme"}]
✅ Type trouvé: {id: 1, libelle: "CV"}
```

**Logs en cas de problème** :
```
🔍 Recherche du type de document, ID: 1
📋 Types disponibles: []
✅ Type trouvé: undefined
```

### 3. Vérifier les APIs directement
Dans la console du navigateur :
```javascript
// Test direct de l'API
fetch('/api/personnel/document-types')
  .then(r => r.json())
  .then(data => console.log('API direct:', data))

fetch('/api/storage-locations')
  .then(r => r.json())
  .then(data => console.log('API direct:', data))
```

## 🔍 Points de vérification

### 1. Structure des données API
**Format attendu pour les types** :
```json
[
  {
    "id": 1,
    "libelle": "CV",
    "description": "Curriculum Vitae"
  }
]
```

**Format attendu pour les emplacements** :
```json
[
  {
    "id": 1,
    "libelle": "Archives centrales",
    "description": "Stockage principal"
  }
]
```

### 2. Configuration du backend
Vérifier que les endpoints existent et retournent les bonnes données :
```bash
# Test des endpoints
curl -X GET http://192.168.1.2:7200/api/personnel/document-types
curl -X GET http://192.168.1.2:7200/api/storage-locations
```

### 3. Configuration CORS
S'assurer que le frontend peut appeler les APIs :
```typescript
// Dans api.ts ou configuration axios
{
  headers: {
    'Access-Control-Allow-Origin': '*'
  }
}
```

## 📋 Actions correctives possibles

### 1. Si les APIs ne retournent rien
```bash
# Vérifier les routes Spring Boot
curl -X GET http://192.168.1.2:7200/actuator/mappings

# Vérifier les contrôleurs
grep -r "@RestController" src/
```

### 2. Si les données sont vides
```typescript
// Ajouter des valeurs par défaut
const loadDocumentTypes = async () => {
  try {
    const response = await api.get('/personnel/document-types')
    if (!response.data || response.data.length === 0) {
      // Valeurs par défaut pour le débogage
      documentTypes.value = [
        { id: 1, libelle: 'CV' },
        { id: 2, libelle: 'Diplôme' }
      ]
    } else {
      documentTypes.value = response.data
    }
  } catch (error) {
    console.error('❌ Erreur:', error)
  }
}
```

### 3. Si les IDs ne correspondent pas
```typescript
// Vérifier le type des IDs
const loadDocumentTypes = async () => {
  const response = await api.get('/personnel/document-types')
  console.log('Types bruts:', response.data)
  response.data.forEach(item => {
    console.log(`Item: ${JSON.stringify(item)}`)
    console.log(`Type de ID: ${typeof item.id}, Type de libelle: ${typeof item.libelle}`)
  })
  documentTypes.value = response.data
}
```

## 🎯 Résultat attendu

Après avoir ajouté les logs :

1. **Ouvrir l'onglet Documents**
2. **Regarder la console** pour voir les logs de chargement
3. **Sélectionner un type et un emplacement**
4. **Vérifier les logs de sélection**
5. **Identifier le problème** grâce aux logs détaillés

Le débogage permettra de trouver exactement pourquoi les selects ne s'affichent pas correctement !
