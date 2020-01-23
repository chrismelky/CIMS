import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPeriodContributionType, PeriodContributionType } from 'app/shared/model/period-contribution-type.model';
import { PeriodContributionTypeService } from './period-contribution-type.service';
import { IPeriodType } from 'app/shared/model/period-type.model';
import { PeriodTypeService } from 'app/entities/period-type/period-type.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';

@Component({
  selector: 'church-period-contribution-type-update',
  templateUrl: './period-contribution-type-update.component.html'
})
export class PeriodContributionTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  periodtypes: IPeriodType[];

  churches: IChurch[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    periodType: [null, Validators.required],
    church: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected periodContributionTypeService: PeriodContributionTypeService,
    protected periodTypeService: PeriodTypeService,
    protected churchService: ChurchService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ periodContributionType }) => {
      this.updateForm(periodContributionType);
    });
    this.periodTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPeriodType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPeriodType[]>) => response.body)
      )
      .subscribe((res: IPeriodType[]) => (this.periodtypes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.churchService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChurch[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurch[]>) => response.body)
      )
      .subscribe((res: IChurch[]) => (this.churches = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(periodContributionType: IPeriodContributionType) {
    this.editForm.patchValue({
      id: periodContributionType.id,
      name: periodContributionType.name,
      periodType: periodContributionType.periodType,
      church: periodContributionType.church
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const periodContributionType = this.createFromForm();
    if (periodContributionType.id !== undefined) {
      this.subscribeToSaveResponse(this.periodContributionTypeService.update(periodContributionType));
    } else {
      this.subscribeToSaveResponse(this.periodContributionTypeService.create(periodContributionType));
    }
  }

  private createFromForm(): IPeriodContributionType {
    return {
      ...new PeriodContributionType(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      periodType: this.editForm.get(['periodType']).value,
      church: this.editForm.get(['church']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodContributionType>>) {
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

  trackChurchById(index: number, item: IChurch) {
    return item.id;
  }
}
