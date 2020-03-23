import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPeriod, Period } from 'app/shared/model/period.model';
import { PeriodService } from './period.service';
import { PeriodComponent } from './period.component';
import { PeriodDetailComponent } from './period-detail.component';
import { PeriodUpdateComponent } from './period-update.component';

@Injectable({ providedIn: 'root' })
export class PeriodResolve implements Resolve<IPeriod> {
  constructor(private service: PeriodService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeriod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((period: HttpResponse<Period>) => {
          if (period.body) {
            return of(period.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Period());
  }
}

export const periodRoute: Routes = [
  {
    path: '',
    component: PeriodComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.period.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PeriodDetailComponent,
    resolve: {
      period: PeriodResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'churchApp.period.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PeriodUpdateComponent,
    resolve: {
      period: PeriodResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'churchApp.period.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PeriodUpdateComponent,
    resolve: {
      period: PeriodResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'churchApp.period.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
