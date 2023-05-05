import { Component } from '@angular/core';
import {Certificate, CertificateRequest, CertificateResponse} from "../model";
import {CertificateService} from "../certificate-cervice/certificate.service";
import {Router} from "@angular/router";
import {RequestService} from "../request-service/request.service";
import {User, UserService} from "../auth-service/authentication.service";

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.css']
})
export class RequestsComponent {
  requests ?: CertificateResponse[] ;
  isAdmin:boolean | undefined = false;

  constructor(private requestService : RequestService, private userService: UserService) {
    this.isAdmin = userService.user.getValue()?.spring.isAdministrator;
    if (this.isAdmin){
      this.getRequestsAdmin();
    }
    else{
      this.getRequests();
    }
  }

  private getRequests() {
    this.requestService.getAll()
      .subscribe(data => {
          this.requests = data;
          console.log(data);
        }
        , error => {
          console.log(error.error.message);
        }
      );
  }

  private getRequestsAdmin() {
    this.requestService.getAllAdmin()
      .subscribe(data => {
          this.requests = data;
          console.log(data);
        }
        , error => {
          console.log(error.error.message);
        }
      );
  }

}
