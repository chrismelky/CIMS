import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeriodType } from 'app/shared/model/period-type.model';

@Component({
  selector: 'church-period-type-detail',
  templateUrl: './period-type-detail.component.html'
})
export class PeriodTypeDetailComponent implements OnInit {
  periodType: IPeriodType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ periodType }) => {
      this.periodType = periodType;
    });
  }

  previousState() {
    window.history.back();
  }
}
