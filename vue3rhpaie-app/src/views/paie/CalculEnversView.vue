<template>
  <div class="calcul-envers-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><Switch /></el-icon>
            Calcul à l'envers
          </h1>
          <p class="page-subtitle">Détermine le sursalaire nécessaire pour atteindre un net à payer cible</p>
        </div>
      </div>
    </div>

    <!-- Paramètres -->
    <div class="enhanced-card">
      <h3 class="section-title">Paramètres du calcul</h3>
      <el-form inline label-position="top">
        <el-form-item label="Personnel">
          <el-select
            v-model="selectedPersonnelId"
            filterable
            remote
            clearable
            :remote-method="searchPersonnel"
            :loading="searching"
            placeholder="Rechercher un personnel (nom, matricule)..."
            style="width: 380px"
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
        <el-form-item label="Net à payer cible">
          <el-input-number
            v-model="netCible"
            :min="0"
            :step="1000"
            :controls="false"
            style="width: 200px"
            placeholder="Montant net"
          />
        </el-form-item>
        <el-form-item label=" ">
          <el-button
            type="primary"
            :disabled="!selectedPersonnelId || !netCible"
            :loading="calculating"
            @click="compute"
          >
            <el-icon><Switch /></el-icon>
            Calculer
          </el-button>
        </el-form-item>
      </el-form>
      <div v-if="currentPersonnel" class="personnel-summary">
        <el-tag>Net actuel du contrat : {{ formatCurrency(currentPersonnel.netAPayer) }}</el-tag>
        <el-tag type="info">Salaire catégoriel : {{ formatCurrency(currentPersonnel.salaireCategoriel) }}</el-tag>
        <el-tag type="info">{{ currentPersonnel.fonction || '—' }}</el-tag>
      </div>
    </div>

    <!-- Résultat -->
    <div class="enhanced-card" v-if="result">
      <div class="result-header">
        <h3 class="section-title">Résultat du calcul</h3>
        <el-tag :type="converged ? 'success' : 'warning'" size="large">
          {{ converged ? 'Convergence atteinte' : 'Net approximatif' }}
        </el-tag>
      </div>

      <el-descriptions :column="3" border class="result-descriptions">
        <el-descriptions-item label="Sursalaire à appliquer">
          <span class="highlight-amount">{{ formatCurrency(result.sursalaire) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="Brut imposable">
          {{ formatCurrency(result.brutImposable) }}
        </el-descriptions-item>
        <el-descriptions-item label="Brut non imposable">
          {{ formatCurrency(result.brutNonImposable) }}
        </el-descriptions-item>
        <el-descriptions-item label="ITS">{{ formatCurrency(result.its) }}</el-descriptions-item>
        <el-descriptions-item label="CN">{{ formatCurrency(result.cn) }}</el-descriptions-item>
        <el-descriptions-item label="IGR">{{ formatCurrency(result.igr) }}</el-descriptions-item>
        <el-descriptions-item label="CNPS">{{ formatCurrency(result.cnps) }}</el-descriptions-item>
        <el-descriptions-item label="Total retenues">
          {{ formatCurrency(result.totalRetenue) }}
        </el-descriptions-item>
        <el-descriptions-item label="Net calculé">
          <span class="net-amount">{{ formatCurrency(result.netPayer) }}</span>
        </el-descriptions-item>
      </el-descriptions>

      <div class="convergence-info">
        <span>Net cible : {{ formatCurrency(netCible) }}</span>
        <span>Écart : {{ formatCurrency(ecartNet) }}</span>
      </div>

      <div class="result-actions">
        <el-button
          type="success"
          :loading="applying"
          @click="applySursalaire"
        >
          <el-icon><Check /></el-icon>
          Appliquer le sursalaire au contrat
        </el-button>
        <el-alert
          type="info"
          :closable="false"
          show-icon
          title="L'application met à jour le sursalaire du contrat actif de ce personnel. Il sera utilisé lors de la prochaine génération de paie."
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Switch, Check } from '@element-plus/icons-vue'
import livrePaieService from '@/services/livrepaie.service'
import type { LivrePaiePersonnel } from '@/services/livrepaie.service'

const searching = ref(false)
const calculating = ref(false)
const applying = ref(false)

const personnelOptions = ref<LivrePaiePersonnel[]>([])
const selectedPersonnelId = ref<number | null>(null)
const currentPersonnel = ref<LivrePaiePersonnel | null>(null)
const netCible = ref<number | null>(null)
const result = ref<any | null>(null)

const ecartNet = computed(() => {
  if (!result.value || netCible.value == null) return 0
  return Math.abs(Number(result.value.netPayer ?? 0) - netCible.value)
})

const converged = computed(() => ecartNet.value <= 1)

const formatCurrency = (value: number | null | undefined) => {
  return new Intl.NumberFormat('fr-FR').format(Number(value ?? 0))
}

const searchPersonnel = async (query: string) => {
  searching.value = true
  try {
    const res = await livrePaieService.loadPersonnel({ page: 1, size: 20, search: query || '' })
    if (res.success) {
      personnelOptions.value = res.data
    }
  } finally {
    searching.value = false
  }
}

const onPersonnelChange = (id: number | null) => {
  currentPersonnel.value = personnelOptions.value.find(p => p.id === id) || null
  result.value = null
  if (currentPersonnel.value?.netAPayer) {
    netCible.value = Math.round(currentPersonnel.value.netAPayer)
  }
}

const compute = async () => {
  if (!selectedPersonnelId.value || netCible.value == null) return
  calculating.value = true
  result.value = null
  try {
    const res = await livrePaieService.calculEnversIndividuel(selectedPersonnelId.value, netCible.value)
    if (res.success && res.data) {
      result.value = res.data
      if (!converged.value) {
        ElMessage.warning('Convergence partielle : le net obtenu diffère légèrement de la cible')
      }
    } else {
      ElMessage.error(res.message || 'Échec du calcul à l\'envers')
    }
  } finally {
    calculating.value = false
  }
}

const applySursalaire = async () => {
  if (!selectedPersonnelId.value || !result.value) return
  const sursalaire = Number(result.value.sursalaire ?? 0)
  try {
    await ElMessageBox.confirm(
      `Appliquer un sursalaire de ${formatCurrency(sursalaire)} au contrat de ${currentPersonnel.value?.nomComplet || 'ce personnel'} ?`,
      'Confirmation',
      { confirmButtonText: 'Appliquer', cancelButtonText: 'Annuler', type: 'warning' }
    )
  } catch {
    return
  }
  applying.value = true
  try {
    const res = await livrePaieService.updateContratSursalaire(selectedPersonnelId.value, sursalaire)
    if (res.success) {
      ElMessage.success('Sursalaire appliqué au contrat avec succès')
    } else {
      ElMessage.error(res.message || 'Échec de l\'application du sursalaire')
    }
  } finally {
    applying.value = false
  }
}
</script>

<style scoped>
.calcul-envers-view {
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

.section-title {
  margin: 0 0 15px;
  font-size: 16px;
  color: #303133;
}

.personnel-summary {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.result-header .section-title {
  margin: 0;
}

.highlight-amount {
  font-weight: 700;
  color: #e6a23c;
  font-size: 16px;
}

.net-amount {
  font-weight: 700;
  color: #67c23a;
  font-size: 16px;
}

.convergence-info {
  display: flex;
  gap: 30px;
  margin: 15px 0;
  color: #606266;
  font-size: 14px;
}

.result-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: flex-start;
}

.result-actions .el-alert {
  width: 100%;
}
</style>
