import { Moment } from 'moment';
import { IMember } from 'app/shared/model/member.model';
import { IChurchActivity } from 'app/shared/model/church-activity.model';

export interface IMemberPromise {
  id?: number;
  promiseDate?: Moment;
  amount?: number;
  otherPromise?: string;
  fulfillmentDate?: Moment;
  isFulfilled?: boolean;
  member?: IMember;
  churchActivity?: IChurchActivity;
}

export class MemberPromise implements IMemberPromise {
  constructor(
    public id?: number,
    public promiseDate?: Moment,
    public amount?: number,
    public otherPromise?: string,
    public fulfillmentDate?: Moment,
    public isFulfilled?: boolean,
    public member?: IMember,
    public churchActivity?: IChurchActivity
  ) {
    this.isFulfilled = this.isFulfilled || false;
  }
}
