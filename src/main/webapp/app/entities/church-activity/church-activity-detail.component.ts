import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChurchActivity } from 'app/shared/model/church-activity.model';

@Component({
  selector: 'church-church-activity-detail',
  templateUrl: './church-activity-detail.component.html'
})
export class ChurchActivityDetailComponent implements OnInit {
  churchActivity: IChurchActivity;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ churchActivity }) => {
      this.churchActivity = churchActivity;
    });
  }

  previousState() {
    window.history.back();
  }
}
