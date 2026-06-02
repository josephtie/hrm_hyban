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
        <div class="panel-header modern-panel-header">
          <div class="panel-header-left">
            <div class="panel-header-icon">
              <el-icon><Edit v-if="editingElement" /><Plus v-else /></el-icon>
            </div>
            <div class="panel-header-text">
              <h3>{{ editingElement ? 'Modifier un Élément' : 'Ajouter un Élément' }}</h3>
              <span>{{ editingElement ? 'Mettre à jour les informations' : 'Saisir les éléments de paie' }}</span>
            </div>
          </div>
          <el-button @click="closeModal" circle size="small" class="panel-close-btn">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <div class="form-content">
          <el-form :model="elementForm" :rules="elementRules" ref="elementFormRef" label-width="140px" size="large">
            <el-form-item label="Personnel" prop="personnelId" required>
              <el-select
                v-model="elementForm.personnelId"
                placeholder="Rechercher par nom, prénom ou matricule..."
                style="width: 100%"
                filterable
                clearable
                :filter-method="filterPersonnel"
                :loading="personnelsLoading"
                loading-text="Chargement du personnel..."
                no-data-text="Aucun personnel trouvé"
              >
                <el-option
                  v-for="personnel in filteredPersonnels"
                  :key="personnel.id"
                  :label="`${personnel.nomComplet} - ${personnel.matricule}`"
                  :value="personnel.id"
                >
                  <span style="float: left">{{ personnel.nomComplet }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ personnel.matricule }}</span>
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="Période" prop="periode" required>
              <el-select
                v-model="elementForm.periode"
                placeholder="Rechercher une période..."
                style="width: 100%"
                filterable
                clearable
                :loading="periodesLoading"
                loading-text="Chargement des périodes..."
                no-data-text="Aucune période trouvée"
              >
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
                      placeholder="Sélectionner une rubrique" 
                      size="small"
                      style="width: 100%"
                      filterable
                      clearable
                      :loading="typesElementsLoading"
                      loading-text="Chargement des rubriques..."
                      no-data-text="Aucune rubrique trouvée"
                      @change="updateElementRow($index)"
                    >
                      <el-option
                        v-for="type in typesElements"
                        :key="type.id"
                        :label="`${type.libelle}${type.code ? ' (' + type.code + ')' : ''}`"
                        :value="type.id"
                      >
                        <span style="float: left">{{ type.libelle }}</span>
                        <el-tag :type="type.type === 'Gain' ? 'success' : 'danger'" size="small" style="float: right; margin-left: 8px">
                          {{ type.type }}
                        </el-tag>
                      </el-option>
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
              :total="totalElements"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de détails — Design moderne -->
    <el-dialog
      v-model="showDetailsModal"
      width="640px"
      :show-close="false"
      align-center
      destroy-on-close
      class="details-modal"
    >
      <template #header>
        <div class="details-modal-header" :class="selectedElement?.type === 'Gain' ? 'is-gain' : 'is-retenue'">
          <div class="header-icon">
            <el-icon><View /></el-icon>
          </div>
          <div class="header-text">
            <h2 class="header-title">Détails de l'élément de paie</h2>
            <p class="header-subtitle" v-if="selectedElement">
              {{ selectedElement.personnelNom }} · {{ selectedElement.matricule }}
            </p>
          </div>
          <el-button text class="header-close" @click="showDetailsModal = false">
            <el-icon :size="22"><Close /></el-icon>
          </el-button>
        </div>
      </template>

      <div v-if="selectedElement" class="details-body">
        <!-- Hero amount -->
        <div class="amount-hero" :class="selectedElement.type === 'Gain' ? 'is-gain' : 'is-retenue'">
          <span class="amount-label">{{ selectedElement.type === 'Gain' ? 'Gain' : 'Retenue' }}</span>
          <div class="amount-value">{{ formatCurrency(Math.abs(selectedElement.montant)) }}</div>
          <div class="amount-tags">
            <el-tag :type="getTypeColor(selectedElement.type)" round>{{ selectedElement.type }}</el-tag>
            <el-tag :type="getStatusColor(selectedElement.statut)" round effect="plain">
              {{ getStatusLabel(selectedElement.statut) }}
            </el-tag>
          </div>
        </div>

        <!-- Info grid -->
        <div class="details-grid">
          <div class="detail-item">
            <div class="detail-icon"><el-icon><User /></el-icon></div>
            <div class="detail-content">
              <span class="detail-label">Personnel</span>
              <span class="detail-value">{{ selectedElement.personnelNom }}</span>
              <span class="detail-meta">Matricule : {{ selectedElement.matricule }}</span>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-icon"><el-icon><Present /></el-icon></div>
            <div class="detail-content">
              <span class="detail-label">Rubrique</span>
              <span class="detail-value">{{ selectedElement.libelleElement }}</span>
              <span class="detail-meta" v-if="selectedElement.codeElement">Code : {{ selectedElement.codeElement }}</span>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-icon"><el-icon><Calendar /></el-icon></div>
            <div class="detail-content">
              <span class="detail-label">Période</span>
              <span class="detail-value">{{ getPeriodeLabel(selectedElement.periode) }}</span>
            </div>
          </div>

          <div class="detail-item">
            <div class="detail-icon"><el-icon><Coin /></el-icon></div>
            <div class="detail-content">
              <span class="detail-label">Quantité</span>
              <span class="detail-value">{{ selectedElement.quantiteAffichee || '-' }}</span>
            </div>
          </div>
        </div>

        <!-- Commentaire -->
        <div class="comment-block" v-if="selectedElement.commentaire">
          <div class="comment-header">
            <el-icon><ChatLineRound /></el-icon>
            <span>Commentaire</span>
          </div>
          <p class="comment-text">{{ selectedElement.commentaire }}</p>
        </div>
      </div>

      <template #footer>
        <div class="details-modal-footer">
          <el-button size="large" plain @click="showDetailsModal = false">Fermer</el-button>
          <el-button
            v-if="selectedElement"
            type="primary"
            size="large"
            class="btn-edit-details"
            @click="() => { showDetailsModal = false; editElement(selectedElement!) }"
          >
            <el-icon><Edit /></el-icon>
            Modifier
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Modal pour primes collectives — Design moderne -->
    <el-dialog
      v-model="showPrimeCollectiveModal"
      width="820px"
      :show-close="false"
      align-center
      destroy-on-close
      class="prime-modal"
    >
      <!-- Header personnalisé avec gradient -->
      <template #header>
        <div class="prime-modal-header">
          <div class="header-icon">
            <el-icon><Trophy /></el-icon>
          </div>
          <div class="header-text">
            <h2 class="header-title">Attribution d'une Prime Collective</h2>
            <p class="header-subtitle">Distribuez une prime à plusieurs personnels en une seule opération</p>
          </div>
          <el-button text class="header-close" @click="closePrimeCollectiveModal">
            <el-icon :size="22"><Close /></el-icon>
          </el-button>
        </div>
      </template>

      <div class="prime-modal-body">
        <el-form
          :model="primeCollectiveForm"
          :rules="primeCollectiveRules"
          ref="primeCollectiveFormRef"
          label-position="top"
          size="large"
        >
          <!-- ÉTAPE 1 : Définition de la prime -->
          <div class="step-section">
            <div class="step-header">
              <div class="step-number">1</div>
              <div class="step-title">
                <h3>Définition de la prime</h3>
                <span>Choisissez la rubrique, le montant et la période</span>
              </div>
            </div>

            <div class="step-grid">
              <el-form-item label="Type de prime" prop="typeElementId" class="span-2">
                <el-select
                  v-model="primeCollectiveForm.typeElementId"
                  placeholder="Rechercher une rubrique..."
                  style="width: 100%"
                  filterable
                  clearable
                  :loading="typesElementsLoading"
                  loading-text="Chargement des rubriques..."
                  no-data-text="Aucune rubrique trouvée"
                >
                  <template #prefix><el-icon><Present /></el-icon></template>
                  <el-option
                    v-for="type in typesElements"
                    :key="type.id"
                    :label="`${type.libelle}${type.code ? ' (' + type.code + ')' : ''}`"
                    :value="type.id"
                  >
                    <div class="option-row">
                      <span class="option-label">{{ type.libelle }}</span>
                      <el-tag :type="type.type === 'Gain' ? 'success' : 'danger'" size="small" round>
                        {{ type.type }}
                      </el-tag>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="Montant par personne" prop="montant">
                <el-input-number
                  v-model="primeCollectiveForm.montant"
                  :min="0"
                  :step="1000"
                  style="width: 100%"
                  placeholder="0"
                  :controls="false"
                  :formatter="(value: number) => `${value.toLocaleString()} XOF`"
                  :parser="(value: string) => value.replace(/[^\d]/g, '')"
                />
              </el-form-item>

              <el-form-item label="Période de paie" prop="periode">
                <el-select
                  v-model="primeCollectiveForm.periode"
                  placeholder="Sélectionner une période"
                  style="width: 100%"
                  filterable
                  clearable
                  :loading="periodesLoading"
                >
                  <template #prefix><el-icon><Calendar /></el-icon></template>
                  <el-option
                    v-for="periode in periodes"
                    :key="periode.value"
                    :label="periode.label"
                    :value="periode.value"
                  />
                </el-select>
              </el-form-item>
            </div>
          </div>

          <!-- ÉTAPE 2 : Cible -->
          <div class="step-section">
            <div class="step-header">
              <div class="step-number">2</div>
              <div class="step-title">
                <h3>Bénéficiaires</h3>
                <span>Qui doit recevoir cette prime ?</span>
              </div>
            </div>

            <!-- Cards radio -->
            <div class="target-cards">
              <div
                class="target-card"
                :class="{ active: primeCollectiveForm.typeCible === 'tout_le_monde' }"
                @click="primeCollectiveForm.typeCible = 'tout_le_monde'"
              >
                <div class="target-icon"><el-icon><UserFilled /></el-icon></div>
                <div class="target-name">Tout le personnel</div>
                <div class="target-desc">Attribution à tous les employés actifs</div>
                <div class="target-check"><el-icon><CircleCheckFilled /></el-icon></div>
              </div>

              <div
                class="target-card"
                :class="{ active: primeCollectiveForm.typeCible === 'categorie' }"
                @click="primeCollectiveForm.typeCible = 'categorie'"
              >
                <div class="target-icon"><el-icon><OfficeBuilding /></el-icon></div>
                <div class="target-name">Par catégorie</div>
                <div class="target-desc">Filtrer par catégorie professionnelle</div>
                <div class="target-check"><el-icon><CircleCheckFilled /></el-icon></div>
              </div>

              <div
                class="target-card"
                :class="{ active: primeCollectiveForm.typeCible === 'selection' }"
                @click="primeCollectiveForm.typeCible = 'selection'"
              >
                <div class="target-icon"><el-icon><Select /></el-icon></div>
                <div class="target-name">Sélection manuelle</div>
                <div class="target-desc">Choisir les personnels un par un</div>
                <div class="target-check"><el-icon><CircleCheckFilled /></el-icon></div>
              </div>
            </div>

            <!-- Sous-sélection conditionnelle -->
            <transition name="fade-slide">
              <el-form-item
                v-if="primeCollectiveForm.typeCible === 'categorie'"
                label="Catégorie(s) à cibler"
                prop="categorieIds"
                class="sub-select"
              >
                <el-select
                  v-model="primeCollectiveForm.categorieIds"
                  placeholder="Sélectionner une ou plusieurs catégories"
                  style="width: 100%"
                  multiple
                  filterable
                  clearable
                  collapse-tags
                  collapse-tags-tooltip
                >
                  <el-option
                    v-for="categorie in categories"
                    :key="categorie.id"
                    :label="categorie.libelle"
                    :value="categorie.id"
                  />
                </el-select>
              </el-form-item>
            </transition>

            <transition name="fade-slide">
              <el-form-item
                v-if="primeCollectiveForm.typeCible === 'selection'"
                label="Personnels à cibler"
                prop="personnelIds"
                class="sub-select"
              >
                <el-select
                  v-model="primeCollectiveForm.personnelIds"
                  placeholder="Sélectionner les personnels"
                  style="width: 100%"
                  multiple
                  filterable
                  collapse-tags
                  collapse-tags-tooltip
                >
                  <el-option
                    v-for="personnel in personnels"
                    :key="personnel.id"
                    :label="`${personnel.nomComplet} - ${personnel.matricule}`"
                    :value="personnel.id"
                  >
                    <div class="option-row">
                      <span class="option-label">{{ personnel.nomComplet }}</span>
                      <span class="option-meta">{{ personnel.matricule }}</span>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>
            </transition>
          </div>

          <!-- ÉTAPE 3 : Commentaire -->
          <div class="step-section">
            <div class="step-header">
              <div class="step-number">3</div>
              <div class="step-title">
                <h3>Justification (optionnel)</h3>
                <span>Ajoutez un commentaire pour la traçabilité</span>
              </div>
            </div>
            <el-form-item prop="commentaire" class="no-label">
              <el-input
                v-model="primeCollectiveForm.commentaire"
                type="textarea"
                :rows="3"
                resize="none"
                placeholder="Ex : Prime de fin d'année, prime exceptionnelle de productivité..."
              />
            </el-form-item>
          </div>

          <!-- Résumé live -->
          <div class="prime-recap" v-if="primeCollectiveForm.typeElementId && primeCollectiveForm.montant > 0">
            <div class="recap-header">
              <el-icon><Coin /></el-icon>
              <span>Récapitulatif</span>
            </div>
            <div class="recap-grid">
              <div class="recap-card recap-people">
                <div class="recap-value">{{ getPersonnelsCibles().length }}</div>
                <div class="recap-label">Bénéficiaires</div>
              </div>
              <div class="recap-card recap-unit">
                <div class="recap-value">{{ formatCurrency(primeCollectiveForm.montant) }}</div>
                <div class="recap-label">Par personne</div>
              </div>
              <div class="recap-card recap-total">
                <div class="recap-value">
                  {{ formatCurrency(primeCollectiveForm.montant * getPersonnelsCibles().length) }}
                </div>
                <div class="recap-label">Montant total</div>
              </div>
            </div>
          </div>
        </el-form>
      </div>

      <!-- Footer custom -->
      <template #footer>
        <div class="prime-modal-footer">
          <el-button size="large" @click="closePrimeCollectiveModal" plain>
            Annuler
          </el-button>
          <el-button
            type="primary"
            size="large"
            class="btn-attribute"
            :loading="collectiveLoading"
            :disabled="getPersonnelsCibles().length === 0 || !primeCollectiveForm.typeElementId || !primeCollectiveForm.montant"
            @click="savePrimeCollective"
          >
            <el-icon><Trophy /></el-icon>
            Attribuer la prime
            <span v-if="getPersonnelsCibles().length > 0" class="btn-badge">
              {{ getPersonnelsCibles().length }}
            </span>
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Money, Plus, Close, Edit, View, Delete, Search, Present,
  User, UserFilled, Coin, Calendar, ChatLineRound, Trophy, OfficeBuilding, Select, CircleCheckFilled
} from '@element-plus/icons-vue'
import { primepersonnelrestService, type PrimePersonnelRestDto  } from '@/services/primepersonnelrest.service'
import { pretspersonnelsService } from '@/services/pretspersonnels.service'
import { rubriquerestService } from '@/services/rubriquerest.service'
interface Categorie {
  id: string
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
  categorieId?: number
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
const totalElements = ref(0)
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
  categorieIds: [] as string[],
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
  categorieIds: [{ type: 'array', required: true, min: 1, message: 'Veuillez sélectionner au moins une catégorie', trigger: 'change' }],
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

const personnels = ref<Personnel[]>([])
const personnelsLoading = ref(false)
const personnelSearchQuery = ref('')

const filteredPersonnels = computed(() => {
  if (!personnelSearchQuery.value) return personnels.value
  const query = personnelSearchQuery.value.toLowerCase()
  return personnels.value.filter(p =>
    p.nomComplet.toLowerCase().includes(query) ||
    p.matricule.toLowerCase().includes(query)
  )
})

const filterPersonnel = (query: string) => {
  personnelSearchQuery.value = query
}

const loadPersonnels = async () => {
  personnelsLoading.value = true
  try {
    const data = await pretspersonnelsService.getPersonnels()
    personnels.value = data.map((p: any) => ({
      id: p.id,
      matricule: p.matricule,
      nomComplet: p.nomComplet || `${p.prenom || ''} ${p.nom || ''}`.trim(),
      situationEmploie: p.situationEmploie || p.situationEmploi || ''
    }))
  } catch (error) {
    console.error('Erreur lors du chargement du personnel:', error)
    ElMessage.error('Erreur lors du chargement du personnel')
    personnels.value = []
  } finally {
    personnelsLoading.value = false
  }
}

// Catégories basées sur le champ situationEmploie du Personnel
const categories = ref<Categorie[]>([
  { id: 'Exécutant', libelle: 'Niveau 1 - Exécutant' },
  { id: 'Opérationnel', libelle: 'Niveau 2 - Opérationnel' },
  { id: 'Technique', libelle: 'Niveau 3 - Technique' },
  { id: 'Manager', libelle: 'Niveau 4 - Manager' },
  { id: 'Directeur', libelle: 'Niveau 5 - Directeur' }
])

const typesElements = ref<TypeElement[]>([])
const typesElementsLoading = ref(false)

const loadTypesElements = async () => {
  typesElementsLoading.value = true
  try {
    const response = await rubriquerestService.getAll({ size: 1000, page: 0 })
    if (response.success) {
      // Le backend peut retourner soit un array direct, soit { rows: [...] }
      const raw: any = response.data
      const list: any[] = Array.isArray(raw) ? raw : (raw?.rows || raw?.data || [])
      typesElements.value = list.map((r: any) => {
        const rawType = (r.typeRubrique || r.categorie || '').toString().toLowerCase()
        const type = rawType.includes('reten') || rawType.includes('charge') ? 'Retenue' : 'Gain'
        const modeCalcul = (r.modeCalcul || '').toString().toLowerCase()
        // Si le mode de calcul implique des heures/jours/quantité, afficher le champ quantité
        const quantite = modeCalcul.includes('heure') || modeCalcul.includes('jour') || modeCalcul.includes('quant')
        return {
          id: r.id,
          libelle: r.libelle || '',
          code: r.code || '',
          type,
          quantite
        }
      })
    } else {
      ElMessage.error(response.message || 'Erreur lors du chargement des rubriques')
    }
  } catch (error) {
    console.error('Erreur lors du chargement des rubriques:', error)
    ElMessage.error('Erreur lors du chargement des rubriques')
  } finally {
    typesElementsLoading.value = false
  }
}

const periodes = ref<Array<{ value: string; label: string }>>([])
const periodesLoading = ref(false)

const loadPeriodes = async () => {
  periodesLoading.value = true
  try {
    const data = await pretspersonnelsService.getPeriodesPaie()
    periodes.value = data.map((p: any) => ({
      value: String(p.id),
      label: p.affiche || p.libelle || p.periode || `Période ${p.id}`
    }))
  } catch (error) {
    console.error('Erreur lors du chargement des périodes:', error)
    ElMessage.error('Erreur lors du chargement des périodes de paie')
    periodes.value = []
  } finally {
    periodesLoading.value = false
  }
}

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
  console.group('[getPersonnelsCibles] Calcul des cibles')
  console.log('typeCible =', primeCollectiveForm.typeCible)
  console.log('categorieIds (raw) =', JSON.parse(JSON.stringify(primeCollectiveForm.categorieIds)))
  console.log('personnelIds =', JSON.parse(JSON.stringify(primeCollectiveForm.personnelIds)))
  console.log('personnels.length =', personnels.value.length)
  if (personnels.value.length > 0) {
    console.log('Echantillon personnel[0] =', JSON.parse(JSON.stringify(personnels.value[0])))
    const situations = Array.from(new Set(personnels.value.map((p: any) => p.situationEmploie || p.situationEmploi || p.situation || '(vide)')))
    console.log('Situations distinctes en BD =', situations)
  }

  let result: any[] = []
  if (primeCollectiveForm.typeCible === 'tout_le_monde') {
    result = personnels.value
  } else if (primeCollectiveForm.typeCible === 'categorie') {
    const selected = primeCollectiveForm.categorieIds.map((c: any) => String(c).toLowerCase().trim())
    console.log('Catégories sélectionnées (normalisées) =', selected)
    if (selected.length === 0) {
      console.warn('Aucune catégorie sélectionnée → retour []')
      console.groupEnd()
      return []
    }
    result = personnels.value.filter((p: any) => {
      const situation = String(p.situationEmploie || p.situationEmploi || p.situation || '').toLowerCase().trim()
      const match = selected.includes(situation)
      console.log(`  - ${p.nom || ''} ${p.prenom || ''} (id=${p.id}) situation="${situation}" → ${match ? 'MATCH' : 'skip'}`)
      return match
    })
  } else if (primeCollectiveForm.typeCible === 'selection') {
    result = personnels.value.filter(p =>
      primeCollectiveForm.personnelIds.includes(p.id)
    )
  }
  console.log('=> Total ciblés =', result.length)
  console.groupEnd()
  return result
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
    categorieIds: [],
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

    const idPeriode = primeCollectiveForm.periode ? Number(primeCollectiveForm.periode) : undefined
    const primeType = typesElements.value.find(t => t.id === primeCollectiveForm.typeElementId)

    // ✅ UN SEUL appel API batch côté backend → 1 seule transaction saveAll
    const res = await primepersonnelrestService.savePrimeCollectiveBatch({
      idPrime: primeCollectiveForm.typeElementId,
      montantop: Math.abs(primeCollectiveForm.montant),
      valeurop: 1,
      idPeriode,
      idPersonnels: personnelsCibles.map(p => p.id),
      commentaire: primeCollectiveForm.commentaire
    })

    if (res.success) {
      const successCount = res.total || personnelsCibles.length
      ElMessage.success(
        res.message || `Prime "${primeType?.libelle || ''}" attribuée à ${successCount} personne(s)`
      )
      await reloadElements()
      closePrimeCollectiveModal()
    } else {
      ElMessage.error(res.message || 'Échec de l\'enregistrement de la prime collective')
    }
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

    // Appel API réel pour chaque élément
    const idPeriode = elementForm.periode ? Number(elementForm.periode) : undefined
    const results = await Promise.allSettled(
      elementForm.elements.map(el =>
        primepersonnelrestService.savePrime({
          // En mode édition, transmettre l'id pour faire un UPDATE
          id: editingElement.value ? (elementForm.id || el.id) : undefined,
          montantop: Math.abs(el.montant),
          valeurop: el.quantite || 0,
          idPeriode,
          idPersonnel: elementForm.personnelId,
          idCtrat: elementForm.personnelId, // À remplacer par l'ID contrat réel si disponible
          ctrat: elementForm.personnelId,
          idAbsence: el.typeElementId,
          idPrime: el.typeElementId
        })
      )
    )

    const failures = results
      .map((r, i) => ({ r, i }))
      .filter(({ r }) => r.status === 'rejected' || (r.status === 'fulfilled' && !r.value.success))

    if (failures.length > 0) {
      const messages = failures.map(({ r }) =>
        r.status === 'fulfilled' ? r.value.message : 'Erreur réseau'
      ).filter(Boolean)
      ElMessage.error(`${failures.length} élément(s) en échec: ${messages.join(', ')}`)
    }

    const successCount = results.length - failures.length
    if (successCount > 0) {
      const verb = editingElement.value ? 'modifié' : 'enregistré'
      ElMessage.success(`${successCount} élément(s) ${verb}(s) avec succès`)
      await reloadElements()
      closeModal()
    }
  } catch (error) {
    console.error('Erreur lors de la sauvegarde:', error)
  } finally {
    formLoading.value = false
  }
}

const editElement = (element: ElementPaie) => {
  // Réinitialiser le formulaire d'abord
  Object.assign(elementForm, {
    id: element.id,
    personnelId: element.personnelId,
    periode: element.periode,
    statut: element.statut || 'actif',
    elements: [
      {
        id: element.id,
        typeElementId: element.typeElementId,
        quantite: element.quantite || 0,
        montant: Math.abs(element.montant) || 0,
        commentaire: element.commentaire || ''
      }
    ]
  })
  editingElement.value = true
  showAddModal.value = true
}

const reloadElements = async () => {
  loading.value = true
  try {
    const response = await primepersonnelrestService.getAll({
      page: currentPage.value,
      size: pageSize.value,
      search: searchQuery.value
    })
    if (response.success) {
      elements.value = response.data as any
      totalElements.value = response.total || 0
    }
  } finally {
    loading.value = false
  }
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
  } catch {
    return // L'utilisateur a annulé
  }

  try {
    loading.value = true
    const response = await primepersonnelrestService.deletePrime(element.id)
    if (response.success) {
      ElMessage.success('Élément supprimé avec succès')
      await reloadElements()
    } else {
      ElMessage.error(response.message || 'Erreur lors de la suppression')
    }
  } catch (error: any) {
    console.error('Erreur lors de la suppression:', error)
    ElMessage.error('Erreur lors de la suppression')
  } finally {
    loading.value = false
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
  reloadElements()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  reloadElements()
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

onMounted(async () => {
  loading.value = true
  // Charger le personnel, les périodes et les rubriques en parallèle
  loadPersonnels()
  loadPeriodes()
  loadTypesElements()
  try {
    const response = await primepersonnelrestService.getAll({
      page: currentPage.value,
      size: pageSize.value,
      search: searchQuery.value
    })

    if (response.success) {
      elements.value = response.data
      totalElements.value = response.total || 0
    } else {
      ElMessage.error(response.message)
    }
  } catch (e) {
    ElMessage.error('Erreur de chargement')
  } finally {
    loading.value = false
  }
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

/* ========= MODAL PRIME COLLECTIVE — DESIGN MODERNE ========= */
.prime-modal :deep(.el-dialog) {
  border-radius: 18px;
  overflow: hidden;
  padding: 0;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.25), 0 0 0 1px rgba(255, 255, 255, 0.05);
  background: #f8fafc;
}

.prime-modal :deep(.el-dialog__header) {
  padding: 0;
  margin: 0;
}

.prime-modal :deep(.el-dialog__body) {
  padding: 0;
}

.prime-modal :deep(.el-dialog__footer) {
  padding: 0;
  border-top: 1px solid #e5e7eb;
  background: #ffffff;
}

/* HEADER avec gradient */
.prime-modal-header {
  position: relative;
  display: flex;
  align-items: center;
  gap: 18px;
  padding: 24px 28px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  overflow: hidden;
}

.prime-modal-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.15) 0%, transparent 70%);
  pointer-events: none;
}

.header-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  flex-shrink: 0;
}

.header-text {
  flex: 1;
  position: relative;
  z-index: 1;
}

.header-title {
  margin: 0 0 4px 0;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: -0.3px;
}

.header-subtitle {
  margin: 0;
  font-size: 13px;
  opacity: 0.9;
  font-weight: 400;
}

.header-close {
  color: white !important;
  border: none !important;
  background: rgba(255, 255, 255, 0.1) !important;
  border-radius: 10px;
  padding: 8px;
  height: auto;
  position: relative;
  z-index: 1;
  transition: all 0.2s;
}

.header-close:hover {
  background: rgba(255, 255, 255, 0.25) !important;
  transform: rotate(90deg);
}

/* BODY */
.prime-modal-body {
  padding: 28px;
  max-height: 65vh;
  overflow-y: auto;
}

.prime-modal-body::-webkit-scrollbar {
  width: 8px;
}

.prime-modal-body::-webkit-scrollbar-track {
  background: transparent;
}

.prime-modal-body::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

/* SECTIONS / STEPS */
.step-section {
  background: white;
  border-radius: 14px;
  padding: 22px 24px;
  margin-bottom: 18px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  transition: box-shadow 0.2s;
}

.step-section:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.step-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 18px;
}

.step-number {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 15px;
  flex-shrink: 0;
  box-shadow: 0 4px 10px rgba(102, 126, 234, 0.3);
}

.step-title h3 {
  margin: 0 0 2px 0;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.step-title span {
  font-size: 12px;
  color: #6b7280;
}

/* GRID INPUTS */
.step-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 18px;
}

.step-grid .span-2 {
  grid-column: span 2;
}

.prime-modal-body :deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
  font-size: 13px;
  padding-bottom: 6px;
}

.prime-modal-body :deep(.el-input__wrapper),
.prime-modal-body :deep(.el-textarea__inner) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e5e7eb inset;
  transition: all 0.2s;
}

.prime-modal-body :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c7d2fe inset;
}

.prime-modal-body :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px #667eea inset;
}

/* OPTIONS DANS SELECT */
.option-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.option-label {
  font-weight: 500;
}

.option-meta {
  font-size: 12px;
  color: #9ca3af;
}

/* TARGET CARDS */
.target-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
}

.target-card {
  position: relative;
  padding: 20px 16px;
  border-radius: 12px;
  border: 2px solid #e5e7eb;
  background: #fafafa;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  text-align: center;
  overflow: hidden;
}

.target-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  opacity: 0;
  transition: opacity 0.25s;
  z-index: 0;
}

.target-card > * {
  position: relative;
  z-index: 1;
}

.target-card:hover {
  border-color: #c7d2fe;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.15);
}

.target-card.active {
  border-color: #667eea;
  background: white;
  box-shadow: 0 10px 25px rgba(102, 126, 234, 0.25);
}

.target-card.active::before {
  opacity: 0.04;
}

.target-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto 10px;
  border-radius: 12px;
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  transition: all 0.25s;
}

.target-card.active .target-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transform: scale(1.05);
  box-shadow: 0 6px 14px rgba(102, 126, 234, 0.4);
}

.target-name {
  font-weight: 600;
  font-size: 14px;
  color: #1f2937;
  margin-bottom: 4px;
}

.target-desc {
  font-size: 11.5px;
  color: #6b7280;
  line-height: 1.4;
}

.target-check {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 20px;
  color: #667eea;
  opacity: 0;
  transform: scale(0.5);
  transition: all 0.25s;
}

.target-card.active .target-check {
  opacity: 1;
  transform: scale(1);
}

/* SUB-SELECT (after target cards) */
.sub-select {
  margin-top: 18px;
  padding-top: 18px;
  border-top: 1px dashed #e5e7eb;
}

.sub-select :deep(.el-form-item__label) {
  color: #667eea !important;
  font-weight: 600;
}

/* No-label form item */
.no-label :deep(.el-form-item__label) {
  display: none;
}

/* TRANSITIONS */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
  max-height: 0;
}

/* RECAP */
.prime-recap {
  background: linear-gradient(135deg, #f0f4ff 0%, #faf5ff 100%);
  border-radius: 14px;
  padding: 20px 24px;
  border: 1px solid #e0e7ff;
  margin-top: 4px;
}

.recap-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #4338ca;
  margin-bottom: 14px;
  font-size: 14px;
}

.recap-header .el-icon {
  font-size: 18px;
}

.recap-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.recap-card {
  background: white;
  border-radius: 10px;
  padding: 14px 12px;
  text-align: center;
  border: 1px solid #e5e7eb;
  transition: all 0.2s;
}

.recap-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.06);
}

.recap-value {
  font-size: 18px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 4px;
  letter-spacing: -0.3px;
}

.recap-total .recap-value {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 20px;
}

.recap-label {
  font-size: 11px;
  color: #6b7280;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-weight: 500;
}

/* FOOTER */
.prime-modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 18px 28px;
}

.btn-attribute {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  border: none !important;
  font-weight: 600 !important;
  padding: 0 24px !important;
  height: 44px !important;
  border-radius: 12px !important;
  display: inline-flex !important;
  align-items: center !important;
  gap: 8px;
  box-shadow: 0 6px 18px rgba(102, 126, 234, 0.4);
  transition: all 0.2s;
}

.btn-attribute:hover:not(.is-disabled) {
  transform: translateY(-1px);
  box-shadow: 0 8px 22px rgba(102, 126, 234, 0.55);
}

.btn-attribute.is-disabled {
  background: #d1d5db !important;
  box-shadow: none;
}

.btn-badge {
  background: rgba(255, 255, 255, 0.25);
  padding: 2px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 700;
  margin-left: 4px;
}

/* RESPONSIVE */
@media (max-width: 768px) {
  .step-grid,
  .target-cards,
  .recap-grid {
    grid-template-columns: 1fr;
  }
  .step-grid .span-2 {
    grid-column: span 1;
  }
}

.enhanced-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

/* ========= MODAL DÉTAILS — DESIGN MODERNE ========= */
.details-modal :deep(.el-dialog) {
  border-radius: 18px;
  overflow: hidden;
  padding: 0;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.25), 0 0 0 1px rgba(255, 255, 255, 0.05);
  background: #f8fafc;
}

.details-modal :deep(.el-dialog__header) { padding: 0; margin: 0; }
.details-modal :deep(.el-dialog__body) { padding: 0; }
.details-modal :deep(.el-dialog__footer) {
  padding: 0;
  border-top: 1px solid #e5e7eb;
  background: #ffffff;
}

.details-modal-header {
  position: relative;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 22px 26px;
  color: white;
  overflow: hidden;
}

.details-modal-header.is-gain {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}
.details-modal-header.is-retenue {
  background: linear-gradient(135deg, #ef4444 0%, #b91c1c 100%);
}

.details-modal-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 280px;
  height: 280px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.18) 0%, transparent 70%);
  pointer-events: none;
}

.details-modal-header .header-icon {
  width: 50px;
  height: 50px;
  border-radius: 13px;
  background: rgba(255, 255, 255, 0.22);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.details-modal-header .header-text { flex: 1; position: relative; z-index: 1; }
.details-modal-header .header-title {
  margin: 0 0 3px 0; font-size: 18px; font-weight: 700; letter-spacing: -0.2px;
}
.details-modal-header .header-subtitle {
  margin: 0; font-size: 13px; opacity: 0.92; font-weight: 400;
}

.details-modal-header .header-close {
  color: white !important;
  border: none !important;
  background: rgba(255, 255, 255, 0.12) !important;
  border-radius: 10px;
  padding: 8px;
  height: auto;
  position: relative;
  z-index: 1;
  transition: all 0.2s;
}
.details-modal-header .header-close:hover {
  background: rgba(255, 255, 255, 0.28) !important;
  transform: rotate(90deg);
}

.details-body {
  padding: 24px 26px;
  max-height: 65vh;
  overflow-y: auto;
}

.amount-hero {
  border-radius: 14px;
  padding: 22px 24px;
  margin-bottom: 20px;
  text-align: center;
  position: relative;
  overflow: hidden;
}
.amount-hero.is-gain {
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
  border: 1px solid #a7f3d0;
}
.amount-hero.is-retenue {
  background: linear-gradient(135deg, #fef2f2 0%, #fee2e2 100%);
  border: 1px solid #fecaca;
}

.amount-hero .amount-label {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  font-weight: 600;
  opacity: 0.7;
}

.amount-hero.is-gain .amount-label { color: #047857; }
.amount-hero.is-retenue .amount-label { color: #b91c1c; }

.amount-hero .amount-value {
  font-size: 34px;
  font-weight: 800;
  margin: 6px 0 12px;
  letter-spacing: -1px;
  line-height: 1;
}
.amount-hero.is-gain .amount-value {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.amount-hero.is-retenue .amount-value {
  background: linear-gradient(135deg, #ef4444 0%, #b91c1c 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.amount-hero .amount-tags {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.details-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 18px;
}

.detail-item {
  display: flex;
  gap: 12px;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 14px 16px;
  transition: all 0.2s;
}
.detail-item:hover {
  border-color: #c7d2fe;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transform: translateY(-1px);
}

.detail-item .detail-icon {
  width: 36px; height: 36px;
  border-radius: 10px;
  background: linear-gradient(135deg, #eef2ff 0%, #e0e7ff 100%);
  color: #6366f1;
  display: flex; align-items: center; justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}

.detail-item .detail-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
}

.detail-item .detail-label {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: #6b7280;
  font-weight: 600;
}
.detail-item .detail-value {
  font-size: 14px;
  font-weight: 600;
  color: #1f2937;
  word-break: break-word;
}
.detail-item .detail-meta {
  font-size: 11.5px;
  color: #9ca3af;
}

.comment-block {
  background: linear-gradient(135deg, #fffbeb 0%, #fef3c7 100%);
  border: 1px solid #fde68a;
  border-radius: 12px;
  padding: 14px 18px;
}
.comment-block .comment-header {
  display: flex; align-items: center; gap: 8px;
  font-weight: 600; color: #92400e;
  font-size: 13px;
  margin-bottom: 6px;
}
.comment-block .comment-text {
  margin: 0; color: #78350f; font-size: 13.5px; line-height: 1.5;
}

.details-modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 26px;
}

.btn-edit-details {
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%) !important;
  border: none !important;
  font-weight: 600 !important;
  padding: 0 22px !important;
  height: 42px !important;
  border-radius: 11px !important;
  display: inline-flex !important;
  align-items: center !important;
  gap: 8px;
  box-shadow: 0 6px 16px rgba(99, 102, 241, 0.4);
  transition: all 0.2s;
}
.btn-edit-details:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.55);
}

@media (max-width: 640px) {
  .details-grid { grid-template-columns: 1fr; }
}

/* ========= SIDEBAR PANEL HEADER MODERNISÉ ========= */
.modern-panel-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  color: white;
  padding: 18px 22px !important;
  border-bottom: none !important;
  position: relative;
  overflow: hidden;
}
.modern-panel-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.15) 0%, transparent 70%);
  pointer-events: none;
}
.modern-panel-header .panel-header-left {
  display: flex;
  align-items: center;
  gap: 14px;
  position: relative;
  z-index: 1;
}
.modern-panel-header .panel-header-icon {
  width: 42px; height: 42px;
  border-radius: 11px;
  background: rgba(255, 255, 255, 0.22);
  backdrop-filter: blur(10px);
  display: flex; align-items: center; justify-content: center;
  font-size: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
.modern-panel-header .panel-header-text h3 {
  margin: 0 0 2px 0 !important;
  color: white !important;
  font-size: 16px !important;
  font-weight: 700 !important;
  letter-spacing: -0.2px;
}
.modern-panel-header .panel-header-text span {
  font-size: 12px;
  opacity: 0.9;
}
.modern-panel-header .panel-close-btn {
  background: rgba(255, 255, 255, 0.15) !important;
  border: none !important;
  color: white !important;
  position: relative;
  z-index: 1;
  transition: all 0.2s;
}
.modern-panel-header .panel-close-btn:hover {
  background: rgba(255, 255, 255, 0.3) !important;
  transform: rotate(90deg);
}
</style>
