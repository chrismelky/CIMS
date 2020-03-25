import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { Church, IChurch } from 'app/shared/model/church.model';
import { ChurchService } from './church.service';
import { IChurchType } from 'app/shared/model/church-type.model';
import { ChurchTypeService } from 'app/entities/church-type/church-type.service';

@Component({
  selector: 'church-church-update',
  templateUrl: './church-update.component.html'
})
export class ChurchUpdateComponent implements OnInit {
  isSaving: boolean;

  churches: IChurch[];

  churchtypes: IChurchType[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    email: [],
    phoneNumber: [],
    address: [],
    fax: [],
    latitude: [],
    longitude: [],
    otherDetails: [],
    parent: [],
    type: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected churchService: ChurchService,
    protected churchTypeService: ChurchTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ church }) => {
      this.updateForm(church);
    });
    this.churchService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChurch[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurch[]>) => response.body)
      )
      .subscribe((res: IChurch[]) => (this.churches = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.churchTypeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChurchType[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChurchType[]>) => response.body)
      )
      .subscribe((res: IChurchType[]) => (this.churchtypes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(church: IChurch) {
    this.editForm.patchValue({
      id: church.id,
      name: church.name,
      email: church.email,
      phoneNumber: church.phoneNumber,
      address: church.address,
      fax: church.fax,
      latitude: church.latitude,
      longitude: church.longitude,
      otherDetails: church.otherDetails,
      parent: church.parent,
      type: church.type
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const church = this.createFromForm();
    if (church.id !== undefined) {
      this.subscribeToSaveResponse(this.churchService.update(church));
    } else {
      this.subscribeToSaveResponse(this.churchService.create(church));
    }
  }

  private createFromForm(): IChurch {
    return {
      ...new Church(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      email: this.editForm.get(['email']).value,
      phoneNumber: this.editForm.get(['phoneNumber']).value,
      address: this.editForm.get(['address']).value,
      fax: this.editForm.get(['fax']).value,
      latitude: this.editForm.get(['latitude']).value,
      longitude: this.editForm.get(['longitude']).value,
      otherDetails: this.editForm.get(['otherDetails']).value,
      parent: this.editForm.get(['parent']).value,
      type: this.editForm.get(['type']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChurch>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
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

  trackChurchTypeById(index: number, item: IChurchType) {
    return item.id;
  }
}
