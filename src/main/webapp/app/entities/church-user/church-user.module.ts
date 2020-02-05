import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChurchUserComponent } from 'app/entities/church-user/church-user.component';
import { ChurchUserUpdateComponent } from './church-user-update.component';
import { RouterModule } from '@angular/router';
import { ChurchSharedModule } from 'app/shared/shared.module';

@NgModule({
  declarations: [ChurchUserComponent, ChurchUserUpdateComponent],
  imports: [CommonModule, ChurchSharedModule, RouterModule],
  exports: [ChurchUserComponent]
})
export class ChurchUserModule {}
