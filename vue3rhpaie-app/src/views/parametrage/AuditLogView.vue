<template>
  <div class="audit-log-view">
    <div class="page-header">
      <h1>Journal d'Audit</h1>
      <p>Historique des actions d'administration et de sécurité</p>
    </div>

    <div class="main-panel">
      <div class="panel-header">
        <h3>Événements récents</h3>
        <div class="panel-controls">
          <el-button @click="loadAuditLogs" circle>
            <el-icon><Refresh /></el-icon>
          </el-button>
        </div>
      </div>

      <div class="toolbar">
        <el-select v-model="filterAction" placeholder="Action" style="width: 220px" clearable>
          <el-option label="Assignation de rôles" value="ASSIGN_USER_ROLES" />
          <el-option label="Modification permissions rôle" value="UPDATE_ROLE_PERMISSIONS" />
        </el-select>
      </div>

      <div class="table-container">
        <el-table :data="filteredLogs" style="width: 100%" v-loading="loading">
          <el-table-column prop="timestamp" label="Date/Heure" width="180" sortable>
            <template #default="{ row }">
              {{ formatDateTime(row.timestamp) }}
            </template>
          </el-table-column>

          <el-table-column prop="action" label="Action" width="220" sortable>
            <template #default="{ row }">
              <el-tag :type="getActionColor(row.action)" size="small">
                {{ row.action }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="entityType" label="Type" width="100" sortable />

          <el-table-column prop="entityId" label="Entité" width="150" />

          <el-table-column prop="performedBy" label="Utilisateur" width="150" sortable />

          <el-table-column prop="details" label="Détails" min-width="400">
            <template #default="{ row }">
              <span style="font-size: 13px; color: #606266;">{{ row.details }}</span>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next, total"
            @current-change="onPageChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import { api } from '@/services/api'

interface AuditLog {
  id: number
  action: string
  entityType: string
  entityId: string
  performedBy: string
  details: string
  timestamp: string
}

const loading = ref(false)
const logs = ref<AuditLog[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const filterAction = ref('')

const filteredLogs = computed(() => {
  if (!filterAction.value) return logs.value
  return logs.value.filter(log => log.action === filterAction.value)
})

const loadAuditLogs = async () => {
  try {
    loading.value = true
    const response = await api.get('/audit', {
      params: {
        page: currentPage.value - 1,
        size: pageSize.value
      }
    })
    logs.value = response.data.content || []
    total.value = response.data.totalElements || 0
  } catch (error) {
    ElMessage.error('Erreur lors du chargement du journal d\'audit')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const onPageChange = (page: number) => {
  currentPage.value = page
  loadAuditLogs()
}

const formatDateTime = (dateString: string) => {
  if (!dateString) return 'N/A'
  return new Date(dateString).toLocaleString('fr-FR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

const getActionColor = (action: string) => {
  const colors: Record<string, string> = {
    'ASSIGN_USER_ROLES': 'warning',
    'UPDATE_ROLE_PERMISSIONS': 'primary'
  }
  return colors[action] || 'info'
}

onMounted(() => {
  loadAuditLogs()
})
</script>

<style scoped>
.audit-log-view {
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

.main-panel {
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

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.table-container {
  flex: 1;
  overflow: hidden;
  padding: 20px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
