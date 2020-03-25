import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ChuchService, IChuchService } from 'app/shared/model/chuch-service.model';
import { ChuchServiceService } from './chuch-service.service';
import { IChurch } from 'app/shared/model/church.model';
import { ChurchService } from 'app/entities/church/church.service';

@Component({
  selector: 'church-chuch-service-update',
  templateUrl: './chuch-service-update.component.html'
})
export class ChuchServiceUpdateComponent implements OnInit {
  isSaving: boolean;
  church: IChurch;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    day: [],
    startTime: [],
    endTime: [],
    church: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected chuchServiceService: ChuchServiceService,
    protected churchService: ChurchService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ chuchService, church }) => {
      this.church = church;
      this.updateForm(chuchService);
    });
  }

  updateForm(chuchService: IChuchService) {
    this.editForm.patchValue({
      id: chuchService.id,
      name: chuchService.name,
      description: chuchService.description,
      day: chuchService.day,
      startTime: chuchService.startTime,
      endTime: chuchService.endTime,
      church: { id: this.church.id, name: this.church.name }
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const chuchService = this.createFromForm();
    if (chuchService.id !== undefined) {
      this.subscribeToSaveResponse(this.chuchServiceService.update(chuchService));
    } else {
      this.subscribeToSaveResponse(this.chuchServiceService.create(chuchService));
    }
  }

  private createFromForm(): IChuchService {
    return {
      ...new ChuchService(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      day: this.editForm.get(['day']).value,
      startTime: this.editForm.get(['startTime']).value,
      endTime: this.editForm.get(['endTime']).value,
      church: this.editForm.get(['church']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChuchService>>) {
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
