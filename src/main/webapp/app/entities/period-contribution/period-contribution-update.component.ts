import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPeriodContribution, PeriodContribution } from 'app/shared/model/period-contribution.model';
import { PeriodContributionService } from './period-contribution.service';
import { IPeriod } from 'app/shared/model/period.model';
import { PeriodService } from 'app/entities/period/period.service';
import { IMemberPromise } from 'app/shared/model/member-promise.model';
import { MemberPromiseService } from 'app/entities/member-promise/member-promise.service';

type SelectableEntity = IPeriod | IMemberPromise;

@Component({
  selector: 'church-period-contribution-update',
  templateUrl: './period-contribution-update.component.html'
})
export class PeriodContributionUpdateComponent implements OnInit {
  isSaving = false;
  periods: IPeriod[] = [];
  memberpromises: IMemberPromise[] = [];
  dueDateDp: any;

  editForm = this.fb.group({
    id: [],
    amountPromised: [null, [Validators.required]],
    amountContributed: [],
    description: [],
    dueDate: [null, [Validators.required]],
    period: [null, Validators.required],
    memberPromise: []
  });

  constructor(
    protected periodContributionService: PeriodContributionService,
    protected periodService: PeriodService,
    protected memberPromiseService: MemberPromiseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ periodContribution }) => {
      this.updateForm(periodContribution);

      this.periodService.query().subscribe((res: HttpResponse<IPeriod[]>) => (this.periods = res.body || []));

      this.memberPromiseService.query().subscribe((res: HttpResponse<IMemberPromise[]>) => (this.memberpromises = res.body || []));
    });
  }

  updateForm(periodContribution: IPeriodContribution): void {
    this.editForm.patchValue({
      id: periodContribution.id,
      amountPromised: periodContribution.amountPromised,
      amountContributed: periodContribution.amountContributed,
      description: periodContribution.description,
      dueDate: periodContribution.dueDate,
      period: periodContribution.period,
      memberPromise: periodContribution.memberPromise
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const periodContribution = this.createFromForm();
    if (periodContribution.id !== undefined) {
      this.subscribeToSaveResponse(this.periodContributionService.update(periodContribution));
    } else {
      this.subscribeToSaveResponse(this.periodContributionService.create(periodContribution));
    }
  }

  private createFromForm(): IPeriodContribution {
    return {
      ...new PeriodContribution(),
      id: this.editForm.get(['id'])!.value,
      amountPromised: this.editForm.get(['amountPromised'])!.value,
      amountContributed: this.editForm.get(['amountContributed'])!.value,
      description: this.editForm.get(['description'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value,
      period: this.editForm.get(['period'])!.value,
      memberPromise: this.editForm.get(['memberPromise'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodContribution>>): void {
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
