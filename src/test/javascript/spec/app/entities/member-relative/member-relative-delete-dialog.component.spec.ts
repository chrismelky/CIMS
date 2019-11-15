import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { MemberRelativeDeleteDialogComponent } from 'app/entities/member-relative/member-relative-delete-dialog.component';
import { MemberRelativeService } from 'app/entities/member-relative/member-relative.service';

describe('Component Tests', () => {
  describe('MemberRelative Management Delete Component', () => {
    let comp: MemberRelativeDeleteDialogComponent;
    let fixture: ComponentFixture<MemberRelativeDeleteDialogComponent>;
    let service: MemberRelativeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [MemberRelativeDeleteDialogComponent]
      })
        .overrideTemplate(MemberRelativeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MemberRelativeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MemberRelativeService);
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
