import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMemberPromise, MemberPromise } from 'app/shared/model/member-promise.model';
import { MemberPromiseService } from './member-promise.service';
import { IMember } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';
import { IChurchActivity } from 'app/shared/model/church-activity.model';
import { ChurchActivityService } from 'app/entities/church-activity/church-activity.service';

@Component({
  selector: 'church-member-promise-update',
  templateUrl: './member-promise-update.component.html'
})
export class MemberPromiseUpdateComponent implements OnInit {
  isSaving: boolean;

  members: IMember[];

  churchactivities: IChurchActivity[];
  promiseDateDp: any;
  fulfillmentDateDp: any;

  editForm = this.fb.group({
    id: [],
    promiseDate: [null, [Validators.required]],
    amount: [],
    otherPromise: [],
    fulfillmentDate: [],
    isFulfilled: [],
    member: [null, Validators.required],
    churchActivity: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected memberPromiseService: MemberPromiseService,
    protected memberService: MemberService,
    protected churchActivityService: ChurchActivityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ memberPromise }) => {
      this.updateForm(memberPromise);
    });
    this.memberService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMember[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMember[]>) => response.body)
      )
      .subscribe((res: IMember[]) => (this.members = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.churchActivityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChurchActivity[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurchActivity[]>) => response.body)
      )
      .subscribe((res: IChurchActivity[]) => (this.churchactivities = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(memberPromise: IMemberPromise) {
    this.editForm.patchValue({
      id: memberPromise.id,
      promiseDate: memberPromise.promiseDate,
      amount: memberPromise.amount,
      otherPromise: memberPromise.otherPromise,
      fulfillmentDate: memberPromise.fulfillmentDate,
      isFulfilled: memberPromise.isFulfilled,
      member: memberPromise.member,
      churchActivity: memberPromise.churchActivity
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
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
      id: this.editForm.get(['id']).value,
      promiseDate: this.editForm.get(['promiseDate']).value,
      amount: this.editForm.get(['amount']).value,
      otherPromise: this.editForm.get(['otherPromise']).value,
      fulfillmentDate: this.editForm.get(['fulfillmentDate']).value,
      isFulfilled: this.editForm.get(['isFulfilled']).value,
      member: this.editForm.get(['member']).value,
      churchActivity: this.editForm.get(['churchActivity']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMemberPromise>>) {
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

  trackChurchActivityById(index: number, item: IChurchActivity) {
    return item.id;
  }
}
