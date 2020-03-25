import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IMemberContribution, MemberContribution } from 'app/shared/model/member-contribution.model';
import { MemberContributionService } from './member-contribution.service';
import { MemberContributionDetailComponent } from './member-contribution-detail.component';
import { MemberContributionUpdateComponent } from './member-contribution-update.component';
import { MemberIdResolve } from 'app/entities/member/member.route';

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
    path: ':id/view',
    component: MemberContributionDetailComponent,
    resolve: {
      memberContribution: MemberContributionResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.memberContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':memberId/new',
    component: MemberContributionUpdateComponent,
    resolve: {
      memberContribution: MemberContributionResolve,
      member: MemberIdResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.memberContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':memberId/:id/edit',
    component: MemberContributionUpdateComponent,
    resolve: {
      memberContribution: MemberContributionResolve,
      member: MemberIdResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.memberContribution.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const memberContributionPopupRoute: Routes = [];
