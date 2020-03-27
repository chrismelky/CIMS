import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { PeriodContributionItemDetailComponent } from './period-contribution-item-detail.component';
import { PeriodContributionItemUpdateComponent } from './period-contribution-item-update.component';
import {
  PeriodContributionItemDeleteDialogComponent,
  PeriodContributionItemDeletePopupComponent
} from './period-contribution-item-delete-dialog.component';
import { periodContributionItemPopupRoute, periodContributionItemRoute } from './period-contribution-item.route';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';

const ENTITY_STATES = [...periodContributionItemRoute, ...periodContributionItemPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, NgbDatepickerModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PeriodContributionItemDetailComponent,
    PeriodContributionItemUpdateComponent,
    PeriodContributionItemDeleteDialogComponent,
    PeriodContributionItemDeletePopupComponent
  ],
  entryComponents: [PeriodContributionItemDeleteDialogComponent]
})
export class ChurchPeriodContributionItemModule {}
