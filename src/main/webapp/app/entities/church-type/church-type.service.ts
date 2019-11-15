import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChurchType } from 'app/shared/model/church-type.model';

type EntityResponseType = HttpResponse<IChurchType>;
type EntityArrayResponseType = HttpResponse<IChurchType[]>;

@Injectable({ providedIn: 'root' })
export class ChurchTypeService {
  public resourceUrl = SERVER_API_URL + 'api/church-types';

  constructor(protected http: HttpClient) {}

  create(churchType: IChurchType): Observable<EntityResponseType> {
    return this.http.post<IChurchType>(this.resourceUrl, churchType, { observe: 'response' });
  }

  update(churchType: IChurchType): Observable<EntityResponseType> {
    return this.http.put<IChurchType>(this.resourceUrl, churchType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChurchType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChurchType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
