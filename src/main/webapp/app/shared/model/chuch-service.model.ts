import { IChurch } from 'app/shared/model/church.model';
import { WeekDay } from 'app/shared/model/enumerations/week-day.model';

export interface IChuchService {
  id?: number;
  name?: string;
  description?: any;
  day?: WeekDay;
  startTime?: string;
  endTime?: string;
  church?: IChurch;
}

export class ChuchService implements IChuchService {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public day?: WeekDay,
    public startTime?: string,
    public endTime?: string,
    public church?: IChurch
  ) {}
}
