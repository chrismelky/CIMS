import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriodType } from 'app/shared/model/period-type.model';
import { PeriodTypeService } from './period-type.service';

@Component({
  selector: 'church-period-type-delete-dialog',
  templateUrl: './period-type-delete-dialog.component.html'
})
export class PeriodTypeDeleteDialogComponent {
  periodType: IPeriodType;

  constructor(
    protected periodTypeService: PeriodTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.periodTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'periodTypeListModification',
        content: 'Deleted an periodType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-period-type-delete-popup',
  template: ''
})
export class PeriodTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ periodType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PeriodTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.periodType = periodType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/period-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/period-type', { outlets: { popup: null } }]);
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
