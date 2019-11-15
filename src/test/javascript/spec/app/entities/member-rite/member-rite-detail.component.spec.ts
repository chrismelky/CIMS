import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { MemberRiteDetailComponent } from 'app/entities/member-rite/member-rite-detail.component';
import { MemberRite } from 'app/shared/model/member-rite.model';

describe('Component Tests', () => {
  describe('MemberRite Management Detail Component', () => {
    let comp: MemberRiteDetailComponent;
    let fixture: ComponentFixture<MemberRiteDetailComponent>;
    const route = ({ data: of({ memberRite: new MemberRite(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberRiteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MemberRiteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MemberRiteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.memberRite).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
