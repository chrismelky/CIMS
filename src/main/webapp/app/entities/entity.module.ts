import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'dashboard',
        loadChildren: () => import('../dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'church-type',
        loadChildren: () => import('./church-type/church-type.module').then(m => m.ChurchChurchTypeModule)
      },
      {
        path: 'church-community',
        loadChildren: () => import('./church-community/church-community.module').then(m => m.ChurchChurchCommunityModule)
      },
      {
        path: 'church',
        loadChildren: () => import('./church/church.module').then(m => m.ChurchChurchModule)
      },
      {
        path: 'church-activity',
        loadChildren: () => import('./church-activity/church-activity.module').then(m => m.ChurchChurchActivityModule)
      },
      {
        path: 'chuch-service',
        loadChildren: () => import('./chuch-service/chuch-service.module').then(m => m.ChurchChuchServiceModule)
      },
      {
        path: 'payment-method',
        loadChildren: () => import('./payment-method/payment-method.module').then(m => m.ChurchPaymentMethodModule)
      },
      {
        path: 'contribution-type',
        loadChildren: () => import('./contribution-type/contribution-type.module').then(m => m.ChurchContributionTypeModule)
      },
      {
        path: 'member',
        loadChildren: () => import('./member/member.module').then(m => m.ChurchMemberModule)
      },
      {
        path: 'rite',
        loadChildren: () => import('./rite/rite.module').then(m => m.ChurchRiteModule)
      },
      {
        path: 'member-rite',
        loadChildren: () => import('./member-rite/member-rite.module').then(m => m.ChurchMemberRiteModule)
      },
      {
        path: 'member-promise',
        loadChildren: () => import('./member-promise/member-promise.module').then(m => m.ChurchMemberPromiseModule)
      },
      {
        path: 'member-relative',
        loadChildren: () => import('./member-relative/member-relative.module').then(m => m.ChurchMemberRelativeModule)
      },
      {
        path: 'member-contribution',
        loadChildren: () => import('./member-contribution/member-contribution.module').then(m => m.ChurchMemberContributionModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ChurchEntityModule {}
