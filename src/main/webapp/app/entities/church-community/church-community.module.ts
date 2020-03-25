import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { ChurchCommunityDetailComponent } from './church-community-detail.component';
import { ChurchCommunityUpdateComponent } from './church-community-update.component';
import { ChurchCommunityDeleteDialogComponent, ChurchCommunityDeletePopupComponent } from './church-community-delete-dialog.component';
import { churchCommunityPopupRoute, churchCommunityRoute } from './church-community.route';

const ENTITY_STATES = [...churchCommunityRoute, ...churchCommunityPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ChurchCommunityDetailComponent,
    ChurchCommunityUpdateComponent,
    ChurchCommunityDeleteDialogComponent,
    ChurchCommunityDeletePopupComponent
  ],
  entryComponents: [ChurchCommunityDeleteDialogComponent]
})
export class ChurchChurchCommunityModule {}
