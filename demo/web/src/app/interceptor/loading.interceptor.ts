import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {finalize} from 'rxjs/operators';
import {CommonService} from '../../service/common.service';

/**
 * 设置loading.
 */
@Injectable({
  providedIn: 'root'
})
export class LoadingInterceptor implements HttpInterceptor {

  public static loadingSubject = new Subject<boolean>();
  public static loading$ = LoadingInterceptor.loadingSubject.asObservable();

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    LoadingInterceptor.loadingSubject.next(true);
    return next.handle(req).pipe(finalize(() => LoadingInterceptor.loadingSubject.next(false)));
  }

}
