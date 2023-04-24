import { Injectable, Optional } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpInterceptor,
  HttpEvent,
  HttpClient
} from '@angular/common/http';

import { BehaviorSubject, Observable } from 'rxjs';

export interface User {
  springId: number;
  auth0Id: string;
  name: string;
  email: string;
  phone: string;
  isAdmin: boolean;
  imgSrc: string;
}

@Injectable()
export class AuthenticationService {
  token: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
}

@Injectable()
export class UserService {
  constructor(private auth: AuthenticationService, private http: HttpClient) {
    this.auth.token.subscribe(() => {
      if (this.auth.token.value)
        this.http.get('https://dev-uox28mbzk3p270l1.us.auth0.com/userinfo').subscribe(
          {
            next: (userinfo: any) => {
              this.user.next({
                springId: 0,  // TODO: need one more request to SPRING SERVER to get this value
                auth0Id: userinfo?.sub ?? '',
                name: userinfo?.name ?? '',
                email: userinfo?.email ?? '',
                phone: userinfo?.phone ?? '',  // TODO: need one more request to SPRING SERVER to get this value
                isAdmin: false,  // TODO: need one more request to SPRING SERVER to get this value
                imgSrc: userinfo?.picture ?? ''
              })
            }
          })
    });
  };

  user: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
}

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private auth: AuthenticationService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.auth.token.value) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.auth.token.value}`
        }
      });
    }
    return next.handle(request);
  }
}