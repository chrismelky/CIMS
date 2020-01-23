import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IChurchActivity } from 'app/shared/model/church-activity.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ChurchActivityService } from './church-activity.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'church-church-activity',
  templateUrl: './church-activity.component.html'
})
export class ChurchActivityComponent implements OnInit, OnDestroy {
  currentAccount: any;
  churchActivities: IChurchActivity[];
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
    protected churchActivityService: ChurchActivityService,
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
    if (this.churchId === undefined) {
      return;
    }
    this.churchActivityService
      .query({
        'churchId.equals': this.churchId
      })
      .subscribe((res: HttpResponse<IChurchActivity[]>) => this.paginateChurchActivities(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
    }
  }

  transition() {
    this.router.navigate(['/church-activity'], {
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
      '/church-activity',
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
    this.registerChangeInChurchActivities();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IChurchActivity) {
    return item.id;
  }

  registerChangeInChurchActivities() {
    this.eventSubscriber = this.eventManager.subscribe('churchActivityListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateChurchActivities(data: IChurchActivity[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.churchActivities = data;
  }

  openDeleteDialog(content, id) {
    this.deleteModal.open(content, { backdrop: false }).result.then(
      r => {
        this.churchActivityService.delete(id).subscribe(resp => {
          this.loadAll();
        });
      },
      d => {}
    );
  }
}
