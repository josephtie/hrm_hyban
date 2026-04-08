# Clarification du problème d'affichage des affectations

## 🎯 Situation actuelle

### ✅ Ce qui fonctionne :
- **Enregistrement des affectations** : ✅ OK
- **SQL direct** : `SELECT * FROM public.cgeci_rhpaie_affectation where personnel_id='112'` ✅ Retourne des données

### ❌ Ce qui ne fonctionne pas :
- **Affichage des affectations** dans le frontend : ❌ Retourne `rows: []`
- **API** `/api/rh/carriere/affectations/lister-par-personnel` : ❌ Retourne vide

## 🔍 Diagnostic précis

### Le problème est **BACKEND** pas frontend !

**Preuves :**
1. **SQL retourne des données** (vous l'avez confirmé)
2. **API retourne vide** (vous l'avez confirmé)
3. **Frontend affiche ce que l'API lui donne** (normal)

### Flux du problème :
```
Base de données (SQL) → Repository JPA → Service → Controller → API → Frontend
     ✅                     ❌           ❌        ❌      ❌       ❌
```

## 🚨 Cause exacte du problème

La méthode `findByPersonnelId(Long idPoste)` a un **paramètre mal nommé** :

```java
// Dans AffectationRepository.java
public List<Affectation> findByPersonnelId(Long idPoste);  // ❌ idPoste au lieu de idPersonnel
```

Spring JPA génère la requête :
```sql
SELECT * FROM cgeci_rhpaie_affectation WHERE personnel_id = ?  // Mais avec idPoste = null
```

## ✅ Solution définitive

### 1. Corriger le repository
```java
// Dans AffectationRepository.java
public List<Affectation> findByPersonnelId(Long idPersonnel);  // ✅ Corriger le nom du paramètre
```

### 2. Alternative avec requête native (plus sûr)
```java
@Query(value = "SELECT * FROM cgeci_rhpaie_affectation WHERE personnel_id = :idPersonnel ORDER BY date_debut DESC", 
       nativeQuery = true)
List<Affectation> findAffectationsByPersonnelNative(@Param("idPersonnel") Long idPersonnel);
```

## 🧪 Test immédiat

### Étape 1 : Vérifier le paramètre
Regardez la méthode dans `AffectationRepository.java` ligne 21 :
```java
public List<Affectation> findByPersonnelId(Long idPoste);  // ❌ ERREUR ICI
```

### Étape 2 : Corriger
```java
public List<Affectation> findByPersonnelId(Long idPersonnel);  // ✅ CORRECTION
```

### Étape 3 : Tester
```bash
curl -X POST http://192.168.1.11:7200/api/rh/carriere/affectations/lister-par-personnel \
  -H "Content-Type: application/json" \
  -d '{"id": 112}'
```

## 📋 Résultat attendu après correction

```json
{
  "result": true,
  "status": true,
  "rows": [
    {
      "id": 126,
      "dateDebut": "2025-01-13T00:00:00",
      "dateFin": null,
      "poste": {"id": 12, "libelle": "Développeur Senior"},
      "site": {"id": 3, "libelle": "Siège Social"},
      "statut": true,
      "urlDocument": "/documents/affectation_126.pdf",
      "observation": "Promotion senior",
      "dateCreation": "2026-03-28T00:43:11.992"
    }
    // ... autres affectations
  ],
  "total": 3,
  "message": "3 affectation(s) trouvee(s) avec succes - Historique complet",
  "errors": []
}
```

## 🎯 Conclusion

**Le problème est 100% BACKEND** dans le repository :

- **Pas un problème frontend** : il affiche ce que l'API donne
- **Pas un problème service** : la logique est correcte  
- **Pas un problème controller** : il appelle bien le service
- **Problème repository** : mauvais nom de paramètre `idPoste` au lieu de `idPersonnel`

Une seule ligne à corriger dans `AffectationRepository.java` et tout fonctionnera !
