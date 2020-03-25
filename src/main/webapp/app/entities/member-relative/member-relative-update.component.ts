import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IMemberRelative, MemberRelative } from 'app/shared/model/member-relative.model';
import { MemberRelativeService } from './member-relative.service';
import { IMember } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';

@Component({
  selector: 'church-member-relative-update',
  templateUrl: './member-relative-update.component.html'
})
export class MemberRelativeUpdateComponent implements OnInit {
  isSaving: boolean;

  members: IMember[];
  member: IMember;

  editForm = this.fb.group({
    id: [],
    relativeType: [],
    relativeFullName: [],
    relativePhoneNumber: [],
    member: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected memberRelativeService: MemberRelativeService,
    protected memberService: MemberService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ memberRelative, member }) => {
      this.member = member;
      this.updateForm(memberRelative);
      this.editForm.patchValue({
        member: this.member
      });
      if (this.member !== null) {
        this.members = [this.member];
      }
    });
  }

  updateForm(memberRelative: IMemberRelative) {
    this.editForm.patchValue({
      id: memberRelative.id,
      relativeType: memberRelative.relativeType,
      relativeFullName: memberRelative.relativeFullName,
      relativePhoneNumber: memberRelative.relativePhoneNumber,
      member: memberRelative.member
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const memberRelative = this.createFromForm();
    if (memberRelative.id !== undefined) {
      this.subscribeToSaveResponse(this.memberRelativeService.update(memberRelative));
    } else {
      this.subscribeToSaveResponse(this.memberRelativeService.create(memberRelative));
    }
  }

  private createFromForm(): IMemberRelative {
    return {
      ...new MemberRelative(),
      id: this.editForm.get(['id']).value,
      relativeType: this.editForm.get(['relativeType']).value,
      relativeFullName: this.editForm.get(['relativeFullName']).value,
      relativePhoneNumber: this.editForm.get(['relativePhoneNumber']).value,
      member: this.editForm.get(['member']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMemberRelative>>) {
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
}
