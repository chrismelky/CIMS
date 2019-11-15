import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { MemberContributionComponent } from './member-contribution.component';
import { MemberContributionDetailComponent } from './member-contribution-detail.component';
import { MemberContributionUpdateComponent } from './member-contribution-update.component';
import {
  MemberContributionDeletePopupComponent,
  MemberContributionDeleteDialogComponent
} from './member-contribution-delete-dialog.component';
import { memberContributionRoute, memberContributionPopupRoute } from './member-contribution.route';

const ENTITY_STATES = [...memberContributionRoute, ...memberContributionPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MemberContributionComponent,
    MemberContributionDetailComponent,
    MemberContributionUpdateComponent,
    MemberContributionDeleteDialogComponent,
    MemberContributionDeletePopupComponent
  ],
  entryComponents: [MemberContributionDeleteDialogComponent]
})
export class ChurchMemberContributionModule {}
