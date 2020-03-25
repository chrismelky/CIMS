import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChurchType, IChurchType } from 'app/shared/model/church-type.model';
import { ChurchTypeService } from './church-type.service';
import { ChurchTypeComponent } from './church-type.component';
import { ChurchTypeDetailComponent } from './church-type-detail.component';
import { ChurchTypeUpdateComponent } from './church-type-update.component';
import { ChurchTypeDeletePopupComponent } from './church-type-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class ChurchTypeResolve implements Resolve<IChurchType> {
  constructor(private service: ChurchTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChurchType> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChurchType>) => response.ok),
        map((churchType: HttpResponse<ChurchType>) => churchType.body)
      );
    }
    return of(new ChurchType());
  }
}

export const churchTypeRoute: Routes = [
  {
    path: '',
    component: ChurchTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.churchType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChurchTypeDetailComponent,
    resolve: {
      churchType: ChurchTypeResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.churchType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChurchTypeUpdateComponent,
    resolve: {
      churchType: ChurchTypeResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.churchType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChurchTypeUpdateComponent,
    resolve: {
      churchType: ChurchTypeResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.churchType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const churchTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChurchTypeDeletePopupComponent,
    resolve: {
      churchType: ChurchTypeResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.churchType.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
