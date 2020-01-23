import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { PeriodContributionTypeUpdateComponent } from 'app/entities/period-contribution-type/period-contribution-type-update.component';
import { PeriodContributionTypeService } from 'app/entities/period-contribution-type/period-contribution-type.service';
import { PeriodContributionType } from 'app/shared/model/period-contribution-type.model';

describe('Component Tests', () => {
  describe('PeriodContributionType Management Update Component', () => {
    let comp: PeriodContributionTypeUpdateComponent;
    let fixture: ComponentFixture<PeriodContributionTypeUpdateComponent>;
    let service: PeriodContributionTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodContributionTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PeriodContributionTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeriodContributionTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PeriodContributionTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PeriodContributionType(123);
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
        const entity = new PeriodContributionType();
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
