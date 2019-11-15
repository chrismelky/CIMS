import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRite } from 'app/shared/model/rite.model';
import { RiteService } from './rite.service';

@Component({
  selector: 'church-rite-delete-dialog',
  templateUrl: './rite-delete-dialog.component.html'
})
export class RiteDeleteDialogComponent {
  rite: IRite;

  constructor(protected riteService: RiteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.riteService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'riteListModification',
        content: 'Deleted an rite'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-rite-delete-popup',
  template: ''
})
export class RiteDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ rite }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RiteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.rite = rite;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/rite', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/rite', { outlets: { popup: null } }]);
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
