import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { PeriodContributionComponent } from './period-contribution.component';
import { PeriodContributionDetailComponent } from './period-contribution-detail.component';
import { PeriodContributionUpdateComponent } from './period-contribution-update.component';
import { PeriodContributionDeleteDialogComponent } from './period-contribution-delete-dialog.component';
import { periodContributionRoute } from './period-contribution.route';

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(periodContributionRoute)],
  declarations: [PeriodContributionDetailComponent, PeriodContributionUpdateComponent, PeriodContributionDeleteDialogComponent],
  entryComponents: [PeriodContributionDeleteDialogComponent]
})
export class ChurchPeriodContributionModule {}
