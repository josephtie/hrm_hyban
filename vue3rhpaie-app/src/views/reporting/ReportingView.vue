<template>
  <div class="reporting-view">
    <div class="page-header">
      <h1>Reporting & Tableaux de bord</h1>
      <p>Analyses et rapports sur les données RH et de paie</p>
    </div>

    <el-row :gutter="24">
      <el-col :span="6">
        <el-card class="kpi-card">
          <div class="kpi-content">
            <div class="kpi-icon" style="background: #e7f5ff; color: #1890ff;">
              <el-icon><User /></el-icon>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">156</div>
              <div class="kpi-label">Effectif total</div>
              <div class="kpi-trend">
                <el-icon><CaretTop /></el-icon>
                <span>+5% ce mois</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="kpi-card">
          <div class="kpi-content">
            <div class="kpi-icon" style="background: #f6ffed; color: #52c41a;">
              <el-icon><Money /></el-icon>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">485K€</div>
              <div class="kpi-label">Masse salariale</div>
              <div class="kpi-trend">
                <el-icon><CaretTop /></el-icon>
                <span>+2.3% ce mois</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="kpi-card">
          <div class="kpi-content">
            <div class="kpi-icon" style="background: #fff2e8; color: #fa8c16;">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">95.2%</div>
              <div class="kpi-label">Taux de présence</div>
              <div class="kpi-trend">
                <el-icon><CaretBottom /></el-icon>
                <span>-0.8% ce mois</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="kpi-card">
          <div class="kpi-content">
            <div class="kpi-icon" style="background: #fff1f0; color: #ff4d4f;">
              <el-icon><SwitchButton /></el-icon>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">8.5%</div>
              <div class="kpi-label">Taux de turnover</div>
              <div class="kpi-trend">
                <el-icon><CaretBottom /></el-icon>
                <span>-1.2% ce mois</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-tabs v-model="activeTab" style="margin-top: 24px;">
      <el-tab-pane label="Tableau de bord" name="dashboard">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>Évolution de l'effectif</span>
              </template>
              <div class="chart-container">
                <canvas ref="staffChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card>
              <template #header>
                <span>Répartition par département</span>
              </template>
              <div class="chart-container">
                <canvas ref="departmentChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>Masse salariale mensuelle</span>
              </template>
              <div class="chart-container">
                <canvas ref="salaryChart"></canvas>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card>
              <template #header>
                <span>Taux d'absentéisme</span>
              </template>
              <div class="chart-container">
                <canvas ref="absenceChart"></canvas>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="Rapports RH" name="hr-reports">
        <el-row :gutter="24">
          <el-col :span="8">
            <el-card class="report-card">
              <div class="report-content">
                <div class="report-icon" style="background: #e7f5ff; color: #1890ff;">
                  <el-icon><Document /></el-icon>
                </div>
                <div class="report-info">
                  <div class="report-title">Effectifs au 31/03</div>
                  <div class="report-desc">État des effectifs par catégorie</div>
                  <el-button type="primary" size="small">Générer</el-button>
                </div>
              </div>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card class="report-card">
              <div class="report-content">
                <div class="report-icon" style="background: #f6ffed; color: #52c41a;">
                  <el-icon><TrendCharts /></el-icon>
                </div>
                <div class="report-info">
                  <div class="report-title">Analyse turnover</div>
                  <div class="report-desc">Analyse des départs et arrivées</div>
                  <el-button type="primary" size="small">Générer</el-button>
                </div>
              </div>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card class="report-card">
              <div class="report-content">
                <div class="report-icon" style="background: #fff2e8; color: #fa8c16;">
                  <el-icon><Calendar /></el-icon>
                </div>
                <div class="report-info">
                  <div class="report-title">Planning congés</div>
                  <div class="report-desc">État des congés par service</div>
                  <el-button type="primary" size="small">Générer</el-button>
                </div>
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
                  {{ scope.row.trend === 'positive' ? '↗ Positif' : '↘ Négatif' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="Rapports de paie" name="payroll-reports">
        <el-row :gutter="24">
          <el-col :span="8">
            <el-card class="report-card">
              <div class="report-content">
                <div class="report-icon" style="background: #e7f5ff; color: #1890ff;">
                  <el-icon><Money /></el-icon>
                </div>
                <div class="report-info">
                  <div class="report-title">État des charges</div>
                  <div class="report-desc">Détail des cotisations sociales</div>
                  <el-button type="primary" size="small">Générer</el-button>
                </div>
              </div>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card class="report-card">
              <div class="report-content">
                <div class="report-icon" style="background: #f6ffed; color: #52c41a;">
                  <el-icon><PieChart /></el-icon>
                </div>
                <div class="report-info">
                  <div class="report-title">Analytique salaire</div>
                  <div class="report-desc">Répartition des salaires</div>
                  <el-button type="primary" size="small">Générer</el-button>
                </div>
              </div>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card class="report-card">
              <div class="report-content">
                <div class="report-icon" style="background: #fff2e8; color: #fa8c16;">
                  <el-icon><DataAnalysis /></el-icon>
                </div>
                <div class="report-info">
                  <div class="report-title">Coût du personnel</div>
                  <div class="report-desc">Analyse des coûts globaux</div>
                  <el-button type="primary" size="small">Générer</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-card style="margin-top: 24px;">
          <template #header>
            <span>Statistiques de paie</span>
          </template>
          <el-row :gutter="24">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">3 250€</div>
                <div class="stat-label">Salaire moyen</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">2 850€</div>
                <div class="stat-label">Salaire médian</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">42.5%</div>
                <div class="stat-label">Taux de charges</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">485K€</div>
                <div class="stat-label">Masse salariale/mois</div>
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
import { ref, onMounted } from 'vue'
import { Chart, registerables } from 'chart.js'
import { 
  User, Money, Clock, SwitchButton, CaretTop, CaretBottom,
  Document, TrendCharts, Calendar, PieChart, DataAnalysis, Download
} from '@element-plus/icons-vue'

interface SocialIndicator {
  indicator: string
  value: string
  target: string
  trend: 'positive' | 'negative'
}

interface Export {
  id: number
  name: string
  type: string
  format: string
  date: string
  status: string
}

const activeTab = ref('dashboard')

const socialIndicators = ref<SocialIndicator[]>([
  { indicator: 'Taux de turnover', value: '8.5%', target: '< 10%', trend: 'positive' },
  { indicator: 'Taux d\'absentéisme', value: '4.8%', target: '< 5%', trend: 'positive' },
  { indicator: 'Taux de satisfaction', value: '78%', target: '> 80%', trend: 'negative' },
  { indicator: 'Taux de formation', value: '65%', target: '> 70%', trend: 'negative' },
])

const exports = ref<Export[]>([
  { id: 1, name: 'Effectifs Mars 2024', type: 'RH', format: 'Excel', date: '01/04/2024', status: 'Terminé' },
  { id: 2, name: 'Paie Q1 2024', type: 'Paie', format: 'PDF', date: '05/04/2024', status: 'En cours' },
  { id: 3, name: 'Analytique Annuel 2023', type: 'Analytique', format: 'CSV', date: '15/01/2024', status: 'Terminé' },
])

Chart.register(...registerables)

onMounted(() => {
  initCharts()
})

const initCharts = () => {
  // Chart.js sera initialisé ici
  // Pour l'instant, nous utilisons des placeholders
}

const getExportStatusType = (status: string) => {
  switch (status) {
    case 'Terminé': return 'success'
    case 'En cours': return 'warning'
    case 'Erreur': return 'danger'
    default: return 'info'
  }
}

const deleteExport = (exportItem: Export) => {
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
  color: #52c41a;
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
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
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
