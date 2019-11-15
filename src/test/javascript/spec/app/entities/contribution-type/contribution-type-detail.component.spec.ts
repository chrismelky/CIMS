import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ContributionTypeDetailComponent } from 'app/entities/contribution-type/contribution-type-detail.component';
import { ContributionType } from 'app/shared/model/contribution-type.model';

describe('Component Tests', () => {
  describe('ContributionType Management Detail Component', () => {
    let comp: ContributionTypeDetailComponent;
    let fixture: ComponentFixture<ContributionTypeDetailComponent>;
    const route = ({ data: of({ contributionType: new ContributionType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ContributionTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ContributionTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContributionTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contributionType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
