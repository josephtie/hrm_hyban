<template>
  <div class="historique-bulletins-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><FolderOpened /></el-icon>
            Historique des Bulletins
          </h1>
          <p class="page-subtitle">Consultation et export des bulletins de paie</p>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="showNewModal = true" class="enhanced-button">
            <el-icon><Plus /></el-icon>
            Nouveau
          </el-button>
          <el-button type="success" @click="exportAllData" class="enhanced-button">
            <el-icon><Download /></el-icon>
            Exporter Tout
          </el-button>
        </div>
      </div>
    </div>

    <!-- Filtres et recherche -->
    <div class="filters-section enhanced-card">
      <div class="filters-content">
        <el-form :model="filters" inline>
          <el-form-item label="Période">
            <el-select v-model="filters.period" placeholder="Sélectionner la période" style="width: 200px" clearable>
              <el-option
                v-for="period in periods"
                :key="period.value"
                :label="period.label"
                :value="period.value"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="Recherche">
            <el-input
              v-model="filters.search"
              placeholder="Zone de recherche"
              prefix-icon="Search"
              style="width: 250px"
              clearable
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="info" @click="searchBulletins">
              <el-icon><Search /></el-icon>
              Rechercher
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- Tableau principal -->
    <div class="main-table-section enhanced-card">
      <div class="table-header">
        <h3>Liste des Bulletins Historiques</h3>
        <div class="table-stats">
          <el-tag>{{ filteredBulletins.length }} bulletin(s)</el-tag>
          <el-tag type="success">{{ totalNet.toLocaleString() }} XOF Total Net</el-tag>
        </div>
      </div>
      
      <div class="table-container">
        <el-table 
          :data="paginatedBulletins" 
          stripe 
          v-loading="loading"
          @sort-change="handleSortChange"
          @selection-change="handleSelectionChange"
          max-height="600"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column label="Matricule" prop="matricule" width="120" sortable="custom" />
          
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

          <el-table-column label="Statut" prop="statut" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusColor(row.statut)" size="small">
                {{ getStatusLabel(row.statut) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Nb Parts" prop="nombrePart" width="100" align="right" sortable="custom" />

          <el-table-column label="Ancienneté" prop="anciennete" width="120" align="center" sortable="custom" />

          <el-table-column label="Salaire Base" prop="salaireBase" width="120" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.salaireBase) }}
            </template>
          </el-table-column>

          <el-table-column label="Sursalaire" prop="sursalaire" width="120" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.sursalaire) }}
            </template>
          </el-table-column>

          <el-table-column label="Prime Ancienneté" prop="primeAnciennete" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.primeAnciennete) }}
            </template>
          </el-table-column>

          <el-table-column label="Indem Logement" prop="indemniteLogement" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.indemniteLogement) }}
            </template>
          </el-table-column>

          <el-table-column label="Indem Transport Imp." prop="indemniteTransportImp" width="160" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.indemniteTransportImp) }}
            </template>
          </el-table-column>

          <el-table-column label="Indem Imp." prop="autreIndemImposable" width="120" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.autreIndemImposable) }}
            </template>
          </el-table-column>

          <!-- Colonnes dynamiques pour primes imposables -->
          <el-table-column 
            v-for="prime in primesImposables" 
            :key="prime.id"
            :label="prime.libelle" 
            :prop="`prime${prime.id}`" 
            width="140" 
            align="right"
          >
            <template #default="{ row }">
              {{ formatCurrency(row[`prime${prime.id}`] || 0) }}
            </template>
          </el-table-column>

          <!-- Colonnes dynamiques pour primes imposables et non -->
          <el-table-column 
            v-for="prime in primesImposablesNon" 
            :key="prime.id"
            :label="prime.libelle" 
            :prop="`primeI${prime.id}`" 
            width="140" 
            align="right"
          >
            <template #default="{ row }">
              {{ formatCurrency(row[`primeI${prime.id}`] || 0) }}
            </template>
          </el-table-column>

          <el-table-column label="Brut Imposable" prop="brutImposable" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.brutImposable) }}
            </template>
          </el-table-column>

          <el-table-column label="Indem Repres." prop="indemniteRepresentation" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.indemniteRepresentation) }}
            </template>
          </el-table-column>

          <el-table-column label="Indem Transp" prop="indemniteTransport" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.indemniteTransport) }}
            </template>
          </el-table-column>

          <!-- Colonnes dynamiques pour primes non imposables -->
          <el-table-column 
            v-for="prime in primesNonImposables" 
            :key="prime.id"
            :label="prime.libelle" 
            :prop="`prime${prime.id}`" 
            width="140" 
            align="right"
          >
            <template #default="{ row }">
              {{ formatCurrency(row[`prime${prime.id}`] || 0) }}
            </template>
          </el-table-column>

          <!-- Colonnes dynamiques pour primes imposables et non (autre vue) -->
          <el-table-column 
            v-for="prime in primesImposablesNon" 
            :key="`rubriq${prime.id}`"
            :label="prime.libelle" 
            :prop="`rubriq${prime.id}`" 
            width="140" 
            align="right"
          >
            <template #default="{ row }">
              {{ formatCurrency(row[`rubriq${prime.id}`] || 0) }}
            </template>
          </el-table-column>

          <el-table-column label="Brut Non Imp." prop="brutNonImposable" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.brutNonImposable) }}
            </template>
          </el-table-column>

          <el-table-column label="ITS" prop="its" width="100" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.its) }}
            </template>
          </el-table-column>

          <el-table-column label="CN" prop="cn" width="100" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.cn) }}
            </template>
          </el-table-column>

          <el-table-column label="IGR" prop="igr" width="100" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.igr) }}
            </template>
          </el-table-column>

          <el-table-column label="Retenue Fiscale" prop="retenueFiscale" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.retenueFiscale) }}
            </template>
          </el-table-column>

          <el-table-column label="Base CNPS" prop="baseCnps" width="120" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.baseCnps) }}
            </template>
          </el-table-column>

          <el-table-column label="CNPS" prop="cnps" width="100" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.cnps) }}
            </template>
          </el-table-column>

          <el-table-column label="Avance & Acompte" prop="avanceAcompte" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.avanceAcompte) }}
            </template>
          </el-table-column>

          <el-table-column label="Prêt" prop="pret" width="100" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.pret) }}
            </template>
          </el-table-column>

          <!-- Colonnes dynamiques pour primes mutuelles -->
          <el-table-column 
            v-for="prime in primesMutuelles" 
            :key="prime.id"
            :label="prime.libelle" 
            :prop="`primeM${prime.id}`" 
            width="140" 
            align="right"
          >
            <template #default="{ row }">
              {{ formatCurrency(row[`primeM${prime.id}`] || 0) }}
            </template>
          </el-table-column>

          <el-table-column label="Total Retenue" prop="totalRetenue" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.totalRetenue) }}
            </template>
          </el-table-column>

          <!-- Colonnes dynamiques pour primes gains -->
          <el-table-column 
            v-for="prime in primesGains" 
            :key="prime.id"
            :label="prime.libelle" 
            :prop="`primeG${prime.id}`" 
            width="140" 
            align="right"
          >
            <template #default="{ row }">
              {{ formatCurrency(row[`primeG${prime.id}`] || 0) }}
            </template>
          </el-table-column>

          <el-table-column label="Net à Payer" prop="netAPayer" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              <span class="net-amount">{{ formatCurrency(row.netAPayer) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Total Brut" prop="totalBrut" width="120" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.totalBrut) }}
            </template>
          </el-table-column>

          <el-table-column label="IS" prop="is" width="100" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.is) }}
            </template>
          </el-table-column>

          <el-table-column label="TA" prop="ta" width="100" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.ta) }}
            </template>
          </el-table-column>

          <el-table-column label="FPC" prop="fpc" width="100" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.fpc) }}
            </template>
          </el-table-column>

          <el-table-column label="Prest Familiale" prop="prestationFamiliale" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.prestationFamiliale) }}
            </template>
          </el-table-column>

          <el-table-column label="Acc Travail" prop="accidentTravail" width="120" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.accidentTravail) }}
            </template>
          </el-table-column>

          <el-table-column label="Retraite" prop="retraite" width="120" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.retraite) }}
            </template>
          </el-table-column>

          <el-table-column label="Total Patronal" prop="totalPatronal" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.totalPatronal) }}
            </template>
          </el-table-column>

          <el-table-column label="Total Masse Salariale" prop="totalMasseSalariale" width="180" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.totalMasseSalariale) }}
            </template>
          </el-table-column>

          <el-table-column label="Actions" width="120" fixed="right">
            <template #default="{ row }">
              <el-button-group>
                <el-button type="primary" size="small" @click="viewBulletin(row)">
                  <el-icon><View /></el-icon>
                </el-button>
                <el-button type="success" size="small" @click="printBulletin(row)">
                  <el-icon><Printer /></el-icon>
                </el-button>
                <el-dropdown trigger="click">
                  <el-button type="info" size="small">
                    <el-icon><MoreFilled /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="exportBulletin(row)">
                        <el-icon><Download /></el-icon>
                        Exporter
                      </el-dropdown-item>
                      <el-dropdown-item divided @click="deleteBulletin(row)">
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
            :page-sizes="[20, 50, 100, 200, 500, 2000]"
            :total="filteredBulletins.length"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <!-- Modal Nouveau -->
    <el-dialog v-model="showNewModal" title="Nouveau Bulletin" width="600px">
      <div class="new-modal-content">
        <el-form :model="newForm" label-width="140px">
          <el-form-item label="Période">
            <el-select v-model="newForm.period" placeholder="Sélectionner" style="width: 100%">
              <el-option
                v-for="period in periods"
                :key="period.value"
                :label="period.label"
                :value="period.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="Personnel">
            <el-select v-model="newForm.personnelId" placeholder="Sélectionner" style="width: 100%">
              <el-option
                v-for="personnel in personnels"
                :key="personnel.id"
                :label="personnel.nomComplet"
                :value="personnel.id"
              />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="showNewModal = false">Annuler</el-button>
        <el-button type="primary" @click="createBulletin">Créer</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  FolderOpened, Plus, Download, Search, View, Printer, 
  MoreFilled, Delete
} from '@element-plus/icons-vue'

interface Bulletin {
  id: number
  matricule: string
  nomComplet: string
  statut: string
  nombrePart: number
  anciennete: string
  salaireBase: number
  sursalaire: number
  primeAnciennete: number
  indemniteLogement: number
  brutImposable: number
  its: number
  cn: number
  igr: number
  retenueFiscale: number
  baseCnps: number
  cnps: number
  avanceAcompte: number
  pret: number
  totalRetenue: number
  netAPayer: number
  totalBrut: number
  is: number
  ta: number
  fpc: number
  prestationFamiliale: number
  accidentTravail: number
  retraite: number
  totalPatronal: number
  totalMasseSalariale: number
  indemniteTransportImp: number
  autreIndemImposable: number
  indemniteRepresentation: number
  indemniteTransport: number
  brutNonImposable: number
  [key: string]: any // Pour les colonnes dynamiques
}

interface Prime {
  id: number
  libelle: string
}

interface Personnel {
  id: number
  nomComplet: string
}

// Données réactives
const loading = ref(false)
const showNewModal = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const sortBy = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')
const selectedBulletins = ref<Bulletin[]>([])

const filters = reactive({
  period: '',
  search: ''
})

const newForm = reactive({
  period: '',
  personnelId: 0
})

// Données mockées
const bulletinsData = ref<Bulletin[]>([
  {
    id: 1,
    matricule: 'EMP001',
    nomComplet: 'Kouadio Jean',
    statut: 'actif',
    nombrePart: 2,
    anciennete: '5 ans',
    salaireBase: 500000,
    sursalaire: 50000,
    primeAnciennete: 25000,
    indemniteLogement: 30000,
    brutImposable: 575000,
    its: 57500,
    cn: 4500,
    igr: 86000,
    retenueFiscale: 86000,
    baseCnps: 550000,
    cnps: 45000,
    avanceAcompte: 0,
    pret: 10000,
    totalRetenue: 188500,
    netAPayer: 425000,
    totalBrut: 605000,
    is: 5000,
    ta: 2000,
    fpc: 1500,
    prestationFamiliale: 8000,
    accidentTravail: 3000,
    retraite: 55000,
    totalPatronal: 182500,
    totalMasseSalariale: 787500,
    indemniteTransportImp: 15000,
    autreIndemImposable: 5000,
    indemniteRepresentation: 10000,
    indemniteTransport: 20000,
    brutNonImposable: 35000
  },
  {
    id: 2,
    matricule: 'EMP002',
    nomComplet: 'Touré Aminata',
    statut: 'actif',
    nombrePart: 1,
    anciennete: '3 ans',
    salaireBase: 350000,
    sursalaire: 0,
    primeAnciennete: 15000,
    indemniteLogement: 25000,
    brutImposable: 365000,
    its: 36500,
    cn: 3150,
    igr: 55000,
    retenueFiscale: 55000,
    baseCnps: 385000,
    cnps: 31500,
    avanceAcompte: 5000,
    pret: 0,
    totalRetenue: 145000,
    netAPayer: 245000,
    totalBrut: 390000,
    is: 3000,
    ta: 1200,
    fpc: 900,
    prestationFamiliale: 4000,
    accidentTravail: 2000,
    retraite: 38500,
    totalPatronal: 127500,
    totalMasseSalariale: 517500,
    indemniteTransportImp: 12000,
    autreIndemImposable: 3000,
    indemniteRepresentation: 8000,
    indemniteTransport: 15000,
    brutNonImposable: 23000
  }
])

const primesImposables = ref<Prime[]>([
  { id: 1, libelle: 'Prime de risque' },
  { id: 2, libelle: 'Prime de panier' }
])

const primesImposablesNon = ref<Prime[]>([
  { id: 3, libelle: 'Prime de transport' },
  { id: 4, libelle: 'Indemnité de fonction' }
])

const primesNonImposables = ref<Prime[]>([
  { id: 5, libelle: 'Prime de logement' },
  { id: 6, libelle: 'Prime familiale' }
])

const primesMutuelles = ref<Prime[]>([
  { id: 7, libelle: 'Mutuelle santé' },
  { id: 8, libelle: 'Mutuelle retraite' }
])

const primesGains = ref<Prime[]>([
  { id: 9, libelle: 'Prime exceptionnelle' },
  { id: 10, libelle: 'Bonus performance' }
])

const personnels = ref<Personnel[]>([
  { id: 1, nomComplet: 'Kouadio Jean' },
  { id: 2, nomComplet: 'Touré Aminata' },
  { id: 3, nomComplet: 'Soro Mohamed' }
])

const periods = ref([
  { value: '2024-01', label: 'Janvier 2024' },
  { value: '2024-02', label: 'Février 2024' },
  { value: '2024-03', label: 'Mars 2024' },
  { value: '2024-04', label: 'Avril 2024' }
])

// Computed properties
const filteredBulletins = computed(() => {
  let filtered = bulletinsData.value

  if (filters.period) {
    filtered = filtered.filter(b => b.periode === filters.period)
  }

  if (filters.search) {
    const query = filters.search.toLowerCase()
    filtered = filtered.filter(b => 
      b.nomComplet.toLowerCase().includes(query) ||
      b.matricule.toLowerCase().includes(query)
    )
  }

  return filtered
})

const paginatedBulletins = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredBulletins.value.slice(start, end)
})

const totalNet = computed(() => {
  return filteredBulletins.value.reduce((sum, b) => sum + b.netAPayer, 0)
})

// Méthodes
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF' }).format(amount)
}

const getStatusColor = (status: string) => {
  return status === 'actif' ? 'success' : 'danger'
}

const getStatusLabel = (status: string) => {
  return status === 'actif' ? 'Actif' : 'En sommeil'
}

const handleSelectionChange = (selection: Bulletin[]) => {
  selectedBulletins.value = selection
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

const searchBulletins = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('Recherche effectuée')
  }, 1000)
}

const exportAllData = () => {
  ElMessage.success('Export de tous les bulletins en cours...')
}

const viewBulletin = (bulletin: Bulletin) => {
  ElMessage.info(`Visualisation du bulletin de ${bulletin.nomComplet}`)
}

const printBulletin = (bulletin: Bulletin) => {
  ElMessage.info(`Impression du bulletin de ${bulletin.nomComplet}`)
}

const exportBulletin = (bulletin: Bulletin) => {
  ElMessage.success(`Export du bulletin de ${bulletin.nomComplet}`)
}

const deleteBulletin = async (bulletin: Bulletin) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer le bulletin de ${bulletin.nomComplet} ?`,
      'Confirmation de suppression',
      {
        confirmButtonText: 'Supprimer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )
    
    const index = bulletinsData.value.findIndex(b => b.id === bulletin.id)
    if (index !== -1) {
      bulletinsData.value.splice(index, 1)
      ElMessage.success('Bulletin supprimé')
    }
  } catch {
    // Annulé
  }
}

const createBulletin = () => {
  ElMessage.success('Création du bulletin')
  showNewModal.value = false
}

onMounted(() => {
  searchBulletins()
})
</script>

<style scoped>
.historique-bulletins-view {
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

.filters-section {
  margin-bottom: 24px;
}

.filters-content {
  padding: 20px;
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
</style>
