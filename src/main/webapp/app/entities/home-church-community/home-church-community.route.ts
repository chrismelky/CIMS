import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HomeChurchCommunity, IHomeChurchCommunity } from 'app/shared/model/home-church-community.model';
import { HomeChurchCommunityService } from './home-church-community.service';
import { HomeChurchCommunityUpdateComponent } from './home-church-community-update.component';
import { ChurchResolve } from 'app/entities/chuch-service/chuch-service.route';

@Injectable({ providedIn: 'root' })
export class HomeChurchCommunityResolve implements Resolve<IHomeChurchCommunity> {
  constructor(private service: HomeChurchCommunityService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IHomeChurchCommunity> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<HomeChurchCommunity>) => response.ok),
        map((homeChurchCommunity: HttpResponse<HomeChurchCommunity>) => homeChurchCommunity.body)
      );
    }
    return of(new HomeChurchCommunity());
  }
}

export const homeChurchCommunityRoute: Routes = [
  {
    path: ':churchId/new',
    component: HomeChurchCommunityUpdateComponent,
    resolve: {
      homeChurchCommunity: HomeChurchCommunityResolve,
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.homeChurchCommunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':churchId/:id/edit',
    component: HomeChurchCommunityUpdateComponent,
    resolve: {
      homeChurchCommunity: HomeChurchCommunityResolve,
      church: ChurchResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.homeChurchCommunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const homeChurchCommunityPopupRoute: Routes = [];
