import { IMember } from 'app/shared/model/member.model';
import { RelativeType } from 'app/shared/model/enumerations/relative-type.model';

export interface IMemberRelative {
  id?: number;
  relativeType?: RelativeType;
  relativeFullName?: string;
  relativePhoneNumber?: string;
  member?: IMember;
}

export class MemberRelative implements IMemberRelative {
  constructor(
    public id?: number,
    public relativeType?: RelativeType,
    public relativeFullName?: string,
    public relativePhoneNumber?: string,
    public member?: IMember
  ) {}
}
