import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { FinancialYear, IFinancialYear } from 'app/shared/model/financial-year.model';
import { FinancialYearService } from './financial-year.service';

@Component({
  selector: 'church-financial-year-update',
  templateUrl: './financial-year-update.component.html'
})
export class FinancialYearUpdateComponent implements OnInit {
  isSaving = false;
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(4)]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]]
  });

  constructor(protected financialYearService: FinancialYearService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ financialYear }) => {
      this.updateForm(financialYear);
    });
  }

  updateForm(financialYear: IFinancialYear): void {
    this.editForm.patchValue({
      id: financialYear.id,
      name: financialYear.name,
      startDate: financialYear.startDate,
      endDate: financialYear.endDate
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const financialYear = this.createFromForm();
    if (financialYear.id !== undefined) {
      this.subscribeToSaveResponse(this.financialYearService.update(financialYear));
    } else {
      this.subscribeToSaveResponse(this.financialYearService.create(financialYear));
    }
  }

  private createFromForm(): IFinancialYear {
    return {
      ...new FinancialYear(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      startDate: this.editForm.get(['startDate']).value,
      endDate: this.editForm.get(['endDate']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinancialYear>>): void {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
