import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthenticationService, TokenInterceptor, UserService } from './auth-service/authentication.service';
import { ButtonComponentComponent } from './button-component/button-component.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { UserInfoComponent } from './user-info/user-info.component';
import { CardComponent } from './card/card.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { CertificatesComponent } from './certificates/certificates.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import {MatGridListModule} from "@angular/material/grid-list";
import {MatLegacyCardModule} from "@angular/material/legacy-card";
import {MatButtonModule} from "@angular/material/button";
import { CertificateDetailsComponent } from './certificate-details/certificate-details.component';
import { NewCertificateComponent } from './new-certificate/new-certificate.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatRadioModule} from "@angular/material/radio";
import { IsValidUploadComponent } from './is-valid-upload/is-valid-upload.component';
import {MatIconModule} from "@angular/material/icon";
import { RequestsComponent } from './requests/requests.component';
import { RequestComponent } from './request/request.component';
import { MyRequestsComponent } from './my-requests/my-requests.component';
import { IndexPageComponent } from './index-page/index-page.component';

import { RecaptchaModule, RecaptchaFormsModule, RecaptchaSettings, RECAPTCHA_SETTINGS } from "ng-recaptcha";
const globalSettings: RecaptchaSettings = { siteKey: '6LdS680lAAAAAAilDhTT_KiLheEg2bGwsRA2eBeM' };

@NgModule({
  declarations: [
    AppComponent,
    ButtonComponentComponent,
    NavBarComponent,
    UserInfoComponent,
    CardComponent,
    CertificatesComponent,
    CertificateDetailsComponent,
    NewCertificateComponent,
    IsValidUploadComponent,
    RequestsComponent,
    RequestComponent,
    MyRequestsComponent,
    IndexPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NoopAnimationsModule,
    MatGridListModule,
    MatLegacyCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    ReactiveFormsModule,
    MatIconModule,
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
