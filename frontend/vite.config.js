import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  base: '/wision4-seam/app/',
  server: {
    proxy: {
      '/wision4-seam/api': {
        target: 'http://localhost:8180',
        changeOrigin: true,
      },
    },
  },
})
