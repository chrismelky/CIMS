import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IPeriodContribution } from 'app/shared/model/period-contribution.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PeriodContributionService } from './period-contribution.service';
import { PeriodService } from 'app/entities/period/period.service';
import { IPeriod, Period } from 'app/shared/model/period.model';
import { IFinancialYear } from 'app/shared/model/financial-year.model';
import { FinancialYearService } from 'app/entities/financial-year/financial-year.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'church-period-contribution',
  templateUrl: './period-contribution.component.html'
})
export class PeriodContributionComponent implements OnInit, OnDestroy {
  @Input() periodContributionTypeId: number;
  @Input() memberId: number;
  @Input() churchId: number;
  @Input() periodTypeId: number;

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
  period: IPeriod;
  periods: Period[] = [];
  progress = 0;

  fys: IFinancialYear[] = [];
  fy;

  maxdate = {};

  constructor(
    protected periodContributionService: PeriodContributionService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected periodService: PeriodService,
    private fyService: FinancialYearService,
    protected deleteModal: NgbModal
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
  }

  loadAll() {
    if (this.period === undefined) {
      return;
    }
    this.periodContributionService
      .query({
        'periodContributionTypeId.equals': this.periodContributionTypeId,
        'memberId.equals': this.memberId,
        'churchId.equals': this.churchId,
        'periodId.equals': this.period.id
      })
      .subscribe((res: HttpResponse<IPeriodContribution[]>) => this.paginatePeriodContributions(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
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

  loadFy() {
    this.fyService
      .query({
        'startDate.lessOrEqualThan': this.formatDate(new Date()),
        sort: ['startDate,desc']
      })
      .subscribe(resp => {
        const all = resp.body;
        if (all.length > 2) {
          this.fys = [all[0], all[1]];
        } else {
          this.fys = all;
        }
        if (this.fys.length) {
          this.fy = this.fys[0];
        }
        this.loadPeriods();
      });
  }

  loadPeriods() {
    this.periods = [];
    if (this.periodTypeId === undefined || this.fy === undefined) {
      return;
    }
    this.periodService
      .query({
        'typeId.equals': this.periodTypeId,
        'financialYearId.equals': this.fy.id,
        'startDate.lessOrEqualThan': this.formatDate(new Date()),
        sort: ['startDate,desc']
      })
      .subscribe(resp => {
        const periods = resp.body;
        // take max of two
        this.periods = periods.length > 2 ? [periods[0], periods[1]] : periods;
        if (this.periods.length) {
          this.period = this.periods[0];
          this.loadAll();
        }
      });
  }

  ngOnInit() {
    this.loadFy();
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
    if (data.length) {
      this.progress = Math.floor((data[0].amountContributed / data[0].amountPromised) * 100);
    }
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.periodContributions = data;
  }

  itemChange(changed) {
    if (changed) {
      this.loadAll();
    }
  }

  formatDate(date) {
    const d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }

  setFy() {
    this.periodContributions = [];
    this.loadPeriods();
  }

  openDeleteDialog(content, id: number) {
    this.deleteModal.open(content, { backdrop: false }).result.then(
      r => {
        this.periodContributionService.delete(id).subscribe(resp => {
          this.loadAll();
        });
      },
      d => {}
    );
  }
}
