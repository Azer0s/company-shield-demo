import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { AuthService } from './service/auth_service.js'
import './index.css'
import App from './App.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App authService={new AuthService("http://localhost:8080")} />
  </StrictMode>,
)
