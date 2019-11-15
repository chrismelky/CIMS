import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMemberRelative } from 'app/shared/model/member-relative.model';

@Component({
  selector: 'church-member-relative-detail',
  templateUrl: './member-relative-detail.component.html'
})
export class MemberRelativeDetailComponent implements OnInit {
  memberRelative: IMemberRelative;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ memberRelative }) => {
      this.memberRelative = memberRelative;
    });
  }

  previousState() {
    window.history.back();
  }
}
