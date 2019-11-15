import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { RiteComponent } from './rite.component';
import { RiteDetailComponent } from './rite-detail.component';
import { RiteUpdateComponent } from './rite-update.component';
import { RiteDeletePopupComponent, RiteDeleteDialogComponent } from './rite-delete-dialog.component';
import { riteRoute, ritePopupRoute } from './rite.route';

const ENTITY_STATES = [...riteRoute, ...ritePopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [RiteComponent, RiteDetailComponent, RiteUpdateComponent, RiteDeleteDialogComponent, RiteDeletePopupComponent],
  entryComponents: [RiteDeleteDialogComponent]
})
export class ChurchRiteModule {}
