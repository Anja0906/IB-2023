import {UserSpring} from "./auth-service/authentication.service";

export class Certificate {
  id ?: number;
  issuedAt ?: Date;
  user ?: UserSpring;
  type ?: string;
  serialNumber ?: string;
  validTo ?: Date;
}

export class CertificateRequest{
  id ?: number;
  issuerSN ?: string;
  type ?: number;
  durationInMonths ?: number;
}

export class CertificateResponse{
  id ?: number;
  issuerSN ?: string;
  type ?: string;
  durationInMonths ?: number;
  certificateState ?: string;
}

export class DeclineRequest {
  constructor(reason: string) {
    this.reason = reason;
  }
  reason ?: string;
}
