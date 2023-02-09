import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PersonalRoutingModule } from './personal-routing.module';
import {PersonalComponent} from './personal.component';
import {ModifyPasswordComponent} from './modify-password/modify-password.component';
import {ReactiveFormsModule} from '@angular/forms';


@NgModule({
  declarations: [PersonalComponent, ModifyPasswordComponent],
  imports: [
    CommonModule,
    PersonalRoutingModule,
    ReactiveFormsModule
  ]
})
export class PersonalModule { }
