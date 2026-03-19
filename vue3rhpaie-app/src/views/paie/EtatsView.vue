<template>
  <div class="etats-view">
    <div class="page-header">
      <h1>États de paie</h1>
      <p>Génération des états et déclarations sociales</p>
    </div>

    <el-row :gutter="24">
      <el-col :span="8">
        <el-card class="report-card">
          <div class="report-content">
            <div class="report-icon" style="background: #e7f5ff; color: #1890ff;">
              <el-icon><Document /></el-icon>
            </div>
            <div class="report-info">
              <div class="report-title">DSN</div>
              <div class="report-desc">Déclaration Sociale Nominative</div>
              <el-button type="primary" size="small" @click="generateDSN">Générer</el-button>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="report-card">
          <div class="report-content">
            <div class="report-icon" style="background: #f6ffed; color: #52c41a;">
              <el-icon><Files /></el-icon>
            </div>
            <div class="report-info">
              <div class="report-title">Livre de paie</div>
              <div class="report-desc">Registre des bulletins de paie</div>
              <el-button type="primary" size="small" @click="generateLivrePaie">Générer</el-button>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="report-card">
          <div class="report-content">
            <div class="report-icon" style="background: #fff2e8; color: #fa8c16;">
              <el-icon><PieChart /></el-icon>
            </div>
            <div class="report-info">
              <div class="report-title">État des charges</div>
              <div class="report-desc">Récapitulatif des cotisations</div>
              <el-button type="primary" size="small" @click="generateCharges">Générer</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" style="margin-top: 24px;">
      <el-col :span="8">
        <el-card class="report-card">
          <div class="report-content">
            <div class="report-icon" style="background: #f9f0ff; color: #722ed1;">
              <el-icon><Calendar /></el-icon>
            </div>
            <div class="report-info">
              <div class="report-title">État annuel</div>
              <div class="report-desc">Récapitulatif annuel des salaires</div>
              <el-button type="primary" size="small" @click="generateAnnual">Générer</el-button>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="report-card">
          <div class="report-content">
            <div class="report-icon" style="background: #fff1f0; color: #ff4d4f;">
              <el-icon><Money /></el-icon>
            </div>
            <div class="report-info">
              <div class="report-title">Déclaration fiscale</div>
              <div class="report-desc">Déclaration des salaires annuels</div>
              <el-button type="primary" size="small" @click="generateFiscal">Générer</el-button>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="report-card">
          <div class="report-content">
            <div class="report-icon" style="background: #e6f7ff; color: #13c2c2;">
              <el-icon><DataAnalysis /></el-icon>
            </div>
            <div class="report-info">
              <div class="report-title">Analytique paie</div>
              <div class="report-desc">Analyse des coûts salariaux</div>
              <el-button type="primary" size="small" @click="generateAnalytique">Générer</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 24px;">
      <template #header>
        <div class="card-header">
          <span>États générés</span>
          <div class="header-actions">
            <el-select v-model="selectedPeriod" placeholder="Période" style="width: 150px; margin-right: 12px;">
              <el-option label="Janvier 2024" value="2024-01" />
              <el-option label="Février 2024" value="2024-02" />
              <el-option label="Mars 2024" value="2024-03" />
              <el-option label="Avril 2024" value="2024-04" />
            </el-select>
            <el-select v-model="selectedType" placeholder="Type" style="width: 150px;">
              <el-option label="Tous" value="" />
              <el-option label="DSN" value="dsn" />
              <el-option label="Livre de paie" value="livre" />
              <el-option label="État des charges" value="charges" />
              <el-option label="État annuel" value="annual" />
              <el-option label="Déclaration fiscale" value="fiscal" />
              <el-option label="Analytique paie" value="analytique" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="filteredReports" style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="Nom du rapport" />
        <el-table-column prop="type" label="Type">
          <template #default="scope">
            <el-tag :type="getReportTypeColor(scope.row.type)">
              {{ scope.row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="period" label="Période" />
        <el-table-column prop="generationDate" label="Date génération" />
        <el-table-column prop="status" label="Statut">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="Taille fichier" />
        <el-table-column label="Actions" width="200">
          <template #default="scope">
            <el-button size="small" type="primary" @click="viewReport(scope.row)">
              Voir
            </el-button>
            <el-button size="small" type="success" @click="downloadReport(scope.row)">
              Télécharger
            </el-button>
            <el-button size="small" type="danger" @click="deleteReport(scope.row)">
              Supprimer
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalReports"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- Configuration de génération -->
    <el-dialog v-model="configDialogVisible" title="Configuration de génération" width="600px">
      <el-form :model="reportConfig" label-width="150px">
        <el-form-item label="Type de rapport">
          <el-input v-model="reportConfig.type" disabled />
        </el-form-item>
        <el-form-item label="Période">
          <el-date-picker
            v-model="reportConfig.period"
            type="month"
            placeholder="Sélectionner une période"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Format">
          <el-radio-group v-model="reportConfig.format">
            <el-radio label="pdf">PDF</el-radio>
            <el-radio label="excel">Excel</el-radio>
            <el-radio label="csv">CSV</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Inclure les détails">
          <el-switch v-model="reportConfig.includeDetails" />
        </el-form-item>
        <el-form-item label="Envoyer par email">
          <el-switch v-model="reportConfig.sendEmail" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="configDialogVisible = false">Annuler</el-button>
        <el-button type="primary" @click="confirmGeneration">Générer</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document, Files, PieChart, Calendar, Money, DataAnalysis } from '@element-plus/icons-vue'

interface Report {
  id: number
  name: string
  type: string
  period: string
  generationDate: string
  status: string
  fileSize: string
}

const loading = ref(false)
const selectedPeriod = ref('2024-03')
const selectedType = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const totalReports = ref(45)

const configDialogVisible = ref(false)
const currentReportType = ref('')

const reports = ref<Report[]>([
  {
    id: 1,
    name: 'DSN Mars 2024',
    type: 'DSN',
    period: 'Mars 2024',
    generationDate: '05/04/2024',
    status: 'Généré',
    fileSize: '2.3 MB'
  },
  {
    id: 2,
    name: 'Livre de paie Q1 2024',
    type: 'Livre de paie',
    period: 'Q1 2024',
    generationDate: '10/04/2024',
    status: 'Généré',
    fileSize: '5.7 MB'
  },
  {
    id: 3,
    name: 'État des charges Mars 2024',
    type: 'État des charges',
    period: 'Mars 2024',
    generationDate: '08/04/2024',
    status: 'En cours',
    fileSize: '-'
  },
  {
    id: 4,
    name: 'Déclaration fiscale 2023',
    type: 'Déclaration fiscale',
    period: '2023',
    generationDate: '15/01/2024',
    status: 'Généré',
    fileSize: '1.2 MB'
  },
])

const reportConfig = ref({
  type: '',
  period: '',
  format: 'pdf',
  includeDetails: true,
  sendEmail: false
})

const filteredReports = computed(() => {
  let filtered = reports.value

  if (selectedPeriod.value) {
    filtered = filtered.filter(r => r.period.includes(selectedPeriod.value))
  }

  if (selectedType.value) {
    filtered = filtered.filter(r => r.type.toLowerCase().includes(selectedType.value.toLowerCase()))
  }

  return filtered
})

const getReportTypeColor = (type: string) => {
  switch (type) {
    case 'DSN': return 'primary'
    case 'Livre de paie': return 'success'
    case 'État des charges': return 'warning'
    case 'État annuel': return 'info'
    case 'Déclaration fiscale': return 'danger'
    case 'Analytique paie': return ''
    default: return 'info'
  }
}

const getStatusType = (status: string) => {
  switch (status) {
    case 'Généré': return 'success'
    case 'En cours': return 'warning'
    case 'Erreur': return 'danger'
    default: return 'info'
  }
}

const openConfigDialog = (type: string) => {
  currentReportType.value = type
  reportConfig.value.type = type
  reportConfig.value.period = ''
  configDialogVisible.value = true
}

const generateDSN = () => openConfigDialog('DSN')
const generateLivrePaie = () => openConfigDialog('Livre de paie')
const generateCharges = () => openConfigDialog('État des charges')
const generateAnnual = () => openConfigDialog('État annuel')
const generateFiscal = () => openConfigDialog('Déclaration fiscale')
const generateAnalytique = () => openConfigDialog('Analytique paie')

const confirmGeneration = () => {
  loading.value = true
  configDialogVisible.value = false
  
  ElMessage.info(`Génération du ${reportConfig.value.type} en cours...`)
  
  setTimeout(() => {
    loading.value = false
    ElMessage.success(`${reportConfig.value.type} généré avec succès`)
    
    // Ajouter le nouveau rapport à la liste
    const newReport: Report = {
      id: reports.value.length + 1,
      name: `${reportConfig.value.type} ${reportConfig.value.period}`,
      type: reportConfig.value.type,
      period: reportConfig.value.period,
      generationDate: new Date().toLocaleDateString('fr-FR'),
      status: 'Généré',
      fileSize: '1.5 MB'
    }
    reports.value.unshift(newReport)
  }, 2000)
}

const viewReport = (report: Report) => {
  ElMessage.info(`Affichage du rapport: ${report.name}`)
}

const downloadReport = (report: Report) => {
  ElMessage.success(`Téléchargement de ${report.name}...`)
}

const deleteReport = (report: Report) => {
  ElMessageBox.confirm(
    `Êtes-vous sûr de vouloir supprimer ${report.name}?`,
    'Confirmation',
    {
      confirmButtonText: 'Supprimer',
      cancelButtonText: 'Annuler',
      type: 'warning'
    }
  ).then(() => {
    const index = reports.value.findIndex(r => r.id === report.id)
    if (index > -1) {
      reports.value.splice(index, 1)
      ElMessage.success('Rapport supprimé avec succès')
    }
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
.etats-view {
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
</style>
