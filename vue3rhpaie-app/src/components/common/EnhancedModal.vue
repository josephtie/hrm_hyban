<template>
  <el-dialog
    v-model="visible"
    :title="title"
    :width="width"
    :before-close="handleClose"
    :close-on-click-modal="closeOnClickModal"
    :close-on-press-escape="closeOnPressEscape"
    :show-close="showClose"
    :draggable="draggable"
    class="enhanced-modal"
    :class="{ 'modal-fullscreen': fullscreen }"
  >
    <!-- Header personnalisé -->
    <template #header="{ close }">
      <div class="modal-header">
        <div class="modal-header-content">
          <div class="modal-title-section">
            <h3 class="modal-title">
              <el-icon v-if="icon" :class="`icon-${type}`">
                <component :is="icon" />
              </el-icon>
              {{ title }}
            </h3>
            <p v-if="subtitle" class="modal-subtitle">{{ subtitle }}</p>
          </div>
          <div class="modal-header-actions">
            <el-button
              v-if="showFullscreen"
              @click="toggleFullscreen"
              circle
              size="small"
              class="header-action-btn"
            >
              <el-icon>
                <FullScreen v-if="!fullscreen" />
                <Aim v-else />
              </el-icon>
            </el-button>
            <el-button
              v-if="showRefresh"
              @click="handleRefresh"
              circle
              size="small"
              class="header-action-btn"
            >
              <el-icon><Refresh /></el-icon>
            </el-button>
            <el-button
              v-if="showClose"
              @click="close"
              circle
              size="small"
              class="header-action-btn close-btn"
            >
              <el-icon><Close /></el-icon>
            </el-button>
          </div>
        </div>
      </div>
    </template>

    <!-- Body avec alertes intégrées -->
    <div class="modal-body">
      <!-- Alertes -->
      <div v-if="alerts.length > 0" class="modal-alerts">
        <div
          v-for="(alert, index) in alerts"
          :key="index"
          :class="['alert', `alert-${alert.type}`]"
        >
          <el-icon>
            <SuccessFilled v-if="alert.type === 'success'" />
            <WarningFilled v-else-if="alert.type === 'warning'" />
            <CircleCloseFilled v-else-if="alert.type === 'danger'" />
            <InfoFilled v-else />
          </el-icon>
          <div class="alert-content">
            <div v-if="alert.title" class="alert-title">{{ alert.title }}</div>
            <div class="alert-message">{{ alert.message }}</div>
          </div>
          <el-button
            v-if="alert.closable"
            @click="removeAlert(index)"
            circle
            size="small"
            class="alert-close"
          >
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
      </div>

      <!-- Contenu principal -->
      <div class="modal-content" :class="{ 'has-alerts': alerts.length > 0 }">
        <slot>
          <div class="default-content">
            <el-empty description="Aucun contenu à afficher" />
          </div>
        </slot>
      </div>
    </div>

    <!-- Footer personnalisé -->
    <template #footer>
      <div class="modal-footer">
        <div class="footer-left">
          <slot name="footer-left">
            <span v-if="showInfo" class="footer-info">
              <el-icon><InfoFilled /></el-icon>
              {{ infoText }}
            </span>
          </slot>
        </div>
        <div class="footer-right">
          <slot name="footer-actions">
            <el-button
              v-if="showCancelButton"
              @click="handleCancel"
              :disabled="loading"
              class="enhanced-button btn-cancel"
            >
              <el-icon><Close /></el-icon>
              {{ cancelText }}
            </el-button>
            <el-button
              v-if="showConfirmButton"
              @click="handleConfirm"
              :loading="loading"
              :disabled="disabled"
              type="primary"
              class="enhanced-button btn-confirm"
            >
              <el-icon><Check /></el-icon>
              {{ confirmText }}
            </el-button>
          </slot>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Close, Refresh, FullScreen, Aim, Check, InfoFilled,
  SuccessFilled, WarningFilled, CircleCloseFilled
} from '@element-plus/icons-vue'

interface Alert {
  type: 'success' | 'warning' | 'danger' | 'info'
  title?: string
  message: string
  closable?: boolean
}

interface Props {
  modelValue: boolean
  title: string
  subtitle?: string
  width?: string
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info'
  icon?: any
  alerts?: Alert[]
  showClose?: boolean
  showFullscreen?: boolean
  showRefresh?: boolean
  showCancelButton?: boolean
  showConfirmButton?: boolean
  showInfo?: boolean
  infoText?: string
  cancelText?: string
  confirmText?: string
  closeOnClickModal?: boolean
  closeOnPressEscape?: boolean
  draggable?: boolean
  loading?: boolean
  disabled?: boolean
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'close'): void
  (e: 'cancel'): void
  (e: 'confirm'): void
  (e: 'refresh'): void
}

const props = withDefaults(defineProps<Props>(), {
  width: '600px',
  type: 'primary',
  alerts: () => [],
  showClose: true,
  showFullscreen: false,
  showRefresh: false,
  showCancelButton: true,
  showConfirmButton: true,
  showInfo: false,
  infoText: '',
  cancelText: 'Annuler',
  confirmText: 'Confirmer',
  closeOnClickModal: false,
  closeOnPressEscape: true,
  draggable: true,
  loading: false,
  disabled: false
})

const emit = defineEmits<Emits>()

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const fullscreen = ref(false)
const alerts = ref<Alert[]>([...props.alerts])

// Watch pour les alerts externes
watch(() => props.alerts, (newAlerts) => {
  alerts.value = [...newAlerts]
}, { deep: true })

const toggleFullscreen = () => {
  fullscreen.value = !fullscreen.value
}

const handleClose = () => {
  if (props.loading) return
  
  emit('close')
  visible.value = false
}

const handleCancel = () => {
  if (props.loading) return
  
  emit('cancel')
  visible.value = false
}

const handleConfirm = () => {
  if (props.loading || props.disabled) return
  
  emit('confirm')
  // Ne pas fermer automatiquement - laisser le parent gérer la fermeture
}

const handleRefresh = () => {
  emit('refresh')
}

const removeAlert = (index: number) => {
  alerts.value.splice(index, 1)
}

// Méthodes utilitaires
const addAlert = (alert: Alert) => {
  alerts.value.push(alert)
}

const clearAlerts = () => {
  alerts.value = []
}

const showSuccess = (message: string, title?: string) => {
  addAlert({ type: 'success', message, title, closable: true })
}

const showWarning = (message: string, title?: string) => {
  addAlert({ type: 'warning', message, title, closable: true })
}

const showError = (message: string, title?: string) => {
  addAlert({ type: 'danger', message, title, closable: true })
}

const showInfo = (message: string, title?: string) => {
  addAlert({ type: 'info', message, title, closable: true })
}

// Exposer les méthodes pour le parent
defineExpose({
  addAlert,
  clearAlerts,
  showSuccess,
  showWarning,
  showError,
  showInfo
})
</script>

<style lang="scss">
@use '@/styles/design-system.scss' as *;

.enhanced-modal {
  .el-dialog {
    border-radius: var(--border-radius-lg);
    overflow: hidden;
    box-shadow: var(--shadow-dark);
    
    &.modal-fullscreen {
      width: 95vw !important;
      height: 95vh !important;
      margin: 2.5vh auto !important;
      
      .el-dialog__body {
        max-height: calc(95vh - 120px);
      }
    }
  }
  
  .el-dialog__header {
    padding: 0;
    margin: 0;
  }
  
  .el-dialog__body {
    padding: 0;
    max-height: 70vh;
    overflow-y: auto;
  }
  
  .el-dialog__footer {
    padding: 0;
    margin: 0;
  }
  
  .modal-footer {
    padding: var(--spacing-lg);
    border-top: 1px solid var(--border-light);
    background: var(--bg-tertiary);
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: var(--spacing-md);
  }
}

.modal-header {
  .modal-header-content {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: var(--spacing-md);
  }
  
  .modal-title-section {
    flex: 1;
  }
  
  .modal-header-actions {
    display: flex;
    gap: var(--spacing-xs);
    
    .header-action-btn {
      background: rgba(255, 255, 255, 0.1);
      border: 1px solid rgba(255, 255, 255, 0.2);
      color: white;
      
      &:hover {
        background: rgba(255, 255, 255, 0.2);
        border-color: rgba(255, 255, 255, 0.3);
      }
      
      &.close-btn:hover {
        background: rgba(245, 108, 108, 0.8);
        border-color: var(--danger-color);
      }
    }
  }
}

.modal-alerts {
  margin-bottom: var(--spacing-lg);
  
  .alert {
    animation: slideInRight 0.3s ease;
    
    .alert-content {
      flex: 1;
    }
    
    .alert-title {
      font-weight: 600;
      margin-bottom: var(--spacing-xs);
    }
    
    .alert-close {
      opacity: 0.6;
      
      &:hover {
        opacity: 1;
      }
    }
  }
}

.modal-content {
  &.has-alerts {
    animation: fadeInUp 0.3s ease;
  }
}

.modal-footer {
  .footer-left {
    flex: 1;
    
    .footer-info {
      display: flex;
      align-items: center;
      gap: var(--spacing-xs);
      color: var(--text-secondary);
      font-size: 14px;
    }
  }
  
  .footer-left {
    display: flex;
    align-items: center;
    gap: var(--spacing-xs);
    color: var(--text-secondary);
    font-size: 14px;
  }
  
  .footer-right {
    display: flex;
    gap: var(--spacing-md);
  }
}

// Animation d'ouverture
.enhanced-modal {
  .el-dialog {
    animation: fadeInUp 0.3s ease;
  }
}

// Responsive
@media (max-width: 768px) {
  .enhanced-modal .el-dialog {
    width: 95vw !important;
    margin: 2.5vh auto !important;
    
    .modal-header-content {
      flex-direction: column;
      gap: var(--spacing-sm);
    }
    
    .modal-header-actions {
      align-self: flex-end;
    }
  }
}
</style>
