import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMemberRite, MemberRite } from 'app/shared/model/member-rite.model';
import { MemberRiteService } from './member-rite.service';
import { IRite } from 'app/shared/model/rite.model';
import { RiteService } from 'app/entities/rite/rite.service';
import { IMember } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';

@Component({
  selector: 'church-member-rite-update',
  templateUrl: './member-rite-update.component.html'
})
export class MemberRiteUpdateComponent implements OnInit {
  isSaving: boolean;

  rites: IRite[];

  members: IMember[];
  member: IMember;

  churches: IChurch[];
  dateReceivedDp: any;

  editForm = this.fb.group({
    id: [],
    dateReceived: [null, [Validators.required]],
    rite: [null, Validators.required],
    member: [],
    church: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected memberRiteService: MemberRiteService,
    protected riteService: RiteService,
    protected memberService: MemberService,
    protected churchService: ChurchService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ memberRite, member }) => {
      this.member = member;
      this.updateForm(memberRite);
      this.editForm.patchValue({
        member: this.member
      });
      if (this.member !== null) {
        this.members = [this.member];
      }
    });
    this.riteService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IRite[]>) => mayBeOk.ok),
        map((response: HttpResponse<IRite[]>) => response.body)
      )
      .subscribe((res: IRite[]) => (this.rites = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.churchService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChurch[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurch[]>) => response.body)
      )
      .subscribe((res: IChurch[]) => (this.churches = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(memberRite: IMemberRite) {
    this.editForm.patchValue({
      id: memberRite.id,
      dateReceived: memberRite.dateReceived,
      rite: memberRite.rite,
      member: memberRite.member,
      church: memberRite.church
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const memberRite = this.createFromForm();
    if (memberRite.id !== undefined) {
      this.subscribeToSaveResponse(this.memberRiteService.update(memberRite));
    } else {
      this.subscribeToSaveResponse(this.memberRiteService.create(memberRite));
    }
  }

  private createFromForm(): IMemberRite {
    return {
      ...new MemberRite(),
      id: this.editForm.get(['id']).value,
      dateReceived: this.editForm.get(['dateReceived']).value,
      rite: this.editForm.get(['rite']).value,
      member: this.editForm.get(['member']).value,
      church: this.editForm.get(['church']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMemberRite>>) {
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

  trackRiteById(index: number, item: IRite) {
    return item.id;
  }

  trackMemberById(index: number, item: IMember) {
    return item.id;
  }

  trackChurchById(index: number, item: IChurch) {
    return item.id;
  }
}
