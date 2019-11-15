import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { ContributionTypeDeleteDialogComponent } from 'app/entities/contribution-type/contribution-type-delete-dialog.component';
import { ContributionTypeService } from 'app/entities/contribution-type/contribution-type.service';

describe('Component Tests', () => {
  describe('ContributionType Management Delete Component', () => {
    let comp: ContributionTypeDeleteDialogComponent;
    let fixture: ComponentFixture<ContributionTypeDeleteDialogComponent>;
    let service: ContributionTypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ContributionTypeDeleteDialogComponent]
      })
        .overrideTemplate(ContributionTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContributionTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContributionTypeService);
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
