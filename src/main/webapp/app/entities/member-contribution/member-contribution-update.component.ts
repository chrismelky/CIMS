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
import { IChurch } from 'app/shared/model/church.model';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/payment-method.service';
import { IMemberPromise } from 'app/shared/model/member-promise.model';
import { IContributionType } from 'app/shared/model/contribution-type.model';
import * as moment from 'moment';
import { SessionStorageService } from 'ngx-webstorage';

@Component({
  selector: 'church-member-contribution-update',
  templateUrl: './member-contribution-update.component.html'
})
export class MemberContributionUpdateComponent implements OnInit {
  isSaving: boolean;

  paymentmethods: IPaymentMethod[];

  paymentDateDp: any;

  memberPromise: IMember;

  selectedDate: any;

  editForm = this.fb.group({
    id: [],
    paymentDate: [],
    amount: [null, [Validators.required]],
    memberPromise: [null, Validators.required],
    paymentMethod: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected memberContributionService: MemberContributionService,
    protected paymentMethodService: PaymentMethodService,
    protected activatedRoute: ActivatedRoute,
    protected sessionStorage: SessionStorageService,
    private fb: FormBuilder
  ) {
    this.selectedDate = this.sessionStorage.retrieve('piDate');
    console.error(this.selectedDate);
  }

  ngOnInit() {
    console.error(this.selectedDate);
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ memberContribution, memberPromise }) => {
      this.memberPromise = memberPromise;
      memberContribution.memberPromise = { id: this.memberPromise.id };
      if (memberContribution.id === undefined) {
        memberContribution.paymentDate = this.selectedDate !== undefined ? moment(this.selectedDate) : null;
      }
      console.error(memberContribution);
      this.updateForm(memberContribution);
    });
    this.paymentMethodService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPaymentMethod[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPaymentMethod[]>) => response.body)
      )
      .subscribe((res: IPaymentMethod[]) => (this.paymentmethods = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(memberContribution: IMemberContribution) {
    this.editForm.patchValue({
      id: memberContribution.id,
      paymentDate: memberContribution.paymentDate,
      amount: memberContribution.amount,
      memberPromise: memberContribution.memberPromise,
      paymentMethod: memberContribution.paymentMethod
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
      memberPromise: this.editForm.get(['memberPromise']).value,
      paymentMethod: this.editForm.get(['paymentMethod']).value
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
