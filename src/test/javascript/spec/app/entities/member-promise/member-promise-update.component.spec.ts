import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { MemberPromiseUpdateComponent } from 'app/entities/member-promise/member-promise-update.component';
import { MemberPromiseService } from 'app/entities/member-promise/member-promise.service';
import { MemberPromise } from 'app/shared/model/member-promise.model';

describe('Component Tests', () => {
  describe('MemberPromise Management Update Component', () => {
    let comp: MemberPromiseUpdateComponent;
    let fixture: ComponentFixture<MemberPromiseUpdateComponent>;
    let service: MemberPromiseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberPromiseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MemberPromiseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MemberPromiseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MemberPromiseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MemberPromise(123);
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
        const entity = new MemberPromise();
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
