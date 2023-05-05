import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CertificatesComponent} from "./certificates/certificates.component";
import {CertificateDetailsComponent} from "./certificate-details/certificate-details.component";
import {NewCertificateComponent} from "./new-certificate/new-certificate.component";
import {IsValidUploadComponent} from "./is-valid-upload/is-valid-upload.component";

const routes: Routes = [
  { path:'certificates',component: CertificatesComponent},
  { path: 'certificate', component: CertificateDetailsComponent },
  { path: 'newCertificate', component: NewCertificateComponent },
  { path: 'checkUpload', component: IsValidUploadComponent }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
