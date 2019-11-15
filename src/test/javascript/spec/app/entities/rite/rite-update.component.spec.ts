import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { RiteUpdateComponent } from 'app/entities/rite/rite-update.component';
import { RiteService } from 'app/entities/rite/rite.service';
import { Rite } from 'app/shared/model/rite.model';

describe('Component Tests', () => {
  describe('Rite Management Update Component', () => {
    let comp: RiteUpdateComponent;
    let fixture: ComponentFixture<RiteUpdateComponent>;
    let service: RiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [RiteUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RiteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RiteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RiteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Rite(123);
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
        const entity = new Rite();
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
