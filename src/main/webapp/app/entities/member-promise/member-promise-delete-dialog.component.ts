import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMemberPromise } from 'app/shared/model/member-promise.model';
import { MemberPromiseService } from './member-promise.service';

@Component({
  templateUrl: './member-promise-delete-dialog.component.html'
})
export class MemberPromiseDeleteDialogComponent {
  memberPromise?: IMemberPromise;

  constructor(
    protected memberPromiseService: MemberPromiseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.memberPromiseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('memberPromiseListModification');
      this.activeModal.close();
    });
  }
}
