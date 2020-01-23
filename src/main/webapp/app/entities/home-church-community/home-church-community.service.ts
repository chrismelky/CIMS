import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IHomeChurchCommunity } from 'app/shared/model/home-church-community.model';

type EntityResponseType = HttpResponse<IHomeChurchCommunity>;
type EntityArrayResponseType = HttpResponse<IHomeChurchCommunity[]>;

@Injectable({ providedIn: 'root' })
export class HomeChurchCommunityService {
  public resourceUrl = SERVER_API_URL + 'api/home-church-communities';

  constructor(protected http: HttpClient) {}

  create(homeChurchCommunity: IHomeChurchCommunity): Observable<EntityResponseType> {
    return this.http.post<IHomeChurchCommunity>(this.resourceUrl, homeChurchCommunity, { observe: 'response' });
  }

  update(homeChurchCommunity: IHomeChurchCommunity): Observable<EntityResponseType> {
    return this.http.put<IHomeChurchCommunity>(this.resourceUrl, homeChurchCommunity, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHomeChurchCommunity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHomeChurchCommunity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
