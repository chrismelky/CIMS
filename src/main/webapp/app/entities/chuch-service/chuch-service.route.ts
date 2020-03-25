import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChuchService, IChuchService } from 'app/shared/model/chuch-service.model';
import { ChuchServiceService } from './chuch-service.service';
import { ChuchServiceUpdateComponent } from './chuch-service-update.component';
import { Church, IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';

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

@Injectable({ providedIn: 'root' })
export class ChurchResolve implements Resolve<IChurch> {
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

export const chuchServiceRoute: Routes = [
  {
    path: ':churchId/new',
    component: ChuchServiceUpdateComponent,
    resolve: {
      chuchService: ChuchServiceResolve,
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.chuchService.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':churchId/:id/edit',
    component: ChuchServiceUpdateComponent,
    resolve: {
      chuchService: ChuchServiceResolve,
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.chuchService.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const chuchServicePopupRoute: Routes = [];
