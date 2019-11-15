import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMemberPromise } from 'app/shared/model/member-promise.model';
import { MemberPromiseService } from './member-promise.service';

@Component({
  selector: 'church-member-promise-delete-dialog',
  templateUrl: './member-promise-delete-dialog.component.html'
})
export class MemberPromiseDeleteDialogComponent {
  memberPromise: IMemberPromise;

  constructor(
    protected memberPromiseService: MemberPromiseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.memberPromiseService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'memberPromiseListModification',
        content: 'Deleted an memberPromise'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-member-promise-delete-popup',
  template: ''
})
export class MemberPromiseDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ memberPromise }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MemberPromiseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.memberPromise = memberPromise;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/member-promise', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/member-promise', { outlets: { popup: null } }]);
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
