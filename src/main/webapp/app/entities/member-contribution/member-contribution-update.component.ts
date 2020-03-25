import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMemberContribution, MemberContribution } from 'app/shared/model/member-contribution.model';
import { MemberContributionService } from './member-contribution.service';
import { IMember } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/payment-method.service';
import { IMemberPromise } from 'app/shared/model/member-promise.model';
import { MemberPromiseService } from 'app/entities/member-promise/member-promise.service';
import { IContributionType } from 'app/shared/model/contribution-type.model';
import { ContributionTypeService } from 'app/entities/contribution-type/contribution-type.service';

@Component({
  selector: 'church-member-contribution-update',
  templateUrl: './member-contribution-update.component.html'
})
export class MemberContributionUpdateComponent implements OnInit {
  isSaving: boolean;

  paymentmethods: IPaymentMethod[];

  memberpromises: IMemberPromise[];

  contributiontypes: IContributionType[];
  paymentDateDp: any;

  member: IMember;

  editForm = this.fb.group({
    id: [],
    paymentDate: [],
    amount: [null, [Validators.required]],
    member: [null, Validators.required],
    church: [null, Validators.required],
    paymentMethod: [null, Validators.required],
    promise: [],
    type: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected memberContributionService: MemberContributionService,
    protected memberService: MemberService,
    protected churchService: ChurchService,
    protected paymentMethodService: PaymentMethodService,
    protected memberPromiseService: MemberPromiseService,
    protected contributionTypeService: ContributionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ memberContribution, member }) => {
      this.member = member;
      memberContribution.member = { id: this.member.id };
      memberContribution.church = memberContribution.church ? { id: memberContribution.church.id } : { id: this.member.church.id };
      this.updateForm(memberContribution);
    });
    this.paymentMethodService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPaymentMethod[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPaymentMethod[]>) => response.body)
      )
      .subscribe((res: IPaymentMethod[]) => (this.paymentmethods = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.memberPromiseService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMemberPromise[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMemberPromise[]>) => response.body)
      )
      .subscribe((res: IMemberPromise[]) => (this.memberpromises = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.contributionTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IContributionType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IContributionType[]>) => response.body)
      )
      .subscribe((res: IContributionType[]) => (this.contributiontypes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(memberContribution: IMemberContribution) {
    this.editForm.patchValue({
      id: memberContribution.id,
      paymentDate: memberContribution.paymentDate,
      amount: memberContribution.amount,
      member: memberContribution.member,
      church: memberContribution.church,
      paymentMethod: memberContribution.paymentMethod,
      promise: memberContribution.promise,
      type: memberContribution.type
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const memberContribution = this.createFromForm();
    if (memberContribution.id !== undefined) {
      this.subscribeToSaveResponse(this.memberContributionService.update(memberContribution));
    } else {
      this.subscribeToSaveResponse(this.memberContributionService.create(memberContribution));
    }
  }

  private createFromForm(): IMemberContribution {
    return {
      ...new MemberContribution(),
      id: this.editForm.get(['id']).value,
      paymentDate: this.editForm.get(['paymentDate']).value,
      amount: this.editForm.get(['amount']).value,
      member: this.editForm.get(['member']).value,
      church: this.editForm.get(['church']).value,
      paymentMethod: this.editForm.get(['paymentMethod']).value,
      promise: this.editForm.get(['promise']).value,
      type: this.editForm.get(['type']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMemberContribution>>) {
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

  trackPaymentMethodById(index: number, item: IPaymentMethod) {
    return item.id;
  }

  trackMemberPromiseById(index: number, item: IMemberPromise) {
    return item.id;
  }

  trackContributionTypeById(index: number, item: IContributionType) {
    return item.id;
  }
}
