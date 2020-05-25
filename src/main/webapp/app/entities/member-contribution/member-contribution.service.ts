import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMemberContribution } from 'app/shared/model/member-contribution.model';

type EntityResponseType = HttpResponse<IMemberContribution>;
type EntityArrayResponseType = HttpResponse<IMemberContribution[]>;

@Injectable({ providedIn: 'root' })
export class MemberContributionService {
  public resourceUrl = SERVER_API_URL + 'api/member-contributions';

  constructor(protected http: HttpClient) {}

  create(memberContribution: IMemberContribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberContribution);
    return this.http
      .post<IMemberContribution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(memberContribution: IMemberContribution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberContribution);
    return this.http
      .put<IMemberContribution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMemberContribution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findByPromise(promiseId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMemberContribution[]>(`${this.resourceUrl}/by-promise/${promiseId}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMemberContribution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(memberContribution: IMemberContribution): IMemberContribution {
    const copy: IMemberContribution = Object.assign({}, memberContribution, {
      paymentDate:
        memberContribution.paymentDate != null && memberContribution.paymentDate.isValid()
          ? memberContribution.paymentDate.format(DATE_FORMAT)
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.paymentDate = res.body.paymentDate != null ? moment(res.body.paymentDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((memberContribution: IMemberContribution) => {
        memberContribution.paymentDate = memberContribution.paymentDate != null ? moment(memberContribution.paymentDate) : null;
      });
    }
    return res;
  }
}
