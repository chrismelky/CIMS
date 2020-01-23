import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { ChuchServiceDetailComponent } from './chuch-service-detail.component';
import { ChuchServiceUpdateComponent } from './chuch-service-update.component';
import { ChuchServiceDeletePopupComponent, ChuchServiceDeleteDialogComponent } from './chuch-service-delete-dialog.component';
import { chuchServiceRoute, chuchServicePopupRoute } from './chuch-service.route';

const ENTITY_STATES = [...chuchServiceRoute, ...chuchServicePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChuchServiceDetailComponent,
    ChuchServiceUpdateComponent,
    ChuchServiceDeleteDialogComponent,
    ChuchServiceDeletePopupComponent
  ],
  entryComponents: [ChuchServiceDeleteDialogComponent]
})
export class ChurchChuchServiceModule {}
