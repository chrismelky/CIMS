import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChurchCommunity } from 'app/shared/model/church-community.model';
import { ChurchCommunityService } from './church-community.service';

@Component({
  selector: 'church-church-community-delete-dialog',
  templateUrl: './church-community-delete-dialog.component.html'
})
export class ChurchCommunityDeleteDialogComponent {
  churchCommunity: IChurchCommunity;

  constructor(
    protected churchCommunityService: ChurchCommunityService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.churchCommunityService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'churchCommunityListModification',
        content: 'Deleted an churchCommunity'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-church-community-delete-popup',
  template: ''
})
export class ChurchCommunityDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ churchCommunity }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ChurchCommunityDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.churchCommunity = churchCommunity;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/church-community', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/church-community', { outlets: { popup: null } }]);
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
