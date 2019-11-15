import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { ChuchServiceDeleteDialogComponent } from 'app/entities/chuch-service/chuch-service-delete-dialog.component';
import { ChuchServiceService } from 'app/entities/chuch-service/chuch-service.service';

describe('Component Tests', () => {
  describe('ChuchService Management Delete Component', () => {
    let comp: ChuchServiceDeleteDialogComponent;
    let fixture: ComponentFixture<ChuchServiceDeleteDialogComponent>;
    let service: ChuchServiceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChuchServiceDeleteDialogComponent]
      })
        .overrideTemplate(ChuchServiceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChuchServiceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChuchServiceService);
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
