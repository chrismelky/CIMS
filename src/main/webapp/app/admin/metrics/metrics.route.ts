import { Route } from '@angular/router';

import { ChurchMetricsMonitoringComponent } from './metrics.component';

export const metricsRoute: Route = {
  path: '',
  component: ChurchMetricsMonitoringComponent,
  data: {
    pageTitle: 'metrics.title'
  }
};
