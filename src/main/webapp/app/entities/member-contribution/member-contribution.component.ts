import { Component, OnDestroy, OnInit, Input } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IMemberContribution } from 'app/shared/model/member-contribution.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MemberContributionService } from './member-contribution.service';
import { NgbModal, NgbDate, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { SessionStorageService } from 'ngx-webstorage';

@Component({
  selector: 'church-member-contribution',
  templateUrl: './member-contribution.component.html'
})
export class MemberContributionComponent implements OnInit, OnDestroy {
  @Input() memberPromiseId: number;

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
  selectedContributionId: any;

  maxdate = {};
  byDate: boolean;
  selectedDate;
  selectedPickerDate: NgbDateStruct;
  showDatePicker = true;

  constructor(
    protected memberContributionService: MemberContributionService,
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
    this.setPickerDate(this.selectedDate);
    this.byDate = sessionStorage.retrieve('piByDate');
  }
  isDisabled = (date: NgbDate, current: { month: number }) => !this.byDate;

  loadAll() {
    if (this.memberPromiseId === undefined) {
      return;
    }
    const dateFilter = this.selectedDate ? { paymentDate: this.selectedDate } : {};
    this.memberContributionService
      .findByPromise(this.memberPromiseId, dateFilter)
      .subscribe((res: HttpResponse<IMemberContribution[]>) => this.paginateMemberContributions(res.body, res.headers));
  }

  setPickerDate(_date) {
    if (_date === undefined) {
      return;
    }
    const date = new Date(_date);
    this.selectedPickerDate = { year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate() };
    this.showDatePicker = false;
    setTimeout(() => {
      this.showDatePicker = true;
    }, 100);
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
    }
  }

  onDateSelect(date: NgbDateStruct) {
    console.error(date);
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

  transition() {
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
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
          this.eventManager.broadcast({ name: 'memberPromiseListModification' });
        });
      },
      d => {
        this.selectedContributionId = undefined;
      }
    );
  }
}
