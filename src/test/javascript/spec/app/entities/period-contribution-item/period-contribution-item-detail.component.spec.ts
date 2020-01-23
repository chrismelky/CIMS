import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { PeriodContributionItemDetailComponent } from 'app/entities/period-contribution-item/period-contribution-item-detail.component';
import { PeriodContributionItem } from 'app/shared/model/period-contribution-item.model';

describe('Component Tests', () => {
  describe('PeriodContributionItem Management Detail Component', () => {
    let comp: PeriodContributionItemDetailComponent;
    let fixture: ComponentFixture<PeriodContributionItemDetailComponent>;
    const route = ({ data: of({ periodContributionItem: new PeriodContributionItem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodContributionItemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PeriodContributionItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeriodContributionItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.periodContributionItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
