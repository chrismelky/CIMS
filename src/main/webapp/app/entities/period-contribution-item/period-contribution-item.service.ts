import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPeriodContributionItem } from 'app/shared/model/period-contribution-item.model';

type EntityResponseType = HttpResponse<IPeriodContributionItem>;
type EntityArrayResponseType = HttpResponse<IPeriodContributionItem[]>;

@Injectable({ providedIn: 'root' })
export class PeriodContributionItemService {
  public resourceUrl = SERVER_API_URL + 'api/period-contribution-items';

  constructor(protected http: HttpClient) {}

  create(periodContributionItem: IPeriodContributionItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(periodContributionItem);
    return this.http
      .post<IPeriodContributionItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(periodContributionItem: IPeriodContributionItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(periodContributionItem);
    return this.http
      .put<IPeriodContributionItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPeriodContributionItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPeriodContributionItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(periodContributionItem: IPeriodContributionItem): IPeriodContributionItem {
    const copy: IPeriodContributionItem = Object.assign({}, periodContributionItem, {
      dateReceived:
        periodContributionItem.dateReceived != null && periodContributionItem.dateReceived.isValid()
          ? periodContributionItem.dateReceived.format(DATE_FORMAT)
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateReceived = res.body.dateReceived != null ? moment(res.body.dateReceived) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((periodContributionItem: IPeriodContributionItem) => {
        periodContributionItem.dateReceived =
          periodContributionItem.dateReceived != null ? moment(periodContributionItem.dateReceived) : null;
      });
    }
    return res;
  }
}
