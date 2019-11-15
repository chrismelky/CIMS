import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { MemberRiteDeleteDialogComponent } from 'app/entities/member-rite/member-rite-delete-dialog.component';
import { MemberRiteService } from 'app/entities/member-rite/member-rite.service';

describe('Component Tests', () => {
  describe('MemberRite Management Delete Component', () => {
    let comp: MemberRiteDeleteDialogComponent;
    let fixture: ComponentFixture<MemberRiteDeleteDialogComponent>;
    let service: MemberRiteService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberRiteDeleteDialogComponent]
      })
        .overrideTemplate(MemberRiteDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MemberRiteDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MemberRiteService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
