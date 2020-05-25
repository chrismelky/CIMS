import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMemberPromise, MemberPromise } from 'app/shared/model/member-promise.model';
import { MemberPromiseService } from './member-promise.service';
import { IMember } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';
import { IChurchActivity } from 'app/shared/model/church-activity.model';
import { ChurchActivityService } from 'app/entities/church-activity/church-activity.service';
import { IFinancialYear } from 'app/shared/model/financial-year.model';
import { FinancialYearService } from 'app/entities/financial-year/financial-year.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';
import { IPeriodContributionType } from 'app/shared/model/period-contribution-type.model';
import { PeriodContributionTypeService } from 'app/entities/period-contribution-type/period-contribution-type.service';

type SelectableEntity = IMember | IChurchActivity | IFinancialYear | IChurch | IPeriodContributionType;

@Component({
  selector: 'church-member-promise-update',
  templateUrl: './member-promise-update.component.html'
})
export class MemberPromiseUpdateComponent implements OnInit {
  isSaving = false;
  promiseDateDp: any;
  fulfillmentDateDp: any;

  editForm = this.fb.group({
    id: [],
    promiseDate: [null, [Validators.required]],
    amount: [null, Validators.required],
    otherPromise: [],
    fulfillmentDate: [],
    isFulfilled: [],
    totalContribution: [],
    member: [null, Validators.required],
    financialYear: [null, Validators.required],
    church: [null, Validators.required],
    periodContributionType: [null, Validators.required]
  });

  constructor(
    protected memberPromiseService: MemberPromiseService,
    protected memberService: MemberService,
    protected churchActivityService: ChurchActivityService,
    protected financialYearService: FinancialYearService,
    protected churchService: ChurchService,
    protected periodContributionTypeService: PeriodContributionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ memberPromise, church, member, financialYear, type }) => {
      if (memberPromise.id === undefined) {
        memberPromise.church = { id: church.id };
        memberPromise.member = { id: member.id };
        memberPromise.financialYear = { id: financialYear.id };
        memberPromise.periodContributionType = { id: type.id };
        memberPromise.fulfillmentDate = financialYear.endDate;
        memberPromise.promiseDate = financialYear.startDate;
        memberPromise.totalContribution = 0;
      }
      this.updateForm(memberPromise);
    });
  }

  updateForm(memberPromise: IMemberPromise): void {
    this.editForm.patchValue({
      id: memberPromise.id,
      promiseDate: memberPromise.promiseDate,
      amount: memberPromise.amount,
      otherPromise: memberPromise.otherPromise,
      fulfillmentDate: memberPromise.fulfillmentDate,
      isFulfilled: memberPromise.isFulfilled,
      totalContribution: memberPromise.totalContribution,
      member: memberPromise.member,
      financialYear: memberPromise.financialYear,
      church: memberPromise.church,
      periodContributionType: memberPromise.periodContributionType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const memberPromise = this.createFromForm();
    if (memberPromise.id !== undefined) {
      this.subscribeToSaveResponse(this.memberPromiseService.update(memberPromise));
    } else {
      this.subscribeToSaveResponse(this.memberPromiseService.create(memberPromise));
    }
  }

  private createFromForm(): IMemberPromise {
    return {
      ...new MemberPromise(),
      id: this.editForm.get(['id'])!.value,
      promiseDate: this.editForm.get(['promiseDate'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      otherPromise: this.editForm.get(['otherPromise'])!.value,
      fulfillmentDate: this.editForm.get(['fulfillmentDate'])!.value,
      isFulfilled: this.editForm.get(['isFulfilled'])!.value,
      totalContribution: this.editForm.get(['totalContribution'])!.value,
      member: this.editForm.get(['member'])!.value,
      financialYear: this.editForm.get(['financialYear'])!.value,
      church: this.editForm.get(['church'])!.value,
      periodContributionType: this.editForm.get(['periodContributionType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMemberPromise>>): void {
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
