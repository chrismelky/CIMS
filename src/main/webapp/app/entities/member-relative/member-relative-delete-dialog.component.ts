import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMemberRelative } from 'app/shared/model/member-relative.model';
import { MemberRelativeService } from './member-relative.service';

@Component({
  selector: 'church-member-relative-delete-dialog',
  templateUrl: './member-relative-delete-dialog.component.html'
})
export class MemberRelativeDeleteDialogComponent {
  memberRelative: IMemberRelative;

  constructor(
    protected memberRelativeService: MemberRelativeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.memberRelativeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'memberRelativeListModification',
        content: 'Deleted an memberRelative'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-member-relative-delete-popup',
  template: ''
})
export class MemberRelativeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ memberRelative }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MemberRelativeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.memberRelative = memberRelative;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/member-relative', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/member-relative', { outlets: { popup: null } }]);
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
