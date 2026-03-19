<template>
  <div class="enhanced-fonctions-view">
    <!-- Header amélioré -->
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Emplois et Fonctions</h1>
          <p class="page-subtitle">Gestion des postes et fonctions dans l'entreprise</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ fonctions.length }}</div>
            <div class="stat-label">Total fonctions</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ fonctionsActives }}</div>
            <div class="stat-label">Actives</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire amélioré -->
      <div class="sidebar-panel enhanced-card" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Fonction</h3>
          <el-button @click="closeForm" circle size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <!-- Formulaire amélioré -->
        <el-form
          ref="formRef"
          :model="form"
          :rules="formRules"
          label-position="top"
          size="large"
          class="fonction-form"
        >
            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Briefcase /></el-icon>
                Informations de la fonction
              </h4>
              
              <el-form-item label="Libellé" required class="form-required">
                <el-input
                  v-model="form.libelle"
                  placeholder="Libellé de la fonction"
                  size="large"
                  class="enhanced-input"
                  maxlength="100"
                  show-word-limit
                >
                  <template #prefix>
                    <el-icon><Briefcase /></el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item label="Description">
                <el-input
                  v-model="form.description"
                  type="textarea"
                  placeholder="Description de la fonction"
                  size="large"
                  class="enhanced-input"
                  :rows="3"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
            </div>

            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Collection /></el-icon>
                Classification
              </h4>
              
              <el-form-item label="Niveau hiérarchique" required class="form-required">
                <el-select
                  v-model="form.niveau"
                  placeholder="Sélectionnez un niveau"
                  size="large"
                  class="enhanced-input"
                >
                  <el-option
                    v-for="niveau in niveaux"
                    :key="niveau.value"
                    :label="niveau.label"
                    :value="niveau.value"
                  >
                    <div class="niveau-option">
                      <el-icon><Star /></el-icon>
                      <span>{{ niveau.label }}</span>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="Département">
                <el-select
                  v-model="form.departement"
                  placeholder="Sélectionnez un département"
                  size="large"
                  class="enhanced-input"
                  filterable
                  allow-create
                >
                  <el-option
                    v-for="dept in departements"
                    :key="dept"
                    :label="dept"
                    :value="dept"
                  />
                </el-select>
              </el-form-item>
            </div>

            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Setting /></el-icon>
                Configuration
              </h4>
              
              <el-form-item label="Statut">
                <el-radio-group v-model="form.statut" size="large" class="enhanced-radio">
                  <el-radio value="ACTIF" class="radio-option">
                    <div class="radio-content">
                      <el-icon><Check /></el-icon>
                      <div>
                        <div class="radio-label">Actif</div>
                        <div class="radio-desc">Fonction disponible pour les affectations</div>
                      </div>
                    </div>
                  </el-radio>
                  <el-radio value="INACTIF" class="radio-option">
                    <div class="radio-content">
                      <el-icon><Close /></el-icon>
                      <div>
                        <div class="radio-label">Inactif</div>
                        <div class="radio-desc">Fonction non utilisée</div>
                      </div>
                    </div>
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
          
          <!-- Boutons d'action -->
          <div class="form-actions">
            <el-button 
              @click="resetForm" 
              size="large"
              class="enhanced-button"
            >
              <el-icon><Refresh /></el-icon>
              Réinitialiser
            </el-button>
            <el-button 
              @click="closeForm" 
              size="large"
              class="enhanced-button"
            >
              <el-icon><Close /></el-icon>
              Annuler
            </el-button>
            <el-button 
              type="primary" 
              @click="saveForm" 
              :loading="loading"
              size="large"
              class="enhanced-button"
            >
              <el-icon><Check /></el-icon>
              {{ isEditing ? 'Mettre à jour' : 'Créer' }}
            </el-button>
          </div>
        </el-form>
      </div>

      <!-- Colonne principale avec le tableau amélioré -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Emplois/Fonctions</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle class="enhanced-button">
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary" class="enhanced-button">
              <el-icon><Plus /></el-icon>
              Nouvelle Fonction
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils améliorée -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une fonction..."
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
              v-model="filterNiveau"
              placeholder="Filtrer par niveau"
              style="width: 150px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option
                v-for="niveau in niveaux"
                :key="niveau.value"
                :label="niveau.label"
                :value="niveau.value"
              />
            </el-select>
            <el-select
              v-model="filterStatut"
              placeholder="Filtrer par statut"
              style="width: 120px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option label="Actif" value="ACTIF" />
              <el-option label="Inactif" value="INACTIF" />
            </el-select>
          </div>
        </div>

        <!-- Tableau amélioré -->
        <div class="table-container">
          <el-table
            :data="paginatedFonctions"
            style="width: 100%"
            class="enhanced-table"
            :row-class-name="tableRowClassName"
          >
            <el-table-column prop="libelle" label="Libellé" sortable>
              <template #default="{ row }">
                <div class="fonction-info">
                  <el-icon class="fonction-icon"><Briefcase /></el-icon>
                  <div class="fonction-details">
                    <div class="fonction-libelle">{{ row.libelle }}</div>
                    <div class="fonction-id">ID: FCT-{{ String(row.id).padStart(3, '0') }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="description" label="Description" width="200">
              <template #default="{ row }">
                <div class="description-info">
                  <el-tooltip :content="row.description" placement="top" :disabled="!row.description">
                    <span class="description-text">{{ row.description || 'Aucune description' }}</span>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            
            
            <el-table-column prop="niveau" label="Niveau" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="getNiveauType(row.niveau)" size="small" class="enhanced-tag">
                  <el-icon><Star /></el-icon>
                  {{ getNiveauLabel(row.niveau) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="departement" label="Département" width="140" sortable>
              <template #default="{ row }">
                <div class="departement-info">
                  <el-icon><OfficeBuilding /></el-icon>
                  <span>{{ row.departement || 'Non spécifié' }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="statut" label="Statut" width="100" sortable>
              <template #default="{ row }">
                <el-tag :type="row.statut === 'ACTIF' ? 'success' : 'danger'" size="small" class="enhanced-tag">
                  <el-icon>
                    <Check v-if="row.statut === 'ACTIF'" />
                    <Close v-else />
                  </el-icon>
                  {{ row.statut === 'ACTIF' ? 'Actif' : 'Inactif' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="150" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button
                    size="small"
                    @click="editFonction(row)"
                    type="primary"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="duplicateFonction(row)"
                    type="info"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><CopyDocument /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="deleteFonction(row)"
                    type="danger"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
          
          <!-- Pagination -->
            <div class="pagination-container">
            <el-pagination
              :current-page="currentPage + 1"
              :page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="totalFonctions"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
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
        <p>Êtes-vous sûr de vouloir supprimer la fonction <strong>{{ selectedFonction?.libelle }}</strong> ?</p>
        <div class="fonction-summary">
          <div class="summary-item">
            <label>Niveau:</label>
            <span>{{ getNiveauLabel(selectedFonction?.niveau || '') }}</span>
          </div>
          <div class="summary-item">
            <label>Département:</label>
            <span>{{ selectedFonction?.departement || 'Non spécifié' }}</span>
          </div>
        </div>
        <p class="delete-impact">
          <strong>Impact :</strong> Cette action supprimera définitivement la fonction et affectera les employés qui l'occupent.
        </p>
      </div>
    </EnhancedModal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, Refresh, Close, Collection, WarningFilled
} from '@element-plus/icons-vue'
import EnhancedForm from '@/components/common/EnhancedForm.vue'
import EnhancedModal from '@/components/common/EnhancedModal.vue'
import { fonctionRestService, type FonctionRestDto } from '@/services/fonction.service'

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const selectedFonctions = ref<FonctionRestDto[]>([])
const filterNiveau = ref<string | null>(null)
const filterStatut = ref<string | null>(null)
const showDeleteModal = ref(false)
const selectedFonction = ref<Fonction | null>(null)

// Variables pour la pagination
const currentPage = ref(0) // Numéro de page (0, 1, 2...)
const pageSize = ref(10)
const totalFonctions = ref(0) // Total depuis l'API

const form = reactive({
  id: 0,
  libelle: '',
  description: '',
  niveau: '',
  departement: '',
  statut: 'ACTIF' as 'ACTIF' | 'INACTIF'
})

const fonctions = ref<FonctionRestDto[]>([])
const niveaux = ref([
  { value: '1', label: 'Niveau 1 - Exécutant' },
  { value: '2', label: 'Niveau 2 - Opérationnel' },
  { value: '3', label: 'Niveau 3 - Technique' },
  { value: '4', label: 'Niveau 4 - Manager' },
  { value: '5', label: 'Niveau 5 - Direction' }
])

const departements = ref([
  'Direction Générale',
  'Ressources Humaines',
  'Finance et Comptabilité',
  'Commercial',
  'Production',
  'Informatique',
  'Marketing',
  'Logistique',
  'Qualité',
  'Administration'
])

const filteredFonctions = computed(() => {
  let filtered = fonctions.value

  if (searchText.value) {
    filtered = filtered.filter(fonction =>
      fonction.libelle.toLowerCase().includes(searchText.value.toLowerCase()) ||
      fonction.description.toLowerCase().includes(searchText.value.toLowerCase()) ||
      (fonction.departement && fonction.departement.toLowerCase().includes(searchText.value.toLowerCase()))
    )
  }

  if (filterNiveau.value !== null) {
    filtered = filtered.filter(fonction => fonction.niveau === filterNiveau.value)
  }

  if (filterStatut.value !== null) {
    filtered = filtered.filter(fonction => fonction.statut === filterStatut.value)
  }

  return filtered
})

// Données pour le tableau (pagination côté serveur = utiliser fonctions.value directement)
const paginatedFonctions = computed(() => {
  return fonctions.value // Utiliser directement les données paginées par l'API
})

const fonctionsActives = computed(() => {
  return fonctions.value.filter(f => f.statut === 'ACTIF').length
})

const handleSelectionChange = (selection: FonctionRestDto[]) => {
  selectedFonctions.value = selection
}

// Handlers pour la pagination (EXACTEMENT comme CategoriesView)
const handleSizeChange = (val: number) => {
  console.log('📄 handleSizeChange:', val)
  pageSize.value = val
  currentPage.value = 0 // Reset à première page
  loadFonctions()
}

const handleCurrentChange = (val: number) => {
  console.log('📄 handleCurrentChange:', val)
  currentPage.value = val - 1 // Convertir 1-indexé en 0-indexé
  loadFonctions()
}

// Test simple pour pagination
const testPage2 = () => {
  console.log('🧪 testPage2 - Page actuelle:', currentPage.value)
  currentPage.value = 1 // Page 2 (0-indexé)
  console.log('🧪 testPage2 - Nouvelle page:', currentPage.value)
  loadFonctions()
}

const deleteAlerts = computed(() => {
  if (!selectedFonction.value) return []
  
  return [
    {
      type: 'danger' as const,
      title: 'Attention',
      message: 'La suppression est définitive et affectera les employés',
      closable: false
    }
  ]
})

// Méthodes
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XOF'
  }).format(amount)
}

const getNiveauType = (niveau: string) => {
  switch (niveau) {
    case '5': return 'danger'
    case '4': return 'warning'
    case '3': return 'primary'
    case '2': return 'success'
    case '1': return 'info'
    default: return 'info' // 'default' n'est pas valide pour el-tag
  }
}

const getNiveauLabel = (niveau: string) => {
  const niveauOption = niveaux.value.find(n => n.value === niveau)
  return niveauOption ? niveauOption.label : niveau
}

const tableRowClassName = ({ row }: { row: Fonction }) => {
  return row.statut === 'ACTIF' ? 'row-active' : 'row-inactive'
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
    description: '',
    categorieId: 0,
    niveau: '',
    departement: '',
    statut: 'ACTIF'
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.libelle || !form.niveau) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  loading.value = true

  try {
    console.log('💾 Sauvegarde de la fonction:', form)

    let response
    if (isEditing.value && form.id) {
      console.log('✏️ Mode édition')
      response = await fonctionRestService.updateFonction({
        id: form.id,
        libelle: form.libelle,
        description: form.description,
        niveau: form.niveau,
        departement: form.departement,
        statut: form.statut
      })
    } else {
      console.log('➕ Mode création')
      response = await fonctionRestService.createFonction({
        libelle: form.libelle,
        description: form.description,
        niveau: form.niveau,
        departement: form.departement,
        statut: form.statut
      })
    }

    console.log('📥 Réponse API:', response)

    if (response.success) {
      ElMessage.success(`Fonction ${isEditing.value ? 'modifiée' : 'créée'} avec succès`)
      closeForm()
      await loadFonctions()
    } else {
      ElMessage.error(response.message || `Erreur lors de la ${isEditing.value ? 'modification' : 'création'}`)
    }
  } catch (error) {
    console.error('💥 Erreur lors de la sauvegarde:', error)
    ElMessage.error(`Erreur lors de la ${isEditing.value ? 'modification' : 'création'}`)
  } finally {
    loading.value = false
  }
}

const editFonction = (fonction: FonctionRestDto) => {
  console.log('✏️ Modifier la fonction:', fonction)
  Object.assign(form, fonction)
  isEditing.value = true
  showForm.value = true
}

const deleteFonction = async (fonction: FonctionRestDto) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer "${fonction.libelle}" ?`,
      'Confirmation de suppression',
      {
        confirmButtonText: 'Oui, supprimer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )
    
    console.log('🗑️ Supprimer la fonction:', fonction.id)
    const response = await fonctionRestService.deleteFonction(fonction.id!)
    
    if (response.success) {
      ElMessage.success('Fonction supprimée avec succès')
      await loadFonctions()
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

const confirmDelete = async () => {
  if (!selectedFonction.value) return

  try {
    await new Promise(resolve => setTimeout(resolve, 1000))

    const index = fonctions.value.findIndex(f => f.id === selectedFonction.value!.id)
    if (index !== -1) {
      fonctions.value.splice(index, 1)
      ElMessage.success('Fonction supprimée avec succès')
    }
  } catch (error) {
    ElMessage.error('Une erreur est survenue lors de la suppression')
  } finally {
    showDeleteModal.value = false
    selectedFonction.value = null
  }
}

const cancelDelete = () => {
  showDeleteModal.value = false
  selectedFonction.value = null
}

const loadFonctions = async () => {
  try {
    console.log('loadFonctions appelé avec:', { page: currentPage.value, size: pageSize.value })
    const response = await fonctionRestService.getFonctions({
      page: currentPage.value,
      size: pageSize.value,
      search: searchText.value || undefined,
      sortBy: 'libelle',
      sortOrder: 'asc'
    })
    fonctions.value = response.data
    totalFonctions.value = response.total || 0
    console.log('Réponse reçue:', { data: response.data, total: response.total })
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || 'Une erreur est survenue lors du chargement des fonctions')
  }
}

const refreshData = async () => {
  await loadFonctions()
  ElMessage.success('Données actualisées')
}

watch(searchText, () => {
  currentPage.value = 0 // Reset à première page pour la recherche
  loadFonctions()
})

watch(pageSize, () => {
  currentPage.value = 0 // Reset à première page
  loadFonctions()
})

onMounted(() => {
  loadFonctions()
})
</script>

<style lang="scss" scoped>
@use '@/styles/design-system.scss' as *;

.enhanced-fonctions-view {
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
  
  .fonction-form {
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
  overflow: auto; // Changé de hidden à auto pour voir la pagination
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

.fonction-form {
  .form-actions {
    display: flex;
    gap: var(--spacing-md);
    justify-content: flex-end;
    margin-top: var(--spacing-xl);
    padding-top: var(--spacing-lg);
    border-top: 1px solid var(--border-light);
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: var(--spacing-lg);
  padding: var(--spacing-md);
  background: var(--bg-tertiary);
  border-radius: var(--radius-md);
}

.enhanced-pagination {
  :deep(.el-pagination) {
    --el-pagination-button-bg-color: var(--bg-primary);
    --el-pagination-hover-color: var(--primary-color);
    --el-pagination-text-color: var(--text-primary);
  }
}

.categorie-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  
  .categorie-libelle {
    font-weight: 600;
    color: var(--text-primary);
  }
  
  .categorie-salaire {
    font-size: 12px;
    color: var(--text-secondary);
  }
}

.niveau-option {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

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

.fonction-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .fonction-icon {
    font-size: 20px;
    color: var(--primary-color);
  }
  
  .fonction-details {
    .fonction-libelle {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .fonction-id {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.description-info {
  .description-text {
    display: block;
    max-width: 180px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    color: var(--text-secondary);
    font-size: 14px;
  }
}

.categorie-info,
.departement-info {
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
  
  .fonction-summary {
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
  .enhanced-fonctions-view {
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
