import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeriodContributionType } from 'app/shared/model/period-contribution-type.model';

@Component({
  selector: 'church-period-contribution-type-detail',
  templateUrl: './period-contribution-type-detail.component.html'
})
export class PeriodContributionTypeDetailComponent implements OnInit {
  periodContributionType: IPeriodContributionType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ periodContributionType }) => {
      this.periodContributionType = periodContributionType;
    });
  }

  previousState() {
    window.history.back();
  }
}
