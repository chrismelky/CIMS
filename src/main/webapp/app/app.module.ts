import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ChurchSharedModule } from 'app/shared/shared.module';
import { ChurchCoreModule } from 'app/core/core.module';
import { ChurchAppRoutingModule } from './app-routing.module';
import { ChurchHomeModule } from './home/home.module';
import { ChurchEntityModule } from './entities/entity.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import 'hammerjs';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { ChurchMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { NgHttpLoaderModule } from 'ng-http-loader';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ChurchSharedModule,
    ChurchCoreModule,
    ChurchHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ChurchEntityModule,
    ChurchAppRoutingModule,
    NgHttpLoaderModule.forRoot()
  ],
  declarations: [ChurchMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [ChurchMainComponent]
})
export class ChurchAppModule {}
