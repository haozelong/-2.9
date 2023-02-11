import { Component, OnInit } from '@angular/core';
import {Assert} from '../../common/utils';
import {filter, first} from 'rxjs/operators';
import {CommonService} from '../../service/common.service';
import {UserService} from '../../service/user.service';
import {User} from '../../entity/user';
import { Location } from '@angular/common';

@Component({
  selector: 'app-personal',
  templateUrl: './personal.component.html',
  styleUrls: ['./personal.component.scss']
})
export class PersonalComponent implements OnInit {

  user = new User();
  isShowQrCode = false;
  qrCodeSrc: string;

  constructor(private userService: UserService,
              private location: Location,
              private commonService: CommonService) { }

  ngOnInit(): void {
    this.userService.getCurrentLoginUser$()
      .pipe(filter(v => v !== null && v !== undefined))
      .subscribe((data: User) => {
        this.setUser(data);
      });
  }



  setUser(user: User): void {
    Assert.isNotNullOrUndefined(user.name, 'name must be exit');
    Assert.isNotNullOrUndefined(user.username, 'username must be exit');
    this.user = user;
  }

  onBindWeChat() {
    this.userService.generateBindQrCode()
      .subscribe(src => {
        this.qrCodeSrc = src;
        this.isShowQrCode = true;
        this.userService.onScanBindUserQrCode$
          .pipe(first())
          .subscribe(stomp => {
            this.user.weChatUser = {openid: stomp.body};
            this.isShowQrCode = false;
            this.commonService.success();
          });
      });
  }

  onClose() {
    this.isShowQrCode = false;
  }

}
