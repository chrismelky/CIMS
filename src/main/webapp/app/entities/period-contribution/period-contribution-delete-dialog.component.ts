import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriodContribution } from 'app/shared/model/period-contribution.model';
import { PeriodContributionService } from './period-contribution.service';

@Component({
  selector: 'church-period-contribution-delete-dialog',
  templateUrl: './period-contribution-delete-dialog.component.html'
})
export class PeriodContributionDeleteDialogComponent {
  periodContribution: IPeriodContribution;

  constructor(
    protected periodContributionService: PeriodContributionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.periodContributionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'periodContributionListModification',
        content: 'Deleted an periodContribution'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-period-contribution-delete-popup',
  template: ''
})
export class PeriodContributionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ periodContribution }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PeriodContributionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.periodContribution = periodContribution;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/period-contribution', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/period-contribution', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
