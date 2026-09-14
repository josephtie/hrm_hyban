import { ref } from 'vue'

const visible = ref(false)

export function usePasswordDialog() {
  const open = () => {
    visible.value = true
  }

  const close = () => {
    visible.value = false
  }

  return {
    visible,
    open,
    close,
  }
}
