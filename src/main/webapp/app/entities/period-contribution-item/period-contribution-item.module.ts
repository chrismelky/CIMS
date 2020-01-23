import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { PeriodContributionItemComponent } from './period-contribution-item.component';
import { PeriodContributionItemDetailComponent } from './period-contribution-item-detail.component';
import { PeriodContributionItemUpdateComponent } from './period-contribution-item-update.component';
import {
  PeriodContributionItemDeletePopupComponent,
  PeriodContributionItemDeleteDialogComponent
} from './period-contribution-item-delete-dialog.component';
import { periodContributionItemRoute, periodContributionItemPopupRoute } from './period-contribution-item.route';

const ENTITY_STATES = [...periodContributionItemRoute, ...periodContributionItemPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PeriodContributionItemDetailComponent,
    PeriodContributionItemUpdateComponent,
    PeriodContributionItemDeleteDialogComponent,
    PeriodContributionItemDeletePopupComponent
  ],
  entryComponents: [PeriodContributionItemDeleteDialogComponent]
})
export class ChurchPeriodContributionItemModule {}
