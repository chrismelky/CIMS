import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPeriodContribution } from 'app/shared/model/period-contribution.model';

type EntityResponseType = HttpResponse<IPeriodContribution>;
type EntityArrayResponseType = HttpResponse<IPeriodContribution[]>;

@Injectable({ providedIn: 'root' })
export class PeriodContributionService {
  public resourceUrl = SERVER_API_URL + 'api/period-contributions';

  constructor(protected http: HttpClient) {}

  create(periodContribution: IPeriodContribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(periodContribution);
    return this.http
      .post<IPeriodContribution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(periodContribution: IPeriodContribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(periodContribution);
    return this.http
      .put<IPeriodContribution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPeriodContribution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPeriodContribution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(periodContribution: IPeriodContribution): IPeriodContribution {
    const copy: IPeriodContribution = Object.assign({}, periodContribution, {
      dueDate:
        periodContribution.dueDate && periodContribution.dueDate.isValid() ? periodContribution.dueDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dueDate = res.body.dueDate ? moment(res.body.dueDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((periodContribution: IPeriodContribution) => {
        periodContribution.dueDate = periodContribution.dueDate ? moment(periodContribution.dueDate) : undefined;
      });
    }
    return res;
  }
}
