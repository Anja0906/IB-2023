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
    IsValidUploadComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        NoopAnimationsModule,
        MatGridListModule,
        MatLegacyCardModule,
        MatButtonModule,
        MatFormFieldModule,
        MatInputModule,
        MatRadioModule,
        ReactiveFormsModule,
        MatIconModule,
        // Import the module into the application, with configuration
        // AuthModule.forRoot({
        //   domain: 'dev-uox28mbzk3p270l1.us.auth0.com',
        //   clientId: 'okCV1iX8I6mb9BqQZ6YnNF1hDcx3fv5n',
        //   authorizationParams: {
        //     redirect_uri: window.location.origin,
        //     audience: 'localhost',
        //     scope: 'email profile openid'
        //   }
        // }),
    ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }, AuthenticationService, UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }
