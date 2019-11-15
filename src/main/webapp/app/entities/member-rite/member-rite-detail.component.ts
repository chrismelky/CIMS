import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMemberRite } from 'app/shared/model/member-rite.model';

@Component({
  selector: 'church-member-rite-detail',
  templateUrl: './member-rite-detail.component.html'
})
export class MemberRiteDetailComponent implements OnInit {
  memberRite: IMemberRite;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ memberRite }) => {
      this.memberRite = memberRite;
    });
  }

  previousState() {
    window.history.back();
  }
}
