import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Certificate} from "../model";

const serverUrl = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {
  public certificate!: Certificate;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Certificate[]> {
    return this.http.get<Certificate[]>(serverUrl + 'api/certificate');
  }

  getCertificate() : Certificate {
    return this.certificate
  }

  setCertificate(certificate: Certificate) : void{
    this.certificate = certificate;
  }
}
