import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChurchUpdateComponent } from 'app/entities/church/church-update.component';
import { ChurchService } from 'app/entities/church/church.service';
import { Church } from 'app/shared/model/church.model';

describe('Component Tests', () => {
  describe('Church Management Update Component', () => {
    let comp: ChurchUpdateComponent;
    let fixture: ComponentFixture<ChurchUpdateComponent>;
    let service: ChurchService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChurchUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChurchUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChurchService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Church(123);
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
        const entity = new Church();
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
