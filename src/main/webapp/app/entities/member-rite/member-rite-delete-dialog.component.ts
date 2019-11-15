import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMemberRite } from 'app/shared/model/member-rite.model';
import { MemberRiteService } from './member-rite.service';

@Component({
  selector: 'church-member-rite-delete-dialog',
  templateUrl: './member-rite-delete-dialog.component.html'
})
export class MemberRiteDeleteDialogComponent {
  memberRite: IMemberRite;

  constructor(
    protected memberRiteService: MemberRiteService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.memberRiteService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'memberRiteListModification',
        content: 'Deleted an memberRite'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-member-rite-delete-popup',
  template: ''
})
export class MemberRiteDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ memberRite }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MemberRiteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.memberRite = memberRite;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/member-rite', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/member-rite', { outlets: { popup: null } }]);
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
