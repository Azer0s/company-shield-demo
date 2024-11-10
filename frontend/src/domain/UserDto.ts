import { UserRole } from "./UserRole";

export interface UserDto {
    id: string;
    username: string;
    balance: number;
    roles: UserRole[];
}