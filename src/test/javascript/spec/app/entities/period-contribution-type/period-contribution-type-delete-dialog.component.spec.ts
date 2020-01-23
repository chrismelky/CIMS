import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { PeriodContributionTypeDeleteDialogComponent } from 'app/entities/period-contribution-type/period-contribution-type-delete-dialog.component';
import { PeriodContributionTypeService } from 'app/entities/period-contribution-type/period-contribution-type.service';

describe('Component Tests', () => {
  describe('PeriodContributionType Management Delete Component', () => {
    let comp: PeriodContributionTypeDeleteDialogComponent;
    let fixture: ComponentFixture<PeriodContributionTypeDeleteDialogComponent>;
    let service: PeriodContributionTypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodContributionTypeDeleteDialogComponent]
      })
        .overrideTemplate(PeriodContributionTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeriodContributionTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PeriodContributionTypeService);
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
