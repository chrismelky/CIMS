import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { createRequestOption } from 'app/shared/util/request-util';

type EntityArrayResponseType = HttpResponse<any[]>;

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  public resourceUrl = SERVER_API_URL + 'api/dashboard';

  constructor(protected http: HttpClient) {}

  getContribution(churchId: number, periodId: number): Observable<EntityArrayResponseType> {
    const url = this.resourceUrl + '/contribution/' + churchId + '/' + periodId;
    return this.http.get<any[]>(url, { observe: 'response' });
  }

  getMemberContribution(churchId: number, periodId: number, contTypeId: number, opt?): Observable<EntityArrayResponseType> {
    const url = this.resourceUrl + '/member-contribution/' + churchId + '/' + periodId + '/' + contTypeId;
    const options = createRequestOption(opt);
    return this.http.get<any[]>(url, { params: options, observe: 'response' });
  }
}
