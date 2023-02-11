import {Component, OnDestroy, OnInit} from '@angular/core';
import {Page} from '../../../common/page';
import {ActivatedRoute, Params} from '@angular/router';
import {FormControl, FormGroup} from '@angular/forms';
import {CommonService} from '../../../service/common.service';
import {Assert} from '@yunzhi/ng-mock-api';
import {WebsocketService} from '../../../service/websocket.service';
import {Subject} from 'rxjs';
import {UserService} from '../../../service/user.service';
import {User} from '../../../entity/user';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.scss']
})
export class IndexComponent implements OnInit, OnDestroy {

  keys = {
    page: 'page',
    size: 'size',
    name: 'name',
    username: 'username'
  };
  isShowQrCode = false;
  nameFormControl = new FormControl('');
  pageData = {} as Page<User>;
  params: Params;
  queryForm = new FormGroup({});
  usernameFormControl = new FormControl('');
  private subject = new Subject<void>();

  constructor(private commonService: CommonService,
              private userService: UserService,
              private websocketService: WebsocketService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    // 使用this.keys初始化FormControl，从而避免拼写错误
    this.queryForm!.addControl(this.keys.name, this.nameFormControl);
    this.queryForm!.addControl(this.keys.username, this.usernameFormControl);

    // 订阅参数变化
    this.route.params.subscribe(params => {
      // 缓存查询参数
      this.params = params;
      // 使用参数中的数据设置formGroup
      this.queryForm.get(this.keys.name).setValue(params[this.keys.name]);
      this.queryForm.get(this.keys.username).setValue(params[this.keys.username]);

      // 发起查询
      this.userService.page(
          +params.page,
          +params.size,
        {username: params[this.keys.name],
          name: params[this.keys.username],}

      ).subscribe(pageDate => this.setData(pageDate));
    });
  }

  /**
   * 删除
   * @param object 班级
   */
  onDelete(object: User): void {
    Assert.isNotNullOrUndefined(object.id, 'id未定义');
    this.commonService.confirm((confirm = false) => {
      if (confirm) {
        const index = this.pageData.content.indexOf(object);
        this.userService.delete(object.id!)
          .subscribe(() => {
            this.commonService.success(() => this.pageData.content.splice(index, 1));
          });
      }
    }, '');
  }

  /**
   * 点击分页
   * @param page 当前页
   */
  onPageChange(page: number): void {
    this.reload({...this.params, ...{page}});
  }

  onSizeChange(size: number): void {
    this.reload({...this.params, ...{size}});
  }

  onSubmit(queryForm: FormGroup): void {
    this.reload({...this.params, ...queryForm.value});
  }

  /**
   * 查询
   * @param params page: 当前页 size: 每页大小
   */
  reload(params = this.params): void {
    this.commonService.reloadByParam(params).then();
  }

  /**
   * 设置数据
   * @param data 分页数据
   */
  setData(data: Page<User>): void {
    this.pageData.content = data.content;
    this.validateData(data);
    this.pageData = data;
  }

  /**
   * 校验数据是否满足前台列表的条件
   * @param data 分页数据
   */
  validateData(data: Page<User>): void {
    Assert.isNotNullOrUndefined(data.number, data.size, data.totalElements, '未满足page组件的初始化条件');
    data.content.forEach(v => this.validateUser(v));
    this.pageData = data;
  }

  /**
   * 校验字段是否符合V层表现
   * @param user USER
   */
  validateUser(user: User): void {
    // 必有条件
    Assert.isNotNullOrUndefined(
      user.id,
      user.name,
      '未满足table列表的初始化条件'
    );
  }

  ngOnDestroy(): void {
    this.subject.next(null);
    this.subject.complete();
  }
}
