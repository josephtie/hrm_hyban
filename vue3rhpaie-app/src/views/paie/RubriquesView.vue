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
              Type de rubrique
            </label>
            <el-select v-model="form.type" placeholder="Type de rubrique" size="large">
              <el-option label="Imposable" value="1" />
              <el-option label="Non Imposable" value="2" />
              <el-option label="Imposable & Non Imposable" value="3" />
              <el-option label="Retenue Mutuelle" value="4" />
              <el-option label="Regularisation" value="5" />
              <el-option label="Retenue Sociale" value="6" />
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

          <!-- Champ MtExedent - visible uniquement si type = "3" (Imposable & Non Imposable) -->
          <div class="form-group" v-if="form.type === '3'">
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
              <el-icon class="label-icon"><SwitchButton /></el-icon>
              Imposable
            </label>
            <el-radio-group v-model="form.imposable" size="large">
              <el-radio :value="true">Oui</el-radio>
              <el-radio :value="false">Non</el-radio>
            </el-radio-group>
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><SwitchButton /></el-icon>
              Cotisable
            </label>
            <el-radio-group v-model="form.cotisable" size="large">
              <el-radio :value="true">Oui</el-radio>
              <el-radio :value="false">Non</el-radio>
            </el-radio-group>
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><SwitchButton /></el-icon>
              Rubrique active
            </label>
            <el-radio-group v-model="form.active" size="large">
              <el-radio :value="true">Oui</el-radio>
              <el-radio :value="false">Non</el-radio>
            </el-radio-group>
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
              <el-option label="Imposable" value="1" />
              <el-option label="Non Imposable" value="2" />
              <el-option label="Imposable & Non Imposable" value="3" />
              <el-option label="Retenue Mutuelle" value="4" />
              <el-option label="Regularisation" value="5" />
              <el-option label="Retenue Sociale" value="6" />
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
            
            <el-table-column prop="type" label="Type" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="getTypeColor(row.type)" size="large">
                  {{ getTypeLibelle(row.type) }}
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
            
            <el-table-column prop="cotisable" label="Cotisable" width="90" sortable>
              <template #default="{ row }">
                <el-tag :type="row.cotisable ? 'success' : 'warning'" size="small">
                  {{ row.cotisable ? 'Oui' : 'Non' }}
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
import { rubriqvariablerestService, type RubriqueRestDto } from '@/services/rubriqvariablerest.service'

interface Rubrique {
  id: number
  code: string
  libelle: string
  type: string
  modeCalcul: string
  valeur: number
  mtExedent: number
  imposable: boolean
  cotisable: boolean
  active: boolean
  description: string
  dateCreation: Date
}

const showForm = ref(false)
const isEditing = ref(false)
const searchText = ref('')
const filterType = ref('')
const filterStatut = ref<boolean | null>(null)
const selectedRubriques = ref<Rubrique[]>([])

const form = reactive({
  id: 0,
  code: '',
  libelle: '',
  type: '',
  modeCalcul: '',
  valeur: 0,
  mtExedent: 0,
  imposable: true,
  cotisable: true,
  active: true,
  description: '',
  categorie: 'GAIN',
  afficherBulletin: true
})

const rubriques = ref<Rubrique[]>([])
const loading = ref(false)

const filteredRubriques = computed(() => {
  let filtered = rubriques.value

  if (searchText.value) {
    filtered = filtered.filter(rubrique => 
      rubrique.code.toLowerCase().includes(searchText.value.toLowerCase()) ||
      rubrique.libelle.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  if (filterType.value) {
    filtered = filtered.filter(rubrique => rubrique.type === filterType.value)
  }

  if (filterStatut.value !== null) {
    filtered = filtered.filter(rubrique => rubrique.active === filterStatut.value)
  }

  return filtered
})

const getTypeLibelle = (type: string) => {
  const types = {
    'SALAIRE_BASE': 'Salaire base',
    'PRIME': 'Prime',
    'INDEMNITE': 'Indemnité',
    'AVANTAGE': 'Avantage',
    'RETENUE': 'Retenue',
    'COTISATION': 'Cotisation',
    'IMPOT': 'Impôt'
  }
  return types[type as keyof typeof types] || type
}

const getTypeColor = (type: string) => {
  const colors = {
    'SALAIRE_BASE': 'primary',
    'PRIME': 'success',
    'INDEMNITE': 'info',
    'AVANTAGE': 'warning',
    'RETENUE': 'danger',
    'COTISATION': 'warning',
    'IMPOT': 'danger'
  }
  return colors[type as keyof typeof colors] || 'info'
}

const getModeCalculLibelle = (mode: string) => {
  const modes = {
    'FIXE': 'Fixe',
    'POURCENTAGE': '%',
    'TAUX_VARIABLE': 'Variable',
    'BAREME': 'Barème'
  }
  return modes[mode as keyof typeof modes] || mode
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
  if (!form.code || !form.libelle || !form.type) {
    ElMessage.error('Veuillez renseigner le code, le libellé et le type')
    return
  }

  try {
    loading.value = true
    
    const rubriqueData: RubriqueRestDto = {
      code: form.code,
      libelle: form.libelle,
      type: form.type,
      etatImposition: parseInt(form.type),
      modeCalcul: form.modeCalcul,
      valeurDef: form.valeur,
      mtExedent: form.mtExedent,
      imposable: form.imposable,
      cotisable: form.cotisable,
      active: form.active,
      description: form.description,
      categorie: form.categorie,
      afficherBulletin: form.afficherBulletin
    }

    if (isEditing.value) {
      // Pour la modification, ajouter l'ID
      rubriqueData.idR = form.id
      await rubriqvariablerestService.update(rubriqueData)
      ElMessage.success('Rubrique mise à jour avec succès')
    } else {
      // Pour la création, pas d'ID
      await rubriqvariablerestService.create(rubriqueData)
      ElMessage.success('Rubrique créée avec succès')
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

const editRubrique = (rubrique: Rubrique) => {
  Object.assign(form, {
    id: rubrique.id,
    code: rubrique.code,
    libelle: rubrique.libelle,
    type: rubrique.type,
    modeCalcul: rubrique.modeCalcul,
    valeur: rubrique.valeur,
    mtExedent: rubrique.mtExedent,
    imposable: rubrique.imposable,
    cotisable: rubrique.cotisable,
    active: rubrique.active,
    description: rubrique.description,
    categorie: 'GAIN',
    afficherBulletin: true
  })
  isEditing.value = true
  showForm.value = true
}

const deleteRubrique = async (rubrique: Rubrique) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer la rubrique "${rubrique.libelle}" ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    )
    
    await rubriqvariablerestService.delete(rubrique.id)
    ElMessage.success('Rubrique supprimée avec succès')
    await loadRubriques()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors de la suppression')
      console.error('Error deleting rubrique:', error)
    }
  }
}

const handleSelectionChange = (selection: Rubrique[]) => {
  selectedRubriques.value = selection
}

const toggleStatut = async (rubrique: Rubrique) => {
  try {
    loading.value = true
    const updatedRubrique = {
      ...rubrique,
      active: !rubrique.active
    }
    await rubriqvariablerestService.update(updatedRubrique)
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
    const response = await rubriqvariablerestService.getAll({
      search: searchText.value || undefined,
      size: 50,
      page: 0
    })
    
    console.log('Response from rubrique service:', response)
    console.log('Response length:', response.data?.length || 0)
    
    // Transformer les données du backend pour la vue
    rubriques.value = response.data.map((r: any) => {
      return {
        id: r.idR || 0,
        code: r.code || '',
        libelle: r.libelle || '',
        type: r.type || String(r.etatImposition || 1),
        modeCalcul: r.modeCalcul || '',
        valeur: r.valeurDef || 0,
        mtExedent: r.mtExedent || 0,
        imposable: r.imposable !== undefined ? r.imposable : true,
        cotisable: r.cotisable !== undefined ? r.cotisable : true,
        active: r.active !== undefined ? r.active : true,
        description: r.description || '',
        dateCreation: r.createdAt ? new Date(r.createdAt) : new Date()
      }
    })
    
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
