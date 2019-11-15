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
import { IMember, Member } from 'app/shared/model/member.model';
import { MemberService } from './member.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';
import { IChurchCommunity } from 'app/shared/model/church-community.model';
import { ChurchCommunityService } from 'app/entities/church-community/church-community.service';

@Component({
  selector: 'church-member-update',
  templateUrl: './member-update.component.html'
})
export class MemberUpdateComponent implements OnInit {
  isSaving: boolean;

  churches: IChurch[];

  churchcommunities: IChurchCommunity[];
  dateOfBirthDp: any;
  deceasedDateDp: any;

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required, Validators.maxLength(100)]],
    lastName: [null, [Validators.required, Validators.maxLength(100)]],
    middleName: [null, [Validators.maxLength(100)]],
    gender: [],
    phoneNumber: [],
    email: [],
    dateOfBirth: [],
    placeOfBirth: [],
    maritalStatus: [],
    work: [],
    placeOfWork: [],
    isActive: [],
    isDeceased: [],
    deceasedDate: [],
    church: [null, Validators.required],
    churchCommunities: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected memberService: MemberService,
    protected churchService: ChurchService,
    protected churchCommunityService: ChurchCommunityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ member }) => {
      this.updateForm(member);
    });
    this.churchService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChurch[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurch[]>) => response.body)
      )
      .subscribe((res: IChurch[]) => (this.churches = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.churchCommunityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChurchCommunity[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurchCommunity[]>) => response.body)
      )
      .subscribe((res: IChurchCommunity[]) => (this.churchcommunities = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(member: IMember) {
    this.editForm.patchValue({
      id: member.id,
      firstName: member.firstName,
      lastName: member.lastName,
      middleName: member.middleName,
      gender: member.gender,
      phoneNumber: member.phoneNumber,
      email: member.email,
      dateOfBirth: member.dateOfBirth,
      placeOfBirth: member.placeOfBirth,
      maritalStatus: member.maritalStatus,
      work: member.work,
      placeOfWork: member.placeOfWork,
      isActive: member.isActive,
      isDeceased: member.isDeceased,
      deceasedDate: member.deceasedDate,
      church: member.church,
      churchCommunities: member.churchCommunities
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const member = this.createFromForm();
    if (member.id !== undefined) {
      this.subscribeToSaveResponse(this.memberService.update(member));
    } else {
      this.subscribeToSaveResponse(this.memberService.create(member));
    }
  }

  private createFromForm(): IMember {
    return {
      ...new Member(),
      id: this.editForm.get(['id']).value,
      firstName: this.editForm.get(['firstName']).value,
      lastName: this.editForm.get(['lastName']).value,
      middleName: this.editForm.get(['middleName']).value,
      gender: this.editForm.get(['gender']).value,
      phoneNumber: this.editForm.get(['phoneNumber']).value,
      email: this.editForm.get(['email']).value,
      dateOfBirth: this.editForm.get(['dateOfBirth']).value,
      placeOfBirth: this.editForm.get(['placeOfBirth']).value,
      maritalStatus: this.editForm.get(['maritalStatus']).value,
      work: this.editForm.get(['work']).value,
      placeOfWork: this.editForm.get(['placeOfWork']).value,
      isActive: this.editForm.get(['isActive']).value,
      isDeceased: this.editForm.get(['isDeceased']).value,
      deceasedDate: this.editForm.get(['deceasedDate']).value,
      church: this.editForm.get(['church']).value,
      churchCommunities: this.editForm.get(['churchCommunities']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMember>>) {
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

  trackChurchById(index: number, item: IChurch) {
    return item.id;
  }

  trackChurchCommunityById(index: number, item: IChurchCommunity) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
