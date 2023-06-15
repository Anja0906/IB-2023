import { Component } from '@angular/core';
import {CertificateService} from "../certificate-cervice/certificate.service";
import {Certificate, CertificateRequest} from "../model";
import {UserService} from "../auth-service/authentication.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingComponentComponent} from "../loading-component/loading-component.component";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-certificate',
  templateUrl: './new-certificate.component.html',
  styleUrls: ['./new-certificate.component.css']
})
export class NewCertificateComponent {
  items !: Certificate[];
  options = ['ROOT', 'INTERMEDIATE', 'END'];
  selectedOption = 0;
  responseDTO!:CertificateRequest;
  selectedItem: string = "";
  duration!: number;
  isAdmin : boolean | undefined = false;
  captcha: string = "";

  constructor(private certificateService: CertificateService, private userService: UserService, public dialog: MatDialog, private router:Router) { }

  ngOnInit(): void {
    this.isAdmin = this.userService.user.getValue()?.spring.isAdministrator;
    if (!this.isAdmin){
      this.options = ['', 'INTERMEDIATE', 'END'];
    }
    this.certificateService.getValidIssuers().subscribe(result => {
      this.items = result;
    });
  }

  onSubmit() {
    let request = {
      "id" : 1,
      "type": this.selectedOption,
      "durationInMonths": this.duration,
      "issuerSN": this.selectedItem,
      "captcha": this.captcha
    };
    console.log(request);
    if (this.validateDuration(this.selectedOption, this.duration) && this.validateReCaptcha()) {
      let dialogRef: MatDialogRef<LoadingComponentComponent> = this.dialog.open(LoadingComponentComponent);
      this.certificateService.newCertificate(request).subscribe(
        response => {
          this.responseDTO = response;
          if (response!=null){
            dialogRef.close();
            if (this.isAdmin){
              this.router.navigate(["certificates"]);
            }
            else{
              this.router.navigate(["requests"]);
            }
          }
        },
        error => {
          dialogRef.close();
        }
      );
    }
  }

  validateDuration(type:number, duration:number): boolean{
    if (type===0 && duration < 6){
      alert("Certificate of type ROOT cannot last les than 6 months!");
      return false;
    }
    else if (type===1 && duration > 6){
      alert("Certificate of type INTERMEDIATE cannot last more than 6 months!");
      return false;
    }
    else if (type===2 && duration > 3){
      alert("Certificate of type END cannot last more than 3 months!");
      return false;
    }
    return true;
  }

  validateIssuer(type:number, issuerSN:string): boolean{
    let issuer;
    for (let item of this.items) {
      if (item.serialNumber === issuerSN){
        issuer = item;
      }
    }
    if (issuer!== undefined && issuer.type==="ROOT" && type !== 0){
      return true;
    }
    else{
      alert("Request for ROOT certificate can not contain Issuer!")
    }
    if (issuer!== undefined && issuer.type==="INTERMEDIATE" && type === 0){
      alert("Issuer of type INTERMEDIATE can not sign ROOT type");
      return false;
    }
    return true;
  }

  validateReCaptcha(): boolean{
    if (this.captcha) return true;
    alert("You must pass ReCaptcha before using this feature!")
    return false;
  }
}
