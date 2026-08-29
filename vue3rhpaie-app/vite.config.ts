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

        target: 'http://192.168.1.7:7200',

        changeOrigin: true,

        secure: false

      },

      '/realms': {

        target: 'http://192.168.1.7:8083',

        changeOrigin: true,

        secure: false

      },

      '/static': {

        target: 'http://192.168.1.7:7200',

        changeOrigin: true,

        secure: false

      },

      '/uploads': {

        target: 'http://192.168.1.7:7200',

        changeOrigin: true,

        secure: false

      }

    }

  }

})

