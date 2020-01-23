import { IChurch } from 'app/shared/model/church.model';
import { IMember } from 'app/shared/model/member.model';

export interface IHomeChurchCommunity {
  id?: number;
  name?: string;
  numberOfHouseHold?: number;
  phoneNumber?: string;
  address?: string;
  church?: IChurch;
  chairMan?: IMember;
  secreatry?: IMember;
  treasurer?: IMember;
}

export class HomeChurchCommunity implements IHomeChurchCommunity {
  constructor(
    public id?: number,
    public name?: string,
    public numberOfHouseHold?: number,
    public phoneNumber?: string,
    public address?: string,
    public church?: IChurch,
    public chairMan?: IMember,
    public secreatry?: IMember,
    public treasurer?: IMember
  ) {}
}
