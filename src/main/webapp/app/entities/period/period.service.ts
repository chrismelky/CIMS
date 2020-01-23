import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPeriod } from 'app/shared/model/period.model';

type EntityResponseType = HttpResponse<IPeriod>;
type EntityArrayResponseType = HttpResponse<IPeriod[]>;

@Injectable({ providedIn: 'root' })
export class PeriodService {
  public resourceUrl = SERVER_API_URL + 'api/periods';

  constructor(protected http: HttpClient) {}

  create(period: IPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(period);
    return this.http
      .post<IPeriod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(period: IPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(period);
    return this.http
      .put<IPeriod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPeriod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(period: IPeriod): IPeriod {
    const copy: IPeriod = Object.assign({}, period, {
      startDate: period.startDate != null && period.startDate.isValid() ? period.startDate.format(DATE_FORMAT) : null,
      endDate: period.endDate != null && period.endDate.isValid() ? period.endDate.format(DATE_FORMAT) : null
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
      res.body.forEach((period: IPeriod) => {
        period.startDate = period.startDate != null ? moment(period.startDate) : null;
        period.endDate = period.endDate != null ? moment(period.endDate) : null;
      });
    }
    return res;
  }
}
