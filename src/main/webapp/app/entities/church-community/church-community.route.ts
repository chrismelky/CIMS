import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChurchCommunity, IChurchCommunity } from 'app/shared/model/church-community.model';
import { ChurchCommunityService } from './church-community.service';
import { ChurchCommunityComponent } from './church-community.component';
import { ChurchCommunityDetailComponent } from './church-community-detail.component';
import { ChurchCommunityUpdateComponent } from './church-community-update.component';
import { ChurchCommunityDeletePopupComponent } from './church-community-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class ChurchCommunityResolve implements Resolve<IChurchCommunity> {
  constructor(private service: ChurchCommunityService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChurchCommunity> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChurchCommunity>) => response.ok),
        map((churchCommunity: HttpResponse<ChurchCommunity>) => churchCommunity.body)
      );
    }
    return of(new ChurchCommunity());
  }
}

export const churchCommunityRoute: Routes = [
  {
    path: '',
    component: ChurchCommunityComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.churchCommunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChurchCommunityDetailComponent,
    resolve: {
      churchCommunity: ChurchCommunityResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.churchCommunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChurchCommunityUpdateComponent,
    resolve: {
      churchCommunity: ChurchCommunityResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.churchCommunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChurchCommunityUpdateComponent,
    resolve: {
      churchCommunity: ChurchCommunityResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.churchCommunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const churchCommunityPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChurchCommunityDeletePopupComponent,
    resolve: {
      churchCommunity: ChurchCommunityResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.churchCommunity.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
