import { IChurch } from 'app/shared/model/church.model';
import { WeekDay } from 'app/shared/model/enumerations/week-day.model';

export interface IChuchService {
  id?: number;
  name?: string;
  description?: any;
  day?: string;
  startTime?: string;
  endTime?: string;
  church?: IChurch;
}

export class ChuchService implements IChuchService {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public day?: string,
    public startTime?: string,
    public endTime?: string,
    public church?: IChurch
  ) {}
}
