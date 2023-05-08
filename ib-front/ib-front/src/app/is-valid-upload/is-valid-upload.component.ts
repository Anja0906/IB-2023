import { Component } from '@angular/core';
import {CertificateService} from "../certificate-cervice/certificate.service";

@Component({
  selector: 'app-is-valid-upload',
  templateUrl: './is-valid-upload.component.html',
  styleUrls: ['./is-valid-upload.component.css']
})
export class IsValidUploadComponent {

  file!: File;
  validUpload!: boolean;
  isUploaded!: boolean;

  constructor(private certificateService: CertificateService) {
  }

  onFileSelected(event:any): void {
    this.file = event.target.files[0];
    this.isUploaded = true;
  }

  onDrop(event:any): void {
    event.preventDefault();
    event.stopPropagation();
    this.file = event.dataTransfer.files[0];
    this.isUploaded = true;
  }

  onDragOver(event:any): void {
    event.preventDefault();
    event.stopPropagation();
  }

  check() {
    if (this.file.type !== 'application/x-x509-ca-cert') {
      console.error('Invalid file type');
    }
    else {
      if (this.file.size/1048576>1){
        alert("Uploaded file is too big! You can not upload file longer than 1Mb")
      }
      else{
        this.certificateService.validateCertificate(this.file).subscribe(
          response => {
            this.validUpload = response;
          }
        );
      }
    }
  }
}
