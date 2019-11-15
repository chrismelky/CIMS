import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { RiteDetailComponent } from 'app/entities/rite/rite-detail.component';
import { Rite } from 'app/shared/model/rite.model';

describe('Component Tests', () => {
  describe('Rite Management Detail Component', () => {
    let comp: RiteDetailComponent;
    let fixture: ComponentFixture<RiteDetailComponent>;
    const route = ({ data: of({ rite: new Rite(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [RiteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RiteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RiteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rite).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
