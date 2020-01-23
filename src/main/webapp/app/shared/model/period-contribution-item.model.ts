import { Moment } from 'moment';
import { IPeriodContribution } from 'app/shared/model/period-contribution.model';

export interface IPeriodContributionItem {
  id?: number;
  amount?: number;
  description?: string;
  dateReceived?: Moment;
  receivedBy?: string;
  periodContribution?: IPeriodContribution;
}

export class PeriodContributionItem implements IPeriodContributionItem {
  constructor(
    public id?: number,
    public amount?: number,
    public description?: string,
    public dateReceived?: Moment,
    public receivedBy?: string,
    public periodContribution?: IPeriodContribution
  ) {}
}
