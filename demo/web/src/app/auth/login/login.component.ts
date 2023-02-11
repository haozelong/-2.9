import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../../service/user.service';
import {config} from '../../../conf/app.config';
import {CommonService} from '../../../service/common.service';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  // 正在倒计时
  countDowning = false;

  loginModel = 'username' as 'username' | 'wechat';

  qrCodeSrc: string;

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
    });
  }

  ngOnInit(): void {
    this.errorInfo = '';
    this.loginForm.valueChanges
      .subscribe(() => {
        this.errorInfo = '';
      });
  }

  login(user: {username: string, password: string}): void {
    this.userService.login(user)
      // tslint:disable-next-line:no-shadowed-variable
      .subscribe((user) => {
        console.log('user' + user);
        this.userService.initCurrentLoginUser().subscribe({
          next: () => this.router.navigateByUrl('dashboard').then()
        });
      }, (response) => {
        const errorCode = +response.headers.get(config.ERROR_RESPONSE_CODE_KEY);
        const errorMessage = response.headers.get(config.ERROR_RESPONSE_MESSAGE_KEY);
        console.log(`发生错误：${errorCode}, ${errorMessage}`);
        this.errorInfo = '登录失败，请检查您填写的信息是否正确, 如若检查无误，可能是您的账号被冻结';
      });
  }

  onLogin(): void {
    console.log('执行了login方法');
    const user = {
      // tslint:disable-next-line:no-non-null-assertion
      username: this.loginForm.get('username')!.value as string,
      // tslint:disable-next-line:no-non-null-assertion
      password: this.loginForm.get('password')!.value as string,
    };
    this.login(user);
  }

  /**
   * 微信扫码登录
   */
  onWeChatLogin() {
    this.userService.getLoginQrCode()
      .subscribe(src => {
        this.qrCodeSrc = src;
        this.loginModel = 'wechat';
        this.userService.onScanLoginQrCode$.pipe(first()).subscribe(data => {
          const uuid = data.body;
          console.log('从后台获取的uuid是' + uuid);
          console.log('登陆了');
          this.login({username: uuid, password: uuid});
        });
      });
  }
}
