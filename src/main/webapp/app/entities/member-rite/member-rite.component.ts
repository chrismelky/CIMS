import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IMemberRite } from 'app/shared/model/member-rite.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MemberRiteService } from './member-rite.service';
import { IMember } from 'app/shared/model/member.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'church-member-rite',
  templateUrl: './member-rite.component.html'
})
export class MemberRiteComponent implements OnInit, OnDestroy {
  currentAccount: any;
  member: IMember;
  memberRites: IMemberRite[];
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
  selectedRiteId: any;

  constructor(
    protected memberRiteService: MemberRiteService,
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
    this.memberRiteService
      .query({
        'memberId.equals': this.member.id
      })
      .subscribe((res: HttpResponse<IMemberRite[]>) => this.paginateMemberRites(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
    }
  }

  transition() {
    this.router.navigate(['/member-rites'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/member-rite',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
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
    this.registerChangeInMemberRites();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IMemberRite) {
    return item.id;
  }

  registerChangeInMemberRites() {
    this.eventSubscriber = this.eventManager.subscribe('memberRiteListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateMemberRites(data: IMemberRite[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.memberRites = data;
  }

  openDeleteDialog(content, id) {
    this.deleteModal.open(content, { backdrop: false }).result.then(
      r => {
        this.memberRiteService.delete(id).subscribe(resp => {
          this.loadAll();
        });
      },
      d => {}
    );
  }
}
