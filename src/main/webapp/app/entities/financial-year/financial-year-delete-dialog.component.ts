import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFinancialYear } from 'app/shared/model/financial-year.model';
import { FinancialYearService } from './financial-year.service';

@Component({
  templateUrl: './financial-year-delete-dialog.component.html'
})
export class FinancialYearDeleteDialogComponent {
  financialYear?: IFinancialYear;

  constructor(
    protected financialYearService: FinancialYearService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.financialYearService.delete(id).subscribe(() => {
      this.eventManager.broadcast('financialYearListModification');
      this.activeModal.close();
    });
  }
}
