import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChurchSharedModule } from 'app/shared/shared.module';

import { ChurchConfigurationComponent } from './configuration.component';

import { configurationRoute } from './configuration.route';

@NgModule({
  imports: [ChurchSharedModule, RouterModule.forChild([configurationRoute])],
  declarations: [ChurchConfigurationComponent]
})
export class ConfigurationModule {}
