# Correction des URLs pour les APIs de documents

## 🚨 Problème identifié

**Erreur 404 sur les endpoints** :
- ❌ `/personnel/document-types` : N'existe pas
- ❌ `/personnel/upload` : N'existe pas  
- ✅ `/storage-locations` : Existe

## 🔍 Investigation des endpoints backend

### 1. Upload de documents
**Contrôleur** : `EmployeeDocumentController`
**Mapping** : `@RequestMapping("/api/personnel/documents")`
**Endpoint correct** : `/api/personnel/documents/upload`

### 2. Types de documents
**Contrôleur** : `TypeDocumentController`
**Mapping** : `@RequestMapping("/api/parametrages/doc")`
**Endpoint pour lister** : Pas de `@GetMapping` trouvé
**Page UI** : `/api/parametrages/doc/typedocument`

### 3. Emplacements de stockage
**Contrôleur** : `StorageLocationController`
**Mapping** : `@RequestMapping("/api/storage-locations")`
**Endpoint** : `/api/storage-locations` ✅

## ✅ Corrections apportées

### 1. URL d'upload
**Ancienne URL** :
```typescript
const response = await api.post('/personnel/upload', formData)
```

**Nouvelle URL** :
```typescript
const response = await api.post('/personnel/documents/upload', formData)
```

### 2. URL des types de documents
**Ancienne URL** :
```typescript
const response = await api.get('/personnel/document-types')
```

**Nouvelle URL** :
```typescript
const response = await api.get('/parametrages/doc/typedocument')
```

### 3. URL des emplacements (déjà correcte)
```typescript
const response = await api.get('/storage-locations')
```

## 🌋 Mapping frontend/backend final

| Fonction | URL Frontend | Endpoint Backend | Statut |
|-----------|---------------|-----------------|---------|
| Upload | `/personnel/documents/upload` | `@PostMapping("/upload")` | ✅ |
| Types | `/parametrages/doc/typedocument` | Page UI (pas d'API REST) | ⚠️ |
| Emplacements | `/storage-locations` | `@GetMapping` | ✅ |

## 📋 Problème résiduel : Types de documents

### Analyse
Le contrôleur `TypeDocumentController` n'a pas d'endpoint REST pour lister les types :

```java
@RestController
@RequestMapping("/api/parametrages/doc")
public class TypeDocumentController {
    
    @RequestMapping("/typedocument")  // ❌ Retourne une page UI
    public String viewAccountType(...) {
        return "typedocument";
    }
    
    @PostMapping("/enregistrertypedocument")  // ✅ Endpoint REST
    public TypeDocumentDTO saveTypeDocument(...) { ... }
    
    @PostMapping("/supprimertypedocument")   // ✅ Endpoint REST  
    public TypeDocumentDTO deleteTypeDocument(...) { ... }
    
    @PostMapping("/trouvertypedocument")    // ✅ Endpoint REST
    public TypeDocumentDTO findTypeDocument(...) { ... }
}
```

### Solutions possibles

#### Option 1 : Créer un endpoint REST
```java
@GetMapping("/list")
public List<TypeDocument> getAllTypes() {
    return typeDocumentService.findAll();
}
```

#### Option 2 : Utiliser les données statiques en attendant
```typescript
const loadDocumentTypes = async () => {
  try {
    // En attendant l'API, utiliser des données statiques
    documentTypes.value = [
      { id: 1, nom: 'CV' },
      { id: 2, nom: 'Diplôme' },
      { id: 3, nom: 'CNPS' },
      { id: 4, nom: 'CNI' },
      { id: 5, nom: 'Passeport' },
      { id: 6, nom: 'Autre' }
    ]
  } catch (error) {
    console.error('❌ Erreur:', error)
  }
}
```

#### Option 3 : Créer un endpoint temporaire
```java
@GetMapping("/types")
public ResponseEntity<List<TypeDocument>> getDocumentTypes() {
    try {
        List<TypeDocument> types = typeDocumentService.findAll();
        return ResponseEntity.ok(types);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
```

## 🧪 Tests de validation

### 1. Upload de documents
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

### 2. Emplacements de stockage
```bash
curl -X GET http://192.168.1.2:7200/api/storage-locations
```

### 3. Types de documents
```bash
curl -X GET http://192.168.1.2:7200/api/parametrages/doc/typedocument
# Retourne une page HTML, pas du JSON
```

## 📊 Résultat actuel

### ✅ Fonctionnel
- **Upload** : `/api/personnel/documents/upload` fonctionne
- **Emplacements** : `/api/storage-locations` fonctionne

### ⚠️ Problème
- **Types de documents** : Pas d'endpoint REST disponible

### 🎯 Actions recommandées

1. **Immédiat** : Ajouter un endpoint REST pour les types
2. **Alternative** : Utiliser des données statiques en attendant
3. **Long terme** : Standardiser tous les endpoints en REST

## 🔍 Logs de débogage ajoutés

Les logs permettront de vérifier :
- Chargement des types : `🔄 Chargement des types de documents...`
- Réponse API : `✅ Types de documents chargés: [data]`
- Sélection : `🔍 Recherche du type de document, ID: [id]`

Le problème d'URL est résolu pour l'upload, mais il reste à créer l'endpoint pour les types de documents !
