import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { ChurchComponent } from './church.component';
import { ChurchDetailComponent } from './church-detail.component';
import { ChurchUpdateComponent } from './church-update.component';
import { ChurchDeleteDialogComponent, ChurchDeletePopupComponent } from './church-delete-dialog.component';
import { churchPopupRoute, churchRoute } from './church.route';
import { ChurchUserModule } from 'app/entities/church-user/church-user.module';
import { UserManagementUpdateComponent } from 'app/admin/user-management/user-management-update.component';

const ENTITY_STATES = [...churchRoute, ...churchPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, ChurchUserModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChurchComponent,
    ChurchDetailComponent,
    ChurchUpdateComponent,
    ChurchDeleteDialogComponent,
    ChurchDeletePopupComponent,
    UserManagementUpdateComponent
  ],
  entryComponents: [ChurchDeleteDialogComponent]
})
export class ChurchChurchModule {}
