import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CertificatesComponent} from "./certificates/certificates.component";
import {CertificateDetailsComponent} from "./certificate-details/certificate-details.component";

const routes: Routes = [
  { path:'certificates',component: CertificatesComponent},
  { path: 'certificate', component: CertificateDetailsComponent }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
