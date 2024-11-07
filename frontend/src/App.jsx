import { useState } from 'react'
import './App.css'

function App({ authService }) {
  const [user, setUser] = useState('')
  const [password, setPassword] = useState('')
  const [loggingIn, setLoggingIn] = useState(false)

  async function login() {
    setLoggingIn(true)

    if (!await authService.login(user, password)) {
      alert('Invalid username or password')
      setLoggingIn(false)
      return
    }
  }

  return (
    <>
      <h1>Fast & Reckless Bank</h1>
      <div className="card">
        <input disabled={loggingIn} type="text" placeholder="Username" onChange={e => setUser(e.target.value)} />
        <input disabled={loggingIn} type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} />
        <button disabled={loggingIn} onClick={login}>
          {loggingIn ? 'Logging in...' : 'Login'}
        </button>
      </div>
    </>
  )
}

export default App
