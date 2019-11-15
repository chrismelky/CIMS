import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMemberContribution } from 'app/shared/model/member-contribution.model';

@Component({
  selector: 'church-member-contribution-detail',
  templateUrl: './member-contribution-detail.component.html'
})
export class MemberContributionDetailComponent implements OnInit {
  memberContribution: IMemberContribution;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ memberContribution }) => {
      this.memberContribution = memberContribution;
    });
  }

  previousState() {
    window.history.back();
  }
}
