<template>
  <div class="paie-view">
    <div class="page-header">
      <h1>Paramétrage Paie</h1>
      <p>Configuration des périodes de paie et exercices</p>
    </div>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="Périodes de paie" name="periods">
        <el-row :gutter="24">
          <el-col :span="16">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>Périodes de paie</span>
                  <el-button type="primary" size="small" @click="openPeriodDialog">
                    Nouvelle période
                  </el-button>
                </div>
              </template>
              <el-table :data="payPeriods" style="width: 100%">
                <el-table-column prop="name" label="Période" />
                <el-table-column prop="startDate" label="Date début" />
                <el-table-column prop="endDate" label="Date fin" />
                <el-table-column prop="status" label="Statut">
                  <template #default="scope">
                    <el-tag :type="getPeriodStatusType(scope.row.status)">
                      {{ scope.row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="Actions">
                  <template #default="scope">
                    <el-button 
                      v-if="scope.row.status === 'Ouvert'" 
                      size="small" 
                      type="warning" 
                      @click="closePeriod(scope.row)"
                    >
                      Clôturer
                    </el-button>
                    <el-button 
                      v-if="scope.row.status === 'Clôturé'" 
                      size="small" 
                      type="success" 
                      @click="reopenPeriod(scope.row)"
                    >
                      Rouvrir
                    </el-button>
                    <el-button size="small" type="primary">Détails</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card>
              <template #header>
                <span>Actions rapides</span>
              </template>
              <div class="quick-actions">
                <el-button type="primary" style="width: 100%; margin-bottom: 12px;">
                  Générer périodes mensuelles
                </el-button>
                <el-button type="success" style="width: 100%; margin-bottom: 12px;">
                  Clôturer toutes les périodes ouvertes
                </el-button>
                <el-button type="warning" style="width: 100%;">
                  Exporter calendrier
                </el-button>
              </div>
            </el-card>

            <el-card style="margin-top: 24px;">
              <template #header>
                <span>Statistiques</span>
              </template>
              <div class="stats">
                <div class="stat-item">
                  <div class="stat-number">{{ openPeriods }}</div>
                  <div class="stat-label">Périodes ouvertes</div>
                </div>
                <div class="stat-item">
                  <div class="stat-number">{{ closedPeriods }}</div>
                  <div class="stat-label">Périodes clôturées</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="Exercices de paie" name="exercises">
        <el-row :gutter="24">
          <el-col :span="16">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>Exercices de paie</span>
                  <el-button type="primary" size="small" @click="openExerciseDialog">
                    Nouvel exercice
                  </el-button>
                </div>
              </template>
              <el-table :data="payExercises" style="width: 100%">
                <el-table-column prop="name" label="Exercice" />
                <el-table-column prop="startDate" label="Date début" />
                <el-table-column prop="endDate" label="Date fin" />
                <el-table-column prop="status" label="Statut">
                  <template #default="scope">
                    <el-tag :type="getExerciseStatusType(scope.row.status)">
                      {{ scope.row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="periods" label="Périodes" />
                <el-table-column label="Actions">
                  <template #default="scope">
                    <el-button size="small" type="primary">Détails</el-button>
                    <el-button 
                      v-if="scope.row.status === 'Actif'" 
                      size="small" 
                      type="warning"
                      @click="closeExercise(scope.row)"
                    >
                      Clôturer
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card>
              <template #header>
                <span>Configuration</span>
              </template>
              <el-form :model="exerciseConfig" label-width="120px">
                <el-form-item label="Type">
                  <el-select v-model="exerciseConfig.type" style="width: 100%">
                    <el-option label="Annuel" value="annual" />
                    <el-option label="Semestriel" value="semiannual" />
                    <el-option label="Trimestriel" value="quarterly" />
                  </el-select>
                </el-form-item>
                <el-form-item label="Mois début">
                  <el-select v-model="exerciseConfig.startMonth" style="width: 100%">
                    <el-option label="Janvier" value="1" />
                    <el-option label="Février" value="2" />
                    <el-option label="Mars" value="3" />
                    <el-option label="Avril" value="4" />
                    <el-option label="Mai" value="5" />
                    <el-option label="Juin" value="6" />
                    <el-option label="Juillet" value="7" />
                    <el-option label="Août" value="8" />
                    <el-option label="Septembre" value="9" />
                    <el-option label="Octobre" value="10" />
                    <el-option label="Novembre" value="11" />
                    <el-option label="Décembre" value="12" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="generateExercise">
                    Générer exercice
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="Fonctions employés" name="functions">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>Fonctions des employés</span>
                  <el-button type="primary" size="small" @click="openFunctionDialog">
                    Nouvelle fonction
                  </el-button>
                </div>
              </template>
              <el-table :data="employeeFunctions" style="width: 100%">
                <el-table-column prop="name" label="Fonction" />
                <el-table-column prop="category" label="Catégorie" />
                <el-table-column prop="level" label="Niveau" />
                <el-table-column prop="salary" label="Salaire de base" />
                <el-table-column label="Actions">
                  <template #default="scope">
                    <el-button size="small" type="primary">Modifier</el-button>
                    <el-button size="small" type="danger">Supprimer</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card>
              <template #header>
                <span>Grille salariale</span>
              </template>
              <el-table :data="salaryGrid" style="width: 100%">
                <el-table-column prop="level" label="Niveau" />
                <el-table-column prop="minSalary" label="Salaire min" />
                <el-table-column prop="maxSalary" label="Salaire max" />
                <el-table-column prop="coefficient" label="Coefficient" />
              </el-table>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

    <!-- Dialogues -->
    <el-dialog v-model="periodDialogVisible" title="Nouvelle période de paie" width="500px">
      <el-form :model="newPeriod" label-width="120px">
        <el-form-item label="Nom">
          <el-input v-model="newPeriod.name" />
        </el-form-item>
        <el-form-item label="Date début">
          <el-date-picker v-model="newPeriod.startDate" type="date" style="width: 100%" />
        </el-form-item>
        <el-form-item label="Date fin">
          <el-date-picker v-model="newPeriod.endDate" type="date" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="periodDialogVisible = false">Annuler</el-button>
        <el-button type="primary" @click="savePeriod">Enregistrer</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="exerciseDialogVisible" title="Nouvel exercice de paie" width="500px">
      <el-form :model="newExercise" label-width="120px">
        <el-form-item label="Nom">
          <el-input v-model="newExercise.name" />
        </el-form-item>
        <el-form-item label="Date début">
          <el-date-picker v-model="newExercise.startDate" type="date" style="width: 100%" />
        </el-form-item>
        <el-form-item label="Date fin">
          <el-date-picker v-model="newExercise.endDate" type="date" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="exerciseDialogVisible = false">Annuler</el-button>
        <el-button type="primary" @click="saveExercise">Enregistrer</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface PayPeriod {
  id: number
  name: string
  startDate: string
  endDate: string
  status: string
}

interface PayExercise {
  id: number
  name: string
  startDate: string
  endDate: string
  status: string
  periods: number
}

interface EmployeeFunction {
  id: number
  name: string
  category: string
  level: string
  salary: string
}

interface SalaryGrid {
  level: string
  minSalary: string
  maxSalary: string
  coefficient: number
}

const activeTab = ref('periods')

const payPeriods = ref<PayPeriod[]>([
  { id: 1, name: 'Janvier 2024', startDate: '01/01/2024', endDate: '31/01/2024', status: 'Clôturé' },
  { id: 2, name: 'Février 2024', startDate: '01/02/2024', endDate: '29/02/2024', status: 'Clôturé' },
  { id: 3, name: 'Mars 2024', startDate: '01/03/2024', endDate: '31/03/2024', status: 'Ouvert' },
  { id: 4, name: 'Avril 2024', startDate: '01/04/2024', endDate: '30/04/2024', status: 'Ouvert' },
])

const payExercises = ref<PayExercise[]>([
  { id: 1, name: 'Exercice 2023', startDate: '01/01/2023', endDate: '31/12/2023', status: 'Clôturé', periods: 12 },
  { id: 2, name: 'Exercice 2024', startDate: '01/01/2024', endDate: '31/12/2024', status: 'Actif', periods: 12 },
])

const employeeFunctions = ref<EmployeeFunction[]>([
  { id: 1, name: 'Développeur Senior', category: 'Technique', level: 'A3', salary: '45 000 €' },
  { id: 2, name: 'Chef de projet', category: 'Management', level: 'B2', salary: '52 000 €' },
  { id: 3, name: 'Comptable', category: 'Administratif', level: 'B1', salary: '38 000 €' },
])

const salaryGrid = ref<SalaryGrid[]>([
  { level: 'A1', minSalary: '25 000 €', maxSalary: '30 000 €', coefficient: 120 },
  { level: 'A2', minSalary: '30 000 €', maxSalary: '38 000 €', coefficient: 140 },
  { level: 'A3', minSalary: '38 000 €', maxSalary: '48 000 €', coefficient: 160 },
  { level: 'B1', minSalary: '35 000 €', maxSalary: '42 000 €', coefficient: 150 },
  { level: 'B2', minSalary: '42 000 €', maxSalary: '55 000 €', coefficient: 180 },
])

const exerciseConfig = ref({
  type: 'annual',
  startMonth: '1'
})

const periodDialogVisible = ref(false)
const exerciseDialogVisible = ref(false)

const newPeriod = ref({
  name: '',
  startDate: '',
  endDate: ''
})

const newExercise = ref({
  name: '',
  startDate: '',
  endDate: ''
})

const openPeriods = computed(() => payPeriods.value.filter(p => p.status === 'Ouvert').length)
const closedPeriods = computed(() => payPeriods.value.filter(p => p.status === 'Clôturé').length)

const getPeriodStatusType = (status: string) => {
  switch (status) {
    case 'Ouvert': return 'success'
    case 'Clôturé': return 'info'
    default: return 'warning'
  }
}

const getExerciseStatusType = (status: string) => {
  switch (status) {
    case 'Actif': return 'success'
    case 'Clôturé': return 'info'
    default: return 'warning'
  }
}

const openPeriodDialog = () => {
  periodDialogVisible.value = true
}

const openExerciseDialog = () => {
  exerciseDialogVisible.value = true
}

const openFunctionDialog = () => {
  // Implémentation à ajouter
}

const savePeriod = () => {
  // Implémentation à ajouter
  periodDialogVisible.value = false
}

const saveExercise = () => {
  // Implémentation à ajouter
  exerciseDialogVisible.value = false
}

const closePeriod = (period: PayPeriod) => {
  period.status = 'Clôturé'
}

const reopenPeriod = (period: PayPeriod) => {
  period.status = 'Ouvert'
}

const closeExercise = (exercise: PayExercise) => {
  exercise.status = 'Clôturé'
}

const generateExercise = () => {
  // Implémentation à ajouter
}
</script>

<style scoped>
.paie-view {
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

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quick-actions {
  display: flex;
  flex-direction: column;
}

.stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>
