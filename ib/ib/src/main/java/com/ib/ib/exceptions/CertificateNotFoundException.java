package com.ib.ib.exceptions;

public class CertificateNotFoundException extends RuntimeException {
    public CertificateNotFoundException(String message) {
        super(message);
    }
}
