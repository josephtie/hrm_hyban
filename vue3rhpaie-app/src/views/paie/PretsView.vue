<template>
  <div class="prets-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><CreditCard /></el-icon>
            Gestion des Prêts
          </h1>
          <p class="page-subtitle">Suivi et gestion des prêts du personnel</p>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="showAddModal = true" class="enhanced-button">
            <el-icon><Plus /></el-icon>
            Nouveau Prêt
          </el-button>
        </div>
      </div>
    </div>

    <!-- Layout avec sidebar et contenu principal -->
    <div class="content-layout">
      <!-- Sidebar pour le formulaire -->
      <div class="sidebar-panel enhanced-card" v-if="showAddModal">
        <div class="panel-header">
          <h3>{{ editingLoan ? 'Modifier un Prêt' : 'Ajouter un Prêt' }}</h3>
          <el-button @click="closeModal" circle size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <div class="form-content">
          <el-form :model="loanForm" :rules="loanRules" ref="loanFormRef" label-width="140px" size="large">
            <el-form-item label="Personnel" prop="personnelId" required>
              <el-select v-model="loanForm.personnelId" placeholder="Sélectionner le personnel" style="width: 100%" filterable>
                <el-option
                  v-for="personnel in personnels"
                  :key="personnel.id"
                  :label="`${personnel.nomComplet} - ${personnel.matricule}`"
                  :value="personnel.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Type de Prêt" prop="type" required>
              <el-select v-model="loanForm.type" placeholder="Type de prêt" style="width: 100%">
                <el-option label="Prêt Personnel" value="personnel" />
                <el-option label="Prêt Logement" value="logement" />
                <el-option label="Prêt Véhicule" value="vehicule" />
                <el-option label="Prêt Études" value="etudes" />
                <el-option label="Prêt Urgence" value="urgence" />
                <el-option label="Autre" value="autre" />
              </el-select>
            </el-form-item>

            <el-form-item label="Montant" prop="montant" required>
              <el-input-number 
                v-model="loanForm.montant" 
                :min="0" 
                :max="10000000"
                :step="10000"
                style="width: 100%"
                placeholder="Montant du prêt"
                :formatter="(value: number) => `${value.toLocaleString()} XOF`"
                :parser="(value: string) => value.replace(/[^\d]/g, '')"
              />
            </el-form-item>

            <el-form-item label="Date Contraction" prop="dateContraction" required>
              <el-date-picker
                v-model="loanForm.dateContraction"
                type="date"
                placeholder="Date de contraction"
                style="width: 100%"
                format="DD/MM/YYYY"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>

            <el-form-item label="Période Début Prélèvement" prop="periodeDebut" required>
              <el-select v-model="loanForm.periodeDebut" placeholder="Période de début" style="width: 100%">
                <el-option
                  v-for="periode in periodes"
                  :key="periode.value"
                  :label="periode.label"
                  :value="periode.value"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Durée (mois)" prop="duree" required>
              <el-input-number
                v-model="loanForm.duree"
                :min="1"
                :max="120"
                style="width: 100%"
                placeholder="Durée en mois"
              />
            </el-form-item>

            <el-form-item label="Taux d'Intérêt (%)" prop="tauxInteret">
              <el-input-number
                v-model="loanForm.tauxInteret"
                :min="0"
                :max="20"
                :precision="2"
                :step="0.1"
                style="width: 100%"
                placeholder="Taux d'intérêt annuel"
              />
            </el-form-item>

            <el-form-item label="Mensualité" prop="mensualite">
              <el-input-number
                v-model="loanForm.mensualite"
                :min="0"
                :step="1000"
                style="width: 100%"
                placeholder="Mensualité calculée"
                :disabled="true"
              />
            </el-form-item>

            <el-form-item label="Motif" prop="motif">
              <el-input
                v-model="loanForm.motif"
                type="textarea"
                :rows="3"
                placeholder="Motif du prêt"
              />
            </el-form-item>

            <el-form-item label="Statut" prop="statut">
              <el-radio-group v-model="loanForm.statut">
                <el-radio value="en_cours">En cours</el-radio>
                <el-radio value="termine">Terminé</el-radio>
                <el-radio value="suspendu">Suspendu</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveLoan" :loading="formLoading">
                {{ editingLoan ? 'Mettre à jour' : 'Enregistrer' }}
              </el-button>
              <el-button @click="closeModal">Annuler</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- Contenu principal avec le tableau -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Prêts</h3>
          <div class="panel-controls">
            <el-input
              v-model="searchQuery"
              placeholder="Rechercher un prêt..."
              prefix-icon="Search"
              style="width: 300px"
              clearable
            />
            <el-select v-model="filterStatus" placeholder="Filtrer par statut" style="width: 150px" clearable>
              <el-option label="En cours" value="en_cours" />
              <el-option label="Terminé" value="termine" />
              <el-option label="Suspendu" value="suspendu" />
            </el-select>
          </div>
        </div>

        <div class="table-container">
          <el-table :data="filteredLoans" stripe v-loading="loading" @sort-change="handleSortChange">
            <el-table-column label="Personnel" prop="personnelNom" min-width="200" sortable="custom">
              <template #default="{ row }">
                <div class="personnel-info">
                  <div class="personnel-name">{{ row.personnelNom }}</div>
                  <div class="personnel-matricule">{{ row.matricule }}</div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="Type" prop="type" width="120" sortable="custom">
              <template #default="{ row }">
                <el-tag :type="getTypeColor(row.type)">
                  {{ getTypeLabel(row.type) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column label="Montant" prop="montant" width="120" sortable="custom">
              <template #default="{ row }">
                <span class="amount">{{ formatCurrency(row.montant) }}</span>
              </template>
            </el-table-column>

            <el-table-column label="Date Contraction" prop="dateContraction" width="140" sortable="custom">
              <template #default="{ row }">
                {{ formatDate(row.dateContraction) }}
              </template>
            </el-table-column>

            <el-table-column label="Durée" prop="duree" width="80" sortable="custom">
              <template #default="{ row }">
                {{ row.duree }} mois
              </template>
            </el-table-column>

            <el-table-column label="Mensualité" prop="mensualite" width="120" sortable="custom">
              <template #default="{ row }">
                <span class="amount">{{ formatCurrency(row.mensualite) }}</span>
              </template>
            </el-table-column>

            <el-table-column label="Reste à payer" prop="resteAPayer" width="130" sortable="custom">
              <template #default="{ row }">
                <span class="amount" :class="{ 'text-danger': row.resteAPayer > 0 }">
                  {{ formatCurrency(row.resteAPayer) }}
                </span>
              </template>
            </el-table-column>

            <el-table-column label="Statut" prop="statut" width="100" sortable="custom">
              <template #default="{ row }">
                <el-tag :type="getStatusColor(row.statut)">
                  {{ getStatusLabel(row.statut) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column label="Actions" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="editLoan(row)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button type="info" size="small" @click="viewLoanDetails(row)">
                  <el-icon><View /></el-icon>
                </el-button>
                <el-button type="danger" size="small" @click="deleteLoan(row)">
                  <el-icon><Delete /></el-icon>
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
              :total="filteredLoans.length"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de détails -->
    <el-dialog v-model="showDetailsModal" title="Détails du Prêt" width="600px">
      <div v-if="selectedLoan" class="loan-details">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Personnel">
            {{ selectedLoan.personnelNom }} ({{ selectedLoan.matricule }})
          </el-descriptions-item>
          <el-descriptions-item label="Type">
            <el-tag :type="getTypeColor(selectedLoan.type)">
              {{ getTypeLabel(selectedLoan.type) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Montant">
            <span class="amount">{{ formatCurrency(selectedLoan.montant) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Date Contraction">
            {{ formatDate(selectedLoan.dateContraction) }}
          </el-descriptions-item>
          <el-descriptions-item label="Durée">
            {{ selectedLoan.duree }} mois
          </el-descriptions-item>
          <el-descriptions-item label="Taux Intérêt">
            {{ selectedLoan.tauxInteret }}%
          </el-descriptions-item>
          <el-descriptions-item label="Mensualité">
            <span class="amount">{{ formatCurrency(selectedLoan.mensualite) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Reste à payer">
            <span class="amount text-danger">{{ formatCurrency(selectedLoan.resteAPayer) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Statut" span="2">
            <el-tag :type="getStatusColor(selectedLoan.statut)">
              {{ getStatusLabel(selectedLoan.statut) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Motif" span="2" v-if="selectedLoan.motif">
            {{ selectedLoan.motif }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  CreditCard, Plus, Close, Edit, View, Delete, Search 
} from '@element-plus/icons-vue'

interface Loan {
  id: number
  personnelId: number
  personnelNom: string
  matricule: string
  type: string
  montant: number
  dateContraction: string
  periodeDebut: string
  duree: number
  tauxInteret: number
  mensualite: number
  resteAPayer: number
  motif: string
  statut: string
}

interface Personnel {
  id: number
  matricule: string
  nomComplet: string
}

// Données réactives
const loading = ref(false)
const formLoading = ref(false)
const showAddModal = ref(false)
const showDetailsModal = ref(false)
const editingLoan = ref(false)
const selectedLoan = ref<Loan | null>(null)
const searchQuery = ref('')
const filterStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const sortBy = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')

// Formulaire de prêt
const loanForm = reactive({
  id: 0,
  personnelId: 0,
  type: '',
  montant: 0,
  dateContraction: '',
  periodeDebut: '',
  duree: 12,
  tauxInteret: 5,
  mensualite: 0,
  motif: '',
  statut: 'en_cours'
})

// Règles de validation
const loanRules = {
  personnelId: [{ required: true, message: 'Veuillez sélectionner le personnel', trigger: 'change' }],
  type: [{ required: true, message: 'Veuillez sélectionner le type de prêt', trigger: 'change' }],
  montant: [{ required: true, message: 'Veuillez saisir le montant', trigger: 'blur' }],
  dateContraction: [{ required: true, message: 'Veuillez sélectionner la date', trigger: 'change' }],
  periodeDebut: [{ required: true, message: 'Veuillez sélectionner la période', trigger: 'change' }],
  duree: [{ required: true, message: 'Veuillez saisir la durée', trigger: 'blur' }]
}

const loanFormRef = ref()

// Données mockées
const loans = ref<Loan[]>([
  {
    id: 1,
    personnelId: 1,
    personnelNom: 'Kouadio Jean',
    matricule: 'EMP001',
    type: 'personnel',
    montant: 500000,
    dateContraction: '2024-01-15',
    periodeDebut: '2024-02',
    duree: 12,
    tauxInteret: 5,
    mensualite: 42800,
    resteAPayer: 342400,
    motif: 'Prêt pour frais de scolarité',
    statut: 'en_cours'
  },
  {
    id: 2,
    personnelId: 2,
    personnelNom: 'Touré Aminata',
    matricule: 'EMP002',
    type: 'logement',
    montant: 2000000,
    dateContraction: '2023-06-01',
    periodeDebut: '2023-07',
    duree: 36,
    tauxInteret: 4.5,
    mensualite: 59400,
    resteAPayer: 1188000,
    motif: 'Aide au logement',
    statut: 'en_cours'
  },
  {
    id: 3,
    personnelId: 3,
    personnelNom: 'Soro Mohamed',
    matricule: 'EMP003',
    type: 'vehicule',
    montant: 1500000,
    dateContraction: '2023-03-10',
    periodeDebut: '2023-04',
    duree: 24,
    tauxInteret: 6,
    mensualite: 66400,
    resteAPayer: 0,
    motif: 'Achat véhicule',
    statut: 'termine'
  }
])

const personnels = ref<Personnel[]>([
  { id: 1, nomComplet: 'Kouadio Jean', matricule: 'EMP001' },
  { id: 2, nomComplet: 'Touré Aminata', matricule: 'EMP002' },
  { id: 3, nomComplet: 'Soro Mohamed', matricule: 'EMP003' },
  { id: 4, nomComplet: 'Koné Fatoumata', matricule: 'EMP004' },
  { id: 5, nomComplet: 'Bamba Yves', matricule: 'EMP005' }
])

const periodes = ref([
  { value: '2024-01', label: 'Janvier 2024' },
  { value: '2024-02', label: 'Février 2024' },
  { value: '2024-03', label: 'Mars 2024' },
  { value: '2024-04', label: 'Avril 2024' },
  { value: '2024-05', label: 'Mai 2024' },
  { value: '2024-06', label: 'Juin 2024' },
  { value: '2024-07', label: 'Juillet 2024' },
  { value: '2024-08', label: 'Août 2024' },
  { value: '2024-09', label: 'Septembre 2024' },
  { value: '2024-10', label: 'Octobre 2024' },
  { value: '2024-11', label: 'Novembre 2024' },
  { value: '2024-12', label: 'Décembre 2024' }
])

// Computed properties
const filteredLoans = computed(() => {
  let filtered = loans.value

  // Filtrage par recherche
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(loan => 
      loan.personnelNom.toLowerCase().includes(query) ||
      loan.matricule.toLowerCase().includes(query) ||
      loan.type.toLowerCase().includes(query)
    )
  }

  // Filtrage par statut
  if (filterStatus.value) {
    filtered = filtered.filter(loan => loan.statut === filterStatus.value)
  }

  // Tri
  if (sortBy.value) {
    filtered.sort((a, b) => {
      const aVal = a[sortBy.value as keyof Loan]
      const bVal = b[sortBy.value as keyof Loan]
      
      if (sortOrder.value === 'asc') {
        return aVal > bVal ? 1 : -1
      } else {
        return aVal < bVal ? 1 : -1
      }
    })
  }

  return filtered
})

// Watchers
watch(() => [loanForm.montant, loanForm.duree, loanForm.tauxInteret], () => {
  calculateMensualite()
}, { deep: true })

// Méthodes
const calculateMensualite = () => {
  if (loanForm.montant && loanForm.duree && loanForm.tauxInteret) {
    const principal = loanForm.montant
    const monthlyRate = loanForm.tauxInteret / 100 / 12
    const months = loanForm.duree
    
    if (monthlyRate === 0) {
      loanForm.mensualite = principal / months
    } else {
      loanForm.mensualite = principal * (monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1)
    }
  }
}

const closeModal = () => {
  showAddModal.value = false
  editingLoan.value = false
  resetForm()
}

const resetForm = () => {
  Object.assign(loanForm, {
    id: 0,
    personnelId: 0,
    type: '',
    montant: 0,
    dateContraction: '',
    periodeDebut: '',
    duree: 12,
    tauxInteret: 5,
    mensualite: 0,
    motif: '',
    statut: 'en_cours'
  })
  loanFormRef.value?.resetFields()
}

const saveLoan = async () => {
  if (!loanFormRef.value) return
  
  try {
    await loanFormRef.value.validate()
    formLoading.value = true

    // Simulation d'un appel API
    await new Promise(resolve => setTimeout(resolve, 1000))

    const personnel = personnels.value.find(p => p.id === loanForm.personnelId)
    
    if (editingLoan.value) {
      // Modification
      const index = loans.value.findIndex(l => l.id === loanForm.id)
      if (index !== -1) {
        loans.value[index] = {
          ...loanForm,
          personnelNom: personnel?.nomComplet || '',
          matricule: personnel?.matricule || '',
          resteAPayer: loanForm.mensualite * loanForm.duree
        }
        ElMessage.success('Prêt modifié avec succès')
      }
    } else {
      // Ajout
      const newLoan: Loan = {
        ...loanForm,
        id: Date.now(),
        personnelNom: personnel?.nomComplet || '',
        matricule: personnel?.matricule || '',
        resteAPayer: loanForm.mensualite * loanForm.duree
      }
      loans.value.unshift(newLoan)
      ElMessage.success('Prêt ajouté avec succès')
    }

    closeModal()
  } catch (error) {
    console.error('Erreur lors de la sauvegarde:', error)
  } finally {
    formLoading.value = false
  }
}

const editLoan = (loan: Loan) => {
  Object.assign(loanForm, loan)
  editingLoan.value = true
  showAddModal.value = true
}

const deleteLoan = async (loan: Loan) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer le prêt de ${loan.personnelNom} ?`,
      'Confirmation de suppression',
      {
        confirmButtonText: 'Supprimer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )

    const index = loans.value.findIndex(l => l.id === loan.id)
    if (index !== -1) {
      loans.value.splice(index, 1)
      ElMessage.success('Prêt supprimé avec succès')
    }
  } catch {
    // L'utilisateur a annulé
  }
}

const viewLoanDetails = (loan: Loan) => {
  selectedLoan.value = loan
  showDetailsModal.value = true
}

const handleSortChange = ({ prop, order }: { prop: string; order: string | null }) => {
  sortBy.value = prop
  sortOrder.value = order === 'ascending' ? 'asc' : 'desc'
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

const getTypeColor = (type: string) => {
  const colors: Record<string, string> = {
    personnel: 'primary',
    logement: 'success',
    vehicule: 'warning',
    etudes: 'info',
    urgence: 'danger',
    autre: ''
  }
  return colors[type] || ''
}

const getTypeLabel = (type: string) => {
  const labels: Record<string, string> = {
    personnel: 'Personnel',
    logement: 'Logement',
    vehicule: 'Véhicule',
    etudes: 'Études',
    urgence: 'Urgence',
    autre: 'Autre'
  }
  return labels[type] || type
}

const getStatusColor = (status: string) => {
  const colors: Record<string, string> = {
    en_cours: 'success',
    termine: 'info',
    suspendu: 'warning'
  }
  return colors[status] || ''
}

const getStatusLabel = (status: string) => {
  const labels: Record<string, string> = {
    en_cours: 'En cours',
    termine: 'Terminé',
    suspendu: 'Suspendu'
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
.prets-view {
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

.content-layout {
  display: flex;
  gap: 24px;
  height: calc(100vh - 200px);
}

.sidebar-panel {
  width: 500px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.panel-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.form-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.main-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-controls {
  display: flex;
  gap: 12px;
  align-items: center;
}

.table-container {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
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

.amount {
  font-weight: 600;
  color: #67c23a;
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

.loan-details {
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
