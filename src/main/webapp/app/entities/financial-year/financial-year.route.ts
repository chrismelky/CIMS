import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { FinancialYear, IFinancialYear } from 'app/shared/model/financial-year.model';
import { FinancialYearService } from './financial-year.service';
import { FinancialYearComponent } from './financial-year.component';
import { FinancialYearDetailComponent } from './financial-year-detail.component';
import { FinancialYearUpdateComponent } from './financial-year-update.component';

@Injectable({ providedIn: 'root' })
export class FinancialYearResolve implements Resolve<IFinancialYear> {
  constructor(private service: FinancialYearService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFinancialYear> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((financialYear: HttpResponse<FinancialYear>) => {
          if (financialYear.body) {
            return of(financialYear.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FinancialYear());
  }
}

export const financialYearRoute: Routes = [
  {
    path: '',
    component: FinancialYearComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.financialYear.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FinancialYearDetailComponent,
    resolve: {
      financialYear: FinancialYearResolve
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'churchApp.financialYear.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FinancialYearUpdateComponent,
    resolve: {
      financialYear: FinancialYearResolve
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'churchApp.financialYear.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FinancialYearUpdateComponent,
    resolve: {
      financialYear: FinancialYearResolve
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'churchApp.financialYear.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
