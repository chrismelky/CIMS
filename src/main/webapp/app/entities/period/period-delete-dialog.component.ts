import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriod } from 'app/shared/model/period.model';
import { PeriodService } from './period.service';

@Component({
  templateUrl: './period-delete-dialog.component.html'
})
export class PeriodDeleteDialogComponent {
  period?: IPeriod;

  constructor(protected periodService: PeriodService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.periodService.delete(id).subscribe(() => {
      this.eventManager.broadcast('periodListModification');
      this.activeModal.close();
    });
  }
}
