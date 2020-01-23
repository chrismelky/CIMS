import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IMemberRelative } from 'app/shared/model/member-relative.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MemberRelativeService } from './member-relative.service';
import { IMember } from 'app/shared/model/member.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'church-member-relative',
  templateUrl: './member-relative.component.html'
})
export class MemberRelativeComponent implements OnInit, OnDestroy {
  currentAccount: any;
  member: IMember;
  memberRelatives: IMemberRelative[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  constructor(
    protected memberRelativeService: MemberRelativeService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected deleteModal: NgbModal
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
  }

  loadAll() {
    if (this.member.id === undefined) {
      return;
    }
    this.memberRelativeService
      .query({
        'memberId.equals': this.member.id
      })
      .subscribe((res: HttpResponse<IMemberRelative[]>) => this.paginateMemberRelatives(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
    }
  }

  transition() {
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.loadAll();
  }

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.member = member;
      this.loadAll();
    });
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInMemberRelatives();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IMemberRelative) {
    return item.id;
  }

  registerChangeInMemberRelatives() {
    this.eventSubscriber = this.eventManager.subscribe('memberRelativeListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateMemberRelatives(data: IMemberRelative[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.memberRelatives = data;
  }

  openDeleteDialog(content, id) {
    this.deleteModal.open(content, { backdrop: false }).result.then(
      r => {
        this.memberRelativeService.delete(id).subscribe(resp => {
          this.loadAll();
        });
      },
      d => {}
    );
  }
}
