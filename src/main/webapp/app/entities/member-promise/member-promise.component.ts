import { Component, OnInit, OnDestroy, Input, AfterViewInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMemberPromise } from 'app/shared/model/member-promise.model';

import { MemberPromiseService } from './member-promise.service';
import { MemberPromiseDeleteDialogComponent } from './member-promise-delete-dialog.component';
import { IFinancialYear } from 'app/shared/model/financial-year.model';
import { FinancialYearService } from '../financial-year/financial-year.service';

@Component({
  selector: 'church-member-promise',
  templateUrl: './member-promise.component.html'
})
export class MemberPromiseComponent implements OnInit, OnDestroy {
  @Input() typeId: number;
  @Input() periodTypeId: number;
  memberId: number;
  churchId: number;

  memberPromise?: IMemberPromise;
  eventSubscriber?: Subscription;

  fys: IFinancialYear[] = [];
  fy: IFinancialYear;

  constructor(
    protected memberPromiseService: MemberPromiseService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected fyService: FinancialYearService
  ) {}

  loadPromise(): void {
    if (this.churchId === undefined || this.memberId === undefined || this.fy === undefined || this.typeId === undefined) {
      return;
    }
    this.memberPromiseService
      .getOne(this.churchId, this.memberId, this.typeId, this.fy.id)
      .subscribe((res: HttpResponse<IMemberPromise>) => (this.memberPromise = res.body), () => this.onError());
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
        this.loadPromise();
        // this.loadPeriods();
      });
  }

  setFy() {
    this.loadPromise();
  }

  ngOnInit(): void {
    this.churchId = this.activatedRoute.snapshot.params['churchId'];
    this.memberId = this.activatedRoute.snapshot.params['id'];
    this.loadFy();
    this.registerChangeInMemberPromises();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMemberPromise): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMemberPromises(): void {
    this.eventSubscriber = this.eventManager.subscribe('memberPromiseListModification', () => this.loadPromise());
  }

  delete(memberPromise: IMemberPromise): void {
    const modalRef = this.modalService.open(MemberPromiseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.memberPromise = memberPromise;
  }

  protected onError(): void {}

  formatDate(date) {
    const d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }
}
