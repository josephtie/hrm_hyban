<template>
  <div class="roles-permissions-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Rôles & Permissions</span>
          <el-button type="primary" @click="loadData" :loading="loading">
            <el-icon><Refresh /></el-icon>
            Actualiser
          </el-button>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="role-card" shadow="hover">
            <template #header>
              <span>Sélection du rôle</span>
            </template>
            <el-select
              v-model="selectedRole"
              placeholder="Sélectionner un rôle"
              @change="onRoleChange"
              style="width: 100%"
            >
              <el-option
                v-for="role in roles"
                :key="role.name"
                :label="role.name"
                :value="role.name"
              />
            </el-select>
          </el-card>
        </el-col>

        <el-col :span="16">
          <el-card class="permissions-card" shadow="hover">
            <template #header>
              <div class="permissions-header">
                <span>Permissions pour : {{ selectedRole || '—' }}</span>
                <el-button
                  type="success"
                  @click="savePermissions"
                  :loading="saving"
                  :disabled="!selectedRole"
                >
                  <el-icon><Check /></el-icon>
                  Enregistrer
                </el-button>
              </div>
            </template>

            <div v-if="!selectedRole" class="no-role-selected">
              <el-empty description="Sélectionnez un rôle pour gérer ses permissions" />
            </div>

            <div v-else>
              <div
                v-for="group in permissionGroups"
                :key="group.name"
                class="permission-group"
              >
                <h4>{{ group.label }}</h4>
                <el-checkbox-group v-model="selectedPermissions">
                  <el-checkbox
                    v-for="perm in group.permissions"
                    :key="perm.code"
                    :value="perm.code"
                    :label="perm.code"
                  >
                    <span class="perm-code">{{ perm.code }}</span>
                    <span class="perm-desc">{{ perm.description }}</span>
                  </el-checkbox>
                </el-checkbox-group>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Check } from '@element-plus/icons-vue'
import permissionService, { type Permission, type RoleWithPermissions } from '@/services/permission.service'

const loading = ref(false)
const saving = ref(false)
const selectedRole = ref('')
const allPermissions = ref<Permission[]>([])
const roles = ref<RoleWithPermissions[]>([])
const selectedPermissions = ref<string[]>([])

const PERMISSION_GROUPS = [
  { name: 'employee', label: 'Personnel', prefix: 'EMPLOYEE_' },
  { name: 'contract', label: 'Contrats', prefix: 'CONTRACT_' },
  { name: 'payroll', label: 'Paie', prefix: 'PAYROLL_' },
  { name: 'leave', label: 'Congés', prefix: 'LEAVE_' },
  { name: 'absence', label: 'Absences', prefix: 'ABSENCE_' },
  { name: 'report', label: 'Rapports', prefix: 'REPORT_' },
  { name: 'parameter', label: 'Paramétrage', prefix: 'PARAMETER_' },
  { name: 'user', label: 'Utilisateurs', prefix: 'USER_' },
  { name: 'role', label: 'Rôles', prefix: 'ROLE_' },
  { name: 'permission', label: 'Permissions', prefix: 'PERMISSION_' },
  { name: 'pointage', label: 'Pointage', prefix: 'POINTAGE_' },
  { name: 'sanction', label: 'Sanctions', prefix: 'SANCTION_' },
  { name: 'formation', label: 'Formation', prefix: 'FORMATION_' },
  { name: 'carriere', label: 'Carrière', prefix: 'CARRIERE_' },
  { name: 'dashboard', label: 'Tableau de bord', prefix: 'DASHBOARD_' }
]

const permissionGroups = computed(() => {
  return PERMISSION_GROUPS.map(group => ({
    ...group,
    permissions: allPermissions.value.filter(p => p.code.startsWith(group.prefix))
  })).filter(group => group.permissions.length > 0)
})

const loadData = async () => {
  loading.value = true
  try {
    const [perms, rolesData] = await Promise.all([
      permissionService.getAllPermissions(),
      permissionService.getRolesWithPermissions()
    ])
    allPermissions.value = perms
    roles.value = rolesData
  } catch (error) {
    console.error('Erreur chargement données:', error)
    ElMessage.error('Erreur lors du chargement des données')
  } finally {
    loading.value = false
  }
}

const onRoleChange = async () => {
  if (!selectedRole.value) {
    selectedPermissions.value = []
    return
  }
  try {
    const data = await permissionService.getPermissionsByRole(selectedRole.value)
    selectedPermissions.value = data.permissions || []
  } catch (error) {
    console.error('Erreur chargement permissions rôle:', error)
    selectedPermissions.value = []
  }
}

const savePermissions = async () => {
  if (!selectedRole.value) return
  saving.value = true
  try {
    await permissionService.updateRolePermissions(selectedRole.value, selectedPermissions.value)
    ElMessage.success('Permissions mises à jour avec succès')
    await loadData()
    await onRoleChange()
  } catch (error) {
    console.error('Erreur sauvegarde permissions:', error)
    ElMessage.error('Erreur lors de la sauvegarde')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.roles-permissions-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.role-card {
  margin-bottom: 20px;
}

.permissions-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.permission-group {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.permission-group:last-child {
  border-bottom: none;
}

.permission-group h4 {
  margin-bottom: 10px;
  color: #409eff;
}

.perm-code {
  font-weight: bold;
  margin-right: 8px;
}

.perm-desc {
  color: #909399;
  font-size: 0.9em;
}

.no-role-selected {
  padding: 40px 0;
}

.el-checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
</style>
