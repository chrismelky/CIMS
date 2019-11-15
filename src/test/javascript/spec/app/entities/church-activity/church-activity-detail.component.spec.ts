import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChurchActivityDetailComponent } from 'app/entities/church-activity/church-activity-detail.component';
import { ChurchActivity } from 'app/shared/model/church-activity.model';

describe('Component Tests', () => {
  describe('ChurchActivity Management Detail Component', () => {
    let comp: ChurchActivityDetailComponent;
    let fixture: ComponentFixture<ChurchActivityDetailComponent>;
    const route = ({ data: of({ churchActivity: new ChurchActivity(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchActivityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChurchActivityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChurchActivityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.churchActivity).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
