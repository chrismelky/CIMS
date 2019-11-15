export interface IContributionType {
  id?: number;
  code?: string;
  name?: string;
}

export class ContributionType implements IContributionType {
  constructor(public id?: number, public code?: string, public name?: string) {}
}
