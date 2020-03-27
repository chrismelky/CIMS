import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IPeriodContributionItem } from 'app/shared/model/period-contribution-item.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PeriodContributionItemService } from './period-contribution-item.service';
import { NgbModal, NgbDateStruct, NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { SessionStorageService } from 'ngx-webstorage';

@Component({
  selector: 'church-period-contribution-item',
  templateUrl: './period-contribution-item.component.html'
})
export class PeriodContributionItemComponent implements OnInit, OnDestroy {
  @Input() periodContributionId: number;

  currentAccount: any;
  periodContributionItems: IPeriodContributionItem[];
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
  @Output() itemChange: EventEmitter<boolean> = new EventEmitter();
  maxdate = {};
  byDate: boolean;
  selectedDate;
  showDatePicker = true;

  constructor(
    protected periodContributionItemService: PeriodContributionItemService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected deleteModal: NgbModal,
    protected sessionStorage: SessionStorageService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    const today = new Date();
    this.maxdate = { year: today.getFullYear(), month: today.getMonth() + 1, day: today.getDate() };
    this.selectedDate = sessionStorage.retrieve('piDate');
    this.byDate = sessionStorage.retrieve('piByDate');
  }

  isDisabled = (date: NgbDate, current: { month: number }) => !this.byDate;

  loadAll() {
    const dateFilter = this.selectedDate ? { 'dateReceived.equals': this.selectedDate } : {};
    this.periodContributionItemService
      .query({
        'periodContributionId.equals': this.periodContributionId,
        ...dateFilter
      })
      .subscribe((res: HttpResponse<IPeriodContributionItem[]>) => this.paginatePeriodContributionItems(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
    }
  }

  transition() {
    this.router.navigate(['/period-contribution-item'], {
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
      '/period-contribution-item',
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
    this.registerChangeInPeriodContributionItems();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPeriodContributionItem) {
    return item.id;
  }

  registerChangeInPeriodContributionItems() {
    this.eventSubscriber = this.eventManager.subscribe('periodContributionItemListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  onDateSelect(date: NgbDateStruct) {
    const m = date.month < 10 ? '0' + date.month : date.month;
    const d = date.day < 10 ? '0' + date.day : date.day;
    this.selectedDate = date.year + '-' + m + '-' + d;
    this.loadAll();
    this.sessionStorage.store('piDate', this.selectedDate);
  }

  setPicker(byDate: boolean) {
    this.byDate = byDate;
    this.sessionStorage.store('piByDate', byDate);
    this.isDisabled = (date: NgbDate, current: { month: number }) => !this.byDate;
    if (!byDate) {
      this.selectedDate = undefined;
      this.loadAll();
      this.showDatePicker = false;
      this.sessionStorage.clear('piDate');
      setTimeout(() => {
        this.showDatePicker = true;
      }, 100);
    }
  }

  protected paginatePeriodContributionItems(data: IPeriodContributionItem[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.periodContributionItems = data;
  }

  openDeleteDialog(content, id) {
    this.deleteModal.open(content, { backdrop: false }).result.then(
      r => {
        this.periodContributionItemService.delete(id).subscribe(resp => {
          this.loadAll();
          this.itemChange.emit(true);
        });
      },
      d => {}
    );
  }
}
