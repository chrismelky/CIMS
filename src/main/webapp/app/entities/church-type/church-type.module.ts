import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { ChurchTypeComponent } from './church-type.component';
import { ChurchTypeDetailComponent } from './church-type-detail.component';
import { ChurchTypeUpdateComponent } from './church-type-update.component';
import { ChurchTypeDeletePopupComponent, ChurchTypeDeleteDialogComponent } from './church-type-delete-dialog.component';
import { churchTypeRoute, churchTypePopupRoute } from './church-type.route';

const ENTITY_STATES = [...churchTypeRoute, ...churchTypePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChurchTypeComponent,
    ChurchTypeDetailComponent,
    ChurchTypeUpdateComponent,
    ChurchTypeDeleteDialogComponent,
    ChurchTypeDeletePopupComponent
  ],
  entryComponents: [ChurchTypeDeleteDialogComponent]
})
export class ChurchChurchTypeModule {}
