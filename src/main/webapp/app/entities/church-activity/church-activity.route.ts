import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChurchActivity } from 'app/shared/model/church-activity.model';
import { ChurchActivityService } from './church-activity.service';
import { ChurchActivityComponent } from './church-activity.component';
import { ChurchActivityDetailComponent } from './church-activity-detail.component';
import { ChurchActivityUpdateComponent } from './church-activity-update.component';
import { ChurchActivityDeletePopupComponent } from './church-activity-delete-dialog.component';
import { IChurchActivity } from 'app/shared/model/church-activity.model';

@Injectable({ providedIn: 'root' })
export class ChurchActivityResolve implements Resolve<IChurchActivity> {
  constructor(private service: ChurchActivityService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChurchActivity> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChurchActivity>) => response.ok),
        map((churchActivity: HttpResponse<ChurchActivity>) => churchActivity.body)
      );
    }
    return of(new ChurchActivity());
  }
}

export const churchActivityRoute: Routes = [
  {
    path: '',
    component: ChurchActivityComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.churchActivity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChurchActivityDetailComponent,
    resolve: {
      churchActivity: ChurchActivityResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.churchActivity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChurchActivityUpdateComponent,
    resolve: {
      churchActivity: ChurchActivityResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.churchActivity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChurchActivityUpdateComponent,
    resolve: {
      churchActivity: ChurchActivityResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.churchActivity.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const churchActivityPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChurchActivityDeletePopupComponent,
    resolve: {
      churchActivity: ChurchActivityResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.churchActivity.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
