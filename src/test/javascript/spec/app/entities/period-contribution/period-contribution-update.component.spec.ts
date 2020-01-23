import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { PeriodContributionUpdateComponent } from 'app/entities/period-contribution/period-contribution-update.component';
import { PeriodContributionService } from 'app/entities/period-contribution/period-contribution.service';
import { PeriodContribution } from 'app/shared/model/period-contribution.model';

describe('Component Tests', () => {
  describe('PeriodContribution Management Update Component', () => {
    let comp: PeriodContributionUpdateComponent;
    let fixture: ComponentFixture<PeriodContributionUpdateComponent>;
    let service: PeriodContributionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodContributionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PeriodContributionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeriodContributionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PeriodContributionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PeriodContribution(123);
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
        const entity = new PeriodContribution();
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
