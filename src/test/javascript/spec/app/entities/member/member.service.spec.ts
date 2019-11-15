import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { MemberService } from 'app/entities/member/member.service';
import { IMember, Member } from 'app/shared/model/member.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { MaritalStatus } from 'app/shared/model/enumerations/marital-status.model';

describe('Service Tests', () => {
  describe('Member Service', () => {
    let injector: TestBed;
    let service: MemberService;
    let httpMock: HttpTestingController;
    let elemDefault: IMember;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(MemberService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Member(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        Gender.Male,
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        MaritalStatus.Single,
        'AAAAAAA',
        'AAAAAAA',
        false,
        false,
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateOfBith: currentDate.format(DATE_FORMAT),
            deceasedDate: currentDate.format(DATE_FORMAT)
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

      it('should create a Member', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateOfBith: currentDate.format(DATE_FORMAT),
            deceasedDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateOfBith: currentDate,
            deceasedDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Member(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Member', () => {
        const returnedFromService = Object.assign(
          {
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            middleName: 'BBBBBB',
            gender: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            email: 'BBBBBB',
            dateOfBith: currentDate.format(DATE_FORMAT),
            placeOfBith: 'BBBBBB',
            maritalStatus: 'BBBBBB',
            work: 'BBBBBB',
            placeOfWork: 'BBBBBB',
            isActive: true,
            isDeceased: true,
            deceasedDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBith: currentDate,
            deceasedDate: currentDate
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

      it('should return a list of Member', () => {
        const returnedFromService = Object.assign(
          {
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            middleName: 'BBBBBB',
            gender: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            email: 'BBBBBB',
            dateOfBith: currentDate.format(DATE_FORMAT),
            placeOfBith: 'BBBBBB',
            maritalStatus: 'BBBBBB',
            work: 'BBBBBB',
            placeOfWork: 'BBBBBB',
            isActive: true,
            isDeceased: true,
            deceasedDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateOfBith: currentDate,
            deceasedDate: currentDate
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

      it('should delete a Member', () => {
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
