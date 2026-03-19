<template>
  <div class="societe-view">
    <!-- Header aligné sur Organisation -->
    <div class="page-header">
      <h1>Paramétrage des Sociétés</h1>
      <p>Informations sur l'entreprise et ses filiales</p>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire -->
      <div class="sidebar-panel" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Société</h3>
          <el-button @click="closeForm" circle>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <el-form :model="form" label-width="100px" style="padding: 20px 20px 10px 20px; flex: 1; overflow-y: auto;">
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><OfficeBuilding /></el-icon>
              Raison sociale
            </label>
            <el-input 
              v-model="form.raisonSociale" 
              placeholder="Nom de la société"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Flag /></el-icon>
              Sigle
            </label>
            <el-input 
              v-model="form.sigle" 
              placeholder="Acronyme de la société"
              size="large"
              maxlength="10"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Document /></el-icon>
              N° RCCM
            </label>
            <el-input 
              v-model="form.rccm" 
              placeholder="Numéro RCCM"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><CreditCard /></el-icon>
              N° Compte Contribuable
            </label>
            <el-input 
              v-model="form.compteContribuable" 
              placeholder="Numéro de compte contribuable"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Phone /></el-icon>
              Téléphone
            </label>
            <el-input 
              v-model="form.telephone" 
              placeholder="Numéro de téléphone"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Message /></el-icon>
              Email
            </label>
            <el-input 
              v-model="form.email" 
              type="email"
              placeholder="Adresse email"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><House /></el-icon>
              Adresse
            </label>
            <el-input 
              v-model="form.adresse" 
              type="textarea"
              :rows="3"
              placeholder="Adresse complète"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Star /></el-icon>
              Société principale
            </label>
            <el-radio-group v-model="form.principale" size="large">
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
          <h3>Liste des Sociétés</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle>
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary">
              <el-icon><Plus /></el-icon>
              Nouvelle Société
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une société..."
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
              <el-option label="Société principale" :value="true" />
              <el-option label="Filiale" :value="false" />
            </el-select>
          </div>
        </div>

        <!-- Tableau des sociétés -->
        <div class="table-container">
          <el-table 
            :data="filteredSocietes" 
            style="width: 100%"
            @selection-change="handleSelectionChange"
            v-loading="loading"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="raisonSociale" label="Raison sociale" min-width="200" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><OfficeBuilding /></el-icon>
                  <span>{{ row.raisonSociale }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="sigle" label="Sigle" width="100" sortable>
              <template #default="{ row }">
                <el-tag type="primary" size="large">{{ row.sigle }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="rccm" label="N° RCCM" width="120" sortable>
              <template #default="{ row }">
                <el-tag type="info" size="small">{{ row.rccm }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="telephone" label="Téléphone" width="120" sortable>
              <template #default="{ row }">
                <el-tag type="info" size="small">
                  <el-icon><Phone /></el-icon>
                  {{ row.telephone }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="email" label="Email" min-width="200" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><Message /></el-icon>
                  <span>{{ row.email }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="principale" label="Type" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="row.principale ? 'success' : 'warning'" size="large">
                  <el-icon style="margin-right: 4px;">
                    <Star v-if="row.principale" />
                    <OfficeBuilding v-else />
                  </el-icon>
                  {{ row.principale ? 'Principale' : 'Filiale' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="120" fixed="right">
              <template #default="{ row }">
                <el-button-group>
                  <el-button size="small" @click="editSociete(row)" type="primary">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" @click="togglePrincipale(row)" :type="row.principale ? 'warning' : 'success'">
                    <el-icon><Star /></el-icon>
                  </el-button>
                  <el-button size="small" @click="deleteSociete(row)" type="danger">
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
  Plus, Edit, Delete, Search, Refresh, Close, OfficeBuilding, Flag,
  Document, CreditCard, Phone, Message, House, Star
} from '@element-plus/icons-vue'
import { soceterestService, type SocieteRestDto } from '@/services/soceterest.service'

interface Societe {
  id: number
  raisonSociale: string
  sigle: string
  rccm: string
  compteContribuable: string
  telephone: string
  email: string
  adresse: string
  principale: boolean
  dateCreation: Date
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const filterType = ref<boolean | null>(null)
const selectedSocietes = ref<Societe[]>([])
const societes = ref<Societe[]>([])

const filteredSocietes = computed(() => {
  let filtered = societes.value

  if (searchText.value) {
    filtered = filtered.filter(societe => 
      societe.raisonSociale.toLowerCase().includes(searchText.value.toLowerCase()) ||
      societe.sigle.toLowerCase().includes(searchText.value.toLowerCase()) ||
      societe.email.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  if (filterType.value !== null) {
    filtered = filtered.filter(societe => societe.principale === filterType.value)
  }

  return filtered
})

const form = reactive({
  id: 0,
  raisonSociale: '',
  sigle: '',
  rccm: '',
  compteContribuable: '',
  telephone: '',
  email: '',
  adresse: '',
  principale: false
})

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
    raisonSociale: '',
    sigle: '',
    rccm: '',
    compteContribuable: '',
    telephone: '',
    email: '',
    adresse: '',
    principale: false
  })
  isEditing.value = false
}

const loadSocietes = async () => {
  try {
    loading.value = true
    const response = await soceterestService.getAll({
      page: 0,
      size: 100,
      search: searchText.value
    })
    if (response.success) {
      societes.value = response.data.map((societe: any) => ({
        id: societe.id,
        raisonSociale: societe.raisonSociale || '',
        sigle: societe.sigle || '',
        rccm: societe.rccm || '',
        compteContribuable: societe.compteContribuable || '',
        telephone: societe.telephone || '',
        email: societe.email || '',
        adresse: societe.adresse || '',
        principale: societe.principale || false,
        dateCreation: societe.dateCreation ? new Date(societe.dateCreation) : new Date(),
        urlLogo: societe.urlLogo || ''
      }))
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('Erreur lors du chargement des sociétés')
  } finally {
    loading.value = false
  }
}

const saveForm = async () => {
  if (!form.raisonSociale || !form.sigle || !form.rccm) {
    ElMessage.error('Veuillez renseigner les champs obligatoires')
    return
  }

  try {
    loading.value = true
    let response
    
    if (isEditing.value) {
      response = await soceterestService.update(form)
    } else {
      response = await soceterestService.create(form)
    }

    if (response.success) {
      ElMessage.success(isEditing.value ? 'Société mise à jour avec succès' : 'Société créée avec succès')
      closeForm()
      await loadSocietes()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('Erreur lors de l\'opération')
  } finally {
    loading.value = false
  }
}

const editSociete = (societe: Societe) => {
  Object.assign(form, societe)
  isEditing.value = true
  showForm.value = true
}

const togglePrincipale = async (societe: Societe) => {
  const action = societe.principale ? 'retirer du statut de société principale' : 'définir comme société principale'
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir ${action} pour ${societe.raisonSociale} ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    )

    loading.value = true
    const updatedSociete = { ...societe, principale: !societe.principale }
    const response = await soceterestService.update(updatedSociete)
    
    if (response.success) {
      ElMessage.success('Statut modifié avec succès')
      await loadSocietes()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors de la modification')
    }
  } finally {
    loading.value = false
  }
}

const deleteSociete = async (societe: Societe) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer la société ${societe.raisonSociale} ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    )

    loading.value = true
    const response = await soceterestService.delete(societe.id)
    
    if (response.success) {
      ElMessage.success('Société supprimée avec succès')
      await loadSocietes()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors de la suppression')
    }
  } finally {
    loading.value = false
  }
}

const refreshData = async () => {
  await loadSocietes()
  ElMessage.success('Données actualisées')
}

const handleSearch = async () => {
  await loadSocietes()
}

const handleFilter = async () => {
  await loadSocietes()
}

const handleSelectionChange = (selection: Societe[]) => {
  selectedSocietes.value = selection
}

onMounted(() => {
  loadSocietes()
})
</script>

<style scoped>
.societe-view {
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
