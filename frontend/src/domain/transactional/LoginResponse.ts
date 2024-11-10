import { UserDto } from "../UserDto";

export interface LoginResponse {
    token: string;
    forUser: UserDto;
}