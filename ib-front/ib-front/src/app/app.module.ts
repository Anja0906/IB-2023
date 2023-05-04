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
import {FormsModule} from "@angular/forms";
import { CertificatesComponent } from './certificates/certificates.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import {MatGridListModule} from "@angular/material/grid-list";
import {MatLegacyCardModule} from "@angular/material/legacy-card";
import {MatButtonModule} from "@angular/material/button";
import { CertificateDetailsComponent } from './certificate-details/certificate-details.component';

@NgModule({
  declarations: [
    AppComponent,
    ButtonComponentComponent,
    NavBarComponent,
    UserInfoComponent,
    CardComponent,
    CertificatesComponent,
    CertificateDetailsComponent
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
