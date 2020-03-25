import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';

import { JhiLanguageHelper } from 'app/core/language/language.helper';
import { AccountService } from 'app/core/auth/account.service';
import { VERSION } from 'app/app.constants';
import { LocalStorageService } from 'ngx-webstorage';
import { User } from 'app/core/user/user.model';

@Component({
  selector: 'church-main',
  templateUrl: './main.component.html'
})
export class ChurchMainComponent implements OnInit {
  version: string;
  user: User;

  constructor(
    private jhiLanguageHelper: JhiLanguageHelper,
    private router: Router,
    private accountService: AccountService,
    private $localStorage: LocalStorageService
  ) {
    this.version = VERSION ? (VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION) : '';
    this.user = this.$localStorage.retrieve('user');
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
    let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'churchApp';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  ngOnInit() {
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
}
