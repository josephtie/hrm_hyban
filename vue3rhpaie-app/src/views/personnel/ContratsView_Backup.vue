<template>
  <div class="enhanced-contrats-view">
    <!-- Header amélioré -->
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Gestion des Contrats de Travail</h1>
          <p class="page-subtitle">Gestion, édition et suivi des contrats de travail</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ contrats.length }}</div>
            <div class="stat-label">Total contrats</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ contratsEnCours }}</div>
            <div class="stat-label">En cours</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ contratsExpires }}</div>
            <div class="stat-label">Expirant</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <!-- Colonne principale avec le tableau amélioré -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Contrats</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle class="enhanced-button">
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="exportExcel" type="success" class="enhanced-button">
              <el-icon><Download /></el-icon>
              Exporter Excel
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils améliorée avec vues multiples -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher un contrat..."
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
            <el-button-group class="view-toggle">
              <el-button 
                :type="currentView === 'all' ? 'primary' : 'default'"
                @click="switchView('all')"
                size="large"
              >
                <el-icon><Document /></el-icon>
                Tous les contrats
              </el-button>
              <el-button 
                :type="currentView === 'expires' ? 'primary' : 'default'"
                @click="switchView('expires')"
                size="large"
              >
                <el-icon><WarningFilled /></el-icon>
                Expirant par mois
              </el-button>
              <el-button 
                :type="currentView === 'date' ? 'primary' : 'default'"
                @click="switchView('date')"
                size="large"
              >
                <el-icon><Calendar /></el-icon>
                Par date
              </el-button>
            </el-button-group>
          </div>
        </div>

        <!-- Filtres par date pour la vue par date -->
        <div v-if="currentView === 'date'" class="date-filters">
          <div class="filter-row">
            <el-date-picker
              v-model="dateFilter.debut"
              type="date"
              placeholder="Date de début"
              size="large"
              class="enhanced-input"
              format="DD/MM/YYYY"
              value-format="YYYY-MM-DD"
            />
            <el-date-picker
              v-model="dateFilter.fin"
              type="date"
              placeholder="Date de fin"
              size="large"
              class="enhanced-input"
              format="DD/MM/YYYY"
              value-format="YYYY-MM-DD"
            />
            <el-button @click="applyDateFilter" type="primary" size="large">
              <el-icon><Search /></el-icon>
              Rechercher
            </el-button>
            <el-button @click="exportDateFilterExcel" type="success" size="large">
              <el-icon><Download /></el-icon>
              Exporter Excel
            </el-button>
          </div>
        </div>

        <!-- Filtres par période pour la vue expirant -->
        <div v-if="currentView === 'expires'" class="periode-filters">
          <div class="filter-row">
            <el-select
              v-model="periodeFilter"
              placeholder="Sélectionner une période"
              size="large"
              class="enhanced-input"
              style="width: 200px"
            >
              <el-option
                v-for="periode in periodes"
                :key="periode.value"
                :label="periode.label"
                :value="periode.value"
              />
            </el-select>
            <el-button @click="applyPeriodeFilter" type="primary" size="large">
              <el-icon><Search /></el-icon>
              Filtrer
            </el-button>
            <el-button @click="exportPeriodeExcel" type="success" size="large">
              <el-icon><Download /></el-icon>
              Exporter Excel
            </el-button>
          </div>
        </div>

        <!-- Tableau amélioré -->
        <div class="table-container">
          <el-table
            :data="contrats"
            style="width: 100%"
            @selection-change="handleSelectionChange"
            class="enhanced-table"
            :row-class-name="tableRowClassName"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="personnel.matricule" label="Matricule" width="90" sortable>
              <template #default="{ row }">
                <div class="matricule-info">
                  <el-tag type="info" size="small" class="enhanced-tag">
                    {{ row.personnel?.matricule }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="personnel.nom" label="Nom" min-width="120" sortable>
              <template #default="{ row }">
                <div class="personnel-info">
                  <el-avatar :size="32" class="personnel-avatar">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                  <div class="personnel-details">
                    <div class="personnel-nom">{{ row.personnel?.nom }} {{ row.personnel?.prenom }}</div>
                    <div class="personnel-sexe">
                      <el-icon v-if="row.personnel?.sexe === 'M'"><Male /></el-icon>
                      <el-icon v-else><Female /></el-icon>
                      {{ row.personnel?.sexe === 'M' ? 'Masculin' : 'Féminin' }}
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="personnel.cnps" label="Num CNPS" width="110" sortable>
              <template #default="{ row }">
                <div class="cnps-info">
                  <el-icon><CreditCard /></el-icon>
                  {{ row.personnel?.cnps }}
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="personnel.situationMatrimoniale" label="Sit. Matri" width="90" sortable>
              <template #default="{ row }">
                <el-tag :type="getSituationMatrimonialeType(row.personnel?.situationMatrimoniale)" size="small">
                  {{ row.personnel?.situationMatrimoniale }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="personnel.nombreEnfant" label="Nbre enfants" width="80" sortable>
              <template #default="{ row }">
                <div class="enfants-info">
                  <el-icon><UserFilled /></el-icon>
                  {{ row.personnel?.nombreEnfant }}
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="typeContrat.libelle" label="Type contrat" width="100" sortable>
              <template #default="{ row }">
                <el-tag :type="getTypeContratType(row.typeContrat?.libelle || row.typeContrat)" size="small" class="enhanced-tag">
                  {{ row.typeContrat?.libelle || row.typeContrat }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="fonction.libelle" label="Fonction" min-width="130" sortable>
              <template #default="{ row }">
                <div class="fonction-info">
                  <el-icon><Briefcase /></el-icon>
                  {{ row.fonction?.libelle || row.fonction }}
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="dateDebut" label="Date début" width="100" sortable>
              <template #default="{ row }">
                <div class="date-info">
                  <el-icon><Calendar /></el-icon>
                  {{ formatDate(row.dateDebut) }}
                  <small style="display: block; color: #999; font-size: 10px;">
                    Raw: {{ row.dateDebut }}
                  </small>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="dateFin" label="Date fin" width="100" sortable>
              <template #default="{ row }">
                <div class="date-info" :class="{ 'expiring-soon': isExpiringSoon(row.dateFin) }">
                  <el-icon><Clock /></el-icon>
                  {{ formatDate(row.dateFin) }}
                  <small style="display: block; color: #999; font-size: 10px;">
                    Raw: {{ row.dateFin }}
                  </small>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="salaireCategoriel" label="Salaire catégoriel" width="110" sortable>
              <template #default="{ row }">
                <div class="salaire-info">
                  <el-tag type="success" size="small" class="enhanced-tag">
                    {{ formatCurrency(row.categorie?.salaireBase || row.salaireCategoriel || 0) }}
                  </el-tag>
                
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="netAPayer" label="Net à payer" width="100" sortable>
              <template #default="{ row }">
                <div class="salaire-info">
                  <el-tag type="primary" size="small" class="enhanced-tag">
                    {{ formatCurrency(row.netAPayer) }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="150" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-button
                    size="small"
                    @click="editContrat(row)"
                    type="primary"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="terminerContrat(row)"
                    type="warning"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><Close /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    @click="viewContrat(row)"
                    type="info"
                    class="enhanced-button btn-action"
                  >
                    <el-icon><View /></el-icon>
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>

    <!-- Modal amélioré pour terminer un contrat -->
    <EnhancedModal
      v-model="showTerminerModal"
      title="Mettre fin au contrat"
      subtitle="Modification des informations du contrat"
      type="warning"
      icon="Close"
      :alerts="terminerAlerts"
      @confirm="confirmTerminer"
      @cancel="cancelTerminer"
      confirm-text="Valider"
      cancel-text="Annuler"
      :loading="terminerLoading"
      loading-text="Traitement en cours..."
    >
      <div class="terminer-contrat-form">
        <div class="contrat-info">
          <h4>Informations du contrat</h4>
          <div class="info-item">
            <label>Employé:</label>
            <span><strong>{{ selectedContrat?.personnel.nom }} {{ selectedContrat?.personnel.prenom }}</strong></span>
          </div>
          <div class="info-item">
            <label>Date début:</label>
            <span>{{ formatDate(selectedContrat?.dateDebut || '') }}</span>
          </div>
        </div>

        <el-form :model="terminerForm" label-width="120px">
          <el-form-item label="Date de modification" required>
            <el-date-picker
              v-model="terminerForm.dateModification"
              type="date"
              placeholder="Date de modification"
              size="large"
              class="enhanced-input"
              format="DD/MM/YYYY"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>

          <el-form-item label="Date de fin/arrêt" required>
            <el-date-picker
              v-model="terminerForm.dateFin"
              type="date"
              placeholder="Date de fin / arrêt"
              size="large"
              class="enhanced-input"
              format="DD/MM/YYYY"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>

          <el-form-item label="Départ">
            <el-radio-group v-model="terminerForm.depart" size="large">
              <el-radio value="false">Non</el-radio>
              <el-radio value="true">Oui</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="Observation" required>
            <el-select
              v-model="terminerForm.observation"
              placeholder="Sélectionner une observation"
              size="large"
              class="enhanced-input"
            >
              <el-option label="Aucun" value="Aucun" />
              <el-option label="Démission" value="Demission" />
              <el-option label="Décès" value="Deces" />
              <el-option label="Fin de contrat" value="Fin de contrat" />
              <el-option label="Renvoi" value="Renvoi" />
              <el-option label="Modification" value="Modification" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </EnhancedModal>

    <!-- Modal amélioré pour les détails du contrat -->
    <EnhancedModal
      v-model="showDetailsModal"
      title="Détails du contrat"
      subtitle="Informations complètes"
      type="info"
      icon="View"
      :show-footer="false"
    >
      <div class="contrat-details" v-if="selectedContrat">
        <div class="detail-section">
          <h4>Informations personnelles</h4>
          <div class="detail-grid">
            <div class="detail-item">
              <label>Matricule:</label>
              <span>{{ selectedContrat.personnel.matricule }}</span>
            </div>
            <div class="detail-item">
              <label>Nom complet:</label>
              <span>{{ selectedContrat.personnel.nom }} {{ selectedContrat.personnel.prenom }}</span>
            </div>
            <div class="detail-item">
              <label>Sexe:</label>
              <span>{{ selectedContrat.personnel.sexe === 'M' ? 'Homme' : 'Femme' }}</span>
            </div>
            <div class="detail-item">
              <label>Num CNPS:</label>
              <span>{{ selectedContrat.personnel.cnps }}</span>
            </div>
            <div class="detail-item">
              <label>Situation matrimoniale:</label>
              <span>{{ selectedContrat.personnel.situationMatrimoniale }}</span>
            </div>
            <div class="detail-item">
              <label>Nombre d'enfants:</label>
              <span>{{ selectedContrat.personnel.nombreEnfant }}</span>
            </div>
          </div>
        </div>

        <div class="detail-section">
          <h4>Informations du contrat</h4>
          <div class="detail-grid">
            <div class="detail-item">
              <label>Type de contrat:</label>
              <span>{{ selectedContrat.typeContrat }}</span>
            </div>
            <div class="detail-item">
              <label>Fonction:</label>
              <span>{{ selectedContrat.fonction }}</span>
            </div>
            <div class="detail-item">
              <label>Date de début:</label>
              <span>{{ formatDate(selectedContrat.dateDebut) }}</span>
            </div>
            <div class="detail-item">
              <label>Date de fin:</label>
              <span>{{ formatDate(selectedContrat.dateFin) }}</span>
            </div>
            <div class="detail-item">
              <label>Salaire catégoriel:</label>
              <span>{{ formatCurrency(selectedContrat.salaireCategoriel) }}</span>
            </div>
            <div class="detail-item">
              <label>Net à payer:</label>
              <span>{{ formatCurrency(selectedContrat.netAPayer) }}</span>
            </div>
          </div>
        </div>
      </div>
    </EnhancedModal>

    <!-- Modal amélioré pour modifier un contrat -->
    <EnhancedModal
      v-model="showEditModal"
      title="Modifier le contrat"
      subtitle="Modification des informations du contrat"
      type="primary"
      icon="Edit"
      :loading="editLoading"
      loading-text="Modification en cours..."
      @confirm="confirmEdit"
      @cancel="cancelEdit"
      @close="cancelEdit"
      confirm-text="Enregistrer"
      cancel-text="Annuler"
    >
      <div class="edit-form">
        <el-form :model="editForm" label-width="120px" size="large">
          <el-form-item label="Type de contrat">
            <el-select v-model="editForm.typeContrat" placeholder="Sélectionnez un type" style="width: 100%">
              <el-option label="CDI" value="CDI" />
              <el-option label="CDD" value="CDD" />
              <el-option label="Stage" value="Stage" />
              <el-option label="Contrat d'apprentissage" value="Apprentissage" />
            </el-select>
          </el-form-item>

          <el-form-item label="Fonction">
            <el-input v-model="editForm.fonction" placeholder="Fonction du salarié" />
          </el-form-item>

          <el-form-item label="Date de début">
            <el-date-picker
              v-model="editForm.dateDebut"
              type="date"
              placeholder="Date de début"
              style="width: 100%"
              format="DD/MM/YYYY"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>

          <el-form-item label="Date de fin">
            <el-date-picker
              v-model="editForm.dateFin"
              type="date"
              placeholder="Date de fin"
              style="width: 100%"
              format="DD/MM/YYYY"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>

          <el-form-item label="Salaire catégoriel">
            <el-input-number
              v-model="editForm.salaireCategoriel"
              placeholder="Salaire catégoriel"
              style="width: 100%"
              :min="0"
              :max="999999999"
            />
          </el-form-item>

          <el-form-item label="Net à payer">
            <el-input-number
              v-model="editForm.netAPayer"
              placeholder="Net à payer"
              style="width: 100%"
              :min="0"
              :max="999999999"
            />
          </el-form-item>
        </el-form>
      </div>
    </EnhancedModal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, Refresh, Close, Calendar, Clock,
  User, UserFilled, CreditCard, Briefcase, View, Download,
  Document, WarningFilled
} from '@element-plus/icons-vue'
import EnhancedModal from '@/components/common/EnhancedModal.vue'
import { contratPersonnelService, type ContratPersonnel, type ContratPersonnelDTO, type PaginationRequest, type EndContractRequest } from '@/services/contrat-personnel.service'

interface Personnel {
  matricule: string
  nom: string
  prenom: string
  sexe: 'M' | 'F'
  cnps: string
  situationMatrimoniale: string
  nombreEnfant: number
}

interface Contrat {
  id: number
  personnel: Personnel
  typeContrat: string
  fonction: string
  dateDebut: Date
  dateFin: Date
  salaireCategoriel: number
  netAPayer: number
}

const currentView = ref('all')
const searchText = ref('')
const selectedContrats = ref<Contrat[]>([])
const showTerminerModal = ref(false)
const showDetailsModal = ref(false)
const showEditModal = ref(false)
const selectedContrat = ref<Contrat | null>(null)
const terminerLoading = ref(false)
const editLoading = ref(false)

let editForm = reactive({
  id: 0,
  typeContrat: '',
  fonction: '',
  dateDebut: '',
  dateFin: '',
  salaireCategoriel: 0,
  netAPayer: 0
})

let dateFilter = reactive({
  debut: '',
  fin: ''
})

const periodeFilter = ref('')

let terminerForm = reactive({
  dateModification: '',
  dateFin: '',
  depart: 'false',
  observation: ''
})

const contrats = ref<ContratPersonnel[]>([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(0)
const pageSize = ref(10)

const periodes = ref([
  { value: '2024-01', label: 'Janvier 2024' },
  { value: '2024-02', label: 'Février 2024' },
  { value: '2024-03', label: 'Mars 2024' },
  { value: '2024-04', label: 'Avril 2024' },
  { value: '2024-05', label: 'Mai 2024' },
  { value: '2024-06', label: 'Juin 2024' }
])

const filteredContrats = computed(() => {
  let filtered = contrats.value

  if (searchText.value) {
    filtered = filtered.filter(contrat =>
      contrat.personnel?.matricule?.toLowerCase().includes(searchText.value.toLowerCase()) ||
      contrat.personnel?.nom?.toLowerCase().includes(searchText.value.toLowerCase()) ||
      contrat.personnel?.prenom?.toLowerCase().includes(searchText.value.toLowerCase()) ||
      (typeof contrat.fonction === 'string' ? contrat.fonction : contrat.fonction?.libelle)?.toLowerCase().includes(searchText.value.toLowerCase()) ||
      (typeof contrat.typeContrat === 'string' ? contrat.typeContrat : contrat.typeContrat?.libelle)?.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  if (currentView.value === 'expires') {
    const now = new Date()
    filtered = filtered.filter(contrat => {
      const dateFin = new Date(contrat.dateFin)
      const diffTime = dateFin.getTime() - now.getTime()
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      return diffDays <= 90 && diffDays > 0
    })
  }

  if (currentView.value === 'date' && dateFilter.debut && dateFilter.fin) {
    filtered = filtered.filter(contrat => {
      const debut = new Date(contrat.dateDebut)
      const fin = new Date(contrat.dateFin)
      const filterDebut = new Date(dateFilter.debut)
      const filterFin = new Date(dateFilter.fin)
      return debut >= filterDebut && fin <= filterFin
    })
  }

  return filtered
})

const contratsEnCours = computed(() => {
  const now = new Date()
  return contrats.value.filter(contrat => {
    const dateFin = new Date(contrat.dateFin)
    return dateFin > now
  }).length
})

const contratsExpires = computed(() => {
  const now = new Date()
  return contrats.value.filter(contrat => {
    const dateFin = new Date(contrat.dateFin)
    const diffTime = dateFin.getTime() - now.getTime()
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    return diffDays <= 90 && diffDays > 0
  }).length
})

const terminerAlerts = computed(() => {
  if (!selectedContrat.value) return []
  
  return [
    {
      type: 'warning' as const,
      title: 'Action importante',
      message: 'La modification du contrat affectera le statut de l\'employé',
      closable: false
    }
  ]
})

// Méthodes
const formatDate = (date: Date | string | undefined | null) => {
  if (!date) return '-'
  
  try {
    // Debug: Afficher la valeur reçue
    console.log('📅 Date reçue:', date, 'Type:', typeof date)
    
    let d: Date
    
    if (typeof date === 'string') {
      // Gérer différents formats de date
      if (date.includes('T')) {
        // Format ISO: 2023-01-15T08:39:58.024958
        d = new Date(date)
      } else if (date.includes('-')) {
        // Format YYYY-MM-DD: 2023-01-15
        d = new Date(date + 'T00:00:00')
      } else if (date.includes('/')) {
        // Format DD/MM/YYYY: 15/01/2023
        const parts = date.split('/')
        d = new Date(`${parts[2]}-${parts[1]}-${parts[0]}`)
      } else {
        // Autre format, essayer directement
        d = new Date(date)
      }
    } else {
      d = new Date(date)
    }
    
    // Vérifier si la date est valide
    if (isNaN(d.getTime())) {
      console.warn('⚠️ Date invalide:', date)
      return date.toString() // Retourner la valeur originale si invalide
    }
    
    const formatted = new Intl.DateTimeFormat('fr-FR').format(d)
    console.log('📅 Date formatée:', formatted)
    return formatted
  } catch (error) {
    console.warn('❌ Erreur formatage date:', date, error)
    return date?.toString() || '-'
  }
}

const formatCurrency = (amount: number | string | undefined | null) => {
  if (!amount) return '0 F CFA'
  
  try {
    // Debug: Afficher la valeur reçue
    console.log('💰 Montant reçu:', amount, 'Type:', typeof amount)
    
    let numAmount: number
    
    if (typeof amount === 'string') {
      // Nettoyer la chaîne : retirer espaces, caractères non numériques
      const cleanAmount = amount.replace(/[^\d.-]/g, '')
      numAmount = parseFloat(cleanAmount)
      
      if (isNaN(numAmount)) {
        console.warn('⚠️ Montant invalide après nettoyage:', amount, '→', cleanAmount)
        return amount + ' F CFA' // Retourner la valeur originale
      }
      
      console.log('💰 Montant nettoyé:', cleanAmount, '→', numAmount)
    } else {
      numAmount = amount
    }
    
    const formatted = new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(numAmount)
    
    console.log('💰 Montant formaté:', formatted)
    return formatted
  } catch (error) {
    console.warn('❌ Erreur formatage montant:', amount, error)
    return amount?.toString() + ' F CFA' || '0 F CFA'
  }
}

const isExpiringSoon = (dateFin: Date | string | undefined | null) => {
  if (!dateFin) return false
  
  try {
    const now = new Date()
    const fin = new Date(dateFin)
    
    // Vérifier si la date est valide
    if (isNaN(fin.getTime())) {
      return false
    }
    
    const diffTime = fin.getTime() - now.getTime()
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    return diffDays <= 30 && diffDays > 0
  } catch (error) {
    console.warn('Invalid date in isExpiringSoon:', dateFin, error)
    return false
  }
}

const getSituationMatrimonialeType = (situation: string) => {
  switch (situation) {
    case 'Marié': return 'success'
    case 'Célibataire': return 'info'
    case 'Divorcé': return 'warning'
    case 'Veuf': return 'danger'
    default: return 'default'
  }
}

const getTypeContratType = (type: string) => {
  switch (type) {
    case 'CDI': return 'success'
    case 'CDD': return 'warning'
    case 'Stage': return 'info'
    case 'Intérim': return 'danger'
    default: return 'default'
  }
}

const tableRowClassName = ({ row }: { row: Contrat }) => {
  if (isExpiringSoon(row.dateFin)) {
    return 'row-expiring'
  }
  const now = new Date()
  const dateFin = new Date(row.dateFin)
  return dateFin < now ? 'row-expired' : 'row-active'
}

const switchView = (view: string) => {
  currentView.value = view
}

const applyDateFilter = () => {
  loadContratsByDate()
}

const applyPeriodeFilter = () => {
  loadContratsByPeriode()
}

const exportExcel = async () => {
  try {
    // Simulation d'export Excel avec les données actuelles
    const csvContent = generateCSVContent(contrats.value)
    downloadCSV(csvContent, 'contrats_export.csv')
    ElMessage.success('Export Excel terminé avec succès')
  } catch (error) {
    console.error('Erreur lors de l\'export:', error)
    ElMessage.error('Erreur lors de l\'export Excel')
  }
}

const exportDateFilterExcel = async () => {
  if (!dateFilter.debut || !dateFilter.fin) {
    ElMessage.warning('Veuillez sélectionner une date de début et de fin')
    return
  }

  try {
    const response = await contratPersonnelService.getContratsExpiresParDate(
      dateFilter.debut,
      dateFilter.fin,
      { offset: 0, limit: 10000, search: '' }
    )
    const csvContent = generateCSVContent(response.data?.rows || [])
    downloadCSV(csvContent, 'contrats_export_date.csv')
    ElMessage.success('Export Excel (par date) terminé avec succès')
  } catch (error) {
    console.error('Erreur lors de l\'export par date:', error)
    ElMessage.error('Erreur lors de l\'export Excel')
  }
}

const exportPeriodeExcel = async () => {
  if (!periodeFilter.value) {
    ElMessage.warning('Veuillez sélectionner une période')
    return
  }

  try {
    const response = await contratPersonnelService.getContratsExpiresParPeriode(
      parseInt(periodeFilter.value),
      '',
      { offset: 0, limit: 10000 }
    )
    const csvContent = generateCSVContent(response.data?.rows || [])
    downloadCSV(csvContent, 'contrats_export_periode.csv')
    ElMessage.success('Export Excel (par période) terminé avec succès')
  } catch (error) {
    console.error('Erreur lors de l\'export par période:', error)
    ElMessage.error('Erreur lors de l\'export Excel')
  }
}

// Fonctions utilitaires pour l'export CSV
const generateCSVContent = (data: ContratPersonnel[]) => {
  const headers = [
    'Matricule', 'Nom', 'Prénom', 'Sexe', 'CNPS', 'Situation Matrimoniale',
    'Nombre Enfants', 'Type Contrat', 'Fonction', 'Date Début', 'Date Fin',
    'Salaire Catégoriel', 'Net à Payer'
  ]
  
  const rows = data.map(contrat => {
    // Extraire le salaire catégoriel depuis categorie.salaireBase
    let salaireCat = 0
    if (contrat.categorie?.salaireBase) {
      const cleanSalaire = contrat.categorie.salaireBase.toString().replace(/[^\d.-]/g, '')
      salaireCat = parseFloat(cleanSalaire) || 0
    } else if (contrat.salaireCategoriel) {
      salaireCat = typeof contrat.salaireCategoriel === 'string' 
        ? parseFloat(contrat.salaireCategoriel.replace(/[^\d.-]/g, '')) || 0
        : contrat.salaireCategoriel
    }
    
    return [
      contrat.personnel?.matricule || '',
      contrat.personnel?.nom || '',
      contrat.personnel?.prenom || '',
      contrat.personnel?.sexe || '',
      contrat.personnel?.cnps || '',
      contrat.personnel?.situationMatrimoniale || '',
      contrat.personnel?.nombreEnfant || 0,
      typeof contrat.typeContrat === 'string' ? contrat.typeContrat : contrat.typeContrat?.libelle || '',
      typeof contrat.fonction === 'string' ? contrat.fonction : contrat.fonction?.libelle || '',
      contrat.dateDebut || '',
      contrat.dateFin || '',
      salaireCat,
      contrat.netAPayer || 0
    ]
  })

  return [headers, ...rows].map(row => row.join(',')).join('\n')
}

const downloadCSV = (content: string, filename: string) => {
  const blob = new Blob([content], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  link.setAttribute('href', url)
  link.setAttribute('download', filename)
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

const editContrat = (contrat: Contrat) => {
  selectedContrat.value = contrat
  Object.assign(editForm, {
    id: contrat.id,
    typeContrat: contrat.typeContrat,
    fonction: contrat.fonction,
    dateDebut: contrat.dateDebut instanceof Date ? contrat.dateDebut.toISOString().split('T')[0] : contrat.dateDebut,
    dateFin: contrat.dateFin instanceof Date ? contrat.dateFin.toISOString().split('T')[0] : contrat.dateFin,
    salaireCategoriel: contrat.salaireCategoriel,
    netAPayer: contrat.netAPayer
  })
  showEditModal.value = true
}

const confirmEdit = async () => {
  if (!editForm.typeContrat || !editForm.fonction || !editForm.dateDebut) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  editLoading.value = true

  try {
    // Préparation de la requête pour l'API
    const request = {
      id: editForm.id,
      idPersonnel: selectedContrat.value?.personnel.id || 0,
      idCategorie: 1, // À adapter selon les catégories disponibles
      idFonction: 1, // À adapter selon les fonctions disponibles
      idTypeContrat: editForm.typeContrat === 'CDI' ? 1 : 2, // À adapter selon les types
      dateDebut: editForm.dateDebut,
      dateFin: editForm.dateFin || undefined,
      netAPayer: editForm.netAPayer,
      indemniteLogement: 0, // À adapter
      ancienete: 0, // À adapter
      sursalaire: 0, // À adapter
      indemnitetransport: 0, // À adapter
      indemniterespons: 0, // À adapter
      indemniterepresent: 0 // À adapter
    }

    const response = await contratPersonnelService.saveContrat(request)
    
    if (response.result) {
      // Recharger les données
      await loadContrats()
      ElMessage.success('Contrat modifié avec succès')
      
      // Fermeture du modal
      showEditModal.value = false
      selectedContrat.value = null
      
      // Réinitialisation du formulaire
      Object.assign(editForm, {
        id: 0,
        typeContrat: '',
        fonction: '',
        dateDebut: '',
        dateFin: '',
        salaireCategoriel: 0,
        netAPayer: 0
      })
    } else {
      ElMessage.error('Erreur lors de la modification du contrat')
    }
  } catch (error) {
    console.error('Erreur lors de la modification du contrat:', error)
    ElMessage.error('Une erreur est survenue lors de la modification')
  } finally {
    editLoading.value = false
  }
}

const confirmTerminer = async () => {
  if (!selectedContrat.value) return

  if (!terminerForm.dateModification || !terminerForm.dateFin || !terminerForm.observation) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  terminerLoading.value = true

  try {
    const request: EndContractRequest = {
      id: selectedContrat.value.id,
      dateFin: terminerForm.dateFin,
      dateMod: terminerForm.dateModification,
      permanent: terminerForm.depart,
      observCtrat: terminerForm.observation
    }

    const response = await contratPersonnelService.endContract(request)
    
    if (response.result) {
      // Recharger les données
      await loadContrats()
      ElMessage.success('Contrat modifié avec succès')
      
      // Fermeture du modal
      showTerminerModal.value = false
      selectedContrat.value = null
      
      // Réinitialisation du formulaire
      Object.assign(terminerForm, {
        dateModification: '',
        dateFin: '',
        depart: 'false',
        observation: ''
      })
    } else {
      ElMessage.error('Erreur lors de la modification du contrat')
    }
  } catch (error) {
    console.error('Erreur lors de la modification du contrat:', error)
    ElMessage.error('Une erreur est survenue lors de la modification')
  } finally {
    terminerLoading.value = false
    showTerminerModal.value = false
    selectedContrat.value = null
  }
}

const cancelTerminer = () => {
  showTerminerModal.value = false
  selectedContrat.value = null
}

const viewContrat = (contrat: ContratPersonnel) => {
  selectedContrat.value = contrat
  showDetailsModal.value = true
}

// Fonctions pour charger les données depuis l'API
const loadContrats = async () => {
  loading.value = true
  try {
    const pagination = {
      page: Math.floor(currentPage.value * pageSize.value / 10),
      limit: pageSize.value,
      search: searchText.value
    }
    
    // Debug: Pagination
    console.log('📄 Pagination debug:')
    console.log('  currentPage.value:', currentPage.value)
    console.log('  pageSize.value:', pageSize.value)
    console.log('  page calculée:', Math.floor(currentPage.value * pageSize.value / 10))
    console.log('  pagination envoyée:', pagination)

    let response: ContratPersonnelDTO

    switch (currentView.value) {
      case 'all':
        response = await contratPersonnelService.getAllContrats(pagination)
        break
      case 'expires':
        response = await contratPersonnelService.getContratsExpires(pagination)
        break
      default:
        response = await contratPersonnelService.getContratsActifs(pagination)
        // Assigner les données avec réactivité forcée
    contrats.value = response.data?.rows || []
    total.value = response.data?.total || 0
    
    // Forcer la réactivité
    await nextTick()
    
    console.log('📋 Données chargées:', {
      contratsLength: contrats.value.length,
      total: total.value,
      firstContrat: contrats.value[0]
    })
    }

    contrats.value = response.data?.rows || []
    total.value = response.data?.total || 0
    console.log('  total.value après nextTick:', total.value)
    
    // Debug: Afficher la structure des données reçues
    console.log('📋 Structure des données contrats:', response)
    if (response.data?.rows && response.data.rows.length > 0) {
      console.log('📋 Premier contrat exemple:', response.data.rows[0])
      console.log('📋 Champs de date disponibles:', Object.keys(response.data.rows[0]).filter(key => key.toLowerCase().includes('date')))
    }
  } catch (error) {
    console.error('Erreur lors du chargement des contrats:', error)
    ElMessage.error('Erreur lors du chargement des contrats')
  } finally {
    loading.value = false
  }
}

const loadContratsByDate = async () => {
  if (!dateFilter.debut || !dateFilter.fin) {
    ElMessage.warning('Veuillez sélectionner une date de début et de fin')
    return
  }

  loading.value = true
  try {
    const response = await contratPersonnelService.getContratsExpiresParDate(
      dateFilter.debut,
      dateFilter.fin,
      {
        offset: currentPage.value * pageSize.value,
        limit: pageSize.value,
        search: searchText.value
      }
    )
    contrats.value = response.data?.rows || []
    total.value = response.data?.total || 0
    ElMessage.success('Filtre par date appliqué')
  } catch (error) {
    console.error('Erreur lors du chargement des contrats par date:', error)
    ElMessage.error('Erreur lors du chargement des contrats')
  } finally {
    loading.value = false
  }
}

const loadContratsByPeriode = async () => {
  if (!periodeFilter.value) {
    ElMessage.warning('Veuillez sélectionner une période')
    return
  }

  loading.value = true
  try {
    const response = await contratPersonnelService.getContratsExpiresParPeriode(
      parseInt(periodeFilter.value),
      searchText.value,
      {
        offset: currentPage.value * pageSize.value,
        limit: pageSize.value
      }
    )
    contrats.value = response.data?.rows || []
    total.value = response.data?.total || 0
    ElMessage.success('Filtre par période appliqué')
  } catch (error) {
    console.error('Erreur lors du chargement des contrats par période:', error)
    ElMessage.error('Erreur lors du chargement des contrats')
  } finally {
    loading.value = false
  }
}

const refreshData = async () => {
  await loadContrats()
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: Contrat[]) => {
  selectedContrats.value = selection
}

onMounted(() => {
  // Charger les données initiales
  loadContrats()
})

// Watchers pour recharger les données automatiquement
watch(currentView, () => {
  currentPage.value = 0
  loadContrats()
})

watch(searchText, () => {
  currentPage.value = 0
  loadContrats()
})
</script>

<style lang="scss" scoped>
@use '@/styles/design-system.scss' as *;

.enhanced-contrats-view {
  padding: 20px;
  background: var(--bg-secondary);
  min-height: 100vh;
  width: 100%;
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

.main-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 0;
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
  
  .view-toggle {
    .el-button {
      border-radius: 0;
      
      &:first-child {
        border-radius: var(--border-radius-base) 0 0 var(--border-radius-base);
      }
      
      &:last-child {
        border-radius: 0 var(--border-radius-base) var(--border-radius-base) 0;
      }
    }
  }
}

.date-filters,
.periode-filters {
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-tertiary);
  
  .filter-row {
    display: flex;
    gap: var(--spacing-md);
    align-items: center;
  }
}

.table-container {
  flex: 1;
  overflow: hidden;
  padding: var(--spacing-lg);
  width: 100%;
  min-height: 0;
}

.enhanced-table {
  width: 100%;
  
  :deep(.el-table) {
    width: 100% !important;
    table-layout: fixed;
  }
  
  :deep(.el-table__body-wrapper) {
    width: 100% !important;
  }
  
  :deep(.el-table__header-wrapper) {
    width: 100% !important;
  }
  
  :deep(.el-table__header) {
    width: 100% !important;
  }
  
  :deep(.el-table__body) {
    width: 100% !important;
  }
}

.personnel-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .personnel-avatar {
    background: var(--primary-color);
    color: white;
  }
  
  .personnel-details {
    .personnel-nom {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .personnel-sexe {
      font-size: 12px;
    }
  }
}

.matricule-info,
.cnps-info,
.enfants-info,
.fonction-info,
.date-info,
.salaire-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  
  .expiring-soon {
    color: var(--warning-color);
    font-weight: 600;
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
    
    &.row-active {
      background: rgba(103, 194, 58, 0.05);
      
      &:hover {
        background: rgba(103, 194, 58, 0.1);
      }
    }
    
    &.row-expiring {
      background: rgba(230, 162, 60, 0.05);
      
      &:hover {
        background: rgba(230, 162, 60, 0.1);
      }
    }
    
    &.row-expired {
      background: rgba(245, 108, 108, 0.05);
      
      &:hover {
        background: rgba(245, 108, 108, 0.1);
      }
    }
  }
}

.terminer-contrat-form {
  .contrat-info {
    background: var(--bg-tertiary);
    padding: var(--spacing-lg);
    border-radius: var(--border-radius-base);
    margin-bottom: var(--spacing-lg);
    
    h4 {
      margin: 0 0 var(--spacing-md) 0;
      color: var(--text-primary);
    }
    
    .info-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: var(--spacing-sm);
      
      &:last-child {
        margin-bottom: 0;
      }
      
      label {
        font-weight: 600;
        color: var(--text-secondary);
      }
      
      span {
        color: var(--text-primary);
      }
    }
  }
}

.contrat-details {
  .detail-section {
    margin-bottom: var(--spacing-xl);
    
    &:last-child {
      margin-bottom: 0;
    }
    
    h4 {
      margin: 0 0 var(--spacing-lg) 0;
      color: var(--text-primary);
      font-size: 16px;
      font-weight: 600;
      padding-bottom: var(--spacing-sm);
      border-bottom: 2px solid var(--border-light);
    }
  }
  
  .detail-grid {
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
        font-weight: 500;
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
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .enhanced-contrats-view {
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
  
  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
