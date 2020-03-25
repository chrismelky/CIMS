import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChuchService } from 'app/shared/model/chuch-service.model';
import { ChuchServiceService } from './chuch-service.service';

@Component({
  selector: 'church-chuch-service-delete-dialog',
  templateUrl: './chuch-service-delete-dialog.component.html'
})
export class ChuchServiceDeleteDialogComponent {
  chuchService: IChuchService;

  constructor(
    protected chuchServiceService: ChuchServiceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.chuchServiceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'chuchServiceListModification',
        content: 'Deleted an chuchService'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-chuch-service-delete-popup',
  template: ''
})
export class ChuchServiceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chuchService }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChuchServiceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.chuchService = chuchService;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/chuch-service', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/chuch-service', { outlets: { popup: null } }]);
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
