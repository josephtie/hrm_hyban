<template>
  <div class="personnels-view">
    <!-- Header simple aligné sur Organisation -->
    <div class="page-header">
      <h1>Gestion du Personnel</h1>
      <p>Administration complète des ressources humaines</p>
    </div>

    <!-- Filtres -->
    <div class="filters-section">
      <!-- Première ligne de filtres -->
      <div class="filters-row">
        <el-input
          v-model="searchText"
          placeholder="Rechercher par nom, prénom, matricule..."
          @input="handleSearch"
          style="width: 250px"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <el-select
          v-model="filterService"
          placeholder="Service"
          @change="handleFilter"
          style="width: 150px"
          clearable
        >
          <el-option label="Direction Générale" value="DG" />
          <el-option label="Direction RH" value="DRH" />
          <el-option label="Direction Financière" value="DF" />
          <el-option label="Direction Technique" value="DT" />
          <el-option label="Service Paie" value="PAIE" />
          <el-option label="Service Informatique" value="INFORMATIQUE" />
        </el-select>
        
        <el-select
          v-model="filterStatut"
          placeholder="Statut"
          @change="handleFilter"
          style="width: 150px"
          clearable
        >
          <el-option label="Contractuel" value="true" />
          <el-option label="Non Contractuel" value="false" />
        </el-select>
      </div>
      
      <!-- Deuxième ligne de filtres -->
      <div class="filters-row">
        <el-select
          v-model="filterModePaiement"
          placeholder="Mode Paiement"
          @change="handleFilter"
          style="width: 150px"
          clearable
        >
          <el-option label="Virement Bancaire" value="virement-bancaire" />
          <el-option label="Mobile Money" value="transfert-mobile-money" />
          <el-option label="Espèces" value="espece" />
          <el-option label="Chèque" value="cheque" />
        </el-select>

        <el-select
          v-model="filterFonction"
          placeholder="Poste/Fonction"
          @change="handleFilter"
          style="width: 180px"
          clearable
          filterable
        >
          <el-option label="Directeur Général" value="Directeur Général" />
          <el-option label="Directeur Technique" value="Directeur Technique" />
          <el-option label="Directeur Financier" value="Directeur Financier" />
          <el-option label="Directeur RH" value="Directeur RH" />
          <el-option label="Responsable RH" value="Responsable RH" />
          <el-option label="Comptable Principal" value="Comptable Principal" />
          <el-option label="Développeur Senior" value="Développeur Senior" />
          <el-option label="Gardien NE" value="Gardien NE" />
        </el-select>

        <div class="header-actions">
          <el-button @click="resetFilters" type="info" plain>
            <el-icon><Refresh /></el-icon>
            Réinitialiser les filtres
          </el-button>
          <el-button type="primary" @click="exportPersonnel" :loading="exporting">
            <el-icon><Download /></el-icon>
            Exporter
          </el-button>
          <el-button type="success" @click="openWizard">
            <el-icon><Plus /></el-icon>
            Nouvel Employé
          </el-button>
        </div>
      </div>
    </div>

    <!-- Tableau principal avec scroll interne -->
    <div class="table-container">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>Liste du Personnel ({{ totalPersonnels }})</span>
       
          </div>
        </template>

        <!-- Tableau scrollable -->
        <div class="table-scroll">
          <el-table 
            :data="paginatedPersonnels" 
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column prop="matricule" label="Matricule" width="120" sortable>
              <template #default="{ row }">
                <el-tag type="info" size="small">{{ row.matricule }}</el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Nom complet" min-width="200" sortable>
              <template #default="{ row }">
                <div class="personnel-info">
                  <div class="personnel-name">{{ row.nom }} {{ row.prenom }}</div>
                  <div class="personnel-email">{{ row.email }}</div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="emploi" label="Poste" width="180" sortable>
              <template #default="{ row }">
                {{ row.fonction || row.emploi || '-' }}
              </template>
            </el-table-column>
            
            <el-table-column prop="service" label="Service" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="getServiceType(row.service?.libelle)" size="small">
                  {{ row.service?.libelle || '-' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="modePaiement" label="Mode Paiement" width="140" sortable>
              <template #default="{ row }">
                <el-tag :type="getPaiementType(row.modePaiement)" size="small">
                  {{ formatModePaiement(row.modePaiement) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="telephone" label="Téléphone" width="130" sortable>
              <template #default="{ row }">
                {{ row.telephone || '-' }}
              </template>
            </el-table-column>
            
            <el-table-column prop="numeroCompte" label="N° Compte" width="120" sortable>
              <template #default="{ row }">
                {{ row.numeroCompte || '-' }}
              </template>
            </el-table-column>
            
            <el-table-column prop="carec" label="Statut" width="120" sortable>
              <template #default="{ row }">
                <el-tag :type="getCarecType(row.carec)" size="small">
                  {{ getCarecLabel(row.carec) }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="120" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button @click="viewPersonnel(row)" type="primary" size="small">
                    <el-icon><View /></el-icon>
                  </el-button>
                  <el-button @click="editPersonnel(row)" type="warning" size="small">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- Pagination -->
        <div class="pagination-container">
          <el-pagination
            :current-page="currentPage + 1"
            :page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="totalPersonnels"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Download, Edit, View, Refresh } from '@element-plus/icons-vue'
import { personnelRestService, type PersonnelRestDto } from '@/services/personnel.service'
import { api } from '@/services/api'

interface Personnel {
  id: number
  matricule: string
  nom: string
  prenom: string
  nomComplet: string
  sexe: string
  dateNaissance: string
  numeroCNPS: string
  situationMatrimoniale: string
  modePaiement: string
  numeroCompte: string
  telephone: string
  emploi: string
  salaireNet: number
  statut: string
  service: string
  email: string
}

const router = useRouter()

const showOptionsModal = ref(false)
const selectedPersonnel = ref<Personnel | null>(null)
const selectedPersonnels = ref<PersonnelRestDto[]>([])

// Variables pour la pagination (EXACTEMENT comme CategoriesView)
const currentPage = ref(0) // Numéro de page (0, 1, 2...)
const pageSize = ref(10)
const totalPersonnels = ref(0)
const searchText = ref('')
const filterService = ref('')
const filterStatut = ref('')
const filterModePaiement = ref('')
const filterFonction = ref('')
const loading = ref(false)
const exporting = ref(false)
const personnels = ref<PersonnelRestDto[]>([])

// Méthode pour charger les personnels
const loadPersonnels = async () => {
  try {
    loading.value = true
    console.log('loadPersonnels appelé avec:', { 
      page: currentPage.value, 
      size: pageSize.value,
      search: searchText.value,
      service: filterService.value,
      statut: filterStatut.value,
      modePaiement: filterModePaiement.value,
      fonction: filterFonction.value
    })
    const response = await personnelRestService.getPersonnels({
      page: currentPage.value,
      size: pageSize.value,
      search: searchText.value || undefined,
      service: filterService.value || undefined,
      statut: filterStatut.value || undefined,
      modePaiement: filterModePaiement.value || undefined,
      fonction: filterFonction.value || undefined,
      sortBy: 'nom',
      sortOrder: 'asc'
    })
    personnels.value = response.data
    totalPersonnels.value = response.total || 0
    console.log('Réponse reçue:', { data: response.data, total: response.total })
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || 'Une erreur est survenue lors du chargement du personnel')
  } finally {
    loading.value = false
  }
}

// Handlers pour la pagination (EXACTEMENT comme CategoriesView)
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 0 // Reset à première page
  loadPersonnels()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val - 1 // Convertir 1-indexé en 0-indexé
  loadPersonnels()
}

// Watchers pour la recherche et les filtres
watch(searchText, () => {
  currentPage.value = 0 // Reset à première page pour la recherche
  loadPersonnels()
})

watch(pageSize, () => {
  currentPage.value = 0 // Reset à première page
  loadPersonnels()
})

// Données pour le tableau (pagination côté serveur = utiliser personnels.value directement)
const paginatedPersonnels = computed(() => {
  return personnels.value // Utiliser directement les données paginées par l'API
})

const actifsPersonnels = computed(() => personnels.value.filter(p => p.statut === 'ACTIF').length)
const contratsActifs = computed(() => personnels.value.filter(p => p.statut === 'ACTIF').length)

const openWizard = () => {
  router.push('/personnel/wizard')
}

const handleSearch = () => {
  currentPage.value = 0
  loadPersonnels()
}

const handleFilter = () => {
  currentPage.value = 0
  loadPersonnels()
}

// Fonction pour réinitialiser tous les filtres
const resetFilters = () => {
  searchText.value = ''
  filterService.value = ''
  filterStatut.value = ''
  filterModePaiement.value = ''
  filterFonction.value = ''
  currentPage.value = 0
  loadPersonnels()
}

const showPersonnelOptions = (personnel: Personnel) => {
  selectedPersonnel.value = personnel
  showOptionsModal.value = true
}

const editPersonnel = (personnel: Personnel) => {
  router.push(`/personnel/wizard?id=${personnel.id}`)
}

const viewPersonnel = (personnel: Personnel) => {
  router.push(`/personnel/view/${personnel.id}`)
}

const exportExcel = () => {
  ElMessage.success('Exportation Excel - À implémenter')
}

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF' }).format(amount)
}

const getStatutColor = (statut: string) => {
  const colors = { ACTIF: 'success', INACTIF: 'danger', CONGE: 'warning' }
  return colors[statut as keyof typeof colors] || 'info'
}

const getServiceType = (service: string) => {
  const types = {
    DG: 'danger', DRH: 'warning', DF: 'success', 
    DT: 'primary', PAIE: 'info', INFORMATIQUE: 'primary'
  }
  return types[service as keyof typeof types] || 'default'
}

const getPaiementType = (modePaiement: string) => {
  const types = {
    'virement-bancaire': 'success',
    'transfert-mobile-money': 'primary',
    'espece': 'warning',
    'cheque': 'info'
  }
  return types[modePaiement as keyof typeof types] || 'default'
}

const formatModePaiement = (modePaiement: string) => {
  const formats = {
    'virement-bancaire': 'Virement Bancaire',
    'transfert-mobile-money': 'Mobile Money',
    'espece': 'Espèces',
    'cheque': 'Chèque'
  }
  return formats[modePaiement as keyof typeof formats] || modePaiement || '-'
}

const getCarecType = (carec: boolean) => {
  return carec ? 'success' : 'danger'
}

const getCarecLabel = (carec: boolean) => {
  return carec ? 'Contractuel' : 'Non Contractuel'
}

// Fonction pour exporter les personnels
const exportPersonnel = async () => {
  try {
    exporting.value = true
    
    // Construire les paramètres de filtre pour l'export
    const exportParams = {
      search: searchText.value,
      service: filterService.value,
      statut: filterStatut.value,
      modePaiement: filterModePaiement.value,
      fonction: filterFonction.value
    }
    
    // Appeler l'endpoint d'export avec l'instance api
    const response = await api.post('/personnels/personnel/export', exportParams, {
      responseType: 'blob',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    
    // Télécharger le fichier Excel
    const blob = new Blob([response.data], { 
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
    })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `personnels_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(a)
    a.click()
    window.URL.revokeObjectURL(url)
    document.body.removeChild(a)
    
    ElMessage.success('Export réussi')
  } catch (error) {
    console.error('Erreur export:', error)
    ElMessage.error('Erreur lors de l\'export des personnels')
  } finally {
    exporting.value = false
  }
}

// onMounted pour charger les données au démarrage
onMounted(() => {
  loadPersonnels()
})
</script>

<style scoped>
.personnels-view {
  padding: 20px;
  background: #f5f7fa;
  height: 100vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* Header aligné sur Organisation */
.page-header {
  margin-bottom: 20px;
  flex-shrink: 0;
}

.page-header h1 {
  margin: 0 0 5px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

/* Recherche et filtres */
.filters-section {
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.filters-row {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.filters-row:last-child {
  margin-bottom: 0;
}

.search-filters {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.header-actions {
  margin-left: auto;
  display: flex;
  gap: 10px;
}

/* Tableau avec scroll interne */
.table-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.table-container :deep(.el-card) {
  flex: 1;
  display: flex;
  flex-direction: column;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: hidden;
}

.table-container :deep(.el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  flex-shrink: 0;
}

.export-actions {
  display: flex;
  gap: 10px;
}

/* Table scrollable */
.table-scroll {
  flex: 1;
  overflow-y: auto;
  overflow-x: auto;
}

.pagination-container {
  padding: 20px;
  border-top: 1px solid #e4e7ed;
  flex-shrink: 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #e4e7ed;
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
}

.personnel-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.personnel-details {
  flex: 1;
}

.personnel-name {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

.personnel-email {
  color: #909399;
  font-size: 12px;
}

.amount {
  font-weight: 600;
  color: #67c23a;
}

.pagination-container {
  padding: 20px;
  border-top: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.options-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  padding: 20px 0;
}

.option-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 20px;
  border: 2px solid #e4e7ed;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.option-card:hover {
  border-color: #409eff;
  background: #f0f9ff;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(64, 158, 255, 0.2);
}

.option-icon {
  font-size: 32px;
  color: #409eff;
}

.option-title {
  font-weight: 600;
  color: #303133;
  text-align: center;
}

/* Responsive Design */
@media (max-width: 1200px) {
  .header-content {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }
  
  .header-stats {
    justify-content: center;
  }
  
  .main-content {
    flex-direction: column;
    height: auto;
  }
  
  .sidebar-panel {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .personnels-view {
    padding: 10px;
  }
  
  .header-content {
    padding: 20px;
  }
  
  .header-left h1 {
    font-size: 24px;
  }
  
  .header-stats {
    flex-direction: column;
    width: 100%;
  }
  
  .stat-card {
    min-width: auto;
  }
  
  .toolbar {
    flex-direction: column;
    gap: 15px;
  }
  
  .toolbar-left {
    flex-direction: column;
    width: 100%;
  }
  
  .form-grid {
    grid-template-columns: 1fr;
  }
  
  .options-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

/* Animations */
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.sidebar-panel {
  animation: slideIn 0.3s ease-out;
}

/* Scrollbar Styling */
.form-container::-webkit-scrollbar {
  width: 6px;
}

.form-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.form-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.form-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Element Plus Overrides */
:deep(.el-card__body) {
  padding: 0;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  background: #f8f9fa;
  font-weight: 600;
  color: #303133;
}

:deep(.el-table td) {
  padding: 12px 0;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

:deep(.el-button) {
  border-radius: 8px;
}

:deep(.el-dialog) {
  border-radius: 15px;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
  margin: 0;
  border-radius: 15px 15px 0 0;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}
</style>
