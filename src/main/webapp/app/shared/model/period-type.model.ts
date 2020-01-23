export interface IPeriodType {
  id?: number;
  name?: string;
}

export class PeriodType implements IPeriodType {
  constructor(public id?: number, public name?: string) {}
}
