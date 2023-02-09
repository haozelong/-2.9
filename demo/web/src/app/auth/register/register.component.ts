import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../../../entity/user';
import {UserService} from '../../../service/user.service';
import {CommonService} from '../../../service/common.service';
import {Subject} from 'rxjs';
import {YzValidator} from '../../validator/yz-validator';
import {debounceTime} from 'rxjs/operators';
import {USER_ROLE} from '../../../entity/enum/user-role';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  /** 注册表单对象 */
  registerForm: FormGroup;

  /** 通过输入的手机号判断身份，如果是学生，设置为学号，如果是老师，设置为工号 */
  numName = '学号';

  /** 是否展示学号/工号输入框 */
  showNumInput = false;

  @Output()
  beDone = new EventEmitter<void>();

  /**
   * 加入防抖功能，参考https://segmentfault.com/a/1190000023374668
   */
  registerUsernameSubject: Subject<string> = new Subject<string>();

  constructor(private userService: UserService,
              private builder: FormBuilder,
              private commonService: CommonService) {
  }

  ngOnInit(): void {
    /** 创建注册表单 */
    this.registerForm = this.builder.group({
      registerUsername: ['', [YzValidator.phone,
        Validators.required]],
      name: ['', [Validators.minLength(1),
        Validators.maxLength(100),
        // Validators.pattern('\\w+'),
        Validators.required]],
      num: ['', [Validators.minLength(4),
        Validators.maxLength(18),
        Validators.pattern('\\w+'),
        Validators.required]],
      verificationCode: ['', Validators.required],
    });

    this.registerUsernameSubject.asObservable().pipe(debounceTime(500))
      .subscribe(() => {
        if (this.registerForm.get('registerUsername').errors.phone) {
          return;
        }
        const username = this.registerForm.get('registerUsername').value;
        if (username !== '') {
          this.userService.getRolesByUsername(username)
            .subscribe((roles: Array<number>) => {
              this.showNumInput = true;
              roles.forEach(role => {
                if (role === USER_ROLE.student.value) {
                  this.numName = '学号';
                  return;
                } else if (role === USER_ROLE.teacher.value) {
                  this.numName = '工号';
                  return;
                }
              });
            }, () => {
              this.commonService.error(() => {
              }, '用户名未找到，请重新输入');
            });
        } else {
          this.showNumInput = false;
        }
      });
  }

  onRegister(): void {
    this.userService.bind({
      name: this.registerForm.get('name').value,
      username: this.registerForm.get('registerUsername').value,
      num: this.registerForm.get('num').value,
      verificationCode: this.registerForm.get('verificationCode').value
    })
      .subscribe((backUser: User) => {
        this.commonService.success(() => {
          this.beDone.emit();
        }, '您的密码为' + backUser.password + ', 请牢记');
      }, () => {
        this.commonService.error(() => {
        }, '用户绑定失败');
      });
  }

  onUsernameChange(): void {
    this.registerUsernameSubject.next();
  }
}
