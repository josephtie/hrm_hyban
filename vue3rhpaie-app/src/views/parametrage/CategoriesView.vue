<template>
  <div class="enhanced-categories-view">
    <!-- Header amélioré -->
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Catégories Professionnelles</h1>
          <p class="page-subtitle">Gestion des salaires catégoriels et indemnités</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ categories.length }}</div>
            <div class="stat-label">Total catégories</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ formatCurrency(salaireMoyen) }}</div>
            <div class="stat-label">Salaire moyen</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ formatCurrency(indemniteMoyenne) }}</div>
            <div class="stat-label">Indemnité moyenne</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire amélioré -->
      <div class="sidebar-panel enhanced-card" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Catégorie</h3>
          <el-button @click="closeForm" circle size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <!-- Formulaire amélioré -->
        <EnhancedForm
          v-model="form"
          :title="isEditing ? 'Modifier une catégorie' : 'Ajouter une catégorie'"
          subtitle="Remplissez les informations ci-dessous"
          :loading="loading"
          :show-footer="true"
          :show-cancel="true"
          :show-submit="true"
          cancel-text="Annuler"
          :submit-text="isEditing ? 'Mettre à jour' : 'Créer'"
          @submit="saveForm"
          @cancel="closeForm"
          loading-text="Enregistrement en cours..."
        >
          <template #default>
            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Collection /></el-icon>
                Informations de la catégorie
              </h4>
              
              <el-form-item label="Libellé" required class="form-required">
                <el-input
                  v-model="form.libelle"
                  placeholder="Libellé de la catégorie"
                  size="large"
                  class="enhanced-input"
                  maxlength="100"
                  show-word-limit
                >
                  <template #prefix>
                    <el-icon><Collection /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </div>

            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Money /></el-icon>
                Rémunération
              </h4>
              
              <el-form-item label="Salaire de base" required class="form-required">
                <el-input-number
                  v-model="form.salaireDeBase"
                  placeholder="Salaire de base"
                  size="large"
                  class="enhanced-input"
                  :min="0"
                  :max="999999999"
                  :precision="0"
                  :step="10000"
                  style="width: 100%"
                >
                  <template #prefix>
                    <span>XOF</span>
                  </template>
                </el-input-number>
              </el-form-item>

              <el-form-item label="Indemnité de logement" required class="form-required">
                <el-input-number
                  v-model="form.indemniteLogement"
                  placeholder="Indemnité de logement"
                  size="large"
                  class="enhanced-input"
                  :min="0"
                  :max="999999999"
                  :precision="0"
                  :step="5000"
                  style="width: 100%"
                >
                  <template #prefix>
                    <span>XOF</span>
                  </template>
                </el-input-number>
              </el-form-item>

              <el-form-item label="Total rémunération">
                <div class="total-info">
                  <div class="total-amount">
                    <span class="total-label">Total:</span>
                    <span class="total-value">{{ formatCurrency(totalRemuneration) }}</span>
                  </div>
                  <div class="total-breakdown">
                    <div class="breakdown-item">
                      <span>Salaire:</span>
                      <span>{{ formatCurrency(form.salaireDeBase) }}</span>
                    </div>
                    <div class="breakdown-item">
                      <span>Indemnité:</span>
                      <span>{{ formatCurrency(form.indemniteLogement) }}</span>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </div>
          </template>
        </EnhancedForm>
      </div>

      <!-- Colonne principale avec le tableau amélioré -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Catégories Professionnelles</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle class="enhanced-button">
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary" class="enhanced-button">
              <el-icon><Plus /></el-icon>
              Nouvelle Catégorie
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils améliorée -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une catégorie..."
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
              v-model="filterSalaire"
              placeholder="Filtrer par salaire"
              style="width: 180px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option label="Moins de 100k" value="low" />
              <el-option label="100k - 300k" value="medium" />
              <el-option label="300k - 500k" value="high" />
              <el-option label="Plus de 500k" value="veryhigh" />
            </el-select>
            <el-button @click="exportExcel" type="success" class="enhanced-button">
              <el-icon><Download /></el-icon>
              Exporter Excel
            </el-button>
          </div>
        </div>

        <!-- Tableau simple comme BanqueView -->
        <div class="table-container">
          <el-table 
            :data="filteredCategories" 
            style="width: 100%"
            @selection-change="handleSelectionChange"
            v-loading="loading"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="libelle" label="Libellé" min-width="200" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><Collection /></el-icon>
                  <span>{{ row.libelle }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="salaireDeBase" label="Salaire de base" width="150" sortable>
              <template #default="{ row }">
                <el-tag type="success" size="large">{{ formatCurrency(row.salaireDeBase) }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="indemniteLogement" label="Indemnité logement" width="160" sortable>
              <template #default="{ row }">
                <el-tag type="primary" size="large">{{ formatCurrency(row.indemniteLogement || 0) }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="total" label="Total rémunération" width="180" sortable>
              <template #default="{ row }">
                <el-tag type="warning" size="large">
                  {{ formatCurrency((row.salaireDeBase || 0) + (row.indemniteLogement || 0)) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="niveau" label="Niveau" width="100" sortable>
              <template #default="{ row }">
                <el-tag :type="getNiveauType(row.salaireDeBase)" size="small">
                  {{ getNiveauLabel(row.salaireDeBase) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="150" fixed="right">
              <template #default="{ row }">
                <el-button-group>
                  <el-button size="small" @click="editCategorie(row)" type="primary">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" @click="deleteCategorie(row)" type="danger">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
          
          <!-- Pagination -->
          <div class="pagination-container">
            <el-pagination
              :current-page="currentPage + 1"
              :page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="totalCategories"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, Refresh, Close, Collection, Money,
  House, Wallet, CopyDocument, Download, WarningFilled
} from '@element-plus/icons-vue'
import EnhancedForm from '@/components/common/EnhancedForm.vue'
import EnhancedModal from '@/components/common/EnhancedModal.vue'
import { categoriesService, type CategorieDto } from '@/services/categories.service'

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const filterSalaire = ref<string | null>(null)
const selectedCategories = ref<CategorieDto[]>([])

// Variables pour la pagination
const currentPage = ref(0) // Numéro de page (0, 1, 2...)
const pageSize = ref(10)
const totalCategories = ref(0)

// Watchers pour la recherche et la pagination
watch(searchText, () => {
  currentPage.value = 0 // Reset à première page pour la recherche
  loadCategories()
})

watch(pageSize, () => {
  currentPage.value = 0 // Reset à première page
  loadCategories()
})

watch(filterSalaire, () => {
  currentPage.value = 0 // Reset à première page pour le filtre
  loadCategories()
})

const form = reactive({
  id: 0,
  libelle: '',
  salaireDeBase: 0,
  indemniteLogement: 0
})

const categories = ref<CategorieDto[]>([])

const filteredCategories = computed(() => {
  let filtered = categories.value

  if (searchText.value) {
    filtered = filtered.filter(categorie =>
      categorie.libelle.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  if (filterSalaire.value !== null) {
    filtered = filtered.filter(categorie => {
      switch (filterSalaire.value) {
        case 'low':
          return categorie.salaireDeBase < 100000
        case 'medium':
          return categorie.salaireDeBase >= 100000 && categorie.salaireDeBase < 300000
        case 'high':
          return categorie.salaireDeBase >= 300000 && categorie.salaireDeBase < 500000
        case 'veryhigh':
          return categorie.salaireDeBase >= 500000
        default:
          return true
      }
    })
  }

  return filtered
})

const totalRemuneration = computed(() => {
  return form.salaireDeBase + form.indemniteLogement
})

const salaireMoyen = computed(() => {
  if (categories.value.length === 0) return 0
  const total = categories.value.reduce((sum, cat) => sum + cat.salaireDeBase, 0)
  return total / categories.value.length
})

const indemniteMoyenne = computed(() => {
  if (categories.value.length === 0) return 0
  const total = categories.value.reduce((sum, cat) => sum + (cat.indemniteLogement || 0), 0)
  return total / categories.value.length
})

// Méthodes de gestion de la pagination
const handleSizeChange = (val: number) => {
  console.log('handleSizeChange appelé avec:', val)
  pageSize.value = val
  currentPage.value = 0 // Reset à première page
  loadCategories()
}

const handleCurrentChange = (val: number) => {
  console.log('handleCurrentChange appelé avec:', val)
  currentPage.value = val // Envoyer le numéro de page directement
  loadCategories()
}

const deleteAlerts = computed(() => {
  return []
})

// Méthodes
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XOF'
  }).format(amount)
}

const getNiveauType = (salaire: number) => {
  if (salaire >= 500000) return 'danger'
  if (salaire >= 300000) return 'warning'
  if (salaire >= 200000) return 'primary'
  return 'success'
}

const getNiveauLabel = (salaire: number) => {
  if (salaire >= 500000) return 'A'
  if (salaire >= 300000) return 'B'
  if (salaire >= 200000) return 'C'
  return 'D'
}

const tableRowClassName = ({ row }: { row: CategorieDto }) => {
  if (row.salaireDeBase >= 500000) return 'row-high'
  if (row.salaireDeBase >= 300000) return 'row-medium'
  if (row.salaireDeBase >= 200000) return 'row-low'
  return 'row-basic'
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
    libelle: '',
    salaireDeBase: 0,
    indemniteLogement: 0
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.libelle || form.salaireDeBase <= 0 || form.indemniteLogement < 0) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  loading.value = true

  try {
    if (isEditing.value && form.id) {
      await categoriesService.updateCategorie(form.id, {
        libelle: form.libelle,
        salaireDeBase: form.salaireDeBase,
        indemniteLogement: form.indemniteLogement
      })
      ElMessage.success('Catégorie mise à jour avec succès')
    } else {
      await categoriesService.createCategorie({
        libelle: form.libelle,
        salaireDeBase: form.salaireDeBase,
        indemniteLogement: form.indemniteLogement
      })
      ElMessage.success('Catégorie créée avec succès')
    }

    await loadCategories()
    closeForm()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || 'Une erreur est survenue lors de l\'enregistrement')
  } finally {
    loading.value = false
  }
}

const editCategorie = (categorie: CategorieDto) => {
  Object.assign(form, categorie)
  isEditing.value = true
  showForm.value = true
}

const duplicateCategorie = (categorie: CategorieDto) => {
  Object.assign(form, {
    ...categorie,
    id: 0,
    libelle: `${categorie.libelle} (Copie)`
  })
  isEditing.value = false
  showForm.value = true
}

const deleteCategorie = async (categorie: CategorieDto) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer "${categorie.libelle}" ?`,
      'Confirmation de suppression',
      {
        confirmButtonText: 'Oui, supprimer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )
    
    console.log('🗑️ Supprimer la catégorie:', categorie.id)
    const response = await categoriesService.deleteCategorie(categorie.id!)
    
    if (response.success) {
      ElMessage.success('Catégorie supprimée avec succès')
      // Revenir à la première page pour éviter un tableau vide
      currentPage.value = 0
      await loadCategories()
    } else {
      ElMessage.error('Erreur lors de la suppression')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('💥 Erreur lors de la suppression:', error)
      ElMessage.error('Erreur lors de la suppression')
    }
  }
}

const exportExcel = async () => {
  try {
    const blob = await categoriesService.exportCategories({
      search: searchText.value || undefined,
      sortBy: 'libelle',
      sortOrder: 'asc'
    })
    
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `categories_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('Export Excel terminé avec succès')
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || 'Une erreur est survenue lors de l\'export')
  }
}

const refreshData = async () => {
  await loadCategories()
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: CategorieDto[]) => {
  selectedCategories.value = selection
}

const loadCategories = async () => {
  try {
    console.log('loadCategories appelé avec:', { page: currentPage.value, size: pageSize.value })
    const response = await categoriesService.getCategories({
      page: currentPage.value,
      size: pageSize.value,
      search: searchText.value || undefined,
      sortBy: 'libelle',
      sortOrder: 'asc'
    })
    categories.value = response.data
    totalCategories.value = response.total || 0
    console.log('Réponse reçue:', { data: response.data, total: response.total })
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || 'Une erreur est survenue lors du chargement des catégories')
  }
}

const loadStats = async () => {
  try {
    const response = await categoriesService.getCategoriesStats()
    const stats = response.data
    // Mettre à jour les statistiques si nécessaire
  } catch (error: any) {
    console.error('Erreur lors du chargement des statistiques:', error)
  }
}

onMounted(() => {
  loadCategories()
  loadStats()
})
</script>

<style lang="scss" scoped>
@use '@/styles/design-system.scss' as *;

.enhanced-categories-view {
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
  
  .categorie-form {
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
  overflow: auto; // Permettre le scroll si nécessaire
  padding: var(--spacing-lg);
  min-height: 400px; // Hauteur minimale pour le tableau
  
  .el-table {
    height: auto !important; // Permettre au tableau de s'adapter au contenu
    max-height: none !important; // Pas de hauteur maximale fixe
  }
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

.total-info {
  background: var(--bg-tertiary);
  padding: var(--spacing-lg);
  border-radius: var(--border-radius-base);
  border: 2px solid var(--primary-color);
  
  .total-amount {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: var(--spacing-md);
    
    .total-label {
      font-weight: 600;
      color: var(--text-secondary);
    }
    
    .total-value {
      font-size: 18px;
      font-weight: 700;
      color: var(--primary-color);
    }
  }
  
  .total-breakdown {
    border-top: 1px solid var(--border-light);
    padding-top: var(--spacing-md);
    
    .breakdown-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: var(--spacing-xs);
      
      &:last-child {
        margin-bottom: 0;
      }
      
      span:first-child {
        color: var(--text-secondary);
        font-size: 14px;
      }
      
      span:last-child {
        color: var(--text-primary);
        font-weight: 500;
      }
    }
  }
}

.categorie-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .categorie-icon {
    font-size: 20px;
    color: var(--primary-color);
  }
  
  .categorie-details {
    .categorie-libelle {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .categorie-id {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.salaire-info,
.indemnite-info,
.total-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  
  .total-tag {
    font-weight: 600;
  }
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
    
    &.row-high {
      background: rgba(245, 108, 108, 0.05);
      
      &:hover {
        background: rgba(245, 108, 108, 0.1);
      }
    }
    
    &.row-medium {
      background: rgba(230, 162, 60, 0.05);
      
      &:hover {
        background: rgba(230, 162, 60, 0.1);
      }
    }
    
    &.row-low {
      background: rgba(64, 158, 255, 0.05);
      
      &:hover {
        background: rgba(64, 158, 255, 0.1);
      }
    }
    
    &.row-basic {
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
  
  .categorie-summary {
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
  .enhanced-categories-view {
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
