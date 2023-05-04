import { Component } from '@angular/core';
import {Certificate} from "./model";
import {CertificateService} from "./certificate-cervice/certificate.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'ib-front';
}
