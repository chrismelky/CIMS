import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Church, IChurch } from 'app/shared/model/church.model';
import { ChurchService } from './church.service';
import { ChurchComponent } from './church.component';
import { ChurchDetailComponent } from './church-detail.component';
import { ChurchUpdateComponent } from './church-update.component';
import { ChurchDeletePopupComponent } from './church-delete-dialog.component';
import { UserManagementUpdateComponent } from 'app/admin/user-management/user-management-update.component';
import { UserManagementResolve } from 'app/admin/user-management/user-management.route';

@Injectable({ providedIn: 'root' })
export class ChurchResolve implements Resolve<IChurch> {
  constructor(private service: ChurchService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChurch> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Church>) => response.ok),
        map((church: HttpResponse<Church>) => church.body)
      );
    }
    return of(new Church());
  }
}
@Injectable({ providedIn: 'root' })
export class ChurchIdResolve implements Resolve<IChurch> {
  constructor(private service: ChurchService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChurch> {
    const id = route.params['churchId'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Church>) => response.ok),
        map((church: HttpResponse<Church>) => church.body)
      );
    }
    return of(null);
  }
}

export const churchRoute: Routes = [
  {
    path: '',
    component: ChurchComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.church.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChurchDetailComponent,
    resolve: {
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.church.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChurchUpdateComponent,
    resolve: {
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.church.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChurchUpdateComponent,
    resolve: {
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.church.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':churchId/user/:login/edit',
    component: UserManagementUpdateComponent,
    resolve: {
      church: ChurchIdResolve,
      user: UserManagementResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.church.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':churchId/user/new',
    component: UserManagementUpdateComponent,
    resolve: {
      church: ChurchIdResolve,
      user: UserManagementResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.church.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const churchPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChurchDeletePopupComponent,
    resolve: {
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.church.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
