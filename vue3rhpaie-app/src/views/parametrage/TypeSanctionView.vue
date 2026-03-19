<template>
  <div class="type-sanction-view">
    <!-- Header aligné sur Organisation -->
    <div class="page-header">
      <h1>Paramétrage des Types de Sanctions</h1>
      <p>Gestion des types de sanctions disciplinaires</p>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire -->
      <div class="sidebar-panel" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} un Type de Sanction</h3>
          <el-button @click="closeForm" circle>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <el-form :model="form" label-width="100px" style="padding: 20px 20px 10px 20px; flex: 1; overflow-y: auto;">
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Document /></el-icon>
              Libellé
            </label>
            <el-input 
              v-model="form.libelle" 
              placeholder="Libellé du type de sanction..."
              size="large"
            />
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
              placeholder="Description détaillée..."
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
          <h3>Liste des Types de Sanctions</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle>
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary">
              <el-icon><Plus /></el-icon>
              Nouveau Type
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher un type de sanction..."
              style="width: 300px"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>
        </div>

        <!-- Tableau des types de sanctions -->
        <div class="table-container">
          <el-table 
            :data="filteredTypesSanctions" 
            style="width: 100%"
            v-loading="loading"
            empty-text="Aucun type de sanction trouvé"
          >
            <el-table-column prop="libelle" label="Libellé" min-width="200" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 10px;">
                  <el-avatar :size="32" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                    <el-icon style="color: white;"><Document /></el-icon>
                  </el-avatar>
                  <div>
                    <div style="font-weight: 500; color: #303133;">{{ row.libelle }}</div>
                    <div style="font-size: 12px; color: #909399;">ID: {{ row.id }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="description" label="Description" min-width="300" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon style="color: #909399;"><Message /></el-icon>
                  <span :style="{ color: row.description ? '#606266' : '#c0c4cc' }">
                    {{ row.description || 'Aucune description' }}
                  </span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="dateCreation" label="Date de création" width="150" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon style="color: #909399;"><Clock /></el-icon>
                  <el-tag type="info" size="small">
                    {{ formatDate(row.dateCreation) }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="createdBy" label="Créé par" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="row.createdBy === 'SYSTEM' ? 'warning' : 'success'" size="small">
                  <el-icon><User /></el-icon>
                  {{ row.createdBy || 'Système' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="120" fixed="right">
              <template #default="{ row }">
                <el-button-group>
                  <el-button size="small" @click="editTypeSanction(row)" type="primary">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" @click="deleteTypeSanction(row)" type="danger">
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
import { Plus, Edit, Delete, Search, Refresh, Close, Document, Message, Clock, Warning, User } from '@element-plus/icons-vue'
import typeSanctionViewService, { type TypeSanctionDto, type TypeSanctionRequest } from '@/services/typeSanctionView.service'

interface TypeSanction {
  id: number
  libelle: string
  description?: string
  dateCreation: Date
  createdBy?: string
  createdAt?: string
  updatedAt?: string
  updatedBy?: string
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const typesSanctions = ref<TypeSanction[]>([])

const form = reactive<TypeSanctionRequest>({
  id: 0,
  libelle: '',
  description: ''
})

const loadTypesSanctions = async () => {
  try {
    loading.value = true
    // Utiliser l'endpoint /lister qui renvoie un format JSON propre
    const response = await typeSanctionViewService.getAllTypeSanctions()
    
    console.log('Response from service:', response)
    console.log('Response length:', response.length)
    
    typesSanctions.value = response.map((item: TypeSanctionDto) => {
      // Gestion robuste des dates
      let dateCreation: Date
      
      if (item.dateCreation) {
        // Essayer de parser dateCreation
        const parsedDate = new Date(item.dateCreation)
        dateCreation = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else if (item.createdAt) {
        // Essayer de parser createdAt
        const parsedDate = new Date(item.createdAt)
        dateCreation = isNaN(parsedDate.getTime()) ? new Date() : parsedDate
      } else {
        // Date par défaut
        dateCreation = new Date()
      }
      
      return {
        id: item.id,
        libelle: item.libelle || '',
        description: item.description || '',
        dateCreation: dateCreation,
        createdBy: item.createdBy || 'SYSTEM',
        createdAt: item.createdAt,
        updatedAt: item.updatedAt,
        updatedBy: item.updatedBy
      }
    })
    
    console.log('Types sanctions loaded:', typesSanctions.value)
  } catch (error) {
    ElMessage.error('Erreur lors du chargement des types de sanctions')
    console.error('Error loading types sanctions:', error)
  } finally {
    loading.value = false
  }
}

const filteredTypesSanctions = computed(() => {
  if (!searchText.value) return typesSanctions.value
  
  return typesSanctions.value.filter(type => 
    type.libelle.toLowerCase().includes(searchText.value.toLowerCase()) ||
    (type.description && type.description.toLowerCase().includes(searchText.value.toLowerCase()))
  )
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
  if (!showForm.value) resetForm()
}

const closeForm = () => {
  showForm.value = false
  resetForm()
}

const resetForm = () => {
  Object.assign(form, {
    id: 0,
    libelle: '',
    description: ''
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.libelle) {
    ElMessage.error('Veuillez renseigner le libellé')
    return
  }

  try {
    loading.value = true
    
    if (isEditing.value) {
      await typeSanctionViewService.update(form)
      ElMessage.success('Type de sanction mis à jour')
    } else {
      await typeSanctionViewService.create(form)
      ElMessage.success('Type de sanction créé')
    }

    await loadTypesSanctions()
    closeForm()
  } catch (error) {
    ElMessage.error('Erreur lors de l\'enregistrement')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const editTypeSanction = (type: TypeSanction) => {
  Object.assign(form, {
    id: type.id,
    libelle: type.libelle,
    description: type.description
  })
  isEditing.value = true
  showForm.value = true
}

const deleteTypeSanction = async (type: TypeSanction) => {
  try {
    await ElMessageBox.confirm(
      `Supprimer "${type.libelle}" ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    )
    
    await typeSanctionViewService.delete(type.id)
    ElMessage.success('Type de sanction supprimé')
    await loadTypesSanctions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors de la suppression')
      console.error(error)
    }
  }
}

const refreshData = async () => {
  await loadTypesSanctions()
  ElMessage.success('Données actualisées')
}

onMounted(() => {
  loadTypesSanctions()
})
</script>

<style scoped>
.type-sanction-view {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 24px;
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.page-header h1 {
  margin: 0 0 8px 0;
  font-size: 24px;
  color: #303133;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
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
