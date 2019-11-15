import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { MemberPromiseComponent } from './member-promise.component';
import { MemberPromiseDetailComponent } from './member-promise-detail.component';
import { MemberPromiseUpdateComponent } from './member-promise-update.component';
import { MemberPromiseDeletePopupComponent, MemberPromiseDeleteDialogComponent } from './member-promise-delete-dialog.component';
import { memberPromiseRoute, memberPromisePopupRoute } from './member-promise.route';

const ENTITY_STATES = [...memberPromiseRoute, ...memberPromisePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MemberPromiseComponent,
    MemberPromiseDetailComponent,
    MemberPromiseUpdateComponent,
    MemberPromiseDeleteDialogComponent,
    MemberPromiseDeletePopupComponent
  ],
  entryComponents: [MemberPromiseDeleteDialogComponent]
})
export class ChurchMemberPromiseModule {}
