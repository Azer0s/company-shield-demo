import { useEffect, useState } from 'react'
import { AuthService } from '../../service/auth_service'
import { Strings } from './LoginPageStrings'
import { useNavigate } from 'react-router-dom'
import { Paths } from '../../util/paths'

interface LoginPageProps {
  authService: AuthService
}

function LoginPage({ authService }: LoginPageProps) {
  const navigate = useNavigate()
  const [user, setUser] = useState('')
  const [password, setPassword] = useState('')
  const [loggingIn, setLoggingIn] = useState(false)

  async function login() {
    setLoggingIn(true)

    if (!await authService.login(user, password)) {
      //IMPROVEMENT: Use a better alert system
      alert(Strings.invalidCredentialsMessage)
      setLoggingIn(false)
      return
    }

    navigate(authService.isAdmin() ? Paths.adminDashboardPath : Paths.dashboardPath);
  }

  useEffect(() => {
    authService.isLoggedIn().then(user => {
      if (user) {
        navigate(authService.isAdmin() ? Paths.adminDashboardPath : Paths.dashboardPath);
      } else {
        navigate(Paths.loginPath);
      }
    })
  }, [])

  //IMPROVEMENT: Form validation could be better
  //IMPROVEMENT: Add a loading spinner
  return (
    <>
      <h1>Fast & Reckless Bank</h1>
      <form className="card">
        <input required disabled={loggingIn} type="text" placeholder={Strings.userNameLabel} onChange={e => setUser(e.target.value)} />
        <input required disabled={loggingIn} type="password" placeholder={Strings.passwordLabel} onChange={e => setPassword(e.target.value)} />
        <button disabled={loggingIn} onClick={login}>
          {loggingIn ? Strings.loggingInLabel : Strings.loginLabel}
        </button>
      </form>
    </>
  )
}

export default LoginPage
