import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { PeriodContributionItemDeleteDialogComponent } from 'app/entities/period-contribution-item/period-contribution-item-delete-dialog.component';
import { PeriodContributionItemService } from 'app/entities/period-contribution-item/period-contribution-item.service';

describe('Component Tests', () => {
  describe('PeriodContributionItem Management Delete Component', () => {
    let comp: PeriodContributionItemDeleteDialogComponent;
    let fixture: ComponentFixture<PeriodContributionItemDeleteDialogComponent>;
    let service: PeriodContributionItemService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [PeriodContributionItemDeleteDialogComponent]
      })
        .overrideTemplate(PeriodContributionItemDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeriodContributionItemDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PeriodContributionItemService);
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
