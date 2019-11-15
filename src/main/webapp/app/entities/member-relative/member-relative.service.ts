import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMemberRelative } from 'app/shared/model/member-relative.model';

type EntityResponseType = HttpResponse<IMemberRelative>;
type EntityArrayResponseType = HttpResponse<IMemberRelative[]>;

@Injectable({ providedIn: 'root' })
export class MemberRelativeService {
  public resourceUrl = SERVER_API_URL + 'api/member-relatives';

  constructor(protected http: HttpClient) {}

  create(memberRelative: IMemberRelative): Observable<EntityResponseType> {
    return this.http.post<IMemberRelative>(this.resourceUrl, memberRelative, { observe: 'response' });
  }

  update(memberRelative: IMemberRelative): Observable<EntityResponseType> {
    return this.http.put<IMemberRelative>(this.resourceUrl, memberRelative, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMemberRelative>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMemberRelative[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
