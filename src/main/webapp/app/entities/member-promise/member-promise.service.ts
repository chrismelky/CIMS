import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
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

  getOne(churchId: number, memberId: number, typeId: number, fyId: number): Observable<EntityResponseType> {
    return this.http
      .get<IMemberPromise>(`${this.resourceUrl}/${churchId}/${memberId}/${typeId}/${fyId}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMemberPromise[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(memberPromise: IMemberPromise): IMemberPromise {
    const copy: IMemberPromise = Object.assign({}, memberPromise, {
      promiseDate:
        memberPromise.promiseDate && memberPromise.promiseDate.isValid() ? memberPromise.promiseDate.format(DATE_FORMAT) : undefined,
      fulfillmentDate:
        memberPromise.fulfillmentDate && memberPromise.fulfillmentDate.isValid()
          ? memberPromise.fulfillmentDate.format(DATE_FORMAT)
          : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.promiseDate = res.body.promiseDate ? moment(res.body.promiseDate) : undefined;
      res.body.fulfillmentDate = res.body.fulfillmentDate ? moment(res.body.fulfillmentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((memberPromise: IMemberPromise) => {
        memberPromise.promiseDate = memberPromise.promiseDate ? moment(memberPromise.promiseDate) : undefined;
        memberPromise.fulfillmentDate = memberPromise.fulfillmentDate ? moment(memberPromise.fulfillmentDate) : undefined;
      });
    }
    return res;
  }
}
