import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { PeriodContributionTypeDetailComponent } from 'app/entities/period-contribution-type/period-contribution-type-detail.component';
import { PeriodContributionType } from 'app/shared/model/period-contribution-type.model';

describe('Component Tests', () => {
  describe('PeriodContributionType Management Detail Component', () => {
    let comp: PeriodContributionTypeDetailComponent;
    let fixture: ComponentFixture<PeriodContributionTypeDetailComponent>;
    const route = ({ data: of({ periodContributionType: new PeriodContributionType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodContributionTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PeriodContributionTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeriodContributionTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.periodContributionType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
