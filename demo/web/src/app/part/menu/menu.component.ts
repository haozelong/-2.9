import {Component, OnDestroy, OnInit} from '@angular/core';
import {environment} from '../../../environments/environment';
import {menus} from '../../../conf/menu.config';
import {Subscription} from 'rxjs';
import {UserService} from '../../../service/user.service';
import {Router} from '@angular/router';
import {BaseMenu} from '../../../common/base-menu';
import {isNotNullOrUndefined} from '../../../common/utils';
import {User} from '../../../entity/user';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, OnDestroy {
  environment = environment;
  menus = new Array<BaseMenu>();
  private subscription: Subscription | undefined;

  constructor(private userService: UserService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.menus = [];
    this.subscription = this.userService.getCurrentLoginUser$().subscribe((user: User) => {
      this.menus = [];
      if (user) {
        menus.forEach(menu => {
          this.menus.push(menu);
        });
      }
    });
  }

  navigate(menu: BaseMenu): void {
    this.router.navigateByUrl(menu.url as string).then();
  }

  getBackgroundColor(menu: BaseMenu): string | undefined {
    if (this.active(menu)) {
      return environment.color;
    }
    return undefined;
  }

  getTextColor(menu: BaseMenu): string | undefined {
    if (this.active(menu)) {
      return 'white';
    }
    return undefined;
  }

  /**
   * 判断当前菜单是否激活
   * @param menu 菜单
   */
  active(menu: BaseMenu): boolean {
    return this.router.isActive(menu.url, false);
  }

  ngOnDestroy(): void {
    if (isNotNullOrUndefined(this.subscription)) {
      /** 取消订阅 */
      this.subscription.unsubscribe();
    }
  }
}
