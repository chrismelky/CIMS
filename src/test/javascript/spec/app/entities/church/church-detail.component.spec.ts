import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChurchDetailComponent } from 'app/entities/church/church-detail.component';
import { Church } from 'app/shared/model/church.model';

describe('Component Tests', () => {
  describe('Church Management Detail Component', () => {
    let comp: ChurchDetailComponent;
    let fixture: ComponentFixture<ChurchDetailComponent>;
    const route = ({ data: of({ church: new Church(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChurchDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChurchDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.church).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
