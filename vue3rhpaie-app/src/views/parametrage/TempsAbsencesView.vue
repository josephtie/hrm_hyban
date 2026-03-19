<template>
  <div class="temps-absences-view">
    <div class="page-header">
      <h1>Temps & Absences</h1>
      <p>Gestion des congés, absences et temps de travail</p>
    </div>

    <el-row :gutter="24">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #e7f5ff; color: #1890ff;">
              <el-icon><Calendar /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">24</div>
              <div class="stat-label">Congés en attente</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #f6ffed; color: #52c41a;">
              <el-icon><Check /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">156</div>
              <div class="stat-label">Congés validés</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #fff2e8; color: #fa8c16;">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">8</div>
              <div class="stat-label">Absences ce mois</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #fff1f0; color: #ff4d4f;">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">3</div>
              <div class="stat-label">Alertes</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" style="margin-top: 24px;">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>Demandes récentes</span>
              <el-button type="primary" size="small">Nouvelle demande</el-button>
            </div>
          </template>
          <el-table :data="recentRequests" style="width: 100%">
            <el-table-column prop="employee" label="Employé" />
            <el-table-column prop="type" label="Type" />
            <el-table-column prop="startDate" label="Date début" />
            <el-table-column prop="endDate" label="Date fin" />
            <el-table-column prop="status" label="Statut">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="Actions">
              <template #default="scope">
                <el-button size="small" type="primary">Voir</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card>
          <template #header>
            <span>Types de congés</span>
          </template>
          <div class="leave-types">
            <div class="leave-type" v-for="type in leaveTypes" :key="type.name">
              <div class="leave-type-info">
                <div class="leave-type-name">{{ type.name }}</div>
                <div class="leave-type-days">{{ type.days }} jours/an</div>
              </div>
              <el-progress :percentage="type.used" :color="getProgressColor(type.used)" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 24px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>Configuration du temps de travail</span>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="Horaires" name="hours">
              <el-form :model="workHours" label-width="200px">
                <el-form-item label="Heures de travail par jour">
                  <el-input-number v-model="workHours.daily" :min="1" :max="12" />
                </el-form-item>
                <el-form-item label="Jours de travail par semaine">
                  <el-input-number v-model="workHours.weekly" :min="1" :max="7" />
                </el-form-item>
                <el-form-item label="Pause déjeuner">
                  <el-input-number v-model="workHours.lunchBreak" :min="0" :max="120" /> minutes
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="Congés" name="leaves">
              <el-form :model="leaveConfig" label-width="200px">
                <el-form-item label="Congés payés par an">
                  <el-input-number v-model="leaveConfig.paid" :min="0" :max="50" /> jours
                </el-form-item>
                <el-form-item label="RTT par an">
                  <el-input-number v-model="leaveConfig.rtt" :min="0" :max="20" /> jours
                </el-form-item>
                <el-form-item label="Congés exceptionnels">
                  <el-input-number v-model="leaveConfig.exceptional" :min="0" :max="10" /> jours
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Calendar, Check, Clock, Warning } from '@element-plus/icons-vue'

interface Request {
  employee: string
  type: string
  startDate: string
  endDate: string
  status: string
}

interface LeaveType {
  name: string
  days: number
  used: number
}

const recentRequests = ref<Request[]>([
  { employee: 'Martin Dupont', type: 'Congés payés', startDate: '15/01/2024', endDate: '19/01/2024', status: 'En attente' },
  { employee: 'Sophie Martin', type: 'RTT', startDate: '10/01/2024', endDate: '10/01/2024', status: 'Validé' },
  { employee: 'Thomas Bernard', type: 'Maladie', startDate: '08/01/2024', endDate: '09/01/2024', status: 'Validé' },
  { employee: 'Marie Petit', type: 'Congés payés', startDate: '22/01/2024', endDate: '26/01/2024', status: 'En attente' },
])

const leaveTypes = ref<LeaveType[]>([
  { name: 'Congés payés', days: 25, used: 80 },
  { name: 'RTT', days: 12, used: 60 },
  { name: 'Congés exceptionnels', days: 6, used: 30 },
])

const workHours = ref({
  daily: 8,
  weekly: 5,
  lunchBreak: 60
})

const leaveConfig = ref({
  paid: 25,
  rtt: 12,
  exceptional: 6
})

const activeTab = ref('hours')

const getStatusType = (status: string) => {
  switch (status) {
    case 'Validé': return 'success'
    case 'En attente': return 'warning'
    case 'Refusé': return 'danger'
    default: return 'info'
  }
}

const getProgressColor = (percentage: number) => {
  if (percentage >= 80) return '#ff4d4f'
  if (percentage >= 60) return '#fa8c16'
  return '#52c41a'
}
</script>

<style scoped>
.temps-absences-view {
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

.stats-card {
  margin-bottom: 24px;
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 20px;
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.leave-types {
  space-y: 16px;
}

.leave-type {
  margin-bottom: 16px;
}

.leave-type-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.leave-type-name {
  font-weight: 500;
  color: #303133;
}

.leave-type-days {
  color: #909399;
  font-size: 14px;
}
</style>
