import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChurchSharedModule } from 'app/shared/shared.module';

import { ChurchTrackerComponent } from './tracker.component';

import { trackerRoute } from './tracker.route';

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild([trackerRoute])],
  declarations: [ChurchTrackerComponent]
})
export class TrackerModule {}
