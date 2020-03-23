import { Moment } from 'moment';

export interface IFinancialYear {
  id?: number;
  name?: string;
  startDate?: Moment;
  endDate?: Moment;
}

export class FinancialYear implements IFinancialYear {
  constructor(public id?: number, public name?: string, public startDate?: Moment, public endDate?: Moment) {}
}
