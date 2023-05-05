import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Certificate, CertificateRequest} from "../model";
import { saveAs } from "file-saver";

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

  downloadCertificate(id: number | undefined){
    this.http.get(serverUrl+ 'api/certificate/download/' + id, { responseType: 'blob' })
      .subscribe((result) => {
        saveAs(result, id+'.crt');
      });

  }
  checkIfIsValid(id: number | undefined): Observable<boolean>{
    return this.http.get<boolean>(serverUrl + 'api/certificate/valid/' + id);
  }

  validateCertificate(file: File) : Observable<boolean>{
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(serverUrl + 'api/certificate/validUploaded', formData);
  }

  newCertificate(certReq: { issuerSN: string; durationInMonths: number; id: number; type: number }) : Observable<CertificateRequest>{
    return this.http.post<CertificateRequest>(serverUrl + 'api/certificate/new', certReq);
  }

  withdraw(id: number | undefined): Observable<void>{
    return this.http.put<void>(serverUrl + 'api/certificate/withdraw/' + id, null);
  }

}
