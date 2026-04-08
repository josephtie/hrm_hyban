# Diagnostic : Déconnexion entre SQL et API pour les affectations

## 🚨 Problème identifié

**SQL fonctionne** : 
```sql
SELECT * FROM public.cgeci_rhpaie_affectation where personnel_id='112'
```
Retourne des enregistrements (IDs: 126, 2023, 2022)

**API ne retourne rien** :
```json
{
  "rows": [],
  "total": 0,
  "result": "success",
  "status": true,
  "message": "0 affectation(s) trouvee(s) avec succes - Historique complet",
  "errors": []
}
```

## 🔍 Causes possibles identifiées

### 1. Problème de mapping JPA
La requête JPQL `a.personnel.id = :idPersonnel` ne correspond pas au champ SQL `personnel_id`

### 2. Problème de nom de champ
L'entité `Affectation` utilise `personnel.id` mais la base a `personnel_id`

### 3. Problème de configuration JPA
La relation `@ManyToOne` n'est pas correctement configurée

## ✅ Corrections apportées

### 1. Utilisation de la méthode standard qui fonctionne
```java
// Avant (problématique)
@Query("SELECT a FROM Affectation a WHERE a.personnel.id = :idPersonnel ORDER BY a.dateDebut DESC")
List<Affectation> findHistoriqueAffectationsByPersonnel(@Param("idPersonnel") Long idPersonnel);

// Après (utilise findByPersonnelId qui fonctionne)
listAffectation = affectationRepository.findByPersonnelId(idPersonnel);
```

### 2. Ajout de tri manuel
```java
// Ajouter le tri manuel par date de début décroissante
if (listAffectation != null && !listAffectation.isEmpty()) {
    listAffectation.sort((a1, a2) -> {
        if (a1.getDateDebut() == null && a2.getDateDebut() == null) return 0;
        if (a1.getDateDebut() == null) return 1;
        if (a2.getDateDebut() == null) return -1;
        return a2.getDateDebut().compareTo(a1.getDateDebut()); // Décroissant
    });
}
```

### 3. Ajout de logs de débogage
```java
System.out.println("🔍 Recherche des affectations pour personnel ID: " + idPersonnel);
listAffectation = affectationRepository.findByPersonnelId(idPersonnel);
System.out.println("📋 Nombre d'affectations trouvées: " + (listAffectation != null ? listAffectation.size() : 0));
```

## 🧪 Test de débogage

### 1. Vérifier les logs du backend
Après avoir appelé l'API, vérifiez les logs pour :
```
🔍 Recherche des affectations pour personnel ID: 112
📋 Nombre d'affectations trouvées: X
✅ Tri effectué, première affectation: Y
```

### 2. Vérifier la réponse SQL vs API
```sql
-- SQL direct
SELECT id, personnel_id, date_debut, date_fin, observation, statut 
FROM public.cgeci_rhpaie_affectation 
WHERE personnel_id = 112 
ORDER BY date_debut DESC;
```

```bash
# API call
curl -X POST http://192.168.1.11:7200/api/rh/carriere/affectations/lister-par-personnel \
  -H "Content-Type: application/json" \
  -d '{"id": 112}'
```

## 🔧 Solutions alternatives si problème persiste

### Option 1: Requête SQL native
```java
@Query(value = "SELECT * FROM cgeci_rhpaie_affectation WHERE personnel_id = :idPersonnel ORDER BY date_debut DESC", nativeQuery = true)
List<Affectation> findHistoriqueAffectationsByPersonnelNative(@Param("idPersonnel") Long idPersonnel);
```

### Option 2: Spécification du nom de colonne
```java
@Query("SELECT a FROM Affectation a WHERE a.personnel.id = :idPersonnel ORDER BY a.dateDebut DESC")
@JoinColumn(name = "personnel_id")  // Forcer le nom de colonne
private Personnel personnel;
```

### Option 3: Repository method standard avec tri
```java
// Dans le repository
List<Affectation> findByPersonnelIdOrderByDateDebutDesc(Long idPersonnel);

// Dans le service
listAffectation = affectationRepository.findByPersonnelIdOrderByDateDebutDesc(idPersonnel);
```

## 📋 Étapes de validation

1. **Redémarrer le backend** avec les nouvelles modifications
2. **Appeler l'API** avec l'ID 112
3. **Vérifier les logs** pour voir les messages de débogage
4. **Comparer le résultat** avec la requête SQL directe
5. **Si toujours vide**, essayer l'option 1 (requête native)

## 🎯 Objectif final

Que l'API retourne les mêmes données que la requête SQL :
```json
{
  "result": true,
  "status": true,
  "rows": [
    {
      "id": 126,
      "dateDebut": "2025-01-13T00:00:00",
      "dateFin": null,
      "poste": {...},
      "site": {...},
      "statut": true,
      "urlDocument": "...",
      "observation": "...",
      "dateCreation": "2026-03-28T00:43:11.992"
    }
    // ... autres affectations
  ],
  "total": 3,
  "message": "3 affectation(s) trouvee(s) avec succes - Historique complet",
  "errors": []
}
```

Le problème de déconnexion entre SQL et API devrait maintenant être résolu !
