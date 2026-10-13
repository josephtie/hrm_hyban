<template>
  <div class="simulation-paie-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><Odometer /></el-icon>
            Simulation de salaire
          </h1>
          <p class="page-subtitle">
            Calcul à blanc du bulletin à partir de rubriques choisies — aucune donnée n'est enregistrée
          </p>
        </div>
        <div class="header-right">
          <el-button
            type="success"
            class="enhanced-button"
            :disabled="!canSimulate"
            :loading="simulating"
            @click="simulate"
          >
            <el-icon><MagicStick /></el-icon>
            Simuler
          </el-button>
        </div>
      </div>
    </div>

    <!-- Sélection -->
    <div class="filters-section enhanced-card">
      <div class="filters-header">
        <h3>Période & Personnel</h3>
        <el-tag type="primary" size="large">{{ selectedPeriodLabel }}</el-tag>
      </div>
      <div class="filters-content">
        <el-form inline>
          <el-form-item label="Période">
            <el-select
              v-model="filters.period"
              placeholder="Sélectionner la période"
              style="width: 220px"
              @change="onPeriodChange"
            >
              <el-option
                v-for="period in periods"
                :key="period.id"
                :label="periodLabel(period)"
                :value="period.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="Personnel">
            <el-select
              v-model="filters.personnel"
              placeholder="Rechercher un personnel..."
              filterable
              remote
              :remote-method="searchPersonnel"
              :loading="personnelLoading"
              style="width: 360px"
              @change="onPersonnelChange"
            >
              <el-option
                v-for="p in personnelOptions"
                :key="p.id"
                :label="`${p.matricule} — ${p.nomComplet}`"
                :value="p.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :disabled="!filters.period || !selectedPersonnel"
              :loading="rubriquesLoading"
              @click="loadRubriques"
            >
              <el-icon><Search /></el-icon>
              Charger les rubriques
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-alert
        v-if="selectedPersonnel"
        :title="`Salaire catégoriel : ${formatCurrency(selectedPersonnel.salaireCategoriel)} FCFA — simulez avec les paramètres ci-dessous et/ou les rubriques (optionnelles). Aucune donnée n'est enregistrée.`"
        type="info"
        :closable="false"
        show-icon
        class="personnel-alert"
      />

      <!-- Paramètres de simulation (vides = valeurs du contrat/personnel) -->
      <template v-if="selectedPersonnel">
        <el-divider content-position="left">Paramètres simulés</el-divider>
        <el-form inline label-position="top" class="params-form">
          <el-form-item label="Catégorie professionnelle">
            <el-select
              v-model="params.idCategorie"
              placeholder="Catégorie du contrat"
              clearable
              filterable
              style="width: 240px"
              @change="onCategorieChange"
            >
              <el-option
                v-for="c in categories"
                :key="c.id"
                :label="`${c.libelle} (${formatCurrency(c.salaireDeBase)})`"
                :value="c.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="Salaire de base">
            <el-input-number v-model="params.salaireBase" :min="0" :controls="false" style="width: 150px" placeholder="Contrat" />
          </el-form-item>
          <el-form-item label="Sursalaire">
            <el-input-number v-model="params.sursalaire" :min="0" :controls="false" style="width: 140px" placeholder="Contrat" />
          </el-form-item>
          <el-form-item label="Indemnité transport">
            <el-input-number v-model="params.indemniteTransport" :min="0" :controls="false" style="width: 140px" placeholder="Contrat" />
          </el-form-item>
          <el-form-item label="Situation matrimoniale">
            <el-select v-model="params.situationMatrimoniale" style="width: 160px" placeholder="Personnel">
              <el-option label="Marié(e)" :value="1" />
              <el-option label="Célibataire" :value="2" />
              <el-option label="Divorcé(e)" :value="3" />
              <el-option label="Veuf/Veuve" :value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="Nb enfants">
            <el-input-number v-model="params.nombreEnfants" :min="0" :max="20" style="width: 100px" placeholder="Personnel" />
          </el-form-item>
          <el-form-item label="Net à payer cible">
            <el-input-number v-model="params.netCible" :min="0" :controls="false" style="width: 160px" placeholder="Objectif" />
          </el-form-item>
        </el-form>
      </template>
    </div>

    <!-- Rubriques éditables -->
    <div class="enhanced-card" v-if="rubriqueGroups.length > 0" v-loading="rubriquesLoading">
      <div class="table-toolbar">
        <h3>Rubriques de la simulation</h3>
        <el-tag>{{ includedCount }} rubrique(s) incluse(s)</el-tag>
      </div>
      <el-tabs v-model="activeTab">
        <el-tab-pane
          v-for="group in rubriqueGroups"
          :key="group.key"
          :label="`${group.title} (${group.items.filter(i => i.included).length})`"
          :name="group.key"
        >
          <el-table :data="group.items" size="small" stripe max-height="380">
            <el-table-column width="60" align="center">
              <template #default="{ row }">
                <el-checkbox v-model="row.included" />
              </template>
            </el-table-column>
            <el-table-column label="Code" prop="code" width="100" />
            <el-table-column label="Rubrique" prop="libelle" min-width="260" />
            <el-table-column label="Montant" width="200" align="right">
              <template #default="{ row }">
                <el-input-number
                  v-model="row.montant"
                  :min="0"
                  :controls="false"
                  :disabled="!row.included"
                  style="width: 160px"
                  placeholder="0"
                />
              </template>
            </el-table-column>
            <el-table-column label="Actuel" width="140" align="right">
              <template #default="{ row }">
                <span :class="{ 'current-amount': row.currentMontant > 0 }">
                  {{ formatCurrency(row.currentMontant) }}
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- Résultat -->
    <div class="enhanced-card" v-if="resultRow">
      <div class="table-toolbar">
        <h3>Résultat de la simulation — {{ resultRow.nomPrenom }}</h3>
        <el-tag type="warning">Bulletin simulé (non enregistré)</el-tag>
      </div>

      <div class="sim-summary">
        <div class="sim-item">
          <span class="sim-label">Brut imposable</span>
          <span class="sim-value">{{ formatCurrency(resultBull?.brutImposable) }}</span>
        </div>
        <div class="sim-item">
          <span class="sim-label">Total brut</span>
          <span class="sim-value">{{ formatCurrency(resultBull?.totalbrut) }}</span>
        </div>
        <div class="sim-item sursalaire" v-if="params.netCible">
          <span class="sim-label">Sursalaire calculé</span>
          <span class="sim-value">{{ formatCurrency(resultBull?.sursalaire) }}</span>
        </div>
        <div class="sim-item">
          <span class="sim-label">Total retenues</span>
          <span class="sim-value retenue">{{ formatCurrency(resultBull?.totalretenue) }}</span>
        </div>
        <div class="sim-item">
          <span class="sim-label">Charges patronales</span>
          <span class="sim-value">{{ formatCurrency(resultBull?.totalpatronal) }}</span>
        </div>
        <div class="sim-item highlight">
          <span class="sim-label">Net à payer</span>
          <span class="sim-value">{{ formatCurrency(resultBull?.netapayer) }}</span>
        </div>
        <div class="sim-item ecart" v-if="params.netCible">
          <span class="sim-label">Écart vs net cible</span>
          <span class="sim-value" :class="ecartNetCible >= 0 ? 'positif' : 'negatif'">
            {{ ecartNetCible >= 0 ? '+' : '' }}{{ formatCurrency(ecartNetCible) }}
          </span>
        </div>
      </div>

      <BulletinDetail
        :bull="resultBull"
        :matricule="resultRow.matricule"
        :nom-prenom="resultRow.nomPrenom"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Odometer, MagicStick, Search } from '@element-plus/icons-vue'
import BulletinDetail from '@/components/paie/BulletinDetail.vue'
import livrePaieService, { situationMatriToCode } from '@/services/livrepaie.service'
import type {
  LivrePaiePersonnel,
  PeriodePaieRef,
  RubriqueMontant,
  RubriquesContratPeriode,
  CategorieRef
} from '@/services/livrepaie.service'

interface RubriqueItem extends RubriqueMontant {
  included: boolean
  currentMontant: number
}

interface RubriqueGroup {
  key: string
  title: string
  items: RubriqueItem[]
}

const periods = ref<PeriodePaieRef[]>([])
const personnelOptions = ref<LivrePaiePersonnel[]>([])
const selectedPersonnel = ref<LivrePaiePersonnel | null>(null)
const rubriqueGroups = ref<RubriqueGroup[]>([])
const resultRow = ref<any>(null)
const categories = ref<CategorieRef[]>([])

// Paramètres simulés (null/vide = valeur réelle du contrat ou du personnel)
const params = reactive({
  idCategorie: null as number | null,
  salaireBase: null as number | null,
  sursalaire: null as number | null,
  indemniteTransport: null as number | null,
  situationMatrimoniale: null as number | null,
  nombreEnfants: null as number | null,
  netCible: null as number | null
})

const personnelLoading = ref(false)
const rubriquesLoading = ref(false)
const simulating = ref(false)
const activeTab = ref('imp')

const filters = reactive({
  period: null as number | null,
  personnel: null as number | null
})

const resultBull = computed(() => resultRow.value?.bullpaie || null)

const ecartNetCible = computed(() => {
  if (!resultBull.value || !params.netCible) return 0
  return Number(resultBull.value.netapayer ?? 0) - Number(params.netCible)
})

const includedCount = computed(() =>
  rubriqueGroups.value.reduce(
    (n, g) => n + g.items.filter(i => i.included).length,
    0
  )
)

const canSimulate = computed(
  () => !!filters.period && !!filters.personnel && !simulating.value
)

const selectedPeriodLabel = computed(() => {
  const p = periods.value.find(p => p.id === filters.period)
  return p ? periodLabel(p) : 'Période active'
})

const periodLabel = (p: PeriodePaieRef) => p.libelle || `Période ${p.id}`

const formatCurrency = (value: number | null | undefined) => {
  return new Intl.NumberFormat('fr-FR').format(Number(value ?? 0))
}

const loadPeriods = async () => {
  const res = await livrePaieService.loadPeriodes()
  if (res.success) {
    periods.value = res.data
    const active = await livrePaieService.getPeriodeActive()
    filters.period = active.data?.id ?? periods.value[0]?.id ?? null
  }
}

const searchPersonnel = async (query: string) => {
  personnelLoading.value = true
  try {
    const res = await livrePaieService.loadPersonnel({ page: 1, size: 30, search: query })
    if (res.success) {
      personnelOptions.value = res.data
    }
  } finally {
    personnelLoading.value = false
  }
}

const onPersonnelChange = (id: number | null) => {
  selectedPersonnel.value = personnelOptions.value.find(p => p.id === id) || null
  rubriqueGroups.value = []
  resultRow.value = null
  // Charger automatiquement les rubriques réelles du contrat (modifiables ensuite)
  if (selectedPersonnel.value?.contratId && filters.period) {
    loadRubriques()
  }
  // Pré-remplir les paramètres avec les valeurs réelles du personnel
  const p = selectedPersonnel.value
  params.salaireBase = p?.salaireCategoriel ?? null
  params.situationMatrimoniale = situationMatriToCode(p?.situationMatrimoniale) || null
  params.nombreEnfants = p?.nbEnfants ?? null
}

const onCategorieChange = (idCategorie: number | null) => {
  const cat = categories.value.find(c => c.id === idCategorie)
  if (cat) {
    params.salaireBase = Number(cat.salaireDeBase ?? 0)
  }
}

const onPeriodChange = () => {
  if (selectedPersonnel.value?.contratId && filters.period) {
    loadRubriques()
  }
}

const loadCategories = async () => {
  const res = await livrePaieService.loadCategories()
  if (res.success) {
    categories.value = res.data || []
  }
}

const toItems = (list?: RubriqueMontant[]): RubriqueItem[] =>
  (list || []).map(r => ({
    ...r,
    montant: Number(r.montant ?? 0),
    included: Number(r.montant ?? 0) > 0 || !!r.primePersonnelId,
    currentMontant: Number(r.montant ?? 0)
  }))

const loadRubriques = async () => {
  if (!filters.period || !selectedPersonnel.value?.contratId) {
    ElMessage.warning('Sélectionnez une période et un personnel avec contrat actif')
    return
  }
  rubriquesLoading.value = true
  resultRow.value = null
  try {
    const res = await livrePaieService.getRubriquesContratPeriode(
      filters.period,
      selectedPersonnel.value.contratId
    )
    if (!res.success || !res.data) {
      ElMessage.error(res.message || 'Erreur de chargement des rubriques')
      return
    }
    const d = res.data as RubriquesContratPeriode
    rubriqueGroups.value = [
      { key: 'imp', title: 'Imposables', items: toItems(d.listePrimesImp) },
      { key: 'impNonImp', title: 'Imposables & Non imposables', items: toItems(d.listePrimesImposetNon) },
      { key: 'nonImp', title: 'Non imposables', items: toItems(d.listePrimesNonImpos) },
      { key: 'gains', title: 'Gains nets', items: toItems(d.listePrimesGains) },
      { key: 'mutuelle', title: 'Retenues mutuelle', items: toItems(d.listePrimesMutuelle) },
      { key: 'sociale', title: 'Retenues sociales', items: toItems(d.listePrimesSociale) }
    ].filter(g => g.items.length > 0)
    activeTab.value = rubriqueGroups.value[0]?.key || 'imp'
  } finally {
    rubriquesLoading.value = false
  }
}

const simulate = async () => {
  if (!canSimulate.value) return
  simulating.value = true
  try {
    // null = rubriques jamais chargées → le backend garde les primes réelles du contrat
    const rubriques = rubriqueGroups.value.length
      ? rubriqueGroups.value
          .flatMap(g => g.items)
          .filter(i => i.included)
          .map(i => ({ rubriqueId: i.rubriqueId, montant: Number(i.montant ?? 0), valeur: i.valeur ?? 0 }))
      : null

    const res = await livrePaieService.simulerBulletin({
      idPeriode: filters.period,
      idPersonnel: filters.personnel!,
      idCategorie: params.idCategorie,
      salaireBase: params.salaireBase,
      sursalaire: params.sursalaire,
      indemniteTransport: params.indemniteTransport,
      situationMatrimoniale: params.situationMatrimoniale,
      nombreEnfants: params.nombreEnfants,
      netCible: params.netCible,
      rubriques
    })
    if (res.success && res.data?.rows?.length) {
      resultRow.value = res.data.rows[0]
      ElMessage.success('Simulation terminée — bulletin calculé sans enregistrement')
    } else {
      ElMessage.error(res.message || 'Aucun bulletin calculé — vérifiez la période et le personnel')
    }
  } finally {
    simulating.value = false
  }
}

onMounted(async () => {
  await loadPeriods()
  await loadCategories()
  await searchPersonnel('')
})
</script>

<style scoped>
.simulation-paie-view {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-header {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  margin: 0;
  font-size: 24px;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 10px;
}

.page-subtitle {
  margin: 5px 0 0;
  color: #909399;
  font-size: 14px;
}

.enhanced-card {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.filters-header,
.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.filters-header h3,
.table-toolbar h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.personnel-alert {
  margin-top: 8px;
}

.current-amount {
  color: #909399;
}

.sim-summary {
  display: flex;
  gap: 20px;
  justify-content: space-around;
  flex-wrap: wrap;
  background: #f5f7fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 10px;
}

.sim-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.sim-item.highlight {
  background: #f0f9eb;
  border: 1px solid #c2e7b0;
  border-radius: 8px;
  padding: 8px 20px;
}

.sim-label {
  font-size: 12px;
  color: #909399;
  text-transform: uppercase;
}

.sim-value {
  font-size: 18px;
  font-weight: 700;
  color: #303133;
}

.sim-value.retenue {
  color: #f56c6c;
}

.sim-item.highlight .sim-value {
  color: #67c23a;
  font-size: 22px;
}

.sim-item.sursalaire {
  background: #ecf5ff;
  border: 1px solid #b3d8ff;
  border-radius: 8px;
  padding: 8px 20px;
}

.sim-item.ecart {
  background: #fdf6ec;
  border: 1px solid #f5dab1;
  border-radius: 8px;
  padding: 8px 20px;
}

.sim-value.positif {
  color: #67c23a;
}

.sim-value.negatif {
  color: #f56c6c;
}

.params-form :deep(.el-form-item) {
  margin-bottom: 8px;
}
</style>
