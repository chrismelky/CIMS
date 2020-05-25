import { Moment } from 'moment';
import { IPeriod } from 'app/shared/model/period.model';
import { IMemberPromise } from 'app/shared/model/member-promise.model';

export interface IPeriodContribution {
  id?: number;
  amountPromised?: number;
  amountContributed?: number;
  description?: string;
  dueDate?: Moment;
  period?: IPeriod;
  memberPromise?: IMemberPromise;
}

export class PeriodContribution implements IPeriodContribution {
  constructor(
    public id?: number,
    public amountPromised?: number,
    public amountContributed?: number,
    public description?: string,
    public dueDate?: Moment,
    public period?: IPeriod,
    public memberPromise?: IMemberPromise
  ) {}
}
