import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { MemberPromiseService } from 'app/entities/member-promise/member-promise.service';
import { IMemberPromise, MemberPromise } from 'app/shared/model/member-promise.model';

describe('Service Tests', () => {
  describe('MemberPromise Service', () => {
    let injector: TestBed;
    let service: MemberPromiseService;
    let httpMock: HttpTestingController;
    let elemDefault: IMemberPromise;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(MemberPromiseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new MemberPromise(0, currentDate, 0, 'AAAAAAA', currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            promiseDate: currentDate.format(DATE_FORMAT),
            fulfillmentDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a MemberPromise', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            promiseDate: currentDate.format(DATE_FORMAT),
            fulfillmentDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            promiseDate: currentDate,
            fulfillmentDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new MemberPromise(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a MemberPromise', () => {
        const returnedFromService = Object.assign(
          {
            promiseDate: currentDate.format(DATE_FORMAT),
            amount: 1,
            otherPromise: 'BBBBBB',
            fulfillmentDate: currentDate.format(DATE_FORMAT),
            isFulfilled: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            promiseDate: currentDate,
            fulfillmentDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of MemberPromise', () => {
        const returnedFromService = Object.assign(
          {
            promiseDate: currentDate.format(DATE_FORMAT),
            amount: 1,
            otherPromise: 'BBBBBB',
            fulfillmentDate: currentDate.format(DATE_FORMAT),
            isFulfilled: true
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            promiseDate: currentDate,
            fulfillmentDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MemberPromise', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
