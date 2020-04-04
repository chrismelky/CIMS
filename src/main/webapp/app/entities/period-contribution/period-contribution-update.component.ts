import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IPeriodContribution, PeriodContribution } from 'app/shared/model/period-contribution.model';
import { PeriodContributionService } from './period-contribution.service';
import { IPeriod } from 'app/shared/model/period.model';
import { IMember } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';

@Component({
  selector: 'church-period-contribution-update',
  templateUrl: './period-contribution-update.component.html'
})
export class PeriodContributionUpdateComponent implements OnInit {
  isSaving: boolean;

  periods: IPeriod[];
  periodId: number;
  churchId: number;
  memberId: number;
  typeId: number;
  dueDateDp: any;

  editForm = this.fb.group({
    id: [],
    amountPromised: [null, [Validators.required]],
    amountContributed: [{ value: 0, disabled: true }],
    description: [],
    dueDate: [null, [Validators.required]],
    period: [null, Validators.required],
    member: [null, Validators.required],
    church: [null, Validators.required],
    periodContributionType: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected periodContributionService: PeriodContributionService,
    protected memberService: MemberService,
    protected churchService: ChurchService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.periodId = this.activatedRoute.snapshot.params['periodId'];
    this.churchId = this.activatedRoute.snapshot.params['churchId'];
    this.memberId = this.activatedRoute.snapshot.params['memberId'];
    this.typeId = this.activatedRoute.snapshot.params['typeId'];

    this.activatedRoute.data.subscribe(({ periodContribution }) => {
      this.updateForm({
        ...periodContribution,
        period: { id: this.periodId },
        member: { id: this.memberId },
        church: { id: this.churchId },
        periodContributionType: { id: this.typeId }
      });
    });
  }

  updateForm(periodContribution: IPeriodContribution) {
    this.editForm.patchValue({
      id: periodContribution.id,
      amountPromised: periodContribution.amountPromised,
      amountContributed: periodContribution.amountContributed,
      description: periodContribution.description,
      dueDate: periodContribution.dueDate,
      period: periodContribution.period,
      member: periodContribution.member,
      church: periodContribution.church,
      periodContributionType: periodContribution.periodContributionType
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
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
      id: this.editForm.get(['id']).value,
      amountPromised: this.editForm.get(['amountPromised']).value,
      amountContributed: this.editForm.get(['amountContributed']).value,
      description: this.editForm.get(['description']).value,
      dueDate: this.editForm.get(['dueDate']).value,
      period: this.editForm.get(['period']).value,
      member: this.editForm.get(['member']).value,
      church: this.editForm.get(['church']).value,
      periodContributionType: this.editForm.get(['periodContributionType']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodContribution>>) {
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

  trackMemberById(index: number, item: IMember) {
    return item.id;
  }

  trackChurchById(index: number, item: IChurch) {
    return item.id;
  }
}
