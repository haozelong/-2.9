import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IndexComponent } from './index/index.component';
import {SizeModule} from "../../common/size/size.module";
import {PageModule} from "../../common/page/page.module";
import { AddComponent } from './add/add.component';
import { EditComponent } from './edit/edit.component';
import {ReactiveFormsModule} from '@angular/forms';
import {UserRoutingModule} from './user-routing.module';
import {YzModalModule} from '../../common/yz-modal/yz-modal.module';

@NgModule({
  declarations: [
    IndexComponent,
    AddComponent,
    EditComponent
  ],
  imports: [
    CommonModule,
    SizeModule,
    PageModule,
    ReactiveFormsModule,
    UserRoutingModule,
    YzModalModule
  ]
})
export class UserModule { }
