import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { MemberContributionUpdateComponent } from 'app/entities/member-contribution/member-contribution-update.component';
import { MemberContributionService } from 'app/entities/member-contribution/member-contribution.service';
import { MemberContribution } from 'app/shared/model/member-contribution.model';

describe('Component Tests', () => {
  describe('MemberContribution Management Update Component', () => {
    let comp: MemberContributionUpdateComponent;
    let fixture: ComponentFixture<MemberContributionUpdateComponent>;
    let service: MemberContributionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberContributionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MemberContributionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MemberContributionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MemberContributionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MemberContribution(123);
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
        const entity = new MemberContribution();
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
