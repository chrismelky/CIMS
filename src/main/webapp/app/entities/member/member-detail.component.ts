import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMember } from 'app/shared/model/member.model';

@Component({
  selector: 'church-member-detail',
  templateUrl: './member-detail.component.html'
})
export class MemberDetailComponent implements OnInit {
  member: IMember;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.member = member;
    });
  }

  previousState() {
    window.history.back();
  }
}
