import type { Directive, DirectiveBinding } from 'vue'
import { useAuthStore } from '@/stores/auth'

/**
 * v-permission directive — hides the element if the user lacks the required permission code(s).
 *
 * Usage:
 *   v-permission="'EMPLOYEE_CREATE'"
 *   v-permission="['EMPLOYEE_CREATE', 'EMPLOYEE_UPDATE']"   (any of)
 */
export const permissionDirective: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding)
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding)
  },
}

function checkPermission(el: HTMLElement, binding: DirectiveBinding) {
  const authStore = useAuthStore()
  const value = binding.value

  let codes: string[]
  if (typeof value === 'string') {
    codes = [value]
  } else if (Array.isArray(value)) {
    codes = value
  } else {
    return
  }

  const hasAccess = authStore.hasAnyPermissionCode(codes)

  if (!hasAccess) {
    el.style.display = 'none'
  } else {
    el.style.display = ''
  }
}

export default permissionDirective
