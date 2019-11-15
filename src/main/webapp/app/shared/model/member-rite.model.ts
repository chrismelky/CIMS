import { Moment } from 'moment';
import { IRite } from 'app/shared/model/rite.model';
import { IMember } from 'app/shared/model/member.model';
import { IChurch } from 'app/shared/model/church.model';

export interface IMemberRite {
  id?: number;
  dateReceived?: Moment;
  rite?: IRite;
  member?: IMember;
  church?: IChurch;
}

export class MemberRite implements IMemberRite {
  constructor(public id?: number, public dateReceived?: Moment, public rite?: IRite, public member?: IMember, public church?: IChurch) {}
}
