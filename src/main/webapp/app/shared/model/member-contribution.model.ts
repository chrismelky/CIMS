import { Moment } from 'moment';
import { IMember } from 'app/shared/model/member.model';
import { IChurch } from 'app/shared/model/church.model';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IMemberPromise } from 'app/shared/model/member-promise.model';
import { IContributionType } from 'app/shared/model/contribution-type.model';

export interface IMemberContribution {
  id?: number;
  paymentDate?: Moment;
  amount?: number;
  member?: IMember;
  church?: IChurch;
  paymentMethod?: IPaymentMethod;
  promise?: IMemberPromise;
  type?: IContributionType;
}

export class MemberContribution implements IMemberContribution {
  constructor(
    public id?: number,
    public paymentDate?: Moment,
    public amount?: number,
    public member?: IMember,
    public church?: IChurch,
    public paymentMethod?: IPaymentMethod,
    public promise?: IMemberPromise,
    public type?: IContributionType
  ) {}
}
