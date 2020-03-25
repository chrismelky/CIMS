import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ContributionType, IContributionType } from 'app/shared/model/contribution-type.model';
import { ContributionTypeService } from './contribution-type.service';

@Component({
  selector: 'church-contribution-type-update',
  templateUrl: './contribution-type-update.component.html'
})
export class ContributionTypeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    code: [],
    name: [null, [Validators.required]]
  });

  constructor(
    protected contributionTypeService: ContributionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ contributionType }) => {
      this.updateForm(contributionType);
    });
  }

  updateForm(contributionType: IContributionType) {
    this.editForm.patchValue({
      id: contributionType.id,
      code: contributionType.code,
      name: contributionType.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const contributionType = this.createFromForm();
    if (contributionType.id !== undefined) {
      this.subscribeToSaveResponse(this.contributionTypeService.update(contributionType));
    } else {
      this.subscribeToSaveResponse(this.contributionTypeService.create(contributionType));
    }
  }

  private createFromForm(): IContributionType {
    return {
      ...new ContributionType(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContributionType>>) {
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
