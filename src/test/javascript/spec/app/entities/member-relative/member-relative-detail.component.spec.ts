import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { MemberRelativeDetailComponent } from 'app/entities/member-relative/member-relative-detail.component';
import { MemberRelative } from 'app/shared/model/member-relative.model';

describe('Component Tests', () => {
  describe('MemberRelative Management Detail Component', () => {
    let comp: MemberRelativeDetailComponent;
    let fixture: ComponentFixture<MemberRelativeDetailComponent>;
    const route = ({ data: of({ memberRelative: new MemberRelative(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberRelativeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MemberRelativeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MemberRelativeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.memberRelative).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
