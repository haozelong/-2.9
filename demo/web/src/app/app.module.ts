import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {LayoutModule} from './part/layout/layout.module';
import {HttpClientModule} from '@angular/common/http';
import {ApiProModule} from '../api/api.pro.module';
import {PersonalModule} from './personal/personal.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    LayoutModule,
    ApiProModule,
    PersonalModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
