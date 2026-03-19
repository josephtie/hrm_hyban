<template>
  <div class="bulletins-view">
    <div class="page-header">
      <h1>Bulletins de paie</h1>
      <p>Génération et consultation des bulletins de paie</p>
    </div>

    <el-row :gutter="24">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #e7f5ff; color: #1890ff;">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">312</div>
              <div class="stat-label">Bulletins générés</div>
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
              <div class="stat-number">298</div>
              <div class="stat-label">Bulletins validés</div>
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
              <div class="stat-number">14</div>
              <div class="stat-label">En attente</div>
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
              <div class="stat-label">Erreurs</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 24px;">
      <template #header>
        <div class="card-header">
          <span>Liste des bulletins</span>
          <div class="header-actions">
            <el-select v-model="selectedPeriod" placeholder="Période" style="width: 150px; margin-right: 12px;">
              <el-option label="Janvier 2024" value="2024-01" />
              <el-option label="Février 2024" value="2024-02" />
              <el-option label="Mars 2024" value="2024-03" />
              <el-option label="Avril 2024" value="2024-04" />
            </el-select>
            <el-select v-model="selectedStatus" placeholder="Statut" style="width: 120px; margin-right: 12px;">
              <el-option label="Tous" value="" />
              <el-option label="Validé" value="validé" />
              <el-option label="En attente" value="en attente" />
              <el-option label="Erreur" value="erreur" />
            </el-select>
            <el-button type="primary" @click="generateBulletins">
              <el-icon><Plus /></el-icon>
              Générer bulletins
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="filteredBulletins" style="width: 100%" v-loading="loading">
        <el-table-column prop="employee" label="Employé" />
        <el-table-column prop="matricule" label="Matricule" />
        <el-table-column prop="period" label="Période" />
        <el-table-column prop="brutSalary" label="Salaire brut">
          <template #default="scope">
            {{ formatCurrency(scope.row.brutSalary) }}
          </template>
        </el-table-column>
        <el-table-column prop="netSalary" label="Salaire net">
          <template #default="scope">
            {{ formatCurrency(scope.row.netSalary) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Statut">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="generationDate" label="Date génération" />
        <el-table-column label="Actions" width="200">
          <template #default="scope">
            <el-button size="small" type="primary" @click="viewBulletin(scope.row)">
              Voir
            </el-button>
            <el-button size="small" type="success" @click="downloadBulletin(scope.row)">
              Télécharger
            </el-button>
            <el-button 
              v-if="scope.row.status === 'En attente'" 
              size="small" 
              type="warning" 
              @click="validateBulletin(scope.row)"
            >
              Valider
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalBulletins"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- Détails du bulletin -->
    <el-dialog v-model="bulletinDialogVisible" title="Détails du bulletin de paie" width="900px">
      <div v-if="selectedBulletin" class="bulletin-details">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Employé">
            {{ selectedBulletin.employee }}
          </el-descriptions-item>
          <el-descriptions-item label="Matricule">
            {{ selectedBulletin.matricule }}
          </el-descriptions-item>
          <el-descriptions-item label="Période">
            {{ selectedBulletin.period }}
          </el-descriptions-item>
          <el-descriptions-item label="Date génération">
            {{ selectedBulletin.generationDate }}
          </el-descriptions-item>
        </el-descriptions>

        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>Éléments de salaire</span>
              </template>
              <el-table :data="salaryElements" size="small">
                <el-table-column prop="label" label="Libellé" />
                <el-table-column prop="amount" label="Montant">
                  <template #default="scope">
                    {{ formatCurrency(scope.row.amount) }}
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card>
              <template #header>
                <span>Cotisations</span>
              </template>
              <el-table :data="deductions" size="small">
                <el-table-column prop="label" label="Libellé" />
                <el-table-column prop="amount" label="Montant">
                  <template #default="scope">
                    {{ formatCurrency(scope.row.amount) }}
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>

        <el-card style="margin-top: 24px;">
          <template #header>
            <span>Récapitulatif</span>
          </template>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="Salaire brut">
              {{ formatCurrency(selectedBulletin.brutSalary) }}
            </el-descriptions-item>
            <el-descriptions-item label="Total cotisations">
              {{ formatCurrency(totalDeductions) }}
            </el-descriptions-item>
            <el-descriptions-item label="Salaire net">
              <strong>{{ formatCurrency(selectedBulletin.netSalary) }}</strong>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document, Check, Clock, Warning, Plus } from '@element-plus/icons-vue'

interface Bulletin {
  id: number
  employee: string
  matricule: string
  period: string
  brutSalary: number
  netSalary: number
  status: string
  generationDate: string
}

interface SalaryElement {
  label: string
  amount: number
}

const loading = ref(false)
const selectedPeriod = ref('2024-03')
const selectedStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const totalBulletins = ref(312)

const bulletinDialogVisible = ref(false)
const selectedBulletin = ref<Bulletin | null>(null)

const bulletins = ref<Bulletin[]>([
  {
    id: 1,
    employee: 'Martin Dupont',
    matricule: 'EMP001',
    period: 'Mars 2024',
    brutSalary: 3500,
    netSalary: 2800,
    status: 'Validé',
    generationDate: '31/03/2024'
  },
  {
    id: 2,
    employee: 'Sophie Martin',
    matricule: 'EMP002',
    period: 'Mars 2024',
    brutSalary: 4200,
    netSalary: 3360,
    status: 'Validé',
    generationDate: '31/03/2024'
  },
  {
    id: 3,
    employee: 'Thomas Bernard',
    matricule: 'EMP003',
    period: 'Mars 2024',
    brutSalary: 2800,
    netSalary: 2240,
    status: 'En attente',
    generationDate: '31/03/2024'
  },
  {
    id: 4,
    employee: 'Marie Petit',
    matricule: 'EMP004',
    period: 'Mars 2024',
    brutSalary: 3100,
    netSalary: 2480,
    status: 'Erreur',
    generationDate: '31/03/2024'
  },
])

const salaryElements = ref<SalaryElement[]>([
  { label: 'Salaire de base', amount: 3500 },
  { label: 'Prime ancienneté', amount: 150 },
  { label: 'Heures supplémentaires', amount: 200 },
  { label: 'Avantage en nature', amount: 100 },
])

const deductions = ref<SalaryElement[]>([
  { label: 'CSG déductible', amount: 280 },
  { label: 'CSG non déductible', amount: 140 },
  { label: 'CRDS', amount: 35 },
  { label: 'Sécurité sociale', amount: 315 },
  { label: 'Retraite complémentaire', amount: 180 },
])

const filteredBulletins = computed(() => {
  let filtered = bulletins.value

  if (selectedPeriod.value) {
    filtered = filtered.filter(b => b.period.includes(selectedPeriod.value))
  }

  if (selectedStatus.value) {
    filtered = filtered.filter(b => b.status.toLowerCase() === selectedStatus.value.toLowerCase())
  }

  return filtered
})

const totalDeductions = computed(() => {
  return deductions.value.reduce((total, deduction) => total + deduction.amount, 0)
})

const getStatusType = (status: string) => {
  switch (status) {
    case 'Validé': return 'success'
    case 'En attente': return 'warning'
    case 'Erreur': return 'danger'
    default: return 'info'
  }
}

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR'
  }).format(amount)
}

const generateBulletins = () => {
  loading.value = true
  ElMessage.info('Génération des bulletins en cours...')
  
  setTimeout(() => {
    loading.value = false
    ElMessage.success('Bulletins générés avec succès')
  }, 2000)
}

const viewBulletin = (bulletin: Bulletin) => {
  selectedBulletin.value = bulletin
  bulletinDialogVisible.value = true
}

const downloadBulletin = (bulletin: Bulletin) => {
  ElMessage.success(`Téléchargement du bulletin de ${bulletin.employee}...`)
}

const validateBulletin = (bulletin: Bulletin) => {
  ElMessageBox.confirm(
    `Valider le bulletin de ${bulletin.employee} pour la période ${bulletin.period}?`,
    'Validation',
    {
      confirmButtonText: 'Valider',
      cancelButtonText: 'Annuler',
      type: 'info'
    }
  ).then(() => {
    bulletin.status = 'Validé'
    ElMessage.success('Bulletin validé avec succès')
  })
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
}
</script>

<style scoped>
.bulletins-view {
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

.header-actions {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.bulletin-details {
  max-height: 600px;
  overflow-y: auto;
}
</style>
