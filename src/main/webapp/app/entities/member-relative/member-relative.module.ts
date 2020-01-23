import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { MemberRelativeDetailComponent } from './member-relative-detail.component';
import { MemberRelativeUpdateComponent } from './member-relative-update.component';
import { MemberRelativeDeleteDialogComponent, MemberRelativeDeletePopupComponent } from './member-relative-delete-dialog.component';
import { memberRelativePopupRoute, memberRelativeRoute } from './member-relative.route';

const ENTITY_STATES = [...memberRelativeRoute, ...memberRelativePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MemberRelativeDetailComponent,
    MemberRelativeUpdateComponent,
    MemberRelativeDeleteDialogComponent,
    MemberRelativeDeletePopupComponent
  ],
  entryComponents: [MemberRelativeDeleteDialogComponent]
})
export class ChurchMemberRelativeModule {}
