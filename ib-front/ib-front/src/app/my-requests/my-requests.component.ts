import { Component } from '@angular/core';
import {CertificateRequest, CertificateResponse} from "../model";
import {RequestService} from "../request-service/request.service";
import {UserService} from "../auth-service/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-my-requests',
  templateUrl: './my-requests.component.html',
  styleUrls: ['./my-requests.component.css']
})
export class MyRequestsComponent {
  requests ?: CertificateResponse[] ;

  constructor(private requestService : RequestService, private userService: UserService, private router:Router) {
    this.getRequests();
  }

  private getRequests() {
    this.requestService.getAllForMe()
      .subscribe(data => {
          this.requests = data;
          console.log(data);
        }
        , error => {
          console.log(error.error.message);
        }
      );
  }

  getRequest(request: CertificateResponse){
    this.requestService.setRequest(request);
    this.router.navigate(["request"]);
  }

}
