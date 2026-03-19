<template>
  <div class="sanction-view">
    <!-- Header aligné sur Organisation -->
    <div class="page-header">
      <h1>Paramétrage des Sanctions</h1>
      <p>Gestion des types de sanctions disciplinaires</p>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire -->
      <div class="sidebar-panel" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une Sanction</h3>
          <el-button @click="closeForm" circle>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <el-form :model="form" label-width="100px" style="padding: 20px 20px 10px 20px; flex: 1; overflow-y: auto;">
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Warning /></el-icon>
              Type de sanction
            </label>
            <el-select v-model="form.idTypeSanction" placeholder="Type de sanction" size="large">
              <el-option 
                v-for="type in typesSanctions" 
                :key="type.id"
                :label="type.libelle" 
                :value="type.id" 
              />
            </el-select>
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Message /></el-icon>
              Faute
            </label>
            <el-input 
              v-model="form.faute" 
              placeholder="Description de la faute..."
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Document /></el-icon>
              Commentaire
            </label>
            <el-input 
              v-model="form.commentaire" 
              type="textarea"
              :rows="3"
              placeholder="Commentaires supplémentaires..."
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
          <h3>Liste des Sanctions</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle>
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary">
              <el-icon><Plus /></el-icon>
              Nouvelle Sanction
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher une sanction..."
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
              placeholder="Type de sanction"
              style="width: 180px"
              clearable
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

        <!-- Tableau des sanctions -->
        <div class="table-container">
          <el-table 
            :data="filteredSanctions" 
            style="width: 100%"
            @selection-change="handleSelectionChange"
            v-loading="loading"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="faute" label="Faute" min-width="200" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><Warning /></el-icon>
                  <span>{{ row.faute }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="commentaire" label="Commentaire" min-width="200" show-overflow-tooltip sortable />
            
            <el-table-column prop="typeSanction" label="Type de sanction" width="180" sortable>
              <template #default="{ row }">
                <el-tag :type="getTypeSanctionColor(row.idTypeSanction)" size="small">
                  {{ row.typeSanction }}
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
            
            <el-table-column label="Actions" width="120" fixed="right">
              <template #default="{ row }">
                <el-button-group>
                  <el-button size="small" @click="editSanction(row)" type="primary">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" @click="deleteSanction(row)" type="danger">
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
  Plus, Edit, Delete, Search, Refresh, Close, Warning, Message, 
  Document, Clock
} from '@element-plus/icons-vue'
import { sanctionrestService, type SanctionRestDto } from '@/services/sanctionrest.service'

interface Sanction {
  id: number
  faute: string
  commentaire: string
  idTypeSanction: number
  typeSanction: string
  dateCreation: Date
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const filterType = ref('')
const selectedSanctions = ref<Sanction[]>([])
const sanctions = ref<Sanction[]>([])
const typesSanctions = ref<any[]>([])

const form = reactive<SanctionRestDto>({
  id: 0,
  idTypeSanction: 0,
  faute: '',
  commentaire: ''
})

// Charger les données depuis le backend
const loadSanctions = async () => {
  try {
    loading.value = true
    const response = await sanctionrestService.getAll({
      search: searchText.value || undefined,
      size: 50,
      page: 0
    })
    
    console.log('Response from sanction service:', response)
    console.log('Response length:', response.data?.length || 0)
    
    // Transformer les données du backend pour la vue
    // Le service extrait maintenant correctement les données
    const actualData = response.data || []
    
    sanctions.value = actualData.map((item: any) => {
      // Gestion robuste des dates
      let dateCreation: Date
      
      if (item.dateCreation) {
        const parsedDate = new Date(item.dateCreation)
        dateCreation = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else if (item.createdAt) {
        const parsedDate = new Date(item.createdAt)
        dateCreation = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else {
        dateCreation = new Date()
      }
      
      // Gestion robuste du typeSanction - on voit dans les logs que c'est un objet avec libelle
      let typeSanctionLibelle = 'Non défini'
      if (item.typeSanction && typeof item.typeSanction === 'object') {
        typeSanctionLibelle = item.typeSanction.libelle || item.typeSanction.code || 'Non défini'
      } else if (typeof item.typeSanction === 'string') {
        typeSanctionLibelle = item.typeSanction
      }
      
      return {
        id: item.id || 0,
        faute: item.faute || '',
        commentaire: item.commentaire || '',
        idTypeSanction: item.idTypeSanction || 0,
        typeSanction: typeSanctionLibelle,
        dateCreation: dateCreation,
        createdBy: item.createdBy || 'SYSTEM',
        createdAt: item.createdAt,
        updatedAt: item.updatedAt,
        updatedBy: item.updatedBy
      }
    })
    
    console.log('Sanctions loaded:', sanctions.value)
    console.log('TypeSanction debug:', sanctions.value.map(s => ({ id: s.id, typeSanction: s.typeSanction })))
  } catch (error) {
    console.error('Error loading sanctions:', error)
    ElMessage.error('Erreur lors du chargement des sanctions')
  } finally {
    loading.value = false
  }
}

// Charger les types de sanctions
const loadTypesSanctions = async () => {
  try {
    const response = await sanctionrestService.getTypesSanctions()
    typesSanctions.value = response.data
  } catch (error) {
    console.error('Error loading types sanctions:', error)
    ElMessage.error('Erreur lors du chargement des types de sanctions')
  }
}

const filteredSanctions = computed(() => {
  let filtered = sanctions.value

  if (searchText.value) {
    filtered = filtered.filter(sanction => 
      sanction.faute.toLowerCase().includes(searchText.value.toLowerCase()) ||
      sanction.commentaire.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  if (filterType.value) {
    filtered = filtered.filter(sanction => sanction.idTypeSanction === Number(filterType.value))
  }

  return filtered
})

const getTypeSanctionLibelle = (typeId: number): string => {
  const type = typesSanctions.value.find(t => t.id === typeId)
  return type?.libelle || 'Type inconnu'
}

const getTypeSanctionColor = (typeId: number): string => {
  const type = typesSanctions.value.find(t => t.id === typeId)
  const libelle = type?.libelle || ''
  
  const colors: { [key: string]: string } = {
    'Avertissement verbal': 'info',
    'Avertissement écrit': 'warning',
    'Mise à pied': 'danger',
    'Rétrogradation': 'warning',
    'Licenciement': 'danger',
    'Autre': 'info'
  }
  
  return colors[libelle] || 'info'
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
    idTypeSanction: 0,
    faute: '',
    commentaire: ''
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.faute || !form.idTypeSanction) {
    ElMessage.error('Veuillez renseigner la faute et le type de sanction')
    return
  }

  try {
    loading.value = true
    
    const sanctionData: SanctionRestDto = {
      idTypeSanction: form.idTypeSanction,
      faute: form.faute,
      commentaire: form.commentaire
    }

    if (isEditing.value) {
      // Pour la modification, ajouter l'ID
      sanctionData.id = form.id
      await sanctionrestService.update(sanctionData)
      ElMessage.success('Sanction mise à jour avec succès')
    } else {
      // Pour la création, pas d'ID
      await sanctionrestService.create(sanctionData)
      ElMessage.success('Sanction créée avec succès')
    }

    await loadSanctions()
    closeForm()
  } catch (error) {
    ElMessage.error('Erreur lors de l\'enregistrement')
    console.error('Error saving sanction:', error)
  } finally {
    loading.value = false
  }
}

const editSanction = (sanction: Sanction) => {
  Object.assign(form, {
    id: sanction.id,
    idTypeSanction: sanction.idTypeSanction,
    faute: sanction.faute,
    commentaire: sanction.commentaire
  })
  isEditing.value = true
  showForm.value = true
}

const deleteSanction = async (sanction: Sanction) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer la sanction "${sanction.faute}" ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    )
    
    await sanctionrestService.delete(sanction.id)
    ElMessage.success('Sanction supprimée avec succès')
    await loadSanctions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors de la suppression')
      console.error(error)
    }
  }
}

const refreshData = async () => {
  await Promise.all([loadSanctions(), loadTypesSanctions()])
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: Sanction[]) => {
  selectedSanctions.value = selection
}

onMounted(() => {
  Promise.all([loadSanctions(), loadTypesSanctions()])
})
</script>

<style scoped>
.sanction-view {
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

.form-group :deep(.el-input__prefix) {
  color: #909399;
}
</style>
