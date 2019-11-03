import { Route } from '@angular/router';

import { ChurchConfigurationComponent } from './configuration.component';

export const configurationRoute: Route = {
  path: '',
  component: ChurchConfigurationComponent,
  data: {
    pageTitle: 'configuration.title'
  }
};
