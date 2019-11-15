export interface IPaymentMethod {
  id?: number;
  code?: string;
  name?: string;
}

export class PaymentMethod implements IPaymentMethod {
  constructor(public id?: number, public code?: string, public name?: string) {}
}
