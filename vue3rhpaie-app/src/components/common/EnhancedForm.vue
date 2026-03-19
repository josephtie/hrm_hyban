<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    :label-width="labelWidth"
    :label-position="labelPosition"
    :disabled="disabled"
    :validate-on-rule-change="false"
    class="enhanced-form"
    :class="{ 'form-compact': compact, 'form-loading': loading }"
    @submit.prevent="handleSubmit"
  >
    <!-- Header du formulaire -->
    <div v-if="showHeader" class="form-header">
      <div class="form-header-content">
        <div class="form-title-section">
          <h3 v-if="title" class="form-title">{{ title }}</h3>
          <p v-if="subtitle" class="form-subtitle">{{ subtitle }}</p>
        </div>
        <div v-if="showProgress" class="form-progress">
          <el-progress
            :percentage="progressPercentage"
            :status="progressStatus"
            :stroke-width="4"
          />
        </div>
      </div>
    </div>

    <!-- Contenu du formulaire -->
    <div class="form-content" :class="{ 'has-header': showHeader }">
      <slot>
        <!-- Champs par défaut si aucun slot -->
        <div v-if="defaultFields.length > 0" class="default-fields">
          <div
            v-for="field in defaultFields"
            :key="field.name"
            class="form-field"
          >
            <component
              :is="getFieldComponent(field.type)"
              v-model="formData[field.name]"
              v-bind="getFieldProps(field)"
              :class="{ 'field-required': field.required }"
              @blur="validateField(field.name)"
              @input="handleFieldChange(field.name, $event)"
            />
          </div>
        </div>
      </slot>
    </div>

    <!-- Footer du formulaire -->
    <div v-if="showFooter" class="form-footer">
      <div class="form-footer-content">
        <div class="footer-left">
          <slot name="footer-left">
            <el-button
              v-if="showReset"
              @click="handleReset"
              :disabled="loading"
              class="enhanced-button btn-reset"
            >
              <el-icon><Refresh /></el-icon>
              {{ resetText }}
            </el-button>
          </slot>
        </div>
        <div class="footer-right">
          <slot name="footer-actions">
            <el-button
              v-if="showCancel"
              @click="handleCancel"
              :disabled="loading"
              class="enhanced-button btn-cancel"
            >
              <el-icon><Close /></el-icon>
              {{ cancelText }}
            </el-button>
            <el-button
              v-if="showSubmit"
              @click="handleSubmit"
              :loading="loading"
              :disabled="disabled || !isFormValid"
              type="primary"
              class="enhanced-button btn-submit"
            >
              <el-icon><Check /></el-icon>
              {{ submitText }}
            </el-button>
          </slot>
        </div>
      </div>
    </div>

    <!-- Indicateur de chargement -->
    <div v-if="loading" class="form-loading-overlay">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>{{ loadingText }}</span>
    </div>
  </el-form>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Close, Check, Refresh, Loading } from '@element-plus/icons-vue'

interface FormField {
  name: string
  label: string
  type: 'input' | 'textarea' | 'select' | 'date' | 'number' | 'radio' | 'checkbox'
  required?: boolean
  placeholder?: string
  options?: Array<{ label: string; value: any }>
  rules?: any[]
  props?: Record<string, any>
}

interface Props {
  modelValue: Record<string, any>
  rules?: FormRules
  title?: string
  subtitle?: string
  labelWidth?: string
  labelPosition?: 'left' | 'right' | 'top'
  disabled?: boolean
  loading?: boolean
  loadingText?: string
  showHeader?: boolean
  showFooter?: boolean
  showProgress?: boolean
  showReset?: boolean
  showCancel?: boolean
  showSubmit?: boolean
  resetText?: string
  cancelText?: string
  submitText?: string
  compact?: boolean
  defaultFields?: FormField[]
  validateOnChange?: boolean
}

interface Emits {
  (e: 'update:modelValue', value: Record<string, any>): void
  (e: 'submit', data: Record<string, any>): void
  (e: 'reset'): void
  (e: 'cancel'): void
  (e: 'field-change', field: string, value: any): void
  (e: 'validation-error', errors: Record<string, string[]>): void
}

const props = withDefaults(defineProps<Props>(), {
  labelWidth: '120px',
  labelPosition: 'top',
  loadingText: 'Chargement...',
  showHeader: false,
  showFooter: true,
  showProgress: false,
  showReset: true,
  showCancel: true,
  showSubmit: true,
  resetText: 'Réinitialiser',
  cancelText: 'Annuler',
  submitText: 'Valider',
  compact: false,
  defaultFields: () => [],
  validateOnChange: true
})

const emit = defineEmits<Emits>()

const formRef = ref<FormInstance>()
const formData = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const progressPercentage = ref(0)
const progressStatus = ref<'success' | 'exception' | 'warning' | undefined>()
const fieldErrors = ref<Record<string, string[]>>({})

const isFormValid = computed(() => {
  return Object.keys(fieldErrors.value).length === 0
})

// Méthodes de validation
const validateField = async (fieldName: string) => {
  if (!formRef.value) return false
  
  try {
    await formRef.value.validateField(fieldName)
    delete fieldErrors.value[fieldName]
    return true
  } catch (error) {
    fieldErrors.value[fieldName] = error as string[]
    emit('validation-error', fieldErrors.value)
    return false
  }
}

const validateForm = async () => {
  if (!formRef.value) return false
  
  try {
    const valid = await formRef.value.validate()
    if (valid) {
      fieldErrors.value = {}
    }
    return valid
  } catch (error) {
    fieldErrors.value = error as Record<string, string[]>
    emit('validation-error', fieldErrors.value)
    return false
  }
}

// Gestion des champs
const getFieldComponent = (type: string) => {
  const components = {
    input: 'el-input',
    textarea: 'el-input',
    select: 'el-select',
    date: 'el-date-picker',
    number: 'el-input-number',
    radio: 'el-radio-group',
    checkbox: 'el-checkbox-group'
  }
  return components[type as keyof typeof components] || 'el-input'
}

const getFieldProps = (field: FormField) => {
  const baseProps = {
    placeholder: field.placeholder,
    ...field.props
  }
  
  switch (field.type) {
    case 'textarea':
      return { ...baseProps, type: 'textarea', rows: 3 }
    case 'select':
      return { ...baseProps, clearable: true }
    case 'number':
      return { ...baseProps, controlsPosition: 'right' }
    default:
      return baseProps
  }
}

// Gestionnaires d'événements
const handleSubmit = async () => {
  if (props.loading) return
  
  const isValid = await validateForm()
  if (isValid) {
    emit('submit', formData.value)
  } else {
    ElMessage.error('Veuillez corriger les erreurs dans le formulaire')
  }
}

const handleReset = () => {
  if (formRef.value) {
    formRef.value.resetFields()
    fieldErrors.value = {}
    progressPercentage.value = 0
    emit('reset')
  }
}

const handleCancel = () => {
  emit('cancel')
}

const handleFieldChange = (fieldName: string, value: any) => {
  emit('field-change', fieldName, value)
  
  if (props.validateOnChange) {
    nextTick(() => {
      validateField(fieldName)
    })
  }
  
  updateProgress()
}

// Mise à jour du progrès
const updateProgress = () => {
  if (!props.showProgress) return
  
  const totalFields = props.defaultFields.length
  const validFields = totalFields - Object.keys(fieldErrors.value).length
  
  if (totalFields > 0) {
    progressPercentage.value = Math.round((validFields / totalFields) * 100)
    
    if (progressPercentage.value === 100) {
      progressStatus.value = 'success'
    } else if (progressPercentage.value >= 70) {
      progressStatus.value = undefined
    } else {
      progressStatus.value = 'warning'
    }
  }
}

// Watch pour les changements de données
watch(() => props.modelValue, () => {
  if (props.validateOnChange) {
    nextTick(() => {
      updateProgress()
    })
  }
}, { deep: true })

// Exposer les méthodes pour le parent
defineExpose({
  validateField,
  validateForm,
  resetForm: handleReset,
  clearValidation: () => { fieldErrors.value = {} }
})
</script>

<style lang="scss">
@use '@/styles/design-system.scss' as *;

.enhanced-form {
  position: relative;
  background: var(--bg-primary);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-light);
  border: 1px solid var(--border-light);
  overflow: hidden;
  
  &.form-compact {
    .form-content {
      padding: var(--spacing-md);
    }
    
    .form-footer {
      padding: var(--spacing-md) var(--spacing-lg);
    }
  }
  
  &.form-loading {
    pointer-events: none;
    opacity: 0.7;
  }
}

.form-header {
  background: linear-gradient(135deg, var(--bg-secondary), var(--bg-tertiary));
  padding: var(--spacing-lg) var(--spacing-xl);
  border-bottom: 1px solid var(--border-light);
  
  .form-header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: var(--spacing-md);
  }
  
  .form-title {
    color: var(--text-primary);
    font-size: 20px;
    font-weight: 600;
    margin: 0;
  }
  
  .form-subtitle {
    color: var(--text-secondary);
    font-size: 14px;
    margin: var(--spacing-xs) 0 0 0;
  }
  
  .form-progress {
    min-width: 200px;
  }
}

.form-content {
  padding: var(--spacing-xl);
  
  &.has-header {
    padding-top: var(--spacing-lg);
  }
  
  .default-fields {
    .form-field {
      margin-bottom: var(--spacing-lg);
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

.form-footer {
  background: var(--bg-secondary);
  padding: var(--spacing-lg) var(--spacing-xl);
  border-top: 1px solid var(--border-light);
  
  .form-footer-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: var(--spacing-md);
  }
  
  .footer-right {
    display: flex;
    gap: var(--spacing-md);
  }
}

// Styles pour les champs requis
.field-required {
  .el-form-item__label {
    position: relative;
    
    &::after {
      content: '*';
      color: var(--danger-color);
      margin-left: var(--spacing-xs);
      font-weight: 600;
    }
  }
}

// Animation de validation
.el-form-item {
  transition: all var(--transition-base);
  
  &.is-error {
    .el-input__wrapper,
    .el-textarea__inner,
    .el-select .el-input__wrapper {
      box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2);
      border-color: var(--danger-color);
      animation: shake 0.5s ease;
    }
  }
  
  &.is-success {
    .el-input__wrapper,
    .el-textarea__inner,
    .el-select .el-input__wrapper {
      box-shadow: 0 0 0 2px rgba(103, 194, 58, 0.2);
      border-color: var(--success-color);
    }
  }
}

// Overlay de chargement
.form-loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: var(--spacing-md);
  z-index: 1000;
  
  .el-icon {
    font-size: 32px;
    color: var(--primary-color);
  }
  
  span {
    color: var(--text-regular);
    font-weight: 500;
  }
}

// Animation de secousse pour les erreurs
@keyframes shake {
  0%, 100% { transform: translateX(0); }
  10%, 30%, 50%, 70%, 90% { transform: translateX(-2px); }
  20%, 40%, 60%, 80% { transform: translateX(2px); }
}

// Responsive
@media (max-width: 768px) {
  .enhanced-form {
    .form-header,
    .form-content,
    .form-footer {
      padding-left: var(--spacing-lg);
      padding-right: var(--spacing-lg);
    }
    
    .form-header-content {
      flex-direction: column;
      align-items: flex-start;
      gap: var(--spacing-sm);
    }
    
    .form-footer-content {
      flex-direction: column;
      gap: var(--spacing-md);
      
      .footer-right {
        width: 100%;
        justify-content: stretch;
        
        .el-button {
          flex: 1;
        }
      }
    }
  }
}
</style>
