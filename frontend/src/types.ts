export type Transaction = {
    date: string;
    type: 'DEPOSIT' | 'WITHDRAWAL';
    amount: number;
    balanceAfter: number;
};
