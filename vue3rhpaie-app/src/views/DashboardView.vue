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
            <p>Masse salariale mensuelle</p>
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
          <el-table :data="contratsEcheance" style="width: 100%">
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

Chart.register(...registerables)

const authStore = useAuthStore()

// Références
const effectifsChart = ref<HTMLCanvasElement>()

// Données mockées
const stats = ref({
  totalEmployes: 245,
  employesActifs: 228,
  contratsExpirant: 12,
  masseSalariale: 45678900
})

const recentActivities = ref([
  {
    id: 1,
    type: 'create',
    title: 'Nouvel employé ajouté: Jean Dupont',
    createdAt: new Date(Date.now() - 1000 * 60 * 30)
  },
  {
    id: 2,
    type: 'edit',
    title: 'Contrat renouvelé: Marie Curie',
    createdAt: new Date(Date.now() - 1000 * 60 * 60 * 2)
  },
  {
    id: 3,
    type: 'absence',
    title: 'Demande de congé: Albert Einstein',
    createdAt: new Date(Date.now() - 1000 * 60 * 60 * 4)
  },
  {
    id: 4,
    type: 'payroll',
    title: 'Bulletin généré: 45 employés',
    createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24)
  }
])

const contratsEcheance = ref([
  {
    id: 1,
    employe: 'Jean Dupont',
    poste: 'Développeur Senior',
    dateFin: new Date(Date.now() + 1000 * 60 * 60 * 24 * 15),
    joursRestants: 15
  },
  {
    id: 2,
    employe: 'Marie Curie',
    poste: 'Chercheur',
    dateFin: new Date(Date.now() + 1000 * 60 * 60 * 24 * 5),
    joursRestants: 5
  }
])

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

const refreshData = () => {
  ElMessage.success('Données actualisées')
  // TODO: Recharger les données depuis l'API
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
  
  new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['Direction IT', 'Direction RH', 'Direction Fin', 'Direction Com', 'Direction Ops'],
      datasets: [{
        data: [45, 25, 35, 40, 83],
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
  nextTick(() => {
    initChart()
  })
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
