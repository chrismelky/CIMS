import { NgModule } from '@angular/core';
import { ChurchSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { ChurchAlertComponent } from './alert/alert.component';
import { ChurchAlertErrorComponent } from './alert/alert-error.component';
import { ChurchLoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { UserMemberComponent } from './user-member/user-member.component';
import { MemberRelativeComponent } from 'app/entities/member-relative/member-relative.component';
import { MemberRiteComponent } from 'app/entities/member-rite/member-rite.component';
import { MemberContributionComponent } from 'app/entities/member-contribution/member-contribution.component';
import { PeriodContributionComponent } from 'app/entities/period-contribution/period-contribution.component';
import { RouterModule } from '@angular/router';
import { PeriodContributionItemComponent } from 'app/entities/period-contribution-item/period-contribution-item.component';
import { ChuchServiceComponent } from 'app/entities/chuch-service/chuch-service.component';
import { ChurchActivityComponent } from 'app/entities/church-activity/church-activity.component';
import { ChurchCommunityComponent } from 'app/entities/church-community/church-community.component';
import { HomeChurchCommunityComponent } from 'app/entities/home-church-community/home-church-community.component';
import { UserManagementUpdateComponent } from 'app/admin/user-management/user-management-update.component';
import { PeriodContributionTypeComponent } from 'app/entities/period-contribution-type/period-contribution-type.component';

@NgModule({
  imports: [
    // JhMaterialModule,
    ChurchSharedLibsModule,
    RouterModule
  ],
  declarations: [
    FindLanguageFromKeyPipe,
    ChurchAlertComponent,
    ChurchAlertErrorComponent,
    ChurchLoginModalComponent,
    HasAnyAuthorityDirective,
    UserMemberComponent,
    MemberRelativeComponent,
    MemberRiteComponent,
    MemberContributionComponent,
    PeriodContributionComponent,
    PeriodContributionItemComponent,
    ChuchServiceComponent,
    ChurchActivityComponent,
    ChurchCommunityComponent,
    HomeChurchCommunityComponent,
    UserManagementUpdateComponent,
    PeriodContributionTypeComponent
  ],
  entryComponents: [ChurchLoginModalComponent],
  exports: [
    // JhMaterialModule,
    ChurchSharedLibsModule,
    FindLanguageFromKeyPipe,
    ChurchAlertComponent,
    ChurchAlertErrorComponent,
    ChurchLoginModalComponent,
    HasAnyAuthorityDirective,
    UserMemberComponent,
    MemberRelativeComponent,
    MemberRiteComponent,
    MemberContributionComponent,
    PeriodContributionComponent,
    PeriodContributionItemComponent,
    ChuchServiceComponent,
    ChurchActivityComponent,
    ChurchCommunityComponent,
    HomeChurchCommunityComponent,
    UserManagementUpdateComponent,
    PeriodContributionTypeComponent
  ]
})
export class ChurchSharedModule {}
