import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { PeriodContributionDetailComponent } from './period-contribution-detail.component';
import { PeriodContributionUpdateComponent } from './period-contribution-update.component';
import {
  PeriodContributionDeleteDialogComponent,
  PeriodContributionDeletePopupComponent
} from './period-contribution-delete-dialog.component';
import { periodContributionPopupRoute, periodContributionRoute } from './period-contribution.route';
import { ChurchPeriodContributionItemModule } from 'app/entities/period-contribution-item/period-contribution-item.module';

const ENTITY_STATES = [...periodContributionRoute, ...periodContributionPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, ChurchPeriodContributionItemModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PeriodContributionDetailComponent,
    PeriodContributionUpdateComponent,
    PeriodContributionDeleteDialogComponent,
    PeriodContributionDeletePopupComponent
  ],
  entryComponents: [PeriodContributionDeleteDialogComponent]
})
export class ChurchPeriodContributionModule {}
