import { Moment } from 'moment';
import { IPeriodType } from 'app/shared/model/period-type.model';
import { IFinancialYear } from 'app/shared/model/financial-year.model';

export interface IPeriod {
  id?: number;
  name?: string;
  startDate?: Moment;
  endDate?: Moment;
  isCurrent?: boolean;
  type?: IPeriodType;
  financialYear?: IFinancialYear;
}

export class Period implements IPeriod {
  constructor(
    public id?: number,
    public name?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public isCurrent?: boolean,
    public type?: IPeriodType,
    public financialYear?: IFinancialYear
  ) {
    this.isCurrent = this.isCurrent || false;
  }
}
