import { UserDto } from "../domain/UserDto";

export interface AuthService {
  login(username: string, password: string): Promise<UserDto | undefined>;
  isAdmin(): boolean | undefined;
  getUser(): UserDto | undefined;
  logout(): void;
  isLoggedIn(): Promise<UserDto | undefined>;
  withAuth(r?: RequestInit): RequestInit;
}