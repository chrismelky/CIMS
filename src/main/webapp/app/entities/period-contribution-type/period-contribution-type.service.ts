import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPeriodContributionType } from 'app/shared/model/period-contribution-type.model';

type EntityResponseType = HttpResponse<IPeriodContributionType>;
type EntityArrayResponseType = HttpResponse<IPeriodContributionType[]>;

@Injectable({ providedIn: 'root' })
export class PeriodContributionTypeService {
  public resourceUrl = SERVER_API_URL + 'api/period-contribution-types';

  constructor(protected http: HttpClient) {}

  create(periodContributionType: IPeriodContributionType): Observable<EntityResponseType> {
    return this.http.post<IPeriodContributionType>(this.resourceUrl, periodContributionType, { observe: 'response' });
  }

  update(periodContributionType: IPeriodContributionType): Observable<EntityResponseType> {
    return this.http.put<IPeriodContributionType>(this.resourceUrl, periodContributionType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeriodContributionType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeriodContributionType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
