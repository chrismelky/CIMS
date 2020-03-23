import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFinancialYear } from 'app/shared/model/financial-year.model';

type EntityResponseType = HttpResponse<IFinancialYear>;
type EntityArrayResponseType = HttpResponse<IFinancialYear[]>;

@Injectable({ providedIn: 'root' })
export class FinancialYearService {
  public resourceUrl = SERVER_API_URL + 'api/financial-years';

  constructor(protected http: HttpClient) {}

  create(financialYear: IFinancialYear): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialYear);
    return this.http
      .post<IFinancialYear>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(financialYear: IFinancialYear): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(financialYear);
    return this.http
      .put<IFinancialYear>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFinancialYear>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFinancialYear[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(financialYear: IFinancialYear): IFinancialYear {
    const copy: IFinancialYear = Object.assign({}, financialYear, {
      startDate: financialYear.startDate && financialYear.startDate.isValid() ? financialYear.startDate.format(DATE_FORMAT) : undefined,
      endDate: financialYear.endDate && financialYear.endDate.isValid() ? financialYear.endDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((financialYear: IFinancialYear) => {
        financialYear.startDate = financialYear.startDate ? moment(financialYear.startDate) : undefined;
        financialYear.endDate = financialYear.endDate ? moment(financialYear.endDate) : undefined;
      });
    }
    return res;
  }
}
