import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthRoutingModule} from './auth-routing.module';
import {ReactiveFormsModule} from '@angular/forms';
import {RegisterComponent} from './register/register.component';
import {LoginComponent} from './login/login.component';
import {YzSubmitButtonModule} from '../../func/yz-submit-button/yz-submit-button.module';
import { AuthComponent } from './auth.component';
import { VerificationCodeComponent } from './verification-code/verification-code.component';


@NgModule({
  declarations: [RegisterComponent, LoginComponent, AuthComponent, VerificationCodeComponent],
  exports: [
    RegisterComponent
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    ReactiveFormsModule,
    YzSubmitButtonModule
  ]
})
export class AuthModule {
}
