import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { MemberComponent } from './member.component';
import { MemberDetailComponent } from './member-detail.component';
import { MemberUpdateComponent } from './member-update.component';
import { MemberDeletePopupComponent, MemberDeleteDialogComponent } from './member-delete-dialog.component';
import { memberRoute, memberPopupRoute } from './member.route';
import { ChurchMemberRelativeModule } from 'app/entities/member-relative/member-relative.module';
import { ChurchMemberRiteModule } from 'app/entities/member-rite/member-rite.module';
import { ChurchMemberContributionModule } from 'app/entities/member-contribution/member-contribution.module';

const ENTITY_STATES = [...memberRoute, ...memberPopupRoute];

@NgModule({
  imports: [
    ChurchSharedModule,
    ChurchMemberRelativeModule,
    ChurchMemberRiteModule,
    ChurchMemberContributionModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [MemberComponent, MemberDetailComponent, MemberUpdateComponent, MemberDeleteDialogComponent, MemberDeletePopupComponent],
  exports: [],
  entryComponents: [MemberDeleteDialogComponent]
})
export class ChurchMemberModule {}
