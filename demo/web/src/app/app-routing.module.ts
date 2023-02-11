import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LayoutComponent} from './part/layout/layout.component';


const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule),
  },
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule),
        data: {
          title: '首页'
        }
      }, {
        path: 'personal',
        loadChildren: () => import('./personal/personal.module')
          .then(m => m.PersonalModule),
        data: {
          title: '个人中心'
        }
      }, {
        path: 'user',
        loadChildren: () => import('./user/user.module')
          .then(m => m.UserModule),
        data: {
          title: '用戶管理'
        }
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
