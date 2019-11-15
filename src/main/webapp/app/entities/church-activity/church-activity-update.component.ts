import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IChurchActivity, ChurchActivity } from 'app/shared/model/church-activity.model';
import { ChurchActivityService } from './church-activity.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';

@Component({
  selector: 'church-church-activity-update',
  templateUrl: './church-activity-update.component.html'
})
export class ChurchActivityUpdateComponent implements OnInit {
  isSaving: boolean;

  churches: IChurch[];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    location: [],
    estamateBudget: [],
    startDate: [null, [Validators.required]],
    endDate: [],
    church: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected churchActivityService: ChurchActivityService,
    protected churchService: ChurchService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ churchActivity }) => {
      this.updateForm(churchActivity);
    });
    this.churchService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChurch[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurch[]>) => response.body)
      )
      .subscribe((res: IChurch[]) => (this.churches = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(churchActivity: IChurchActivity) {
    this.editForm.patchValue({
      id: churchActivity.id,
      name: churchActivity.name,
      location: churchActivity.location,
      estamateBudget: churchActivity.estamateBudget,
      startDate: churchActivity.startDate,
      endDate: churchActivity.endDate,
      church: churchActivity.church
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const churchActivity = this.createFromForm();
    if (churchActivity.id !== undefined) {
      this.subscribeToSaveResponse(this.churchActivityService.update(churchActivity));
    } else {
      this.subscribeToSaveResponse(this.churchActivityService.create(churchActivity));
    }
  }

  private createFromForm(): IChurchActivity {
    return {
      ...new ChurchActivity(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      location: this.editForm.get(['location']).value,
      estamateBudget: this.editForm.get(['estamateBudget']).value,
      startDate: this.editForm.get(['startDate']).value,
      endDate: this.editForm.get(['endDate']).value,
      church: this.editForm.get(['church']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChurchActivity>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackChurchById(index: number, item: IChurch) {
    return item.id;
  }
}
