<template>
  <div class="exercice-view">
    <!-- Header aligné sur Organisation -->
    <div class="page-header">
      <h1>Paramétrage des Exercices</h1>
      <p>Gestion des exercices fiscaux et comptables</p>
      
      <!-- Affichage de l'exercice actif -->
      <div v-if="exerciceActif" class="exercice-actif">
        <el-tag type="success" size="large">
          <el-icon style="margin-right: 8px;"><Calendar /></el-icon>
          Exercice actif: {{ exerciceActif.annee }}
        </el-tag>
      </div>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire -->
      <div class="sidebar-panel" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} un Exercice</h3>
          <el-button @click="closeForm" circle>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <el-form :model="form" label-width="100px" style="padding: 20px 20px 10px 20px; flex: 1; overflow-y: auto;">
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Calendar /></el-icon>
              Année de l'exercice
            </label>
            <el-input 
              v-model="form.annee" 
              placeholder="Ex: 2024"
              size="large"
              type="number"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Clock /></el-icon>
              Mois de début
            </label>
            <el-select v-model="form.moisDebut" placeholder="Mois de début" size="large">
              <el-option label="Janvier" value="1" />
              <el-option label="Février" value="2" />
              <el-option label="Mars" value="3" />
              <el-option label="Avril" value="4" />
              <el-option label="Mai" value="5" />
              <el-option label="Juin" value="6" />
              <el-option label="Juillet" value="7" />
              <el-option label="Août" value="8" />
              <el-option label="Septembre" value="9" />
              <el-option label="Octobre" value="10" />
              <el-option label="Novembre" value="11" />
              <el-option label="Décembre" value="12" />
            </el-select>
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><SwitchButton /></el-icon>
              Mois de fin
            </label>
            <el-select v-model="form.moisFin" placeholder="Mois de fin" size="large">
              <el-option label="Janvier" value="1" />
              <el-option label="Février" value="2" />
              <el-option label="Mars" value="3" />
              <el-option label="Avril" value="4" />
              <el-option label="Mai" value="5" />
              <el-option label="Juin" value="6" />
              <el-option label="Juillet" value="7" />
              <el-option label="Août" value="8" />
              <el-option label="Septembre" value="9" />
              <el-option label="Octobre" value="10" />
              <el-option label="Novembre" value="11" />
              <el-option label="Décembre" value="12" />
            </el-select>
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Document /></el-icon>
              Statut
            </label>
            <el-radio-group v-model="form.statut" size="large">
              <el-radio value="OUVERT">Ouvert</el-radio>
              <el-radio value="CLOTURE">Clôturé</el-radio>
            </el-radio-group>
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Message /></el-icon>
              Description
            </label>
            <el-input 
              v-model="form.description" 
              type="textarea"
              :rows="3"
              placeholder="Description de l'exercice..."
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
          <h3>Liste des Exercices</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle>
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary">
              <el-icon><Plus /></el-icon>
              Nouvel Exercice
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher un exercice..."
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
              v-model="filterStatut"
              placeholder="Statut"
              style="width: 150px"
              clearable
            >
              <el-option label="Ouvert" value="OUVERT" />
              <el-option label="Clôturé" value="CLOTURE" />
            </el-select>
          </div>
        </div>

        <!-- Tableau des exercices -->
        <div class="table-container">
          <el-table 
            :data="filteredExercices" 
            style="width: 100%"
            @selection-change="handleSelectionChange"
            class="enhanced-table"
            :row-class-name="tableRowClassName"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="annee" label="Exercice" width="120" sortable>
              <template #default="{ row }">
                <el-tag type="primary" size="large">{{ row.annee }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Période" min-width="220" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><Calendar /></el-icon>
                  <span>{{ getMoisNom(row.periodeDebut) }} - {{ getMoisNom(row.periodeFin) }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="statut" label="Statut" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="row.statut === 'OUVERT' ? 'success' : 'danger'" size="large">
                  <el-icon style="margin-right: 4px;">
                    <SwitchButton v-if="row.statut === 'OUVERT'" />
                    <Close v-else />
                  </el-icon>
                  {{ row.statut === 'OUVERT' ? 'Ouvert' : 'Clôturé' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="description" label="Description" min-width="200" show-overflow-tooltip />
            
            <el-table-column prop="dateCreation" label="Date de création" width="150" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><Clock /></el-icon>
                  <span>{{ formatDate(row.dateCreation) }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="150" fixed="right">
              <template #default="{ row }">
                <el-button-group>
                  <el-button size="small" @click="editExercice(row)" type="primary">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button 
                    size="small" 
                    @click="toggleCloture(row)" 
                    :type="row.statut === 'OUVERT' ? 'warning' : 'success'"
                  >
                    <el-icon><SwitchButton /></el-icon>
                  </el-button>
                  <el-button size="small" @click="deleteExercice(row)" type="danger">
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
  Plus, Edit, Delete, Search, Refresh, Close, Calendar, Clock, 
  SwitchButton, Document, Message
} from '@element-plus/icons-vue'
import exerciceService, { type ExerciceDto, type ExerciceRequest } from '@/services/exercice.service'
import { apiUtils } from '@/services/api'

interface Exercice {
  id: number
  annee: string
  periodeDebut: string
  periodeFin: string
  statut: 'OUVERT' | 'CLOTURE'
  description: string
  dateCreation: Date
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const filterStatut = ref('')
const selectedExercices = ref<Exercice[]>([])

let form = reactive({
  id: 0,
  annee: '',
  moisDebut: '1',
  moisFin: '12',
  statut: 'OUVERT' as 'OUVERT' | 'CLOTURE',
  description: ''
})

const exercices = ref<Exercice[]>([])
const exerciceActif = ref<Exercice | null>(null)

// Charger les exercices depuis le backend
const loadExercices = async () => {
  try {
    loading.value = true
    const response = await exerciceService.getAllExercices()
    
    console.log('Response from exercice service:', response)
    
    exercices.value = response.map((item: ExerciceDto) => ({
      id: item.id,
      annee: item.annee,
      periodeDebut: item.moisDebut,
      periodeFin: item.moisFin,
      statut: item.statut,
      description: item.description || '',
      dateCreation: new Date(item.dateCreation || Date.now())
    }))
    
    console.log('Exercices loaded:', exercices.value)
  } catch (error) {
    ElMessage.error('Erreur lors du chargement des exercices')
    console.error('Error loading exercices:', error)
  } finally {
    loading.value = false
  }
}

// Charger l'exercice actif et peupler la table
const loadExerciceActif = async () => {
  try {
    // Vérifier l'authentification avant l'appel API
    apiUtils.ensureAuth()
    
    loading.value = true
    const response = await exerciceService.getExerciceActif()
    
    console.log('Exercice actif loaded:', response)
    
    // Transformer la réponse et peupler la table
    if (response && response.id) {
      const exerciceTransforme = {
        id: response.id,
        annee: response.annee,
        periodeDebut: response.moisDebut,
        periodeFin: response.moisFin,
        statut: response.statut,
        description: response.description || '',
        dateCreation: new Date(response.dateCreation || Date.now())
      }
      
      // Mettre à jour l'exercice actif
      exerciceActif.value = exerciceTransforme
      
      // Peupler la table avec l'exercice actif
      exercices.value = [exerciceTransforme]
      
      console.log('Table peuplée avec exercice actif:', exerciceTransforme)
    } else {
      // Aucun exercice actif trouvé
      exerciceActif.value = null
      exercices.value = []
      console.log('Aucun exercice actif trouvé')
    }
  } catch (error: any) {
    console.error('Error loading active exercice:', error)
    
    // Si erreur 403, essayer de rafraîchir le token
    if (error.response?.status === 403) {
      console.log('🔄 403 error, attempting token refresh...')
      const refreshed = await apiUtils.refreshToken()
      if (refreshed) {
        // Réessayer la requête après rafraîchissement
        return loadExerciceActif()
      }
    }
    
    ElMessage.error('Erreur lors du chargement de l\'exercice actif')
    exerciceActif.value = null
    exercices.value = []
  } finally {
    loading.value = false
  }
}

const filteredExercices = computed(() => {
  let filtered = exercices.value

  // Filtrer par recherche
  if (searchText.value) {
    filtered = filtered.filter(exercice => 
      exercice.annee.toLowerCase().includes(searchText.value.toLowerCase()) ||
      exercice.description.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  // Filtrer par statut
  if (filterStatut.value) {
    filtered = filtered.filter(exercice => exercice.statut === filterStatut.value)
  }

  return filtered
})

const getMoisNom = (mois: string) => {
  const moisNoms = {
    '1': 'Janvier', '2': 'Février', '3': 'Mars', '4': 'Avril',
    '5': 'Mai', '6': 'Juin', '7': 'Juillet', '8': 'Août',
    '9': 'Septembre', '10': 'Octobre', '11': 'Novembre', '12': 'Décembre'
  }
  return moisNoms[mois as keyof typeof moisNoms] || mois
}

const formatDate = (date: Date) => {
  return new Intl.DateTimeFormat('fr-FR').format(date)
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
    annee: '',
    moisDebut: '1',
    moisFin: '12',
    statut: 'OUVERT',
    description: ''
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.annee) {
    ElMessage.error('Veuillez renseigner l\'année de l\'exercice')
    return
  }

  try {
    loading.value = true
    
    if (isEditing.value) {
      // Mise à jour
      await exerciceService.update(form)
      ElMessage.success('Exercice mis à jour avec succès')
    } else {
      // Création
      await exerciceService.create(form)
      ElMessage.success('Exercice créé avec succès')
    }
    
    // Recharger avec l'exercice actif
    await loadExerciceActif()
    closeForm()
  } catch (error) {
    ElMessage.error('Erreur lors de l\'enregistrement')
    console.error('Error saving exercice:', error)
  } finally {
    loading.value = false
  }
}

const editExercice = (exercice: Exercice) => {
  Object.assign(form, exercice)
  isEditing.value = true
  showForm.value = true
}

const deleteExercice = async (exercice: Exercice) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer l'exercice ${exercice.annee} ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    ).then(async () => {
      await exerciceService.delete(exercice.id)
      ElMessage.success('Exercice supprimé avec succès')
      await loadExercices()
    })
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors de la suppression')
      console.error('Error deleting exercice:', error)
    }
  }
}

const toggleCloture = async (exercice: Exercice) => {
  try {
    const newStatut = exercice.statut === 'OUVERT' ? 'CLOTURE' : 'OUVERT'
    const action = exercice.statut === 'OUVERT' ? 'clôturer' : 'rouvrir'
    
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir ${action} l'exercice ${exercice.annee} ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    ).then(async () => {
      await exerciceService.update({
        id: exercice.id,
        annee: exercice.annee,
        moisDebut: exercice.periodeDebut,
        moisFin: exercice.periodeFin,
        statut: newStatut,
        description: exercice.description
      })
      ElMessage.success(`Exercice ${action} avec succès`)
      
      // Recharger avec l'exercice actif
      await loadExerciceActif()
    })
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors du changement de statut')
      console.error('Error toggling status:', error)
    }
  }
}

const refreshData = async () => {
  await loadExerciceActif()
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: Exercice[]) => {
  selectedExercices.value = selection
}

onMounted(() => {
  // Debug: Vérifier l'état de l'authentification
  console.log('🔍 Auth check:', {
    isAuthenticated: apiUtils.isAuthenticated(),
    token: localStorage.getItem('access_token') ? 'EXISTS' : 'MISSING',
    user: localStorage.getItem('user') ? 'EXISTS' : 'MISSING'
  })
  
  loadExerciceActif()
})
</script>

<style scoped>
.exercice-view {
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

.exercice-actif {
  margin-top: 10px;
  display: inline-block;
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
  width: 100%;
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

.form-group :deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
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
