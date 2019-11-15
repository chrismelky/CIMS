import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChuchServiceDetailComponent } from 'app/entities/chuch-service/chuch-service-detail.component';
import { ChuchService } from 'app/shared/model/chuch-service.model';

describe('Component Tests', () => {
  describe('ChuchService Management Detail Component', () => {
    let comp: ChuchServiceDetailComponent;
    let fixture: ComponentFixture<ChuchServiceDetailComponent>;
    const route = ({ data: of({ chuchService: new ChuchService(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChuchServiceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChuchServiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChuchServiceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chuchService).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
