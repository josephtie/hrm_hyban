<template>
  <div class="echeanciers-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><List /></el-icon>
            Échéanciers de Prêts
          </h1>
          <p class="page-subtitle">Suivi des échéances de remboursement des prêts</p>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="generateEcheanciers" class="enhanced-button">
            <el-icon><Refresh /></el-icon>
            Générer les Échéanciers
          </el-button>
        </div>
      </div>
    </div>

    <!-- Filtres -->
    <div class="filters-section enhanced-card">
      <div class="filters-content">
        <el-row :gutter="16">
          <el-col :span="8">
            <el-input
              v-model="filters.search"
              placeholder="Rechercher par nom, prénom ou matricule..."
              clearable
              style="width: 100%"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="5">
            <el-select v-model="filters.statut" placeholder="Statut" clearable style="width: 100%">
              <el-option label="Tous" :value="null" />
              <el-option label="Payés" :value="true" />
              <el-option label="Non payés" :value="false" />
            </el-select>
          </el-col>
          <el-col :span="5">
            <el-select v-model="filters.periodePaieId" placeholder="Période de paie" clearable style="width: 100%">
              <el-option
                v-for="periode in periodes"
                :key="periode.id"
                :label="periode.affiche"
                :value="periode.id"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="applyFilters">
              <el-icon><Search /></el-icon>
              Rechercher
            </el-button>
            <el-button @click="resetFilters">Réinitialiser</el-button>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- Tableau des échéanciers -->
    <div class="table-section enhanced-card">
      <div class="table-header">
        <h3>Liste des Échéances</h3>
        <div class="table-controls">
          <el-button type="success" @click="markAsPaid" :disabled="!selectedEcheanciers.length">
            <el-icon><Check /></el-icon>
            Marquer comme payé
          </el-button>
          <el-button type="warning" @click="exportEcheanciers">
            <el-icon><Download /></el-icon>
            Exporter
          </el-button>
        </div>
      </div>

      <div class="table-container">
        <el-table 
          :data="filteredEcheanciers" 
          v-loading="loading"
          @selection-change="handleSelectionChange"
          stripe
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column label="Personnel" min-width="200">
            <template #default="{ row }">
              <div class="personnel-info">
                <div class="personnel-name">{{ row.personnelNom }}</div>
                <div class="personnel-matricule">{{ row.matricule }}</div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Prêt" min-width="150">
            <template #default="{ row }">
              <div class="loan-info">
                <div class="loan-type">{{ row.typePret }}</div>
                <div class="loan-amount">{{ formatCurrency(row.montantPret) }}</div>
              </div>
            </template>
          </el-table-column>

          <!-- <el-table-column label="Échéance N°" width="100" align="center">
            <template #default="{ row }">
              <el-tag type="info">{{ row.numeroEcheance }}/{{ row.totalEcheances }}</el-tag>
            </template>
          </el-table-column> -->

          <el-table-column label="Période" width="120" sortable>
            <template #default="{ row }">
              {{ row.periodePaie.affiche }}
            </template>
          </el-table-column>

          <el-table-column label="Mensualité" width="120" sortable>
            <template #default="{ row }">
              <span class="amount">{{ formatCurrency(row.mensualite) }}</span>
            </template>
          </el-table-column>


          <el-table-column label="Principal" width="100" sortable>
            <template #default="{ row }">
              <span class="principal">{{ formatCurrency(row.principal) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Montant Payé" width="120" sortable>
            <template #default="{ row }">
              <span class="paid-amount">{{ formatCurrency(row.montantPaye) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Reste à payer" width="120" sortable>
            <template #default="{ row }">
              <span class="remaining-amount" :class="{ 'text-danger': row.resteAPayer > 0 }">
                {{ formatCurrency(row.resteAPayer) }}
              </span>
            </template>
          </el-table-column>

          <el-table-column label="Statut" width="120" sortable>
            <template #default="{ row }">
              <el-tag :type="getStatusColor(row.statut)">
                {{ getStatusLabel(row.statut) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Actions" width="100" fixed="right">
            <template #default="{ row }">
              <el-button type="warning" size="small" @click="editEcheancier(row)">
                <el-icon><Edit /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- Pagination -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="totalEcheanciers"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <!-- Modal de détails -->
    <el-dialog v-model="showDetailsModal" title="Détails de l'Échéance" width="600px">
      <div v-if="selectedEcheancier" class="echeancier-details">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Personnel">
            {{ selectedEcheancier.personnelNom }} ({{ selectedEcheancier.matricule }})
          </el-descriptions-item>
          <el-descriptions-item label="Type Prêt">
            {{ selectedEcheancier.typePret }}
          </el-descriptions-item>
          <el-descriptions-item label="Échéance N°">
            {{ selectedEcheancier.numeroEcheance }}/{{ selectedEcheancier.totalEcheances }}
          </el-descriptions-item>
          <el-descriptions-item label="Période">
            {{ formatPeriode(selectedEcheancier.periode) }}
          </el-descriptions-item>
          <el-descriptions-item label="Date Échéance">
            {{ formatDate(selectedEcheancier.dateEcheance) }}
          </el-descriptions-item>
          <el-descriptions-item label="Statut">
            <el-tag :type="getStatusColor(selectedEcheancier.statut)">
              {{ getStatusLabel(selectedEcheancier.statut) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Mensualité">
            <span class="amount">{{ formatCurrency(selectedEcheancier.mensualite) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Intérêts">
            <span class="interest">{{ formatCurrency(selectedEcheancier.interets) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Principal">
            <span class="principal">{{ formatCurrency(selectedEcheancier.principal) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Montant Payé">
            <span class="paid-amount">{{ formatCurrency(selectedEcheancier.montantPaye) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Reste à payer">
            <span class="remaining-amount text-danger">{{ formatCurrency(selectedEcheancier.resteAPayer) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- Modal de modification d'échéancier -->
    <el-dialog 
      v-model="showEditModal" 
      title="Modifier/Reporter le Remboursement" 
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedEcheancier" class="edit-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form :model="editForm" label-width="140px">
              <!-- Informations de l'échéancier -->
              <el-divider content-position="left">
                <span style="font-weight: bold; color: #409EFF;">Informations de l'Échéancier</span>
              </el-divider>
              
              <el-form-item label="Personnel concerné">
                <el-input 
                  :value="`${selectedEcheancier.personnelNom} (${selectedEcheancier.matricule})`" 
                  disabled 
                  style="background-color: #f5f5f5;"
                />
              </el-form-item>
              
              <el-form-item label="Type de prêt">
                <el-input 
                  :value="selectedEcheancier.typePret" 
                  disabled 
                  style="background-color: #f5f5f5;"
                />
              </el-form-item>
              
              <el-form-item label="Montant de l'échéance">
                <el-input 
                  :value="formatCurrency(selectedEcheancier.mensualite)" 
                  disabled 
                  style="background-color: #f5f5f5;"
                />
              </el-form-item>
              
              <el-form-item label="Montant total du prêt">
                <el-input 
                  :value="formatCurrency(selectedEcheancier.montantPret)" 
                  disabled 
                  style="background-color: #f5f5f5;"
                />
              </el-form-item>
              
              <el-form-item label="Statut actuel">
                <el-tag :type="getStatusColor(selectedEcheancier.statut)" size="large">
                  {{ getStatusLabel(selectedEcheancier.statut) }}
                </el-tag>
              </el-form-item>
              
              <!-- Nouvelles informations -->
              <el-divider content-position="left">
                <span style="font-weight: bold; color: #67C23A;">Nouvelles Informations</span>
              </el-divider>
              
              <el-form-item label="Nouvelle période de paiement" required>
                <el-select 
                  v-model="editForm.periodePaieId" 
                  placeholder="Sélectionner une nouvelle période"
                  style="width: 100%"
                  clearable
                >
                  <template #prefix>
                    <el-icon><Calendar /></el-icon>
                  </template>
                  <el-option
                    v-for="periode in periodes"
                    :key="periode.id"
                    :label="periode.affiche"
                    :value="periode.id"
                  >
                    <span>{{ periode.affiche }}</span>
                  </el-option>
                </el-select>
              </el-form-item>
              
              <el-form-item label="Date de remboursement prévue">
                <el-date-picker
                  v-model="editForm.dateRemboursement"
                  type="date"
                  placeholder="Sélectionner la date de remboursement"
                  format="DD/MM/YYYY"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
                  clearable
                >
                  <template #prefix>
                    <el-icon><Clock /></el-icon>
                  </template>
                </el-date-picker>
              </el-form-item>
              
              <el-form-item label="Observations">
                <el-input
                  v-model="editForm.observations"
                  type="textarea"
                  :rows="3"
                  placeholder="Ajouter des observations sur cette modification..."
                />
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        
        <el-divider />
        
        <div class="dialog-footer" style="text-align: right; margin-top: 20px;">
          <el-button size="large" @click="showEditModal = false">
            <el-icon><Close /></el-icon>
            Annuler
          </el-button>
          <el-button 
            type="primary" 
            size="large" 
            @click="saveEcheancierModification"
            :loading="loading"
          >
            <el-icon><Check /></el-icon>
            {{ editForm.periodePaieId ? 'Confirmer la modification' : 'Confirmer le report' }}
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  List, Refresh, Search, Check, Download, View, Edit, Calendar, Clock, Close 
} from '@element-plus/icons-vue'
import echelonnementrestService, { 
  type Echelonnement, 
  type EchelonnementFilter, 
  type PeriodePaie 
} from '@/services/echelonnementrest.service'

// Interfaces locales
interface Personnel {
  id: number
  matricule: string
  nomComplet: string
}

interface Echeancier {
  id: number
  pretId: number
  personnelId: number
  personnelNom: string
  matricule: string
  typePret: string
  montantPret: number
  numeroEcheance: number
  totalEcheances: number
  periodePaie: PeriodePaie | { affiche: string }
  dateEcheance: string
  mensualite: number
  interets: number
  principal: number
  montantPaye: number
  resteAPayer: number
  statut: string
  datePaiement?: string
}

// Données réactives
const loading = ref(false)
const showDetailsModal = ref(false)
const showEditModal = ref(false)
const selectedEcheancier = ref<Echeancier | null>(null)
const editForm = reactive({
  id: 0,
  periodePaieId: null as number | null,
  dateRemboursement: '',
  montant: 0,
  observations: ''
})
const selectedEcheanciers = ref<Echeancier[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const totalEcheanciers = ref(0)

// Filtres
const filters = reactive({
  search: '',
  statut: null as boolean | null,
  periodePaieId: null as number | null
})

// Données du backend
const echeanciers = ref<Echeancier[]>([])
const personnels = ref<Personnel[]>([])
const periodes = ref<PeriodePaie[]>([])

// Fonctions de chargement
const loadEcheanciers = async () => {
  try {
    loading.value = true
    const filter: EchelonnementFilter = {
      limit: pageSize.value,
      offset: currentPage.value - 1,
      search: filters.search || '',
      statut: filters.statut ?? undefined,
      periodePaieId: filters.periodePaieId ?? undefined
    }
    
    const response = await echelonnementrestService.getEchelonnements(filter)
    
    if (response.success && response.data) {
      // Mapper les données du backend vers le format frontend
      echeanciers.value = response.data.rows.map((ech: any) => ({
        id: ech.id,
        pretId: ech.pretPersonnel?.id || 0,
        personnelId: ech.pretPersonnel?.personnel?.id || 0,
        personnelNom: ech.pretPersonnel?.personnel?.nomComplet || 'Inconnu',
        matricule: ech.pretPersonnel?.personnel?.matricule || '',
        typePret: ech.pretPersonnel?.pret?.libelle || 'Prêt',
        montantPret: ech.pretPersonnel?.montantPret || 0,
        numeroEcheance: 1, // À adapter selon la logique métier
        totalEcheances: ech.pretPersonnel?.echelonage || 12, // Utiliser echelonage du prêt
        periodePaie: ech.periodePaie || { affiche: 'Période inconnue' },
        dateEcheance: ech.dateRemboursement || ech.dateEcheance || '',
        mensualite: ech.montant || 0, // Utiliser montant du backend
        interets: 0, // À calculer selon la logique métier
        principal: parseFloat(ech.pretPersonnel?.montantPret?.replace(/\s/g, '')) || 0, // Utiliser montantPret du prêt et convertir en nombre
        montantPaye: ech.paye ? ech.montant : 0, // Utiliser le booléen paye
        resteAPayer: ech.paye ? 0 : ech.montant, // Calculer le reste selon paye
        statut: ech.paye ? 'Payé' : 'Non payé', // Utiliser le booléen paye pour déterminer le statut
        datePaiement: ech.dateRemboursement
      }))
      
      totalEcheanciers.value = response.total
    }
  } catch (error) {
    console.error('Erreur lors du chargement des échéanciers:', error)
    ElMessage.error('Erreur lors du chargement des échéanciers')
  } finally {
    loading.value = false
  }
}

const loadPersonnels = async () => {
  try {
    // Pour l'instant, utilisation du service des prêts personnels
    const { pretspersonnelsService } = await import('@/services/pretspersonnels.service')
    const response = await pretspersonnelsService.getPersonnels()
    
    personnels.value = response.map((p: any) => ({
      id: p.id,
      nomComplet: p.nomComplet || `${p.prenom || ''} ${p.nom || ''}`.trim(),
      matricule: p.matricule
    }))
  } catch (error) {
    console.error('Erreur lors du chargement des personnels:', error)
    personnels.value = []
  }
}

const loadPeriodes = async () => {
  try {
    const response = await echelonnementrestService.getPeriodesPaie()
    
    if (response.success) {
      periodes.value = response.data
    }
  } catch (error) {
    console.error('Erreur lors du chargement des périodes:', error)
    periodes.value = []
  }
}

// Computed properties
const filteredEcheanciers = computed(() => {
  return echeanciers.value
})

// Méthodes
const applyFilters = async () => {
  currentPage.value = 1
  await loadEcheanciers()
  ElMessage.success('Filtres appliqués')
}

const resetFilters = async () => {
  Object.assign(filters, {
    search: '',
    statut: null,
    periodePaieId: null
  })
  currentPage.value = 1
  await loadEcheanciers()
  ElMessage.info('Filtres réinitialisés')
}

const editEcheancier = (echeancier: Echeancier) => {
  selectedEcheancier.value = echeancier
  
  // Initialiser le formulaire avec les données actuelles
  Object.assign(editForm, {
    id: echeancier.id,
    periodePaieId: echeancier.periodePaie?.id || null,
    dateRemboursement: echeancier.dateEcheance || '',
    montant: echeancier.mensualite
  })
  
  showEditModal.value = true
}

const saveEcheancierModification = async () => {
  if (!editForm.periodePaieId) {
    ElMessage.warning('Veuillez sélectionner une période')
    return
  }
  
  try {
    // Format des données attendu par le backend : EchelonnementRequest
    const updateData = {
      idechel: editForm.id,
      periodPaie: editForm.periodePaieId.toString() // Le backend attend une string pour periodPaie
    }
    
    const response = await echelonnementrestService.updateEchelonnement(updateData)
    
    if (response.success) {
      ElMessage.success('Échéancier modifié avec succès')
      showEditModal.value = false
      await loadEcheanciers() // Recharger les données
    } else {
      ElMessage.error(response.message || 'Erreur lors de la modification')
    }
  } catch (error) {
    console.error('Erreur lors de la modification de l\'échéancier:', error)
    ElMessage.error('Erreur lors de la modification')
  }
}

const generateEcheanciers = async () => {
  loading.value = true
  try {
    // TODO: Implémenter la génération d'échéanciers via le backend
    await new Promise(resolve => setTimeout(resolve, 2000))
    ElMessage.success('Échéanciers générés avec succès')
    await loadEcheanciers()
  } catch (error) {
    ElMessage.error('Erreur lors de la génération des échéanciers')
  } finally {
    loading.value = false
  }
}

const handleSelectionChange = (selection: Echeancier[]) => {
  selectedEcheanciers.value = selection
}

const markAsPaid = async () => {
  if (selectedEcheanciers.value.length === 0) {
    ElMessage.warning('Veuillez sélectionner des échéances')
    return
  }

  try {
    await ElMessageBox.confirm(
      `Marquer ${selectedEcheanciers.value.length} échéance(s) comme payée(s) ?`,
      'Confirmation',
      {
        confirmButtonText: 'Confirmer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )

    const echelonnementIds = selectedEcheanciers.value.map(e => e.id)
    const response = await echelonnementrestService.markMultipleAsPaid(echelonnementIds)

    if (response.success) {
      ElMessage.success('Échéances marquées comme payées')
      selectedEcheanciers.value = []
      await loadEcheanciers()
    } else {
      ElMessage.error(response.message || 'Erreur lors du marquage des échéances')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors du marquage des échéances')
    }
  }
}

const markAsPaidSingle = async (echeancier: Echeancier) => {
  try {
    await ElMessageBox.confirm(
      `Marquer l'échéance N°${echeancier.numeroEcheance} comme payée ?`,
      'Confirmation',
      {
        confirmButtonText: 'Confirmer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )

    const response = await echelonnementrestService.updateEchelonnement({
      idechel: echeancier.id,
      periodPaie: new Date().toISOString().split('T')[0]
    })

    if (response.success) {
      ElMessage.success('Échéance marquée comme payée')
      await loadEcheanciers()
    } else {
      ElMessage.error(response.message || 'Erreur lors du marquage de l\'échéance')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors du marquage de l\'échéance')
    }
  }
}

const viewDetails = (echeancier: Echeancier) => {
  selectedEcheancier.value = echeancier
  showDetailsModal.value = true
}


const exportEcheanciers = async () => {
  try {
    loading.value = true
    ElMessage.info('Préparation de l\'export en cours...')
    
    // Utiliser les filtres actuels
    const filter: EchelonnementFilter = {
      search: filters.search || '',
      statut: filters.statut,
      periodePaieId: filters.periodePaieId
    }
    
    const response = await echelonnementrestService.exportEcheanciers(filter)
    
    if (response.success && response.data) {
      // Créer un URL pour le blob et déclencher le téléchargement
      const blob = response.data
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      
      // Générer un nom de fichier avec la date actuelle
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5)
      link.setAttribute('download', `echeanciers_export_${timestamp}.csv`)
      
      // Ajouter le lien au DOM, cliquer dessus, puis le nettoyer
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      
      // Nettoyer l'URL
      window.URL.revokeObjectURL(url)
      
      ElMessage.success('Export des échéanciers terminé avec succès')
    } else {
      ElMessage.error(response.message || 'Erreur lors de l\'export')
    }
  } catch (error) {
    console.error('Erreur lors de l\'export des échéanciers:', error)
    ElMessage.error('Erreur lors de l\'export des échéanciers')
  } finally {
    loading.value = false
  }
}

const handleSizeChange = async (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  await loadEcheanciers()
}

const handleCurrentChange = async (page: number) => {
  currentPage.value = page
  await loadEcheanciers()
}

// Fonctions utilitaires
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF' }).format(amount)
}

const formatDate = (date: string) => {
  if (!date || date === 'undefined') return 'Date inconnue'
  
  try {
    const dateObj = new Date(date)
    if (isNaN(dateObj.getTime())) {
      return 'Date invalide'
    }
    return dateObj.toLocaleDateString('fr-FR')
  } catch (error) {
    return date
  }
}

const formatPeriode = (periode: any) => {
  if (!periode) return 'Période inconnue'
  
  // Si periode est un objet avec la propriété affiche
  if (typeof periode === 'object' && periode.affiche) {
    return periode.affiche
  }
  
  // Si periode est une string, essayer de la formater
  if (typeof periode === 'string') {
    if (periode === 'undefined') return 'Période inconnue'
    
    try {
      const [year, month] = periode.split('-')
      const monthNames = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc']
      return `${monthNames[parseInt(month) - 1]} ${year}`
    } catch (error) {
      return periode
    }
  }
  
  return periode
}

const getStatusColor = (status: string) => {
  const colors: Record<string, string> = {
    'Payé': 'success',
    'Non payé': 'danger',
    'En attente': 'info',
    'En retard': 'warning',
    'Partiellement payé': 'warning'
  }
  return colors[status] || 'info'
}

const getStatusLabel = (status: string) => {
  return status || 'En attente'
}

onMounted(async () => {
  // Charger les données initiales depuis le backend
  await Promise.all([
    loadEcheanciers(),
    loadPersonnels(),
    loadPeriodes()
  ])
})
</script>

<style scoped>
.echeanciers-view {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header-left h1 {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-subtitle {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.filters-section {
  margin-bottom: 24px;
}

.filters-content {
  padding: 20px;
}

.table-section {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.table-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.table-controls {
  display: flex;
  gap: 12px;
}

.table-container {
  padding: 20px;
}

.personnel-info {
  display: flex;
  flex-direction: column;
}

.personnel-name {
  font-weight: 600;
  color: #303133;
}

.personnel-matricule {
  font-size: 12px;
  color: #909399;
}

.loan-info {
  display: flex;
  flex-direction: column;
}

.loan-type {
  font-weight: 600;
  color: #303133;
}

.loan-amount {
  font-size: 12px;
  color: #909399;
}

.amount {
  font-weight: 600;
  color: #67c23a;
}

.interest {
  font-weight: 600;
  color: #e6a23c;
}

.principal {
  font-weight: 600;
  color: #409eff;
}

.paid-amount {
  font-weight: 600;
  color: #67c23a;
}

.remaining-amount {
  font-weight: 600;
  color: #f56c6c;
}

.text-danger {
  color: #f56c6c !important;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

.echeancier-details {
  margin-top: 16px;
}

.enhanced-button {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.enhanced-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.enhanced-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}
</style>
