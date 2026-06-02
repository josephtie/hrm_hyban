import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const usePayrollStore = defineStore('payroll', () => {
  // État
  const currentPeriod = ref<any>(null)
  const isGenerating = ref(false)

  // Getters
  const hasCurrentPeriod = computed(() => !!currentPeriod.value)

  // Actions
  const setCurrentPeriod = (period: any) => {
    currentPeriod.value = period
  }

  const setGenerating = (value: boolean) => {
    isGenerating.value = value
  }

  return {
    currentPeriod,
    isGenerating,
    hasCurrentPeriod,
    setCurrentPeriod,
    setGenerating
  }
})
