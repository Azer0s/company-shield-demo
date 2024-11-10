import { GetTransactionsResponse } from "../../domain/transactional/GetTransactionsResponse";
import { TransactionDto } from "../../domain/TransactionDto";
import { AuthService } from "../auth_service";
import { TransactionService } from "../transaction_service";

const myTransactionsPath = '/api/transaction/me';
const depositPath = '/api/transaction/deposit';
const withdrawPath = '/api/transaction/withdraw';
const transferPath = '/api/transaction/transfer';

export class TransactionServiceImpl implements TransactionService {
    authService: AuthService;

    constructor(authService: AuthService) {
        this.authService = authService
    }

    async deposit(userId: string, amount: number): Promise<boolean> {
        let response = await fetch(depositPath, this.authService.withAuth({
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userId, amount }),
        }));

        return response.ok && Boolean(await response.text());
    }

    async withdraw(userId: string, amount: number): Promise<boolean> {
        let response = await fetch(withdrawPath, this.authService.withAuth({
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userId, amount }),
        }));

        return response.ok && Boolean(await response.text());
    }

    async transfer(userId: string, amount: number): Promise<boolean> {
        let response = await fetch(transferPath, this.authService.withAuth({
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ toUserId: userId, amount }),
        }));

        return response.ok && Boolean(await response.text());
    }

    async getTransactions(): Promise<GetTransactionsResponse | Error> {
        let response = await fetch(myTransactionsPath, this.authService.withAuth({
            method: 'GET',
        }));

        if (!response.ok) {
            return new Error("Failed to get transactions");
        }

        return await response.json();
    }
}