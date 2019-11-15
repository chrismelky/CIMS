import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChurchTypeDetailComponent } from 'app/entities/church-type/church-type-detail.component';
import { ChurchType } from 'app/shared/model/church-type.model';

describe('Component Tests', () => {
  describe('ChurchType Management Detail Component', () => {
    let comp: ChurchTypeDetailComponent;
    let fixture: ComponentFixture<ChurchTypeDetailComponent>;
    const route = ({ data: of({ churchType: new ChurchType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChurchTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChurchTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.churchType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
