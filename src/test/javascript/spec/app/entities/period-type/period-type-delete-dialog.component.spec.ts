import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { PeriodTypeDeleteDialogComponent } from 'app/entities/period-type/period-type-delete-dialog.component';
import { PeriodTypeService } from 'app/entities/period-type/period-type.service';

describe('Component Tests', () => {
  describe('PeriodType Management Delete Component', () => {
    let comp: PeriodTypeDeleteDialogComponent;
    let fixture: ComponentFixture<PeriodTypeDeleteDialogComponent>;
    let service: PeriodTypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodTypeDeleteDialogComponent]
      })
        .overrideTemplate(PeriodTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeriodTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PeriodTypeService);
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
