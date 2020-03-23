import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { FinancialYearComponent } from './financial-year.component';
import { FinancialYearDetailComponent } from './financial-year-detail.component';
import { FinancialYearUpdateComponent } from './financial-year-update.component';
import { FinancialYearDeleteDialogComponent } from './financial-year-delete-dialog.component';
import { financialYearRoute } from './financial-year.route';

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(financialYearRoute)],
  declarations: [FinancialYearComponent, FinancialYearDetailComponent, FinancialYearUpdateComponent, FinancialYearDeleteDialogComponent],
  entryComponents: [FinancialYearDeleteDialogComponent]
})
export class ChurchFinancialYearModule {}
