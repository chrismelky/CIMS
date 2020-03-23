import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFinancialYear } from 'app/shared/model/financial-year.model';

@Component({
  selector: 'church-financial-year-detail',
  templateUrl: './financial-year-detail.component.html'
})
export class FinancialYearDetailComponent implements OnInit {
  financialYear: IFinancialYear | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ financialYear }) => (this.financialYear = financialYear));
  }

  previousState(): void {
    window.history.back();
  }
}
