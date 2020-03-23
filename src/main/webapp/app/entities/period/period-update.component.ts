import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPeriod, Period } from 'app/shared/model/period.model';
import { PeriodService } from './period.service';
import { IPeriodType } from 'app/shared/model/period-type.model';
import { PeriodTypeService } from 'app/entities/period-type/period-type.service';
import { IFinancialYear } from 'app/shared/model/financial-year.model';
import { FinancialYearService } from 'app/entities/financial-year/financial-year.service';

type SelectableEntity = IPeriodType | IFinancialYear;

@Component({
  selector: 'church-period-update',
  templateUrl: './period-update.component.html'
})
export class PeriodUpdateComponent implements OnInit {
  isSaving = false;
  periodtypes: IPeriodType[] = [];
  financialyears: IFinancialYear[] = [];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    isCurrent: [],
    type: [null, Validators.required],
    financialYear: []
  });

  constructor(
    protected periodService: PeriodService,
    protected periodTypeService: PeriodTypeService,
    protected financialYearService: FinancialYearService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ period }) => {
      this.updateForm(period);

      this.periodTypeService.query().subscribe((res: HttpResponse<IPeriodType[]>) => (this.periodtypes = res.body || []));

      this.financialYearService.query().subscribe((res: HttpResponse<IFinancialYear[]>) => (this.financialyears = res.body || []));
    });
  }

  updateForm(period: IPeriod): void {
    this.editForm.patchValue({
      id: period.id,
      name: period.name,
      startDate: period.startDate,
      endDate: period.endDate,
      isCurrent: period.isCurrent,
      type: period.type,
      financialYear: period.financialYear
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const period = this.createFromForm();
    if (period.id !== undefined) {
      this.subscribeToSaveResponse(this.periodService.update(period));
    } else {
      this.subscribeToSaveResponse(this.periodService.create(period));
    }
  }

  private createFromForm(): IPeriod {
    return {
      ...new Period(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      isCurrent: this.editForm.get(['isCurrent'])!.value,
      type: this.editForm.get(['type'])!.value,
      financialYear: this.editForm.get(['financialYear'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriod>>): void {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
