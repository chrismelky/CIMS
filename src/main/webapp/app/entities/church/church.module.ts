import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { ChurchComponent } from './church.component';
import { ChurchDetailComponent } from './church-detail.component';
import { ChurchUpdateComponent } from './church-update.component';
import { ChurchDeletePopupComponent, ChurchDeleteDialogComponent } from './church-delete-dialog.component';
import { churchRoute, churchPopupRoute } from './church.route';

const ENTITY_STATES = [...churchRoute, ...churchPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ChurchComponent, ChurchDetailComponent, ChurchUpdateComponent, ChurchDeleteDialogComponent, ChurchDeletePopupComponent],
  entryComponents: [ChurchDeleteDialogComponent]
})
export class ChurchChurchModule {}
