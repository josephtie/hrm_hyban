<template>
  <div class="enhanced-comptes-virement-view">
    <!-- Header amélioré -->
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Gestion des Comptes de Virement</h1>
          <p class="page-subtitle">Configuration des comptes bancaires pour les virements</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ comptes.length }}</div>
            <div class="stat-label">Total comptes</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ banquesActives }}</div>
            <div class="stat-label">Banques actives</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire -->
      <div class="sidebar-panel" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} un Compte</h3>
          <el-button @click="closeForm" circle>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <el-form :model="form" label-width="120px" style="padding: 20px 20px 10px 20px; flex: 1; overflow-y: auto;">
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><OfficeBuilding /></el-icon>
              Banque
            </label>
            <el-select 
              v-model="form.bankId" 
              placeholder="Sélectionnez une banque"
              size="large"
              style="width: 100%"
              filterable
            >
              <el-option
                v-for="banque in banques"
                :key="banque.id"
                :label="`${banque.sigle} (${banque.codbanq})`"
                :value="banque.id"
              />
            </el-select>
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><CreditCard /></el-icon>
              RIB
            </label>
            <el-input-number
              v-model="form.rib"
              placeholder="RIB (numérique)"
              :min="0"
              :max="999999"
              size="large"
              controls-position="right"
              style="width: 100%"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><House /></el-icon>
              Numéro de guichet
            </label>
            <el-input 
              v-model="form.numGuichet" 
              placeholder="Numéro de guichet (5 chiffres)"
              size="large"
              maxlength="5"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Document /></el-icon>
              Numéro de compte
            </label>
            <el-input 
              v-model="form.numCompte" 
              placeholder="Numéro de compte (12 chiffres)"
              size="large"
              maxlength="12"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><User /></el-icon>
              Donneur ordre
            </label>
            <el-input 
              v-model="form.donneurOrdre" 
              placeholder="Donneur d'ordre (15 caractères)"
              size="large"
              maxlength="15"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><OfficeBuilding /></el-icon>
              Code établissement
            </label>
            <el-input 
              v-model="form.codeEtablissement" 
              placeholder="Code établissement (5 chiffres)"
              size="large"
              maxlength="5"
            />
          </div>

          <div class="form-actions">
            <el-button @click="closeForm" size="large">Annuler</el-button>
            <el-button type="primary" @click="saveForm" size="large">
              {{ isEditing ? 'Mettre à jour' : 'Créer' }}
            </el-button>
          </div>
        </el-form>
      </div>

      <!-- Colonne principale avec le tableau amélioré -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Comptes de Virement</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle class="enhanced-button">
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary" class="enhanced-button">
              <el-icon><Plus /></el-icon>
              Nouveau Compte
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils améliorée -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher un compte..."
              style="width: 300px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>
          
          <div class="toolbar-right">
            <el-select
              v-model="filterBank"
              placeholder="Banque"
              style="width: 200px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option
                v-for="banque in banques"
                :key="banque.id"
                :label="banque.sigle"
                :value="banque.id"
              />
            </el-select>
          </div>
        </div>

        <!-- Tableau amélioré -->
        <div class="table-container">
          <el-table
            :data="filteredComptes"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            class="enhanced-table"
            :row-class-name="tableRowClassName"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="bank" label="Banque" width="150" sortable>
              <template #default="{ row }">
                <div class="bank-info">
                  <el-tag type="primary" size="large" class="enhanced-tag">
                    {{ row.bank.sigle }}
                  </el-tag>
                  <span class="bank-code">{{ row.bank.codbanq }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="rib" label="RIB" width="80" sortable>
              <template #default="{ row }">
                <el-tag type="info" size="small" class="enhanced-tag">
                  {{ row.rib }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="numGuichet" label="Guichet" width="100" sortable>
              <template #default="{ row }">
                <div class="guichet-info">
                  <el-icon><House /></el-icon>
                  <span>{{ row.numGuichet }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="numCompte" label="Numéro de compte" min-width="180" sortable>
              <template #default="{ row }">
                <div class="compte-info">
                  <el-icon class="compte-icon"><Document /></el-icon>
                  <div class="compte-details">
                    <div class="compte-number">{{ formatCompte(row.numCompte) }}</div>
                    <div class="compte-rib">RIB: {{ row.rib }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="donneurOrdre" label="Donneur ordre" width="140" sortable>
              <template #default="{ row }">
                <div class="donneur-info">
                  <el-icon><User /></el-icon>
                  <span>{{ row.donneurOrdre }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="codeEtablissement" label="Code Etab." width="100" sortable>
              <template #default="{ row }">
                <el-tag type="warning" size="small" class="enhanced-tag">
                  {{ row.codeEtablissement }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="dateCreation" label="Création" width="120" sortable>
              <template #default="{ row }">
                <div class="date-info">
                  <el-icon><Clock /></el-icon>
                  <span>{{ formatDate(row.dateCreation) }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="180" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button
                    size="small"
                    @click="editCompte(row)"
                    type="primary"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="viewDetails(row)"
                    type="info"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><View /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="deleteCompte(row)"
                    type="danger"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>

    <!-- Modal amélioré pour la confirmation -->
    <EnhancedModal
      v-model="showDeleteModal"
      title="Confirmation de suppression"
      subtitle="Cette action est irréversible"
      type="danger"
      icon="WarningFilled"
      :alerts="deleteAlerts"
      @confirm="confirmDelete"
      @cancel="cancelDelete"
      confirm-text="Supprimer"
      cancel-text="Annuler"
    >
      <div class="delete-confirmation">
        <p>Êtes-vous sûr de vouloir supprimer le compte de <strong>{{ selectedCompte?.bank.sigle }}</strong> ?</p>
        <p>Numéro de compte: <strong>{{ formatCompte(selectedCompte?.numCompte || '') }}</strong></p>
        <p>Cette action supprimera définitivement toutes les données associées.</p>
      </div>
    </EnhancedModal>

    <!-- Modal pour afficher les détails -->
    <EnhancedModal
      v-model="showDetailsModal"
      title="Détails du compte"
      subtitle="Informations complètes du compte de virement"
      type="info"
      icon="View"
      :show-footer="false"
      width="800px"
    >
      <div v-if="selectedCompte" class="compte-details-modal">
        <div class="details-grid">
          <div class="detail-item">
            <label>Banque:</label>
            <span>{{ selectedCompte.bank.sigle }} ({{ selectedCompte.bank.codbanq }})</span>
          </div>
          <div class="detail-item">
            <label>RIB:</label>
            <span>{{ selectedCompte.rib }}</span>
          </div>
          <div class="detail-item">
            <label>Guichet:</label>
            <span>{{ selectedCompte.numGuichet }}</span>
          </div>
          <div class="detail-item">
            <label>Numéro compte:</label>
            <span>{{ formatCompte(selectedCompte.numCompte) }}</span>
          </div>
          <div class="detail-item">
            <label>Donneur ordre:</label>
            <span>{{ selectedCompte.donneurOrdre }}</span>
          </div>
          <div class="detail-item">
            <label>Code établissement:</label>
            <span>{{ selectedCompte.codeEtablissement }}</span>
          </div>
          <div class="detail-item">
            <label>Date de création:</label>
            <span>{{ formatDate(selectedCompte.dateCreation) }}</span>
          </div>
        </div>
      </div>
    </EnhancedModal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, Refresh, Close, OfficeBuilding, CreditCard,
  Key, House, Document, User, Clock, WarningFilled, View
} from '@element-plus/icons-vue'
import EnhancedModal from '@/components/common/EnhancedModal.vue'
import { compteVirementService, type CompteVirementDto } from '@/services/compteVirement.service'

interface Banque {
  id: number
  sigle: string
  codbanq: string
  libelle: string
}

interface CompteVirement {
  id: number
  bank: Banque
  rib: string
  numGuichet: string
  numCompte: string
  donneurOrdre: string
  codeEtablissement: string
  dateCreation: Date
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const filterBank = ref<number | null>(null)
const selectedComptes = ref<CompteVirement[]>([])
const showDeleteModal = ref(false)
const showDetailsModal = ref(false)
const selectedCompte = ref<CompteVirement | null>(null)

const form = reactive({
  id: 0,
  bankId: 0,
  rib: 0, // Changé de string à number pour correspondre à l'entité
  numGuichet: '',
  numCompte: '',
  donneurOrdre: '',
  codeEtablissement: ''
})

const banques = ref<Banque[]>([])
const comptes = ref<CompteVirement[]>([])

// Charger les comptes de virement depuis l'API
const loadComptes = async () => {
  try {
    loading.value = true
    const response = await compteVirementService.getAll({
      search: searchText.value || undefined,
      size: 50,
      page: 0
    })
    
    if (response.success && response.data) {
      // Transformer les données pour correspondre à l'interface locale
      comptes.value = response.data.map((compte: CompteVirementDto) => ({
        id: compte.id || 0,
        bank: compte.bank ? {
          id: compte.bank.id,
          sigle: compte.bank.sigle || '',
          codbanq: compte.bank.codbanq || '',
          libelle: compte.bank.libelle || ''
        } : banques.value[0],
        rib: String(compte.ribCpteVirmt || 0), // Convertir le nombre en chaîne pour l'affichage
        codeEtablissement: compte.codEtablVirmt || '',
        donneurOrdre: compte.donneurOrdCpteVirmt || '',
        numCompte: compte.numcpteCpteVirmt || '',
        numGuichet: compte.numguichCpteVirmt || '',
        dateCreation: new Date(compte.createdAt || new Date())
      }))
    }
    console.log('Comptes chargés:', comptes.value)
  } catch (error) {
    console.error('Erreur lors du chargement des comptes:', error)
    ElMessage.error('Erreur lors du chargement des comptes')
  } finally {
    loading.value = false
  }
}

// Charger les banques disponibles
const loadBanques = async () => {
  try {
    const response = await compteVirementService.getBanques()
    if (response.success && response.data) {
      banques.value = response.data.map((banque: any) => ({
        id: banque.id,
        sigle: banque.sigle || '',
        codbanq: banque.codbanq || '',
        libelle: banque.libelle || ''
      }))
    }
  } catch (error) {
    console.error('Erreur lors du chargement des banques:', error)
  }
}

const refreshData = async () => {
  await Promise.all([loadComptes(), loadBanques()])
  ElMessage.success('Données actualisées')
}

const filteredComptes = computed(() => {
  let filtered = comptes.value

  if (searchText.value) {
    filtered = filtered.filter(compte =>
      compte.bank.sigle.toLowerCase().includes(searchText.value.toLowerCase()) ||
      compte.numCompte.includes(searchText.value) ||
      compte.donneurOrdre.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  if (filterBank.value !== null) {
    filtered = filtered.filter(compte => compte.bank.id === filterBank.value)
  }

  return filtered
})

const banquesActives = computed(() => {
  return new Set(comptes.value.map(c => c.bank.id)).size
})

const deleteAlerts = computed(() => {
  if (!selectedCompte.value) return []
  
  return [
    {
      type: 'danger' as const,
      title: 'Attention',
      message: 'La suppression est définitive et irréversible',
      closable: false
    }
  ]
})

// Méthodes
const formatDate = (date: Date) => {
  return new Intl.DateTimeFormat('fr-FR').format(date)
}

const formatCompte = (numCompte: string) => {
  // Formater le numéro de compte par groupes de 4 chiffres
  if (numCompte.length === 12) {
    return `${numCompte.slice(0, 4)} ${numCompte.slice(4, 8)} ${numCompte.slice(8, 12)}`
  }
  return numCompte
}

const tableRowClassName = ({ row }: { row: CompteVirement }) => {
  return 'row-compte'
}

const toggleForm = () => {
  showForm.value = !showForm.value
  if (!showForm.value) {
    resetForm()
  }
}

const closeForm = () => {
  showForm.value = false
  resetForm()
}

const resetForm = () => {
  Object.assign(form, {
    id: 0,
    bankId: 0,
    rib: '',
    numGuichet: '',
    numCompte: '',
    donneurOrdre: '',
    codeEtablissement: ''
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.bankId || !form.rib || !form.numGuichet || !form.numCompte || !form.donneurOrdre || !form.codeEtablissement) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  if (form.rib === 0 || form.rib === null) {
    ElMessage.error('Le RIB est obligatoire')
    return
  }

  if (form.numGuichet.length !== 5) {
    ElMessage.error('Le numéro de guichet doit contenir exactement 5 chiffres')
    return
  }

  if (form.numCompte.length !== 12) {
    ElMessage.error('Le numéro de compte doit contenir exactement 12 chiffres')
    return
  }

  if (form.codeEtablissement.length !== 5) {
    ElMessage.error('Le code établissement doit contenir exactement 5 chiffres')
    return
  }

  loading.value = true
  try {
    const compteData: CompteVirementDto = {
      // Ne pas envoyer l'ID pour la création
      codEtablVirmt: form.codeEtablissement,
      donneurOrdCpteVirmt: form.donneurOrdre,
      bank: { id: form.bankId, sigle: '', codbanq: '', libelle: '' },
      numcpteCpteVirmt: form.numCompte,
      numguichCpteVirmt: form.numGuichet,
      ribCpteVirmt: form.rib // Plus besoin de parseInt, c'est déjà un nombre
    }

    if (isEditing.value) {
      // Pour la modification, ajouter l'ID
      compteData.id = form.id
      await compteVirementService.update(compteData)
      ElMessage.success('Compte mis à jour avec succès')
    } else {
      // Pour la création, pas d'ID
      await compteVirementService.create(compteData)
      ElMessage.success('Compte créé avec succès')
    }

    resetForm()
    await loadComptes()
    closeForm() // Fermer le formulaire en cas de succès
  } catch (error) {
    console.error('Erreur lors de la sauvegarde:', error)
    ElMessage.error('Erreur lors de la sauvegarde du compte')
  }
}

const editCompte = (compte: CompteVirement) => {
  Object.assign(form, {
    id: compte.id,
    bankId: compte.bank.id,
    rib: compte.rib,
    numGuichet: compte.numGuichet,
    numCompte: compte.numCompte,
    donneurOrdre: compte.donneurOrdre,
    codeEtablissement: compte.codeEtablissement
  })
  isEditing.value = true
  showForm.value = true
}

const viewDetails = (compte: CompteVirement) => {
  selectedCompte.value = compte
  showDetailsModal.value = true
}

const deleteCompte = (compte: CompteVirement) => {
  selectedCompte.value = compte
  showDeleteModal.value = true
}

const confirmDelete = async () => {
  if (!selectedCompte.value) return

  try {
    await compteVirementService.delete(selectedCompte.value.id)
    ElMessage.success('Compte supprimé avec succès')
    showDeleteModal.value = false
    selectedCompte.value = null
    await loadComptes()
    // Pas besoin de fermer le formulaire ici, seulement la modal de suppression
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
    ElMessage.error('Erreur lors de la suppression du compte')
  } finally {
    showDeleteModal.value = false
    selectedCompte.value = null
  }
}

const cancelDelete = () => {
  showDeleteModal.value = false
  selectedCompte.value = null
}

const handleSelectionChange = (selection: CompteVirement[]) => {
  selectedComptes.value = selection
}

// Charger les données au montage du composant
onMounted(() => {
  refreshData()
})

onMounted(() => {
  // Initialisation
})
</script>

<style lang="scss" scoped>
@use '@/styles/design-system.scss' as *;

.enhanced-comptes-virement-view {
  padding: var(--spacing-xl);
  background: var(--bg-secondary);
  min-height: 100vh;
}

.page-header {
  margin-bottom: var(--spacing-xl);
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: var(--spacing-xl);
  }
  
  .page-title {
    margin: 0 0 var(--spacing-xs) 0;
    font-size: 32px;
    font-weight: 700;
  }
  
  .page-subtitle {
    margin: 0;
    color: var(--text-secondary);
    font-size: 16px;
  }
  
  .header-stats {
    display: flex;
    gap: var(--spacing-xl);
    
    .stat-item {
      text-align: center;
      
      .stat-value {
        font-size: 28px;
        font-weight: 700;
        color: var(--primary-color);
        margin-bottom: var(--spacing-xs);
      }
      
      .stat-label {
        font-size: 14px;
        color: var(--text-secondary);
      }
    }
  }
}

.main-content {
  display: flex;
  gap: var(--spacing-xl);
  height: calc(100vh - 200px);
}

.sidebar-panel {
  width: 500px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  
  .panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: var(--spacing-lg);
    border-bottom: 1px solid var(--border-light);
    
    h3 {
      margin: 0;
      color: var(--text-primary);
      font-size: 18px;
      font-weight: 600;
    }
  }
  
  .compte-form {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
}

.main-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  
  .panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: var(--spacing-lg);
    border-bottom: 1px solid var(--border-light);
    
    h3 {
      margin: 0;
      color: var(--text-primary);
      font-size: 18px;
      font-weight: 600;
    }
    
    .panel-controls {
      display: flex;
      gap: var(--spacing-sm);
    }
  }
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-secondary);
}

.table-container {
  flex: 1;
  overflow: hidden;
  padding: var(--spacing-lg);
}

.form-section {
  margin-bottom: var(--spacing-xl);
  
  .section-title {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
    margin: 0 0 var(--spacing-lg) 0;
    color: var(--text-primary);
    font-size: 16px;
    font-weight: 600;
    padding-bottom: var(--spacing-sm);
    border-bottom: 2px solid var(--border-light);
  }
}

.form-row {
  display: flex;
  gap: var(--spacing-lg);
  
  .el-form-item {
    flex: 1;
  }
}

.bank-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  
  .bank-sigle {
    font-weight: 600;
    color: var(--text-primary);
  }
  
  .bank-code {
    color: var(--text-secondary);
    font-size: 12px;
  }
}

.bank-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
  
  .bank-code {
    font-size: 12px;
    color: var(--text-secondary);
  }
}

.guichet-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  color: var(--text-regular);
}

.compte-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .compte-icon {
    font-size: 20px;
    color: var(--primary-color);
  }
  
  .compte-details {
    .compte-number {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .compte-rib {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.donneur-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  color: var(--text-regular);
}

.date-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  color: var(--text-secondary);
  font-size: 14px;
}

// Styles pour le formulaire
.compte-form {
  padding: var(--spacing-lg);
  
  .form-section {
    margin-bottom: var(--spacing-xl);
    
    h4 {
      margin-bottom: var(--spacing-md);
      color: var(--text-primary);
      font-weight: 600;
      font-size: 16px;
      border-bottom: 2px solid var(--primary-color);
      padding-bottom: var(--spacing-xs);
    }
    
    .el-form-item {
      margin-bottom: var(--spacing-lg);
    }
  }
  
  .form-actions {
    display: flex !important;
    gap: var(--spacing-md);
    justify-content: flex-end;
    padding-top: var(--spacing-lg);
    border-top: 1px solid var(--border-color);
    margin-top: var(--spacing-lg);
    
    .el-button {
      min-width: 120px;
      font-weight: 500;
      height: 40px;
      display: inline-flex !important;
      align-items: center;
      justify-content: center;
      
      &.el-button--primary {
        background: var(--primary-color);
        border-color: var(--primary-color);
        color: white;
        
        &:hover {
          background: var(--primary-color-dark);
          border-color: var(--primary-color-dark);
        }
      }
      
      &.el-button--default {
        background: white;
        border-color: var(--border-color);
        color: var(--text-primary);
        
        &:hover {
          background: var(--bg-color);
          border-color: var(--primary-color);
        }
      }
    }
  }
}

.action-buttons {
  display: flex;
  gap: var(--spacing-xs);
  
  .btn-action {
    padding: 6px;
    border-radius: var(--border-radius-sm);
    
    &:hover {
      transform: scale(1.1);
    }
  }
}

.enhanced-table {
  :deep(.el-table__row) {
    transition: all var(--transition-base);
    
    &:hover {
      background: var(--bg-secondary);
    }
    
    &.row-compte {
      background: rgba(64, 158, 255, 0.02);
      
      &:hover {
        background: rgba(64, 158, 255, 0.08);
      }
    }
  }
}

.delete-confirmation {
  text-align: center;
  
  p {
    margin-bottom: var(--spacing-md);
    color: var(--text-regular);
    
    strong {
      color: var(--danger-color);
    }
  }
}

.compte-details-modal {
  .details-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: var(--spacing-lg);
    
    .detail-item {
      display: flex;
      flex-direction: column;
      gap: var(--spacing-xs);
      
      label {
        font-weight: 600;
        color: var(--text-secondary);
        font-size: 14px;
      }
      
      span {
        color: var(--text-primary);
        font-size: 16px;
      }
    }
  }
}

// Responsive
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }
  
  .sidebar-panel {
    width: 100%;
  }
  
  .form-row {
    flex-direction: column;
    gap: 0;
  }
  
  .compte-details-modal .details-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .enhanced-comptes-virement-view {
    padding: var(--spacing-lg);
  }
  
  .page-header .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-lg);
  }
  
  .header-stats {
    width: 100%;
    justify-content: space-around;
  }
  
  .toolbar {
    flex-direction: column;
    gap: var(--spacing-md);
    align-items: stretch;
  }
}
.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-weight: 500;
  color: #303133;
  font-size: 16px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.form-group :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #dcdfe6;
  transition: all 0.3s ease;
}

.form-group :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc;
}

.form-group :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.form-group :deep(.el-textarea__inner) {
  border-radius: 8px;
  resize: none;
}

.form-group :deep(.el-textarea__inner:focus) {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.form-group :deep(.el-radio-group) {
  display: flex;
  gap: 20px;
}

.form-group :deep(.el-input__prefix) {
  color: #909399;
}
</style>
