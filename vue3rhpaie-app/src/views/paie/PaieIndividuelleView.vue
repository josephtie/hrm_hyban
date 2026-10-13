<template>
  <div class="paie-individuelle-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><User /></el-icon>
            Paie Individuelle
          </h1>
          <p class="page-subtitle">Génération des bulletins pour un ou plusieurs personnels sélectionnés</p>
        </div>
        <div class="header-right">
          <el-button
            type="success"
            class="enhanced-button"
            :disabled="selectedPersonnel.size === 0"
            :loading="generating"
            @click="confirmGenerate"
          >
            <el-icon><Money /></el-icon>
            Générer la paie ({{ selectedPersonnel.size }})
          </el-button>
        </div>
      </div>
    </div>

    <!-- Filtres -->
    <div class="filters-section enhanced-card">
      <div class="filters-header">
        <h3>Période de Paie</h3>
        <div class="period-info">
          <el-tag type="primary" size="large">{{ selectedPeriodLabel }}</el-tag>
        </div>
      </div>
      <div class="filters-content">
        <el-form inline>
          <el-form-item label="Période">
            <el-select v-model="filters.period" placeholder="Sélectionner la période" style="width: 220px">
              <el-option
                v-for="period in periods"
                :key="period.id"
                :label="periodLabel(period)"
                :value="period.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="filters.search"
              placeholder="Rechercher (matricule, nom, prénom)..."
              clearable
              style="width: 320px"
              @keyup.enter="loadPersonnel(1)"
              @clear="loadPersonnel(1)"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadPersonnel(1)">
              <el-icon><Search /></el-icon>
              Rechercher
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- Table personnel -->
    <div class="enhanced-card">
      <div class="table-toolbar">
        <h3>Personnel actif</h3>
        <div class="selection-info" v-if="selectedPersonnel.size > 0">
          <el-tag type="success">{{ selectedPersonnel.size }} sélectionné(s)</el-tag>
          <el-button link type="danger" @click="clearSelection">Tout désélectionner</el-button>
        </div>
      </div>
      <el-table
        ref="personnelTable"
        :data="personnel"
        stripe
        v-loading="loading"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" reserve-selection />
        <el-table-column label="Matricule" prop="matricule" width="120" />
        <el-table-column label="Nom & Prénoms" prop="nomComplet" min-width="200">
          <template #default="{ row }">
            <div class="personnel-info">
              <div class="personnel-name">{{ row.nomComplet }}</div>
              <el-tag :type="row.enSommeil ? 'danger' : 'success'" size="small">
                {{ row.enSommeil ? 'Sommeil' : 'Actif' }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Fonction/Emploi" prop="fonction" min-width="160" />
        <el-table-column label="Salaire Catégoriel" prop="salaireCategoriel" width="150" align="right">
          <template #default="{ row }">{{ formatCurrency(row.salaireCategoriel) }}</template>
        </el-table-column>
        <el-table-column label="Net à Payer" prop="netAPayer" width="140" align="right">
          <template #default="{ row }">
            <span class="net-amount">{{ formatCurrency(row.netAPayer) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadPersonnel(1)"
          @current-change="loadPersonnel"
        />
      </div>
    </div>

    <!-- Résultat de la génération -->
    <div class="enhanced-card" v-if="generatedRows.length > 0">
      <div class="table-toolbar">
        <h3>Bulletins générés</h3>
        <el-tag type="success">{{ generatedRows.length }} bulletin(s)</el-tag>
      </div>
      <el-table :data="generatedRows" stripe>
        <el-table-column label="Matricule" prop="matricule" width="120" />
        <el-table-column label="Nom & Prénoms" prop="nomPrenom" min-width="180" />
        <el-table-column label="Salaire de base" width="140" align="right">
          <template #default="{ row }">{{ formatCurrency(row.salaireBase) }}</template>
        </el-table-column>
        <el-table-column label="Sursalaire" width="130" align="right">
          <template #default="{ row }">{{ formatCurrency(row.sursalaire) }}</template>
        </el-table-column>
        <el-table-column label="Brut imposable" width="150" align="right">
          <template #default="{ row }">{{ formatCurrency(row.brutImposable) }}</template>
        </el-table-column>
        <el-table-column label="Total brut" width="140" align="right">
          <template #default="{ row }">{{ formatCurrency(row.totalBrut) }}</template>
        </el-table-column>
        <el-table-column label="Net à payer" width="150" align="right">
          <template #default="{ row }">
            <span class="net-amount">{{ formatCurrency(row.netPayer) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="190" fixed="right" align="center">
          <template #default="{ row }">
            <el-button size="small" @click="openDetail(row)">
              <el-icon><View /></el-icon>
              Détail
            </el-button>
            <el-button
              size="small"
              type="primary"
              :disabled="!row.bullpaie?.id"
              :loading="pdfLoadingId === row.bullpaie?.id"
              @click="previewPdf(row)"
            >
              <el-icon><Printer /></el-icon>
              PDF
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Dialogue détail bulletin -->
    <el-dialog
      v-model="detailVisible"
      :title="`Bulletin de paie — ${detailRow?.nomPrenom || ''}`"
      width="900px"
      top="3vh"
    >
      <BulletinDetail
        :bull="detailBull"
        :matricule="detailRow?.matricule"
        :nom-prenom="detailRow?.nomPrenom"
      />
      <template #footer>
        <el-button @click="detailVisible = false">Fermer</el-button>
        <el-button
          type="primary"
          :disabled="!detailBull?.id"
          :loading="pdfLoadingId === detailBull?.id"
          @click="previewPdf(detailRow)"
        >
          <el-icon><Printer /></el-icon>
          Aperçu PDF
        </el-button>
      </template>
    </el-dialog>

    <!-- Dialogue aperçu PDF -->
    <el-dialog
      v-model="pdfVisible"
      title="Aperçu du bulletin"
      width="900px"
      top="2vh"
      destroy-on-close
      @closed="closePdf"
    >
      <div class="pdf-container" v-loading="!!pdfLoadingId">
        <iframe v-if="pdfUrl" :src="pdfUrl" class="pdf-frame" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElTable } from 'element-plus'
import { User, Money, Search, View, Printer } from '@element-plus/icons-vue'
import BulletinDetail from '@/components/paie/BulletinDetail.vue'
import livrePaieService from '@/services/livrepaie.service'
import type { LivrePaiePersonnel, PeriodePaieRef } from '@/services/livrepaie.service'

const loading = ref(false)
const generating = ref(false)
const personnel = ref<LivrePaiePersonnel[]>([])
const periods = ref<PeriodePaieRef[]>([])
const generatedRows = ref<any[]>([])
// reserve-selection + row-key : Element Plus conserve la sélection entre les pages
const selectedRows = ref<LivrePaiePersonnel[]>([])
const selectedPersonnel = computed(() => new Set(selectedRows.value.map(r => r.id)))
const personnelTable = ref<InstanceType<typeof ElTable>>()

const filters = reactive({
  period: null as number | null,
  search: ''
})

const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

const selectedPeriodLabel = computed(() => {
  const p = periods.value.find(p => p.id === filters.period)
  return p ? periodLabel(p) : 'Période active'
})

const periodLabel = (p: PeriodePaieRef) => {
  return p.libelle || `Période ${p.id}`
}

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

const loadPersonnel = async (page?: number) => {
  if (page) pagination.page = page
  loading.value = true
  try {
    const res = await livrePaieService.loadPersonnel({
      page: pagination.page,
      size: pagination.size,
      search: filters.search
    })
    if (res.success) {
      personnel.value = res.data
      pagination.total = res.total ?? res.data.length
    } else {
      ElMessage.error(res.message || 'Erreur de chargement du personnel')
    }
  } finally {
    loading.value = false
  }
}

const handleSelectionChange = (rows: LivrePaiePersonnel[]) => {
  selectedRows.value = rows
}

const clearSelection = () => {
  selectedRows.value = []
  personnelTable.value?.clearSelection()
}

// ---------- Détail bulletin & aperçu PDF ----------
const detailVisible = ref(false)
const detailRow = ref<any>(null)
const detailBull = computed(() => detailRow.value?.bullpaie || null)

const pdfVisible = ref(false)
const pdfUrl = ref<string | null>(null)
const pdfLoadingId = ref<number | null>(null)

const openDetail = (row: any) => {
  detailRow.value = row
  detailVisible.value = true
}

const previewPdf = async (row?: any) => {
  const id = row?.bullpaie?.id ?? detailBull.value?.id
  if (!id) {
    ElMessage.warning('Bulletin non disponible pour cet employé')
    return
  }
  pdfLoadingId.value = id
  try {
    const blob = await livrePaieService.printBulletinPdf(id)
    if (!blob) {
      ElMessage.error("Impossible de générer l'aperçu PDF du bulletin")
      return
    }
    if (pdfUrl.value) URL.revokeObjectURL(pdfUrl.value)
    pdfUrl.value = URL.createObjectURL(blob)
    pdfVisible.value = true
  } catch (error) {
    console.error('Erreur aperçu PDF:', error)
    ElMessage.error("Impossible de générer l'aperçu PDF du bulletin")
  } finally {
    pdfLoadingId.value = null
  }
}

const closePdf = () => {
  if (pdfUrl.value) {
    URL.revokeObjectURL(pdfUrl.value)
    pdfUrl.value = null
  }
}

const confirmGenerate = async () => {
  if (!filters.period) {
    ElMessage.warning('Veuillez sélectionner une période')
    return
  }
  try {
    await ElMessageBox.confirm(
      `Générer les bulletins de paie pour ${selectedPersonnel.value.size} personnel(s) sur la période « ${selectedPeriodLabel.value} » ?`,
      'Confirmation',
      { confirmButtonText: 'Générer', cancelButtonText: 'Annuler', type: 'warning' }
    )
  } catch {
    return
  }
  await generate()
}

const generate = async () => {
  generating.value = true
  try {
    const res = await livrePaieService.generateBulletinsSelection(
      filters.period,
      Array.from(selectedPersonnel.value)
    )
    if (res.success) {
      generatedRows.value = res.data?.rows || []
      ElMessage.success(res.message || 'Bulletins générés avec succès')
    } else {
      ElMessage.error(res.message || 'Échec de la génération')
    }
  } finally {
    generating.value = false
  }
}

onMounted(async () => {
  await loadPeriods()
  await loadPersonnel(1)
})
</script>

<style scoped>
.paie-individuelle-view {
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

.filters-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.filters-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.table-toolbar h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.selection-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.personnel-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.personnel-name {
  font-weight: 500;
}

.net-amount {
  font-weight: 600;
  color: #67c23a;
}

.pagination-container {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}

.pdf-container {
  height: 75vh;
}

.pdf-frame {
  width: 100%;
  height: 100%;
  border: none;
}
</style>
