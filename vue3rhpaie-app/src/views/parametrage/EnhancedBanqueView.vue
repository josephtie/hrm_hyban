<template>
  <div class="enhanced-banque-view">
    <!-- Header amélioré -->
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Gestion des Banques</h1>
          <p class="page-subtitle">Configuration des établissements bancaires</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ banques.length }}</div>
            <div class="stat-label">Total banques</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ banquesPrincipales }}</div>
            <div class="stat-label">Principales</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire amélioré -->
      <div class="sidebar-panel enhanced-card" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Banque</h3>
          <el-button @click="closeForm" circle size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <!-- Formulaire amélioré -->
        <EnhancedForm
          v-model="form"
          :title="isEditing ? 'Modification de banque' : 'Nouvelle banque'"
          subtitle="Renseignez les informations bancaires"
          :show-header="true"
          :show-progress="true"
          :loading="loading"
          loading-text="Enregistrement en cours..."
          @submit="saveForm"
          @reset="resetForm"
          @cancel="closeForm"
          class="banque-form"
        >
          <template #default>
            <div class="form-section">
              <h4 class="section-title">
                <el-icon><OfficeBuilding /></el-icon>
                Informations générales
              </h4>
              
              <el-form-item label="Sigle" required class="form-required">
                <el-input
                  v-model="form.sigle"
                  placeholder="Ex: SGBCI, ECOBANK"
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

              <el-form-item label="Nom de la banque" required class="form-required">
                <el-input
                  v-model="form.libelle"
                  type="textarea"
                  :rows="3"
                  placeholder="Nom complet de la banque..."
                  size="large"
                  class="enhanced-input"
                >
                  <template #prefix>
                    <el-icon><OfficeBuilding /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item label="Code de banque" required class="form-required">
                <el-input
                  v-model="form.codbanq"
                  placeholder="Code bancaire (5 caractères)"
                  maxlength="5"
                  size="large"
                  class="enhanced-input"
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </div>

            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Star /></el-icon>
                Configuration
              </h4>
              
              <el-form-item label="Type de banque" required class="form-required">
                <el-radio-group v-model="form.statut" size="large" class="enhanced-radio">
                  <el-radio :value="true" class="radio-option">
                    <div class="radio-content">
                      <el-icon><Star /></el-icon>
                      <div>
                        <div class="radio-label">Banque principale</div>
                        <div class="radio-desc">Utilisée pour les opérations principales</div>
                      </div>
                    </div>
                  </el-radio>
                  <el-radio :value="false" class="radio-option">
                    <div class="radio-content">
                      <el-icon><OfficeBuilding /></el-icon>
                      <div>
                        <div class="radio-label">Banque secondaire</div>
                        <div class="radio-desc">Banque auxiliaire</div>
                      </div>
                    </div>
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
          </template>
        </EnhancedForm>
      </div>

      <!-- Colonne principale avec le tableau amélioré -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Banques</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle class="enhanced-button">
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary" class="enhanced-button">
              <el-icon><Plus /></el-icon>
              Nouvelle Banque
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils améliorée -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une banque..."
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
              v-model="filterStatut"
              placeholder="Type"
              style="width: 150px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option label="Banque principale" :value="true" />
              <el-option label="Banque secondaire" :value="false" />
            </el-select>
          </div>
        </div>

        <!-- Tableau amélioré -->
        <div class="table-container">
          <el-table
            :data="filteredBanques"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            class="enhanced-table"
            :row-class-name="tableRowClassName"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="sigle" label="Sigle" width="120" sortable>
              <template #default="{ row }">
                <el-tag type="primary" size="large" class="enhanced-tag">
                  {{ row.sigle }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="libelle" label="Banque" min-width="200" sortable>
              <template #default="{ row }">
                <div class="bank-info">
                  <el-icon class="bank-icon"><OfficeBuilding /></el-icon>
                  <div class="bank-details">
                    <div class="bank-name">{{ row.libelle }}</div>
                    <div class="bank-code">Code: {{ row.codbanq }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="codbanq" label="Code" width="100" sortable>
              <template #default="{ row }">
                <el-tag type="info" size="small" class="enhanced-tag">
                  {{ row.codbanq }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="statut" label="Type" width="140" sortable>
              <template #default="{ row }">
                <el-tag :type="row.statut ? 'success' : 'warning'" size="large" class="enhanced-tag">
                  <el-icon style="margin-right: 4px;">
                    <Star v-if="row.statut" />
                    <OfficeBuilding v-else />
                  </el-icon>
                  {{ row.statut ? 'Principale' : 'Secondaire' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="dateCreation" label="Création" width="120" sortable>
              <template #default="{ row }">
                <div class="date-info">
                  <el-icon><Clock /></el-icon>
                  <span>{{ formatDate(row.dateCreation) }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="180" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button
                    size="small"
                    @click="editBanque(row)"
                    type="primary"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="toggleStatut(row)"
                    :type="row.statut ? 'warning' : 'success'"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Star /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="deleteBanque(row)"
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

    <!-- Modal amélioré pour la confirmation -->
    <EnhancedModal
      v-model="showDeleteModal"
      title="Confirmation de suppression"
      subtitle="Cette action est irréversible"
      type="danger"
      icon="WarningFilled"
      :alerts="deleteAlerts"
      @confirm="confirmDelete"
      @cancel="cancelDelete"
      confirm-text="Supprimer"
      cancel-text="Annuler"
    >
      <div class="delete-confirmation">
        <p>Êtes-vous sûr de vouloir supprimer la banque <strong>{{ selectedBanque?.sigle }}</strong> ?</p>
        <p>Cette action supprimera définitivement toutes les données associées.</p>
      </div>
    </EnhancedModal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, Refresh, Close, OfficeBuilding, CreditCard,
  Star, Clock, Key, Flag, WarningFilled
} from '@element-plus/icons-vue'
import EnhancedForm from '@/components/common/EnhancedForm.vue'
import EnhancedModal from '@/components/common/EnhancedModal.vue'

interface Banque {
  id: number
  sigle: string
  libelle: string
  codbanq: string
  statut: boolean
  dateCreation: Date
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const filterStatut = ref<boolean | null>(null)
const selectedBanques = ref<Banque[]>([])
const showDeleteModal = ref(false)
const selectedBanque = ref<Banque | null>(null)

const form = reactive({
  id: 0,
  sigle: '',
  libelle: '',
  codbanq: '',
  statut: false
})

const banques = ref<Banque[]>([
  {
    id: 1,
    sigle: 'SGBCI',
    libelle: 'Société Générale de Banques en Côte d\'Ivoire',
    codbanq: '00101',
    statut: true,
    dateCreation: new Date('2024-01-01')
  },
  {
    id: 2,
    sigle: 'ECOBANK',
    libelle: 'Ecobank Côte d\'Ivoire',
    codbanq: '00102',
    statut: false,
    dateCreation: new Date('2024-01-15')
  },
  {
    id: 3,
    sigle: 'BIAO',
    libelle: 'Banque Internationale pour l\'Afrique Occidentale',
    codbanq: '00103',
    statut: false,
    dateCreation: new Date('2024-02-01')
  }
])

const filteredBanques = computed(() => {
  let filtered = banques.value

  if (searchText.value) {
    filtered = filtered.filter(banque =>
      banque.sigle.toLowerCase().includes(searchText.value.toLowerCase()) ||
      banque.libelle.toLowerCase().includes(searchText.value.toLowerCase()) ||
      banque.codbanq.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  if (filterStatut.value !== null) {
    filtered = filtered.filter(banque => banque.statut === filterStatut.value)
  }

  return filtered
})

const banquesPrincipales = computed(() => {
  return banques.value.filter(b => b.statut).length
})

const deleteAlerts = computed(() => {
  if (!selectedBanque.value) return []
  
  return [
    {
      type: 'danger' as const,
      title: 'Attention',
      message: 'La suppression est définitive et irréversible',
      closable: false
    }
  ]
})

// Méthodes
const formatDate = (date: Date) => {
  return new Intl.DateTimeFormat('fr-FR').format(date)
}

const tableRowClassName = ({ row }: { row: Banque }) => {
  return row.statut ? 'row-principal' : 'row-secondary'
}

const toggleForm = () => {
  showForm.value = !showForm.value
  if (!showForm.value) {
    resetForm()
  }
}

const closeForm = () => {
  showForm.value = false
  resetForm()
}

const resetForm = () => {
  Object.assign(form, {
    id: 0,
    sigle: '',
    libelle: '',
    codbanq: '',
    statut: false
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.sigle || !form.libelle || !form.codbanq) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  if (form.codbanq.length !== 5) {
    ElMessage.error('Le code de banque doit contenir exactement 5 caractères')
    return
  }

  loading.value = true

  try {
    // Simulation d'une API call
    await new Promise(resolve => setTimeout(resolve, 1000))

    if (isEditing.value) {
      const index = banques.value.findIndex(b => b.id === form.id)
      if (index !== -1) {
        banques.value[index] = {
          ...form,
          dateCreation: banques.value[index].dateCreation
        }
        ElMessage.success('Banque mise à jour avec succès')
      }
    } else {
      const newBanque: Banque = {
        ...form,
        id: Date.now(),
        dateCreation: new Date()
      }
      banques.value.unshift(newBanque)
      ElMessage.success('Banque créée avec succès')
    }

    closeForm()
  } catch (error) {
    ElMessage.error('Une erreur est survenue lors de l\'enregistrement')
  } finally {
    loading.value = false
  }
}

const editBanque = (banque: Banque) => {
  Object.assign(form, banque)
  isEditing.value = true
  showForm.value = true
}

const toggleStatut = (banque: Banque) => {
  const action = banque.statut ? 'retirer du statut de banque principale' : 'définir comme banque principale'
  ElMessageBox.confirm(
    `Êtes-vous sûr de vouloir ${action} pour ${banque.sigle} ?`,
    'Confirmation',
    { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
  ).then(() => {
    banque.statut = !banque.statut
    ElMessage.success(`Statut modifié avec succès`)
  })
}

const deleteBanque = (banque: Banque) => {
  selectedBanque.value = banque
  showDeleteModal.value = true
}

const confirmDelete = async () => {
  if (!selectedBanque.value) return

  try {
    // Simulation d'une API call
    await new Promise(resolve => setTimeout(resolve, 1000))

    const index = banques.value.findIndex(b => b.id === selectedBanque.value!.id)
    if (index !== -1) {
      banques.value.splice(index, 1)
      ElMessage.success('Banque supprimée avec succès')
    }
  } catch (error) {
    ElMessage.error('Une erreur est survenue lors de la suppression')
  } finally {
    showDeleteModal.value = false
    selectedBanque.value = null
  }
}

const cancelDelete = () => {
  showDeleteModal.value = false
  selectedBanque.value = null
}

const refreshData = () => {
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: Banque[]) => {
  selectedBanques.value = selection
}

onMounted(() => {
  // Initialisation
})
</script>

<style lang="scss" scoped>
@use '@/styles/design-system.scss' as *;

.enhanced-banque-view {
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
  
  .banque-form {
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

// Styles pour les sections du formulaire
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

// Styles pour les radio améliorés
.enhanced-radio {
  .radio-option {
    width: 100%;
    margin-bottom: var(--spacing-md);
    
    .radio-content {
      display: flex;
      align-items: center;
      gap: var(--spacing-md);
      padding: var(--spacing-md);
      border: 1px solid var(--border-light);
      border-radius: var(--border-radius-base);
      transition: all var(--transition-base);
      
      .radio-label {
        font-weight: 600;
        color: var(--text-primary);
        margin-bottom: var(--spacing-xs);
      }
      
      .radio-desc {
        font-size: 12px;
        color: var(--text-secondary);
      }
    }
    
    &:hover .radio-content {
      border-color: var(--primary-color);
      background: rgba(64, 158, 255, 0.05);
    }
    
    &.is-checked .radio-content {
      border-color: var(--primary-color);
      background: rgba(64, 158, 255, 0.1);
    }
  }
}

// Styles pour les tags améliorés
.enhanced-tag {
  font-weight: 500;
  border-radius: var(--border-radius-base);
  transition: all var(--transition-base);
  
  &:hover {
    transform: scale(1.05);
  }
}

// Styles pour les informations bancaires
.bank-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .bank-icon {
    font-size: 20px;
    color: var(--primary-color);
  }
  
  .bank-details {
    .bank-name {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .bank-code {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.date-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  color: var(--text-secondary);
  font-size: 14px;
}

// Styles pour les boutons d'action
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

// Styles pour le tableau
.enhanced-table {
  :deep(.el-table__row) {
    transition: all var(--transition-base);
    
    &:hover {
      background: var(--bg-secondary);
    }
    
    &.row-principal {
      background: rgba(103, 194, 58, 0.05);
      
      &:hover {
        background: rgba(103, 194, 58, 0.1);
      }
    }
    
    &.row-secondary {
      background: rgba(230, 162, 60, 0.05);
      
      &:hover {
        background: rgba(230, 162, 60, 0.1);
      }
    }
  }
}

// Styles pour la confirmation de suppression
.delete-confirmation {
  text-align: center;
  
  p {
    margin-bottom: var(--spacing-md);
    color: var(--text-regular);
    
    strong {
      color: var(--danger-color);
    }
  }
}

// Responsive
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }
  
  .sidebar-panel {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .enhanced-banque-view {
    padding: var(--spacing-lg);
  }
  
  .page-header .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-lg);
  }
  
  .header-stats {
    width: 100%;
    justify-content: space-around;
  }
  
  .toolbar {
    flex-direction: column;
    gap: var(--spacing-md);
    align-items: stretch;
  }
}
</style>
