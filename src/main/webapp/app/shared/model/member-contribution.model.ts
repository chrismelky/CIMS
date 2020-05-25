import { Moment } from 'moment';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IMemberPromise } from 'app/shared/model/member-promise.model';

export interface IMemberContribution {
  id?: number;
  paymentDate?: Moment;
  amount?: number;
  memberPromise?: IMemberPromise;
  paymentMethod?: IPaymentMethod;
}

export class MemberContribution implements IMemberContribution {
  constructor(
    public id?: number,
    public paymentDate?: Moment,
    public amount?: number,
    public memberPromise?: IMemberPromise,
    public paymentMethod?: IPaymentMethod
  ) {}
}
