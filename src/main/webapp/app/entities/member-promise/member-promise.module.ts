import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { MemberPromiseDetailComponent } from './member-promise-detail.component';
import { MemberPromiseUpdateComponent } from './member-promise-update.component';
import { MemberPromiseDeleteDialogComponent } from './member-promise-delete-dialog.component';
import { memberPromiseRoute } from './member-promise.route';

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(memberPromiseRoute)],
  declarations: [MemberPromiseDetailComponent, MemberPromiseUpdateComponent, MemberPromiseDeleteDialogComponent],
  entryComponents: [MemberPromiseDeleteDialogComponent]
})
export class ChurchMemberPromiseModule {}
