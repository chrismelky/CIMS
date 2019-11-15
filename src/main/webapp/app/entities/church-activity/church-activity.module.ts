import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { ChurchActivityComponent } from './church-activity.component';
import { ChurchActivityDetailComponent } from './church-activity-detail.component';
import { ChurchActivityUpdateComponent } from './church-activity-update.component';
import { ChurchActivityDeletePopupComponent, ChurchActivityDeleteDialogComponent } from './church-activity-delete-dialog.component';
import { churchActivityRoute, churchActivityPopupRoute } from './church-activity.route';

const ENTITY_STATES = [...churchActivityRoute, ...churchActivityPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChurchActivityComponent,
    ChurchActivityDetailComponent,
    ChurchActivityUpdateComponent,
    ChurchActivityDeleteDialogComponent,
    ChurchActivityDeletePopupComponent
  ],
  entryComponents: [ChurchActivityDeleteDialogComponent]
})
export class ChurchChurchActivityModule {}
