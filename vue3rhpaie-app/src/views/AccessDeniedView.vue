<template>
  <div class="access-denied">
    <el-result
      icon="warning"
      title="Accès refusé"
      sub-title="Vous n'avez pas les permissions nécessaires pour accéder à cette page."
    >
      <template #extra>
        <el-button type="primary" @click="goHome">Retour au tableau de bord</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push({ name: 'dashboard' })
}
</script>

<style scoped>
.access-denied {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: #f5f7fa;
}
</style>
