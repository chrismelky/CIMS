import { IPeriodType } from 'app/shared/model/period-type.model';
import { IChurch } from 'app/shared/model/church.model';

export interface IPeriodContributionType {
  id?: number;
  name?: string;
  periodType?: IPeriodType;
  church?: IChurch;
}

export class PeriodContributionType implements IPeriodContributionType {
  constructor(public id?: number, public name?: string, public periodType?: IPeriodType, public church?: IChurch) {}
}
