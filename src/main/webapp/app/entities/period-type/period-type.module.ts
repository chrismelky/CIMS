import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { PeriodTypeComponent } from './period-type.component';
import { PeriodTypeDetailComponent } from './period-type-detail.component';
import { PeriodTypeUpdateComponent } from './period-type-update.component';
import { PeriodTypeDeletePopupComponent, PeriodTypeDeleteDialogComponent } from './period-type-delete-dialog.component';
import { periodTypeRoute, periodTypePopupRoute } from './period-type.route';

const ENTITY_STATES = [...periodTypeRoute, ...periodTypePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PeriodTypeComponent,
    PeriodTypeDetailComponent,
    PeriodTypeUpdateComponent,
    PeriodTypeDeleteDialogComponent,
    PeriodTypeDeletePopupComponent
  ],
  entryComponents: [PeriodTypeDeleteDialogComponent]
})
export class ChurchPeriodTypeModule {}
