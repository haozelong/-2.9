import {Component, Injector, OnInit} from '@angular/core';
import {CommonService} from '../service/common.service';
import {Router} from '@angular/router';
import {UserService} from '../service/user.service';
import {XAuthTokenInterceptor} from './interceptor/x-auth-token.interceptor';
import {LoadingInterceptor} from './interceptor/loading.interceptor';
import {randomNumber} from '../common/utils';
import {HttpErrorInterceptor} from './interceptor/http-error.interceptor';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {Title} from '@angular/platform-browser';
import {WebsocketService} from '../service/websocket.service';
import {environment} from '../environments/environment';
import {ConfigService} from '../service/config.service';
import {of, Subscription} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  loading: boolean;
  showLoading: boolean;
  /* showLoading计时器 */
  showLoadingTimer: any;

  spinnerType: string;
  spinnerTypes = ['ball-fussion', 'ball-clip-rotate-multiple',
    'ball-spin-clockwise', 'cog', 'square-jelly-box', 'timer'];
  private showLoadingSubscription = null as Subscription;
  private hideLoadingSubscription = null as Subscription;
  // private showLoading = false;
  private token: string;

  constructor(private commonService: CommonService,
              private webSocketService: WebsocketService,
              private userService: UserService,
              // private spinner: NgxSpinnerService,
              private configService: ConfigService,
              private titleService: Title,
              private injector: Injector,
              private router: Router) {
    this.titleService.setTitle(environment.title);
    this.configService.checkVersion();
  }

  generateSpinnerType() {
    this.spinnerType = this.spinnerTypes[randomNumber() % this.spinnerTypes.length];
  }

  ngOnInit(): void {
    const httpInterceptors = this.injector.get(HTTP_INTERCEPTORS);
    httpInterceptors.forEach(httpInterceptor => {
      if (httpInterceptor instanceof HttpErrorInterceptor) {
        httpInterceptor.error = (url, message) => {
          this.commonService.error(() => {
          }, url, message);
        };
        httpInterceptor.goToLoginPath = () => {
          if (this.router && this.router.url && !this.router.url.startsWith(`/login`)) {
            this.router.navigateByUrl('/login').then();
          }
        }
      }
    })
    const appOnReadyItem = this.commonService.getAppOnReadyItem();
    if (this.router && this.router.url && !this.router.url.startsWith(`/login`)) {
      this.userService.initCurrentLoginUser(() => {
        appOnReadyItem.ready = true;
      }).subscribe({
        error: () =>
          this.router.navigateByUrl('/login').then()
      });
    } else {
      appOnReadyItem.ready = true;
    }
    this.generateSpinnerType();
    LoadingInterceptor.loading$.subscribe(loading => {
      this.setLoading(loading);
    });

    // 登录用户改变时，重新将wsUuid与xAuthToken绑定
    this.userService.getCurrentLoginUser$()
      .subscribe(() => {
        const token = XAuthTokenInterceptor.getToken();
        if (token && this.token !== token) {
          this.token = token;
          this.webSocketService.send('/ws/bind', XAuthTokenInterceptor.getToken());
        }
      });
  }

  setLoading(loading: boolean): void {
    if (loading) {
      if (!this.loading) {
        this.loading = true;
        this.showLoadingTimer = setTimeout(() => this.showLoading = true, 500);
      }
    } else {
      this.loading = this.showLoading = false;
      if (this.showLoadingTimer !== null && this.showLoadingTimer !== undefined) {
        clearTimeout(this.showLoadingTimer);
        this.showLoadingTimer = null;
      }
    }
  }


  // setLoading(loading: boolean): void {
  //   if (loading) {
  //     this.loading++;
  //   } else if (this.loading > 0) {
  //     this.loading--;
  //   }
  //
  //   if (this.loading === 1 && loading) {
  //     if (this.hideLoadingSubscription) {
  //       this.hideLoadingSubscription.unsubscribe();
  //       this.hideLoadingSubscription = null;
  //     } else {
  //       this.showLoadingSubscription = of({}).pipe(delay(500)).subscribe(
  //         () => {
  //           this.showLoading = true;
  //           this.spinner.show().then();
  //           this.showLoadingSubscription = null;
  //         });
  //     }
  //   } else if (this.loading === 0) {
  //     if (this.showLoadingSubscription) {
  //       this.showLoadingSubscription.unsubscribe();
  //       this.showLoadingSubscription = null;
  //     } else {
  //       // 100MS后再选择隐藏，防止前台接连请求时loading频闪的问题
  //       this.hideLoadingSubscription = of({}).pipe(delay(100))
  //         .subscribe(() => {
  //           this.showLoading = false;
  //           this.hideLoadingSubscription = null;
  //           this.spinner.hide().then();
  //         });
  //     }
  //   }
  // }
}
