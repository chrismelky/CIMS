import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChurchCommunity } from 'app/shared/model/church-community.model';

@Component({
  selector: 'church-church-community-detail',
  templateUrl: './church-community-detail.component.html'
})
export class ChurchCommunityDetailComponent implements OnInit {
  churchCommunity: IChurchCommunity;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ churchCommunity }) => {
      this.churchCommunity = churchCommunity;
    });
  }

  previousState() {
    window.history.back();
  }
}
