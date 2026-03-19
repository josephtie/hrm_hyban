<template>
  <div class="utilisateur-view">
    <!-- Header aligné sur Organisation -->
    <div class="page-header">
      <h1>Paramétrage des Utilisateurs</h1>
      <p>Gestion des comptes utilisateurs et des accès</p>
    </div>

    <div class="main-content">
      <!-- Colonne de gauche avec le formulaire -->
      <div class="sidebar-panel" v-if="showForm">
        <div class="panel-header">
          <h3>{{ isEditing ? 'Modifier' : 'Ajouter' }} un Utilisateur</h3>
          <el-button @click="closeForm" circle>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
        
        <el-form :model="form" label-width="100px" style="padding: 20px 20px 10px 20px; flex: 1; overflow-y: auto;">
          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><UserFilled /></el-icon>
              Rôle
            </label>
            <el-select v-model="form.idRole" placeholder="Rôle de l'utilisateur" size="large">
              <el-option label="Administrateur" :value="1" />
              <el-option label="DAF" :value="2" />
              <el-option label="RH" :value="3" />
              <el-option label="Pointage" :value="4" />
            </el-select>
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><User /></el-icon>
              Nom & Prénom(s)
            </label>
            <el-input 
              v-model="form.nomComplet" 
              placeholder="Nom et prénom(s)"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Key /></el-icon>
              Username
            </label>
            <el-input 
              v-model="form.username" 
              placeholder="Nom d'utilisateur"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Message /></el-icon>
              Email
            </label>
            <el-input 
              v-model="form.email" 
              type="email"
              placeholder="Adresse email"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><Phone /></el-icon>
              Téléphone
            </label>
            <el-input 
              v-model="form.telephone" 
              placeholder="Numéro de téléphone"
              size="large"
              maxlength="8"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><House /></el-icon>
              Adresse
            </label>
            <el-input 
              v-model="form.adresse" 
              placeholder="Adresse complète"
              size="large"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              <el-icon class="label-icon"><SwitchButton /></el-icon>
              Statut
            </label>
            <el-radio-group v-model="form.actif" size="large">
              <el-radio :value="true">Actif</el-radio>
              <el-radio :value="false">Inactif</el-radio>
            </el-radio-group>
          </div>

          <div class="form-actions">
            <el-button @click="closeForm" size="large">Annuler</el-button>
            <el-button type="primary" @click="saveForm" size="large">
              {{ isEditing ? 'Mettre à jour' : 'Créer' }}
            </el-button>
          </div>
        </el-form>
      </div>

      <!-- Colonne principale avec le tableau -->
      <div class="main-panel">
        <div class="panel-header">
          <h3>Liste des Utilisateurs</h3>
          <div class="panel-controls">
            <el-button @click="refreshData" circle>
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button @click="toggleForm" type="primary">
              <el-icon><Plus /></el-icon>
              Nouvel Utilisateur
            </el-button>
          </div>
        </div>

        <!-- Barre d'outils -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchText"
              placeholder="Rechercher un utilisateur..."
              style="width: 300px"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>
          
          <div class="toolbar-right">
            <el-select
              v-model="filterRole"
              placeholder="Rôle"
              style="width: 150px"
              clearable
            >
              <el-option label="Administrateur" value="ADMINISTRATEUR" />
              <el-option label="DAF" value="DAF" />
              <el-option label="RH" value="RH" />
              <el-option label="Pointage" value="POINTAGE" />
            </el-select>
            <el-select
              v-model="filterStatut"
              placeholder="Statut"
              style="width: 120px"
              clearable
            >
              <el-option label="Actif" :value="true" />
              <el-option label="Inactif" :value="false" />
            </el-select>
          </div>
        </div>

        <!-- Tableau des utilisateurs -->
        <div class="table-container">
          <el-table 
            :data="filteredUtilisateurs" 
            style="width: 100%"
            @selection-change="handleSelectionChange"
            v-loading="loading"
          >
            <el-table-column type="selection" width="55" />
            
            <el-table-column prop="nom" label="Nom" min-width="200" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 10px;">
                  <el-avatar :size="32" :src="`https://api.dicebear.com/7.x/avataaars/svg?seed=${row.username}`">
                    <el-icon><User /></el-icon>
                  </el-avatar>
                  <div>
                    <div style="font-weight: 600;">{{ row.firstName }} {{ row.lastName }}</div>
                    <div style="font-size: 12px; color: #999;">@{{ row.username }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="email" label="Email" min-width="200" sortable>
              <template #default="{ row }">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><Message /></el-icon>
                  <span>{{ row.email }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="roles" label="Rôles" width="150" sortable>
              <template #default="{ row }">
                <div style="display: flex; flex-direction: column; gap: 4px;">
                  <el-tag 
                    v-for="role in (row.roles || []).filter(role => role !== 'default-roles-hyban')" 
                    :key="role" 
                    :type="getRoleColor(role)" 
                    size="small"
                  >
                    {{ role }}
                  </el-tag>
                  <span v-if="!row.roles || row.roles.filter(role => role !== 'default-roles-hyban').length === 0" style="color: #999; font-size: 12px;">
                    Aucun rôle
                  </span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="enabled" label="Statut" width="100" sortable>
              <template #default="{ row }">
                <el-tag :type="row.enabled ? 'success' : 'danger'" size="large">
                  {{ row.enabled ? 'Actif' : 'Inactif' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column prop="lastLogin" label="Dernière connexion" width="150" sortable>
              <template #default="{ row }">
                <div v-if="row.lastLogin" style="display: flex; align-items: center; gap: 8px;">
                  <el-icon><Clock /></el-icon>
                  <span>{{ formatDate(row.lastLogin) }}</span>
                </div>
                <span v-else style="color: #999; font-size: 12px;">
                  Jamais connecté
                </span>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="120" fixed="right">
              <template #default="{ row }">
                <el-button-group>
                  <el-button size="small" @click="editUtilisateur(row)" type="primary">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" @click="toggleStatut(row)" :type="row.enabled ? 'warning' : 'success'">
                    <el-icon><SwitchButton /></el-icon>
                  </el-button>
                  <el-button size="small" @click="deleteUtilisateur(row)" type="danger">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, Edit, Delete, Search, Refresh, Close, User, UserFilled, Key,
  Message, Phone, House, SwitchButton
} from '@element-plus/icons-vue'
import { utilisateurrestService, type UtilisateurRestDto, type CreateUserRequest } from '@/services/utilisateurrest.service'

interface Utilisateur {
  id: string
  username: string
  email: string
  firstName: string
  lastName: string
  roles?: string[]
  enabled?: boolean
  accountNonExpired?: boolean
  credentialsNonExpired?: boolean
  accountNonLocked?: boolean
  authorities?: { authority: string }[]
  lastLogin?: string
}

const showForm = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const searchText = ref('')
const filterRole = ref('')
const filterStatut = ref<boolean | null>(null)
const selectedUtilisateurs = ref<Utilisateur[]>([])
const utilisateurs = ref<Utilisateur[]>([])

const filteredUtilisateurs = computed(() => {
  let filtered = utilisateurs.value

  if (searchText.value) {
    filtered = filtered.filter(utilisateur => 
      utilisateur.firstName.toLowerCase().includes(searchText.value.toLowerCase()) ||
      utilisateur.lastName.toLowerCase().includes(searchText.value.toLowerCase()) ||
      utilisateur.username.toLowerCase().includes(searchText.value.toLowerCase()) ||
      utilisateur.email.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  if (filterRole.value) {
    filtered = filtered.filter(utilisateur => {
      const roles = utilisateur.roles || []
      return roles.some(role => role.includes(filterRole.value))
    })
  }

  if (filterStatut.value !== null) {
    filtered = filtered.filter(utilisateur => utilisateur.enabled === filterStatut.value)
  }

  return filtered
})

const form = reactive<CreateUserRequest>({
  id: 0,
  username: '',
  email: '',
  nomComplet: '',
  password: '',
  dateNaissance: '',
  telephone: '',
  adresse: '',
  idRole: undefined,
  actif: true
})

// Charger les données depuis le backend
const loadUtilisateurs = async () => {
  try {
    loading.value = true
    const response = await utilisateurrestService.getAll({
      page: 0,
      size: 100,
      search: searchText.value
    })
    
    // Transformer les données du backend pour la vue
    utilisateurs.value = response.data.map((item: any) => ({
      id: item.id,
      username: item.username,
      email: item.email,
      firstName: item.firstName || '',
      lastName: item.lastName || '',
      roles: item.roles || [],
      enabled: item.enabled || false,
      lastLogin: item.lastLogin
    }))
  } catch (error) {
    ElMessage.error('Erreur lors du chargement des utilisateurs')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString: string | null) => {
  if (!dateString) return 'N/A'
  return new Date(dateString).toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: '2-digit', 
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getRoleColor = (role: string) => {
  const colors = {
    'ADMIN': 'danger',
    'DAF': 'warning',
    'RH': 'primary',
    'POINTAGE': 'info'
  }
  return colors[role as keyof typeof colors] || 'info'
}

const toggleForm = () => {
  showForm.value = !showForm.value
  if (!showForm.value) {
    resetForm()
  }
}

const closeForm = () => {
  showForm.value = false
  resetForm()
}

const resetForm = () => {
  Object.assign(form, {
    id: 0,
    username: '',
    email: '',
    nomComplet: '',
    password: '',
    dateNaissance: '',
    telephone: '',
    adresse: '',
    idRole: undefined,
    actif: true
  })
  isEditing.value = false
}

const saveForm = async () => {
  if (!form.nomComplet || !form.username || !form.email || !form.idRole) {
    ElMessage.error('Veuillez renseigner tous les champs obligatoires')
    return
  }

  try {
    loading.value = true
    let response
    
    if (isEditing.value) {
      response = await utilisateurrestService.update(form)
      if (response.result === 'success') {
        ElMessage.success('Utilisateur mis à jour avec succès')
        await loadUtilisateurs()
      }
    } else {
      response = await utilisateurrestService.create(form)
      if (response.result === 'success') {
        ElMessage.success('Utilisateur créé avec succès')
        await loadUtilisateurs()
      }
    }

    closeForm()
  } catch (error) {
    ElMessage.error('Erreur lors de l\'enregistrement')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const editUtilisateur = (utilisateur: Utilisateur) => {
  Object.assign(form, {
    id: utilisateur.id,
    username: utilisateur.username,
    email: utilisateur.email,
    firstName: utilisateur.firstName,
    lastName: utilisateur.lastName,
    roles: utilisateur.roles || []
  })
  isEditing.value = true
  showForm.value = true
}

const toggleStatut = async (utilisateur: Utilisateur) => {
  try {
    const nomComplet = `${utilisateur.firstName} ${utilisateur.lastName}`.trim()
    const action = utilisateur.enabled ? 'désactiver' : 'activer'
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir ${action} l'utilisateur ${nomComplet} ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    )
    
    await utilisateurrestService.toggleUserStatus(utilisateur.id, !utilisateur.enabled)
    ElMessage.success(`Utilisateur ${action} avec succès`)
    await loadUtilisateurs()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors du changement de statut')
      console.error(error)
    }
  }
}

const deleteUtilisateur = async (utilisateur: Utilisateur) => {
  try {
    const nomComplet = `${utilisateur.firstName} ${utilisateur.lastName}`.trim()
    await ElMessageBox.confirm(
      `Êtes-vous sûr de vouloir supprimer l'utilisateur ${nomComplet} ?`,
      'Confirmation',
      { confirmButtonText: 'Oui', cancelButtonText: 'Non', type: 'warning' }
    )
    
    await utilisateurrestService.delete(utilisateur.id)
    ElMessage.success('Utilisateur supprimé avec succès')
    await loadUtilisateurs()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Erreur lors de la suppression')
      console.error(error)
    }
  }
}

const refreshData = async () => {
  await loadUtilisateurs()
  ElMessage.success('Données actualisées')
}

const handleSelectionChange = (selection: Utilisateur[]) => {
  selectedUtilisateurs.value = selection
}

onMounted(() => {
  loadUtilisateurs()
})
</script>

<style scoped>
.utilisateur-view {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0 0 5px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
}

.main-content {
  display: flex;
  gap: 20px;
  height: calc(100vh - 120px);
}

.sidebar-panel {
  width: 400px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.panel-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.panel-controls {
  display: flex;
  gap: 10px;
}

.main-panel {
  flex: 1;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.toolbar-right {
  display: flex;
  gap: 10px;
}

.table-container {
  flex: 1;
  overflow: hidden;
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-weight: 500;
  color: #303133;
  font-size: 14px;
}

.label-icon {
  color: #909399;
  font-size: 16px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.form-group :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #dcdfe6;
  transition: all 0.3s ease;
}

.form-group :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc;
}

.form-group :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.form-group :deep(.el-radio-group) {
  display: flex;
  gap: 20px;
}

.form-group :deep(.el-input__prefix) {
  color: #909399;
}
</style>
