import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeriodContributionItem } from 'app/shared/model/period-contribution-item.model';

@Component({
  selector: 'church-period-contribution-item-detail',
  templateUrl: './period-contribution-item-detail.component.html'
})
export class PeriodContributionItemDetailComponent implements OnInit {
  periodContributionItem: IPeriodContributionItem;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ periodContributionItem }) => {
      this.periodContributionItem = periodContributionItem;
    });
  }

  previousState() {
    window.history.back();
  }
}
