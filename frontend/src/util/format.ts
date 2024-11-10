export function formatAmount(amount: number): string {
    return amount > 0 ? `+${amount.toFixed(2)}$` : `${amount.toFixed(2)}$`;
}