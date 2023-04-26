import { Injectable, Optional } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpInterceptor,
  HttpEvent,
  HttpClient
} from '@angular/common/http';

import { BehaviorSubject, forkJoin, Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';

export interface UserAuth0 {
  sub: string,
  nickname: string,
  name: string,
  picture: string,
  updated_at: string,
  email: string,
  email_verified: boolean
}

export interface UserSpring {
  id: number;
  email: string,
  firstName: string | null,
  lastName: string | null,
  telephoneNumber: string | null,
  isAdministrator: boolean
}

export interface User {
  auth0: UserAuth0;
  spring: UserSpring;
}

const domain = 'https://dev-uox28mbzk3p270l1.us.auth0.com';
const audience = 'localhost';
const clientId = 'okCV1iX8I6mb9BqQZ6YnNF1hDcx3fv5n';
const redirectUrl = 'http://localhost:4200';
const redirectUrlEnc = encodeURIComponent(redirectUrl);
const scope = encodeURIComponent('email profile openid');
const signInUrl = `${domain}/authorize?audience=${audience}&response_type=code&client_id=${clientId}&redirect_uri=${redirectUrlEnc}&scope=${scope}`;

const returnToUrl = redirectUrl;
const logOutUrl = `${domain}/v2/logout?client_id=${clientId}&returnTo=${returnToUrl}`;

const serverUrl = 'http://localhost:8080';
const logInUrl = serverUrl + '/front/login';
const getUserInfoSpringUrl = serverUrl + '/api/user';
const getUserInfoAuth0Url = domain + '/userinfo';

@Injectable()
export class AuthenticationService {
  // Just store token
  token: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
}

@Injectable()
export class UserService {
  // Monitor parametres, after getting one-time code as parameter log in user and get all it's info from both Auth0 and Spring
  user: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  // It is true until all User data is present or something fails
  loading: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private auth: AuthenticationService, private route: ActivatedRoute, private http: HttpClient, private router: Router) {
    this.route.queryParams.subscribe(this.checkParams);

    this.auth.token.subscribe(this.updateUser);
  };

  checkParams = (params: { [x: string]: any; }) => {
    // When we get one-time code we need to send it to Spring and get JWT tokens
    if (params['code']) {
      this.loading.next(true);
      this.http.post(
        logInUrl,
        { code: params['code'], url: redirectUrl },
        { responseType: 'text' }
      ).subscribe({
        next: (token)=>this.auth.token.next(token),
        error: this.handleError
      }).add(this.routerRemoveParams);
    }
  }

  updateUser = (token: string | null) => {
    if (token == null) return;
    
    const getUserInfoAuth0$ = this.http.get<UserAuth0>(getUserInfoAuth0Url);
    const getUserInfoSpring$ = this.http.get<UserSpring>(getUserInfoSpringUrl);

    forkJoin([getUserInfoAuth0$, getUserInfoSpring$]).subscribe({
      next: ([auth0UserInfo, springUserInfo]) => {
        this.user.next({
          auth0: auth0UserInfo,
          spring: springUserInfo
        })
        this.loading.next(false);
      },
      error: this.handleError
    });
  }

  routerRemoveParams = () => {
    this.router.navigate(
      ['.'],
      { relativeTo: this.route }
    );
  }

  onSignUp() {
    window.location.href = signInUrl;
    this.loading.next(true);
  }

  onLogOut() {
    this.user.next(null);
    this.auth.token.next(null);
    window.location.href = logOutUrl;
    this.loading.next(false);
  }

  handleError = (error: any) => {
    console.error(error);
    this.loading.next(false);
  }
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