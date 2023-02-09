import {ComponentFixture, TestBed} from '@angular/core/testing';

import {LayoutComponent} from './layout.component';
import {ApiTestingModule} from '../../../api/api.testing.module';
import {RouterTestingModule} from '@angular/router/testing';
import {HeaderModule} from '../header/header.module';
import {MenuModule} from '../menu/menu.module';
import {NavModule} from '../nav/nav.module';
import {Router} from '@angular/router';
import {of} from 'rxjs';

describe('LayoutComponent', () => {
  let component: LayoutComponent;
  let fixture: ComponentFixture<LayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LayoutComponent],
      imports: [
        ApiTestingModule,
        RouterTestingModule,
        HeaderModule,
        MenuModule,
        NavModule
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    const router = TestBed.inject(Router);
    spyOn(router, 'navigateByUrl')
      .and.returnValue(of().toPromise<any>());
    fixture = TestBed.createComponent(LayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  afterEach((done) => {
    fixture.whenStable().then(() => done());
  });
});
