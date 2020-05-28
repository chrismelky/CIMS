import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';

import { JhiLanguageHelper } from 'app/core/language/language.helper';
import { AccountService } from 'app/core/auth/account.service';
import { VERSION } from 'app/app.constants';
import { LocalStorageService } from 'ngx-webstorage';
import { IUser } from 'app/core/user/user.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
  selector: 'church-main',
  templateUrl: './main.component.html'
})
export class ChurchMainComponent implements OnInit, OnDestroy {
  version: string;
  user: IUser;
  eventSubscriber?: Subscription;

  constructor(
    private jhiLanguageHelper: JhiLanguageHelper,
    private router: Router,
    private accountService: AccountService,
    private $localStorage: LocalStorageService,
    protected eventManager: JhiEventManager
  ) {
    this.version = VERSION ? (VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION) : '';
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
    let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'churchApp';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  setUser() {
    this.user = this.$localStorage.retrieve('user');
    console.error(this.user);
  }

  ngOnInit() {
    this.registerChangeLogin();
    this.setUser();
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  registerChangeLogin(): void {
    console.error('Called');
    this.eventSubscriber = this.eventManager.subscribe('authenticationSuccess', () => this.setUser());
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
}
