import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChurchCommunityDetailComponent } from 'app/entities/church-community/church-community-detail.component';
import { ChurchCommunity } from 'app/shared/model/church-community.model';

describe('Component Tests', () => {
  describe('ChurchCommunity Management Detail Component', () => {
    let comp: ChurchCommunityDetailComponent;
    let fixture: ComponentFixture<ChurchCommunityDetailComponent>;
    const route = ({ data: of({ churchCommunity: new ChurchCommunity(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchCommunityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChurchCommunityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChurchCommunityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.churchCommunity).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
