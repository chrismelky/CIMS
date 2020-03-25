import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ChurchActivity, IChurchActivity } from 'app/shared/model/church-activity.model';
import { ChurchActivityService } from './church-activity.service';
import { ChurchActivityUpdateComponent } from './church-activity-update.component';
import { ChurchResolve } from 'app/entities/chuch-service/chuch-service.route';

@Injectable({ providedIn: 'root' })
export class ChurchActivityResolve implements Resolve<IChurchActivity> {
  constructor(private service: ChurchActivityService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IChurchActivity> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ChurchActivity>) => response.ok),
        map((churchActivity: HttpResponse<ChurchActivity>) => churchActivity.body)
      );
    }
    return of(new ChurchActivity());
  }
}

export const churchActivityRoute: Routes = [
  {
    path: ':churchId/new',
    component: ChurchActivityUpdateComponent,
    resolve: {
      churchActivity: ChurchActivityResolve,
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.churchActivity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':churchId/:id/edit',
    component: ChurchActivityUpdateComponent,
    resolve: {
      churchActivity: ChurchActivityResolve,
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.churchActivity.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const churchActivityPopupRoute: Routes = [];
