import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { MemberPromiseDetailComponent } from 'app/entities/member-promise/member-promise-detail.component';
import { MemberPromise } from 'app/shared/model/member-promise.model';

describe('Component Tests', () => {
  describe('MemberPromise Management Detail Component', () => {
    let comp: MemberPromiseDetailComponent;
    let fixture: ComponentFixture<MemberPromiseDetailComponent>;
    const route = ({ data: of({ memberPromise: new MemberPromise(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberPromiseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MemberPromiseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MemberPromiseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.memberPromise).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
