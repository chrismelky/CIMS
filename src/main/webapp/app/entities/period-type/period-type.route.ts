import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PeriodType } from 'app/shared/model/period-type.model';
import { PeriodTypeService } from './period-type.service';
import { PeriodTypeComponent } from './period-type.component';
import { PeriodTypeDetailComponent } from './period-type-detail.component';
import { PeriodTypeUpdateComponent } from './period-type-update.component';
import { PeriodTypeDeletePopupComponent } from './period-type-delete-dialog.component';
import { IPeriodType } from 'app/shared/model/period-type.model';

@Injectable({ providedIn: 'root' })
export class PeriodTypeResolve implements Resolve<IPeriodType> {
  constructor(private service: PeriodTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPeriodType> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PeriodType>) => response.ok),
        map((periodType: HttpResponse<PeriodType>) => periodType.body)
      );
    }
    return of(new PeriodType());
  }
}

export const periodTypeRoute: Routes = [
  {
    path: '',
    component: PeriodTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.periodType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PeriodTypeDetailComponent,
    resolve: {
      periodType: PeriodTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PeriodTypeUpdateComponent,
    resolve: {
      periodType: PeriodTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PeriodTypeUpdateComponent,
    resolve: {
      periodType: PeriodTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const periodTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PeriodTypeDeletePopupComponent,
    resolve: {
      periodType: PeriodTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.periodType.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
