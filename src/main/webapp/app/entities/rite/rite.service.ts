import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRite } from 'app/shared/model/rite.model';

type EntityResponseType = HttpResponse<IRite>;
type EntityArrayResponseType = HttpResponse<IRite[]>;

@Injectable({ providedIn: 'root' })
export class RiteService {
  public resourceUrl = SERVER_API_URL + 'api/rites';

  constructor(protected http: HttpClient) {}

  create(rite: IRite): Observable<EntityResponseType> {
    return this.http.post<IRite>(this.resourceUrl, rite, { observe: 'response' });
  }

  update(rite: IRite): Observable<EntityResponseType> {
    return this.http.put<IRite>(this.resourceUrl, rite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
