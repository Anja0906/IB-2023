package com.ib.ib.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
public class CertificateRequest {
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

    @Column
    private CertificateType certificateType;

    @Column
    private CertificateState status;

    private String reason;


    public CertificateRequest() {}
    public CertificateRequest(Certificate issuer, User issuedTo, CertificateType certificateType, CertificateState status, String reason) {
        this.issuer          = issuer;
        this.issuedTo        = issuedTo;
        this.certificateType = certificateType;
        this.status          = status;
        this.reason          = reason;
    }

    public CertificateState getStatus() {return status;}
    public void setStatus(CertificateState status) {this.status = status;}

    public String getReason() {return reason;}
    public void setReason(String reason) {this.reason = reason;}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Certificate getIssuer() {return issuer;}
    public void setIssuer(Certificate issuer) {this.issuer = issuer;}

    public User getIssuedTo() {return issuedTo;}
    public void setIssuedTo(User issuedTo) {this.issuedTo = issuedTo;}

    public CertificateType getCertificateType() {return certificateType;}
    public void setCertificateType(CertificateType certificateType) {this.certificateType = certificateType;}

    @Override
    public String toString() {
        return "CertificateRequest{" +
                "id=" + id +
                ", issuer=" + issuer +
                ", issuedTo=" + issuedTo +
                ", certificateType=" + certificateType +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
