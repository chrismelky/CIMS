import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { MemberRelativeUpdateComponent } from 'app/entities/member-relative/member-relative-update.component';
import { MemberRelativeService } from 'app/entities/member-relative/member-relative.service';
import { MemberRelative } from 'app/shared/model/member-relative.model';

describe('Component Tests', () => {
  describe('MemberRelative Management Update Component', () => {
    let comp: MemberRelativeUpdateComponent;
    let fixture: ComponentFixture<MemberRelativeUpdateComponent>;
    let service: MemberRelativeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberRelativeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MemberRelativeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MemberRelativeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MemberRelativeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MemberRelative(123);
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
        const entity = new MemberRelative();
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
