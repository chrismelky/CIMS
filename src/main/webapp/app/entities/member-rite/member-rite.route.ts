import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MemberRite } from 'app/shared/model/member-rite.model';
import { MemberRiteService } from './member-rite.service';
import { MemberRiteComponent } from './member-rite.component';
import { MemberRiteDetailComponent } from './member-rite-detail.component';
import { MemberRiteUpdateComponent } from './member-rite-update.component';
import { MemberRiteDeletePopupComponent } from './member-rite-delete-dialog.component';
import { IMemberRite } from 'app/shared/model/member-rite.model';
import { IMember, Member } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';

@Injectable({ providedIn: 'root' })
export class MemberRiteResolve implements Resolve<IMemberRite> {
  constructor(private service: MemberRiteService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMemberRite> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MemberRite>) => response.ok),
        map((memberRite: HttpResponse<MemberRite>) => memberRite.body)
      );
    }
    return of(new MemberRite());
  }
}

@Injectable({ providedIn: 'root' })
export class MemberRelativeMemberResolve implements Resolve<IMember> {
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

export const memberRiteRoute: Routes = [
  {
    path: ':memberId/:id/view',
    component: MemberRiteDetailComponent,
    resolve: {
      memberRite: MemberRiteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.memberRite.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':memberId/new',
    component: MemberRiteUpdateComponent,
    resolve: {
      memberRite: MemberRiteResolve,
      member: MemberRelativeMemberResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.memberRite.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':memberId/:id/edit',
    component: MemberRiteUpdateComponent,
    resolve: {
      memberRite: MemberRiteResolve,
      member: MemberRelativeMemberResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.memberRite.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const memberRitePopupRoute: Routes = [];
