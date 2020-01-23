import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IPeriodType, PeriodType } from 'app/shared/model/period-type.model';
import { PeriodTypeService } from './period-type.service';

@Component({
  selector: 'church-period-type-update',
  templateUrl: './period-type-update.component.html'
})
export class PeriodTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected periodTypeService: PeriodTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ periodType }) => {
      this.updateForm(periodType);
    });
  }

  updateForm(periodType: IPeriodType) {
    this.editForm.patchValue({
      id: periodType.id,
      name: periodType.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const periodType = this.createFromForm();
    if (periodType.id !== undefined) {
      this.subscribeToSaveResponse(this.periodTypeService.update(periodType));
    } else {
      this.subscribeToSaveResponse(this.periodTypeService.create(periodType));
    }
  }

  private createFromForm(): IPeriodType {
    return {
      ...new PeriodType(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodType>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
