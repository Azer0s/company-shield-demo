import { GetTransactionsResponse } from "../domain/transactional/GetTransactionsResponse";
import { TransactionDto } from "../domain/TransactionDto";

export interface TransactionService {
    getTransactions: () => Promise<GetTransactionsResponse | Error>;
    deposit: (accountId: string, amount: number) => Promise<boolean>;
    withdraw: (accountId: string, amount: number) => Promise<boolean>;
    transfer: (toAccountId: string, amount: number) => Promise<boolean>;
}