import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { ChurchSharedModule } from 'app/shared/shared.module';
import { FusionChartsModule } from 'angular-fusioncharts';

import * as fusionCharts from 'fusioncharts';
import * as charts from 'fusioncharts/fusioncharts.charts';
import * as theme from 'fusioncharts/themes/fusioncharts.theme.fusion';

FusionChartsModule.fcRoot(fusionCharts, charts, theme);

@NgModule({
  declarations: [DashboardComponent],
  imports: [CommonModule, ChurchSharedModule, DashboardRoutingModule, FusionChartsModule]
})
export class DashboardModule {}
