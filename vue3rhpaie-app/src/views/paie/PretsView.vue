<template>
  <div class="prets-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><CreditCard /></el-icon>
            Gestion des Prêts
          </h1>
          <p class="page-subtitle">Suivi et gestion des prêts du personnel</p>
        </div>
        <div class="header-right">
          <el-button type="primary" @click="showAddModal = true" class="enhanced-button">
            <el-icon><Plus /></el-icon>
            Nouveau Prêt
          </el-button>
        </div>
      </div>
    </div>

    <!-- Layout avec sidebar et contenu principal -->
    <div class="content-layout">
      <!-- Sidebar pour le formulaire -->
      <div class="sidebar-panel enhanced-card" v-if="showAddModal">
        <div class="panel-header">
          <h3>{{ editingLoan ? 'Modifier un Prêt' : 'Ajouter un Prêt' }}</h3>
          <el-button @click="closeModal" circle size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <div class="form-content">
          <el-form :model="loanForm" :rules="loanRules" ref="loanFormRef" label-width="140px" size="large">
            <el-form-item label="Personnel" prop="personnelId" required>
              <el-select 
                v-model="loanForm.personnelId" 
                placeholder="Sélectionner le personnel" 
                style="width: 100%" 
                filterable
                clearable
                :filter-method="filterPersonnels"
                @clear="clearPersonnelFilter"
              >
                <el-option
                  v-for="personnel in (filteredPersonnels.length > 0 ? filteredPersonnels : personnels)"
                  :key="personnel.id"
                  :label="`${personnel.nomComplet || personnel.Nom || personnel.nom || 'Nom inconnu'} - ${personnel.matricule}`"
                  :value="personnel.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Type de Prêt" prop="type" required>
              <el-select v-model="loanForm.type" placeholder="Type de prêt" style="width: 100%">
                <el-option label="Prêt " value="1" />
                <el-option label="Avances & Acomptes" value="2" />
                <el-option label="Prêt Alios" value="3" />
              
              </el-select>
            </el-form-item>

            <el-form-item label="Montant" prop="montant" required>
              <el-input-number 
                v-model="loanForm.montant" 
                :min="0" 
                :max="10000000"
                :step="10000"
                style="width: 100%"
                placeholder="Montant du prêt"
                :formatter="(value: number) => `${value.toLocaleString()} XOF`"
                :parser="(value: string) => value.replace(/[^\d]/g, '')"
              />
            </el-form-item>

            <el-form-item label="Date Contraction" prop="dateContraction" required>
              <el-date-picker
                v-model="loanForm.dateContraction"
                type="date"
                placeholder="Date de contraction"
                style="width: 100%"
                format="DD/MM/YYYY"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>

            <el-form-item label="Période Début Prélèvement" prop="periodeDebut" required>
              <el-select 
                v-model="loanForm.periodeDebut" 
                placeholder="Période de début" 
                style="width: 100%"
                filterable
                clearable
                :filter-method="filterPeriodes"
                @clear="clearPeriodeFilter"
              >
                <el-option
                  v-for="periode in filteredPeriodes"
                  :key="periode.value"
                  :label="periode.label"
                  :value="periode.value"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Durée (mois)" prop="duree" required>
              <el-input-number
                v-model="loanForm.duree"
                :min="1"
                :max="120"
                style="width: 100%"
                placeholder="Durée en mois"
              />
            </el-form-item>

         

          

            <el-form-item label="Motif" prop="motif">
              <el-input
                v-model="loanForm.motif"
                type="textarea"
                :rows="3"
                placeholder="Motif du prêt"
              />
            </el-form-item>

            <el-form-item label="Statut" prop="statut">
              <el-radio-group v-model="loanForm.statut">
                <el-radio value="en_cours">En cours</el-radio>
                <el-radio value="termine">Terminé</el-radio>
                <el-radio value="suspendu">Suspendu</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveLoan" :loading="formLoading">
                {{ editingLoan ? 'Mettre à jour' : 'Enregistrer' }}
              </el-button>
              <el-button @click="closeModal">Annuler</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- Contenu principal avec le tableau -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Prêts</h3>
          <div class="panel-controls">
            <el-input
              v-model="searchQuery"
              placeholder="Rechercher un prêt..."
              prefix-icon="Search"
              style="width: 300px"
              clearable
            />
            <el-select v-model="filterStatus" placeholder="Filtrer par statut" style="width: 150px" clearable>
              <el-option label="En cours" value="en_cours" />
              <el-option label="Terminé" value="termine" />
              <el-option label="Suspendu" value="suspendu" />
            </el-select>
          </div>
        </div>

        <div class="table-container">
          <el-table :data="filteredLoans" stripe v-loading="loading" @sort-change="handleSortChange">
            <el-table-column label="Personnel" prop="personnelNom" min-width="250" sortable="custom">
              <template #default="{ row }">
                <div class="personnel-info">
                  <div class="personnel-avatar">
                    <el-avatar :size="40" :src="row.personnel?.photo">
                      {{ row.personnel?.nomComplet?.charAt(0) || 'P' }}
                    </el-avatar>
                  </div>
                  <div class="personnel-details">
                    <div class="personnel-name">{{ row.personnel?.nomComplet || row.personnel.Nom }}</div>
                    <div class="personnel-matricule">{{ row.personnel?.matricule || row.matricule }}</div>
                    <div class="personnel-id">ID: {{ row.personnel.id }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="Type" prop="type" width="120" sortable="custom">
              <template #default="{ row }">
                <el-tag :type="getTypeColor(row.pret?.libelle)">
                  {{ getTypeLabel(row.pret?.libelle) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column label="Montant" prop="montant" width="120" sortable="custom">
              <template #default="{ row }">
                <span class="amount">{{ formatCurrency(row.montant) }}</span>
              </template>
            </el-table-column>

            <el-table-column label="Date Contraction" prop="dateContraction" width="140" sortable="custom">
              <template #default="{ row }">
                {{ formatDate(row.dateContraction) }}
              </template>
            </el-table-column>

            <el-table-column label="Période Départ" prop="periodeDebut" width="140" sortable="custom">
              <template #default="{ row }">
                {{ row.periodeDebut }}
              </template>
            </el-table-column>

            <el-table-column label="Durée" prop="duree" width="80" sortable="custom">
              <template #default="{ row }">
                {{ row.duree }} mois
              </template>
            </el-table-column>

            <el-table-column label="Mensualité" prop="mensualite" width="120" sortable="custom">
              <template #default="{ row }">
                <span class="amount">{{ formatCurrency(row.mensualite) }}</span>
              </template>
            </el-table-column>

            <el-table-column label="Reste à payer" prop="resteAPayer" width="130" sortable="custom">
              <template #default="{ row }">
                <span class="amount" :class="{ 'text-danger': row.resteAPayer > 0 }">
                  {{ formatCurrency(row.resteAPayer) }}
                </span>
              </template>
            </el-table-column>

            <el-table-column label="Statut" prop="statut" width="100" sortable="custom">
              <template #default="{ row }">
                <el-tag :type="getStatusColor(row.statut)">
                  {{ getStatusLabel(row.statut) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column label="Actions" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="editLoan(row)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button type="info" size="small" @click="viewLoanDetails(row)">
                  <el-icon><View /></el-icon>
                </el-button>
                <el-button type="danger" size="small" @click="deleteLoan(row)">
                  <el-icon><Delete /></el-icon>
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
              :total="filteredLoans.length"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de détails -->
    <el-dialog v-model="showDetailsModal" title="Détails du Prêt" width="600px">
      <div v-if="selectedLoan" class="loan-details">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Personnel">
            {{ selectedLoan.personnelNom }} ({{ selectedLoan.matricule }})
          </el-descriptions-item>
          <el-descriptions-item label="Type">
            <el-tag :type="getTypeColor(selectedLoan.type)">
              {{ getTypeLabel(selectedLoan.type) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Montant">
            <span class="amount">{{ formatCurrency(selectedLoan.montant) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Date Contraction">
            {{ formatDate(selectedLoan.dateContraction) }}
          </el-descriptions-item>
          <el-descriptions-item label="Durée">
            {{ selectedLoan.duree }} mois
          </el-descriptions-item>
          <el-descriptions-item label="Taux Intérêt">
            {{ selectedLoan.tauxInteret }}%
          </el-descriptions-item>
          <el-descriptions-item label="Mensualité">
            <span class="amount">{{ formatCurrency(selectedLoan.mensualite) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Reste à payer">
            <span class="amount text-danger">{{ formatCurrency(selectedLoan.resteAPayer) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Statut" span="2">
            <el-tag :type="getStatusColor(selectedLoan.statut)">
              {{ getStatusLabel(selectedLoan.statut) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Motif" span="2" v-if="selectedLoan.motif">
            {{ selectedLoan.motif }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  CreditCard, Plus, Close, Edit, View, Delete, Search 
} from '@element-plus/icons-vue'
import { 
  pretspersonnelsService, 
  type PretPersonnelResponse, 
  type PretPersonnelRequest,
  type Personnel,
  type PeriodePaie
} from '@/services/pretspersonnels.service'

interface Loan {
  id: number
  personnelId: number
  personnelNom: string
  matricule: string
  type: string
  montant: number
  dateContraction: string
  periodeDebut: string
  duree: number
  tauxInteret: number
  mensualite: number
  resteAPayer: number
  motif: string
  statut: string
}

interface Personnel {
  id: number
  matricule: string
  nomComplet: string
}

// Données réactives
const loading = ref(false)
const formLoading = ref(false)
const showAddModal = ref(false)
const showDetailsModal = ref(false)
const editingLoan = ref(false)
const selectedLoan = ref<Loan | null>(null)
const searchQuery = ref('')
const filterStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const sortBy = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')

// Formulaire de prêt
const loanForm = reactive({
  id: 0,
  personnelId: 0,
  type: '',
  montant: 0,
  dateContraction: '',
  periodeDebut: '',
  duree: 12,
  tauxInteret: 5,
  mensualite: 0,
  motif: '',
  statut: 'en_cours'
})

// Règles de validation
const loanRules = {
  personnelId: [{ required: true, message: 'Veuillez sélectionner le personnel', trigger: 'change' }],
  type: [{ required: true, message: 'Veuillez sélectionner le type de prêt', trigger: 'change' }],
  montant: [{ required: true, message: 'Veuillez saisir le montant', trigger: 'blur' }],
  dateContraction: [{ required: true, message: 'Veuillez sélectionner la date', trigger: 'change' }],
  periodeDebut: [{ required: true, message: 'Veuillez sélectionner la période', trigger: 'change' }],
  duree: [{ required: true, message: 'Veuillez saisir la durée', trigger: 'blur' }]
}

const loanFormRef = ref()

// Données depuis le backend
const loans = ref<Loan[]>([])
const personnels = ref<Personnel[]>([])
const periodes = ref<{ id: number; value: string; label: string }[]>([])
const totalLoans = ref(0)
const testing = ref(false)

// Variables pour le filtrage des sélects
const personnelSearchQuery = ref('')
const periodeSearchQuery = ref('')

// Données filtrées pour les sélects
const filteredPersonnels = computed(() => {
  if (!personnelSearchQuery.value) return personnels.value
  
  const query = personnelSearchQuery.value.toLowerCase()
  return personnels.value.filter(personnel => {
    // Gérer différentes structures de données pour le nom
    const nomComplet = personnel.nomComplet || personnel.Nom || personnel.nom || ''
    const matricule = personnel.matricule || ''
    
    return nomComplet.toLowerCase().includes(query) ||
           matricule.toLowerCase().includes(query)
  })
})

const filteredPeriodes = computed(() => {
  if (!periodeSearchQuery.value) return periodes.value
  
  const query = periodeSearchQuery.value.toLowerCase()
  return periodes.value.filter(periode => 
    String(periode.label).toLowerCase().includes(query) ||
    String(periode.value).toLowerCase().includes(query)
  )
})

const filteredLoans = computed(() => {
  let filtered = loans.value

  // Filtrage par recherche
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(loan => 
      loan.personnelNom.toLowerCase().includes(query) ||
      loan.matricule.toLowerCase().includes(query) ||
      loan.type.toLowerCase().includes(query)
    )
  }

  // Filtrage par statut
  if (filterStatus.value) {
    filtered = filtered.filter(loan => loan.statut === filterStatus.value)
  }

  // Tri
  if (sortBy.value) {
    filtered.sort((a, b) => {
      const aVal = a[sortBy.value as keyof Loan]
      const bVal = b[sortBy.value as keyof Loan]
      
      if (sortOrder.value === 'asc') {
        return aVal > bVal ? 1 : -1
      } else {
        return aVal < bVal ? 1 : -1
      }
    })
  }

  return filtered
})

// Watchers
watch(() => [loanForm.montant, loanForm.duree, loanForm.tauxInteret], () => {
  calculateMensualite()
}, { deep: true })

// Méthodes
const calculateMensualite = () => {
  if (loanForm.montant && loanForm.duree && loanForm.tauxInteret) {
    const principal = loanForm.montant
    const monthlyRate = loanForm.tauxInteret / 100 / 12
    const months = loanForm.duree
    
    if (monthlyRate === 0) {
      loanForm.mensualite = principal / months
    } else {
      loanForm.mensualite = principal * (monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1)
    }
    
    // Arrondir la mensualité
    loanForm.mensualite = Math.round(loanForm.mensualite)
  }
}

// Fonction pour calculer le reste à payer en fonction des paiements effectués
const calculateResteAPayer = (montant: number, mensualite: number, duree: number, moisPayes: number = 0) => {
  const totalPaiement = mensualite * duree
  const montantDejaPaye = mensualite * moisPayes
  const reste = Math.max(0, totalPaiement - montantDejaPaye)
  
  // S'assurer que le reste à payer ne dépasse pas le montant initial
  return Math.min(reste, montant)
}

const openAddModal = () => {
  showAddModal.value = true
  editingLoan.value = false
  resetForm()
}

const closeModal = () => {
  showAddModal.value = false
  editingLoan.value = false
  resetForm()
}

const resetForm = () => {
  Object.assign(loanForm, {
    id: 0,
    personnelId: 0,
    type: '',
    montant: 0,
    dateContraction: '',
    periodeDebut: '',
    duree: 12,
    tauxInteret: 5,
    mensualite: 0,
    motif: '',
    statut: 'en_cours'
  })
  loanFormRef.value?.resetFields()
}

const saveLoan = async () => {
  if (!loanFormRef.value) return
  
  try {
    await loanFormRef.value.validate()
    formLoading.value = true

    const personnel = personnels.value.find(p => p.id === loanForm.personnelId)
    const periodeId = parseInt(String(loanForm.periodeDebut), 10)

    if (Number.isNaN(periodeId)) {
      ElMessage.error('Période de début invalide')
      return
    }
    
    // Préparation de la requête pour le backend
    const request: PretPersonnelRequest = {
      montant: loanForm.montant,
      echelonage: loanForm.duree,
      pret: parseInt(loanForm.type), // Type de prêt depuis le formulaire
      idPret: loanForm.id, // ID du prêt pour la modification
      idpers: loanForm.personnelId,
      dEmprunt: loanForm.dateContraction,
      periodepaie: periodeId, // Utiliser l'ID de la période
      statut: loanForm.statut // Ajout du statut du prêt
    }

    let result: PretPersonnelResponse
    
    if (editingLoan.value && loanForm.id) {
      // Modification
      result = await pretspersonnelsService.updatePretPersonnel(loanForm.id, request)
      
      // Mise à jour locale
      const index = loans.value.findIndex(l => l.id === loanForm.id)
      if (index !== -1) {
        loans.value[index] = {
          id: loanForm.id,
          personnelId: loanForm.personnelId,
          personnelNom: personnel?.nomComplet || '',
          matricule: personnel?.matricule || '',
          type: loanForm.type,
          pret: { id: parseInt(loanForm.type), libelle: getTypeLabel(loanForm.type) },
          personnel: personnel,
          montant: loanForm.montant,
          dateContraction: loanForm.dateContraction,
          periodeDebut: loanForm.periodeDebut,
          duree: loanForm.duree,
          tauxInteret: loanForm.tauxInteret,
          mensualite: loanForm.mensualite,
          resteAPayer: loanForm.mensualite * loanForm.duree,
          motif: loanForm.motif,
          statut: loanForm.statut
        }
      }
      ElMessage.success('Prêt modifié avec succès')
    } else {
      // Ajout
      result = await pretspersonnelsService.savePretPersonnel(request)
      ElMessage.success('Prêt ajouté avec succès')
    }

    // Recharger la liste des prêts depuis le backend
    await loadLoans()
    closeModal()
  } catch (error) {
    console.error('Erreur lors de la sauvegarde:', error)
    ElMessage.error('Erreur lors de la sauvegarde du prêt')
  } finally {
    formLoading.value = false
  }
}

// Fonctions de chargement depuis le backend
const loadLoans = async () => {
  try {
    loading.value = true
    const response = await pretspersonnelsService.getPretsPersonnels(20, 0, searchQuery.value || undefined)
    
    // Transformation des données backend vers le format frontend
    console.log('=== DÉBOGAGE LOAD LOANS ===')
    console.log('Response brute:', response)
    console.log('Response rows:', response.rows)
    
    // Le backend renvoie response.rows[0] qui est un tableau de prêts
    const allLoans = response.rows[0] || []
    
    loans.value = allLoans.map((pretData: any, index) => {
      console.log(`Mapping prêt ${index}:`, pretData)
      
      // Gérer la période - utiliser l'objet periode du backend
      let periodeDebut = '2024-01' // Valeur par défaut
      if (pretData.periode?.affiche) {
        periodeDebut = pretData.periode.affiche
      }
      
      // Gérer les valeurs nulles du backend
      const personnelNom = pretData.personnel?.nomComplet || `Personnel ${pretData.idpers}`
      const matricule = pretData.personnel?.matricule || `EMP${pretData.idpers}`
      const dateContraction = pretData.dEmprunt || pretData.dateEmprunt || new Date().toISOString().split('T')[0]
      
      const mappedLoan = {
        id: pretData.id,
        personnelId: pretData.idpers,
        personnelNom: personnelNom,
        matricule: matricule,
        type: 'personnel', // Type par défaut
        pret: pretData.pret, // Objet pret du backend pour le tableau
        personnel: pretData.personnel, // Objet personnel du backend pour le tableau
        montant: pretData.montant,
        dateContraction: dateContraction,
        periodeDebut: periodeDebut,
        duree: pretData.echelonage,
        tauxInteret: 5, // Taux par défaut
        mensualite: Math.round(pretData.montant / pretData.echelonage), // Calcul simple
        resteAPayer: pretData.montant, // Simplifié
        motif: pretData.libelle || '',
        statut: 'en_cours' // Statut par défaut
      }
      
      console.log(`Prêt mappé ${index}:`, mappedLoan)
      return mappedLoan
    })
    
    console.log('Loans après mapping:', loans.value)
    console.log('=== FIN DÉBOGAGE LOAD LOANS ===')
    
    totalLoans.value = response.total
  } catch (error) {
    console.error('Erreur lors du chargement des prêts:', error)
    ElMessage.error('Erreur lors du chargement des prêts')
  } finally {
    loading.value = false
  }
}

const loadPersonnels = async () => {
  try {
    ElMessage.info('Chargement des personnels en cours...')
    const response = await pretspersonnelsService.getPersonnels()
    
    // Charger tout le personnel sans limitation
    personnels.value = response.map((p: Personnel) => ({
      id: p.id,
      matricule: p.matricule,
      nomComplet: p.nomComplet || `${p.prenom || ''} ${p.nom || ''}`.trim()
    }))
    
    ElMessage.success(`${personnels.value.length} personnels chargés`)
  } catch (error) {
    console.error('Erreur lors du chargement des personnels:', error)
    if (error.message?.includes('timeout')) {
      ElMessage.error('Le chargement des personnels prend trop de temps. Veuillez réessayer.')
    } else {
      ElMessage.error('Erreur lors du chargement des personnels')
    }
  }
}

const loadPeriodes = async () => {
  try {
    ElMessage.info('Chargement des périodes de paie...')
    const response = await pretspersonnelsService.getPeriodesPaie()
    
    if (response && response.length > 0) {
      periodes.value = response.map((p: any) => {
        const label = p.affiche || p.mois || p.libelle || 'Période inconnue'
        
        return {
          id: p.id,
          value: String(p.id),
          label: label
        }
      })
      ElMessage.success(`${periodes.value.length} périodes chargées`)
    } else {
      // Données par défaut si le backend ne renvoie rien
      const currentYear = new Date().getFullYear()
      periodes.value = [
        { id: 1, value: `${currentYear}01`, label: 'Janvier 2024' },
        { id: 2, value: `${currentYear}02`, label: 'Février 2024' },
        { id: 3, value: `${currentYear}03`, label: 'Mars 2024' },
        { id: 4, value: `${currentYear}04`, label: 'Avril 2024' },
        { id: 5, value: `${currentYear}05`, label: 'Mai 2024' },
        { id: 6, value: `${currentYear}06`, label: 'Juin 2024' },
        { id: 7, value: `${currentYear}07`, label: 'Juillet 2024' },
        { id: 8, value: `${currentYear}08`, label: 'Août 2024' },
        { id: 9, value: `${currentYear}09`, label: 'Septembre 2024' },
        { id: 10, value: `${currentYear}10`, label: 'Octobre 2024' },
        { id: 11, value: `${currentYear}11`, label: 'Novembre 2024' },
        { id: 12, value: `${currentYear}12`, label: 'Décembre 2024' }
      ]
      ElMessage.warning('Utilisation des périodes par défaut')
    }
  } catch (error) {
    console.error('Erreur lors du chargement des périodes:', error)
    
    // Données par défaut en cas d'erreur
    const currentYear = new Date().getFullYear()
    periodes.value = [
      { id: 1, value: `${currentYear}01`, label: 'Janvier 2024' },
      { id: 2, value: `${currentYear}02`, label: 'Février 2024' },
      { id: 3, value: `${currentYear}03`, label: 'Mars 2024' },
      { id: 4, value: `${currentYear}04`, label: 'Avril 2024' },
      { id: 5, value: `${currentYear}05`, label: 'Mai 2024' },
      { id: 6, value: `${currentYear}06`, label: 'Juin 2024' },
      { id: 7, value: `${currentYear}07`, label: 'Juillet 2024' },
      { id: 8, value: `${currentYear}08`, label: 'Août 2024' },
      { id: 9, value: `${currentYear}09`, label: 'Septembre 2024' },
      { id: 10, value: `${currentYear}10`, label: 'Octobre 2024' },
      { id: 11, value: `${currentYear}11`, label: 'Novembre 2024' },
      { id: 12, value: `${currentYear}12`, label: 'Décembre 2024' }
    ]
    
    if (error.message?.includes('timeout')) {
      ElMessage.error('Le chargement des périodes prend trop de temps. Utilisation des données par défaut.')
    } else {
      ElMessage.error('Erreur lors du chargement des périodes. Utilisation des données par défaut.')
    }
  }
}

// Fonction utilitaire pour convertir le nom du mois en numéro
const getMoisNumero = (moisNom: string): number => {
  const moisMap: Record<string, number> = {
    'JANVIER': 1, 'JANVARY': 1,
    'FEVRIER': 2, 'FEBRUARY': 2,
    'MARS': 3, 'MARCH': 3,
    'AVRIL': 4, 'APRIL': 4,
    'MAI': 5, 'MAY': 5,
    'JUIN': 6, 'JUNE': 6,
    'JUILLET': 7, 'JULY': 7,
    'AOUT': 8, 'AUGUST': 8,
    'SEPTEMBRE': 9, 'SEPTEMBER': 9,
    'OCTOBRE': 10, 'OCTOBER': 10,
    'NOVEMBRE': 11, 'NOVEMBER': 11,
    'DECEMBRE': 12, 'DECEMBER': 12
  }
  return moisMap[moisNom.toUpperCase()] || 1
}

const editLoan = (loan: Loan) => {
  console.log('=== DÉBOGAGE EDIT LOAN ===')
  console.log('Prêt à modifier:', loan)
  console.log('loan.pret:', loan.pret)
  console.log('loan.personnel:', loan.personnel)
  console.log('personnels.value (list):', personnels.value)
  console.log('filteredPersonnels.value:', filteredPersonnels.value)
  console.log('personnels.value.length:', personnels.value.length)
  
  // Mapper les données du prêt vers le formulaire
  loanForm.id = loan.id
  loanForm.personnelId = loan.personnel?.id || loan.personnelId
  loanForm.type = loan.pret?.id?.toString() || loan.type
  loanForm.montant = loan.montant
  loanForm.dateContraction = loan.dateContraction
  const matchedPeriode = periodes.value.find(
    p => p.value === loan.periodeDebut || p.label === loan.periodeDebut
  )
  loanForm.periodeDebut = matchedPeriode ? matchedPeriode.value : loan.periodeDebut
  loanForm.duree = loan.duree
  loanForm.tauxInteret = loan.tauxInteret || 5
  loanForm.mensualite = loan.mensualite
  loanForm.motif = loan.motif || ''
  loanForm.statut = loan.statut || 'en_cours'
  
  console.log('loanForm après mapping:', loanForm)
  console.log('loanForm.personnelId:', loanForm.personnelId)
  console.log('loanForm.type:', loanForm.type)
  console.log('loanForm.montant:', loanForm.montant)
  console.log('loanForm.dateContraction:', loanForm.dateContraction)
  console.log('loanForm.periodeDebut:', loanForm.periodeDebut)
  console.log('=== FIN DÉBOGAGE EDIT LOAN ===')
  
  editingLoan.value = true
  showAddModal.value = true
}

const deleteLoan = async (loan: Loan) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer le prêt de ${loan.personnelNom} ?`,
      'Confirmation de suppression',
      {
        confirmButtonText: 'Supprimer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )

    await pretspersonnelsService.deletePretPersonnel(loan.id)
    
    const index = loans.value.findIndex(l => l.id === loan.id)
    if (index !== -1) {
      loans.value.splice(index, 1)
      ElMessage.success('Prêt supprimé avec succès')
    }
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
    ElMessage.error('Erreur lors de la suppression du prêt')
  }
}

const viewLoanDetails = (loan: Loan) => {
  selectedLoan.value = loan
  showDetailsModal.value = true
}

const handleSortChange = ({ prop, order }: { prop: string; order: string | null }) => {
  sortBy.value = prop
  sortOrder.value = order === 'ascending' ? 'asc' : 'desc'
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
}

// Fonctions utilitaires
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF' }).format(amount)
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString('fr-FR')
}

const getTypeColor = (type: string) => {
  if (!type) return 'info' // Valeur par défaut si type est vide ou undefined
  
  const colors: Record<string, string> = {
    personnel: 'primary',
    logement: 'success',
    vehicule: 'warning',
    etudes: 'info',
    urgence: 'danger',
    PRET: 'primary', // Ajout pour le type de prêt réel
    'AVANCES & ACOMPTES': 'primary', // Type réel du backend
    autre: 'info'
  }
  return colors[type] || 'info'
}

const getTypeLabel = (type: string) => {
  if (!type) return 'Non spécifié' // Valeur par défaut si type est vide ou undefined
  
  const labels: Record<string, string> = {
    personnel: 'Personnel',
    logement: 'Logement',
    vehicule: 'Véhicule',
    etudes: 'Études',
    urgence: 'Urgence',
    PRET: 'Prêt Personnel', // Ajout pour le type de prêt réel
    'AVANCES & ACOMPTES': 'Avances & Comptes', // Type réel du backend
    autre: 'Autre'
  }
  return labels[type] || 'Autre'
}

const getStatusColor = (status: string) => {
  const colors: Record<string, string> = {
    en_cours: 'success',
    termine: 'info',
    suspendu: 'warning'
  }
  return colors[status] || ''
}

const getStatusLabel = (status: string) => {
  const labels: Record<string, string> = {
    en_cours: 'En cours',
    termine: 'Terminé',
    suspendu: 'Suspendu'
  }
  return labels[status] || status
}

// Fonctions de filtrage pour les sélects
const filterPersonnels = (query: string) => {
  personnelSearchQuery.value = query
}

const clearPersonnelFilter = () => {
  personnelSearchQuery.value = ''
}

const filterPeriodes = (query: string) => {
  periodeSearchQuery.value = query
}

const clearPeriodeFilter = () => {
  periodeSearchQuery.value = ''
}

onMounted(async () => {
  // Charger les données depuis le backend
  await Promise.all([
    loadLoans(),
    loadPersonnels(),
    loadPeriodes()
  ])
})
</script>

<style scoped>
.prets-view {
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

.content-layout {
  display: flex;
  gap: 24px;
  height: calc(100vh - 200px);
}

.sidebar-panel {
  width: 500px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.panel-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.form-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.main-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-controls {
  display: flex;
  gap: 12px;
  align-items: center;
}

.table-container {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.personnel-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 0;
}

.personnel-avatar {
  flex-shrink: 0;
}

.personnel-details {
  flex: 1;
  min-width: 0;
}

.personnel-name {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
  margin-bottom: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.personnel-matricule {
  font-size: 12px;
  color: #7f8c8d;
  margin-bottom: 2px;
  font-weight: 500;
}

.personnel-id {
  font-size: 11px;
  color: #95a5a6;
  font-family: 'Courier New', monospace;
}

/* Styles pour l'avatar */
.el-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-weight: 600;
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.amount {
  font-weight: 600;
  color: #67c23a;
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

.loan-details {
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
