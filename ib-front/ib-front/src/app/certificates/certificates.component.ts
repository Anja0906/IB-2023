import { Component } from '@angular/core';
import {CertificateService} from "../certificate-cervice/certificate.service";
import {Certificate} from "../model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.css']
})
export class CertificatesComponent {
  certificates ?: Certificate[] ;

  constructor(private certificateService : CertificateService, private router:Router) {
    this.getCertificates();
  }

  private getCertificates() {
    this.certificateService.getAll()
      .subscribe(data => {
          this.certificates = data;
          console.log(data);
        }
        , error => {
          console.log(error.error.message);
        }
      );
  }

  goToCertificateDetails(certificate: Certificate) {
    this.certificateService.setCertificate(certificate);
    this.router.navigate(["certificate"]);
  }

}
