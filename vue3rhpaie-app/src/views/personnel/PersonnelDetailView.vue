<template>
  <div class="personnel-detail-view">
    <!-- Header avec navigation -->
    <div class="page-header">
      <div class="header-content">
        <div class="breadcrumb-section">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/personnel' }">Personnel</el-breadcrumb-item>
            <el-breadcrumb-item>Détails</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-actions">
          <el-button @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
            Retour
          </el-button>
          <el-button type="primary" @click="editPersonnel">
            <el-icon><Edit /></el-icon>
            Modifier
          </el-button>
        </div>
      </div>
    </div>

    <!-- Contenu principal -->
    <div class="detail-content" v-if="personnel">
      <!-- Header avec photo et infos principales -->
      <div class="detail-header">
        <div class="avatar-section">
          <el-avatar :size="120" :src="personnelPhotoUrl">
            {{ personnel.prenom?.charAt(0).toUpperCase() }}{{ personnel.nom?.charAt(0).toUpperCase() }}
          </el-avatar>
          <div class="avatar-actions">
            <el-button 
              type="primary" 
              size="small" 
              @click="triggerPhotoUpload"
              :loading="photoUploading"
            >
              <el-icon><Camera /></el-icon>
              Changer la photo
            </el-button>
            <input 
              type="file" 
              ref="photoInput" 
              @change="handlePhotoChange" 
              accept="image/*"
              style="display: none"
            />
          </div>
          <div class="status-section">
            <el-tag :type="getStatutColor('ACTIF')" size="large" effect="dark">
              ACTIF
            </el-tag>
          </div>
        </div>
        <div class="main-info">
          <h1>{{ personnel?.nom }} {{ personnel?.prenom }}</h1>
          <div class="info-row">
            <span class="label">Nom complet:</span>
            <span class="value">{{ personnel?.nom }} {{ personnel?.prenom }}</span>
          </div>
          <div class="info-row">
            <span class="label">Matricule:</span>
            <span class="value">{{ personnel?.matricule }}</span>
          </div>
          <div class="info-row">
            <span class="label">Email:</span>
            <span class="value">{{ personnel?.email }}</span>
          </div>
          <div class="info-row">
            <span class="label">Téléphone:</span>
            <span class="value">{{ personnel?.telephone }}</span>
          </div>
          <div class="contact-info">
            <div class="contact-item" v-if="personnel.email">
              <el-icon><Message /></el-icon>
              <span>{{ personnel.email }}</span>
            </div>
          </div>
        </div>
        <div class="salary-section">
          <div class="salary-card">
            <div class="salary-label">Salaire Net</div>
            <div class="salary-amount">{{ formatCurrency(personnel.salaireNet || 0) }}</div>
          </div>
        </div>
      </div>

      <!-- Layout avec sidebar et contenu principal -->
      <div class="content-layout">
        <!-- Sidebar pour les formulaires -->
        <div class="sidebar-panel enhanced-card" v-if="showForm">
          <!-- DEBUG: Sidebar affichée - showForm={{ showForm }}, activeTab={{ activeTab }} -->
          <div class="panel-header">
            <h3>{{ getFormTitle() }}</h3>
            <el-button @click="closeForm" circle size="small">
              <el-icon><Close /></el-icon>
            </el-button>
          </div>
          
          <!-- Formulaire dynamique selon le tab actif -->
          <div class="form-content">
            <!-- Formulaire Contrat -->
            <div v-if="activeTab === 'contracts'">
              <el-form :model="contractForm" label-width="140px" size="large">
                <div class="form-grid">
                  <el-form-item label="Emploi" required>
                    <el-select v-model="contractForm.emploi" placeholder="Emploi/Fonction" style="width: 100%" :loading="loadingLists" filterable>
                      <el-option 
                        v-for="fonction in fonctions" 
                        :key="fonction.id" 
                        :label="fonction.libelle" 
                        :value="fonction.libelle" 
                      />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="Type Contrat" required>
                    <el-select v-model="contractForm.type" placeholder="Type de contrat" style="width: 100%" :loading="loadingLists">
                      <el-option 
                        v-for="typeContrat in typeContrats" 
                        :key="typeContrat.id" 
                        :label="typeContrat.libelle" 
                        :value="typeContrat.libelle" 
                      />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="Catégorie" required>
                    <el-select v-model="contractForm.categorie" placeholder="Catégorie" style="width: 100%" :loading="loadingLists">
                      <el-option 
                        v-for="categorie in categories" 
                        :key="categorie.id" 
                        :label="categorie.libelle" 
                        :value="categorie.libelle" 
                      />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="Indemnité de logement">
                    <el-input 
                      v-model="contractForm.indemniteLogement" 
                      placeholder="Indemnité de logement"
                      type="number"
                      :min="0"
                      style="width: 100%"
                    />
                  </el-form-item>

                  <el-form-item label="Indemnité de Transport">
                    <el-input 
                      v-model="contractForm.indemniteTransport" 
                      placeholder="Indemnité de transport"
                      type="number"
                      :min="0"
                      style="width: 100%"
                    />
                  </el-form-item>

                  <el-form-item label="Sursalaire">
                    <el-input 
                      v-model="contractForm.sursalaire" 
                      placeholder="Sursalaire"
                      type="number"
                      :min="0"
                      style="width: 100%"
                    />
                  </el-form-item>

                  <el-form-item label="Indemnité de Représentation">
                    <el-input 
                      v-model="contractForm.indemniteRepresentation" 
                      placeholder="Indemnité de représentation"
                      type="number"
                      :min="0"
                      style="width: 100%"
                    />
                  </el-form-item>

                  <el-form-item label="Date début du contrat" required>
                    <el-date-picker
                      v-model="contractForm.dateDebut"
                      type="date"
                      placeholder="Date début du contrat"
                      style="width: 100%"
                      format="DD/MM/YYYY"
                      value-format="YYYY-MM-DD"
                    />
                  </el-form-item>

                  <el-form-item 
                    label="Date fin du contrat" 
                    v-if="contractForm.type === 'CDD'"
                  >
                    <el-date-picker
                      v-model="contractForm.dateFin"
                      type="date"
                      placeholder="Date fin du contrat"
                      style="width: 100%"
                      format="DD/MM/YYYY"
                      value-format="YYYY-MM-DD"
                    />
                  </el-form-item>

                  <el-form-item label="Net à payer" required>
                    <el-input 
                      v-model="contractForm.salaire" 
                      placeholder="Net à payer"
                      type="number"
                      :min="0"
                      style="width: 100%"
                    />
                  </el-form-item>

                  <el-form-item label="Ancienneté initiale">
                    <el-select v-model="contractForm.ancienneteInitiale" placeholder="Ancienneté initiale" style="width: 100%">
                      <el-option v-for="n in 11" :key="n-1" :label="n-1 + ' an(s)'" :value="n-1" />
                    </el-select>
                  </el-form-item>
                </div>

                <el-form-item label="Observations">
                  <el-input 
                    v-model="contractForm.observations" 
                    type="textarea" 
                    :rows="3" 
                    placeholder="Observations sur le contrat"
                  />
                </el-form-item>
              </el-form>
              
              <!-- Boutons d'action -->
              <div class="form-actions" v-if="activeTab === 'contracts'">
                <el-button 
                  type="primary" 
                  size="large" 
                  @click="saveContract"
                  :loading="formLoading"
                  style="width: 120px"
                >
                  <el-icon><Check /></el-icon>
                  Valider
                </el-button>
                <el-button 
                  size="large" 
                  @click="closeForm"
                  style="width: 120px"
                >
                  <el-icon><Close /></el-icon>
                  Annuler
                </el-button>
              </div>
            </div>

            <!-- Formulaire Absence -->
            <div v-if="activeTab === 'absences'">
              <!-- DEBUG: activeTab={{ activeTab }}, showForm={{ showForm }} -->
              <el-form :model="absenceForm" label-width="140px" size="large">
                <el-form-item label="Type d'absence" required>
                  <el-select 
                    v-model="absenceForm.idAbsence" 
                    placeholder="Type d'absence" 
                    style="width: 100%" 
                    :loading="absenceTypes.length === 0"
                    filterable
                    clearable
                  >
                    <el-option 
                      v-for="type in absenceTypes" 
                      :key="type.id" 
                      :label="type.faute" 
                      :value="type.id" 
                    />
                  </el-select>
                </el-form-item>
                
                <el-form-item label="Date Début" required>
                  <el-date-picker v-model="absenceForm.dateDebut" type="date" placeholder="Date début" style="width: 100%" format="DD/MM/YYYY"
                  value-format="YYYY-MM-DD" />
                </el-form-item>
                
                <el-form-item label="Date Fin" required>
                  <el-date-picker v-model="absenceForm.dateFin" type="date" placeholder="Date fin" style="width: 100%" format="DD/MM/YYYY"
                  value-format="YYYY-MM-DD" />
                </el-form-item>
                   <el-form-item label="Statut" >
                  <el-select v-model="absenceForm.statut" placeholder="Impact" style="width: 100%">
                    <el-option label="Justifié" value="true" />
                    <el-option label="Non Justifié" value="false" />                   
                  </el-select>
                </el-form-item>

                 <el-form-item label="Impact" required>
                  <el-select v-model="absenceForm.sanctionSalaire" placeholder="Impact" style="width: 100%">
                    <el-option label="Aucun" value="3" />
                    <el-option label="Salaire" value="4" />
                    <el-option label="Conge" value="2" />
                  </el-select>
                </el-form-item>
                <el-form-item label="Jours d'absence" required>
                  <el-input-number v-model="absenceForm.duree" placeholder="Jours" :min="0" style="width: 100%" />
                </el-form-item>
                
                <el-form-item label="Heures d'absence">
                  <el-input-number v-model="absenceForm.heuresAbsence" placeholder="Heures" :min="0" :precision="1" style="width: 100%" />
                </el-form-item>
                
                <el-form-item label="Motif" >
                  <el-input v-model="absenceForm.motif" type="textarea" :rows="3" placeholder="Motif de l'absence" />
                </el-form-item>
                
                <el-form-item label="Observation">
                  <el-input v-model="absenceForm.observation" type="textarea" :rows="3" placeholder="Observation supplémentaire" />
                </el-form-item>
               

                
                <el-form-item label="Statut">
                  <el-switch v-model="absenceForm.statut" active-text="Approuvé" inactive-text="En attente" />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="handleSaveAbsence" :loading="formLoading">Enregistrer</el-button>
                  <el-button @click="closeForm">Annuler</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- Formulaire Affectation -->
            <div v-if="activeTab === 'assignment'">
              <el-form :model="assignmentForm" label-width="140px" size="large">
                <el-form-item label="Poste" required>
                  <el-select 
                    v-model="assignmentForm.poste" 
                    placeholder="Poste" 
                    style="width: 100%" 
                    :loading="fonctions.length === 0"
                    filterable
                    clearable
                  >
                    <el-option 
                      v-for="func in fonctions" 
                      :key="func.id" 
                      :label="func.libelle" 
                      :value="func.libelle" 
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="Site">
                  <el-select 
                    v-model="assignmentForm.site" 
                    placeholder="Site" 
                    style="width: 100%" 
                    :loading="sites.length === 0"
                    filterable
                    clearable
                  >
                    <el-option 
                      v-for="site in sites" 
                      :key="site.id" 
                      :label="site.libelle" 
                      :value="site.libelle" 
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="Date Début" required>
                  <el-date-picker v-model="assignmentForm.dateDebut" type="date" placeholder="Date début" style="width: 100%" format="DD/MM/YYYY" value-format="YYYY-MM-DD" />
                </el-form-item>
                <el-form-item label="Date Fin">
                  <el-date-picker v-model="assignmentForm.dateFin" type="date" placeholder="Date fin" style="width: 100%" format="DD/MM/YYYY" value-format="YYYY-MM-DD" />
                </el-form-item>
                <el-form-item label="Observation">
                  <el-input v-model="assignmentForm.observation" type="textarea" :rows="3" placeholder="Observation sur l'affectation" />
                </el-form-item>
                <el-form-item label="Document">
                  <el-upload
                    class="upload-demo"
                    drag
                    :auto-upload="false"
                    :on-change="handleFileChange"
                    :file-list="fileList"
                    :limit="1"
                  >
                    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                    <div class="el-upload__text">
                      Glissez le fichier ici ou <em>cliquez pour télécharger</em>
                    </div>
                    <template #tip>
                      Fichiers PDF/Word uniquement (max 10MB)
                    </template>
                  </el-upload>
                </el-form-item>
                <el-form-item label="Statut">
                  <el-switch v-model="assignmentForm.statut" active-text="Actif" inactive-text="Inactif" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveAssignment" :loading="formLoading">Enregistrer</el-button>
                  <el-button @click="closeForm">Annuler</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- Formulaire Mouvement Congé -->
            <div v-if="activeTab === 'leave-movements'">
              <el-form :model="leaveMovementForm" label-width="140px" size="large">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="Date Départ" required>
                      <el-date-picker v-model="leaveMovementForm.dateDepart" type="date" placeholder="Date départ" style="width: 100%" format="DD/MM/YYYY" value-format="YYYY-MM-DD" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="Date Retour">
                      <el-date-picker v-model="leaveMovementForm.dateRetour" type="date" placeholder="Date retour" style="width: 100%" format="DD/MM/YYYY" value-format="YYYY-MM-DD" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="Jours Pris">
                      <el-input-number v-model="leaveMovementForm.nombreJourPris" placeholder="Jours pris" :min="0" style="width: 100%" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="Jours Restants">
                      <el-input-number v-model="leaveMovementForm.nombreJourRestant" placeholder="Jours restants" :min="0" style="width: 100%" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="Montant Versé">
                      <el-input-number v-model="leaveMovementForm.montantVerse" placeholder="Montant versé" :min="0" style="width: 100%" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="Date Création">
                  <el-date-picker v-model="leaveMovementForm.dateCreation" type="date" placeholder="Date création" style="width: 100%" format="DD/MM/YYYY" value-format="YYYY-MM-DD" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveLeaveMovement" :loading="formLoading">Enregistrer</el-button>
                  <el-button @click="closeForm">Annuler</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- Formulaire Document -->
            <div v-if="activeTab === 'documents'">
              <el-form :model="documentForm" label-width="140px" size="large">
                <el-form-item label="Type de document" required>
                  <el-select 
                    v-model="documentForm.typeId" 
                    placeholder="Type de document" 
                    style="width: 100%"
                    filterable
                    clearable
                  >
                    <el-option 
                      v-for="type in documentTypes" 
                      :key="type.id" 
                      :label="type.nom" 
                      :value="type.nom" 
                    />
                  </el-select>
                  <div v-if="documentForm.typeId" class="selected-info">
                    <small>✅ Sélectionné: {{ getSelectedDocumentType() }}</small>
                  </div> 
                </el-form-item>
                <el-form-item label="Emplacement">
                  <el-select 
                    v-model="documentForm.locationId" 
                    placeholder="Emplacement de stockage" 
                    style="width: 100%"
                    filterable
                    clearable
                  >
                    <el-option 
                      v-for="location in storageLocations" 
                      :key="location.id" 
                      :label="location.nom" 
                      :value="location.nom" 
                    />
                  </el-select>
                 <div v-if="documentForm.locationId" class="selected-info">
                    <small>📁 Sélectionné: {{ getSelectedStorageLocation() }}</small>
                  </div>
                </el-form-item>
           
                <el-form-item label="Date dépôt">
                      <el-date-picker v-model="documentForm.dateDepot" type="date" placeholder="Date dépôt" style="width: 100%" format="DD/MM/YYYY" value-format="YYYY-MM-DD" />
                </el-form-item>
                
                <el-form-item label="Référence">
                      <el-input v-model="documentForm.numeroReference" placeholder="Numéro de référence" />
                </el-form-item>
                
               
                <el-form-item label="Présent">
                  <el-switch v-model="documentForm.present" active-text="Oui" inactive-text="Non" />
                </el-form-item>
                <el-form-item label="Remarques">
                  <el-input v-model="documentForm.remarques" type="textarea" :rows="3" placeholder="Remarques sur le document" />
                </el-form-item>
                <el-form-item label="Fichier">
                  <el-upload
                    class="upload-demo"
                    drag
                    action="#"
                    :auto-upload="false"
                    :on-change="handleFileChange"
                    :file-list="fileList"
                    :limit="1"
                  >
                    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                    <div class="el-upload__text">
                      Glissez le fichier ici ou <em>cliquez pour uploader</em>
                    </div>
                    <template #tip>
                      Fichiers PDF/Word/Image uniquement (max 10MB)
                    </template>
                  </el-upload>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveDocument" :loading="formLoading">Enregistrer</el-button>
                
                  <el-button @click="closeForm">Annuler</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- Formulaire Enfant -->
            <div v-if="activeTab === 'children'">
              <el-form :model="childForm" label-width="120px" size="large">
                <el-form-item label="Nom" required>
                  <el-input v-model="childForm.nom" placeholder="Nom de l'enfant" />
                </el-form-item>
                <el-form-item label="Matricule">
                  <el-input v-model="childForm.matricule" placeholder="Matricule de l'enfant" />
                </el-form-item>
                <el-form-item label="Date Naissance" required>
                  <el-date-picker v-model="childForm.dateNaissance" type="date" placeholder="Date de naissance" style="width: 100%" format="DD/MM/YYYY" value-format="YYYY-MM-DD" />
                </el-form-item>
                <el-form-item label="Sexe" required>
                  <el-radio-group v-model="childForm.sexe">
                    <el-radio value="M">Masculin</el-radio>
                    <el-radio value="F">Féminin</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="École">
                  <el-input v-model="childForm.ecole" placeholder="École" />
                </el-form-item>
                <el-form-item label="À Charge">
                  <el-switch v-model="childForm.aCharge" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveChild" :loading="formLoading">Enregistrer</el-button>
                  <el-button @click="closeForm">Annuler</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- Formulaire Conjoint -->
            <div v-if="activeTab === 'spouse'">
              <el-form :model="spouseForm" label-width="120px" size="large">
                <el-form-item label="Nom" required>
                  <el-input v-model="spouseForm.nom" placeholder="Nom du conjoint" />
                </el-form-item>
                <el-form-item label="Matricule">
                  <el-input v-model="spouseForm.matricule" placeholder="Matricule du conjoint" />
                </el-form-item>
                <el-form-item label="Date Naissance" required>
                  <el-date-picker v-model="spouseForm.dateNaissance" type="date" placeholder="Date de naissance" style="width: 100%" format="DD/MM/YYYY" value-format="YYYY-MM-DD" />
                </el-form-item>
                <el-form-item label="Lieu Naissance">
                  <el-input v-model="spouseForm.lieuNaissance" placeholder="Lieu de naissance" />
                </el-form-item>
                <el-form-item label="Téléphone">
                  <el-input v-model="spouseForm.telephone" placeholder="Téléphone" />
                </el-form-item>
                <el-form-item label="Sexe" required>
                  <el-radio-group v-model="spouseForm.sexe">
                    <el-radio value="M">Masculin</el-radio>
                    <el-radio value="F">Féminin</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveSpouse" :loading="formLoading">Enregistrer</el-button>
                  <el-button @click="closeForm">Annuler</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- Formulaire Sanction -->
            <div v-if="activeTab === 'sanctions'">
              <el-form :model="sanctionForm" label-width="120px" size="large">
                <el-form-item label="Type" required>
                  <el-select v-model="sanctionForm.type" placeholder="Type de sanction" style="width: 100%">
                    <el-option label="Avertissement" value="Avertissement" />
                    <el-option label="Retard" value="Retard" />
                    <el-option label="Absence" value="Absence" />
                    <el-option label="Faute grave" value="Faute grave" />
                  </el-select>
                </el-form-item>
                <el-form-item label="Date" required>
                  <el-date-picker v-model="sanctionForm.date" type="date" placeholder="Date" style="width: 100%" format="DD/MM/YYYY" value-format="YYYY-MM-DD" />
                </el-form-item>
                <el-form-item label="Motif" required>
                  <el-input v-model="sanctionForm.motif" placeholder="Motif de la sanction" />
                </el-form-item>
                <el-form-item label="Durée">
                  <el-input v-model="sanctionForm.duree" placeholder="Durée" />
                </el-form-item>
                <el-form-item label="Gravité" required>
                  <el-select v-model="sanctionForm.gravite" placeholder="Gravité" style="width: 100%">
                    <el-option label="Mineure" value="Mineure" />
                    <el-option label="Moyenne" value="Moyenne" />
                    <el-option label="Majeure" value="Majeure" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveSanction" :loading="formLoading">Enregistrer</el-button>
                  <el-button @click="closeForm">Annuler</el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>

        <!-- Contenu principal avec les tabs -->
        <div class="main-panel enhanced-card">
          <div class="panel-header">
            <h3>{{ getTabTitle() }}</h3>
            <div class="panel-controls">
              <el-button @click="toggleForm" type="primary" class="enhanced-button">
                <el-icon><Plus /></el-icon>
                {{ getAddButtonText() }}
              </el-button>
            </div>
          </div>

          <!-- Sections détaillées en tabs -->
          <el-tabs v-model="activeTab" class="detail-tabs">
        <!-- Contrats -->
        <el-tab-pane label="Contrats" name="contracts">
          <div class="tab-content">
            <el-card>
              <template #header>
                <div class="card-header">
                  <el-icon><Document /></el-icon>
                  <span>Contrats du Personnel</span>
                  <el-button 
                    type="primary" 
                    size="small" 
                    @click="toggleForm" 
                    style="margin-left: auto"
                    :disabled="!allContractsInactive"
                    :title="!allContractsInactive ? 'Impossible de créer un contrat tant qu\'il existe des contrats actifs' : 'Créer un nouveau contrat'"
                  >
                    <el-icon><Plus /></el-icon>
                    Nouveau Contrat
                  </el-button>
                </div>
              </template>
              <el-table :data="contracts" stripe>
                <el-table-column label="Type" prop="type" width="120" />
                <el-table-column label="Catégorie" prop="categorie" width="120" />
                <el-table-column label="Fonction" prop="fonction" width="150" />
                <el-table-column label="Date Début" prop="dateDebut" width="120" />
                <el-table-column label="Date Fin" prop="dateFin" width="120" />
                <el-table-column label="Durée" prop="duree" width="100" />
                <el-table-column label="Salaire Net" prop="salaire" width="120">
                  <template #default="{ row }">
                    {{ formatCurrency(row.salaire) }}
                  </template>
                </el-table-column>
                <el-table-column label="Indem. Logement" prop="indemniteLogement" width="120">
                  <template #default="{ row }">
                    {{ formatCurrency(row.indemniteLogement) }}
                  </template>
                </el-table-column>
                <el-table-column label="Indem. Transport" prop="indemniteTransport" width="120">
                  <template #default="{ row }">
                    {{ formatCurrency(row.indemniteTransport) }}
                  </template>
                </el-table-column>
                <el-table-column label="Sursalaire" prop="sursalaire" width="120">
                  <template #default="{ row }">
                    {{ formatCurrency(row.sursalaire) }}
                  </template>
                </el-table-column>
                <el-table-column label="Statut" prop="statut" width="100">
                  <template #default="{ row }">
                    <el-tag :type="row.statut === 'Actif' ? 'success' : 'danger'">
                      {{ row.statut }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="Actions" width="120">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="viewContract(row)">
                      <el-icon><View /></el-icon>
                      Voir
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
            
            <!-- Message informatif quand le bouton est désactivé -->
            <el-alert 
              v-if="!allContractsInactive && contracts.length > 0"
              type="warning" 
              :closable="false"
              style="margin-top: 16px"
            >
              <template #title>
                <strong>Création de contrat désactivée</strong>
              </template>
              <template #default>
                Le personnel a actuellement des contrats actifs. Pour créer un nouveau contrat, veuillez d'abord terminer les contrats actifs existants.
              </template>
            </el-alert>
          </div>
        </el-tab-pane>

        <!-- Absences -->
        <el-tab-pane label="Absences" name="absences">
          <div class="tab-content">
            <el-card>
              <template #header>
                <div class="card-header">
                  <el-icon><Calendar /></el-icon>
                  <span>Gestion des Absences</span>
                  <el-button type="primary" size="small" @click="toggleForm" style="margin-left: auto">
                    <el-icon><Plus /></el-icon>
                    Nouvelle Absence
                  </el-button>
                </div>
              </template>
              <el-table :data="absences" stripe>
                <el-table-column label="Type" prop="type" width="150" />
                <el-table-column label="Date Début" prop="dateDebut" width="120" />
                <el-table-column label="Date Fin" prop="dateFin" width="120" />
                <el-table-column label="Jours" prop="duree" width="80" />
                <el-table-column label="Heures" prop="heuresAbsence" width="80" />
                <el-table-column label="Observation" prop="motif" />
                <el-table-column label="Sanction Salaire" prop="sanctionSalaire" width="120" />
                <el-table-column label="Impact" prop="impact" width="100" />
                <el-table-column label="Statut" prop="statut" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getAbsenceStatusColor(row.statut)">
                      {{ row.statut }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="Actions" width="150">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="editAbsence(row)">
                      <el-icon><Edit /></el-icon>
                    </el-button>
                    <el-button type="danger" size="small" @click="deleteAbsence(row)">
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>
        <!-- Affectation -->
        <el-tab-pane label="Affectation" name="assignment">
          <div class="tab-content">
            <el-card>
              <template #header>
                <div class="card-header">
                  <el-icon><OfficeBuilding /></el-icon>
                  <span>Historique d'Affectation</span>
                  <el-button type="primary" size="small" @click="toggleForm" style="margin-left: auto">
                    <el-icon><Plus /></el-icon>
                    Nouvelle Affectation
                  </el-button>
                </div>
              </template>
              <el-table :data="assignments" stripe>
                <el-table-column label="Poste" prop="poste" width="150" />
                <el-table-column label="Site" prop="site" width="120" />
                <el-table-column label="Date Début" prop="dateDebut" width="120" />
                <el-table-column label="Date Fin" prop="dateFin" width="120" />
                <el-table-column label="Observation" prop="observation" />
                <el-table-column label="Document" prop="urlDocument" width="120"/>
           
                <el-table-column label="Statut" prop="statut" width="100">
                  <template #default="{ row }">
                    <el-tag :type="row.statut === 'Actif' ? 'success' : 'info'">
                      {{ row.statut }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="Actions" width="260">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="viewAssignment(row)">
                      <el-icon><View /></el-icon>
                      Voir
                    </el-button>
                    <el-button type="warning" size="small" @click="editAssignment(row)">
                      <el-icon><Edit /></el-icon>
                      Modifier
                    </el-button>
                    <el-button v-if="row.urlDocument" type="success" size="small" @click="downloadDocument(row.id)">
                      <el-icon><Download /></el-icon>
                      Doc
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- Mouvements Congé -->
        <el-tab-pane label="Mouvements Congé" name="leave-movements">
          <div class="tab-content">
            <el-card>
              <template #header>
                <div class="card-header">
                  <el-icon><Clock /></el-icon>
                  <span>Mouvements de Congé</span>
                  <el-button type="primary" size="small" @click="toggleForm" style="margin-left: auto">
                    <el-icon><Plus /></el-icon>
                    Nouveau Mouvement
                  </el-button>
                </div>
              </template>
              <el-table :data="leaveMovements" stripe>
                <el-table-column label="Date Départ" prop="dateDepart" width="120" />
                <el-table-column label="Date Retour" prop="dateRetour" width="120" />
                <el-table-column label="Jours Pris" prop="nombreJourPris" width="100" />
                <el-table-column label="Jours Restants" prop="nombreJourRestant" width="120" />
                <el-table-column label="Montant Versé" prop="montantVerse" width="120">
                  <template #default="{ row }">
                    {{ formatCurrency(row.montantVerse) }}
                  </template>
                </el-table-column>
                <el-table-column label="Date Création" prop="dateCreation" width="120" />
                <el-table-column label="Actions" width="120">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="viewLeaveMovement(row)">
                      <el-icon><View /></el-icon>
                      Voir
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- Documents -->
        <el-tab-pane label="Documents" name="documents">
          <div class="tab-content">
            <el-card>
              <template #header>
                <div class="card-header">
                  <el-icon><Folder /></el-icon>
                  <span>Documents du Personnel</span>
                  <el-button type="primary" size="small" @click="toggleForm" style="margin-left: auto">
                    <el-icon><Plus /></el-icon>
                    Télécharger Document
                  </el-button>
                </div>
              </template>
              <el-table :data="documents" stripe>
                <el-table-column label="Type" prop="type" width="150" />
                <el-table-column label="Emplacement" prop="emplacement" width="120" />
                <el-table-column label="Date Dépôt" prop="dateDepot" width="120" />
                <el-table-column label="Référence" prop="numeroReference" width="150" />
                <el-table-column label="Présent" prop="present" width="80">
                  <template #default="{ row }">
                    <el-tag :type="row.present === 'Oui' ? 'success' : 'danger'">
                      {{ row.present }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="Fichier" prop="urlFichier" width="200">
                  <template #default="{ row }">
                    <span v-if="row.urlFichier" class="filename">
                      {{ getFileName(row.urlFichier) }}
                    </span>
                    <span v-else class="no-file">
                      Aucun fichier
                    </span>
                  </template>
                </el-table-column>
                <el-table-column label="Remarques" prop="remarques" />
                <el-table-column label="Actions" width="150">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="viewDocument(row)">
                      <el-icon><View /></el-icon>
                      Voir
                    </el-button>
                    <el-button v-if="row.urlFichier" type="success" size="small" @click="downloadPersonnelDocument(row.id)">
                      <el-icon><Download /></el-icon>
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- Enfants -->
        <el-tab-pane label="Enfants" name="children">
          <div class="tab-content">
            <el-card>
              <template #header>
                <div class="card-header">
                  <el-icon><UserFilled /></el-icon>
                  <span>Enfants à Charge</span>
                  <el-button type="primary" size="small" @click="toggleForm" style="margin-left: auto">
                    <el-icon><Plus /></el-icon>
                    Ajouter un Enfant
                  </el-button>
                </div>
              </template>
              <el-table :data="children" stripe>
                <el-table-column label="Nom" prop="nom" width="150" />
                <el-table-column label="Matricule" prop="matricule" width="120" />
                <el-table-column label="Date Naissance" prop="dateNaissance" width="120" />
                <el-table-column label="Sexe" prop="sexe" width="80">
                  <template #default="{ row }">
                    <el-tag :type="row.sexe === 'M' ? 'primary' : 'danger'" size="small">
                      {{ row.sexe }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="École" prop="ecole" />
                <el-table-column label="À Charge" prop="aCharge" width="100">
                  <template #default="{ row }">
                    <el-tag :type="row.aCharge ? 'success' : 'info'" size="small">
                      {{ row.aCharge ? 'Oui' : 'Non' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="Actions" width="150">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="editChild(row)">
                      <el-icon><Edit /></el-icon>
                      Modifier
                    </el-button>
                    <el-button type="danger" size="small" @click="deleteChild(row.id)">
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- Conjoint -->
        <el-tab-pane label="Conjoint" name="spouse">
          <div class="tab-content">
            <el-card>
              <template #header>
                <div class="card-header">
                  <el-icon><User /></el-icon>
                  <span>Informations sur le Conjoint</span>
                  <el-button type="primary" size="small" @click="toggleForm" style="margin-left: auto">
                    <el-icon><Plus /></el-icon>
                    Ajouter un Conjoint
                  </el-button>
                </div>
              </template>
              <el-table :data="spouses" stripe>
                <el-table-column label="Matricule" prop="matricule" width="120" />
                <el-table-column label="Nom" prop="nom" width="150" />
                <el-table-column label="Date Naissance" prop="dateNaissance" width="120" />
                <el-table-column label="Lieu Naissance" prop="lieuNaissance" width="150" />
                <el-table-column label="Téléphone" prop="telephone" width="120" />
                <el-table-column label="Sexe" prop="sexe" width="80">
                  <template #default="{ row }">
                    <el-tag :type="row.sexe === 'M' ? 'primary' : 'danger'" size="small">
                      {{ row.sexe }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="Statut" prop="actif" width="100">
                  <template #default="{ row }">
                    <el-tag :type="row.actif ? 'success' : 'info'" size="small">
                      {{ row.actif ? 'Actif' : 'Inactif' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="Actions" width="150">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="editSpouse(row)">
                      <el-icon><Edit /></el-icon>
                      Modifier
                    </el-button>
                    <el-button type="danger" size="small" @click="deleteSpouse(row.id)">
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- Sanctions -->
        <el-tab-pane label="Sanctions" name="sanctions">
          <div class="tab-content">
            <el-card>
              <template #header>
                <div class="card-header">
                  <el-icon><Warning /></el-icon>
                  <span>Historique des Sanctions</span>
                  <el-button type="primary" size="small" @click="toggleForm" style="margin-left: auto">
                    <el-icon><Plus /></el-icon>
                    Nouvelle Sanction
                  </el-button>
                </div>
              </template>
              <el-table :data="sanctions" stripe>
                <el-table-column label="Type Sanction" prop="typeSanction" width="150" />
                <el-table-column label="Date Début" prop="dateDebut" width="150" />
                <el-table-column label="Date Fin" prop="dateFin" width="150" />
                <el-table-column label="Gravité" prop="gravite" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getSanctionGravityColor(row.gravite)">
                      {{ row.gravite }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="Observation" prop="observation" />
                <el-table-column label="Documents" width="120">
                  <template #default="{ row }">
                    <el-button v-if="row.urlDemande" type="info" size="small" @click="openDocument(row.urlDemande)" style="margin-right: 5px">
                      Demande
                    </el-button>
                    <el-button v-if="row.urlReponse" type="success" size="small" @click="openDocument(row.urlReponse)">
                      Réponse
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column label="Actions" width="120">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="viewSanction(row)">
                      <el-icon><View /></el-icon>
                      Voir
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- Promotions -->
        <el-tab-pane label="Promotions" name="promotions">
          <div class="tab-content">
            <el-card>
              <template #header>
                <div class="card-header">
                  <el-icon><Trophy /></el-icon>
                  <span>Historique des Promotions</span>
                  <el-button type="primary" size="small" @click="toggleForm" style="margin-left: auto">
                    <el-icon><Plus /></el-icon>
                    Nouvelle Promotion
                  </el-button>
                </div>
              </template>
              <el-table :data="promotions" stripe>
                <el-table-column label="Promotion" prop="promotion" width="150" />
                <el-table-column label="Date Promotion" prop="datePromotion" width="150" />
                <el-table-column label="Date Création" prop="dateCreation" width="150" />
                <el-table-column label="Commentaire" prop="commentaire" />
                <el-table-column label="Document" width="120">
                  <template #default="{ row }">
                    <el-button v-if="row.urlDocument" type="success" size="small" @click="openDocument(row.urlDocument)">
                      <el-icon><Download /></el-icon>
                      Voir
                    </el-button>
                    <span v-else class="no-document">Aucun</span>
                  </template>
                </el-table-column>
                <el-table-column label="Actions" width="120">
                  <template #default="{ row }">
                    <el-button type="primary" size="small" @click="viewPromotion(row)">
                      <el-icon><View /></el-icon>
                      Voir
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>

    <!-- État de chargement -->
    <div v-else class="loading-state">
      <el-skeleton :rows="10" animated />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  ArrowLeft, Edit, User, OfficeBuilding, Location, Phone, Message, 
  CreditCard, Money, Star, Document, Calendar, Folder, Delete, Plus, Clock,
  UserFilled, Warning, View, Download, Upload, Close, UploadFilled, Trophy, Camera, Check
} from '@element-plus/icons-vue'
import { personnelRestService, type PersonnelRestDto } from '@/services/personnel.service'
import { api } from '@/services/api'
import { referencesService, type TypeContrat } from '@/services/references.service'

interface Personnel {
  id: number
  matricule: string
  nom: string
  prenom: string
  nomComplet: string
  sexe: string
  dateNaissance: string
  numeroCNPS: string
  situationMatrimoniale: string
  modePaiement: string
  numeroCompte: string
  telephone: string
  emploi: string
  salaireNet: number
  statut: string
  service: string
  email?: string
}

const route = useRoute()
const router = useRouter()

const activeTab = ref('contracts')
const personnel = ref<PersonnelRestDto | null>(null)
const loading = ref(false)
const showForm = ref(false)
const formLoading = ref(false)
const fileList = ref<any[]>([])

// Formulaires pour chaque section
const contractForm = reactive({
  id: 0,
  type: '',
  categorie: '',
  emploi: '', // Changé de 'fonction' à 'emploi' comme dans le wizard
  dateDebut: '',
  dateFin: '',
  salaire: 0,
  indemniteLogement: 0,
  indemniteTransport: 0,
  sursalaire: 0,
  indemniteRepresentation: 0, // Nouveau champ
  ancienneteInitiale: 0, // Nouveau champ
  observations: ''
})

// Variables pour les listes dynamiques
const typeContrats = ref<TypeContrat[]>([])
const fonctions = ref<any[]>([])
const sites = ref<any[]>([])
const categories = ref<any[]>([])
const loadingLists = ref(false)

// Fonction pour charger les listes dynamiques
const loadContractLists = async () => {
  loadingLists.value = true
  try {
    console.log('🔄 Début chargement des listes...')
    
    // Utiliser referencesService.loadAllReferences() comme dans le wizard
    console.log('📋 Chargement de toutes les références...')
    const refs = await referencesService.loadAllReferences()
    
    console.log('📊 Réponse complète de loadAllReferences:', refs)
    console.log('📋 Types de contrat bruts:', refs.typeContrats)
    console.log('📋 Catégories brutes:', refs.categories)
    console.log('📋 Fonctions brutes:', refs.fonctions)
    
    // Stocker les références
    categories.value = refs.categories || []
    fonctions.value = refs.fonctions || []
    typeContrats.value = refs.typeContrats || []

    // Si aucun type de contrat n'est trouvé, utiliser des valeurs par défaut
    if (typeContrats.value.length === 0) {
      console.warn('⚠️ Aucun type de contrat trouvé, utilisation des valeurs par défaut')
      typeContrats.value = [
        { id: 1, libelle: 'CDI', code: 'CDI', actif: true },
        { id: 2, libelle: 'CDD', code: 'CDD', actif: true },
        { id: 3, libelle: 'Stage', code: 'STAGE', actif: true }
      ]
    }

    console.log('✅ Toutes les listes chargées:', {
      categories: categories.value.length,
      fonctions: fonctions.value.length,
      typeContrats: typeContrats.value.length
    })

    // Vérifier spécifiquement les types de contrat
    if (refs.typeContrats && refs.typeContrats.length > 0) {
      console.log('✅ Types de contrat disponibles:', refs.typeContrats.map((tc: any) => ({ 
        id: tc.id, 
        libelle: tc.libelle, 
        code: tc.code,
        actif: tc.actif,
        structure: Object.keys(tc)
      })))
    } else {
      console.warn('⚠️ Aucun type de contrat trouvé dans la réponse API')
      console.warn('📋 Réponse typeContrats:', refs.typeContrats)
    }
  } catch (error: any) {
    console.error('❌ Erreur lors du chargement des listes:', error)
    ElMessage.error('Erreur lors du chargement des données: ' + (error.response?.data?.message || error.message))
  } finally {
    loadingLists.value = false
  }
}

// Charger les fonctions depuis l'API
const loadFunctions = async () => {
  try {
    console.log('🔄 Chargement des fonctions...')
    const response = await api.post('/api/personnels/fonctions/listerfonctionjson')
    
    if (response.data && response.data.rows) {
      fonctions.value = response.data.rows.map((func: any) => ({
        id: func.id,
        libelle: func.libelle || func.fonction || func.faute,
        code: func.code || String(func.id)
      }))
      console.log('✅ Fonctions chargées:', fonctions.value)
    } else {
      console.warn('⚠️ Aucune fonction trouvée')
      fonctions.value = []
    }
  } catch (error: any) {
    console.error('❌ Erreur lors du chargement des fonctions:', error)
    ElMessage.error('Erreur lors du chargement des fonctions')
    fonctions.value = []
  }
}

// Charger les sites depuis l'API
const loadSites = async () => {
  try {
    console.log('🔄 Chargement des sites...')
    const response = await api.post('/rh/carriere/site/lister')
    
    if (response.data && response.data.rows) {
      sites.value = response.data.rows.map((site: any) => ({
        id: site.id,
        libelle: site.libelle || site.nom || site.site,
        code: site.code || String(site.id)
      }))
      console.log('✅ Sites chargés:', sites.value)
    } else {
      console.warn('⚠️ Aucun site trouvé')
      sites.value = []
    }
  } catch (error: any) {
    console.error('❌ Erreur lors du chargement des sites:', error)
    ElMessage.error('Erreur lors du chargement des sites')
    sites.value = []
  }
}

const absenceForm = reactive({
  id: 0,
  idAbsence: null as number | null,
  dateDebut: '',
  dateFin: '',
  duree: 0,
  heuresAbsence: 0,
  motif: '',
  observation: '',
  sanctionSalaire: 0,
  impact: '',
  statut: false
})

const assignmentForm = reactive({
  id: 0,
  poste: '',
  site: '',
  dateDebut: '',
  dateFin: '',
  observation: '',
  statut: true
})

const leaveMovementForm = reactive({
  id: 0,
  dateDepart: '',
  dateRetour: '',
  nombreJourPris: 0,
  nombreJourRestant: 0,
  montantVerse: 0,
  dateCreation: ''
})

const documentForm = reactive({
  id: 0,
  typeId: '', // ID du type de document (numérique)
  locationId: '', // ID de l'emplacement (numérique)
  dateDepot: '',
  numeroReference: '',
  present: true,
  remarques: '',
  fichier: null
})

// Fonction pour obtenir l'ID numérique depuis le nom
const getDocumentTypeId = (typeName) => {
  const type = documentTypes.value.find(t => t.nom === typeName)
  return type ? type.id : ''
}

const getStorageLocationId = (locationName) => {
  const location = storageLocations.value.find(l => l.nom === locationName)
  return location ? location.id : ''
}

// Fonction pour extraire le nom du fichier du chemin
const getFileName = (filePath) => {
  if (!filePath) return ''
  // Gérer les chemins Windows et Unix
  const normalizedPath = filePath.replace(/\\/g, '/')
  const parts = normalizedPath.split('/')
  return parts[parts.length - 1] || filePath
}

// Variables pour les listes dynamiques
const documentTypes = ref([])
const storageLocations = ref([])

const childForm = reactive({
  id: null, // null pour création, numérique pour modification
  nom: '',
  matricule: '',
  dateNaissance: '',
  sexe: 'M',
  ecole: '',
  aCharge: true
})

const sanctionForm = reactive({
  id: 0,
  type: '',
  date: '',
  motif: '',
  duree: '',
  gravite: ''
})

const spouseForm = reactive({
  id: null, // null pour création, numérique pour modification
  nom: '',
  matricule: '',
  dateNaissance: '',
  lieuNaissance: '',
  telephone: '',
  sexe: 'M'
})

// Variables pour la gestion des photos
const personnelPhotoUrl = ref<string>('')
const photoUploading = ref(false)
const photoInput = ref<HTMLInputElement>()

// Données pour chaque section
const contracts = ref<Contract[]>([])

// Propriété calculée pour vérifier si tous les contrats sont inactifs
const allContractsInactive = computed(() => {
  return contracts.value.length === 0 || contracts.value.every(contract => contract.statut === 'Inactif')
})

const absences = ref([
  { id: 1, type: 'Maladie', dateDebut: '15/01/2024', dateFin: '17/01/2024', duree: 3, motif: 'Grippe', statut: 'Approuvé' },
  { id: 2, type: 'Congé', dateDebut: '10/02/2024', dateFin: '14/02/2024', duree: 5, motif: 'Vacances', statut: 'En attente' }
])

// Interface pour les types d'absence
interface AbsenceType {
  id: number
  faute: string
  libelle: string
  code: string
  actif: boolean
}

const absenceTypes = ref<AbsenceType[]>([])

const assignments = ref<any[]>([])

// Interface pour les contrats
interface Contract {
  id?: number
  type: string
  categorie: string
  fonction: string
  dateDebut: string
  dateFin?: string
  duree?: string
  salaire: number
  indemniteLogement: number
  indemniteTransport: number
  sursalaire: number
  statut: string
}

const documents = ref<any[]>([])

const children = ref<any[]>([])

const sanctions = ref<any[]>([])

const promotions = ref<any[]>([])

const spouses = ref<any[]>([])

// Données mockées (à remplacer par un appel API)
const mockPersonnel: Personnel[] = [
  { 
    id: 1, 
    matricule: 'EMP001', 
    nom: 'Kouadio', 
    prenom: 'Jean', 
    nomComplet: 'Kouadio Jean', 
    sexe: 'Masculin', 
    dateNaissance: '15/01/1985', 
    numeroCNPS: '123456789', 
    situationMatrimoniale: 'Marié', 
    modePaiement: 'Virement Bancaire', 
    numeroCompte: '0123456789012345678901234', 
    telephone: '0712345678', 
    emploi: 'Directeur Technique', 
    salaireNet: 500000, 
    statut: 'ACTIF', 
    service: 'DT', 
    email: 'kouadio.jean@email.com' 
  },
  { 
    id: 2, 
    matricule: 'EMP002', 
    nom: 'Touré', 
    prenom: 'Aminata', 
    nomComplet: 'Touré Aminata', 
    sexe: 'Féminin', 
    dateNaissance: '22/03/1990', 
    numeroCNPS: '987654321', 
    situationMatrimoniale: 'Célibataire', 
    modePaiement: 'Mobile Money', 
    numeroCompte: '0567890123456789', 
    telephone: '0789012345', 
    emploi: 'Responsable RH', 
    salaireNet: 350000, 
    statut: 'ACTIF', 
    service: 'DRH', 
    email: 'toure.aminata@email.com' 
  }
]

const loadPersonnel = async () => {
  try {
    const id = parseInt(route.params.id as string)
    loading.value = true
    
    const response = await personnelRestService.getPersonnelById(id)
    if (response.success && response.data) {
      personnel.value = response.data
      // Charger les données de chaque onglet une fois le personnel chargé
      await loadContracts(id)
      await loadAbsences(id)
      await loadAssignments(id)
      await loadLeaveMovements(id)
      await loadDocuments(id)
      await loadChildren(id)
      await loadSpouses(id)
      await loadSanctions(id)
      await loadPromotions(id)
      await loadPersonnelPhoto()
    } else {
      ElMessage.error(response.message || 'Personnel non trouvé')
      goBack()
    }
  } catch (error: any) {
    console.error('Erreur lors du chargement du personnel:', error)
    ElMessage.error('Erreur lors du chargement du personnel')
    goBack()
  } finally {
    loading.value = false
  }
}

const loadSanctions = async (personnelId: number) => {
  try {
    console.log('🔄 Chargement des sanctions pour le personnel ID:', personnelId)
    const response = await api.post('/rh/carriere/sanctions-personnel/trouver', { id: personnelId })
    console.log('📥 Réponse API sanctions:', response)
    
    if (response.data && response.data.rows) {
      console.log('📋 Données brutes des sanctions:', response.data.rows)
      sanctions.value = response.data.rows.map((sanction: any) => ({
        id: sanction.id,
        observation: sanction.observation || 'N/A',
        urlDemande: sanction.urlDemande || '',
        urlReponse: sanction.urlReponse || '',
        dateDebut: sanction.dDebut || 'N/A',
        dateFin: sanction.dFin || 'N/A',
        dateCreation: sanction.dCreation || 'N/A',
        dateModification: sanction.dModification || 'N/A',
        typeSanction: sanction.sanction?.libelle || 'N/A',
        gravite: sanction.sanction?.gravite || 'N/A'
      }))
      console.log('✅ Sanctions mappées avec succès:', sanctions.value)
    } else {
      console.log('❌ Structure de réponse inattendue:', response)
      sanctions.value = [] // Tableau vide si aucune donnée
    }
  } catch (error) {
    console.error('❌ Erreur lors du chargement des sanctions:', error)
    ElMessage.error('Erreur lors du chargement des sanctions')
  }
}

const loadSpouses = async (personnelId: number) => {
  try {
    console.log('🔄 Chargement des conjoints pour le personnel ID:', personnelId)
    const response = await api.post('/personnel/listerconjointsparpersonnel', { id: personnelId })
    console.log('📥 Réponse API conjoints:', response)
    
    if (response.data && response.data.rows) {
      console.log('📋 Données brutes des conjoints:', response.data.rows)
      spouses.value = response.data.rows.map((spouse: any) => ({
        id: spouse.id,
        nom: spouse.nom || 'N/A',
        matricule: spouse.matricule || 'N/A',
        dateNaissance: spouse.dateNaissance || 'N/A',
        lieuNaissance: spouse.lieuNaissance || 'N/A',
        telephone: spouse.telephone || 'N/A',
        sexe: spouse.sexe || 'N/A',
        actif: spouse.actif || false,
        statut: spouse.statut || 'N/A'
      }))
      console.log('✅ Conjoints mappés avec succès:', spouses.value)
    } else {
      console.log('❌ Structure de réponse inattendue:', response)
      spouses.value = [] // Tableau vide si aucune donnée
    }
  } catch (error) {
    console.error('❌ Erreur lors du chargement des conjoints:', error)
    ElMessage.error('Erreur lors du chargement des conjoints')
  }
}

const loadChildren = async (personnelId: number) => {
  try {
    console.log('🔄 Chargement des enfants pour le personnel ID:', personnelId)
    const response = await api.post('/personnel/listerenfantsparpersonnel', { id: personnelId })
    console.log('📥 Réponse API enfants:', response)
    
    if (response.data && response.data.rows) {
      console.log('📋 Données brutes des enfants:', response.data.rows)
      children.value = response.data.rows.map((child: any) => ({
        id: child.id,
        nom: child.nom || 'N/A',
        matricule: child.matricule || 'N/A',
        dateNaissance: child.dateNaissance || 'N/A',
        sexe: child.sexe || 'N/A',
        ecole: child.ecole || 'N/A',
        aCharge: child.aCharge || false
      }))
      console.log('✅ Enfants mappés avec succès:', children.value)
    } else {
      console.log('❌ Structure de réponse inattendue:', response)
      children.value = [] // Tableau vide si aucune donnée
    }
  } catch (error) {
    console.error('❌ Erreur lors du chargement des enfants:', error)
    ElMessage.error('Erreur lors du chargement des enfants')
  }
}

const loadDocuments = async (personnelId: number) => {
  try {
    console.log('🔄 Chargement des documents pour le personnel ID:', personnelId)
    const response = await api.post('/personnel/documents/employeId', { id: personnelId })
    console.log('📥 Réponse API documents:', response)
    
    if (response.data && response.data.rows) {
      console.log('📋 Données brutes des documents:', response.data.rows)
      documents.value = response.data.rows.map((doc: any) => ({
        id: doc.id,
        type: doc.documentType?.libelle || 'N/A',
        emplacement: doc.storageLocation?.libelle || 'N/A',
        dateDepot: doc.ddatedepot || 'N/A',
        numeroReference: doc.numeroReference || 'N/A',
        present: doc.present ? 'Oui' : 'Non',
        remarques: doc.remarques || 'N/A',
        urlFichier: doc.urlFichier || '',
        dateDepotOriginal: doc.dateDepot
      }))
      console.log('✅ Documents mappés avec succès:', documents.value)
    } else {
      console.log('❌ Structure de réponse inattendue:', response)
      documents.value = [] // Tableau vide si aucune donnée
    }
  } catch (error) {
    console.error('❌ Erreur lors du chargement des documents:', error)
    ElMessage.error('Erreur lors du chargement des documents')
  }
}

const loadLeaveMovements = async (personnelId: number) => {
  try {
    console.log('🔄 Chargement des mouvements de congé pour le personnel ID:', personnelId)
    const response = await api.get(`/personnel/mvt-conges/lister/personnel/${personnelId}`)
    console.log('📥 Réponse API mouvements congé:', response)
    
    if (response.data && response.data.rows) {
      console.log('📋 Données brutes des mouvements congé:', response.data.rows)
      leaveMovements.value = response.data.rows.map((movement: any) => ({
        id: movement.id,
        dateDepart: movement.dDepart || 'N/A',
        dateRetour: movement.dRetour || 'N/A',
        nombreJourPris: movement.nombreJourPris || 0,
        nombreJourRestant: movement.nombreJourRestant || 0,
        montantVerse: movement.montantVerse || 0,
        mtnVerse: movement.mtnVerse || '0',
        dateCreation: movement.dCreation || 'N/A',
        dateModification: movement.dModification || 'N/A'
      }))
      console.log('✅ Mouvements congé mappés avec succès:', leaveMovements.value)
    } else {
      console.log('❌ Structure de réponse inattendue:', response)
      leaveMovements.value = [] // Tableau vide si aucune donnée
    }
  } catch (error) {
    console.error('❌ Erreur lors du chargement des mouvements congé:', error)
    ElMessage.error('Erreur lors du chargement des mouvements congé')
  }
}

const loadAssignments = async (personnelId: number) => {
  try {
    console.log('🔄 Chargement des affectations pour le personnel ID:', personnelId)
    const response = await api.post('/rh/carriere/affectations/lister-par-personnel', { idPersonnel: personnelId })
    console.log('📥 Réponse API affectations:', response)
    
    if (response.data && response.data.rows) {
      console.log('📋 Données brutes des affectations:', response.data.rows)
      assignments.value = response.data.rows.map((assignment: any) => ({
        id: assignment.id,
        poste: assignment.poste?.libelle || 'N/A',
        site: assignment.site?.libelle || 'N/A',
        dateDebut: assignment.dateDebut || 'N/A',
        dateFin: assignment.dateFin || 'N/A',
        observation: assignment.observation || 'N/A',
        statut: assignment.statut === true ? 'Actif' : 'Inactif',
        urlDocument: assignment.urlDocument || '',
        dateCreation: assignment.dateCreation || 'N/A',
        dateModification: assignment.dateModification || 'N/A'
      }))
      console.log('✅ Affectations mappées avec succès:', assignments.value)
      console.log('🔍 Debug URL Documents:', assignments.value.map(a => ({ id: a.id, urlDocument: a.urlDocument, hasDoc: !!a.urlDocument })))
    } else {
      console.log('❌ Structure de réponse inattendue:', response)
      assignments.value = [] // Tableau vide si aucune donnée
    }
  } catch (error) {
    console.error('❌ Erreur lors du chargement des affectations:', error)
    ElMessage.error('Erreur lors du chargement des affectations')
    assignments.value = []
  }
}

// Charger les types d'absence
const loadAbsenceTypes = async () => {
  try {
    console.log('🔄 Chargement des types d\'absence...')
    // Utiliser GET /list au lieu de POST /lister
    const response = await api.get('/rh/absences/list')
    console.log('📥 Réponse API types d\'absence:', response)
    
    if (response.data && response.data.result === 'success') {
      // Si response.data.rows est un tableau
      const typesData = Array.isArray(response.data.rows) ? response.data.rows : []
      absenceTypes.value = typesData.map((type: any) => ({
        id: type.id,
        faute: type.faute,  // ✅ Utiliser le champ 'faute' comme dans l'API
        libelle: type.libelle || type.libelleAbsence || type.nom || type.faute, // Garder pour compatibilité
        code: type.code || type.codeAbsence,
        actif: type.actif !== false
      }))
      
      console.log('✅ Types d\'absence chargés:', absenceTypes.value)
    } else {
      console.warn('⚠️ Aucun type d\'absence trouvé dans la réponse')
      // Fallback avec des types par défaut
      absenceTypes.value = [
        { id: 1, faute: 'Maladie', libelle: 'Maladie', code: 'MALADIE', actif: true },
        { id: 2, faute: 'Congé', libelle: 'Congé', code: 'CONGE', actif: true },
        { id: 3, faute: 'Congé maternité', libelle: 'Congé maternité', code: 'CONGE_MAT', actif: true },
        { id: 4, faute: 'Congé sans solde', libelle: 'Congé sans solde', code: 'CONGE_SS', actif: true }
      ]
    }
  } catch (error: any) {
    console.error('❌ Erreur lors du chargement des types d\'absence:', error)
    console.warn('⚠️ Utilisation des types d\'absence par défaut en raison de l\'erreur API')
    
    // Fallback en cas d'erreur - utiliser les types par défaut
    absenceTypes.value = [
      { id: 1, faute: 'Maladie', libelle: 'Maladie', code: 'MALADIE', actif: true },
      { id: 2, faute: 'Congé', libelle: 'Congé', code: 'CONGE', actif: true },
      { id: 3, faute: 'Congé maternité', libelle: 'Congé maternité', code: 'CONGE_MAT', actif: true },
      { id: 4, faute: 'Congé sans solde', libelle: 'Congé sans solde', code: 'CONGE_SS', actif: true }
    ]
    
    // Ne pas afficher de message d'erreur pour ne pas perturber l'utilisateur
    // ElMessage.error('Erreur lors du chargement des types d\'absence: ' + (error.response?.data?.message || error.message))
  }
}

const loadAbsences = async (personnelId: number) => {
  try {
    console.log('🔄 Chargement des absences pour le personnel ID:', personnelId)
    const response = await api.post('/absence/listerabsencepersonnelsparpersonnel', { id: personnelId })
    console.log('📥 Réponse API absences:', response)
    
    if (response.data && response.data.rows) {
      console.log('📋 Données brutes des absences:', response.data.rows)
      absences.value = response.data.rows.map((absence: any) => ({
        id: absence.id,
        idAbsence: absence.absences?.id || absence.idAbsence,
        type: absence.absences?.faute || 'N/A',  // ✅ Utilise directement le libellé du backend
        dateDebut: absence.dDebut || 'N/A',
        dateFin: absence.dRet || 'N/A',
        duree: absence.joursabsence || 0,
        heuresAbsence: absence.heursabsence || 0,
        motif: absence.observation || 'N/A',
        statut: absence.statut === true ? 'Approuvé' : 'En attente',
        sanctionSalaire: getSanctionSalaireLabel(absence.sanctionsalaire),
        impact: absence.impact || 'N/A',
        dateCreation: absence.dCreation || 'N/A',
        dateModification: absence.dModification || 'N/A'
      }))
      console.log('✅ Absences mappées avec succès:', absences.value)
    } else {
      console.log('❌ Structure de réponse inattendue:', response)
      absences.value = [] // Tableau vide si aucune donnée
    }
  } catch (error) {
    console.error('❌ Erreur lors du chargement des absences:', error)
    ElMessage.error('Erreur lors du chargement des absences')
  }
}

const loadContracts = async (personnelId: number) => {
  try {
    console.log('🔄 Chargement des contrats pour le personnel ID:', personnelId)
    const response = await api.post('/personnels/listcontratparpersonnel', { id: personnelId })
    console.log('📥 Réponse API contrats:', response)
    
    if (response.data) {
      console.log('📋 Données brutes des contrats:', response.data)
      // Si response.data est directement un tableau
      const contractsData = Array.isArray(response.data) ? response.data : response.data.rows || []
      contracts.value = contractsData.map((contract: any) => ({
        id: contract.id,
        type: contract.typeContrat?.libelle || 'N/A',
        dateDebut: contract.dDebut || 'N/A',
        dateFin: contract.dFin || 'N/A',
        duree: calculateDuration(contract.dDebut, contract.dFin),
        salaire: contract.netAPayer || 0,
        statut: contract.statut ? 'Actif' : 'Inactif',
        categorie: contract.categorie?.libelle || 'N/A',
        fonction: contract.fonction?.libelle || 'N/A',
        indemniteLogement: contract.indemniteLogement || 0,
        indemniteTransport: contract.indemniteTransport || 0,
        sursalaire: contract.sursalaire || 0,
        observations: contract.observCtrat || ''
      }))
      console.log('✅ Contrats mappés avec succès:', contracts.value)
    } else {
      console.log('❌ Structure de réponse inattendue:', response)
    }
  } catch (error) {
    console.error('❌ Erreur lors du chargement des contrats:', error)
    ElMessage.error('Erreur lors du chargement des contrats')
  }
}

const viewContract = (contract: any) => {
  // Afficher les détails du contrat dans une modal (lecture seule)
  ElMessage.info(`Affichage du contrat: ${contract.type} - ${contract.fonction}`)
  
  // Vous pouvez aussi ouvrir une modal avec les détails complets
  ElMessageBox.alert(
    `Détails du contrat\n\n` +
    `Type: ${contract.type}\n` +
    `Catégorie: ${contract.categorie}\n` +
    `Fonction: ${contract.fonction}\n` +
    `Date début: ${contract.dateDebut}\n` +
    `Date fin: ${contract.dateFin}\n` +
    `Salaire: ${formatCurrency(contract.salaire)}\n` +
    `Indem. logement: ${formatCurrency(contract.indemniteLogement)}\n` +
    `Indem. transport: ${formatCurrency(contract.indemniteTransport)}\n` +
    `Sursalaire: ${formatCurrency(contract.sursalaire)}\n` +
    `Observations: ${contract.observations || 'Aucune'}`,
    'Détails du contrat',
    {
      confirmButtonText: 'Fermer',
      type: 'info'
    }
  )
}

const loadPromotions = async (personnelId: number) => {
  try {
    console.log('🔄 Chargement des promotions pour le personnel ID:', personnelId)
    const response = await api.post('/rh/carriere/promotions-personnel/lister-par-personnel', { id: personnelId })
    console.log('📥 Réponse API promotions:', response)
    
    if (response.data && response.data.rows) {
      console.log('📋 Données brutes des promotions:', response.data.rows)
      promotions.value = response.data.rows.map((promotion: any) => ({
        id: promotion.id,
        urlDocument: promotion.urlDocument || '',
        commentaire: promotion.commentaire || 'N/A',
        datePromotion: promotion.dPromotion || 'N/A',
        dateCreation: promotion.dCreation || 'N/A',
        dateModification: promotion.dModification || 'N/A',
        promotion: promotion.promotion?.libelle || 'N/A'
      }))
      console.log('✅ Promotions mappées avec succès:', promotions.value)
    } else {
      console.log('❌ Structure de réponse inattendue:', response)
      promotions.value = [] // Tableau vide si aucune donnée
    }
  } catch (error) {
    console.error('❌ Erreur lors du chargement des promotions:', error)
    ElMessage.error('Erreur lors du chargement des promotions')
  }
}

const viewPromotion = (promotion: any) => {
  // Afficher les détails de la promotion dans une modal (lecture seule)
  ElMessage.info(`Affichage de la promotion: ${promotion.promotion}`)
  
  // Affichage complet dans une alerte
  ElMessageBox.alert(
    `Détails de la promotion\n\n` +
    `Promotion: ${promotion.promotion}\n` +
    `Date promotion: ${promotion.datePromotion}\n` +
    `Date création: ${promotion.dateCreation}\n` +
    `Date modification: ${promotion.dateModification}\n` +
    `Commentaire: ${promotion.commentaire}\n` +
    `Document: ${promotion.urlDocument ? 'Disponible' : 'Aucun'}`,
    'Détails de la promotion',
    {
      confirmButtonText: 'Fermer',
      type: 'info'
    }
  )
}

const viewSanction = (sanction: any) => {
  // Afficher les détails de la sanction dans une modal (lecture seule)
  ElMessage.info(`Affichage de la sanction: ${sanction.typeSanction}`)
  
  // Affichage complet dans une alerte
  ElMessageBox.alert(
    `Détails de la sanction\n\n` +
    `Type: ${sanction.typeSanction}\n` +
    `Observation: ${sanction.observation}\n` +
    `Date début: ${sanction.dateDebut}\n` +
    `Date fin: ${sanction.dateFin}\n` +
    `Gravité: ${sanction.gravite}\n` +
    `Document demande: ${sanction.urlDemande ? 'Disponible' : 'Aucun'}\n` +
    `Document réponse: ${sanction.urlReponse ? 'Disponible' : 'Aucun'}\n` +
    `Date création: ${sanction.dateCreation}\n` +
    `Date modification: ${sanction.dateModification}`,
    'Détails de la sanction',
    {
      confirmButtonText: 'Fermer',
      type: 'info'
    }
  )
}

const viewSpouse = (spouse: any) => {
  // Afficher les détails du conjoint dans une modal (lecture seule)
  ElMessage.info(`Affichage du conjoint: ${spouse.nom} (${spouse.matricule})`)
  
  // Affichage complet dans une alerte
  ElMessageBox.alert(
    `Détails du conjoint\n\n` +
    `Matricule: ${spouse.matricule}\n` +
    `Nom: ${spouse.nom}\n` +
    `Date de naissance: ${spouse.dateNaissance}\n` +
    `Lieu de naissance: ${spouse.lieuNaissance}\n` +
    `Lieu de résidence: ${spouse.lieuResidence}\n` +
    `Téléphone: ${spouse.telephone}\n` +
    `Email: ${spouse.email}\n` +
    `Sexe: ${spouse.sexe}\n` +
    `Photo: ${spouse.photo ? 'Disponible' : 'Aucune'}\n` +
    `Statut: ${spouse.actif}\n` +
    `Statut détaillé: ${spouse.statut}`,
    'Détails du conjoint',
    {
      confirmButtonText: 'Fermer',
      type: 'info'
    }
  )
}

const viewChild = (child: any) => {
  // Afficher les détails de l'enfant dans une modal (lecture seule)
  ElMessage.info(`Affichage de l'enfant: ${child.prenom} ${child.nom}`)
  
  // Affichage complet dans une alerte
  ElMessageBox.alert(
    `Détails de l'enfant\n\n` +
    `Nom: ${child.nom}\n` +
    `Prénom: ${child.prenom}\n` +
    `Date de naissance: ${child.dateNaissance}\n` +
    `Sexe: ${child.sexe}\n` +
    `École: ${child.ecole || 'Non spécifiée'}\n` +
    `À charge: ${child.aCharge ? 'Oui' : 'Non'}`,
    'Détails de l\'enfant',
    {
      confirmButtonText: 'Fermer',
      type: 'info'
    }
  )
}

const viewDocument = (document: any) => {
  // Afficher les détails du document dans une modal (lecture seule)
  ElMessage.info(`Affichage du document: ${document.type} - ${document.numeroReference}`)
  
  // Affichage complet dans une alerte
  ElMessageBox.alert(
    `Détails du document\n\n` +
    `Type: ${document.type}\n` +
    `Emplacement: ${document.emplacement}\n` +
    `Date dépôt: ${document.dateDepot}\n` +
    `Référence: ${document.numeroReference}\n` +
    `Présent: ${document.present}\n` +
    `Remarques: ${document.remarques}\n` +
    `Fichier: ${document.urlFichier ? 'Disponible' : 'Aucun'}`,
    'Détails du document',
    {
      confirmButtonText: 'Fermer',
      type: 'info'
    }
  )
}

// ==================== MÉTHODES DE GESTION DES PHOTOS ====================

// Déclencher l'upload de photo
const triggerPhotoUpload = () => {
  photoInput.value?.click()
}

// Gérer le changement de photo
const handlePhotoChange = async (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (file && personnel.value?.id) {
    try {
      photoUploading.value = true
      
      // Uploader la photo
      const result = await personnelRestService.uploadPhoto(personnel.value.id, file)
      
      if (result.success) {
        ElMessage.success('Photo mise à jour avec succès')
        // Recharger la photo
        await loadPersonnelPhoto()
      } else {
        ElMessage.error(result.message || 'Erreur lors de la mise à jour de la photo')
      }
    } catch (error) {
      console.error('Erreur upload photo:', error)
      ElMessage.error('Erreur lors de la mise à jour de la photo')
    } finally {
      photoUploading.value = false
    }
  }
}

// Charger la photo du personnel
const loadPersonnelPhoto = async () => {
  if (personnel.value?.id) {
    try {
      const photoUrl = await personnelRestService.getPhoto(personnel.value.id)
      personnelPhotoUrl.value = photoUrl
    } catch (error: any) {
      // Si c'est une erreur 404, c'est normal (pas de photo)
      if (error.response?.status === 404) {
        console.log(`ℹ️ Aucune photo trouvée pour le personnel ID: ${personnel.value.id}`)
      } else {
        console.error('Erreur chargement photo:', error)
      }
      // Ne pas afficher d'erreur à l'utilisateur, juste utiliser l'avatar par défaut
      personnelPhotoUrl.value = ''
    }
  }
}

const viewLeaveMovement = (movement: any) => {
  // Afficher les détails du mouvement de congé dans une modal (lecture seule)
  ElMessage.info(`Affichage du mouvement de congé du ${movement.dateDepart}`)
  
  // Affichage complet dans une alerte
  ElMessageBox.alert(
    `Détails du mouvement de congé\n\n` +
    `Date départ: ${movement.dateDepart}\n` +
    `Date retour: ${movement.dateRetour}\n` +
    `Jours pris: ${movement.nombreJourPris}\n` +
    `Jours restants: ${movement.nombreJourRestant}\n` +
    `Montant versé: ${formatCurrency(movement.montantVerse)}\n` +
    `Date création: ${movement.dateCreation}\n` +
    `Date modification: ${movement.dateModification}`,
    'Détails du mouvement de congé',
    {
      confirmButtonText: 'Fermer',
      type: 'info'
    }
  )
}

const openDocument = (url: string) => {
  if (url) {
    window.open(url, '_blank')
  }
}

const downloadDocument = async (affectationId: number) => {
  if (affectationId) {
    try {
      // Utiliser l'endpoint backend avec l'ID de l'affectation
      const response = await api.post('/rh/carriere/affectations/download', {
        id: affectationId
      }, {
        responseType: 'blob' // Important pour les fichiers binaires
      })
      
      // Extraire le nom du fichier depuis les headers
      const contentDisposition = response.headers['content-disposition']
      let fileName = 'document'
      if (contentDisposition) {
        const fileNameMatch = contentDisposition.match(/filename="(.+)"/)
        if (fileNameMatch && fileNameMatch[1]) {
          fileName = fileNameMatch[1]
        }
      }
      
      // Créer un lien temporaire pour le téléchargement
      const blob = new Blob([response.data])
      const downloadUrl = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = downloadUrl
      link.download = fileName
      
      // Ajouter le lien au DOM, cliquer dessus, puis le supprimer
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(downloadUrl)
      
      ElMessage.success(`Téléchargement du document pour: ${poste}`)
    } catch (error) {
      console.error('❌ Erreur lors du téléchargement:', error)
      ElMessage.error('Erreur lors du téléchargement du document')
    }
  } else {
    ElMessage.warning('Aucun document disponible pour cette affectation')
  }
}

// Fonction pour télécharger les documents personnels
const downloadPersonnelDocument = async (documentId: number) => {
  if (!documentId) {
    ElMessage.warning('Aucun document à télécharger')
    return
  }
  
  try {
    // Utiliser l'ID du document pour le téléchargement
    const response = await api.post('/personnel/documents/download', {
      id: documentId
    }, {
      responseType: 'blob'
    })
    
    // Extraire le nom du fichier depuis les headers
    const contentDisposition = response.headers['content-disposition']
    let fileName = 'document'
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename="(.+)"/)
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = fileNameMatch[1]
      }
    }
    
    // Créer un lien temporaire pour le téléchargement
    const blob = new Blob([response.data])
    const downloadUrl = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = downloadUrl
    link.download = fileName
    
    // Ajouter le lien au DOM, cliquer dessus, puis le supprimer
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(downloadUrl)
    
    ElMessage.success(`Téléchargement du document: ID ${documentId}`)
  } catch (error) {
    console.error('❌ Erreur lors du téléchargement du document:', error)
    ElMessage.error('Erreur lors du téléchargement du document')
  }
}

const viewAssignment = (assignment: any) => {
  // Afficher les détails de l'affectation dans une modal (lecture seule)
  ElMessage.info(`Affichage de l'affectation: ${assignment.poste} - ${assignment.site}`)
  
  // Affichage complet dans une alerte
  ElMessageBox.alert(
    `Détails de l'affectation\n\n` +
    `Poste: ${assignment.poste}\n` +
    `Site: ${assignment.site}\n` +
    `Date début: ${assignment.dateDebut}\n` +
    `Date fin: ${assignment.dateFin}\n` +
    `Observation: ${assignment.observation}\n` +
    `Document: ${assignment.urlDocument ? 'Disponible' : 'Aucun'}\n` +
    `Statut: ${assignment.statut}`,
    `Détails de l'affectation`,
    {
      confirmButtonText: 'Fermer',
      type: 'info'
    }
  )
}

const editAssignment = (assignment: any) => {
  console.log('📝 Modification de l\'affectation:', assignment)
  
  // Basculer vers l'onglet affectation et pré-remplir le formulaire
  activeTab.value = 'assignment'
  
  // Pré-remplir le formulaire avec les données de l'affectation
  assignmentForm.id = assignment.id
  assignmentForm.poste = assignment.poste
  assignmentForm.site = assignment.site
  assignmentForm.dateDebut = assignment.dateDebut
  assignmentForm.dateFin = assignment.dateFin
  assignmentForm.observation = assignment.observation
  
  // Afficher le formulaire
  showForm.value = true
  
  ElMessage.success(`Formulaire prêt pour modifier l'affectation: ${assignment.poste}`)
}

const viewAbsence = (absence: any) => {
  // Afficher les détails de l'absence dans une modal (lecture seule)
  ElMessage.info(`Affichage de l'absence: ${absence.type} - ${absence.motif}`)
  
  // Affichage complet dans une alerte
  ElMessageBox.alert(
    `Détails de l'absence\n\n` +
    `Type: ${absence.type}\n` +
    `Date début: ${absence.dateDebut}\n` +
    `Date fin: ${absence.dateFin}\n` +
    `Jours d'absence: ${absence.duree}\n` +
    `Heures d'absence: ${absence.heuresAbsence}\n` +
    `Observation: ${absence.motif}\n` +
    `Sanction salaire: ${absence.sanctionSalaire > 0 ? formatCurrency(absence.sanctionSalaire) : 'Aucune'}\n` +
    `Impact: ${absence.impact}\n` +
    `Statut: ${absence.statut}\n` +
    `Date création: ${absence.dateCreation}\n` +
    `Date modification: ${absence.dateModification}`,
    'Détails de l\'absence',
    {
      confirmButtonText: 'Fermer',
      type: 'info'
    }
  )
}

const goBack = () => {
  router.push('/personnel')
}

const editPersonnel = () => {
  if (personnel.value) {
    router.push(`/personnel/wizard?id=${personnel.value.id}`)
  }
}

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'XOF' }).format(amount)
}

// Fonctions utilitaires pour les formulaires
const getFormTitle = () => {
  const titles = {
    contracts: 'Ajouter un Contrat',
    absences: 'Ajouter une Absence',
    assignment: 'Ajouter une Affectation',
    'leave-movements': 'Ajouter un Mouvement de Congé',
    documents: 'Ajouter un Document',
    children: 'Ajouter un Enfant',
    spouse: 'Ajouter un Conjoint',
    sanctions: 'Ajouter une Sanction',
    promotions: 'Ajouter une Promotion'
  }
  return titles[activeTab.value as keyof typeof titles] || 'Formulaire'
}

const getTabTitle = () => {
  const titles = {
    contracts: 'Contrats du Personnel',
    absences: 'Gestion des Absences',
    assignment: 'Historique d\'Affectation',
    'leave-movements': 'Mouvements de Congé',
    documents: 'Documents du Personnel',
    children: 'Enfants à Charge',
    spouse: 'Informations sur le Conjoint',
    sanctions: 'Historique des Sanctions',
    promotions: 'Historique des Promotions'
  }
  return titles[activeTab.value as keyof typeof titles] || 'Détails'
}

const getAddButtonText = () => {
  const buttons = {
    contracts: 'Nouveau Contrat',
    absences: 'Nouvelle Absence',
    assignment: 'Nouvelle Affectation',
    'leave-movements': 'Nouveau Mouvement',
    documents: 'Télécharger Document',
    children: 'Ajouter un Enfant',
    spouse: 'Ajouter un Conjoint',
    sanctions: 'Nouvelle Sanction',
    promotions: 'Nouvelle Promotion'
  }
  return buttons[activeTab.value as keyof typeof buttons] || 'Ajouter'
}

const toggleForm = () => {
  console.log('🔄 toggleForm appelé - activeTab:', activeTab.value, 'showForm avant:', showForm.value)
  
  // Vérifier si tous les contrats sont inactifs avant de permettre la création
  if (!allContractsInactive.value && contracts.value.length > 0 && activeTab.value === 'contracts') {
    ElMessage.warning('Impossible de créer un nouveau contrat tant qu\'il existe des contrats actifs.')
    return
  }
  
  showForm.value = !showForm.value
  console.log('📊 showForm après toggle:', showForm.value)
  
  if (!showForm.value) {
    resetForms()
  } else {
    // Charger les listes dynamiques selon l'onglet actif
    if (activeTab.value === 'contracts' && typeContrats.value.length === 0) {
      loadContractLists()
    } else if (activeTab.value === 'absences' && absenceTypes.value.length === 0) {
      loadAbsenceTypes()
    } else if (activeTab.value === 'assignment' && fonctions.value.length === 0) {
      loadFunctions()
    } else if (activeTab.value === 'assignment' && sites.value.length === 0) {
      loadSites()
    }
  }
  
  // Pré-remplir le formulaire avec le dernier contrat si on ouvre le formulaire des contrats
  if (showForm.value && activeTab.value === 'contracts') {
    prefillWithLastContract()
  }
  
  console.log('✅ toggleForm terminé - showForm:', showForm.value, 'activeTab:', activeTab.value)
}

// Pré-remplir le formulaire avec le dernier contrat de l'employé
const prefillWithLastContract = async () => {
  if (!contracts.value || contracts.value.length === 0) {
    console.log('ℹ️ Aucun contrat existant à utiliser pour le pré-remplissage')
    return
  }

  try {
    // Récupérer le dernier contrat (le plus récent) basé sur la date de début
    const lastContract = contracts.value.reduce((latest, current) => {
      const latestDate = new Date(latest.dateDebut === 'N/A' ? '1900-01-01' : latest.dateDebut)
      const currentDate = new Date(current.dateDebut === 'N/A' ? '1900-01-01' : current.dateDebut)
      return currentDate > latestDate ? current : latest
    })

    console.log('📋 Dernier contrat trouvé pour pré-remplissage:', lastContract)

    // Récupérer les données complètes du dernier contrat depuis l'API
    const fullContractResponse = await api.post('/personnels/listcontratparpersonnel', { 
      id: personnel.value?.id 
    })
    
    const contractsData = Array.isArray(fullContractResponse.data) ? 
      fullContractResponse.data : 
      fullContractResponse.data.rows || []
    
    // Trouver le contrat complet correspondant au dernier contrat
    const fullLastContract = contractsData.find((contract: any) => 
      contract.id === lastContract.id
    )

    if (fullLastContract) {
      console.log('📄 Données complètes du dernier contrat:', fullLastContract)

      // Pré-remplir le formulaire avec les données complètes du dernier contrat
      Object.assign(contractForm, {
        // Garder l'ID à 0 pour créer un nouveau contrat
        id: 0,
        
        // Pré-remplir avec les valeurs du dernier contrat (utiliser les bons noms de champs)
        categorie: fullLastContract.categorie?.libelle || '',
        emploi: fullLastContract.fonction?.libelle || '',
        salaire: fullLastContract.netAPayer || 0,
        indemniteLogement: fullLastContract.indemniteLogement || 0,
        indemniteTransport: fullLastContract.indemniteTransport || 0,
        sursalaire: fullLastContract.sursalaire || 0,
        indemniteRepresentation: fullLastContract.indemniteRepresent || 0,
        ancienneteInitiale: fullLastContract.ancienneteInitial || 0,
        
        // Pré-remplir le type de contrat et les dates
        type: fullLastContract.typeContrat?.libelle || '',
        
        // Convertir les dates au format YYYY-MM-DD attendu par le date-picker
        dateDebut: fullLastContract.dDebut ? convertDateToISO(fullLastContract.dDebut) : '',
        dateFin: fullLastContract.dFin ? convertDateToISO(fullLastContract.dFin) : '',
        
        observations: fullLastContract.observCtrat || ''
      })

      console.log('✅ Formulaire pré-rempli avec les données complètes du dernier contrat')
      ElMessage.success('Formulaire pré-rempli avec les données du dernier contrat')
    } else {
      console.warn('⚠️ Contrat complet non trouvé, utilisation des données de base')
      // Utiliser les données de base si le contrat complet n'est pas trouvé
      Object.assign(contractForm, {
        id: 0,
        categorie: lastContract.categorie || '',
        emploi: lastContract.fonction || '',
        salaire: lastContract.salaire || 0,
        indemniteLogement: lastContract.indemniteLogement || 0,
        indemniteTransport: lastContract.indemniteTransport || 0,
        sursalaire: lastContract.sursalaire || 0,
        indemniteRepresentation: 0,
        ancienneteInitiale: 0,
        type: '',
        dateDebut: '',
        dateFin: '',
        observations: lastContract.observations || ''
      })
    }
  } catch (error) {
    console.error('❌ Erreur lors du pré-remplissage du formulaire:', error)
    ElMessage.warning('Impossible de pré-remplir le formulaire avec le dernier contrat')
  }
}

// Fonction pour convertir les dates du format YYYY-MM-DD vers dd/MM/yyyy (format attendu par le backend)
const convertDateToBackendFormat = (dateString: string): string => {
  if (!dateString) return ''
  
  try {
    // Si le format est déjà dd/MM/yyyy, le retourner tel quel
    if (dateString.includes('/') && dateString.length === 10) {
      return dateString
    }
    
    // Si le format contient des heures (YYYY-MM-DD HH:mm:ss), extraire juste la date
    let cleanDate = dateString
    if (dateString.includes(' ') && dateString.includes(':')) {
      cleanDate = dateString.split(' ')[0] // Extraire YYYY-MM-DD
    }
    
    // Convertir YYYY-MM-DD vers dd/MM/yyyy
    if (cleanDate.includes('-')) {
      const [year, month, day] = cleanDate.split('-')
      return `${day}/${month}/${year}`
    }
    
    // Format par défaut si la conversion échoue
    return dateString
  } catch (error) {
    console.warn('⚠️ Erreur de conversion de date pour le backend:', dateString, error)
    return dateString
  }
}

// Fonction pour convertir les dates du format DD/MM/YYYY vers YYYY-MM-DD
const convertDateToISO = (dateString: string): string => {
  if (!dateString) return ''
  
  try {
    // Gérer différents formats de date possibles
    // Format: "01/07/2025" -> "2025-07-01"
    const parts = dateString.split('/')
    if (parts.length === 3) {
      const [day, month, year] = parts
      return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
    }
    
    // Si le format est déjà correct, le retourner tel quel
    if (dateString.includes('-')) {
      return dateString
    }
    
    // Format par défaut si la conversion échoue
    return dateString
  } catch (error) {
    console.warn('⚠️ Erreur de conversion de date:', dateString, error)
    return dateString
  }
}

const closeForm = () => {
  showForm.value = false
  resetForms()
}

const resetForms = () => {
  Object.assign(contractForm, { 
    id: 0, 
    type: '', 
    categorie: '', 
    emploi: '', // Changé de 'fonction' à 'emploi'
    dateDebut: '', 
    dateFin: '', 
    salaire: 0, 
    indemniteLogement: 0, 
    indemniteTransport: 0, 
    sursalaire: 0, 
    indemniteRepresentation: 0, 
    ancienneteInitiale: 0, 
    observations: '' 
  })
  Object.assign(absenceForm, { id: 0, type: '', dateDebut: '', dateFin: '', motif: '' })
  Object.assign(assignmentForm, { id: 0, service: '', poste: '', dateDebut: '', dateFin: '', motif: '' })
  Object.assign(leaveMovementForm, { id: 0, typeConge: '', dateDebut: '', dateFin: '', soldeRestant: 0 })
  Object.assign(documentForm, { id: 0, type: '', nom: '', fichier: null })
  Object.assign(childForm, { id: null, nom: '', matricule: '', dateNaissance: '', sexe: 'M', ecole: '', aCharge: true })
  Object.assign(spouseForm, { id: null, nom: '', matricule: '', dateNaissance: '', lieuNaissance: '', telephone: '', sexe: 'M' })
  Object.assign(sanctionForm, { id: 0, type: '', date: '', motif: '', duree: '', gravite: '' })
  fileList.value = []
}

const handleFileChange = (file: any) => {
  documentForm.fichier = file.raw
  fileList.value = [file]
}

// Fonctions pour afficher les libellés sélectionnés
const getSelectedDocumentType = () => {
  console.log('🔍 Recherche du type de document, ID:', documentForm.typeId)
  console.log('📋 Types disponibles:', documentTypes.value)
  const selected = documentTypes.value.find(type => type.id === documentForm.typeId)
  console.log('✅ Type trouvé:', selected)
  return selected ? selected.libelle : ''
}

const getSelectedStorageLocation = () => {
  console.log('🔍 Recherche de l\'emplacement, ID:', documentForm.locationId)
  console.log('📋 Emplacements disponibles:', storageLocations.value)
  const selected = storageLocations.value.find(location => location.id === documentForm.locationId)
  console.log('✅ Emplacement trouvé:', selected)
  return selected ? selected.libelle : ''
}

// Fonctions pour charger les listes dynamiques
const loadDocumentTypes = async () => {
  try {
    console.log('🔄 Chargement des types de documents...')
    const response = await api.get('/personnel/document-types')
    console.log('✅ Types de documents chargés:', response.data)
    documentTypes.value = response.data
  } catch (error) {
    console.error('❌ Erreur lors du chargement des types de documents:', error)
    ElMessage.error('Erreur lors du chargement des types de documents')
  }
}

const loadStorageLocations = async () => {
  try {
    console.log('🔄 Chargement des emplacements de stockage...')
    const response = await api.get('/storage-locations')
    console.log('✅ Emplacements de stockage chargés:', response.data)
    storageLocations.value = response.data
  } catch (error) {
    console.error('❌ Erreur lors du chargement des emplacements de stockage:', error)
    ElMessage.error('Erreur lors du chargement des emplacements de stockage')
  }
}

onMounted(() => {
  loadPersonnel()
  loadContractLists() // Charger les listes au démarrage pour optimiser
  loadDocumentTypes() // Charger les types de documents
  loadStorageLocations() // Charger les emplacements de stockage
})

const getAbsenceStatusColor = (statut: string) => {
  const colors = { Approuvé: 'success', 'En attente': 'warning', Refusé: 'danger' }
  return colors[statut as keyof typeof colors] || 'info'
}

const getLeaveStatusColor = (statut: string) => {
  const colors = { Approuvé: 'success', 'En attente': 'warning', Refusé: 'danger' }
  return colors[statut as keyof typeof colors] || 'info'
}

const getSanctionGravityColor = (gravite: string) => {
  const colors = { Mineure: 'success', Moyenne: 'warning', Majeure: 'danger' }
  return colors[gravite as keyof typeof colors] || 'info'
}

const getStatutColor = (statut: string) => {
  const colors = { ACTIF: 'success', INACTIF: 'danger', CONGE: 'warning' }
  return colors[statut as keyof typeof colors] || 'info'
}

// Fonctions de sauvegarde pour chaque section
const saveContract = async () => {
  if (!contractForm.emploi || !contractForm.type || !contractForm.dateDebut || contractForm.salaire <= 0) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  // Vérifier que l'ID du personnel est disponible
  if (!personnel.value?.id) {
    ElMessage.error('ID du personnel non disponible. Veuillez recharger la page.')
    return
  }

  formLoading.value = true
  try {
    console.log('💾 Enregistrement du contrat avec les données:', contractForm)
    console.log('👤 ID du personnel:', personnel.value.id)
    
    // Fonction pour trouver l'ID d'un objet par son libellé
    const findIdByLibelle = (items: any[], libelle: string) => {
      const item = items.find(item => item.libelle === libelle)
      return item ? item.id : null
    }
    
    // Récupérer les IDs réels depuis les listes chargées
    const categorieId = findIdByLibelle(categories.value, contractForm.categorie)
    const fonctionId = findIdByLibelle(fonctions.value, contractForm.emploi)
    const typeContratId = findIdByLibelle(typeContrats.value, contractForm.type)
    
    console.log('🔍 IDs trouvés:', {
      categorieId,
      fonctionId, 
      typeContratId,
      categories: categories.value,
      fonctions: fonctions.value,
      typeContrats: typeContrats.value
    })
    
    if (!categorieId || !fonctionId || !typeContratId) {
      ElMessage.error('Impossible de trouver les IDs correspondants. Veuillez vérifier les listes déroulantes.')
      return
    }
    
    // Préparer les données pour l'API - format plat comme attendu par ContratPersonnelRequest
    const contractData = {
      id: null,  // Pour un nouveau contrat (généré par le backend)
      idPersonnel: personnel.value.id,
      idCategorie: categorieId,
      idFonction: fonctionId,
      idTypeContrat: typeContratId,
      dateDebut: convertDateToBackendFormat(contractForm.dateDebut), // ✅ Format dd/MM/yyyy
      dateFin: contractForm.dateFin ? convertDateToBackendFormat(contractForm.dateFin) : null,
      netAPayer: contractForm.salaire,
      indemniteLogement: contractForm.indemniteLogement,
      ancienete: contractForm.ancienneteInitiale,
      sursalaire: contractForm.sursalaire,
      indemnitetransport: contractForm.indemniteTransport,
      indemniterespons: contractForm.indemniteRepresentation, // Note: indemniterespons dans le backend
      indemniterepresent: contractForm.indemniteRepresentation,
      statut: true
    }
    
    console.log('📤 Envoi des données au endpoint /api/personnels/savecontratpersonnel:', contractData)
    
    // Appel API pour enregistrer le contrat
    const response = await api.post('/personnels/savecontratpersonnel', contractData)
    
    console.log('📥 Réponse API savecontratpersonnel:', response)
    
    if (response.data) {
      // Recharger la liste des contrats
      await loadContracts(personnel.value.id)
      
      ElMessage.success('Contrat enregistré avec succès')
      closeForm()
    } else {
      throw new Error('Aucune donnée retournée par l\'API')
    }
  } catch (error: any) {
    console.error('❌ Erreur lors de l\'enregistrement du contrat:', error)
    ElMessage.error('Erreur lors de l\'enregistrement du contrat: ' + (error.response?.data?.message || error.message))
  } finally {
    formLoading.value = false
  }
}

// Fonction wrapper pour tracer le clic sur le bouton
const handleSaveAbsence = () => {
  console.log('🖱️ Bouton Enregistrer cliqué!')
  console.log('📋 État actuel du formulaire avant appel:', absenceForm)
  console.log('🔄 Appel de saveAbsence()...')
  saveAbsence()
}

const saveAbsence = async () => {
  // DEBUG: Tracer l'entrée dans la fonction
  console.log('🚀 saveAbsence() appelé')
  console.log('📋 État du formulaire absenceForm:', absenceForm)
  console.log('👤 Personnel ID:', personnel.value?.id)
  
  // Vérifier tous les champs obligatoires du formulaire
  const champsManquants = []
  if (!absenceForm.idAbsence) champsManquants.push('idAbsence')
  if (!absenceForm.dateDebut) champsManquants.push('dateDebut')
  if (!absenceForm.dateFin) champsManquants.push('dateFin')
  if (!absenceForm.sanctionSalaire) champsManquants.push('sanctionSalaire')
  if (!absenceForm.duree) champsManquants.push('duree')
  
  console.log('❌ Champs manquants:', champsManquants)
  
  if (champsManquants.length > 0) {
    console.log('⛔ Arrêt: champs obligatoires manquants')
    ElMessage.error('Veuillez renseigner tous les champs obligatoires: Type, Date début, Date fin, Impact et Jours d\'absence')
    return
  }

  // Vérifier que l'ID du personnel est disponible
  if (!personnel.value?.id) {
    console.log('❌ Arrêt: ID personnel non disponible')
    ElMessage.error('ID du personnel non disponible. Veuillez recharger la page.')
    return
  }

  console.log('✅ Validation OK, envoi en cours...')
  formLoading.value = true
  try {
    console.log('💾 Enregistrement de l\'absence avec les données:', absenceForm)
    console.log('👤 ID du personnel:', personnel.value.id)
    
    // Préparer les données pour l'API - envoyer l'ID directement avec les bons formats
    const absenceData = {
      id: absenceForm.id || null,  // ✅ null pour création, ID existant pour mise à jour
      idPersonnel: personnel.value.id,
      idAbsence: absenceForm.idAbsence,
      dateDebut: convertDateToBackendFormat(absenceForm.dateDebut),
      dateFin: absenceForm.dateFin ? convertDateToBackendFormat(absenceForm.dateFin) : null,
      motif: absenceForm.motif,
      observation: absenceForm.observation || '',
      sanctionsalaire: Number(absenceForm.sanctionSalaire) || 0,  // ✅ Convertir en nombre
      joursabsence: absenceForm.duree || 0,
      heursabsence: absenceForm.heuresAbsence || 0,
      statut: false
    }
    
    console.log('📤 Envoi des données au endpoint /api/absence/enregistrerabsencepersonnel:', absenceData)
    
    // Appel API pour enregistrer l'absence
    const response = await api.post('/absence/enregistrerabsencepersonnel', absenceData)
    
    console.log('📥 Réponse API enregistrerabsencepersonnel:', response)
    
    if (response.data) {
      // Recharger la liste des absences
      await loadAbsences(personnel.value.id)
      
      ElMessage.success('Absence enregistrée avec succès')
      closeForm()
    } else {
      throw new Error('Aucune donnée retournée par l\'API')
    }
  } catch (error: any) {
    console.error('❌ Erreur lors de l\'enregistrement de l\'absence:', error)
    ElMessage.error('Erreur lors de l\'enregistrement de l\'absence: ' + (error.response?.data?.message || error.message))
  } finally {
    formLoading.value = false
  }
}

// Fonction pour trouver une absence spécifique
const findAbsence = async (absenceId: number) => {
  // Vérifier que l'ID du personnel est disponible
  if (!personnel.value?.id) {
    ElMessage.error('ID du personnel non disponible. Veuillez recharger la page.')
    return null
  }

  try {
    console.log('🔍 Recherche de l\'absence ID:', absenceId)
    console.log('👤 ID du personnel:', personnel.value.id)
    
    const response = await api.post('/absence/trouverabsencepersonnel', { 
      id: absenceId,
      personnelId: personnel.value.id  // Changé: envoi direct de l'ID
    })
    
    console.log('📥 Réponse API trouverabsencepersonnel:', response)
    
    if (response.data) {
      return response.data
    } else {
      throw new Error('Absence non trouvée')
    }
  } catch (error: any) {
    console.error('❌ Erreur lors de la recherche de l\'absence:', error)
    ElMessage.error('Erreur lors de la recherche de l\'absence: ' + (error.response?.data?.message || error.message))
    return null
  }
}

const saveAssignment = async () => {
  if (!assignmentForm.poste || !assignmentForm.dateDebut) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  formLoading.value = true
  try {
    console.log('💾 Enregistrement de l\'affectation avec les données:', assignmentForm)
    console.log('👤 ID du personnel:', personnel.value.id)
    
    // Trouver l'ID du poste et du site à partir des libellés
    const posteObj = fonctions.value.find(f => f.libelle === assignmentForm.poste)
    const siteObj = sites.value.find(s => s.libelle === assignmentForm.site)
    
    // Préparer les données pour l'API selon le backend @RequestPart
    const assignmentData = {
      id: assignmentForm.id || null,  // null pour création, ID existant pour mise à jour
      idPersonnel: personnel.value.id,
      idPoste: posteObj?.id || null,  // Envoyer l'ID du poste, pas le libellé
      idSite: siteObj?.id || null,     // Envoyer l'ID du site, pas le libellé
      statutAffect: assignmentForm.statut,  // statutAffect comme dans le backend
      dateDebut: assignmentForm.dateDebut,  // ⚠️ String direct, pas de conversion
      dateFin: assignmentForm.dateFin || null,  // ⚠️ String direct, pas de conversion
      observation: assignmentForm.observation || ''
    }
    
    console.log('📤 Données d\'affectation:', assignmentData)
    
    // Créer FormData pour l'upload avec document (Solution 1)
    const formData = new FormData()
    
    // Ajouter les données de l'affectation en champs séparés pour @RequestPart
    formData.append('id', assignmentData.id?.toString() || '')
    formData.append('idPersonnel', assignmentData.idPersonnel?.toString() || '')
    formData.append('idPoste', assignmentData.idPoste?.toString() || '')
    formData.append('idSite', assignmentData.idSite?.toString() || '')
    formData.append('statutAffect', assignmentData.statutAffect?.toString() || 'false')
    formData.append('dateDebut', convertDateToBackendFormat(assignmentData.dateDebut))
    formData.append('dateFin', assignmentData.dateFin ? convertDateToBackendFormat(assignmentData.dateFin) : '')
    formData.append('observation', assignmentData.observation || '')
    
    // Ajouter le document s'il existe
    if (fileList.value.length > 0 && fileList.value[0].raw) {
      formData.append('document', fileList.value[0].raw)
      console.log('📎 Document ajouté à l\'upload:', fileList.value[0].name)
    }
    
    console.log('📤 FormData créé avec', formData.getAll('id').length, 'champs')
    console.log('📤 Envoi FormData au endpoint /api/rh/carriere/affectations/enregistrer')
    
    // Appel API pour enregistrer l'affectation avec document en une seule fois
    const response = await api.post('/rh/carriere/affectations/enregistrer', formData)
    
    console.log('📥 Réponse API enregistrer affectation:', response)
    
    if (response.data) {
      // Ajouter l'affectation localement
      const newAssignment = {
        id: response.data.id || Date.now(),
        poste: assignmentForm.poste,
        site: assignmentForm.site || 'Non défini',
        dateDebut: assignmentForm.dateDebut,
        dateFin: assignmentForm.dateFin || 'Non défini',
        observation: assignmentForm.observation,
        statut: assignmentForm.statut ? 'Actif' : 'Inactif',
        document: fileList.value.length > 0 ? fileList.value[0].name : null
      }
      
      assignments.value.unshift(newAssignment)
      ElMessage.success('Affectation enregistrée avec succès' + (fileList.value.length > 0 ? ' (document inclus)' : ''))
      closeForm()
    } else {
      ElMessage.error('Erreur lors de l\'enregistrement de l\'affectation')
    }
  } catch (error: any) {
    console.error('❌ Erreur lors de l\'enregistrement de l\'affectation:', error)
    ElMessage.error('Erreur lors de l\'enregistrement: ' + (error.response?.data?.message || error.message))
  } finally {
    formLoading.value = false
  }
}

const saveLeaveMovement = async () => {
  if (!leaveMovementForm.dateDepart) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  formLoading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    const newLeaveMovement = {
      id: Date.now(),
      dateDepart: leaveMovementForm.dateDepart,
      dateRetour: leaveMovementForm.dateRetour || 'Non défini',
      nombreJourPris: leaveMovementForm.nombreJourPris,
      nombreJourRestant: leaveMovementForm.nombreJourRestant,
      montantVerse: leaveMovementForm.montantVerse,
      dateCreation: leaveMovementForm.dateCreation || new Date().toISOString().split('T')[0]
    }
    
    leaveMovements.value.unshift(newLeaveMovement)
    ElMessage.success('Mouvement de congé ajouté avec succès')
    closeForm()
  } catch (error) {
    ElMessage.error('Erreur lors de l\'ajout du mouvement de congé')
  } finally {
    formLoading.value = false
  }
}

const saveDocument = async () => {
  if (!documentForm.typeId || !documentForm.fichier) {
    ElMessage.error('Veuillez renseigner le type de document et sélectionner un fichier')
    return
  }

  // Convertir les noms en IDs si nécessaire
  const documentTypeId = isNaN(documentForm.typeId) ? getDocumentTypeId(documentForm.typeId) : documentForm.typeId
  const storageLocationId = isNaN(documentForm.locationId) ? getStorageLocationId(documentForm.locationId) : documentForm.locationId

  formLoading.value = true
  try {
    // Créer FormData pour l'upload
    const formData = new FormData()
    formData.append('fichierDocument', documentForm.fichier)
    formData.append('idPersonnel', personnel.value?.id?.toString() || '')
    formData.append('idDocument', documentTypeId?.toString() || '')
    formData.append('dateDepot', documentForm.dateDepot ? new Date(documentForm.dateDepot).toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' }) : new Date().toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' }))
    formData.append('statutpresent', documentForm.present.toString())
    formData.append('numeroReference', documentForm.numeroReference || '')
    formData.append('idStorage', storageLocationId?.toString() || '')
    formData.append('description', documentForm.remarques || '')

    console.log('📤 Envoi des données:', {
      idPersonnel: personnel.value?.id,
      idDocument: documentTypeId,
      dateDepot: documentForm.dateDepot,
      statutpresent: documentForm.present,
      numeroReference: documentForm.numeroReference,
      idStorage: storageLocationId,
      description: documentForm.remarques
    })

    const response = await api.post('/personnel/documents/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data.result) {
      ElMessage.success('Document ajouté avec succès')
      closeForm()
      // Recharger la liste des documents
      await loadDocuments(personnel.value?.id)
    } else {
      ElMessage.error('Erreur lors de l\'ajout du document')
    }
  } catch (error) {
    console.error('❌ Erreur lors de l\'upload du document:', error)
    ElMessage.error('Erreur lors de l\'ajout du document')
  } finally {
    formLoading.value = false
  }
}

const saveDocumentAlternative = async () => {
  if (!documentForm.typeId || !documentForm.fichier) {
    ElMessage.error('Veuillez renseigner le type de document et sélectionner un fichier')
    return
  }

  formLoading.value = true
  try {
    // Créer FormData pour l'upload avec l'ancienne URL
    const formData = new FormData()
    formData.append('fichierDocument', documentForm.fichier)
    formData.append('idPersonnel', personnel.value?.id?.toString() || '')
    formData.append('idDocument', documentForm.typeId?.toString() || '')
    formData.append('dateDepot', documentForm.dateDepot ? new Date(documentForm.dateDepot).toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' }) : new Date().toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' }))
    formData.append('statutpresent', documentForm.present.toString())
    formData.append('numeroReference', documentForm.numeroReference || '')
    formData.append('idStorage', documentForm.locationId?.toString() || '')
    formData.append('description', documentForm.remarques || '')

    const response = await api.post('/personnel/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data.result) {
      ElMessage.success('Document ajouté avec succès')
      closeForm()
      // Recharger la liste des documents
      await loadDocuments(personnel.value?.id)
    } else {
      ElMessage.error('Erreur lors de l\'ajout du document')
    }
  } catch (error) {
    console.error('❌ Erreur lors de l\'upload du document:', error)
    ElMessage.error('Erreur lors de l\'ajout du document')
  } finally {
    formLoading.value = false
  }
}

const saveChild = async () => {
  if (!childForm.nom || !childForm.dateNaissance) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  formLoading.value = true
  try {
    // Utiliser la même fonction de conversion que les contrats
    const formattedDate = convertDateToBackendFormat(childForm.dateNaissance)
    
    if (!formattedDate) {
      ElMessage.error('Date de naissance invalide')
      formLoading.value = false
      return
    }

    console.log('📅 Date formatée pour backend:', formattedDate)

    // Préparation de la requête selon le backend
    const childRequest = {
      id: childForm.id || null, // null pour création, id pour modification
      idPersonnel: personnel.value?.id,
      matricule: childForm.matricule || '',
      nom: childForm.nom,
      dateNaissanceString: formattedDate,
      lieuNaissance: '', // Champ optionnel
      sexe: childForm.sexe,
      ecole: childForm.ecole || '', // ✅ Ajout champ ecole
      aCharge: childForm.aCharge // ✅ Ajout champ aCharge
    }

    console.log('📤 Envoi des données enfant:', childRequest)

    const response = await api.post('/personnel/enregistrerenfant', childRequest)
    
    if (response.data) {
      ElMessage.success('Enfant enregistré avec succès')
      closeForm()
      // Recharger la liste des enfants
      await loadChildren(personnel.value?.id)
    } else {
      ElMessage.error('Erreur lors de l\'enregistrement de l\'enfant')
    }
  } catch (error) {
    console.error('❌ Erreur lors de l\'enregistrement de l\'enfant:', error)
    ElMessage.error('Erreur lors de l\'enregistrement de l\'enfant')
  } finally {
    formLoading.value = false
  }
}

const saveSanction = async () => {
  if (!sanctionForm.type || !sanctionForm.date || !sanctionForm.motif || !sanctionForm.gravite) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  formLoading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    const newSanction = {
      id: Date.now(),
      type: sanctionForm.type,
      date: sanctionForm.date,
      motif: sanctionForm.motif,
      duree: sanctionForm.duree,
      gravite: sanctionForm.gravite,
      statut: 'Active'
    }
    
    sanctions.value.unshift(newSanction)
    ElMessage.success('Sanction ajoutée avec succès')
    closeForm()
  } catch (error) {
    ElMessage.error('Erreur lors de l\'ajout de la sanction')
  } finally {
    formLoading.value = false
  }
}

const saveSpouse = async () => {
  if (!spouseForm.nom || !spouseForm.dateNaissance || !spouseForm.sexe) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  formLoading.value = true
  try {
    // Utiliser la même fonction de conversion que les enfants et contrats
    const formattedDate = convertDateToBackendFormat(spouseForm.dateNaissance)
    
    if (!formattedDate) {
      ElMessage.error('Date de naissance invalide')
      formLoading.value = false
      return
    }

    // Préparation de la requête selon le backend (utilise EnfantRequest)
    const spouseRequest = {
      id: spouseForm.id || null, // null pour création, id pour modification
      idPersonnel: personnel.value?.id,
      matricule: spouseForm.matricule || '',
      nom: spouseForm.nom,
      dateNaissanceString: formattedDate,
      lieuNaissance: spouseForm.lieuNaissance || '',
      telephone: spouseForm.telephone || '',
      sexe: spouseForm.sexe
    }

    console.log('📤 Envoi des données conjoint:', spouseRequest)

    const response = await api.post('/personnel/enregistrerconjoint', spouseRequest)
    
    if (response.data) {
      ElMessage.success('Conjoint enregistré avec succès')
      closeForm()
      // Recharger la liste des conjoints
      await loadSpouses(personnel.value?.id)
    } else {
      ElMessage.error('Erreur lors de l\'enregistrement du conjoint')
    }
  } catch (error) {
    console.error('❌ Erreur lors de l\'enregistrement du conjoint:', error)
    ElMessage.error('Erreur lors de l\'enregistrement du conjoint')
  } finally {
    formLoading.value = false
  }
}

// Fonctions utilitaires
const calculateDuration = (dateDebut: string, dateFin: string) => {
  if (!dateFin) return 'Indéterminé'
  
  const debut = new Date(dateDebut)
  const fin = new Date(dateFin)
  const diffTime = Math.abs(fin.getTime() - debut.getTime())
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  const diffYears = Math.floor(diffDays / 365)
  const diffMonths = Math.floor((diffDays % 365) / 30)
  
  if (diffYears > 0) {
    return `${diffYears} an${diffYears > 1 ? 's' : ''}`
  } else if (diffMonths > 0) {
    return `${diffMonths} mois`
  } else {
    return `${diffDays} jours`
  }
}

const calculateDaysBetween = (dateDebut: string, dateFin: string) => {
  const debut = new Date(dateDebut)
  const fin = new Date(dateFin)
  const diffTime = Math.abs(fin.getTime() - debut.getTime())
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1
}

// Fonctions pour les actions des tableaux
const editContract = (contract: any) => {
  Object.assign(contractForm, contract)
  showForm.value = true
}

const deleteContract = (contract: any) => {
  const index = contracts.value.findIndex(c => c.id === contract.id)
  if (index !== -1) {
    contracts.value.splice(index, 1)
    ElMessage.success('Contrat supprimé avec succès')
  }
}

const editAbsence = async (absence: any) => {
  // S'assurer que les types d'absence sont chargés
  if (absenceTypes.value.length === 0) {
    await loadAbsenceTypes()
  }
  
  // Récupérer les données brutes depuis le backend
  try {
    const response = await api.post('/absence/trouverabsencepersonnel', { 
      id: absence.id,
      idPersonnel: personnel.value.id
    })
    
    // L'API retourne row (singulier) pour une seule absence
    const rawAbsence = response.data?.row || response.data?.rows?.[0]
    
    if (rawAbsence) {
      // Récupérer l'ID et le libellé du type d'absence
      const typeId = rawAbsence.absences?.id || rawAbsence.idAbsence
      const typeLabel = rawAbsence.absences?.faute || 'N/A'
      
      // Vérifier si le type existe dans absenceTypes, sinon l'ajouter temporairement
      const typeExists = absenceTypes.value.some(t => t.id === typeId)
      if (!typeExists && typeId) {
        absenceTypes.value.push({
          id: typeId,
          faute: typeLabel,
          libelle: typeLabel,
          code: String(typeId),
          actif: true
        })
      }
      
      // Charger les valeurs brutes dans le formulaire
      absenceForm.id = rawAbsence.id
      absenceForm.idAbsence = typeId
      absenceForm.dateDebut = rawAbsence.dDebut
      absenceForm.dateFin = rawAbsence.dRet
      absenceForm.duree = rawAbsence.joursabsence || 0
      absenceForm.heuresAbsence = rawAbsence.heursabsence || 0
      absenceForm.motif = rawAbsence.observation || ''
      absenceForm.observation = rawAbsence.observation || ''
      absenceForm.sanctionSalaire = String(rawAbsence.sanctionsalaire || 0)  // ✅ Convertir en string pour le select
      absenceForm.statut = rawAbsence.statut || false
      
      showForm.value = true
    } else {
      ElMessage.error('Impossible de charger les données de l\'absence')
    }
  } catch (error: any) {
    console.error('❌ Erreur lors du chargement de l\'absence:', error)
    ElMessage.error('Erreur lors du chargement: ' + (error.response?.data?.message || error.message))
  }
}

const deleteAbsence = async (absence: any) => {
  try {
    // Confirmation avant suppression
    await ElMessageBox.confirm(
      'Êtes-vous sûr de vouloir supprimer cette absence ?',
      'Confirmation de suppression',
      {
        confirmButtonText: 'Oui, supprimer',
        cancelButtonText: 'Annuler',
        type: 'warning'
      }
    )
    
    // Appel API pour supprimer l'absence
    const response = await api.post('/absence/supprimerabsencepersonnel', { id: absence.id })
    
    if (response.data && response.data.result) {
      // Suppression locale après succès API
      const index = absences.value.findIndex(a => a.id === absence.id)
      if (index !== -1) {
        absences.value.splice(index, 1)
      }
      ElMessage.success('Absence supprimée avec succès')
    } else {
      ElMessage.error(response.data?.message || 'Erreur lors de la suppression')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('❌ Erreur lors de la suppression de l\'absence:', error)
      ElMessage.error('Erreur lors de la suppression: ' + (error.response?.data?.message || error.message))
    }
  }
}

// Fonction pour obtenir le libellé du type d'absence par ID
const getAbsenceTypeLabel = (idAbsence: number | null): string => {
  const type = absenceTypes.value.find(t => t.id === idAbsence)
  return type?.faute || 'N/A'
}

// Fonction pour convertir le code sanctionsalaire en libellé
const getSanctionSalaireLabel = (code: number | string): string => {
  const codeNum = typeof code === 'string' ? parseInt(code) : code
  switch (codeNum) {
    case 3: return 'Aucun'
    case 4: return 'Salaire'
    case 2: return 'Conge'
    default: return 'N/A'
  }
}

const deleteAssignment = (assignment: any) => {
  const index = assignments.value.findIndex(a => a.id === assignment.id)
  if (index !== -1) {
    assignments.value.splice(index, 1)
    ElMessage.success('Affectation supprimée avec succès')
  }
}

const editLeaveMovement = (movement: any) => {
  Object.assign(leaveMovementForm, movement)
  showForm.value = true
}

const deleteLeaveMovement = (movement: any) => {
  const index = leaveMovements.value.findIndex(m => m.id === movement.id)
  if (index !== -1) {
    leaveMovements.value.splice(index, 1)
    ElMessage.success('Mouvement de congé supprimé avec succès')
  }
}

const deleteDocument = (document: any) => {
  const index = documents.value.findIndex(d => d.id === document.id)
  if (index !== -1) {
    documents.value.splice(index, 1)
    ElMessage.success('Document supprimé avec succès')
  }
}

const editChild = (child: any) => {
  Object.assign(childForm, child)
  showForm.value = true
}

const deleteChild = async (childId: number) => {
  try {
    await ElMessageBox.confirm('Êtes-vous sûr de vouloir supprimer cet enfant ?', 'Confirmation', {
      confirmButtonText: 'Oui',
      cancelButtonText: 'Non',
      type: 'warning'
    })

    const response = await api.post('/personnel/supprimerenfant', { id: childId })
    
    if (response.data) {
      ElMessage.success('Enfant supprimé avec succès')
      // Recharger la liste des enfants
      await loadChildren(personnel.value?.id)
    } else {
      ElMessage.error('Erreur lors de la suppression de l\'enfant')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('❌ Erreur lors de la suppression de l\'enfant:', error)
      ElMessage.error('Erreur lors de la suppression de l\'enfant')
    }
  }
}

const editSanction = (sanction: any) => {
  Object.assign(sanctionForm, sanction)
  showForm.value = true
}

const deleteSanction = (sanction: any) => {
  const index = sanctions.value.findIndex(s => s.id === sanction.id)
  if (index !== -1) {
    sanctions.value.splice(index, 1)
    ElMessage.success('Sanction supprimée avec succès')
  }
}

const editSpouse = (spouse: any) => {
  Object.assign(spouseForm, spouse)
  showForm.value = true
}

const deleteSpouse = async (spouseId: number) => {
  try {
    await ElMessageBox.confirm('Êtes-vous sûr de vouloir supprimer ce conjoint ?', 'Confirmation', {
      confirmButtonText: 'Oui',
      cancelButtonText: 'Non',
      type: 'warning'
    })

    const response = await api.post('/personnel/supprimerconjoint', { id: spouseId })
    
    if (response.data) {
      ElMessage.success('Conjoint supprimé avec succès')
      // Recharger la liste des conjoints
      await loadSpouses(personnel.value?.id)
    } else {
      ElMessage.error('Erreur lors de la suppression du conjoint')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('❌ Erreur lors de la suppression du conjoint:', error)
      ElMessage.error('Erreur lors de la suppression du conjoint')
    }
  }
}
</script>

<style scoped>
.personnel-detail-view {
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
}

.header-actions {
  display: flex;
  gap: 12px;
}

.selected-info {
  margin-top: 5px;
  padding: 4px 8px;
  background-color: #f0f9ff;
  border: 1px solid #409eff;
  border-radius: 4px;
  color: #409eff;
  font-size: 12px;
  font-weight: 500;
}

.selected-info small {
  margin: 0;
  line-height: 1.2;
}

.filename {
  color: #409eff;
  font-weight: 500;
  font-size: 12px;
  word-break: break-all;
  max-width: 200px;
  display: inline-block;
}

.no-file {
  color: #909399;
  font-style: italic;
  font-size: 12px;
}

.detail-content {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.content-layout {
  display: flex;
  gap: 24px;
  height: calc(100vh - 300px);
}

.sidebar-panel {
  width: 450px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  
  .panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    border-bottom: 1px solid #f0f0f0;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 18px;
      font-weight: 600;
    }
  }
  
  .form-content {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
  }
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
    padding: 20px;
    border-bottom: 1px solid #f0f0f0;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 18px;
      font-weight: 600;
    }
    
    .panel-controls {
      display: flex;
      gap: 12px;
    }
  }
}

.detail-header {
  display: flex;
  align-items: flex-start;
  gap: 32px;
  padding: 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.status-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: center;
}

.matricule-tag {
  opacity: 0.9;
}

.main-info {
  flex: 1;
}

.main-info h1 {
  margin: 0 0 16px 0;
  font-size: 32px;
  font-weight: 600;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  opacity: 0.9;
}

.contact-info {
  display: flex;
  gap: 24px;
  margin-top: 16px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 6px;
  opacity: 0.9;
}

.salary-section {
  display: flex;
  align-items: center;
}

.salary-card {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  min-width: 200px;
}

.salary-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 8px;
}

.salary-amount {
  font-size: 24px;
  font-weight: 700;
}

.detail-tabs {
  padding: 0 24px 24px 24px;
}

.tab-content {
  padding: 24px 0;
}

.info-card {
  height: 100%;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #303133;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item label {
  font-weight: 500;
  color: #606266;
  min-width: 140px;
}

.info-item span {
  color: #303133;
  font-weight: 500;
}

.salary-highlight {
  color: #67c23a;
  font-size: 18px;
  font-weight: 600;
}

.actions-card {
  text-align: center;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.action-grid .el-button {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border-radius: 8px;
}

.loading-state {
  padding: 40px;
}

/* Responsive */
@media (max-width: 768px) {
  .detail-header {
    flex-direction: column;
    text-align: center;
    gap: 24px;
  }
  
  .header-content {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .contact-info {
    flex-direction: column;
    gap: 12px;
  }
  
  .action-grid {
    grid-template-columns: 1fr;
  }
}

/* Element Plus overrides */
:deep(.el-tabs__header) {
  margin: 0;
}

:deep(.el-tabs__nav-wrap) {
  padding: 0 24px;
}

:deep(.el-tabs__content) {
  padding: 0;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-skeleton__item) {
  border-radius: 8px;
}

/* Style pour le bouton de création de contrat désactivé */
:deep(.el-button.is-disabled) {
  opacity: 0.6;
  cursor: not-allowed;
}

:deep(.el-alert) {
  border-radius: 8px;
}

/* Style pour la grille du formulaire */
.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.form-grid .el-form-item {
  margin-bottom: 0;
}

/* Style pour les boutons d'action du formulaire */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.form-actions .el-button {
  display: flex;
  align-items: center;
  gap: 6px;
}
</style>
