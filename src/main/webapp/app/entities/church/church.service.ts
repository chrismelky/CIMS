import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChurch } from 'app/shared/model/church.model';

type EntityResponseType = HttpResponse<IChurch>;
type EntityArrayResponseType = HttpResponse<IChurch[]>;

@Injectable({ providedIn: 'root' })
export class ChurchService {
  public resourceUrl = SERVER_API_URL + 'api/churches';

  constructor(protected http: HttpClient) {}

  create(church: IChurch): Observable<EntityResponseType> {
    return this.http.post<IChurch>(this.resourceUrl, church, { observe: 'response' });
  }

  update(church: IChurch): Observable<EntityResponseType> {
    return this.http.put<IChurch>(this.resourceUrl, church, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChurch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChurch[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
