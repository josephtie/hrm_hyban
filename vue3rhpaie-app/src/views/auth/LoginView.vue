<template>
  <div class="login-container">
    <!-- Panel gauche avec image parlante -->
    <div class="left-panel">
      <div class="image-overlay" :style="backgroundImageStyle">
        <div class="content">
          <div class="logo-section">
            <h1 class="brand-title">RHPAIE</h1>
            <p class="brand-subtitle">Gestion RH & Paie</p>
          </div>
          
          <div class="features">
            <div class="feature-item">
              <el-icon class="feature-icon"><User /></el-icon>
              <div class="feature-text">
                <h3>Gestion du Personnel</h3>
                <p>Administration complète des ressources humaines</p>
              </div>
            </div>
            
            <div class="feature-item">
              <el-icon class="feature-icon"><Money /></el-icon>
              <div class="feature-text">
                <h3>Paie Automatisée</h3>
                <p>Calcul et génération des bulletins de paie</p>
              </div>
            </div>
            
            <div class="feature-item">
              <el-icon class="feature-icon"><DataAnalysis /></el-icon>
              <div class="feature-text">
                <h3>Reporting Avancé</h3>
                <p>Tableaux de bord et analyses détaillées</p>
              </div>
            </div>
          </div>
          
          <div class="stats">
            <div class="stat-item">
              <div class="stat-number">500+</div>
              <div class="stat-label">Entreprises</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">50K+</div>
              <div class="stat-label">Employés</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">99.9%</div>
              <div class="stat-label">Satisfaction</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Panel droit pour la connexion -->
    <div class="right-panel">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <h1>Connexion</h1>
            <p>Accédez à votre espace de travail</p>
          </div>
        </div>
        
        <div class="login-body">
          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            label-position="top"
            size="large"
          >
            <el-form-item label="Nom d'utilisateur" prop="username">
              <el-input
                v-model="loginForm.username"
                placeholder="Entrez votre nom d'utilisateur"
                :prefix-icon="User"
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            
            <el-form-item label="Mot de passe" prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="Entrez votre mot de passe"
                :prefix-icon="Lock"
                show-password
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                style="width: 100%"
                :loading="authStore.loading"
                @click="handleLogin"
              >
                Se connecter
              </el-button>
            </el-form-item>
          </el-form>
          
          <div v-if="errorMessage" class="error-message">
            <el-alert
              :title="errorMessage"
              type="error"
              :closable="false"
              show-icon
            />
          </div>
        </div>
        
        <div class="login-footer">
          <p>© 2024 RHPAIE - Tous droits réservés</p>
          <div class="demo-info">
            <el-tag type="success" size="large" effect="dark">
              <strong>Identifiants de démo :</strong> admin / admin123
            </el-tag>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Money, DataAnalysis } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import loginBgImage from '@/assets/images/login-bg.jpg'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const loginFormRef = ref()
const errorMessage = ref('')

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: 'Veuillez entrer votre nom d\'utilisateur', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Veuillez entrer votre mot de passe', trigger: 'blur' },
    { min: 6, message: 'Le mot de passe doit contenir au moins 6 caractères', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    
    errorMessage.value = ''
    
    const success = await authStore.login({
      username: loginForm.username,
      password: loginForm.password
    })
    
    console.log('Résultat login dans LoginView:', success, 'Utilisateur:', authStore.user) // Debug
    
    if (success) {
      ElMessage.success('Connexion réussie')
      
      // Debug : Vérifier l'état après connexion
      console.log('État après connexion - Token:', authStore.token, 'User:', authStore.user, 'IsAuthenticated:', authStore.isAuthenticated)
      
      // Rediriger vers la page demandée ou le dashboard
      const redirect = route.query.redirect as string
      console.log('Redirection vers:', redirect || '/') // Debug
      
      // Forcer la redirection après un court délai pour la réactivité
      setTimeout(() => {
        router.push(redirect || '/')
      }, 100)
    } else {
      errorMessage.value = 'Nom d\'utilisateur ou mot de passe incorrect'
    }
  } catch (error) {
    console.error('Login error:', error)
    errorMessage.value = 'Une erreur est survenue lors de la connexion'
  }
}

// Style dynamique pour l'image de fond
const backgroundImageStyle = {
  backgroundImage: `url(${loginBgImage})`
}
</script>

<style lang="scss" scoped>
.login-container {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.left-panel {
  flex: 1.2;
  background: #ffffff;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  
  @media (max-width: 768px) {
    display: none;
  }
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-size: cover;
  background-position: center;
  opacity: 0.6;
  filter: brightness(1) contrast(1.1);
}

.content {
  position: relative;
  z-index: 2;
  color: #2c3e50;
  text-align: center;
  padding: 40px;
  max-width: 600px;
}

.logo-section {
  margin-bottom: 60px;
  
  .brand-title {
    font-size: 3.5rem;
    font-weight: 800;
    margin: 0 0 10px 0;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
  
  .brand-subtitle {
    font-size: 1.2rem;
    margin: 0;
    opacity: 0.8;
    font-weight: 500;
    color: #5a6c7d;
  }
}

.features {
  margin-bottom: 60px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
  text-align: left;
  
  .feature-icon {
    font-size: 2.5rem;
    opacity: 0.9;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    padding: 15px;
    border-radius: 15px;
    background-color: rgba(102, 126, 234, 0.1);
  }
  
  .feature-text {
    flex: 1;
    
    h3 {
      font-size: 1.2rem;
      margin: 0 0 5px 0;
      font-weight: 600;
      color: #2c3e50;
    }
    
    p {
      margin: 0;
      opacity: 0.7;
      font-size: 0.9rem;
      line-height: 1.4;
      color: #5a6c7d;
    }
  }
}

.stats {
  display: flex;
  justify-content: space-around;
  gap: 30px;
  
  .stat-item {
    text-align: center;
    
    .stat-number {
      font-size: 2.5rem;
      font-weight: 800;
      margin-bottom: 5px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
    
    .stat-label {
      font-size: 0.9rem;
      opacity: 0.8;
      font-weight: 500;
      color: #5a6c7d;
    }
  }
}

.right-panel {
  flex: 0.8;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  
  @media (max-width: 768px) {
    flex: 1;
    padding: 20px;
  }
}

.login-card {
  width: 100%;
  max-width: 450px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.login-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 30px;
  text-align: center;
  
  .logo {
    h1 {
      color: white;
      font-size: 2rem;
      font-weight: 700;
      margin: 0 0 10px 0;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    }
    
    p {
      color: rgba(255, 255, 255, 0.9);
      font-size: 1rem;
      margin: 0;
      font-weight: 300;
    }
  }
}

.login-body {
  padding: 40px 30px;
  
  :deep(.el-form-item__label) {
    font-weight: 500;
    color: #303133;
  }
  
  :deep(.el-input__wrapper) {
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border: 1px solid #e4e7ed;
    transition: all 0.3s ease;
    
    &:hover {
      border-color: #667eea;
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
    }
    
    &.is-focus {
      border-color: #667eea;
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
    }
  }
  
  :deep(.el-button--primary) {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    border-radius: 10px;
    font-weight: 600;
    padding: 12px 20px;
    font-size: 1rem;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
    }
    
    &:active {
      transform: translateY(0);
    }
  }
}

.error-message {
  margin-top: 20px;
  
  :deep(.el-alert) {
    border-radius: 10px;
  }
}

.login-footer {
  background: #f8f9fa;
  padding: 20px 30px;
  text-align: center;
  border-top: 1px solid #e9ecef;
  
  p {
    color: #6c757d;
    font-size: 0.875rem;
    margin: 0 0 10px 0;
  }
}

.demo-info {
  margin-top: 15px;
  
  :deep(.el-tag) {
    font-weight: 600;
    border-radius: 8px;
    padding: 8px 16px;
    font-size: 0.9rem;
    box-shadow: 0 2px 8px rgba(82, 196, 26, 0.3);
  }
}

// Responsive design
@media (max-width: 1024px) {
  .left-panel {
    flex: 1;
  }
  
  .right-panel {
    flex: 1;
  }
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }
  
  .right-panel {
    min-height: 100vh;
  }
  
  .login-card {
    max-width: 100%;
  }
  
  .stats {
    flex-direction: column;
    gap: 20px;
    
    .stat-item {
      .stat-number {
        font-size: 2rem;
      }
    }
  }
  
  .features {
    .feature-item {
      flex-direction: column;
      text-align: center;
      gap: 15px;
      
      .feature-icon {
        font-size: 2rem;
        padding: 12px;
      }
    }
  }
}

// Animations
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-card {
  animation: fadeInUp 0.6s ease-out;
}

.feature-item {
  animation: fadeInUp 0.6s ease-out;
  animation-fill-mode: both;
  
  &:nth-child(1) { animation-delay: 0.1s; }
  &:nth-child(2) { animation-delay: 0.2s; }
  &:nth-child(3) { animation-delay: 0.3s; }
}

.stat-item {
  animation: fadeInUp 0.6s ease-out;
  animation-fill-mode: both;
  
  &:nth-child(1) { animation-delay: 0.4s; }
  &:nth-child(2) { animation-delay: 0.5s; }
  &:nth-child(3) { animation-delay: 0.6s; }
}
</style>
