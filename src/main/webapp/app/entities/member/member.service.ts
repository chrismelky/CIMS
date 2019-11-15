import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMember } from 'app/shared/model/member.model';

type EntityResponseType = HttpResponse<IMember>;
type EntityArrayResponseType = HttpResponse<IMember[]>;

@Injectable({ providedIn: 'root' })
export class MemberService {
  public resourceUrl = SERVER_API_URL + 'api/members';

  constructor(protected http: HttpClient) {}

  create(member: IMember): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(member);
    return this.http
      .post<IMember>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(member: IMember): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(member);
    return this.http
      .put<IMember>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMember>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findByRn(rn: string): Observable<EntityResponseType> {
    return this.http
      .get<IMember>(`${this.resourceUrl}/byRn/${rn}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMember[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(member: IMember): IMember {
    const copy: IMember = Object.assign({}, member, {
      dateOfBirth: member.dateOfBirth != null && member.dateOfBirth.isValid() ? member.dateOfBirth.format(DATE_FORMAT) : null,
      deceasedDate: member.deceasedDate != null && member.deceasedDate.isValid() ? member.deceasedDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth != null ? moment(res.body.dateOfBirth) : null;
      res.body.deceasedDate = res.body.deceasedDate != null ? moment(res.body.deceasedDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((member: IMember) => {
        member.dateOfBirth = member.dateOfBirth != null ? moment(member.dateOfBirth) : null;
        member.deceasedDate = member.deceasedDate != null ? moment(member.deceasedDate) : null;
      });
    }
    return res;
  }
}
