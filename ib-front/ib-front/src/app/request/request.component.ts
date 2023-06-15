import { Component } from '@angular/core';
import {Certificate, CertificateRequest, CertificateResponse, DeclineRequest} from "../model";
import {User, UserService} from "../auth-service/authentication.service";
import {CertificateService} from "../certificate-cervice/certificate.service";
import {RequestService} from "../request-service/request.service";
import {Router} from "@angular/router";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingComponentComponent} from "../loading-component/loading-component.component";

@Component({
  selector: 'app-request',
  templateUrl: './request.component.html',
  styleUrls: ['./request.component.css']
})
export class RequestComponent {
  isPending: boolean = false;
  certificateRequest!: CertificateResponse;
  issuedAt: string = "";
  clickedDecline : boolean = false;
  reason: string = "";

  constructor(private requestService: RequestService, private userService : UserService, private router:Router, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.certificateRequest = this.requestService.getRequest();
    console.log(this.certificateRequest);
    this.isPending = this.certificateRequest.certificateState==="PENDING";
  }

  decline() {
    this.clickedDecline = true;
  }

  accept() {
    this.clickedDecline = false;
    let dialogRef: MatDialogRef<LoadingComponentComponent> = this.dialog.open(LoadingComponentComponent);
    this.requestService.accept(this.certificateRequest.id).subscribe(
      result => {
        dialogRef.close();
        this.router.navigate(["my-requests"]);
      },
      error => {
        dialogRef.close();
      });
  }

  confirmDeclining() {
    let reason = new DeclineRequest(this.reason)
    this.requestService.decline(this.certificateRequest.id, reason).subscribe(result => {
      this.router.navigate(["my-requests"]);
    });
  }
}
