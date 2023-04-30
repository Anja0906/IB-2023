import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthenticationService, TokenInterceptor, UserService } from './auth-service/authentication.service';
import { ButtonComponentComponent } from './button-component/button-component.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { UserInfoComponent } from './user-info/user-info.component';
import { IndexPageComponent } from './index-page/index-page.component';
import { FormsModule } from '@angular/forms';

import { RecaptchaModule, RecaptchaFormsModule, RecaptchaSettings, RECAPTCHA_SETTINGS } from "ng-recaptcha";
const globalSettings: RecaptchaSettings = { siteKey: '6LdS680lAAAAAAilDhTT_KiLheEg2bGwsRA2eBeM' };

@NgModule({
  declarations: [
    AppComponent,
    ButtonComponentComponent,
    NavBarComponent,
    UserInfoComponent,
    IndexPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    RecaptchaModule,
    RecaptchaFormsModule,
    FormsModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }, AuthenticationService, UserService, {
    provide: RECAPTCHA_SETTINGS,
    useValue: globalSettings,
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
