import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IChurchCommunity } from 'app/shared/model/church-community.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ChurchCommunityService } from './church-community.service';

@Component({
  selector: 'church-church-community',
  templateUrl: './church-community.component.html'
})
export class ChurchCommunityComponent implements OnInit, OnDestroy {
  currentAccount: any;
  churchCommunities: IChurchCommunity[];
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
  churchId: number;

  constructor(
    protected churchCommunityService: ChurchCommunityService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
  }

  loadAll() {
    if (this.churchId === undefined) {
      return;
    }
    this.churchCommunityService
      .query({
        'churchId.equals': this.churchId
      })
      .subscribe((res: HttpResponse<IChurchCommunity[]>) => this.paginateChurchCommunities(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
    }
  }

  transition() {
    this.router.navigate(['/church-community'], {
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
      '/church-community',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.churchId = this.activatedRoute.snapshot.params['id'];
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInChurchCommunities();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IChurchCommunity) {
    return item.id;
  }

  registerChangeInChurchCommunities() {
    this.eventSubscriber = this.eventManager.subscribe('churchCommunityListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateChurchCommunities(data: IChurchCommunity[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.churchCommunities = data;
  }
}
