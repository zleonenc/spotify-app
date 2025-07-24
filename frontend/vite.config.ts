import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        port: 9090,
        proxy: {
            '/api': {
                target: 'http://localhost:8080', // Adjust the target to your backend server
                changeOrigin: true,
                secure: false,
                rewrite: (path) => path.replace(/^\//, '')
            }
        }
    },
})
