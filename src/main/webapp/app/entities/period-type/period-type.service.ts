import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPeriodType } from 'app/shared/model/period-type.model';

type EntityResponseType = HttpResponse<IPeriodType>;
type EntityArrayResponseType = HttpResponse<IPeriodType[]>;

@Injectable({ providedIn: 'root' })
export class PeriodTypeService {
  public resourceUrl = SERVER_API_URL + 'api/period-types';

  constructor(protected http: HttpClient) {}

  create(periodType: IPeriodType): Observable<EntityResponseType> {
    return this.http.post<IPeriodType>(this.resourceUrl, periodType, { observe: 'response' });
  }

  update(periodType: IPeriodType): Observable<EntityResponseType> {
    return this.http.put<IPeriodType>(this.resourceUrl, periodType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeriodType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeriodType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
