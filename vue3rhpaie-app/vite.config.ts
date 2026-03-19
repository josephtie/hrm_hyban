import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        api: 'modern-compiler' // ✅ Force l'utilisation de la nouvelle API Sass
      }
    }
  },
  server: {
    port: 7153,
    open: true,
    proxy: {
      '/api': {
        target: 'http://192.168.1.5:7200/api',
        changeOrigin: true,
        secure: false
      },
      '/keycloak': {
        target: 'http://192.168.1.5:8080',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/keycloak/, '')
      }
    }
  }
})
