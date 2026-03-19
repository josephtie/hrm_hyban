<template>
  <div class="enhanced-periodes-paie-view">
    <!-- Header amélioré -->
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Gestion des Périodes de Paie</h1>
          <p class="page-subtitle">Configuration et suivi des périodes de paie</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ periodes.length }}</div>
            <div class="stat-label">Total périodes</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ periodesActives }}</div>
            <div class="stat-label">Actives</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire amélioré -->
      <div class="sidebar-panel enhanced-card" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Période</h3>
          <el-button @click="closeForm" circle size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <!-- Formulaire amélioré -->
        <EnhancedForm
          v-model="form"
          :title="isEditing ? 'Modification de période' : 'Nouvelle période de paie'"
          subtitle="Configurez les dates de la période"
          :show-header="true"
          :show-progress="true"
          :loading="loading"
          loading-text="Enregistrement en cours..."
          @submit="saveForm"
          @reset="resetForm"
          @cancel="closeForm"
          class="periode-form"
        >
          <template #default>
            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Calendar /></el-icon>
                Informations de la période
              </h4>
              
              <el-form-item label="Exercice" required class="form-required">
                <el-select 
                  v-model="form.exerciceId" 
                  placeholder="Sélectionnez un exercice"
                  size="large"
                  class="enhanced-input"
                  filterable
                >
                  <el-option
                    v-for="exercice in exercices"
                    :key="exercice.id"
                    :label="exercice.annee"
                    :value="exercice.id"
                  >
                    <div class="exercice-option">
                      <span class="exercice-annee">{{ exercice.annee }}</span>
                      <span class="exercice-status">{{ exercice.actif ? 'Actif' : 'Inactif' }}</span>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="Mois" required class="form-required">
                <el-select 
                  v-model="form.mois" 
                  placeholder="Sélectionnez un mois"
                  size="large"
                  class="enhanced-input"
                >
                  <el-option
                    v-for="mois in moisOptions"
                    :key="mois.value"
                    :label="mois.label"
                    :value="mois.value"
                  >
                    <div class="mois-option">
                      <el-icon><Calendar /></el-icon>
                      <span>{{ mois.label }}</span>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>
            </div>

            <div class="form-section">
              <h4 class="section-title">
                <el-icon><Clock /></el-icon>
                Dates de la période
              </h4>
              
              <div class="form-row">
                <el-form-item label="Date d'ouverture" required class="form-required">
                  <el-date-picker
                    v-model="form.dateDebut"
                    type="date"
                    placeholder="Date de début"
                    size="large"
                    class="enhanced-input"
                    format="DD/MM/YYYY"
                    value-format="YYYY-MM-DD"
                  />
                </el-form-item>

                <el-form-item label="Date de fin" required class="form-required">
                  <el-date-picker
                    v-model="form.dateFin"
                    type="date"
                    placeholder="Date de fin"
                    size="large"
                    class="enhanced-input"
                    format="DD/MM/YYYY"
                    value-format="YYYY-MM-DD"
                    :disabled-date="disabledDateFin"
                  />
                </el-form-item>
              </div>

              <el-form-item label="Statut">
                <el-radio-group v-model="form.statut" size="large" class="enhanced-radio">
                  <el-radio value="OUVERT" class="radio-option">
                    <div class="radio-content">
                      <el-icon><Unlock /></el-icon>
                      <div>
                        <div class="radio-label">Ouverte</div>
                        <div class="radio-desc">Période active pour les modifications</div>
                      </div>
                    </div>
                  </el-radio>
                  <el-radio value="CLOTURE" class="radio-option">
                    <div class="radio-content">
                      <el-icon><Lock /></el-icon>
                      <div>
                        <div class="radio-label">Clôturée</div>
                        <div class="radio-desc">Période verrouillée</div>
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
          <h3>Liste des Périodes de Paie</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle class="enhanced-button">
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary" class="enhanced-button">
              <el-icon><Plus /></el-icon>
              Nouvelle Période
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils améliorée -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une période..."
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
              v-model="filterExercice"
              placeholder="Exercice"
              style="width: 150px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option
                v-for="exercice in exercices"
                :key="exercice.id"
                :label="exercice.annee"
                :value="exercice.id"
              />
            </el-select>
            <el-select
              v-model="filterStatut"
              placeholder="Statut"
              style="width: 120px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option label="Ouverte" value="OUVERT" />
              <el-option label="Clôturée" value="CLOTURE" />
            </el-select>
          </div>
        </div>

        <!-- Tableau amélioré -->
        <div class="table-container">
          <el-table
            :data="filteredPeriodes"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            class="enhanced-table"
            :row-class-name="tableRowClassName"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="periode" label="Période" width="200" sortable>
              <template #default="{ row }">
                <div class="periode-info">
                  <el-icon class="periode-icon"><Calendar /></el-icon>
                  <div class="periode-details">
                    <div class="periode-mois">{{ getMoisLibelle(row.mois) }}</div>
                    <div class="periode-annee">{{ row.exercice.annee }}</div>
                    <div class="periode-affiche" v-if="row.affiche">{{ row.affiche }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="dateDebut" label="Date d'ouverture" width="140" sortable>
              <template #default="{ row }">
                <div class="date-info">
                  <el-icon><Clock /></el-icon>
                  <span>{{ formatDate(row.dateDebut) }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="dateFin" label="Date de fin" width="140" sortable>
              <template #default="{ row }">
                <div class="date-info">
                  <el-icon><Clock /></el-icon>
                  <span>{{ formatDate(row.dateFin) }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="duree" label="Durée" width="80" sortable>
              <template #default="{ row }">
                <el-tag type="info" size="small" class="enhanced-tag">
                  {{ calculateDuree(row.dateDebut, row.dateFin) }}j
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="statut" label="Statut" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="row.statut === 'OUVERT' ? 'success' : 'danger'" size="large" class="enhanced-tag">
                  <el-icon style="margin-right: 4px;">
                    <Unlock v-if="row.statut === 'OUVERT'" />
                    <Lock v-else />
                  </el-icon>
                  {{ row.statut === 'OUVERT' ? 'Ouverte' : 'Clôturée' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="180" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button
                    size="small"
                    @click="editPeriode(row)"
                    type="primary"
                    class="enhanced-button btn-action"
                    :disabled="row.statut === 'CLOTURE'"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="cloturerPeriode(row)"
                    type="warning"
                    class="enhanced-button btn-action"
                    :disabled="row.statut === 'CLOTURE'"
                  >
                    <el-icon><Lock /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="deletePeriode(row)"
                    type="danger"
                    class="enhanced-button btn-action"
                    :disabled="row.statut === 'CLOTURE'"
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

    <!-- Modal amélioré pour la clôture -->
    <EnhancedModal
      v-model="showClotureModal"
      title="Clôture de la période de paie"
      subtitle="Cette action est irréversible"
      type="warning"
      icon="Lock"
      :alerts="clotureAlerts"
      @confirm="confirmCloture"
      @cancel="cancelCloture"
      confirm-text="Clôturer"
      cancel-text="Annuler"
      :loading="clotureLoading"
      loading-text="Clôture en cours..."
    >
      <div class="cloture-confirmation">
        <div class="cloture-warning">
          <el-icon class="warning-icon"><WarningFilled /></el-icon>
          <h3>Attention</h3>
        </div>
        <p>Vous êtes sur le point de clôturer la période de paie :</p>
        <div class="periode-summary">
          <div class="summary-item">
            <label>Période:</label>
            <span><strong>{{ getMoisLibelle(selectedPeriode?.mois || '') }} {{ selectedPeriode?.exercice.annee }}</strong></span>
          </div>
          <div class="summary-item">
            <label>Dates:</label>
            <span>{{ formatDate(selectedPeriode?.dateDebut || '') }} au {{ formatDate(selectedPeriode?.dateFin || '') }}</span>
          </div>
        </div>
        <p class="cloture-impact">
          <strong>Impact :</strong> Les bulletins de paie du personnel ne seront plus modifiables pour cette période.
        </p>
        <p>En cliquant sur "Clôturer", le processus sera lancé.</p>
      </div>
    </EnhancedModal>

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
        <p>Êtes-vous sûr de vouloir supprimer la période <strong>{{ getMoisLibelle(selectedPeriode?.mois || '') }} {{ selectedPeriode?.exercice.annee }}</strong> ?</p>
        <p>Cette action supprimera définitivement toutes les données associées.</p>
      </div>
    </EnhancedModal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, Refresh, Close, Calendar, Clock,
  Lock, Unlock, WarningFilled
} from '@element-plus/icons-vue'
import EnhancedForm from '@/components/common/EnhancedForm.vue'
import EnhancedModal from '@/components/common/EnhancedModal.vue'
import periodePaieService, { type PeriodePaieDto, type PeriodePaieRequest } from '@/services/periodePaie.service'
import exerciceService from '@/services/exercice.service'

interface Exercice {
  id: number
  annee: string
  actif: boolean
}

interface PeriodePaie {
  id: number
  exercice: Exercice
  mois: string
  dateDebut: Date
  dateFin: Date
  statut: 'OUVERT' | 'CLOTURE'
  affiche?: string
  dateCreation: Date
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const clotureLoading = ref(false)
const searchText = ref('')
const filterExercice = ref<number | null>(null)
const filterStatut = ref<string | null>(null)
const selectedPeriodes = ref<PeriodePaie[]>([])
const showClotureModal = ref(false)
const showDeleteModal = ref(false)
const selectedPeriode = ref<PeriodePaie | null>(null)

let form = reactive({
  id: 0,
  exerciceId: 0,
  mois: '',
  dateDebut: '',
  dateFin: '',
  statut: 'OUVERT' as 'OUVERT' | 'CLOTURE'
})

const exercices = ref<Exercice[]>([])

// Charger les exercices depuis le backend
const loadExercices = async () => {
  try {
    loading.value = true
    const response = await exerciceService.getAllExercices()
    
    exercices.value = response.map((item: any) => ({
      id: item.id,
      annee: item.annee,
      actif: item.actif
    }))
    
    console.log('Exercices loaded:', exercices.value)
  } catch (error) {
    ElMessage.error('Erreur lors du chargement des exercices')
    console.error('Error loading exercices:', error)
  } finally {
    loading.value = false
  }
}

const moisOptions = [
  { value: '01', label: 'Janvier' },
  { value: '02', label: 'Février' },
  { value: '03', label: 'Mars' },
  { value: '04', label: 'Avril' },
  { value: '05', label: 'Mai' },
  { value: '06', label: 'Juin' },
  { value: '07', label: 'Juillet' },
  { value: '08', label: 'Août' },
  { value: '09', label: 'Septembre' },
  { value: '10', label: 'Octobre' },
  { value: '11', label: 'Novembre' },
  { value: '12', label: 'Décembre' }
]

const periodes = ref<PeriodePaie[]>([])

// Charger les périodes depuis le backend
const loadPeriodes = async () => {
  try {
    loading.value = true
    const response = await periodePaieService.getAllPeriodesPaie()
    
    console.log('Response from periode service:', response)
    console.log('Response length:', response.length)
    
    // Transformer les données du backend pour la vue
    periodes.value = response.map((item: any) => {
      // Gestion robuste des dates
      let dateDebut: Date
      let dateFin: Date
      let dateCreation: Date
      
      // Conversion sécurisée des dates
      if (item.datedeb) {
        const parsedDate = new Date(item.datedeb)
        dateDebut = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else if (item.createdAt) {
        const parsedDate = new Date(item.createdAt)
        dateDebut = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else {
        dateDebut = new Date()
      }
      
      if (item.datefin) {
        const parsedDate = new Date(item.datefin)
        dateFin = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else {
        dateFin = new Date()
      }
      
      if (item.dateCreation) {
        const parsedDate = new Date(item.dateCreation)
        dateCreation = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else if (item.createdAt) {
        const parsedDate = new Date(item.createdAt)
        dateCreation = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else {
        dateCreation = new Date()
      }
      
      return {
        id: item.id,
        exercice: {
          id: item.exerciceId || item.annee?.id || 0,
          annee: item.annee?.annee || '',
          actif: true
        },
        mois: item.mois,
        dateDebut: dateDebut,
        dateFin: dateFin,
        statut: item.datecloture ? 'CLOTURE' : 'OUVERT',
        affiche: item.affiche || '',
        dateCreation: dateCreation,
        createdBy: item.createdBy || 'SYSTEM',
        createdAt: item.createdAt,
        updatedAt: item.updatedAt,
        updatedBy: item.updatedBy
      }
    })
    
    console.log('Periodes loaded:', periodes.value)
  } catch (error) {
    ElMessage.error('Erreur lors du chargement des périodes')
    console.error('Error loading periodes:', error)
  } finally {
    loading.value = false
  }
}

const filteredPeriodes = computed(() => {
  let filtered = periodes.value

  if (searchText.value) {
    filtered = filtered.filter(periode =>
      getMoisLibelle(periode.mois).toLowerCase().includes(searchText.value.toLowerCase()) ||
      periode.exercice.annee.includes(searchText.value)
    )
  }

  if (filterExercice.value !== null) {
    filtered = filtered.filter(periode => periode.exercice.id === filterExercice.value)
  }

  if (filterStatut.value) {
    filtered = filtered.filter(periode => periode.statut === filterStatut.value)
  }

  return filtered
})

const periodesActives = computed(() => {
  return periodes.value.filter(p => p.statut === 'OUVERT').length
})

const clotureAlerts = computed(() => {
  if (!selectedPeriode.value) return []
  
  return [
    {
      type: 'warning' as const,
      title: 'Action irréversible',
      message: 'La clôture rendra les bulletins non modifiables',
      closable: false
    }
  ]
})

const deleteAlerts = computed(() => {
  if (!selectedPeriode.value) return []
  
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
const getMoisLibelle = (mois: string) => {
  const moisOption = moisOptions.find(m => m.value === mois)
  return moisOption ? moisOption.label : mois
}

const formatDate = (date: Date | string | undefined | null) => {
  if (!date) return 'N/A'
  
  try {
    // Si c'est une chaîne de caractères, la convertir en Date
    const dateObj = typeof date === 'string' ? new Date(date) : date
    
    // Vérifier si la date est valide
    if (isNaN(dateObj.getTime())) {
      console.warn('Invalid date:', date)
      return 'Date invalide'
    }
    
    return new Intl.DateTimeFormat('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    }).format(dateObj)
  } catch (error) {
    console.error('Error formatting date:', date, error)
    return 'Erreur date'
  }
}

const calculateDuree = (dateDebut: Date, dateFin: Date) => {
  const debut = new Date(dateDebut)
  const fin = new Date(dateFin)
  const diff = fin.getTime() - debut.getTime()
  return Math.ceil(diff / (1000 * 60 * 60 * 24)) + 1
}

const disabledDateFin = (time: Date) => {
  if (!form.dateDebut) return false
  return time.getTime() < new Date(form.dateDebut).getTime()
}

const tableRowClassName = ({ row }: { row: PeriodePaie }) => {
  return row.statut === 'OUVERT' ? 'row-ouverte' : 'row-cloturee'
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
    exerciceId: 0,
    mois: '',
    dateDebut: '',
    dateFin: '',
    statut: 'OUVERT'
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.exerciceId || !form.mois || !form.dateDebut || !form.dateFin) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  try {
    loading.value = true
    
    if (isEditing.value) {
      // Mise à jour
      await periodePaieService.update({
        id: form.id,
        exerciceId: form.exerciceId,
        mois: form.mois,
        datedeb: form.dateDebut,
        datefin: form.dateFin,
        description: `Période ${form.mois}`
      })
      ElMessage.success('Période mise à jour avec succès')
    } else {
      // Création
      await periodePaieService.create({
        exerciceId: form.exerciceId,
        mois: form.mois,
        datedeb: form.dateDebut,
        datefin: form.dateFin,
        description: `Période ${form.mois}`
      })
      ElMessage.success('Période créée avec succès')
    }
    
    await loadPeriodes()
    closeForm()
  } catch (error) {
    ElMessage.error('Erreur lors de l\'enregistrement')
    console.error('Error saving periode:', error)
  } finally {
    loading.value = false
  }
}

const editPeriode = (periode: PeriodePaie) => {
  Object.assign(form, {
    id: periode.id,
    exerciceId: periode.exercice.id,
    mois: periode.mois,
    dateDebut: periode.dateDebut.toISOString().split('T')[0],
    dateFin: periode.dateFin.toISOString().split('T')[0],
    affiche: periode.affiche || ''
  })
  isEditing.value = true
  showForm.value = true
}

const deletePeriode = async (periode: PeriodePaie) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer la période ${getMoisLibelle(periode.mois)} ${periode.exercice.annee} ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    ).then(async () => {
      await periodePaieService.delete(periode.id)
      ElMessage.success('Période supprimée avec succès')
      await loadPeriodes()
    })
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors de la suppression')
      console.error('Error deleting periode:', error)
    }
  }
}

const cloturerPeriode = async (periode: PeriodePaie) => {
  try {
    const newStatut = periode.statut === 'OUVERT' ? 'CLOTURE' : 'OUVERT'
    const action = periode.statut === 'OUVERT' ? 'clôturer' : 'rouvrir'
    
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir ${action} la période ${getMoisLibelle(periode.mois)} ${periode.exercice.annee} ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    ).then(async () => {
      await periodePaieService.update({
        id: periode.id,
        exerciceId: periode.exercice.id,
        mois: periode.mois,
        datedeb: periode.dateDebut.toISOString().split('T')[0],
        datefin: periode.dateFin.toISOString().split('T')[0],
        datecloture: newStatut === 'CLOTURE' ? new Date().toISOString().split('T')[0] : undefined,
        affiche: periode.affiche || ''
      })
      ElMessage.success(`Période ${action} avec succès`)
      await loadPeriodes()
    })
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors du changement de statut')
      console.error('Error toggling status:', error)
    }
  }
}

const refreshData = async () => {
  await loadPeriodes()
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: PeriodePaie[]) => {
  selectedPeriodes.value = selection
}

onMounted(() => {
  loadExercices()
  loadPeriodes()
})
</script>

<style lang="scss" scoped>
@use '@/styles/design-system.scss' as *;

.enhanced-periodes-paie-view {
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
  
  .periode-form {
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
  padding: 20px;
}

.enhanced-table {
  width: 100% !important;
}

.enhanced-table :deep(.el-table) {
  width: 100% !important;
  table-layout: fixed;
}

.enhanced-table :deep(.el-table__body-wrapper) {
  width: 100% !important;
}

.enhanced-table :deep(.el-table__header-wrapper) {
  width: 100% !important;
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

.form-row {
  display: flex;
  gap: var(--spacing-lg);
  
  .el-form-item {
    flex: 1;
  }
}

.exercice-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  
  .exercice-annee {
    font-weight: 600;
    color: var(--text-primary);
  }
  
  .exercice-status {
    font-size: 12px;
    color: var(--text-secondary);
  }
}

.mois-option {
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

.periode-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .periode-icon {
    font-size: 20px;
    color: var(--primary-color);
  }
  
  .periode-details {
    .periode-mois {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .periode-annee {
      font-size: 12px;
      color: var(--text-secondary);
    }
    
    .periode-affiche {
      font-size: 11px;
      color: var(--text-secondary);
      font-style: italic;
      margin-top: var(--spacing-xs);
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

.action-buttons {
  display: flex;
  gap: var(--spacing-xs);
  
  .btn-action {
    padding: 6px;
    border-radius: var(--border-radius-sm);
    
    &:hover {
      transform: scale(1.1);
    }
    
    &:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }
  }
}

.enhanced-table {
  :deep(.el-table__row) {
    transition: all var(--transition-base);
    
    &:hover {
      background: var(--bg-secondary);
    }
    
    &.row-ouverte {
      background: rgba(103, 194, 58, 0.05);
      
      &:hover {
        background: rgba(103, 194, 58, 0.1);
      }
    }
    
    &.row-cloturee {
      background: rgba(245, 108, 108, 0.05);
      
      &:hover {
        background: rgba(245, 108, 108, 0.1);
      }
    }
  }
}

.cloture-confirmation {
  text-align: center;
  
  .cloture-warning {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: var(--spacing-md);
    margin-bottom: var(--spacing-lg);
    
    .warning-icon {
      font-size: 48px;
      color: var(--warning-color);
    }
    
    h3 {
      margin: 0;
      color: var(--warning-color);
    }
  }
  
  .periode-summary {
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
  
  .cloture-impact {
    background: rgba(230, 162, 60, 0.1);
    border-left: 4px solid var(--warning-color);
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
  
  .form-row {
    flex-direction: column;
    gap: 0;
  }
}

@media (max-width: 768px) {
  .enhanced-periodes-paie-view {
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
