export interface Payment {
    moneyAmout: number;
    creditCardNumber: string;
    cardVerificationCode: string;
    expirationDate: string;
    email: string;
    personName: string;
    createdAt: Date;
}