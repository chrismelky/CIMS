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

export const memberRiteRoute: Routes = [
  {
    path: '',
    component: MemberRiteComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.memberRite.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
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
    path: 'new',
    component: MemberRiteUpdateComponent,
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
    path: ':id/edit',
    component: MemberRiteUpdateComponent,
    resolve: {
      memberRite: MemberRiteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.memberRite.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const memberRitePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MemberRiteDeletePopupComponent,
    resolve: {
      memberRite: MemberRiteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.memberRite.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
