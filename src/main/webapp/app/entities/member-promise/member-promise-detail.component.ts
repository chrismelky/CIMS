import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMemberPromise } from 'app/shared/model/member-promise.model';

@Component({
  selector: 'church-member-promise-detail',
  templateUrl: './member-promise-detail.component.html'
})
export class MemberPromiseDetailComponent implements OnInit {
  memberPromise: IMemberPromise;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ memberPromise }) => {
      this.memberPromise = memberPromise;
    });
  }

  previousState() {
    window.history.back();
  }
}
