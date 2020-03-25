import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IPeriodContributionItem, PeriodContributionItem } from 'app/shared/model/period-contribution-item.model';
import { PeriodContributionItemService } from './period-contribution-item.service';
import { PeriodContributionItemComponent } from './period-contribution-item.component';
import { PeriodContributionItemDetailComponent } from './period-contribution-item-detail.component';
import { PeriodContributionItemUpdateComponent } from './period-contribution-item-update.component';
import { PeriodContributionItemDeletePopupComponent } from './period-contribution-item-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class PeriodContributionItemResolve implements Resolve<IPeriodContributionItem> {
  constructor(private service: PeriodContributionItemService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPeriodContributionItem> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PeriodContributionItem>) => response.ok),
        map((periodContributionItem: HttpResponse<PeriodContributionItem>) => periodContributionItem.body)
      );
    }
    return of(new PeriodContributionItem());
  }
}

export const periodContributionItemRoute: Routes = [
  {
    path: '',
    component: PeriodContributionItemComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'churchApp.periodContributionItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PeriodContributionItemDetailComponent,
    resolve: {
      periodContributionItem: PeriodContributionItemResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.periodContributionItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'byContribution/:periodContributionId/new',
    component: PeriodContributionItemUpdateComponent,
    resolve: {
      periodContributionItem: PeriodContributionItemResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.periodContributionItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PeriodContributionItemUpdateComponent,
    resolve: {
      periodContributionItem: PeriodContributionItemResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.periodContributionItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const periodContributionItemPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PeriodContributionItemDeletePopupComponent,
    resolve: {
      periodContributionItem: PeriodContributionItemResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_CHURCH_ADMIN'],
      pageTitle: 'churchApp.periodContributionItem.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
