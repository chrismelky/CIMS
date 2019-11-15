import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IChurchCommunity, ChurchCommunity } from 'app/shared/model/church-community.model';
import { ChurchCommunityService } from './church-community.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';
import { IMember } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';

@Component({
  selector: 'church-church-community-update',
  templateUrl: './church-community-update.component.html'
})
export class ChurchCommunityUpdateComponent implements OnInit {
  isSaving: boolean;

  churches: IChurch[];

  members: IMember[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    church: [null, Validators.required],
    chairPerson: [],
    secretary: [],
    treasurer: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected churchCommunityService: ChurchCommunityService,
    protected churchService: ChurchService,
    protected memberService: MemberService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ churchCommunity }) => {
      this.updateForm(churchCommunity);
    });
    this.churchService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChurch[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurch[]>) => response.body)
      )
      .subscribe((res: IChurch[]) => (this.churches = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.memberService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMember[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMember[]>) => response.body)
      )
      .subscribe((res: IMember[]) => (this.members = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(churchCommunity: IChurchCommunity) {
    this.editForm.patchValue({
      id: churchCommunity.id,
      name: churchCommunity.name,
      church: churchCommunity.church,
      chairPerson: churchCommunity.chairPerson,
      secretary: churchCommunity.secretary,
      treasurer: churchCommunity.treasurer
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const churchCommunity = this.createFromForm();
    if (churchCommunity.id !== undefined) {
      this.subscribeToSaveResponse(this.churchCommunityService.update(churchCommunity));
    } else {
      this.subscribeToSaveResponse(this.churchCommunityService.create(churchCommunity));
    }
  }

  private createFromForm(): IChurchCommunity {
    return {
      ...new ChurchCommunity(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      church: this.editForm.get(['church']).value,
      chairPerson: this.editForm.get(['chairPerson']).value,
      secretary: this.editForm.get(['secretary']).value,
      treasurer: this.editForm.get(['treasurer']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChurchCommunity>>) {
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
