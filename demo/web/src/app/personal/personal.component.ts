import { Component, OnInit } from '@angular/core';
import {Assert} from '../../common/utils';
import {User} from "../../entity/user";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-personal',
  templateUrl: './personal.component.html',
  styleUrls: ['./personal.component.scss']
})
export class PersonalComponent implements OnInit {

  /**
   * 初始化对象
   */
  user = new User();

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getCurrentLoginUser$()
      .subscribe((data) => {
        this.setStudent(data);
      });
  }

  /**
   * 设置user内容
   */
  setStudent(user: User): void {
    Assert.isString(user.name, 'name参数不存在');
    Assert.isString(user.username, 'username参数不存在');

    this.user = user;
  }

}
