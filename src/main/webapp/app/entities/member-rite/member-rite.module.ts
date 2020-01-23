import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { MemberRiteDetailComponent } from './member-rite-detail.component';
import { MemberRiteUpdateComponent } from './member-rite-update.component';
import { MemberRiteDeleteDialogComponent, MemberRiteDeletePopupComponent } from './member-rite-delete-dialog.component';
import { memberRitePopupRoute, memberRiteRoute } from './member-rite.route';

const ENTITY_STATES = [...memberRiteRoute, ...memberRitePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [MemberRiteDetailComponent, MemberRiteUpdateComponent, MemberRiteDeleteDialogComponent, MemberRiteDeletePopupComponent],
  entryComponents: [MemberRiteDeleteDialogComponent]
})
export class ChurchMemberRiteModule {}
