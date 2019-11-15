import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChurchType } from 'app/shared/model/church-type.model';
import { ChurchTypeService } from './church-type.service';

@Component({
  selector: 'church-church-type-delete-dialog',
  templateUrl: './church-type-delete-dialog.component.html'
})
export class ChurchTypeDeleteDialogComponent {
  churchType: IChurchType;

  constructor(
    protected churchTypeService: ChurchTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.churchTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'churchTypeListModification',
        content: 'Deleted an churchType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-church-type-delete-popup',
  template: ''
})
export class ChurchTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ churchType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChurchTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.churchType = churchType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/church-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/church-type', { outlets: { popup: null } }]);
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
