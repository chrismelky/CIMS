import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { JhiLanguageHelper } from 'app/core/language/language.helper';
import { User } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { Member } from 'app/shared/model/member.model';
import { ChurchService } from 'app/entities/church/church.service';
import { IChurch } from 'app/shared/model/church.model';

@Component({
  selector: 'church-user-mgmt-update',
  templateUrl: './user-management-update.component.html'
})
export class UserManagementUpdateComponent implements OnInit {
  user: User;
  languages: any[];
  authorities: any[];
  isSaving: boolean;
  churches: IChurch[] = [];
  church: IChurch;

  editForm = this.fb.group({
    id: [null],
    login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[_.@A-Za-z0-9-]*')]],
    firstName: ['', [Validators.maxLength(50)]],
    lastName: ['', [Validators.maxLength(50)]],
    email: ['', [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    activated: [true],
    langKey: [],
    authorities: [],
    member: [],
    church: []
  });

  constructor(
    private languageHelper: JhiLanguageHelper,
    private userService: UserService,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private churchService: ChurchService
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.route.data.subscribe(({ user, church }) => {
      this.user = user.body ? user.body : user;
      this.church = church;
      this.updateForm(this.user);
    });
    this.authorities = [];
    this.userService.authorities().subscribe(authorities => {
      this.authorities = authorities;
    });
    this.languages = this.languageHelper.getAll();
    this.loadChurches();
  }

  loadChurches() {
    if (this.church) {
      this.editForm.patchValue({
        church: { id: this.church.id, name: this.church.name }
      });
      this.churches.push(this.church);
      return;
    }
    this.churchService
      .query({
        size: 1000
      })
      .subscribe(resp => {
        this.churches = resp.body;
      });
  }

  private updateForm(user: User): void {
    this.editForm.patchValue({
      id: user.id,
      login: user.login,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      activated: user.activated,
      langKey: user.langKey,
      authorities: user.authorities,
      member: user.member,
      church: user.church
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    this.updateUser(this.user);
    if (this.user.id !== null) {
      this.userService.update(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
    } else {
      this.userService.create(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
    }
  }

  private updateUser(user: User): void {
    user.login = this.editForm.get(['login']).value;
    user.firstName = this.editForm.get(['firstName']).value;
    user.lastName = this.editForm.get(['lastName']).value;
    user.email = this.editForm.get(['email']).value;
    user.activated = this.editForm.get(['activated']).value;
    user.langKey = this.editForm.get(['langKey']).value;
    user.authorities = this.editForm.get(['authorities']).value;
    user.member = this.editForm.get(['member']).value;
    user.church = this.editForm.get(['church']).value;
  }

  private onSaveSuccess(result) {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }

  setMember(member: Member) {
    console.error(member);
    this.editForm.patchValue({
      member
    });
  }

  trackChurchById(index: number, item: IChurch) {
    return item.id;
  }
}
