import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { PeriodContributionDetailComponent } from 'app/entities/period-contribution/period-contribution-detail.component';
import { PeriodContribution } from 'app/shared/model/period-contribution.model';

describe('Component Tests', () => {
  describe('PeriodContribution Management Detail Component', () => {
    let comp: PeriodContributionDetailComponent;
    let fixture: ComponentFixture<PeriodContributionDetailComponent>;
    const route = ({ data: of({ periodContribution: new PeriodContribution(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodContributionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PeriodContributionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeriodContributionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.periodContribution).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
