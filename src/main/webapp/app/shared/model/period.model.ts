import { Moment } from 'moment';
import { IPeriodType } from 'app/shared/model/period-type.model';

export interface IPeriod {
  id?: number;
  name?: string;
  startDate?: Moment;
  endDate?: Moment;
  isCurrent?: boolean;
  type?: IPeriodType;
}

export class Period implements IPeriod {
  constructor(
    public id?: number,
    public name?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public isCurrent?: boolean,
    public type?: IPeriodType
  ) {
    this.isCurrent = this.isCurrent || false;
  }
}
