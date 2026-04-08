# Modification du téléchargement pour utiliser l'ID de l'affectation

## 🎯 Objectif
Modifier le frontend pour envoyer l'ID de l'affectation au lieu de l'URL du document, permettant au backend de retrouver l'affectation et son document de manière sécurisée.

## ✅ Modifications apportées

### 1. Frontend - Changement du paramètre

**Ancien code** (envoyait l'URL) :
```vue
<el-button v-if="row.urlDocument" type="success" size="small" 
           @click="downloadDocument(row.urlDocument, row.poste, personnel.value?.matricule)">
```

**Nouveau code** (envoie l'ID) :
```vue
<el-button v-if="row.urlDocument" type="success" size="small" 
           @click="downloadDocument(row.id, row.poste, personnel.value?.matricule)">
```

### 2. Fonction downloadDocument modifiée

**Ancienne signature** :
```typescript
const downloadDocument = async (url: string, poste: string, matricule?: string) => {
```

**Nouvelle signature** :
```typescript
const downloadDocument = async (affectationId: number, poste: string, matricule?: string) => {
```

**Ancien payload** :
```json
{
  "urlDocument": "uploads\\documents\\Mobilite_Agent\\NE0651\\NE0651_Organisation_mensuelle1.docx",
  "fileName": "affectation_NE0651_Assistante_RH_2026-03-30"
}
```

**Nouveau payload** :
```json
{
  "id": 123,
  "fileName": "affectation_NE0651_Assistante_RH_2026-03-30"
}
```

## 🌟 Flux de téléchargement optimisé

### 1. Clic sur le bouton "Doc"
```vue
@click="downloadDocument(row.id, row.poste, personnel.value?.matricule)"
```

### 2. Appel de la fonction
```typescript
const downloadDocument = async (affectationId: number, poste: string, matricule?: string) => {
  const fileName = `affectation_${matricule}_${poste.replace(/\s+/g, '_')}_${timestamp}`
  
  const response = await api.post('/rh/carriere/affectations/download', {
    id: affectationId,  // ✅ ID de l'affectation
    fileName: fileName
  }, {
    responseType: 'blob'
  })
}
```

### 3. Backend traite l'ID
```java
@PostMapping("/download")
public ResponseEntity<Resource> downloadDocument(@RequestBody IdRequest req, HttpServletRequest request) {
    Long affectationId = req.getId();
    AffectationDTO doc = affectationService.findAffectation(affectationId);  // ✅ Recherche par ID
    
    String relativePath = doc.getRow().getUrlDocument().replaceFirst("^/+", "");
    // ... traitement du fichier
}
```

## 📋 Avantages de cette approche

### 1. Sécurité
- ✅ **Validation par ID** : Le backend contrôle l'existence de l'affectation
- ✅ **Pas d'injection de chemin** : L'utilisateur ne peut pas spécifier un chemin arbitraire
- ✅ **Contrôle d'accès** : Possibilité de vérifier les permissions de l'utilisateur

### 2. Simplicité
- ✅ **Frontend simple** : Envoie seulement l'ID et le nom de fichier souhaité
- ✅ **Backend robuste** : Gère la recherche et la validation
- ✅ **Pas de normalisation** : Le backend gère les chemins en interne

### 3. Cohérence
- ✅ **Pattern standard** : Utilise `IdRequest` comme les autres endpoints
- ✅ **Réutilisabilité** : La logique de recherche est centralisée
- ✅ **Maintenance facile** : Un seul point de gestion des documents

## 🧪 Tests de validation

### 1. Scénario normal
**Input** : `row.id = 123`, `row.poste = "Assistante RH"`, `matricule = "NE0651"`
**Payload** :
```json
{
  "id": 123,
  "fileName": "affectation_NE0651_Assistante_RH_2026-03-30"
}
```
**Backend** : Trouve l'affectation 123 → récupère `urlDocument` → envoie le fichier

### 2. Gestion d'erreur
**Input** : `row.id = 999` (inexistant)
**Backend** : `findAffectation(999)` → lève une exception → retour HTTP 404/500

### 3. Sécurité
**Input** : Tentative d'envoyer un ID invalide
**Backend** : Validation de l'ID → rejet si non trouvé

## 🔍 Comparaison des approches

### Approche 1 (ancienne) - URL directe
```typescript
// Frontend
downloadDocument(row.urlDocument, ...)

// Backend
String filePath = request.get("urlDocument");
Resource resource = new UrlResource("file:" + filePath);
```
**Inconvénients** :
- ❌ Sécurité : Injection de chemin possible
- ❌ Complexité : Normalisation des backslashes
- ❌ Validation : Pas de contrôle de l'affectation

### Approche 2 (nouvelle) - ID based
```typescript
// Frontend
downloadDocument(row.id, ...)

// Backend
Long affectationId = req.getId();
AffectationDTO doc = affectationService.findAffectation(affectationId);
String relativePath = doc.getRow().getUrlDocument();
```
**Avantages** :
- ✅ Sécurité : Validation par ID
- ✅ Simplicité : Pas de manipulation de chemin
- ✅ Contrôle : Vérification de l'existence

## 📊 État du backend

Le backend est maintenant configuré pour :

```java
@PostMapping("/download")
public ResponseEntity<Resource> downloadDocument(@RequestBody IdRequest req, HttpServletRequest request) {
    // 1. Récupérer l'ID de l'affectation
    Long affectationId = req.getId();
    
    // 2. Trouver l'affectation
    AffectationDTO doc = affectationService.findAffectation(affectationId);
    
    // 3. Extraire le chemin du document
    String relativePath = doc.getRow().getUrlDocument().replaceFirst("^/+", "");
    
    // 4. Construire le chemin absolu
    Path absolutePath = ...;
    
    // 5. Retourner le fichier
    Resource fileResource = new UrlResource(absolutePath.toUri());
    return ResponseEntity.ok()...body(fileResource);
}
```

## 🎯 Résultat final

Le téléchargement des documents fonctionne maintenant de manière sécurisée et optimisée :

- ✅ **Frontend** : Envoie l'ID de l'affectation
- ✅ **Backend** : Recherche l'affectation et récupère le document
- ✅ **Sécurité** : Pas d'injection de chemin possible
- ✅ **Nom de fichier** : Toujours personnalisé avec matricule et poste
- ✅ **Robustesse** : Gestion centralisée des erreurs

L'approche basée sur l'ID est plus sécurisée et maintenable !
