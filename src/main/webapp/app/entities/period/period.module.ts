import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { PeriodComponent } from './period.component';
import { PeriodDetailComponent } from './period-detail.component';
import { PeriodUpdateComponent } from './period-update.component';
import { PeriodDeletePopupComponent, PeriodDeleteDialogComponent } from './period-delete-dialog.component';
import { periodRoute, periodPopupRoute } from './period.route';

const ENTITY_STATES = [...periodRoute, ...periodPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PeriodComponent, PeriodDetailComponent, PeriodUpdateComponent, PeriodDeleteDialogComponent, PeriodDeletePopupComponent],
  entryComponents: [PeriodDeleteDialogComponent]
})
export class ChurchPeriodModule {}
