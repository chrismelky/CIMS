import { Route } from '@angular/router';

import { ChurchTrackerComponent } from './tracker.component';

export const trackerRoute: Route = {
  path: '',
  component: ChurchTrackerComponent,
  data: {
    pageTitle: 'tracker.title'
  }
};
