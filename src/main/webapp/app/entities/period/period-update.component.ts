import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IPeriod, Period } from 'app/shared/model/period.model';
import { PeriodService } from './period.service';
import { IPeriodType } from 'app/shared/model/period-type.model';
import { PeriodTypeService } from 'app/entities/period-type/period-type.service';

@Component({
  selector: 'church-period-update',
  templateUrl: './period-update.component.html'
})
export class PeriodUpdateComponent implements OnInit {
  isSaving: boolean;

  periodtypes: IPeriodType[];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    isCurrent: [],
    type: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected periodService: PeriodService,
    protected periodTypeService: PeriodTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ period }) => {
      this.updateForm(period);
    });
    this.periodTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPeriodType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPeriodType[]>) => response.body)
      )
      .subscribe((res: IPeriodType[]) => (this.periodtypes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(period: IPeriod) {
    this.editForm.patchValue({
      id: period.id,
      name: period.name,
      startDate: period.startDate,
      endDate: period.endDate,
      isCurrent: period.isCurrent,
      type: period.type
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
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
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      startDate: this.editForm.get(['startDate']).value,
      endDate: this.editForm.get(['endDate']).value,
      isCurrent: this.editForm.get(['isCurrent']).value,
      type: this.editForm.get(['type']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriod>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackPeriodTypeById(index: number, item: IPeriodType) {
    return item.id;
  }
}
