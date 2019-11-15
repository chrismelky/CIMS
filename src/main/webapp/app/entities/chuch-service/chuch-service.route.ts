import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChuchService } from 'app/shared/model/chuch-service.model';
import { ChuchServiceService } from './chuch-service.service';
import { ChuchServiceComponent } from './chuch-service.component';
import { ChuchServiceDetailComponent } from './chuch-service-detail.component';
import { ChuchServiceUpdateComponent } from './chuch-service-update.component';
import { ChuchServiceDeletePopupComponent } from './chuch-service-delete-dialog.component';
import { IChuchService } from 'app/shared/model/chuch-service.model';

@Injectable({ providedIn: 'root' })
export class ChuchServiceResolve implements Resolve<IChuchService> {
  constructor(private service: ChuchServiceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChuchService> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChuchService>) => response.ok),
        map((chuchService: HttpResponse<ChuchService>) => chuchService.body)
      );
    }
    return of(new ChuchService());
  }
}

export const chuchServiceRoute: Routes = [
  {
    path: '',
    component: ChuchServiceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.chuchService.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChuchServiceDetailComponent,
    resolve: {
      chuchService: ChuchServiceResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.chuchService.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChuchServiceUpdateComponent,
    resolve: {
      chuchService: ChuchServiceResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.chuchService.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChuchServiceUpdateComponent,
    resolve: {
      chuchService: ChuchServiceResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.chuchService.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const chuchServicePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ChuchServiceDeletePopupComponent,
    resolve: {
      chuchService: ChuchServiceResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'churchApp.chuchService.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
