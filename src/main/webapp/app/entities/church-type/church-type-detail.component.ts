import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChurchType } from 'app/shared/model/church-type.model';

@Component({
  selector: 'church-church-type-detail',
  templateUrl: './church-type-detail.component.html'
})
export class ChurchTypeDetailComponent implements OnInit {
  churchType: IChurchType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ churchType }) => {
      this.churchType = churchType;
    });
  }

  previousState() {
    window.history.back();
  }
}
