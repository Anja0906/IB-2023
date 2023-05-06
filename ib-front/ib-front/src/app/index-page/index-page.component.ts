import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-index-page',
  templateUrl: './index-page.component.html',
  styleUrls: ['./index-page.component.css']
})
export class IndexPageComponent {
  id: Number = 0;
  issuerSN: string = "";
  type: Number = 0;
  durationInMonths: Number = 0;
  captcha: string = "";
  error = console.error;

  constructor(private http: HttpClient) {}

  send() {
    if (this.captcha.length) {
      this.http.post(
        'http://localhost:8080/api/certificate/new',
        { 
          id: this.id,
          issuerSN: this.issuerSN,
          type: this.type,
          durationInMonths: this.durationInMonths,
          captcha: this.captcha
        }
      ).subscribe(console.log);
    }
  }
}
