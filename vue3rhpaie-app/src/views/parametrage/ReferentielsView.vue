<template>
  <div class="referentiels-view">
    <div class="page-header">
      <h1>Référentiels RH</h1>
      <p>Gestion des référentiels et données de base</p>
    </div>

    <el-row :gutter="24">
      <el-col :span="8">
        <el-card class="ref-card">
          <template #header>
            <div class="card-header">
              <span>Postes et Fonctions</span>
              <el-button type="primary" size="small">Gérer</el-button>
            </div>
          </template>
          <div class="card-content">
            <p>Configuration des postes, fonctions et niveaux hiérarchiques</p>
            <el-progress :percentage="75" status="success" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="ref-card">
          <template #header>
            <div class="card-header">
              <span>Compétences</span>
              <el-button type="primary" size="small">Gérer</el-button>
            </div>
          </template>
          <div class="card-content">
            <p>Définition des compétences et qualifications requises</p>
            <el-progress :percentage="60" status="warning" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="ref-card">
          <template #header>
            <div class="card-header">
              <span>Formations</span>
              <el-button type="primary" size="small">Gérer</el-button>
            </div>
          </template>
          <div class="card-content">
            <p>Catalogue des formations et certifications</p>
            <el-progress :percentage="45" status="exception" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" style="margin-top: 24px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>Référentiels récents</span>
          </template>
          <el-table :data="recentReferences" style="width: 100%">
            <el-table-column prop="name" label="Nom" />
            <el-table-column prop="type" label="Type" />
            <el-table-column prop="status" label="Statut">
              <template #default="scope">
                <el-tag :type="scope.row.status === 'Actif' ? 'success' : 'warning'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <span>Statistiques</span>
          </template>
          <div class="stats-grid">
            <div class="stat-item">
              <div class="stat-number">156</div>
              <div class="stat-label">Postes définis</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">89</div>
              <div class="stat-label">Compétences répertoriées</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">45</div>
              <div class="stat-label">Formations disponibles</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">12</div>
              <div class="stat-label">Niveaux hiérarchiques</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface Reference {
  name: string
  type: string
  status: string
}

const recentReferences = ref<Reference[]>([
  { name: 'Développeur Senior', type: 'Poste', status: 'Actif' },
  { name: 'Gestion de projet', type: 'Compétence', status: 'Actif' },
  { name: 'Certification PMP', type: 'Formation', status: 'Inactif' },
  { name: 'Chef de projet', type: 'Poste', status: 'Actif' },
])
</script>

<style scoped>
.referentiels-view {
  padding: 24px;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
}

.ref-card {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  text-align: center;
}

.card-content p {
  margin-bottom: 16px;
  color: #606266;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>
