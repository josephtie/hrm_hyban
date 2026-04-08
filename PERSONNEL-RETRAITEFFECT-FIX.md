# Correction du filtre retraitEffect = false

## 🚨 Problème identifié
L'endpoint `/api/personnels/personnel/list` ne filtrait pas sur `p.retraitEffect = false`, donc retournait tous les personnels même ceux ayant quitté l'entreprise.

## ✅ Solution appliquée

### Modification dans `PersonnelServiceImpl.java`

Dans la méthode `findAllfilter()`, j'ai ajouté le filtre obligatoire :

```java
// AJOUTER LE FILTRE IMPORTANT : retraitEffect = false
Specification<Personnel> retraitEffectSpec = (root, query, criteriaBuilder) -> {
    return criteriaBuilder.equal(root.get("retraitEffect"), false);
};
specification = specification.and(retraitEffectSpec);
```

### Position du filtre
- **Placé au début** de la chaîne de spécifications
- **Appliqué systématiquement** pour toutes les requêtes
- **Combiné avec les autres filtres** (recherche, statut, etc.)

## 🔍 Fonctionnement

Maintenant, la méthode `findAllfilter()` :
1. ✅ **Filtre toujours** sur `retraitEffect = false`
2. ✅ **Applique ensuite** les filtres de recherche
3. ✅ **Combine avec** les filtres additionnels (service, fonction, etc.)
4. ✅ **Retourne uniquement** les personnels actifs

## 🧪 Test

Pour vérifier la correction :

```bash
# Test de l'endpoint avec filtre
curl -X POST http://192.168.1.11:7200/api/personnels/personnel/list \
  -H "Content-Type: application/json" \
  -d '{
    "offset": 0,
    "limit": 10,
    "search": ""
  }'
```

## 📋 Résultat attendu

- ❌ **Avant** : Retournait tous les personnels (actifs + partis)
- ✅ **Après** : Retourne uniquement les personnels avec `retraitEffect = false`

## 🔄 Impact sur l'application

### Frontend
- La liste du personnel n'affichera que les employés actifs
- Les recherches ne retourneront que les personnels actifs
- Les exports n'incluront que les personnels actifs

### Backend
- Tous les endpoints utilisant `findAllfilter()` sont impactés :
  - `/api/personnels/personnel/list`
  - `/api/personnels/personnel/export`
  - Autres endpoints de filtrage

## ✅ Vérification

Après redéploiement :
1. Les personnels partis n'apparaissent plus dans la liste
2. Les recherches ne retournent que des personnels actifs
3. Les exports n'incluent que les personnels actifs
4. Les autres filtres (service, fonction, statut) fonctionnent toujours

Le filtre `retraitEffect = false` est maintenant appliqué systématiquement !
