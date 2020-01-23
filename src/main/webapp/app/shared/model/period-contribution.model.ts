import { Moment } from 'moment';
import { IPeriod } from 'app/shared/model/period.model';
import { IMember } from 'app/shared/model/member.model';
import { IChurch } from 'app/shared/model/church.model';
import { IPeriodContributionType } from 'app/shared/model/period-contribution-type.model';

export interface IPeriodContribution {
  id?: number;
  amountPromised?: number;
  amountContributed?: number;
  description?: string;
  dueDate?: Moment;
  period?: IPeriod;
  member?: IMember;
  church?: IChurch;
  periodContributionType?: IPeriodContributionType;
}

export class PeriodContribution implements IPeriodContribution {
  constructor(
    public id?: number,
    public amountPromised?: number,
    public amountContributed?: number,
    public description?: string,
    public dueDate?: Moment,
    public period?: IPeriod,
    public member?: IMember,
    public church?: IChurch,
    public periodContributionType?: IPeriodContributionType
  ) {}
}
