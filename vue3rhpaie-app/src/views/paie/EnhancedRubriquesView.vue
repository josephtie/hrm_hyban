<template>
  <div class="enhanced-rubriques-view">
    <!-- Header amélioré -->
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Gestion des Rubriques de Paie</h1>
          <p class="page-subtitle">Configuration des éléments de calcul de la paie</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ rubriques.length }}</div>
            <div class="stat-label">Total rubriques</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ rubriquesActives }}</div>
            <div class="stat-label">Actives</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire amélioré -->
      <div class="sidebar-panel enhanced-card" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Rubrique</h3>
          <el-button @click="closeForm" circle size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <!-- Formulaire amélioré -->
        <EnhancedForm
          v-model="form"
          :title="isEditing ? 'Modification de rubrique' : 'Nouvelle rubrique'"
          subtitle="Configurez les éléments de paie"
          :show-header="true"
          :show-progress="true"
          :loading="loading"
          loading-text="Enregistrement en cours..."
          @submit="saveForm"
          @reset="resetForm"
          @cancel="closeForm"
          class="rubrique-form"
        >
          <template #default>
            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Document /></el-icon>
                Informations générales
              </h4>
              
              <el-form-item label="Code" required class="form-required">
                <el-input
                  v-model="form.code"
                  placeholder="Code de la rubrique"
                  maxlength="10"
                  show-word-limit
                  size="large"
                  class="enhanced-input"
                >
                  <template #prefix>
                    <el-icon><Flag /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item label="Libellé" required class="form-required">
                <el-input
                  v-model="form.libelle"
                  placeholder="Nom de la rubrique"
                  size="large"
                  class="enhanced-input"
                >
                  <template #prefix>
                    <el-icon><Message /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item label="Type de rubrique" required class="form-required">
                <el-select v-model="form.type" placeholder="Type de rubrique" size="large" class="enhanced-input">
                  <el-option label="Salaire de base" value="SALAIRE_BASE" />
                  <el-option label="Prime" value="PRIME" />
                  <el-option label="Indemnité" value="INDEMNITE" />
                  <el-option label="Avantage" value="AVANTAGE" />
                  <el-option label="Retenue" value="RETENUE" />
                  <el-option label="Cotisation" value="COTISATION" />
                  <el-option label="Impôt" value="IMPOT" />
                </el-select>
              </el-form-item>
            </div>

            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Money /></el-icon>
                Calcul et valeur
              </h4>
              
              <el-form-item label="Mode de calcul" required class="form-required">
                <el-select v-model="form.modeCalcul" placeholder="Mode de calcul" size="large" class="enhanced-input">
                  <el-option label="Montant fixe" value="FIXE" />
                  <el-option label="Pourcentage" value="POURCENTAGE" />
                  <el-option label="Taux variable" value="TAUX_VARIABLE" />
                  <el-option label="Barème" value="BAREME" />
                </el-select>
              </el-form-item>

              <el-form-item label="Valeur par défaut">
                <el-input
                  v-model="form.valeur"
                  placeholder="Valeur par défaut"
                  size="large"
                  type="number"
                  step="0.01"
                  class="enhanced-input"
                >
                  <template #prefix>
                    <el-icon><Money /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </div>

            <div class="form-section">
              <h4 class="section-title">
                <el-icon><SwitchButton /></el-icon>
                Options fiscales et sociales
              </h4>
              
              <el-form-item label="Imposable">
                <el-radio-group v-model="form.imposable" size="large" class="enhanced-radio">
                  <el-radio :value="true">Oui</el-radio>
                  <el-radio :value="false">Non</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="Cotisable">
                <el-radio-group v-model="form.cotisable" size="large" class="enhanced-radio">
                  <el-radio :value="true">Oui</el-radio>
                  <el-radio :value="false">Non</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="Statut">
                <el-radio-group v-model="form.active" size="large" class="enhanced-radio">
                  <el-radio :value="true">Active</el-radio>
                  <el-radio :value="false">Inactive</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
          </template>
        </EnhancedForm>
      </div>

      <!-- Colonne principale avec le tableau amélioré -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Rubriques</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle class="enhanced-button">
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary" class="enhanced-button">
              <el-icon><Plus /></el-icon>
              Nouvelle Rubrique
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils améliorée -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une rubrique..."
              style="width: 300px"
              clearable
              size="large"
              class="enhanced-input"
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
              size="large"
              class="enhanced-input"
            >
              <el-option label="Salaire de base" value="SALAIRE_BASE" />
              <el-option label="Prime" value="PRIME" />
              <el-option label="Indemnité" value="INDEMNITE" />
              <el-option label="Avantage" value="AVANTAGE" />
              <el-option label="Retenue" value="RETENUE" />
              <el-option label="Cotisation" value="COTISATION" />
              <el-option label="Impôt" value="IMPOT" />
            </el-select>
            <el-select
              v-model="filterStatut"
              placeholder="Statut"
              style="width: 120px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option label="Active" :value="true" />
              <el-option label="Inactive" :value="false" />
            </el-select>
          </div>
        </div>

        <!-- Tableau amélioré -->
        <div class="table-container">
          <el-table
            :data="filteredRubriques"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            class="enhanced-table"
            :row-class-name="tableRowClassName"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="code" label="Code" width="100" sortable>
              <template #default="{ row }">
                <el-tag type="primary" size="large" class="enhanced-tag">
                  {{ row.code }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="libelle" label="Libellé" min-width="200" sortable>
              <template #default="{ row }">
                <div class="rubrique-info">
                  <el-icon class="rubrique-icon"><Document /></el-icon>
                  <div class="rubrique-details">
                    <div class="rubrique-name">{{ row.libelle }}</div>
                    <div class="rubrique-mode">{{ getModeCalculLibelle(row.modeCalcul) }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="type" label="Type" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="getTypeColor(row.type)" size="large" class="enhanced-tag">
                  {{ getTypeLibelle(row.type) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="valeur" label="Valeur" width="100" sortable>
              <template #default="{ row }">
                <span class="valeur-text">{{ formatCurrency(row.valeur) }}</span>
              </template>
            </el-table-column>
            
            <el-table-column prop="imposable" label="Imposable" width="90" sortable>
              <template #default="{ row }">
                <el-tag :type="row.imposable ? 'success' : 'warning'" size="small" class="enhanced-tag">
                  {{ row.imposable ? 'Oui' : 'Non' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="cotisable" label="Cotisable" width="90" sortable>
              <template #default="{ row }">
                <el-tag :type="row.cotisable ? 'success' : 'warning'" size="small" class="enhanced-tag">
                  {{ row.cotisable ? 'Oui' : 'Non' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="active" label="Statut" width="90" sortable>
              <template #default="{ row }">
                <el-tag :type="row.active ? 'success' : 'danger'" size="small" class="enhanced-tag">
                  {{ row.active ? 'Active' : 'Inactive' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="180" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button
                    size="small"
                    @click="editRubrique(row)"
                    type="primary"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="toggleStatut(row)"
                    :type="row.active ? 'warning' : 'success'"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><SwitchButton /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="deleteRubrique(row)"
                    type="danger"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
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
  Trophy, Money, SwitchButton, Flag
} from '@element-plus/icons-vue'
import EnhancedForm from '@/components/common/EnhancedForm.vue'

interface Rubrique {
  id: number
  code: string
  libelle: string
  type: string
  modeCalcul: string
  valeur: number
  imposable: boolean
  cotisable: boolean
  active: boolean
  description: string
  dateCreation: Date
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
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
  imposable: true,
  cotisable: true,
  active: true,
  description: ''
})

const rubriques = ref<Rubrique[]>([
  {
    id: 1,
    code: 'SAL_BASE',
    libelle: 'Salaire de base',
    type: 'SALAIRE_BASE',
    modeCalcul: 'FIXE',
    valeur: 200000,
    imposable: true,
    cotisable: true,
    active: true,
    description: 'Salaire de base mensuel',
    dateCreation: new Date('2024-01-01')
  },
  {
    id: 2,
    code: 'PRIME_LOG',
    libelle: 'Prime de logement',
    type: 'PRIME',
    modeCalcul: 'FIXE',
    valeur: 50000,
    imposable: true,
    cotisable: true,
    active: true,
    description: 'Prime mensuelle de logement',
    dateCreation: new Date('2024-01-01')
  }
])

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

const rubriquesActives = computed(() => {
  return rubriques.value.filter(r => r.active).length
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

const tableRowClassName = ({ row }: { row: Rubrique }) => {
  return row.active ? 'row-active' : 'row-inactive'
}

const toggleForm = () => {
  showForm.value = !showForm.value
  if (!showForm.value) resetForm()
}

const closeForm = () => {
  showForm.value = false
  resetForm()
}

const resetForm = () => {
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
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.code || !form.libelle || !form.type || !form.modeCalcul) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    if (isEditing.value) {
      const index = rubriques.value.findIndex(r => r.id === form.id)
      if (index !== -1) {
        rubriques.value[index] = { ...form, dateCreation: rubriques.value[index].dateCreation }
        ElMessage.success('Rubrique mise à jour avec succès')
      }
    } else {
      const newRubrique: Rubrique = { ...form, id: Date.now(), dateCreation: new Date() }
      rubriques.value.unshift(newRubrique)
      ElMessage.success('Rubrique créée avec succès')
    }
    closeForm()
  } catch (error) {
    ElMessage.error('Une erreur est survenue')
  } finally {
    loading.value = false
  }
}

const editRubrique = (rubrique: Rubrique) => {
  Object.assign(form, rubrique)
  isEditing.value = true
  showForm.value = true
}

const toggleStatut = (rubrique: Rubrique) => {
  const action = rubrique.active ? 'désactiver' : 'activer'
  ElMessageBox.confirm(
    `Êtes-vous sûr de vouloir ${action} la rubrique ${rubrique.libelle} ?`,
    'Confirmation',
    { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
  ).then(() => {
    rubrique.active = !rubrique.active
    ElMessage.success(`Rubrique ${action} avec succès`)
  })
}

const deleteRubrique = (rubrique: Rubrique) => {
  ElMessageBox.confirm(
    `Êtes-vous sûr de vouloir supprimer la rubrique ${rubrique.libelle} ?`,
    'Confirmation',
    { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
  ).then(() => {
    const index = rubriques.value.findIndex(r => r.id === rubrique.id)
    if (index !== -1) {
      rubriques.value.splice(index, 1)
      ElMessage.success('Rubrique supprimée avec succès')
    }
  })
}

const refreshData = () => {
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: Rubrique[]) => {
  selectedRubriques.value = selection
}

onMounted(() => {
  // Initialisation
})
</script>

<style lang="scss" scoped>
@use '@/styles/design-system.scss' as *;

.enhanced-rubriques-view {
  padding: var(--spacing-xl);
  background: var(--bg-secondary);
  min-height: 100vh;
}

.page-header {
  margin-bottom: var(--spacing-xl);
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: var(--spacing-xl);
  }
  
  .page-title {
    margin: 0 0 var(--spacing-xs) 0;
    font-size: 32px;
    font-weight: 700;
  }
  
  .page-subtitle {
    margin: 0;
    color: var(--text-secondary);
    font-size: 16px;
  }
  
  .header-stats {
    display: flex;
    gap: var(--spacing-xl);
    
    .stat-item {
      text-align: center;
      
      .stat-value {
        font-size: 28px;
        font-weight: 700;
        color: var(--primary-color);
        margin-bottom: var(--spacing-xs);
      }
      
      .stat-label {
        font-size: 14px;
        color: var(--text-secondary);
      }
    }
  }
}

.main-content {
  display: flex;
  gap: var(--spacing-xl);
  height: calc(100vh - 200px);
}

.sidebar-panel {
  width: 450px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  
  .panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: var(--spacing-lg);
    border-bottom: 1px solid var(--border-light);
    
    h3 {
      margin: 0;
      color: var(--text-primary);
      font-size: 18px;
      font-weight: 600;
    }
  }
  
  .rubrique-form {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
}

.main-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  
  .panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: var(--spacing-lg);
    border-bottom: 1px solid var(--border-light);
    
    h3 {
      margin: 0;
      color: var(--text-primary);
      font-size: 18px;
      font-weight: 600;
    }
    
    .panel-controls {
      display: flex;
      gap: var(--spacing-sm);
    }
  }
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-secondary);
}

.table-container {
  flex: 1;
  overflow: hidden;
  padding: var(--spacing-lg);
}

.form-section {
  margin-bottom: var(--spacing-xl);
  
  .section-title {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
    margin: 0 0 var(--spacing-lg) 0;
    color: var(--text-primary);
    font-size: 16px;
    font-weight: 600;
    padding-bottom: var(--spacing-sm);
    border-bottom: 2px solid var(--border-light);
  }
}

.rubrique-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .rubrique-icon {
    font-size: 20px;
    color: var(--primary-color);
  }
  
  .rubrique-details {
    .rubrique-name {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .rubrique-mode {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.valeur-text {
  font-weight: 600;
  color: var(--success-color);
}

.action-buttons {
  display: flex;
  gap: var(--spacing-xs);
  
  .btn-action {
    padding: 6px;
    border-radius: var(--border-radius-sm);
    
    &:hover {
      transform: scale(1.1);
    }
  }
}

.enhanced-table {
  :deep(.el-table__row) {
    transition: all var(--transition-base);
    
    &:hover {
      background: var(--bg-secondary);
    }
    
    &.row-active {
      background: rgba(103, 194, 58, 0.05);
      
      &:hover {
        background: rgba(103, 194, 58, 0.1);
      }
    }
    
    &.row-inactive {
      background: rgba(245, 108, 108, 0.05);
      
      &:hover {
        background: rgba(245, 108, 108, 0.1);
      }
    }
  }
}

@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }
  
  .sidebar-panel {
    width: 100%;
  }
}
</style>
