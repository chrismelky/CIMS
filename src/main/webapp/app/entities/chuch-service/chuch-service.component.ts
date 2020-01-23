import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';

import { IChuchService } from 'app/shared/model/chuch-service.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ChuchServiceService } from './chuch-service.service';
import { IChurch } from 'app/shared/model/church.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'church-chuch-service',
  templateUrl: './chuch-service.component.html'
})
export class ChuchServiceComponent implements OnInit, OnDestroy {
  currentAccount: any;
  church: IChurch;
  chuchServices: IChuchService[];
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
    protected chuchServiceService: ChuchServiceService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected deleteModal: NgbModal
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
  }

  loadAll() {
    this.chuchServiceService
      .query({
        'churchId.equals': this.churchId
      })
      .subscribe((res: HttpResponse<IChuchService[]>) => this.paginateChuchServices(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
    }
  }

  transition() {
    this.router.navigate(['/chuch-service'], {
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
      '/chuch-service',
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
    this.registerChangeInChuchServices();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IChuchService) {
    return item.id;
  }

  registerChangeInChuchServices() {
    this.eventSubscriber = this.eventManager.subscribe('chuchServiceListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateChuchServices(data: IChuchService[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.chuchServices = data;
  }

  openDeleteDialog(content, id) {
    this.deleteModal.open(content, { backdrop: false }).result.then(
      r => {
        this.chuchServiceService.delete(id).subscribe(resp => {
          this.loadAll();
        });
      },
      d => {}
    );
  }
}
