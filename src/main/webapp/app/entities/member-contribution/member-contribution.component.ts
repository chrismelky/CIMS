import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IMemberContribution } from 'app/shared/model/member-contribution.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MemberContributionService } from './member-contribution.service';
import { IMember } from 'app/shared/model/member.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'church-member-contribution',
  templateUrl: './member-contribution.component.html'
})
export class MemberContributionComponent implements OnInit, OnDestroy {
  currentAccount: any;
  memberContributions: IMemberContribution[];
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
  member: IMember;
  selectedContributionId: any;

  constructor(
    protected memberContributionService: MemberContributionService,
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
    this.memberContributionService
      .query({
        'memberId.equals': this.member.id,
        'churchId.equals': this.member.church.id
      })
      .subscribe((res: HttpResponse<IMemberContribution[]>) => this.paginateMemberContributions(res.body, res.headers));
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
    this.registerChangeInMemberContributions();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IMemberContribution) {
    return item.id;
  }

  registerChangeInMemberContributions() {
    this.eventSubscriber = this.eventManager.subscribe('memberContributionListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateMemberContributions(data: IMemberContribution[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.memberContributions = data;
  }

  openDeleteDialog(content, id) {
    this.selectedContributionId = id;
    this.deleteModal.open(content, { backdrop: false }).result.then(
      r => {
        this.memberContributionService.delete(this.selectedContributionId).subscribe(resp => {
          this.selectedContributionId = undefined;
          this.loadAll();
        });
      },
      d => {
        this.selectedContributionId = undefined;
      }
    );
  }
}
