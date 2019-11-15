import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMemberContribution } from 'app/shared/model/member-contribution.model';
import { MemberContributionService } from './member-contribution.service';

@Component({
  selector: 'church-member-contribution-delete-dialog',
  templateUrl: './member-contribution-delete-dialog.component.html'
})
export class MemberContributionDeleteDialogComponent {
  memberContribution: IMemberContribution;

  constructor(
    protected memberContributionService: MemberContributionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.memberContributionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'memberContributionListModification',
        content: 'Deleted an memberContribution'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-member-contribution-delete-popup',
  template: ''
})
export class MemberContributionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ memberContribution }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MemberContributionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.memberContribution = memberContribution;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/member-contribution', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/member-contribution', { outlets: { popup: null } }]);
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
