<template>
  <div class="depart-cdd-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><Switch /></el-icon>
            Liste des Départs/Démissions
          </h1>
          <p class="page-subtitle">Gestion des départs et soldes de tout compte</p>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="showNewModal = true" class="enhanced-button">
            <el-icon><Plus /></el-icon>
            Nouveau Départ
          </el-button>
          <el-button type="success" @click="exportAllData" class="enhanced-button">
            <el-icon><Download /></el-icon>
            Exporter
          </el-button>
        </div>
      </div>
    </div>

    <!-- Tableau principal -->
    <div class="main-table-section enhanced-card">
      <div class="table-header">
        <h3>Liste des Personnels en Départ</h3>
        <div class="table-stats">
          <el-tag>{{ departPersonnels.length }} départ(s)</el-tag>
          <el-tag type="success">{{ totalNet.toLocaleString() }} XOF Total Net</el-tag>
        </div>
      </div>
      
      <div class="table-container">
        <el-table 
          :data="paginatedPersonnels" 
          stripe 
          v-loading="loading"
          @sort-change="handleSortChange"
          @selection-change="handleSelectionChange"
          max-height="600"
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column label="Matricule" prop="matricule" width="120" sortable="custom">
            <template #default="{ row }">
              <span class="matricule">{{ row.matricule }}</span>
            </template>
          </el-table-column>
          
          <el-table-column label="Nom" prop="nomComplet" min-width="200" sortable="custom">
            <template #default="{ row }">
              <div class="personnel-info">
                <div class="personnel-name">{{ row.nomComplet }}</div>
                <el-tag :type="getStatusColor(row.statut)" size="small">
                  {{ getStatusLabel(row.statut) }}
                </el-tag>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Statut" prop="statut" width="120">
            <template #default="{ row }">
              <el-tag :type="getStatusColor(row.statut)" size="small">
                {{ getStatusLabel(row.statut) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Sexe" prop="sexe" width="80" align="center">
            <template #default="{ row }">
              <el-tag :type="row.sexe === 'M' ? 'primary' : 'warning'" size="small">
                {{ row.sexe }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Né(e) le" prop="dateNaissance" width="120" align="center" sortable="custom">
            <template #default="{ row }">
              {{ formatDate(row.dateNaissance) }}
            </template>
          </el-table-column>

          <el-table-column label="Type Contrat" prop="typeContrat" width="140" sortable="custom">
            <template #default="{ row }">
              <el-tag :type="getContractTypeColor(row.typeContrat)" size="small">
                {{ row.typeContrat }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Calculé" prop="soldeCalcule" width="100" align="center" sortable="custom">
            <template #default="{ row }">
              <el-tag :type="row.soldeCalcule ? 'success' : 'danger'" size="small">
                {{ row.soldeCalcule ? 'Oui' : 'Non' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Sit. Matri" prop="situationMatrimoniale" width="120" align="center" />

          <el-table-column label="Nb Enfants" prop="nbEnfants" width="100" align="right" sortable="custom">
            <template #default="{ row }">
              <span class="number-cell">{{ row.nbEnfants }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Salaire Catégoriel" prop="salaireCategoriel" width="150" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.salaireCategoriel) }}
            </template>
          </el-table-column>

          <el-table-column label="Net à Payer" prop="netAPayer" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              <span class="net-amount">{{ formatCurrency(row.netAPayer) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Actions" width="180" fixed="right">
            <template #default="{ row }">
              <el-button-group>
                <el-button type="primary" size="small" @click="viewPersonnel(row)">
                  <el-icon><View /></el-icon>
                </el-button>
                <el-button 
                  type="success" 
                  size="small" 
                  @click="showSoldeModalFunc(row)"
                  :disabled="row.soldeCalcule"
                >
                  <el-icon><Operation /></el-icon>
                  Solde
                </el-button>
                <el-button type="warning" size="small" @click="generateBulletin(row)">
                  <el-icon><Document /></el-icon>
                  Bulletin
                </el-button>
                <el-dropdown trigger="click">
                  <el-button type="info" size="small">
                    <el-icon><MoreFilled /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="exportPersonnel(row)">
                        <el-icon><Download /></el-icon>
                        Exporter
                      </el-dropdown-item>
                      <el-dropdown-item divided @click="deletePersonnel(row)">
                        <el-icon><Delete /></el-icon>
                        Supprimer
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>

        <!-- Pagination -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100, 200]"
            :total="departPersonnels.length"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <!-- Modal Nouveau Départ -->
    <el-dialog v-model="showNewModal" title="Nouveau Départ" width="600px">
      <div class="new-modal-content">
        <el-form :model="newForm" label-width="140px">
          <el-form-item label="Personnel">
            <el-select v-model="newForm.personnelId" placeholder="Sélectionner" style="width: 100%">
              <el-option
                v-for="personnel in availablePersonnels"
                :key="personnel.id"
                :label="personnel.nomComplet"
                :value="personnel.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="Date de départ">
            <el-date-picker
              v-model="newForm.dateDepart"
              type="date"
              placeholder="Sélectionner"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="Motif">
            <el-input
              v-model="newForm.motif"
              type="textarea"
              :rows="3"
              placeholder="Motif du départ"
            />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="showNewModal = false">Annuler</el-button>
        <el-button type="primary" @click="createDepart">Créer</el-button>
      </template>
    </el-dialog>

    <!-- Modal Solde de Tout Compte -->
    <el-dialog 
      v-model="showSoldeModal" 
      title="Soldes de tous les comptes" 
      width="80%"
      :before-close="handleCloseSoldeModal"
    >
      <div class="solde-modal-content" v-if="selectedPersonnel">
        <!-- Informations Personnel -->
        <div class="personnel-info-section">
          <h3>Personnel</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="Matricule">{{ selectedPersonnel.matricule }}</el-descriptions-item>
            <el-descriptions-item label="N° CNPS">{{ selectedPersonnel.numeroCnps }}</el-descriptions-item>
            <el-descriptions-item label="Nom">{{ selectedPersonnel.nomComplet }}</el-descriptions-item>
            <el-descriptions-item label="Sexe">{{ selectedPersonnel.sexe }}</el-descriptions-item>
            <el-descriptions-item label="Né(e) le">{{ formatDate(selectedPersonnel.dateNaissance) }}</el-descriptions-item>
            <el-descriptions-item label="À">{{ selectedPersonnel.lieuNaissance }}</el-descriptions-item>
            <el-descriptions-item label="Situation matrimoniale">{{ selectedPersonnel.situationMatrimoniale }}</el-descriptions-item>
            <el-descriptions-item label="Téléphone">{{ selectedPersonnel.telephone }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- Formulaire de calcul -->
        <div class="solde-form-section">
          <h3>Calcul de solde de tout compte</h3>
          <el-form :model="soldeForm" label-width="200px">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item label="Nbre de Jours dus de congés" required>
                  <el-input-number
                    v-model="soldeForm.joursCongesDus"
                    :min="0"
                    :max="365"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="Préavis non effectué (jours)">
                  <el-input-number
                    v-model="soldeForm.preavisNonEffectue"
                    :min="0"
                    :max="365"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="Primes dues" required>
                  <el-input-number
                    v-model="soldeForm.primesDues"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="13e mois applicable" required>
                  <el-radio-group v-model="soldeForm.treiziemeMoisApplicable">
                    <el-radio value="OUI">Oui</el-radio>
                    <el-radio value="NON">Non</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item label="Acomptes versés" required>
                  <el-input-number
                    v-model="soldeForm.acomptesVerses"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="Avantages en nature">
                  <el-input-number
                    v-model="soldeForm.avantagesNature"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="Heures supplémentaires" required>
                  <el-input-number
                    v-model="soldeForm.heuresSupplementaires"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="13e mois applicable" required>
                  <el-radio-group v-model="soldeForm.treiziemeMoisApplicable2">
                    <el-radio value="OUI">Oui</el-radio>
                    <el-radio value="NON">Non</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="Observations">
              <el-input
                v-model="soldeForm.observations"
                type="textarea"
                :rows="3"
                placeholder="Observations"
              />
            </el-form-item>
          </el-form>
        </div>

        <!-- Tableau des primes -->
        <div class="primes-section">
          <div class="primes-header">
            <h3>Primes du solde</h3>
            <el-button type="primary" @click="addPrime">
              <el-icon><Plus /></el-icon>
              Nouvelle prime
            </el-button>
          </div>
          
          <el-table :data="soldePrimes" stripe>
            <el-table-column label="Prime" prop="libelle" />
            <el-table-column label="Montant" prop="montant" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.montant) }}
              </template>
            </el-table-column>
            <el-table-column label="Valeur" prop="valeur" />
            <el-table-column label="Montant versé" prop="montantVerse" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.montantVerse) }}
              </template>
            </el-table-column>
            <el-table-column label="Actions" width="120" align="center">
              <template #default="{ row, $index }">
                <el-button type="primary" size="small" @click="editPrime(row, $index)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button type="danger" size="small" @click="deletePrime($index)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="showSoldeModal = false">Annuler</el-button>
        <el-button type="success" @click="calculateSolde">Valider</el-button>
      </template>
    </el-dialog>

    <!-- Modal Confirmation Suppression -->
    <el-dialog v-model="showDeleteModal" title="Confirmation" width="400px">
      <div class="delete-modal-content">
        <div class="delete-icon">
          <el-icon class="danger-icon"><WarningFilled /></el-icon>
        </div>
        <p>Êtes-vous sûr de vouloir supprimer ce personnel ?</p>
        <h4 v-if="selectedPersonnel">{{ selectedPersonnel.nomComplet }}</h4>
      </div>
      <template #footer>
        <el-button @click="showDeleteModal = false">Annuler</el-button>
        <el-button type="success" @click="confirmDelete">Valider</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Switch, Plus, Download, View, Operation, Document, 
  MoreFilled, Delete, Edit, WarningFilled
} from '@element-plus/icons-vue'

interface DepartPersonnel {
  id: number
  matricule: string
  nomComplet: string
  statut: string
  sexe: string
  dateNaissance: string
  lieuNaissance: string
  situationMatrimoniale: string
  telephone: string
  numeroCnps: string
  typeContrat: string
  soldeCalcule: boolean
  nbEnfants: number
  salaireCategoriel: number
  netAPayer: number
}

interface SoldePrime {
  id: number
  libelle: string
  montant: number
  valeur: string
  montantVerse: number
}

// Données réactives
const loading = ref(false)
const showNewModal = ref(false)
const showSoldeModal = ref(false)
const showDeleteModal = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const sortBy = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')
const selectedPersonnel = ref<DepartPersonnel | null>(null)
const selectedPersonnels = ref<DepartPersonnel[]>([])

const newForm = reactive({
  personnelId: 0,
  dateDepart: '',
  motif: ''
})

const soldeForm = reactive({
  joursCongesDus: 30,
  preavisNonEffectue: 0,
  primesDues: 0,
  treiziemeMoisApplicable: 'OUI',
  acomptesVerses: 0,
  avantagesNature: 0,
  heuresSupplementaires: 0,
  treiziemeMoisApplicable2: 'OUI',
  observations: ''
})

const soldePrimes = ref<SoldePrime[]>([
  {
    id: 1,
    libelle: 'Prime de départ',
    montant: 50000,
    valeur: 'Fixe',
    montantVerse: 50000
  }
])

// Données mockées
const departPersonnels = ref<DepartPersonnel[]>([
  {
    id: 1,
    matricule: 'EMP001',
    nomComplet: 'Kouadio Jean',
    statut: 'actif',
    sexe: 'M',
    dateNaissance: '1985-05-15',
    lieuNaissance: 'Abidjan',
    situationMatrimoniale: 'Marié',
    telephone: '07-89-45-12-34',
    numeroCnps: 'CNPS001234',
    typeContrat: 'CDD',
    soldeCalcule: false,
    nbEnfants: 2,
    salaireCategoriel: 500000,
    netAPayer: 425000
  },
  {
    id: 2,
    matricule: 'EMP002',
    nomComplet: 'Touré Aminata',
    statut: 'actif',
    sexe: 'F',
    dateNaissance: '1990-08-22',
    lieuNaissance: 'Yamoussoukro',
    situationMatrimoniale: 'Célibataire',
    telephone: '07-45-78-90-12',
    numeroCnps: 'CNPS005678',
    typeContrat: 'CDI',
    soldeCalcule: true,
    nbEnfants: 1,
    salaireCategoriel: 350000,
    netAPayer: 245000
  }
])

const availablePersonnels = ref([
  { id: 3, nomComplet: 'Soro Mohamed' },
  { id: 4, nomComplet: 'Konan Yasmine' },
  { id: 5, nomComplet: 'Bamba Koffi' }
])

// Computed properties
const paginatedPersonnels = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return departPersonnels.value.slice(start, end)
})

const totalNet = computed(() => {
  return departPersonnels.value.reduce((sum, p) => sum + p.netAPayer, 0)
})

// Méthodes
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF' }).format(amount)
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('fr-FR')
}

const getStatusColor = (status: string) => {
  return status === 'actif' ? 'success' : 'danger'
}

const getStatusLabel = (status: string) => {
  return status === 'actif' ? 'Actif' : 'En sommeil'
}

const getContractTypeColor = (type: string) => {
  return type === 'CDD' ? 'warning' : 'primary'
}

const handleSelectionChange = (selection: DepartPersonnel[]) => {
  selectedPersonnels.value = selection
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

const exportAllData = () => {
  ElMessage.success('Export des départs en cours...')
}

const viewPersonnel = (personnel: DepartPersonnel) => {
  ElMessage.info(`Visualisation de ${personnel.nomComplet}`)
}

const showSoldeModalFunc = (personnel: DepartPersonnel) => {
  selectedPersonnel.value = personnel
  showSoldeModal.value = true
}

const generateBulletin = (personnel: DepartPersonnel) => {
  ElMessage.info(`Génération du bulletin de ${personnel.nomComplet}`)
}

const exportPersonnel = (personnel: DepartPersonnel) => {
  ElMessage.success(`Export de ${personnel.nomComplet}`)
}

const deletePersonnel = (personnel: DepartPersonnel) => {
  selectedPersonnel.value = personnel
  showDeleteModal.value = true
}

const confirmDelete = () => {
  if (selectedPersonnel.value) {
    const index = departPersonnels.value.findIndex(p => p.id === selectedPersonnel.value!.id)
    if (index !== -1) {
      departPersonnels.value.splice(index, 1)
      ElMessage.success('Personnel supprimé')
    }
  }
  showDeleteModal.value = false
}

const createDepart = () => {
  ElMessage.success('Création du départ')
  showNewModal.value = false
}

const calculateSolde = () => {
  if (selectedPersonnel.value) {
    selectedPersonnel.value.soldeCalcule = true
    ElMessage.success('Solde calculé avec succès')
    showSoldeModal.value = false
  }
}

const handleCloseSoldeModal = () => {
  showSoldeModal.value = false
  selectedPersonnel.value = null
}

const addPrime = () => {
  const newPrime: SoldePrime = {
    id: Date.now(),
    libelle: 'Nouvelle prime',
    montant: 0,
    valeur: 'Variable',
    montantVerse: 0
  }
  soldePrimes.value.push(newPrime)
}

const editPrime = (prime: SoldePrime, index: number) => {
  ElMessage.info(`Modification de la prime: ${prime.libelle}`)
}

const deletePrime = (index: number) => {
  soldePrimes.value.splice(index, 1)
  ElMessage.success('Prime supprimée')
}

onMounted(() => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 1000)
})
</script>

<style scoped>
.depart-cdd-view {
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

.header-right {
  display: flex;
  gap: 12px;
}

.main-table-section {
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

.table-stats {
  display: flex;
  gap: 12px;
}

.table-container {
  padding: 20px;
}

.personnel-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.personnel-name {
  font-weight: 600;
  color: #303133;
}

.matricule {
  font-weight: 600;
  color: #667eea;
}

.number-cell {
  font-weight: 600;
  color: #303133;
}

.net-amount {
  font-weight: 700;
  color: #67c23a;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
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

.new-modal-content {
  padding: 20px 0;
}

.solde-modal-content {
  max-height: 70vh;
  overflow-y: auto;
}

.personnel-info-section,
.solde-form-section,
.primes-section {
  margin-bottom: 30px;
}

.personnel-info-section h3,
.solde-form-section h3,
.primes-section h3 {
  margin-bottom: 16px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.primes-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.delete-modal-content {
  text-align: center;
  padding: 20px 0;
}

.delete-icon {
  margin-bottom: 16px;
}

.danger-icon {
  font-size: 48px;
  color: #f56c6c;
}

.delete-modal-content h4 {
  margin: 16px 0 0 0;
  color: #303133;
  font-weight: 600;
}
</style>
