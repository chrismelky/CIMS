import { Route } from '@angular/router';

import { ChurchDocsComponent } from './docs.component';

export const docsRoute: Route = {
  path: '',
  component: ChurchDocsComponent,
  data: {
    pageTitle: 'global.menu.admin.apidocs'
  }
};
