-- 🔧 Mise à jour de la table CGECI_RHPAIE_RUBRIQUE
-- Ajout des champs manquants pour la synchronisation avec le frontend

-- ==========================================
-- 📋 Ajout du champ CODE
-- ==========================================
-- Le champ code est obligatoire dans le frontend (10 caractères max, unique)
-- Pour les données existantes, nous allons générer un code basé sur le libellé

-- Ajout de la colonne CODE (temporairement nullable pour la migration)
ALTER TABLE CGECI_RHPAIE_RUBRIQUE ADD CODE VARCHAR2(10);

-- Génération des codes pour les données existantes
UPDATE CGECI_RHPAIE_RUBRIQUE 
SET CODE = 
    CASE 
        WHEN LENGTH(REGEXP_REPLACE(UPPER(libelle), '[^A-Z0-9]', '')) <= 10 
        THEN REGEXP_REPLACE(UPPER(libelle), '[^A-Z0-9]', '')
        ELSE SUBSTR(REGEXP_REPLACE(UPPER(libelle), '[^A-Z0-9]', ''), 1, 10)
    END
WHERE CODE IS NULL;

-- Gestion des doublons potentiels
DECLARE
    CURSOR c_duplicates IS 
        SELECT CODE, COUNT(*) as cnt 
        FROM CGECI_RHPAIE_RUBRIQUE 
        WHERE CODE IS NOT NULL
        GROUP BY CODE 
        HAVING COUNT(*) > 1;
BEGIN
    FOR rec IN c_duplicates LOOP
        -- Ajouter un suffixe numérique pour les doublons
        UPDATE CGECI_RHPAIE_RUBRIQUE 
        SET CODE = CODE || '_' || ROWNUM
        WHERE CODE = rec.CODE 
        AND ROWNUM <= rec.cnt - 1;
    END LOOP;
END;
/

-- Rendre la colonne NOT NULL et UNIQUE
ALTER TABLE CGECI_RHPAIE_RUBRIQUE MODIFY CODE VARCHAR2(10) NOT NULL;
ALTER TABLE CGECI_RHPAIE_RUBRIQUE ADD CONSTRAINT UK_RUBRIQUE_CODE UNIQUE (CODE);

-- ==========================================
-- 📋 Ajout du champ COTISABLE
-- ==========================================
-- Le champ cotisable est optionnel dans le frontend (boolean, nullable)

ALTER TABLE CGECI_RHPAIE_RUBRIQUE ADD COTISABLE NUMBER(1);

-- Valeur par défaut : TRUE pour les nouvelles rubriques
-- Pour les données existantes, nous mettons NULL pour permettre la configuration manuelle
UPDATE CGECI_RHPAIE_RUBRIQUE 
SET COTISABLE = CASE 
    WHEN etatImposition IN (1, 3) THEN 1  -- Imposable ou Imposable & Non Imposable
    WHEN etatImposition IN (2, 4, 5, 6) THEN 0  -- Non imposable, retenues
    ELSE NULL  -- Laisser NULL pour les cas non définis
END
WHERE COTISABLE IS NULL;

-- ==========================================
-- 📋 Vérification des contraintes
-- ==========================================

-- Vérifier que tous les enregistrements ont un code
SELECT COUNT(*) as rubriques_sans_code 
FROM CGECI_RHPAIE_RUBRIQUE 
WHERE CODE IS NULL;

-- Vérifier l'unicité des codes
SELECT CODE, COUNT(*) as occurrences 
FROM CGECI_RHPAIE_RUBRIQUE 
GROUP BY CODE 
HAVING COUNT(*) > 1;

-- Vérifier les types de rubrique existants
SELECT etatImposition, COUNT(*) as nombre, 
       CASE etatImposition
           WHEN 1 THEN 'Imposable'
           WHEN 2 THEN 'Non Imposable'
           WHEN 3 THEN 'Imposable & Non Imposable'
           WHEN 4 THEN 'Retenue Mutuelle'
           WHEN 5 THEN 'Regularisation'
           WHEN 6 THEN 'Retenue Sociale'
           ELSE 'Autre'
       END as libelle_type
FROM CGECI_RHPAIE_RUBRIQUE 
GROUP BY etatImposition
ORDER BY etatImposition;

-- ==========================================
-- 📋 Statistiques après migration
-- ==========================================

SELECT 
    'Total rubriques' as statut,
    COUNT(*) as valeur
FROM CGECI_RHPAIE_RUBRIQUE
UNION ALL
SELECT 
    'Rubriques avec code' as statut,
    COUNT(*) as valeur
FROM CGECI_RHPAIE_RUBRIQUE 
WHERE CODE IS NOT NULL
UNION ALL
SELECT 
    'Rubriques avec cotisable défini' as statut,
    COUNT(*) as valeur
FROM CGECI_RHPAIE_RUBRIQUE 
WHERE COTISABLE IS NOT NULL
UNION ALL
SELECT 
    'Codes uniques' as statut,
    COUNT(DISTINCT CODE) as valeur
FROM CGECI_RHPAIE_RUBRIQUE;

-- ==========================================
-- 📋 Nettoyage (optionnel)
-- ==========================================
-- Si vous voulez réinitialiser les valeurs cotisable pour configuration manuelle
-- UPDATE CGECI_RHPAIE_RUBRIQUE SET COTISABLE = NULL;

-- ==========================================
-- ✅ Migration terminée
-- ==========================================

/*
NOTES IMPORTANTES :
1. Le champ CODE est maintenant obligatoire et unique (10 caractères max)
2. Le champ COTISABLE est optionnel (NUMBER(1), NULL autorisé)
3. Les valeurs existantes ont été migrées automatiquement
4. Le champ IMPOSABLE est calculé et ne nécessite pas de colonne en base
5. Le mapping type (1-6) correspond à etatImposition existant

TESTS RECOMMANDÉS :
1. Vérifier que l'application frontend peut créer de nouvelles rubriques
2. Tester la modification des champs code et cotisable
3. Valider que le champ imposable est correctement calculé
4. Confirmer l'unicité des codes

ROLLBACK POSSIBLE :
-- Pour annuler la migration :
ALTER TABLE CGECI_RHPAIE_RUBRIQUE DROP CONSTRAINT UK_RUBRIQUE_CODE;
ALTER TABLE CGECI_RHPAIE_RUBRIQUE DROP COLUMN CODE;
ALTER TABLE CGECI_RHPAIE_RUBRIQUE DROP COLUMN COTISABLE;
*/
