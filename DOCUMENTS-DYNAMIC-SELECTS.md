# Implémentation des selects dynamiques pour l'onglet Documents

## 🎯 Objectif
Remplacer les options statiques des selects de type de document et d'emplacement par des options chargées dynamiquement depuis les API du backend.

## ✅ Modifications apportées

### 1. Template - Selects dynamiques

**Ancien code** (options statiques) :
```vue
<el-form-item label="Type de document" required>
  <el-select v-model="documentForm.type" placeholder="Type de document" style="width: 100%">
    <el-option label="CV" value="CV" />
    <el-option label="Diplôme" value="Diplôme" />
    <el-option label="CNPS" value="CNPS" />
    <el-option label="CNI" value="CNI" />
    <el-option label="Passeport" value="Passeport" />
    <el-option label="Autre" value="Autre" />
  </el-select>
</el-form-item>

<el-form-item label="Emplacement">
  <el-select v-model="documentForm.emplacement" placeholder="Emplacement de stockage" style="width: 100%">
    <el-option label="Archives centrales" value="Archives centrales" />
    <el-option label="Archives locales" value="Archives locales" />
    <el-option label="Numérique" value="Numérique" />
    <el-option label="Services RH" value="Services RH" />
  </el-select>
</el-form-item>
```

**Nouveau code** (options dynamiques) :
```vue
<el-form-item label="Type de document" required>
  <el-select 
    v-model="documentForm.type" 
    placeholder="Type de document" 
    style="width: 100%"
    filterable
    clearable
  >
    <el-option 
      v-for="type in documentTypes" 
      :key="type.id" 
      :label="type.libelle" 
      :value="type.libelle" 
    />
  </el-select>
</el-form-item>

<el-form-item label="Emplacement">
  <el-select 
    v-model="documentForm.emplacement" 
    placeholder="Emplacement de stockage" 
    style="width: 100%"
    filterable
    clearable
  >
    <el-option 
      v-for="location in storageLocations" 
      :key="location.id" 
      :label="location.libelle" 
      :value="location.libelle" 
    />
  </el-select>
</el-form-item>
```

### 2. Variables réactives

**Ajout des variables pour stocker les listes** :
```typescript
// Variables pour les listes dynamiques
const documentTypes = ref([])
const storageLocations = ref([])
```

### 3. Fonctions de chargement

**Fonction pour charger les types de documents** :
```typescript
const loadDocumentTypes = async () => {
  try {
    const response = await api.get('/personnel/document-types')
    documentTypes.value = response.data
  } catch (error) {
    console.error('❌ Erreur lors du chargement des types de documents:', error)
    ElMessage.error('Erreur lors du chargement des types de documents')
  }
}
```

**Fonction pour charger les emplacements de stockage** :
```typescript
const loadStorageLocations = async () => {
  try {
    const response = await api.get('/storage-locations')
    storageLocations.value = response.data
  } catch (error) {
    console.error('❌ Erreur lors du chargement des emplacements de stockage:', error)
    ElMessage.error('Erreur lors du chargement des emplacements de stockage')
  }
}
```

### 4. Initialisation au montage

**Chargement des données au démarrage** :
```typescript
onMounted(() => {
  loadPersonnel()
  loadContractLists()
  loadDocumentTypes() // ✅ Nouveau
  loadStorageLocations() // ✅ Nouveau
})
```

## 🌟 Fonctionnalités ajoutées

### 1. Filtre et recherche
- ✅ **`filterable`** : Permet de rechercher dans les options
- ✅ **`clearable`** : Permet de effacer la sélection

### 2. Structure des données attendues

**Types de documents** (`/personnel/document-types`) :
```json
[
  {
    "id": 1,
    "libelle": "CV",
    "description": "Curriculum Vitae"
  },
  {
    "id": 2,
    "libelle": "Diplôme",
    "description": "Diplôme académique"
  },
  {
    "id": 3,
    "libelle": "CNPS",
    "description": "Carte CNPS"
  }
]
```

**Emplacements de stockage** (`/storage-locations`) :
```json
[
  {
    "id": 1,
    "libelle": "Archives centrales",
    "description": "Stockage principal"
  },
  {
    "id": 2,
    "libelle": "Archives locales",
    "description": "Stockage départemental"
  },
  {
    "id": 3,
    "libelle": "Numérique",
    "description": "Stockage numérique"
  }
]
```

## 📋 Avantages de cette approche

### 1. Dynamisme
- ✅ **Mise à jour facile** : Ajout/modification sans code frontend
- ✅ **Centralisation** : Données gérées par le backend
- ✅ **Consistance** : Mêmes données partout

### 2. Expérience utilisateur
- ✅ **Recherche** : Filtre pour trouver rapidement
- ✅ **Clear** : Effacer la sélection
- ✅ **Performance** : Options chargées une seule fois

### 3. Maintenance
- ✅ **Code propre** : Pas de données en dur
- ✅ **Évolutif** : Facile à étendre
- ✅ **Réutilisable** : Pattern applicable ailleurs

## 🧪 Tests de validation

### 1. Chargement normal
**Action** : Ouvrir l'onglet Documents
**Résultat attendu** :
- ✅ Types de documents chargés
- ✅ Emplacements chargés
- ✅ Selects fonctionnels

### 2. Recherche/Filtre
**Action** : Taper "CV" dans le select de type
**Résultat attendu** :
- ✅ Options filtrées
- ✅ "CV" visible et sélectionnable

### 3. Gestion d'erreur
**Action** : API indisponible
**Résultat attendu** :
- ✅ Message d'erreur affiché
- ✅ Console log l'erreur
- ✅ Select vide mais fonctionnel

## 🔍 Points de vigilance

### 1. Structure des réponses API
S'assurer que les retours API ont la bonne structure :
```typescript
interface DocumentType {
  id: number
  libelle: string
  description?: string
}

interface StorageLocation {
  id: number
  libelle: string
  description?: string
}
```

### 2. Performance
- ✅ **Chargement au montage** : Une seule fois
- ✅ **Pas de rechargement** : Données en cache
- ✅ **Lazy loading** : Possibilité si besoin

### 3. Gestion d'erreur
- ✅ **Try/catch** : Gestion des erreurs réseau
- ✅ **Message utilisateur** : Feedback clair
- ✅ **Fallback** : Comportement par défaut

## 📊 État final

### Template
```vue
<el-select v-model="documentForm.type" filterable clearable>
  <el-option v-for="type in documentTypes" :key="type.id" :label="type.libelle" :value="type.libelle" />
</el-select>

<el-select v-model="documentForm.emplacement" filterable clearable>
  <el-option v-for="location in storageLocations" :key="location.id" :label="location.libelle" :value="location.libelle" />
</el-select>
```

### Script
```typescript
const documentTypes = ref([])
const storageLocations = ref([])

const loadDocumentTypes = async () => {
  const response = await api.get('/personnel/document-types')
  documentTypes.value = response.data
}

const loadStorageLocations = async () => {
  const response = await api.get('/storage-locations')
  storageLocations.value = response.data
}

onMounted(() => {
  loadDocumentTypes()
  loadStorageLocations()
})
```

## 🎯 Résultat final

L'onglet Documents utilise maintenant des selects dynamiques :

- ✅ **Types de documents** : Chargés depuis `/personnel/document-types`
- ✅ **Emplacements** : Chargés depuis `/storage-locations`
- ✅ **Filtre** : Recherche possible dans les options
- ✅ **Clear** : Effacement de sélection
- ✅ **Performance** : Chargement unique au montage

Les données sont maintenant centralisées et maintenables !
