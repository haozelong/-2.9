import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PersonalRoutingModule } from './personal-routing.module';
import {PersonalComponent} from './personal.component';
import {ModifyPasswordComponent} from './modify-password/modify-password.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {YzModalModule} from '../../common/yz-modal/yz-modal.module';


@NgModule({
  declarations: [PersonalComponent, ModifyPasswordComponent],
    imports: [
        CommonModule,
        PersonalRoutingModule,
        ReactiveFormsModule,
        YzModalModule,
        HttpClientModule,
        FormsModule,
    ]
})
export class PersonalModule { }
