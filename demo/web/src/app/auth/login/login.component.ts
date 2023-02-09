import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../../service/user.service';
import {config} from '../../../conf/app.config';
import {HTTP_STATUS_CODE} from '../../../common/http-code';
import {CommonService} from '../../../service/common.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  // 正在倒计时
  countDowning = false;

  /** 登录表单对象 */
  loginForm: FormGroup;

  /** 错误信息 */
  errorInfo: string | undefined;

  /** 提交状态 */
  submitting = false;

  showValidateCode = false;


  constructor(private builder: FormBuilder,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private commonService: CommonService,
              private userService: UserService) {
    /** 创建登录表单 */
    this.loginForm = this.builder.group({
      username: ['', [Validators.minLength(11),
        Validators.maxLength(11),
        Validators.pattern('\\d+'),
        Validators.required]],
      password: ['', Validators.required],
      verificationCode: ['0000', Validators.required],
    });
  }

  ngOnInit(): void {
    this.errorInfo = '';
    this.loginForm.valueChanges
      .subscribe(() => {
        this.errorInfo = '';
      });
  }

  login(): void {
    const user = {
      username: this.loginForm.get('username')!.value as string,
      password: this.loginForm.get('password')!.value as string,
      verificationCode: this.loginForm.get('verificationCode')!.value as string
    };

    this.userService.login(user)
      .subscribe(() => {
        this.userService.initCurrentLoginUser(() => {
          this.router.navigateByUrl('dashboard').then();
        });
      }, (response) => {
        const errorCode = +response.headers.get(config.ERROR_RESPONSE_CODE_KEY);
        const errorMessage = response.headers.get(config.ERROR_RESPONSE_MESSAGE_KEY);
        console.log(`发生错误：${errorCode}, ${errorMessage}`);
        if (errorCode === HTTP_STATUS_CODE.REQUIRE_VERIFICATION_CODE.code) {
          this.showValidateCode = true;
          this.loginForm.patchValue({verificationCode: ''});
        } else {
          this.showValidateCode = false;
          this.loginForm.patchValue({verificationCode: '0000'});
        }
        this.errorInfo = '登录失败，请检查您填写的信息是否正确, 如若检查无误，可能是您的账号被冻结';
        this.submitting = false;
      });
  }
}
