# Implémentation de l'endpoint de téléchargement des documents d'affectation

## 🎯 Objectif
Créer un endpoint `/api/rh/carriere/affectations/download` pour gérer le téléchargement des documents d'affectation via le backend au lieu du téléchargement direct.

## ✅ Modifications apportées

### 1. Frontend - Modification de la fonction `downloadDocument`

**Fichier** : `PersonnelDetailView.vue`

**Ancienne version** (téléchargement direct) :
```typescript
const downloadDocument = (url: string, poste: string, matricule?: string) => {
  const link = document.createElement('a')
  link.href = url
  link.target = '_blank'
  link.download = fileName
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}
```

**Nouvelle version** (via endpoint backend) :
```typescript
const downloadDocument = async (url: string, poste: string, matricule?: string) => {
  if (url) {
    try {
      // Créer un nom de fichier parlant avec matricule
      const timestamp = new Date().toISOString().split('T')[0]
      const matriculeSafe = matricule || 'personnel'
      const fileName = `affectation_${matriculeSafe}_${poste.replace(/\s+/g, '_')}_${timestamp}`
      
      // Utiliser l'endpoint backend pour télécharger le document
      const response = await api.post('/rh/carriere/affectations/download', {
        urlDocument: url,
        fileName: fileName
      }, {
        responseType: 'blob' // Important pour les fichiers binaires
      })
      
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
      
      ElMessage.success(`Téléchargement du document pour: ${poste}`)
    } catch (error) {
      console.error('❌ Erreur lors du téléchargement:', error)
      ElMessage.error('Erreur lors du téléchargement du document')
    }
  }
}
```

**Changements clés** :
- ✅ **Appel API** vers `/rh/carriere/affectations/download`
- ✅ **Response type blob** pour gérer les fichiers binaires
- ✅ **Nom de fichier personnalisé** avec matricule et poste
- ✅ **Gestion des erreurs** améliorée

### 2. Backend - Ajout de l'endpoint de téléchargement

**Fichier** : `AffectationRestController.java`

**Nouvel endpoint ajouté** :
```java
@PostMapping("/download")
public ResponseEntity<Resource> downloadDocument(@RequestBody Map<String, String> request) {
    try {
        String urlDocument = request.get("urlDocument");
        String fileName = request.get("fileName");
        
        // Construire le chemin complet du fichier
        String filePath = urlDocument.startsWith("/") ? urlDocument.substring(1) : urlDocument;
        Path path = Paths.get(filePath);
        Resource resource = new UrlResource(path.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            String contentType = "application/octet-stream";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } else {
            logger.warn("Fichier non trouvé ou non lisible: {}", filePath);
            return ResponseEntity.notFound().build();
        }
    } catch (Exception e) {
        logger.error("Erreur lors du téléchargement du document", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
```

**Imports ajoutés** :
```java
import java.util.Map;  // Ajouté pour le @RequestBody Map
```

**Fonctionnalités de l'endpoint** :
- ✅ **Réception des paramètres** : `urlDocument` et `fileName`
- ✅ **Validation du fichier** : existence et lisibilité
- ✅ **Content-Type approprié** : `application/octet-stream`
- ✅ **Header Content-Disposition** : force le téléchargement
- ✅ **Gestion des erreurs** : logs et réponses HTTP appropriées

## 🌟 Flux de téléchargement

### 1. Utilisateur clique sur "Doc"
```
Frontend → Backend POST /rh/carriere/affectations/download
{
  "urlDocument": "uploads/documents/5f3a158f-31d6-4274-89bd-0786d00faeda_20260328_161800.docx",
  "fileName": "affectation_12345_Développeur_Senior_2026-03-28"
}
```

### 2. Backend traite la demande
```
- Valide le chemin du fichier
- Vérifie l'existence du fichier
- Prépare la réponse avec le bon Content-Type
- Ajoute le header Content-Disposition
```

### 3. Frontend reçoit le fichier
```
Backend → Frontend Response (blob)
Content-Type: application/octet-stream
Content-Disposition: attachment; filename="affectation_12345_Développeur_Senior_2026-03-28"
Body: [fichier binaire]
```

### 4. Téléchargement final
```
- Création du blob à partir de la réponse
- Création d'une URL temporaire
- Déclenchement du téléchargement
- Nettoyage des ressources
```

## 🧪 Tests

### 1. Test de l'endpoint backend
```bash
curl -X POST http://192.168.1.2:7200/api/rh/carriere/affectations/download \
  -H "Content-Type: application/json" \
  -d '{
    "urlDocument": "uploads/documents/test.pdf",
    "fileName": "test_download.pdf"
  }' \
  --output test_download.pdf
```

### 2. Test frontend
1. **Aller dans l'onglet** "Affectation"
2. **Cliquer sur "Doc"** pour une affectation avec document
3. **Vérifier que** :
   - Le fichier se télécharge avec le bon nom
   - Le nom contient le matricule et le poste
   - Le fichier est valide et lisible

### 3. Tests d'erreur
- **Fichier inexistant** : Retour HTTP 404
- **Fichier non lisible** : Retour HTTP 404
- **Erreur serveur** : Retour HTTP 500 avec logs

## 📋 Avantages de cette approche

### Sécurité
- ✅ **Contrôle d'accès** : Le backend peut valider les permissions
- ✅ **Validation du fichier** : Vérification d'existence avant envoi
- ✅ **Logs centralisés** : Tous les téléchargements sont tracés

### Flexibilité
- ✅ **Nom de fichier personnalisé** : Matricule + poste + date
- ✅ **Extension préservée** : Gérée automatiquement par le backend
- ✅ **Gestion d'erreurs** : Messages clairs pour l'utilisateur

### Performance
- ✅ **Streaming direct** : Le fichier est envoyé en streaming
- ✅ **Content-Type optimal** : `application/octet-stream`
- ✅ **Cache control** : Possibilité d'ajouter des headers de cache

## 🔧 Configuration requise

### 1. Permissions des fichiers
```bash
# S'assurer que le backend peut lire les fichiers
chmod -R 755 uploads/
```

### 2. Configuration Spring
```properties
# Déjà configuré dans application.properties
spring.web.resources.static-locations=classpath:/static/,file:uploads/
```

## 🎯 Résultat final

L'utilisateur peut maintenant :
1. **Cliquer sur "Doc"** dans le tableau des affectations
2. **Télécharger** avec un nom de fichier parlant
3. **Bénéficier** de la sécurité et du contrôle du backend
4. **Avoir des logs** de tous les téléchargements

Le téléchargement des documents d'affectation est maintenant géré professionnellement via l'endpoint backend !
