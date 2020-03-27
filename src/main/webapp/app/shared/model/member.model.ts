import { Moment } from 'moment';
import { IMemberRelative } from 'app/shared/model/member-relative.model';
import { IChurch } from 'app/shared/model/church.model';
import { IChurchCommunity } from 'app/shared/model/church-community.model';
import { IMemberRite } from 'app/shared/model/member-rite.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { MaritalStatus } from 'app/shared/model/enumerations/marital-status.model';
import { IHomeChurchCommunity } from 'app/shared/model/home-church-community.model';

export interface IMember {
  id?: number;
  firstName?: string;
  lastName?: string;
  middleName?: string;
  memberRn?: string;
  churchRn?: string;
  gender?: Gender;
  phoneNumber?: string;
  email?: string;
  dateOfBirth?: Moment;
  placeOfBirth?: string;
  maritalStatus?: MaritalStatus;
  work?: string;
  placeOfWork?: string;
  isActive?: boolean;
  isDeceased?: boolean;
  deceasedDate?: Moment;
  relatives?: IMemberRelative[];
  church?: IChurch;
  churchCommunities?: IChurchCommunity[];
  memberRites?: IMemberRite[];
  homeChurchCommunity?: IHomeChurchCommunity;
}

export class Member implements IMember {
  constructor(
    public id?: number,
    public memberRn?: string,
    public churchRn?: string,
    public firstName?: string,
    public lastName?: string,
    public middleName?: string,
    public gender?: Gender,
    public phoneNumber?: string,
    public email?: string,
    public dateOfBirth?: Moment,
    public placeOfBirth?: string,
    public maritalStatus?: MaritalStatus,
    public work?: string,
    public placeOfWork?: string,
    public isActive?: boolean,
    public isDeceased?: boolean,
    public deceasedDate?: Moment,
    public relatives?: IMemberRelative[],
    public church?: IChurch,
    public churchCommunities?: IChurchCommunity[],
    public memberRites?: IMemberRite[],
    public homeChurchCommunity?: IHomeChurchCommunity
  ) {
    this.isActive = this.isActive || false;
    this.isDeceased = this.isDeceased || false;
  }
}
