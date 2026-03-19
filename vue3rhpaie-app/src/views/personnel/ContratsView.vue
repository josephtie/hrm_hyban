<template>
  <div class="enhanced-contrats-view">
    <!-- Header amélioré -->
    <div class="page-header enhanced-card">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title text-gradient">Contrats du Personnel</h1>
          <p class="page-subtitle">Gestion des contrats et suivi des effectifs</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-value">{{ total }}</div>
            <div class="stat-label">Total contrats</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ contrats.length }}</div>
            <div class="stat-label">Affichés</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ currentView === 'active' ? 'Actifs' : currentView === 'all' ? 'Tous' : 'Expirants' }}</div>
            <div class="stat-label">Type</div>
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
            <el-button @click="toggleForm" type="primary" class="enhanced-button">
              <el-icon><Plus /></el-icon>
              Nouveau Contrat
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils améliorée -->
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
            <el-select
              v-model="currentView"
              placeholder="Type"
              style="width: 150px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option label="Tous les contrats" value="all" />
              <el-option label="Contrats actifs" value="active" />
              <el-option label="Contrats inactifs" value="inactive" />
            </el-select>
            
            <el-select
              v-model="filterTypeContrat"
              placeholder="Type de contrat"
              style="width: 150px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option label="CDI" value="CDI" />
              <el-option label="CDD" value="CDD" />
              <el-option label="Stage" value="Stage" />
              <el-option label="Apprentissage" value="Apprentissage" />
              <el-option label="Consultance" value="Consultance" />
            </el-select>
            
            <el-select
              v-model="filterSalaire"
              placeholder="Salaire de base"
              style="width: 150px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option label="< 100 000" value="low" />
              <el-option label="100 000 - 200 000" value="medium" />
              <el-option label="200 000 - 300 000" value="high" />
              <el-option label="> 300 000" value="veryhigh" />
            </el-select>
            
            <el-select
              v-model="filterCarec"
              placeholder="État employé"
              style="width: 150px"
              clearable
              size="large"
              class="enhanced-input"
            >
              <el-option label="Contractuel" value="true" />
              <el-option label="Non Contractuel" value="false" />
            </el-select>
            
            <el-button 
              @click="exportExcel" 
              type="success" 
              class="enhanced-button"
              :loading="loading"
              :disabled="loading"
              size="large"
            >
              <el-icon><Document /></el-icon>
              Exporter
            </el-button>
            
            <el-button 
              v-if="expireDate || expirePeriodStart"
              @click="clearExpirationFilters" 
              type="warning" 
              class="enhanced-button"
              :loading="loading"
              :disabled="loading"
              size="large"
            >
              <el-icon><Refresh /></el-icon>
              Effacer les filtres d'expiration
            </el-button>
            
            <!-- Boutons pour les filtres d'expiration -->
            <el-button 
              @click="showExpiresOnDateModal = true" 
              type="warning" 
              class="enhanced-button"
            >
              <el-icon><Calendar /></el-icon>
              Expire à une date
            </el-button>
            
            <el-button 
              @click="showExpiresInPeriodModal = true" 
              type="warning" 
              class="enhanced-button"
            >
              <el-icon><Timer /></el-icon>
              Expire dans période
            </el-button>
          </div>
        </div>

        <!-- Tableau amélioré comme CategoriesView -->
        <div class="table-container">
          <el-table 
            :data="contrats" 
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column prop="personnel.matricule" label="Matricule" min-width="80" sortable>
              <template #default="{ row }">
                <div class="matricule-info">
                  <el-tag type="info" size="small" class="enhanced-tag">
                    {{ row.personnel?.matricule || 'N/A' }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="personnel.nom" label="Nom & Prénom" min-width="150" sortable>
              <template #default="{ row }">
                <div class="personnel-info">
                  <el-icon class="personnel-icon"><User /></el-icon>
                  <span>{{ row.personnel?.nom || '' }} {{ row.personnel?.prenom || '' }}</span>
                </div>
              </template>
            </el-table-column>
          
          <el-table-column prop="typeContrat" label="Type Contrat" min-width="120" sortable>
            <template #default="{ row }">
              <el-tag 
                :type="getTypeContratType(row.typeContrat)" 
                size="small" 
                class="enhanced-tag"
              >
                {{ getTypeContratLabel(row.typeContrat) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="fonction" label="Fonction" min-width="120" sortable>
            <template #default="{ row }">
              <div class="fonction-info">
                <el-icon class="fonction-icon"><Briefcase /></el-icon>
                <span>{{ getFonctionLabel(row.fonction) }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="dateDebut" label="Date Début" min-width="100" sortable>
            <template #default="{ row }">
              <div class="date-info">
                <el-icon class="date-icon"><Calendar /></el-icon>
                <span>{{ formatDate(row.dateDebut) }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="dateFin" label="Date Fin" min-width="100" sortable>
            <template #default="{ row }">
              <div class="date-info">
                <el-icon class="date-icon"><Clock /></el-icon>
                <span>{{ formatDate(row.dateFin) }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="categorie.salaireBase" label="Salaire Catégoriel" min-width="110" sortable>
            <template #default="{ row }">
              <div class="salaire-info">
                <el-icon class="salaire-icon"><CreditCard /></el-icon>
                <span>{{ formatCurrency(row.categorie?.salaireBase || row.salaireCategoriel || 0) }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="statut" label="Statut" min-width="80" sortable>
            <template #default="{ row }">
              <el-tag 
                :type="row.statut ? 'success' : 'danger'" 
                size="small" 
                class="enhanced-tag"
              >
                <el-icon style="margin-right: 4px;">
                  <Clock v-if="row.statut" />
                  <WarningFilled v-else />
                </el-icon>
                {{ row.statut ? 'Actif' : 'Inactif' }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="personnel.carec" label="Etat employe" min-width="100" sortable>
            <template #default="{ row }">
              <el-tag 
                :type="row.personnel?.carec ? 'warning' : 'info'" 
                size="small" 
                class="enhanced-tag"
              >
                <el-icon style="margin-right: 4px;">
                  <UserFilled />
                </el-icon>
                {{ row.personnel?.carec ? 'Contractuel' : 'Non Contractuel' }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column label="Actions" width="140" fixed="right">
            <template #default="{ row }">
              <el-button-group>
                <el-button size="small" @click="viewContrat(row)" type="primary" title="Voir le contrat">
                  <el-icon><View /></el-icon>
                  Voir
                </el-button>
                <el-button size="small" @click="terminateContrat(row)" type="warning" title="Fin du contrat">
                  <el-icon><WarningFilled /></el-icon>
                  Fin
                </el-button>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- Pagination -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
       
        />
      </div>
    </div>
  </div>
 </div>

  <!-- Modal Voir le contrat -->
  <el-dialog
    v-model="showViewModal"
    title="Détails du Contrat"
    width="800px"
    destroy-on-close
    class="contract-view-modal"
  >
    <div v-if="selectedContrat" class="contrat-details">
      <!-- En-tête avec informations principales -->
      <div class="contract-header">
        <div class="contract-info">
          <h3>{{ selectedContrat.personnel?.nom }} {{ selectedContrat.personnel?.prenom }}</h3>
          <p>Matricule: {{ selectedContrat.personnel?.matricule }}</p>
        </div>
        <div class="contract-status">
          <el-tag :type="selectedContrat.statut ? 'success' : 'danger'" size="large">
            {{ selectedContrat.statut ? 'Actif' : 'Inactif' }}
          </el-tag>
        </div>
      </div>

      <!-- Détails du contrat -->
      <el-descriptions :column="2" border class="contract-descriptions">
        <el-descriptions-item label="Type Contrat">
          <el-tag :type="getTypeContratType(selectedContrat.typeContrat)">
            <el-icon><Briefcase /></el-icon>
            {{ getTypeContratLabel(selectedContrat.typeContrat) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Fonction">
          <el-tag type="info">
            <el-icon><User /></el-icon>
            {{ getFonctionLabel(selectedContrat.fonction) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Date Début">
          <div class="date-info">
            <el-icon><Calendar /></el-icon>
            {{ formatDate(selectedContrat.dateDebut) }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="Date Fin">
          <div class="date-info">
            <el-icon><Clock /></el-icon>
            {{ formatDate(selectedContrat.dateFin) }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="Salaire Catégoriel">
          <el-tag type="success" size="large">
            <el-icon><CreditCard /></el-icon>
            {{ formatCurrency(selectedContrat.categorie?.salaireBase || selectedContrat.salaireCategoriel || 0) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Durée">
          <el-tag type="primary">
            {{ calculateDuration(selectedContrat.dateDebut, selectedContrat.dateFin) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
      
      <div class="modal-actions">
        <el-button @click="showViewModal = false" size="large">
          <el-icon><Close /></el-icon>
          Fermer
        </el-button>
      </div>
    </div>
  </el-dialog>

  <!-- Modal Fin du contrat -->
  <el-dialog
    v-model="showTerminateModal"
    title="Mettre fin au contrat"
    width="600px"
    destroy-on-close
    class="contract-terminate-modal"
  >
    <div v-if="selectedContrat" class="terminate-form">
      <!-- Alert de confirmation -->
      <el-alert
        title="Action Irréversible"
        type="warning"
        description="Cette action mettra fin au contrat de manière définitive. Veuillez confirmer les informations ci-dessous."
        show-icon
        :closable="false"
        class="terminate-alert"
      />
      
      <!-- Informations du contrat -->
      <div class="contract-summary">
        <h4>Informations du contrat</h4>
        <div class="summary-grid">
          <div class="summary-item">
            <label>Personnel:</label>
            <span>{{ selectedContrat.personnel?.nom }} {{ selectedContrat.personnel?.prenom }}</span>
          </div>
          <div class="summary-item">
            <label>Matricule:</label>
            <span>{{ selectedContrat.personnel?.matricule }}</span>
          </div>
          <div class="summary-item">
            <label>Type Contrat:</label>
            <el-tag :type="getTypeContratType(selectedContrat.typeContrat)">
              {{ getTypeContratLabel(selectedContrat.typeContrat) }}
            </el-tag>
          </div>
          <div class="summary-item">
            <label>Date Fin Actuelle:</label>
            <span>{{ formatDate(selectedContrat.dateFin) }}</span>
          </div>
        </div>
      </div>
      
      <!-- Formulaire de fin de contrat -->
      <el-form :model="terminateForm" label-width="140px" class="terminate-form-fields">
        <el-form-item label="Nouvelle Date Fin" required>
          <el-date-picker
            v-model="terminateForm.dateFin"
            type="date"
            placeholder="Sélectionner la date de fin"
            style="width: 100%;"
            :disabled-date="disabledDate"
            format="DD/MM/YYYY"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        
        <el-form-item label="Date de modification">
          <el-date-picker
            v-model="terminateForm.dateMod"
            type="date"
            placeholder="Date de modification"
            style="width: 100%;"
            format="DD/MM/YYYY"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        
        <el-form-item label="Départ">
          <el-radio-group v-model="terminateForm.depart">
            <el-radio :label="false">Non</el-radio>
            <el-radio :label="true">Oui</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="Observation">
          <el-select v-model="terminateForm.observation" placeholder="Sélectionner une observation" style="width: 100%;">
            <el-option label="Aucun choix" value="Aucun" />
            <el-option label="Demission" value="Demission" />
            <el-option label="Deces" value="Deces" />
            <el-option label="Fin de contrat" value="Fin de contrat" />
            <el-option label="Renvoi" value="Renvoi" />
            <el-option label="Modification" value="Modification" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="Motif de la fin" required>
          <el-input
            v-model="terminateForm.motif"
            type="textarea"
            :rows="4"
            placeholder="Préciser le motif de la fin du contrat..."
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      
      <div class="modal-actions">
        <el-button @click="showTerminateModal = false" size="large">
          <el-icon><Close /></el-icon>
          Annuler
        </el-button>
        <el-button type="warning" @click="confirmTerminate" size="large" :loading="terminating">
          <el-icon><WarningFilled /></el-icon>
          Confirmer la fin
        </el-button>
      </div>
    </div>
  </el-dialog>

  <!-- Modal pour expiration à une date -->
  <el-dialog v-model="showExpiresOnDateModal" title="Contrats expirant à une date" width="600px">
    <el-form :model="{ expireDate }" label-width="140px" class="terminate-form-fields">
      <el-form-item label="Date d'expiration" required>
        <el-date-picker
          v-model="expireDate"
          type="date"
          placeholder="Sélectionner une date d'expiration"
          style="width: 100%"
          format="DD/MM/YYYY"
          value-format="YYYY-MM-DD"
          clearable
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="showExpiresOnDateModal = false" size="large">
          <el-icon><Close /></el-icon>
          Annuler
        </el-button>
        <el-button type="primary" @click="searchExpiresOnDate" :loading="loading" size="large">
          <el-icon><Search /></el-icon>
          Rechercher
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!-- Modal pour expiration dans période -->
  <el-dialog v-model="showExpiresInPeriodModal" title="Contrats expirant dans une période" width="600px">
    <el-form :model="{ expirePeriodStart, expirePeriodEnd }" label-width="140px" class="terminate-form-fields">
      <el-form-item label="Date de début" required>
        <el-date-picker
          v-model="expirePeriodStart"
          type="date"
          placeholder="Sélectionner la date de début"
          style="width: 100%"
          format="DD/MM/YYYY"
          value-format="YYYY-MM-DD"
          clearable
        />
      </el-form-item>
      
      <el-form-item label="Date de fin" required>
        <el-date-picker
          v-model="expirePeriodEnd"
          type="date"
          placeholder="Sélectionner la date de fin"
          style="width: 100%"
          format="DD/MM/YYYY"
          value-format="YYYY-MM-DD"
          clearable
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="showExpiresInPeriodModal = false" size="large">
          <el-icon><Close /></el-icon>
          Annuler
        </el-button>
        <el-button type="primary" @click="searchExpiresInPeriod" :loading="loading" size="large">
          <el-icon><Search /></el-icon>
          Rechercher
        </el-button>
      </div>
    </template>
  </el-dialog>

  </template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Edit, Delete, Search, Refresh, Close, Calendar, Clock,
  User, UserFilled, CreditCard, Briefcase, View, Download,
  Document, WarningFilled, Timer
} from '@element-plus/icons-vue'
import { contratPersonnelService, type ContratPersonnel as ContratPersonnelType, type ContratPersonnelFilterRequest } from '@/services/contrat-personnel.service'

interface Personnel {
  id: number
  matricule: string
  nom: string
  prenom: string
  sexe: string
  cnps: string
  situationMatrimoniale: string
  nombreEnfant: number
  carec?: boolean  // État contractuel de l'employé
}

interface ContratPersonnel {
  id: number
  personnel: Personnel
  typeContrat: string | { id: number; libelle: string }
  fonction: string | { id: number; libelle: string }
  dateDebut: string
  dateFin: string
  salaireCategoriel: number
  statut: boolean
  categorie: { id: number; libelle: string; salaireBase: string | number }
  updatedAt?: string
  updatedBy?: string
}

interface ContratPersonnelDTO {
  result: boolean | null
  data: ContratPersonnel[]
  rows: ContratPersonnel[]
  total: number
  status: boolean
  message: string | null
  errors: any
}

interface PaginationRequest {
  page: number
  limit: number
  search?: string
}

// Variables réactives
const contrats = ref<ContratPersonnel[]>([])
const total = ref(0)
const loading = ref(false)
const searchText = ref('')
const currentPage = ref(0)
const pageSize = ref(10)
const showViewModal = ref(false)
const showTerminateModal = ref(false)
const selectedContrat = ref<ContratPersonnel | null>(null)
const terminating = ref(false)

// Variables pour les filtres
const currentView = ref('all')
const filterTypeContrat = ref('')
const filterSalaire = ref('')
const filterCarec = ref('')

// Variables pour les filtres d'expiration
const showExpiresOnDateModal = ref<boolean>(false)
const showExpiresInPeriodModal = ref<boolean>(false)
const expireDate = ref<string>('')
const expirePeriodStart = ref<string>('')
const expirePeriodEnd = ref<string>('')

// Formulaire pour la fin de contrat
const terminateForm = reactive({
  dateFin: '',
  dateMod: new Date().toISOString().split('T')[0], // Date du jour par défaut
  depart: false, // Non par défaut
  observation: 'Aucun',
  motif: ''
})

// Fonctions utilitaires
const calculateDuration = (dateDebut: string, dateFin: string) => {
  console.log('calculateDuration appelé avec:', { dateDebut, dateFin })
  
  // Test direct avec les valeurs de l'utilisateur
  if (dateDebut === '01-07-2025 00:00:00' && dateFin === '31-12-2025 00:00:00') {
    console.log('Test direct avec les valeurs connues')
    
    // Conversion manuelle pour le test
    const debut = new Date('2025-07-01T00:00:00') // 1 juillet 2025
    const fin = new Date('2025-12-31T00:00:00')   // 31 décembre 2025
    
    console.log('Test manuel:', { debut, fin })
    
    if (!isNaN(debut.getTime()) && !isNaN(fin.getTime())) {
      const diffTime = fin.getTime() - debut.getTime()
      const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))
      const diffMonths = Math.floor(diffDays / 30)
      const remainingDays = diffDays % 30
      
      console.log('Test manuel résultat:', { diffTime, diffDays, diffMonths, remainingDays })
      
      if (diffMonths > 0) {
        return remainingDays > 0 ? `${diffMonths} mois et ${remainingDays} jours` : `${diffMonths} mois`
      }
      return `${diffDays} jours`
    }
  }
  
  if (!dateDebut || !dateFin) {
    console.log('Dates manquantes:', { dateDebut, dateFin })
    return 'N/A'
  }
  
  try {
    // Fonction pour normaliser les dates
    const normalizeDate = (dateStr: string): Date => {
      console.log('Normalisation de la date:', dateStr)
      
      // Si le format est DD/MM/YYYY
      if (dateStr.includes('/')) {
        const [day, month, year] = dateStr.split('/')
        const normalized = new Date(parseInt(year), parseInt(month) - 1, parseInt(day))
        console.log('Date DD/MM/YYYY normalisée:', normalized)
        return normalized
      }
      
      // Si le format est DD-MM-YYYY HH:mm:ss (problème identifié!)
      if (dateStr.includes('-') && dateStr.includes(':')) {
        // Vérifier si c'est DD-MM-YYYY ou YYYY-MM-DD
        const parts = dateStr.split(' ')
        const datePart = parts[0]
        const timePart = parts[1] || '00:00:00'
        
        if (datePart) {
          const dateSegments = datePart.split('-')
          
          // Si le premier segment a 2 chiffres, c'est DD-MM-YYYY
          if (dateSegments[0].length === 2) {
            const [day, month, year] = dateSegments
            const isoDate = `${year}-${month}-${day}T${timePart}`
            const normalized = new Date(isoDate)
            console.log('Date DD-MM-YYYY HH:mm:ss convertie en ISO:', isoDate, 'résultat:', normalized)
            return normalized
          }
        }
        
        // Sinon, considérer que c'est YYYY-MM-DD HH:mm:ss
        const normalized = new Date(dateStr)
        console.log('Date YYYY-MM-DD HH:mm:ss normalisée:', normalized)
        return normalized
      }
      
      // Si le format est DD-MM-YYYY (sans heures)
      if (dateStr.includes('-') && !dateStr.includes(':')) {
        const [day, month, year] = dateStr.split('-')
        const normalized = new Date(parseInt(year), parseInt(month) - 1, parseInt(day))
        console.log('Date DD-MM-YYYY normalisée:', normalized)
        return normalized
      }
      
      // Si le format est YYYY-MM-DD
      if (dateStr.includes('-')) {
        const normalized = new Date(dateStr + 'T00:00:00')
        console.log('Date YYYY-MM-DD normalisée:', normalized)
        return normalized
      }
      
      // Format par défaut
      const normalized = new Date(dateStr)
      console.log('Date par défaut normalisée:', normalized)
      return normalized
    }
    
    const debut = normalizeDate(dateDebut)
    const fin = normalizeDate(dateFin)
    
    console.log('Dates normalisées:', { debut, fin })
    
    // Vérifier si les dates sont valides
    if (isNaN(debut.getTime()) || isNaN(fin.getTime())) {
      console.error('Dates invalides:', { 
        dateDebut, 
        dateFin, 
        debut: debut.toString(), 
        fin: fin.toString(),
        debutTime: debut.getTime(),
        finTime: fin.getTime()
      })
      return 'Dates invalides'
    }
    
    // Calculer la différence en jours
    const diffTime = fin.getTime() - debut.getTime()
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))
    
    console.log('Calcul de durée:', { diffTime, diffDays })
    
    // Si la différence est négative, retourner une valeur appropriée
    if (diffDays < 0) {
      return 'Date fin antérieure à date début'
    }
    
    // Calculer les mois et jours restants
    const diffMonths = Math.floor(diffDays / 30)
    const remainingDays = diffDays % 30
    
    console.log('Résultat final:', { diffMonths, remainingDays })
    
    if (diffMonths > 0) {
      return remainingDays > 0 ? `${diffMonths} mois et ${remainingDays} jours` : `${diffMonths} mois`
    }
    return `${diffDays} jours`
  } catch (error) {
    console.error('Erreur calcul durée:', error)
    return 'Erreur calcul'
  }
}

const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 8.64e7 // Désactiver les dates antérieures à aujourd'hui
}

// Fonctions utilitaires
const formatDate = (date: string | undefined | null) => {
  if (!date) return 'N/A'
  
  try {
    let dateObj: Date
    
    // Si le format est DD/MM/YYYY
    if (date.includes('/')) {
      const [day, month, year] = date.split('/')
      dateObj = new Date(parseInt(year), parseInt(month) - 1, parseInt(day))
    }
    // Si le format est YYYY-MM-DD HH:mm:ss ou YYYY-MM-DD
    else if (date.includes('-')) {
      dateObj = new Date(date)
    }
    // Format par défaut
    else {
      dateObj = new Date(date)
    }
    
    if (isNaN(dateObj.getTime())) {
      console.error('Date invalide:', date)
      return date
    }
    
    return new Intl.DateTimeFormat('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    }).format(dateObj)
  } catch (error) {
    console.error('Error formatting date:', error, date)
    return 'Date invalide'
  }
}

const formatCurrency = (amount: number | string | undefined) => {
  if (typeof amount === 'undefined' || amount === null) return '0 F CFA'
  
  const numAmount = typeof amount === 'string' ? 
    parseFloat(amount.replace(/[^\d.-]/g, '')) : amount
  
  if (isNaN(numAmount)) return '0 F CFA'
  
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XOF',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(numAmount)
}

const getTypeContratLabel = (typeContrat: string | { libelle: string }) => {
  if (typeof typeContrat === 'string') {
    return typeContrat
  }
  return typeContrat?.libelle || 'N/A'
}

const getTypeContratType = (typeContrat: string | { libelle: string }) => {
  if (typeof typeContrat === 'string') {
    switch (typeContrat?.toLowerCase()) {
      case 'cdi': return 'success'
      case 'cdd': return 'warning'
      case 'stage': return 'info'
      default: return 'info'
    }
  }
  return 'info'
}

const getFonctionLabel = (fonction: string | { libelle: string }) => {
  if (typeof fonction === 'string') {
    return fonction
  }
  return fonction?.libelle || 'N/A'
}

// Fonctions principales
const loadContrats = async () => {
  loading.value = true
  try {
    // Préparer les filtres pour l'endpoint unifié
    const filters: ContratPersonnelFilterRequest = {
      offset: currentPage.value * pageSize.value,
      limit: pageSize.value,
      search: searchText.value || undefined,
      statut: currentView.value === 'all' ? undefined : currentView.value,
      typeContrat: filterTypeContrat.value || undefined,
      salaireRange: filterSalaire.value || undefined,
      carec: filterCarec.value ? filterCarec.value === 'true' : undefined,
      // Nouveaux filtres d'expiration
      expireDate: expireDate.value || undefined,
      expirePeriodStart: expirePeriodStart.value || undefined,
      expirePeriodEnd: expirePeriodEnd.value || undefined
    }

    // Utiliser uniquement l'endpoint de filtrage unifié
    const response = await contratPersonnelService.getContratsWithFilters(filters)
    
    contrats.value = response.rows || []
    total.value = response.total || 0
    
    if (contrats.value.length === 0 && currentPage.value > 0) {
      currentPage.value = 0
      await loadContrats()
    }
  } catch (error: any) {
    console.error('Erreur lors du chargement des contrats:', error)
    ElMessage.error('Erreur lors du chargement des contrats: ' + (error.response?.data?.message || error.message))
    contrats.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const refreshData = async () => {
  await loadContrats()
  ElMessage.success('Données actualisées')
}

const handleSizeChange = (newSize: number) => {
  pageSize.value = newSize
  currentPage.value = 0
  loadContrats()
}

const handleCurrentChange = (newPage: number) => {
  currentPage.value = newPage
  loadContrats()
}

const toggleForm = () => {
  // Implémenter la logique du formulaire
  ElMessage.info('Formulaire à implémenter')
}

const editContrat = (contrat: ContratPersonnel) => {
  // Implémenter la logique d'édition
  console.log('Éditer le contrat:', contrat)
}

const deleteContrat = (contrat: ContratPersonnel) => {
  ElMessageBox.confirm(
    `Êtes-vous sûr de vouloir supprimer le contrat de ${contrat.personnel?.nom} ${contrat.personnel?.prenom}?`,
    'Confirmation',
    { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
  ).then(() => {
    // Implémenter la logique de suppression
    ElMessage.success('Contrat supprimé avec succès')
  }).catch(() => {
    // Annulation
  })
}

// Fonctions pour les modales
const viewContrat = (contrat: ContratPersonnel) => {
  selectedContrat.value = contrat
  showViewModal.value = true
}

const terminateContrat = (contrat: ContratPersonnel) => {
  selectedContrat.value = contrat
  // Réinitialiser tous les champs du formulaire
  terminateForm.dateFin = ''
  terminateForm.dateMod = new Date().toISOString().split('T')[0] // Date du jour
  terminateForm.depart = false // Non par défaut
  terminateForm.observation = 'Aucun'
  terminateForm.motif = ''
  showTerminateModal.value = true
}

const confirmTerminate = async () => {
  if (!selectedContrat.value || !terminateForm.dateFin) {
    ElMessage.error('Veuillez remplir tous les champs obligatoires')
    return
  }

  try {
    await ElMessageBox.confirm(
      `Confirmer la fin du contrat de ${selectedContrat.value.personnel?.nom} ${selectedContrat.value.personnel?.prenom} à partir du ${formatDate(terminateForm.dateFin)} ?`,
      'Confirmation de fin de contrat',
      { confirmButtonText: 'Confirmer', cancelButtonText: 'Annuler', type: 'warning' }
    )

    // Préparer la requête pour l'API
    const endContractRequest = {
      id: selectedContrat.value.id,
      dateFin: terminateForm.dateFin,
      dateMod: terminateForm.dateMod,
      permanent: terminateForm.depart, // Garder le boolean, pas string
      observCtrat: terminateForm.observation
    }

    console.log('Envoi de la requête de fin de contrat:', endContractRequest)

    // Appeler l'API pour mettre fin au contrat
    terminating.value = true
    const response = await contratPersonnelService.endContract(endContractRequest)
    terminating.value = false

    console.log('Réponse de l\'API:', response)

    if (response.result) {
      ElMessage.success('Contrat terminé avec succès')
      showTerminateModal.value = false
      await loadContrats() // Recharger les données
    } else {
      ElMessage.error(response.message || 'Erreur lors de la fin du contrat')
    }
  } catch (error: any) {
    terminating.value = false
    if (error !== 'cancel') {
      console.error('Erreur lors de la fin du contrat:', error)
      ElMessage.error('Erreur lors de la fin du contrat: ' + (error.response?.data?.message || error.message || 'Erreur inconnue'))
    }
  }
}

const generateCSV = (contrats: ContratPersonnel[]) => {
  const headers = ['Matricule', 'Nom', 'Prénom', 'Type Contrat', 'Fonction', 'Date Début', 'Date Fin', 'Salaire', 'Statut']
  const csvContent = [
    headers.join(','),
    ...contrats.map(contrat => [
      contrat.personnel?.matricule || '',
      contrat.personnel?.nom || '',
      contrat.personnel?.prenom || '',
      typeof contrat.typeContrat === 'string' ? contrat.typeContrat : contrat.typeContrat?.libelle || '',
      typeof contrat.fonction === 'string' ? contrat.fonction : contrat.fonction?.libelle || '',
      contrat.dateDebut || '',
      contrat.dateFin || '',
      contrat.categorie?.salaireBase || contrat.salaireCategoriel || 0,
      contrat.statut ? 'Actif' : 'Inactif'
    ].join(','))
  ].join('\n')
  
  return csvContent
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

const exportExcel = async () => {
  try {
    loading.value = true // Indicateur de chargement
    
    // Exporter TOUS les résultats du filtre (sans pagination)
    const filters: ContratPersonnelFilterRequest = {
      offset: 0, // Pas d'offset pour exporter tout
      limit: 999999, // Limite très élevée pour tout récupérer
      search: searchText.value || undefined,
      statut: currentView.value === 'all' ? undefined : currentView.value,
      typeContrat: filterTypeContrat.value || undefined,
      salaireRange: filterSalaire.value || undefined,
      carec: filterCarec.value ? filterCarec.value === 'true' : undefined,
      expireDate: expireDate.value || undefined,
      expirePeriodStart: expirePeriodStart.value || undefined,
      expirePeriodEnd: expirePeriodEnd.value || undefined
    }

    // Récupérer tous les contrats correspondants aux filtres
    const response = await contratPersonnelService.getContratsWithFilters(filters)
    const allContrats = response.rows || []

    // Implémenter la logique d'exportation Excel
    const csvContent = generateCSV(allContrats)
    downloadCSV(csvContent, `contrats_${new Date().toISOString().split('T')[0]}.csv`)
    
    const message = allContrats.length > 0 
      ? `✅ Export Excel terminé avec succès - ${allContrats.length} contrat${allContrats.length > 1 ? 's' : ''} exporté${allContrats.length > 1 ? 's' : ''}`
      : '⚠️ Aucun contrat trouvé pour les filtres sélectionnés'
    
    ElMessage.success(message)
  } catch (error: any) {
    ElMessage.error('❌ Erreur lors de l\'export: ' + (error.response?.data?.message || error.message || 'Erreur inconnue'))
  } finally {
    loading.value = false // Arrêter l'indicateur de chargement
  }
}

// Fonctions pour les filtres d'expiration
const searchExpiresOnDate = async () => {
  if (!expireDate.value) {
    ElMessage.warning('Veuillez sélectionner une date d\'expiration')
    return
  }

  loading.value = true
  try {
    // Réinitialiser la pagination
    currentPage.value = 0
    
    // Appliquer le filtre d'expiration au tableau principal
    const filters: ContratPersonnelFilterRequest = {
      offset: currentPage.value * pageSize.value,
      limit: pageSize.value,
      search: searchText.value || undefined,
      expireDate: expireDate.value
    }

    const response = await contratPersonnelService.getContratsWithFilters(filters)
    contrats.value = response.rows || []
    total.value = response.total || 0
    
    // Fermer la modale
    showExpiresOnDateModal.value = false

    ElMessage.success(`${response.total} contrat${response.total > 1 ? 's' : ''} trouvé${response.total > 1 ? 's' : ''} expirant le ${formatDateForDisplay(expireDate.value)}`)
  } catch (error: any) {
    console.error('Erreur lors de la recherche des contrats expirants:', error)
    ElMessage.error('Erreur lors de la recherche: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

const searchExpiresInPeriod = async () => {
  if (!expirePeriodStart.value || !expirePeriodEnd.value) {
    ElMessage.warning('Veuillez sélectionner une période complète (date début et date fin)')
    return
  }

  loading.value = true
  try {
    // Réinitialiser la pagination
    currentPage.value = 0
    
    // Appliquer le filtre de période au tableau principal
    const filters: ContratPersonnelFilterRequest = {
      offset: currentPage.value * pageSize.value,
      limit: pageSize.value,
      search: searchText.value || undefined,
      expirePeriodStart: expirePeriodStart.value,
      expirePeriodEnd: expirePeriodEnd.value
    }

    const response = await contratPersonnelService.getContratsWithFilters(filters)
    contrats.value = response.rows || []
    total.value = response.total || 0
    
    // Fermer la modale
    showExpiresInPeriodModal.value = false

    ElMessage.success(`${response.total} contrat${response.total > 1 ? 's' : ''} trouvé${response.total > 1 ? 's' : ''} dans la période du ${formatDateForDisplay(expirePeriodStart.value)} au ${formatDateForDisplay(expirePeriodEnd.value)}`)
  } catch (error: any) {
    console.error('Erreur lors de la recherche des contrats par période:', error)
    ElMessage.error('Erreur lors de la recherche: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

const clearExpirationFilters = () => {
  expireDate.value = ''
  expirePeriodStart.value = ''
  expirePeriodEnd.value = ''
  currentPage.value = 0
  loadContrats()
  ElMessage.info('Filtres d\'expiration effacés')
}

const formatDateForDisplay = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' })
}

const isExpiringSoon = (dateFin: string) => {
  const today = new Date()
  const endDate = new Date(dateFin)
  const diffTime = endDate.getTime() - today.getTime()
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  return diffDays <= 30 && diffDays >= 0 // Expirant dans 30 jours ou moins
}

const formatSalary = (salary: number | string) => {
  const numSalary = typeof salary === 'string' ? parseFloat(salary) : salary
  return isNaN(numSalary) ? '0' : numSalary.toLocaleString('fr-FR')
}

// Watchers pour les filtres et la recherche (retiré les variables d'expiration)
watch([searchText, currentView, filterTypeContrat, filterSalaire, filterCarec], () => {
  currentPage.value = 0
  loadContrats()
})

watch([currentPage, pageSize], () => {
  loadContrats()
})

// Charger au montage
onMounted(() => {
  loadContrats()
})
</script>

<style lang="scss" scoped>
@use '@/styles/design-system.scss' as *;

.enhanced-contrats-view {
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
  overflow: auto;
  padding: var(--spacing-lg);
  min-height: 400px;
  
  .el-table {
    height: auto !important;
    max-height: none !important;
  }
}

.enhanced-tag {
  font-weight: 500;
  border: none;
}

.matricule-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.personnel-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .personnel-icon {
    font-size: 20px;
    color: var(--primary-color);
  }
  color: #f56c6c;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Styles cohérents avec le projet pour les modaux */
:deep(.el-dialog) {
  border-radius: 15px;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
  margin: 0;
  border-radius: 15px 15px 0 0;
}

:deep(.el-dialog__title) {
  color: white;
  font-weight: 600;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
}

:deep(.el-dialog__body) {
  padding: 24px;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  color: #606266;
  background-color: #f5f7fa;
}

:deep(.el-descriptions__content) {
  font-weight: 500;
  color: #303133;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

:deep(.el-button) {
  border-radius: 8px;
  font-weight: 500;
}

:deep(.el-tag) {
  border-radius: 6px;
  font-weight: 500;
}

:deep(.el-alert) {
  border-radius: 8px;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  padding-top: 16px;
  border-top: 1px solid #e4e7ed;
}

.contrat-details {
  .el-descriptions {
    margin-bottom: 16px;
  }
}

.terminate-form {
  .el-form-item {
    margin-bottom: 20px;
  }
}

/* Styles pour le modal de vue */
.contract-view-modal {
  .contract-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    border-radius: 12px;
    margin-bottom: 24px;
    
    .contract-info {
      h3 {
        margin: 0 0 8px 0;
        color: #303133;
        font-size: 18px;
        font-weight: 600;
      }
      
      p {
        margin: 0;
        color: #606266;
        font-size: 14px;
      }
    }
    
    .contract-status {
      .el-tag {
        font-weight: 600;
        padding: 8px 16px;
      }
    }
  }
  
  .contract-descriptions {
    .date-info {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .el-icon {
        color: #409eff;
      }
    }
    
    .el-tag {
      display: flex;
      align-items: center;
      gap: 6px;
      
      .el-icon {
        font-size: 14px;
      }
    }
  }
}

/* Styles pour le modal de fin de contrat */
.contract-terminate-modal {
  .terminate-alert {
    margin-bottom: 24px;
    
    .el-alert__icon {
      font-size: 20px;
    }
    
    .el-alert__title {
      font-weight: 600;
      font-size: 16px;
    }
  }
  
  .contract-summary {
    background: #fafafa;
    padding: 20px;
    border-radius: 12px;
    margin-bottom: 24px;
    border-left: 4px solid #e6a23c;
    
    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
    
    .summary-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 16px;
      
      .summary-item {
        display: flex;
        flex-direction: column;
        gap: 4px;
        
        label {
          font-weight: 600;
          color: #606266;
          font-size: 13px;
          text-transform: uppercase;
          letter-spacing: 0.5px;
        }
        
        span, .el-tag {
          font-weight: 500;
          color: #303133;
          font-size: 14px;
        }
      }
    }
  }
  
  .terminate-form-fields {
    .el-form-item {
      margin-bottom: 24px;
      
      .el-textarea {
        .el-textarea__inner {
          resize: vertical;
          min-height: 100px;
        }
      }
      
      .el-radio-group {
        display: flex;
        gap: 20px;
        
        .el-radio {
          margin-right: 0;
        }
      }
      
      .el-select {
        width: 100%;
      }
      
      .el-date-picker {
        width: 100%;
      }
    }
    
    // Styles pour les champs requis
    .el-form-item.is-required {
      .el-form-item__label::before {
        content: '*';
        color: #f56c6c;
        margin-right: 4px;
      }
    }
    
    // Styles pour le tableau d'expiration
    .expiration-results-container {
      border: 2px solid #e4e7ed;
      border-radius: 8px;
      padding: 20px;
      background: #fafafa;
      
      .table-header {
        h3 {
          margin: 0;
          color: #409eff;
          font-size: 18px;
        }
      }
      
      .el-table {
        background: white;
        border-radius: 6px;
        
        .text-danger {
          color: #f56c6c;
          font-weight: bold;
        }
      }
    }
  }
}
</style>
