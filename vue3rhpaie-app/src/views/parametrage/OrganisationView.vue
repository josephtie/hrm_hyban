<template>
  <div class="organisation-view">
    <div class="page-header">
      <h1>Organisation</h1>
      <p>Gestion de la structure organisationnelle</p>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire -->
      <div class="sidebar-panel" v-if="dialogVisible">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} une structure</h3>
          <el-button @click="closeDialog" circle>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <el-form :model="form" label-width="100px" style="padding: 20px 20px 10px 20px; flex: 1; overflow-y: auto;">
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><OfficeBuilding /></el-icon>
              Type de structure
            </label>
            <el-select v-model="form.type" placeholder="Sélectionner le type" size="large" @change="onTypeChange">
              <el-option label="Direction" value="DIRECTION">
                <div class="option-item">
                  <el-icon><OfficeBuilding /></el-icon>
                  <span>Direction</span>
                </div>
              </el-option>
              <el-option label="Département" value="DEPARTEMENT">
                <div class="option-item">
                  <el-icon><Grid /></el-icon>
                  <span>Département</span>
                </div>
              </el-option>
              <el-option label="Service" value="SERVICE">
                <div class="option-item">
                  <el-icon><Collection /></el-icon>
                  <span>Service</span>
                </div>
              </el-option>
            </el-select>
          </div>
          
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><EditPen /></el-icon>
              Nom de la structure
            </label>
            <el-input 
              v-model="form.libelle" 
              placeholder="Entrez le libellé de la structure" 
              size="large"
              :prefix-icon="EditPen"
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Connection /></el-icon>
              Structure parente
            </label>
            <!-- Pour les DÉPARTEMENTS : choisir la direction parente -->
            <el-select v-if="form.type === 'DEPARTEMENT'" v-model="form.idDirection" placeholder="Sélectionner la direction" size="large" clearable>
              <el-option label="Aucun" :value="0">
                <div class="option-item">
                  <el-icon><Minus /></el-icon>
                  <span>Aucun</span>
                </div>
              </el-option>
              <el-option 
                v-for="direction in directions" 
                :key="direction.id" 
                :label="direction.libelle" 
                :value="direction.id"
              >
                <div class="option-item">
                  <el-icon><OfficeBuilding /></el-icon>
                  <span>{{ direction.libelle }}</span>
                </div>
              </el-option>
            </el-select>
            
            <!-- Pour les SERVICES : choisir le département parent -->
            <el-select v-else-if="form.type === 'SERVICE'" v-model="form.idDepartement" placeholder="Sélectionner le département" size="large" clearable>
              <el-option label="Aucun" :value="0">
                <div class="option-item">
                  <el-icon><Minus /></el-icon>
                  <span>Aucun</span>
                </div>
              </el-option>
              <el-option 
                v-for="departement in departements" 
                :key="departement.id" 
                :label="departement.libelle" 
                :value="departement.id"
              >
                <div class="option-item">
                  <el-icon><Grid /></el-icon>
                  <span>{{ departement.libelle }}</span>
                </div>
              </el-option>
            </el-select>
            
            <!-- Pour les DIRECTIONS : pas de parent -->
            <div v-else style="color: #999; padding: 8px 12px; background: #f5f5f5; border-radius: 4px;">
              Une direction n'a pas de structure parente
            </div>
          </div>
        </el-form>
        
        <div class="panel-footer">
          <el-button @click="closeDialog">Annuler</el-button>
          <el-button type="primary" @click="saveStructure">
            {{ isEditing ? 'Modifier' : 'Ajouter' }}
          </el-button>
        </div>
      </div>

      <!-- Colonne de droite avec le tableau -->
      <div class="table-panel">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Directions & Services</span>
            </div>
          </template>

          <!-- Barre de recherche et filtres -->
          <div class="toolbar">
            <div class="toolbar-left">
              <el-input
                v-model="searchText"
                placeholder="Rechercher..."
                @input="handleSearch"
                style="width: 300px"
                clearable
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              
              <el-select
                v-model="filterType"
                placeholder="Type"
                @change="handleFilter"
                style="width: 150px; margin-left: 10px"
                clearable
              >
                <el-option label="Direction" value="DIRECTION" />
                <el-option label="Département" value="DEPARTEMENT" />
                <el-option label="Service" value="SERVICE" />
              </el-select>
              
              <el-select
                v-model="filterParent"
                placeholder="Parent"
                @change="handleFilter"
                style="width: 200px; margin-left: 10px"
                clearable
              >
                <el-option label="Aucun" value="" />
                <el-option
                  v-for="parent in parentOptions"
                  :key="parent"
                  :label="parent"
                  :value="parent"
                />
              </el-select>
            </div>
            
            <div class="toolbar-right">
              <el-button @click="resetFilters">
                <el-icon><Refresh /></el-icon>
                Réinitialiser
              </el-button>
              <el-button type="primary" @click="showAddDialog">
                <el-icon><Plus /></el-icon>
                Ajouter
              </el-button>
            </div>
          </div>

          <div class="table-container">
            <el-table :data="paginatedStructures" style="width: 100%" v-loading="loading">
              <el-table-column prop="type" label="Type" width="120">
                <template #default="{ row }">
                  <el-tag :type="getTypeColor(row.type)">
                    {{ row.type }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="libelle" label="Nom" />
              <el-table-column label="Direction" width="150">
                <template #default="{ row }">
                  <span v-if="row.type === 'DIRECTION'">-</span>
                  <span v-else-if="row.idDirection">
                    {{ getDirectionLibelle(row.idDirection) }}
                  </span>
                  <span v-else style="color: #999;">Non défini</span>
                </template>
              </el-table-column>
              <el-table-column label="Département" width="150">
                <template #default="{ row }">
                  <span v-if="row.type === 'DIRECTION' || row.type === 'DEPARTEMENT'">-</span>
                  <span v-else-if="row.idDepartement">
                    {{ getDepartementLibelle(row.idDepartement) }}
                  </span>
                  <span v-else style="color: #999;">Non défini</span>
                </template>
              </el-table-column>
              <el-table-column prop="active" label="Actif" width="80">
                <template #default="{ row }">
                  <el-tag :type="row.active ? 'success' : 'danger'">
                    {{ row.active ? 'Oui' : 'Non' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createdAt" label="Créé le" width="150" />
              <el-table-column label="Actions" width="150">
                <template #default="{ row }">
                  <el-button size="small" @click="editService(row)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" type="danger" @click="deleteService(row)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          
          <!-- Pagination -->
          <div class="pagination-container">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[5, 10, 20, 50, 100]"
              :total="filteredStructures.length"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, 
  Edit, 
  Delete, 
  Search, 
  Refresh, 
  Close,
  OfficeBuilding,
  Grid,
  Collection,
  EditPen,
  Connection,
  Minus,
  Share,
  Document
} from '@element-plus/icons-vue'
import { organisationservicerestService, type OrganisationServiceDto } from '@/services/organisationservice.service'

const dialogVisible = ref(false)
const isEditing = ref(false)
const loading = ref(false)

// Données pour la recherche et les filtres
const searchText = ref('')
const filterType = ref('')
const filterParent = ref('')
const currentPage = ref(1)
const pageSize = ref(10)

// Variables pour les services organisationnels
const services = ref<OrganisationServiceDto[]>([])
const directions = ref<OrganisationServiceDto[]>([])
const departements = ref<OrganisationServiceDto[]>([])
const total = ref(0)

// Computed properties
const parentOptions = computed(() => {
  const parents = new Set<string>()
  services.value.forEach(service => {
    if (service.type === 'DIRECTION') {
      parents.add(service.libelle)
    } else if (service.type === 'DEPARTEMENT') {
      const direction = directions.value.find(d => d.id === service.idDirection)
      if (direction) {
        parents.add(direction.libelle)
      }
    } else if (service.type === 'SERVICE') {
      const departement = departements.value.find(d => d.id === service.idDepartement)
      if (departement) {
        parents.add(departement.libelle)
      }
    }
  })
  return Array.from(parents)
})

// Données filtrées pour le tableau
const filteredStructures = computed(() => {
  let filtered = services.value
  
  // Filtrer par recherche
  if (searchText.value) {
    filtered = filtered.filter(item => 
      item.libelle.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }
  
  // Filtrer par type
  if (filterType.value) {
    filtered = filtered.filter(item => item.type === filterType.value)
  }
  
  // Filtrer par parent
  if (filterParent.value) {
    filtered = filtered.filter(item => {
      if (item.type === 'DIRECTION') {
        return false // Les directions n'ont pas de parent
      } else if (item.type === 'DEPARTEMENT') {
        const direction = directions.value.find(d => d.id === item.idDirection)
        return direction && direction.libelle === filterParent.value
      } else if (item.type === 'SERVICE') {
        const departement = departements.value.find(d => d.id === item.idDepartement)
        return departement && departement.libelle === filterParent.value
      }
      return false
    })
  }
  
  return filtered
})

const paginatedStructures = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredStructures.value.slice(start, end)
})

const form = reactive({
  id: null as number | null,
  type: 'SERVICE' as 'DIRECTION' | 'DEPARTEMENT' | 'SERVICE',
  libelle: '',
  idDirection: 0 as number,
  idDepartement: 0 as number,
  active: true
})

// Fonction pour réinitialiser le formulaire
const resetForm = () => {
  form.id = null
  form.type = 'SERVICE'
  form.libelle = ''
  form.idDirection = 0
  form.idDepartement = 0
  form.active = true
}

// Fonction appelée quand le type change pour réinitialiser les relations
const onTypeChange = () => {
  console.log('🔄 Type de service changé:', form.type)
  // Réinitialiser les relations quand le type change
  form.idDirection = 0
  form.idDepartement = 0
  console.log('📝 Relations réinitialisées:', {
    idDirection: form.idDirection,
    idDepartement: form.idDepartement
  })
}

const getTypeColor = (type: string) => {
  const colors = {
    Direction: 'danger',
    Département: 'warning',
    Service: 'primary'
  }
  return colors[type as keyof typeof colors] || 'info'
}

// Fonctions utilitaires pour récupérer les libellés
const getDirectionLibelle = (idDirection: number) => {
  const direction = directions.value.find(d => d.id === idDirection)
  return direction ? direction.libelle : `ID ${idDirection}`
}

const getDepartementLibelle = (idDepartement: number) => {
  const departement = departements.value.find(d => d.id === idDepartement)
  return departement ? departement.libelle : `ID ${idDepartement}`
}

// Méthodes pour la recherche et les filtres
const handleSearch = () => {
  currentPage.value = 1
}

const handleFilter = () => {
  currentPage.value = 1
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
}

const showAddDialog = () => {
  isEditing.value = false
  Object.assign(form, {
    type: '',
    name: '',
    parent: '',
    description: ''
  })
  dialogVisible.value = true
}

const closeDialog = () => {
  dialogVisible.value = false
  resetForm()
}

const resetFilters = () => {
  searchText.value = ''
  filterType.value = ''
  filterParent.value = ''
  currentPage.value = 1
}

const editService = (service: OrganisationServiceDto) => {
  console.log('✏️ Modifier le service:', service)
  isEditing.value = true
  form.id = service.id || null
  form.type = service.type || 'SERVICE'
  form.libelle = service.libelle || ''
  form.idDirection = service.idDirection || 0
  form.idDepartement = service.idDepartement || 0
  form.active = service.active ?? true
  dialogVisible.value = true
}

const deleteService = async (service: OrganisationServiceDto) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer "${service.libelle}" ?`,
      'Confirmation de suppression',
      {
        confirmButtonText: 'Oui, supprimer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )
    
    console.log('🗑️ Supprimer le service:', service.id)
    const response = await organisationservicerestService.delete(service.id!)
    
    if (response.success) {
      ElMessage.success('Service supprimé avec succès')
      await loadServices() // Recharger les données
    } else {
      ElMessage.error(response.message || 'Erreur lors de la suppression')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('💥 Erreur lors de la suppression:', error)
      ElMessage.error('Erreur lors de la suppression')
    }
  }
}

const saveStructure = async () => {
  try {
    console.log('💾 Sauvegarder le service:', form)
    
    const serviceData: OrganisationServiceDto = {
      libelle: form.libelle,
      type: form.type,
      idDirection: form.idDirection && form.idDirection !== 0 ? form.idDirection : undefined,
      idDepartement: form.idDepartement && form.idDepartement !== 0 ? form.idDepartement : undefined,
      active: form.active
    }
    
    // Ajouter l'ID seulement en mode édition
    if (isEditing.value && form.id) {
      serviceData.id = form.id
    }
    
    console.log('📤 Données envoyées au backend:', serviceData)
    
    let response
    if (isEditing.value) {
      console.log('✏️ Mode édition - appel update')
      response = await organisationservicerestService.update(serviceData)
    } else {
      console.log('➕ Mode création - appel create')
      response = await organisationservicerestService.create(serviceData)
    }
    
    console.log('📥 Réponse du backend:', response)
    
    if (response.success) {
      ElMessage.success(`Service ${isEditing.value ? 'modifié' : 'ajouté'} avec succès`)
      dialogVisible.value = false
      resetForm()
      await loadServices() // Recharger les données
    } else {
      ElMessage.error(response.message || `Erreur lors de la ${isEditing.value ? 'modification' : 'création'}`)
    }
  } catch (error) {
    console.error('💥 Erreur lors de la sauvegarde:', error)
    ElMessage.error(`Erreur lors de la ${isEditing.value ? 'modification' : 'création'}`)
  }
}

onMounted(() => {
  console.log('🚀 OrganisationView montée, chargement des services...')
  loadServices()
})

const loadServices = async () => {
  console.log('📡 loadServices appelée')
  try {
    loading.value = true
    
    const response = await organisationservicerestService.getAll({
      page: (currentPage.value - 1) * pageSize.value,
      size: pageSize.value,
      search: searchText.value
    })
    
    console.log('📥 Réponse API:', response)
    
    if (response.success) {
      services.value = response.data
      directions.value = services.value.filter(s => s.type === 'DIRECTION')
      departements.value = services.value.filter(s => s.type === 'DEPARTEMENT')
      total.value = response.total || 0
      
      console.log('✅ Services chargés:', {
        total: services.value.length,
        directions: directions.value.length,
        departements: departements.value.length
      })
    } else {
      console.error('❌ Erreur API:', response.message)
      ElMessage.error(response.message || 'Erreur lors du chargement des services')
    }
  } catch (error) {
    console.error('💥 Exception dans loadServices:', error)
    ElMessage.error('Erreur lors du chargement des services')
  } finally {
    loading.value = false
    console.log('🏁 loadServices terminée')
  }
}
</script>

<style scoped>
.organisation-view {
  padding: 20px;
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
  height: calc(100vh - 200px);
  min-height: 600px;
}

.sidebar-panel {
  width: 30%;
  min-width: 350px;
  max-width: 450px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
  background: #f8f9fa;
  border-radius: 8px 8px 0 0;
  flex-shrink: 0;
}

.panel-header h3 {
  margin: 0;
  color: #303133;
  font-size: 16px;
}

.panel-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 15px 20px;
  border-top: 1px solid #ebeef5;
  background: #f8f9fa;
  border-radius: 0 0 8px 8px;
  flex-shrink: 0;
}

.table-panel {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.table-panel :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.table-panel :deep(.el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 4px;
  margin: 15px;
  flex-shrink: 0;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.toolbar-right {
  display: flex;
  gap: 10px;
  flex-shrink: 0;
}

.table-container {
  flex: 1;
  overflow: hidden;
  margin: 0 15px;
}

.table-container :deep(.el-table) {
  height: 100%;
}

.table-container :deep(.el-table__body-wrapper) {
  overflow-y: auto;
}

.pagination-container {
  padding: 15px;
  border-top: 1px solid #ebeef5;
  background: #fff;
  flex-shrink: 0;
}

/* Styles personnalisés pour le formulaire */
.form-group {
  margin-bottom: 25px;
}

.form-label {
  display: flex;
  align-items: center;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
  font-size: 14px;
}

.label-icon {
  margin-right: 8px;
  color: #409eff;
  font-size: 16px;
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

.option-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.option-item .el-icon {
  color: #909399;
  font-size: 14px;
}

.form-group :deep(.el-select-dropdown__item.selected .option-item .el-icon) {
  color: #409eff;
}

.form-group :deep(.el-input__prefix) {
  color: #909399;
}
</style>
