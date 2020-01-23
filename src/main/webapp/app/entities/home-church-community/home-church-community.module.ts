import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChurchSharedModule } from 'app/shared/shared.module';
import { HomeChurchCommunityDetailComponent } from './home-church-community-detail.component';
import { HomeChurchCommunityUpdateComponent } from './home-church-community-update.component';
import {
  HomeChurchCommunityDeleteDialogComponent,
  HomeChurchCommunityDeletePopupComponent
} from './home-church-community-delete-dialog.component';
import { homeChurchCommunityPopupRoute, homeChurchCommunityRoute } from './home-church-community.route';

const ENTITY_STATES = [...homeChurchCommunityRoute, ...homeChurchCommunityPopupRoute];

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    HomeChurchCommunityDetailComponent,
    HomeChurchCommunityUpdateComponent,
    HomeChurchCommunityDeleteDialogComponent,
    HomeChurchCommunityDeletePopupComponent
  ],
  entryComponents: [HomeChurchCommunityDeleteDialogComponent]
})
export class ChurchHomeChurchCommunityModule {}
