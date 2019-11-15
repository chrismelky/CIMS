import { NgModule } from '@angular/core';
import { ChurchSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { ChurchAlertComponent } from './alert/alert.component';
import { ChurchAlertErrorComponent } from './alert/alert-error.component';
import { ChurchLoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';

import { JhMaterialModule } from 'app/shared/jh-material.module';
import { UserMemberComponent } from './user-member/user-member.component';
@NgModule({
  imports: [JhMaterialModule, ChurchSharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    ChurchAlertComponent,
    ChurchAlertErrorComponent,
    ChurchLoginModalComponent,
    HasAnyAuthorityDirective,
    UserMemberComponent
  ],
  entryComponents: [ChurchLoginModalComponent],
  exports: [
    JhMaterialModule,
    ChurchSharedLibsModule,
    FindLanguageFromKeyPipe,
    ChurchAlertComponent,
    ChurchAlertErrorComponent,
    ChurchLoginModalComponent,
    HasAnyAuthorityDirective,
    UserMemberComponent
  ]
})
export class ChurchSharedModule {}
