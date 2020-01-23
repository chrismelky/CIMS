import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PeriodContribution } from 'app/shared/model/period-contribution.model';
import { PeriodContributionService } from './period-contribution.service';
import { PeriodContributionComponent } from './period-contribution.component';
import { PeriodContributionDetailComponent } from './period-contribution-detail.component';
import { PeriodContributionUpdateComponent } from './period-contribution-update.component';
import { PeriodContributionDeletePopupComponent } from './period-contribution-delete-dialog.component';
import { IPeriodContribution } from 'app/shared/model/period-contribution.model';

@Injectable({ providedIn: 'root' })
export class PeriodContributionResolve implements Resolve<IPeriodContribution> {
  constructor(private service: PeriodContributionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPeriodContribution> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PeriodContribution>) => response.ok),
        map((periodContribution: HttpResponse<PeriodContribution>) => periodContribution.body)
      );
    }
    return of(new PeriodContribution());
  }
}

export const periodContributionRoute: Routes = [
  {
    path: '',
    component: PeriodContributionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.periodContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PeriodContributionDetailComponent,
    resolve: {
      periodContribution: PeriodContributionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'byChurch/:churchId/byMember/:memberId/byType/:typeId/byPeriod/:periodId/new',
    component: PeriodContributionUpdateComponent,
    resolve: {
      periodContribution: PeriodContributionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'byChurch/:churchId/byMember/:memberId/byType/:typeId/byPeriod/:periodId/:id/edit',
    component: PeriodContributionUpdateComponent,
    resolve: {
      periodContribution: PeriodContributionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const periodContributionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PeriodContributionDeletePopupComponent,
    resolve: {
      periodContribution: PeriodContributionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodContribution.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
