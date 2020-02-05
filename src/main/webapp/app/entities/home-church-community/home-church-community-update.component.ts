import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IHomeChurchCommunity, HomeChurchCommunity } from 'app/shared/model/home-church-community.model';
import { HomeChurchCommunityService } from './home-church-community.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';
import { IMember } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';

@Component({
  selector: 'church-home-church-community-update',
  templateUrl: './home-church-community-update.component.html'
})
export class HomeChurchCommunityUpdateComponent implements OnInit {
  isSaving: boolean;

  churches: IChurch[];
  church: IChurch;

  members: IMember[];
  homeChurchCommunity: IHomeChurchCommunity;
  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    numberOfHouseHold: [],
    phoneNumber: [],
    address: [],
    church: [null, Validators.required],
    chairMan: [],
    secreatry: [],
    treasurer: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected homeChurchCommunityService: HomeChurchCommunityService,
    protected churchService: ChurchService,
    protected memberService: MemberService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ homeChurchCommunity, church }) => {
      this.church = church;
      this.updateForm(homeChurchCommunity);
      this.homeChurchCommunity = homeChurchCommunity;
      this.loadMembers();
    });
  }

  loadMembers() {
    const filterBy = { 'churchId.equals': this.church.id };
    if (this.homeChurchCommunity.id) {
      filterBy['homeChurchCommunityId.equals'] = this.homeChurchCommunity.id;
    } else {
      filterBy['homeChurchCommunityId.specified'] = false;
    }
    this.memberService
      .query(filterBy)
      .pipe(
        filter((mayBeOk: HttpResponse<IMember[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMember[]>) => response.body)
      )
      .subscribe(
        (res: IMember[]) =>
          (this.members = res.map(m => {
            return { id: m.id, firstName: m.firstName, lastName: m.lastName };
          })),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(comm: IHomeChurchCommunity) {
    this.editForm.patchValue({
      id: comm.id,
      name: comm.name,
      numberOfHouseHold: comm.numberOfHouseHold,
      phoneNumber: comm.phoneNumber,
      address: comm.address,
      church: { id: this.church.id },
      chairMan: comm.chairMan ? { id: comm.chairMan.id, firstName: comm.chairMan.firstName, lastName: comm.chairMan.lastName } : null,
      secreatry: comm.secreatry ? { id: comm.secreatry.id, firstName: comm.secreatry.firstName, lastName: comm.secreatry.lastName } : null,
      treasurer: comm.treasurer ? { id: comm.treasurer.id, firstName: comm.treasurer.firstName, lastName: comm.treasurer.lastName } : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const homeChurchCommunity = this.createFromForm();
    if (homeChurchCommunity.id !== undefined) {
      this.subscribeToSaveResponse(this.homeChurchCommunityService.update(homeChurchCommunity));
    } else {
      this.subscribeToSaveResponse(this.homeChurchCommunityService.create(homeChurchCommunity));
    }
  }

  private createFromForm(): IHomeChurchCommunity {
    return {
      ...new HomeChurchCommunity(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      numberOfHouseHold: this.editForm.get(['numberOfHouseHold']).value,
      phoneNumber: this.editForm.get(['phoneNumber']).value,
      address: this.editForm.get(['address']).value,
      church: this.editForm.get(['church']).value,
      chairMan: this.editForm.get(['chairMan']).value,
      secreatry: this.editForm.get(['secreatry']).value,
      treasurer: this.editForm.get(['treasurer']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHomeChurchCommunity>>) {
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

  trackMemberById(index: number, item: IMember) {
    return item.id;
  }
}
