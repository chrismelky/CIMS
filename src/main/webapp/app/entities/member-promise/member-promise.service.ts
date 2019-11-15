import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMemberPromise } from 'app/shared/model/member-promise.model';

type EntityResponseType = HttpResponse<IMemberPromise>;
type EntityArrayResponseType = HttpResponse<IMemberPromise[]>;

@Injectable({ providedIn: 'root' })
export class MemberPromiseService {
  public resourceUrl = SERVER_API_URL + 'api/member-promises';

  constructor(protected http: HttpClient) {}

  create(memberPromise: IMemberPromise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberPromise);
    return this.http
      .post<IMemberPromise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(memberPromise: IMemberPromise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberPromise);
    return this.http
      .put<IMemberPromise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMemberPromise>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMemberPromise[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(memberPromise: IMemberPromise): IMemberPromise {
    const copy: IMemberPromise = Object.assign({}, memberPromise, {
      promiseDate:
        memberPromise.promiseDate != null && memberPromise.promiseDate.isValid() ? memberPromise.promiseDate.format(DATE_FORMAT) : null,
      fulfillmentDate:
        memberPromise.fulfillmentDate != null && memberPromise.fulfillmentDate.isValid()
          ? memberPromise.fulfillmentDate.format(DATE_FORMAT)
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.promiseDate = res.body.promiseDate != null ? moment(res.body.promiseDate) : null;
      res.body.fulfillmentDate = res.body.fulfillmentDate != null ? moment(res.body.fulfillmentDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((memberPromise: IMemberPromise) => {
        memberPromise.promiseDate = memberPromise.promiseDate != null ? moment(memberPromise.promiseDate) : null;
        memberPromise.fulfillmentDate = memberPromise.fulfillmentDate != null ? moment(memberPromise.fulfillmentDate) : null;
      });
    }
    return res;
  }
}
