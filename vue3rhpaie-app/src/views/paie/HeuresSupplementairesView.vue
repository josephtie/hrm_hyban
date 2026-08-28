<template>
  <div class="heures-supp-view">
    <div class="page-header">
      <h1>Heures Supplémentaires</h1>
      <p>Gestion des heures supplémentaires et intégration à la paie</p>
    </div>

    <div class="main-content">
      <!-- Filtres -->
      <div class="filters-bar">
        <el-row :gutter="12">
          <el-col :span="6">
            <el-select v-model="filters.employeId" placeholder="Employé" clearable filterable style="width: 100%">
              <el-option
                v-for="p in personnels"
                :key="p.id"
                :label="p.nomComplet || `${p.matricule || ''} - ${p.nom || ''} ${p.prenom || ''}`.trim()"
                :value="p.id"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filters.periodePaieId" placeholder="Période de paie" clearable filterable style="width: 100%">
              <el-option
                v-for="periode in periodes"
                :key="periode.id"
                :label="periode.affiche || periode.libelle || `Période ${periode.id}`"
                :value="periode.id"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="filters.statut" placeholder="Statut" clearable style="width: 100%">
              <el-option label="Brouillon" value="BROUILLON" />
              <el-option label="À valider" value="A_VALIDER" />
              <el-option label="Rejeté" value="REJETE" />
              <el-option label="Validé" value="VALIDE" />
              <el-option label="Intégré paie" value="INTEGRE_PAIE" />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-date-picker v-model="filters.dateDebut" type="date" placeholder="Date début" format="DD/MM/YYYY" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-col>
          <el-col :span="4">
            <el-date-picker v-model="filters.dateFin" type="date" placeholder="Date fin" format="DD/MM/YYYY" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-col>
        </el-row>
        <div style="margin-top: 10px; display: flex; gap: 10px;">
          <el-button type="primary" @click="loadHeuresSupp">Filtrer</el-button>
          <el-button @click="resetFilters">Réinitialiser</el-button>
          <el-button type="success" @click="openCreateForm" v-permission="'HS_CREATE'">
            <el-icon><Plus /></el-icon>&nbsp; Nouvelle saisie
          </el-button>
          <el-button type="warning" @click="bulkSoumettre" :disabled="!hasBrouillons" v-permission="'HS_CREATE'">
            <el-icon><Promotion /></el-icon>&nbsp; Tout soumettre
          </el-button>
          <el-button type="info" @click="bulkValider" :disabled="!hasAValider" v-permission="'HS_VALIDATE'">
            <el-icon><Check /></el-icon>&nbsp; Tout valider
          </el-button>
        </div>
      </div>

      <!-- Tableau -->
      <el-table :data="heuresSupp" v-loading="loading" style="width: 100%; margin-top: 15px;" stripe>
        <el-table-column label="Matricule" width="120" sortable>
          <template #default="{ row }">
            {{ row.personnel?.matricule }}
          </template>
        </el-table-column>
        <el-table-column label="Employé" min-width="180" sortable>
          <template #default="{ row }">
            {{ row.personnel?.nom }} {{ row.personnel?.prenom }}
          </template>
        </el-table-column>
        <el-table-column label="Période" width="160" sortable>
          <template #default="{ row }">
            {{ row.periodePaie?.affiche || (row.periodePaie?.mois ? row.periodePaie.mois + ' ' + (row.periodePaie?.annee || '') : '—') }}
          </template>
        </el-table-column>
        <el-table-column label="Date" width="110" sortable>
          <template #default="{ row }">
            {{ formatDate(row.dateTravail) }}
          </template>
        </el-table-column>
        <el-table-column label="Heures" width="90" align="center">
          <template #default="{ row }">
            {{ row.nombreHeures }}h
          </template>
        </el-table-column>
        <el-table-column label="Type" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.typeHS)" size="small">{{ getTypeLabel(row.typeHS) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Taux hor." width="110" align="right">
          <template #default="{ row }">
            {{ formatMontant(row.tauxHoraire) }}
          </template>
        </el-table-column>
        <el-table-column label="Coefficient" width="100" align="center">
          <template #default="{ row }">
            {{ row.coefficient }}
          </template>
        </el-table-column>
        <el-table-column label="Montant" width="130" align="right">
          <template #default="{ row }">
            <strong>{{ formatMontant(row.montant) }}</strong>
          </template>
        </el-table-column>
        <el-table-column label="Statut" width="130" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatutTagType(row.statut)" size="default">{{ getStatutLabel(row.statut) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" min-width="180" fixed="right" align="center">
          <template #default="{ row }">
            <div class="action-buttons">
              <!-- BROUILLON -->
              <el-button v-if="row.statut === 'BROUILLON'" size="small" type="primary" circle @click="openEditForm(row)" title="Modifier">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button v-if="row.statut === 'BROUILLON'" size="small" type="success" circle @click="soumettre(row)" title="Soumettre">
                <el-icon><Promotion /></el-icon>
              </el-button>
              <el-button v-if="row.statut === 'BROUILLON'" size="small" type="danger" circle @click="supprimer(row)" title="Supprimer">
                <el-icon><Delete /></el-icon>
              </el-button>

              <!-- A_VALIDER -->
              <el-button v-if="row.statut === 'A_VALIDER'" size="small" type="success" circle @click="valider(row)" title="Valider">
                <el-icon><Check /></el-icon>
              </el-button>
              <el-button v-if="row.statut === 'A_VALIDER'" size="small" type="danger" circle @click="openRejetModal(row)" title="Rejeter">
                <el-icon><CloseBold /></el-icon>
              </el-button>

              <!-- REJETE -->
              <el-button v-if="row.statut === 'REJETE'" size="small" type="warning" circle @click="openEditForm(row)" title="Modifier">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button v-if="row.statut === 'REJETE'" size="small" type="success" circle @click="soumettre(row)" title="Resoumettre">
                <el-icon><Promotion /></el-icon>
              </el-button>

              <!-- VALIDE -->
              <el-button v-if="row.statut === 'VALIDE'" size="small" type="primary" circle @click="integrerPaie(row)" title="Intégrer à la paie">
                <el-icon><Connection /></el-icon>
              </el-button>

              <!-- INTEGRE_PAIE -->
              <el-button v-if="row.statut === 'INTEGRE_PAIE'" size="small" type="info" circle @click="voirDetail(row)" title="Consulter">
                <el-icon><View /></el-icon>
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Dialog: Formulaire de saisie -->
    <el-dialog v-model="showForm" :title="isEditing ? 'Modifier l&rsquo;heure supplémentaire' : 'Nouvelle heure supplémentaire'" width="600px" @close="resetForm">
      <el-form :model="form" label-width="140px">
        <el-form-item label="Employé" required>
          <el-select v-model="form.personnelId" placeholder="Sélectionner un employé" filterable style="width: 100%" @change="onPersonnelChange">
            <el-option
              v-for="p in personnels"
              :key="p.id"
              :label="p.nomComplet || `${p.matricule || ''} - ${p.nom || ''} ${p.prenom || ''}`.trim()"
              :value="p.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Date de travail" required>
          <el-date-picker v-model="form.dateTravail" type="date" placeholder="Date" format="DD/MM/YYYY" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="Période de paie" required>
          <el-select v-model="form.periodePaieId" placeholder="Sélectionner une période" filterable style="width: 100%">
            <el-option
              v-for="periode in periodes"
              :key="periode.id"
              :label="periode.affiche || periode.libelle || `Période ${periode.id}`"
              :value="periode.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Salaire de base">
          <el-tag type="info" size="large">{{ formatMontant(salaireDeBase) }} FCFA</el-tag>
          <span style="font-size: 12px; color: #909399; margin-left: 8px;">Base mensuelle : 173,33 h</span>
        </el-form-item>
        <el-form-item label="Nombre d'heures" required>
          <el-input-number v-model="form.nombreHeures" :min="0.5" :step="0.5" :precision="2" style="width: 100%" @change="calculerMontant" />
        </el-form-item>
        <el-form-item label="Type HS" required>
          <el-select v-model="form.typeHS" placeholder="Sélectionner le type" style="width: 100%" @change="onTypeChange">
            <el-option label="HS 15%" value="HS_15" />
            <el-option label="HS 50%" value="HS_50" />
            <el-option label="HS 75%" value="HS_75" />
            <el-option label="HS 100%" value="HS_100" />
          </el-select>
        </el-form-item>
        <el-form-item label="Taux horaire">
          <div style="display: flex; align-items: center; gap: 8px;">
            <el-tag type="info" size="large">{{ form.tauxHoraire }} FCFA/h</el-tag>
            <span style="font-size: 12px; color: #909399;">{{ formatMontant(salaireDeBase) }} ÷ 173,33</span>
          </div>
        </el-form-item>
        <el-form-item label="Taux de majoration">
          <el-tag type="info">{{ form.tauxMajoration }}%</el-tag>
        </el-form-item>
        <el-form-item label="Coefficient">
          <el-tag type="info">{{ form.coefficient }}</el-tag>
        </el-form-item>
        <el-form-item label="Montant estimé">
          <div style="display: flex; flex-direction: column; gap: 4px;">
            <el-tag type="success" size="large">{{ formatMontant(montantEstime) }} FCFA</el-tag>
            <span style="font-size: 12px; color: #909399;">{{ calculerMontantDetail }}</span>
          </div>
        </el-form-item>
        <el-form-item label="Motif">
          <el-input v-model="form.motif" placeholder="Motif de l'heure supplémentaire" />
        </el-form-item>
        <el-form-item label="Commentaire">
          <el-input v-model="form.commentaire" type="textarea" :rows="2" placeholder="Commentaire optionnel" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showForm = false">Annuler</el-button>
        <el-button type="primary" @click="saveForm" :loading="saving">{{ isEditing ? 'Modifier' : 'Créer' }}</el-button>
      </template>
    </el-dialog>

    <!-- Dialog: Rejet -->
    <el-dialog v-model="showRejetModal" title="Rejeter l'heure supplémentaire" width="500px">
      <el-form>
        <el-form-item label="Motif du rejet" required>
          <el-input v-model="motifRejet" type="textarea" :rows="3" placeholder="Expliquer la raison du rejet" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRejetModal = false">Annuler</el-button>
        <el-button type="danger" @click="confirmerRejet" :loading="saving">Rejeter</el-button>
      </template>
    </el-dialog>

    <!-- Dialog: Détail -->
    <el-dialog v-model="showDetail" title="Détail de l'heure supplémentaire" width="600px">
      <el-descriptions v-if="selectedItem" :column="2" border>
        <el-descriptions-item label="Employé">{{ selectedItem.personnel?.nom }} {{ selectedItem.personnel?.prenom }}</el-descriptions-item>
        <el-descriptions-item label="Matricule">{{ selectedItem.personnel?.matricule }}</el-descriptions-item>
        <el-descriptions-item label="Date">{{ formatDate(selectedItem.dateTravail) }}</el-descriptions-item>
        <el-descriptions-item label="Heures">{{ selectedItem.nombreHeures }}h</el-descriptions-item>
        <el-descriptions-item label="Type">{{ getTypeLabel(selectedItem.typeHS) }}</el-descriptions-item>
        <el-descriptions-item label="Taux horaire">{{ formatMontant(selectedItem.tauxHoraire) }}</el-descriptions-item>
        <el-descriptions-item label="Coefficient">{{ selectedItem.coefficient }}</el-descriptions-item>
        <el-descriptions-item label="Montant">{{ formatMontant(selectedItem.montant) }} FCFA</el-descriptions-item>
        <el-descriptions-item label="Statut">{{ getStatutLabel(selectedItem.statut) }}</el-descriptions-item>
        <el-descriptions-item label="Motif">{{ selectedItem.motif || '&#8212;' }}</el-descriptions-item>
        <el-descriptions-item label="Commentaire" :span="2">{{ selectedItem.commentaire || '&#8212;' }}</el-descriptions-item>
        <el-descriptions-item v-if="selectedItem.motifRejet" label="Motif du rejet" :span="2">
          <el-tag type="danger">{{ selectedItem.motifRejet }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Date soumission">{{ formatDate(selectedItem.dateSoumission) }}</el-descriptions-item>
        <el-descriptions-item label="Date validation">{{ formatDate(selectedItem.dateValidation) }}</el-descriptions-item>
        <el-descriptions-item label="Date intégration">{{ formatDate(selectedItem.dateIntegrationPaie) }}</el-descriptions-item>
        <el-descriptions-item label="Validé par">{{ selectedItem.validatedBy || '&#8212;' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="showDetail = false">Fermer</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Check, CloseBold, Promotion, Connection, View } from '@element-plus/icons-vue'
import { api } from '@/services/api'
import { contratPersonnelService } from '@/services/contrat-personnel.service'
import { heureSupplementaireService, type HeureSupplementaire, type RegleHeureSupplementaire } from '@/services/heure-supplementaire.service'

const loading = ref(false)
const saving = ref(false)
const heuresSupp = ref<HeureSupplementaire[]>([])
const personnels = ref<any[]>([])
const contratsMap = ref<Record<number, any>>({})
const periodes = ref<any[]>([])
const salaireDeBase = ref(0)
const BASE_MENSUELLE_HEURES = 173.33
const regles = ref<RegleHeureSupplementaire[]>([])

const showForm = ref(false)
const showRejetModal = ref(false)
const showDetail = ref(false)
const isEditing = ref(false)
const selectedItem = ref<HeureSupplementaire | null>(null)
const motifRejet = ref('')
const montantEstime = ref(0)

const filters = reactive({
  employeId: null as number | null,
  periodePaieId: null as number | null,
  statut: '' as string,
  dateDebut: '' as string,
  dateFin: '' as string,
})

const form = reactive({
  id: 0,
  personnelId: null as number | null,
  dateTravail: '',
  periodePaieId: null as number | null,
  nombreHeures: 0,
  typeHS: '' as string,
  tauxHoraire: 0,
  tauxMajoration: 0,
  coefficient: 0,
  motif: '',
  commentaire: '',
})

const loadHeuresSupp = async () => {
  loading.value = true
  try {
    const filterParams: any = {}
    if (filters.employeId) filterParams.employeId = filters.employeId
    if (filters.periodePaieId) filterParams.periodePaie = filters.periodePaieId
    if (filters.statut) filterParams.statut = filters.statut
    if (filters.dateDebut) filterParams.dateDebut = filters.dateDebut
    if (filters.dateFin) filterParams.dateFin = filters.dateFin
    heuresSupp.value = await heureSupplementaireService.getAll(filterParams)
  } catch (e: any) {
    ElMessage.error(e?.response?.data?.message || 'Erreur lors du chargement')
  } finally {
    loading.value = false
  }
}

const loadPersonnels = async () => {
  try {
    const result = await contratPersonnelService.getContratsActifs({ limit: 1000, offset: 0 })
    const rows = result.rows || result.data || []
    const contrats = rows.filter((c: any) => c.personnel && c.personnel.id)
    contratsMap.value = {}
    contrats.forEach((c: any) => {
      contratsMap.value[c.personnel.id] = c
    })
    personnels.value = contrats.map((c: any) => c.personnel)
  } catch (e) {
    console.error('Erreur chargement personnels', e)
  }
}

const activePeriodeId = ref<number | null>(null)

const loadPeriodes = async () => {
  try {
    const { data } = await api.get('/parametrages/periodes/list', { params: { limit: 1000, offset: 0, search: '' } })
    const rawRows = Array.isArray(data) ? data : Array.isArray(data?.rows) ? data.rows : []
    periodes.value = rawRows.flatMap((row: any) => Array.isArray(row) ? row : [row])

    // Charger la période active
    try {
      const { data: activeData } = await api.get('/parametrages/periodes/active')
      if (activeData && activeData.id) {
        activePeriodeId.value = Number(activeData.id)
        filters.periodePaieId = activePeriodeId.value
      }
    } catch (e) {
      console.error('Erreur chargement période active', e)
    }
  } catch (e) {
    console.error('Erreur chargement périodes', e)
  }
}

const loadRegles = async () => {
  try {
    regles.value = await heureSupplementaireService.getRegles()
  } catch (e) {
    console.error('Erreur chargement règles', e)
  }
}

const onPersonnelChange = () => {
  const contrat = contratsMap.value[form.personnelId as number]
  console.log('Contrat sélectionné:', contrat)
  if (contrat) {
    const cat = contrat.categorie || {}
    const salaire = Number(cat.salaireDeBase || cat.salaireBase || cat.salairebase || cat.salaire_de_base || 0)
    console.log('Categorie:', cat, 'Salaire de base:', salaire)
    salaireDeBase.value = salaire
    form.tauxHoraire = Number((salaire / BASE_MENSUELLE_HEURES).toFixed(2))
  } else {
    salaireDeBase.value = 0
    form.tauxHoraire = 0
  }
  calculerMontant()
}

const onTypeChange = () => {
  const regle = regles.value.find(r => r.typeHS === form.typeHS)
  if (regle) {
    form.tauxMajoration = regle.tauxMajoration
    form.coefficient = regle.coefficient
  }
  calculerMontant()
}

const calculerMontant = () => {
  if (form.nombreHeures && form.tauxHoraire && form.coefficient) {
    montantEstime.value = Number((form.nombreHeures * form.tauxHoraire * form.coefficient).toFixed(2))
  } else {
    montantEstime.value = 0
  }
}

const calculerMontantDetail = computed(() => {
  if (!form.nombreHeures || !form.tauxHoraire || !form.coefficient) return ''
  const detail = `${form.nombreHeures}h × ${form.tauxHoraire} × ${form.coefficient} = ${montantEstime.value} FCFA`
  return detail
})

const openCreateForm = () => {
  isEditing.value = false
  resetForm()
  // Pré-sélectionner la période active
  if (activePeriodeId.value) {
    form.periodePaieId = activePeriodeId.value
  }
  showForm.value = true
}

const openEditForm = (row: HeureSupplementaire) => {
  isEditing.value = true
  Object.assign(form, {
    id: row.id || 0,
    personnelId: row.personnel?.id || null,
    dateTravail: row.dateTravail || '',
    periodePaieId: row.periodePaie?.id || null,
    nombreHeures: row.nombreHeures || 0,
    typeHS: row.typeHS || '',
    tauxHoraire: row.tauxHoraire || 0,
    tauxMajoration: row.tauxMajoration || 0,
    coefficient: row.coefficient || 0,
    motif: row.motif || '',
    commentaire: row.commentaire || '',
  })
  calculerMontant()
  showForm.value = true
}

const resetForm = () => {
  Object.assign(form, {
    id: 0,
    personnelId: null,
    dateTravail: '',
    periodePaieId: null,
    nombreHeures: 0,
    typeHS: '',
    tauxHoraire: 0,
    tauxMajoration: 0,
    coefficient: 0,
    motif: '',
    commentaire: '',
  })
  salaireDeBase.value = 0
  montantEstime.value = 0
}

const saveForm = async () => {
  if (!form.personnelId || !form.dateTravail || !form.periodePaieId || !form.nombreHeures || !form.typeHS) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  saving.value = true
  try {
    const payload: any = {
      personnel: { id: form.personnelId },
      dateTravail: form.dateTravail,
      periodePaie: { id: form.periodePaieId },
      nombreHeures: form.nombreHeures,
      typeHS: form.typeHS,
      tauxHoraire: form.tauxHoraire,
      motif: form.motif,
      commentaire: form.commentaire,
    }

    if (isEditing.value && form.id) {
      await heureSupplementaireService.update(form.id, payload)
      ElMessage.success('Heure supplémentaire modifiée')
    } else {
      await heureSupplementaireService.create(payload)
      ElMessage.success('Heure supplémentaire créée')
    }
    showForm.value = false
    await loadHeuresSupp()
  } catch (e: any) {
    ElMessage.error(e?.response?.data?.message || "Erreur lors de l'enregistrement")
  } finally {
    saving.value = false
  }
}

const soumettre = async (row: HeureSupplementaire) => {
  try {
    await ElMessageBox.confirm('Confirmer la soumission de cette heure supplémentaire ?', 'Confirmation')
    await heureSupplementaireService.soumettre(row.id!)
    ElMessage.success('Soumise pour validation')
    await loadHeuresSupp()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error(e?.response?.data?.message || 'Erreur')
  }
}

const valider = async (row: HeureSupplementaire) => {
  try {
    await ElMessageBox.confirm('Confirmer la validation de cette heure supplémentaire ?', 'Confirmation')
    await heureSupplementaireService.valider(row.id!)
    ElMessage.success('Heure supplémentaire validée')
    await loadHeuresSupp()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error(e?.response?.data?.message || 'Erreur')
  }
}

const openRejetModal = (row: HeureSupplementaire) => {
  selectedItem.value = row
  motifRejet.value = ''
  showRejetModal.value = true
}

const confirmerRejet = async () => {
  if (!motifRejet.value.trim()) {
    ElMessage.error('Le motif de rejet est obligatoire')
    return
  }
  saving.value = true
  try {
    await heureSupplementaireService.rejeter(selectedItem.value!.id!, motifRejet.value)
    ElMessage.success('Heure supplémentaire rejetée')
    showRejetModal.value = false
    await loadHeuresSupp()
  } catch (e: any) {
    ElMessage.error(e?.response?.data?.message || 'Erreur')
  } finally {
    saving.value = false
  }
}

const integrerPaie = async (row: HeureSupplementaire) => {
  try {
    await ElMessageBox.confirm("Confirmer l'intégration de cette heure supplémentaire dans la paie ?", 'Confirmation')
    await heureSupplementaireService.integrerPaie(row.id!)
    ElMessage.success('Intégrée à la paie avec succès')
    await loadHeuresSupp()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error(e?.response?.data?.message || 'Erreur')
  }
}

const hasBrouillons = computed(() => heuresSupp.value.some(h => h.statut === 'BROUILLON'))
const hasAValider = computed(() => heuresSupp.value.some(h => h.statut === 'A_VALIDER'))

const bulkSoumettre = async () => {
  try {
    await ElMessageBox.confirm(`Confirmer la soumission de toutes les heures en brouillon (${heuresSupp.value.filter(h => h.statut === 'BROUILLON').length}) ?`, 'Confirmation')
    loading.value = true
    const brouillons = heuresSupp.value.filter(h => h.statut === 'BROUILLON')
    let success = 0
    let errors = 0
    for (const hs of brouillons) {
      try {
        await heureSupplementaireService.soumettre(hs.id!)
        success++
      } catch {
        errors++
      }
    }
    ElMessage.success(`${success} heure(s) soumise(s)${errors > 0 ? `, ${errors} erreur(s)` : ''}`)
    await loadHeuresSupp()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error(e?.response?.data?.message || 'Erreur')
  } finally {
    loading.value = false
  }
}

const bulkValider = async () => {
  try {
    await ElMessageBox.confirm(`Confirmer la validation de toutes les heures à valider (${heuresSupp.value.filter(h => h.statut === 'A_VALIDER').length}) ?`, 'Confirmation')
    loading.value = true
    const aValider = heuresSupp.value.filter(h => h.statut === 'A_VALIDER')
    let success = 0
    let errors = 0
    for (const hs of aValider) {
      try {
        await heureSupplementaireService.valider(hs.id!)
        success++
      } catch {
        errors++
      }
    }
    ElMessage.success(`${success} heure(s) validée(s)${errors > 0 ? `, ${errors} erreur(s)` : ''}`)
    await loadHeuresSupp()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error(e?.response?.data?.message || 'Erreur')
  } finally {
    loading.value = false
  }
}

const supprimer = async (row: HeureSupplementaire) => {
  try {
    await ElMessageBox.confirm('Confirmer la suppression de cette heure supplémentaire ?', 'Confirmation', { type: 'warning' })
    await heureSupplementaireService.delete(row.id!)
    ElMessage.success('Supprimée')
    await loadHeuresSupp()
  } catch (e: any) {
    if (e !== 'cancel') ElMessage.error(e?.response?.data?.message || 'Erreur')
  }
}

const voirDetail = (row: HeureSupplementaire) => {
  selectedItem.value = row
  showDetail.value = true
}

const resetFilters = () => {
  Object.assign(filters, { employeId: null, periodePaieId: null, statut: '', dateDebut: '', dateFin: '' })
  loadHeuresSupp()
}

const formatDate = (date: string | undefined) => {
  if (!date) return '—'
  try {
    const d = new Date(date)
    return d.toLocaleDateString('fr-FR')
  } catch {
    return date
  }
}

const formatMontant = (value: number | undefined) => {
  if (!value) return '0'
  return new Intl.NumberFormat('fr-FR').format(value)
}

const getTypeLabel = (type: string) => {
  const labels: Record<string, string> = { HS_15: '15%', HS_50: '50%', HS_75: '75%', HS_100: '100%' }
  return labels[type] || type
}

const getTypeTagType = (type: string) => {
  const types: Record<string, string> = { HS_15: 'info', HS_50: 'warning', HS_75: 'danger', HS_100: 'success' }
  return types[type] || 'info'
}

const getStatutLabel = (statut: string) => {
  const labels: Record<string, string> = {
    BROUILLON: 'Brouillon',
    A_VALIDER: 'À valider',
    REJETE: 'Rejeté',
    VALIDE: 'Validé',
    INTEGRE_PAIE: 'Intégré paie',
  }
  return labels[statut] || statut
}

const getStatutTagType = (statut: string) => {
  const types: Record<string, string> = {
    BROUILLON: 'info',
    A_VALIDER: 'warning',
    REJETE: 'danger',
    VALIDE: 'success',
    INTEGRE_PAIE: 'primary',
  }
  return types[statut] || 'info'
}

onMounted(async () => {
  await Promise.all([loadPersonnels(), loadPeriodes(), loadRegles()])
  await loadHeuresSupp()
})
</script>

<style scoped>
.heures-supp-view {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  font-size: 24px;
  margin: 0 0 5px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.main-content {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.filters-bar {
  margin-bottom: 10px;
}

.action-buttons {
  display: flex;
  gap: 6px;
  justify-content: center;
  flex-wrap: wrap;
}
</style>
