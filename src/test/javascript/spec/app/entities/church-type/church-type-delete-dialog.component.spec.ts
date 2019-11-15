import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { ChurchTypeDeleteDialogComponent } from 'app/entities/church-type/church-type-delete-dialog.component';
import { ChurchTypeService } from 'app/entities/church-type/church-type.service';

describe('Component Tests', () => {
  describe('ChurchType Management Delete Component', () => {
    let comp: ChurchTypeDeleteDialogComponent;
    let fixture: ComponentFixture<ChurchTypeDeleteDialogComponent>;
    let service: ChurchTypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchTypeDeleteDialogComponent]
      })
        .overrideTemplate(ChurchTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChurchTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChurchTypeService);
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
