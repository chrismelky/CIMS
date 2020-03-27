import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IPeriodContributionItem, PeriodContributionItem } from 'app/shared/model/period-contribution-item.model';
import { PeriodContributionItemService } from './period-contribution-item.service';
import { SessionStorageService } from 'ngx-webstorage';
import * as moment from 'moment';

@Component({
  selector: 'church-period-contribution-item-update',
  templateUrl: './period-contribution-item-update.component.html'
})
export class PeriodContributionItemUpdateComponent implements OnInit {
  isSaving: boolean;

  dateReceivedDp: any;
  contributionId: number;

  editForm = this.fb.group({
    id: [],
    amount: [null, [Validators.required]],
    description: [],
    dateReceived: ['2020-03-20', [Validators.required]],
    receivedBy: [null, [Validators.required]],
    periodContribution: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected periodContributionItemService: PeriodContributionItemService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected sessionStorage: SessionStorageService
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.contributionId = this.activatedRoute.snapshot.params['periodContributionId'];
    this.activatedRoute.data.subscribe(({ periodContributionItem }) => {
      this.updateForm(periodContributionItem);
      const date = this.sessionStorage.retrieve('piDate');
      if (date) {
        this.editForm.patchValue({
          dateReceived: moment(new Date(date))
        });
      }
      if (!periodContributionItem.id) {
        console.error(this.contributionId);
        this.editForm.patchValue({
          periodContribution: { id: this.contributionId }
        });
      }
    });
  }

  updateForm(periodContributionItem: IPeriodContributionItem) {
    console.error(periodContributionItem);
    this.editForm.patchValue({
      id: periodContributionItem.id,
      amount: periodContributionItem.amount,
      description: periodContributionItem.description,
      dateReceived: periodContributionItem.dateReceived,
      receivedBy: periodContributionItem.receivedBy,
      periodContribution: periodContributionItem.periodContribution
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const periodContributionItem = this.createFromForm();
    if (periodContributionItem.id !== undefined) {
      this.subscribeToSaveResponse(this.periodContributionItemService.update(periodContributionItem));
    } else {
      this.subscribeToSaveResponse(this.periodContributionItemService.create(periodContributionItem));
    }
  }

  private createFromForm(): IPeriodContributionItem {
    return {
      ...new PeriodContributionItem(),
      id: this.editForm.get(['id']).value,
      amount: this.editForm.get(['amount']).value,
      description: this.editForm.get(['description']).value,
      dateReceived: this.editForm.get(['dateReceived']).value,
      receivedBy: this.editForm.get(['receivedBy']).value,
      periodContribution: this.editForm.get(['periodContribution']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodContributionItem>>) {
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
}
