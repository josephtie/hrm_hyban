<template>
  <div class="enhanced-sanctions-view">
    <!-- Header amélioré -->
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Sanctions Disciplinaires</h1>
          <p class="page-subtitle">Gestion des sanctions et fautes disciplinaires</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ sanctions.length }}</div>
            <div class="stat-label">Total sanctions</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ sanctionsActives }}</div>
            <div class="stat-label">Actives</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ typesCount }}</div>
            <div class="stat-label">Types</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire amélioré -->
      <div class="sidebar-panel enhanced-card" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Sanction</h3>
          <el-button @click="closeForm" circle size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <!-- Formulaire amélioré -->
        <EnhancedForm
          v-model="form"
          :title="isEditing ? 'Modification de sanction' : 'Nouvelle sanction disciplinaire'"
          subtitle="Configurez les détails de la sanction"
          :show-header="true"
          :show-progress="true"
          :loading="loading"
          loading-text="Enregistrement en cours..."
          @submit="saveForm"
          @reset="resetForm"
          @cancel="closeForm"
          class="sanction-form"
        >
          <template #default>
            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Warning /></el-icon>
                Informations de la sanction
              </h4>
              
              <el-form-item label="Type de sanction" required class="form-required">
                <el-select
                  v-model="form.typeSanctionId"
                  placeholder="Sélectionnez un type"
                  size="large"
                  class="enhanced-input"
                  filterable
                >
                  <el-option
                    v-for="type in typesSanctions"
                    :key="type.id"
                    :label="type.libelle"
                    :value="type.id"
                  >
                    <div class="type-option">
                      <el-icon><Warning /></el-icon>
                      <span>{{ type.libelle }}</span>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="Faute" required class="form-required">
                <el-input
                  v-model="form.faute"
                  placeholder="Description de la faute"
                  size="large"
                  class="enhanced-input"
                  maxlength="200"
                  show-word-limit
                >
                  <template #prefix>
                    <el-icon><Document /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item label="Commentaire">
                <el-input
                  v-model="form.commentaire"
                  type="textarea"
                  placeholder="Commentaire détaillé"
                  size="large"
                  class="enhanced-input"
                  :rows="4"
                  maxlength="1000"
                  show-word-limit
                />
              </el-form-item>
            </div>
          </template>
        </EnhancedForm>
      </div>

      <!-- Colonne principale avec le tableau amélioré -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Sanctions</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle class="enhanced-button">
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary" class="enhanced-button">
              <el-icon><Plus /></el-icon>
              Nouvelle Sanction
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils améliorée -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une sanction..."
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
              placeholder="Filtrer par type"
              style="width: 180px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option
                v-for="type in typesSanctions"
                :key="type.id"
                :label="type.libelle"
                :value="type.id"
              />
            </el-select>
          </div>
        </div>

        <!-- Tableau amélioré -->
        <div class="table-container">
          <el-table
            :data="filteredSanctions"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            class="enhanced-table"
            :row-class-name="tableRowClassName"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="faute" label="Faute" sortable>
              <template #default="{ row }">
                <div class="faute-info">
                  <el-icon class="faute-icon"><Document /></el-icon>
                  <div class="faute-details">
                    <div class="faute-libelle">{{ row.faute }}</div>
                    <div class="sanction-id">ID: SAN-{{ String(row.id).padStart(3, '0') }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="commentaire" label="Commentaire" width="300">
              <template #default="{ row }">
                <div class="commentaire-info">
                  <el-tooltip :content="row.commentaire" placement="top" :disabled="!row.commentaire">
                    <span class="commentaire-text">{{ row.commentaire || 'Aucun commentaire' }}</span>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="typeSanction" label="Type sanction" width="180" sortable>
              <template #default="{ row }">
                <div class="type-info">
                  <el-tag :type="getTypeColor(row.typeSanction?.id)" size="small" class="enhanced-tag">
                    <el-icon><Warning /></el-icon>
                    {{ row.typeSanction?.libelle }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="dateCreation" label="Date création" width="130" sortable>
              <template #default="{ row }">
                <div class="date-info">
                  <el-icon><Calendar /></el-icon>
                  <span>{{ formatDate(row.dateCreation) }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="150" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button
                    size="small"
                    @click="editSanction(row)"
                    type="primary"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="duplicateSanction(row)"
                    type="info"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><CopyDocument /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="deleteSanction(row)"
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

    <!-- Modal amélioré pour la suppression -->
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
        <div class="delete-warning">
          <el-icon class="warning-icon"><WarningFilled /></el-icon>
          <h3>Attention</h3>
        </div>
        <p>Êtes-vous sûr de vouloir supprimer la sanction <strong>{{ selectedSanction?.faute }}</strong> ?</p>
        <div class="sanction-summary">
          <div class="summary-item">
            <label>Faute:</label>
            <span>{{ selectedSanction?.faute }}</span>
          </div>
          <div class="summary-item">
            <label>Type:</label>
            <span>{{ selectedSanction?.typeSanction?.libelle }}</span>
          </div>
          <div class="summary-item">
            <label>Date:</label>
            <span>{{ formatDate(selectedSanction?.dateCreation || '') }}</span>
          </div>
        </div>
        <p class="delete-impact">
          <strong>Impact :</strong> Cette action supprimera définitivement la sanction et affectera les dossiers disciplinaires associés.
        </p>
      </div>
    </EnhancedModal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, Refresh, Close, Warning, Document,
  CopyDocument, Calendar, WarningFilled
} from '@element-plus/icons-vue'
import EnhancedForm from '@/components/common/EnhancedForm.vue'
import EnhancedModal from '@/components/common/EnhancedModal.vue'

interface TypeSanction {
  id: number
  libelle: string
}

interface Sanction {
  id: number
  faute: string
  commentaire: string
  typeSanctionId: number
  typeSanction?: TypeSanction
  dateCreation: string
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const filterType = ref<number | null>(null)
const selectedSanctions = ref<Sanction[]>([])
const showDeleteModal = ref(false)
const selectedSanction = ref<Sanction | null>(null)

const form = reactive({
  id: 0,
  faute: '',
  commentaire: '',
  typeSanctionId: 0
})

const typesSanctions = ref<TypeSanction[]>([
  { id: 1, libelle: 'Avertissement verbal' },
  { id: 2, libelle: 'Avertissement écrit' },
  { id: 3, libelle: 'Blâme' },
  { id: 4, libelle: 'Mise à pied' },
  { id: 5, libelle: 'Suspension' },
  { id: 6, libelle: 'Rétention sur salaire' },
  { id: 7, libelle: 'Licenciement' }
])

const sanctions = ref<Sanction[]>([
  {
    id: 1,
    faute: 'Retard répété',
    commentaire: 'Retard de plus de 30 minutes à 3 reprises ce mois',
    typeSanctionId: 1,
    typeSanction: typesSanctions.value[0],
    dateCreation: '2024-01-15'
  },
  {
    id: 2,
    faute: 'Absence injustifiée',
    commentaire: 'Absence sans justification pendant 2 jours consécutifs',
    typeSanctionId: 2,
    typeSanction: typesSanctions.value[1],
    dateCreation: '2024-01-20'
  },
  {
    id: 3,
    faute: 'Non-respect des consignes',
    commentaire: 'Ignorance volontaire des procédures de sécurité',
    typeSanctionId: 3,
    typeSanction: typesSanctions.value[2],
    dateCreation: '2024-01-25'
  },
  {
    id: 4,
    faute: 'Faute grave',
    commentaire: 'Comportement inapproprié avec un client',
    typeSanctionId: 4,
    typeSanction: typesSanctions.value[3],
    dateCreation: '2024-02-01'
  },
  {
    id: 5,
    faute: 'Détérioration matériel',
    commentaire: 'Dégâts causés intentionnellement sur le matériel de l\'entreprise',
    typeSanctionId: 5,
    typeSanction: typesSanctions.value[4],
    dateCreation: '2024-02-05'
  }
])

const filteredSanctions = computed(() => {
  let filtered = sanctions.value

  if (searchText.value) {
    filtered = filtered.filter(sanction =>
      sanction.faute.toLowerCase().includes(searchText.value.toLowerCase()) ||
      sanction.commentaire.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  if (filterType.value !== null) {
    filtered = filtered.filter(sanction => sanction.typeSanctionId === filterType.value)
  }

  return filtered
})

const sanctionsActives = computed(() => {
  return sanctions.value.length
})

const typesCount = computed(() => {
  return [...new Set(sanctions.value.map(s => s.typeSanctionId))].length
})

const deleteAlerts = computed(() => {
  if (!selectedSanction.value) return []
  
  return [
    {
      type: 'danger' as const,
      title: 'Attention',
      message: 'La suppression est définitive et affectera les dossiers disciplinaires',
      closable: false
    }
  ]
})

// Méthodes
const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('fr-FR')
}

const getTypeColor = (typeId: number) => {
  switch (typeId) {
    case 1: return 'info'
    case 2: return 'warning'
    case 3: return 'warning'
    case 4: return 'danger'
    case 5: return 'danger'
    case 6: return 'warning'
    case 7: return 'danger'
    default: return 'default'
  }
}

const tableRowClassName = ({ row }: { row: Sanction }) => {
  const typeId = row.typeSanctionId
  if (typeId >= 4 && typeId <= 7) return 'row-grave'
  if (typeId >= 2 && typeId <= 3) return 'row-moyenne'
  return 'row-legere'
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
    faute: '',
    commentaire: '',
    typeSanctionId: 0
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.faute || !form.typeSanctionId) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  loading.value = true

  try {
    await new Promise(resolve => setTimeout(resolve, 1000))

    const selectedType = typesSanctions.value.find(t => t.id === form.typeSanctionId)
    if (!selectedType) {
      ElMessage.error('Type de sanction non trouvé')
      return
    }

    if (isEditing.value) {
      const index = sanctions.value.findIndex(s => s.id === form.id)
      if (index !== -1) {
        sanctions.value[index] = {
          ...form,
          typeSanction: selectedType,
          dateCreation: sanctions.value[index].dateCreation
        }
        ElMessage.success('Sanction mise à jour avec succès')
      }
    } else {
      const exists = sanctions.value.some(s => s.faute.toLowerCase() === form.faute.toLowerCase())
      
      if (exists) {
        ElMessage.error('Cette sanction existe déjà')
        return
      }

      const newSanction: Sanction = {
        ...form,
        id: Date.now(),
        typeSanction: selectedType,
        dateCreation: new Date().toISOString().split('T')[0]
      }
      sanctions.value.unshift(newSanction)
      ElMessage.success('Sanction créée avec succès')
    }

    closeForm()
  } catch (error) {
    ElMessage.error('Une erreur est survenue lors de l\'enregistrement')
  } finally {
    loading.value = false
  }
}

const editSanction = (sanction: Sanction) => {
  Object.assign(form, sanction)
  isEditing.value = true
  showForm.value = true
}

const duplicateSanction = (sanction: Sanction) => {
  Object.assign(form, {
    ...sanction,
    id: 0,
    faute: `${sanction.faute} (Copie)`
  })
  isEditing.value = false
  showForm.value = true
}

const deleteSanction = (sanction: Sanction) => {
  selectedSanction.value = sanction
  showDeleteModal.value = true
}

const confirmDelete = async () => {
  if (!selectedSanction.value) return

  try {
    await new Promise(resolve => setTimeout(resolve, 1000))

    const index = sanctions.value.findIndex(s => s.id === selectedSanction.value!.id)
    if (index !== -1) {
      sanctions.value.splice(index, 1)
      ElMessage.success('Sanction supprimée avec succès')
    }
  } catch (error) {
    ElMessage.error('Une erreur est survenue lors de la suppression')
  } finally {
    showDeleteModal.value = false
    selectedSanction.value = null
  }
}

const cancelDelete = () => {
  showDeleteModal.value = false
  selectedSanction.value = null
}

const refreshData = () => {
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: Sanction[]) => {
  selectedSanctions.value = selection
}

onMounted(() => {
  // Initialisation
})
</script>

<style lang="scss" scoped>
@use '@/styles/design-system.scss' as *;

.enhanced-sanctions-view {
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
  
  .sanction-form {
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

.type-option {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.faute-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .faute-icon {
    font-size: 20px;
    color: var(--warning-color);
  }
  
  .faute-details {
    .faute-libelle {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .sanction-id {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.commentaire-info {
  .commentaire-text {
    display: block;
    max-width: 280px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    color: var(--text-secondary);
    font-size: 14px;
  }
}

.type-info,
.date-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
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
    
    &.row-grave {
      background: rgba(245, 108, 108, 0.05);
      
      &:hover {
        background: rgba(245, 108, 108, 0.1);
      }
    }
    
    &.row-moyenne {
      background: rgba(230, 162, 60, 0.05);
      
      &:hover {
        background: rgba(230, 162, 60, 0.1);
      }
    }
    
    &.row-legere {
      background: rgba(103, 194, 58, 0.05);
      
      &:hover {
        background: rgba(103, 194, 58, 0.1);
      }
    }
  }
}

.delete-confirmation {
  text-align: center;
  
  .delete-warning {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: var(--spacing-md);
    margin-bottom: var(--spacing-lg);
    
    .warning-icon {
      font-size: 48px;
      color: var(--danger-color);
    }
    
    h3 {
      margin: 0;
      color: var(--danger-color);
    }
  }
  
  .sanction-summary {
    background: var(--bg-tertiary);
    padding: var(--spacing-lg);
    border-radius: var(--border-radius-base);
    margin: var(--spacing-lg) 0;
    
    .summary-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: var(--spacing-sm);
      
      &:last-child {
        margin-bottom: 0;
      }
      
      label {
        font-weight: 600;
        color: var(--text-secondary);
      }
      
      span {
        color: var(--text-primary);
      }
    }
  }
  
  .delete-impact {
    background: rgba(245, 108, 108, 0.1);
    border-left: 4px solid var(--danger-color);
    padding: var(--spacing-md);
    margin: var(--spacing-lg) 0;
    border-radius: var(--border-radius-base);
  }
  
  p {
    margin-bottom: var(--spacing-md);
    color: var(--text-regular);
    
    &:last-child {
      margin-bottom: 0;
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
  .enhanced-sanctions-view {
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
