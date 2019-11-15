import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ContributionType } from 'app/shared/model/contribution-type.model';
import { ContributionTypeService } from './contribution-type.service';
import { ContributionTypeComponent } from './contribution-type.component';
import { ContributionTypeDetailComponent } from './contribution-type-detail.component';
import { ContributionTypeUpdateComponent } from './contribution-type-update.component';
import { ContributionTypeDeletePopupComponent } from './contribution-type-delete-dialog.component';
import { IContributionType } from 'app/shared/model/contribution-type.model';

@Injectable({ providedIn: 'root' })
export class ContributionTypeResolve implements Resolve<IContributionType> {
  constructor(private service: ContributionTypeService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IContributionType> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ContributionType>) => response.ok),
        map((contributionType: HttpResponse<ContributionType>) => contributionType.body)
      );
    }
    return of(new ContributionType());
  }
}

export const contributionTypeRoute: Routes = [
  {
    path: '',
    component: ContributionTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.contributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ContributionTypeDetailComponent,
    resolve: {
      contributionType: ContributionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.contributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ContributionTypeUpdateComponent,
    resolve: {
      contributionType: ContributionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.contributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ContributionTypeUpdateComponent,
    resolve: {
      contributionType: ContributionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.contributionType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const contributionTypePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ContributionTypeDeletePopupComponent,
    resolve: {
      contributionType: ContributionTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'churchApp.contributionType.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
