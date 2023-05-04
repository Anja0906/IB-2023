import {UserSpring} from "./auth-service/authentication.service";

export class Certificate {
  id ?: number;
  issuedAt ?: Date;
  user ?: UserSpring;
  certificateType ?: number;
  serialNumber ?: string;
  validTo ?: Date;
}

export class CertificateRequest{
  id ?: number;
  issuerSN ?: string;
  certificateType ?: number;
  durationInMonths ?: number;
}

export class DeclineRequest {
  reason ?: string;
}

