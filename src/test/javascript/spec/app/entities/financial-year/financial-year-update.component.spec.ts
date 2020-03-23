import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { FinancialYearUpdateComponent } from 'app/entities/financial-year/financial-year-update.component';
import { FinancialYearService } from 'app/entities/financial-year/financial-year.service';
import { FinancialYear } from 'app/shared/model/financial-year.model';

describe('Component Tests', () => {
  describe('FinancialYear Management Update Component', () => {
    let comp: FinancialYearUpdateComponent;
    let fixture: ComponentFixture<FinancialYearUpdateComponent>;
    let service: FinancialYearService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [FinancialYearUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FinancialYearUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FinancialYearUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinancialYearService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FinancialYear(123);
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
        const entity = new FinancialYear();
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
