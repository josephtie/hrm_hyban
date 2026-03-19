<template>
  <div class="personnel-layout">
    <!-- Router-view pour les composants enfants sans entête -->
    <router-view />
  </div>
</template>

<script setup lang="ts">
// Layout simple pour le module personnel sans entête spécifique
</script>

<style scoped>
.personnel-layout {
  width: 100%;
  height: 100%;
  min-height: 100vh;
}
</style>
