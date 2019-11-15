import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChurchActivityUpdateComponent } from 'app/entities/church-activity/church-activity-update.component';
import { ChurchActivityService } from 'app/entities/church-activity/church-activity.service';
import { ChurchActivity } from 'app/shared/model/church-activity.model';

describe('Component Tests', () => {
  describe('ChurchActivity Management Update Component', () => {
    let comp: ChurchActivityUpdateComponent;
    let fixture: ComponentFixture<ChurchActivityUpdateComponent>;
    let service: ChurchActivityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchActivityUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChurchActivityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChurchActivityUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChurchActivityService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChurchActivity(123);
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
        const entity = new ChurchActivity();
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
