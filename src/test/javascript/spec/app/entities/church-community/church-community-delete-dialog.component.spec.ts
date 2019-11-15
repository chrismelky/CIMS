import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { ChurchCommunityDeleteDialogComponent } from 'app/entities/church-community/church-community-delete-dialog.component';
import { ChurchCommunityService } from 'app/entities/church-community/church-community.service';

describe('Component Tests', () => {
  describe('ChurchCommunity Management Delete Component', () => {
    let comp: ChurchCommunityDeleteDialogComponent;
    let fixture: ComponentFixture<ChurchCommunityDeleteDialogComponent>;
    let service: ChurchCommunityService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchCommunityDeleteDialogComponent]
      })
        .overrideTemplate(ChurchCommunityDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChurchCommunityDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChurchCommunityService);
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
