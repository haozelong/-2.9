import {ComponentFixture, TestBed} from '@angular/core/testing';

import {VerificationCodeComponent} from './verification-code.component';
import {ApiTestingModule} from '../../../api/api.testing.module';
import {RouterTestingModule} from '@yunzhi/ng-router-testing';
import {ReactiveFormsModule} from '@angular/forms';

describe('VerificationCodeComponent', () => {
  let component: VerificationCodeComponent;
  let fixture: ComponentFixture<VerificationCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VerificationCodeComponent],
      imports: [
        ApiTestingModule,
        ReactiveFormsModule,
        RouterTestingModule
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VerificationCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
