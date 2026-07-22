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
          <el-form-item label="Mois / Période">
            <el-select v-model="filters.period" placeholder="Sélectionner la période" style="width: 220px" clearable @change="searchBulletins">
              <el-option
                v-for="period in periods"
                :key="period.value"
                :label="period.label"
                :value="period.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="Matricule">
            <el-input
              v-model="filters.matricule"
              placeholder="Matricule..."
              style="width: 160px"
              clearable
              @keyup.enter="applyFilters"
              @clear="applyFilters"
            />
          </el-form-item>

          <el-form-item label="Nom">
            <el-input
              v-model="filters.nom"
              placeholder="Nom..."
              style="width: 160px"
              clearable
              @keyup.enter="applyFilters"
              @clear="applyFilters"
            />
          </el-form-item>

          <el-form-item label="Prénom">
            <el-input
              v-model="filters.prenom"
              placeholder="Prénom..."
              style="width: 160px"
              clearable
              @keyup.enter="applyFilters"
              @clear="applyFilters"
            />
          </el-form-item>

          <el-form-item label="Recherche">
            <el-input
              v-model="filters.search"
              placeholder="Rechercher (matricule, nom, prénom)..."
              style="width: 260px"
              clearable
              @keyup.enter="applyFilters"
              @clear="applyFilters"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="info" @click="applyFilters">
              <el-icon><Search /></el-icon>
              Rechercher
            </el-button>
            <el-button @click="resetFilters">
              <el-icon><Refresh /></el-icon>
              Réinitialiser
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

          <el-table-column label="Statut trvail" prop="statutTravail" width="120" align="center" />
          <el-table-column label="Nb Jours Trv" prop="nbJoursTravail" width="120" align="center" sortable="custom" />

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

          <!-- Colonnes dynamiques pour primes sociales -->
          <el-table-column
            v-for="prime in primesSociales"
            :key="prime.id"
            :label="prime.libelle"
            :prop="`primeS${prime.id}`"
            width="140"
            align="right"
          >
            <template #default="{ row }">
              {{ formatCurrency(row[`primeS${prime.id}`] || 0) }}
            </template>
          </el-table-column>

          <el-table-column label="Retenue sociale" prop="cnps" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.cnps) }}
            </template>
          </el-table-column>

          <el-table-column label="Avance & Acompte" prop="avceAcpte" width="150" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.avceAcpte) }}
            </template>
          </el-table-column>

          <el-table-column label="Pret" prop="pretAlios" width="120" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.pretAlios) }}
            </template>
          </el-table-column>

          <el-table-column label="Net a reguler" prop="netRegulPayer" width="140" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.netRegulPayer) }}
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

          <el-table-column label="FPC REGUL" prop="fpcregul" width="120" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.fpcregul) }}
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
              <el-button type="primary" size="small" @click="printBulletin(row)">
                <el-icon><Printer /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- Pagination -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[20, 50, 100, 200, 500, 2000]"
            :total="totalBulletins"
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
import { ElLoading, ElMessage } from 'element-plus'
import {
  FolderOpened, Plus, Download, Search, Printer, Refresh
} from '@element-plus/icons-vue'
import { createAuthenticatedApi } from '@/services/api'
import livrePaieService from '@/services/livrepaie.service'

interface Bulletin {
  id: number
  matricule: string
  nom: string
  prenom: string
  nomComplet: string
  statut: string
  statutTravail: string
  nbJoursTravail: number
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
  avceAcpte: number
  pretAlios: number
  regularisation: number
  netRegulPayer: number
  totalRetenue: number
  netAPayer: number
  totalBrut: number
  is: number
  ta: number
  fpc: number
  fpcregul: number
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
const api = createAuthenticatedApi()
const showNewModal = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const sortBy = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')
const selectedBulletins = ref<Bulletin[]>([])
const filters = reactive({
  period: null as number | null,
  search: '',
  matricule: '',
  nom: '',
  prenom: ''
})

const newForm = reactive({
  period: null as number | null,
  personnelId: 0
})

const bulletinsData = ref<Bulletin[]>([])

const primesImposables = ref<Prime[]>([])

const primesImposablesNon = ref<Prime[]>([])

const primesSociales = ref<Prime[]>([])

const primesMutuelles = ref<Prime[]>([])

const primesGains = ref<Prime[]>([])

const personnels = ref<Personnel[]>([
  { id: 1, nomComplet: 'Kouadio Jean' },
  { id: 2, nomComplet: 'Touré Aminata' },
  { id: 3, nomComplet: 'Soro Mohamed' }
])

const periods = ref<Array<{ value: number; label: string }>>([])

const normalizeText = (value: any): string => {
  return String(value ?? '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .trim()
}

// Computed properties
const filteredBulletins = computed(() => {
  let filtered = bulletinsData.value

  if (filters.search) {
    const query = normalizeText(filters.search)
    filtered = filtered.filter(b => {
      const matricule = normalizeText(b.matricule)
      const nomComplet = normalizeText(b.nomComplet)
      return matricule.includes(query) || nomComplet.includes(query)
    })
  }

  if (filters.matricule) {
    const query = normalizeText(filters.matricule)
    filtered = filtered.filter(b => normalizeText(b.matricule).includes(query))
  }

  if (filters.nom) {
    const query = normalizeText(filters.nom)
    filtered = filtered.filter(b => normalizeText(b.nom).includes(query))
  }

  if (filters.prenom) {
    const query = normalizeText(filters.prenom)
    filtered = filtered.filter(b => normalizeText(b.prenom).includes(query))
  }

  return filtered
})

const totalBulletins = computed(() => filteredBulletins.value.length)

const paginatedBulletins = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredBulletins.value.slice(start, start + pageSize.value)
})

const totalNet = computed(() => {
  return filteredBulletins.value.reduce((sum, b) => sum + b.netAPayer, 0)
})

const toNumber = (value: any, fallback = 0): number => {
  const parsed = Number(value ?? fallback)
  return Number.isFinite(parsed) ? parsed : fallback
}

const resolvePersonnel = (row: any) => row?.contratPersonnel?.personnel || row?.personnel || {}

const extractPrimeMeta = (primeRow: any): Prime | null => {
  const id = toNumber(primeRow?.prime?.id ?? primeRow?.rubrique?.id ?? primeRow?.idPrime ?? primeRow?.id)
  if (!id) return null
  return {
    id,
    libelle: String(primeRow?.prime?.libelle ?? primeRow?.rubrique?.libelle ?? primeRow?.libelle ?? `Prime ${id}`)
  }
}

const applyPrimeValues = (target: Bulletin, primeRows: any, prefix: 'prime' | 'primeI' | 'rubriq' | 'primeM' | 'primeG') => {
  if (!Array.isArray(primeRows)) return
  for (const primeRow of primeRows) {
    const meta = extractPrimeMeta(primeRow)
    if (!meta) continue
    target[`${prefix}${meta.id}`] = toNumber(primeRow?.montant ?? primeRow?.valeur ?? primeRow?.mtmontant)
  }
}

const mergePrimeColumns = (target: typeof primesImposables, rows: any[], resolvers: Array<(row: any) => any[]>) => {
  const map = new Map<number, Prime>()
  for (const col of target.value) map.set(col.id, col)
  for (const row of rows) {
    for (const resolve of resolvers) {
      const list = resolve(row)
      if (!Array.isArray(list)) continue
      for (const item of list) {
        const meta = extractPrimeMeta(item)
        if (meta && !map.has(meta.id)) map.set(meta.id, meta)
      }
    }
  }
  target.value = Array.from(map.values())
}

const mapBulletinRow = (row: any): Bulletin => {
  const personnel = resolvePersonnel(row)
  const matricule = personnel.matricule || row.matricule || ''
  const nom = personnel.nom || row.nom || ''
  const prenom = personnel.prenom || row.prenom || ''
  const nomComplet = personnel.nomComplet
    || `${prenom} ${nom}`.trim()
    || row.nomPrenom
    || ''

  const its = toNumber(row.its)
  const retenueFiscale = toNumber(row.igr ?? row.retenueFiscale)
  const cnps = toNumber(row.cnps ?? row.cn)

  const mapped = {
    id: toNumber(row.id ?? personnel.id),
    matricule,
    nom,
    prenom,
    nomComplet,
    statut: personnel.enSommeil === true ? 'sommeil' : 'actif',
    statutTravail: personnel.statfonct ?? '',
    nbJoursTravail: toNumber(row.jourTravail ?? row.jourtravail, 30),
    nombrePart: toNumber(row.nbrepart ?? row.nombrePart ?? personnel.nombrePart),
    anciennete: row.anciennete ?? '',
    salaireBase: toNumber(row.salairbase ?? row.salaireBase),
    sursalaire: toNumber(row.sursalaire),
    primeAnciennete: toNumber(row.primeanciennete ?? row.primeAnciennete),
    indemniteLogement: toNumber(row.indemnitelogement ?? row.indemniteLogement),
    brutImposable: toNumber(row.brutimposable ?? row.brutImposable),
    its,
    cn: toNumber(row.cn),
    igr: toNumber(row.igr),
    retenueFiscale,
    baseCnps: toNumber(row.basecnps ?? row.baseCnps),
    cnps,
    avceAcpte: toNumber(row.avceAcpte ?? row.avanceetacompte),
    pretAlios: toNumber(row.pretAlios ?? row.pretaloes),
    regularisation: toNumber(row.regularisation),
    netRegulPayer: toNumber(row.netRegulPayer ?? row.netRegulpayer),
    totalRetenue: toNumber(row.totalretenue ?? row.totalRetenue, its + retenueFiscale + cnps),
    netAPayer: toNumber(row.netapayer ?? row.netAPayer),
    totalBrut: toNumber(row.totalbrut ?? row.totalBrut),
    is: toNumber(row.is ?? row.impotSalaire),
    ta: toNumber(row.ta),
    fpc: toNumber(row.fpc),
    fpcregul: toNumber(row.fpcregul ?? row.fpcRegul),
    prestationFamiliale: toNumber(row.prestationFamiliale),
    accidentTravail: toNumber(row.accidentTravail),
    retraite: toNumber(row.retraite),
    totalPatronal: toNumber(row.totalpatronal ?? row.totalPatronal),
    totalMasseSalariale: toNumber(row.totalmassesalarial ?? row.totalMasseSalariale),
    indemniteTransportImp: toNumber(row.indemnitetransportimp ?? row.indemniteTransportImp),
    autreIndemImposable: toNumber(row.autreindemimposable ?? row.autreIndemImposable),
    indemniteRepresentation: toNumber(row.indemniterepresentation ?? row.indemniteRepresentation),
    indemniteTransport: toNumber(row.indemnitetransport ?? row.indemniteTransport),
    brutNonImposable: toNumber(row.brutnonimposable ?? row.brutNonImposable)
  } as Bulletin

  applyPrimeValues(mapped, row?.listIndemniteBrut ?? row?.listIndemBrut, 'prime')
  applyPrimeValues(mapped, row?.listIndemniteNonImp ?? row?.listIndemBrutNonImp, 'primeI')
  applyPrimeValues(mapped, row?.listRetenueSociale ?? row?.listRetenueSocial, 'primeS')
  applyPrimeValues(mapped, row?.listRetenueMutuellt ?? row?.listRetenueMutuelle, 'primeM')
  applyPrimeValues(mapped, row?.listGainsNet ?? row?.listPrimeGains, 'primeG')

  return mapped
}

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

const loadPeriodes = async () => {
  try {
    const res = await api.get('/parametrages/periodes/list', {
      params: {
        limit: 1000,
        offset: 0,
        search: ''
      }
    })
    const rawRows = Array.isArray(res.data)
      ? res.data
      : Array.isArray(res.data?.rows)
        ? res.data.rows
        : []
    const rows: any[] = rawRows.flatMap((row: any) => Array.isArray(row) ? row : [row])

    periods.value = rows
      .filter((period: any) => Number(period?.id))
      .map((period: any) => ({
        value: Number(period.id),
        label: period.affiche || period.libelle || `${period.mois?.mois || period.mois || ''} ${period.annee?.annee || period.annee || ''}`.trim() || `Période ${period.id}`
      }))
  } catch (e: any) {
    console.error('loadPeriodes error', e)
    ElMessage.error(e?.response?.data?.message || e?.message || 'Erreur lors du chargement des périodes')
    return
  }

  const active = await livrePaieService.getPeriodeActive()
  const activeId = active.success && active.data?.id ? Number(active.data.id) : periods.value[0]?.value
  if (activeId) {
    filters.period = activeId
    newForm.period = activeId
  }
}

const loadBulletins = async () => {
  if (!filters.period) {
    bulletinsData.value = []
    ElMessage.warning('Veuillez sélectionner une période de paie')
    return
  }

  loading.value = true
  try {
    const response = await api.get('/paie/bulletin/chargerbulletinparperiode', {
      params: {
        id: filters.period,
        limit: 999999,
        offset: 0
      }
    })

    const dto = response.data || {}
    const rows = Array.isArray(dto.rows) ? dto.rows : []
    primesImposables.value = []
    primesImposablesNon.value = []
    primesSociales.value = []
    primesMutuelles.value = []
    primesGains.value = []
    mergePrimeColumns(primesImposables, rows, [r => r?.listIndemniteBrut, r => r?.listIndemBrut])
    mergePrimeColumns(primesImposablesNon, rows, [r => r?.listIndemniteNonImp, r => r?.listIndemBrutNonImp])
    mergePrimeColumns(primesSociales, rows, [r => r?.listRetenueSociale, r => r?.listRetenueSocial])
    mergePrimeColumns(primesMutuelles, rows, [r => r?.listRetenueMutuellt, r => r?.listRetenueMutuelle])
    mergePrimeColumns(primesGains, rows, [r => r?.listGainsNet, r => r?.listPrimeGains])
    bulletinsData.value = rows.map(mapBulletinRow)
  } catch (e: any) {
    console.error('loadBulletins error', e)
    bulletinsData.value = []
    ElMessage.error(e?.response?.data?.message || e?.message || 'Erreur lors du chargement des bulletins')
  } finally {
    loading.value = false
  }
}

const searchBulletins = async () => {
  currentPage.value = 1
  await loadBulletins()
}

const applyFilters = () => {
  currentPage.value = 1
}

const resetFilters = async () => {
  filters.search = ''
  filters.matricule = ''
  filters.nom = ''
  filters.prenom = ''
  currentPage.value = 1
  await loadBulletins()
}

const exportAllData = () => {
  ElMessage.success('Export de tous les bulletins en cours...')
}

const downloadBlob = (blob: Blob, filename: string) => {
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

const printBulletin = async (bulletin: Bulletin) => {
  if (!bulletin.id) {
    ElMessage.warning('Identifiant du bulletin manquant')
    return
  }

  const loadingInstance = ElLoading.service({
    lock: true,
    text: 'Génération du PDF...',
    background: 'rgba(0, 0, 0, 0.7)'
  })

  try {
    const blob = await livrePaieService.printBulletinPdf(bulletin.id)
    if (!blob) {
      ElMessage.error('Erreur lors de la génération du bulletin')
      return
    }

    const periodLabel = periods.value.find(period => period.value === filters.period)?.label || 'paie'
    downloadBlob(blob, `Bulletin_${bulletin.matricule}_${periodLabel}.pdf`)
    ElMessage.success(`Bulletin de ${bulletin.nomComplet} téléchargé`)
  } catch (e: any) {
    console.error('printBulletin error', e)
    ElMessage.error(e?.message || 'Erreur lors de l\'impression du bulletin')
  } finally {
    loadingInstance.close()
  }
}

const createBulletin = () => {
  ElMessage.success('Création du bulletin')
  showNewModal.value = false
}

onMounted(() => {
  ;(async () => {
    await loadPeriodes()
    await loadBulletins()
  })()
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
