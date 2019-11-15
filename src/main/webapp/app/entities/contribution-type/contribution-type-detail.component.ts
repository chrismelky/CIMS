import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContributionType } from 'app/shared/model/contribution-type.model';

@Component({
  selector: 'church-contribution-type-detail',
  templateUrl: './contribution-type-detail.component.html'
})
export class ContributionTypeDetailComponent implements OnInit {
  contributionType: IContributionType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ contributionType }) => {
      this.contributionType = contributionType;
    });
  }

  previousState() {
    window.history.back();
  }
}
