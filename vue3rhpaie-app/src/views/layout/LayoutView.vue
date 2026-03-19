<template>
  <div class="layout-container">
    <!-- Sidebar -->
    <div class="sidebar" :class="{ collapsed: isCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <h2>RHPAIE</h2>
        </div>
        <el-button
          class="collapse-btn"
          text
          @click="toggleSidebar"
        >
          <el-icon><Expand v-if="isCollapsed" /><Fold v-else /></el-icon>
        </el-button>
      </div>
      
      <el-menu
        :default-active="$route.path"
        class="sidebar-menu"
        :collapse="isCollapsed"
        unique-opened
      >
        <el-menu-item index="/" @click="navigateTo('/')">
          <el-icon><Odometer /></el-icon>
          <template #title>Tableau de bord</template>
        </el-menu-item>
        
        <el-sub-menu
          index="parametrage"
          v-if="authStore.isAuthenticated"
        >
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>Paramétrage RH</span>
          </template>
          
          <el-menu-item
            index="/parametrage/organisation"
            @click="navigateTo('/parametrage/organisation')"
            v-if="authStore.isAuthenticated"
          >
            <el-icon><OfficeBuilding /></el-icon>
            <template #title>Organisation</template>
          </el-menu-item>
          
          <el-menu-item
            index="/parametrage/categories"
            @click="navigateTo('/parametrage/categories')"
            v-if="authStore.isAuthenticated"
          >
            <el-icon><Collection /></el-icon>
            <template #title>Catégories</template>
          </el-menu-item>
          
          <el-menu-item
            index="/parametrage/fonctions"
            @click="navigateTo('/parametrage/fonctions')"
            v-if="authStore.isAuthenticated"
          >
            <el-icon><Collection /></el-icon>
            <template #title>Emplois/Fonctions</template>
          </el-menu-item>
          
          <el-menu-item
            index="/parametrage/referentiels"
            @click="navigateTo('/parametrage/referentiels')"
            v-if="authStore.isAuthenticated"
          >
            <el-icon><Collection /></el-icon>
            <template #title>Référentiels RH</template>
          </el-menu-item>
          
          <el-menu-item
            index="/parametrage/temps-absences"
            @click="navigateTo('/parametrage/temps-absences')"
            v-if="authStore.isAuthenticated"
          >
            <el-icon><Clock /></el-icon>
            <template #title>Temps & Absences</template>
          </el-menu-item>
          
          <el-menu-item
            index="/parametrage/paie"
            @click="navigateTo('/parametrage/paie')"
            v-if="authStore.isAuthenticated"
          >
            <el-icon><Money /></el-icon>
            <template #title>Paramétrage Paie</template>
          </el-menu-item>
        </el-sub-menu>
        
        <el-menu-item
          index="/personnel"
          @click="navigateTo('/personnel')"
          v-if="authStore.isAuthenticated"
        >
          <el-icon><User /></el-icon>
          <template #title>Gestion du Personnel</template>
        </el-menu-item>
        
        <el-sub-menu
          index="paie"
          v-if="authStore.hasRole('ADMIN') || authStore.hasAnyPermission([
            { resource: 'bulletins', action: 'read' },
            { resource: 'etats', action: 'read' }
          ])"
        >
          <template #title>
            <el-icon><Money /></el-icon>
            <span>Module Paie</span>
          </template>
          
          <el-menu-item
            index="/paie/bulletins"
            @click="navigateTo('/paie/bulletins')"
            v-if="authStore.hasRole('ADMIN') || authStore.hasPermission('bulletins', 'read')"
          >
            <el-icon><Document /></el-icon>
            <template #title>Bulletins de paie</template>
          </el-menu-item>
          
          <el-menu-item
            index="/paie/etats"
            @click="navigateTo('/paie/etats')"
            v-if="authStore.hasRole('ADMIN') || authStore.hasPermission('etats', 'read')"
          >
            <el-icon><DataAnalysis /></el-icon>
            <template #title>États de paie</template>
          </el-menu-item>
        </el-sub-menu>
        
        <el-menu-item
          index="/reporting"
          @click="navigateTo('/reporting')"
          v-if="authStore.hasRole('ADMIN') || authStore.hasPermission('reporting', 'read')"
        >
          <el-icon><PieChart /></el-icon>
          <template #title>Reporting</template>
        </el-menu-item>
      </el-menu>
    </div>
    
    <!-- Main Content -->
    <div class="main-content" :class="{ collapsed: isCollapsed }">
      <!-- Header -->
      <header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">Accueil</el-breadcrumb-item>
            <el-breadcrumb-item v-if="$route.meta.title">
              {{ $route.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right">
          <el-dropdown @command="handleUserMenu">
            <div class="user-info">
              <el-avatar :size="32" :icon="UserFilled" />
              <span class="username">{{ authStore.user?.firstName }} {{ authStore.user?.lastName }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  Mon profil
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <el-icon><Setting /></el-icon>
                  Paramètres
                </el-dropdown-item>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import {
  Expand,
  Fold,
  Odometer,
  Setting,
  OfficeBuilding,
  Collection,
  Clock,
  Money,
  User,
  Document,
  DataAnalysis,
  PieChart,
  UserFilled,
  ArrowDown,
  SwitchButton
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import TagsView from '@/components/TagsView.vue'

const router = useRouter()
const authStore = useAuthStore()

const isCollapsed = ref(false)

onMounted(() => {
  // Vérifier l'authentification au chargement
  authStore.initializeAuth()
  
  // Debug: Afficher les informations de l'utilisateur
  console.log('Debug - User:', authStore.user)
  console.log('Debug - Role:', authStore.userRole)
  console.log('Debug - Permissions:', authStore.permissions)
  console.log('Debug - Has ADMIN role:', authStore.hasRole('ADMIN'))
})

const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

const navigateTo = (path: string) => {
  router.push(path)
}

const handleUserMenu = async (command: string) => {
  switch (command) {
    case 'profile':
      // TODO: Navigate to profile
      break
    case 'settings':
      // TODO: Navigate to settings
      break
    case 'logout':
      try {
        await ElMessageBox.confirm(
          'Êtes-vous sûr de vouloir vous déconnecter?',
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
.layout-container {
  display: flex;
  height: 100vh;
  background-color: #f5f5f5;
}

.sidebar {
  width: 250px;
  background: #fff;
  border-right: 1px solid #e4e7ed;
  transition: width 0.3s ease;
  overflow: hidden;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  border-bottom: 1px solid #e4e7ed;
}

.logo h2 {
  margin: 0;
  color: #409eff;
  font-size: 1.5rem;
  font-weight: 700;
}

.collapse-btn {
  font-size: 18px;
}

.sidebar-menu {
  border-right: none;
  height: calc(100vh - 60px);
  overflow-y: auto;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-left: 0;
  transition: margin-left 0.3s ease;
}

.main-content.collapsed {
  margin-left: 0;
}

.header {
  height: 60px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: background-color 0.3s ease;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  font-weight: 500;
  color: #303133;
}

.page-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    left: 0;
    top: 0;
    height: 100vh;
    z-index: 1000;
    transform: translateX(-100%);
  }
  
  .sidebar:not(.collapsed) {
    transform: translateX(0);
  }
  
  .main-content {
    margin-left: 0;
  }
  
  .header {
    padding: 0 15px;
  }
  
  .page-content {
    padding: 15px;
  }
}
</style>
