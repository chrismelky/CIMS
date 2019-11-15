import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MemberRelative } from 'app/shared/model/member-relative.model';
import { MemberRelativeService } from './member-relative.service';
import { MemberRelativeComponent } from './member-relative.component';
import { MemberRelativeDetailComponent } from './member-relative-detail.component';
import { MemberRelativeUpdateComponent } from './member-relative-update.component';
import { MemberRelativeDeletePopupComponent } from './member-relative-delete-dialog.component';
import { IMemberRelative } from 'app/shared/model/member-relative.model';

@Injectable({ providedIn: 'root' })
export class MemberRelativeResolve implements Resolve<IMemberRelative> {
  constructor(private service: MemberRelativeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMemberRelative> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MemberRelative>) => response.ok),
        map((memberRelative: HttpResponse<MemberRelative>) => memberRelative.body)
      );
    }
    return of(new MemberRelative());
  }
}

export const memberRelativeRoute: Routes = [
  {
    path: '',
    component: MemberRelativeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.memberRelative.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MemberRelativeDetailComponent,
    resolve: {
      memberRelative: MemberRelativeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.memberRelative.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MemberRelativeUpdateComponent,
    resolve: {
      memberRelative: MemberRelativeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.memberRelative.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MemberRelativeUpdateComponent,
    resolve: {
      memberRelative: MemberRelativeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.memberRelative.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const memberRelativePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MemberRelativeDeletePopupComponent,
    resolve: {
      memberRelative: MemberRelativeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.memberRelative.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
