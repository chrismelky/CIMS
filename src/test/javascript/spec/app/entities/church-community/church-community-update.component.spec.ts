import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChurchCommunityUpdateComponent } from 'app/entities/church-community/church-community-update.component';
import { ChurchCommunityService } from 'app/entities/church-community/church-community.service';
import { ChurchCommunity } from 'app/shared/model/church-community.model';

describe('Component Tests', () => {
  describe('ChurchCommunity Management Update Component', () => {
    let comp: ChurchCommunityUpdateComponent;
    let fixture: ComponentFixture<ChurchCommunityUpdateComponent>;
    let service: ChurchCommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchCommunityUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChurchCommunityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChurchCommunityUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChurchCommunityService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChurchCommunity(123);
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
        const entity = new ChurchCommunity();
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
