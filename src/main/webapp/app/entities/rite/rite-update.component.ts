import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IRite, Rite } from 'app/shared/model/rite.model';
import { RiteService } from './rite.service';

@Component({
  selector: 'church-rite-update',
  templateUrl: './rite-update.component.html'
})
export class RiteUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    code: [],
    name: [null, [Validators.required]]
  });

  constructor(protected riteService: RiteService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ rite }) => {
      this.updateForm(rite);
    });
  }

  updateForm(rite: IRite) {
    this.editForm.patchValue({
      id: rite.id,
      code: rite.code,
      name: rite.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const rite = this.createFromForm();
    if (rite.id !== undefined) {
      this.subscribeToSaveResponse(this.riteService.update(rite));
    } else {
      this.subscribeToSaveResponse(this.riteService.create(rite));
    }
  }

  private createFromForm(): IRite {
    return {
      ...new Rite(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRite>>) {
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
