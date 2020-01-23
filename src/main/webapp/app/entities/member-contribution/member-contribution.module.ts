import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { MemberContributionDetailComponent } from './member-contribution-detail.component';
import { MemberContributionUpdateComponent } from './member-contribution-update.component';
import {
  MemberContributionDeleteDialogComponent,
  MemberContributionDeletePopupComponent
} from './member-contribution-delete-dialog.component';
import { memberContributionPopupRoute, memberContributionRoute } from './member-contribution.route';

const ENTITY_STATES = [...memberContributionRoute, ...memberContributionPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MemberContributionDetailComponent,
    MemberContributionUpdateComponent,
    MemberContributionDeleteDialogComponent,
    MemberContributionDeletePopupComponent
  ],
  entryComponents: [MemberContributionDeleteDialogComponent]
})
export class ChurchMemberContributionModule {}
