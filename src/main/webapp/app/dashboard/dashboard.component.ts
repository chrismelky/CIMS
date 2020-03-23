import { Component, OnInit } from '@angular/core';
import { DashboardService } from 'app/dashboard/dashboard.service';
import { IUser, User } from 'app/core/user/user.model';
import { LocalStorageService } from 'ngx-webstorage';
import { ContributionType, IContributionType } from 'app/shared/model/contribution-type.model';
import { IPeriod, Period } from 'app/shared/model/period.model';
import { ContributionTypeService } from 'app/entities/contribution-type/contribution-type.service';
import { PeriodService } from 'app/entities/period/period.service';
import { IChurch } from 'app/shared/model/church.model';
import { IPeriodContributionType } from 'app/shared/model/period-contribution-type.model';
import { PeriodContributionTypeService } from 'app/entities/period-contribution-type/period-contribution-type.service';

@Component({
  selector: 'church-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  category = [];
  promises = [];
  collections = [];
  memberContr = [];
  dataSource = {
    chart: {
      theme: 'fusion',
      caption: 'Comparison of Contribution Promise and Collection',
      xAxisname: 'Contribution Type',
      yAxisName: 'Amount (In TZs)',
      numberPrefix: 'TZs',
      plotFillAlpha: '80'
    },
    categories: [],
    dataset: []
  };
  page = 1;
  totalItems = 100;
  itemsPerPage = 10;
  user: IUser;
  church: IChurch;
  period: IPeriod;
  contributionType: IPeriodContributionType;
  contributionTypes: IPeriodContributionType[] = [];
  periods: IPeriod[] = [];

  constructor(
    private service: DashboardService,
    private $localStorage: LocalStorageService,
    private contributionTypeService: PeriodContributionTypeService,
    private periodService: PeriodService
  ) {
    this.user = this.$localStorage.retrieve('user');
    this.church = this.user.church;
  }

  ngOnInit() {
    this.getContributionTypes();
  }

  getContributionTypes() {
    this.contributionTypes = [];
    if (this.church === undefined) {
      return;
    }
    this.contributionTypeService
      .query({
        'churchId.equals': this.church.id
      })
      .subscribe(resp => {
        this.contributionTypes = resp.body;
        if (this.contributionTypes.length) {
          this.contributionType = this.contributionTypes[0];
          this.getPeriods();
        }
      });
  }

  getPeriods() {
    this.periods = [];
    if (this.contributionType === undefined) {
      return;
    }
    this.periodService
      .query({
        'typeId.equals': this.contributionType.periodType.id
      })
      .subscribe(resp => {
        // todo sort by date,
        const periods = resp.body;
        // take max of two
        this.periods = periods.length > 2 ? [periods[0], periods[1]] : periods;
        if (this.periods.length) {
          this.period = this.periods[0];
          this.getContributions();
          this.getMemberContribution();
        }
      });
  }

  getContributions() {
    if (this.church === undefined || this.period === undefined) {
      return;
    }
    this.service.getContribution(this.church.id, this.period.id).subscribe(resp => {
      this.createChartData(resp.body);
    });
  }

  getMemberContribution() {
    if (this.church === undefined || this.contributionType === undefined || this.period === undefined) {
      return;
    }
    const params = {
      size: this.itemsPerPage,
      page: this.page - 1
    };
    this.service.getMemberContribution(this.church.id, this.period.id, this.contributionType.id, params).subscribe(resp => {
      this.memberContr = resp.body;
    });
  }

  createChartData(data: any[]) {
    this.category = [];
    this.promises = [];
    this.collections = [];
    this.dataSource.categories.length = 0;
    this.dataSource.dataset.length = 0;
    data.forEach(d => {
      this.category.push({
        label: d.name
      });
      this.promises.push({
        value: d.promise
      });
      this.collections.push({
        value: d.collection
      });
    });
    this.dataSource.categories.push({
      category: this.category
    });
    this.dataSource.dataset.push({
      seriesname: 'Promise',
      data: this.promises
    });
    this.dataSource.dataset.push({
      seriesname: 'Collection',
      data: this.collections
    });
  }

  loadPage(page: number) {
    this.page = page;
    this.getMemberContribution();
  }

  setContributionType(t: IPeriodContributionType) {
    if (t.id !== this.contributionType.id) {
      this.contributionType = { ...t };
      this.getPeriods();
    }
  }

  setPeriod(p: IPeriod) {
    if (p.id !== this.period.id) {
      this.period = { ...p };
      this.getMemberContribution();
    }
  }
}
