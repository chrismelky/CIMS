import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Rite } from 'app/shared/model/rite.model';
import { RiteService } from './rite.service';
import { RiteComponent } from './rite.component';
import { RiteDetailComponent } from './rite-detail.component';
import { RiteUpdateComponent } from './rite-update.component';
import { RiteDeletePopupComponent } from './rite-delete-dialog.component';
import { IRite } from 'app/shared/model/rite.model';

@Injectable({ providedIn: 'root' })
export class RiteResolve implements Resolve<IRite> {
  constructor(private service: RiteService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRite> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Rite>) => response.ok),
        map((rite: HttpResponse<Rite>) => rite.body)
      );
    }
    return of(new Rite());
  }
}

export const riteRoute: Routes = [
  {
    path: '',
    component: RiteComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.rite.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RiteDetailComponent,
    resolve: {
      rite: RiteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.rite.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RiteUpdateComponent,
    resolve: {
      rite: RiteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.rite.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RiteUpdateComponent,
    resolve: {
      rite: RiteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.rite.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const ritePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: RiteDeletePopupComponent,
    resolve: {
      rite: RiteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.rite.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
