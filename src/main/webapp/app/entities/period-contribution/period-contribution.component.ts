import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeriodContribution } from 'app/shared/model/period-contribution.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PeriodContributionService } from './period-contribution.service';
import { PeriodContributionDeleteDialogComponent } from './period-contribution-delete-dialog.component';

@Component({
  selector: 'church-period-contribution',
  templateUrl: './period-contribution.component.html'
})
export class PeriodContributionComponent implements OnInit, OnDestroy {
  periodContributions?: IPeriodContribution[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected periodContributionService: PeriodContributionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.periodContributionService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPeriodContribution[]>) => this.onSuccess(res.body, res.headers, pageToLoad), () => this.onError());
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInPeriodContributions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPeriodContribution): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPeriodContributions(): void {
    this.eventSubscriber = this.eventManager.subscribe('periodContributionListModification', () => this.loadPage());
  }

  delete(periodContribution: IPeriodContribution): void {
    const modalRef = this.modalService.open(PeriodContributionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.periodContribution = periodContribution;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IPeriodContribution[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/period-contribution'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.periodContributions = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
