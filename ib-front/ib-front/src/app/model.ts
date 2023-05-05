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

export class DeclineRequest {
  reason ?: string;
}

