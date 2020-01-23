import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line
// @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IPeriodContribution } from 'app/shared/model/period-contribution.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PeriodContributionService } from './period-contribution.service';

@Component({
  selector: 'church-period-contribution',
  templateUrl: './period-contribution.component.html'
})
export class PeriodContributionComponent implements OnInit, OnDestroy {
  @Input() periodContributionTypeId: number;
  @Input() memberId: number;
  @Input() churchId: number;
  @Input() periodId: number;

  currentAccount: any;
  periodContributions: IPeriodContribution[];
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
    protected periodContributionService: PeriodContributionService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
  }

  loadAll() {
    this.periodContributionService
      .query({
        'periodContributionTypeId.equals': this.periodContributionTypeId,
        'memberId.equals': this.memberId,
        'churchId.equals': this.churchId,
        'periodId.equals': this.periodId
      })
      .subscribe((res: HttpResponse<IPeriodContribution[]>) => this.paginatePeriodContributions(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      //  this.transition();
    }
  }

  transition() {
    this.router.navigate(['/period-contribution'], {
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
      '/period-contribution',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInPeriodContributions();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPeriodContribution) {
    return item.id;
  }

  registerChangeInPeriodContributions() {
    this.eventSubscriber = this.eventManager.subscribe('periodContributionListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePeriodContributions(data: IPeriodContribution[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.periodContributions = data;
  }

  itemChange(changed) {
    if (changed) {
      this.loadAll();
    }
  }
}
