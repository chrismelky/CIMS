import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChurchTypeUpdateComponent } from 'app/entities/church-type/church-type-update.component';
import { ChurchTypeService } from 'app/entities/church-type/church-type.service';
import { ChurchType } from 'app/shared/model/church-type.model';

describe('Component Tests', () => {
  describe('ChurchType Management Update Component', () => {
    let comp: ChurchTypeUpdateComponent;
    let fixture: ComponentFixture<ChurchTypeUpdateComponent>;
    let service: ChurchTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChurchTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChurchTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChurchTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChurchType(123);
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
        const entity = new ChurchType();
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
