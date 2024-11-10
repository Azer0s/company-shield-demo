interface DashboardStrings {
    transactionsLabel: string;
    fromLabel: string;
    toLabel: string;
    amountLabel: string;
    typeLabel: string;
    newTransactionLabel: string;
    transferLabel: string;
    welcomeBackLabel: string;
    accountBalanceLabel: string;
    selfLabel: string;
    naLabel: string;
    dashboardLabel: string;

    transactionTypeStrings: TransactionTypeStrings;
}

export interface TransactionTypeStrings {
    deposit: string;
    withdrawal: string;
    transferOut: string;
    transferIn: string;
}

export const Strings: DashboardStrings = {
    transactionsLabel: 'Transactions',
    fromLabel: 'From',
    toLabel: 'To',
    amountLabel: 'Amount',
    typeLabel: 'Type',
    newTransactionLabel: 'New transaction',
    transferLabel: 'Transfer',
    welcomeBackLabel: 'Welcome back',
    accountBalanceLabel: 'Account balance',
    selfLabel: 'Self',
    naLabel: 'N/A',
    dashboardLabel: 'Dashboard',

    transactionTypeStrings: {
        deposit: 'Deposit',
        withdrawal: 'Withdrawal',
        transferOut: 'Transfer (out)',
        transferIn: 'Transfer (in)'
    }
};