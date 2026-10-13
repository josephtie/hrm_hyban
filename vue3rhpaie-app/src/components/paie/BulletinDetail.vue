<template>
  <div class="bulletin-detail" v-if="bull">
    <!-- Infos générales -->
    <h4 class="detail-section">Informations</h4>
    <el-descriptions :column="3" border size="small">
      <el-descriptions-item label="Matricule">{{ matricule }}</el-descriptions-item>
      <el-descriptions-item label="Nb parts">{{ bull.nombrePart }}</el-descriptions-item>
      <el-descriptions-item label="Ancienneté">{{ bull.anciennete }} an(s)</el-descriptions-item>
      <el-descriptions-item label="Jours travaillés">{{ bull.jourTravail }}</el-descriptions-item>
      <el-descriptions-item label="Temps de travail">{{ bull.temptravail }}</el-descriptions-item>
      <el-descriptions-item label="Mois/Tps présence">{{ bull.moisOfpresence }} / {{ bull.tempsOfpresence }}</el-descriptions-item>
    </el-descriptions>

    <!-- Gains -->
    <h4 class="detail-section">Gains</h4>
    <el-descriptions :column="3" border size="small">
      <el-descriptions-item label="Salaire de base">{{ formatCurrency(bull.salairbase) }}</el-descriptions-item>
      <el-descriptions-item label="Prime d'ancienneté">{{ formatCurrency(bull.primeanciennete) }}</el-descriptions-item>
      <el-descriptions-item label="Sursalaire">{{ formatCurrency(bull.sursalaire) }}</el-descriptions-item>
      <el-descriptions-item label="Indemnité logement">{{ formatCurrency(bull.indemnitelogement) }}</el-descriptions-item>
      <el-descriptions-item label="Autres indem. imposables">{{ formatCurrency(bull.autreIndemImposable) }}</el-descriptions-item>
      <el-descriptions-item label="Autres imposables">{{ formatCurrency(bull.autreImposable) }}</el-descriptions-item>
      <el-descriptions-item label="Indem. représentation">{{ formatCurrency(bull.indemniteRepresentation) }}</el-descriptions-item>
      <el-descriptions-item label="Indem. transport imp.">{{ formatCurrency(bull.indemniteTransportImp) }}</el-descriptions-item>
      <el-descriptions-item label="Indem. responsabilité">{{ formatCurrency(bull.indemniteRespons) }}</el-descriptions-item>
    </el-descriptions>
    <el-descriptions :column="1" border size="small" class="total-row">
      <el-descriptions-item label="Brut imposable">
        <strong>{{ formatCurrency(bull.brutImposable) }}</strong>
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions :column="3" border size="small">
      <el-descriptions-item label="Indem. transport">{{ formatCurrency(bull.indemniteTransport) }}</el-descriptions-item>
      <el-descriptions-item label="Autres non imposables">{{ formatCurrency(bull.autreNonImposable) }}</el-descriptions-item>
      <el-descriptions-item label="Brut non imposable">
        <strong>{{ formatCurrency(bull.brutNonImposable) }}</strong>
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions :column="1" border size="small" class="total-row">
      <el-descriptions-item label="Total brut">
        <strong>{{ formatCurrency(bull.totalbrut) }}</strong>
      </el-descriptions-item>
    </el-descriptions>

    <!-- Rubriques de gains -->
    <template v-for="section in gainSections" :key="section.title">
      <template v-if="primeList(bull[section.key]).length">
        <h4 class="detail-section">{{ section.title }}</h4>
        <el-table :data="primeList(bull[section.key])" size="small" border>
          <el-table-column label="Rubrique" min-width="220">
            <template #default="{ row }">{{ row.prime?.libelle || row.prime?.code || '—' }}</template>
          </el-table-column>
          <el-table-column label="Montant" width="150" align="right">
            <template #default="{ row }">{{ formatCurrency(row.montant) }}</template>
          </el-table-column>
        </el-table>
      </template>
    </template>

    <!-- Retenues -->
    <h4 class="detail-section">Retenues fiscales & sociales</h4>
    <el-descriptions :column="3" border size="small">
      <el-descriptions-item label="Base CNPS">{{ formatCurrency(bull.basecnps) }}</el-descriptions-item>
      <el-descriptions-item label="ITS">{{ formatCurrency(bull.its) }}</el-descriptions-item>
      <el-descriptions-item label="CN">{{ formatCurrency(bull.cn) }}</el-descriptions-item>
      <el-descriptions-item label="IGR">{{ formatCurrency(bull.igr) }}</el-descriptions-item>
      <el-descriptions-item label="Impôt salaire (IS)">{{ formatCurrency(bull.impotSalaire) }}</el-descriptions-item>
      <el-descriptions-item label="Total retenue fiscale">{{ formatCurrency(bull.totalretenuefiscal) }}</el-descriptions-item>
      <el-descriptions-item label="CNPS salarial">{{ formatCurrency(bull.cnps) }}</el-descriptions-item>
      <el-descriptions-item label="CMU salarial">{{ formatCurrency(bull.CMUSalarial) }}</el-descriptions-item>
      <el-descriptions-item label="Retenue sociale">{{ formatCurrency(bull.retenueSociiale) }}</el-descriptions-item>
      <el-descriptions-item label="Avances & acomptes">{{ formatCurrency(bull.avanceetacompte) }}</el-descriptions-item>
      <el-descriptions-item label="Prêts">{{ formatCurrency(bull.pretaloes) }}</el-descriptions-item>
      <el-descriptions-item label="CAREC">{{ formatCurrency(bull.carec) }}</el-descriptions-item>
      <el-descriptions-item label="Régularisation">{{ formatCurrency(bull.regularisation) }}</el-descriptions-item>
      <el-descriptions-item label="Autres prélèvements">{{ formatCurrency(bull.autrePrelevement) }}</el-descriptions-item>
      <el-descriptions-item label="Total retenues">
        <strong>{{ formatCurrency(bull.totalretenue) }}</strong>
      </el-descriptions-item>
    </el-descriptions>

    <!-- Retenues listes -->
    <template v-for="section in retenueSections" :key="section.title">
      <template v-if="primeList(bull[section.key]).length">
        <h4 class="detail-section">{{ section.title }}</h4>
        <el-table :data="primeList(bull[section.key])" size="small" border>
          <el-table-column label="Rubrique" min-width="220">
            <template #default="{ row }">{{ row.prime?.libelle || row.prime?.code || '—' }}</template>
          </el-table-column>
          <el-table-column label="Montant" width="150" align="right">
            <template #default="{ row }">{{ formatCurrency(row.montant) }}</template>
          </el-table-column>
        </el-table>
      </template>
    </template>

    <!-- Charges patronales -->
    <h4 class="detail-section">Charges patronales</h4>
    <el-descriptions :column="3" border size="small">
      <el-descriptions-item label="CMU patronal">{{ formatCurrency(bull.CMUPatronal) }}</el-descriptions-item>
      <el-descriptions-item label="TA">{{ formatCurrency(bull.ta) }}</el-descriptions-item>
      <el-descriptions-item label="FPC">{{ formatCurrency(bull.fpc) }}</el-descriptions-item>
      <el-descriptions-item label="FPC régul.">{{ formatCurrency(bull.fpcregul) }}</el-descriptions-item>
      <el-descriptions-item label="Prestations familiales">{{ formatCurrency(bull.prestationFamiliale) }}</el-descriptions-item>
      <el-descriptions-item label="Accident travail">{{ formatCurrency(bull.accidentTravail) }}</el-descriptions-item>
      <el-descriptions-item label="Retraite">{{ formatCurrency(bull.retraite) }}</el-descriptions-item>
      <el-descriptions-item label="IS patronal">{{ formatCurrency(bull.isPatronal) }}</el-descriptions-item>
      <el-descriptions-item label="Total patronal">
        <strong>{{ formatCurrency(bull.totalpatronal) }}</strong>
      </el-descriptions-item>
    </el-descriptions>

    <!-- Cumuls -->
    <h4 class="detail-section">Cumuls annuels</h4>
    <el-descriptions :column="3" border size="small">
      <el-descriptions-item label="Cumul ITS">{{ formatCurrency(bull.cumulIts) }}</el-descriptions-item>
      <el-descriptions-item label="Cumul CN">{{ formatCurrency(bull.cumulCn) }}</el-descriptions-item>
      <el-descriptions-item label="Cumul IGR">{{ formatCurrency(bull.cumulIgr) }}</el-descriptions-item>
      <el-descriptions-item label="Cumul CNPS sal.">{{ formatCurrency(bull.cumulCnpsSal) }}</el-descriptions-item>
      <el-descriptions-item label="Cumul salaire net">{{ formatCurrency(bull.cumulSalaireNet) }}</el-descriptions-item>
      <el-descriptions-item label="Cumul retenue net">{{ formatCurrency(bull.cumulRetenueNet) }}</el-descriptions-item>
    </el-descriptions>

    <!-- Net -->
    <div class="net-banner">
      <div class="net-item">
        <span class="net-label">Net à payer</span>
        <span class="net-value">{{ formatCurrency(bull.netapayer) }}</span>
      </div>
      <div class="net-item">
        <span class="net-label">Net régularisé</span>
        <span class="net-value">{{ formatCurrency(bull.netRegulPayer) }}</span>
      </div>
      <div class="net-item">
        <span class="net-label">Masse salariale</span>
        <span class="net-value">{{ formatCurrency(bull.totalmassesalarial) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  bull: any
  matricule?: string
  nomPrenom?: string
}>()

const gainSections = [
  { key: 'listIndemniteBrut', title: 'Primes & indemnités imposables' },
  { key: 'listIndemniteNonImp', title: 'Primes & indemnités non imposables' },
  { key: 'listGainsNet', title: 'Gains nets' }
]

const retenueSections = [
  { key: 'listRetenueSociale', title: 'Retenues sociales' },
  { key: 'listRetenueMutuellt', title: 'Retenues mutuelle' }
]

const primeList = (list?: any[]): any[] => list || []

const formatCurrency = (value: number | null | undefined) => {
  return new Intl.NumberFormat('fr-FR').format(Number(value ?? 0))
}
</script>

<style scoped>
.bulletin-detail {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-section {
  margin: 14px 0 8px;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  border-left: 3px solid #409eff;
  padding-left: 8px;
}

.total-row {
  margin: 6px 0;
}

.net-banner {
  margin-top: 18px;
  display: flex;
  gap: 24px;
  justify-content: space-around;
  background: #f0f9eb;
  border: 1px solid #c2e7b0;
  border-radius: 8px;
  padding: 16px;
}

.net-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.net-label {
  font-size: 12px;
  color: #909399;
  text-transform: uppercase;
}

.net-value {
  font-size: 20px;
  font-weight: 700;
  color: #67c23a;
}
</style>
