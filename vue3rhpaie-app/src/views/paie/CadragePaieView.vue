<template>
  <div class="cadrage-paie-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><TrendCharts /></el-icon>
            Cadrage de Paie
          </h1>
          <p class="page-subtitle">Analyse comparative M-1 vs M (contrôle de masse salariale)</p>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="exportExcel" :loading="exporting">
            <el-icon><Download /></el-icon>
            Exporter Excel
          </el-button>
        </div>
      </div>
    </div>

    <!-- Filtres -->
    <div class="filters-section enhanced-card">
      <el-form :model="filters" inline>
        <el-form-item label="Période courante">
          <el-select v-model="filters.periodeId" placeholder="Sélectionner la période" style="width: 250px" @change="loadCadrage">
            <el-option
              v-for="period in periods"
              :key="period.value"
              :label="period.label"
              :value="period.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadCadrage" :loading="loading">
            <el-icon><Search /></el-icon>
            Comparer
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- Message si aucune période sélectionnée -->
    <el-empty v-if="!cadrage && !loading" description="Sélectionnez une période pour comparer avec la période précédente" />

    <!-- Tableau de rapprochement global -->
    <div v-if="cadrage" class="global-section enhanced-card">
      <h3 class="section-title">Tableau de rapprochement global</h3>
      <div class="periods-info">
        <el-tag type="primary" size="large">M : {{ cadrage.periodeCourant }}</el-tag>
        <el-icon class="arrow-icon"><ArrowRight /></el-icon>
        <el-tag type="info" size="large">M-1 : {{ cadrage.periodePrecedente }}</el-tag>
      </div>

      <el-table :data="globalTableData" border stripe style="width: 100%; margin-top: 20px">
        <el-table-column prop="indicateur" label="Indicateur" width="250" />
        <el-table-column prop="m1" label="Mois Courant (M)" width="150" align="right">
          <template #default="{ row }">
            {{ formatCurrency(row.m1) }}
          </template>
        </el-table-column>
        <el-table-column prop="mMoins1" label="Mois Précédent (M-1)" width="150" align="right">
          <template #default="{ row }">
            {{ formatCurrency(row.mMoins1) }}
          </template>
        </el-table-column>
        <el-table-column prop="ecart" label="Écart (FCFA)" width="150" align="right">
          <template #default="{ row }">
            <span :class="getEcartClass(row.ecart)">
              {{ formatCurrency(row.ecart) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="ecartPourcent" label="Écart (%)" width="120" align="right">
          <template #default="{ row }">
            <span :class="getEcartClass(row.ecartPourcent)">
              {{ formatPercent(row.ecartPourcent) }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Analyse nominative par salarié -->
    <div v-if="cadrage" class="personnel-section enhanced-card">
      <h3 class="section-title">Analyse nominative (par salarié)</h3>

      <div class="filters-row">
        <el-form :model="personnelFilters" inline>
          <el-form-item label="Type d'écart">
            <el-select v-model="personnelFilters.typeEcart" placeholder="Tous" clearable style="width: 150px" @change="filterEcarts">
              <el-option label="Entrées" value="ENTREE" />
              <el-option label="Sorties" value="SORTIE" />
              <el-option label="Variables" value="VARIABLE" />
              <el-option label="Contractuels" value="CONTRACTUEL" />
              <el-option label="Stables" value="STABLE" />
            </el-select>
          </el-form-item>
          <el-form-item label="Recherche">
            <el-input v-model="personnelFilters.search" placeholder="Matricule ou nom" clearable style="width: 200px" @input="filterEcarts" />
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="filteredEcarts" border stripe style="width: 100%; margin-top: 15px" :default-sort="{ prop: 'ecartBrut', order: 'descending' }">
        <el-table-column prop="matricule" label="Matricule" width="120" />
        <el-table-column prop="nomPrenom" label="Nom & Prénom" width="200" />
        <el-table-column prop="typeEcart" label="Type" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeEcartTagType(row.typeEcart)" size="small">
              {{ row.typeEcart }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="brutCourant" label="Brut M" width="120" align="right">
          <template #default="{ row }">
            {{ formatCurrency(row.brutCourant) }}
          </template>
        </el-table-column>
        <el-table-column prop="brutPrecedent" label="Brut M-1" width="120" align="right">
          <template #default="{ row }">
            {{ formatCurrency(row.brutPrecedent) }}
          </template>
        </el-table-column>
        <el-table-column prop="ecartBrut" label="Écart Brut" width="120" align="right" sortable>
          <template #default="{ row }">
            <span :class="getEcartClass(row.ecartBrut)">
              {{ formatCurrency(row.ecartBrut) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="ecartBrutPourcent" label="Écart Brut %" width="120" align="right" sortable>
          <template #default="{ row }">
            <span :class="getEcartClass(row.ecartBrutPourcent)">
              {{ formatPercent(row.ecartBrutPourcent) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="netCourant" label="Net M" width="120" align="right">
          <template #default="{ row }">
            {{ formatCurrency(row.netCourant) }}
          </template>
        </el-table-column>
        <el-table-column prop="netPrecedent" label="Net M-1" width="120" align="right">
          <template #default="{ row }">
            {{ formatCurrency(row.netPrecedent) }}
          </template>
        </el-table-column>
        <el-table-column prop="ecartNet" label="Écart Net" width="120" align="right" sortable>
          <template #default="{ row }">
            <span :class="getEcartClass(row.ecartNet)">
              {{ formatCurrency(row.ecartNet) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="ecartNetPourcent" label="Écart Net %" width="120" align="right" sortable>
          <template #default="{ row }">
            <span :class="getEcartClass(row.ecartNetPourcent)">
              {{ formatPercent(row.ecartNetPourcent) }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { TrendCharts, ArrowRight, Search, Download } from '@element-plus/icons-vue'
import api from '@/services/api'

interface Periode {
  value: number
  label: string
}

interface CadrageGlobal {
  effectifCourant: number
  effectifPrecedent: number
  ecartEffectif: number
  ecartEffectifPourcent: number
  salaireBaseCourant: number
  salaireBasePrecedent: number
  ecartSalaireBase: number
  ecartSalaireBasePourcent: number
  primesIndemnitesCourant: number
  primesIndemnitesPrecedent: number
  ecartPrimesIndemnites: number
  ecartPrimesIndemnitesPourcent: number
  totalBrutCourant: number
  totalBrutPrecedent: number
  ecartTotalBrut: number
  ecartTotalBrutPourcent: number
  chargesPatronalesCourant: number
  chargesPatronalesPrecedent: number
  ecartChargesPatronales: number
  ecartChargesPatronalesPourcent: number
  cotisationsSalarialesCourant: number
  cotisationsSalarialesPrecedent: number
  ecartCotisationsSalariales: number
  ecartCotisationsSalarialesPourcent: number
  netAPayerCourant: number
  netAPayerPrecedent: number
  ecartNetAPayer: number
  ecartNetAPayerPourcent: number
}

interface EcartPersonnel {
  id: number
  matricule: string
  nomPrenom: string
  brutCourant: number
  brutPrecedent: number
  ecartBrut: number
  ecartBrutPourcent: number
  netCourant: number
  netPrecedent: number
  ecartNet: number
  ecartNetPourcent: number
  typeEcart: string
}

interface CadrageData {
  periodeCourant: string
  periodePrecedente: string
  global: CadrageGlobal
  ecartsPersonnels: EcartPersonnel[]
}

const loading = ref(false)
const exporting = ref(false)
const periods = ref<Periode[]>([])
const cadrage = ref<CadrageData | null>(null)
const filters = ref({ periodeId: null as number | null })
const personnelFilters = ref({ typeEcart: '' as string, search: '' as string })

const globalTableData = computed(() => {
  if (!cadrage.value?.global) return []
  const g = cadrage.value.global
  return [
    { indicateur: 'Effectif total', m1: g.effectifCourant, mMoins1: g.effectifPrecedent, ecart: g.ecartEffectif, ecartPourcent: g.ecartEffectifPourcent },
    { indicateur: 'Salaire de base brut', m1: g.salaireBaseCourant, mMoins1: g.salaireBasePrecedent, ecart: g.ecartSalaireBase, ecartPourcent: g.ecartSalaireBasePourcent },
    { indicateur: 'Primes & Indemnités', m1: g.primesIndemnitesCourant, mMoins1: g.primesIndemnitesPrecedent, ecart: g.ecartPrimesIndemnites, ecartPourcent: g.ecartPrimesIndemnitesPourcent },
    { indicateur: 'Total Brut', m1: g.totalBrutCourant, mMoins1: g.totalBrutPrecedent, ecart: g.ecartTotalBrut, ecartPourcent: g.ecartTotalBrutPourcent },
    { indicateur: 'Cotisations Patronales', m1: g.chargesPatronalesCourant, mMoins1: g.chargesPatronalesPrecedent, ecart: g.ecartChargesPatronales, ecartPourcent: g.ecartChargesPatronalesPourcent },
    { indicateur: 'Cotisations Salariales', m1: g.cotisationsSalarialesCourant, mMoins1: g.cotisationsSalarialesPrecedent, ecart: g.ecartCotisationsSalariales, ecartPourcent: g.ecartCotisationsSalarialesPourcent },
    { indicateur: 'Net à payer', m1: g.netAPayerCourant, mMoins1: g.netAPayerPrecedent, ecart: g.ecartNetAPayer, ecartPourcent: g.ecartNetAPayerPourcent }
  ]
})

const filteredEcarts = computed(() => {
  if (!cadrage.value?.ecartsPersonnels) return []
  let ecarts = [...cadrage.value.ecartsPersonnels]

  if (personnelFilters.value.typeEcart) {
    ecarts = ecarts.filter(e => e.typeEcart === personnelFilters.value.typeEcart)
  }

  if (personnelFilters.value.search) {
    const search = personnelFilters.value.search.toLowerCase()
    ecarts = ecarts.filter(e =>
      e.matricule?.toLowerCase().includes(search) ||
      e.nomPrenom?.toLowerCase().includes(search)
    )
  }

  return ecarts
})

const loadPeriods = async () => {
  try {
    const response = await api.get('/periodepaie')
    periods.value = response.data.map((p: any) => ({
      value: p.id,
      label: p.affiche || `${p.mois?.mois} ${p.annee?.annee}`
    }))
  } catch (error) {
    ElMessage.error('Erreur lors du chargement des périodes')
  }
}

const loadCadrage = async () => {
  if (!filters.value.periodeId) {
    ElMessage.warning('Veuillez sélectionner une période')
    return
  }

  loading.value = true
  try {
    const response = await api.get(`/paie/bulletin/cadrage/${filters.value.periodeId}`)
    cadrage.value = response.data
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || 'Erreur lors du calcul du cadrage')
  } finally {
    loading.value = false
  }
}

const filterEcarts = () => {
  // Filtrage géré par computed
}

const formatCurrency = (value: number) => {
  if (value === null || value === undefined) return '0'
  return new Intl.NumberFormat('fr-FR').format(value)
}

const formatPercent = (value: number) => {
  if (value === null || value === undefined) return '0%'
  return `${value.toFixed(2)}%`
}

const getEcartClass = (value: number) => {
  if (value > 0) return 'ecart-positive'
  if (value < 0) return 'ecart-negative'
  return ''
}

const getTypeEcartTagType = (type: string) => {
  switch (type) {
    case 'ENTREE': return 'success'
    case 'SORTIE': return 'danger'
    case 'VARIABLE': return 'warning'
    case 'CONTRACTUEL': return 'info'
    default: return ''
  }
}

const exportExcel = () => {
  ElMessage.info('Export Excel en cours de développement')
}

onMounted(() => {
  loadPeriods()
})
</script>

<style scoped>
.cadrage-paie-view {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.page-subtitle {
  margin: 5px 0 0 0;
  color: #909399;
  font-size: 14px;
}

.filters-section {
  margin-bottom: 20px;
  padding: 20px;
}

.global-section,
.personnel-section {
  margin-bottom: 20px;
  padding: 20px;
}

.section-title {
  margin: 0 0 15px 0;
  font-size: 18px;
  color: #303133;
}

.periods-info {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.arrow-icon {
  font-size: 20px;
  color: #909399;
}

.filters-row {
  margin-bottom: 15px;
}

.ecart-positive {
  color: #67c23a;
  font-weight: bold;
}

.ecart-negative {
  color: #f56c6c;
  font-weight: bold;
}

.enhanced-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.enhanced-button {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>
