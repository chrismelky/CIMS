import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { ContributionTypeComponent } from './contribution-type.component';
import { ContributionTypeDetailComponent } from './contribution-type-detail.component';
import { ContributionTypeUpdateComponent } from './contribution-type-update.component';
import { ContributionTypeDeletePopupComponent, ContributionTypeDeleteDialogComponent } from './contribution-type-delete-dialog.component';
import { contributionTypeRoute, contributionTypePopupRoute } from './contribution-type.route';

const ENTITY_STATES = [...contributionTypeRoute, ...contributionTypePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ContributionTypeComponent,
    ContributionTypeDetailComponent,
    ContributionTypeUpdateComponent,
    ContributionTypeDeleteDialogComponent,
    ContributionTypeDeletePopupComponent
  ],
  entryComponents: [ContributionTypeDeleteDialogComponent]
})
export class ChurchContributionTypeModule {}
