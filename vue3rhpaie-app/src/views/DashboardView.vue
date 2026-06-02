<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h1>Tableau de bord</h1>
      <p>Bienvenue {{ authStore.user?.firstName }} {{ authStore.user?.lastName }}</p>
    </div>
    
    <!-- Statistiques principales -->
    <div class="stats-grid">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon employees">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <h3>{{ stats.totalEmployes }}</h3>
            <p>Total Employés</p>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon active">
            <el-icon><UserFilled /></el-icon>
          </div>
          <div class="stat-info">
            <h3>{{ stats.employesActifs }}</h3>
            <p>Employés Actifs</p>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon contracts">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <h3>{{ stats.contratsExpirant }}</h3>
            <p>Contrats expirant ce mois</p>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon payroll">
            <el-icon><Money /></el-icon>
          </div>
          <div class="stat-info">
            <h3>{{ formatCurrency(stats.masseSalariale) }}</h3>
            <p>Masse salariale mensuelle mois antérieur</p>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- Graphiques et tableaux -->
    <div class="dashboard-content">
      <div class="dashboard-row">
        <!-- Graphique des effectifs par direction -->
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <h3>Effectifs par Direction</h3>
              <el-button type="text" @click="refreshData">
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>
          <div class="chart-container">
            <canvas ref="effectifsChart"></canvas>
          </div>
        </el-card>
        
        <!-- Dernières activités -->
        <el-card class="activity-card">
          <template #header>
            <h3>Activités Récentes</h3>
          </template>
          <div class="activity-list">
            <div
              v-for="activity in recentActivities"
              :key="activity.id"
              class="activity-item"
            >
              <div class="activity-icon" :class="activity.type">
                <el-icon>
                  <component :is="getActivityIcon(activity.type)" />
                </el-icon>
              </div>
              <div class="activity-content">
                <p class="activity-title">{{ activity.title }}</p>
                <p class="activity-time">{{ formatTime(activity.createdAt) }}</p>
              </div>
            </div>
          </div>
        </el-card>
      </div>
      
      <div class="dashboard-row">
        <!-- Contrats arrivant à échéance -->
        <el-card class="contracts-card">
          <template #header>
            <div class="card-header">
              <h3>Contrats arrivant à échéance</h3>
              <el-tag type="warning">{{ contratsEcheance.length }}</el-tag>
            </div>
          </template>
          <el-table :data="contratsEcheance" height="300" style="width: 100%">
            <el-table-column prop="employe" label="Employé" />
            <el-table-column prop="poste" label="Poste" />
            <el-table-column prop="dateFin" label="Date fin">
              <template #default="scope">
                <el-tag type="warning" size="small">
                  {{ formatDate(scope.row.dateFin) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="joursRestants" label="Jours restants">
              <template #default="scope">
                <el-tag
                  :type="scope.row.joursRestants <= 7 ? 'danger' : 'warning'"
                  size="small"
                >
                  {{ scope.row.joursRestants }} jours
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        
        <!-- Absences en attente -->
        <el-card class="absences-card">
          <template #header>
            <div class="card-header">
              <h3>Absences en attente</h3>
              <el-tag type="info">{{ absencesEnAttente.length }}</el-tag>
            </div>
          </template>
          <el-table :data="absencesEnAttente" style="width: 100%">
            <el-table-column prop="employe" label="Employé" />
            <el-table-column prop="type" label="Type" />
            <el-table-column prop="dateDebut" label="Début">
              <template #default="scope">
                {{ formatDate(scope.row.dateDebut) }}
              </template>
            </el-table-column>
            <el-table-column prop="nombreJours" label="Jours" />
            <el-table-column label="Actions">
              <template #default="scope">
                <el-button
                  type="success"
                  size="small"
                  @click="approuverAbsence(scope.row)"
                >
                  Approuver
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="refuserAbsence(scope.row)"
                >
                  Refuser
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import {
  User,
  UserFilled,
  Document,
  Money,
  Refresh,
  Plus,
  Edit,
  Calendar
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { Chart, registerables } from 'chart.js'
import { api } from '@/services/api'
import { personnelRestService } from '@/services/personnel.service'
import { contratPersonnelService } from '@/services/contrat-personnel.service'

Chart.register(...registerables)

const authStore = useAuthStore()

// Références
const effectifsChart = ref<HTMLCanvasElement>()
const effectifsChartInstance = ref<Chart | null>(null)

// Données mockées
const stats = ref({
  totalEmployes: 0,
  employesActifs: 0,
  contratsExpirant: 0,
  masseSalariale: 0
})

const effectifsParDirection = ref<{ libelle: string; effectif: number }[]>([])

 const recentActivities = ref<any[]>([])

const contratsEcheance = ref<any[]>([])

const absencesEnAttente = ref([
  {
    id: 1,
    employe: 'Albert Einstein',
    type: 'Congé annuel',
    dateDebut: new Date(Date.now() + 1000 * 60 * 60 * 24 * 7),
    nombreJours: 10
  },
  {
    id: 2,
    employe: 'Isaac Newton',
    type: 'Maladie',
    dateDebut: new Date(Date.now() + 1000 * 60 * 60 * 24 * 2),
    nombreJours: 3
  }
])

// Méthodes
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XOF'
  }).format(amount)
}

const formatDate = (date: Date) => {
  return date.toLocaleDateString('fr-FR')
}

const formatTime = (date: Date) => {
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const hours = Math.floor(diff / (1000 * 60 * 60))
  
  if (hours < 1) {
    const minutes = Math.floor(diff / (1000 * 60))
    return `Il y a ${minutes} minute${minutes > 1 ? 's' : ''}`
  } else if (hours < 24) {
    return `Il y a ${hours} heure${hours > 1 ? 's' : ''}`
  } else {
    const days = Math.floor(hours / 24)
    return `Il y a ${days} jour${days > 1 ? 's' : ''}`
  }
}

const getActivityIcon = (type: string) => {
  switch (type) {
    case 'create': return Plus
    case 'edit': return Edit
    case 'absence': return Calendar
    case 'payroll': return Money
    default: return Document
  }
}

const toNumber = (value: any) => {
  const parsed = Number(value ?? 0)
  return Number.isFinite(parsed) ? parsed : 0
}

const formatDateParam = (date: Date) => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const parseDate = (value: any) => {
  if (!value) return null
  if (value instanceof Date) return value
  if (typeof value === 'string' && value.includes('/')) {
    const [day, month, year] = value.split('/')
    return new Date(Number(year), Number(month) - 1, Number(day))
  }
  return new Date(value)
}

const getLibelle = (value: any) => {
  if (!value) return 'Non renseigné'
  return typeof value === 'string' ? value : value.libelle || 'Non renseigné'
}

const getEmployeName = (personnel: any) => {
  if (!personnel) return 'Non renseigné'
  return `${personnel.nom || ''} ${personnel.prenom || ''}`.trim() || 'Non renseigné'
}

const getDaysUntil = (date: Date) => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  const target = new Date(date)
  target.setHours(0, 0, 0, 0)
  return Math.ceil((target.getTime() - today.getTime()) / (1000 * 60 * 60 * 24))
}

const getCurrentMonthRange = () => {
  const now = new Date()
  const start = new Date(now.getFullYear(), now.getMonth(), 1)
  const end = new Date(now.getFullYear(), now.getMonth() + 1, 0)
  return {
    start: formatDateParam(start),
    end: formatDateParam(end)
  }
}

const getUpcomingContractRange = () => {
  const start = new Date()
  const end = new Date()
  start.setFullYear(start.getFullYear() - 10)
  return {
    start: formatDateParam(start),
    end: formatDateParam(end)
  }
}

const loadMasseSalarialeMoisAnterieur = async () => {
  const response = await api.get('/paie/bulletin/masse-salariale-mois-anterieur')
  stats.value.masseSalariale = toNumber(response.data?.masseSalariale)
}

const loadEffectifsParDirection = async () => {
  const response = await api.get('/personnels/effectifs-par-direction')
  effectifsParDirection.value = Array.isArray(response.data)
    ? response.data.map((row: any) => ({
      libelle: row.libelle || 'Non renseigné',
      effectif: toNumber(row.effectif)
    }))
    : []
  await nextTick()
  initChart()
}

const loadRecentActivities = async () => {
  const response = await api.get('/personnels/dashboard/activites-recentes')
  recentActivities.value = Array.isArray(response.data)
    ? response.data.map((activity: any) => ({
      ...activity,
      createdAt: activity.createdAt ? new Date(activity.createdAt) : new Date()
    }))
    : []
}

const loadContratsEcheance = async () => {
  const range = getUpcomingContractRange()
  const response = await contratPersonnelService.getContratsWithFilters({
    offset: 0,
    limit: 1000,
    expirePeriodStart: range.start,
    expirePeriodEnd: range.end
  })
  const rows = response.rows || response.data || []
  contratsEcheance.value = rows
    .map((contrat: any) => {
      const dateFin = parseDate(contrat.dateFin)
      if (!dateFin || Number.isNaN(dateFin.getTime())) return null
      const joursRestants = getDaysUntil(dateFin)
      if (contrat.statut !== true || joursRestants > 0) return null
      return {
        id: contrat.id,
        employe: getEmployeName(contrat.personnel),
        poste: getLibelle(contrat.fonction),
        dateFin,
        joursRestants
      }
    })
    .filter(Boolean)
    .sort((a: any, b: any) => a.dateFin.getTime() - b.dateFin.getTime())
}

const refreshData = async () => {
  try {
    const currentMonthRange = getCurrentMonthRange()
    const [personnels, contratsActifs, contratsExpirant] = await Promise.all([
      personnelRestService.getPersonnels({ page: 0, size: 1 }),
      contratPersonnelService.getContratsActifs({ offset: 0, limit: 1 }),
      contratPersonnelService.getContratsWithFilters({
        offset: 0,
        limit: 1,
        expirePeriodStart: currentMonthRange.start,
        expirePeriodEnd: currentMonthRange.end
      })
    ])

    stats.value.totalEmployes = Number(personnels.total ?? personnels.data?.length ?? 0)
    stats.value.employesActifs = Number(contratsActifs.total ?? contratsActifs.rows?.length ?? 0)
    stats.value.contratsExpirant = Number(contratsExpirant.total ?? contratsExpirant.rows?.length ?? 0)
    await loadMasseSalarialeMoisAnterieur()
    await loadEffectifsParDirection()
    await loadRecentActivities()
    await loadContratsEcheance()
    ElMessage.success('Données actualisées')
  } catch (error) {
    console.error('Erreur chargement dashboard:', error)
    ElMessage.error('Erreur lors du chargement du tableau de bord')
  }
}

const approuverAbsence = (absence: any) => {
  ElMessage.success(`Absence approuvée: ${absence.employe}`)
  // TODO: Appeler l'API
}

const refuserAbsence = (absence: any) => {
  ElMessage.info(`Absence refusée: ${absence.employe}`)
  // TODO: Appeler l'API
}

const initChart = () => {
  if (!effectifsChart.value) return
  
  const ctx = effectifsChart.value.getContext('2d')
  if (!ctx) return

  if (effectifsChartInstance.value) {
    effectifsChartInstance.value.destroy()
  }

  const labels = effectifsParDirection.value.map(item => item.libelle)
  const values = effectifsParDirection.value.map(item => item.effectif)
  
  effectifsChartInstance.value = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: labels.length ? labels : ['Aucune direction'],
      datasets: [{
        data: values.length ? values : [1],
        backgroundColor: [
          '#409EFF',
          '#67C23A',
          '#E6A23C',
          '#F56C6C',
          '#909399'
        ],
        borderWidth: 0
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'bottom'
        }
      }
    }
  })
}

onMounted(() => {
  refreshData()
})
</script>

<style scoped>
.dashboard {
  width: 100%;
  padding: 0 20px;
}

.dashboard-header {
  margin-bottom: 30px;
  width: 100%;
}

.dashboard-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 2rem;
  font-weight: 600;
}

.dashboard-header p {
  margin: 0;
  color: #606266;
  font-size: 1rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  width: 100%;
  margin-bottom: 30px;
}

.stat-card {
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.stat-icon.employees {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.active {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.contracts {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.payroll {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info h3 {
  margin: 0 0 4px 0;
  font-size: 1.5rem;
  font-weight: 600;
  color: #303133;
}

.stat-info p {
  margin: 0;
  color: #909399;
  font-size: 0.875rem;
}

.dashboard-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.dashboard-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.chart-card,
.activity-card,
.contracts-card,
.absences-card {
  border: none;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  color: #303133;
  font-size: 1.1rem;
  font-weight: 600;
}

.chart-container {
  height: 300px;
  position: relative;
}

.activity-list {
  max-height: 300px;
  overflow-y: auto;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: white;
}

.activity-icon.create {
  background: #67c23a;
}

.activity-icon.edit {
  background: #e6a23c;
}

.activity-icon.absence {
  background: #409eff;
}

.activity-icon.payroll {
  background: #f56c6c;
}

.activity-content {
  flex: 1;
}

.activity-title {
  margin: 0 0 4px 0;
  color: #303133;
  font-size: 0.875rem;
  font-weight: 500;
}

.activity-time {
  margin: 0;
  color: #909399;
  font-size: 0.75rem;
}

@media (max-width: 1200px) {
  .dashboard-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .dashboard-header h1 {
    font-size: 1.5rem;
  }
}
</style>
