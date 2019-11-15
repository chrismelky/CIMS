import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { ChurchActivityDeleteDialogComponent } from 'app/entities/church-activity/church-activity-delete-dialog.component';
import { ChurchActivityService } from 'app/entities/church-activity/church-activity.service';

describe('Component Tests', () => {
  describe('ChurchActivity Management Delete Component', () => {
    let comp: ChurchActivityDeleteDialogComponent;
    let fixture: ComponentFixture<ChurchActivityDeleteDialogComponent>;
    let service: ChurchActivityService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchActivityDeleteDialogComponent]
      })
        .overrideTemplate(ChurchActivityDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChurchActivityDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChurchActivityService);
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
