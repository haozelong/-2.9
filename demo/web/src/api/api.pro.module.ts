import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {XAuthTokenInterceptor} from '../app/interceptor/x-auth-token.interceptor';
import {ApiPrefixAndMergeMapInterceptor} from '../app/interceptor/api-prefix-and-merge-map.interceptor';
import {NullOrUndefinedOrEmptyInterceptor} from '../app/interceptor/null-or-undefined-or-empty.interceptor';
import {HttpErrorInterceptor} from '../app/interceptor/http-error.interceptor';
import {LoadingInterceptor} from '../app/interceptor/loading.interceptor';
import {Prevent401Popup} from '../app/interceptor/prevent-401-popup';

@NgModule({
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: ApiPrefixAndMergeMapInterceptor,
    multi: true
  }, {
    provide: HTTP_INTERCEPTORS,
    useClass: XAuthTokenInterceptor,
    multi: true
  }, {
    provide: HTTP_INTERCEPTORS,
    useClass: NullOrUndefinedOrEmptyInterceptor,
    multi: true
  }, {
    provide: HTTP_INTERCEPTORS,
    useClass: LoadingInterceptor,
    multi: true
  }, {
    provide: HTTP_INTERCEPTORS,
    useClass: HttpErrorInterceptor,
    multi: true
  }, {
    provide: HTTP_INTERCEPTORS,
    useClass: Prevent401Popup,
    multi: true
  }]
})
export class ApiProModule {
}
