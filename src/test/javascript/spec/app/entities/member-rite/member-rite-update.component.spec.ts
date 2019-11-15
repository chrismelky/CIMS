import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { MemberRiteUpdateComponent } from 'app/entities/member-rite/member-rite-update.component';
import { MemberRiteService } from 'app/entities/member-rite/member-rite.service';
import { MemberRite } from 'app/shared/model/member-rite.model';

describe('Component Tests', () => {
  describe('MemberRite Management Update Component', () => {
    let comp: MemberRiteUpdateComponent;
    let fixture: ComponentFixture<MemberRiteUpdateComponent>;
    let service: MemberRiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberRiteUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MemberRiteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MemberRiteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MemberRiteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MemberRite(123);
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
        const entity = new MemberRite();
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
