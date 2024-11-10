import { useNavigate } from "react-router-dom";
import { Strings } from "./LogoutButtonStrings";
import { AuthService } from "../../service/auth_service";
import { Paths } from "../../util/paths";

interface LogoutButtonProps {
  authService: AuthService
}

function LogoutButton({ authService }: LogoutButtonProps) {
  const navigate = useNavigate()

  function logout() {
    authService.logout()
    navigate(Paths.loginPath)
  }

  return (
    <button onClick={logout}>
      {Strings.logoutLabel}
    </button>
  );
}

export default LogoutButton;