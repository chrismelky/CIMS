import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRite } from 'app/shared/model/rite.model';

@Component({
  selector: 'church-rite-detail',
  templateUrl: './rite-detail.component.html'
})
export class RiteDetailComponent implements OnInit {
  rite: IRite;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ rite }) => {
      this.rite = rite;
    });
  }

  previousState() {
    window.history.back();
  }
}
