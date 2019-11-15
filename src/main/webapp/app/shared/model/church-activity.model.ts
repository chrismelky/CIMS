import { Moment } from 'moment';
import { IChurch } from 'app/shared/model/church.model';

export interface IChurchActivity {
  id?: number;
  name?: string;
  location?: string;
  estamateBudget?: number;
  startDate?: Moment;
  endDate?: Moment;
  church?: IChurch;
}

export class ChurchActivity implements IChurchActivity {
  constructor(
    public id?: number,
    public name?: string,
    public location?: string,
    public estamateBudget?: number,
    public startDate?: Moment,
    public endDate?: Moment,
    public church?: IChurch
  ) {}
}
