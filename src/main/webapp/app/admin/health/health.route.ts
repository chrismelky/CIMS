import { Route } from '@angular/router';

import { ChurchHealthCheckComponent } from './health.component';

export const healthRoute: Route = {
  path: '',
  component: ChurchHealthCheckComponent,
  data: {
    pageTitle: 'health.title'
  }
};
