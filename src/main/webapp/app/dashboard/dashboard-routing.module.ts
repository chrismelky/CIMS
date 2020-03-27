import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from 'app/dashboard/dashboard.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule {}
