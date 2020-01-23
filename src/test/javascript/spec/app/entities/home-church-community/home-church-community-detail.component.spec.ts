import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { HomeChurchCommunityDetailComponent } from 'app/entities/home-church-community/home-church-community-detail.component';
import { HomeChurchCommunity } from 'app/shared/model/home-church-community.model';

describe('Component Tests', () => {
  describe('HomeChurchCommunity Management Detail Component', () => {
    let comp: HomeChurchCommunityDetailComponent;
    let fixture: ComponentFixture<HomeChurchCommunityDetailComponent>;
    const route = ({ data: of({ homeChurchCommunity: new HomeChurchCommunity(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [HomeChurchCommunityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HomeChurchCommunityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HomeChurchCommunityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.homeChurchCommunity).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
