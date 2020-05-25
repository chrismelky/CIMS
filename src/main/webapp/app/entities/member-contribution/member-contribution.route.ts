import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IMemberContribution, MemberContribution } from 'app/shared/model/member-contribution.model';
import { MemberContributionService } from './member-contribution.service';
import { MemberContributionUpdateComponent } from './member-contribution-update.component';
import { MemberPromiseIdResolve } from '../member-promise/member-promise.route';

@Injectable({ providedIn: 'root' })
export class MemberContributionResolve implements Resolve<IMemberContribution> {
  constructor(private service: MemberContributionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMemberContribution> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MemberContribution>) => response.ok),
        map((memberContribution: HttpResponse<MemberContribution>) => memberContribution.body)
      );
    }
    return of(new MemberContribution());
  }
}

export const memberContributionRoute: Routes = [
  {
    path: ':memberPromiseId/new',
    component: MemberContributionUpdateComponent,
    resolve: {
      memberContribution: MemberContributionResolve,
      memberPromise: MemberPromiseIdResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.memberContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':memberPromiseId/:id/edit',
    component: MemberContributionUpdateComponent,
    resolve: {
      memberContribution: MemberContributionResolve,
      memberPromise: MemberPromiseIdResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.memberContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const memberContributionPopupRoute: Routes = [];
