import { TransactionDto } from "../TransactionDto";

export interface GetTransactionsResponse {
    transactions: TransactionDto[];
}