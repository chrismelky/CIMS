import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { MemberRiteComponent } from './member-rite.component';
import { MemberRiteDetailComponent } from './member-rite-detail.component';
import { MemberRiteUpdateComponent } from './member-rite-update.component';
import { MemberRiteDeletePopupComponent, MemberRiteDeleteDialogComponent } from './member-rite-delete-dialog.component';
import { memberRiteRoute, memberRitePopupRoute } from './member-rite.route';

const ENTITY_STATES = [...memberRiteRoute, ...memberRitePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MemberRiteComponent,
    MemberRiteDetailComponent,
    MemberRiteUpdateComponent,
    MemberRiteDeleteDialogComponent,
    MemberRiteDeletePopupComponent
  ],
  entryComponents: [MemberRiteDeleteDialogComponent],
  exports: [MemberRiteComponent]
})
export class ChurchMemberRiteModule {}
