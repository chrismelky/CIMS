import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContributionType } from 'app/shared/model/contribution-type.model';
import { ContributionTypeService } from './contribution-type.service';

@Component({
  selector: 'church-contribution-type-delete-dialog',
  templateUrl: './contribution-type-delete-dialog.component.html'
})
export class ContributionTypeDeleteDialogComponent {
  contributionType: IContributionType;

  constructor(
    protected contributionTypeService: ContributionTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.contributionTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'contributionTypeListModification',
        content: 'Deleted an contributionType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-contribution-type-delete-popup',
  template: ''
})
export class ContributionTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ contributionType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ContributionTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.contributionType = contributionType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/contribution-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/contribution-type', { outlets: { popup: null } }]);
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
