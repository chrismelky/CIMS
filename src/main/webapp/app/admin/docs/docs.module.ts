import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChurchSharedModule } from 'app/shared/shared.module';

import { ChurchDocsComponent } from './docs.component';

import { docsRoute } from './docs.route';

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild([docsRoute])],
  declarations: [ChurchDocsComponent]
})
export class DocsModule {}
