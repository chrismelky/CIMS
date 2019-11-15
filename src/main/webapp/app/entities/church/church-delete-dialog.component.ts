import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from './church.service';

@Component({
  selector: 'church-church-delete-dialog',
  templateUrl: './church-delete-dialog.component.html'
})
export class ChurchDeleteDialogComponent {
  church: IChurch;

  constructor(protected churchService: ChurchService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.churchService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'churchListModification',
        content: 'Deleted an church'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-church-delete-popup',
  template: ''
})
export class ChurchDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ church }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChurchDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.church = church;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/church', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/church', { outlets: { popup: null } }]);
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
