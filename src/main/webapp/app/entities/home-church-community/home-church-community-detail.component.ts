import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHomeChurchCommunity } from 'app/shared/model/home-church-community.model';

@Component({
  selector: 'church-home-church-community-detail',
  templateUrl: './home-church-community-detail.component.html'
})
export class HomeChurchCommunityDetailComponent implements OnInit {
  homeChurchCommunity: IHomeChurchCommunity;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ homeChurchCommunity }) => {
      this.homeChurchCommunity = homeChurchCommunity;
    });
  }

  previousState() {
    window.history.back();
  }
}
