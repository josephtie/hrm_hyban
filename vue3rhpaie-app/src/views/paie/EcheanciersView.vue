<template>
  <div class="echeanciers-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><List /></el-icon>
            Échéanciers de Prêts
          </h1>
          <p class="page-subtitle">Suivi des échéances de remboursement des prêts</p>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="generateEcheanciers" class="enhanced-button">
            <el-icon><Refresh /></el-icon>
            Générer les Échéanciers
          </el-button>
        </div>
      </div>
    </div>

    <!-- Filtres -->
    <div class="filters-section enhanced-card">
      <div class="filters-content">
        <el-row :gutter="16">
          <el-col :span="6">
            <el-select v-model="filters.personnelId" placeholder="Personnel" clearable style="width: 100%">
              <el-option
                v-for="personnel in personnels"
                :key="personnel.id"
                :label="`${personnel.nomComplet} - ${personnel.matricule}`"
                :value="personnel.id"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filters.statut" placeholder="Statut" clearable style="width: 100%">
              <el-option label="En attente" value="en_attente" />
              <el-option label="Payé" value="paye" />
              <el-option label="En retard" value="en_retard" />
              <el-option label="Partiellement payé" value="partiellement_paye" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-date-picker
              v-model="filters.periode"
              type="month"
              placeholder="Période"
              format="MM/YYYY"
              value-format="YYYY-MM"
              style="width: 100%"
            />
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="applyFilters">
              <el-icon><Search /></el-icon>
              Rechercher
            </el-button>
            <el-button @click="resetFilters">Réinitialiser</el-button>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- Tableau des échéanciers -->
    <div class="table-section enhanced-card">
      <div class="table-header">
        <h3>Liste des Échéances</h3>
        <div class="table-controls">
          <el-button type="success" @click="markAsPaid" :disabled="!selectedEcheanciers.length">
            <el-icon><Check /></el-icon>
            Marquer comme payé
          </el-button>
          <el-button type="warning" @click="exportEcheanciers">
            <el-icon><Download /></el-icon>
            Exporter
          </el-button>
        </div>
      </div>

      <div class="table-container">
        <el-table 
          :data="filteredEcheanciers" 
          v-loading="loading"
          @selection-change="handleSelectionChange"
          stripe
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column label="Personnel" min-width="200">
            <template #default="{ row }">
              <div class="personnel-info">
                <div class="personnel-name">{{ row.personnelNom }}</div>
                <div class="personnel-matricule">{{ row.matricule }}</div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Prêt" min-width="150">
            <template #default="{ row }">
              <div class="loan-info">
                <div class="loan-type">{{ row.typePret }}</div>
                <div class="loan-amount">{{ formatCurrency(row.montantPret) }}</div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Échéance N°" width="100" align="center">
            <template #default="{ row }">
              <el-tag type="info">{{ row.numeroEcheance }}/{{ row.totalEcheances }}</el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Période" width="120" sortable>
            <template #default="{ row }">
              {{ formatPeriode(row.periode) }}
            </template>
          </el-table-column>

          <el-table-column label="Date Échéance" width="120" sortable>
            <template #default="{ row }">
              {{ formatDate(row.dateEcheance) }}
            </template>
          </el-table-column>

          <el-table-column label="Mensualité" width="120" sortable>
            <template #default="{ row }">
              <span class="amount">{{ formatCurrency(row.mensualite) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Intérêts" width="100" sortable>
            <template #default="{ row }">
              <span class="interest">{{ formatCurrency(row.interets) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Principal" width="100" sortable>
            <template #default="{ row }">
              <span class="principal">{{ formatCurrency(row.principal) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Montant Payé" width="120" sortable>
            <template #default="{ row }">
              <span class="paid-amount">{{ formatCurrency(row.montantPaye) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Reste à payer" width="120" sortable>
            <template #default="{ row }">
              <span class="remaining-amount" :class="{ 'text-danger': row.resteAPayer > 0 }">
                {{ formatCurrency(row.resteAPayer) }}
              </span>
            </template>
          </el-table-column>

          <el-table-column label="Statut" width="120" sortable>
            <template #default="{ row }">
              <el-tag :type="getStatusColor(row.statut)">
                {{ getStatusLabel(row.statut) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Actions" width="150" fixed="right">
            <template #default="{ row }">
              <el-button 
                type="success" 
                size="small" 
                @click="markAsPaidSingle(row)"
                :disabled="row.statut === 'paye'"
              >
                <el-icon><Check /></el-icon>
              </el-button>
              <el-button type="info" size="small" @click="viewDetails(row)">
                <el-icon><View /></el-icon>
              </el-button>
              <el-button type="warning" size="small" @click="editEcheancier(row)">
                <el-icon><Edit /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- Pagination -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="filteredEcheanciers.length"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <!-- Modal de détails -->
    <el-dialog v-model="showDetailsModal" title="Détails de l'Échéance" width="600px">
      <div v-if="selectedEcheancier" class="echeancier-details">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Personnel">
            {{ selectedEcheancier.personnelNom }} ({{ selectedEcheancier.matricule }})
          </el-descriptions-item>
          <el-descriptions-item label="Type Prêt">
            {{ selectedEcheancier.typePret }}
          </el-descriptions-item>
          <el-descriptions-item label="Échéance N°">
            {{ selectedEcheancier.numeroEcheance }}/{{ selectedEcheancier.totalEcheances }}
          </el-descriptions-item>
          <el-descriptions-item label="Période">
            {{ formatPeriode(selectedEcheancier.periode) }}
          </el-descriptions-item>
          <el-descriptions-item label="Date Échéance">
            {{ formatDate(selectedEcheancier.dateEcheance) }}
          </el-descriptions-item>
          <el-descriptions-item label="Statut">
            <el-tag :type="getStatusColor(selectedEcheancier.statut)">
              {{ getStatusLabel(selectedEcheancier.statut) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Mensualité">
            <span class="amount">{{ formatCurrency(selectedEcheancier.mensualite) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Intérêts">
            <span class="interest">{{ formatCurrency(selectedEcheancier.interets) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Principal">
            <span class="principal">{{ formatCurrency(selectedEcheancier.principal) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Montant Payé">
            <span class="paid-amount">{{ formatCurrency(selectedEcheancier.montantPaye) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Reste à payer">
            <span class="remaining-amount text-danger">{{ formatCurrency(selectedEcheancier.resteAPayer) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  List, Refresh, Search, Check, Download, View, Edit 
} from '@element-plus/icons-vue'

interface Echeancier {
  id: number
  pretId: number
  personnelId: number
  personnelNom: string
  matricule: string
  typePret: string
  montantPret: number
  numeroEcheance: number
  totalEcheances: number
  periode: string
  dateEcheance: string
  mensualite: number
  interets: number
  principal: number
  montantPaye: number
  resteAPayer: number
  statut: 'en_attente' | 'paye' | 'en_retard' | 'partiellement_paye'
  datePaiement?: string
}

interface Personnel {
  id: number
  matricule: string
  nomComplet: string
}

// Données réactives
const loading = ref(false)
const showDetailsModal = ref(false)
const selectedEcheancier = ref<Echeancier | null>(null)
const selectedEcheanciers = ref<Echeancier[]>([])
const currentPage = ref(1)
const pageSize = ref(20)

// Filtres
const filters = reactive({
  personnelId: null as number | null,
  statut: '',
  periode: ''
})

// Données mockées
const echeanciers = ref<Echeancier[]>([
  {
    id: 1,
    pretId: 1,
    personnelId: 1,
    personnelNom: 'Kouadio Jean',
    matricule: 'EMP001',
    typePret: 'Prêt Personnel',
    montantPret: 500000,
    numeroEcheance: 1,
    totalEcheances: 12,
    periode: '2024-02',
    dateEcheance: '2024-02-28',
    mensualite: 42800,
    interets: 2083,
    principal: 40717,
    montantPaye: 0,
    resteAPayer: 42800,
    statut: 'en_attente'
  },
  {
    id: 2,
    pretId: 1,
    personnelId: 1,
    personnelNom: 'Kouadio Jean',
    matricule: 'EMP001',
    typePret: 'Prêt Personnel',
    montantPret: 500000,
    numeroEcheance: 2,
    totalEcheances: 12,
    periode: '2024-03',
    dateEcheance: '2024-03-31',
    mensualite: 42800,
    interets: 1920,
    principal: 40880,
    montantPaye: 42800,
    resteAPayer: 0,
    statut: 'paye',
    datePaiement: '2024-03-15'
  },
  {
    id: 3,
    pretId: 2,
    personnelId: 2,
    personnelNom: 'Touré Aminata',
    matricule: 'EMP002',
    typePret: 'Prêt Logement',
    montantPret: 2000000,
    numeroEcheance: 6,
    totalEcheances: 36,
    periode: '2024-02',
    dateEcheance: '2024-02-28',
    mensualite: 59400,
    interets: 7500,
    principal: 51900,
    montantPaye: 30000,
    resteAPayer: 29400,
    statut: 'partiellement_paye'
  },
  {
    id: 4,
    pretId: 2,
    personnelId: 2,
    personnelNom: 'Touré Aminata',
    matricule: 'EMP002',
    typePret: 'Prêt Logement',
    montantPret: 2000000,
    numeroEcheance: 7,
    totalEcheances: 36,
    periode: '2024-01',
    dateEcheance: '2024-01-31',
    mensualite: 59400,
    interets: 7560,
    principal: 51840,
    montantPaye: 0,
    resteAPayer: 59400,
    statut: 'en_retard'
  }
])

const personnels = ref<Personnel[]>([
  { id: 1, nomComplet: 'Kouadio Jean', matricule: 'EMP001' },
  { id: 2, nomComplet: 'Touré Aminata', matricule: 'EMP002' },
  { id: 3, nomComplet: 'Soro Mohamed', matricule: 'EMP003' },
  { id: 4, nomComplet: 'Koné Fatoumata', matricule: 'EMP004' },
  { id: 5, nomComplet: 'Bamba Yves', matricule: 'EMP005' }
])

// Computed properties
const filteredEcheanciers = computed(() => {
  let filtered = echeanciers.value

  // Filtrage par personnel
  if (filters.personnelId) {
    filtered = filtered.filter(e => e.personnelId === filters.personnelId)
  }

  // Filtrage par statut
  if (filters.statut) {
    filtered = filtered.filter(e => e.statut === filters.statut)
  }

  // Filtrage par période
  if (filters.periode) {
    filtered = filtered.filter(e => e.periode === filters.periode)
  }

  return filtered
})

// Méthodes
const applyFilters = () => {
  currentPage.value = 1
  ElMessage.success('Filtres appliqués')
}

const resetFilters = () => {
  Object.assign(filters, {
    personnelId: null,
    statut: '',
    periode: ''
  })
  currentPage.value = 1
  ElMessage.info('Filtres réinitialisés')
}

const generateEcheanciers = async () => {
  loading.value = true
  try {
    // Simulation de génération d'échéanciers
    await new Promise(resolve => setTimeout(resolve, 2000))
    ElMessage.success('Échéanciers générés avec succès')
  } catch (error) {
    ElMessage.error('Erreur lors de la génération des échéanciers')
  } finally {
    loading.value = false
  }
}

const handleSelectionChange = (selection: Echeancier[]) => {
  selectedEcheanciers.value = selection
}

const markAsPaid = async () => {
  if (selectedEcheanciers.value.length === 0) {
    ElMessage.warning('Veuillez sélectionner des échéances')
    return
  }

  try {
    await ElMessageBox.confirm(
      `Marquer ${selectedEcheanciers.value.length} échéance(s) comme payée(s) ?`,
      'Confirmation',
      {
        confirmButtonText: 'Confirmer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )

    selectedEcheanciers.value.forEach(echeancier => {
      echeancier.statut = 'paye'
      echeancier.montantPaye = echeancier.mensualite
      echeancier.resteAPayer = 0
      echeancier.datePaiement = new Date().toISOString().split('T')[0]
    })

    ElMessage.success('Échéances marquées comme payées')
    selectedEcheanciers.value = []
  } catch {
    // L'utilisateur a annulé
  }
}

const markAsPaidSingle = async (echeancier: Echeancier) => {
  try {
    await ElMessageBox.confirm(
      `Marquer l'échéance N°${echeancier.numeroEcheance} comme payée ?`,
      'Confirmation',
      {
        confirmButtonText: 'Confirmer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )

    echeancier.statut = 'paye'
    echeancier.montantPaye = echeancier.mensualite
    echeancier.resteAPayer = 0
    echeancier.datePaiement = new Date().toISOString().split('T')[0]

    ElMessage.success('Échéance marquée comme payée')
  } catch {
    // L'utilisateur a annulé
  }
}

const viewDetails = (echeancier: Echeancier) => {
  selectedEcheancier.value = echeancier
  showDetailsModal.value = true
}

const editEcheancier = (echeancier: Echeancier) => {
  ElMessage.info(`Modification de l'échéance N°${echeancier.numeroEcheance}`)
}

const exportEcheanciers = () => {
  ElMessage.success('Export des échéanciers en cours...')
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
}

// Fonctions utilitaires
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF' }).format(amount)
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('fr-FR')
}

const formatPeriode = (periode: string) => {
  const [year, month] = periode.split('-')
  const monthNames = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc']
  return `${monthNames[parseInt(month) - 1]} ${year}`
}

const getStatusColor = (status: string) => {
  const colors: Record<string, string> = {
    en_attente: 'info',
    paye: 'success',
    en_retard: 'danger',
    partiellement_paye: 'warning'
  }
  return colors[status] || ''
}

const getStatusLabel = (status: string) => {
  const labels: Record<string, string> = {
    en_attente: 'En attente',
    paye: 'Payé',
    en_retard: 'En retard',
    partiellement_paye: 'Partiellement payé'
  }
  return labels[status] || status
}

onMounted(() => {
  // Charger les données initiales
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
})
</script>

<style scoped>
.echeanciers-view {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header-left h1 {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-subtitle {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.filters-section {
  margin-bottom: 24px;
}

.filters-content {
  padding: 20px;
}

.table-section {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.table-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.table-controls {
  display: flex;
  gap: 12px;
}

.table-container {
  padding: 20px;
}

.personnel-info {
  display: flex;
  flex-direction: column;
}

.personnel-name {
  font-weight: 600;
  color: #303133;
}

.personnel-matricule {
  font-size: 12px;
  color: #909399;
}

.loan-info {
  display: flex;
  flex-direction: column;
}

.loan-type {
  font-weight: 600;
  color: #303133;
}

.loan-amount {
  font-size: 12px;
  color: #909399;
}

.amount {
  font-weight: 600;
  color: #67c23a;
}

.interest {
  font-weight: 600;
  color: #e6a23c;
}

.principal {
  font-weight: 600;
  color: #409eff;
}

.paid-amount {
  font-weight: 600;
  color: #67c23a;
}

.remaining-amount {
  font-weight: 600;
  color: #f56c6c;
}

.text-danger {
  color: #f56c6c !important;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

.echeancier-details {
  margin-top: 16px;
}

.enhanced-button {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.enhanced-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.enhanced-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}
</style>
