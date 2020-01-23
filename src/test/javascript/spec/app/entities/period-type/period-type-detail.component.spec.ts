import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { PeriodTypeDetailComponent } from 'app/entities/period-type/period-type-detail.component';
import { PeriodType } from 'app/shared/model/period-type.model';

describe('Component Tests', () => {
  describe('PeriodType Management Detail Component', () => {
    let comp: PeriodTypeDetailComponent;
    let fixture: ComponentFixture<PeriodTypeDetailComponent>;
    const route = ({ data: of({ periodType: new PeriodType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PeriodTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeriodTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.periodType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
