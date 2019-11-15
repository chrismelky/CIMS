import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChuchService } from 'app/shared/model/chuch-service.model';

type EntityResponseType = HttpResponse<IChuchService>;
type EntityArrayResponseType = HttpResponse<IChuchService[]>;

@Injectable({ providedIn: 'root' })
export class ChuchServiceService {
  public resourceUrl = SERVER_API_URL + 'api/chuch-services';

  constructor(protected http: HttpClient) {}

  create(chuchService: IChuchService): Observable<EntityResponseType> {
    return this.http.post<IChuchService>(this.resourceUrl, chuchService, { observe: 'response' });
  }

  update(chuchService: IChuchService): Observable<EntityResponseType> {
    return this.http.put<IChuchService>(this.resourceUrl, chuchService, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChuchService>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChuchService[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
