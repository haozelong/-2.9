import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AuthComponent} from './auth.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {VerificationCodeComponent} from './verification-code/verification-code.component';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterTestingModule} from '@yunzhi/ng-router-testing';
import {ApiTestingModule} from '../../api/api.testing.module';
import {YzSubmitButtonModule} from '../../func/yz-submit-button/yz-submit-button.module';

describe('AuthComponent', () => {
  let component: AuthComponent;
  let fixture: ComponentFixture<AuthComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AuthComponent,
        LoginComponent,
        RegisterComponent,
        VerificationCodeComponent],
      imports: [ReactiveFormsModule,
        ApiTestingModule,
        YzSubmitButtonModule,
        RouterTestingModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
