<template>
  <div class="reporting-view">
    <div class="page-header">
      <h1>Reporting & Tableaux de bord</h1>
      <p>Analyses et rapports sur les données RH et de paie</p>
      <el-tag v-if="currentPeriodeLabel" type="info" size="large" style="margin-left: 12px;">
        {{ currentPeriodeLabel }}
      </el-tag>
    </div>

    <el-row :gutter="24">
      <el-col :span="6">
        <el-card class="kpi-card" v-loading="loading.kpis">
          <div class="kpi-content">
            <div class="kpi-icon" style="background: #e7f5ff; color: #1890ff;">
              <el-icon><User /></el-icon>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">{{ kpis.effectifTotal ?? '—' }}</div>
              <div class="kpi-label">Effectif total</div>
              <div class="kpi-trend">
                <span>H: {{ kpis.effectifHommes ?? 0 }} / F: {{ kpis.effectifFemmes ?? 0 }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="kpi-card" v-loading="loading.kpis">
          <div class="kpi-content">
            <div class="kpi-icon" style="background: #f6ffed; color: #52c41a;">
              <el-icon><Money /></el-icon>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">{{ formatMasseSalariale(kpis.masseSalariale) }}</div>
              <div class="kpi-label">Masse salariale (période active)</div>
              <div class="kpi-trend">
                <span>Période #{{ kpis.periodeActive ?? 'N/A' }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="kpi-card" v-loading="loading.kpis">
          <div class="kpi-content">
            <div class="kpi-icon" style="background: #fff2e8; color: #fa8c16;">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">{{ kpis.contractuels ?? 0 }}</div>
              <div class="kpi-label">Contractuels</div>
              <div class="kpi-trend">
                <span>Stag.: {{ kpis.stagiaires ?? 0 }} | Fct.: {{ kpis.fonctionnaires ?? 0 }} | Cons.: {{ kpis.consultants ?? 0 }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="kpi-card" v-loading="loading.retraite">
          <div class="kpi-content">
            <div class="kpi-icon" style="background: #fff1f0; color: #ff4d4f;">
              <el-icon><SwitchButton /></el-icon>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">{{ retraiteTotal }}</div>
              <div class="kpi-label">Proches de la retraite</div>
              <div class="kpi-trend">
                <span>H: {{ retraiteHommes }} / F: {{ retraiteFemmes }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-tabs v-model="activeTab" style="margin-top: 24px;" @tab-change="onTabChange">
      <el-tab-pane label="Dashboard DG" name="dg-dashboard">
        <el-row :gutter="24">
          <el-col :span="24">
            <el-card v-loading="loading.masseSalarialeEvo">
              <template #header>
                <span>Évolution de la masse salariale (année active)</span>
              </template>
              <div class="chart-container">
                <canvas ref="masseSalarialeEvoChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="12">
            <el-card v-loading="loading.coutsPersonnel">
              <template #header>
                <span>Répartition des coûts du personnel</span>
              </template>
              <div class="chart-container">
                <canvas ref="coutsPersonnelChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card v-loading="loading.typeContrat">
              <template #header>
                <span>Effectif total par statut</span>
              </template>
              <div class="chart-container">
                <canvas ref="typeContratChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="Tableau de bord" name="dashboard">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-card v-loading="loading.effectifAnnuel">
              <template #header>
                <span>Évolution de l'effectif (5 dernières années)</span>
              </template>
              <div class="chart-container">
                <canvas ref="staffChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card v-loading="loading.typeContrat">
              <template #header>
                <span>Répartition par type de contrat</span>
              </template>
              <div class="chart-container">
                <canvas ref="typeContratChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="12">
            <el-card v-loading="loading.masseTypeContrat">
              <template #header>
                <span>Masse salariale par type de contrat</span>
              </template>
              <div class="chart-container">
                <canvas ref="salaryChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card v-loading="loading.effectifSite">
              <template #header>
                <span>Effectif par site</span>
              </template>
              <div class="chart-container">
                <canvas ref="siteChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="24">
            <el-card v-loading="loading.masseSite">
              <template #header>
                <span>Masse salariale par site</span>
              </template>
              <div class="chart-container">
                <canvas ref="masseSiteChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="Rapports RH" name="hr-reports">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-card v-loading="loading.effectifMensuel">
              <template #header>
                <span>Évolution des effectifs (mois de l'année active)</span>
              </template>
              <div class="chart-container">
                <canvas ref="effectifMensuelChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card v-loading="loading.agePyramid">
              <template #header>
                <span>Pyramide des âges</span>
              </template>
              <div class="chart-container">
                <canvas ref="agePyramidChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="12">
            <el-card v-loading="loading.anciennete">
              <template #header>
                <span>Ancienneté des employés</span>
              </template>
              <div class="chart-container">
                <canvas ref="ancienneteChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card v-loading="loading.conge">
              <template #header>
                <span>Statistiques des congés par mois</span>
              </template>
              <div class="chart-container">
                <canvas ref="congeChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="24">
            <el-card v-loading="loading.retraite">
              <template #header>
                <span>Personnel proche de la retraite</span>
              </template>
              <div class="chart-container">
                <canvas ref="retraiteChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-card style="margin-top: 24px;">
          <template #header>
            <span>Indicateurs sociaux</span>
          </template>
          <el-table :data="socialIndicators" style="width: 100%">
            <el-table-column prop="indicator" label="Indicateur" />
            <el-table-column prop="value" label="Valeur" />
            <el-table-column prop="target" label="Objectif" />
            <el-table-column prop="trend" label="Tendance">
              <template #default="scope">
                <el-tag :type="scope.row.trend === 'positive' ? 'success' : 'danger'">
                  {{ scope.row.trend === 'positive' ? 'Positif' : 'Négatif' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="Rapports de paie" name="payroll-reports">
        <el-row :gutter="24">
          <el-col :span="24">
            <el-card v-loading="loading.netPaye">
              <template #header>
                <span>Évolution du net payé (année active)</span>
              </template>
              <div class="chart-container">
                <canvas ref="netPayeChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="12">
            <el-card v-loading="loading.brutNetCharges">
              <template #header>
                <span>Brut vs Net vs Charges (période active)</span>
              </template>
              <div class="chart-container">
                <canvas ref="brutNetChargesChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card v-loading="loading.primes">
              <template #header>
                <span>Répartition des primes (période active)</span>
              </template>
              <div class="chart-container">
                <canvas ref="primesChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="24">
            <el-card v-loading="loading.retenues">
              <template #header>
                <span>Analyse des retenues (période active)</span>
              </template>
              <div class="chart-container">
                <canvas ref="retenuesChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-card style="margin-top: 24px;">
          <template #header>
            <span>Statistiques de paie (période active)</span>
          </template>
          <el-row :gutter="24">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ kpis.effectifTotal ?? '—' }}</div>
                <div class="stat-label">Effectif total</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ formatMasseSalariale(kpis.masseSalariale) }}</div>
                <div class="stat-label">Masse salariale</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ kpis.contractuels ?? 0 }}</div>
                <div class="stat-label">Contractuels</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ kpis.stagiaires ?? 0 }}</div>
                <div class="stat-label">Stagiaires</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="Exportations" name="exports">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Exportations disponibles</span>
              <el-button type="primary">
                <el-icon><Download /></el-icon>
                Nouvel export
              </el-button>
            </div>
          </template>

          <el-table :data="exports" style="width: 100%">
            <el-table-column prop="name" label="Nom de l'export" />
            <el-table-column prop="type" label="Type" />
            <el-table-column prop="format" label="Format" />
            <el-table-column prop="date" label="Date de création" />
            <el-table-column prop="status" label="Statut">
              <template #default="scope">
                <el-tag :type="getExportStatusType(scope.row.status)">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="Actions">
              <template #default="scope">
                <el-button size="small" type="primary" v-if="scope.row.status === 'Terminé'">
                  Télécharger
                </el-button>
                <el-button size="small" type="danger" @click="deleteExport(scope.row)">
                  Supprimer
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { Chart, registerables } from 'chart.js'
import {
  User, Money, Clock, SwitchButton,
  PieChart, DataAnalysis, Download
} from '@element-plus/icons-vue'
import { reportingService, type ReportingKpis, type PrintLs } from '@/services/reporting.service'
import { api } from '@/services/api'

Chart.register(...registerables)

interface SocialIndicator {
  indicator: string
  value: string
  target: string
  trend: 'positive' | 'negative'
}

interface ExportItem {
  id: number
  name: string
  type: string
  format: string
  date: string
  status: string
}

const activeTab = ref('dg-dashboard')

const currentExerciceId = ref<number | null>(null)
const currentPeriodeLabel = ref('')

const loadActivePeriode = async () => {
  try {
    const { data } = await api.get('/parametrages/periodes/active')
    const periode = data?.row || data
    if (periode?.affiche) {
      currentPeriodeLabel.value = periode.affiche
    } else if (periode?.mois && periode?.annee) {
      currentPeriodeLabel.value = `${periode.mois} ${periode.annee}`
    }
    if (periode?.annee?.id) {
      currentExerciceId.value = periode.annee.id
    }
  } catch (e) {
    console.error('Erreur chargement période active:', e)
  }
}

const onTabChange = (tabName: string) => {
  nextTick(() => {
    setTimeout(() => {
      if (tabName === 'hr-reports') {
        renderCongeChart(congeData.value)
        renderRetraiteChart(retraiteData.value)
      }
    }, 100)
  })
}

const kpis = ref<ReportingKpis>({
  effectifTotal: 0,
  effectifHommes: 0,
  effectifFemmes: 0,
  masseSalariale: 0,
  contractuels: 0,
  stagiaires: 0,
  fonctionnaires: 0,
  consultants: 0,
  periodeActive: null,
})

const retraiteHommes = ref(0)
const retraiteFemmes = ref(0)
const retraiteTotal = ref(0)

const congeData = ref<PrintLs[]>([])
const retraiteData = ref<PrintLs[]>([])

const loading = ref({
  kpis: false,
  effectifAnnuel: false,
  typeContrat: false,
  masseTypeContrat: false,
  effectifSite: false,
  masseSite: false,
  conge: false,
  retraite: false,
  agePyramid: false,
  anciennete: false,
  effectifMensuel: false,
  netPaye: false,
  brutNetCharges: false,
  primes: false,
  retenues: false,
  masseSalarialeEvo: false,
  coutsPersonnel: false,
})

const socialIndicators = ref<SocialIndicator[]>([
  { indicator: 'Taux de turnover', value: '—', target: '< 10%', trend: 'positive' },
  { indicator: 'Taux d\'absentéisme', value: '—', target: '< 5%', trend: 'positive' },
  { indicator: 'Taux de satisfaction', value: '—', target: '> 80%', trend: 'negative' },
  { indicator: 'Taux de formation', value: '—', target: '> 70%', trend: 'negative' },
])

const exports = ref<ExportItem[]>([
  { id: 1, name: 'Effectifs Mars 2024', type: 'RH', format: 'Excel', date: '01/04/2024', status: 'Terminé' },
  { id: 2, name: 'Paie Q1 2024', type: 'Paie', format: 'PDF', date: '05/04/2024', status: 'En cours' },
  { id: 3, name: 'Analytique Annuel 2023', type: 'Analytique', format: 'CSV', date: '15/01/2024', status: 'Terminé' },
])

// Chart refs
const staffChart = ref<HTMLCanvasElement>()
const typeContratChart = ref<HTMLCanvasElement>()
const salaryChart = ref<HTMLCanvasElement>()
const siteChart = ref<HTMLCanvasElement>()
const masseSiteChart = ref<HTMLCanvasElement>()
const congeChart = ref<HTMLCanvasElement>()
const retraiteChart = ref<HTMLCanvasElement>()
const agePyramidChart = ref<HTMLCanvasElement>()
const ancienneteChart = ref<HTMLCanvasElement>()
const effectifMensuelChart = ref<HTMLCanvasElement>()
const netPayeChart = ref<HTMLCanvasElement>()
const brutNetChargesChart = ref<HTMLCanvasElement>()
const primesChart = ref<HTMLCanvasElement>()
const retenuesChart = ref<HTMLCanvasElement>()
const masseSalarialeEvoChart = ref<HTMLCanvasElement>()
const coutsPersonnelChart = ref<HTMLCanvasElement>()

// Chart instances
let charts: Chart[] = []

onMounted(async () => {
  await nextTick()
  await loadActivePeriode()
  await loadAllData()
})

onBeforeUnmount(() => {
  charts.forEach(c => c.destroy())
  charts = []
})

const toNumber = (v: any): number => {
  if (v == null) return 0
  const n = Number(v)
  return Number.isFinite(n) ? n : 0
}

const formatMasseSalariale = (value: any): string => {
  const num = toNumber(value)
  if (num === 0) return '—'
  if (num >= 1_000_000) return (num / 1_000_000).toFixed(1) + 'M'
  if (num >= 1_000) return (num / 1_000).toFixed(1) + 'K'
  return num.toFixed(0)
}

const loadAllData = async () => {
  await Promise.all([
    loadKpis(),
    loadEffectifAnnuel(),
    loadTypeContrat(),
    loadMasseSalarialeTypeContrat(),
    loadEffectifParSite(),
    loadMasseSalarialeParSite(),
    loadCongeStat(),
    loadRetraiteStat(),
    loadPyramideAges(),
    loadAnciennete(),
    loadEffectifMensuel(),
    loadNetPayeEvolution(),
    loadBrutNetCharges(),
    loadRepartitionPrimes(),
    loadAnalyseRetenues(),
    loadMasseSalarialeEvolution(),
    loadCoutsPersonnel(),
  ])
}

const loadKpis = async () => {
  loading.value.kpis = true
  try {
    const data = await reportingService.getKpis()
    kpis.value = {
      effectifTotal: toNumber(data.effectifTotal),
      effectifHommes: toNumber(data.effectifHommes),
      effectifFemmes: toNumber(data.effectifFemmes),
      masseSalariale: toNumber(data.masseSalariale),
      contractuels: toNumber(data.contractuels),
      stagiaires: toNumber(data.stagiaires),
      fonctionnaires: toNumber(data.fonctionnaires),
      consultants: toNumber(data.consultants),
      periodeActive: data.periodeActive ?? null,
    }
  } catch (e) {
    console.error('Failed to load KPIs:', e)
  } finally {
    loading.value.kpis = false
  }
}

const loadEffectifAnnuel = async () => {
  loading.value.effectifAnnuel = true
  try {
    const data = await reportingService.getEffectifAnnuel(currentExerciceId.value ?? undefined)
    await nextTick()
    renderStaffChart(data)
  } catch (e) {
    console.error('Failed to load effectif annuel:', e)
  } finally {
    loading.value.effectifAnnuel = false
  }
}

const loadTypeContrat = async () => {
  loading.value.typeContrat = true
  try {
    const data = await reportingService.getTypeContratStat()
    await nextTick()
    renderTypeContratChart(data)
  } catch (e) {
    console.error('Failed to load type contrat stat:', e)
  } finally {
    loading.value.typeContrat = false
  }
}

const loadMasseSalarialeTypeContrat = async () => {
  loading.value.masseTypeContrat = true
  try {
    const data = await reportingService.getMasseSalarialeParTypeContrat()
    await nextTick()
    renderSalaryChart(data)
  } catch (e) {
    console.error('Failed to load masse salariale par type contrat:', e)
  } finally {
    loading.value.masseTypeContrat = false
  }
}

const loadEffectifParSite = async () => {
  loading.value.effectifSite = true
  try {
    const data = await reportingService.getEffectifParSite()
    await nextTick()
    renderSiteChart(data)
  } catch (e) {
    console.error('Failed to load effectif par site:', e)
  } finally {
    loading.value.effectifSite = false
  }
}

const loadMasseSalarialeParSite = async () => {
  loading.value.masseSite = true
  try {
    const data = await reportingService.getMasseSalarialeParSite()
    await nextTick()
    renderMasseSiteChart(data)
  } catch (e) {
    console.error('Failed to load masse salariale par site:', e)
  } finally {
    loading.value.masseSite = false
  }
}

const loadCongeStat = async () => {
  loading.value.conge = true
  try {
    const data = await reportingService.getCongeStat(currentExerciceId.value ?? undefined)
    congeData.value = data
    await nextTick()
    renderCongeChart(data)
  } catch (e) {
    console.error('Failed to load conge stat:', e)
  } finally {
    loading.value.conge = false
  }
}

const loadRetraiteStat = async () => {
  loading.value.retraite = true
  try {
    const data = await reportingService.getRetraiteStat(currentExerciceId.value ?? undefined)
    retraiteData.value = data
    if (data.length > 0) {
      retraiteHommes.value = toNumber(data[0].i1)
      retraiteFemmes.value = toNumber(data[0].i2)
      retraiteTotal.value = retraiteHommes.value + retraiteFemmes.value
    }
    await nextTick()
    renderRetraiteChart(data)
  } catch (e) {
    console.error('Failed to load retraite stat:', e)
  } finally {
    loading.value.retraite = false
  }
}

const loadPyramideAges = async () => {
  loading.value.agePyramid = true
  try {
    const data = await reportingService.getPyramideAges()
    await nextTick()
    renderAgePyramidChart(data)
  } catch (e) {
    console.error('Failed to load pyramide ages:', e)
  } finally {
    loading.value.agePyramid = false
  }
}

const loadAnciennete = async () => {
  loading.value.anciennete = true
  try {
    const data = await reportingService.getAnciennete()
    await nextTick()
    renderAncienneteChart(data)
  } catch (e) {
    console.error('Failed to load anciennete:', e)
  } finally {
    loading.value.anciennete = false
  }
}

const loadEffectifMensuel = async () => {
  loading.value.effectifMensuel = true
  try {
    const data = await reportingService.getEffectifMensuel()
    await nextTick()
    renderEffectifMensuelChart(data)
  } catch (e) {
    console.error('Failed to load effectif mensuel:', e)
  } finally {
    loading.value.effectifMensuel = false
  }
}

const loadNetPayeEvolution = async () => {
  loading.value.netPaye = true
  try {
    const data = await reportingService.getNetPayeEvolution()
    await nextTick()
    renderNetPayeChart(data)
  } catch (e) {
    console.error('Failed to load net paye evolution:', e)
  } finally {
    loading.value.netPaye = false
  }
}

const loadBrutNetCharges = async () => {
  loading.value.brutNetCharges = true
  try {
    const data = await reportingService.getBrutNetCharges()
    await nextTick()
    renderBrutNetChargesChart(data)
  } catch (e) {
    console.error('Failed to load brut net charges:', e)
  } finally {
    loading.value.brutNetCharges = false
  }
}

const loadRepartitionPrimes = async () => {
  loading.value.primes = true
  try {
    const data = await reportingService.getRepartitionPrimes()
    await nextTick()
    renderPrimesChart(data)
  } catch (e) {
    console.error('Failed to load repartition primes:', e)
  } finally {
    loading.value.primes = false
  }
}

const loadAnalyseRetenues = async () => {
  loading.value.retenues = true
  try {
    const data = await reportingService.getAnalyseRetenues()
    await nextTick()
    renderRetenuesChart(data)
  } catch (e) {
    console.error('Failed to load analyse retenues:', e)
  } finally {
    loading.value.retenues = false
  }
}

const loadMasseSalarialeEvolution = async () => {
  loading.value.masseSalarialeEvo = true
  try {
    const data = await reportingService.getMasseSalarialeEvolution()
    await nextTick()
    renderMasseSalarialeEvoChart(data)
  } catch (e) {
    console.error('Failed to load masse salariale evolution:', e)
  } finally {
    loading.value.masseSalarialeEvo = false
  }
}

const loadCoutsPersonnel = async () => {
  loading.value.coutsPersonnel = true
  try {
    const data = await reportingService.getCoutsPersonnel()
    await nextTick()
    renderCoutsPersonnelChart(data)
  } catch (e) {
    console.error('Failed to load couts personnel:', e)
  } finally {
    loading.value.coutsPersonnel = false
  }
}

// ==================== Chart renderers ====================

const destroyChart = (canvas: HTMLCanvasElement | undefined) => {
  if (!canvas) return
  const existing = Chart.getChart(canvas)
  if (existing) existing.destroy()
}

const renderStaffChart = (data: PrintLs[]) => {
  destroyChart(staffChart.value)
  if (!staffChart.value || data.length === 0) return

  const chart = new Chart(staffChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [
        {
          label: 'Hommes',
          data: data.map(d => toNumber(d.i1)),
          backgroundColor: '#1890ff',
        },
        {
          label: 'Femmes',
          data: data.map(d => toNumber(d.i2)),
          backgroundColor: '#eb2f96',
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderTypeContratChart = (data: PrintLs[]) => {
  destroyChart(typeContratChart.value)
  if (!typeContratChart.value || data.length === 0) return

  const colors = ['#1890ff', '#52c41a', '#fa8c16', '#ff4d4f', '#722ed1']
  const chart = new Chart(typeContratChart.value, {
    type: 'pie',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        data: data.map(d => toNumber(d.i1)),
        backgroundColor: colors.slice(0, data.length),
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
    },
  })
  charts.push(chart)
}

const renderSalaryChart = (data: PrintLs[]) => {
  destroyChart(salaryChart.value)
  if (!salaryChart.value || data.length === 0) return

  const chart = new Chart(salaryChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        label: 'Masse salariale',
        data: data.map(d => toNumber(d.value1)),
        backgroundColor: '#52c41a',
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderSiteChart = (data: PrintLs[]) => {
  destroyChart(siteChart.value)
  if (!siteChart.value || data.length === 0) return

  const chart = new Chart(siteChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        label: 'Effectif',
        data: data.map(d => toNumber(d.i1)),
        backgroundColor: '#fa8c16',
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderMasseSiteChart = (data: PrintLs[]) => {
  destroyChart(masseSiteChart.value)
  if (!masseSiteChart.value || data.length === 0) return

  const chart = new Chart(masseSiteChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        label: 'Masse salariale',
        data: data.map(d => toNumber(d.value1)),
        backgroundColor: '#722ed1',
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderCongeChart = (data: PrintLs[]) => {
  destroyChart(congeChart.value)
  if (!congeChart.value || data.length === 0) return

  const chart = new Chart(congeChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [
        {
          label: 'Congés',
          data: data.map(d => toNumber(d.i1)),
          backgroundColor: '#1890ff',
        },
        {
          label: 'Planning congés',
          data: data.map(d => toNumber(d.i2)),
          backgroundColor: '#52c41a',
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderRetraiteChart = (data: PrintLs[]) => {
  destroyChart(retraiteChart.value)
  if (!retraiteChart.value || data.length === 0) return

  const chart = new Chart(retraiteChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => `Retraite ${d.s1 ?? ''}`),
      datasets: [
        {
          label: 'Hommes',
          data: data.map(d => toNumber(d.i1)),
          backgroundColor: '#1890ff',
        },
        {
          label: 'Femmes',
          data: data.map(d => toNumber(d.i2)),
          backgroundColor: '#eb2f96',
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderAgePyramidChart = (data: PrintLs[]) => {
  destroyChart(agePyramidChart.value)
  if (!agePyramidChart.value || data.length === 0) return

  const chart = new Chart(agePyramidChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [
        {
          label: 'Hommes',
          data: data.map(d => toNumber(d.i1)),
          backgroundColor: '#1890ff',
        },
        {
          label: 'Femmes',
          data: data.map(d => -toNumber(d.i2)),
          backgroundColor: '#eb2f96',
        },
      ],
    },
    options: {
      indexAxis: 'y',
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        x: {
          stacked: true,
          ticks: {
            callback: (val: any) => Math.abs(val),
          },
        },
        y: { stacked: true },
      },
      plugins: {
        tooltip: {
          callbacks: {
            label: (ctx: any) => `${ctx.dataset.label}: ${Math.abs(ctx.parsed.x)}`,
          },
        },
      },
    },
  })
  charts.push(chart)
}

const renderAncienneteChart = (data: PrintLs[]) => {
  destroyChart(ancienneteChart.value)
  if (!ancienneteChart.value || data.length === 0) return

  const chart = new Chart(ancienneteChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        label: 'Effectif',
        data: data.map(d => toNumber(d.i1)),
        backgroundColor: '#722ed1',
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderEffectifMensuelChart = (data: PrintLs[]) => {
  destroyChart(effectifMensuelChart.value)
  if (!effectifMensuelChart.value || data.length === 0) return

  const chart = new Chart(effectifMensuelChart.value, {
    type: 'line',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        label: 'Effectif',
        data: data.map(d => toNumber(d.i1)),
        borderColor: '#1890ff',
        backgroundColor: 'rgba(24, 144, 255, 0.1)',
        fill: true,
        tension: 0.3,
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderNetPayeChart = (data: PrintLs[]) => {
  destroyChart(netPayeChart.value)
  if (!netPayeChart.value || data.length === 0) return

  const chart = new Chart(netPayeChart.value, {
    type: 'line',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        label: 'Net payé',
        data: data.map(d => toNumber(d.value1)),
        borderColor: '#52c41a',
        backgroundColor: 'rgba(82, 196, 26, 0.1)',
        fill: true,
        tension: 0.3,
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderBrutNetChargesChart = (data: PrintLs[]) => {
  destroyChart(brutNetChargesChart.value)
  if (!brutNetChargesChart.value || data.length === 0) return

  const chart = new Chart(brutNetChargesChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        label: 'Montant',
        data: data.map(d => toNumber(d.value1)),
        backgroundColor: ['#faad14', '#52c41a', '#ff4d4f'],
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderPrimesChart = (data: PrintLs[]) => {
  destroyChart(primesChart.value)
  if (!primesChart.value || data.length === 0) return

  const chart = new Chart(primesChart.value, {
    type: 'doughnut',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        data: data.map(d => toNumber(d.value1)),
        backgroundColor: ['#1890ff', '#52c41a', '#faad14', '#722ed1', '#eb2f96'],
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { position: 'right' },
      },
    },
  })
  charts.push(chart)
}

const renderRetenuesChart = (data: PrintLs[]) => {
  destroyChart(retenuesChart.value)
  if (!retenuesChart.value || data.length === 0) return

  const chart = new Chart(retenuesChart.value, {
    type: 'bar',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        label: 'Retenues',
        data: data.map(d => toNumber(d.value1)),
        backgroundColor: '#fa541c',
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderMasseSalarialeEvoChart = (data: PrintLs[]) => {
  destroyChart(masseSalarialeEvoChart.value)
  if (!masseSalarialeEvoChart.value || data.length === 0) return

  const chart = new Chart(masseSalarialeEvoChart.value, {
    type: 'line',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        label: 'Masse salariale',
        data: data.map(d => toNumber(d.value1)),
        borderColor: '#faad14',
        backgroundColor: 'rgba(250, 173, 20, 0.1)',
        fill: true,
        tension: 0.3,
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } },
    },
  })
  charts.push(chart)
}

const renderCoutsPersonnelChart = (data: PrintLs[]) => {
  destroyChart(coutsPersonnelChart.value)
  if (!coutsPersonnelChart.value || data.length === 0) return

  const chart = new Chart(coutsPersonnelChart.value, {
    type: 'doughnut',
    data: {
      labels: data.map(d => d.s1 ?? ''),
      datasets: [{
        data: data.map(d => toNumber(d.value1)),
        backgroundColor: ['#1890ff', '#52c41a', '#faad14', '#ff4d4f', '#722ed1'],
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { position: 'right' },
      },
    },
  })
  charts.push(chart)
}

const getExportStatusType = (status: string) => {
  switch (status) {
    case 'Terminé': return 'success'
    case 'En cours': return 'warning'
    case 'Erreur': return 'danger'
    default: return 'info'
  }
}

const deleteExport = (exportItem: ExportItem) => {
  const index = exports.value.findIndex(e => e.id === exportItem.id)
  if (index > -1) {
    exports.value.splice(index, 1)
  }
}
</script>

<style scoped>
.reporting-view {
  padding: 24px;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
}

.kpi-card {
  margin-bottom: 24px;
}

.kpi-content {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.kpi-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 20px;
}

.kpi-info {
  flex: 1;
}

.kpi-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.kpi-label {
  font-size: 14px;
  color: #909399;
  margin: 4px 0;
}

.kpi-trend {
  font-size: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

.report-card {
  margin-bottom: 24px;
  cursor: pointer;
  transition: all 0.3s;
}

.report-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.report-content {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.report-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 20px;
}

.report-info {
  flex: 1;
}

.report-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
}

.report-desc {
  font-size: 14px;
  color: #909399;
  margin-bottom: 12px;
}

.chart-container {
  height: 300px;
  position: relative;
}

.stat-item {
  text-align: center;
  padding: 16px;
}

.stat-number {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
