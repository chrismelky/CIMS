import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMemberPromise, MemberPromise } from 'app/shared/model/member-promise.model';
import { MemberPromiseService } from './member-promise.service';
import { MemberPromiseComponent } from './member-promise.component';
import { MemberPromiseUpdateComponent } from './member-promise-update.component';
import { MemberIdResolve } from '../member/member.route';
import { ChurchIdResolve } from '../church/church.route';
import { PeriodContributionTypeIdResolve } from '../period-contribution-type/period-contribution-type.route';
import { FinancialYearIdResolve } from '../financial-year/financial-year.route';

@Injectable({ providedIn: 'root' })
export class MemberPromiseResolve implements Resolve<IMemberPromise> {
  constructor(private service: MemberPromiseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMemberPromise> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((memberPromise: HttpResponse<MemberPromise>) => {
          if (memberPromise.body) {
            return of(memberPromise.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    } else {
      return of(new MemberPromise());
    }
  }
}

@Injectable({ providedIn: 'root' })
export class MemberPromiseIdResolve implements Resolve<IMemberPromise> {
  constructor(private service: MemberPromiseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMemberPromise> | Observable<never> {
    const id = route.params['memberPromiseId'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((memberPromise: HttpResponse<MemberPromise>) => {
          if (memberPromise.body) {
            return of(memberPromise.body);
          } else {
            return of(undefined);
          }
        })
      );
    }
    return of(undefined);
  }
}

export const memberPromiseRoute: Routes = [
  {
    path: '',
    component: MemberPromiseComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.ADMIN, Authority.CHURCH_ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.memberPromise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':churchId/:memberId/:financialYearId/:typeId/new',
    component: MemberPromiseUpdateComponent,
    resolve: {
      memberPromise: MemberPromiseResolve,
      member: MemberIdResolve,
      church: ChurchIdResolve,
      financialYear: FinancialYearIdResolve,
      type: PeriodContributionTypeIdResolve
    },
    data: {
      authorities: [Authority.ADMIN, Authority.CHURCH_ADMIN],
      pageTitle: 'churchApp.memberPromise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':churchId/:memberId/:financialYearId/:typeId/:id/edit',
    component: MemberPromiseUpdateComponent,
    resolve: {
      memberPromise: MemberPromiseResolve,
      member: MemberIdResolve,
      church: ChurchIdResolve,
      financialYear: FinancialYearIdResolve,
      type: PeriodContributionTypeIdResolve
    },
    data: {
      authorities: [Authority.ADMIN, Authority.CHURCH_ADMIN],
      pageTitle: 'churchApp.memberPromise.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
