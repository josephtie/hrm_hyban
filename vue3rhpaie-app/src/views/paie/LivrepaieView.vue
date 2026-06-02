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
          <el-button type="success" @click="showPrintModal = true" class="enhanced-button">
            <el-icon><Printer /></el-icon>
            Imprimer Bulletins
          </el-button>
          <el-button type="success" @click="showTransferModal = true" class="enhanced-button">
            <el-icon><CreditCard /></el-icon>
            Ordre de Virement
          </el-button>
          <el-button type="success" @click="regularizeNet" class="enhanced-button">
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
          <el-form-item>
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
        <el-tab-pane :label="personnelTabLabel" name="personnel">
          <div class="personnel-actions" style="display: flex; justify-content: flex-end; gap: 10px; margin-bottom: 12px;">
            <el-select v-model="filters.status" placeholder="Filtrer par statut" style="width: 170px" clearable @change="searchPersonnel">
              <el-option label="Actif" value="actif" />
              <el-option label="En sommeil" value="sommeil" />
            </el-select>
            <el-input
              v-model="filters.search"
              placeholder="Rechercher (matricule, nom, prénom)..."
              clearable
              style="max-width: 320px"
              @keyup.enter="searchPersonnel"
              @clear="searchPersonnel"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="searchPersonnel">
              <el-icon><Search /></el-icon>
              Rechercher
            </el-button>
          </div>
          <!-- Tableau principal -->
          <div class="table-container">
            <el-table 
              :data="paginatedPersonnel" 
              stripe 
              v-loading="loading"
              @selection-change="handleSelectionChange"
              @row-click="handlePersonnelRowClick"
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
              :current-page="currentPage + 1"
              :page-size="pageSize"
              :page-sizes="[20, 50, 100, 200, 500]"
              :total="totalPersonnel"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane :label="livrePaieTabLabel" name="livre-paie" :disabled="!livrePaieGenerated">
        <div class="livre-paie-content">
          <div class="livre-paie-header">
            <h4>Livre de Paie - {{ selectedPeriod }}</h4>
            <div class="livre-paie-stats">
              <el-tag>{{ livrePaieData.length }} bulletin(s)</el-tag>
              <el-tag type="success">{{ totalLivreNet.toLocaleString() }} XOF Total Net</el-tag>
              <el-button type="primary" @click="confirmGeneration" :loading="confirming">
                <el-icon><Check /></el-icon>
                Confirmer la génération
              </el-button>
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
              <el-table-column label="Statut trvail" prop="statutTravail" width="120" align="center" />
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
              <el-table-column
                v-for="prime in livrePrimesImpCols"
                :key="`prime-${prime.id}`"
                :label="prime.libelle"
                :prop="`prime${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeImposableValue(row, prime)) }}
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
              <el-table-column
                v-for="prime in livrePrimesImposeNonCols"
                :key="`primeI-brutnon-${prime.id}`"
                :label="prime.libelle"
                :prop="`primeI${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeNonImposableValue(row, prime)) }}
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
              <el-table-column
                v-for="prime in livrePrimesSocialeCols"
                :key="`primeS-livre-${prime.id}`"
                :label="prime.libelle"
                :prop="`primeS${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeSocialeValue(row, prime)) }}
                </template>
              </el-table-column>
              <el-table-column label="Retenue sociale" prop="cnps" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.cnps) }}
                </template>
              </el-table-column>
              <el-table-column
                v-for="prime in livrePrimesMutuelleCols"
                :key="`primeM-livre-${prime.id}`"
                :label="prime.libelle"
                :prop="`primeM${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeMutuelleValue(row, prime)) }}
                </template>
              </el-table-column>
              <el-table-column label="Total Retenue" prop="totalRetenue" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.totalRetenue) }}
                </template>
              </el-table-column>
              <el-table-column label="Avance & Acompte" prop="avceAcpte" width="150" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.avceAcpte) }}
                </template>
              </el-table-column>
              <el-table-column label="Pret" prop="pretAlios" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.pretAlios) }}
                </template>
              </el-table-column>
              <el-table-column label="Net a reguler" prop="netRegulPayer" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.netRegulPayer) }}
                </template>
              </el-table-column>
              <el-table-column
                v-for="prime in livrePrimesGainsCols"
                :key="`primeG-livre-${prime.id}`"
                :label="prime.libelle"
                :prop="`primeG${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeGainValue(row, prime)) }}
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
              <el-table-column label="IS" prop="is" width="100" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.is) }}
                </template>
              </el-table-column>
              <el-table-column label="TA" prop="ta" width="100" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.ta) }}
                </template>
              </el-table-column>
              <el-table-column label="FPC" prop="fpc" width="110" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.fpc) }}
                </template>
              </el-table-column>
              <el-table-column label="FPC REGUL" prop="fpcregul" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.fpcregul) }}
                </template>
              </el-table-column>
              <el-table-column label="Prest familiale" prop="prestationFamiliale" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.prestationFamiliale) }}
                </template>
              </el-table-column>
              <el-table-column label="Acc de travail" prop="accidentTravail" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.accidentTravail) }}
                </template>
              </el-table-column>
              <el-table-column label="Retraite" prop="retraite" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.retraite) }}
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

      <el-tab-pane :label="bulletinsTabLabel" name="bulletins" :disabled="!bulletinsGenerated">
        <div class="bulletins-content">
          <div class="bulletins-header">
            <h4>Bulletins de Paie - {{ selectedPeriod }}</h4>
            <div class="bulletins-stats">
              <el-tag>{{ filteredBulletins.length }} bulletin(s)</el-tag>
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
          <div class="bulletins-actions">
            <el-input
              v-model="bulletinsSearch"
              placeholder="Rechercher (matricule, nom, prénom)..."
              clearable
              style="max-width: 320px"
              @keyup.enter="searchBulletins"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="searchBulletins">
              <el-icon><Search /></el-icon>
              Rechercher
            </el-button>
            <el-button type="warning" @click="exportJulaya">
              <el-icon><Download /></el-icon>
              Export Julaya
            </el-button>
          </div>
          <div class="bulletins-table-container">
            <el-table :data="filteredBulletins" :loading="bulletinsLoading" stripe max-height="600">
              <el-table-column label="Matricule" prop="matricule" width="120" />
              <el-table-column label="Nom & Prénoms" prop="nomComplet" min-width="200" />
              <el-table-column label="Statut trvail" prop="statutTravail" width="120" align="center" />
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
              <el-table-column
                v-for="prime in livrePrimesImpCols"
                :key="`prime-bulletin-${prime.id}`"
                :label="prime.libelle"
                :prop="`prime${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeImposableValue(row, prime)) }}
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
              <el-table-column
                v-for="prime in livrePrimesImposeNonCols"
                :key="`primeI-bulletin-brutnon-${prime.id}`"
                :label="prime.libelle"
                :prop="`primeI${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeNonImposableValue(row, prime)) }}
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
              <el-table-column
                v-for="prime in livrePrimesSocialeCols"
                :key="`primeS-bulletin-${prime.id}`"
                :label="prime.libelle"
                :prop="`primeS${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeSocialeValue(row, prime)) }}
                </template>
              </el-table-column>
              <el-table-column label="Retenue sociale" prop="cnps" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.cnps) }}
                </template>
              </el-table-column>
              <el-table-column
                v-for="prime in livrePrimesMutuelleCols"
                :key="`primeM-bulletin-${prime.id}`"
                :label="prime.libelle"
                :prop="`primeM${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeMutuelleValue(row, prime)) }}
                </template>
              </el-table-column>
              <el-table-column label="Total Retenue" prop="totalRetenue" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.totalRetenue) }}
                </template>
              </el-table-column>
              <el-table-column label="Avance & Acompte" prop="avceAcpte" width="150" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.avceAcpte) }}
                </template>
              </el-table-column>
              <el-table-column label="Pret" prop="pretAlios" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.pretAlios) }}
                </template>
              </el-table-column>
              <el-table-column label="Net a reguler" prop="netRegulPayer" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.netRegulPayer) }}
                </template>
              </el-table-column>
              <el-table-column
                v-for="prime in livrePrimesGainsCols"
                :key="`primeG-bulletin-${prime.id}`"
                :label="prime.libelle"
                :prop="`primeG${prime.id}`"
                width="140"
                align="right"
              >
                <template #default="{ row }">
                  {{ formatCurrency(getPrimeGainValue(row, prime)) }}
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
              <el-table-column label="IS" prop="is" width="100" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.is) }}
                </template>
              </el-table-column>
              <el-table-column label="TA" prop="ta" width="100" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.ta) }}
                </template>
              </el-table-column>
              <el-table-column label="FPC" prop="fpc" width="110" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.fpc) }}
                </template>
              </el-table-column>
              <el-table-column label="FPC REGUL" prop="fpcregul" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.fpcregul) }}
                </template>
              </el-table-column>
              <el-table-column label="Prest familiale" prop="prestationFamiliale" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.prestationFamiliale) }}
                </template>
              </el-table-column>
              <el-table-column label="Acc de travail" prop="accidentTravail" width="140" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.accidentTravail) }}
                </template>
              </el-table-column>
              <el-table-column label="Retraite" prop="retraite" width="120" align="right">
                <template #default="{ row }">
                  {{ formatCurrency(row.retraite) }}
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
              <el-table-column label="Actions" width="100" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" size="small" @click="printBulletin(row)">
                    <el-icon><Printer /></el-icon>
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container">
              <el-pagination
                v-model:current-page="bulletinsCurrentPage"
                v-model:page-size="bulletinsPageSize"
                :page-sizes="[50, 100, 200, 500]"
                :total="bulletinsTotal"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleBulletinsSizeChange"
                @current-change="handleBulletinsPageChange"
              />
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>

    <!-- Modal de génération — Design moderne -->
    <el-dialog
      v-model="showGenerateModal"
      width="540px"
      :show-close="false"
      align-center
      destroy-on-close
      class="modern-modal theme-success"
    >
      <template #header>
        <div class="mm-header">
          <div class="mm-header-icon"><el-icon><Document /></el-icon></div>
          <div class="mm-header-text">
            <h2>Génération du Bulletin de Paie</h2>
            <p>Lancer le calcul automatique pour la période</p>
          </div>
          <el-button link class="mm-close" @click="showGenerateModal = false">
            <el-icon :size="20"><Close /></el-icon>
          </el-button>
        </div>
      </template>

      <div class="mm-body">
        <div class="mm-banner mm-banner-warning">
          <el-icon class="mm-banner-icon"><Warning /></el-icon>
          <div>
            <strong>Période ciblée : {{ currentPeriode?.affiche || currentPeriode?.libelle || selectedPeriod }}</strong>
            <p v-if="currentPeriode">
              Du <em>{{ currentPeriode.ddeb }}</em> au <em>{{ currentPeriode.dfin }}</em> — tous les bulletins de paie de cette période seront (re)calculés.
            </p>
            <p v-else>Aucune période active trouvée. La période active sera utilisée par défaut côté serveur.</p>
          </div>
        </div>
        <div class="mm-process-card">
          <el-icon class="spin"><Loading /></el-icon>
          <div>
            <strong>Processus long</strong>
            <p>L'opération peut prendre plusieurs minutes selon le nombre de personnels.</p>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="mm-footer">
          <el-button size="large" plain @click="showGenerateModal = false">Annuler</el-button>
          <el-button
            type="success"
            size="large"
            class="mm-btn-primary"
            :loading="generating"
            @click="generateBulletins"
          >
            <el-icon><Document /></el-icon>
            Lancer la génération
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Modal d'impression — Design moderne -->
    <el-dialog
      v-model="showPrintModal"
      width="90%"
      :show-close="false"
      align-center
      destroy-on-close
      class="modern-modal theme-info"
    >
      <template #header>
        <div class="mm-header">
          <div class="mm-header-icon"><el-icon><Printer /></el-icon></div>
          <div class="mm-header-text">
            <h2>Impression des Bulletins de Paie</h2>
            <p>{{ filteredBulletins.length }} bulletin(s) · Période : {{ selectedPeriod }}</p>
          </div>
          <el-button link class="mm-close" @click="showPrintModal = false">
            <el-icon :size="20"><Close /></el-icon>
          </el-button>
        </div>
      </template>

      <div class="mm-body print-modal-content">
        <div class="print-header">
          <div class="print-summary">
            <el-tag size="large" type="info" round>{{ filteredBulletins.length }} bulletin(s)</el-tag>
            <el-tag size="large" type="success" round>{{ totalBulletinsNet.toLocaleString() }} XOF Net total</el-tag>
          </div>
          <el-button type="success" @click="exportBulletinsExcel" class="mm-btn-primary">
            <el-icon><Download /></el-icon>
            Exporter en Excel
          </el-button>
        </div>
        <div class="print-table-container">
          <el-table :data="filteredBulletins" stripe max-height="500">
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
            <el-table-column
              v-for="prime in livrePrimesImposeNonCols"
              :key="`primeI-print-brutnon-${prime.id}`"
              :label="prime.libelle"
              :prop="`primeI${prime.id}`"
              width="140"
              align="right"
            >
              <template #default="{ row }">
                {{ formatCurrency(getPrimeNonImposableValue(row, prime)) }}
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
            <el-table-column
              v-for="prime in livrePrimesSocialeCols"
              :key="`primeS-print-${prime.id}`"
              :label="prime.libelle"
              :prop="`primeS${prime.id}`"
              width="140"
              align="right"
            >
              <template #default="{ row }">
                {{ formatCurrency(getPrimeSocialeValue(row, prime)) }}
              </template>
            </el-table-column>
            <el-table-column label="Retenue sociale" prop="cnps" width="140" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.cnps) }}
              </template>
            </el-table-column>
            <el-table-column
              v-for="prime in livrePrimesMutuelleCols"
              :key="`primeM-print-${prime.id}`"
              :label="prime.libelle"
              :prop="`primeM${prime.id}`"
              width="140"
              align="right"
            >
              <template #default="{ row }">
                {{ formatCurrency(getPrimeMutuelleValue(row, prime)) }}
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
            <el-table-column label="IS" prop="is" width="100" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.is) }}
              </template>
            </el-table-column>
            <el-table-column label="TA" prop="ta" width="100" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.ta) }}
              </template>
            </el-table-column>
            <el-table-column label="FPC" prop="fpc" width="110" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.fpc) }}
              </template>
            </el-table-column>
            <el-table-column label="FPC REGUL" prop="fpcregul" width="120" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.fpcregul) }}
              </template>
            </el-table-column>
            <el-table-column label="Prest familiale" prop="prestationFamiliale" width="140" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.prestationFamiliale) }}
              </template>
            </el-table-column>
            <el-table-column label="Acc de travail" prop="accidentTravail" width="140" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.accidentTravail) }}
              </template>
            </el-table-column>
            <el-table-column label="Retraite" prop="retraite" width="120" align="right">
              <template #default="{ row }">
                {{ formatCurrency(row.retraite) }}
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
        <div class="mm-footer">
          <el-button size="large" plain @click="showPrintModal = false">Fermer</el-button>
          <el-button type="success" size="large" class="mm-btn-primary" @click="printAllBulletins">
            <el-icon><Printer /></el-icon>
            Imprimer tous les bulletins
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Modal d'ordre de virement — Design moderne -->
    <el-dialog
      v-model="showTransferModal"
      width="600px"
      :show-close="false"
      align-center
      destroy-on-close
      class="modern-modal theme-warning"
    >
      <template #header>
        <div class="mm-header">
          <div class="mm-header-icon"><el-icon><CreditCard /></el-icon></div>
          <div class="mm-header-text">
            <h2>Ordre de Virement</h2>
            <p>Générer le fichier de virement bancaire</p>
          </div>
          <el-button link class="mm-close" @click="showTransferModal = false">
            <el-icon :size="20"><Close /></el-icon>
          </el-button>
        </div>
      </template>

      <div class="mm-body">
        <el-form :model="transferForm" label-position="top" size="large">
          <div class="mm-section">
            <div class="mm-section-header">
              <el-icon><OfficeBuilding /></el-icon>
              <span>Comptes bancaires</span>
            </div>
            <el-form-item label="Compte Entreprise (émetteur)">
              <el-select v-model="transferForm.compteEntreprise" placeholder="Sélectionner le compte source" style="width: 100%">
                <el-option
                  v-for="bank in banks"
                  :key="bank.id"
                  :label="`${bank.sigle} (${bank.code})`"
                  :value="bank.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="Banque destinataire">
              <el-select v-model="transferForm.banque" placeholder="Sélectionner la banque" style="width: 100%">
                <el-option
                  v-for="bank in banks"
                  :key="bank.id"
                  :label="`${bank.sigle} (${bank.code})`"
                  :value="bank.id"
                />
              </el-select>
            </el-form-item>
          </div>
        </el-form>
      </div>

      <template #footer>
        <div class="mm-footer">
          <el-button size="large" plain @click="showTransferModal = false">Annuler</el-button>
          <el-button type="success" size="large" class="mm-btn-primary" @click="generateTransferOrder">
            <el-icon><CreditCard /></el-icon>
            Générer l'ordre de virement
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Modal de modification personnel — Design moderne -->
    <el-dialog
      v-model="showEditModal"
      width="540px"
      :show-close="false"
      align-center
      destroy-on-close
      class="modern-modal theme-primary"
    >
      <template #header>
        <div class="mm-header">
          <div class="mm-header-icon"><el-icon><Edit /></el-icon></div>
          <div class="mm-header-text">
            <h2>Modification du personnel</h2>
            <p v-if="selectedPersonnel">{{ selectedPersonnel.nomComplet }} · {{ selectedPersonnel.matricule }}</p>
          </div>
          <el-button link class="mm-close" @click="showEditModal = false">
            <el-icon :size="20"><Close /></el-icon>
          </el-button>
        </div>
      </template>

      <div class="mm-body">
        <el-form :model="editForm" label-position="top" size="large">
          <div class="mm-grid-2">
            <el-form-item label="Nombre d'enfants">
              <el-input-number v-model="editForm.nbEnfants" :min="0" :controls-position="'right'" style="width: 100%" />
            </el-form-item>
            <el-form-item label="Situation matrimoniale">
              <el-select v-model="editForm.situationMatrimoniale" style="width: 100%" placeholder="Sélectionner">
                <el-option label="Marié(e)" value="marie" />
                <el-option label="Célibataire" value="celibataire" />
                <el-option label="Divorcé(e)" value="divorce" />
                <el-option label="Veuf/Veuve" value="veuf" />
              </el-select>
            </el-form-item>
          </div>
          <el-form-item label="Statut du personnel">
            <div class="mm-radio-cards">
              <div
                class="mm-radio-card"
                :class="{ active: editForm.statut === 'actif' }"
                @click="editForm.statut = 'actif'"
              >
                <el-icon><User /></el-icon>
                <div>
                  <strong>Actif</strong>
                  <span>Personnel en activité</span>
                </div>
              </div>
              <div
                class="mm-radio-card"
                :class="{ active: editForm.statut === 'sommeil' }"
                @click="editForm.statut = 'sommeil'"
              >
                <el-icon><Bell /></el-icon>
                <div>
                  <strong>En sommeil</strong>
                  <span>Pas de calcul de paie</span>
                </div>
              </div>
            </div>
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <div class="mm-footer">
          <el-button size="large" plain @click="showEditModal = false">Annuler</el-button>
          <el-button type="primary" size="large" class="mm-btn-primary" :loading="editing" @click="savePersonnelChanges">
            <el-icon><Edit /></el-icon>
            Enregistrer les modifications
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Modal modification temps de travail — Design moderne -->
    <el-dialog
      v-model="showWorkTimeModal"
      width="540px"
      :show-close="false"
      align-center
      destroy-on-close
      class="modern-modal theme-primary"
    >
      <template #header>
        <div class="mm-header">
          <div class="mm-header-icon"><el-icon><Clock /></el-icon></div>
          <div class="mm-header-text">
            <h2>Temps de travail</h2>
            <p v-if="selectedPersonnel">{{ selectedPersonnel.nomComplet }} · {{ selectedPersonnel.matricule }}</p>
          </div>
          <el-button link class="mm-close" @click="showWorkTimeModal = false">
            <el-icon :size="20"><Close /></el-icon>
          </el-button>
        </div>
      </template>

      <div class="mm-body">
        <el-form :model="workTimeForm" label-position="top" size="large">
          <div class="mm-grid-2">
            <el-form-item label="Nombre de jours travaillés">
              <el-input-number
                v-model="workTimeForm.nbJours"
                :min="0"
                :max="31"
                :step="1"
                :precision="2"
                :controls-position="'right'"
                style="width: 100%"
                @change="onJoursChange"
              />
            </el-form-item>
            <el-form-item label="Temps de travail (heures)">
              <el-input-number
                v-model="workTimeForm.tempsTravail"
                :min="0"
                :step="0.5"
                :precision="2"
                :controls-position="'right'"
                style="width: 100%"
                @change="onHeuresChange"
              />
            </el-form-item>
          </div>
          <div class="mm-info-card">
            <el-icon><Calendar /></el-icon>
            <span v-if="currentPeriode">
              Période : <strong>{{ currentPeriode.libelle || `#${currentPeriode.id}` }}</strong> · Standard : 30 jours / 173,33 heures
            </span>
            <span v-else>Standard mensuel : 30 jours · 173,33 heures</span>
          </div>
        </el-form>
      </div>

      <template #footer>
        <div class="mm-footer">
          <el-button size="large" plain @click="showWorkTimeModal = false">Annuler</el-button>
          <el-button type="primary" size="large" class="mm-btn-primary" :loading="savingWorkTime" @click="saveWorkTimeChanges">
            <el-icon><Clock /></el-icon>
            Enregistrer le temps
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Modal calcul à l'envers — Design moderne -->
    <el-dialog
      v-model="showReverseModal"
      width="560px"
      :show-close="false"
      align-center
      destroy-on-close
      class="modern-modal theme-info"
    >
      <template #header>
        <div class="mm-header">
          <div class="mm-header-icon"><el-icon><Operation /></el-icon></div>
          <div class="mm-header-text">
            <h2>Calcul à l'envers</h2>
            <p v-if="selectedPersonnel">{{ selectedPersonnel.nomComplet }} · {{ selectedPersonnel.matricule }}</p>
          </div>
          <el-button link class="mm-close" @click="showReverseModal = false">
            <el-icon :size="20"><Close /></el-icon>
          </el-button>
        </div>
      </template>

      <div class="mm-body">
        <div class="mm-banner mm-banner-info">
          <el-icon class="mm-banner-icon"><Operation /></el-icon>
          <div>
            <strong>Saisissez le net souhaité</strong>
            <p>Le système calculera automatiquement le nouveau sursalaire correspondant.</p>
          </div>
        </div>

        <el-form :model="reverseForm" label-position="top" size="large">
          <el-form-item label="Net à payer souhaité">
            <el-input-number
              v-model="reverseForm.netAPayer"
              :min="0"
              :step="1000"
              :controls="false"
              style="width: 100%"
              :formatter="(v: number) => `${(v||0).toLocaleString()} XOF`"
              :parser="(v: string) => v.replace(/[^\d]/g, '')"
            />
          </el-form-item>
          <el-form-item label="Nouveau Sursalaire (calculé)">
            <el-input-number
              v-model="reverseForm.nouveauSursalaire"
              :min="0"
              disabled
              :controls="false"
              style="width: 100%"
              :formatter="(v: number) => `${(v||0).toLocaleString()} XOF`"
            />
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <div class="mm-footer">
          <el-button size="large" plain @click="showReverseModal = false">Annuler</el-button>
          <el-button size="large" type="info" plain @click="calculateReverse">
            <el-icon><Operation /></el-icon>
            Calculer
          </el-button>
          <el-button
            type="primary"
            size="large"
            class="mm-btn-primary"
            :loading="savingReverse"
            :disabled="!reverseForm.nouveauSursalaire || reverseForm.nouveauSursalaire <= 0"
            @click="saveReverseCalculation"
          >
            Valider
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, type Ref } from 'vue'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import {
  Document, Plus, Printer, CreditCard, Refresh, Search, Download,
  Male, Female, View, Edit, MoreFilled, Clock, Delete, Loading, Operation,
  Close, User, Calendar, OfficeBuilding, Money, Warning, Bell, Check
} from '@element-plus/icons-vue'
import { useRoute } from 'vue-router'
import { useLivreDePaieService } from '@/services/livrepaie.service'
import { usePayrollStore } from '@/stores/payroll.store'
import { livrePaieService, situationMatriToCode, type PeriodePaieRef } from '@/services/livrepaie.service'
import {
  primepersonnelrestService,
  type RubriquePrimeMontantDto,
  type RubriquesContratPeriodeDto
} from '@/services/primepersonnelrest.service'
import * as XLSX from 'xlsx'

interface Personnel {
  id: number
  contratId?: number
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
  statutTravail: string
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
  avceAcpte: number
  pretAlios: number
  regularisation: number
  netRegulPayer: number
  netAPayer: number
  totalBrut: number
  is: number
  ta: number
  fpc: number
  fpcregul: number
  prestationFamiliale: number
  accidentTravail: number
  retraite: number
  totalPatronal: number
  nbJoursTravail: number
  indemniteTransportImp: number
  autreIndemImposable: number
  indemniteRepresentation: number
  indemniteTransport: number
  brutNonImposable: number
  baseCnps: number
  totalMasseSalariale: number
  [key: string]: any
}

interface PrimeColumn {
  id: number
  libelle: string
}

interface PrimeMetaLike {
  id?: number
  libelle?: string
  mtExedent?: any
}

interface PrimeRowLike {
  id?: number
  idPrime?: number
  libelle?: string
  montant?: any
  valeur?: any
  mtmontant?: any
  prime?: PrimeMetaLike
  rubrique?: PrimeMetaLike
}

interface Bank {
  id: number
  sigle: string
  code: string
}

// Données réactives
const route = useRoute()
const isSpecialAgentsLivre = computed(() => route.meta.specialAgents === true)
const personnelTabLabel = computed(() => isSpecialAgentsLivre.value ? 'Liste Agents Speciales' : 'Liste Personnel')
const livrePaieTabLabel = computed(() => isSpecialAgentsLivre.value ? 'Liste de Paie Speciale' : 'Livre de Paie')
const bulletinsTabLabel = computed(() => isSpecialAgentsLivre.value ? 'Bulletin de Paie Speciale' : 'Bulletins de Paie')
const loading = ref(false)
const generating = ref(false)
const confirming = ref(false)
const savingWorkTime = ref(false)
const savingReverse = ref(false)
const currentPeriode = ref<PeriodePaieRef | null>(null)
const periodesPaie = ref<PeriodePaieRef[]>([])
const showGenerateModal = ref(false)
const showPrintModal = ref(false)
const showTransferModal = ref(false)
const showEditModal = ref(false)
const showWorkTimeModal = ref(false)
const showReverseModal = ref(false)
const selectedPersonnel = ref<Personnel | null>(null)
const selectedPersonnelList = ref<Personnel[]>([])
const currentPage = ref(0)
const pageSize = ref(20)
const sortBy = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')
const activeTab = ref('personnel')
const livrePaieGenerated = ref(false)
const bulletinsGenerated = ref(false)

// Total lignes Livre de Paie (affiché dans le toast de génération).
const livreTotal = ref(0)
const bulletinsCurrentPage = ref(1)
const bulletinsPageSize = ref(50)
const bulletinsTotal = ref(0)
const bulletinsSearch = ref('')
const bulletinsLoading = ref(false)

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

// Données du personnel chargées depuis le backend
const personnelData = ref<Personnel[]>([])
const totalPersonnel = ref(0)

const bulletinsData = ref<Bulletin[]>([])
const livrePrimesImpCols = ref<PrimeColumn[]>([])
const livrePrimesImposeNonCols = ref<PrimeColumn[]>([])
const livrePrimesSocialeCols = ref<PrimeColumn[]>([])
const livrePrimesMutuelleCols = ref<PrimeColumn[]>([])
const livrePrimesGainsCols = ref<PrimeColumn[]>([])
const cataloguePrimesImpCols = ref<PrimeColumn[]>([])
const cataloguePrimesImposeNonCols = ref<PrimeColumn[]>([])
const cataloguePrimesSocialeCols = ref<PrimeColumn[]>([])
const cataloguePrimesMutuelleCols = ref<PrimeColumn[]>([])
const cataloguePrimesGainsCols = ref<PrimeColumn[]>([])

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
    avceAcpte: 0,
    pretAlios: 0,
    netRegulPayer: 0,
    netAPayer: 425000,
    totalBrut: 605000,
    is: 0,
    ta: 0,
    fpc: 0,
    fpcregul: 0,
    prestationFamiliale: 0,
    accidentTravail: 0,
    retraite: 0,
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

// Liste des périodes alimentée dynamiquement depuis `periodesPaie` (backend).
// Chaque entrée associe l'id de période -> libellé affichable (ex: "MARS 2026").
const periods = computed(() => {
  return periodesPaie.value.map(p => ({
    value: String(p.id),
    label: p.affiche || p.libelle || `Période #${p.id}`
  }))
})

// Libellé de la période ciblée pour les en-têtes/onglets.
// Priorité : la période active backend (currentPeriode), puis la sélection du dropdown.
const selectedPeriod = computed(() => {
  if (currentPeriode.value) {
    return (currentPeriode.value as any).affiche
      || (currentPeriode.value as any).libelle
      || `Période #${currentPeriode.value.id}`
  }
  const period = periods.value.find(p => p.value === filters.period)
  return period?.label || filters.period || '—'
})

const normalizeQueryText = (value: any): string => {
  return String(value ?? '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .trim()
}

const filteredPersonnel = computed(() => {
  let filtered = personnelData.value

  if (filters.status) {
    const expected = normalizeQueryText(filters.status)
    filtered = filtered.filter((p) => normalizeQueryText(p.statut) === expected)
  }

  if (filters.search) {
    const query = normalizeQueryText(filters.search)
    filtered = filtered.filter((p) => {
      const matricule = normalizeQueryText(p.matricule)
      const nomComplet = normalizeQueryText(p.nomComplet)
      const tokens = nomComplet.split(/\s+/).filter(Boolean)

      return matricule.includes(query)
        || nomComplet.includes(query)
        || tokens.some(t => t.includes(query))
    })
  }

  return filtered
})

// Pagination côté serveur : on affiche directement la page renvoyée par le backend.
// Les filtres client-side (status) restent appliqués sur la page courante.
const paginatedPersonnel = computed(() => filteredPersonnel.value)

const filteredBulletins = computed(() => {
  const query = bulletinsSearch.value.trim().toLowerCase()
  if (!query) return bulletinsData.value

  return bulletinsData.value.filter(b => {
    const matricule = String(b.matricule ?? '').toLowerCase()
    const nomComplet = String(b.nomComplet ?? '').toLowerCase()
    const tokens = nomComplet.split(/\s+/).filter(Boolean)

    return matricule.includes(query)
      || nomComplet.includes(query)
      || tokens.some(t => t.includes(query))
  })
})

const totalNet = computed(() => {
  return filteredPersonnel.value.reduce((sum, p) => sum + p.netAPayer, 0)
})

const totalLivreNet = computed(() => {
  return livrePaieData.value.reduce((sum, p) => sum + p.netAPayer, 0)
})

const totalBulletinsNet = computed(() => {
  return filteredBulletins.value.reduce((sum, p) => sum + p.netAPayer, 0)
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

  if (selection.length > 0) {
    loadPrimeCatalogColumns(selection[0])
  }
}

const handlePersonnelRowClick = (row: Personnel) => {
  selectedPersonnel.value = row
  loadPrimeCatalogColumns(row)
}

const handleSortChange = ({ prop, order }: { prop: string; order: string | null }) => {
  sortBy.value = prop
  sortOrder.value = order === 'ascending' ? 'asc' : 'desc'
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 0
  loadPersonnel()

  if (livrePaieGenerated.value) {
    loadLivrePaiePage()
  }

  if (bulletinsGenerated.value) {
    bulletinsCurrentPage.value = 1
    loadBulletinsPage()
  }
}

const getPrimeGainValue = (row: Bulletin, prime: PrimeColumn): number => {
  const key = `primeG${prime.id}`
  return Number((row as any)?.[key] ?? 0)
}

const resolveImposableRows = (row: any): any[] => {
  if (Array.isArray(row?.listIndemniteBrut)) return row.listIndemniteBrut
  if (Array.isArray(row?.listIndemBrut)) return row.listIndemBrut
  if (Array.isArray(row?.bullpaie?.listIndemniteBrut)) return row.bullpaie.listIndemniteBrut
  if (Array.isArray(row?.bullpaie?.listIndemBrut)) return row.bullpaie.listIndemBrut
  return []
}

const resolveNonImposableRows = (row: any): any[] => {
  if (Array.isArray(row?.listIndemniteNonImp)) return row.listIndemniteNonImp
  if (Array.isArray(row?.listIndemBrutNonImp)) return row.listIndemBrutNonImp
  if (Array.isArray(row?.bullpaie?.listIndemniteNonImp)) return row.bullpaie.listIndemniteNonImp
  if (Array.isArray(row?.bullpaie?.listIndemBrutNonImp)) return row.bullpaie.listIndemBrutNonImp
  return []
}

const resolveMutuelleRows = (row: any): any[] => {
  if (Array.isArray(row?.listRetenueMutuellt)) return row.listRetenueMutuellt
  if (Array.isArray(row?.listRetenueMutuelle)) return row.listRetenueMutuelle
  if (Array.isArray(row?.bullpaie?.listRetenueMutuellt)) return row.bullpaie.listRetenueMutuellt
  if (Array.isArray(row?.bullpaie?.listRetenueMutuelle)) return row.bullpaie.listRetenueMutuelle
  return []
}

const findPrimeMontantInRows = (rows: PrimeRowLike[], primeId: number): number | null => {
  for (const primeRow of rows) {
    const meta = extractPrimeMeta(primeRow)
    if (meta?.id === primeId) {
      const montant = parsePrimeAmount(
        primeRow?.montant ?? primeRow?.valeur ?? primeRow?.mtmontant ?? 0
      )
      const mtExedent = parsePrimeAmount(
        primeRow?.prime?.mtExedent ?? primeRow?.rubrique?.mtExedent ?? 0
      )

      if (mtExedent > 0) {
        return Math.max(montant - mtExedent, 0)
      }
      return montant
    }
  }

  return null
}

const getPrimeValueFromResolver = (
  row: Bulletin,
  prime: PrimeColumn,
  keyPrefix: 'prime' | 'primeI' | 'primeM',
  resolver: (row: any) => any[]
): number => {
  const key = `${keyPrefix}${prime.id}`
  const direct = Number((row as any)?.[key] ?? 0)
  if (direct > 0) return direct

  const montant = findPrimeMontantInRows(resolver(row as any), prime.id)
  return montant ?? direct
}

const getPrimeImposableValue = (row: Bulletin, prime: PrimeColumn): number => {
  return getPrimeValueFromResolver(row, prime, 'prime', resolveImposableRows)
}

const getPrimeNonImposableValue = (row: Bulletin, prime: PrimeColumn): number => {
  return getPrimeValueFromResolver(row, prime, 'primeI', resolveNonImposableRows)
}

const resolveSocialeRows = (row: any): any[] => {
  if (Array.isArray(row?.listRetenueSociale)) return row.listRetenueSociale
  if (Array.isArray(row?.listRetenueSocial)) return row.listRetenueSocial
  if (Array.isArray(row?.bullpaie?.listRetenueSociale)) return row.bullpaie.listRetenueSociale
  if (Array.isArray(row?.bullpaie?.listRetenueSocial)) return row.bullpaie.listRetenueSocial
  return []
}

const getPrimeSocialeValue = (row: Bulletin, prime: PrimeColumn): number => {
  return getPrimeValueFromResolver(row, prime, 'primeS', resolveSocialeRows)
}

const getPrimeMutuelleValue = (row: Bulletin, prime: PrimeColumn): number => {
  return getPrimeValueFromResolver(row, prime, 'primeM', resolveMutuelleRows)
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page - 1
  loadPersonnel()
}

const handleTabChange = (tabName: string) => {
  activeTab.value = tabName

  if (tabName === 'bulletins' && bulletinsGenerated.value) {
    loadBulletinsPage()
  }
}

const syncExistingBulletinsForCurrentPeriod = async () => {
  bulletinsCurrentPage.value = 1
  const ok = await loadBulletinsPage()
  if (!ok) {
    bulletinsGenerated.value = false
    return
  }

  bulletinsGenerated.value = bulletinsTotal.value > 0
  if (bulletinsGenerated.value) {
    activeTab.value = 'bulletins'
  }
}

const changePeriod = async () => {
  // Aligner currentPeriode (objet PeriodePaieRef) sur la sélection du dropdown
  // pour que tous les en-têtes / appels backend ciblent la bonne période.
  const id = Number(filters.period)
  const found = periodesPaie.value.find(p => p.id === id)
  if (found) currentPeriode.value = found

  await loadPrimeCatalogColumns()

  currentPage.value = 0
  await loadPersonnel()

  if (livrePaieGenerated.value) {
    await loadLivrePaiePage()
  }

  await syncExistingBulletinsForCurrentPeriod()
}

const searchPersonnel = async () => {
  currentPage.value = 0
  await loadPersonnel()
}

const loadPersonnel = async () => {
  loading.value = true
  try {
    const res = await livrePaieService.loadPersonnel({
      page: currentPage.value + 1,
      size: pageSize.value,
      search: filters.search?.trim() || ''
    })
    if (res.success) {
      personnelData.value = res.data
      totalPersonnel.value = res.total || 0
      await loadPrimeCatalogColumns()
    } else {
      personnelData.value = []
      totalPersonnel.value = 0
      ElMessage.error(res.message || 'Erreur de chargement du personnel')
    }
  } catch (e: any) {
    console.error('loadPersonnel error', e)
    ElMessage.error('Erreur de chargement du personnel')
  } finally {
    loading.value = false
  }
}

const resolveActiveContratId = (preferredPersonnel?: Personnel | null): number | null => {
  const candidateIds = [
    Number((preferredPersonnel as any)?.contratId ?? 0),
    Number((selectedPersonnel.value as any)?.contratId ?? 0),
    Number((selectedPersonnelList.value?.[0] as any)?.contratId ?? 0),
    Number((personnelData.value?.[0] as any)?.contratId ?? 0)
  ]

  for (const id of candidateIds) {
    if (Number.isFinite(id) && id > 0) return id
  }

  return null
}

// Mapping d'une ligne brute backend (LivreDePaie) vers le type local Bulletin.
const mapLivreRow = (r: any): Bulletin => {
  const b = r?.bullpaie || {}
  const personnel = r?.contratPersonnel?.personnel || r?.personnel || {}
  const matricule = personnel.matricule || r.matricule || ''
  const nomComplet = personnel.nomComplet
    || `${personnel.prenom || ''} ${personnel.nom || ''}`.trim()
    || r.nomPrenom || ''
  const salaireBase = Number(r.salaireBase ?? r.salairecateg ?? 0)
  const sursalaire = Number(r.sursalaire ?? 0)
  const indemLog = Number(r.indemniteLogement ?? r.indemnitelog ?? 0)
  const cnps = Number(r.cnps ?? r.retenueCNPS ?? 0)
  const its = Number(r.its ?? r.retenueITS ?? 0)
  const totalRetenue = Number(r.totalRetenue ?? r.retenueTotale ?? (cnps + its))
  const brutImposable = Number(r.brutImposable ?? r.salaireBrutImposable ?? 0)
  const retenueFiscale = Number(
    r.retenueFiscale
    ?? r.totalRetenueFiscale
    ?? r.igr
    ?? b.totalretenuefiscal
    ?? b.igr
    ?? 0
  )
  const avceAcpte = Number(r.avceAcpte ?? r.avanceetacompte ?? b.avanceetacompte ?? 0)
  const pretAlios = Number(r.pretAlios ?? r.pretaloes ?? b.pretaloes ?? 0)
  const regularisation = Number(r.regularisation ?? b.regularisation ?? 0)
  const netRegulPayer = Number(r.netRegulPayer ?? r.netRegulpayer ?? b.netRegulPayer ?? b.netRegulpayer ?? 0)
  const brutNonImposable = Number(r.brutNonImposable ?? r.brutnonimposable ?? b.brutNonImposable ?? b.brutnonimposable ?? 0)
  const totalBrut = Number(
    r.totalBrut
    ?? r.totalbrut
    ?? b.totalbrut
    ?? b.totalBrut
    ?? (brutImposable + brutNonImposable)
  )
  const is = Number(r.is ?? b.impotSalaire ?? b.isPatronal ?? 0)
  const ta = Number(r.ta ?? b.ta ?? 0)
  const fpc = Number(r.fpc ?? b.fpc ?? 0)
  const fpcregul = Number(r.fpcregul ?? b.fpcregul ?? 0)
  const prestationFamiliale = Number(r.prestationFamiliale ?? b.prestationFamiliale ?? 0)
  const accidentTravail = Number(r.accidentTravail ?? b.accidentTravail ?? 0)
  const retraite = Number(r.retraite ?? b.retraite ?? 0)
  const baseCnps = Number(
    r.baseCnps
    ?? r.basecnps
    ?? b.basecnps
    ?? b.baseCnps
    ?? 0
  )
  const netAPayer = Number(
    r.netPayer ?? r.netAPayer ?? r.netapayer ?? r.bullpaie?.netapayer ?? 0
  )

  const mapped = {
    id: r.id ?? personnel.id,
    matricule,
    nomComplet,
    statut: personnel.enSommeil ? 'sommeil' : 'actif',
    statutTravail: personnel.statfonct ?? '',
    nombrePart: Number(r.nombrePart ?? personnel.nombrePart ?? 0),
    anciennete: r.anciennete ?? '',
    salaireBase,
    sursalaire,
    primeAnciennete: Number(r.primeAnciennete ?? 0),
    indemniteLogement: indemLog,
    brutImposable,
    its,
    retenueFiscale,
    cnps,
    totalRetenue,
    avceAcpte,
    pretAlios,
    regularisation,
    netRegulPayer,
    netAPayer,
    totalBrut,
    is,
    ta,
    fpc,
    fpcregul,
    prestationFamiliale,
    accidentTravail,
    retraite,
    totalPatronal: Number(r.totalPatronal ?? 0),
    nbJoursTravail: Number(
      r.jourTravail
      ?? r.bullpaie?.jourTravail
      ?? r.nbJoursTravail
      ?? r.jourspresence
      ?? 30
    ),
    indemniteTransportImp: Number(r.indemniteTransportImp ?? 0),
    autreIndemImposable: Number(r.autreIndemImposable ?? 0),
    indemniteRepresentation: Number(r.indemniteRepresentation ?? 0),
    indemniteTransport: Number(r.indemniteTransport ?? 0),
    brutNonImposable,
    baseCnps,
    totalMasseSalariale: Number(r.totalMasseSalariale ?? 0)
  } as Bulletin

  applyPrimeValues(mapped, resolveImposableRows(r), 'prime')
  applyPrimeValues(mapped, resolveNonImposableRows(r), 'primeI')
  applyPrimeValues(mapped, resolveSocialeRows(r), 'primeS')
  applyPrimeValues(mapped, resolveMutuelleRows(r), 'primeM')
  applyPrimeValues(mapped, resolveGainRows(r), 'primeG')

  return mapped
}

const mapBulletinRow = (r: any): Bulletin => {
  const personnel = r?.contratPersonnel?.personnel || r?.personnel || {}
  const matricule = personnel.matricule || r.matricule || ''
  const nomComplet = personnel.nomComplet
    || `${personnel.prenom || ''} ${personnel.nom || ''}`.trim()
    || r.nomPrenom
    || ''

  const salaireBase = Number(r.salairbase ?? r.salaireBase ?? 0)
  const sursalaire = Number(r.sursalaire ?? 0)
  const primeAnciennete = Number(r.primeanciennete ?? r.primeAnciennete ?? 0)
  const indemLog = Number(r.indemnitelogement ?? r.indemniteLogement ?? 0)
  const brutImposable = Number(r.brutimposable ?? r.brutImposable ?? 0)
  const its = Number(r.its ?? 0)
  const retenueFiscale = Number(r.igr ?? r.retenueFiscale ?? 0)
  const cnps = Number(r.cnps ?? r.cn ?? 0)
  const totalRetenue = Number(r.totalretenue ?? r.totalRetenue ?? (its + retenueFiscale + cnps))
  const avceAcpte = Number(r.avceAcpte ?? r.avanceetacompte ?? 0)
  const pretAlios = Number(r.pretAlios ?? r.pretaloes ?? 0)
  const regularisation = Number(r.regularisation ?? 0)
  const netRegulPayer = Number(r.netRegulPayer ?? r.netRegulpayer ?? 0)
  const netAPayer = Number(r.netapayer ?? r.netAPayer ?? 0)
  const is = Number(r.is ?? r.impotSalaire ?? 0)
  const ta = Number(r.ta ?? 0)
  const fpc = Number(r.fpc ?? 0)
  const fpcregul = Number(r.fpcregul ?? 0)
  const prestationFamiliale = Number(r.prestationFamiliale ?? 0)
  const accidentTravail = Number(r.accidentTravail ?? 0)
  const retraite = Number(r.retraite ?? 0)

  const mapped = {
    id: r.id ?? personnel.id,
    matricule,
    nomComplet,
    statut: personnel.enSommeil ? 'sommeil' : 'actif',
    statutTravail: personnel.statfonct ?? '',
    nombrePart: Number(r.nbrepart ?? r.nombrePart ?? personnel.nombrePart ?? 0),
    anciennete: r.anciennete ?? '',
    salaireBase,
    sursalaire,
    primeAnciennete,
    indemniteLogement: indemLog,
    brutImposable,
    its,
    retenueFiscale,
    cnps,
    totalRetenue,
    avceAcpte,
    pretAlios,
    regularisation,
    netRegulPayer,
    netAPayer,
    totalBrut: Number(r.totalbrut ?? r.totalBrut ?? 0),
    is,
    ta,
    fpc,
    fpcregul,
    prestationFamiliale,
    accidentTravail,
    retraite,
    totalPatronal: Number(r.totalpatronal ?? r.totalPatronal ?? 0),
    nbJoursTravail: Number(r.jourTravail ?? r.jourtravail ?? 30),
    indemniteTransportImp: Number(r.indemnitetransportimp ?? r.indemniteTransportImp ?? 0),
    autreIndemImposable: Number(r.autreindemimposable ?? r.autreIndemImposable ?? 0),
    indemniteRepresentation: Number(r.indemniterepresentation ?? r.indemniteRepresentation ?? 0),
    indemniteTransport: Number(r.indemnitetransport ?? r.indemniteTransport ?? 0),
    brutNonImposable: Number(r.brutnonimposable ?? r.brutNonImposable ?? 0),
    baseCnps: Number(r.basecnps ?? r.baseCnps ?? 0),
    totalMasseSalariale: Number(r.totalmassesalarial ?? r.totalMasseSalariale ?? 0)
  } as Bulletin

  applyPrimeValues(mapped, resolveImposableRows(r), 'prime')
  applyPrimeValues(mapped, resolveNonImposableRows(r), 'primeI')
  applyPrimeValues(mapped, resolveSocialeRows(r), 'primeS')
  applyPrimeValues(mapped, resolveMutuelleRows(r), 'primeM')
  applyPrimeValues(mapped, resolveGainRows(r), 'primeG')

  return mapped
}

const parsePrimeAmount = (value: any): number => {
  if (value == null) return 0
  if (typeof value === 'number') return value
  let normalized = String(value)
    .replace(/[\s\u00A0\u202F]/g, '')
    .replace(/[^\d,.-]/g, '')

  const hasComma = normalized.includes(',')
  const hasDot = normalized.includes('.')

  if (hasComma && hasDot) {
    const lastComma = normalized.lastIndexOf(',')
    const lastDot = normalized.lastIndexOf('.')
    if (lastComma > lastDot) {
      normalized = normalized.replace(/\./g, '').replace(',', '.')
    } else {
      normalized = normalized.replace(/,/g, '')
    }
  } else if (hasComma) {
    normalized = normalized.replace(',', '.')
  }

  const parsed = Number(normalized)
  return Number.isFinite(parsed) ? parsed : 0
}

const extractPrimeMeta = (primeRow: PrimeRowLike | null | undefined): PrimeColumn | null => {
  if (!primeRow) return null

  const id = Number(
    primeRow?.prime?.id
    ?? primeRow?.rubrique?.id
    ?? primeRow?.idPrime
    ?? primeRow?.id
    ?? 0
  )
  if (!id) return null

  const libelle = String(
    primeRow?.prime?.libelle
    ?? primeRow?.rubrique?.libelle
    ?? primeRow?.libelle
    ?? `Prime ${id}`
  )

  return { id, libelle }
}

const resolveGainRows = (row: any): any[] => {
  if (Array.isArray(row?.listGainsNet)) return row.listGainsNet
  if (Array.isArray(row?.listPrimeGains)) return row.listPrimeGains
  if (Array.isArray(row?.bullpaie?.listGainsNet)) return row.bullpaie.listGainsNet
  if (Array.isArray(row?.bullpaie?.listPrimeGains)) return row.bullpaie.listPrimeGains
  return []
}

const applyPrimeValues = (target: Bulletin, primeRows: any, prefix: 'prime' | 'primeI' | 'primeS' | 'primeM' | 'primeG') => {
  if (!Array.isArray(primeRows) || primeRows.length === 0) return

  for (const primeRow of primeRows) {
    const meta = extractPrimeMeta(primeRow)
    if (!meta) continue

    target[`${prefix}${meta.id}`] = parsePrimeAmount(
      primeRow?.montant ?? primeRow?.valeur ?? primeRow?.mtmontant ?? 0
    )
  }
}

const toPrimeColumns = (...groups: Array<RubriquePrimeMontantDto[] | undefined>): PrimeColumn[] => {
  const map = new Map<number, PrimeColumn>()

  for (const group of groups) {
    if (!Array.isArray(group)) continue
    for (const item of group) {
      const id = Number(item?.rubriqueId ?? 0)
      if (!id) continue
      if (!map.has(id)) {
        map.set(id, {
          id,
          libelle: String(item?.libelle ?? `Prime ${id}`)
        })
      }
    }
  }

  return Array.from(map.values())
}

/**
 * Scanne les lignes brutes backend pour découvrir des rubriques présentes
 * dans les données mais absentes du catalogue (car liées à un autre contrat).
 * Merge les colonnes manquantes dans les refs cibles.
 */
const mergeMissingPrimeColumns = (
  targetRef: Ref<PrimeColumn[]>,
  rows: any[],
  resolvers: Array<(row: any) => any[]>
) => {
  const map = new Map<number, PrimeColumn>()
  for (const col of targetRef.value) map.set(col.id, col)

  for (const row of rows) {
    for (const resolve of resolvers) {
      const list = resolve(row)
      if (!Array.isArray(list)) continue
      for (const item of list) {
        const meta = extractPrimeMeta(item)
        if (meta && !map.has(meta.id)) {
          map.set(meta.id, meta)
        }
      }
    }
  }

  targetRef.value = Array.from(map.values())
}

const loadPrimeCatalogColumns = async (preferredPersonnel?: Personnel | null) => {
  try {
    if (!currentPeriode.value?.id) return

    const contractIdForCatalogue = resolveActiveContratId(preferredPersonnel)
    if (!contractIdForCatalogue) return

    const response = await primepersonnelrestService.getRubriquesContratPeriode(
      Number(currentPeriode.value.id),
      contractIdForCatalogue
    )
    if (!response.success || !response.data) return

    const payload: RubriquesContratPeriodeDto = response.data

    cataloguePrimesImpCols.value = toPrimeColumns(
      payload.listePrimesImp,
      payload.listePrimesImposetNon
    )
    cataloguePrimesImposeNonCols.value = toPrimeColumns(
      payload.listePrimesNonImpos,
      payload.listePrimesImposetNon
    )
    cataloguePrimesSocialeCols.value = toPrimeColumns(payload.listePrimesSociale)
    cataloguePrimesMutuelleCols.value = toPrimeColumns(payload.listePrimesMutuelle)
    cataloguePrimesGainsCols.value = toPrimeColumns(payload.listePrimesGains)

    livrePrimesImpCols.value = [...cataloguePrimesImpCols.value]
    livrePrimesImposeNonCols.value = [...cataloguePrimesImposeNonCols.value]
    livrePrimesSocialeCols.value = [...cataloguePrimesSocialeCols.value]
    livrePrimesMutuelleCols.value = [...cataloguePrimesMutuelleCols.value]
    livrePrimesGainsCols.value = [...cataloguePrimesGainsCols.value]
  } catch (e) {
    console.error('loadPrimeCatalogColumns error', e)
  }
}

// Charge toutes les lignes du Livre de Paie depuis le backend (mode scroll sans pagination).
const loadLivrePaiePage = async () => {
  generating.value = true
  try {
    // Mode scroll : backend renvoie toutes les lignes (limit=0)
    const res = await livrePaieService.generateBulletins(
      currentPeriode.value?.id
    )

    if (!res.success) {
      ElMessage.error(res.message || 'Erreur lors du chargement du livre de paie')
      return false
    }

    const rows: any[] = Array.isArray(res.data?.rows) ? res.data.rows : []
    livrePrimesImpCols.value = [...cataloguePrimesImpCols.value]
    livrePrimesImposeNonCols.value = [...cataloguePrimesImposeNonCols.value]
    livrePrimesSocialeCols.value = [...cataloguePrimesSocialeCols.value]
    livrePrimesMutuelleCols.value = [...cataloguePrimesMutuelleCols.value]
    livrePrimesGainsCols.value = [...cataloguePrimesGainsCols.value]

    // 🔍 Découvrir et ajouter les colonnes manquantes présentes dans les lignes chargées
    mergeMissingPrimeColumns(livrePrimesImpCols, rows, [
      r => r?.listIndemniteBrut, r => r?.listIndemBrut,
      r => r?.bullpaie?.listIndemniteBrut, r => r?.bullpaie?.listIndemBrut
    ])
    mergeMissingPrimeColumns(livrePrimesImposeNonCols, rows, [
      r => r?.listIndemniteNonImp, r => r?.listIndemBrutNonImp,
      r => r?.bullpaie?.listIndemniteNonImp, r => r?.bullpaie?.listIndemBrutNonImp
    ])
    mergeMissingPrimeColumns(livrePrimesSocialeCols, rows, [
      r => r?.listRetenueSociale, r => r?.listRetenueSocial,
      r => r?.bullpaie?.listRetenueSociale, r => r?.bullpaie?.listRetenueSocial
    ])
    mergeMissingPrimeColumns(livrePrimesMutuelleCols, rows, [
      r => r?.listRetenueMutuellt, r => r?.listRetenueMutuelle,
      r => r?.bullpaie?.listRetenueMutuellt, r => r?.bullpaie?.listRetenueMutuelle
    ])
    mergeMissingPrimeColumns(livrePrimesGainsCols, rows, [
      r => r?.listGainsNet, r => r?.listPrimeGains,
      r => r?.bullpaie?.listGainsNet, r => r?.bullpaie?.listPrimeGains
    ])

    livrePaieData.value = rows.map(mapLivreRow)
    livreTotal.value = Number(res.data?.total ?? rows.length)
    return true
  } catch (e: any) {
    console.error('loadLivrePaiePage error', e)
    ElMessage.error(e?.message || 'Erreur lors du chargement du livre de paie')
    livrePrimesImpCols.value = []
    livrePrimesImposeNonCols.value = []
    livrePrimesSocialeCols.value = []
    livrePrimesMutuelleCols.value = []
    livrePrimesGainsCols.value = []
    return false
  } finally {
    generating.value = false
  }
}

const loadBulletinsPage = async () => {
  if (!currentPeriode.value?.id) {
    bulletinsData.value = []
    bulletinsTotal.value = 0
    return false
  }

  bulletinsLoading.value = true
  try {
    const res = await livrePaieService.loadBulletinsByPeriode({
      idPeriode: currentPeriode.value.id,
      page: bulletinsCurrentPage.value,
      size: bulletinsPageSize.value,
      search: bulletinsSearch.value
    })

    if (!res.success) {
      ElMessage.error(res.message || 'Erreur lors du chargement des bulletins')
      bulletinsData.value = []
      bulletinsTotal.value = 0
      return false
    }

    const rows = Array.isArray(res.data) ? res.data : []
    livrePrimesImpCols.value = [...cataloguePrimesImpCols.value]
    livrePrimesImposeNonCols.value = [...cataloguePrimesImposeNonCols.value]
    livrePrimesSocialeCols.value = [...cataloguePrimesSocialeCols.value]
    livrePrimesMutuelleCols.value = [...cataloguePrimesMutuelleCols.value]
    livrePrimesGainsCols.value = [...cataloguePrimesGainsCols.value]

    // 🔍 Découvrir et ajouter les colonnes manquantes présentes dans les lignes chargées
    mergeMissingPrimeColumns(livrePrimesImpCols, rows, [
      r => r?.listIndemniteBrut, r => r?.listIndemBrut,
      r => r?.bullpaie?.listIndemniteBrut, r => r?.bullpaie?.listIndemBrut
    ])
    mergeMissingPrimeColumns(livrePrimesImposeNonCols, rows, [
      r => r?.listIndemniteNonImp, r => r?.listIndemBrutNonImp,
      r => r?.bullpaie?.listIndemniteNonImp, r => r?.bullpaie?.listIndemBrutNonImp
    ])
    mergeMissingPrimeColumns(livrePrimesSocialeCols, rows, [
      r => r?.listRetenueSociale, r => r?.listRetenueSocial,
      r => r?.bullpaie?.listRetenueSociale, r => r?.bullpaie?.listRetenueSocial
    ])
    mergeMissingPrimeColumns(livrePrimesMutuelleCols, rows, [
      r => r?.listRetenueMutuellt, r => r?.listRetenueMutuelle,
      r => r?.bullpaie?.listRetenueMutuellt, r => r?.bullpaie?.listRetenueMutuelle
    ])
    mergeMissingPrimeColumns(livrePrimesGainsCols, rows, [
      r => r?.listGainsNet, r => r?.listPrimeGains,
      r => r?.bullpaie?.listGainsNet, r => r?.bullpaie?.listPrimeGains
    ])

    bulletinsData.value = rows.map(mapBulletinRow)
    bulletinsTotal.value = Number(res.total ?? rows.length)
    return true
  } catch (e: any) {
    console.error('loadBulletinsPage error', e)
    ElMessage.error(e?.message || 'Erreur lors du chargement des bulletins')
    livrePrimesImpCols.value = []
    livrePrimesImposeNonCols.value = []
    livrePrimesSocialeCols.value = []
    livrePrimesMutuelleCols.value = []
    livrePrimesGainsCols.value = []
    bulletinsData.value = []
    bulletinsTotal.value = 0
    return false
  } finally {
    bulletinsLoading.value = false
  }
}

const handleBulletinsSizeChange = (size: number) => {
  bulletinsPageSize.value = size
  bulletinsCurrentPage.value = 1
  loadBulletinsPage()
}

const handleBulletinsPageChange = (page: number) => {
  bulletinsCurrentPage.value = page
  loadBulletinsPage()
}

const searchBulletins = () => {
  bulletinsCurrentPage.value = 1
  loadBulletinsPage()
}

const generateBulletins = async () => {
  const ok = await loadLivrePaiePage()
  if (!ok) return

  livrePaieGenerated.value = true
  bulletinsGenerated.value = true
  bulletinsCurrentPage.value = 1
  await loadBulletinsPage()

  ElMessage.success(`${livreTotal.value} ligne(s) générée(s) dans le livre de paie`) 
  showGenerateModal.value = false
  activeTab.value = 'livre-paie'
}

const downloadBlob = (blob: Blob, filename: string) => {
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

const exportRowsToExcel = (
  rows: Bulletin[],
  filename: string
): Blob | null => {
  if (!rows.length) {
    ElMessage.warning('Aucune donnée à exporter')
    return null
  }

  const numericValue = (value: any): number => {
    const parsed = Number(value ?? 0)
    return Number.isFinite(parsed) ? parsed : 0
  }

  const exportData = rows.map((row) => {
    const item: Record<string, string | number> = {
      Matricule: row.matricule ?? '',
      'Nom & Prénoms': row.nomComplet ?? '',
      'Statut travail': row.statutTravail ?? '',
      'Nb Jours Trv': numericValue(row.nbJoursTravail),
      'Nb Parts': numericValue(row.nombrePart),
      Ancienneté: row.anciennete ?? '',
      'Salaire Base': numericValue(row.salaireBase),
      Sursalaire: numericValue(row.sursalaire),
      'Prime Ancienneté': numericValue(row.primeAnciennete),
      'Indem Logement': numericValue(row.indemniteLogement),
      'Transp Imp': numericValue(row.indemniteTransportImp),
      'Indem Imp': numericValue(row.autreIndemImposable)
    }

    for (const prime of livrePrimesImpCols.value) {
      item[prime.libelle] = numericValue(getPrimeImposableValue(row, prime))
    }

    item['Brut Imposable'] = numericValue(row.brutImposable)
    item['Indem Repres'] = numericValue(row.indemniteRepresentation)
    item['Indem Transport'] = numericValue(row.indemniteTransport)

    for (const prime of livrePrimesImposeNonCols.value) {
      item[prime.libelle] = numericValue(getPrimeNonImposableValue(row, prime))
    }

    item['Brut Non Imp'] = numericValue(row.brutNonImposable)
    item.ITS = numericValue(row.its)
    item['Retenue Fiscale'] = numericValue(row.retenueFiscale)
    item['Base CNPS'] = numericValue(row.baseCnps)

    for (const prime of livrePrimesSocialeCols.value) {
      item[prime.libelle] = numericValue(getPrimeSocialeValue(row, prime))
    }

    item['Retenue sociale'] = numericValue(row.cnps)

    for (const prime of livrePrimesMutuelleCols.value) {
      item[prime.libelle] = numericValue(getPrimeMutuelleValue(row, prime))
    }

    item['Total Retenue'] = numericValue(row.totalRetenue)
    item['Avance & Acompte'] = numericValue(row.avceAcpte)
    item.Pret = numericValue(row.pretAlios)
    item['Net a reguler'] = numericValue(row.netRegulPayer)

    for (const prime of livrePrimesGainsCols.value) {
      item[prime.libelle] = numericValue(getPrimeGainValue(row, prime))
    }

    item['Net à Payer'] = numericValue(row.netAPayer)
    item['Total Brut'] = numericValue(row.totalBrut)
    item.IS = numericValue(row.is)
    item.TA = numericValue(row.ta)
    item.FPC = numericValue(row.fpc)
    item['FPC REGUL'] = numericValue(row.fpcregul)
    item['Prest familiale'] = numericValue(row.prestationFamiliale)
    item['Acc de travail'] = numericValue(row.accidentTravail)
    item.Retraite = numericValue(row.retraite)
    item['Total Patronal'] = numericValue(row.totalPatronal)
    item['Total Masse Salariale'] = numericValue(row.totalMasseSalariale)

    return item
  })

  const worksheet = XLSX.utils.json_to_sheet(exportData)
  const workbook = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Bulletins')
  const buffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' })
  const blob = new Blob([buffer], {
    type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  })

  downloadBlob(blob, filename)
  ElMessage.success('Export Excel téléchargé avec succès')
  return blob
}

const exportExcel = () => {
  exportRowsToExcel(livrePaieData.value, `Livre-de-paie-${selectedPeriod.value}.xlsx`)
}

const exportLivreExcel = () => {
  exportExcel()
}

const exportBulletinsExcel = async () => {
  if (!currentPeriode.value?.id) {
    ElMessage.warning('Aucune période sélectionnée')
    return
  }

  const allRows: Bulletin[] = []
  const batchSize = 500
  let page = 1
  let total = Infinity

  const loading = ElLoading.service({
    lock: true,
    text: 'Chargement de tous les bulletins...',
    background: 'rgba(0, 0, 0, 0.7)'
  })

  try {
    while (allRows.length < total) {
      const res = await livrePaieService.loadBulletinsByPeriode({
        idPeriode: currentPeriode.value.id,
        page,
        size: batchSize,
        search: bulletinsSearch.value
      })

      if (!res.success) break

      const rows = (Array.isArray(res.data) ? res.data : []).map(mapBulletinRow)
      allRows.push(...rows)
      total = Number(res.total ?? allRows.length)
      page++

      if (rows.length === 0) break
    }

    const filename = `Bulletins-${selectedPeriod.value}.xlsx`
    const blob = exportRowsToExcel(allRows, filename)

    if (blob) {
      const saved = await livrePaieService.saveExcelExport(blob, filename)
      if (saved) {
        ElMessage.success('Copie locale sauvegardée sur le serveur')
      } else {
        ElMessage.warning('Téléchargement OK, mais échec de la sauvegarde serveur')
      }
    }
  } catch (e: any) {
    console.error('exportBulletinsExcel error', e)
    ElMessage.error(e?.message || 'Erreur lors de l\'export des bulletins')
  } finally {
    loading.close()
  }
}

const exportJulaya = async () => {
  const loading = ElLoading.service({
    lock: true,
    text: 'Préparation du fichier Julaya...',
    background: 'rgba(0, 0, 0, 0.7)'
  })

  try {
    const blob = await livrePaieService.exportJulaya()
    if (!blob) {
      ElMessage.error('Erreur lors de la génération du fichier Julaya')
      return
    }

    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `julaya_export_${selectedPeriod.value || 'paie'}.xlsm`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    ElMessage.success('Export Julaya téléchargé avec succès')
  } catch (e: any) {
    console.error('exportJulaya error', e)
    ElMessage.error(e?.message || 'Erreur lors de l\'export Julaya')
  } finally {
    loading.close()
  }
}

const confirmGeneration = async () => {
  confirming.value = true
  try {
    const res = await livrePaieService.confirmGeneration()
    if (res.success) {
      ElMessage.success(res.message || 'Génération confirmée avec succès')
      bulletinsCurrentPage.value = 1
      bulletinsGenerated.value = true
      await loadBulletinsPage()
      activeTab.value = 'bulletins'
    } else {
      ElMessage.error(res.message || 'Échec de la confirmation')
    }
  } catch (e: any) {
    console.error('confirmGeneration error', e)
    ElMessage.error(e?.message || 'Erreur lors de la confirmation')
  } finally {
    confirming.value = false
  }
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

// Ratio de proportionnalité jours ↔ heures (30 jours = 173,33 heures)
const STANDARD_JOURS = 30
const STANDARD_HEURES = 173.33
const HEURES_PAR_JOUR = STANDARD_HEURES / STANDARD_JOURS // ≈ 5.7777

const round2 = (n: number) => Math.round(n * 100) / 100

const onJoursChange = (val: number | undefined) => {
  const j = Number(val) || 0
  workTimeForm.tempsTravail = round2(j * HEURES_PAR_JOUR)
}

const onHeuresChange = (val: number | undefined) => {
  const h = Number(val) || 0
  workTimeForm.nbJours = round2(h / HEURES_PAR_JOUR)
}

const editWorkTime = async (personnel: Personnel) => {
  selectedPersonnel.value = personnel
  // Valeurs par défaut
  Object.assign(workTimeForm, { nbJours: 30, tempsTravail: 173.33 })
  showWorkTimeModal.value = true

  // Préchargement de la valeur déjà saisie pour ce personnel sur la période courante
  if (!currentPeriode.value?.id) {
    // Tentative de chargement à la volée si pas encore chargé
    await loadPeriodes()
  }
  if (currentPeriode.value?.id) {
    const res = await livrePaieService.findTempEffectif(personnel.id, currentPeriode.value.id)
    if (res.success && res.data) {
      const t: any = res.data
      // Entité backend : jourspresence / heurspresence
      const jours = t.jourspresence ?? t.jourPresence ?? t.jourtravail ?? t.jourTravail
      const heures = t.heurspresence ?? t.heurePresence ?? t.temptravail ?? t.tempTravail
      if (jours != null) workTimeForm.nbJours = Number(jours)
      if (heures != null) workTimeForm.tempsTravail = Number(heures)
    }
  }
}

const reverseCalculate = (personnel: Personnel) => {
  selectedPersonnel.value = personnel
  // Préremplir avec le net actuel du personnel
  reverseForm.netAPayer = Number(personnel.netAPayer) || 0
  reverseForm.nouveauSursalaire = 0
  showReverseModal.value = true
}

const savePersonnelChanges = async () => {
  if (!selectedPersonnel.value) return

  editing.value = true
  try {
    const res = await livrePaieService.updatePersonnel({
      idPersonnel: selectedPersonnel.value.id,
      situationMatrimoniale: situationMatriToCode(editForm.situationMatrimoniale),
      nombreEnfant: Number(editForm.nbEnfants) || 0,
      statut: editForm.statut === 'actif'
    })

    if (res.success) {
      // Mise à jour locale du tableau pour éviter un reload complet
      const index = personnelData.value.findIndex(p => p.id === selectedPersonnel.value!.id)
      if (index !== -1) {
        Object.assign(personnelData.value[index], {
          nbEnfants: editForm.nbEnfants,
          situationMatrimoniale: editForm.situationMatrimoniale,
          statut: editForm.statut,
          enSommeil: editForm.statut === 'sommeil'
        })
      }
      ElMessage.success(res.message || 'Modifications enregistrées')
      showEditModal.value = false
    } else {
      ElMessage.error(res.message || 'Échec de la modification')
    }
  } catch (e: any) {
    console.error('savePersonnelChanges error', e)
    ElMessage.error('Erreur de modification du personnel')
  } finally {
    editing.value = false
  }
}

const saveWorkTimeChanges = async () => {
  if (!selectedPersonnel.value) return
  if (!currentPeriode.value?.id) {
    ElMessage.error("Aucune période de paie ouverte n'est disponible")
    return
  }

  savingWorkTime.value = true
  try {
    const res = await livrePaieService.saveTempEffectif({
      idPers: selectedPersonnel.value.id,
      idPeriodDep: currentPeriode.value.id,
      jourtravail: Number(workTimeForm.nbJours) || 0,
      temptravail: Number(workTimeForm.tempsTravail) || 0
    })

    if (res.success) {
      ElMessage.success(res.message || 'Temps de travail enregistré')
      showWorkTimeModal.value = false
    } else {
      ElMessage.error(res.message || "Échec de l'enregistrement")
    }
  } catch (e: any) {
    console.error('saveWorkTimeChanges error', e)
    ElMessage.error("Erreur d'enregistrement du temps de travail")
  } finally {
    savingWorkTime.value = false
  }
}

const calculateReverse = () => {
  reverseForm.nouveauSursalaire = reverseForm.netAPayer * 0.8
  ElMessage.info('Calcul effectué')
}

const saveReverseCalculation = async () => {
  if (!selectedPersonnel.value) return

  const sursalaire = Number(reverseForm.nouveauSursalaire)
  if (!sursalaire || sursalaire <= 0) {
    ElMessage.warning("Veuillez d'abord cliquer sur Calculer pour obtenir le nouveau sursalaire")
    return
  }

  savingReverse.value = true
  try {
    const res = await livrePaieService.updateContratSursalaire(
      selectedPersonnel.value.id,
      sursalaire
    )

    if (res.success) {
      ElMessage.success(res.message || 'Sursalaire mis à jour avec succès')
      showReverseModal.value = false
      // Rafraîchir la liste pour voir le nouveau net à payer
      await loadPersonnel()
    } else {
      ElMessage.error(res.message || 'Échec de la mise à jour du sursalaire')
    }
  } catch (e: any) {
    console.error('saveReverseCalculation error', e)
    ElMessage.error('Erreur lors de la mise à jour du sursalaire')
  } finally {
    savingReverse.value = false
  }
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

const printBulletin = async (bulletin: Bulletin) => {
  if (!bulletin.id) {
    ElMessage.warning('Identifiant du bulletin manquant')
    return
  }
  const loading = ElLoading.service({
    lock: true,
    text: 'Génération du PDF...',
    background: 'rgba(0, 0, 0, 0.7)'
  })
  try {
    const blob = await livrePaieService.printBulletinPdf(bulletin.id)
    if (!blob) {
      ElMessage.error('Erreur lors de la génération du bulletin')
      return
    }
    const filename = `Bulletin_${bulletin.matricule}_${selectedPeriod.value || 'paie'}.pdf`
    downloadBlob(blob, filename)
    ElMessage.success(`Bulletin de ${bulletin.nomComplet} téléchargé`)
  } catch (e: any) {
    console.error('printBulletin error', e)
    ElMessage.error(e?.message || 'Erreur lors de l\'impression du bulletin')
  } finally {
    loading.close()
  }
}

const printAllBulletins = () => {
  ElMessage.success('Impression de tous les bulletins')
}

const regularizeNet = async () => {
  try {
    const confirmation = await ElMessageBox.confirm(
      'Cette opération va recalculer le sursalaire de TOUS les contrats actifs '
        + 'pour atteindre le « Net à payer » défini sur chaque contrat. '
        + 'Le calcul peut prendre plusieurs minutes. Continuer ?',
      'Régularisation Net à Payer',
      {
        confirmButtonText: 'Lancer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    ).catch(() => null)

    if (!confirmation) return

    const loading = ElLoading.service({
      lock: true,
      text: 'Régularisation en cours, merci de patienter...',
      background: 'rgba(0, 0, 0, 0.6)'
    })

    try {
      const idPeriode = currentPeriode.value?.id
      const res = await livrePaieService.regularizeNetListe(idPeriode)
      if (res.success) {
        ElMessage.success(res.message || 'Régularisation terminée')
        // Recharger la liste pour refléter les nouveaux sursalaires
        await loadPersonnel()
      } else {
        ElMessage.error(res.message || 'Échec de la régularisation')
      }
    } finally {
      loading.close()
    }
  } catch (e: any) {
    console.error('regularizeNet error:', e)
    ElMessage.error(e?.message || 'Erreur inattendue')
  }
}

const loadPeriodes = async () => {
  // Période active = source de vérité (backend : /api/parametrages/periodes/active)
  const active = await livrePaieService.getPeriodeActive()
  if (active.success && active.data) {
    currentPeriode.value = active.data
  }
  // Liste complète des périodes ouvertes (utilisée pour modal temps de travail, etc.)
  const res = await livrePaieService.loadPeriodes()
  if (res.success && res.data.length > 0) {
    periodesPaie.value = res.data
    // Fallback si /active n'a rien renvoyé
    if (!currentPeriode.value) {
      currentPeriode.value = res.data.find(p => p.cloture === false) || res.data[0]
    }
  }
  // Synchroniser le dropdown sur la période active courante.
  if (currentPeriode.value?.id != null) {
    filters.period = String(currentPeriode.value.id)
  }
}

onMounted(() => {
  ;(async () => {
    await loadPeriodes()
    await loadPersonnel()
    await syncExistingBulletinsForCurrentPeriod()
  })()
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

.bulletins-actions {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 14px;
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

/* ========= MODERN MODAL — STYLES PARTAGÉS ========= */
.modern-modal :deep(.el-dialog) {
  border-radius: 18px;
  overflow: hidden;
  padding: 0;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.25), 0 0 0 1px rgba(255, 255, 255, 0.05);
  background: #f8fafc;
}
.modern-modal :deep(.el-dialog__header) { padding: 0; margin: 0; }
.modern-modal :deep(.el-dialog__body) { padding: 0; }
.modern-modal :deep(.el-dialog__footer) {
  padding: 0;
  border-top: 1px solid #e5e7eb;
  background: white;
}

/* HEADER commun */
.mm-header {
  position: relative;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 22px 26px;
  color: white;
  overflow: hidden;
}
.mm-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 280px;
  height: 280px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.18) 0%, transparent 70%);
  pointer-events: none;
}

.mm-header-icon {
  width: 50px; height: 50px;
  border-radius: 13px;
  background: rgba(255, 255, 255, 0.22);
  backdrop-filter: blur(10px);
  display: flex; align-items: center; justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  position: relative;
  z-index: 1;
}
.mm-header-text { flex: 1; position: relative; z-index: 1; min-width: 0; }
.mm-header-text h2 {
  margin: 0 0 3px 0;
  font-size: 18px;
  font-weight: 700;
  letter-spacing: -0.2px;
  color: white;
}
.mm-header-text p {
  margin: 0;
  font-size: 13px;
  opacity: 0.92;
  font-weight: 400;
}

.mm-close {
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
.mm-close:hover {
  background: rgba(255, 255, 255, 0.28) !important;
  transform: rotate(90deg);
}

/* THEMES */
.modern-modal.theme-success .mm-header {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}
.modern-modal.theme-info .mm-header {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
}
.modern-modal.theme-warning .mm-header {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
}
.modern-modal.theme-primary .mm-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* BODY */
.mm-body {
  padding: 24px 26px;
  max-height: 65vh;
  overflow-y: auto;
}
.mm-body::-webkit-scrollbar { width: 8px; }
.mm-body::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 4px; }

/* FORM dans body */
.mm-body :deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
  font-size: 13px;
  padding-bottom: 6px;
}
.mm-body :deep(.el-input__wrapper),
.mm-body :deep(.el-textarea__inner) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e5e7eb inset;
  transition: all 0.2s;
}
.mm-body :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c7d2fe inset;
}
.mm-body :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px #667eea inset;
}

.mm-grid-2 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 18px;
}

/* SECTION INTERNE */
.mm-section {
  background: white;
  border-radius: 12px;
  padding: 18px 20px;
  border: 1px solid #e5e7eb;
}
.mm-section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #4338ca;
  margin-bottom: 14px;
  font-size: 13px;
}
.mm-section-header .el-icon { font-size: 18px; }

/* BANNERS */
.mm-banner {
  display: flex;
  gap: 14px;
  padding: 14px 18px;
  border-radius: 12px;
  margin-bottom: 16px;
  align-items: flex-start;
}
.mm-banner strong { display: block; font-size: 14px; margin-bottom: 2px; }
.mm-banner p { margin: 0; font-size: 12.5px; line-height: 1.5; }
.mm-banner-icon { font-size: 22px; flex-shrink: 0; margin-top: 2px; }

.mm-banner-warning {
  background: linear-gradient(135deg, #fffbeb 0%, #fef3c7 100%);
  border: 1px solid #fde68a;
  color: #92400e;
}
.mm-banner-warning .mm-banner-icon { color: #d97706; }

.mm-banner-info {
  background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
  border: 1px solid #bfdbfe;
  color: #1e40af;
}
.mm-banner-info .mm-banner-icon { color: #2563eb; }

/* PROCESS CARD (loading) */
.mm-process-card {
  display: flex;
  gap: 14px;
  padding: 14px 18px;
  border-radius: 12px;
  background: #f9fafb;
  border: 1px dashed #d1d5db;
  align-items: center;
}
.mm-process-card strong { display: block; font-size: 13.5px; color: #374151; }
.mm-process-card p { margin: 2px 0 0 0; font-size: 12px; color: #6b7280; }
.mm-process-card .el-icon { font-size: 22px; color: #6366f1; flex-shrink: 0; }
.spin { animation: mm-spin 1.6s linear infinite; }
@keyframes mm-spin { to { transform: rotate(360deg); } }

/* INFO CARD */
.mm-info-card {
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 14px;
  background: #f0f9ff;
  border-radius: 10px;
  font-size: 12.5px;
  color: #0369a1;
  border: 1px solid #bae6fd;
}
.mm-info-card .el-icon { font-size: 16px; }

/* RADIO CARDS (statut actif/sommeil) */
.mm-radio-cards {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  width: 100%;
}
.mm-radio-card {
  display: flex;
  gap: 12px;
  align-items: center;
  padding: 14px 16px;
  background: #fafafa;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}
.mm-radio-card:hover {
  border-color: #c7d2fe;
  transform: translateY(-1px);
}
.mm-radio-card.active {
  border-color: #667eea;
  background: white;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.18);
}
.mm-radio-card .el-icon {
  font-size: 22px;
  color: #6b7280;
  width: 38px; height: 38px;
  border-radius: 10px;
  background: rgba(102, 126, 234, 0.1);
  display: inline-flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}
.mm-radio-card.active .el-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}
.mm-radio-card strong { display: block; font-size: 14px; color: #1f2937; }
.mm-radio-card span { font-size: 12px; color: #6b7280; }

/* PRINT MODAL spécifique */
.print-modal-content { padding: 0 !important; }
.print-modal-content .print-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
  padding: 0 0 16px 0;
  border-bottom: 1px solid #e5e7eb;
  margin-bottom: 16px;
}
.print-summary { display: flex; gap: 10px; flex-wrap: wrap; }

/* FOOTER */
.mm-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 16px 26px;
}

.mm-btn-primary {
  font-weight: 600 !important;
  padding: 0 22px !important;
  height: 42px !important;
  border-radius: 11px !important;
  display: inline-flex !important;
  align-items: center !important;
  gap: 6px;
  transition: all 0.2s;
}
.mm-btn-primary:hover:not(.is-disabled) {
  transform: translateY(-1px);
}

/* Theme-specific button glow */
.modern-modal.theme-success .mm-btn-primary { box-shadow: 0 6px 16px rgba(16, 185, 129, 0.4); }
.modern-modal.theme-info .mm-btn-primary { box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4); }
.modern-modal.theme-warning .mm-btn-primary { box-shadow: 0 6px 16px rgba(245, 158, 11, 0.4); }
.modern-modal.theme-primary .mm-btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  border: none !important;
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
}

@media (max-width: 640px) {
  .mm-grid-2,
  .mm-radio-cards { grid-template-columns: 1fr; }
}
</style>
