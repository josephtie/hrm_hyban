<template>
  <div class="saisie-elt-paie-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><Money /></el-icon>
            Saisie des Éléments de Paie
          </h1>
          <p class="page-subtitle">Gestion des gains et retenues du personnel</p>
        </div>
        <div class="header-right">
          <el-button type="success" @click="showPrimeCollectiveModal = true" class="enhanced-button collective-button">
            <el-icon><Gift /></el-icon>
            Prime Collective
          </el-button>
          <el-button type="primary" @click="showAddModal = true" class="enhanced-button">
            <el-icon><Plus /></el-icon>
            Nouvel Élément
          </el-button>
        </div>
      </div>
    </div>

    <!-- Layout avec sidebar et contenu principal -->
    <div class="content-layout">
      <!-- Sidebar pour le formulaire -->
      <div class="sidebar-panel enhanced-card" v-if="showAddModal">
        <div class="panel-header">
          <h3>{{ editingElement ? 'Modifier un Élément' : 'Ajouter un Élément' }}</h3>
          <el-button @click="closeModal" circle size="small">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <div class="form-content">
          <el-form :model="elementForm" :rules="elementRules" ref="elementFormRef" label-width="140px" size="large">
            <el-form-item label="Personnel" prop="personnelId" required>
              <el-select v-model="elementForm.personnelId" placeholder="Sélectionner le personnel" style="width: 100%" filterable>
                <el-option
                  v-for="personnel in personnels"
                  :key="personnel.id"
                  :label="`${personnel.nomComplet} - ${personnel.matricule}`"
                  :value="personnel.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Période" prop="periode" required>
              <el-select v-model="elementForm.periode" placeholder="Période" style="width: 100%">
                <el-option
                  v-for="periode in periodes"
                  :key="periode.value"
                  :label="periode.label"
                  :value="periode.value"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Statut" prop="statut">
              <el-radio-group v-model="elementForm.statut">
                <el-radio value="actif">Actif</el-radio>
                <el-radio value="inactif">Inactif</el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- Tableau dynamique des éléments -->
            <div class="elements-table-section">
              <div class="section-header">
                <h4>Éléments de Paie</h4>
                <el-button type="primary" size="small" @click="addNewElement">
                  <el-icon><Plus /></el-icon>
                  Ajouter un élément
                </el-button>
              </div>

              <el-table :data="elementForm.elements" stripe class="elements-table">
                <el-table-column label="#" type="index" width="50" />
                
                <el-table-column label="Type d'Élément" min-width="200">
                  <template #default="{ row, $index }">
                    <el-select 
                      v-model="row.typeElementId" 
                      placeholder="Sélectionner" 
                      size="small"
                      style="width: 100%"
                      @change="updateElementRow($index)"
                    >
                      <el-option
                        v-for="type in typesElements"
                        :key="type.id"
                        :label="type.libelle"
                        :value="type.id"
                      />
                    </el-select>
                  </template>
                </el-table-column>

                <el-table-column label="Quantité" width="120">
                  <template #default="{ row, $index }">
                    <el-input-number
                      v-if="shouldShowQuantiteForType(row.typeElementId)"
                      v-model="row.quantite"
                      :min="0"
                      :step="getQuantiteStepForType(row.typeElementId)"
                      size="small"
                      style="width: 100%"
                      :placeholder="getQuantitePlaceholderForType(row.typeElementId)"
                      @change="updateElementRow($index)"
                    />
                    <span v-else>-</span>
                  </template>
                </el-table-column>

                <el-table-column label="Montant" width="150">
                  <template #default="{ row, $index }">
                    <el-input-number
                      v-model="row.montant"
                      :min="0"
                      :step="1000"
                      size="small"
                      style="width: 100%"
                      placeholder="Montant"
                      @change="updateElementRow($index)"
                    />
                  </template>
                </el-table-column>

                <el-table-column label="Type" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getTypeColorForType(row.typeElementId)" size="small">
                      {{ getTypeLabelForType(row.typeElementId) }}
                    </el-tag>
                  </template>
                </el-table-column>

                <el-table-column label="Commentaire" min-width="150">
                  <template #default="{ row }">
                    <el-input
                      v-model="row.commentaire"
                      size="small"
                      placeholder="Commentaire"
                    />
                  </template>
                </el-table-column>

                <el-table-column label="Actions" width="80">
                  <template #default="{ $index }">
                    <el-button type="danger" size="small" circle @click="removeElement($index)">
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- Résumé des totaux -->
              <div class="totals-summary" v-if="elementForm.elements.length > 0">
                <div class="total-item">
                  <span class="total-label">🟢 Total Gains :</span>
                  <span class="total-amount">{{ formatCurrency(totalGains) }}</span>
                </div>
                <div class="total-item">
                  <span class="total-label">🔴 Total Retenues :</span>
                  <span class="total-amount">{{ formatCurrency(Math.abs(totalRetenues)) }}</span>
                </div>
                <div class="total-item net">
                  <span class="total-label">🔵 Net :</span>
                  <span class="total-amount">{{ formatCurrency(totalGains + totalRetenues) }}</span>
                </div>
              </div>
            </div>

            <el-form-item>
              <el-button type="primary" @click="saveElements" :loading="formLoading" :disabled="elementForm.elements.length === 0">
                Enregistrer tous les éléments ({{ elementForm.elements.length }})
              </el-button>
              <el-button @click="closeModal">Annuler</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- Contenu principal avec le tableau -->
      <div class="main-panel enhanced-card">
        <div class="panel-header">
          <h3>Liste des Éléments de Paie</h3>
          <div class="panel-controls">
            <el-input
              v-model="searchQuery"
              placeholder="Rechercher un élément..."
              prefix-icon="Search"
              style="width: 300px"
              clearable
            />
            <el-select v-model="filterType" placeholder="Filtrer par type" style="width: 150px" clearable>
              <el-option label="Gain" value="gain" />
              <el-option label="Retenue" value="retenue" />
            </el-select>
            <el-select v-model="filterStatus" placeholder="Filtrer par statut" style="width: 150px" clearable>
              <el-option label="Actif" value="actif" />
              <el-option label="Inactif" value="inactif" />
            </el-select>
          </div>
        </div>

        <div class="table-container">
          <el-table :data="filteredElements" stripe v-loading="loading" @sort-change="handleSortChange">
            <el-table-column label="Personnel" prop="personnelNom" min-width="200" sortable="custom">
              <template #default="{ row }">
                <div class="personnel-info">
                  <div class="personnel-name">{{ row.personnelNom }}</div>
                  <div class="personnel-matricule">{{ row.matricule }}</div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="Élément" prop="libelleElement" min-width="180" sortable="custom">
              <template #default="{ row }">
                <div class="element-info">
                  <div class="element-name">{{ row.libelleElement }}</div>
                  <div class="element-code">{{ row.codeElement }}</div>
                </div>
              </template>
            </el-table-column>

            <el-table-column label="Type" prop="type" width="100" sortable="custom">
              <template #default="{ row }">
                <el-tag :type="getTypeColor(row.type)">
                  {{ row.type }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column label="Quantité" prop="quantite" width="100" sortable="custom">
              <template #default="{ row }">
                {{ row.quantiteAffichee }}
              </template>
            </el-table-column>

            <el-table-column label="Montant" prop="montant" width="120" sortable="custom">
              <template #default="{ row }">
                <span class="amount" :class="row.type === 'Gain' ? 'gain-amount' : 'retenue-amount'">
                  {{ formatCurrency(Math.abs(row.montant)) }}
                </span>
              </template>
            </el-table-column>

            <el-table-column label="Période" prop="periode" width="120" sortable="custom">
              <template #default="{ row }">
                {{ getPeriodeLabel(row.periode) }}
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
                <el-button type="primary" size="small" @click="editElement(row)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button type="info" size="small" @click="viewElementDetails(row)">
                  <el-icon><View /></el-icon>
                </el-button>
                <el-button type="danger" size="small" @click="deleteElement(row)">
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
              :total="filteredElements.length"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de détails -->
    <el-dialog v-model="showDetailsModal" title="Détails de l'Élément de Paie" width="600px">
      <div v-if="selectedElement" class="element-details">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Personnel">
            {{ selectedElement.personnelNom }} ({{ selectedElement.matricule }})
          </el-descriptions-item>
          <el-descriptions-item label="Élément">
            {{ selectedElement.libelleElement }}
          </el-descriptions-item>
          <el-descriptions-item label="Type">
            <el-tag :type="getTypeColor(selectedElement.type)">
              {{ selectedElement.type }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Quantité">
            {{ selectedElement.quantiteAffichee }}
          </el-descriptions-item>
          <el-descriptions-item label="Montant">
            <span class="amount" :class="selectedElement.type === 'Gain' ? 'gain-amount' : 'retenue-amount'">
              {{ formatCurrency(Math.abs(selectedElement.montant)) }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="Période">
            {{ getPeriodeLabel(selectedElement.periode) }}
          </el-descriptions-item>
          <el-descriptions-item label="Statut" span="2">
            <el-tag :type="getStatusColor(selectedElement.statut)">
              {{ getStatusLabel(selectedElement.statut) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Commentaire" span="2" v-if="selectedElement.commentaire">
            {{ selectedElement.commentaire }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- Modal pour primes collectives -->
    <el-dialog v-model="showPrimeCollectiveModal" title="Prime Collective" width="700px">
      <div class="prime-collective-content">
        <el-form :model="primeCollectiveForm" :rules="primeCollectiveRules" ref="primeCollectiveFormRef" label-width="140px" size="large">
          <el-form-item label="Type de Prime" prop="typeElementId" required>
            <el-select v-model="primeCollectiveForm.typeElementId" placeholder="Sélectionner la prime" style="width: 100%">
              <el-option
                v-for="type in primesCollectives"
                :key="type.id"
                :label="`${type.libelle} - ${formatCurrency(type.montantParDefaut)}`"
                :value="type.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="Montant" prop="montant" required>
            <el-input-number 
              v-model="primeCollectiveForm.montant" 
              :min="0" 
              :step="1000"
              style="width: 100%"
              placeholder="Montant de la prime"
              :formatter="(value: number) => `${value.toLocaleString()} XOF`"
              :parser="(value: string) => value.replace(/[^\d]/g, '')"
            />
          </el-form-item>

          <el-form-item label="Période" prop="periode" required>
            <el-select v-model="primeCollectiveForm.periode" placeholder="Période" style="width: 100%">
              <el-option
                v-for="periode in periodes"
                :key="periode.value"
                :label="periode.label"
                :value="periode.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="Type de Cible" prop="typeCible" required>
            <el-radio-group v-model="primeCollectiveForm.typeCible">
              <el-radio value="tout_le_monde">Tout le monde</el-radio>
              <el-radio value="categorie">Catégorie spécifique</el-radio>
              <el-radio value="selection">Sélection manuelle</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="Catégorie" prop="categorieId" v-if="primeCollectiveForm.typeCible === 'categorie'" required>
            <el-select v-model="primeCollectiveForm.categorieId" placeholder="Sélectionner la catégorie" style="width: 100%">
              <el-option
                v-for="categorie in categories"
                :key="categorie.id"
                :label="categorie.libelle"
                :value="categorie.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="Personnels" prop="personnelIds" v-if="primeCollectiveForm.typeCible === 'selection'" required>
            <el-select 
              v-model="primeCollectiveForm.personnelIds" 
              placeholder="Sélectionner les personnels" 
              style="width: 100%"
              multiple
              filterable
            >
              <el-option
                v-for="personnel in personnels"
                :key="personnel.id"
                :label="`${personnel.nomComplet} - ${personnel.matricule}`"
                :value="personnel.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="Commentaire" prop="commentaire">
            <el-input
              v-model="primeCollectiveForm.commentaire"
              type="textarea"
              :rows="3"
              placeholder="Commentaire sur la prime collective"
            />
          </el-form-item>

          <!-- Résumé -->
          <div class="prime-summary" v-if="getPersonnelsCibles().length > 0">
            <el-alert 
              :title="`Cette prime sera attribuée à ${getPersonnelsCibles().length} personne(s)`" 
              type="info" 
              :closable="false"
              show-icon
            />
            <div class="summary-details">
              <div class="summary-item">
                <span class="label">Montant total :</span>
                <span class="value">{{ formatCurrency(primeCollectiveForm.montant * getPersonnelsCibles().length) }}</span>
              </div>
              <div class="summary-item">
                <span class="label">Montant par personne :</span>
                <span class="value">{{ formatCurrency(primeCollectiveForm.montant) }}</span>
              </div>
            </div>
          </div>

          <el-form-item>
            <el-button type="success" @click="savePrimeCollective" :loading="collectiveLoading" :disabled="getPersonnelsCibles().length === 0">
              Attribuer la prime ({{ getPersonnelsCibles().length }} personne(s))
            </el-button>
            <el-button @click="closePrimeCollectiveModal">Annuler</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Money, Plus, Close, Edit, View, Delete, Search, Present 
} from '@element-plus/icons-vue'

interface Categorie {
  id: number
  libelle: string
}

interface PrimeCollective {
  id: number
  libelle: string
  montantParDefaut: number
}

interface ElementPaie {
  id: number
  personnelId: number
  personnelNom: string
  matricule: string
  typeElementId: number
  libelleElement: string
  codeElement: string
  type: string
  quantite: number
  quantiteAffichee: string
  montant: number
  periode: string
  statut: string
  commentaire: string
}

interface Personnel {
  id: number
  matricule: string
  nomComplet: string
}

interface TypeElement {
  id: number
  libelle: string
  code: string
  type: string
  quantite: boolean
}

// Données réactives
const loading = ref(false)
const formLoading = ref(false)
const collectiveLoading = ref(false)
const showAddModal = ref(false)
const showDetailsModal = ref(false)
const showPrimeCollectiveModal = ref(false)
const editingElement = ref(false)
const selectedElement = ref<ElementPaie | null>(null)
const searchQuery = ref('')
const filterType = ref('')
const filterStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const sortBy = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')

// Formulaire d'élément de paie
const elementForm = reactive({
  id: 0,
  personnelId: 0,
  periode: '',
  statut: 'actif',
  elements: [] as Array<{
    id: number
    typeElementId: number
    quantite: number
    montant: number
    commentaire: string
  }>
})

// Formulaire de prime collective
const primeCollectiveForm = reactive({
  typeElementId: 0,
  montant: 0,
  periode: '',
  typeCible: 'tout_le_monde',
  categorieId: 0,
  personnelIds: [] as number[],
  commentaire: ''
})

// Règles de validation
const elementRules = {
  personnelId: [{ required: true, message: 'Veuillez sélectionner le personnel', trigger: 'change' }],
  periode: [{ required: true, message: 'Veuillez sélectionner la période', trigger: 'change' }]
}

const primeCollectiveRules = {
  typeElementId: [{ required: true, message: 'Veuillez sélectionner le type de prime', trigger: 'change' }],
  montant: [{ required: true, message: 'Veuillez saisir le montant', trigger: 'blur' }],
  periode: [{ required: true, message: 'Veuillez sélectionner la période', trigger: 'change' }],
  typeCible: [{ required: true, message: 'Veuillez sélectionner le type de cible', trigger: 'change' }],
  categorieId: [{ required: true, message: 'Veuillez sélectionner la catégorie', trigger: 'change' }],
  personnelIds: [{ required: true, message: 'Veuillez sélectionner au moins un personnel', trigger: 'change' }]
}

const elementFormRef = ref()
const primeCollectiveFormRef = ref()

// Données mockées
const elements = ref<ElementPaie[]>([
  {
    id: 1,
    personnelId: 1,
    personnelNom: 'Kouadio Jean',
    matricule: 'EMP001',
    typeElementId: 1,
    libelleElement: 'Salaire de base',
    codeElement: 'SAL_BASE',
    type: 'Gain',
    quantite: 1,
    quantiteAffichee: '1',
    montant: 500000,
    periode: '2024-01',
    statut: 'actif',
    commentaire: 'Salaire mensuel de base'
  },
  {
    id: 2,
    personnelId: 1,
    personnelNom: 'Kouadio Jean',
    matricule: 'EMP001',
    typeElementId: 2,
    libelleElement: 'Prime de transport',
    codeElement: 'PRIM_TRANS',
    type: 'Gain',
    quantite: 1,
    quantiteAffichee: '1',
    montant: 50000,
    periode: '2024-01',
    statut: 'actif',
    commentaire: 'Indemnité de transport mensuelle'
  },
  {
    id: 3,
    personnelId: 2,
    personnelNom: 'Touré Aminata',
    matricule: 'EMP002',
    typeElementId: 3,
    libelleElement: 'CNPS',
    codeElement: 'CNPS',
    type: 'Retenue',
    quantite: 1,
    quantiteAffichee: '1',
    montant: -45000,
    periode: '2024-01',
    statut: 'actif',
    commentaire: 'Retenue CNPS obligatoire'
  }
])

const personnels = ref<Personnel[]>([
  { id: 1, nomComplet: 'Kouadio Jean', matricule: 'EMP001' },
  { id: 2, nomComplet: 'Touré Aminata', matricule: 'EMP002' },
  { id: 3, nomComplet: 'Soro Mohamed', matricule: 'EMP003' },
  { id: 4, nomComplet: 'Koné Fatoumata', matricule: 'EMP004' },
  { id: 5, nomComplet: 'Bamba Yves', matricule: 'EMP005' }
])

const categories = ref<Categorie[]>([
  { id: 1, libelle: 'Cadres' },
  { id: 2, libelle: 'Agents de maîtrise' },
  { id: 3, libelle: 'Agents d\'exécution' },
  { id: 4, libelle: 'Contractuels' }
])

const primesCollectives = ref<PrimeCollective[]>([
  { id: 1, libelle: 'Prime de fin d\'année', montantParDefaut: 50000 },
  { id: 2, libelle: 'Prime de performance', montantParDefaut: 25000 },
  { id: 3, libelle: 'Prime de transport', montantParDefaut: 15000 },
  { id: 4, libelle: 'Prime de panier', montantParDefaut: 10000 },
  { id: 5, libelle: 'Prime de logement', montantParDefaut: 75000 }
])

const typesElements = ref<TypeElement[]>([
  { id: 1, libelle: 'Salaire de base', code: 'SAL_BASE', type: 'Gain', quantite: false },
  { id: 2, libelle: 'Prime de transport', code: 'PRIM_TRANS', type: 'Gain', quantite: false },
  { id: 3, libelle: 'CNPS', code: 'CNPS', type: 'Retenue', quantite: false },
  { id: 4, libelle: 'Heures supplémentaires', code: 'HS_SUP', type: 'Gain', quantite: true },
  { id: 5, libelle: 'Prime de panier', code: 'PRIM_PAN', type: 'Gain', quantite: true },
  { id: 6, libelle: 'Impôt sur le revenu', code: 'IR', type: 'Retenue', quantite: false }
])

const periodes = ref([
  { value: '2024-01', label: 'Janvier 2024' },
  { value: '2024-02', label: 'Février 2024' },
  { value: '2024-03', label: 'Mars 2024' },
  { value: '2024-04', label: 'Avril 2024' },
  { value: '2024-05', label: 'Mai 2024' },
  { value: '2024-06', label: 'Juin 2024' },
  { value: '2024-07', label: 'Juillet 2024' },
  { value: '2024-08', label: 'Août 2024' },
  { value: '2024-09', label: 'Septembre 2024' },
  { value: '2024-10', label: 'Octobre 2024' },
  { value: '2024-11', label: 'Novembre 2024' },
  { value: '2024-12', label: 'Décembre 2024' }
])

// Computed properties
const filteredElements = computed(() => {
  let filtered = elements.value

  // Filtrage par recherche
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(element => 
      element.personnelNom.toLowerCase().includes(query) ||
      element.matricule.toLowerCase().includes(query) ||
      element.libelleElement.toLowerCase().includes(query) ||
      element.codeElement.toLowerCase().includes(query)
    )
  }

  // Filtrage par type
  if (filterType.value) {
    filtered = filtered.filter(element => element.type.toLowerCase() === filterType.value)
  }

  // Filtrage par statut
  if (filterStatus.value) {
    filtered = filtered.filter(element => element.statut === filterStatus.value)
  }

  // Tri
  if (sortBy.value) {
    filtered.sort((a, b) => {
      const aVal = a[sortBy.value as keyof ElementPaie]
      const bVal = b[sortBy.value as keyof ElementPaie]
      
      if (sortOrder.value === 'asc') {
        return aVal > bVal ? 1 : -1
      } else {
        return aVal < bVal ? 1 : -1
      }
    })
  }

  return filtered
})

const totalGains = computed(() => {
  return elementForm.elements
    .filter(el => {
      const type = typesElements.value.find(t => t.id === el.typeElementId)
      return type?.type === 'Gain'
    })
    .reduce((sum, el) => sum + el.montant, 0)
})

const totalRetenues = computed(() => {
  return elementForm.elements
    .filter(el => {
      const type = typesElements.value.find(t => t.id === el.typeElementId)
      return type?.type === 'Retenue'
    })
    .reduce((sum, el) => sum + el.montant, 0)
})

const shouldShowQuantiteForType = (typeElementId: number) => {
  const type = typesElements.value.find(t => t.id === typeElementId)
  return type?.quantite || false
}

const getQuantiteStepForType = (typeElementId: number) => {
  const type = typesElements.value.find(t => t.id === typeElementId)
  return type?.code === 'HS_SUP' ? 1 : 0.5
}

const getQuantitePlaceholderForType = (typeElementId: number) => {
  const type = typesElements.value.find(t => t.id === typeElementId)
  return type?.code === 'HS_SUP' ? 'Nombre d\'heures' : 'Nombre de jours'
}

const getTypeColorForType = (typeElementId: number) => {
  const type = typesElements.value.find(t => t.id === typeElementId)
  return type?.type === 'Gain' ? 'success' : 'danger'
}

const getTypeLabelForType = (typeElementId: number) => {
  const type = typesElements.value.find(t => t.id === typeElementId)
  return type?.type || ''
}

// Méthodes pour les primes collectives
const getPersonnelsCibles = () => {
  if (primeCollectiveForm.typeCible === 'tout_le_monde') {
    return personnels.value
  } else if (primeCollectiveForm.typeCible === 'categorie') {
    return personnels.value.filter(p => {
      // Simulation de filtrage par catégorie
      return p.categorieId === primeCollectiveForm.categorieId
    })
  } else if (primeCollectiveForm.typeCible === 'selection') {
    return personnels.value.filter(p => 
      primeCollectiveForm.personnelIds.includes(p.id)
    )
  }
  return []
}

const closePrimeCollectiveModal = () => {
  showPrimeCollectiveModal.value = false
  resetPrimeCollectiveForm()
}

const resetPrimeCollectiveForm = () => {
  Object.assign(primeCollectiveForm, {
    typeElementId: 0,
    montant: 0,
    periode: '',
    typeCible: 'tout_le_monde',
    categorieId: 0,
    personnelIds: [],
    commentaire: ''
  })
  primeCollectiveFormRef.value?.resetFields()
}

const savePrimeCollective = async () => {
  if (!primeCollectiveFormRef.value) return
  
  try {
    await primeCollectiveFormRef.value.validate()
    collectiveLoading.value = true

    const personnelsCibles = getPersonnelsCibles()
    if (personnelsCibles.length === 0) {
      ElMessage.warning('Veuillez sélectionner au moins une cible')
      return
    }

    // Simulation d'un appel API
    await new Promise(resolve => setTimeout(resolve, 1000))

    const primeType = primesCollectives.value.find(p => p.id === primeCollectiveForm.typeElementId)
    
    // Créer les éléments de paie pour chaque personnel cible
    personnelsCibles.forEach(personnel => {
      const newElement: ElementPaie = {
        id: Date.now() + Math.random(),
        personnelId: personnel.id,
        personnelNom: personnel.nomComplet,
        matricule: personnel.matricule,
        typeElementId: primeCollectiveForm.typeElementId,
        libelleElement: primeType?.libelle || '',
        codeElement: `PRIM_${primeType?.libelle?.toUpperCase() || 'COLLECTIVE'}`,
        type: 'Gain',
        quantite: 1,
        quantiteAffichee: '1',
        montant: primeCollectiveForm.montant,
        periode: primeCollectiveForm.periode,
        statut: 'actif',
        commentaire: primeCollectiveForm.commentaire || `Prime collective: ${primeType?.libelle}`
      }
      elements.value.unshift(newElement)
    })

    ElMessage.success(`Prime collective "${primeType?.libelle}" attribuée à ${personnelsCibles.length} personne(s)`)
    closePrimeCollectiveModal()
  } catch (error) {
    console.error('Erreur lors de la sauvegarde:', error)
  } finally {
    collectiveLoading.value = false
  }
}

// Méthodes
const addNewElement = () => {
  elementForm.elements.push({
    id: Date.now(),
    typeElementId: 0,
    quantite: 0,
    montant: 0,
    commentaire: ''
  })
}

const removeElement = (index: number) => {
  elementForm.elements.splice(index, 1)
}

const updateElementRow = (index: number) => {
  // Force la réactivité
  elementForm.elements.splice(index, 1, elementForm.elements[index])
}

const closeModal = () => {
  showAddModal.value = false
  editingElement.value = false
  resetForm()
}

const resetForm = () => {
  Object.assign(elementForm, {
    id: 0,
    personnelId: 0,
    periode: '',
    statut: 'actif',
    elements: []
  })
  elementFormRef.value?.resetFields()
}

const saveElements = async () => {
  if (!elementFormRef.value) return
  
  try {
    await elementFormRef.value.validate()
    formLoading.value = true

    // Vérifier qu'il y a des éléments à sauvegarder
    if (elementForm.elements.length === 0) {
      ElMessage.warning('Veuillez ajouter au moins un élément')
      return
    }

    // Vérifier que tous les éléments sont valides
    const invalidElements = elementForm.elements.filter(el => el.typeElementId === 0 || el.montant === 0)
    if (invalidElements.length > 0) {
      ElMessage.warning('Veuillez compléter tous les éléments (type et montant requis)')
      return
    }

    // Simulation d'un appel API
    await new Promise(resolve => setTimeout(resolve, 1000))

    const personnel = personnels.value.find(p => p.id === elementForm.personnelId)
    
    // Créer les éléments de paie
    elementForm.elements.forEach(el => {
      const typeElement = typesElements.value.find(t => t.id === el.typeElementId)
      const newElement: ElementPaie = {
        id: Date.now() + Math.random(),
        personnelId: elementForm.personnelId,
        personnelNom: personnel?.nomComplet || '',
        matricule: personnel?.matricule || '',
        typeElementId: el.typeElementId,
        libelleElement: typeElement?.libelle || '',
        codeElement: typeElement?.code || '',
        type: typeElement?.type || '',
        quantite: el.quantite,
        quantiteAffichee: shouldShowQuantiteForType(el.typeElementId) ? el.quantite.toString() : '1',
        montant: typeElement?.type === 'Retenue' ? -Math.abs(el.montant) : el.montant,
        periode: elementForm.periode,
        statut: elementForm.statut,
        commentaire: el.commentaire
      }
      elements.value.unshift(newElement)
    })

    ElMessage.success(`${elementForm.elements.length} élément(s) ajouté(s) avec succès`)
    closeModal()
  } catch (error) {
    console.error('Erreur lors de la sauvegarde:', error)
  } finally {
    formLoading.value = false
  }
}

const editElement = (element: ElementPaie) => {
  Object.assign(elementForm, element)
  editingElement.value = true
  showAddModal.value = true
}

const deleteElement = async (element: ElementPaie) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer l'élément "${element.libelleElement}" de ${element.personnelNom} ?`,
      'Confirmation de suppression',
      {
        confirmButtonText: 'Supprimer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )

    const index = elements.value.findIndex(e => e.id === element.id)
    if (index !== -1) {
      elements.value.splice(index, 1)
      ElMessage.success('Élément supprimé avec succès')
    }
  } catch {
    // L'utilisateur a annulé
  }
}

const viewElementDetails = (element: ElementPaie) => {
  selectedElement.value = element
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

const getTypeColor = (type: string) => {
  return type === 'Gain' ? 'success' : 'danger'
}

const getStatusColor = (status: string) => {
  return status === 'actif' ? 'success' : 'warning'
}

const getStatusLabel = (status: string) => {
  return status === 'actif' ? 'Actif' : 'Inactif'
}

const getPeriodeLabel = (periode: string) => {
  const periodeObj = periodes.value.find(p => p.value === periode)
  return periodeObj?.label || periode
}

onMounted(() => {
  // Charger les données initiales
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
})
</script>

<style scoped>
.saisie-elt-paie-view {
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

.elements-table-section {
  margin: 20px 0;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.section-header h4 {
  margin: 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.elements-table {
  margin: 0;
}

.elements-table .el-table__header {
  background: #fafafa;
}

.totals-summary {
  padding: 16px 20px;
  background: #f8f9fa;
  border-top: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.total-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px 16px;
  border-radius: 6px;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  min-width: 120px;
}

.total-item.net {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.total-label {
  font-size: 12px;
  font-weight: 500;
  margin-bottom: 4px;
}

.total-amount {
  font-size: 16px;
  font-weight: 700;
}

.total-item.net .total-amount {
  color: white;
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

.element-info {
  display: flex;
  flex-direction: column;
}

.element-name {
  font-weight: 600;
  color: #303133;
}

.element-code {
  font-size: 12px;
  color: #909399;
}

.amount {
  font-weight: 600;
}

.gain-amount {
  color: #67c23a;
}

.retenue-amount {
  color: #f56c6c;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

.element-details {
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

.collective-button {
  background: linear-gradient(135deg, #28a745 0%, #21c4b6 100%);
  border: none;
  color: white;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.collective-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(40, 167, 69, 0.3);
}

.prime-collective-content {
  padding: 20px;
}

.prime-summary {
  margin: 20px 0;
}

.summary-details {
  display: flex;
  gap: 20px;
  margin-top: 12px;
}

.summary-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px 12px;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}

.summary-label {
  font-size: 12px;
  color: #606266;
  margin-bottom: 4px;
}

.summary-value {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.enhanced-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}
</style>
