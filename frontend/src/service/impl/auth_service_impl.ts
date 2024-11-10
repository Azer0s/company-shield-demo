import { UserDto } from "../../domain/UserDto";
import { UserRole } from "../../domain/UserRole";
import { AuthService } from "../auth_service";

const tokenKey = 'token';
const userKey = 'user';

const loginPath = '/auth/login';
const verifyPath = '/auth/verify';

export class AuthServiceImpl implements AuthService {
  withAuth(r?: RequestInit): RequestInit {
    if (!r) {
      return {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem(tokenKey)}`,
        },
      }
    }

    r.headers = {
      ...r.headers,
      'Authorization': `Bearer ${localStorage.getItem(tokenKey)}`,
    }

    return r;
  }

  getUser(): UserDto | undefined {
    const user = localStorage.getItem(userKey);
    if (!user) {
      return undefined;
    }

    return JSON.parse(user) as UserDto;
  }
  
  isAdmin(): boolean | undefined {
    const user = this.getUser();
    if (!user) {
      return undefined;
    }

    return user.roles.includes(UserRole.ADMIN);
  }

  logout(): void {
    localStorage.removeItem(tokenKey);
    localStorage.removeItem(userKey);
  }

  async login(username: string, password: string): Promise<UserDto | undefined> {
    const response = await fetch(loginPath, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      return undefined;
    }

    const data = await response.json();
    const user = data.forUser as UserDto;

    localStorage.setItem(tokenKey, data.token);
    localStorage.setItem(userKey, JSON.stringify(user));

    return user;
  }

  async isLoggedIn(): Promise<UserDto | undefined> {
    const token = localStorage.getItem(tokenKey);
    if (!token) {
      return undefined;
    }

    const forUser = localStorage.getItem(userKey);
    if (!forUser) {
      return undefined;
    }

    const response = await fetch(verifyPath, this.withAuth());

    if (!response.ok) {
      return undefined;
    }

    return JSON.parse(forUser) as UserDto;
  }
}