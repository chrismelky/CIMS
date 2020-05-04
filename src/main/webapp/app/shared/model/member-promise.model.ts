import { Moment } from 'moment';
import { IMember } from 'app/shared/model/member.model';
import { IFinancialYear } from 'app/shared/model/financial-year.model';
import { IChurch } from 'app/shared/model/church.model';
import { IPeriodContributionType } from 'app/shared/model/period-contribution-type.model';

export interface IMemberPromise {
  id?: number;
  promiseDate?: Moment;
  amount?: number;
  otherPromise?: string;
  fulfillmentDate?: Moment;
  isFulfilled?: boolean;
  totalContribution?: number;
  member?: IMember;
  financialYear?: IFinancialYear;
  church?: IChurch;
  periodContributionType?: IPeriodContributionType;
}

export class MemberPromise implements IMemberPromise {
  constructor(
    public id?: number,
    public promiseDate?: Moment,
    public amount?: number,
    public otherPromise?: string,
    public fulfillmentDate?: Moment,
    public isFulfilled?: boolean,
    public totalContribution?: number,
    public member?: IMember,
    public financialYear?: IFinancialYear,
    public church?: IChurch,
    public periodContributionType?: IPeriodContributionType
  ) {
    this.isFulfilled = this.isFulfilled || false;
  }
}
