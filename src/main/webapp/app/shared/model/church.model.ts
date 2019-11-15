import { IChurch } from 'app/shared/model/church.model';
import { IChurchType } from 'app/shared/model/church-type.model';

export interface IChurch {
  id?: number;
  name?: string;
  email?: string;
  phoneNumber?: string;
  address?: string;
  fax?: string;
  latitude?: number;
  longitude?: number;
  otherDetails?: any;
  parent?: IChurch;
  type?: IChurchType;
}

export class Church implements IChurch {
  constructor(
    public id?: number,
    public name?: string,
    public email?: string,
    public phoneNumber?: string,
    public address?: string,
    public fax?: string,
    public latitude?: number,
    public longitude?: number,
    public otherDetails?: any,
    public parent?: IChurch,
    public type?: IChurchType
  ) {}
}
