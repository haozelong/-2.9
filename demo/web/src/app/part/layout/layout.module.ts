import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LayoutComponent} from './layout.component';
import {RouterModule} from '@angular/router';
import {HeaderModule} from '../header/header.module';
import {MenuModule} from '../menu/menu.module';
import {NavModule} from '../nav/nav.module';


@NgModule({
  declarations: [LayoutComponent],
  imports: [
    CommonModule,
    RouterModule,
    HeaderModule,
    MenuModule,
    NavModule
  ],
  exports: [LayoutComponent]
})
export class LayoutModule {
}
