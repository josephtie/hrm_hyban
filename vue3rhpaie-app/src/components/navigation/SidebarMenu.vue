<template>
  <div class="sidebar">
    <div class="sidebar-header">
      <h2 v-if="!isCollapsed">RH Paie</h2>
      <button 
        class="toggle-btn"
        @click="toggleSidebar"
      >
        <el-icon><Expand v-if="isCollapsed" /><Fold v-else /></el-icon>
      </button>
    </div>
    
    <div class="navigation-title">
      <span v-if="!isCollapsed">Navigation</span>
      <span v-if="!isCollapsed" class="periode">{{ currentPeriode }}</span>
    </div>
    
    <el-menu
      :default-active="$route.path"
      class="sidebar-menu"
      :collapse="isCollapsed"
      :router="true"
      unique-opened
    >
      <el-menu-item index="/">
        <el-icon><Monitor /></el-icon>
        <template #title>Tableau de bord</template>
      </el-menu-item>
      
      <!-- Organisation -->
      <el-sub-menu
        index="organisation"
        v-if="authStore.isAdmin() || authStore.hasPermissionByRole('organisation', 'read')"
      >
        <template #title>
          <el-icon><OfficeBuilding /></el-icon>
          <span>Organisation</span>
        </template>
        <el-menu-item index="/parametrage/organisation">
          <el-icon><Grid /></el-icon>
          <template #title>Services</template>
        </el-menu-item>
      </el-sub-menu>
      
      <!-- Gestion RH -->
      <el-sub-menu
        index="gestion-rh"
        v-if="authStore.isAdmin() || authStore.hasAnyPermission([
          { resource: 'personnel', action: 'read' },
          { resource: 'temps_absences', action: 'read' }
        ])"
      >
        <template #title>
          <el-icon><User /></el-icon>
          <span>Gestion RH</span>
        </template>
        
        <el-menu-item index="/personnel">
          <el-icon><User /></el-icon>
          <template #title>Personnel</template>
        </el-menu-item>
        
                
        <el-menu-item index="/personnel/contrats">
          <el-icon><Document /></el-icon>
          <template #title>Contrat</template>
        </el-menu-item>
        
        <el-menu-item index="/personnel/categories">
          <el-icon><Collection /></el-icon>
          <template #title>Catégories Professionnelles</template>
        </el-menu-item>
        
        <el-menu-item index="/personnel/referentiels">
          <el-icon><Collection /></el-icon>
          <template #title>Referentiels</template>
        </el-menu-item>
        
        <el-menu-item index="/personnel/fonctions">
          <el-icon><Briefcase /></el-icon>
          <template #title>Emploi & Fonction</template>
        </el-menu-item>
        
        <el-menu-item index="/parametrage/sanctions">
          <el-icon><Warning /></el-icon>
          <template #title>Sanctions</template>
        </el-menu-item>
        
        <el-menu-item index="/personnel/temps-absences">
          <el-icon><Clock /></el-icon>
          <template #title>Absences</template>
          <el-badge value="New!" class="new-badge" />
        </el-menu-item>
      </el-sub-menu>
      
      <!-- Pointage -->
      <el-sub-menu
        index="pointage"
        v-if="authStore.isAdmin() || authStore.hasPermissionByRole('personnel', 'read')"
      >
        <template #title>
          <el-icon><Bell /></el-icon>
          <span>Pointage</span>
        </template>
        
        <el-menu-item index="/pointage/pointages">
          <el-icon><Clock /></el-icon>
          <template #title>Pointage</template>
        </el-menu-item>
        
        <el-menu-item index="/pointage/impression">
          <el-icon><Printer /></el-icon>
          <template #title>Impression</template>
        </el-menu-item>
      </el-sub-menu>
      
      <!-- Gestion de la Paie -->
      <el-sub-menu
        index="paie"
        v-if="authStore.isAdmin() || authStore.hasAnyPermission([
          { resource: 'bulletins', action: 'read' },
          { resource: 'etats', action: 'read' },
          { resource: 'paie', action: 'read' }
        ])"
      >
        <template #title>
          <el-icon><Money /></el-icon>
          <span>Gestion de la Paie</span>
        </template>
        
        <el-sub-menu index="paie-saisie">
          <template #title>
            <el-icon><List /></el-icon>
            <span>Paie</span>
          </template>
          
          <el-menu-item index="/paie/saisie-elements">
            <el-icon><EditPen /></el-icon>
            <template #title>Saisie des éléments</template>
          </el-menu-item>
          
          <el-menu-item index="/paie/livre-paie">
            <el-icon><Document /></el-icon>
            <template #title>Livre de paie</template>
          </el-menu-item>
          
          <el-menu-item index="/paie/historique-bulletins">
            <el-icon><FolderOpened /></el-icon>
            <template #title>Historique Bulletin</template>
          </el-menu-item>
          
          <el-menu-item index="/paie/depart-cdd">
            <el-icon><Switch /></el-icon>
            <template #title>Depart CDD</template>
          </el-menu-item>
          
          <el-menu-item index="/paie/provision-conges">
            <el-icon><Grid /></el-icon>
            <template #title>Provision congé</template>
          </el-menu-item>
          
          <el-menu-item index="/paie/livre-gratification">
            <el-icon><DataBoard /></el-icon>
            <template #title>Livre de gratification</template>
          </el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/paie/bulletins">
          <el-icon><Document /></el-icon>
          <template #title>Bulletins de paie</template>
        </el-menu-item>
        
        <el-menu-item index="/paie/etats">
          <el-icon><PieChart /></el-icon>
          <template #title>États de paie</template>
        </el-menu-item>
        
        <el-menu-item index="/paie/prets">
          <el-icon><CreditCard /></el-icon>
          <template #title>Prêts</template>         
        </el-menu-item>
        
        <el-menu-item index="/paie/echeanciers">
          <el-icon><List /></el-icon>
          <template #title>Echeanciers</template>
        </el-menu-item>
      </el-sub-menu>
      
      <!-- Paramétrages -->
      <el-sub-menu
        index="parametrages"
        v-if="authStore.isAdmin() || authStore.hasAnyPermission([
          { resource: 'organisation', action: 'write' },
          { resource: 'referentiels', action: 'write' },
          { resource: 'paie', action: 'write' }
        ])"
      >
        <template #title>
          <el-icon><Setting /></el-icon>
          <span>Paramétrages</span>
        </template>
        
        <el-menu-item index="/parametrage/exercices">
          <el-icon><Sort /></el-icon>
          <template #title>Exercice</template>
        </el-menu-item>
        
        <el-menu-item index="/parametrage/periodes">
          <el-icon><Sort /></el-icon>
          <template #title>Périodes</template>
        </el-menu-item>
        
        <el-menu-item index="/parametrage/banques">
          <el-icon><Download /></el-icon>
          <template #title>Banques</template>
        </el-menu-item>
        
        <el-menu-item index="/parametrage/comptes-virement">
          <el-icon><Document /></el-icon>
          <template #title>Comptes Virement</template>
          <el-badge value="New!" class="new-badge" />
        </el-menu-item>
        
        <el-menu-item index="/paie/rubriques">
          <el-icon><List /></el-icon>
          <template #title>Rubrique de paie</template>
        </el-menu-item>
        
        <el-menu-item index="/parametrage/rubriques-speciales">
          <el-icon><Star /></el-icon>
          <template #title>Rubrique speciales</template>
        </el-menu-item>
        
        <el-menu-item index="/parametrage/types-sanctions">
          <el-icon><Warning /></el-icon>
          <template #title>Types de sanctions</template>
        </el-menu-item>
        
        <el-menu-item index="/parametrage/sanctions">
          <el-icon><Document /></el-icon>
          <template #title>Sanctions</template>
        </el-menu-item>
        
        <el-menu-item index="/parametrage/utilisateurs">
          <el-icon><User /></el-icon>
          <template #title>Utilisateurs</template>
        </el-menu-item>
        
        <el-menu-item index="/parametrage/societe">
          <el-icon><OfficeBuilding /></el-icon>
          <template #title>Societe</template>
        </el-menu-item>
      </el-sub-menu>
      
      <!-- Reporting -->
      <el-menu-item index="/reporting">
        <el-icon><PieChart /></el-icon>
        <template #title>Reporting</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import {
  Expand,
  Fold,
  Monitor,
  OfficeBuilding,
  Grid,
  User,
  UserFilled,
  Document,
  Briefcase,
  Clock,
  Warning,
  Calendar,
  Bell,
  Printer,
  Money,
  List,
  EditPen,
  FolderOpened,
  Switch,
  DataBoard,
  CreditCard,
  Setting,
  Sort,
  Download,
  Star,
  PieChart
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

interface Props {
  isCollapsed: boolean
}

interface Emits {
  (e: 'toggle'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const authStore = useAuthStore()

const currentPeriode = computed(() => {
  const date = new Date()
  const month = date.getMonth() + 1
  const year = date.getFullYear()
  return `${month.toString().padStart(2, '0')}/${year}`
})

const toggleSidebar = () => {
  emit('toggle')
}
</script>

<style scoped>
.sidebar {
  height: 100vh;
  background: #304156;
  transition: width 0.3s;
  width: 250px;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;
}

.sidebar :deep(.el-menu--collapse) {
  width: 64px;
}

.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: #263445;
}

.navigation-title {
  padding: 10px 20px;
  background: #1a2332;
  border-bottom: 1px solid #2c3e50;
}

.navigation-title span {
  color: #fff;
  font-size: 12px;
  font-weight: bold;
  display: block;
}

.navigation-title .periode {
  color: #409eff;
  font-size: 10px;
  margin-top: 2px;
}

.sidebar-header h2 {
  color: #fff;
  margin: 0;
  font-size: 18px;
}

.toggle-btn {
  background: none;
  border: none;
  color: #fff;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: background 0.3s;
}

.toggle-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.sidebar-menu {
  border: none;
  background: #304156;
}

.sidebar-menu :deep(.el-menu-item) {
  color: #bfcbd9;
  border-left: 3px solid transparent;
  transition: all 0.3s ease;
  position: relative;
  background: transparent !important;
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background: transparent !important;
  color: #fff !important;
  transform: translateX(3px);
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
}

.sidebar-menu :deep(.el-menu-item:hover::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(180deg, #409eff 0%, #67c23a 100%);
  animation: slideIn 0.3s ease;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: transparent !important;
  color: #409eff !important;
  border-left-color: #409eff;
  text-shadow: 0 0 15px rgba(64, 158, 255, 0.8);
  transform: translateX(2px);
}

.sidebar-menu :deep(.el-sub-menu__title) {
  color: #bfcbd9;
  transition: all 0.3s ease;
  position: relative;
  background: transparent !important;
}

.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background: transparent !important;
  color: #fff !important;
  transform: translateX(3px);
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
}

.sidebar-menu :deep(.el-sub-menu__title:hover::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(180deg, #e6a23c 0%, #f56c6c 100%);
  animation: slideIn 0.3s ease;
}

.sidebar-menu :deep(.el-sub-menu.is-opened .el-sub-menu__title) {
  background: transparent !important;
  color: #fff !important;
}

.sidebar-menu :deep(.el-menu-item.is-active::after) {
  content: '';
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  width: 6px;
  height: 6px;
  background: #409eff;
  border-radius: 50%;
  animation: pulse 2s infinite;
  box-shadow: 0 0 10px rgba(64, 158, 255, 0.8);
}

/* Correction pour les sous-menus items */
.sidebar-menu :deep(.el-menu--popup) {
  background: rgba(48, 65, 86, 0.95) !important;
  border: 1px solid rgba(38, 52, 69, 0.8) !important;
  backdrop-filter: blur(10px);
}

.sidebar-menu :deep(.el-menu--popup .el-menu-item) {
  background: transparent !important;
  color: #bfcbd9 !important;
}

.sidebar-menu :deep(.el-menu--popup .el-menu-item:hover) {
  background: transparent !important;
  color: #fff !important;
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
}

.sidebar-menu :deep(.el-menu--popup .el-menu-item.is-active) {
  background: transparent !important;
  color: #409eff !important;
  text-shadow: 0 0 15px rgba(64, 158, 255, 0.8);
}

/* Correction pour les éléments dans les sous-menus ouverts */
.sidebar-menu :deep(.el-sub-menu .el-menu-item) {
  background: #1a2332 !important;
  color: #bfcbd9 !important;
  border-left: 3px solid transparent;
  transition: all 0.3s ease;
  position: relative;
}

.sidebar-menu :deep(.el-sub-menu .el-menu-item:hover) {
  background: #263445 !important;
  color: #fff !important;
  transform: translateX(3px);
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
}

.sidebar-menu :deep(.el-sub-menu .el-menu-item:hover::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(180deg, #409eff 0%, #67c23a 100%);
  animation: slideIn 0.3s ease;
}

.sidebar-menu :deep(.el-sub-menu .el-menu-item.is-active) {
  background: #263445 !important;
  color: #409eff !important;
  text-shadow: 0 0 15px rgba(64, 158, 255, 0.8);
  transform: translateX(2px);
  border-left-color: #409eff;
}

.sidebar-menu :deep(.el-sub-menu .el-menu-item.is-active::after) {
  content: '';
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  width: 6px;
  height: 6px;
  background: #409eff;
  border-radius: 50%;
  animation: pulse 2s infinite;
  box-shadow: 0 0 10px rgba(64, 158, 255, 0.8);
}

/* Correction pour le conteneur des sous-menus */
.sidebar-menu :deep(.el-sub-menu .el-menu) {
  background: #1a2332 !important;
  border: none !important;
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.3);
}

@keyframes slideIn {
  from {
    transform: scaleY(0);
  }
  to {
    transform: scaleY(1);
  }
}

@keyframes pulse {
  0% {
    opacity: 1;
    transform: translateY(-50%) scale(1);
  }
  50% {
    opacity: 0.7;
    transform: translateY(-50%) scale(1.2);
  }
  100% {
    opacity: 1;
    transform: translateY(-50%) scale(1);
  }
}

.new-badge {
  margin-left: auto;
}

.new-badge :deep(.el-badge__content) {
  background: #f56c6c;
  font-size: 10px;
  padding: 0 4px;
  height: 16px;
  line-height: 16px;
}
</style>
