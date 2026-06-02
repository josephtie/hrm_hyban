<template>
  <div class="rubriques-view">
    <!-- Header aligné sur Organisation -->
    <div class="page-header">
      <h1>Gestion des Rubriques de Paie</h1>
      <p>Configuration des éléments de calcul de la paie</p>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire -->
      <div class="sidebar-panel" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Rubrique</h3>
          <el-button @click="closeForm" circle>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <el-form :model="form" label-width="100px" style="padding: 20px 20px 10px 20px; flex: 1; overflow-y: auto;">
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Document /></el-icon>
              Code
            </label>
            <el-input 
              v-model="form.code" 
              placeholder="Code de la rubrique"
              size="large"
              maxlength="10"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Message /></el-icon>
              Libellé
            </label>
            <el-input 
              v-model="form.libelle" 
              placeholder="Nom de la rubrique"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Trophy /></el-icon>
              Nature de la rubrique
            </label>
            <el-select v-model="form.typeRubrique" placeholder="Nature" size="large">
              <el-option label="Gain (Salaire, prime...)" value="GAIN" />
              <el-option label="Retenue (CNPS, impôt...)" value="RETENUE" />
              <el-option label="Charge (Part patronale)" value="CHARGE" />
            </el-select>
          </div>

          <div class="form-group">
            <label class="form-label">Catégorie</label>
            <el-select v-model="form.categorie" size="large">
              <el-option label="Salaire de base" value="BASE" />
              <el-option label="Prime" value="PRIME" />
              <el-option label="Indemnité" value="INDEMNITE" />
              <el-option label="Cotisation sociale" value="COTISATION" />
              <el-option label="Impôt" value="IMPOT" />
            </el-select>
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Money /></el-icon>
              Mode de calcul
            </label>
            <el-select v-model="form.modeCalcul" placeholder="Mode de calcul" size="large">
              <el-option label="Montant fixe" value="FIXE" />
              <el-option label="Pourcentage" value="POURCENTAGE" />
              <el-option label="Taux variable" value="TAUX_VARIABLE" />
              <el-option label="Barème" value="BAREME" />
            </el-select>
          </div>

          <div class="form-group" v-if="form.modeCalcul === 'MONTANT'">
            <label class="form-label">Montant</label>
            <el-input v-model="form.montant" type="number" />
          </div>

          <div class="form-group" v-if="form.modeCalcul === 'TAUX'">
            <label class="form-label">Taux (%)</label>
            <el-input v-model="form.taux" type="number" />
          </div>

          <div class="form-group" v-if="form.modeCalcul === 'FORMULE'">
            <label class="form-label">Formule</label>
            <el-input v-model="form.formule" placeholder="ex: BASE * 0.1" />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Money /></el-icon>
              Valeur par défaut
            </label>
            <el-input 
              v-model="form.valeur" 
              placeholder="Valeur par défaut"
              size="large"
              type="number"
              step="0.01"
            />
          </div>

          <div class="form-group">
            <label class="form-label">Imposition</label>
            <el-select v-model="form.typeImposition" size="large">
              <el-option label="Totalement imposable" value="1" />
              <el-option label="Non imposable" value="2" />
              <el-option label="Partiellement imposable" value="3" />
              <el-option label="Retenue Mutuelle" value="4" />
              <el-option label="Regularisation" value="5" />
              <el-option label="Retenue Patronale" value="6" />
            </el-select>
          </div>

          <div class="form-group" v-if="form.modeCalcul === 'MONTANT'">
            <label class="form-label">Montant</label>
            <el-input v-model="form.montant" type="number" />
          </div>

          <div class="form-group" v-if="form.modeCalcul === 'TAUX'">
            <label class="form-label">Taux (%)</label>
            <el-input v-model="form.taux" type="number" />
          </div>

          <div class="form-group" v-if="form.modeCalcul === 'FORMULE'">
            <label class="form-label">Formule</label>
            <el-input v-model="form.formule" placeholder="ex: BASE * 0.1" />
          </div>

          <div class="form-group">
            <label class="form-label">Afficher sur bulletin</label>
            <el-switch v-model="form.afficherBulletin" />
          </div>

          <div class="form-group">
            <label class="form-label">Actif</label>
            <el-switch v-model="form.active" />
          </div>

          <!-- Champ MtExedent - visible uniquement si typeRubrique = "3" (Imposable & Non Imposable) -->
          <div class="form-group" v-if="form.typeImposition === '3'">
            <label class="form-label">
              <el-icon class="label-icon"><Money /></el-icon>
              Montant excédent
            </label>
            <el-input 
              v-model="form.mtExedent" 
              placeholder="Montant excédent"
              size="large"
              type="number"
              step="0.01"
            />
          </div>

  

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Document /></el-icon>
              Description
            </label>
            <el-input 
              v-model="form.description" 
              type="textarea"
              :rows="3"
              placeholder="Description de la rubrique..."
            />
          </div>

          <div class="form-actions">
            <el-button @click="closeForm" size="large">Annuler</el-button>
            <el-button type="primary" @click="saveForm" size="large">
              {{ isEditing ? 'Mettre à jour' : 'Créer' }}
            </el-button>
          </div>
        </el-form>
      </div>

      <!-- Colonne principale avec le tableau -->
      <div class="main-panel">
        <div class="panel-header">
          <h3>Liste des Rubriques</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle>
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary">
              <el-icon><Plus /></el-icon>
              Nouvelle Rubrique
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une rubrique..."
              style="width: 300px"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>
          
          <div class="toolbar-right">
            <el-select
              v-model="filterType"
              placeholder="Type"
              style="width: 150px"
              clearable
            >
              <el-option label="Salaire base" value="SALAIRE_BASE" />
              <el-option label="Prime" value="PRIME" />
              <el-option label="Indemnité" value="INDEMNITE" />
              <el-option label="Cotisation" value="COTISATION" />
              <el-option label="Impôt" value="IMPOT" />
            </el-select>
            <el-select
              v-model="filterStatut"
              placeholder="Statut"
              style="width: 120px"
              clearable
            >
              <el-option label="Active" :value="true" />
              <el-option label="Inactive" :value="false" />
            </el-select>
            <el-select
              v-model="filterImposable"
              placeholder="Imposable"
              style="width: 120px"
              clearable
            >
              <el-option label="Oui" :value="true" />
              <el-option label="Non" :value="false" />
            </el-select>
          </div>
        </div>

        <!-- Tableau des rubriques -->
        <div class="table-container">
          <el-table 
            :data="filteredRubriques" 
            style="width: 100%"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
               <el-table-column prop="id" label="Id" width="100" sortable>
              <template #default="{ row }">
                <el-tag type="primary" size="large">{{ row.id }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="code" label="Code" width="100" sortable>
              <template #default="{ row }">
                <el-tag type="primary" size="large">{{ row.code }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="libelle" label="Libellé" min-width="200" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><Document /></el-icon>
                  <span>{{ row.libelle }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="typeRubrique" label="Type" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="getTypeColor(row.typeRubrique)" size="large">
                  {{ getTypeLibelle(row.typeRubrique) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="etatImposition" label="État Imposition" width="140" sortable>
              <template #default="{ row }">
                <el-tag :type="getEtatImpositionColor(row.etatImposition)" size="large">
                  {{ getEtatImpositionLibelle(row.etatImposition) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="modeCalcul" label="Mode calcul" width="120" sortable>
              <template #default="{ row }">
                <el-tag type="info" size="small">
                  {{ getModeCalculLibelle(row.modeCalcul) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="valeur" label="Valeur" width="100" sortable>
              <template #default="{ row }">
                <span style="font-weight: 600; color: #67c23a;">
                  {{ formatCurrency(row.valeur) }}
                </span>
              </template>
            </el-table-column>
            
            <el-table-column prop="imposable" label="Imposable" width="90" sortable>
              <template #default="{ row }">
                <el-tag :type="row.imposable ? 'success' : 'warning'" size="small">
                  {{ row.imposable ? 'Oui' : 'Non' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="active" label="Statut" width="90" sortable>
              <template #default="{ row }">
                <el-tag :type="row.active ? 'success' : 'danger'" size="small">
                  {{ row.active ? 'Active' : 'Inactive' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="120" fixed="right">
              <template #default="{ row }">
                <el-button-group>
                  <el-button size="small" @click="editRubrique(row)" type="primary">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" @click="toggleStatut(row)" :type="row.active ? 'warning' : 'success'">
                    <el-icon><SwitchButton /></el-icon>
                  </el-button>
                  <el-button size="small" @click="deleteRubrique(row)" type="danger">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Edit, Delete, Search, Refresh, Close, Document, Message,
  Trophy, Money, SwitchButton
} from '@element-plus/icons-vue'
import { rubriquerestService, type RubriqueRestDto } from '@/services/rubriquerest.service'

const showForm = ref(false)
const isEditing = ref(false)
const searchText = ref('')
const filterType = ref('')
const filterStatut = ref<boolean | null>(null)
const filterImposable = ref<boolean | null>(null)
const selectedRubriques = ref<Rubrique[]>([])

const form = reactive({
  id: 0,
  code: '',
  libelle: '',
  typeRubrique: '',
  categorie: 'GAIN',
  modeCalcul: '',
  valeur: 0,
  montant: undefined,
  taux: undefined,
  formule: '',
  typeImposition: '',
  afficherBulletin: true,
  mtExedent: 0,
  imposable: true,
  cotisable: true,
  active: true,
  description: ''
})

const rubriques = ref<Rubrique[]>([])
const loading = ref(false)

const filteredRubriques = computed(() => {
  console.log('=== FILTER DEBUG ===')
  console.log('rubriques.value:', rubriques.value)
  console.log('searchText:', searchText.value)
  console.log('filterType:', filterType.value)
  console.log('filterStatut:', filterStatut.value)
  
  let filtered = rubriques.value

  // Filtre par texte de recherche
  if (searchText.value) {
    const searchLower = searchText.value.toLowerCase()
    filtered = filtered.filter(rubrique => 
      (rubrique.code && rubrique.code.toLowerCase().includes(searchLower)) ||
      (rubrique.libelle && rubrique.libelle.toLowerCase().includes(searchLower))
    )
    console.log('Après filtre texte:', filtered.length)
  }

  // Filtre par type de rubrique
  if (filterType.value) {
    filtered = filtered.filter(rubrique => rubrique.typeRubrique === filterType.value)
    console.log('Après filtre type:', filtered.length)
  }

  // Filtre par statut (actif/inactif)
  if (filterStatut.value !== null) {
    filtered = filtered.filter(rubrique => rubrique.active === filterStatut.value)
    console.log('Après filtre statut:', filtered.length)
  }

  // Filtre par imposable (oui/non)
  if (filterImposable.value !== null) {
    filtered = filtered.filter((rubrique: any) => rubrique.imposable === filterImposable.value)
    console.log('Après filtre imposable:', filtered.length)
  }

  console.log('Résultat final:', filtered.length)
  console.log('=== FIN FILTER DEBUG ===')
  return filtered
})

const getTypeLibelle = (typeRubrique: string) => {
  const types = {
    'SALAIRE_BASE': 'Salaire base',
    'PRIME': 'Prime',
    'INDEMNITE': 'Indemnité',
    'COTISATION': 'Cotisation',
    'IMPOT': 'Impôt'
  }
  return types[typeRubrique as keyof typeof types] || typeRubrique
}

const getTypeColor = (typeRubrique: string) => {
  const colors = {
    'SALAIRE_BASE': 'primary',
    'PRIME': 'success',
    'INDEMNITE': 'warning',
    'COTISATION': 'info',
    'IMPOT': 'danger'
  }
  return colors[typeRubrique as keyof typeof colors] || 'primary'
}

const getModeCalculLibelle = (mode: string) => {
  const modes = {
    'FIXE': 'Fixe',
    'POURCENTAGE': '%',
    'FORMULE': 'Formule'
  }
  return modes[mode as keyof typeof modes] || mode
}

const getEtatImpositionLibelle = (etatImposition: number) => {
  const etats = {
    1: 'Totalement imposable',
    2: 'Non imposable',
    3: 'Partiellement imposable',
    4: 'Retenue Mutuelle',
    5: 'Regularisation',
    6: 'Retenue Sociale'
  }
  return etats[etatImposition as keyof typeof etats] || 'Non défini'
}

const getEtatImpositionColor = (etatImposition: number) => {
  const colors = {
    1: 'danger',    // Totalement imposable - rouge
    2: 'success',   // Non imposable - vert
    3: 'warning',   // Partiellement imposable - orange
    4: 'info',      // Retenue Mutuelle - bleu
    5: 'primary',   // Regularisation - primaire
    6: 'warning'    // Retenue Sociale - orange
  }
  return colors[etatImposition as keyof typeof colors] || 'info'
}

const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XOF',
    minimumFractionDigits: 0
  }).format(value)
}

const toggleForm = () => {
  showForm.value = !showForm.value
  if (!showForm.value) {
    resetForm()
  }
}

const closeForm = () => {
  showForm.value = false
  isEditing.value = false
  Object.assign(form, {
    id: 0,
    code: '',
    libelle: '',
    type: '',
    modeCalcul: '',
    valeur: 0,
    imposable: true,
    cotisable: true,
    active: true,
    description: ''
  })
}

const resetForm = () => {
  Object.assign(form, {
    id: 0,
    code: '',
    libelle: '',
    type: '',
    modeCalcul: '',
    valeur: 0,
    mtExedent: 0,
    imposable: false,
    cotisable: false,
    active: true,
    description: '',
    categorie: 'GAIN',
    afficherBulletin: true
  })
  isEditing.value = false
}

const refreshData = async () => {
  await loadRubriques()
  ElMessage.success('Données actualisées')
}

const saveForm = async () => {
  if (!form.code || !form.libelle || !form.typeRubrique || !form.categorie) {
    ElMessage.error('Veuillez renseigner le code, le libellé, la nature et la catégorie')
    return
  }

  try {
    loading.value = true
    
    const rubriqueData: RubriqueRestDto = {
      code: form.code,
      libelle: form.libelle,
      typeRubrique: form.typeRubrique,
      typeImposition: form.typeImposition,
      modeCalcul: form.modeCalcul,
      valeur: form.valeur,
      montant: form.montant,
      taux: form.taux,
      formule: form.formule,
      mtExedent: form.mtExedent,
      imposable: form.imposable,
      cotisable: form.cotisable,
      active: form.active,
      description: form.description,
      categorie: form.categorie,
      afficherBulletin: form.afficherBulletin
    }

    if (isEditing.value) {
      // POUR LA MODIFICATION - Vérification experte de l'ID
      console.log('=== SAVE MODIFICATION DEBUG ===')
      console.log('Form ID:', form.id)
      console.log('Form complet:', JSON.stringify(form, null, 2))
      
      const rubriqueId = form.id
      if (!rubriqueId || rubriqueId === 0) {
        console.error('ERREUR CRITIQUE: ID invalide pour modification:', rubriqueId)
        ElMessage.error('ID de la rubrique invalide pour la modification')
        return
      }
      
      // Forcer l'ID dans les données envoyées
      rubriqueData.id = rubriqueId
      
      console.log('Données envoyées pour modification:', JSON.stringify(rubriqueData, null, 2))
      console.log('ID utilisé pour modification:', rubriqueId)
      
      await rubriquerestService.update(rubriqueId, rubriqueData)
      ElMessage.success('Rubrique mise à jour avec succès')
      console.log('=== FIN SAVE MODIFICATION ===')
    } else {
      // POUR LA CRÉATION
      console.log('=== SAVE CREATION DEBUG ===')
      console.log('Données envoyées pour création:', JSON.stringify(rubriqueData, null, 2))
      
      await rubriquerestService.create(rubriqueData)
      ElMessage.success('Rubrique créée avec succès')
      console.log('=== FIN SAVE CREATION ===')
    }

    await loadRubriques()
    closeForm()
  } catch (error) {
    ElMessage.error('Erreur lors de l\'enregistrement')
    console.error('Error saving rubrique:', error)
  } finally {
    loading.value = false
  }
}

const editRubrique = (rubrique: RubriqueRestDto) => {
  console.log('=== EDIT RUBRIQUE DEBUG ===')
  console.log('Rubrique reçue:', JSON.stringify(rubrique, null, 2))
  
  // Vérifier l'ID en premier
  const rubriqueId = rubrique.id
  console.log('ID extrait:', rubriqueId)
  
  if (!rubriqueId) {
    console.error('ERREUR: ID manquant dans la rubrique à éditer')
    ElMessage.error('ID de la rubrique manquant pour la modification')
    return
  }
  
  // Approche simple et directe - copie profonde avec ID forcé
  const formCopy = {
    id: rubriqueId, // Forcer l'ID
    code: rubrique.code || '',
    libelle: rubrique.libelle || '',
    typeRubrique: rubrique.typeRubrique || 'GAIN',
    categorie: rubrique.categorie || 'BASE',
    modeCalcul: rubrique.modeCalcul || '',
    valeur: rubrique.valeurDef || 0,
    montant: rubrique.montant,
    taux: rubrique.taux,
    formule: rubrique.formule || '',
    typeImposition: getEtatImpositionLibelle(rubrique.etatImposition) || '',
    afficherBulletin: rubrique.afficherBulletin !== undefined ? rubrique.afficherBulletin : true,
    mtExedent: rubrique.mtExedent || 0,
    imposable: rubrique.imposable !== undefined ? rubrique.imposable : true,
    cotisable: rubrique.cotisable !== undefined ? rubrique.cotisable : true,
    active: rubrique.active !== undefined ? rubrique.active : true,
    description: rubrique.description || ''
  }
  
  // Remplacer complètement le formulaire
  Object.keys(form).forEach(key => {
    delete form[key]
  })
  Object.assign(form, formCopy)
  
  console.log('Formulaire après édition:', JSON.stringify(form, null, 2))
  console.log('ID final dans form:', form.id)
  
  isEditing.value = true
  showForm.value = true
  
  console.log('=== FIN EDIT RUBRIQUE ===')
}

const deleteRubrique = async (rubrique: RubriqueRestDto) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer la rubrique "${rubrique.libelle}" ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    )
    
    const rubriqueId = rubrique.id
    if (!rubriqueId) {
      ElMessage.error('ID de la rubrique manquant pour la suppression')
      return
    }
    await rubriquerestService.delete(rubriqueId)
    ElMessage.success('Rubrique supprimée avec succès')
    await loadRubriques()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors de la suppression')
      console.error('Error deleting rubrique:', error)
    }
  }
}

const handleSelectionChange = (selection: RubriqueRestDto[]) => {
  selectedRubriques.value = selection
}

const toggleStatut = async (rubrique: RubriqueRestDto) => {
  try {
    loading.value = true
    const updatedRubrique = {
      ...rubrique,
      active: !rubrique.active
    }
    await rubriquerestService.update(updatedRubrique.id, updatedRubrique)
    ElMessage.success(`Rubrique "${rubrique.libelle}" ${updatedRubrique.active ? 'activée' : 'désactivée'} avec succès`)
    await loadRubriques()
  } catch (error) {
    ElMessage.error('Erreur lors du changement de statut')
    console.error('Error toggling status:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadRubriques()
})

// Charger les données depuis le backend
const loadRubriques = async () => {
  try {
    loading.value = true
    const response = await rubriquerestService.getAll({
      search: searchText.value || undefined,
      size: 50,
      page: 0
    })
    
    console.log('Response from rubrique service:', response)
    console.log('Response length:', response.data?.length || 0)
    
    // DEBUG COMPLET - Analyser la structure du backend
    console.log('=== BACKEND RESPONSE ANALYSIS ===')
    console.log('Response complète:', JSON.stringify(response, null, 2))
    console.log('Response.data:', response.data)
    console.log('Type de response.data:', typeof response.data)
    console.log('Est un tableau?', Array.isArray(response.data))
    
    // Transformer les données du backend pour la vue
    // Le backend renvoie row (objet unique) ou rows (tableau)
    const rubriqueData = response.data.rows || response.data.row || response.data || []
    console.log('RubriqueData brut:', JSON.stringify(rubriqueData, null, 2))
    console.log('Type de rubriqueData:', typeof rubriqueData)
    console.log('Est un tableau?', Array.isArray(rubriqueData))
    
    const rubriqueArray = Array.isArray(rubriqueData) ? rubriqueData : [rubriqueData]
    console.log('RubriqueArray final:', JSON.stringify(rubriqueArray, null, 2))
    
    rubriques.value = rubriqueArray.map((r: any, index: number) => {
      console.log(`--- Rubrique ${index} ---`)
      console.log('Objet brut:', JSON.stringify(r, null, 2))
      console.log('Clés disponibles:', Object.keys(r))
      console.log('ID tests:')
      console.log('  r.id:', r.id)
      console.log('  r.idR:', r.idR)
      console.log('  r.id_r:', r.id_r)
      console.log('  r.rubriqueId:', r.rubriqueId)
      console.log('  r.rubrique_id:', r.rubrique_id)
      
      // Trouver l'ID correct en testant plusieurs possibilités
      const rubriqueId = r.id || r.idR || r.id_r || r.rubriqueId || r.rubrique_id || 0
      console.log('ID final utilisé:', rubriqueId)
      
      return {
        id: rubriqueId,
        code: r.code || '',
        libelle: r.libelle || '',
        typeRubrique: r.typeRubrique || 'GAIN',
        categorie: r.categorie || 'BASE',
        modeCalcul: r.modeCalcul || '',
        valeur: r.valeurDef || 0,
        montant: r.montant,
        taux: r.taux,
        formule: r.formule,
        typeImposition: r.typeImposition,
        etatImposition: r.etatImposition,
        afficherBulletin: r.afficherBulletin !== undefined ? r.afficherBulletin : true,
        mtExedent: r.mtExedent || 0,
        imposable: r.imposable !== undefined ? r.imposable : true,
        cotisable: r.cotisable !== undefined ? r.cotisable : true,
        active: r.active !== undefined ? r.active : true,
        description: r.description || '',
        dateCreation: r.createdAt ? new Date(r.createdAt) : new Date()
      }
    })
    
    console.log('Rubriques finales:', JSON.stringify(rubriques.value, null, 2))
    console.log('=== FIN BACKEND RESPONSE ANALYSIS ===')
    
    console.log('Rubriques loaded:', rubriques.value)
  } catch (error) {
    console.error('Error loading rubriques:', error)
    ElMessage.error('Erreur lors du chargement des rubriques')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.rubriques-view {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0 0 5px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
}

.main-content {
  display: flex;
  gap: 20px;
  height: calc(100vh - 120px);
}

.sidebar-panel {
  width: 400px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.panel-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.panel-controls {
  display: flex;
  gap: 10px;
}

.main-panel {
  flex: 1;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.toolbar-right {
  display: flex;
  gap: 10px;
}

.table-container {
  flex: 1;
  overflow: hidden;
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-weight: 500;
  color: #303133;
  font-size: 14px;
}

.label-icon {
  color: #909399;
  font-size: 16px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.form-group :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #dcdfe6;
  transition: all 0.3s ease;
}

.form-group :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc;
}

.form-group :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.form-group :deep(.el-textarea__inner) {
  border-radius: 8px;
  resize: none;
}

.form-group :deep(.el-textarea__inner:focus) {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.form-group :deep(.el-radio-group) {
  display: flex;
  gap: 20px;
}

.form-group :deep(.el-input__prefix) {
  color: #909399;
}
</style>
