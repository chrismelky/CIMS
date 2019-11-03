import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChurchSharedModule } from 'app/shared/shared.module';

import { ChurchMetricsMonitoringComponent } from './metrics.component';

import { metricsRoute } from './metrics.route';

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild([metricsRoute])],
  declarations: [ChurchMetricsMonitoringComponent]
})
export class MetricsModule {}
