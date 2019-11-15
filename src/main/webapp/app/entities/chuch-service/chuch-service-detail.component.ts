import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IChuchService } from 'app/shared/model/chuch-service.model';

@Component({
  selector: 'church-chuch-service-detail',
  templateUrl: './chuch-service-detail.component.html'
})
export class ChuchServiceDetailComponent implements OnInit {
  chuchService: IChuchService;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ chuchService }) => {
      this.chuchService = chuchService;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
