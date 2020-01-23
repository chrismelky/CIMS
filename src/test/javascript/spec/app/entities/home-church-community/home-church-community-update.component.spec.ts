import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { HomeChurchCommunityUpdateComponent } from 'app/entities/home-church-community/home-church-community-update.component';
import { HomeChurchCommunityService } from 'app/entities/home-church-community/home-church-community.service';
import { HomeChurchCommunity } from 'app/shared/model/home-church-community.model';

describe('Component Tests', () => {
  describe('HomeChurchCommunity Management Update Component', () => {
    let comp: HomeChurchCommunityUpdateComponent;
    let fixture: ComponentFixture<HomeChurchCommunityUpdateComponent>;
    let service: HomeChurchCommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [HomeChurchCommunityUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HomeChurchCommunityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HomeChurchCommunityUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HomeChurchCommunityService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new HomeChurchCommunity(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new HomeChurchCommunity();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
