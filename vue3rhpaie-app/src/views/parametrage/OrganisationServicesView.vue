<template>
  <div class="organisation-services">
    <!-- Panel Header -->
    <div class="panel panel-warning">
      <div class="panel-heading">
        <div class="panel-title-box">
          <h3>Liste des Services</h3>
          <span>Direction & Departement</span>
        </div>
        <ul class="panel-controls">
          <li>
            <el-tooltip content="Plein écran" placement="top">
              <el-button @click="toggleFullscreen" circle>
                <el-icon><FullScreen /></el-icon>
              </el-button>
            </el-tooltip>
          </li>
          <li>
            <el-tooltip content="Actualiser" placement="top">
              <el-button @click="refreshData" :loading="loading" circle>
                <el-icon><Refresh /></el-icon>
              </el-button>
            </el-tooltip>
          </li>
          <li>
            <el-dropdown @command="handlePanelCommand">
              <el-button circle>
                <el-icon><Setting /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="collapse">
                    <el-icon><ArrowDown /></el-icon> Réduire
                  </el-dropdown-item>
                  <el-dropdown-item command="remove">
                    <el-icon><Close /></el-icon> Supprimer
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </li>
        </ul>
      </div>
      
      <div class="panel-body">
        <!-- Toolbar -->
        <div class="fixed-table-toolbar">
          <div class="bars">
            <el-dropdown @command="handleActionCommand">
              <el-button type="primary">
                Action <el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="new">
                    <el-icon><Plus /></el-icon> Nouveau
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

        <!-- Table -->
        <el-table
          v-loading="loading"
          :data="services"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column
            prop="direction"
            label="Direction"
            sortable
            min-width="200"
          >
            <template #default="{ row }">
              <el-tag v-if="row.type === 'DIRECTION'" type="primary" size="small">
                {{ row.libelle }}
              </el-tag>
              <span v-else>{{ row.direction?.libelle || '-' }}</span>
            </template>
          </el-table-column>
          
          <el-table-column
            prop="departement"
            label="Sous-Direction"
            sortable
            min-width="200"
          >
            <template #default="{ row }">
              <el-tag v-if="row.type === 'DEPARTEMENT'" type="success" size="small">
                {{ row.libelle }}
              </el-tag>
              <span v-else>{{ row.departement?.libelle || '-' }}</span>
            </template>
          </el-table-column>
          
          <el-table-column
            prop="libelle"
            label="Service"
            sortable
            min-width="200"
          >
            <template #default="{ row }">
              <el-tag v-if="row.type === 'SERVICE'" type="info" size="small">
                {{ row.libelle }}
              </el-tag>
              <span v-else>-</span>
            </template>
          </el-table-column>
          
          <el-table-column
            prop="type"
            label="Type"
            width="120"
            sortable
          >
            <template #default="{ row }">
              <el-tag :type="getTypeTagType(row.type)" size="small">
                {{ getTypeLabel(row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column
            label="Options"
            width="150"
            align="center"
            fixed="right"
          >
            <template #default="{ row }">
              <el-button-group>
                <el-tooltip content="Modifier" placement="top">
                  <el-button
                    size="small"
                    type="primary"
                    @click="handleEdit(row)"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="Supprimer" placement="top">
                  <el-button
                    size="small"
                    type="danger"
                    @click="handleDelete(row)"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-tooltip>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- Pagination -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[5, 10, 20, 50, 100, 200]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <!-- Modal Ajout/Modification -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditMode ? 'Modifier' : 'Nouveau'"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item label="Type" prop="type">
          <el-select
            v-model="formData.type"
            placeholder="Sélectionner le type"
            @change="handleTypeChange"
          >
            <el-option
              v-for="type in typeOptions"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item
          v-if="formData.type !== 'DIRECTION'"
          label="Direction"
          prop="idDirection"
        >
          <el-select
            v-model="formData.idDirection"
            placeholder="Sélectionner la direction"
            @change="handleDirectionChange"
          >
            <el-option
              v-for="direction in directions"
              :key="direction.id"
              :label="direction.libelle"
              :value="direction.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item
          v-if="formData.type === 'SERVICE'"
          label="Département"
          prop="idDepartement"
        >
          <el-select
            v-model="formData.idDepartement"
            placeholder="Sélectionner le département"
          >
            <el-option
              v-for="dept in filteredDepartements"
              :key="dept.id"
              :label="dept.libelle"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="getTypeLabel(formData.type)" prop="libelle">
          <el-input
            v-model="formData.libelle"
            :placeholder="`Libellé ${getTypeLabel(formData.type).toLowerCase()}`"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">Annuler</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          Valider
        </el-button>
      </template>
    </el-dialog>

    <!-- Modal Suppression -->
    <el-dialog
      v-model="deleteDialogVisible"
      title="Confirmation de suppression"
      width="400px"
    >
      <div class="delete-confirm">
        <el-icon class="warning-icon"><WarningFilled /></el-icon>
        <h4>Êtes-vous sûr de vouloir supprimer ?</h4>
        <p>{{ selectedItem?.libelle }}</p>
      </div>
      
      <template #footer>
        <el-button @click="deleteDialogVisible = false">Annuler</el-button>
        <el-button type="danger" @click="confirmDelete" :loading="deleting">
          Supprimer
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import {
  FullScreen,
  Refresh,
  Setting,
  ArrowDown,
  Close,
  Plus,
  Edit,
  Delete,
  WarningFilled
} from '@element-plus/icons-vue'
import { organisationservicerestService, type OrganisationServiceDto } from '@/services/organisationservice.service'

// Types
interface Service {
  id: string
  libelle: string
  type: 'DIRECTION' | 'DEPARTEMENT' | 'SERVICE'
  idDirection?: string
  idDepartement?: string
  direction?: Service
  departement?: Service
  active: boolean
  createdAt: string
}

// Reactive data
const loading = ref(false)
const submitting = ref(false)
const deleting = ref(false)
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEditMode = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedItems = ref<Service[]>([])
const selectedItem = ref<Service | null>(null)

const formRef = ref<FormInstance>()

const services = ref<Service[]>([])
const directions = ref<Service[]>([])
const departements = ref<Service[]>([])

const formData = reactive({
  id: '',
  libelle: '',
  type: 'SERVICE' as 'DIRECTION' | 'DEPARTEMENT' | 'SERVICE',
  idDirection: '',
  idDepartement: ''
})

const formRules = {
  type: [{ required: true, message: 'Veuillez sélectionner un type', trigger: 'change' }],
  libelle: [{ required: true, message: 'Veuillez saisir un libellé', trigger: 'blur' }],
  idDirection: [
    { 
      required: true, 
      message: 'Veuillez sélectionner une direction', 
      trigger: 'change' 
    }
  ],
  idDepartement: [
    { 
      required: true, 
      message: 'Veuillez sélectionner un département', 
      trigger: 'change' 
    }
  ]
}

const typeOptions = [
  { value: 'DIRECTION', label: 'Direction' },
  { value: 'DEPARTEMENT', label: 'Département' },
  { value: 'SERVICE', label: 'Service' }
]

// Computed
const filteredDepartements = computed(() => {
  if (!formData.idDirection) return []
  return departements.value.filter(dept => dept.idDirection === formData.idDirection)
})

// Methods
const getTypeLabel = (type: string) => {
  const option = typeOptions.find(opt => opt.value === type)
  return option?.label || type
}

const getTypeTagType = (type: string) => {
  const types = {
    DIRECTION: 'primary',
    DEPARTEMENT: 'success',
    SERVICE: 'info'
  }
  return types[type as keyof typeof types] || 'info'
}

const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

const refreshData = async () => {
  loading.value = true
  try {
    await fetchData()
    ElMessage.success('Données actualisées')
  } catch (error) {
    ElMessage.error('Erreur lors de l\'actualisation')
  } finally {
    loading.value = false
  }
}

const handlePanelCommand = (command: string) => {
  switch (command) {
    case 'collapse':
      // Implement collapse logic
      break
    case 'remove':
      ElMessageBox.confirm('Voulez-vous supprimer ce panneau ?', 'Confirmation')
        .then(() => {
          ElMessage.success('Panneau supprimé')
        })
      break
  }
}

const handleActionCommand = (command: string) => {
  switch (command) {
    case 'new':
      handleNew()
      break
  }
}

const handleNew = () => {
  isEditMode.value = false
  dialogVisible.value = true
}

const handleEdit = (row: Service) => {
  isEditMode.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = (row: Service) => {
  selectedItem.value = row
  deleteDialogVisible.value = true
}

const confirmDelete = async () => {
  if (!selectedItem.value) return
  
  deleting.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    const index = services.value.findIndex(s => s.id === selectedItem.value!.id)
    if (index > -1) {
      services.value.splice(index, 1)
    }
    
    ElMessage.success('Supprimé avec succès')
    deleteDialogVisible.value = false
  } catch (error) {
    ElMessage.error('Erreur lors de la suppression')
  } finally {
    deleting.value = false
  }
}

const handleTypeChange = () => {
  formData.idDirection = ''
  formData.idDepartement = ''
}

const handleDirectionChange = () => {
  formData.idDepartement = ''
}

const handleSelectionChange = (selection: Service[]) => {
  selectedItems.value = selection
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchData()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchData()
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(formData, {
    id: '',
    libelle: '',
    type: 'SERVICE',
    idDirection: '',
    idDepartement: ''
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    if (isEditMode.value) {
      const index = services.value.findIndex(s => s.id === formData.id)
      if (index > -1) {
        services.value[index] = { ...formData, active: true }
      }
      ElMessage.success('Modifié avec succès')
    } else {
      const newService: Service = {
        ...formData,
        id: Date.now().toString(),
        active: true,
        createdAt: new Date().toISOString(),
        direction: undefined,
        departement: undefined
      }
      services.value.push(newService)
      ElMessage.success('Ajouté avec succès')
    }
    
    dialogVisible.value = false
  } catch (error) {
    ElMessage.error('Erreur lors de la soumission')
  } finally {
    submitting.value = false
  }
}

const fetchData = async () => {
  console.log('🚀 fetchData appelée')
  try {
    loading.value = true
    console.log('📡 Appel API organisationservicerestService.getAll')
    
    const response = await organisationservicerestService.getAll({
      page: (currentPage.value - 1) * pageSize.value,
      size: pageSize.value,
      search: '' // Ajouter la recherche si nécessaire
    })
    
    console.log('📥 Réponse API:', response)
    
    if (response.success) {
      // Transformer les données pour la vue
      services.value = response.data.map((service: OrganisationServiceDto) => ({
        id: service.id?.toString() || '',
        libelle: service.libelle || '',
        type: service.type || 'SERVICE',
        idDirection: service.idDirection?.toString() || '',
        idDepartement: service.idDepartement?.toString() || '',
        active: service.active ?? true,
        createdAt: service.createdAt || new Date().toISOString()
      }))
      
      console.log('✅ Services transformés:', services.value)
      
      // Mettre à jour les listes filtrées
      directions.value = services.value.filter(s => s.type === 'DIRECTION')
      departements.value = services.value.filter(s => s.type === 'DEPARTEMENT')
      
      total.value = response.total || 0
      console.log('📊 Total:', total.value)
    } else {
      console.error('❌ Erreur API:', response.message)
      ElMessage.error(response.message || 'Erreur lors du chargement des services')
    }
  } catch (error) {
    console.error('💥 Exception dans fetchData:', error)
    ElMessage.error('Erreur lors du chargement des services')
  } finally {
    loading.value = false
    console.log('🏁 fetchData terminée, loading:', loading.value)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.organisation-services {
  padding: 20px;
}

.panel {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.panel-heading {
  background: #f0ad4e;
  color: #fff;
  padding: 15px 20px;
  border-radius: 4px 4px 0 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-title-box h3 {
  margin: 0 0 5px 0;
  font-size: 18px;
}

.panel-title-box span {
  font-size: 14px;
  opacity: 0.9;
}

.panel-controls {
  display: flex;
  gap: 8px;
  list-style: none;
  margin: 0;
  padding: 0;
}

.panel-controls li {
  display: inline-block;
}

.panel-body {
  padding: 20px;
}

.fixed-table-toolbar {
  margin-bottom: 20px;
}

.bars {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.delete-confirm {
  text-align: center;
  padding: 20px 0;
}

.warning-icon {
  font-size: 48px;
  color: #e6a23c;
  margin-bottom: 15px;
}

.delete-confirm h4 {
  margin: 10px 0;
  color: #303133;
}

.delete-confirm p {
  color: #606266;
  margin: 0;
}
</style>
