import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeriodContribution } from 'app/shared/model/period-contribution.model';

@Component({
  selector: 'church-period-contribution-detail',
  templateUrl: './period-contribution-detail.component.html'
})
export class PeriodContributionDetailComponent implements OnInit {
  periodContribution: IPeriodContribution | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ periodContribution }) => (this.periodContribution = periodContribution));
  }

  previousState(): void {
    window.history.back();
  }
}
