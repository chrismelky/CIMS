import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChurchSharedModule } from 'app/shared/shared.module';

import { ChurchHealthCheckComponent } from './health.component';
import { ChurchHealthModalComponent } from './health-modal.component';

import { healthRoute } from './health.route';

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild([healthRoute])],
  declarations: [ChurchHealthCheckComponent, ChurchHealthModalComponent],
  entryComponents: [ChurchHealthModalComponent]
})
export class HealthModule {}
