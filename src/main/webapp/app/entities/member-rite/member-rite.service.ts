import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMemberRite } from 'app/shared/model/member-rite.model';

type EntityResponseType = HttpResponse<IMemberRite>;
type EntityArrayResponseType = HttpResponse<IMemberRite[]>;

@Injectable({ providedIn: 'root' })
export class MemberRiteService {
  public resourceUrl = SERVER_API_URL + 'api/member-rites';

  constructor(protected http: HttpClient) {}

  create(memberRite: IMemberRite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberRite);
    return this.http
      .post<IMemberRite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(memberRite: IMemberRite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberRite);
    return this.http
      .put<IMemberRite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMemberRite>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMemberRite[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(memberRite: IMemberRite): IMemberRite {
    const copy: IMemberRite = Object.assign({}, memberRite, {
      dateReceived:
        memberRite.dateReceived != null && memberRite.dateReceived.isValid() ? memberRite.dateReceived.format(DATE_FORMAT) : null
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
      res.body.forEach((memberRite: IMemberRite) => {
        memberRite.dateReceived = memberRite.dateReceived != null ? moment(memberRite.dateReceived) : null;
      });
    }
    return res;
  }
}
