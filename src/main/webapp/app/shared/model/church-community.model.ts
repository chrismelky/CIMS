import { IChurch } from 'app/shared/model/church.model';
import { IMember } from 'app/shared/model/member.model';

export interface IChurchCommunity {
  id?: number;
  name?: string;
  church?: IChurch;
  chairPerson?: IMember;
  secretary?: IMember;
  treasurer?: IMember;
  members?: IMember[];
}

export class ChurchCommunity implements IChurchCommunity {
  constructor(
    public id?: number,
    public name?: string,
    public church?: IChurch,
    public chairPerson?: IMember,
    public secretary?: IMember,
    public treasurer?: IMember,
    public members?: IMember[]
  ) {}
}
