import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { PeriodContributionTypeComponent } from './period-contribution-type.component';
import { PeriodContributionTypeDetailComponent } from './period-contribution-type-detail.component';
import { PeriodContributionTypeUpdateComponent } from './period-contribution-type-update.component';
import {
  PeriodContributionTypeDeletePopupComponent,
  PeriodContributionTypeDeleteDialogComponent
} from './period-contribution-type-delete-dialog.component';
import { periodContributionTypeRoute, periodContributionTypePopupRoute } from './period-contribution-type.route';

const ENTITY_STATES = [...periodContributionTypeRoute, ...periodContributionTypePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PeriodContributionTypeComponent,
    PeriodContributionTypeDetailComponent,
    PeriodContributionTypeUpdateComponent,
    PeriodContributionTypeDeleteDialogComponent,
    PeriodContributionTypeDeletePopupComponent
  ],
  entryComponents: [PeriodContributionTypeDeleteDialogComponent]
})
export class ChurchPeriodContributionTypeModule {}
