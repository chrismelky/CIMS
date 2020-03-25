import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IPeriodContributionType, PeriodContributionType } from 'app/shared/model/period-contribution-type.model';
import { PeriodContributionTypeService } from './period-contribution-type.service';
import { PeriodContributionTypeUpdateComponent } from './period-contribution-type-update.component';
import { ChurchResolve } from 'app/entities/chuch-service/chuch-service.route';

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
    path: ':churchId/new',
    component: PeriodContributionTypeUpdateComponent,
    resolve: {
      periodContributionType: PeriodContributionTypeResolve,
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.periodContributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':churchId/:id/edit',
    component: PeriodContributionTypeUpdateComponent,
    resolve: {
      periodContributionType: PeriodContributionTypeResolve,
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.periodContributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const periodContributionTypePopupRoute: Routes = [];
