import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IMember } from 'app/shared/model/member.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MemberService } from './member.service';
import { FinancialYearService } from '../financial-year/financial-year.service';
import { FinancialYear } from 'app/shared/model/financial-year.model';

@Component({
  selector: 'church-member',
  templateUrl: './member.component.html'
})
export class MemberComponent implements OnInit, OnDestroy {
  currentAccount: any;
  members: IMember[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  churchId: number;
  searchText: string;
  filterBy = 'firstName';
  fyId: string;
  isUploading = false;
  fileToUpload: File;
  fys: FinancialYear[] = [];

  constructor(
    protected memberService: MemberService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected fyService: FinancialYearService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  private getSearchKey() {
    let searchKey = {};
    switch (this.filterBy) {
      case 'firstName':
        searchKey = { 'firstName.contains': this.searchText };
        break;
      case 'lastName':
        searchKey = { 'lastName.contains': this.searchText };
        break;
      case 'middleName':
        searchKey = { 'middleName.contains': this.searchText };
        break;
      case 'churchRn':
        searchKey = { 'churchRn.equals': this.searchText };
        break;
      case 'phoneNumber':
        searchKey = { 'phoneNumber.contains': this.searchText };
        break;
    }
    return searchKey;
  }

  loadAll() {
    const search = this.searchText ? this.getSearchKey() : {};
    const churchFilter = this.churchId === undefined ? {} : { 'churchId.equals': this.churchId };
    this.memberService
      .query({
        ...churchFilter,
        ...search,
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IMember[]>) => this.paginateMembers(res.body, res.headers));
  }

  search(s) {
    this.searchText = s;
    this.loadAll();
  }

  clearSearch(inp) {
    if (inp === '') {
      this.searchText = undefined;
      this.loadAll();
    }
  }

  onFileChange(files: FileList) {
    console.error(files);
    this.fileToUpload = files.item(0);
  }

  upload() {
    if (this.fileToUpload === undefined) {
      console.error('File not selected');
      return;
    }
    this.isUploading = true;
    this.memberService.upload(this.fileToUpload, this.churchId, this.fyId).subscribe(
      res => {
        this.isUploading = false;
        this.loadAll();
      },
      err => {
        console.error(err);
        this.isUploading = false;
      }
    );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/member',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  loadFys() {
    this.fyService.query().subscribe(res => {
      this.fys = res.body;
    });
  }

  ngOnInit() {
    this.churchId = this.activatedRoute.snapshot.params['churchId'];
    this.loadFys();
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInMembers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IMember) {
    return item.id;
  }

  registerChangeInMembers() {
    this.eventSubscriber = this.eventManager.subscribe('memberListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateMembers(data: IMember[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.members = data;
  }
}
