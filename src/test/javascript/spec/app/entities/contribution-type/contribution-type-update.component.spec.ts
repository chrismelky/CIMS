import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ContributionTypeUpdateComponent } from 'app/entities/contribution-type/contribution-type-update.component';
import { ContributionTypeService } from 'app/entities/contribution-type/contribution-type.service';
import { ContributionType } from 'app/shared/model/contribution-type.model';

describe('Component Tests', () => {
  describe('ContributionType Management Update Component', () => {
    let comp: ContributionTypeUpdateComponent;
    let fixture: ComponentFixture<ContributionTypeUpdateComponent>;
    let service: ContributionTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ContributionTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ContributionTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContributionTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContributionTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ContributionType(123);
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
        const entity = new ContributionType();
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
