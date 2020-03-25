import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IMemberPromise, MemberPromise } from 'app/shared/model/member-promise.model';
import { MemberPromiseService } from './member-promise.service';
import { MemberPromiseComponent } from './member-promise.component';
import { MemberPromiseDetailComponent } from './member-promise-detail.component';
import { MemberPromiseUpdateComponent } from './member-promise-update.component';
import { MemberPromiseDeletePopupComponent } from './member-promise-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class MemberPromiseResolve implements Resolve<IMemberPromise> {
  constructor(private service: MemberPromiseService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMemberPromise> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MemberPromise>) => response.ok),
        map((memberPromise: HttpResponse<MemberPromise>) => memberPromise.body)
      );
    }
    return of(new MemberPromise());
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
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.memberPromise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MemberPromiseDetailComponent,
    resolve: {
      memberPromise: MemberPromiseResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.memberPromise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MemberPromiseUpdateComponent,
    resolve: {
      memberPromise: MemberPromiseResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.memberPromise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MemberPromiseUpdateComponent,
    resolve: {
      memberPromise: MemberPromiseResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.memberPromise.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const memberPromisePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MemberPromiseDeletePopupComponent,
    resolve: {
      memberPromise: MemberPromiseResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.memberPromise.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
