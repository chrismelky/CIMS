import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PeriodContributionType } from 'app/shared/model/period-contribution-type.model';
import { PeriodContributionTypeService } from './period-contribution-type.service';
import { PeriodContributionTypeComponent } from './period-contribution-type.component';
import { PeriodContributionTypeDetailComponent } from './period-contribution-type-detail.component';
import { PeriodContributionTypeUpdateComponent } from './period-contribution-type-update.component';
import { PeriodContributionTypeDeletePopupComponent } from './period-contribution-type-delete-dialog.component';
import { IPeriodContributionType } from 'app/shared/model/period-contribution-type.model';

@Injectable({ providedIn: 'root' })
export class PeriodContributionTypeResolve implements Resolve<IPeriodContributionType> {
  constructor(private service: PeriodContributionTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPeriodContributionType> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PeriodContributionType>) => response.ok),
        map((periodContributionType: HttpResponse<PeriodContributionType>) => periodContributionType.body)
      );
    }
    return of(new PeriodContributionType());
  }
}

export const periodContributionTypeRoute: Routes = [
  {
    path: '',
    component: PeriodContributionTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.periodContributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PeriodContributionTypeDetailComponent,
    resolve: {
      periodContributionType: PeriodContributionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodContributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PeriodContributionTypeUpdateComponent,
    resolve: {
      periodContributionType: PeriodContributionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodContributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PeriodContributionTypeUpdateComponent,
    resolve: {
      periodContributionType: PeriodContributionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodContributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const periodContributionTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PeriodContributionTypeDeletePopupComponent,
    resolve: {
      periodContributionType: PeriodContributionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodContributionType.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
