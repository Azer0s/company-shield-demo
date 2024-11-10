import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // IMPROVEMENT: This should probably be a cors policy or something
      '/auth': {
        target: 'http://localhost:8080',
      },
      '/api': {
        target: 'http://localhost:8080',
      },
    }
  }
})
