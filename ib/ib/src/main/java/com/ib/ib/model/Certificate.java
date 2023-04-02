package com.ib.ib.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "issuer_id")
    private Certificate issuer;

    @ManyToOne
    @JoinColumn(name = "issued_to_id")
    private User issuedTo;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private String publicKey;

    private String digitalSignature;

    private boolean isValid;

    private CertificateType certificateType;

    public User getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(User issuedTo) {
        this.issuedTo = issuedTo;
    }


    public Certificate getIssuer() {
        return issuer;
    }

    public void setIssuer(Certificate issuer) {
        this.issuer = issuer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public Certificate() {
    }

    public Certificate(Integer id, Certificate issuer, User issuedTo, LocalDateTime validFrom, LocalDateTime validTo, String publicKey, String digitalSignature, boolean isValid, CertificateType certificateType) {
        this.id = id;
        this.issuer = issuer;
        this.issuedTo = issuedTo;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.publicKey = publicKey;
        this.digitalSignature = digitalSignature;
        this.isValid = isValid;
        this.certificateType = certificateType;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", issuer=" + issuer +
                ", issuedTo=" + issuedTo +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", publicKey='" + publicKey + '\'' +
                ", digitalSignature='" + digitalSignature + '\'' +
                ", isValid=" + isValid +
                ", certificateType=" + certificateType +
                '}';
    }
}
