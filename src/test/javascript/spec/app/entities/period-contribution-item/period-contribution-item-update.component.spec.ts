import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { PeriodContributionItemUpdateComponent } from 'app/entities/period-contribution-item/period-contribution-item-update.component';
import { PeriodContributionItemService } from 'app/entities/period-contribution-item/period-contribution-item.service';
import { PeriodContributionItem } from 'app/shared/model/period-contribution-item.model';

describe('Component Tests', () => {
  describe('PeriodContributionItem Management Update Component', () => {
    let comp: PeriodContributionItemUpdateComponent;
    let fixture: ComponentFixture<PeriodContributionItemUpdateComponent>;
    let service: PeriodContributionItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodContributionItemUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PeriodContributionItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeriodContributionItemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PeriodContributionItemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PeriodContributionItem(123);
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
        const entity = new PeriodContributionItem();
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
