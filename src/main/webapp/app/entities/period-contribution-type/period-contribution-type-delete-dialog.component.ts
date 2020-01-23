import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriodContributionType } from 'app/shared/model/period-contribution-type.model';
import { PeriodContributionTypeService } from './period-contribution-type.service';

@Component({
  selector: 'church-period-contribution-type-delete-dialog',
  templateUrl: './period-contribution-type-delete-dialog.component.html'
})
export class PeriodContributionTypeDeleteDialogComponent {
  periodContributionType: IPeriodContributionType;

  constructor(
    protected periodContributionTypeService: PeriodContributionTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.periodContributionTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'periodContributionTypeListModification',
        content: 'Deleted an periodContributionType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-period-contribution-type-delete-popup',
  template: ''
})
export class PeriodContributionTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ periodContributionType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PeriodContributionTypeDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.periodContributionType = periodContributionType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/period-contribution-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/period-contribution-type', { outlets: { popup: null } }]);
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
