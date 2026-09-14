<template>
  <div class="layout">
    <!-- Sidebar -->
    <SidebarMenu 
      :is-collapsed="isCollapsed" 
      @toggle="toggleSidebar" 
    />
    
    <!-- Main Content -->
    <div class="main-content" :class="{ collapsed: isCollapsed }">
      <!-- Header -->
      <header class="header">
        <div class="header-left">
          <h1>{{ currentPageTitle }}</h1>
        </div>
        
        <div class="header-right">
          <el-dropdown @command="handleUserMenu">
            <div class="user-info">
              <el-icon><UserFilled /></el-icon>
              <span>{{ authStore.user?.firstName }} {{ authStore.user?.lastName }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">Mon profil</el-dropdown-item>
                <el-dropdown-item command="account">
                  <el-icon><Key /></el-icon>
                  Gérer mon compte
                </el-dropdown-item>
                <el-dropdown-item command="settings">Paramètres</el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  Déconnexion
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      
      <!-- Tags View -->
      <TagsView />
      
      <!-- Page Content -->
      <main class="page-content">
        <router-view />
      </main>
    </div>

    <!-- Modal de changement de mot de passe -->
    <el-dialog
      v-model="passwordDialogVisible"
      title="Changer mon mot de passe"
      width="450px"
      :close-on-click-modal="false"
    >
      <el-form label-position="top" class="password-form">
        <el-form-item label="Nouveau mot de passe">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="Nouveau mot de passe"
            show-password
            size="large"
          />
        </el-form-item>
        <el-form-item label="Confirmer le mot de passe">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="Confirmer le mot de passe"
            show-password
            size="large"
            @keyup.enter="submitPasswordChange"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">Annuler</el-button>
        <el-button
          type="primary"
          @click="submitPasswordChange"
          :loading="passwordLoading"
          :disabled="!passwordForm.newPassword || !passwordForm.confirmPassword"
        >
          Mettre à jour
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  UserFilled,
  ArrowDown,
  SwitchButton,
  Key
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { api } from '@/services/api'
import { usePasswordDialog } from '@/composables/usePasswordDialog'
import SidebarMenu from '@/components/navigation/SidebarMenu.vue'
import TagsView from '@/components/navigation/TagsView.vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const isCollapsed = ref(false)

const { visible: passwordDialogVisible, open: openPasswordDialog } = usePasswordDialog()
const passwordLoading = ref(false)
const passwordForm = reactive({
  newPassword: '',
  confirmPassword: ''
})

const submitPasswordChange = async () => {
  if (!passwordForm.newPassword || passwordForm.newPassword.length < 6) {
    ElMessage.error('Le mot de passe doit contenir au moins 6 caractères')
    return
  }
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error('Les mots de passe ne correspondent pas')
    return
  }
  try {
    passwordLoading.value = true
    await api.put('/users/me/password', { newPassword: passwordForm.newPassword })
    ElMessage.success('Mot de passe modifié avec succès')
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
    passwordDialogVisible.value = false
  } catch (error: any) {
    ElMessage.error('Erreur lors du changement de mot de passe: ' + (error.response?.data || error.message || 'Erreur inconnue'))
  } finally {
    passwordLoading.value = false
  }
}

const currentPageTitle = computed(() => {
  return route.meta?.title || 'Tableau de bord'
})

onMounted(() => {
  authStore.initializeAuth()
})

const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

const handleUserMenu = async (command: string) => {
  switch (command) {
    case 'profile':
      // TODO: Navigate to profile
      break
    case 'account':
      openPasswordDialog()
      break
    case 'settings':
      // TODO: Navigate to settings
      break
    case 'logout':
      try {
        await ElMessageBox.confirm(
          'Êtes-vous sûr de vouloir vous déconnecter ?',
          'Confirmation',
          {
            confirmButtonText: 'Oui',
            cancelButtonText: 'Non',
            type: 'warning'
          }
        )
        await authStore.logout() // ✅ Le store gère la redirection
        // ❌ Supprimer : router.push('/login')
      } catch {
        // User cancelled
      }
      break
  }
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-left: 250px;
  transition: margin-left 0.3s;
  height: 100vh;
  overflow: hidden;
}

.main-content.collapsed {
  margin-left: 64px;
}

.header {
  height: 60px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  flex-shrink: 0;
}

.header-left h1 {
  margin: 0;
  font-size: 20px;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 4px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f7fa;
}

.page-content {
  flex: 1;
  padding: 20px;
  background: #f5f7fa;
  overflow-y: auto;
  height: calc(100vh - 60px);
}
</style>
