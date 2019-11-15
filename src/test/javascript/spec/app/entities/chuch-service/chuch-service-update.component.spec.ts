import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChuchServiceUpdateComponent } from 'app/entities/chuch-service/chuch-service-update.component';
import { ChuchServiceService } from 'app/entities/chuch-service/chuch-service.service';
import { ChuchService } from 'app/shared/model/chuch-service.model';

describe('Component Tests', () => {
  describe('ChuchService Management Update Component', () => {
    let comp: ChuchServiceUpdateComponent;
    let fixture: ComponentFixture<ChuchServiceUpdateComponent>;
    let service: ChuchServiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChuchServiceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChuchServiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChuchServiceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChuchServiceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChuchService(123);
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
        const entity = new ChuchService();
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
