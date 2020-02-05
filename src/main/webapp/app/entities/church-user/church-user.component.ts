import { Component, OnInit } from '@angular/core';
import { IUser, User } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { UserManagementDeleteDialogComponent } from 'app/admin/user-management/user-management-delete-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'church-church-user',
  templateUrl: './church-user.component.html',
  styleUrls: ['./church-user.component.scss']
})
export class ChurchUserComponent implements OnInit {
  users: IUser[] = [];
  error: any;
  success: any;
  churchId: number;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  currentAccount;

  constructor(
    protected userService: UserService,
    private activateRoute: ActivatedRoute,
    private alertService: JhiAlertService,
    private parseLinks: JhiParseLinks,
    private modalService: NgbModal,
    private accountService: AccountService
  ) {}

  ngOnInit() {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
      this.loadAll();
    });
  }

  private loadAll() {
    this.churchId = this.activateRoute.snapshot.params['id'];
    this.userService.byChurch(this.churchId).subscribe(resp => this.onSuccess(resp.body, resp.headers), resp => this.onError(resp.body));
  }

  setActive(user, isActivated) {
    user.activated = isActivated;

    this.userService.update(user).subscribe(
      response => {
        this.error = null;
        this.success = 'OK';
        this.loadAll();
      },
      () => {
        this.success = null;
        this.error = 'ERROR';
      }
    );
  }

  trackIdentity(index, item: User) {
    return item.id;
  }

  private onSuccess(data, headers) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.users = data;
  }

  private onError(error) {
    this.alertService.error(error.error, error.message, null);
  }

  deleteUser(user: User) {
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
  }
}
