<template>
  <div class="contrats-view">
    <!-- Header -->
    <div class="page-header">
      <h1>Gestion des Contrats</h1>
      <p>Liste des contrats du personnel</p>
    </div>

    <div class="main-content">
      <!-- Toolbar -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-input
            v-model="searchText"
            placeholder="Rechercher un contrat..."
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
            v-model="currentView"
            placeholder="Type"
            style="width: 150px"
          >
            <el-option label="Tous les contrats" value="all" />
            <el-option label="Contrats actifs" value="active" />
            <el-option label="Contrats expirants" value="expires" />
          </el-select>
          
          <el-button @click="refreshData" circle>
            <el-icon><Refresh /></el-icon>
          </el-button>
          
          <el-button @click="toggleForm" type="primary">
            <el-icon><Plus /></el-icon>
            Nouveau Contrat
          </el-button>
        </div>
      </div>

      <!-- Tableau des contrats -->
      <div class="table-container">
        <el-table 
          :data="contrats" 
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column prop="personnel.matricule" label="Matricule" width="90" sortable>
            <template #default="{ row }">
              <div class="matricule-info">
                <el-tag type="info" size="small" class="enhanced-tag">
                  {{ row.personnel?.matricule || 'N/A' }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="personnel.nom" label="Nom" width="120" sortable>
            <template #default="{ row }">
              <div class="personnel-info">
                <el-icon class="personnel-icon"><User /></el-icon>
                <span>{{ row.personnel?.nom || '' }} {{ row.personnel?.prenom || '' }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="typeContrat" label="Type Contrat" width="120" sortable>
            <template #default="{ row }">
              <el-tag 
                :type="getTypeContratType(row.typeContrat)" 
                size="small" 
                class="enhanced-tag"
              >
                {{ getTypeContratLabel(row.typeContrat) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="fonction" label="Fonction" width="150" sortable>
            <template #default="{ row }">
              <div class="fonction-info">
                <el-icon class="function-icon"><Briefcase /></el-icon>
                <span>{{ getFonctionLabel(row.fonction) }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="dateDebut" label="Date Début" width="120" sortable>
            <template #default="{ row }">
              <div class="date-info">
                <el-icon class="date-icon"><Calendar /></el-icon>
                <span>{{ formatDate(row.dateDebut) }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="dateFin" label="Date Fin" width="120" sortable>
            <template #default="{ row }">
              <div class="date-info">
                <el-icon class="date-icon"><Calendar /></el-icon>
                <span>{{ formatDate(row.dateFin) }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="categorie.salaireBase" label="Salaire Catégoriel" width="130" sortable>
            <template #default="{ row }">
              <div class="salaire-info">
                <el-icon class="salaire-icon"><CreditCard /></el-icon>
                <span>{{ formatCurrency(row.categorie?.salaireBase || row.salaireCategoriel || 0) }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="statut" label="Statut" width="100" sortable>
            <template #default="{ row }">
              <el-tag 
                :type="row.statut ? 'success' : 'danger'" 
                size="small" 
                class="enhanced-tag"
              >
                <el-icon style="margin-right: 4px;">
                  <Clock v-if="row.statut" />
                  <WarningFilled v-else />
                </el-icon>
                {{ row.statut ? 'Actif' : 'Inactif' }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column label="Actions" width="150" fixed="right">
            <template #default="{ row }">
              <el-button-group>
                <el-button size="small" @click="viewContrat(row)" type="primary">
                  <el-icon><View /></el-icon>
                </el-button>
                <el-button size="small" @click="editContrat(row)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button size="small" @click="terminateContrat(row)" type="warning">
                  <el-icon><WarningFilled /></el-icon>
                </el-button>
                <el-button size="small" @click="deleteContrat(row)" type="danger">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- Pagination -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, Refresh, Close, Calendar, Clock,
  User, UserFilled, CreditCard, Briefcase, View, Download,
  Document, WarningFilled
} from '@element-plus/icons-vue'
import { contratPersonnelService, type PaginationRequest } from '@/services/contrat-personnel.service'

interface Personnel {
  id: number
  matricule: string
  nom: string
  prenom: string
  sexe: string
  cnps: string
  situationMatrimoniale: string
  nombreEnfant: number
}

interface ContratPersonnel {
  id: number
  personnel: Personnel
  typeContrat: string | { id: number; libelle: string }
  fonction: string | { id: number; libelle: string }
  dateDebut: string
  dateFin: string
  salaireCategoriel: number
  statut: boolean
  categorie: { id: number; libelle: string; salaireBase: string | number }
  updatedAt?: string
  updatedBy?: string
}

interface ContratPersonnelDTO {
  result: boolean | null
  data: ContratPersonnel[]
  rows: ContratPersonnel[]
  total: number
  status: boolean
  message: string | null
  errors: any
}

interface PaginationRequest {
  page: number
  limit: number
  search?: string
}

// Variables réactives
const contrats = ref<ContratPersonnel[]>([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(0)
const pageSize = ref(10)
const searchText = ref('')
const currentView = ref('all')

// Fonctions utilitaires
const formatDate = (date: string | undefined | null) => {
  if (!date) return 'N/A'
  
  try {
    const dateObj = new Date(date)
    if (isNaN(dateObj.getTime())) {
      return date
    }
    
    return new Intl.DateTimeFormat('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    }).format(dateObj)
  } catch (error) {
    console.error('Error formatting date:', error)
    return 'Date invalide'
  }
}

const formatCurrency = (amount: number | string | undefined) => {
  if (typeof amount === 'undefined' || amount === null) return '0 F CFA'
  
  const numAmount = typeof amount === 'string' ? 
    parseFloat(amount.replace(/[^\d.-]/g, '')) : amount
  
  if (isNaN(numAmount)) return '0 F CFA'
  
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XOF',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(numAmount)
}

const getTypeContratLabel = (typeContrat: string | { libelle: string }) => {
  if (typeof typeContrat === 'string') {
    return typeContrat
  }
  return typeContrat?.libelle || 'N/A'
}

const getTypeContratType = (typeContrat: string | { libelle: string }) => {
  if (typeof typeContrat === 'string') {
    switch (typeContrat?.toLowerCase()) {
      case 'cdi': return 'success'
      case 'cdd': return 'warning'
      case 'stage': return 'info'
      default: return 'info'
    }
  }
  return 'info'
}

const getFonctionLabel = (fonction: string | { libelle: string }) => {
  if (typeof fonction === 'string') {
    return fonction
  }
  return fonction?.libelle || 'N/A'
}

// Fonctions principales
const loadContrats = async () => {
  loading.value = true
  try {
    const pagination: PaginationRequest = {
      page: Math.floor(currentPage.value * pageSize.value / 10),
      limit: pageSize.value,
      search: searchText.value
    }

    console.log('📄 Pagination envoyée:', pagination)

    let response
    switch (currentView.value) {
      case 'all':
        response = await contratPersonnelService.getAllContrats(pagination)
        break
      case 'expires':
        response = await contratPersonnelService.getContratsExpires(pagination)
        break
      default:
        response = await contratPersonnelService.getContratsActifs(pagination)
        break
    }

    console.log('📋 Réponse API:', response)

    // Assigner les données avec réactivité forcée
    contrats.value = response.data?.rows || []
    total.value = response.data?.total || 0
    
    // Forcer la réactivité
    await nextTick()
    
    console.log('📋 Données chargées:', {
      contratsLength: contrats.value.length,
      total: total.value,
      firstContrat: contrats.value[0]
    })

  } catch (error) {
    console.error('Erreur lors du chargement des contrats:', error)
    ElMessage.error('Erreur lors du chargement des contrats')
  } finally {
    loading.value = false
  }
}

const refreshData = async () => {
  await loadContrats()
  ElMessage.success('Données actualisées')
}

const handleSizeChange = (newSize: number) => {
  pageSize.value = newSize
  currentPage.value = 0
  loadContrats()
}

const handleCurrentChange = (newPage: number) => {
  currentPage.value = newPage
  loadContrats()
}

const toggleForm = () => {
  // Implémenter la logique du formulaire
  ElMessage.info('Formulaire à implémenter')
}

const viewContrat = (contrat: ContratPersonnel) => {
  // Implémenter la logique de vue
  ElMessage.info(`Vue du contrat: ${contrat.personnel?.nom} ${contrat.personnel?.prenom}`)
}

const editContrat = (contrat: ContratPersonnel) => {
  // Implémenter la logique d'édition
  ElMessage.info(`Édition du contrat: ${contrat.personnel?.nom} ${contrat.personnel?.prenom}`)
}

const terminateContrat = (contrat: ContratPersonnel) => {
  // Implémenter la logique de terminaison
  ElMessage.info(`Terminaison du contrat: ${contrat.personnel?.nom} ${contrat.personnel?.prenom}`)
}

const deleteContrat = (contrat: ContratPersonnel) => {
  ElMessageBox.confirm(
    `Êtes-vous sûr de vouloir supprimer le contrat de ${contrat.personnel?.nom} ${contrat.personnel?.prenom}?`,
    'Confirmation',
    { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
  ).then(() => {
    // Implémenter la logique de suppression
    ElMessage.success('Contrat supprimé avec succès')
  }).catch(() => {
    // Annulation
  })
}

// Watcher pour la recherche
watch(searchText, () => {
  currentPage.value = 0
  loadContrats()
})

// Watcher pour le changement de vue
watch(currentView, () => {
  currentPage.value = 0
  loadContrats()
})

// Charger au montage
onMounted(() => {
  loadContrats()
})
</script>

<style scoped>
.contrats-view {
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
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.enhanced-tag {
  font-weight: 500;
  border: none;
}

.matricule-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.personnel-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.personnel-icon {
  color: #409eff;
}

.function-icon {
  color: #67c23a;
}

.date-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.date-icon {
  color: #e6a23c;
}

.salaire-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.salaire-icon {
  color: #f56c6c;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
