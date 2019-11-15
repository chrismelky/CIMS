import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { MemberRelativeComponent } from './member-relative.component';
import { MemberRelativeDetailComponent } from './member-relative-detail.component';
import { MemberRelativeUpdateComponent } from './member-relative-update.component';
import { MemberRelativeDeletePopupComponent, MemberRelativeDeleteDialogComponent } from './member-relative-delete-dialog.component';
import { memberRelativeRoute, memberRelativePopupRoute } from './member-relative.route';

const ENTITY_STATES = [...memberRelativeRoute, ...memberRelativePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MemberRelativeComponent,
    MemberRelativeDetailComponent,
    MemberRelativeUpdateComponent,
    MemberRelativeDeleteDialogComponent,
    MemberRelativeDeletePopupComponent
  ],
  entryComponents: [MemberRelativeDeleteDialogComponent]
})
export class ChurchMemberRelativeModule {}
