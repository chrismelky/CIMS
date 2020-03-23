import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { FinancialYearDetailComponent } from 'app/entities/financial-year/financial-year-detail.component';
import { FinancialYear } from 'app/shared/model/financial-year.model';

describe('Component Tests', () => {
  describe('FinancialYear Management Detail Component', () => {
    let comp: FinancialYearDetailComponent;
    let fixture: ComponentFixture<FinancialYearDetailComponent>;
    const route = ({ data: of({ financialYear: new FinancialYear(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [FinancialYearDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FinancialYearDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FinancialYearDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load financialYear on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.financialYear).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
