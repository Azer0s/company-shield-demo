import { TransactionType } from "./TransactionType";

export interface TransactionDto {
    fromId: string | null;
    toId: string | null;
    accountBalanceBefore: number;
    accountBalanceAfter: number;
    type: TransactionType;
}