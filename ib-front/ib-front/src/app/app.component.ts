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

  ngOnInit() {
    // Self-XSS warning
    console.log("%cSTOP!", "font-size: 50px; font-weight: bold; color: red;");
    console.log("%cThis is a browser feature intended for developers. If someone told you to copy-paste something here to enable a feature or hack someone's account, it is a scam and will give them access to your account.", "font-size: 16px; color: red;");
  }
}
