import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { PeriodContributionDeleteDialogComponent } from 'app/entities/period-contribution/period-contribution-delete-dialog.component';
import { PeriodContributionService } from 'app/entities/period-contribution/period-contribution.service';

describe('Component Tests', () => {
  describe('PeriodContribution Management Delete Component', () => {
    let comp: PeriodContributionDeleteDialogComponent;
    let fixture: ComponentFixture<PeriodContributionDeleteDialogComponent>;
    let service: PeriodContributionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodContributionDeleteDialogComponent]
      })
        .overrideTemplate(PeriodContributionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeriodContributionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PeriodContributionService);
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
