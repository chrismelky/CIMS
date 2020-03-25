import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { ChurchActivityDetailComponent } from './church-activity-detail.component';
import { ChurchActivityUpdateComponent } from './church-activity-update.component';
import { ChurchActivityDeleteDialogComponent, ChurchActivityDeletePopupComponent } from './church-activity-delete-dialog.component';
import { churchActivityPopupRoute, churchActivityRoute } from './church-activity.route';

const ENTITY_STATES = [...churchActivityRoute, ...churchActivityPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChurchActivityDetailComponent,
    ChurchActivityUpdateComponent,
    ChurchActivityDeleteDialogComponent,
    ChurchActivityDeletePopupComponent
  ],
  entryComponents: [ChurchActivityDeleteDialogComponent]
})
export class ChurchChurchActivityModule {}
