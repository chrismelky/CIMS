import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChurchActivity } from 'app/shared/model/church-activity.model';
import { ChurchActivityService } from './church-activity.service';

@Component({
  selector: 'church-church-activity-delete-dialog',
  templateUrl: './church-activity-delete-dialog.component.html'
})
export class ChurchActivityDeleteDialogComponent {
  churchActivity: IChurchActivity;

  constructor(
    protected churchActivityService: ChurchActivityService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.churchActivityService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'churchActivityListModification',
        content: 'Deleted an churchActivity'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-church-activity-delete-popup',
  template: ''
})
export class ChurchActivityDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ churchActivity }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChurchActivityDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.churchActivity = churchActivity;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/church-activity', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/church-activity', { outlets: { popup: null } }]);
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
