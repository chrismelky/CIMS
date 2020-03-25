import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMember } from 'app/shared/model/member.model';
import { MemberService } from './member.service';

@Component({
  selector: 'church-member-delete-dialog',
  templateUrl: './member-delete-dialog.component.html'
})
export class MemberDeleteDialogComponent {
  member: IMember;

  constructor(protected memberService: MemberService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.memberService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'memberListModification',
        content: 'Deleted an member'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-member-delete-popup',
  template: ''
})
export class MemberDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ member }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MemberDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.member = member;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/member', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/member', { outlets: { popup: null } }]);
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
