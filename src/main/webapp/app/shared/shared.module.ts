import { NgModule } from '@angular/core';
import { ChurchSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { ChurchAlertComponent } from './alert/alert.component';
import { ChurchAlertErrorComponent } from './alert/alert-error.component';
import { ChurchLoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';

@NgModule({
  imports: [ChurchSharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    ChurchAlertComponent,
    ChurchAlertErrorComponent,
    ChurchLoginModalComponent,
    HasAnyAuthorityDirective
  ],
  entryComponents: [ChurchLoginModalComponent],
  exports: [
    ChurchSharedLibsModule,
    FindLanguageFromKeyPipe,
    ChurchAlertComponent,
    ChurchAlertErrorComponent,
    ChurchLoginModalComponent,
    HasAnyAuthorityDirective
  ]
})
export class ChurchSharedModule {}
