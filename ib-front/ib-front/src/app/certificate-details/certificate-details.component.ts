import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Certificate} from "../model";
import {CertificateService} from "../certificate-cervice/certificate.service";

@Component({
  selector: 'app-certificate-details',
  templateUrl: './certificate-details.component.html',
  styleUrls: ['./certificate-details.component.css']
})
export class CertificateDetailsComponent {
  certificate!:Certificate;

  constructor(private certificateService:CertificateService) { }

  ngOnInit(): void {
    this.certificate = this.certificateService.getCertificate();
    console.log(this.certificate);
  }
}
