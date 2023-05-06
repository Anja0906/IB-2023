import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CertificatesComponent} from "./certificates/certificates.component";
import {CertificateDetailsComponent} from "./certificate-details/certificate-details.component";
import {NewCertificateComponent} from "./new-certificate/new-certificate.component";
import {IsValidUploadComponent} from "./is-valid-upload/is-valid-upload.component";
import {RequestsComponent} from "./requests/requests.component";
import {RequestComponent} from "./request/request.component";
import {MyRequestsComponent} from "./my-requests/my-requests.component";
import { IndexPageComponent } from './index-page/index-page.component';

const routes: Routes = [
  { path:'certificates',component: CertificatesComponent},
  { path: 'certificate', component: CertificateDetailsComponent },
  { path: 'request', component: RequestComponent },
  { path: 'newCertificate', component: NewCertificateComponent },
  { path: 'checkUpload', component: IsValidUploadComponent },
  { path: 'requests', component: RequestsComponent },
  { path: 'my-requests', component: MyRequestsComponent },
  { path: 'request', component: RequestComponent },
  { path: '', component: IndexPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
