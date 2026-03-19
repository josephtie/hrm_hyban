<template>
  <div class="banque-view">
    <!-- Header aligné sur Organisation -->
    <div class="page-header">
      <h1>Paramétrage des Banques</h1>
      <p>Gestion des établissements bancaires</p>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire -->
      <div class="sidebar-panel" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Banque</h3>
          <el-button @click="closeForm" circle>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <el-form :model="form" label-width="100px" style="padding: 20px 20px 10px 20px; flex: 1; overflow-y: auto;">
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><OfficeBuilding /></el-icon>
              Sigle
            </label>
            <el-input 
              v-model="form.sigle" 
              placeholder="Ex: SGBCI, ECOBANK"
              size="large"
              maxlength="10"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><CreditCard /></el-icon>
              Nom de la banque
            </label>
            <el-input 
              v-model="form.libelle" 
              type="textarea"
              :rows="3"
              placeholder="Nom complet de la banque..."
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Key /></el-icon>
              Code de banque
            </label>
            <el-input 
              v-model="form.codbanq" 
              placeholder="Code bancaire (5 caractères)"
              size="large"
              maxlength="5"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Star /></el-icon>
              Banque principale
            </label>
            <el-radio-group v-model="form.statut" size="large">
              <el-radio :value="true">Oui</el-radio>
              <el-radio :value="false">Non</el-radio>
            </el-radio-group>
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
          <h3>Liste des Banques</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle>
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary">
              <el-icon><Plus /></el-icon>
              Nouvelle Banque
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une banque..."
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
              placeholder="Type"
              style="width: 150px"
              clearable
            >
              <el-option label="Banque principale" :value="true" />
              <el-option label="Banque secondaire" :value="false" />
            </el-select>
          </div>
        </div>

        <!-- Tableau des banques -->
        <div class="table-container">
          <el-table 
            :data="filteredBanques" 
            style="width: 100%"
            @selection-change="handleSelectionChange"
            v-loading="loading"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="sigle" label="Sigle" width="120" sortable>
              <template #default="{ row }">
                <el-tag type="primary" size="large">{{ row.sigle }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="libelle" label="Banque" min-width="200" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><OfficeBuilding /></el-icon>
                  <span>{{ row.libelle }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="codbanq" label="Code de banque" width="120" sortable>
              <template #default="{ row }">
                <el-tag type="info" size="large">{{ row.codbanq }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="statut" label="Entreprise" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="row.statut ? 'success' : 'warning'" size="large">
                  <el-icon style="margin-right: 4px;">
                    <Star v-if="row.statut" />
                    <OfficeBuilding v-else />
                  </el-icon>
                  {{ row.statut ? 'Principale' : 'Secondaire' }}
                </el-tag>
              </template>
            </el-table-column>
            
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
                  <el-button size="small" @click="editBanque(row)" type="primary">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" @click="toggleStatut(row)" :type="row.statut ? 'warning' : 'success'">
                    <el-icon><Star /></el-icon>
                  </el-button>
                  <el-button size="small" @click="deleteBanque(row)" type="danger">
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
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Edit, Delete, Search, Refresh, Close, OfficeBuilding, CreditCard,
  Star, Clock, Key
} from '@element-plus/icons-vue'
import { banquerestService, type BanqueRestDto } from '@/services/banquerest.service'

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
const searchText = ref('')
const filterStatut = ref<boolean | null>(null)
const selectedBanques = ref<Banque[]>([])
const loading = ref(false)

const form = reactive({
  id: 0,
  sigle: '',
  libelle: '',
  codbanq: '',
  statut: false
})

const banques = ref<Banque[]>([])

// Charger les banques depuis le service
const loadBanques = async () => {
  try {
    loading.value = true
    const response = await banquerestService.getAll({
      search: searchText.value || undefined,
      size: 50,
      page: 0
    })
    
    console.log('Response from banque service:', response)
    console.log('Response length:', response.data?.length || 0)
    
    // Transformer les données du backend pour la vue avec gestion robuste
    banques.value = response.data.map((b: any) => {
      // Gestion robuste de la date de création
      let dateCreation: Date
      
      if (b.createdAt) {
        const parsedDate = new Date(b.createdAt)
        dateCreation = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else if (b.dateCreation) {
        const parsedDate = new Date(b.dateCreation)
        dateCreation = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else {
        dateCreation = new Date()
      }
      
      return {
        id: b.id || 0,
        sigle: b.codbanq || b.sigle || '', // Utiliser codbanq comme sigle
        libelle: b.libelle || '',
        codbanq: b.codbanq || '',
        statut: b.statut !== undefined ? b.statut : false,
        dateCreation: dateCreation,
        createdBy: b.createdBy || 'SYSTEM',
        createdAt: b.createdAt,
        updatedAt: b.updatedAt,
        updatedBy: b.updatedBy
      }
    })
    
    console.log('Banques loaded:', banques.value)
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || 'Erreur lors du chargement des banques')
    console.error('Error loading banques:', error)
  } finally {
    loading.value = false
  }
}

// Charger au montage du composant
onMounted(() => {
  loadBanques()
})

// Watcher pour la recherche
watch(searchText, () => {
  loadBanques()
})

const filteredBanques = computed(() => {
  let filtered = banques.value

  // Filtrer par statut uniquement (la recherche est gérée par le service)
  if (filterStatut.value !== null) {
    filtered = filtered.filter(banque => banque.statut === filterStatut.value)
  }

  return filtered
})

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

  try {
    if (isEditing.value) {
      // Mise à jour
      await banquerestService.update(form.id, {
        codbanq: form.codbanq,
        libelle: form.libelle,
        statut: form.statut
      })
      ElMessage.success('Banque mise à jour avec succès')
    } else {
      // Création
      await banquerestService.create({
        codbanq: form.codbanq,
        libelle: form.libelle,
        statut: form.statut
      })
      ElMessage.success('Banque créée avec succès')
    }

    await loadBanques()
    closeForm()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || 'Erreur lors de l\'enregistrement')
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

const deleteBanque = async (banque: Banque) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer la banque ${banque.sigle} ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    )
    
    await banquerestService.delete(banque.id)
    ElMessage.success('Banque supprimée avec succès')
    await loadBanques()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.response?.data?.message || 'Erreur lors de la suppression')
    }
  }
}

const refreshData = async () => {
  await loadBanques()
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: Banque[]) => {
  selectedBanques.value = selection
}

onMounted(() => {
  // Initialisation
})
</script>

<style scoped>
.banque-view {
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
  width: 100%;
}

.el-table {
  width: 100% !important;
}

.el-table :deep(.el-table) {
  width: 100% !important;
  table-layout: fixed;
}

.el-table :deep(.el-table__body-wrapper) {
  width: 100% !important;
}

.el-table :deep(.el-table__header-wrapper) {
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
