import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriodContribution } from 'app/shared/model/period-contribution.model';
import { PeriodContributionService } from './period-contribution.service';

@Component({
  templateUrl: './period-contribution-delete-dialog.component.html'
})
export class PeriodContributionDeleteDialogComponent {
  periodContribution?: IPeriodContribution;

  constructor(
    protected periodContributionService: PeriodContributionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.periodContributionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('periodContributionListModification');
      this.activeModal.close();
    });
  }
}
