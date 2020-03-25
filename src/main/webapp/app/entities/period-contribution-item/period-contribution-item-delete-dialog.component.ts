import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriodContributionItem } from 'app/shared/model/period-contribution-item.model';
import { PeriodContributionItemService } from './period-contribution-item.service';

@Component({
  selector: 'church-period-contribution-item-delete-dialog',
  templateUrl: './period-contribution-item-delete-dialog.component.html'
})
export class PeriodContributionItemDeleteDialogComponent {
  periodContributionItem: IPeriodContributionItem;

  constructor(
    protected periodContributionItemService: PeriodContributionItemService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.periodContributionItemService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'periodContributionItemListModification',
        content: 'Deleted an periodContributionItem'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-period-contribution-item-delete-popup',
  template: ''
})
export class PeriodContributionItemDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ periodContributionItem }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PeriodContributionItemDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.periodContributionItem = periodContributionItem;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/period-contribution-item', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/period-contribution-item', { outlets: { popup: null } }]);
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
