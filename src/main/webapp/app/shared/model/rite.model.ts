export interface IRite {
  id?: number;
  code?: string;
  name?: string;
}

export class Rite implements IRite {
  constructor(public id?: number, public code?: string, public name?: string) {}
}
