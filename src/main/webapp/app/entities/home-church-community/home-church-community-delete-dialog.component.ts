import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHomeChurchCommunity } from 'app/shared/model/home-church-community.model';
import { HomeChurchCommunityService } from './home-church-community.service';

@Component({
  selector: 'church-home-church-community-delete-dialog',
  templateUrl: './home-church-community-delete-dialog.component.html'
})
export class HomeChurchCommunityDeleteDialogComponent {
  homeChurchCommunity: IHomeChurchCommunity;

  constructor(
    protected homeChurchCommunityService: HomeChurchCommunityService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.homeChurchCommunityService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'homeChurchCommunityListModification',
        content: 'Deleted an homeChurchCommunity'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-home-church-community-delete-popup',
  template: ''
})
export class HomeChurchCommunityDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ homeChurchCommunity }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(HomeChurchCommunityDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.homeChurchCommunity = homeChurchCommunity;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/home-church-community', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/home-church-community', { outlets: { popup: null } }]);
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
