<template>
  <div class="personnel-wizard-view">
    <!-- Header -->
    <div class="wizard-header">
      <div class="header-content">
        <div class="header-left">
          <h1>{{ isEditing ? 'Modifier un Personnel' : 'Ajouter un Nouveau Personnel' }}</h1>
          <p>{{ isEditing ? 'Modifiez les informations du personnel' : 'Remplissez les informations étape par étape' }}</p>
        </div>
        <div class="header-actions">
          <el-button @click="goBack" size="large" :disabled="loading">
            <el-icon><ArrowLeft /></el-icon>
            Retour à la liste
          </el-button>
        </div>
      </div>
    </div>

    <!-- Steps Navigation Vertical -->
    <div class="wizard-layout">
      <div class="wizard-sidebar">
        <div class="steps-vertical">
          <div class="step-item" :class="{ active: currentStep === 1, completed: currentStep > 1 }">
            <div class="step-number" @click="goToStep(1)">
              <el-icon v-if="currentStep > 1"><Check /></el-icon>
              <span v-else>1</span>
            </div>
            <div class="step-content">
              <div class="step-title">Informations personnelles</div>
              <div class="step-desc">Identité et coordonnées</div>
            </div>
          </div>
          
          <div class="step-line" :class="{ completed: currentStep > 1 }"></div>
          
          <div class="step-item" :class="{ active: currentStep === 2, completed: currentStep > 2 }">
            <div class="step-number" @click="goToStep(2)">
              <el-icon v-if="currentStep > 2"><Check /></el-icon>
              <span v-else>2</span>
            </div>
            <div class="step-content">
              <div class="step-title">Autres informations</div>
              <div class="step-desc">Banque et contact</div>
            </div>
          </div>
          
          <div class="step-line" :class="{ completed: currentStep > 2 }"></div>
          
          <div class="step-item" :class="{ active: currentStep === 3, completed: currentStep > 3 }">
            <div class="step-number" @click="goToStep(3)">
              <el-icon v-if="currentStep > 3"><Check /></el-icon>
              <span v-else>3</span>
            </div>
            <div class="step-content">
              <div class="step-title">Contrat</div>
              <div class="step-desc">Poste et rémunération</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Wizard Content -->
      <div class="wizard-main">
      <el-card class="wizard-card">
        <el-form :model="wizardForm" label-position="top">
          <!-- Step 1: Informations Personnelles -->
          <div v-show="currentStep === 1" class="step-content">
            <h4>Informations Personnelles</h4>
            
            <!-- Section Photo -->
            <div class="photo-section">
              <div class="photo-upload">
                <div class="photo-preview" :class="{ 'has-photo': wizardForm.photo }">
                  <img v-if="wizardForm.photo" :src="wizardForm.photo" alt="Photo employé" />
                  <div v-else class="photo-placeholder">
                    <el-icon class="photo-icon"><User /></el-icon>
                    <span>Ajouter une photo</span>
                  </div>
                </div>
                <div class="photo-actions">
                  <input 
                    type="file" 
                    ref="photoInput" 
                    @change="handlePhotoUpload" 
                    accept="image/*"
                    style="display: none"
                  />
                  <el-button 
                    type="primary" 
                    @click="$refs.photoInput?.click()"
                    size="small"
                  >
                    {{ wizardForm.photo ? 'Changer la photo' : 'Ajouter une photo' }}
                  </el-button>
                  <el-button 
                    v-if="wizardForm.photo" 
                    type="danger" 
                    @click="removePhoto"
                    size="small"
                  >
                    Supprimer
                  </el-button>
                </div>
              </div>
            </div>
            
            <div class="form-grid">
              <el-form-item label="Matricule" required :error="!!matriculeError">
                <div style="display: flex; gap: 8px;">
                  <el-input 
                    v-model="wizardForm.matricule" 
                    placeholder="Matricule unique"
                    size="large"
                    :class="{ 'error-input': matriculeError, 'success-input': matriculeVerified }"
                    @blur="checkMatriculeManually"
                  >
                    <template #prefix>
                      <el-icon><User /></el-icon>
                    </template>
                  </el-input>
                  <el-button 
                    v-if="!isEditing && wizardForm.matricule && !matriculeVerified"
                    @click="checkMatriculeManually"
                    size="large"
                    type="warning"
                    :icon="User"
                  >
                    Vérifier
                  </el-button>
                  <div 
                    v-if="matriculeVerified" 
                    class="verified-indicator"
                    title="Matricule vérifié et unique"
                  >
                    ✅
                  </div>
                </div>
                <div v-if="matriculeError" class="error-message">
                  {{ matriculeError }}
                </div>
                <div v-if="matriculeVerified && !matriculeError" class="success-message">
                  🎉 Matricule unique et disponible !
                </div>
              </el-form-item>

              <el-form-item label="Type Employé" required>
                <el-select v-model="wizardForm.typeEmploye" placeholder="Type employé" size="large">
                  <el-option label="Mensuel" value="M" />
                  <el-option label="Journalier" value="J" />
                  <el-option label="Horaire" value="H" />
                </el-select>
              </el-form-item>

              <el-form-item label="Médaille">
                <el-select v-model="wizardForm.medaille" placeholder="Médaille" size="large">
                  <el-option label="Aucun" value="AUCUN" />
                  <el-option label="Honneur" value="1" />
                  <el-option label="Vermeille" value="2" />
                  <el-option label="Argent" value="3" />
                  <el-option label="Or" value="4" />
                </el-select>
              </el-form-item>

              <el-form-item label="Nom" required>
                <el-input 
                  v-model="wizardForm.nom" 
                  placeholder="Nom de famille"
                  size="large"
                />
              </el-form-item>

              <el-form-item label="Prénom(s)" required>
                <el-input 
                  v-model="wizardForm.prenom" 
                  placeholder="Prénom(s)"
                  size="large"
                />
              </el-form-item>

              <el-form-item label="Qualité d'emploi" required>
                <el-select v-model="wizardForm.qualiteEmploi" placeholder="Qualité d'emploi" size="large">
                  <el-option label="Aucun" value="AUCUN" />
                  <el-option label="Directeur" value="1" />
                  <el-option label="Cadre Supérieur" value="2" />
                  <el-option label="Cadre Moyen" value="3" />
                  <el-option label="Employé Qualifié" value="4" />
                  <el-option label="Employé Non Qualifié" value="5" />
                  <el-option label="Ouvrier Qualifié" value="6" />
                  <el-option label="Ouvrier Non Qualifié" value="7" />
                  <el-option label="Autre" value="8" />
                </el-select>
              </el-form-item>

              <el-form-item label="Né(e) le" required>
                <el-date-picker
                  v-model="wizardForm.dateNaissance"
                  type="date"
                  placeholder="Date de naissance"
                  size="large"
                  style="width: 100%"
                  format="DD/MM/YYYY"
                  value-format="YYYY-MM-DD"
                />
              </el-form-item>

              <el-form-item label="Lieu de Naissance" required>
                <el-input 
                  v-model="wizardForm.lieuNaissance" 
                  placeholder="Lieu de naissance"
                  size="large"
                />
              </el-form-item>

              <el-form-item label="Sexe" required>
                <el-radio-group v-model="wizardForm.sexe">
                  <el-radio value="Masculin">Masculin</el-radio>
                  <el-radio value="Féminin">Féminin</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="Nationalité" required>
                <el-select v-model="wizardForm.nationalite" placeholder="Nationalité" size="large" filterable>
                  <el-option 
                    v-for="nationnalite in nationalites" 
                    :key="nationnalite.id" 
                    :label="nationnalite.libelle" 
                    :value="nationnalite.libelle" 
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="Situation Matrimoniale" required>
                <el-select v-model="wizardForm.situationMatrimoniale" placeholder="Situation matrimoniale" size="large">
                  <el-option label="Marié(e)" value="1" />
                  <el-option label="Célibataire" value="2" />
                  <el-option label="Divorcé(e)" value="3" />
                  <el-option label="Veuf(ve)" value="4" />
                </el-select>
              </el-form-item>

              <el-form-item label="Nombre d'enfants" required>
                <el-select v-model="wizardForm.nombreEnfants" placeholder="Nombre d'enfants" size="large">
                  <el-option v-for="n in 11" :key="n-1" :label="n-1" :value="n-1" />
                </el-select>
              </el-form-item>
            </div>
          </div>

          <!-- Step 2: Autres Informations -->
          <div v-show="currentStep === 2" class="step-content">
            <h4>Autres Informations</h4>
            
            <div class="form-grid">
              <el-form-item label="Numéro CNPS">
                <el-input 
                  v-model="wizardForm.numeroCNPS" 
                  placeholder="Numéro CNPS"
                  size="large"
                />
              </el-form-item>

              <el-form-item label="Résidence">
                <el-input 
                  v-model="wizardForm.residence" 
                  placeholder="Résidence"
                  size="large"
                />
              </el-form-item>

              <el-form-item label="Téléphone" required>
                <el-input 
                  v-model="wizardForm.telephone" 
                  placeholder="Téléphone"
                  size="large"
                />
              </el-form-item>

              <el-form-item label="Date d'arrivée" required>
                <el-date-picker
                  v-model="wizardForm.dateArrivee"
                  type="date"
                  placeholder="Date d'arrivée"
                  size="large"
                  style="width: 100%"
                  format="DD/MM/YYYY"
                  value-format="YYYY-MM-DD"
                />
              </el-form-item>

              <el-form-item label="Date de retour du dernier congé">
                <el-date-picker
                  v-model="wizardForm.dateRetourConge"
                  type="date"
                  placeholder="Date de retour du dernier congé"
                  size="large"
                  format="DD/MM/YYYY"
                  value-format="YYYY-MM-DD"
                />
              </el-form-item>

              <el-form-item label="Email">
                <el-input 
                  v-model="wizardForm.email" 
                  placeholder="Email"
                  size="large"
                  type="email"
                />
              </el-form-item>

              <el-form-item label="Contractuel">
                <el-radio-group v-model="wizardForm.contractuel">
                  <el-radio :value="true">Oui</el-radio>
                  <el-radio :value="false">Non</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="Banque">
                <el-select v-model="wizardForm.banque" placeholder="Banque" size="large" filterable>
                  <el-option 
                    v-for="banque in banques" 
                    :key="banque.id" 
                    :label="banque.libelle" 
                    :value="banque.libelle" 
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="Numéro de guichet">
                <el-input 
                  v-model="wizardForm.numeroGuichet" 
                  placeholder="Numéro de guichet"
                  size="large"
                  maxlength="5"
                />
              </el-form-item>

              <el-form-item label="Numéro de compte">
                <el-input 
                  v-model="wizardForm.numeroCompte" 
                  placeholder="Numéro de compte"
                  size="large"
                  maxlength="28"
                />
              </el-form-item>

              <el-form-item label="RIB">
                <el-input 
                  v-model="wizardForm.rib" 
                  placeholder="RIB"
                  size="large"
                  maxlength="2"
                />
              </el-form-item>

              <el-form-item label="Mode paiement" required>
                <el-select v-model="wizardForm.modePaiement" placeholder="Mode de paiement" size="large">
                  <el-option label="Virement Bancaire" value="VIREMENT_BANCAIRE" />
                  <el-option label="Transfert Mobile Money" value="TRANSFERT_MOBILE_MONEY" />
                  <el-option label="Transfert Wave" value="TRANSFERT_WAVE" />
                  <el-option label="Espèce" value="ESPECE" />
                  <el-option label="Chèque" value="CHEQUE" />
                </el-select>
              </el-form-item>

              <el-form-item label="Division" required>
                <el-select v-model="wizardForm.division" placeholder="Division" size="large" @change="onDivisionChange" filterable>
                  <el-option 
                    v-for="typeService in typeServices" 
                    :key="typeService.id" 
                    :label="typeService.libelle" 
                    :value="typeService.id.toString()" 
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="Libellé Division" required>
                <el-select v-model="wizardForm.libelleDivision" placeholder="Libellé Division" size="large" filterable>
                  <el-option 
                    v-for="service in services" 
                    :key="service.id" 
                    :label="service.libelle" 
                    :value="service.libelle" 
                  />
                </el-select>
              </el-form-item>
            </div>
          </div>

          <!-- Step 3: Contrat -->
          <div v-show="currentStep === 3" class="step-content">
            <h4>Contrat</h4>
            
            <!-- Message informatif pour mode édition -->
            <el-alert 
              v-if="isEditing"
              title="Informations de contrat non modifiables" 
              type="info" 
              description="Les informations de contrat ne peuvent pas être modifiées directement. Contactez l'administrateur pour toute modification."
              show-icon
              :closable="false"
              style="margin-bottom: 20px;"
            />
            
            <div class="form-grid">
              <el-form-item label="Emploi" required>
                <el-select v-model="wizardForm.emploi" placeholder="Emploi/Fonction" size="large" :disabled="isEditing && currentStep === 3" filterable>
                  <el-option 
                    v-for="fonction in fonctions" 
                    :key="fonction.id" 
                    :label="fonction.libelle" 
                    :value="fonction.libelle" 
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="Type Contrat" required>
                <el-select v-model="wizardForm.typeContrat" placeholder="Type de contrat" size="large" :disabled="isEditing && currentStep === 3" filterable>
                  <el-option 
                    v-for="typeContrat in typeContrats" 
                    :key="typeContrat.id" 
                    :label="typeContrat.libelle" 
                    :value="typeContrat.libelle" 
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="Catégorie" required>
                <el-select v-model="wizardForm.categorie" placeholder="Catégorie" size="large" :disabled="isEditing && currentStep === 3" filterable>
                  <el-option 
                    v-for="categorie in categories" 
                    :key="categorie.id" 
                    :label="categorie.libelle" 
                    :value="categorie.libelle" 
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="Indemnité de logement" required>
                <el-input 
                  v-model="wizardForm.indemniteLogement" 
                  placeholder="Indemnité de logement"
                  size="large"
                  type="number"
                  :disabled="isEditing"
                />
              </el-form-item>

              <el-form-item label="Indemnité de Transport" required>
                <el-input 
                  v-model="wizardForm.indemniteTransport" 
                  placeholder="Indemnité de transport"
                  size="large"
                  type="number"
                  :disabled="isEditing"
                />
              </el-form-item>

              <el-form-item label="Sursalaire" required>
                <el-input 
                  v-model="wizardForm.sursalaire" 
                  placeholder="Sursalaire"
                  size="large"
                  type="number"
                  :disabled="isEditing"
                />
              </el-form-item>

              <el-form-item label="Indemnité de Représentation" required>
                <el-input 
                  v-model="wizardForm.indemniteRepresentation" 
                  placeholder="Indemnité de représentation"
                  size="large"
                  type="number"
                  :disabled="isEditing"
                />
              </el-form-item>

              <el-form-item label="Date début du contrat" required>
                <el-date-picker
                  v-model="wizardForm.dateDebutContrat"
                  type="date"
                  placeholder="Date début du contrat"
                  size="large"
                  style="width: 100%"
                  format="DD/MM/YYYY"
                  value-format="YYYY-MM-DD"
                  :disabled="isEditing && currentStep === 3"
                  @change="validateContractDates"
                />
              </el-form-item>

              <el-form-item 
                label="Date fin du contrat" 
                :required="wizardForm.typeContrat === 'CDD'"
                v-if="wizardForm.typeContrat === 'CDD'"
              >
                <el-date-picker
                  v-model="wizardForm.dateFinContrat"
                  type="date"
                  placeholder="Date fin du contrat (max 2 ans)"
                  size="large"
                  style="width: 100%"
                  format="DD/MM/YYYY"
                  value-format="YYYY-MM-DD"
                  :disabled="isEditing && currentStep === 3"
                  @change="validateContractDates"
                />
              </el-form-item>

              <el-form-item label="Net à payer" required>
                <el-input 
                  v-model="wizardForm.salaireNet" 
                  placeholder="Net à payer"
                  size="large"
                  type="number"
                  :disabled="isEditing"
                />
              </el-form-item>

              <el-form-item label="Ancienneté initiale" required>
                <el-select v-model="wizardForm.ancienneteInitiale" placeholder="Ancienneté initiale" size="large" :disabled="isEditing">
                  <el-option v-for="n in 11" :key="n-1" :label="n-1 + ' an(s)'" :value="n-1" />
                </el-select>
              </el-form-item>
            </div>
          </div>
        </el-form>

        <!-- Wizard Actions -->
        <div class="wizard-actions">
          <el-button @click="previousStep" :disabled="currentStep === 1" size="large">
            <el-icon><ArrowLeft /></el-icon>
            Précédent
          </el-button>
          
          <div class="step-indicator">
            Étape {{ currentStep }} sur 3
          </div>
          
          <el-button 
            v-if="currentStep < 3" 
            type="primary" 
            @click="nextStep"
            size="large"
          >
            Suivant
            <el-icon><ArrowRight /></el-icon>
          </el-button>
          
          <el-button 
            v-if="currentStep === 3" 
            type="success" 
            @click="finishWizard"
            size="large"
            :loading="loading"
            :disabled="loading"
          >
            <el-icon v-if="!loading"><Check /></el-icon>
            {{ loading ? 'Sauvegarde...' : 'Terminer' }}
          </el-button>
        </div>
      </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  User, Check, ArrowLeft, ArrowRight
} from '@element-plus/icons-vue'
import { personnelRestService, type PersonnelRestDto } from '@/services/personnel.service'
import { nationnaliteService, type Nationnalite } from '@/services/nationalite.service'
import { referencesService, type Banque, type Fonction, type TypeContrat, type Categorie } from '@/services/references.service'
import { api } from '@/services/api'

interface WizardForm {
  id?: number // Ajouter l'ID pour le mode édition
  // Step 1: Informations Personnelles
  photo: string
  matricule: string
  typeEmploye: string
  medaille: string
  nom: string
  prenom: string
  qualiteEmploi: string
  dateNaissance: Date | null
  lieuNaissance: string
  sexe: string
  nationalite: string
  situationMatrimoniale: string
  nombreEnfants: number
  
  // Step 2: Autres Informations
  numeroCNPS: string
  residence: string
  telephone: string
  dateArrivee: Date | null
  dateRetourConge: Date | null
  email: string
  contractuel: boolean
  banque: string
  numeroGuichet: string
  numeroCompte: string
  rib: string
  modePaiement: string
  division: string
  libelleDivision: string
  
  // Step 3: Contrat
  emploi: string
  typeContrat: string
  categorie: string
  indemniteLogement: number
  indemniteTransport: number
  sursalaire: number
  indemniteRepresentation: number
  dateDebutContrat: Date | null
  dateFinContrat: Date | null
  salaireNet: number
  ancienneteInitiale: number
}

const router = useRouter()
const route = useRoute()
const currentStep = ref(1)
const photoInput = ref<HTMLInputElement>()
const isEditing = ref(false)
const loading = ref(false)
const contrats = ref<any[]>([]) // Stocker les contrats de l'employé
const nationalites = ref<Nationnalite[]>([]) // Stocker les nationalités
const banques = ref<Banque[]>([]) // Stocker les banques
const fonctions = ref<Fonction[]>([]) // Stocker les fonctions/emplois
const typeContrats = ref<TypeContrat[]>([]) // Stocker les types de contrats
const categories = ref<Categorie[]>([]) // Stocker les catégories
const typeServices = ref<any[]>([]) // Stocker les types de services
const services = ref<any[]>([]) // Stocker les services/départements
const matriculeError = ref<string>('') // Message d'erreur pour le matricule
const matriculeVerified = ref<boolean>(false) // État de vérification du matricule

const wizardForm = reactive<WizardForm>({
  // Step 1
  photo: '', matricule: '', typeEmploye: 'MENSUEL', medaille: 'AUCUN', nom: '', prenom: '',
  qualiteEmploi: 'AUCUN', dateNaissance: null, lieuNaissance: '', sexe: 'Masculin',
  nationalite: 'IVOIRIENNE', situationMatrimoniale: 'CELIBATAIRE', nombreEnfants: 0,
  // Step 2
  numeroCNPS: '', residence: '', telephone: '', dateArrivee: null, dateRetourConge: null,
  email: '', contractuel: false, banque: '', numeroGuichet: '', numeroCompte: '', rib: '',
  modePaiement: 'VIREMENT_BANCAIRE', division: '', libelleDivision: '',
  // Step 3
  emploi: '', typeContrat: '', categorie: 'A', indemniteLogement: 0, indemniteTransport: 0,
  sursalaire: 0, indemniteRepresentation: 0, dateDebutContrat: null, dateFinContrat: null,
  salaireNet: 0, ancienneteInitiale: 0
})

// Wizard navigation methods
const goToStep = (step: number) => {
  if (step >= 1 && step <= 3) {
    currentStep.value = step
  }
}

const nextStep = () => {
  if (currentStep.value < 3) {
    currentStep.value++
  }
}

const previousStep = () => {
  if (currentStep.value > 1) {
    currentStep.value--
  }
}

// Photo upload methods
const handlePhotoUpload = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (file) {
    const reader = new FileReader()
    reader.onload = (e) => {
      wizardForm.photo = e.target?.result as string
    }
    reader.readAsDataURL(file)
  }
}

const removePhoto = () => {
  wizardForm.photo = ''
}

// Fonction utilitaire pour formater les dates correctement pour l'API
const formatDateForAPI = (date: Date | string | null | undefined): string | undefined => {
  if (!date) {
    return undefined
  }
  
  if (typeof date === 'string') {
    // Si c'est déjà une chaîne, vérifier si elle est au bon format
    if (date.includes('/')) {
      return date  // Format dd/MM/yyyy déjà correct
    }
    // Convertir YYYY-MM-DD vers dd/MM/yyyy
    const [year, month, day] = date.split('-')
    return `${day}/${month}/${year}`
  }
  
  // Si c'est un objet Date, le convertir en dd/MM/yyyy
  const day = String(date.getDate()).padStart(2, '0')
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const year = date.getFullYear()
  return `${day}/${month}/${year}`
}

// Charger toutes les références depuis l'API
const loadAllReferences = async () => {
  try {
    const refs = await referencesService.loadAllReferences()
    
    // Stocker toutes les références
    banques.value = refs.banques
    fonctions.value = refs.fonctions
    typeContrats.value = refs.typeContrats
    categories.value = refs.categories
    typeServices.value = refs.typeServices
    
    // Charger les nationalités séparément
    await loadNationalites()
    
    console.log('Références chargées:', {
      banques: refs.banques.length,
      fonctions: refs.fonctions.length,
      typeContrats: refs.typeContrats.length,
      categories: refs.categories.length,
      nationalites: nationalites.value.length,
      typeServices: refs.typeServices.length
    })
  } catch (error) {
    console.error('Erreur chargement références:', error)
  }
}

// Charger les services selon le type de service sélectionné
const loadServicesByType = async (typeServiceId: number) => {
  try {
    if (!typeServiceId) {
      services.value = []
      return
    }
    
    const result = await referencesService.getServicesByType(typeServiceId)
    if (result.success) {
      services.value = result.data
      console.log('Services chargés pour type', typeServiceId, ':', result.data)
    }
  } catch (error) {
    console.error('Erreur chargement services:', error)
    services.value = []
  }
}

// Charger les nationalités depuis l'API
const loadNationalites = async () => {
  try {
    const result = await nationnaliteService.getAllNationalites()
    if (result.success) {
      nationalites.value = result.data
    } else {
      console.error('Erreur chargement nationalités:', result.message)
    }
  } catch (error) {
    console.error('Erreur chargement nationalités:', error)
  }
}

// Fonctions pour convertir les codes en IDs numériques
const getNationaliteId = (code: string): number => {
  console.log('Conversion nationalité:', code, 'disponibles:', nationalites.value)
  const id = nationnaliteService.getNationaliteIdByCode(nationalites.value, code)
  console.log('ID nationalité:', id)
  return id
}

const getBanqueId = (code: string): number => {
  console.log('Conversion banque:', code, 'disponibles:', banques.value)
  const id = referencesService.getBanqueIdByCode(banques.value, code)
  console.log('ID banque:', id)
  return id
}

const getFonctionId = (code: string): number => {
  console.log('Conversion fonction:', code, 'disponibles:', fonctions.value)
  const id = referencesService.getFonctionIdByCode(fonctions.value, code)
  console.log('ID fonction:', id)
  return id
}

const getTypeContratId = (code: string): number => {
  console.log('Conversion type contrat:', code, 'disponibles:', typeContrats.value)
  const id = referencesService.getTypeContratIdByCode(typeContrats.value, code)
  console.log('ID type contrat:', id)
  return id
}

const getServiceId = (code: string): number => {
  console.log('Conversion service:', code, 'disponibles:', services.value)
  const id = referencesService.getServiceIdByCode(services.value, code)
  console.log('ID service:', id)
  return id
}

// Watcher pour charger les services quand le type de service change
watch(() => wizardForm.division, async (newTypeServiceId) => {
  console.log('Type de service changé:', newTypeServiceId)
  if (newTypeServiceId) {
    await loadServicesByType(parseInt(newTypeServiceId))
    // Vider le service sélectionné quand le type change (seulement en mode création)
    if (!isEditing.value) {
      wizardForm.libelleDivision = ''
    }
  }
})

// Fonction pour gérer le changement de division
const onDivisionChange = (typeServiceId: string) => {
  console.log('Division changée:', typeServiceId)
  loadServicesByType(parseInt(typeServiceId))
  // Vider le service sélectionné quand le type change (seulement en mode création)
  if (!isEditing.value) {
    wizardForm.libelleDivision = ''
  }
}

// Validation des dates de contrat selon le type
const validateContractDates = () => {
  const { typeContrat, dateDebutContrat, dateFinContrat } = wizardForm
  
  if (!dateDebutContrat) return
  
  // Pour CDI: vider la date de fin
  if (typeContrat === 'CDI') {
    wizardForm.dateFinContrat = null
    return
  }
  
  // Pour CDD: valider que la date fin n'excède pas 2 ans
  if (typeContrat === 'CDD' && dateFinContrat) {
    const dateDebut = new Date(dateDebutContrat)
    const dateFin = new Date(dateFinContrat)
    const twoYearsLater = new Date(dateDebut)
    twoYearsLater.setFullYear(twoYearsLater.getFullYear() + 2)
    
    if (dateFin > twoYearsLater) {
      ElMessage.error('Pour un CDD, la date de fin ne peut pas excéder 2 ans après la date de début')
      wizardForm.dateFinContrat = twoYearsLater.toISOString().split('T')[0] // Format YYYY-MM-DD
    }
  }
}

// Watcher pour le type de contrat
watch(() => wizardForm.typeContrat, () => {
  validateContractDates()
})

// Fonction pour vérifier manuellement le matricule
const checkMatriculeManually = async () => {
  if (!wizardForm.matricule) {
    matriculeError.value = ''
    matriculeVerified.value = false
    return
  }
  
  try {
    const exists = await personnelRestService.checkMatriculeExists(wizardForm.matricule)
    if (exists) {
      matriculeError.value = `⚠️ Ce matricule existe déjà ! Choisissez-en un autre.`
      matriculeVerified.value = false
      ElMessage({
        message: `❌ MATRICULE EXISTANT : "${wizardForm.matricule}"\n\n🔄 Veuillez choisir un autre matricule pour éviter la redondance !`,
        type: 'error',
        duration: 6000,
        showClose: true
      })
    } else {
      matriculeError.value = ''
      matriculeVerified.value = true
      ElMessage({
        message: `🎉 EXCELLENT ! Le matricule "${wizardForm.matricule}" est unique et disponible pour la création !`,
        type: 'success',
        duration: 3000,
        showClose: true
      })
    }
  } catch (error) {
    console.error('Erreur lors de la vérification du matricule:', error)
    matriculeVerified.value = false
    ElMessage.error('Erreur lors de la vérification du matricule')
  }
}

// Watcher pour la validation en temps réel du matricule (seulement pour la création)
watch(() => wizardForm.matricule, async (newMatricule) => {
  if (!isEditing.value && newMatricule && newMatricule.length >= 3) {
    try {
      const exists = await personnelRestService.checkMatriculeExists(newMatricule)
      if (exists) {
        matriculeError.value = `⚠️ Ce matricule existe déjà ! Choisissez-en un autre.`
        ElMessage({
          message: `⚠️ Le matricule "${newMatricule}" existe déjà ! Veuillez saisir un autre matricule pour éviter la redondance.`,
          type: 'warning',
          duration: 5000,
          showClose: true
        })
      } else {
        matriculeError.value = ''
      }
    } catch (error) {
      console.error('Erreur lors de la vérification du matricule:', error)
      matriculeError.value = ''
    }
  } else {
    matriculeError.value = ''
  }
})

const getCategorieId = (code: string): number => {
  console.log('Conversion catégorie:', code, 'disponibles:', categories.value)
  const id = referencesService.getCategorieIdByCode(categories.value, code)
  console.log('ID catégorie:', id)
  return id
}

const finishWizard = async () => {
  // Validation basique
  if (!wizardForm.matricule || !wizardForm.nom || !wizardForm.prenom) {
    ElMessage.error('Veuillez remplir les champs obligatoires')
    return
  }

  // Validation des références chargées
  if (nationalites.value.length === 0) {
    ElMessage.error('Les nationalités ne sont pas encore chargées. Veuillez réessayer.')
    return
  }

  // Validation spécifique au type de contrat
  if (!wizardForm.typeContrat) {
    ElMessage.error('Veuillez sélectionner un type de contrat')
    return
  }

  if (!wizardForm.dateDebutContrat) {
    ElMessage.error('Veuillez saisir la date de début du contrat')
    return
  }

  // Validation pour CDD
  if (wizardForm.typeContrat === 'CDD') {
    if (!wizardForm.dateFinContrat) {
      ElMessage.error('Pour un CDD, la date de fin du contrat est obligatoire')
      return
    }
    
    // Vérifier que la durée ne dépasse pas 2 ans
    const dateDebut = new Date(wizardForm.dateDebutContrat)
    const dateFin = new Date(wizardForm.dateFinContrat)
    const twoYearsLater = new Date(dateDebut)
    twoYearsLater.setFullYear(twoYearsLater.getFullYear() + 2)
    
    if (dateFin > twoYearsLater) {
      ElMessage.error('Pour un CDD, la durée ne peut pas excéder 2 ans')
      return
    }
  }

  // Validation pour CDI (date fin doit être nulle)
  if (wizardForm.typeContrat === 'CDI' && wizardForm.dateFinContrat) {
    ElMessage.error('Pour un CDI, la date de fin du contrat ne doit pas être saisie')
    return
  }

  // Validation d'unicité du matricule (seulement pour la création)
  if (!isEditing.value) {
    try {
      const matriculeExists = await personnelRestService.checkMatriculeExists(wizardForm.matricule)
      if (matriculeExists) {
        ElMessage({
          message: `❌ IMPOSSIBLE DE CRÉER : Le matricule "${wizardForm.matricule}" existe déjà !\n\n🔄 Veuillez choisir un autre matricule unique pour éviter la redondance des données.`,
          type: 'error',
          duration: 8000,
          showClose: true
        })
        return
      }
    } catch (error) {
      console.error('Erreur lors de la vérification du matricule:', error)
      ElMessage.error('Erreur lors de la vérification du matricule. Veuillez réessayer.')
      return
    }
  }

  try {
    loading.value = true
    
    console.log('Données du formulaire avant conversion:', wizardForm)
    console.log('Références disponibles:', {
      nationalites: nationalites.value.length,
      fonctions: fonctions.value.length,
      typeContrats: typeContrats.value.length,
      categories: categories.value.length
    })
    
    let personnelId: number
    
    if (isEditing.value) {
      // Mode mise à jour
      personnelId = parseInt(route.query.id as string)
      const personnelData = {
        id: personnelId,
        matricule: wizardForm.matricule,
        nom: wizardForm.nom,
        prenom: wizardForm.prenom,
        email: wizardForm.email,
        telephone: wizardForm.telephone,
        dateNaissance: formatDateForAPI(wizardForm.dateNaissance),
        lieuNaissance: wizardForm.lieuNaissance,
        sexe: wizardForm.sexe,
        nationalite: getNationaliteId(wizardForm.nationalite),
        fonction: getFonctionId(wizardForm.emploi),
        typeContrat: getTypeContratId(wizardForm.typeContrat),
        categorie: getCategorieId(wizardForm.categorie),
        situationMatrimoniale: parseInt(wizardForm.situationMatrimoniale), // Convertir string → number
        nombreEnfant: wizardForm.nombreEnfants, // nombreEnfants → nombreEnfant
        dateArrivee: formatDateForAPI(wizardForm.dateArrivee),
        dateRetourcg: formatDateForAPI(wizardForm.dateRetourConge), // dateRetourConge → dateRetourcg
        idBanque: getBanqueId(wizardForm.banque), // banque → idBanque
        numeroCNPS: wizardForm.numeroCNPS || '',
        residence: wizardForm.residence || '',
        dateDebut: formatDateForAPI(wizardForm.dateDebutContrat), // Utiliser la vraie valeur
        dateFin: formatDateForAPI(wizardForm.dateFinContrat),   // Utiliser la vraie valeur
        salaireNet: wizardForm.salaireNet, // Utiliser la vraie valeur
        indemnitelogement: undefined, // undefined au lieu de null
        modePaiement: wizardForm.modePaiement || 'VIREMENT_BANCAIRE',
        numeroCompte: wizardForm.numeroCompte || '',
        numeroGuichet: wizardForm.numeroGuichet || '',
        rib: wizardForm.rib || '',
        ancienneteInitial: wizardForm.ancienneteInitiale, // Utiliser la vraie valeur
        carec: wizardForm.contractuel, // Utiliser la vraie valeur
        typeEmp: wizardForm.typeEmploye, // Utiliser la vraie valeur
        service: getServiceId(wizardForm.libelleDivision), // Ajouter le service
        situationMedaille: parseInt(wizardForm.medaille), // Convertir string → number
        situationEmploie: parseInt(wizardForm.qualiteEmploi), // Convertir string → number
        indemniteRespons: undefined, // undefined au lieu de null
        indemniteRepresent: undefined, // undefined au lieu de null
        indemniteTransport: undefined, // undefined au lieu de null
        sursalaire: undefined // undefined au lieu de null
      }
      
      console.log('Données envoyées pour mise à jour:', personnelData)
      await personnelRestService.savePersonnel(personnelData)
      ElMessage.success('Personnel mis à jour avec succès!')
    } else {
      // Mode création
      const personnelData = {
        matricule: wizardForm.matricule,
        nom: wizardForm.nom,
        prenom: wizardForm.prenom,
        email: wizardForm.email,
        telephone: wizardForm.telephone,
        dateNaissance: formatDateForAPI(wizardForm.dateNaissance),
        lieuNaissance: wizardForm.lieuNaissance,
        sexe: wizardForm.sexe,
        nationalite: getNationaliteId(wizardForm.nationalite),
        fonction: getFonctionId(wizardForm.emploi),
        typeContrat: getTypeContratId(wizardForm.typeContrat),
        categorie: getCategorieId(wizardForm.categorie),
        situationMatrimoniale: parseInt(wizardForm.situationMatrimoniale), // Convertir string → number
        nombreEnfant: wizardForm.nombreEnfants, // nombreEnfants → nombreEnfant
        dateArrivee: formatDateForAPI(wizardForm.dateArrivee),
        dateRetourcg: formatDateForAPI(wizardForm.dateRetourConge), // dateRetourConge → dateRetourcg
        idBanque: getBanqueId(wizardForm.banque), // banque → idBanque
        numeroCNPS: wizardForm.numeroCNPS || '',
        residence: wizardForm.residence || '',
        dateDebut: formatDateForAPI(wizardForm.dateDebutContrat), // Utiliser la vraie valeur
        dateFin: formatDateForAPI(wizardForm.dateFinContrat),   // Utiliser la vraie valeur
        salaireNet: wizardForm.salaireNet, // Utiliser la vraie valeur
        indemnitelogement: undefined, // undefined au lieu de null
        modePaiement: wizardForm.modePaiement || 'VIREMENT_BANCAIRE',
        numeroCompte: wizardForm.numeroCompte || '',
        numeroGuichet: wizardForm.numeroGuichet || '',
        rib: wizardForm.rib || '',
        ancienneteInitial: wizardForm.ancienneteInitiale, // Utiliser la vraie valeur
        carec: wizardForm.contractuel, // Utiliser la vraie valeur
        typeEmp: wizardForm.typeEmploye, // Utiliser la vraie valeur
        service: getServiceId(wizardForm.libelleDivision), // Ajouter le service
        situationMedaille: parseInt(wizardForm.medaille), // Convertir string → number
        situationEmploie: parseInt(wizardForm.qualiteEmploi), // Convertir string → number
        indemniteRespons: undefined, // undefined au lieu de null
        indemniteRepresent: undefined, // undefined au lieu de null
        indemniteTransport: undefined, // undefined au lieu de null
        sursalaire: undefined // undefined au lieu de null
      }
      
      console.log('Données envoyées pour création:', personnelData)
      const result = await personnelRestService.createPersonnel(personnelData)
      ElMessage.success('Personnel ajouté avec succès!')
    }
    
    router.push('/personnel')
  } catch (error) {
    console.error('Erreur lors de la sauvegarde:', error)
    ElMessage.error('Erreur lors de la sauvegarde du personnel')
  } finally {
    loading.value = false
  }
}

// Fonction pour convertir les dates du format dd/MM/yyyy au format ISO pour les inputs
const convertDateForInput = (dateStr: string): string => {
  if (!dateStr) return ''
  
  // Si la date est déjà au format ISO ou yyyy-MM-dd, la retourner directement
  if (dateStr.includes('-') && dateStr.split('-')[0].length === 4) {
    return dateStr.split(' ')[0] // Enlever l'heure si présente
  }
  
  // Convertir dd/MM/yyyy ou dd-MM-yyyy vers yyyy-MM-dd
  const parts = dateStr.includes('/') ? dateStr.split('/') : dateStr.split('-')
  if (parts.length === 3) {
    const [day, month, year] = parts
    return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
  }
  
  return dateStr // Retourner la date originale si non reconnue
}

// Fonction pour charger les contrats d'un personnel
const loadContrats = async () => {
  if (!wizardForm.id) return // Pas d'ID = pas de chargement de contrats
  
  try {
    const response = await personnelRestService.getContratsByPersonnel(wizardForm.id)
    console.log('Réponse contrats API:', response) // Debug
    
    // Vérifier si les données existent même si status est false
    if ((response.success || response.data) && response.data && response.data.length > 0) {
      contrats.value = response.data
      console.log('Contrats chargés:', contrats.value)
      
      // Prendre le premier contrat (le plus récent) avec statut true
      const dernierContrat = contrats.value.find(c => c.statut === true) || contrats.value[0]
      if (dernierContrat) {
        console.log('Dernier contrat trouvé:', dernierContrat)
        
        // Pré-remplir les champs de contrat dans le formulaire avec les bons noms de champs
        Object.assign(wizardForm, {
          typeContrat: dernierContrat.typeContrat?.libelle || '',
          categorie: dernierContrat.categorie?.libelle || 'A',
          salaireNet: dernierContrat.netAPayer || 0,
          dateDebutContrat: dernierContrat.dDebut ? convertDateForInput(dernierContrat.dDebut) : null,
          dateFinContrat: dernierContrat.dFin ? convertDateForInput(dernierContrat.dFin) : null,
          indemniteLogement: dernierContrat.indemniteLogement || 0,
          indemniteTransport: dernierContrat.indemniteTransport || 0,
          sursalaire: dernierContrat.sursalaire || 0,
          emploi: dernierContrat.fonction?.libelle || '',
          ancienneteInitiale: dernierContrat.ancienneteInitial || 0
        })
        
        console.log('Formulaire contrat après mapping:', {
          typeContrat: wizardForm.typeContrat,
          categorie: wizardForm.categorie,
          salaireNet: wizardForm.salaireNet,
          dateDebutContrat: wizardForm.dateDebutContrat,
          emploi: wizardForm.emploi
        })
      }
    } else {
      console.log('Aucun contrat trouvé')
    }
  } catch (error) {
    console.error('Erreur lors du chargement des contrats:', error)
  }
}

const loadPersonnel = async () => {
  const personnelId = route.query.id as string
  
  if (!personnelId) {
    // Mode création
    isEditing.value = false
    return
  }

  try {
    loading.value = true
    isEditing.value = true
    
    const response = await personnelRestService.getPersonnelById(parseInt(personnelId))
    
    if (response.success && response.data) {
      // Mapper les données de l'API vers le formulaire
      const personnel = response.data as any // Cast en any pour accéder aux champs réels
      console.log('Données personnel chargées:', personnel) // Debug
      
      Object.assign(wizardForm, {
        id: personnel.id,
        matricule: personnel.matricule || '',
        nom: personnel.nom || '',
        prenom: personnel.prenom || '',
        email: personnel.email || '',
        telephone: personnel.telephone || '',
        dateNaissance: personnel.dNaissance ? convertDateForInput(personnel.dNaissance) : (personnel.dateNaissance ? convertDateForInput(personnel.dateNaissance.split(' ')[0]) : ''),
        lieuNaissance: personnel.lieuNaissance || '',
        sexe: personnel.sexe === 'Masculin' ? 'Masculin' : 'Féminin',
        nationalite: personnel.nationnalite?.libelle || 'IVOIRIENNE',
        situationMatrimoniale: personnel.situationMatri === 'CELIBATAIRE' ? 'CELIBATAIRE' : 'MARIE',
        nombreEnfants: personnel.nombrEnfant || 0,
        // Champs supplémentaires
        numeroCNPS: personnel.numeroCnps || '',
        residence: personnel.residence || '',
        dateArrivee: personnel.dArrivee ? convertDateForInput(personnel.dArrivee) : null,
        dateRetourConge: personnel.dRetourconge ? convertDateForInput(personnel.dRetourconge) : null,
        modePaiement: personnel.modePaiement === 'virement-bancaire' ? 'VIREMENT_BANCAIRE' : 'ESPECE',
        banque: personnel.banquek?.libelle || '',
        numeroGuichet: personnel.banquek?.numguich || '',
        numeroCompte: personnel.numeroCompte || '',
        rib: personnel.rib || '',
        contractuel: personnel.carec || false,
        division: personnel.service?.typeService?.id?.toString() || '',
        libelleDivision: personnel.service?.libelle || '',
        emploi: personnel.fonction?.libelle || '',
        service: personnel.service?.libelle || ''
      })
      
      console.log('Formulaire après mapping:', wizardForm) // Debug
      
      // Charger les services pour la division assignée
      if (wizardForm.division) {
        await loadServicesByType(parseInt(wizardForm.division))
      }
      
      // Charger les contrats de l'employé (après avoir l'ID)
      if (wizardForm.id) {
        await loadContrats()
      }
    } else {
      ElMessage.error('Personnel non trouvé')
      router.push('/personnel')
    }
  } catch (error) {
    console.error('Erreur lors du chargement du personnel:', error)
    ElMessage.error('Erreur lors du chargement du personnel')
    router.push('/personnel')
  } finally {
    loading.value = false
  }
}

// Charger les données au montage du composant
onMounted(async () => {
  await loadAllReferences()
  loadPersonnel()
})

const goBack = () => {
  router.push('/personnel')
}
</script>

<style scoped>
.personnel-wizard-view {
  min-height: 100vh;
  background: #f5f7fa;
}

.wizard-header {
  background: white;
  padding: 24px 0;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  width: 100%;
  margin: 0;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left h1 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 28px;
  font-weight: 600;
}

.header-left p {
  margin: 0;
  color: #909399;
  font-size: 16px;
}

/* Layout vertical */
.wizard-layout {
  width: 100%;
  margin: 20px 0;
  padding: 0 20px;
  display: flex;
  gap: 30px;
}

.wizard-sidebar {
  width: 280px;
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  height: fit-content;
  position: sticky;
  top: 20px;
}

.steps-vertical {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.step-item {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 15px;
  border-radius: 8px;
}

.step-item:hover {
  background: #f8f9fa;
}

.step-item.active {
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1) 0%, rgba(102, 126, 234, 0.1) 100%);
  border-left: 4px solid #409eff;
}

.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #e4e7ed;
  color: #909399;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  transition: all 0.3s ease;
  font-size: 16px;
  flex-shrink: 0;
}

.step-item.active .step-number {
  background: linear-gradient(135deg, #409eff 0%, #667eea 100%);
  color: white;
  transform: scale(1.1);
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.4);
}

.step-item.completed .step-number {
  background: #67c23a;
  color: white;
}

.step-content {
  flex: 1;
}

.step-title {
  font-size: 16px;
  color: #303133;
  font-weight: 600;
  margin-bottom: 4px;
}

.step-item.active .step-title {
  color: #409eff;
}

.step-item.completed .step-title {
  color: #67c23a;
}

.step-desc {
  font-size: 13px;
  color: #909399;
  line-height: 1.3;
}

.step-line {
  width: 2px;
  height: 30px;
  background: #e4e7ed;
  margin-left: 20px;
  position: relative;
}

.step-line.completed {
  background: #67c23a;
}

.wizard-main {
  flex: 1;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.wizard-card {
  border: none;
  box-shadow: none;
  border-radius: 0;
}

.step-content h4 {
  margin: 0 0 30px 0;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
  padding-bottom: 15px;
  border-bottom: 2px solid #e4e7ed;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 24px;
}

.photo-section {
  margin-bottom: 30px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 2px dashed #e4e7ed;
}

.photo-upload {
  display: flex;
  align-items: center;
  gap: 20px;
}

.photo-preview {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 3px solid #e4e7ed;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  transition: all 0.3s ease;
}

.photo-preview.has-photo {
  border-color: #409eff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.photo-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.photo-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  text-align: center;
  padding: 10px;
}

.photo-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.photo-placeholder span {
  font-size: 12px;
  font-weight: 500;
}

.photo-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.wizard-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px 0 0 0;
  border-top: 1px solid #e4e7ed;
  margin-top: 40px;
}

.step-indicator {
  font-size: 16px;
  color: #606266;
  font-weight: 500;
  background: #f8f9fa;
  padding: 12px 24px;
  border-radius: 25px;
  border: 1px solid #e4e7ed;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #303133;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}

/* Styles pour l'erreur du matricule */
.error-input :deep(.el-input__wrapper) {
  border-color: #f56c6c !important;
  box-shadow: 0 0 0 1px #f56c6c inset !important;
}

.success-input :deep(.el-input__wrapper) {
  border-color: #67c23a !important;
  box-shadow: 0 0 0 1px #67c23a inset !important;
}

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
  line-height: 1.2;
  font-weight: 500;
}

.success-message {
  color: #67c23a;
  font-size: 12px;
  margin-top: 4px;
  line-height: 1.2;
  font-weight: 500;
}

.verified-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: #67c23a;
  border-radius: 50%;
  font-size: 16px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

:deep(.el-form-item.is-error .el-form-item__label) {
  color: #f56c6c !important;
}

:deep(.el-date-editor.el-input__wrapper) {
  border-radius: 8px;
}

:deep(.el-button) {
  border-radius: 8px;
  font-weight: 500;
}
</style>
