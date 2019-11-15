import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChurchActivity } from 'app/shared/model/church-activity.model';

type EntityResponseType = HttpResponse<IChurchActivity>;
type EntityArrayResponseType = HttpResponse<IChurchActivity[]>;

@Injectable({ providedIn: 'root' })
export class ChurchActivityService {
  public resourceUrl = SERVER_API_URL + 'api/church-activities';

  constructor(protected http: HttpClient) {}

  create(churchActivity: IChurchActivity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(churchActivity);
    return this.http
      .post<IChurchActivity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(churchActivity: IChurchActivity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(churchActivity);
    return this.http
      .put<IChurchActivity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChurchActivity>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChurchActivity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(churchActivity: IChurchActivity): IChurchActivity {
    const copy: IChurchActivity = Object.assign({}, churchActivity, {
      startDate:
        churchActivity.startDate != null && churchActivity.startDate.isValid() ? churchActivity.startDate.format(DATE_FORMAT) : null,
      endDate: churchActivity.endDate != null && churchActivity.endDate.isValid() ? churchActivity.endDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
      res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((churchActivity: IChurchActivity) => {
        churchActivity.startDate = churchActivity.startDate != null ? moment(churchActivity.startDate) : null;
        churchActivity.endDate = churchActivity.endDate != null ? moment(churchActivity.endDate) : null;
      });
    }
    return res;
  }
}
