import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPeriodContribution, PeriodContribution } from 'app/shared/model/period-contribution.model';
import { PeriodContributionService } from './period-contribution.service';
import { PeriodContributionComponent } from './period-contribution.component';
import { PeriodContributionDetailComponent } from './period-contribution-detail.component';
import { PeriodContributionUpdateComponent } from './period-contribution-update.component';

@Injectable({ providedIn: 'root' })
export class PeriodContributionResolve implements Resolve<IPeriodContribution> {
  constructor(private service: PeriodContributionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeriodContribution> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((periodContribution: HttpResponse<PeriodContribution>) => {
          if (periodContribution.body) {
            return of(periodContribution.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
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
      authorities: [Authority.USER],
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
      authorities: [Authority.USER],
      pageTitle: 'churchApp.periodContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PeriodContributionUpdateComponent,
    resolve: {
      periodContribution: PeriodContributionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'churchApp.periodContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PeriodContributionUpdateComponent,
    resolve: {
      periodContribution: PeriodContributionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'churchApp.periodContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
