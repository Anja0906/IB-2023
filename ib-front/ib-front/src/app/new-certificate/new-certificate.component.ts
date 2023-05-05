import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CertificateService} from "../certificate-cervice/certificate.service";
import {Certificate, CertificateRequest} from "../model";

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
  duration: number = 1;

  constructor(private certificateService: CertificateService) { }

  ngOnInit(): void {
    this.certificateService.getAll().subscribe(result => {
      this.items = result;
    });
  }

  onSubmit() {
    let request = {
      "id" : 1,
      "type": this.selectedOption,
      "durationInMonths": this.duration,
      "issuerSN": this.selectedItem,
    };
    console.log(request);
    this.certificateService.newCertificate(request).subscribe(
      response => {
        this.responseDTO = response;
        alert("Successful request for certificate!")
      }
    );
  }
}
