import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IMember, Member } from 'app/shared/model/member.model';
import { MemberService } from './member.service';
import { MemberComponent } from './member.component';
import { MemberDetailComponent } from './member-detail.component';
import { MemberUpdateComponent } from './member-update.component';
import { MemberDeletePopupComponent } from './member-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class MemberResolve implements Resolve<IMember> {
  constructor(private service: MemberService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMember> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Member>) => response.ok),
        map((member: HttpResponse<Member>) => member.body)
      );
    }
    return of(new Member());
  }
}

@Injectable({ providedIn: 'root' })
export class MemberIdResolve implements Resolve<IMember> {
  constructor(private service: MemberService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMember> {
    const id = route.params['memberId'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Member>) => response.ok),
        map((member: HttpResponse<Member>) => member.body)
      );
    }
    return of(null);
  }
}

export const memberRoute: Routes = [
  {
    path: 'byChurch/:churchId',
    component: MemberComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.member.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MemberDetailComponent,
    resolve: {
      member: MemberResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.member.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new/byChurch/:churchId',
    component: MemberUpdateComponent,
    resolve: {
      member: MemberResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.member.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit/byChurch/:churchId',
    component: MemberUpdateComponent,
    resolve: {
      member: MemberResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.member.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const memberPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MemberDeletePopupComponent,
    resolve: {
      member: MemberResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.member.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
