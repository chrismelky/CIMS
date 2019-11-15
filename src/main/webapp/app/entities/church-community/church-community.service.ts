import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChurchCommunity } from 'app/shared/model/church-community.model';

type EntityResponseType = HttpResponse<IChurchCommunity>;
type EntityArrayResponseType = HttpResponse<IChurchCommunity[]>;

@Injectable({ providedIn: 'root' })
export class ChurchCommunityService {
  public resourceUrl = SERVER_API_URL + 'api/church-communities';

  constructor(protected http: HttpClient) {}

  create(churchCommunity: IChurchCommunity): Observable<EntityResponseType> {
    return this.http.post<IChurchCommunity>(this.resourceUrl, churchCommunity, { observe: 'response' });
  }

  update(churchCommunity: IChurchCommunity): Observable<EntityResponseType> {
    return this.http.put<IChurchCommunity>(this.resourceUrl, churchCommunity, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChurchCommunity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChurchCommunity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
