export interface IChurchType {
  id?: number;
  name?: string;
}

export class ChurchType implements IChurchType {
  constructor(public id?: number, public name?: string) {}
}
