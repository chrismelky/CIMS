import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMember, Member } from 'app/shared/model/member.model';
import { MemberService } from './member.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';
import { IChurchCommunity } from 'app/shared/model/church-community.model';
import { ChurchCommunityService } from 'app/entities/church-community/church-community.service';
import { NgbTabChangeEvent, NgbTabset } from '@ng-bootstrap/ng-bootstrap';
import { HomeChurchCommunityService } from 'app/entities/home-church-community/home-church-community.service';
import { IHomeChurchCommunity } from 'app/shared/model/home-church-community.model';
import { PeriodContributionTypeService } from 'app/entities/period-contribution-type/period-contribution-type.service';
import { IPeriodContributionType, PeriodContributionType } from 'app/shared/model/period-contribution-type.model';

@Component({
  selector: 'church-member-update',
  templateUrl: './member-update.component.html'
})
export class MemberUpdateComponent implements OnInit, AfterViewInit {
  isSaving: boolean;
  churchId: number;
  memberId: number;
  churches: IChurch[];

  @ViewChild('memberTab', { static: false }) tab: NgbTabset;

  churchCommunities: IChurchCommunity[];
  homeChurchCommunities: IHomeChurchCommunity[];
  periodContributionTypes: PeriodContributionType[];
  dateOfBirthDp: any;
  deceasedDateDp: any;

  editForm = this.fb.group({
    id: [],
    memberRn: [{ value: null, disabled: true }],
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
    churchCommunities: [],
    homeChurchCommunity: [null]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected memberService: MemberService,
    protected churchService: ChurchService,
    protected churchCommunityService: ChurchCommunityService,
    protected activatedRoute: ActivatedRoute,
    protected homeChurchCommunityService: HomeChurchCommunityService,
    protected periodContribTypeService: PeriodContributionTypeService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.churchId = this.activatedRoute.snapshot.params['churchId'];
    this.memberId = this.activatedRoute.snapshot.params['id'];
    this.activatedRoute.data.subscribe(({ member }) => {
      this.updateForm(member);
    });

    // If not church param passed fetch all churches
    if (this.churchId === undefined) {
      this.churchService
        .query()
        .pipe(
          filter((mayBeOk: HttpResponse<IChurch[]>) => mayBeOk.ok),
          map((response: HttpResponse<IChurch[]>) => response.body)
        )
        .subscribe((res: IChurch[]) => (this.churches = res), (res: HttpErrorResponse) => this.onError(res.message));
    } else {
      this.editForm.patchValue({
        church: { id: this.churchId }
      });

      this.loadChurchData(this.churchId);
    }
  }

  loadChurchData(churchId: number) {
    if (churchId === undefined) {
      return;
    }
    const byChurchId = { 'churchId.equals': churchId };

    this.loadHomeChurchCommunities(byChurchId);
    this.loadPeriodContribTypes(byChurchId);
    this.loadChurchService(byChurchId);
  }

  loadPeriodContribTypes(byChurchId: any) {
    this.periodContribTypeService
      .query(byChurchId)
      .pipe(
        filter((mayBeOk: HttpResponse<IPeriodContributionType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPeriodContributionType[]>) => response.body)
      )
      .subscribe(
        (res: IPeriodContributionType[]) => (this.periodContributionTypes = res),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadChurchService(byChurchId: any) {
    this.churchCommunityService
      .query(byChurchId)
      .pipe(
        filter((mayBeOk: HttpResponse<IChurchCommunity[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurchCommunity[]>) => response.body)
      )
      .subscribe((res: IChurchCommunity[]) => (this.churchCommunities = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  loadHomeChurchCommunities(byChurchId: any) {
    this.homeChurchCommunityService
      .query(byChurchId)
      .pipe(
        filter((mayBeOk: HttpResponse<IHomeChurchCommunity[]>) => mayBeOk.ok),
        map((response: HttpResponse<IHomeChurchCommunity[]>) => response.body)
      )
      .subscribe(
        (res: IHomeChurchCommunity[]) => (this.homeChurchCommunities = res),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(member: IMember) {
    this.editForm.patchValue({
      id: member.id,
      memberRn: member.memberRn,
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
      churchCommunities: member.churchCommunities,
      homeChurchCommunity: member.homeChurchCommunity
    });
  }

  previousState() {
    localStorage.setItem('memberActiveTab', 'basicInfo');
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
      memberRn: this.editForm.get(['memberRn']).value,
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
      churchCommunities: this.editForm.get(['churchCommunities']).value,
      homeChurchCommunity: this.editForm.get(['homeChurchCommunity']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMember>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    // this.previousState();
    // this.jhiAlertService.success("Member info updated", null, null);
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
  trackHomeChurchById(index: number, item: IHomeChurchCommunity) {
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

  setSelectedTab($event: NgbTabChangeEvent) {
    localStorage.setItem('memberActiveTab', $event.nextId);
  }

  ngAfterViewInit(): void {
    const activeTab = localStorage.getItem('memberActiveTab') || 'basicInfo';
    setTimeout(() => this.tab.select(activeTab), 100);
  }
}
