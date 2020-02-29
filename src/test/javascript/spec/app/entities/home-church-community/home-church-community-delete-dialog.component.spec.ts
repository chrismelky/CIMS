import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ChurchTestModule } from '../../../test.module';
import { HomeChurchCommunityDeleteDialogComponent } from 'app/entities/home-church-community/home-church-community-delete-dialog.component';
import { HomeChurchCommunityService } from 'app/entities/home-church-community/home-church-community.service';

describe('Component Tests', () => {
  describe('HomeChurchCommunity Management Delete Component', () => {
    let comp: HomeChurchCommunityDeleteDialogComponent;
    let fixture: ComponentFixture<HomeChurchCommunityDeleteDialogComponent>;
    let service: HomeChurchCommunityService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [HomeChurchCommunityDeleteDialogComponent]
      })
        .overrideTemplate(HomeChurchCommunityDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HomeChurchCommunityDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HomeChurchCommunityService);
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