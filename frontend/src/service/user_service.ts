import { GetAllUsersResponse } from "../domain/transactional/GetAllUsersResponse";
import { GetUserResponse } from "../domain/transactional/GetUserResponse";

export interface UserService {
    getAllUsers(): Promise<GetAllUsersResponse | Error>;
    getCurrentUser(): Promise<GetUserResponse | Error>;
    createUser(username: string, password: string): Promise<boolean>;
}