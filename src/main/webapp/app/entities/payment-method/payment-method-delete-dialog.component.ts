import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from './payment-method.service';

@Component({
  selector: 'church-payment-method-delete-dialog',
  templateUrl: './payment-method-delete-dialog.component.html'
})
export class PaymentMethodDeleteDialogComponent {
  paymentMethod: IPaymentMethod;

  constructor(
    protected paymentMethodService: PaymentMethodService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.paymentMethodService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'paymentMethodListModification',
        content: 'Deleted an paymentMethod'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'church-payment-method-delete-popup',
  template: ''
})
export class PaymentMethodDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ paymentMethod }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PaymentMethodDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.paymentMethod = paymentMethod;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/payment-method', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/payment-method', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
