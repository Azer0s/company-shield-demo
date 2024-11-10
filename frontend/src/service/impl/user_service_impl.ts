import { GetAllUsersResponse } from "../../domain/transactional/GetAllUsersResponse";
import { GetUserResponse } from "../../domain/transactional/GetUserResponse";
import { UserDto } from "../../domain/UserDto";
import { UserRole } from "../../domain/UserRole";
import { AuthService } from "../auth_service";
import { UserService } from "../user_service";

const userPath = '/api/user/';
const currentUserPath = '/api/user/me';

export class UserServiceImpl implements UserService {
    authService: AuthService;

    constructor(authService: AuthService) {
        this.authService = authService
    }

    async createUser(username: string, password: string): Promise<boolean> {
        let response = await fetch(userPath, this.authService.withAuth({
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password, roles: [UserRole.USER] }),
        }));

        return response.ok && Boolean(await response.text());
    }

    async getAllUsers(): Promise<GetAllUsersResponse | Error> {
        let response = await fetch(userPath, this.authService.withAuth({
            method: 'GET',
        }));
        
        if (!response.ok) {
            return new Error("Failed to get users");
        }

        return await response.json();
    }

    async getCurrentUser(): Promise<GetUserResponse | Error> {
        let response = await fetch(currentUserPath, this.authService.withAuth({
            method: 'GET',
        }));

        if (!response.ok) {
            throw new Error("Failed to get current user");
        }

        return await response.json();
    }
}