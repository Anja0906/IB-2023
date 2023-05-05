import { Component } from '@angular/core';
import {Certificate} from "../model";
import {CertificateService} from "../certificate-cervice/certificate.service";
import {AuthenticationService, User, UserService, UserSpring} from "../auth-service/authentication.service";

@Component({
  selector: 'app-certificate-details',
  templateUrl: './certificate-details.component.html',
  styleUrls: ['./certificate-details.component.css']
})
export class CertificateDetailsComponent {
  hasAuthorities: boolean = false;
  certificate!: Certificate;
  issuedAt: string = "";
  validTo: string = "";
  user !: User | null;
  valid!: boolean;
  clickedIsValid : boolean = false;

  constructor(private certificateService: CertificateService, private userService : UserService) {
  }

  ngOnInit(): void {
    this.certificate = this.certificateService.getCertificate();
    console.log(this.certificate);
    // @ts-ignore
    this.issuedAt = this.certificate.issuedAt[2] + "." + (this.certificate.issuedAt[1]) + "." + this.certificate.issuedAt[0] + ".";
    // @ts-ignore
    this.validTo = this.certificate.validTo[2] + "." + (this.certificate.validTo[1] - 1) + "." + this.certificate.validTo[0] + ".";
    this.user = this.userService.user.getValue();
    this.hasAuthorities = this.user?.spring.email === this.certificate.user?.email;
  }

  download() {
    this.certificateService.downloadCertificate(this.certificate.id);
  }

  isValid() {
    this.clickedIsValid = true;
    this.certificateService.checkIfIsValid(this.certificate.id).subscribe(valid => {
      this.valid = valid;
    });
  }

  withdraw() {
    if (this.hasAuthorities && !this.userService.user.getValue()?.spring.isAdministrator){
      this.certificateService.withdraw(this.certificate.id).subscribe(
        response => {
          console.log(response);
        }
      );
    }
    else if (this.userService.user.getValue()?.spring.isAdministrator){
      this.certificateService.withdraw(this.certificate.id).subscribe(
        response => {
          console.log(response);
        }
      );
    }
  }
}

