<template>
  <div class="enhanced-agents-specifiques-view">
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Agents spécifiques</h1>
          <p class="page-subtitle">Gestion des agents exonérés des traitements standards de paie</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">Total agents</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ agents.length }}</div>
            <div class="stat-label">Affichés</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ activeFilterLabel }}</div>
            <div class="stat-label">Statut</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Agents spécifiques</h3>
          <div class="panel-controls">
            <el-button @click="loadAgents" circle class="enhanced-button" :loading="loading">
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="openCreateDialog" type="primary" class="enhanced-button">
              <el-icon><Plus /></el-icon>
              Nouvel agent
            </el-button>
          </div>
        </div>

        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="search"
              placeholder="Rechercher matricule, nom, prénom..."
              style="width: 320px"
              clearable
              size="large"
              class="enhanced-input"
              @keyup.enter="handleSearchSubmit"
              @input="handleSearchInput"
              @clear="handleSearchSubmit"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>
          <div class="toolbar-right">
            <el-select
              v-model="statusFilter"
              placeholder="Statut"
              style="width: 150px"
              clearable
              size="large"
              class="enhanced-input"
              @change="handleFilterChange"
            >
              <el-option label="Tous" value="all" />
              <el-option label="Actifs" value="active" />
              <el-option label="Inactifs" value="inactive" />
            </el-select>
            <el-select
              v-model="typeFilter"
              placeholder="Type agent"
              style="width: 170px"
              clearable
              size="large"
              class="enhanced-input"
              @change="handleFilterChange"
            >
              <el-option label="Staff PDG" value="STAFF_PDG" />
              <el-option label="DOZO" value="DOZO" />
              <el-option label="Stage" value="STAGE" />
              <el-option label="Agent sécurité" value="AGENT_SECURITE" />
            </el-select>
            <el-button @click="clearFilters" type="warning" class="enhanced-button" size="large">
              <el-icon><Refresh /></el-icon>
              Effacer
            </el-button>
          </div>
        </div>

        <div class="table-container">
          <el-table :data="displayedAgents" style="width: 100%" v-loading="loading">
            <el-table-column prop="matricule" label="Matricule" min-width="100" sortable>
              <template #default="{ row }">
                <el-tag type="info" size="small" class="enhanced-tag">{{ row.matricule || 'N/A' }}</el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="nomComplet" label="Nom & Prénom" min-width="190" sortable>
              <template #default="{ row }">
                <div class="personnel-info">
                  <el-icon class="personnel-icon"><User /></el-icon>
                  <span>{{ fullName(row) }}</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="categorieSpeciale" label="Catégorie" min-width="150" sortable>
              <template #default="{ row }">
                <el-tag :type="getCategoryTag(row.categorieSpeciale)" size="small" class="enhanced-tag">
                  {{ getCategoryLabel(row.categorieSpeciale) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="fonction" label="Fonction" min-width="160" sortable>
              <template #default="{ row }">
                <div class="fonction-info">
                  <el-icon class="fonction-icon"><Briefcase /></el-icon>
                  <span>{{ row.fonction || '-' }}</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="Nationalité" min-width="130" sortable>
              <template #default="{ row }">{{ row.nationnalite?.libelle || '-' }}</template>
            </el-table-column>

            <el-table-column prop="phoneNumber" label="Téléphone" min-width="130">
              <template #default="{ row }">
                <div class="date-info">
                  <el-icon class="date-icon"><Phone /></el-icon>
                  <span>{{ row.phoneNumber || '-' }}</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="netapayer" label="Net à payer" min-width="130" align="right" sortable>
              <template #default="{ row }">
                <div class="salaire-info">
                  <el-icon class="salaire-icon"><CreditCard /></el-icon>
                  <span>{{ formatCurrency(row.netapayer) }}</span>
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="actif" label="Statut" min-width="100" align="center" sortable>
              <template #default="{ row }">
                <el-tag :type="row.actif === false ? 'danger' : 'success'" size="small" class="enhanced-tag">
                  <el-icon style="margin-right: 4px;"><CircleCheck v-if="row.actif !== false" /><WarningFilled v-else /></el-icon>
                  {{ row.actif === false ? 'Inactif' : 'Actif' }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column label="Actions" min-width="140" fixed="right" align="center">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button size="small" type="primary" circle @click="openEditDialog(row)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" type="danger" circle :disabled="row.actif === false" @click="confirmDeactivate(row)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="displayedTotal"
            :page-sizes="[5, 10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next"
            @size-change="loadAgents"
            @current-change="loadAgents"
          />
        </div>
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.idEmpl ? 'Modifier agent spécifique' : 'Nouvel agent spécifique'" width="920px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="170px" class="agent-form">
        <el-tabs>
          <el-tab-pane label="Information personnelle">
            <el-row :gutter="16">
              <el-col :span="12"><el-form-item label="Matricule" prop="matricule"><el-input v-model="form.matricule" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Sexe" prop="sexe"><el-radio-group v-model="form.sexe"><el-radio label="Masculin">Masculin</el-radio><el-radio label="Feminin">Féminin</el-radio></el-radio-group></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Nom" prop="nom"><el-input v-model="form.nom" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Prénom" prop="prenom"><el-input v-model="form.prenom" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Situation matrimoniale" prop="situationMatrimoniale"><el-select v-model="form.situationMatrimoniale" style="width: 100%"><el-option label="Aucun" :value="0" /><el-option label="Marié(e)" :value="1" /><el-option label="Célibataire" :value="2" /><el-option label="Divorcé(e)" :value="3" /><el-option label="Veuf(ve)" :value="4" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Nationalité" prop="nationalite"><el-select v-model="form.nationalite" placeholder="Sélectionner" filterable style="width: 100%" :loading="referencesLoading"><el-option v-for="item in nationalites" :key="item.id" :label="item.libelle || item.code || `Nationalité ${item.id}`" :value="item.id" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Date de naissance"><el-date-picker v-model="form.dofbrid" type="date" placeholder="Sélectionner" format="DD/MM/YYYY" value-format="DD/MM/YYYY" style="width: 100%" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Téléphone"><el-input v-model="form.phoneNumber" /></el-form-item></el-col>
              <el-col :span="24"><el-form-item label="Lieu d'habitation"><el-input v-model="form.lieuHabitation" /></el-form-item></el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="Contrat spécifique">
            <el-row :gutter="16">
              <el-col :span="12"><el-form-item label="Type de contrat" prop="typeContrat"><el-select v-model="form.typeContrat" style="width: 100%"><el-option label="Staff PDG" value="STAFF_PDG" /><el-option label="DOZO" value="DOZO" /><el-option label="Stage" value="STAGE" /><el-option label="Agent sécurité" value="AGENT_SECURITE" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Fonction" prop="fonction"><el-select v-model="form.fonction" placeholder="Sélectionner" filterable style="width: 100%" :loading="referencesLoading"><el-option v-for="item in fonctions" :key="item.id" :label="item.libelle || item.description || `Fonction ${item.id}`" :value="item.id" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Site" prop="site"><el-select v-model="form.site" placeholder="Sélectionner" filterable style="width: 100%" :loading="referencesLoading"><el-option v-for="item in sites" :key="item.id" :label="item.libelle || item.description || `Site ${item.id}`" :value="item.id" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Mode de paiement" prop="modepaiement"><el-select v-model="form.modepaiement" style="width: 100%"><el-option label="Virement" value="virement-bancaire" /><el-option label="Mobile Money" value="transfert-mobile-money" /><el-option label="Wave" value="transfert-wave" /></el-select></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Date début" prop="dDeb"><el-date-picker v-model="form.dDeb" type="date" placeholder="Sélectionner" format="DD/MM/YYYY" value-format="DD/MM/YYYY" style="width: 100%" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Date fin"><el-date-picker v-model="form.dFin" type="date" placeholder="Sélectionner" format="DD/MM/YYYY" value-format="DD/MM/YYYY" style="width: 100%" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Compte / Téléphone" prop="paiementNumber"><el-input v-model="form.paiementNumber" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="Net à payer" prop="netpayer"><el-input-number v-model="form.netpayer" :min="0" :precision="0" style="width: 100%" /></el-form-item></el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <div class="modal-actions">
          <el-button @click="dialogVisible = false">Annuler</el-button>
          <el-button type="primary" :loading="saving" @click="saveAgent">Valider</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Briefcase, CircleCheck, CreditCard, Delete, Edit, Phone, Plus, Refresh, Search, User, WarningFilled } from '@element-plus/icons-vue'
import agentsSpecifiquesService, { type AgentSpecifique, type AgentSpecifiqueForm, type ReferenceItem } from '@/services/agents-specifiques.service'

const loading = ref(false)
const saving = ref(false)
const referencesLoading = ref(false)
const dialogVisible = ref(false)
const agents = ref<AgentSpecifique[]>([])
const nationalites = ref<ReferenceItem[]>([])
const fonctions = ref<ReferenceItem[]>([])
const sites = ref<ReferenceItem[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const search = ref('')
const statusFilter = ref('all')
const typeFilter = ref('')
const formRef = ref<FormInstance>()
let searchTimeout: ReturnType<typeof setTimeout> | undefined

let form = reactive<AgentSpecifiqueForm>({ matricule: '', nom: '', prenom: '', sexe: 'Masculin', situationMatrimoniale: 0, nationalite: 1, typeContrat: 'STAGE', fonction: 1, site: 1, dDeb: '', modepaiement: 'virement-bancaire', paiementNumber: '', netpayer: 0 })

const rules: FormRules = {
  matricule: [{ required: true, message: 'Matricule obligatoire', trigger: 'blur' }],
  nom: [{ required: true, message: 'Nom obligatoire', trigger: 'blur' }],
  prenom: [{ required: true, message: 'Prénom obligatoire', trigger: 'blur' }],
  sexe: [{ required: true, message: 'Sexe obligatoire', trigger: 'change' }],
  situationMatrimoniale: [{ required: true, message: 'Situation matrimoniale obligatoire', trigger: 'change' }],
  nationalite: [{ required: true, message: 'Nationalité obligatoire', trigger: 'blur' }],
  typeContrat: [{ required: true, message: 'Type de contrat obligatoire', trigger: 'change' }],
  fonction: [{ required: true, message: 'Fonction obligatoire', trigger: 'blur' }],
  site: [{ required: true, message: 'Site obligatoire', trigger: 'blur' }],
  modepaiement: [{ required: true, message: 'Mode de paiement obligatoire', trigger: 'change' }],
  dDeb: [{ required: true, message: 'Date de début obligatoire', trigger: 'blur' }],
  paiementNumber: [{ required: true, message: 'Compte ou téléphone obligatoire', trigger: 'blur' }],
  netpayer: [{ required: true, message: 'Net à payer obligatoire', trigger: 'blur' }]
}

const activeFilterLabel = computed(() => statusFilter.value === 'active' ? 'Actifs' : statusFilter.value === 'inactive' ? 'Inactifs' : 'Tous')
const hasLocalFilters = computed(() => !!typeFilter.value || (!!statusFilter.value && statusFilter.value !== 'all'))
const filteredAgents = computed(() => agents.value.filter((agent) => {
  const statusMatches = statusFilter.value === 'all' || !statusFilter.value || (statusFilter.value === 'active' && agent.actif !== false) || (statusFilter.value === 'inactive' && agent.actif === false)
  const typeMatches = !typeFilter.value || agent.categorieSpeciale === typeFilter.value
  return statusMatches && typeMatches
}))
const displayedAgents = computed(() => hasLocalFilters.value ? filteredAgents.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value) : agents.value)
const displayedTotal = computed(() => hasLocalFilters.value ? filteredAgents.value.length : total.value)

const resetForm = () => Object.assign(form, { idEmpl: undefined, matricule: '', nom: '', prenom: '', sexe: 'Masculin', situationMatrimoniale: 0, nationalite: 1, lieuHabitation: '', dofbrid: '', phoneNumber: '', typeContrat: 'STAGE', fonction: 1, site: 1, dDeb: '', dFin: '', modepaiement: 'virement-bancaire', paiementNumber: '', netpayer: 0 })

const loadReferences = async () => {
  referencesLoading.value = true
  try {
    const [nationalitesData, fonctionsData, sitesData] = await Promise.all([
      agentsSpecifiquesService.getNationalites(),
      agentsSpecifiquesService.getFonctions(),
      agentsSpecifiquesService.getSites()
    ])
    nationalites.value = nationalitesData
    fonctions.value = fonctionsData
    sites.value = sitesData
    if (!nationalites.value.some((item) => item.id === form.nationalite) && nationalites.value[0]?.id) form.nationalite = nationalites.value[0].id
    if (!fonctions.value.some((item) => item.id === form.fonction) && fonctions.value[0]?.id) form.fonction = fonctions.value[0].id
    if (!sites.value.some((item) => item.id === form.site) && sites.value[0]?.id) form.site = sites.value[0].id
  } catch (error) {
    ElMessage.error('Erreur lors du chargement des référentiels')
  } finally {
    referencesLoading.value = false
  }
}

const loadAgents = async () => {
  loading.value = true
  try {
    const response = await agentsSpecifiquesService.list({
      limit: hasLocalFilters.value ? 10000 : pageSize.value,
      offset: hasLocalFilters.value ? 0 : (currentPage.value - 1) * pageSize.value,
      search: search.value || undefined
    })
    agents.value = response.rows || []
    total.value = response.total || 0
  } catch (error) {
    ElMessage.error('Erreur lors du chargement des agents spécifiques')
  } finally {
    loading.value = false
  }
}

const handleSearchSubmit = () => {
  if (searchTimeout) clearTimeout(searchTimeout)
  currentPage.value = 1
  loadAgents()
}

const handleSearchInput = () => {
  if (searchTimeout) clearTimeout(searchTimeout)
  searchTimeout = setTimeout(handleSearchSubmit, 400)
}

const clearFilters = () => {
  search.value = ''
  statusFilter.value = 'all'
  typeFilter.value = ''
  currentPage.value = 1
  loadAgents()
}

const handleFilterChange = () => {
  currentPage.value = 1
  loadAgents()
}

const openCreateDialog = () => {
  resetForm()
  if (!nationalites.value.length || !fonctions.value.length || !sites.value.length) loadReferences()
  dialogVisible.value = true
}

const openEditDialog = async (agent: AgentSpecifique) => {
  resetForm()
  Object.assign(form, { idEmpl: agent.id, matricule: agent.matricule || '', nom: agent.nom || '', prenom: agent.prenom || '', sexe: agent.sexe || 'Masculin', situationMatrimoniale: agent.situationMatrimoniale ?? 0, nationalite: agent.nationnalite?.id || 1, lieuHabitation: agent.lieuHabitation || '', dofbrid: agent.dofbrid || '', phoneNumber: agent.phoneNumber || '', netpayer: Number(agent.netapayer || 0) })
  if (agent.id) {
    try {
      const contractResponse = await agentsSpecifiquesService.getContractByEmployee(agent.id)
      const contract = contractResponse.row
      if (contract) Object.assign(form, { typeContrat: contract.typeContrat || 'STAGE', fonction: contract.fonction?.id || 1, site: contract.site?.id || 1, dDeb: normalizeDate(contract.dDeb || contract.dateDebut), dFin: normalizeDate(contract.dFin || contract.dateFin), modepaiement: contract.modepaiement || 'virement-bancaire', paiementNumber: contract.paiementNumber || '', netpayer: Number(contract.remunerationForfaitaire || agent.netapayer || 0) })
    } catch (error) {
      ElMessage.warning('Contrat spécifique non chargé pour cet agent')
    }
  }
  dialogVisible.value = true
}

const saveAgent = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    await agentsSpecifiquesService.save({
      ...form,
      dofbrid: formatDateForBackend(form.dofbrid),
      dDeb: formatDateForBackend(form.dDeb),
      dFin: formatDateForBackend(form.dFin)
    })
    ElMessage.success('Agent spécifique enregistré')
    dialogVisible.value = false
    await loadAgents()
  } catch (error) {
    ElMessage.error("Erreur lors de l'enregistrement")
  } finally {
    saving.value = false
  }
}

const confirmDeactivate = async (agent: AgentSpecifique) => {
  if (!agent.id) return
  await ElMessageBox.confirm(`Désactiver ${fullName(agent)} ?`, 'Confirmation', { type: 'warning' })
  await agentsSpecifiquesService.deactivate(agent.id)
  ElMessage.success('Agent désactivé')
  await loadAgents()
}

const fullName = (agent: AgentSpecifique) => agent.nomComplet || `${agent.nom || ''} ${agent.prenom || ''}`.trim() || 'N/A'
const getCategoryLabel = (value?: string) => ({ STAFF_PDG: 'Staff PDG', DOZO: 'DOZO', STAGE: 'Stage', AGENT_SECURITE: 'Agent sécurité' }[value || ''] || value || '-')
const getCategoryTag = (value?: string) => value === 'STAGE' ? 'success' : value === 'DOZO' ? 'warning' : value === 'AGENT_SECURITE' ? 'danger' : 'primary'
const formatCurrency = (value?: string | number) => new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF', maximumFractionDigits: 0 }).format(Number(value || 0))
const normalizeDate = (value?: string) => {
  if (!value) return ''
  if (/^\d{2}\/\d{2}\/\d{4}$/.test(value)) return value
  const datePart = value.substring(0, 10)
  const parts = datePart.split('-')
  if (parts.length === 3) return `${parts[2]}/${parts[1]}/${parts[0]}`
  return value
}
const formatDateForBackend = (value?: string) => value || ''

onMounted(loadAgents)
onMounted(loadReferences)
</script>

<style scoped>
.enhanced-agents-specifiques-view { padding: 24px; background: #f5f7fa; min-height: 100vh; }
.enhanced-card { background: white; border-radius: 15px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08); border: 1px solid #ebeef5; }
.page-header { margin-bottom: 24px; padding: 24px; }
.header-content { display: flex; justify-content: space-between; align-items: center; gap: 24px; }
.page-title { margin: 0; font-size: 28px; font-weight: 700; }
.text-gradient { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }
.page-subtitle { margin: 8px 0 0; color: #606266; font-size: 15px; }
.header-stats { display: flex; gap: 16px; }
.stat-item { min-width: 110px; text-align: center; padding: 14px 18px; background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%); border-radius: 12px; }
.stat-value { color: #409eff; font-size: 22px; font-weight: 700; }
.stat-label { margin-top: 4px; color: #606266; font-size: 12px; font-weight: 600; text-transform: uppercase; }
.main-content { display: grid; grid-template-columns: 1fr; gap: 24px; }
.main-panel { padding: 24px; }
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; padding-bottom: 16px; border-bottom: 1px solid #ebeef5; }
.panel-header h3 { margin: 0; color: #303133; font-size: 20px; font-weight: 600; }
.panel-controls, .toolbar, .toolbar-right, .personnel-info, .fonction-info, .date-info, .salaire-info, .action-buttons, .modal-actions { display: flex; align-items: center; gap: 12px; }
.toolbar { justify-content: space-between; margin-bottom: 20px; flex-wrap: wrap; }
.table-container { background: white; border-radius: 12px; overflow: hidden; border: 1px solid #ebeef5; }
.personnel-icon, .fonction-icon, .date-icon, .salaire-icon { color: #409eff; }
.enhanced-tag { font-weight: 600; border-radius: 6px; }
.pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 20px; }
.modal-actions { justify-content: flex-end; padding-top: 16px; border-top: 1px solid #e4e7ed; }
:deep(.el-dialog) { border-radius: 15px; }
:deep(.el-dialog__header) { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; margin: 0; border-radius: 15px 15px 0 0; }
:deep(.el-dialog__title), :deep(.el-dialog__headerbtn .el-dialog__close) { color: white; }
:deep(.el-dialog__body) { padding: 24px; }
:deep(.el-form-item__label) { font-weight: 500; color: #606266; }
:deep(.el-input__wrapper), :deep(.el-select .el-input__wrapper) { border-radius: 8px; }
:deep(.el-button) { border-radius: 8px; font-weight: 500; }
:deep(.el-table th.el-table__cell) { background: #f5f7fa; color: #303133; font-weight: 600; }
@media (max-width: 900px) { .header-content { flex-direction: column; align-items: flex-start; } .header-stats { width: 100%; flex-wrap: wrap; } .toolbar { align-items: stretch; } .toolbar-left, .toolbar-right { width: 100%; flex-wrap: wrap; } }
</style>
