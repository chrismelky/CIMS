import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { MemberContributionDetailComponent } from 'app/entities/member-contribution/member-contribution-detail.component';
import { MemberContribution } from 'app/shared/model/member-contribution.model';

describe('Component Tests', () => {
  describe('MemberContribution Management Detail Component', () => {
    let comp: MemberContributionDetailComponent;
    let fixture: ComponentFixture<MemberContributionDetailComponent>;
    const route = ({ data: of({ memberContribution: new MemberContribution(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberContributionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MemberContributionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MemberContributionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.memberContribution).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
