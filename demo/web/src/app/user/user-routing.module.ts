import {NgModule} from '@angular/core';
import {Route, RouterModule} from '@angular/router';
import {IndexComponent} from './index/index.component';
import {AddComponent} from "./add/add.component";
import {EditComponent} from "./edit/edit.component";

const routes: Route[] = [
  {
    path: '',
    component: IndexComponent
  },
  {
    path: 'add',
    component: AddComponent
  },
  {
    path: 'edit/:id',
    component: EditComponent
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class UserRoutingModule {
}
