<template>
  <div class="livre-paie-view">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon><Document /></el-icon>
            Livre de Paie
          </h1>
          <p class="page-subtitle">Gestion et génération des bulletins de paie</p>
        </div>
        <div class="header-right">
          <el-button type="success" @click="showGenerateModal = true" class="enhanced-button">
            <el-icon><Plus /></el-icon>
            Générer Livre de Paie
          </el-button>
          <el-button type="primary" @click="showPrintModal = true" class="enhanced-button">
            <el-icon><Printer /></el-icon>
            Imprimer Bulletins
          </el-button>
          <el-button type="warning" @click="showTransferModal = true" class="enhanced-button">
            <el-icon><CreditCard /></el-icon>
            Ordre de Virement
          </el-button>
          <el-button type="info" @click="regularizeNet" class="enhanced-button">
            <el-icon><Refresh /></el-icon>
            Net à Payer Régul
          </el-button>
        </div>
      </div>
    </div>

    <!-- Filtres et contrôles -->
    <div class="filters-section enhanced-card">
      <div class="filters-header">
        <h3>Période de Paie</h3>
        <div class="period-info">
          <el-tag type="primary" size="large">{{ selectedPeriod }}</el-tag>
        </div>
      </div>
      <div class="filters-content">
        <el-form :model="filters" inline>
          <el-form-item label="Période">
            <el-select v-model="filters.period" placeholder="Sélectionner la période" style="width: 200px" @change="changePeriod">
              <el-option
                v-for="period in periods"
                :key="period.value"
                :label="period.label"
                :value="period.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="Statut">
            <el-select v-model="filters.status" placeholder="Filtrer par statut" style="width: 150px" clearable>
              <el-option label="Actif" value="actif" />
              <el-option label="En sommeil" value="sommeil" />
            </el-select>
          </el-form-item>
          <el-form-item label="Recherche">
            <el-input
              v-model="filters.search"
              placeholder="Rechercher un personnel..."
              prefix-icon="Search"
              style="width: 250px"
              clearable
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadPersonnel">
              <el-icon><Search /></el-icon>
              Rechercher
            </el-button>
            <el-button @click="exportExcel">
              <el-icon><Download /></el-icon>
              Exporter Excel
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- Tabulation principale -->
    <div class="main-tabs-section enhanced-card">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="Liste Personnel" name="personnel">
          <!-- Tableau principal -->
          <div class="table-container">
            <el-table 
              :data="paginatedPersonnel" 
              stripe 
              v-loading="loading"
              @selection-change="handleSelectionChange"
              @sort-change="handleSortChange"
            >
          <el-table-column type="selection" width="55" />
          <el-table-column label="Matricule" prop="matricule" width="120" sortable="custom" />
          
          <el-table-column label="Nom & Prénoms" prop="nomComplet" min-width="200" sortable="custom">
            <template #default="{ row }">
              <div class="personnel-info">
                <div class="personnel-name">{{ row.nomComplet }}</div>
                <el-tag :type="getStatusColor(row.statut)" size="small">
                  {{ getStatusLabel(row.statut) }}
                </el-tag>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="Sexe" prop="sexe" width="80" align="center">
            <template #default="{ row }">
              <el-icon v-if="row.sexe === 'M'" color="#409EFF"><Male /></el-icon>
              <el-icon v-else color="#F56C6C"><Female /></el-icon>
            </template>
          </el-table-column>

          <el-table-column label="N° Compte/Tél" prop="compte" width="150" align="center" />

          <el-table-column label="Fonction/Emploi" prop="fonction" min-width="180" />

          <el-table-column label="Sit. Matri" prop="situationMatrimoniale" width="120" align="center" />

          <el-table-column label="Nb Enfants" prop="nbEnfants" width="100" align="right" sortable="custom" />

          <el-table-column label="Salaire Catégoriel" prop="salaireCategoriel" width="150" align="right" sortable="custom">
            <template #default="{ row }">
              {{ formatCurrency(row.salaireCategoriel) }}
            </template>
          </el-table-column>

          <el-table-column label="Net à Payer" prop="netAPayer" width="150" align="right" sortable="custom">
            <template #default="{ row }">
              <span class="net-amount">{{ formatCurrency(row.netAPayer) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="En Sommeil" prop="enSommeil" width="120" align="center">
            <template #default="{ row }">
              <el-tag :type="row.enSommeil ? 'danger' : 'success'" size="small">
                {{ row.enSommeil ? 'Oui' : 'Non' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="Actions" width="120" fixed="right">
            <template #default="{ row }">
              <el-button-group>
                <el-button type="primary" size="small" @click="viewDetails(row)">
                  <el-icon><View /></el-icon>
                </el-button>
                <el-button type="warning" size="small" @click="editPersonnel(row)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-dropdown trigger="click">
                  <el-button type="info" size="small">
                    <el-icon><MoreFilled /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="editWorkTime(row)">
                        <el-icon><Clock /></el-icon>
                        Temps de travail
                      </el-dropdown-item>
                      <el-dropdown-item @click="reverseCalculate(row)">
                        <el-icon><Operation /></el-icon>
                        Calcul à l'envers
                      </el-dropdown-item>
                      <el-dropdown-item divided @click="deletePersonnel(row)">
                        <el-icon><Delete /></el-icon>
                        Supprimer
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>

          <!-- Pagination -->
          <div class="pagination-container">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[20, 50, 100, 200, 500]"
              :total="filteredPersonnel.length"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="Livre de Paie" name="livre-paie" :disabled="!livrePaieGenerated">
        <div class="livre-paie-content">
          <div class="livre-paie-header">
            <h4>Livre de Paie - {{ selectedPeriod }}</h4>
            <div class="livre-paie-stats">
              <el-tag>{{ livrePaieData.length }} bulletin(s)</el-tag>
              <el-tag type="success">{{ totalLivreNet.toLocaleString() }} XOF Total Net</el-tag>
              <el-button type="success" @click="exportLivreExcel">
                <el-icon><Download /></el-icon>
                Exporter Excel
              </el-button>
            </div>
          </div>
          <div class="livre-paie-table-container">
            <el-table :data="livrePaieData" stripe max-height="600">
              <el-table-column label="Matricule" prop="matricule" width="120" />
              <el-table-column label="Nom & Prénoms" prop="nomComplet" min-width="200" />
              <el-table-column label="Nb Jours Trv" prop="nbJoursTravail" width="120" align="center" />
              <el-table-column label="Nb Parts" prop="nombrePart" width="100" align="right" />
              <el-table-column label="Ancienneté" prop="anciennete" width="120" align="center" />
              <el-table-column label="Salaire Base" prop="salaireBase" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.salaireBase) }}
                </template>
              </el-table-column>
              <el-table-column label="Sursalaire" prop="sursalaire" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.sursalaire) }}
                </template>
              </el-table-column>
              <el-table-column label="Prime Ancienneté" prop="primeAnciennete" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.primeAnciennete) }}
                </template>
              </el-table-column>
              <el-table-column label="Indem Logement" prop="indemniteLogement" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.indemniteLogement) }}
                </template>
              </el-table-column>
              <el-table-column label="Transp Imp" prop="indemniteTransportImp" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.indemniteTransportImp) }}
                </template>
              </el-table-column>
              <el-table-column label="Indem Imp" prop="autreIndemImposable" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.autreIndemImposable) }}
                </template>
              </el-table-column>
              <el-table-column label="Brut Imposable" prop="brutImposable" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.brutImposable) }}
                </template>
              </el-table-column>
              <el-table-column label="Indem Repres" prop="indemniteRepresentation" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.indemniteRepresentation) }}
                </template>
              </el-table-column>
              <el-table-column label="Indem Transport" prop="indemniteTransport" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.indemniteTransport) }}
                </template>
              </el-table-column>
              <el-table-column label="Brut Non Imp" prop="brutNonImposable" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.brutNonImposable) }}
                </template>
              </el-table-column>
              <el-table-column label="ITS" prop="its" width="100" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.its) }}
                </template>
              </el-table-column>
              <el-table-column label="Retenue Fiscale" prop="retenueFiscale" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.retenueFiscale) }}
                </template>
              </el-table-column>
              <el-table-column label="Base CNPS" prop="baseCnps" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.baseCnps) }}
                </template>
              </el-table-column>
              <el-table-column label="CNPS" prop="cnps" width="100" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.cnps) }}
                </template>
              </el-table-column>
              <el-table-column label="Total Retenue" prop="totalRetenue" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.totalRetenue) }}
                </template>
              </el-table-column>
              <el-table-column label="Net à Payer" prop="netAPayer" width="140" align="right">
                <template #default="{ row }">
                  <span class="net-amount">{{ formatCurrency(row.netAPayer) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="Total Brut" prop="totalBrut" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.totalBrut) }}
                </template>
              </el-table-column>
              <el-table-column label="Total Patronal" prop="totalPatronal" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.totalPatronal) }}
                </template>
              </el-table-column>
              <el-table-column label="Total Masse Salariale" prop="totalMasseSalariale" width="180" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.totalMasseSalariale) }}
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="Bulletins de Paie" name="bulletins" :disabled="!bulletinsGenerated">
        <div class="bulletins-content">
          <div class="bulletins-header">
            <h4>Bulletins de Paie - {{ selectedPeriod }}</h4>
            <div class="bulletins-stats">
              <el-tag>{{ bulletinsData.length }} bulletin(s)</el-tag>
              <el-tag type="success">{{ totalBulletinsNet.toLocaleString() }} XOF Total Net</el-tag>
              <el-button type="success" @click="exportBulletinsExcel">
                <el-icon><Download /></el-icon>
                Exporter Excel
              </el-button>
              <el-button type="primary" @click="printAllBulletins">
                <el-icon><Printer /></el-icon>
                Imprimer Tous
              </el-button>
            </div>
          </div>
          <div class="bulletins-table-container">
            <el-table :data="bulletinsData" stripe max-height="600">
              <el-table-column label="Matricule" prop="matricule" width="120" />
              <el-table-column label="Nom & Prénoms" prop="nomComplet" min-width="200" />
              <el-table-column label="Statut" prop="statut" width="100">
                <template #default="{ row }">
                  <el-tag :type="getStatusColor(row.statut)" size="small">
                    {{ getStatusLabel(row.statut) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="Nb Parts" prop="nombrePart" width="100" align="right" />
              <el-table-column label="Ancienneté" prop="anciennete" width="120" align="center" />
              <el-table-column label="Salaire Base" prop="salaireBase" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.salaireBase) }}
                </template>
              </el-table-column>
              <el-table-column label="Sursalaire" prop="sursalaire" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.sursalaire) }}
                </template>
              </el-table-column>
              <el-table-column label="Prime Ancienneté" prop="primeAnciennete" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.primeAnciennete) }}
                </template>
              </el-table-column>
              <el-table-column label="Indem Logement" prop="indemniteLogement" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.indemniteLogement) }}
                </template>
              </el-table-column>
              <el-table-column label="Brut Imposable" prop="brutImposable" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.brutImposable) }}
                </template>
              </el-table-column>
              <el-table-column label="ITS" prop="its" width="100" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.its) }}
                </template>
              </el-table-column>
              <el-table-column label="Retenue Fiscale" prop="retenueFiscale" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.retenueFiscale) }}
                </template>
              </el-table-column>
              <el-table-column label="CNPS" prop="cnps" width="100" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.cnps) }}
                </template>
              </el-table-column>
              <el-table-column label="Total Retenue" prop="totalRetenue" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.totalRetenue) }}
                </template>
              </el-table-column>
              <el-table-column label="Net à Payer" prop="netAPayer" width="140" align="right">
                <template #default="{ row }">
                  <span class="net-amount">{{ formatCurrency(row.netAPayer) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="Total Brut" prop="totalBrut" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.totalBrut) }}
                </template>
              </el-table-column>
              <el-table-column label="Total Patronal" prop="totalPatronal" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.totalPatronal) }}
                </template>
              </el-table-column>
              <el-table-column label="Actions" width="100" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" size="small" @click="printBulletin(row)">
                    <el-icon><Printer /></el-icon>
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>

    <!-- Modal de génération -->
    <el-dialog v-model="showGenerateModal" title="Génération du Bulletin de Paie" width="500px">
      <div class="generate-modal-content">
        <el-alert
          :title="`Vous êtes sur le point de générer le bulletin de paie de la période de ${selectedPeriod}`"
          type="warning"
          :closable="false"
          show-icon
        />
        <div class="modal-info">
          <p>En cliquant sur le bouton "Valider", le processus sera lancé.</p>
          <div class="process-info">
            <el-icon><Loading /></el-icon>
            <span>Le processus peut prendre plusieurs minutes selon le nombre de personnels.</span>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="showGenerateModal = false">Annuler</el-button>
        <el-button type="success" @click="generateBulletins" :loading="generating">
          Valider
        </el-button>
      </template>
    </el-dialog>

    <!-- Modal d'impression -->
    <el-dialog v-model="showPrintModal" title="Imprimer les Bulletins de Paie" width="90%">
      <div class="print-modal-content">
        <div class="print-header">
          <h4>Bulletins de paie de {{ selectedPeriod }}</h4>
          <el-button type="success" @click="exportBulletinsExcel">
            <el-icon><Download /></el-icon>
            Exporter en Excel
          </el-button>
        </div>
        <div class="print-table-container">
          <el-table :data="bulletinsData" stripe max-height="500">
            <el-table-column label="Matricule" prop="matricule" width="120" />
            <el-table-column label="Nom & Prénoms" prop="nomComplet" min-width="200" />
            <el-table-column label="Statut" prop="statut" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusColor(row.statut)" size="small">
                  {{ getStatusLabel(row.statut) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="Nb Parts" prop="nombrePart" width="100" align="right" />
            <el-table-column label="Ancienneté" prop="anciennete" width="120" align="center" />
            <el-table-column label="Salaire Base" prop="salaireBase" width="120" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.salaireBase) }}
              </template>
            </el-table-column>
            <el-table-column label="Sursalaire" prop="sursalaire" width="120" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.sursalaire) }}
              </template>
            </el-table-column>
            <el-table-column label="Prime Ancienneté" prop="primeAnciennete" width="140" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.primeAnciennete) }}
              </template>
            </el-table-column>
            <el-table-column label="Indem Logement" prop="indemniteLogement" width="140" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.indemniteLogement) }}
              </template>
            </el-table-column>
            <el-table-column label="Brut Imposable" prop="brutImposable" width="140" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.brutImposable) }}
              </template>
            </el-table-column>
            <el-table-column label="ITS" prop="its" width="100" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.its) }}
              </template>
            </el-table-column>
            <el-table-column label="Retenue Fiscale" prop="retenueFiscale" width="140" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.retenueFiscale) }}
              </template>
            </el-table-column>
            <el-table-column label="CNPS" prop="cnps" width="100" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.cnps) }}
              </template>
            </el-table-column>
            <el-table-column label="Total Retenue" prop="totalRetenue" width="140" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.totalRetenue) }}
              </template>
            </el-table-column>
            <el-table-column label="Net à Payer" prop="netAPayer" width="140" align="right">
              <template #default="{ row }">
                <span class="net-amount">{{ formatCurrency(row.netAPayer) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="Total Brut" prop="totalBrut" width="120" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.totalBrut) }}
              </template>
            </el-table-column>
            <el-table-column label="Total Patronal" prop="totalPatronal" width="140" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.totalPatronal) }}
              </template>
            </el-table-column>
            <el-table-column label="Actions" width="100" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="printBulletin(row)">
                  <el-icon><Printer /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <template #footer>
        <el-button @click="showPrintModal = false">Fermer</el-button>
        <el-button type="success" @click="printAllBulletins">
          <el-icon><Printer /></el-icon>
          Imprimer Tous
        </el-button>
      </template>
    </el-dialog>

    <!-- Modal d'ordre de virement -->
    <el-dialog v-model="showTransferModal" title="Génération d'Ordre de Virement" width="600px">
      <div class="transfer-modal-content">
        <el-form :model="transferForm" label-width="180px">
          <el-form-item label="Compte Entreprise Banque">
            <el-select v-model="transferForm.compteEntreprise" placeholder="Sélectionner" style="width: 100%">
              <el-option
                v-for="bank in banks"
                :key="bank.id"
                :label="`${bank.sigle} (${bank.code})`"
                :value="bank.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="Banque">
            <el-select v-model="transferForm.banque" placeholder="Sélectionner" style="width: 100%">
              <el-option
                v-for="bank in banks"
                :key="bank.id"
                :label="`${bank.sigle} (${bank.code})`"
                :value="bank.id"
              />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="showTransferModal = false">Annuler</el-button>
        <el-button type="success" @click="generateTransferOrder">
          Valider
        </el-button>
      </template>
    </el-dialog>

    <!-- Modal de modification personnel -->
    <el-dialog v-model="showEditModal" :title="`Modification du personnel (${selectedPersonnel?.nomComplet})`" width="500px">
      <el-form :model="editForm" label-width="140px">
        <el-form-item label="Nombre d'enfants">
          <el-input-number v-model="editForm.nbEnfants" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="Situation matrimoniale">
          <el-select v-model="editForm.situationMatrimoniale" style="width: 100%">
            <el-option label="Marié(e)" value="marie" />
            <el-option label="Célibataire" value="celibataire" />
            <el-option label="Veuf/Veuve" value="veuf" />
          </el-select>
        </el-form-item>
        <el-form-item label="Statut">
          <el-radio-group v-model="editForm.statut">
            <el-radio value="actif">Actif</el-radio>
            <el-radio value="sommeil">En sommeil</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditModal = false">Annuler</el-button>
        <el-button type="success" @click="savePersonnelChanges">Valider</el-button>
      </template>
    </el-dialog>

    <!-- Modal modification temps de travail -->
    <el-dialog v-model="showWorkTimeModal" :title="`Modification du temps de travail (${selectedPersonnel?.nomComplet})`" width="500px">
      <el-form :model="workTimeForm" label-width="140px">
        <el-form-item label="Nombre de jours">
          <el-input-number v-model="workTimeForm.nbJours" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="Temps travail">
          <el-input-number v-model="workTimeForm.tempsTravail" :min="0" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showWorkTimeModal = false">Annuler</el-button>
        <el-button type="success" @click="saveWorkTimeChanges">Valider</el-button>
      </template>
    </el-dialog>

    <!-- Modal calcul à l'envers -->
    <el-dialog v-model="showReverseModal" :title="`Calcul à l'envers (${selectedPersonnel?.nomComplet})`" width="500px">
      <el-form :model="reverseForm" label-width="140px">
        <el-form-item label="Net à payer">
          <el-input-number v-model="reverseForm.netAPayer" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="Nouveau Sursalaire">
          <el-input-number v-model="reverseForm.nouveauSursalaire" :min="0" disabled style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showReverseModal = false">Annuler</el-button>
        <el-button type="info" @click="calculateReverse">Calculer</el-button>
        <el-button type="success" @click="saveReverseCalculation">Valider</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Document, Plus, Printer, CreditCard, Refresh, Search, Download,
  Male, Female, View, Edit, MoreFilled, Clock, Delete, Loading, Operation
} from '@element-plus/icons-vue'

interface Personnel {
  id: number
  matricule: string
  nomComplet: string
  statut: string
  sexe: string
  compte: string
  fonction: string
  situationMatrimoniale: string
  nbEnfants: number
  salaireCategoriel: number
  netAPayer: number
  enSommeil: boolean
}

interface Bulletin {
  id: number
  matricule: string
  nomComplet: string
  statut: string
  nombrePart: number
  anciennete: string
  salaireBase: number
  sursalaire: number
  primeAnciennete: number
  indemniteLogement: number
  brutImposable: number
  its: number
  retenueFiscale: number
  cnps: number
  totalRetenue: number
  netAPayer: number
  totalBrut: number
  totalPatronal: number
  nbJoursTravail: number
  indemniteTransportImp: number
  autreIndemImposable: number
  indemniteRepresentation: number
  indemniteTransport: number
  brutNonImposable: number
  baseCnps: number
  totalMasseSalariale: number
}

interface Bank {
  id: number
  sigle: string
  code: string
}

// Données réactives
const loading = ref(false)
const generating = ref(false)
const showGenerateModal = ref(false)
const showPrintModal = ref(false)
const showTransferModal = ref(false)
const showEditModal = ref(false)
const showWorkTimeModal = ref(false)
const showReverseModal = ref(false)
const selectedPersonnel = ref<Personnel | null>(null)
const selectedPersonnelList = ref<Personnel[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const sortBy = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')
const activeTab = ref('personnel')
const livrePaieGenerated = ref(false)
const bulletinsGenerated = ref(false)

const filters = reactive({
  period: '2024-01',
  status: '',
  search: ''
})

const editForm = reactive({
  nbEnfants: 0,
  situationMatrimoniale: '',
  statut: 'actif'
})

const workTimeForm = reactive({
  nbJours: 30,
  tempsTravail: 173.33
})

const reverseForm = reactive({
  netAPayer: 0,
  nouveauSursalaire: 0
})

const transferForm = reactive({
  compteEntreprise: '',
  banque: ''
})

// Données mockées
const personnelData = ref<Personnel[]>([
  {
    id: 1,
    matricule: 'EMP001',
    nomComplet: 'Kouadio Jean',
    statut: 'actif',
    sexe: 'M',
    compte: '0102030405',
    fonction: 'Développeur Senior',
    situationMatrimoniale: 'marie',
    nbEnfants: 2,
    salaireCategoriel: 500000,
    netAPayer: 425000,
    enSommeil: false
  },
  {
    id: 2,
    matricule: 'EMP002',
    nomComplet: 'Touré Aminata',
    statut: 'actif',
    sexe: 'F',
    compte: '0607080910',
    fonction: 'Comptable',
    situationMatrimoniale: 'celibataire',
    nbEnfants: 0,
    salaireCategoriel: 350000,
    netAPayer: 298000,
    enSommeil: false
  },
  {
    id: 3,
    matricule: 'EMP003',
    nomComplet: 'Soro Mohamed',
    statut: 'sommeil',
    sexe: 'M',
    compte: '1112131415',
    fonction: 'Agent Administratif',
    situationMatrimoniale: 'marie',
    nbEnfants: 3,
    salaireCategoriel: 250000,
    netAPayer: 212500,
    enSommeil: true
  }
])

const bulletinsData = ref<Bulletin[]>([
  {
    id: 1,
    matricule: 'EMP001',
    nomComplet: 'Kouadio Jean',
    statut: 'actif',
    nombrePart: 2,
    anciennete: '5 ans',
    salaireBase: 500000,
    sursalaire: 50000,
    primeAnciennete: 25000,
    indemniteLogement: 30000,
    brutImposable: 575000,
    its: 57500,
    retenueFiscale: 86000,
    cnps: 45000,
    totalRetenue: 188500,
    netAPayer: 425000,
    totalBrut: 605000,
    totalPatronal: 182500,
    nbJoursTravail: 30,
    indemniteTransportImp: 15000,
    autreIndemImposable: 5000,
    indemniteRepresentation: 10000,
    indemniteTransport: 20000,
    brutNonImposable: 35000,
    baseCnps: 550000,
    totalMasseSalariale: 787500
  }
])

const livrePaieData = ref<Bulletin[]>([
  {
    id: 1,
    matricule: 'EMP001',
    nomComplet: 'Kouadio Jean',
    statut: 'actif',
    nombrePart: 2,
    anciennete: '5 ans',
    salaireBase: 500000,
    sursalaire: 50000,
    primeAnciennete: 25000,
    indemniteLogement: 30000,
    brutImposable: 575000,
    its: 57500,
    retenueFiscale: 86000,
    cnps: 45000,
    totalRetenue: 188500,
    netAPayer: 425000,
    totalBrut: 605000,
    totalPatronal: 182500,
    nbJoursTravail: 30,
    indemniteTransportImp: 15000,
    autreIndemImposable: 5000,
    indemniteRepresentation: 10000,
    indemniteTransport: 20000,
    brutNonImposable: 35000,
    baseCnps: 550000,
    totalMasseSalariale: 787500
  }
])

const banks = ref<Bank[]>([
  { id: 1, sigle: 'SGBCI', code: 'SGCI' },
  { id: 2, sigle: 'ECOBANK', code: 'ECOC' },
  { id: 3, sigle: 'BIAO', code: 'BIAO' }
])

const periods = ref([
  { value: '2024-01', label: 'Janvier 2024' },
  { value: '2024-02', label: 'Février 2024' },
  { value: '2024-03', label: 'Mars 2024' },
  { value: '2024-04', label: 'Avril 2024' }
])

// Computed properties
const selectedPeriod = computed(() => {
  const period = periods.value.find(p => p.value === filters.period)
  return period?.label || filters.period
})

const filteredPersonnel = computed(() => {
  let filtered = personnelData.value

  if (filters.status) {
    filtered = filtered.filter(p => p.statut === filters.status)
  }

  if (filters.search) {
    const query = filters.search.toLowerCase()
    filtered = filtered.filter(p => 
      p.nomComplet.toLowerCase().includes(query) ||
      p.matricule.toLowerCase().includes(query) ||
      p.fonction.toLowerCase().includes(query)
    )
  }

  return filtered
})

const paginatedPersonnel = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredPersonnel.value.slice(start, end)
})

const totalNet = computed(() => {
  return filteredPersonnel.value.reduce((sum, p) => sum + p.netAPayer, 0)
})

const totalLivreNet = computed(() => {
  return livrePaieData.value.reduce((sum, p) => sum + p.netAPayer, 0)
})

const totalBulletinsNet = computed(() => {
  return bulletinsData.value.reduce((sum, p) => sum + p.netAPayer, 0)
})

// Méthodes
const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF' }).format(amount)
}

const getStatusColor = (status: string) => {
  return status === 'actif' ? 'success' : 'danger'
}

const getStatusLabel = (status: string) => {
  return status === 'actif' ? 'Actif' : 'En sommeil'
}

const handleSelectionChange = (selection: Personnel[]) => {
  selectedPersonnelList.value = selection
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

const handleTabChange = (tabName: string) => {
  activeTab.value = tabName
}

const changePeriod = () => {
  loadPersonnel()
}

const loadPersonnel = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('Données chargées avec succès')
  }, 1000)
}

const generateBulletins = async () => {
  generating.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 3000))
    
    // Générer les données du livre de paie
    livrePaieData.value = personnelData.value.map(personnel => ({
      id: personnel.id,
      matricule: personnel.matricule,
      nomComplet: personnel.nomComplet,
      statut: personnel.statut,
      nombrePart: personnel.nbEnfants + 1,
      anciennete: '5 ans',
      salaireBase: personnel.salaireCategoriel,
      sursalaire: personnel.salaireCategoriel * 0.1,
      primeAnciennete: personnel.salaireCategoriel * 0.05,
      indemniteLogement: 30000,
      brutImposable: personnel.salaireCategoriel * 1.15,
      its: personnel.salaireCategoriel * 0.115,
      retenueFiscale: personnel.salaireCategoriel * 0.15,
      cnps: personnel.salaireCategoriel * 0.09,
      totalRetenue: personnel.salaireCategoriel * 0.355,
      netAPayer: personnel.netAPayer,
      totalBrut: personnel.salaireCategoriel * 1.21,
      totalPatronal: personnel.salaireCategoriel * 0.365,
      nbJoursTravail: 30,
      indemniteTransportImp: 15000,
      autreIndemImposable: 5000,
      indemniteRepresentation: 10000,
      indemniteTransport: 20000,
      brutNonImposable: 35000,
      baseCnps: personnel.salaireCategoriel * 1.1,
      totalMasseSalariale: personnel.netAPayer + (personnel.salaireCategoriel * 0.365)
    }))
    
    // Générer les bulletins
    bulletinsData.value = livrePaieData.value.map(item => ({
      ...item
    }))
    
    livrePaieGenerated.value = true
    bulletinsGenerated.value = true
    
    ElMessage.success('Bulletins générés avec succès')
    showGenerateModal.value = false
    
    // Basculer vers l'onglet livre de paie
    activeTab.value = 'livre-paie'
  } catch (error) {
    ElMessage.error('Erreur lors de la génération')
  } finally {
    generating.value = false
  }
}

const exportExcel = () => {
  ElMessage.success('Export Excel en cours...')
}

const exportLivreExcel = () => {
  ElMessage.success('Export du livre de paie en Excel...')
}

const exportBulletinsExcel = () => {
  ElMessage.success('Export des bulletins en Excel...')
}

const viewDetails = (personnel: Personnel) => {
  ElMessage.info(`Détails de ${personnel.nomComplet}`)
}

const editPersonnel = (personnel: Personnel) => {
  selectedPersonnel.value = personnel
  Object.assign(editForm, {
    nbEnfants: personnel.nbEnfants,
    situationMatrimoniale: personnel.situationMatrimoniale,
    statut: personnel.statut
  })
  showEditModal.value = true
}

const editWorkTime = (personnel: Personnel) => {
  selectedPersonnel.value = personnel
  showWorkTimeModal.value = true
}

const reverseCalculate = (personnel: Personnel) => {
  selectedPersonnel.value = personnel
  showReverseModal.value = true
}

const savePersonnelChanges = () => {
  if (selectedPersonnel.value) {
    const index = personnelData.value.findIndex(p => p.id === selectedPersonnel.value!.id)
    if (index !== -1) {
      Object.assign(personnelData.value[index], editForm)
      ElMessage.success('Modifications enregistrées')
      showEditModal.value = false
    }
  }
}

const saveWorkTimeChanges = () => {
  ElMessage.success('Temps de travail modifié')
  showWorkTimeModal.value = false
}

const calculateReverse = () => {
  reverseForm.nouveauSursalaire = reverseForm.netAPayer * 0.8
  ElMessage.info('Calcul effectué')
}

const saveReverseCalculation = () => {
  ElMessage.success('Calcul à l\'envers validé')
  showReverseModal.value = false
}

const deletePersonnel = async (personnel: Personnel) => {
  try {
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer ${personnel.nomComplet} ?`,
      'Confirmation de suppression',
      {
        confirmButtonText: 'Supprimer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )
    
    const index = personnelData.value.findIndex(p => p.id === personnel.id)
    if (index !== -1) {
      personnelData.value.splice(index, 1)
      ElMessage.success('Personnel supprimé')
    }
  } catch {
    // Annulé
  }
}

const generateTransferOrder = () => {
  ElMessage.success('Ordre de virement généré')
  showTransferModal.value = false
}

const printBulletin = (bulletin: Bulletin) => {
  ElMessage.info(`Impression du bulletin de ${bulletin.nomComplet}`)
}

const printAllBulletins = () => {
  ElMessage.success('Impression de tous les bulletins')
}

const regularizeNet = () => {
  ElMessage.info('Régularisation du net à payer en cours...')
}

onMounted(() => {
  loadPersonnel()
})
</script>

<style scoped>
.livre-paie-view {
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

.header-right {
  display: flex;
  gap: 12px;
}

.filters-section {
  margin-bottom: 24px;
}

.filters-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.filters-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.filters-content {
  padding: 20px;
}

.main-table-section {
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

.table-stats {
  display: flex;
  gap: 12px;
}

.table-container {
  padding: 20px;
}

.personnel-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.personnel-name {
  font-weight: 600;
  color: #303133;
}

.net-amount {
  font-weight: 700;
  color: #67c23a;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
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

.main-tabs-section {
  margin-bottom: 24px;
}

.livre-paie-content {
  padding: 20px;
}

.livre-paie-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.livre-paie-header h4 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.livre-paie-stats {
  display: flex;
  gap: 12px;
  align-items: center;
}

.livre-paie-table-container {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
}

.bulletins-content {
  padding: 20px;
}

.bulletins-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.bulletins-header h4 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.bulletins-stats {
  display: flex;
  gap: 12px;
  align-items: center;
}

.bulletins-table-container {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
}

.enhanced-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.generate-modal-content {
  padding: 20px 0;
}

.modal-info {
  margin-top: 20px;
}

.process-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
  color: #606266;
}

.print-modal-content {
  padding: 20px 0;
}

.print-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.print-header h4 {
  margin: 0;
  color: #303133;
}

.print-table-container {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
}

.transfer-modal-content {
  padding: 20px 0;
}
</style>
