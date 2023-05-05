import { Injectable } from '@angular/core';
import {Certificate, CertificateRequest, CertificateResponse, DeclineRequest} from "../model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const serverUrl = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root'
})
export class RequestService {
  public certificateRequest!: CertificateResponse;

  constructor(private http: HttpClient) { }

  getAll(): Observable<CertificateResponse[]> {
    return this.http.get<CertificateResponse[]>(serverUrl + 'api/certificate/requests');
  }

  getAllAdmin(): Observable<CertificateResponse[]> {
    return this.http.get<CertificateResponse[]>(serverUrl + 'api/certificate/requests/all');
  }

  getAllForMe(): Observable<CertificateResponse[]> {
    return this.http.get<CertificateResponse[]>(serverUrl + 'api/certificate/requestsIssuer');
  }

  getRequest() : CertificateResponse {
    return this.certificateRequest
  }

  setRequest(certificateRequest: CertificateResponse) : void{
    this.certificateRequest = certificateRequest;
  }

  accept(id: number | undefined){
    return this.http.put(serverUrl + 'api/certificate/accept/' + id, {});
  }

  decline(id: number | undefined, reason: DeclineRequest){
    return this.http.put(serverUrl + 'api/certificate/decline/' + id, reason);
  }


}
