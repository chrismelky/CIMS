import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IChurchType, ChurchType } from 'app/shared/model/church-type.model';
import { ChurchTypeService } from './church-type.service';

@Component({
  selector: 'church-church-type-update',
  templateUrl: './church-type-update.component.html'
})
export class ChurchTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected churchTypeService: ChurchTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ churchType }) => {
      this.updateForm(churchType);
    });
  }

  updateForm(churchType: IChurchType) {
    this.editForm.patchValue({
      id: churchType.id,
      name: churchType.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const churchType = this.createFromForm();
    if (churchType.id !== undefined) {
      this.subscribeToSaveResponse(this.churchTypeService.update(churchType));
    } else {
      this.subscribeToSaveResponse(this.churchTypeService.create(churchType));
    }
  }

  private createFromForm(): IChurchType {
    return {
      ...new ChurchType(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChurchType>>) {
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
